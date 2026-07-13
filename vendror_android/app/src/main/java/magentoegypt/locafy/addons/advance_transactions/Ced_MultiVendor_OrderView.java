package magentoegypt.locafy.addons.advance_transactions;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Ced_MultiVendor_OrderView extends Ced_MultiVendor_NavigationActivity {
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    String orderview_url = "";
    String orderID = "";
    String paymentID = "";
    TextView vendor;
    TextView orderid;
    TextView paymentmethod;
    TextView grandtotal;
    TextView shipfee;
    TextView commissionfee;
    TextView shippingamount;
    TextView servicetax;
    TextView fixedfee;
    TextView collectionfee;
    TextView totalamount;
    HashMap<String, String> orderHashmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_bar_color));
        }
        connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(getApplicationContext());
        orderview_url = session.getBase_Url() + "vadvtransactionapi/transaction/details";
        orderHashmap = new HashMap<String, String>();
        orderID = getIntent().getStringExtra("orderid");
        paymentID = getIntent().getStringExtra("paymentid");
        if (connectionDetector.isConnectingToInternet()) {
            ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
            getLayoutInflater().inflate(R.layout.ced_multivendor_orderview, content, true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);
            vendor = findViewById(R.id.MultiVendor_vendor);
            orderid = findViewById(R.id.MultiVendor_orderid);
            paymentmethod = findViewById(R.id.MultiVendor_paymentmode);
            grandtotal = findViewById(R.id.MultiVendor_grandtotal);
            shipfee = findViewById(R.id.MultiVendor_shippingfee);
            commissionfee = findViewById(R.id.MultiVendor_Commissionfee);
            shippingamount = findViewById(R.id.MultiVendor_shippingamount);
            servicetax = findViewById(R.id.MultiVendor_servicetax);
            fixedfee = findViewById(R.id.MultiVendor_fixedfee);
            collectionfee = findViewById(R.id.MultiVendor_collectionfee);
            totalamount = findViewById(R.id.MultiVendor_totalamount);

            orderHashmap.put("vendor_id", session.getVendorid());
            orderHashmap.put("hashkey", getResources().getString(R.string.header));
            orderHashmap.put("order_id", orderID);
            orderHashmap.put("payment_id", paymentID);
            Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
                @Override
                public void processFinish(Object output) throws JSONException {
                    try {
                        String string = output.toString();
                        JSONObject object = new JSONObject(string);
                        JSONObject data = object.getJSONObject("data");
                        String success = data.getString("success");
                        if (success.equals("true")) {
                            JSONArray orderinfo = data.getJSONArray("order_information");
                            JSONObject order = orderinfo.getJSONObject(0);
                            vendor.setText(order.getString("vendor"));
                            orderid.setText(order.getString("order_id"));
                            paymentmethod.setText(order.getString("payment_method"));
                            grandtotal.setText(order.getString("grand_total"));
                            shipfee.setText(order.getString("ship_fee"));
                            commissionfee.setText(order.getString("commission_fee"));
                            shippingamount.setText(order.getString("shipping_amount"));
                            servicetax.setText(order.getString("service_tax"));
                            fixedfee.setText(order.getString("fixed_fee"));
                            collectionfee.setText(order.getString("collection_fee"));
                            totalamount.setText(order.getString("amount"));
                        } else {
                            Toast.makeText(Ced_MultiVendor_OrderView.this, data.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, Ced_MultiVendor_OrderView.this, "POST", orderHashmap);
            response.execute(orderview_url);

        } else {
            Intent nointernet = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(nointernet);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }
    }
}
