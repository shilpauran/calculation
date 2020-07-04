package com.sap.slh.tax.calculation.onboarding;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.sap.slh.tax.calculation.dto.DependencyItem;
import com.sap.slh.tax.calculation.onboarding.impl.DependencyServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class OnboardingDependencyControllerTest {
	
	@Mock
	DependencyServiceImpl mockDependencyService;

	@InjectMocks
	OnboardingDependencyController target;
	
	@Test
	public void resultNotNull() {
		
		List<DependencyItem> depItems = new ArrayList<>();
		
		//Mock
		Mockito.when(mockDependencyService.getDependencyItems()).thenReturn(depItems);
		
		//Execute
		ResponseEntity<List<DependencyItem>> result = target.getDependencies();
		
		//Verify
		assertNotNull(result);
	}

}
