package magentoegypt.locafy.vendor_transaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import magentoegypt.locafy.addons.inventory.OutOfStockActivity;
import magentoegypt.locafy.base_app.Ced_Load_Language;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_UnAuthourizedRequestError;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;
import magentoegypt.locafy.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class mvp_adv_req_payments extends Ced_MultiVendor_NavigationActivity {
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    Ced_MultiVendor_VendorSessionManagement vendorSessionManagement;
    int current = 1;
    String url, req_pay_url;
    JSONObject response_data;
    private ConstraintLayout parent;
    HashMap<String, String> postdata;
    private AppCompatTextView pending_amt, total_earned_amount, total_pending_amount;
    private LinearLayout total_pending_amount_lin;
    private CardView card1, card2, card3;
    private AppCompatButton req_for_pay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
        getLayoutInflater().inflate(R.layout.activity_adv_request_payment_new, content, true);
        pending_amt = findViewById(R.id.pending_amt);
        total_earned_amount = findViewById(R.id.total_earned_amount);
        total_pending_amount = findViewById(R.id.total_pending_amount);
        total_pending_amount_lin = findViewById(R.id.total_pending_amount_lin);
        parent = findViewById(R.id.parent);
        card1 = findViewById(R.id.card1);
        card2 = findViewById(R.id.card2);
        card3 = findViewById(R.id.card3);
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullToRefresh.setRefreshing(true);
                Intent intent = new Intent(mvp_adv_req_payments.this, mvp_adv_req_payments.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
            }
        });
        ImageView raseedi = findViewById(R.id.raseedi);
        ImageView istaqkati = findViewById(R.id.istaqkati);
        ImageView mobiati = findViewById(R.id.mobiati);
        if (session.getStoreLocale() != null && session.getStoreLocale().equalsIgnoreCase("eg")) {
            raseedi.setScaleX(-1f);
            istaqkati.setScaleX(-1f);
            mobiati.setScaleX(-1f);
        }
        raseedi.setOnClickListener(view -> {
            Toast.makeText(mvp_adv_req_payments.this, R.string.the_total_amount_due_but_not_yet_paid, Toast.LENGTH_SHORT).show();
        });
        istaqkati.setOnClickListener(view -> {
            Toast.makeText(mvp_adv_req_payments.this, R.string.the_total_amount_you_ordered_is_still_waiting_for_payment, Toast.LENGTH_SHORT).show();
        });
        mobiati.setOnClickListener(view -> {
            Toast.makeText(mvp_adv_req_payments.this, R.string.the_total_amount_received_for_sales, Toast.LENGTH_SHORT).show();
        });
        req_for_pay = findViewById(R.id.req_for_pay);
        postdata = new HashMap<>();
        url = session.getBase_Url() + "vadvtransactionapi/transaction/view";
        req_pay_url = session.getBase_Url() + "vadvtransactionapi/transaction/request";
        vendorSessionManagement = new Ced_MultiVendor_VendorSessionManagement(getApplicationContext());
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(getApplicationContext());
        postdata.put("vendor_id", vendorSessionManagement.getVendorid());
        request();
        req_for_pay.setOnClickListener(v -> {
            Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
                @Override
                public void processFinish(Object output) throws JSONException {
                    response_data = new JSONObject(output.toString()).getJSONObject("data");
                    Toast.makeText(mvp_adv_req_payments.this, response_data.getString("message"), Toast.LENGTH_SHORT).show();
                }
            }, mvp_adv_req_payments.this, "POST", postdata);
            crr.execute(req_pay_url + "/page/" + current);
        });
    }

    private void request() {
        Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                if (functionalityList.getExtensionAddon()) {
                    response_data = new JSONObject(output.toString()).getJSONObject("data");
                    if (response_data.getBoolean("success")) {
                        if (response_data.has("show_refund_notice")){
                            if (response_data.getBoolean("show_refund_notice")){
                                Toast.makeText(getApplicationContext(), getString(R.string.the_payment_request_has_been_canceled_due_to_returned_requests_please_submit_a_new_payment_request), Toast.LENGTH_LONG).show();
                            }
                        }
                        String pendingAmount = response_data.getString("total_pending_amount");
                        if(!pendingAmount.contains("-")) {
                            String numberOnly = pendingAmount.replaceAll("[^0-9]", "");
                            int amount = Integer.parseInt(numberOnly);
                            if (amount > 0) {
                                total_pending_amount_lin.setBackgroundColor(getResources().getColor(R.color.transaction_color));
                                req_for_pay.setBackground(getDrawable(R.drawable.button_save_style));
                                req_for_pay.setEnabled(true);
                            }else{
                                req_for_pay.setEnabled(false);
                                req_for_pay.setBackground(getDrawable(R.drawable.button_rqst_payment));
                            }
                        }else if(pendingAmount.contains("-")) {
                            req_for_pay.setEnabled(false);
                            req_for_pay.setBackground(getDrawable(R.drawable.button_rqst_payment));
                        }
                        pending_amt.setText(pendingAmount);
                        total_earned_amount.setText(response_data.getString("total_earned_amount"));
                        total_pending_amount.setText(response_data.getString("total_pending_requested_amount"));
                    } else {
                        card1.setVisibility(View.GONE);
                        card2.setVisibility(View.GONE);
                        card3.setVisibility(View.GONE);
                        req_for_pay.setVisibility(View.GONE);
                        Toast.makeText(mvp_adv_req_payments.this, response_data.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                    parent.setVisibility(View.VISIBLE);
                } else {
                    Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }
            }
        }, mvp_adv_req_payments.this, "POST", postdata);
        response.execute(url + "/page/" + current);
    }
}