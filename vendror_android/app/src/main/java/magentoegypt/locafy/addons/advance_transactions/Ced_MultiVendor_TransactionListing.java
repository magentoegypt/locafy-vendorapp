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

public class Ced_MultiVendor_TransactionListing extends Ced_MultiVendor_NavigationActivity {

    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    String transactionlist_url = "";
    HashMap<String, String> transactionHashmap;
    ArrayList<HashMap<String, String>> transactionArraylist;
    TextView totalearnedamount;
    TextView totalpendingamount;
    TextView pendingtransfers;
    RecyclerView transaction_recyclerView;
    Ced_MultiVendor_TransactionAdapter transactionAdapter;


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
        transactionlist_url = session.getBase_Url() + "vadvtransactionapi/transaction/lists";
        transactionArraylist = new ArrayList<HashMap<String, String>>();
        transactionHashmap = new HashMap<String, String>();

        if (connectionDetector.isConnectingToInternet()) {
            ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
            getLayoutInflater().inflate(R.layout.ced_multivendor_transactionlisting, content, true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);
            totalearnedamount = findViewById(R.id.MultiVendor_TotalEarnedAmount);
            totalpendingamount = findViewById(R.id.MultiVendor_TotalPendingAmount);
            pendingtransfers = findViewById(R.id.MultiVendor_PendingTransfers);
            transaction_recyclerView = findViewById(R.id.MultiVendor_transaction_recyclerlist);

            transactionHashmap.put("vendor_id", session.getVendorid());
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
                            totalearnedamount.setText(data.getString("total_earned_amount"));
                            totalpendingamount.setText(data.getString("pendingTransfer"));
                            pendingtransfers.setText(data.getString("pendingAmount"));

                            JSONArray transaction_list = data.getJSONArray("transaction_list");
                            for (int i = 0; i < transaction_list.length(); i++) {
                                JSONObject transactionobject = transaction_list.getJSONObject(i);
                                HashMap<String, String> transaction_data = new HashMap<String, String>();
                                transaction_data.put("paymentid", transactionobject.getString("payment_id"));
                                transaction_data.put("createdat", transactionobject.getString("created_at"));
                                transaction_data.put("paymentmethod", transactionobject.getString("payment_method"));
                                transaction_data.put("transactionid", transactionobject.getString("transaction_id"));
                                transaction_data.put("amount", transactionobject.getString("amount"));
                                transaction_data.put("adjustableamount", transactionobject.getString("adjustable_amount"));
                                transaction_data.put("netamount", transactionobject.getString("net_amount"));
                                transactionArraylist.add(transaction_data);
                            }
                            transactionAdapter = new Ced_MultiVendor_TransactionAdapter(Ced_MultiVendor_TransactionListing.this, transactionArraylist);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Ced_MultiVendor_TransactionListing.this);
                            transaction_recyclerView.setLayoutManager(mLayoutManager);
                            transaction_recyclerView.setAdapter(transactionAdapter);
                        } else {
                            Toast.makeText(Ced_MultiVendor_TransactionListing.this, data.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, Ced_MultiVendor_TransactionListing.this, "POST", transactionHashmap);
            response.execute(transactionlist_url);

        } else {
            Intent nointernet = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(nointernet);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }

    }
}
