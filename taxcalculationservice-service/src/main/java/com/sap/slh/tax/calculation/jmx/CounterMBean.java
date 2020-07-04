package com.sap.slh.tax.calculation.jmx;

public interface CounterMBean {

    public void incrementCount();
    
    public int getCount();

    public void resetCount();
    
}
