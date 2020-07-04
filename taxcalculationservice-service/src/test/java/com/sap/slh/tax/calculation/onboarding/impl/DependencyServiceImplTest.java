package com.sap.slh.tax.calculation.onboarding.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.sap.slh.tax.calculation.exception.DependencyVcapRetrievalException;


public class DependencyServiceImplTest {
	
	DependencyServiceImpl target = new DependencyServiceImpl();
	private final String XSAPPNAME_DEFAULT = "xsappnameContent";
	
	private void mockVCAP(String xsappnameContent) {
        ReflectionTestUtils.setField(this.target, "xsappname", xsappnameContent);
    }

	@Test
	public void xsappnameProvided() {
		
		this.mockVCAP(XSAPPNAME_DEFAULT);
		assertNotNull(target.getDependencyItems());
	}
	
	@Test(expected = DependencyVcapRetrievalException.class)
	public void xsappnameNotProvided() {
		
		this.mockVCAP(null);
		target.getDependencyItems();
	}

}
