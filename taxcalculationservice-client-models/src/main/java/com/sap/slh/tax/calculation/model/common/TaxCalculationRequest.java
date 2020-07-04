
package com.sap.slh.tax.calculation.model.common;

import java.io.Serializable;
import java.util.Date;
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
    "date",
    "amountTypeCode",
    "currencyCode",
    "items"
})
public class TaxCalculationRequest implements Serializable{
	
	private static final long serialVersionUID = -5572752296950306555L;

	@ApiModelProperty(notes = "The identifier of the business transaction for which the consuming application needs taxes to be calculated.", required = true)
	@NotBlank(message = "error.document.id.required")
    @JsonProperty("id")
    private String id;
    
	@ApiModelProperty(notes = "The date of the business transaction, for example, the order date, invoice date, or return date. To determine the tax due for this business transaction, the service applies the tax rules that are applicable on this date. Pattern: YYYY-MM-DD:THH:MM:SSZ.", required = true)
	@NotNull(message = "error.document.date.required")
    @JsonProperty("date")
    private Date date;
    
	@ApiModelProperty(notes = "The type of the amount that can be net or gross indicating if the consuming application wants the tax service to include the tax amount in the item value.", required = true)
	@NotNull(message = "error.document.amountTypeCode.required")
    @JsonProperty("amountTypeCode")
    private TaxCalculationRequest.AmountTypeCode amountTypeCode;
    
	@ApiModelProperty(notes = "The currency of the business transaction. This code follows the standards of ISO 4217. The currency provided here influences the number of digits after the decimal place in the ''amount'' parameters. Pattern: [A-Z]{3}.", required = true)
	@NotNull(message = "error.document.currencyCode.required")
    @JsonProperty("currencyCode")
    private TaxCalculationRequest.CurrencyCode currencyCode;
    
	@Valid
	@NotEmpty(message = "error.document.items.required")
    @JsonProperty("items")
    private List<Item> items = null;
   

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("date")
    public Date getDate() {
        return date;
    }

    @JsonProperty("date")
    public void setDate(Date date) {
        this.date = date;
    }

    @JsonProperty("amountTypeCode")
    public TaxCalculationRequest.AmountTypeCode getAmountTypeCode() {
        return amountTypeCode;
    }

    @JsonProperty("amountTypeCode")
    public void setAmountTypeCode(TaxCalculationRequest.AmountTypeCode amountTypeCode) {
        this.amountTypeCode = amountTypeCode;
    }

    @JsonProperty("currencyCode")
    public TaxCalculationRequest.CurrencyCode getCurrencyCode() {
        return currencyCode;
    }

    @JsonProperty("currencyCode")
    public void setCurrencyCode(TaxCalculationRequest.CurrencyCode currencyCode) {
        this.currencyCode = currencyCode;
    }

    @JsonProperty("items")
    public List<Item> getItems() {
        return items;
    }

    @JsonProperty("items")
    public void setItems(List<Item> items) {
        this.items = items;
    }


    public enum AmountTypeCode {

        GROSS("GROSS"),
        NET("NET");
        private final String value;
        private static final Map<String, TaxCalculationRequest.AmountTypeCode> CONSTANTS = new HashMap<>();

        static {
            for (TaxCalculationRequest.AmountTypeCode c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private AmountTypeCode(String value) {
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
        public static TaxCalculationRequest.AmountTypeCode fromValue(String value) {
            TaxCalculationRequest.AmountTypeCode constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum CurrencyCode {

        AED("AED"),
        AFN("AFN"),
        ALL("ALL"),
        AMD("AMD"),
        ANG("ANG"),
        AOA("AOA"),
        ARS("ARS"),
        AUD("AUD"),
        AWG("AWG"),
        AZN("AZN"),
        BAM("BAM"),
        BBD("BBD"),
        BDT("BDT"),
        BGN("BGN"),
        BHD("BHD"),
        BIF("BIF"),
        BMD("BMD"),
        BND("BND"),
        BOB("BOB"),
        BOV("BOV"),
        BRL("BRL"),
        BSD("BSD"),
        BTN("BTN"),
        BWP("BWP"),
        BYN("BYN"),
        BYR("BYR"),
        BZD("BZD"),
        CAD("CAD"),
        CDF("CDF"),
        CHE("CHE"),
        CHF("CHF"),
        CHW("CHW"),
        CLF("CLF"),
        CLP("CLP"),
        CNY("CNY"),
        COP("COP"),
        COU("COU"),
        CRC("CRC"),
        CSD("CSD"),
        CUC("CUC"),
        CUP("CUP"),
        CVE("CVE"),
        CYP("CYP"),
        CZK("CZK"),
        DJF("DJF"),
        DKK("DKK"),
        DOP("DOP"),
        DZD("DZD"),
        EEK("EEK"),
        EGP("EGP"),
        ERN("ERN"),
        ETB("ETB"),
        EUR("EUR"),
        FJD("FJD"),
        FKP("FKP"),
        GBP("GBP"),
        GEL("GEL"),
        GHC("GHC"),
        GHS("GHS"),
        GIP("GIP"),
        GMD("GMD"),
        GNF("GNF"),
        GTQ("GTQ"),
        GWP("GWP"),
        GYD("GYD"),
        HKD("HKD"),
        HNL("HNL"),
        HRK("HRK"),
        HTG("HTG"),
        HUF("HUF"),
        IDR("IDR"),
        ILS("ILS"),
        INR("INR"),
        IQD("IQD"),
        IRR("IRR"),
        ISK("ISK"),
        JMD("JMD"),
        JOD("JOD"),
        JPY("JPY"),
        KES("KES"),
        KGS("KGS"),
        KHR("KHR"),
        KMF("KMF"),
        KPW("KPW"),
        KRW("KRW"),
        KWD("KWD"),
        KYD("KYD"),
        KZT("KZT"),
        LAK("LAK"),
        LBP("LBP"),
        LKR("LKR"),
        LRD("LRD"),
        LSL("LSL"),
        LTL("LTL"),
        LVL("LVL"),
        LYD("LYD"),
        MAD("MAD"),
        MDL("MDL"),
        MGA("MGA"),
        MKD("MKD"),
        MMK("MMK"),
        MNT("MNT"),
        MOP("MOP"),
        MRO("MRO"),
        MRU("MRU"),
        MTL("MTL"),
        MUR("MUR"),
        MVR("MVR"),
        MWK("MWK"),
        MXN("MXN"),
        MXV("MXV"),
        MYR("MYR"),
        MZN("MZN"),
        NAD("NAD"),
        NGN("NGN"),
        NIO("NIO"),
        NOK("NOK"),
        NPR("NPR"),
        NZD("NZD"),
        OMR("OMR"),
        PAB("PAB"),
        PEN("PEN"),
        PGK("PGK"),
        PHP("PHP"),
        PKR("PKR"),
        PLN("PLN"),
        PYG("PYG"),
        QAR("QAR"),
        ROL("ROL"),
        RON("RON"),
        RSD("RSD"),
        RUB("RUB"),
        RWF("RWF"),
        SAR("SAR"),
        SBD("SBD"),
        SCR("SCR"),
        SDD("SDD"),
        SDG("SDG"),
        SEK("SEK"),
        SGD("SGD"),
        SHP("SHP"),
        SIT("SIT"),
        SKK("SKK"),
        SLL("SLL"),
        SOS("SOS"),
        SRD("SRD"),
        SSP("SSP"),
        STD("STD"),
        SVC("SVC"),
        SYP("SYP"),
        SZL("SZL"),
        THB("THB"),
        TJS("TJS"),
        TMM("TMM"),
        TMT("TMT"),
        TND("TND"),
        TOP("TOP"),
        TRY("TRY"),
        TTD("TTD"),
        TWD("TWD"),
        TZS("TZS"),
        UAH("UAH"),
        UGX("UGX"),
        USD("USD"),
        USN("USN"),
        USS("USS"),
        UYI("UYI"),
        UYU("UYU"),
        UZS("UZS"),
        VEB("VEB"),
        VEF("VEF"),
        VND("VND"),
        VUV("VUV"),
        WST("WST"),
        XAF("XAF"),
        XAG("XAG"),
        XAU("XAU"),
        XBA("XBA"),
        XBB("XBB"),
        XBC("XBC"),
        XBD("XBD"),
        XCD("XCD"),
        XDR("XDR"),
        XFO("XFO"),
        XFU("XFU"),
        XOF("XOF"),
        XPD("XPD"),
        XPF("XPF"),
        XPT("XPT"),
        XSU("XSU"),
        XTS("XTS"),
        XUA("XUA"),
        XXX("XXX"),
        YER("YER"),
        ZAR("ZAR"),
        ZMK("ZMK"),
        ZMW("ZMW"),
        ZWD("ZWD"),
        ZWL("ZWL"),
        ZWN("ZWN");
        private final String value;
        private static final Map<String, TaxCalculationRequest.CurrencyCode> CONSTANTS = new HashMap<>();

        static {
            for (TaxCalculationRequest.CurrencyCode c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private CurrencyCode(String value) {
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
        public static TaxCalculationRequest.CurrencyCode fromValue(String value) {
            TaxCalculationRequest.CurrencyCode constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
