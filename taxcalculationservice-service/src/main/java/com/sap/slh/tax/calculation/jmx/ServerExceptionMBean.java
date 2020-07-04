package com.sap.slh.tax.calculation.jmx;

import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

@Component
@ManagedResource
public class ServerExceptionMBean extends Counter {

    @ManagedAttribute
    @Override
    public int getCount() { 
    	return super.getCount();
    }

    @ManagedOperation
    @Override    
    public void resetCount() {
        super.resetCount();
    }

}