package com.sap.slh.tax.onboarding.json.rule.exception;


public class TenantIDNotFoundException extends JsonRuleException {

	private static final long serialVersionUID = 1152769378657866632L;

	public TenantIDNotFoundException() {
		super(ErrorCode.TENANT_NOT_FOUND.getErrorDetail(),ErrorCode.TENANT_NOT_FOUND);
	}

}