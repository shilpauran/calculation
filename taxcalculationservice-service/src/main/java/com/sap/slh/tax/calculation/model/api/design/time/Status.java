package com.sap.slh.tax.calculation.model.api.design.time;

public enum Status {

    
    SUCCESS("Fetch Operation is successful."),

    FAILURE("Fetch Operation Failed.");

    private String value;

    
    Status(final String value) {
        this.value = value;
    }

   
    public final String getValue() {
        return value;
    }
}