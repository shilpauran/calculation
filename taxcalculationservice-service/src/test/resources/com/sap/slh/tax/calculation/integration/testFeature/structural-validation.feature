Feature: Tax Calculation Service- Structural Validations at Document & Item level for 500 bad request
  #Jira user story : https://sapjira.wdf.sap.corp/browse/GSSITAAS-3577
  Background:
    * url SERVER_URL
    * header Content-Type = 'application/json'
    * configure charset = null
  # @ignore
  @GSSITAAS-3577
  Scenario Outline: <scenario>[GSSITAAS-3577]
    Given path PATH
    And request read('../testData/Validations/' + <level> + "/" + <scenario> + '/request.json')
    And def responseSchema = read('../testData/Validations/' + <level> + "/" + <scenario> + '/response.json')
    When method post
    Then status 400
    And def taxDeterminationResponse = response
    And match taxDeterminationResponse contains responseSchema

    Examples:
      | level | scenario |
      | "Structural validations at document level" | "missing AmountTypeCode,CurrencyCode" |
      | "Structural validations at document level" | "missing date,currencyCode" |
      | "Structural validations at document level" | "missing id,amountTypeCode" |
      | "Structural validations at document level" | "missing id,amountTypeCode,currencyCode" |
      | "Structural validations at document level" | "missing all Required field" |
      | "Structural validations at document level" | "invalid date" |
      
      | "Structural validations at Item level" | "missing id,countryRegionCode,taxEventCode" |
      | "Structural validations at Item level" | "missing id,unitPrice,contryRegionCode" |
      | "Structural validations at Item level" | "missing quantity,unitPrice,contryRegionCode,taxEventCode" |
      | "Structural validations at Item level" | "missing quantity,unitPrice,taxEventCode" |
      | "Structural validations at Item level" | "missing 9 Required field" |
      
      | "Structural validation at taxes level" | "missing id,dueCategoryCode" |
      | "Structural validation at taxes level" | "missing taxTypeCode" |
      
  @GSSITAAS-3577
  Scenario Outline: <scenario>[GSSITAAS-3577]
    Given path PATH
    And request read('../testData/Validations/' + <level> + "/" + <scenario> + '/request.json')
    And def responseSchema = read('../testData/Validations/' + <level> + "/" + <scenario> + '/response.json')
    When method post
    Then status 500
    And def taxDeterminationResponse = response
    And match taxDeterminationResponse contains responseSchema
    Examples:
      | level | scenario |
      | "Structural validations at document level" | "invalid currencyCode" |
      | "Structural validations at document level" | "invalid amountTypeCode,currencycode" |
      
      | "Structural validations at Item level" | "invalid countryRegionCode" |
      | "Structural validations at Item level" | "invalid countryRegionCode,taxEventCode" |
      
      | "Structural validation at taxes level" | "invalid dueCategoryCode" | 
      | "Structural validation at taxes level" | "invalid taxTypeCode,dueCategoryCode" |  
      
  @GSSITAAS-3577
  Scenario Outline: <scenario>[GSSITAAS-3577]
    Given path PATH
    And request read('../testData/Validations/' + <level> + "/" + <scenario> + '/request.json')
    And def responseSchema = read('../testData/Validations/' + <level> + "/" + <scenario> + '/response.json')
    When method post
    Then status 200
    And def taxDeterminationResponse = response
    And match taxDeterminationResponse contains responseSchema
    Examples:
      | level | scenario |
      | "Structural validation at taxes level" | "invalid taxTypeCode" |