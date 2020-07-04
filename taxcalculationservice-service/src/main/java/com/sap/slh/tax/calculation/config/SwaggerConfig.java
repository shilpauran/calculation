package com.sap.slh.tax.calculation.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * The Class SwaggerConfig.
 * 
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket addressApi() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.sap.slh.tax.calculation.controller"))
				.paths(PathSelectors.ant("/api/**")).build().useDefaultResponseMessages(false)
				.directModelSubstitute(Object.class, java.lang.Void.class).apiInfo(apiInfo());
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Tax Calculation Service")
				.description("This service calculates indirect taxes based on the result of tax determination procedures.")
				.termsOfServiceUrl("https://sap.com")
				.contact(new Contact("Tax Team", "https://sap.com", "taxserviceteam@sap.com"))
				.license("License Version 1.0.0").licenseUrl("http://www.sap.com").version("1.0.0").build();
	}
}