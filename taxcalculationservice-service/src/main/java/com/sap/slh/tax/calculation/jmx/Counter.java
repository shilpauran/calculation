package com.sap.slh.tax.calculation.jmx;

public abstract class Counter implements CounterMBean {

    protected int count = 0;

    public void incrementCount() {
        this.count++;
    }

    public int getCount() {
        return this.count;
    }

    public void resetCount() {
        this.count = 0;
    }   

}