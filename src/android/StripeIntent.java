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
import com.jackbayliss.stripeintent.StripeActivity;
import static android.app.Activity.RESULT_OK;

public class StripeIntent extends CordovaPlugin implements StripeActivity.Listener {

    private ViewParent webViewParent;
    private CallbackContext callback = null;
    private String appResourcesPackage;
    private StripeActivity activity;
    private CallbackContext paymentCallbackContext;
    private int containerViewId = 50;
    private String apiUserId;
    private String apiToken;
    private String BACKEND_URL = null;
    private String stripePushableKey = null;


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
            return true;

        }else if(action.equals("AddBackendUrl")) {
            String backendurl = args.getString(0);
            this.BACKEND_URL = backendurl;
            callbackContext.success("Backend Url stored");
            return true;
        }else if(action.equals("addPushableKey")) {
            String pushableKey = args.getString(0);
            this.stripePushableKey = pushableKey;
            callbackContext.success("Pushable key stored");
            return true;
        }
        return false;
    }
    private Intent intent;
    private void openNewActivity(String name,Context context) {
        if(this.BACKEND_URL==null || this.stripePushableKey==null){
            callback.error("Please add a backend URL and your pushable API key before opening the payment activity.");
        }else{
            Intent intent = new Intent(context, StripeActivity.class);
            intent.putExtra("BACKEND_URL", this.BACKEND_URL);
            intent.putExtra("stripePushableKey", this.stripePushableKey);
            cordova.startActivityForResult(this, new Intent(intent), 0);
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode==0 && this.cordova.getActivity().RESULT_OK==resultCode) {
            callback.success("Success");
        }
    }
}
