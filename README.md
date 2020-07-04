# tax-calculation-service

## Contents
* [Pipeline Status](#pipeline-status)
* [Health check endpoints](#health-check-endpoints)
* [How to build](#how-to-build)

### Pipeline Status

#### Dev branch
[![Pipeline Status](https://gketaas.jaas-gcp.cloud.sap.corp/job/taasCF/job/tax-calculation-service/job/dev/badge/icon?subject=Pipeline)](https://gketaas.jaas-gcp.cloud.sap.corp/job/taasCF/job/tax-calculation-service/job/dev)
[![CI Build Status](https://prod-build10000.wdf.sap.corp:443/job/tax-service/job/tax-service-tax-calculation-service-SP-MS-common/badge/icon?subject=CI%20Build)](https://prod-build10000.wdf.sap.corp:443/job/tax-service/job/tax-service-tax-calculation-service-SP-MS-common/)
[![Quality Gate](https://sonarci.wdf.sap.corp:8443/sonar/api/badges/gate?key=com.sap.slh.tax.taxcalculationservice-dev)](https://sonarci.wdf.sap.corp:8443/sonar/dashboard?id=com.sap.slh.tax.taxcalculationservice-dev)
[![Tests](https://sonarci.wdf.sap.corp:8443/sonar/api/badges/measure?key=com.sap.slh.tax.taxcalculationservice-dev&metric=tests)](https://sonarci.wdf.sap.corp:8443/sonar/component_measures/metric/tests?id=com.sap.slh.tax.taxcalculationservice-dev)
[![Coverage](https://sonarci.wdf.sap.corp:8443/sonar/api/badges/measure?key=com.sap.slh.tax.taxcalculationservice-dev&metric=coverage)](https://sonarci.wdf.sap.corp:8443/sonar/component_measures/domain/Coverage?id=com.sap.slh.tax.taxcalculationservice-dev)
[![Bugs](https://sonarci.wdf.sap.corp:8443/sonar/api/badges/measure?key=com.sap.slh.tax.taxcalculationservice-dev&metric=bugs)](https://sonarci.wdf.sap.corp:8443/sonar/project/issues?id=com.sap.slh.tax.taxcalculationservice-dev&resolved=false&types=BUG)
[![Vulnerabilities](https://sonarci.wdf.sap.corp:8443/sonar/api/badges/measure?key=com.sap.slh.tax.taxcalculationservice-dev&metric=vulnerabilities)](https://sonarci.wdf.sap.corp:8443/sonar/project/issues?id=com.sap.slh.tax.taxcalculationservice-dev&resolved=false&types=VULNERABILITY)
[![Code Smells](https://sonarci.wdf.sap.corp:8443/sonar/api/badges/measure?key=com.sap.slh.tax.taxcalculationservice-dev&metric=code_smells)](https://sonarci.wdf.sap.corp:8443/sonar/project/issues?com.sap.slh.tax.taxcalculationservice-dev&resolved=false&types=CODE_SMELL)

[Sonar](https://sonarci.wdf.sap.corp:8443/sonar/dashboard?id=com.sap.slh.tax.taxcalculationservice-dev), 
[Fortify results](https://fortify.tools.sap/ssc/html/ssc/version/22010/fix/null/?filterSet=a243b195-0a59-3f8b-1403-d55b7a7d78e6) & 
[Vulas Scan results](https://vulas.mo.sap.corp/apps/#/5DB8CF42920317E46144471B4798DA3A)

### Health check endpoints


### How to build
mvn clean install

### API documentation 
The documentation pertaining to the tax-calculation service is available in the link below

[Swagger](https://tax-calculation-swagger.internal.cfapps.sap.hana.ondemand.com/swagger-ui.html#/tax-calculation-controller/calculate_tax)


