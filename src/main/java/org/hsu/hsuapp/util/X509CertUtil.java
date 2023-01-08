package org.hsu.hsuapp.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;

import sun.security.x509.AlgorithmId;
import sun.security.tools.keytool.CertAndKeyGen;
import sun.security.x509.CertificateAlgorithmId;
import sun.security.x509.CertificateIssuerName;
import sun.security.x509.CertificateSerialNumber;
import sun.security.x509.CertificateSubjectName;
import sun.security.x509.CertificateValidity;
import sun.security.x509.CertificateVersion;
import sun.security.x509.CertificateX509Key;
import sun.security.x509.X500Name;
import sun.security.x509.X509CertImpl;
import sun.security.x509.X509CertInfo;

public class X509CertUtil {

	private SecureRandom secureRandom;

	public X509CertUtil() {

		try {

			secureRandom = SecureRandom.getInstance("SHA1PRNG", "SUN");

		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();

		} catch (NoSuchProviderException e) {

			e.printStackTrace();

		}

	}

	/**
	 * 頒佈證書
	 * 
	 * @param issue
	 * @param subject
	 * @param issueAlias
	 * @param issuePfxPath
	 * @param issuePassword
	 * @param issueCrtPath
	 * @param subjectAlias
	 * @param subjectPfxPath
	 * @param subjectPassword
	 * @param subjectCrtPath
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 * @throws InvalidKeyException
	 * @throws CertificateException
	 * @throws IOException
	 * @throws KeyStoreException
	 * @throws UnrecoverableKeyException
	 * @throws SignatureException
	 */
	public void createIssueCert(X500Name issue, X500Name subject, String issueAlias, String issuePfxPath,
			String issuePassword, String issueCrtPath, String subjectAlias, String subjectPfxPath,
			String subjectPassword, String subjectCrtPath)
			throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, CertificateException,
			IOException, KeyStoreException, UnrecoverableKeyException, SignatureException {

		CertAndKeyGen certAndKeyGen = new CertAndKeyGen("RSA", "MD5WithRSA", null);
		certAndKeyGen.setRandom(secureRandom);
		certAndKeyGen.generate(1024);

		String sigAlg = "MD5WithRSA";
		// 1年
		long validity = 3650 * 24L * 60L * 60L;
		Date firstDate = new Date();
		Date lastDate = new Date();
		lastDate.setTime(firstDate.getTime() + validity * 1000);
		CertificateValidity interval = new CertificateValidity(firstDate, lastDate);

		X509CertInfo info = new X509CertInfo();
		// Add all mandatory attributes
		// 版本號
		info.set(X509CertInfo.VERSION, new CertificateVersion(CertificateVersion.V3));
		// 序列號
		info.set(X509CertInfo.SERIAL_NUMBER,
				new CertificateSerialNumber(new java.util.Random().nextInt() & 0x7fffffff));
		// 簽章演算法
		AlgorithmId algID = AlgorithmId.get(sigAlg);
		info.set(X509CertInfo.ALGORITHM_ID, new CertificateAlgorithmId(algID));
		// 憑證有效期
		info.set(X509CertInfo.VALIDITY, interval);
		// 頒發者
		info.set(X509CertInfo.ISSUER, new CertificateIssuerName(issue));
		// 主題
		info.set(X509CertInfo.SUBJECT, new CertificateSubjectName(subject));
		// 主題公鑰資訊
		info.set(X509CertInfo.KEY, new CertificateX509Key(certAndKeyGen.getPublicKey()));
		
		PrivateKey privateKey = readPrivateKey(issueAlias, issuePfxPath, issuePassword);

		X509CertImpl cert = new X509CertImpl(info);

		cert.sign(privateKey, sigAlg);

		X509Certificate certificate = (X509Certificate) cert;

		X509Certificate issueCertificate = readX509Certificate(issueCrtPath);

		X509Certificate[] X509Certificates = new X509Certificate[] { certificate, issueCertificate };

		createKeyStore(subjectAlias, certAndKeyGen.getPrivateKey(), subjectPassword.toCharArray(), X509Certificates,
				subjectPfxPath);

		FileOutputStream fos = new FileOutputStream(new File(subjectCrtPath));

		fos.write(certificate.getEncoded());

		fos.close();

	}

	/**
	 * 創建根證書（證書有效期10年，私鑰保存密碼“123456”，公鑰算法“RSA”，簽名算法“MD5WithRSA”）
	 * 
	 * @param rootPfxPath Personal Information Exchange 路徑
	 * @param rootCrtPath 證書路徑
	 * @param issue       頒發者&接收頒發者
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 * @throws InvalidKeyException
	 * @throws IOException
	 * @throws CertificateException
	 * @throws SignatureException
	 * @throws KeyStoreException
	 */
	public void createRootCert(String issuePfxPath, String issueCrtPath, X500Name issue)
			throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, IOException,
			CertificateException, SignatureException, KeyStoreException {

		CertAndKeyGen rootCertAndKeyGen = new CertAndKeyGen("RSA", "MD5WithRSA", null);

		rootCertAndKeyGen.setRandom(secureRandom);

		rootCertAndKeyGen.generate(1024);

		X509Certificate rootCertificate = rootCertAndKeyGen.getSelfCertificate(issue, 3650 * 24L * 60L * 60L);

		X509Certificate[] X509Certificates = new X509Certificate[] { rootCertificate };

		String password = "123456";

		createKeyStore("RootCA", rootCertAndKeyGen.getPrivateKey(), password.toCharArray(), X509Certificates,
				issuePfxPath);

		FileOutputStream fos = new FileOutputStream(new File(issueCrtPath));

		fos.write(rootCertificate.getEncoded());

		fos.close();

	}

	/**
	 * 證書私鑰存儲設施
	 * 
	 * @param alias    KeyStore別名
	 * @param key      密鑰（這裏是私鑰）
	 * @param password 保存密碼
	 * @param chain    證書鏈
	 * @param filePath PFX文件路徑
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateException
	 * @throws IOException
	 */
	private void createKeyStore(String alias, Key key, char[] password, Certificate[] chain, String filePath)
			throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {

		KeyStore keyStore = KeyStore.getInstance("pkcs12");

		keyStore.load(null, password);

		keyStore.setKeyEntry(alias, key, password, chain);

		FileOutputStream fos = new FileOutputStream(filePath);

		keyStore.store(fos, password);

		fos.close();
	}

	/**
	 * 讀取PFX文件中的私鑰
	 * 
	 * @param alias    別名
	 * @param pfxPath  PFX文件路徑
	 * @param password 密碼
	 * @return
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateException
	 * @throws IOException
	 * @throws UnrecoverableKeyException
	 */
	public PrivateKey readPrivateKey(String alias, String pfxPath, String password) throws KeyStoreException,
			NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException {

		KeyStore keyStore = KeyStore.getInstance("pkcs12");

		FileInputStream fis = null;

		fis = new FileInputStream(pfxPath);

		keyStore.load(fis, password.toCharArray());

		fis.close();

		return (PrivateKey) keyStore.getKey(alias, password.toCharArray());
	}

	/**
	 * 讀取X.509證書
	 * 
	 * @param crtPath 證書路徑
	 * @return
	 * @throws CertificateException
	 * @throws IOException
	 */
	public X509Certificate readX509Certificate(String crtPath) throws CertificateException, IOException {

		InputStream inStream = null;

		inStream = new FileInputStream(crtPath);

		CertificateFactory cf = CertificateFactory.getInstance("X.509");

		X509Certificate cert = (X509Certificate) cf.generateCertificate(inStream);

		inStream.close();

		return cert;
	}

	/**
	 * 讀取公鑰證書中的公鑰（字符串形式）
	 * 
	 * @param crtPath
	 * @return
	 * @throws CertificateException
	 * @throws IOException
	 */
	public String readX509CertificatePublicKey(String crtPath) throws CertificateException, IOException {

		X509Certificate x509Certificate = readX509Certificate(crtPath);

		PublicKey publicKey = x509Certificate.getPublicKey();

		return publicKey.toString().replace(" ", "");

	}

	/**
	 * 讀取KeyStore裏面的私鑰（字符串形式）
	 * 
	 * @param alias
	 * @param pfxPath
	 * @param password
	 * @return
	 * @throws UnrecoverableKeyException
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateException
	 * @throws IOException
	 */
	public String readPrivateKeyStr(String alias, String pfxPath, String password) throws UnrecoverableKeyException,
			KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {

		PrivateKey privateKey = readPrivateKey(alias, pfxPath, password);

		return privateKey.toString().replace(" ", "");

	}

	/**
	 * 根據證書讀取 讀取模數N
	 * 
	 * @param crtPath
	 * @return
	 */
	public String getModulusByCrt(String crtPath) {
		String crt = "";
		try {
			crt = readX509CertificatePublicKey(crtPath);
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String modulus = crt.substring(crt.indexOf("modulus:") + "modulus:".length(), crt.indexOf("publicexponent:"));
		return modulus.trim().replace(" ", "");
	}

	/**
	 * 根據證書讀取公鑰e
	 * 
	 * @param crtPath
	 * @return
	 */
	public String getPubExponentByCrt(String crtPath) {

		String crt = "";
		try {
			crt = readX509CertificatePublicKey(crtPath);
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String pubExponent = crt.substring(crt.indexOf("publicexponent:") + "publicexponent:".length(), crt.length());
		return pubExponent.trim().replace(" ", "");

	}

	/**
	 * 根據KeyStore讀取模數N
	 * 
	 * @param alias
	 * @param pfxPath
	 * @param password
	 * @return
	 */
	public String getModulusByPfx(String alias, String pfxPath, String password) {

		String pfx = "";
		try {
			pfx = readPrivateKeyStr(alias, pfxPath, password);
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String modulus = pfx.substring(pfx.indexOf("modulus:") + "modulus:".length(), pfx.indexOf("publicexponent:"));

		return modulus.trim().replace(" ", "");

	}

	/**
	 * 根據KeyStore讀取公鑰e
	 * 
	 * @param alias
	 * @param pfxPath
	 * @param password
	 * @return
	 */
	public String getPubExponentByPfx(String alias, String pfxPath, String password) {

		String pfx = "";
		try {
			pfx = readPrivateKeyStr(alias, pfxPath, password);
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String modulus = pfx.substring(pfx.indexOf("publicexponent:") + "publicexponent:".length(),
				pfx.indexOf("privateexponent:"));

		return modulus.trim().replace(" ", "");

	}

	/**
	 * 根據KeyStore讀取私鑰d
	 * 
	 * @param alias
	 * @param pfxPath
	 * @param password
	 * @return
	 */
	public String getPriExponentByPfx(String alias, String pfxPath, String password) {

		String pfx = "";
		try {
			pfx = readPrivateKeyStr(alias, pfxPath, password);
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String modulus = pfx.substring(pfx.indexOf("privateexponent:") + "privateexponent:".length(),
				pfx.indexOf("primep:"));

		return modulus.trim().replace(" ", "");

	}

	/**
	 * 根據KeyStore讀取p
	 * 
	 * @param alias
	 * @param pfxPath
	 * @param password
	 * @return
	 */
	public String getpByPfx(String alias, String pfxPath, String password) {

		String pfx = "";
		try {
			pfx = readPrivateKeyStr(alias, pfxPath, password);
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String modulus = pfx.substring(pfx.indexOf("primep:") + "primep:".length(), pfx.indexOf("primeq:"));

		return modulus.trim().replace(" ", "");
	}

	/**
	 * 根據KeyStore讀取q
	 * 
	 * @param alias
	 * @param pfxPath
	 * @param password
	 * @return
	 */
	public String getqByPfx(String alias, String pfxPath, String password) {

		String pfx = "";
		try {
			pfx = readPrivateKeyStr(alias, pfxPath, password);
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String modulus = pfx.substring(pfx.indexOf("primeq:") + "primeq:".length(), pfx.indexOf("primeexponentp:"));

		return modulus.trim().replace(" ", "");

	}

	/**
	 * 根據KeyStore讀取dp
	 * 
	 * @param alias
	 * @param pfxPath
	 * @param password
	 * @return
	 */
	public String getdpByPfx(String alias, String pfxPath, String password) {

		String pfx = "";
		try {
			pfx = readPrivateKeyStr(alias, pfxPath, password);
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String modulus = pfx.substring(pfx.indexOf("primeexponentp:") + "primeexponentp:".length(),
				pfx.indexOf("primeexponentq:"));

		return modulus.trim().replace(" ", "");

	}

	/**
	 * 根據KeyStore讀取dq
	 * 
	 * @param alias
	 * @param pfxPath
	 * @param password
	 * @return
	 */
	public String getdqByPfx(String alias, String pfxPath, String password) {

		String pfx = "";
		try {
			pfx = readPrivateKeyStr(alias, pfxPath, password);
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String modulus = pfx.substring(pfx.indexOf("primeexponentq:") + "primeexponentq:".length(),
				pfx.indexOf("crtcoefficient:"));

		return modulus.trim().replace(" ", "");

	}

	/**
	 * 根據KeyStore讀取qInv
	 * 
	 * @param alias
	 * @param pfxPath
	 * @param password
	 * @return
	 */
	public String getqInvByPfx(String alias, String pfxPath, String password) {

		String pfx = "";
		try {
			pfx = readPrivateKeyStr(alias, pfxPath, password);
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String modulus = pfx.substring(pfx.indexOf("crtcoefficient:") + "crtcoefficient:".length(), pfx.length());

		return modulus.trim().replace(" ", "");

	}

	public static void main(String args[]) throws IOException {

		// CN commonName 一般名字
		// L localityName 地方名
		// ST stateOrProvinceName 州省名
		// O organizationName 組織名
		// OU organizationalUnitName 組織單位名
		// C countryName 國家
		// STREET streetAddress 街道地址
		// DC domainComponent 領域
		// UID user id 用戶ID
		X500Name issue = new X500Name("CN=RootCA,OU=ISI,O=BenZeph,L=CD,ST=SC,C=CN");

		X500Name subject = new X500Name("CN=subject,OU=ISI,O=BenZeph,L=CD,ST=SC,C=CN");

		String issuePfxPath = "D://tmp/cert/ROOTCA.pfx";
		String issueCrtPath = "D://tmp/cert/ROOTCA.crt";

		String subjectPfxPath = "D://tmp/cert/ISSUE.pfx";
		String subjectCrtPath = "D://tmp/cert/ISSUE.crt";

		String issueAlias = "RootCA";
		String subjectAlias = "subject";

		String issuePassword = "123456";
		String subjectPassword = "123456";

		X509CertUtil test = new X509CertUtil();

		// createRootCert
		try {
			test.createRootCert(issuePfxPath, issueCrtPath, issue);
		} catch (InvalidKeyException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchProviderException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (CertificateException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SignatureException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (KeyStoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// createIssueCert
//		try {
//			test.createIssueCert(issue, subject, issueAlias, issuePfxPath, issuePassword, issueCrtPath, subjectAlias,
//					subjectPfxPath, subjectPassword, subjectCrtPath);
//		} catch (InvalidKeyException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (UnrecoverableKeyException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (NoSuchAlgorithmException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (NoSuchProviderException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (CertificateException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (KeyStoreException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SignatureException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		// readX509CertificatePublicKey
		try {
			System.out.println(test.readX509CertificatePublicKey(issueCrtPath));
		} catch (CertificateException e) {
			e.printStackTrace();
		}
		System.out.println("");
		System.out.println(test.getModulusByCrt(issueCrtPath));
		System.out.println(test.getPubExponentByCrt(issueCrtPath));
		System.out.println("");
		try {
			System.out.println(test.readPrivateKeyStr(issueAlias, issuePfxPath, issuePassword));
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		}
		System.out.println("");
		System.out.println(test.getModulusByPfx(issueAlias, issuePfxPath, issuePassword));
		System.out.println(test.getPubExponentByPfx(issueAlias, issuePfxPath, issuePassword));
		System.out.println(test.getPriExponentByPfx(issueAlias, issuePfxPath, issuePassword));
		System.out.println(test.getpByPfx(issueAlias, issuePfxPath, issuePassword));
		System.out.println(test.getqByPfx(issueAlias, issuePfxPath, issuePassword));
		System.out.println(test.getdpByPfx(issueAlias, issuePfxPath, issuePassword));
		System.out.println(test.getdqByPfx(issueAlias, issuePfxPath, issuePassword));
		System.out.println(test.getqInvByPfx(issueAlias, issuePfxPath, issuePassword));
	}
}
