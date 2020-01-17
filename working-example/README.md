To install cd into myApp, do ``` npm i ```
Then, do ```ionic cordova run android --device```

Open up a console log and run the following - it'll open the payment activity. Create the back end.

Then, set the URL [here](https://github.com/jackbayliss/cordova-plugin-stripe-intent/blob/master/working-example/StripeIntent/src/android/StripeActivity.java#L49)


You can then run the following in a console log : 
```window.cordova.plugins.StripeIntent.createIntent("",function(test){console.log(test)},function(test){console.log(test)})```
