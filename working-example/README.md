To install cd into myApp, do ``` npm i ```
Then, do ```ionic cordova run android --device```

Create the back end required for the calls [HERE](https://github.com/stripe-samples/accept-a-card-payment/blob/master/without-webhooks/server/php/public/pay.php) 

Then, set the URL [here](https://github.com/jackbayliss/cordova-plugin-stripe-intent/blob/master/working-example/StripeIntent/src/android/StripeActivity.java#L49)

You can then run the following in a console log : 
```window.cordova.plugins.StripeIntent.createIntent("",function(test){console.log(test)},function(test){console.log(test)})```
