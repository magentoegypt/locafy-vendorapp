package magentoegypt.locafy.addons.advance_transactions;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;
import java.util.HashMap;

public class Ced_MultiVendor_TransactionView extends Ced_MultiVendor_NavigationActivity {

    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    String transactionview_url = "";
    HashMap<String, String> transactionHashmap;
    String paymentId = "";
    TextView vendorname;
    TextView paymentmethod_beneficiarydetails;
    TextView transactionid;
    TextView transactiondate;
    TextView transactionmode;
    TextView transactiontype;
    TextView shippingamount;
    TextView amount;
    TextView adjustmentamount;
    TextView netamount;
    TextView notes;
    RecyclerView orderlist_recyclerView;
    Ced_MultiVendor_OrderAdapter Orderlist_Adapter;
    ArrayList<HashMap<String, String>> orderArraylist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ced_multivendor_transactionview);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_bar_color));
        }
        connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(getApplicationContext());
        transactionview_url = session.getBase_Url() + "vadvtransactionapi/transaction/view";
        transactionHashmap = new HashMap<String, String>();
        orderArraylist = new ArrayList<HashMap<String, String>>();
        paymentId = getIntent().getStringExtra("payment_id");

        if (connectionDetector.isConnectingToInternet()) {
            ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
            getLayoutInflater().inflate(R.layout.ced_multivendor_transactionview, content, true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);
            orderlist_recyclerView = findViewById(R.id.MultiVendor_orderdetails_recyclerview);
            vendorname = findViewById(R.id.MultiVendor_vendorname);
            paymentmethod_beneficiarydetails = findViewById(R.id.MultiVendor_beneficiarydetail);
            transactionid = findViewById(R.id.MultiVendor_Transactionid);
            transactiondate = findViewById(R.id.MultiVendor_transactiondate);
            transactionmode = findViewById(R.id.MultiVendor_transactionmode);
            transactiontype = findViewById(R.id.MultiVendor_transactiontype);
            shippingamount = findViewById(R.id.MultiVendor_shippingamount);
            amount = findViewById(R.id.MultiVendor_amount);
            adjustmentamount = findViewById(R.id.MultiVendor_adjustmentamount);
            netamount = findViewById(R.id.MultiVendor_netamount);
            notes = findViewById(R.id.MultiVendor_notes);

            transactionHashmap.put("vendor_id", session.getVendorid());
            transactionHashmap.put("id", paymentId);
            transactionHashmap.put("hashkey", getResources().getString(R.string.header));
            Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
                @Override
                public void processFinish(Object output) throws JSONException {
                    try {
                        String string = output.toString();
                        JSONObject object = new JSONObject(string);
                        JSONObject data = object.getJSONObject("data");
                        String success = data.getString("success");
                        if (success.equals("true")) {
                            JSONObject vendor = data.getJSONObject("vendor");
                            vendorname.setText(vendor.getString("vendor_name"));
                            paymentmethod_beneficiarydetails.setText(vendor.getString("Payment_detail"));

                            JSONArray transaction = data.getJSONArray("Transaction");
                            JSONObject transaction_object = transaction.getJSONObject(0);

                            transactionid.setText(transaction_object.getString("transaction_id"));
                            transactiondate.setText(transaction_object.getString("transaction_date"));
                            transactiontype.setText(transaction_object.getString("transaction_type"));
                            transactionmode.setText(transaction_object.getString("payment_method"));
                            shippingamount.setText(transaction_object.getString("total_shipping_amount"));
                            amount.setText(transaction_object.getString("amount"));
                            adjustmentamount.setText(transaction_object.getString("adjustable_amount"));
                            netamount.setText(transaction_object.getString("net_amount"));
                            notes.setText(transaction_object.getString("notes"));

                            JSONArray orders = data.getJSONArray("orders");

                            for (int x = 0; x < orders.length(); x++) {
                                JSONObject order_object = orders.getJSONObject(x);
                                HashMap<String, String> order_data = new HashMap<String, String>();
                                order_data.put("paymentid", paymentId);
                                order_data.put("orderid", order_object.getString("order_id"));
                                order_data.put("grandtotal", order_object.getString("grand_total"));
                                order_data.put("commissionfee", order_object.getString("commission_fee"));
                                order_data.put("orderpaymode", order_object.getString("order_paymode"));
                                order_data.put("vendorpayment", order_object.getString("vendor_payment"));
                                orderArraylist.add(order_data);
                            }
                            Orderlist_Adapter = new Ced_MultiVendor_OrderAdapter(Ced_MultiVendor_TransactionView.this, orderArraylist);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Ced_MultiVendor_TransactionView.this);
                            orderlist_recyclerView.setLayoutManager(mLayoutManager);
                            orderlist_recyclerView.setAdapter(Orderlist_Adapter);

                        } else {
                            Toast.makeText(Ced_MultiVendor_TransactionView.this, data.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, Ced_MultiVendor_TransactionView.this, "POST", transactionHashmap);
            response.execute(transactionview_url);


        } else {
            Intent nointernet = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(nointernet);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }
    }
}
