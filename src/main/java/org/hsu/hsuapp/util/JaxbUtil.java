package org.hsu.hsuapp.util;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

public class JaxbUtil {

	private static Logger logger = Logger.getLogger(JaxbUtil.class);
	private static JAXBContext jaxbContext;

	// xml轉java物件
	@SuppressWarnings("unchecked")
	public static <T> T xmlToBean(String xml, Class<T> c) {
		T t = null;
		try {
			jaxbContext = JAXBContext.newInstance(c);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			t = (T) unmarshaller.unmarshal(new StringReader(xml));
		} catch (JAXBException e) {
			e.printStackTrace();
			logger.info(e.getMessage());
		}
		return t;
	}

	// java物件轉xml
	public static String beanToXml(Object obj) {
		StringWriter writer = null;
		try {
			jaxbContext = JAXBContext.newInstance(obj.getClass());
			Marshaller marshaller = jaxbContext.createMarshaller();
			// Marshaller.JAXB_FRAGMENT:是否省略xml頭資訊,true省略，false不省略
			marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
			// Marshaller.JAXB_FORMATTED_OUTPUT:決定是否在轉換成xml時同時進行格式化（即按標籤自動換行，否則即是一行的xml）
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			// Marshaller.JAXB_ENCODING:xml的編碼方式
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			writer = new StringWriter();
			marshaller.marshal(obj, writer);
		} catch (JAXBException e) {
			e.printStackTrace();
			logger.info(e.getMessage());
		}
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + writer.toString();
	}

}
