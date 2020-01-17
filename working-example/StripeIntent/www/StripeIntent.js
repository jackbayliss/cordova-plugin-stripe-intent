var exec = require('cordova/exec');

exports.createIntent = function (arg0, success, error) {
    exec(success, error, 'StripeIntent', 'StripeActivity', [arg0]);
};

exports.addBackendUrl = function (backendurl, success, error) {
    exec(success, error, 'StripeIntent', 'AddBackendUrl', [backendurl]);
};

