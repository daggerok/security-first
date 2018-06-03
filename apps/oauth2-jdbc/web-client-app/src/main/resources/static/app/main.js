(function main() {
  'use strict';

  document.addEventListener('DOMContentLoaded', function mainLoaded() {

    setTimeout(function disablePulseAfterTimeout() {
      $('.pulse').removeClass('pulse');
    }, 2000);

    window.config = window.config || {};

    fetch('/api/config')
      .then(function onData(data) {
        return data.json()
      })
      .then(function onJson(json) {
        console.log(json);
        console.log(typeof json);
        window.config = json || {};
      });

    document.querySelector('#client-id').addEventListener('change', function onClientIdKeyPress(e) {
      window.config.clientId = e.target.value || '';
    }, false);

    document.querySelector('#client-secret').addEventListener('change', function onClientIdKeyPress(e) {
      window.config.clientSecret = e.target.value || '';
    }, false);

    function setSecuredContent(data) {
      document.querySelector('#secured-content').textContent = data;
    }

    var obtainTokenBtn = document.querySelector('#obtain-token-btn');

    obtainTokenBtn.addEventListener('click', function onObtainButtonClick() {

      var clientId = window.config.clientId;
      var clientSecret = window.config.clientSecret;

      var formData = {
        grant_type: 'password',
        username: 'usr',
        password: 'pwd',
      };

      var body = Object.keys(formData).map(function onKeyMapping(key) {
        return encodeURIComponent(key) + '=' + encodeURIComponent(formData[key]);
      })
      .join('&');

      var baseUrl = window.config['appProps']['authServerUrl'];
      var url = baseUrl + '/oauth/token';

      var req = {
        method: 'post',
        headers: {
          // 'Access-Control-Allow-Origin': '*',
          Accept: 'application/json; charset=UTF-8',
          Authorization: 'Basic ' + btoa(clientId + ':' + clientSecret),
          'Content-Type': 'application/x-www-form-urlencoded; charset=utf-8',
        },
        body: body,
        mode: 'cors',
      };

      console.log(url, req);

      fetch(url, req)
        .then(function onData(data) {
          return data.json();
        })
        .then(function onJson(json) {
          console.log(json);

          var accessToken = json['access_token'];
          window.config['access_token'] = accessToken;

          setSecuredContent(accessToken);

          var securedDataBtn = document.querySelector('#obtain-data-btn');
          securedDataBtn.classList.remove('hide');

          var clientIdInput = document.querySelector('#client-id');
          clientIdInput.value = '';
          clientIdInput.classList.add('hide');

          var cleintSecretInput = document.querySelector('#client-secret');
          cleintSecretInput.value = '';
          cleintSecretInput.classList.add('hide');

          obtainTokenBtn.classList.add('hide');
        })
        .catch(function onError(reason) {
          setSecuredContent('Some error: ' + reason.message || reason.toString());
        })
    }, false);

    var obtainDataBtn = document.querySelector('#obtain-data-btn');
    obtainDataBtn.addEventListener('click', function onObtainButtonClick() {

      fetch('http://127.0.0.1:8002', {
          headers: {
            Authorization: 'Bearer ' + window.config['access_token'],
            Accept: 'application/json; charset=UTF-8',
            'Access-Control-Allow-Origin': '*',
          },
          mode: 'cors',
        })
        .then(function onResponse(data) {
          return data.json();
        })
        .then(function onResponse(json) {
          setSecuredContent(JSON.stringify(json));
          obtainDataBtn.classList.add('hide');
        })
        .catch(function onError(reason) {
          setSecuredContent('Some error: ' + reason.message || reason.toString());
        })
    }, false);
  }, false);
})();
