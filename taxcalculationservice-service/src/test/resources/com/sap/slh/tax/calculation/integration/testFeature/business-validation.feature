Feature: Tax Calculation Service- Business Validations for invalid request
  #Jira user story : https://sapjira.wdf.sap.corp/browse/GSSITAAS-3577
  Background:
    * url SERVER_URL
    * header Content-Type = 'application/json'
    * configure charset = null
  
  @GSSITAAS-3577
  Scenario Outline: <scenario>
    Given path PATH
    And request read('../testData/' + <validation> + "/" + <scenario> + '/request.json')
    And def responseSchema = read('../testData/' + <validation> + "/" + <scenario> + '/response.json')
    When method post
    Then status 200
    And def taxDeterminationResponse = response
    And match taxDeterminationResponse contains responseSchema

    Examples:
      | validation| scenario |
      | "Validations" | "Business validation" |
