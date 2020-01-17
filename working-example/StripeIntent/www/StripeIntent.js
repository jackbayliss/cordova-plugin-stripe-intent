var exec = require('cordova/exec');

exports.createIntent = function (arg0, success, error) {
    exec(success, error, 'StripeIntent', 'StripeActivity', [arg0]);
};

exports.addBackendUrl = function (arg0, success, error) {
    exec(success, error, 'StripeIntent', 'AddBackendUrl', [arg0]);
};
exports.addPushableKey = function (arg0, success, error) {
    exec(success, error, 'StripeIntent', 'addPushableKey', [arg0]);
};

