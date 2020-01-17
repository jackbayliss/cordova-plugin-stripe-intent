package com.jackbayliss.stripeintent;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.model.PaymentIntent;
import com.stripe.android.model.PaymentMethod;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.view.CardInputWidget;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.app.Activity;
import android.view.ViewParent;
import android.util.Log;
public class StripeActivity extends Activity {

    private static final String TAG = StripeActivity.class.getSimpleName();
    private CardInputWidget widget;
    private String stripePushableKey = "pk_test_DakHBX2TZX5mIjEvVnZ4Fytx";
    private String paymentIntentClientSecret;
    public String BACKEND_URL = "https://google.com";
    public String package_name;
    private OkHttpClient httpClient = new OkHttpClient();
    private Stripe stripe;
    private Listener paymentListener;
    private ViewParent webViewParent;
    private Intent intent = new Intent();
    public interface Listener {
    }
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PaymentConfiguration.init(
          getApplicationContext(),
          this.stripePushableKey
      );
        this.package_name = getApplication().getPackageName();
        setContentView(getApplication().getResources().getIdentifier("activity_intent", "layout", this.package_name));

        loadPage();
    }

    private void loadPage() {
        stripe = new Stripe(getApplicationContext(), stripePushableKey);
      // Clear the card widget
      CardInputWidget cardInputWidget = findViewById(getApplication().getResources().getIdentifier("cardInputWidget", "id", this.package_name));
      cardInputWidget.clear();

       // Hook up the pay button to the card widget and stripe instance
       Button payButton = findViewById(getApplication().getResources().getIdentifier("payButton", "id", this.package_name));
       payButton.setOnClickListener((View view) -> {
        pay(this,cardInputWidget);

       });

  }

    public Boolean storeIntentKey(String intentKey){
      this.paymentIntentClientSecret = intentKey;
      return true;
    }

    private  void pay(Activity activity,CardInputWidget widget){
      PaymentMethodCreateParams params = widget.getPaymentMethodCreateParams();
      if (params == null) {
        return;
    }
    stripe.createPaymentMethod(params, new ApiResultCallback<PaymentMethod>() {
        @Override
        public void onSuccess(@NonNull PaymentMethod result) {
            // Create and confirm the PaymentIntent by calling the sample server's /pay endpoint.
            pay(result.id, null);
            Toast.makeText(activity, "Success calling pay method: ", Toast.LENGTH_LONG).show();

        }

        @Override
        public void onError(@NonNull Exception e) {
            Toast.makeText(activity, "Error: ", Toast.LENGTH_LONG).show();

        }
    });
  }

  private void pay(@Nullable String paymentMethodId, @Nullable String paymentIntentId) {
    final MediaType mediaType = MediaType.get("application/json; charset=utf-8");
    final String json;
    if (paymentMethodId != null) {
        json = "{"
                + "\"useStripeSdk\":true,"
                + "\"paymentMethodId\":" + "\"" + paymentMethodId + "\","
                + "\"currency\":\"usd\","
                + "\"items\":["
                + "{\"id\":\"photo_subscription\"}"
                + "]"
                + "}";
    } else {
        json = "{"
                + "\"paymentIntentId\":" +  "\"" + paymentIntentId + "\""
                + "}";
    }
    RequestBody body = RequestBody.create(json, mediaType);
    Request request = new Request.Builder()
            .url(BACKEND_URL)
            .post(body)
            .build();
    httpClient
            .newCall(request)
            .enqueue(new PayCallback(this, stripe));
}

private static final class PayCallback implements Callback {
    @NonNull private final WeakReference<StripeActivity> activityRef;
    @NonNull private final Stripe stripe;

    private PayCallback(@NonNull StripeActivity activity, @NonNull Stripe stripe) {
        this.activityRef = new WeakReference<>(activity);
        this.stripe = stripe;
    }

    @Override
    public void onFailure(@NonNull Call call, @NonNull IOException e) {
        final StripeActivity activity = activityRef.get();
        if (activity == null) {
            return;
        }

        activity.runOnUiThread(() -> {
            Toast.makeText(activity, "Error: " + e.toString(), Toast.LENGTH_LONG).show();
        });
    }

    @Override
    public void onResponse(@NonNull Call call, @NonNull final Response response)
            throws IOException {
        final StripeActivity activity = activityRef.get();
        if (activity == null) {
            return;
        }

        if (!response.isSuccessful()) {
            activity.runOnUiThread(() -> {
                Toast.makeText(activity,
                        "Error: " + response.toString(), Toast.LENGTH_LONG).show();
            });
        } else {
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, String>>(){}.getType();
            Map<String, String> responseMap = gson.fromJson(response.body().string(), type);

            String error = responseMap.get("error");
            String paymentIntentClientSecret = responseMap.get("clientSecret");
            String requiresAction = responseMap.get("requiresAction");

            if (error != null) {
                Toast.makeText(activity, "Some kind of error", Toast.LENGTH_LONG).show();

            } else if (paymentIntentClientSecret != null) {
                if ("true".equals(requiresAction)) {
                    activity.runOnUiThread(() ->
                            stripe.authenticatePayment(activity, paymentIntentClientSecret));
                } else {
                    activity.runOnUiThread(() -> {
                        Toast.makeText(activity,
                                "BIG SUCCESS", Toast.LENGTH_LONG).show();
                                // Intent i = new Intent(this, StripeIntent.class);
                                // i.putExtra("name", "test"); // pass arbitrary data to launched activity
                                // activity.startActivityForResult(i, 0);
                    });
                }
            }

        }
    }
}
@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Handle the result of stripe.confirmPayment
        stripe.onPaymentResult(requestCode, data, new PaymentResultCallback(this));
    }
    private static final class PaymentResultCallback
    implements ApiResultCallback<PaymentIntentResult> {
private final WeakReference<StripeActivity> activityRef;

PaymentResultCallback(@NonNull StripeActivity activity) {
    activityRef = new WeakReference<>(activity);
}

@Override
public void onSuccess(@NonNull PaymentIntentResult result) {
    final StripeActivity activity = activityRef.get();
    if (activity == null) {
        return;
    }

    PaymentIntent paymentIntent = result.getIntent();
    PaymentIntent.Status status = paymentIntent.getStatus();
    if (status == PaymentIntent.Status.Succeeded) {
        // Payment completed successfully
        activity.runOnUiThread(() -> {
            Toast.makeText(activity,
            "Completed", Toast.LENGTH_LONG).show();
});
    } else if (status == PaymentIntent.Status.RequiresPaymentMethod) {
        // Payment failed – allow retrying using a different payment method
        activity.runOnUiThread(() -> {
            Toast.makeText(activity,
            "Payment failed: " + paymentIntent.getLastPaymentError(), Toast.LENGTH_LONG).show();
});

    } else if (status == PaymentIntent.Status.RequiresConfirmation) {
        // After handling a required action on the client, the status of the PaymentIntent is
        // requires_confirmation. You must send the PaymentIntent ID to your backend
        // and confirm it to finalize the payment. This step enables your integration to
        // synchronously fulfill the order on your backend and return the fulfillment result
        // to your client.
        activity.pay(null, paymentIntent.getId());
    }
}

@Override
public void onError(@NonNull Exception e) {
    final StripeActivity activity = activityRef.get();
    if (activity == null) {
        return;
    }

    // Payment request failed – allow retrying using the same payment method
    activity.runOnUiThread(() -> {
        activity.runOnUiThread(() -> {
            Toast.makeText(activity,
            "Error " +  e.toString(), Toast.LENGTH_LONG).show();
});
    });
}
}

public Boolean AddBackendUrl(String backendurl){
    this.BACKEND_URL = backendurl;
    return true;
}


}




    
    


    

