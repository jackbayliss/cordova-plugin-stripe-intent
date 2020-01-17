# cordova-plugin-stripe-intent
 Cordova Stripe Plugin specifically for the new Intent API, working.

## Why?
There is currently no way to integrate a Hybrid Ionic / Cordova application with Stripe for payment intents (Required for SCA). This fixes that.

## Requirements
Currently only on Android, this plugin requires a minimum of cordova@9 and cordova-android@8. I have been unable to test older versions of Android. Therefore if you want to try it, feel free.

## How do I get this working?
Open a bash / cmd in your Ionic root folder and do the following:
```
ionic cordova plugin add cordova-stripe-intent
```
Or, download the latest release and unpack it into folder and then do the following
```
ionic cordova plugin add ../FolderName
```
You can then build you app, and fingers crossed it will work fine.
# Methods

### addBackendUrl(successCallback, errorCallback)
```js
window.cordova.plugins.StripeIntent.addBackendUrl("BACKEND_URL_HERE",function(test){console.log(test)},function(test){console.log(test)})
```

### addPushableKey(successCallback, errorCallback)
```js
window.cordova.plugins.StripeIntent.addPushableKey("KEY_HERE",function(test){console.log(test)},function(test){console.log(test)})
```
### createIntent(successCallback, errorCallback)
This method is for creating the activity. It'll give you an error callback if you haven't added a pusable key or backendurl.
```js
window.cordova.plugins.StripeIntent.createIntent("",function(test){console.log(test)},function(test){console.log(test)})
```

## What is the backend?

You can find the backend code [HERE](https://github.com/stripe-samples/accept-a-card-payment/blob/master/without-webhooks/server/php/public/pay.php) 

## Issues
if you have an issue, please feel free to create one.


## Todo 
iOS implementation.
