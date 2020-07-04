
package com.sap.slh.tax.calculation.model.common;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.annotations.ApiModelProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "quantity",
    "unitPrice",
    "countryRegionCode",
    "taxEventCode",
    "isTaxEventNonTaxable",
    "taxes"
})
public class Item implements Serializable{

	private static final long serialVersionUID = 1202810666716271103L;

	@ApiModelProperty(notes = "The ID of the item that requires tax calculation.", required = true)
	@NotBlank(message = "error.item.id.required")
    @JsonProperty("id")
    private String id;
	
	@ApiModelProperty(notes = "The quantity of this item. This can include fractions expressed using decimal notation.", required = true)
	@NotNull(message = "error.item.quantity.required")
    @JsonProperty("quantity")
    private BigDecimal quantity;
   
	@ApiModelProperty(notes = "The unit price of the item.", required = true)
	@NotNull(message = "error.item.unitPrice.required")
    @JsonProperty("unitPrice")
    private BigDecimal unitPrice;
    
	@ApiModelProperty(notes = "The code that identifies the country or region. This code follows the standards of ISO 3166-1 alpha-2. Pattern: [A-Z]{2}.", required = true)
	@NotNull(message = "error.item.countryRegionCode.required")
    @JsonProperty("countryRegionCode")
    private Item.CountryRegionCode countryRegionCode;
    
	@ApiModelProperty(notes = "The tax-related circumstances of an item in a business transaction.", required = true)
	@NotBlank(message = "error.item.taxEventCode.required")
    @JsonProperty("taxEventCode")
    private String taxEventCode;

	@ApiModelProperty(notes = "Determines whether the tax event is nontaxable, as defined by local regulations.", required = false)
    @JsonProperty("isTaxEventNonTaxable")
    private Boolean isTaxEventNonTaxable = false;
   
	@ApiModelProperty(notes = "An array of tax lines with tax attributes, such as the taxType and taxRate for each item.", required = true)
	@Valid
	@NotEmpty(message = "error.item.taxes.required")
    @JsonProperty("taxes")
    private List<TaxLine> taxes = null;

  
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("quantity")
    public BigDecimal getQuantity() {
        return quantity;
    }

    @JsonProperty("quantity")
    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    @JsonProperty("unitPrice")
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    @JsonProperty("unitPrice")
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    @JsonProperty("countryRegionCode")
    public Item.CountryRegionCode getCountryRegionCode() {
        return countryRegionCode;
    }

    @JsonProperty("countryRegionCode")
    public void setCountryRegionCode(Item.CountryRegionCode countryRegionCode) {
        this.countryRegionCode = countryRegionCode;
    }

    @JsonProperty("taxEventCode")
    public String getTaxEventCode() {
        return taxEventCode;
    }

    @JsonProperty("taxEventCode")
    public void setTaxEventCode(String taxEventCode) {
        this.taxEventCode = taxEventCode;
    }

    @JsonProperty("isTaxEventNonTaxable")
    public Boolean getIsTaxEventNonTaxable() {
        return isTaxEventNonTaxable;
    }
    
    @JsonProperty("isTaxEventNonTaxable")
    public void setIsTaxEventNonTaxable(Boolean isTaxEventNonTaxable) {
        this.isTaxEventNonTaxable = isTaxEventNonTaxable;
    }

    @JsonProperty("taxes")
    public List<TaxLine> getTaxes() {
        return taxes;
    }

    @JsonProperty("taxes")
    public void setTaxes(List<TaxLine> taxes) {
        this.taxes = taxes;
    }

    public enum CountryRegionCode {

        AD("AD"),
        AE("AE"),
        AF("AF"),
        AG("AG"),
        AI("AI"),
        AL("AL"),
        AM("AM"),
        AN("AN"),
        AO("AO"),
        AQ("AQ"),
        AR("AR"),
        AS("AS"),
        AT("AT"),
        AU("AU"),
        AW("AW"),
        AX("AX"),
        AZ("AZ"),
        BA("BA"),
        BB("BB"),
        BD("BD"),
        BE("BE"),
        BF("BF"),
        BG("BG"),
        BH("BH"),
        BI("BI"),
        BJ("BJ"),
        BL("BL"),
        BM("BM"),
        BN("BN"),
        BO("BO"),
        BR("BR"),
        BS("BS"),
        BT("BT"),
        BV("BV"),
        BW("BW"),
        BY("BY"),
        BZ("BZ"),
        CA("CA"),
        CC("CC"),
        CD("CD"),
        CF("CF"),
        CG("CG"),
        CH("CH"),
        CI("CI"),
        CK("CK"),
        CL("CL"),
        CM("CM"),
        CN("CN"),
        CO("CO"),
        CR("CR"),
        CU("CU"),
        CV("CV"),
        CX("CX"),
        CY("CY"),
        CZ("CZ"),
        DE("DE"),
        DJ("DJ"),
        DK("DK"),
        DM("DM"),
        DO("DO"),
        DZ("DZ"),
        EC("EC"),
        EE("EE"),
        EG("EG"),
        EH("EH"),
        ER("ER"),
        ES("ES"),
        ET("ET"),
        FI("FI"),
        FJ("FJ"),
        FK("FK"),
        FM("FM"),
        FO("FO"),
        FR("FR"),
        GA("GA"),
        GB("GB"),
        GD("GD"),
        GE("GE"),
        GF("GF"),
        GG("GG"),
        GH("GH"),
        GI("GI"),
        GL("GL"),
        GM("GM"),
        GN("GN"),
        GP("GP"),
        GQ("GQ"),
        GR("GR"),
        GS("GS"),
        GT("GT"),
        GU("GU"),
        GW("GW"),
        GY("GY"),
        HK("HK"),
        HM("HM"),
        HN("HN"),
        HR("HR"),
        HT("HT"),
        HU("HU"),
        ID("ID"),
        IE("IE"),
        IL("IL"),
        IM("IM"),
        IN("IN"),
        IO("IO"),
        IQ("IQ"),
        IR("IR"),
        IS("IS"),
        IT("IT"),
        JE("JE"),
        JM("JM"),
        JO("JO"),
        JP("JP"),
        KE("KE"),
        KG("KG"),
        KH("KH"),
        KI("KI"),
        KM("KM"),
        KN("KN"),
        KR("KR"),
        KW("KW"),
        KY("KY"),
        KZ("KZ"),
        LA("LA"),
        LB("LB"),
        LC("LC"),
        LI("LI"),
        LK("LK"),
        LR("LR"),
        LS("LS"),
        LT("LT"),
        LU("LU"),
        LV("LV"),
        LY("LY"),
        MA("MA"),
        MC("MC"),
        MD("MD"),
        ME("ME"),
        MF("MF"),
        MG("MG"),
        MH("MH"),
        MK("MK"),
        ML("ML"),
        MM("MM"),
        MN("MN"),
        MO("MO"),
        MP("MP"),
        MQ("MQ"),
        MR("MR"),
        MS("MS"),
        MT("MT"),
        MU("MU"),
        MV("MV"),
        MW("MW"),
        MX("MX"),
        MY("MY"),
        MZ("MZ"),
        NA("NA"),
        NC("NC"),
        NE("NE"),
        NF("NF"),
        NG("NG"),
        NI("NI"),
        NL("NL"),
        NO("NO"),
        NP("NP"),
        NR("NR"),
        NU("NU"),
        NZ("NZ"),
        OM("OM"),
        PA("PA"),
        PE("PE"),
        PF("PF"),
        PG("PG"),
        PH("PH"),
        PK("PK"),
        PL("PL"),
        PM("PM"),
        PN("PN"),
        PR("PR"),
        PS("PS"),
        PT("PT"),
        PW("PW"),
        PY("PY"),
        QA("QA"),
        RE("RE"),
        RO("RO"),
        RS("RS"),
        RU("RU"),
        RW("RW"),
        SA("SA"),
        SB("SB"),
        SC("SC"),
        SD("SD"),
        SE("SE"),
        SG("SG"),
        SH("SH"),
        SI("SI"),
        SJ("SJ"),
        SK("SK"),
        SL("SL"),
        SM("SM"),
        SN("SN"),
        SO("SO"),
        SR("SR"),
        ST("ST"),
        SV("SV"),
        SY("SY"),
        SZ("SZ"),
        TC("TC"),
        TD("TD"),
        TF("TF"),
        TG("TG"),
        TH("TH"),
        TJ("TJ"),
        TK("TK"),
        TL("TL"),
        TM("TM"),
        TN("TN"),
        TO("TO"),
        TR("TR"),
        TT("TT"),
        TV("TV"),
        TW("TW"),
        TZ("TZ"),
        UA("UA"),
        UG("UG"),
        UM("UM"),
        US("US"),
        UY("UY"),
        UZ("UZ"),
        VA("VA"),
        VC("VC"),
        VE("VE"),
        VG("VG"),
        VI("VI"),
        VN("VN"),
        VU("VU"),
        WF("WF"),
        WS("WS"),
        YE("YE"),
        YT("YT"),
        ZA("ZA"),
        ZM("ZM"),
        ZW("ZW");
        private final String value;
        private static final Map<String, Item.CountryRegionCode> CONSTANTS = new HashMap<>();

        static {
            for (Item.CountryRegionCode c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private CountryRegionCode(String value) {
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
        public static Item.CountryRegionCode fromValue(String value) {
            Item.CountryRegionCode constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
