package com.jackbayliss.stripeintent;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.PluginResult;
import android.content.Intent;
import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.view.ViewParent;
import android.view.ViewGroup;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.content.Intent;

public class StripeIntent extends CordovaPlugin implements StripeActivity.Listener {
    
    private ViewParent webViewParent;
    private CallbackContext callback = null;
    private String appResourcesPackage;
    private StripeActivity activity;
    private CallbackContext paymentCallbackContext;
    private int containerViewId = 50;
    private String apiUserId;
    private String apiToken;
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
    }
    

    
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        Context context = cordova.getActivity().getApplicationContext();
        callback = callbackContext;
        if(action.equals("StripeActivity")) {
            String message = args.getString(0);
            this.openNewActivity(message,context);
            callbackContext.success("Activity Started");
            return true;

        }else if(action.equals("AddBackendUrl")) {
          
          String backendurl = args.getString(0);
           activity.AddBackendUrl(backendurl);
           callbackContext.success("Stored");
          return true;
        }
        return false;
    }
    private Intent intent;
    private void openNewActivity(String name,Context context) {
      Intent intent = new Intent(context, StripeActivity.class);
      cordova.startActivityForResult(this, new Intent(intent), 0);

}


public void onPaymentSuccess(Boolean completed) {
  PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, completed);
  pluginResult.setKeepCallback(true);
  paymentCallbackContext.sendPluginResult(pluginResult);
}




 


}
