package com.hsu.hsuapp.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class JacksonUtilTest {

	@Before
	public void init() {

	}

	@Test
	public void xml2JsonStr() {
		String data = "<?xml version='1.0' encoding='UTF-8'?>" + "<student>" + "<age>11</age>" + "<id>12</id>"
				+ "<name>JavaInterviewPoint</name>" + "</student>";

		//data = "<XMQ003_RS xmlns=\"http://li.etax.nat.gov.tw/MOF/XMQ003Res_new_definition\" xmlns:xmq=\"http://li.etax.nat.gov.tw/MOF/XMQ003059_definition\" xmlns:xmq1=\"http://li.etax.nat.gov.tw/MOF/XMQ003_new_definition\"><XMQ003_RS_001>10000092_1071107_157661_2763</XMQ003_RS_001><XMQ003_RS_002>10000092_1071107_157663_2783</XMQ003_RS_002><XMQ003_RS_003>S</XMQ003_RS_003><XMQ003_RS_004>1</XMQ003_RS_004><XMQ003_RS_005>10</XMQ003_RS_005><XMQ003_RS_006>稅務後端資料查詢成功!!</XMQ003_RS_006><XMQ003_RSR><xmq:XMQ003059_RSD><xmq:XMQ003059_RSDR><xmq:XMQ003059_RSD_001>1</xmq:XMQ003059_RSD_001><xmq1:XMQ003_CMQ><xmq1:XMQ003_CMQ_001/><xmq1:XMQ003_CMQ_002>1024201020280000</xmq1:XMQ003_CMQ_002><xmq1:XMQ003_CMQ_003>可芮姿健康曲線有限公司</xmq1:XMQ003_CMQ_003><xmq1:XMQ003_CMQ_004>106</xmq1:XMQ003_CMQ_004><xmq1:XMQ003_CMQ_005/></xmq1:XMQ003_CMQ><xmq1:XMQ003_CM_001>1</xmq1:XMQ003_CM_001><xmq1:XMQ003059_RSDR_001>106</xmq1:XMQ003059_RSDR_001><xmq1:XMQ003059_RSDR_002>0</xmq1:XMQ003059_RSDR_002></xmq:XMQ003059_RSDR></xmq:XMQ003059_RSD></XMQ003_RSR></XMQ003_RS>";
		data = "<?xml version=\"1.0\" encoding=\"utf-8\"?><CPWSResponse><Code>0</Code><Message>Success</Message><Result><ExeStatus><CP_PublicServiceInfo><MessageInfo><TicketID>10000380_1011120_042686_2661</TicketID></MessageInfo><ServiceInfo><ServiceID>PUB0000629</ServiceID><ServiceName>SmeaAPEX</ServiceName><ServiceAction>Async</ServiceAction></ServiceInfo><ServiceStatus><Status>COMPLETED</Status><Description>Complete</Description></ServiceStatus></CP_PublicServiceInfo><CP_AP_Service><Header><ServiceInfo><ServiceID>GOV0000886</ServiceID><ServiceName /><ServiceAction /></ServiceInfo><ServiceStatus><Status>Complete</Status><Description>Complete</Description></ServiceStatus></Header><Body><XMQ003_RS xmlns:json='http://james.newtonking.com/projects/json' xmlns=\"http://li.etax.nat.gov.tw/MOF/XMQ003Res_new_definition\" xmlns:xmq=\"http://li.etax.nat.gov.tw/MOF/XMQ003020_definition\" xmlns:xmq1=\"http://li.etax.nat.gov.tw/MOF/XMQ003_new_definition\" xmlns:xmq2=\"http://li.etax.nat.gov.tw/MOF/XMQ003_2\" xmlns:xmq3=\"http://li.etax.nat.gov.tw/MOF/XMQ003_3\"><XMQ003_RS_001>10000380_1011120_042685_2651</XMQ003_RS_001><XMQ003_RS_002>10000380_1011120_042686_2661</XMQ003_RS_002><XMQ003_RS_003>S</XMQ003_RS_003><XMQ003_RS_004>4</XMQ003_RS_004><XMQ003_RS_005>10</XMQ003_RS_005><XMQ003_RS_006>稅務後端資料查詢成功!!</XMQ003_RS_006><XMQ003_RSR><xmq:XMQ003020_RSD><xmq:XMQ003020_RSD_001>4</xmq:XMQ003020_RSD_001><xmq2:XMQ003020_RSDR><XMQ003_CMQ><XMQ003_CMQ_001/><XMQ003_CMQ_002>0018280545042114</XMQ003_CMQ_002><XMQ003_CMQ_003>丙丙丙丙丙丙丙丙丙</XMQ003_CMQ_003><XMQ003_CMQ_004>099</XMQ003_CMQ_004><XMQ003_CMQ_005/></XMQ003_CMQ><xmq1:XMQ003_CM_001>1</xmq1:XMQ003_CM_001><xmq1:XMQ003020_RSDR001>09903</xmq1:XMQ003020_RSDR001><xmq1:XMQ003020_RSDR001>06719177</xmq1:XMQ003020_RSDR001><xmq1:XMQ003020_RSDR001>58812</xmq1:XMQ003020_RSDR001><xmq1:XMQ003020_RSDR001>1764</xmq1:XMQ003020_RSDR001><xmq1:XMQ003020_RSDR001>1011120</xmq1:XMQ003020_RSDR001></xmq2:XMQ003020_RSDR><xmq3:XMQ003020_RSDR><XMQ003_CMQ><XMQ003_CMQ_001/><XMQ003_CMQ_002>0018280545042114</XMQ003_CMQ_002><XMQ003_CMQ_003>丙丙丙丙丙丙丙丙丙</XMQ003_CMQ_003><XMQ003_CMQ_004>099</XMQ003_CMQ_004><XMQ003_CMQ_005/></XMQ003_CMQ><xmq1:XMQ003_CM_001>1</xmq1:XMQ003_CM_001><xmq1:XMQ003020_RSDR001>09906</xmq1:XMQ003020_RSDR001><xmq1:XMQ003020_RSDR001>06719177</xmq1:XMQ003020_RSDR001><xmq1:XMQ003020_RSDR001>58812</xmq1:XMQ003020_RSDR001><xmq1:XMQ003020_RSDR001>1764</xmq1:XMQ003020_RSDR001><xmq1:XMQ003020_RSDR001>1011120</xmq1:XMQ003020_RSDR001></xmq3:XMQ003020_RSDR></xmq:XMQ003020_RSD></XMQ003_RSR></XMQ003_RS></Body></CP_AP_Service></ExeStatus></Result></CPWSResponse>";
		
		try {
			// Create a new XmlMapper to read XML tags
			XmlMapper xmlMapper = new XmlMapper();

			// Reading the XML
			JsonNode jsonNode = xmlMapper.readTree(data);
			
			// 查詢<Body>..</Body>
			JsonNode bodyJsonNode = jsonNode.findValue("Body");
			
			Map<String, String> map = new HashMap<String, String>();
			addKeys("", bodyJsonNode, map, new ArrayList());
			
//			Iterator<String> it = map.keySet().iterator();
//			while (it.hasNext()) {
//				String key = it.next();
//				String value = map.get(key);
//				System.out.println("key: " + key + ", vaule: " + value);
//			}
			
			// 增加節點 test
//			((ObjectNode) jsonNode).put("value", "NO");
//			((ObjectNode) jsonNode).putArray("arrayName").add("CC");

			// Create a new ObjectMapper
			ObjectMapper objectMapper = new ObjectMapper();

			// Get JSON as a string
			String value = objectMapper.writeValueAsString(bodyJsonNode);

			System.out.println("*** Converting XML to JSON ***");
			System.out.println(value);

		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 列出全部節點資訊
	 * 
	 * @param currentPath
	 * @param jsonNode
	 * @param map
	 * @param suffix
	 */
	private void addKeys(String currentPath, JsonNode jsonNode, Map<String, String> map, List<Integer> suffix) {

	    if (jsonNode.isObject()) {
	        ObjectNode objectNode = (ObjectNode) jsonNode;
	        Iterator<Map.Entry<String, JsonNode>> iter = objectNode.fields();

	        String pathPrefix = currentPath.isEmpty() ? "" : currentPath + "-";

	        while (iter.hasNext()) {
	            Map.Entry<String, JsonNode> entry = iter.next();
	            addKeys(pathPrefix + entry.getKey(), entry.getValue(), map, suffix);
	        }

	    } else if (jsonNode.isArray()) {

	        ArrayNode arrayNode = (ArrayNode) jsonNode;
	        
	        for (int i = 0; i < arrayNode.size(); i++) {
	            suffix.add(i + 1);
	            addKeys(currentPath, arrayNode.get(i), map, suffix);

	            if (i + 1 <arrayNode.size()){
	                suffix.remove(arrayNode.size() - 1);
	            }
	        }

	    } else if (jsonNode.isValueNode()) {

	        if (currentPath.contains("-")) {
	            for (int i = 0; i < suffix.size(); i++) {
	                currentPath += "-" + suffix.get(i);
	            }
	            suffix = new ArrayList();
	        }
	        ValueNode valueNode = (ValueNode) jsonNode;
	        map.put(currentPath, valueNode.asText());
	    }
	}
	
}
