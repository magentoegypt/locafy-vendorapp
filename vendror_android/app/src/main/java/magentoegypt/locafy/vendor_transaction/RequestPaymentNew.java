package magentoegypt.locafy.vendor_transaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_UnAuthourizedRequestError;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class RequestPaymentNew extends Ced_MultiVendor_NavigationActivity {

    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    int current = 1;
    String url;
    JSONObject response_data;
    HashMap<String, String> postdata;
    boolean isLoading = true;
    JSONArray list_array;
    LinearLayoutManager lm;
    private TextView mPendingAmt;
    private RecyclerView mRequestedList;
    private TextView mRequestedAmt;
    private TextView mCancelledAmt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
        getLayoutInflater().inflate(R.layout.activity_request_payment_new, content, true);
        mPendingAmt = findViewById(R.id.pending_amt);
        mRequestedList = findViewById(R.id.requested_list);
        mRequestedAmt = findViewById(R.id.requested_amt);
        mCancelledAmt = findViewById(R.id.cancelled_amt);
        list_array = new JSONArray();
        postdata = new HashMap<>();
        lm = new LinearLayoutManager(this);
        mRequestedList.setLayoutManager(lm);
        url = session.getBase_Url() + "vendorapi/vtransaction/paymentrequest";
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(getApplicationContext());
        request();
    }

    private void request() {
        postdata.put("vendor_id", session.getVendorid());
        Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                if (functionalityList.getExtensionAddon()) {
                    response_data = new JSONObject(output.toString()).getJSONObject("data");
                    mPendingAmt.setText(response_data.getString("pendingAmount"));
                    mCancelledAmt.setText(response_data.getString("cancelAmount"));
                    mRequestedAmt.setText(response_data.getString("requestAmount"));
                    if (response_data.getBoolean("success")) {
                        if (response_data.has("transactiondata")) {
                            list_array = response_data.getJSONArray("transactiondata");
                            RequestPaymentAdapterNew adapterNew = new RequestPaymentAdapterNew(RequestPaymentNew.this, response_data.getJSONArray("transactiondata"));
                            mRequestedList.setAdapter(adapterNew);
                            mRequestedList.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                @Override
                                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                                    super.onScrollStateChanged(recyclerView, newState);
                                }

                                @Override
                                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                    super.onScrolled(recyclerView, dx, dy);
                                    if (lm.getItemCount() > 9) {
                                        if (lm.findLastVisibleItemPosition() == lm.getItemCount() - 1 && isLoading) {
                                            current = current + 1;
                                            isLoading = false;

                                            scrollData(current);
                                        }
                                    }
                                }
                            });
                      /*      adapterNew.setOnBottomReachedListener(new OnBottomReachedListener() {
                                @Override
                                public void onBottomReached(int position) {
                                    if (isLoading) {
                                        Toast.makeText(RequestPaymentNew.this, "vnf", Toast.LENGTH_SHORT).show();
                                        current++;
                                        isLoading = false;
                                        scrollData(current, position);
                                    }
                                }
                            });*/


                        }
                    }
                } else {
                    Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }
            }
        }, RequestPaymentNew.this, "POST", postdata);
        response.execute(url + "/page/" + current);


    }

    private void scrollData(int page_num) {
        postdata.put("vendor_id", session.getVendorid());
        Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                if (functionalityList.getExtensionAddon()) {
                    response_data = new JSONObject(output.toString()).getJSONObject("data");
                    if (response_data.getBoolean("success")) {
                        mPendingAmt.setText(response_data.getString("pendingAmount"));
                        mCancelledAmt.setText(response_data.getString("cancelAmount"));
                        mRequestedAmt.setText(response_data.getString("requestAmount"));
                        if (response_data.has("transactiondata")) {
                            JSONArray jsonArray = response_data.getJSONArray("transactiondata");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                list_array.put(jsonObject);
                            }
                            RequestPaymentAdapterNew adapterNew = new RequestPaymentAdapterNew(RequestPaymentNew.this, list_array);
                            mRequestedList.setAdapter(adapterNew);
                            mRequestedList.scrollToPosition(lm.getItemCount() - 8);
                            //   mRequestedList.smoothScrollToPosition(position - 1);
                            isLoading = true;
                        } else {
                            isLoading = false;
                        }
                    }
                } else {
                    Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }
            }
        }, RequestPaymentNew.this, "POST", postdata);
        response.execute(url + "/page/" + page_num);
    }
}