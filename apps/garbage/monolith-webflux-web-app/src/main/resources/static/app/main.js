(function main() {
  'use strict';

  document.addEventListener('DOMContentLoaded', function mainLoaded() {

    setTimeout(function disablePulseAfterTimeout() {
      $('.pulse').removeClass('pulse');
    }, 2000);

  }, false);

})();
