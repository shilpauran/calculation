Feature: Tax Calculation Service- Test Scenarios for country Germany
  #Jira user story : https://sapjira.wdf.sap.corp/browse/GSSITAAS-3519
  #Jira user story : https://sapjira.wdf.sap.corp/browse/GSCINSTAXACHIEVERS-606
  Background:
      * url SERVER_URL
      * header Content-Type = 'application/json'
      * configure charset = null
  # @ignore
  @GSSITAAS-3519
  @GSCINSTAXACHIEVERS-606
  Scenario Outline: <scenario>
    Given path PATH
    And request read('../testData/' + <country> + "/" + <scenario> + '/request.json')
    And def responseSchema = read('../testData/' + <country> + "/" + <scenario> + '/response.json')
    When method post
    Then status 200
    And def taxCalculationResponse = response 
    And match taxCalculationResponse contains responseSchema

    Examples:
      | country | scenario |
      |"DE Scenarios" | "AmountType - Gross,Non-Taxable Scenario for GST" |
      |"DE Scenarios" | "AmountType - Net,Non-Taxable Scenario for GST" |
      |"DE Scenarios" | "AmountType - Gross, GST with Reverse Charge" |
      |"DE Scenarios" | "AmountType - Gross, GST" |
      |"DE Scenarios" | "AmountType - Net, GST" |


