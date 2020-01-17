To install cd into myApp, do ``` npm i ```
Then, do ```ionic cordova run android --device```

Create the back end required for the calls [HERE](https://github.com/stripe-samples/accept-a-card-payment/blob/master/without-webhooks/server/php/public/pay.php) 

Then, set the URL [here](https://github.com/jackbayliss/cordova-plugin-stripe-intent/blob/master/working-example/StripeIntent/src/android/StripeActivity.java#L49)

You can then run the following in a console log it will create the Activity ontop of the Ionic one. : 
```window.cordova.plugins.StripeIntent.createIntent("",function(test){console.log(test)},function(test){console.log(test)})```

If you need a test card use [this](https://stripe.com/docs/testing#cards)
