function() {
  karate.configure("ssl", true)

  var config = {
    SERVER_URL: 'https://tax-calculation-client-acceptance.cfapps.sap.hana.ondemand.com',
	PATH: '/api/v1/tax/calculate'
  };

  karate.log('OpenAM Host:', config.SERVER_URL);

  return config;
}
