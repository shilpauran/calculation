Feature: Tax Calculation Service- Error Handling Scenarios
  #Jira user story : https://sapjira.wdf.sap.corp/browse/GSSITAAS-3850
  #Jira user story : https://sapjira.wdf.sap.corp/browse/GSSITAAS-3869
  Background:
    * url SERVER_URL
    * header Content-Type = 'application/json'
    * configure charset = null
    
  @GSSITAAS-3850,GSSITAAS-3869
  Scenario Outline: <scenario>
    Given path PATH
    And request read('../testData/' + <error handling> + "/" + <scenario> + '/request.json')
    And def responseSchema = read('../testData/' + <error handling> + "/" + <scenario> + '/response.json')
    When method post
    Then status 200
    And def taxCalculationResponse = response
    And match taxCalculationResponse contains responseSchema

    Examples:
      | error handling | scenario |
      |"Error Handling" | "No Content DP in IN" |
      |"Error Handling" | "No Content outdated date" |
      |"Error Handling" | "Partial Content DP for tax type - GST, PST & curr -USD" |
      |"Error Handling" | "Partial Content DS for CA and GB" |
      |"Error Handling" | "Partial Content DS for tax type - VAT, curr- CAD" |
