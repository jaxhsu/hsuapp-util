package org.hsu.hsuapp.util;

import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.beanutils.ConstructorUtils;

public class JaxbUtil {

	public Object xmlStr2Bean(Class<?> beanClaz, String xmlString) {

		JAXBContext jaxbContext;
		Object obj = null;

		try {
			obj = ConstructorUtils.invokeConstructor(beanClaz, null);
			
			jaxbContext = JAXBContext.newInstance(obj.getClass());
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			
			StringReader reader = new StringReader(xmlString);
			obj = unmarshaller.unmarshal(reader);

		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}

		return obj;
	}

	public String bean2XmlStr(Class<?> beanClaz, Object bean, String charset) {

		JAXBContext context;
		StringWriter writer = new StringWriter();

		try {
			context = JAXBContext.newInstance(beanClaz);
			Marshaller mar = context.createMarshaller();
			mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			mar.setProperty(Marshaller.JAXB_ENCODING, charset);

			mar.marshal(bean, writer);

		} catch (JAXBException e) {
			e.printStackTrace();
		}

		return writer.toString();
	}

}
