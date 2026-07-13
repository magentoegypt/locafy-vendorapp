package magentoegypt.locafy.vendor_deals;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import magentoegypt.locafy.base_app.Ced_MultiVendor_FontSetting;
import magentoegypt.locafy.vendor_dashboard.Ced_MultiVendor_VendorDashboard;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_UnAuthourizedRequestError;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.R;
import magentoegypt.locafy_constant.AppConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Ced_MultiVendor_Deal_Settings extends Ced_MultiVendor_NavigationActivity {
    HashMap<String, String> postdata;
    Button submit_message;
    EditText edt_message;
    TextView deal_meesage_title, deal_title;
    String message_save_url = "";
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    Ced_MultiVendor_FontSetting fontSetting;
    TextView deal_status_title;
    Spinner deal_status_spinner;
    String status = "", setting_id = "", message_view_url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
        getLayoutInflater().inflate(R.layout.ced_multivendor_deal_settings, content, true);
        message_save_url = session.getBase_Url() + "vdealapi/setting/save";
        message_view_url = session.getBase_Url() + "vdealapi/setting/view";
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(Ced_MultiVendor_Deal_Settings.this);
        fontSetting = new Ced_MultiVendor_FontSetting();
        submit_message = findViewById(R.id.deal_message_submit_button);
        edt_message = findViewById(R.id.edt_deal_message);
        deal_meesage_title = findViewById(R.id.deal_meesage_title);
        deal_title = findViewById(R.id.deal_title);
        deal_status_title = findViewById(R.id.deal_status_title);
        deal_status_spinner = findViewById(R.id.deal_status_spinner);
        postdata = new HashMap<>();
        postdata.put("vendor_id", session.getVendorid());
        postdata.put("hashkey", session.getHahkey());

        fontSetting.setfontforEditText(edt_message, "Roboto-Regular.ttf", getApplicationContext());
        fontSetting.setFontforTextviews(deal_meesage_title, "Roboto-Bold.ttf", getApplicationContext());
        fontSetting.setFontforTextviews(deal_title, "Roboto-Bold.ttf", getApplicationContext());
        fontSetting.setfontforButtons(submit_message, "Roboto-Medium.ttf", getApplicationContext());

        view_data_message();

        deal_status_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (deal_status_spinner.getSelectedItemPosition() == 1) {
                    postdata.put("status", "enable");
                } else if (deal_status_spinner.getSelectedItemPosition() == 2) {
                    postdata.put("status", "disable");
                } else {
                    postdata.put("status", status);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        submit_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppConstant.lockButton(view);
                if (!edt_message.getText().toString().isEmpty()) {
                    postdata.put("deal_message", edt_message.getText().toString());
                    postdata.put("setting_id", setting_id);
                    postdata.put("status", "1");
                    save_data_message();
                } else {
                    Toast.makeText(Ced_MultiVendor_Deal_Settings.this, "Please Fill Message", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void view_data_message() {
        Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                if (functionalityList.getVendor_Deals()) {
                    JSONObject jsonObject = new JSONObject(output.toString());
                    JSONObject jsonObject_data = jsonObject.getJSONObject("data");
                    if (jsonObject_data.getString("success").equals("true")) {
                        status = jsonObject_data.getString("setting_status");
                        setting_id = jsonObject_data.getString("setting_id");
                        if (status.equals("enable")) {
                            deal_status_spinner.setSelection(1);
                        } else if (status.equals("disable")) {
                            deal_status_spinner.setSelection(2);
                        } else {
                            deal_status_spinner.setSelection(0);
                        }
                        edt_message.setText(jsonObject_data.getString("message"));
                        edt_message.setSelection(edt_message.getText().length());
                    } else {
                        Toast.makeText(Ced_MultiVendor_Deal_Settings.this, "" + jsonObject_data.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }
            }


        }, Ced_MultiVendor_Deal_Settings.this, "POST", postdata);
        response.execute(message_view_url);
    }

    public void save_data_message() {
        Log.i("vaibhavtest", "deals parms" + postdata);
        Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                if (functionalityList.getVendor_Deals()) {
                    Log.i("9044_Response", "" + output);
                    JSONObject jsonObject = new JSONObject(output.toString());
                    JSONObject jsonObject_data = jsonObject.getJSONObject("data");
                    if (jsonObject_data.getString("success").equals("true")) {
                        Toast.makeText(Ced_MultiVendor_Deal_Settings.this, "" + jsonObject_data.getString("message"), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Ced_MultiVendor_Deal_Settings.this, Ced_MultiVendor_VendorDashboard.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        Toast.makeText(Ced_MultiVendor_Deal_Settings.this, "" + jsonObject_data.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }
            }


        }, Ced_MultiVendor_Deal_Settings.this, "POST", postdata);
        response.execute(message_save_url);
    }
}