package magentoegypt.locafy.addons.advance_transactions;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_UnAuthourizedRequestError;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MVP_AdvReqPayments extends Ced_MultiVendor_NavigationActivity {

    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    int current = 1;
    String url, req_pay_url;
    JSONObject response_data;
    HashMap<String, String> postdata;
    private AppCompatTextView pending_amt, total_earned_amount, total_pending_amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
        getLayoutInflater().inflate(R.layout.activity_adv_request_payment, content, true);
        pending_amt = findViewById(R.id.pending_amt);
        total_earned_amount = findViewById(R.id.total_earned_amount);
        total_pending_amount = findViewById(R.id.total_pending_amount);
        AppCompatButton req_for_pay = findViewById(R.id.req_for_pay);
        postdata = new HashMap<>();
        url = session.getBase_Url() + "vadvtransactionapi/transaction/view";
        req_pay_url = session.getBase_Url() + "vadvtransactionapi/transaction/request";
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(getApplicationContext());
        postdata.put("vendor_id", session.getVendorid());
        request();
        req_for_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
                    @Override
                    public void processFinish(Object output) throws JSONException {
                        response_data = new JSONObject(output.toString()).getJSONObject("data");
                        Toast.makeText(MVP_AdvReqPayments.this, response_data.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                }, MVP_AdvReqPayments.this, "POST", postdata);
                crr.execute(req_pay_url + "/page/" + current);
            }
        });
    }

    private void request() {
        Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                if (functionalityList.getExtensionAddon()) {
                    response_data = new JSONObject(output.toString()).getJSONObject("data");
                    if (response_data.getBoolean("success")) {
//                        pending_amt.setText(response_data.getString("pendingAmount"));
                        pending_amt.setText(response_data.getString("total_pending_requested_amount"));
                        total_earned_amount.setText(response_data.getString("total_earned_amount"));
                        total_pending_amount.setText(response_data.getString("total_pending_amount"));
                    }
                } else {
                    Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }
            }
        }, MVP_AdvReqPayments.this, "POST", postdata);
        response.execute(url + "/page/" + current);
    }
}