package com.hsu.hsuapp.pojo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "root2class")
public class Root2Class {
	
	List<EleClassA> list;

	@XmlElement(name = "classA")
	public List<EleClassA> getList() {
		return list;
	}

	public void setList(List<EleClassA> list) {
		this.list = list;
	}
	
	
}
