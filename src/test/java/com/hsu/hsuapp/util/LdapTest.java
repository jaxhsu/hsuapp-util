package com.hsu.hsuapp.util;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import org.junit.Before;
import org.junit.Test;

public class LdapTest {

	@Before
	public void init() {
		
	}	
	
	@Test
	public void test() {
		boolean status = false;
		String ldap_url = "LDAP://192.168.99.100:32769";
		String username = "";
		String password = "theduke";
		String sLdapdc = "";
		
		
        Hashtable<String, String> env = new Hashtable<String,String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, ldap_url);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, "uid=jduke,ou=Users,dc=jboss,dc=org");
        env.put(Context.SECURITY_CREDENTIALS, password);

        LdapContext ctx = null;
        try {
            ctx = new InitialLdapContext(env, null);
        } catch (javax.naming.AuthenticationException e) {
            //this.setMessage("登入失敗- [員工編號] 或 [AD密碼] 輸入錯誤 !");
        	System.out.println(username + ":登入失敗- [員工編號] 或 [AD密碼] 輸入錯誤 ! => " + e.getMessage());
        	status = false;
        } catch (javax.naming.CommunicationException e) {
            //this.setMessage("登入失敗- 找不到認證主機 !");
        	System.out.println(username + ":登入失敗- 找入到認證主機 ! => " + e.getMessage());
        	status = false;
        } catch (Exception e) {         
            //this.setMessage("登入失敗- 發生未知的錯誤，請洽系統管理員 !");
        	System.out.println(username + ":登入失敗- 發生未知的錯誤，請洽系統管理員 ! => " + e.getMessage());
        	status = false;
        } finally {
            if (ctx != null) {
                try {
                    ctx.close();
                } catch (NamingException e) {
                    System.out.println(username + ":ctx.close()發生錯誤 ! => " + e.getMessage());
                } 
            }
        }   
        status = true;
	}
	
}
