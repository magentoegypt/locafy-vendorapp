package magentoegypt.locafy.vendor_settings;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import magentoegypt.locafy.base_app.Ced_MultiVendor_FontSetting;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ShippingMethodNew extends Ced_MultiVendor_NavigationActivity implements View.OnClickListener {
    Ced_MultiVendor_FontSetting fontSetting;
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    private LinearLayout MultiVendor_forpaymentmethoods;
    String currenturl = "";
    String savesetting = "";
    private static final String TAG = "ShippingMethodNew";
    private TextView MultiVendor_save;
    private JSONArray postData_param;
    private JSONObject post_obj;
    private HashMap<String, String> saveshippingdata;
    private JSONObject tablerate_obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewGroup content = (ViewGroup) findViewById(R.id.MultiVendor_frame_container);
        getLayoutInflater().inflate(R.layout.activity_shipping_method_new, content, true);

        postData_param = new JSONArray();
        saveshippingdata = new HashMap<>();
        post_obj = new JSONObject();

        tablerate_obj = new JSONObject();
        fontSetting = new Ced_MultiVendor_FontSetting();
        connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(getApplicationContext());
        MultiVendor_forpaymentmethoods = findViewById(R.id.MultiVendor_forpaymentmethoods);
        MultiVendor_save = findViewById(R.id.MultiVendor_save);
        MultiVendor_save.setOnClickListener(this);
        currenturl = session.getBase_Url() + "vstorepickupapi/setting/shippingMethods/" + "vendor_id/" + session.getVendorid() + "hashkey/" + session.getHahkey();
        savesetting = session.getBase_Url() + "vstorepickupapi/setting/saveShippingMethod" + "/hashkey/" + session.getHahkey();

        getShippingMethodData();
    }

    private void getShippingMethodData() {
        Ced_MultiVendor_ClientRequestResponse requestResponse = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                Log.d(TAG, "processFinish: " + output);
                JSONObject response = new JSONObject(output.toString()).getJSONObject("data");
                if (new JSONObject(output.toString()).getBoolean("success")) {
                    final JSONArray shipping_array = response.names();
                    TextView header;
                    TextView label_txt;
                    LinearLayout.LayoutParams params, spn_param;
                    View view;
                    Spinner spn_delivery;
                    ArrayList<String> values_list;
                    HashMap<String, String> values_hash;
                    String header_label[];

                    for (int i = 0; i < shipping_array.length(); i++) {
                        JSONArray shipping_methods = response.getJSONArray(shipping_array.get(i).toString());

                        header = new TextView(ShippingMethodNew.this);
                        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.setMargins(10, 10, 10, 10);
                        header.setTextColor(getResources().getColor(R.color.black));
                        header.setTextSize(20f);

                        header_label = shipping_array.get(i).toString().split("#");
                        header.setText(header_label[1]);
                        header.setTag(header_label[0]);

                        MultiVendor_forpaymentmethoods.addView(header);
                        for (int j = 0; j < shipping_methods.length(); j++) {
                            spn_param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            spn_param.setMargins(10, 20, 10, 10);
                            final JSONObject jsonObject = shipping_methods.getJSONObject(j);

                            if (jsonObject.getString("type").equalsIgnoreCase("select")) {
                                Log.d(TAG, "processFinish: " + jsonObject.getString("name"));
                                view = View.inflate(ShippingMethodNew.this, R.layout.dny_spn_layout, null);
                                spn_delivery = view.findViewById(R.id.spn_delivery);
                                label_txt = view.findViewById(R.id.label_txt);
                                label_txt.setText(jsonObject.getString("label"));
                                view.setLayoutParams(spn_param);
                                MultiVendor_forpaymentmethoods.addView(view);
                                JSONArray values_arr = jsonObject.getJSONArray("values");
                                values_list = new ArrayList<>();
                                values_hash = new HashMap<>();
                                ArrayList<String> res_value = new ArrayList<>();
                                for (int k = 0; k < values_arr.length(); k++) {
                                    JSONObject values_obj = values_arr.getJSONObject(k);
                                    spn_delivery.setTag(values_obj.getString("value"));
                                    values_list.add(values_obj.getString("label"));
                                    values_hash.put(values_obj.getString("label"), values_obj.getString("value"));
                                    res_value.add(values_obj.getString("value"));
                                }

                                Log.d(TAG, "getTagSpinner: " + spn_delivery.getTag());

                                if (values_list.size() > 0) {
                                    spn_delivery.setAdapter(new ArrayAdapter<String>(ShippingMethodNew.this, R.layout.simple_list_item_1, values_list));
                                    if (res_value.contains(jsonObject.getString("value"))) {
                                        spn_delivery.setSelection(res_value.indexOf(jsonObject.getString("value")));
                                    }

                                    final TextView finalHeader = header;
                                    final Spinner finalSpn_delivery = spn_delivery;
                                    final HashMap<String, String> finalValues_hash = values_hash;
                                    spn_delivery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                            JSONObject inner_obj = new JSONObject();
                                            try {

                                                if (finalHeader.getTag().toString().equalsIgnoreCase("tablerate")) {
                                                    tablerate_obj.put(jsonObject.getString("field_to_post"), finalValues_hash.get(finalSpn_delivery.getSelectedItem().toString()));
                                                    post_obj.put(finalHeader.getTag().toString(), tablerate_obj);
                                                }
                                                if (finalHeader.getTag().toString().equalsIgnoreCase("delhivery")) {
                                                    inner_obj.put(jsonObject.getString("field_to_post"), finalValues_hash.get(finalSpn_delivery.getSelectedItem().toString()));
                                                    post_obj.put(finalHeader.getTag().toString(), inner_obj);
                                                }

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
                        }
                    }
                    //   MultiVendor_forpaymentmethoods.addView(linearLayout);
                }
            }
        }, ShippingMethodNew.this);
        requestResponse.execute(currenturl);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() ==  R.id.MultiVendor_save){
            saveShippingMethod();
        }
    }

    private void saveShippingMethod() {
        postData_param.put(post_obj);
        saveshippingdata.put("vendor_id", session.getVendorid());
        saveshippingdata.put("hashkey", session.getHahkey());
        saveshippingdata.put("shipping_method_data", postData_param.toString());
        Ced_MultiVendor_ClientRequestResponse clientRequestResponse = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                Log.d(TAG, "processFinish: " + output);
                JSONObject jsonObject = new JSONObject(output.toString()).getJSONObject("data");
                if (jsonObject.getBoolean("success")) {
                    Toast.makeText(ShippingMethodNew.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    /* Will remove this latter (Abhishek) */
                    startActivity(new Intent(ShippingMethodNew.this, ShippingMethodNew.class));
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                    finish();
                } else {
                    Toast.makeText(ShippingMethodNew.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            }
        }, ShippingMethodNew.this, "POST", saveshippingdata);
        clientRequestResponse.execute(savesetting);
    }
}
