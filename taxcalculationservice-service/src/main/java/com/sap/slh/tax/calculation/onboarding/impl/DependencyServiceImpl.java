package com.sap.slh.tax.calculation.onboarding.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.sap.slh.tax.calculation.dto.DependencyItem;
import com.sap.slh.tax.calculation.exception.DependencyVcapRetrievalException;
import com.sap.slh.tax.calculation.onboarding.DependencyService;

@Service(value = "dependencyServiceImpl")
public class DependencyServiceImpl implements DependencyService {

	private static Logger logger = LoggerFactory.getLogger(DependencyServiceImpl.class);

	@Value("${BRS.credentials.uaa.xsappname:#{null}}")
	private String xsappname;

	@Override
	public List<DependencyItem> getDependencyItems() {
		return getDependenciesXsAppNames().stream().map(DependencyItem::new).collect(Collectors.toList());
	}

	private List<String> getDependenciesXsAppNames() {
		if (!StringUtils.isEmpty(xsappname)) {
			return Arrays.asList(xsappname);
		} else {
			logger.error("No VCAP entry found for the BRS entry {}","brs.vcap.xsappname");
			throw new DependencyVcapRetrievalException("brs.vcap.xsappname");
		}
	}
}
