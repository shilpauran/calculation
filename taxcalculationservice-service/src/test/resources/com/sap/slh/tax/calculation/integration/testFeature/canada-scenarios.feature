Feature: Tax Calculation Service- Test Scenarios for country Canada
  #Jira user story : https://sapjira.wdf.sap.corp/browse/GSSITAAS-3519
  #Jira user story : https://sapjira.wdf.sap.corp/browse/GSCINSTAXACHIEVERS-606
  Background:
      * url SERVER_URL
      * header Content-Type = 'application/json'
      * configure charset = null
  # @ignore
  @GSSITAAS-3519,GSCINSTAXACHIEVERS-606
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
      |"Canada Scenarios" | "DP for tax type - GST, HST" |
      |"Canada Scenarios" | "DP with Tax event11, amount type - NET" |
      |"Canada Scenarios" | "DP  with Tax event11, Amount type - GROSS"|
      #-----------------------non-Taxable-----------------------------
      |"Canada Scenarios" |"AmountType - Net,Non-Taxable Scenario for GST"|
      |"Canada Scenarios" |"AmountType - Net,Non-Taxable Scenario for HST"|
      |"Canada Scenarios" |"AmountType - Net,Non-Taxable Scenario for PST"|
      |"Canada Scenarios" |"AmountType - Net,Non-Taxable Scenario for QST"|
      
      |"Canada Scenarios" |"AmountType - Gross,Non-Taxable Scenario for GST"|
      |"Canada Scenarios" |"AmountType - Gross,Non-Taxable Scenario for HST"|
      |"Canada Scenarios" |"AmountType - Gross,Non-Taxable Scenario for PST"|
      |"Canada Scenarios" |"AmountType - Gross,Non-Taxable Scenario for QST"|
      
      |"Canada Scenarios" |"AmountType - Net,Non-Taxable Scenario for GST,PST"|
      |"Canada Scenarios" |"AmountType - Gross,Non-Taxable Scenario for GST,PST"|
      |"Canada Scenarios" |"AmountType - Net,Non-Taxable Scenario for GST,QST"|
      |"Canada Scenarios" |"AmountType - Gross,Non-Taxable Scenario for GST,QST"|
		  #-----------------------single-taxbase---------------------------
		  |"Canada Scenarios" |"AmountType - Net, GST"|
		  |"Canada Scenarios" |"AmountType - Net, HST"|
		  |"Canada Scenarios" |"AmountType - Net, PST"|
		  |"Canada Scenarios" |"AmountType - Net, QST"|
		  
      |"Canada Scenarios" |"AmountType - Gross, GST with Reverse Charge"|
      |"Canada Scenarios" |"AmountType - Gross, HST with Reverse Charge"|
      |"Canada Scenarios" |"AmountType - Gross, PST with Reverse Charge"|
      |"Canada Scenarios" |"AmountType - Gross, QST with Reverse Charge"|
         
      |"Canada Scenarios" |"AmountType - Gross, GST"|
      |"Canada Scenarios" |"AmountType - Gross, HST"|
      |"Canada Scenarios" |"AmountType - Gross, PST"|
      |"Canada Scenarios" |"AmountType - Gross, QST"|    
      #------------------------GST PST------------------------------------------
      |"Canada Scenarios" |"AmountType - Net, GST,PST"|
      |"Canada Scenarios" |"AmountType - Gross, GST, PST"|
      |"Canada Scenarios" |"AmountType - Gross, GST with Reverse Charge, PST"|
      |"Canada Scenarios" |"AmountType - Gross, GST, PST with Reverse Charge"|
      |"Canada Scenarios" |"AmountType - Gross, GST with Reverse Charge, PST with Reverse Charge"|
      #------------------------GST QST---------------------------------------------
      |"Canada Scenarios" |"AmountType - Net, GST,QST"|
      |"Canada Scenarios" |"AmountType - Gross, GST, QST"|
      |"Canada Scenarios" |"AmountType - Gross, GST with Reverse Charge, QST"|
      |"Canada Scenarios" |"AmountType - Gross, GST, QST with Reverse Charge"|
      |"Canada Scenarios" |"AmountType - Gross, GST with Reverse Charge, QST with Reverse Charge"|