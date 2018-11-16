package com.hsu.hsuapp.util;

import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.hsu.hsuapp.util.JaxbUtil;
import org.junit.Before;
import org.junit.Test;

import com.hsu.hsuapp.pojo.EleClassA;
import com.hsu.hsuapp.pojo.EleClassB;
import com.hsu.hsuapp.pojo.Employee;
import com.hsu.hsuapp.pojo.Root2Class;
import com.hsu.hsuapp.pojo.RootClass;

public class JaxbUtilTest {

	JaxbUtil jaxbUtil;
	
	@Before
	public void init() {
		jaxbUtil = new JaxbUtil();
	}

	// @Test
	public void pojoToxmlStr() {
		RootClass rc = new RootClass();
		EleClassA a = new EleClassA();
		EleClassB b = new EleClassB();

		a.setAttrC("attrc");
		a.setEleA("eleA");
		a.setEleB("eleB");

		b.setAttrPassword("attrPassword");
		b.setAttrUserName("attrUsrName");
		b.setEleCode("eleCode");

		rc.setA(a);
		rc.setB(b);
		rc.setRoot("root");
		rc.setRootA("rootA");

		JAXBContext context;
		try {
			// context = JAXBContext.newInstance(RootClass.class);
			// Marshaller mar = context.createMarshaller();
			// mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			// mar.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			//
			// StringWriter writer = new StringWriter();
			//
			// mar.marshal(rc, writer);
			//
			// System.out.println(writer.toString());

			String xmlStr = jaxbUtil.bean2XmlStr(RootClass.class, rc, "UTF-8");
			System.out.println(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void pojoListToxmlStr() {

		EleClassA a = new EleClassA();
		a.setAttrC("attrc");
		a.setEleA("eleA");
		a.setEleB("eleB");

		EleClassA a2 = new EleClassA();
		a2.setAttrC("attrc2");
		a2.setEleA("eleA2");
		a2.setEleB("eleB2");

		List<EleClassA> list = new LinkedList<EleClassA>();
		list.add(a);
		list.add(a2);

		Root2Class roots = new Root2Class();
		roots.setList(list);

		JAXBContext context;
		try {
			context = JAXBContext.newInstance(Root2Class.class);
			Marshaller mar = context.createMarshaller();
			mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			mar.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

			StringWriter writer = new StringWriter();

			mar.marshal(roots, writer);

			System.out.println(writer.toString());
		} catch (JAXBException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void xmlStrToPojo() {
		String xmlString = "<employee>" +
	            "    <department>" +
	            "        <id>101</id>" +
	            "        <name>IT</name>" +
	            "    </department>" +
	            "    <firstName>Lokesh</firstName>" +
	            "    <id>1</id>" +
	            "    <lastName>Gupta</lastName>" +
	            "</employee>";

		Employee employee = (Employee) jaxbUtil.xmlStr2Bean(Employee.class, xmlString);
		System.out.println(employee);

	}

}
