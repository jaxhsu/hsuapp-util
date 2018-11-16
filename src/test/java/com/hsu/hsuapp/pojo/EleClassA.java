package com.hsu.hsuapp.pojo;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class EleClassA {

	private String eleA;
	private String eleB;
	private String attrC;

	@XmlElement
	public String getEleA() {
		return eleA;
	}

	public void setEleA(String eleA) {
		this.eleA = eleA;
	}

	@XmlElement(name = "elebnewname")
	public String getEleB() {
		return eleB;
	}

	public void setEleB(String eleB) {
		this.eleB = eleB;
	}

	@XmlAttribute()
	public String getAttrC() {
		return attrC;
	}

	public void setAttrC(String attrC) {
		this.attrC = attrC;
	}

}
