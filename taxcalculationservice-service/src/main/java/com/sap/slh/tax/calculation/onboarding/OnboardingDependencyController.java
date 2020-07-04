package com.sap.slh.tax.calculation.onboarding;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sap.slh.tax.calculation.controller.BaseAPIController;
import com.sap.slh.tax.calculation.dto.DependencyItem;
import com.sap.slh.tax.calculation.model.uri.TaxCalculationUriConstant;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Onboarding Dependencies Retrieval ")
public class OnboardingDependencyController extends BaseAPIController {

	@Autowired
	@Qualifier(value = "dependencyServiceImpl")
	private DependencyService dependencyService;

	@ApiOperation(value = "Get dependencies of onboarding service", notes = "Get dependencies of the onboarding service", nickname = "Get dependencies of onboarding service")
	@GetMapping(value = TaxCalculationUriConstant.GET_DEPENDENCIES, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<DependencyItem>> getDependencies() {
		
		return ResponseEntity.ok(dependencyService.getDependencyItems());
	}
}
