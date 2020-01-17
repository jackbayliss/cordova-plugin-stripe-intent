# cordova-plugin-stripe-intent
 Cordova plugin specifically for the new Intent API.

## Why?

There is currently no way to integrate a Hybrid Ionic / Cordova application with Stripe for payment intents (Required for SCA). This fixes that.

## Requirements
Currently only on Android, and extremely experimental - I have this working on Android 9.0.
This plugin requires a minimum of cordova@9 and cordova-android@8.

## How do I get this working?

If you're not sure how to get this working, don't download it just yet- It's really not ready.

## What is the backend?

You can find the backend code [HERE](https://github.com/stripe-samples/accept-a-card-payment/blob/master/without-webhooks/server/php/public/pay.php) 

## Issues
I am expecting to see issues, this is NOT the full release. This is a "working" copy 


## Todo 
1. The payment is 100% working, and goes through fine. The issue is, I have to pass the payment success back to the Cordova / Ionic web view. 
