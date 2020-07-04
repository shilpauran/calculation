
package com.sap.slh.tax.calculation.model.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.annotations.ApiModelProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "taxTypeCode",
    "taxRate",
    "dueCategoryCode",
    "nonDeductibleTaxRate",
    "isReverseChargeRelevant"
})
public class TaxLine implements Serializable{
	
	private static final long serialVersionUID = -2774179321207061220L;

	@ApiModelProperty(notes = "The identifier of each item in the business transaction.", required = true)
	@NotBlank(message = "error.tax.id.required")
    @JsonProperty("id")
    private String id;
    
	@ApiModelProperty(notes = "The coded representation of the type of tax. For example, 1 represents GST.", required = true)
	@NotBlank(message = "error.tax.taxTypeCode.required")
    @JsonProperty("taxTypeCode")
    private String taxTypeCode;
	
	@ApiModelProperty(notes = "The percentage rate that the tax service uses to calculate the tax amount.", required = true)
    @JsonProperty("taxRate")
    private Double taxRate;

	@ApiModelProperty(notes = "The code that specifies whether the product tax represents a receivable or a payable to the tax authority.", required = true)
	@NotNull(message = "error.tax.dueCategoryCode.required")
    @JsonProperty("dueCategoryCode")
    private TaxLine.DueCategoryCode dueCategoryCode;

	@ApiModelProperty(notes = "The percentage rate of the tax that is nondeductible.", required = true)
    @JsonProperty("nonDeductibleTaxRate")
    private Double nonDeductibleTaxRate = 0.0D;
	
	@ApiModelProperty(notes = "Determines whether the reverse charge mechanism applies, as determined by the due category. In a reverse charge scenario, responsibility for the reporting of a transaction moves from the seller to the buyer of goods or services. This means the seller does not need to register for VAT in the country in which the supply of goods or services is made.", required = true)
    @JsonProperty("isReverseChargeRelevant")
    private Boolean isReverseChargeRelevant = false;
   

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("taxTypeCode")
    public String getTaxTypeCode() {
        return taxTypeCode;
    }

    @JsonProperty("taxTypeCode")
    public void setTaxTypeCode(String taxTypeCode) {
        this.taxTypeCode = taxTypeCode;
    }

    @JsonProperty("taxRate")
    public Double getTaxRate() {
        return taxRate;
    }
    
    @JsonProperty("taxRate")
    public void setTaxRate(Double taxRate) {
        this.taxRate = taxRate;
    }

    @JsonProperty("dueCategoryCode")
    public TaxLine.DueCategoryCode getDueCategoryCode() {
        return dueCategoryCode;
    }

    @JsonProperty("dueCategoryCode")
    public void setDueCategoryCode(TaxLine.DueCategoryCode dueCategoryCode) {
        this.dueCategoryCode = dueCategoryCode;
    }

    @JsonProperty("nonDeductibleTaxRate")
    public Double getNonDeductibleTaxRate() {
        return nonDeductibleTaxRate;
    }

    @JsonProperty("nonDeductibleTaxRate")
    public void setNonDeductibleTaxRate(Double nonDeductibleTaxRate) {
        this.nonDeductibleTaxRate = nonDeductibleTaxRate;
    }

  
    @JsonProperty("isReverseChargeRelevant")
    public Boolean getIsReverseChargeRelevant() {
        return isReverseChargeRelevant;
    }

    @JsonProperty("isReverseChargeRelevant")
    public void setIsReverseChargeRelevant(Boolean isReverseChargeRelevant) {
        this.isReverseChargeRelevant = isReverseChargeRelevant;
    }
    
    public enum DueCategoryCode {

        PAYABLE("PAYABLE"),
        RECEIVABLE("RECEIVABLE");
        private final String value;
        private static final  Map<String, TaxLine.DueCategoryCode> CONSTANTS = new HashMap<>();

        static {
            for (TaxLine.DueCategoryCode c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private DueCategoryCode(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        @JsonCreator
        public static TaxLine.DueCategoryCode fromValue(String value) {
            TaxLine.DueCategoryCode constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
