package com.sap.slh.tax.calculation.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DependencyItem implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1718014794614752378L;
	private String xsappname;
	
	public DependencyItem() {
    }

    public DependencyItem(String xsappname) {
        this.xsappname = xsappname;
    }

	public String getXsappname() {
		return xsappname;
	}

	public void setXsappname(String xsappname) {
		this.xsappname = xsappname;
	}
	
	
}
