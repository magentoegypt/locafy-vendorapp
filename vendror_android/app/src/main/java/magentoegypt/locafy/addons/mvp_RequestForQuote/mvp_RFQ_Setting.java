package magentoegypt.locafy.addons.mvp_RequestForQuote;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.LinearLayoutCompat;

import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_ClientRequestResponse;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;
import magentoegypt.locafy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class mvp_RFQ_Setting extends Ced_MultiVendor_NavigationActivity {

    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorSessionManagement vendorSessionManagement;
    String view_setting_url, update_url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        vendorSessionManagement = new Ced_MultiVendor_VendorSessionManagement(getApplicationContext());

        if (connectionDetector.isConnectingToInternet()) {
            ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
            getLayoutInflater().inflate(R.layout.activity_rfq_setting, content, true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);
            final LinearLayoutCompat linearLayoutCompat = findViewById(R.id.dyn_data);
            AppCompatButton save = findViewById(R.id.save);
            view_setting_url = session.getBase_Url()+ "vrfqapi/setting/index";
            update_url = session.getBase_Url() + "vrfqapi/setting/save";
            if (vendorSessionManagement.getvendorname() != null) {
                setname(vendorSessionManagement.getvendorname());
                setprofilepic(vendorSessionManagement.getvendorpic());
                changetitle("Request To Quote Settings");
            }
            final HashMap<String, String> postdata = new HashMap<>();
            postdata.put("hashkey", vendorSessionManagement.getHahkey());
            postdata.put("vendor_id", vendorSessionManagement.getVendorid());
            Ced_ClientRequestResponse response = new Ced_ClientRequestResponse(new AsyncResponse() {
                @Override
                public void processFinish(Object output) throws JSONException {
                    final JSONObject jsonObject = new JSONObject(output.toString());
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONObject fieldset = data.getJSONObject("fieldset");
                    JSONObject rfq_active = fieldset.getJSONObject("rfq-active");
                    View view = View.inflate(mvp_RFQ_Setting.this, R.layout.dny_spn_layout, null);
                    Spinner spn_delivery = view.findViewById(R.id.spn_delivery);
                    TextView label_txt = view.findViewById(R.id.label_txt);
                    label_txt.setText(rfq_active.getString("label"));
                    LinearLayoutCompat.LayoutParams spn_param = new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    spn_param.setMargins(10, 20, 10, 10);
                    view.setLayoutParams(spn_param);
                    linearLayoutCompat.addView(view);
                    JSONArray values_arr = rfq_active.getJSONArray("values");
                    ArrayList<String> values_list = new ArrayList<>();
                    final HashMap<String, String> values_hash = new HashMap<>();
                    final ArrayList<String> res_value = new ArrayList<>();
                    for (int k = 0; k < values_arr.length(); k++) {
                        JSONObject values_obj = values_arr.getJSONObject(k);
                        spn_delivery.setTag(values_obj.getString("value"));
                        values_list.add(values_obj.getString("label"));
                        values_hash.put(values_obj.getString("label"), values_obj.getString("value"));
                        res_value.add(values_obj.getString("value"));
                    }
                    if (values_list.size() > 0) {
                        spn_delivery.setAdapter(new ArrayAdapter<String>(mvp_RFQ_Setting.this, R.layout.simple_list_item_1, values_list));
                        if (res_value.contains(rfq_active.getString("value"))) {
                            spn_delivery.setSelection(res_value.indexOf(rfq_active.getString("value")));
                        }
                        final Spinner finalSpn_delivery = spn_delivery;
                        spn_delivery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                try {
                                    JSONObject spn_selection = new JSONObject();
                                    spn_selection.put("active", values_hash.get(finalSpn_delivery.getSelectedItem()));
                                    JSONObject rfq = new JSONObject();
                                    rfq.put("rfq", spn_selection);
                                    postdata.put("groups", rfq.toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    } else {
                        spn_delivery.setVisibility(View.GONE);
                        label_txt.setVisibility(View.GONE);
                    }
                }
            }, mvp_RFQ_Setting.this, "POST", postdata);
            response.execute(view_setting_url);
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Ced_ClientRequestResponse crr = new Ced_ClientRequestResponse(new AsyncResponse() {
                        @Override
                        public void processFinish(Object output) {
                            startActivity(new Intent(mvp_RFQ_Setting.this, mvp_RFQ_Setting.class));
                            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                            finish();
                        }
                    }, mvp_RFQ_Setting.this, "POST", postdata);
                    crr.execute(update_url);
                }
            });
        }
    }
}