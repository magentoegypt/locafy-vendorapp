/*
 *
 *   Copyright/* *
 *             * CedCommerce
 *             *
 *             * NOTICE OF LICENSE
 *             *
 *             * This source file is subject to the End User License Agreement (EULA)
 *             * that is bundled with this package in the file LICENSE.txt.
 *             * It is also available through the world-wide-web at this URL:
 *             * http://cedcommerce.com/license-agreement.txt
 *             *
 *             * @category  Ced
 *             * @package   MultiVendor
 *             * @author    CedCommerce Core Team <connect@cedcommerce.com >
 *             * @copyright Copyright CEDCOMMERCE (http://cedcommerce.com/)
 *             * @license   http://cedcommerce.com/license-agreement.txt
 *
 *
 *
 */

package magentoegypt.locafy.vendor_settings;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import magentoegypt.locafy.R;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Ced_MultiVendor_ShippingMethod extends Ced_MultiVendor_NavigationActivity implements View.OnClickListener {
    private static final String TAG = "ShippingMethod";
    private static final String KEY_field_to_post = "field_to_post";
    private static final String KEY_label = "label";
    private static final String KEY_value = "value";
    private String ship_edt, from_edt, to_edt = "";
    private String currenturl, savesetting = "";
    private int checkboxDrawable;
    private AppCompatEditText ship, from, to;
    private AppCompatSpinner type;
    private AppCompatTextView message;
    private String[] selected_country = new String[]{};
    private JSONArray shippingZones;
    private TextView multiVendor_save;
    private LinearLayout MultiVendor_forpaymentmethoods, shipping_zones_layout;
    private JSONArray postData_param;
    private JSONObject post_obj;
    private HashMap<String, String> saveshippingdata;
    private int count = 0;
    private boolean shippingZone = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
        getLayoutInflater().inflate(R.layout.activity_shipping_method_new, content, true);

        postData_param = new JSONArray();
        shippingZones = new JSONArray();
        saveshippingdata = new HashMap<>();
        post_obj = new JSONObject();
        checkboxDrawable = Resources.getSystem().getIdentifier("btn_check_holo_light", "drawable", "android");
        MultiVendor_forpaymentmethoods = findViewById(R.id.MultiVendor_forpaymentmethoods);
        multiVendor_save = findViewById(R.id.MultiVendor_save);
        message = findViewById(R.id.message);
        multiVendor_save.setOnClickListener(this);
        currenturl = session.getBase_Url() + "vmultishippingApi/setting/shippingMethods/" + "vendor_id/" + session.getVendorid() + "/hashkey/" + session.getHahkey();
        savesetting = session.getBase_Url() + "vmultishippingApi/setting/saveShippingMethod" + "/hashkey/" + session.getHahkey();
        saveshippingdata.put("vendor_id", session.getVendorid());
        saveshippingdata.put("hashkey", session.getHahkey());
        getShippingMethodData();
    }

    private void getShippingMethodData() {
        Ced_MultiVendor_ClientRequestResponse requestResponse = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                Log.d(TAG, "processFinish: " + output);
                JSONObject data = new JSONObject(output.toString()).getJSONObject("data");
                if (data.has("success")) {
                    if (data.getString("success").equals("false")) {
                        message.setVisibility(View.VISIBLE);
                        message.setText(data.getString("message"));
                    }
                } else if (new JSONObject(output.toString()).getBoolean("success")) {
                    multiVendor_save.setVisibility(View.VISIBLE);
                    MultiVendor_forpaymentmethoods.setVisibility(View.VISIBLE);
                    final JSONArray shipping_array = data.names();
                    Log.e("frozen", "shipping_array= " + shipping_array);
                    TextView header, label_txt;
                    AppCompatButton add_zones;
                    LinearLayout.LayoutParams params, spn_param;
                    View view;
                    Spinner spn_delivery;
                    ArrayList<String> values_list;
                    HashMap<String, String> values_hash;
                    String[] header_label;
                    for (int i = 0; i < Objects.requireNonNull(shipping_array).length(); i++) {
                        final JSONObject method_name = new JSONObject();
                        final JSONObject method_attributes = new JSONObject();
                        JSONArray shipping_methods = data.getJSONArray(shipping_array.get(i).toString());
                        Log.e("frozen", "shipping_methods= " + shipping_methods);
                        header = new TextView(Ced_MultiVendor_ShippingMethod.this);
                        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.setMargins(20, 10, 20, 10);
                        header.setTextColor(getResources().getColor(R.color.AppTheme));
                        header.setTextSize(20f);
                        header_label = shipping_array.get(i).toString().split("#");
                        header.setText(header_label[1]);
                        header.setTag(header_label[0]);
                        header.setLayoutParams(params);
                        header.setTypeface(null, Typeface.BOLD);
                        MultiVendor_forpaymentmethoods.addView(header);
                        for (int j = 0; j < shipping_methods.length(); j++) {
                            spn_param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            spn_param.setMargins(10, 10, 10, 10);
                            final JSONObject jsonObject = shipping_methods.getJSONObject(j);
                            Log.e("frozen", "shipping_methods jsonObject= " + jsonObject);
                            TextView text_title;
                            switch (jsonObject.getString("type")) {
                                case "select":
                                    view = View.inflate(Ced_MultiVendor_ShippingMethod.this, R.layout.dny_spn_layout, null);
                                    spn_delivery = view.findViewById(R.id.spn_delivery);
                                    label_txt = view.findViewById(R.id.label_txt);
                                    label_txt.setText(jsonObject.getString(KEY_label));
                                    view.setLayoutParams(spn_param);
                                    MultiVendor_forpaymentmethoods.addView(view);
                                    JSONArray values_arr = jsonObject.getJSONArray("values");
                                    values_list = new ArrayList<>();
                                    values_hash = new HashMap<>();
                                    ArrayList<String> res_value = new ArrayList<>();
                                    for (int k = 0; k < values_arr.length(); k++) {
                                        JSONObject values_obj = values_arr.getJSONObject(k);
                                        spn_delivery.setTag(values_obj.getString(KEY_value));
                                        values_list.add(values_obj.getString(KEY_label));
                                        values_hash.put(values_obj.getString(KEY_label), values_obj.getString(KEY_value));
                                        res_value.add(values_obj.getString(KEY_value));
                                    }
                                    if (values_list.size() > 0) {
                                        spn_delivery.setAdapter(new ArrayAdapter<>(Ced_MultiVendor_ShippingMethod.this, R.layout.simple_list_item_1, values_list));
                                        if (res_value.contains(jsonObject.getString(KEY_value))) {
                                            spn_delivery.setSelection(res_value.indexOf(jsonObject.getString(KEY_value)));
                                        }

                                        final TextView finalHeader = header;
                                        final Spinner finalSpn_delivery = spn_delivery;
                                        final HashMap<String, String> finalValues_hash = values_hash;
                                        spn_delivery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                try {
                                                    method_attributes.put(jsonObject.getString(KEY_field_to_post), finalValues_hash.get(finalSpn_delivery.getSelectedItem().toString()));
                                                    method_name.put(finalHeader.getTag().toString(), method_attributes);
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
                                    break;
                                case "text":
                                    if (jsonObject.getString(KEY_field_to_post).equalsIgnoreCase("shipping_zones")) {
                                        shippingZone = true;
                                        view = View.inflate(Ced_MultiVendor_ShippingMethod.this, R.layout.layout_linear_with_btn, null);
                                        shipping_zones_layout = view.findViewById(R.id.shipping_zones);
                                        text_title = view.findViewById(R.id.type_title);
                                        text_title.setText(R.string.shipping_zone_txt);
                                        add_zones = view.findViewById(R.id.add_zones);
                                        add_zones.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view1) {
                                                try {
                                                    create_shipping_zones("", "", "", "");
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                        if (jsonObject.has(KEY_value)) {
                                            jsonObject.getString(KEY_value);
                                        }
                                        if (jsonObject.get(KEY_value) instanceof JSONArray) {
                                            for (int k = 0; k < jsonObject.getJSONArray(KEY_value).length(); k++) {
                                                JSONObject saved_zones = jsonObject.getJSONArray(KEY_value).getJSONObject(k);
                                                create_shipping_zones(saved_zones.has("price") ? saved_zones.getString("price") : "",
                                                        saved_zones.has("from") ? saved_zones.getString("from") : "",
                                                        saved_zones.has("to") ? saved_zones.getString("to") : "",
                                                        saved_zones.has("type") ? saved_zones.getString("type") : "");
                                            }
                                        }
                                        MultiVendor_forpaymentmethoods.addView(view);
                                    } else {
                                        view = View.inflate(Ced_MultiVendor_ShippingMethod.this, R.layout.view_text, null);
                                        text_title = view.findViewById(R.id.text_title);
                                        text_title.setText(jsonObject.getString(KEY_label));
                                        final AppCompatEditText text_data = view.findViewById(R.id.text_data);
                                        MultiVendor_forpaymentmethoods.addView(view);
                                        if (jsonObject.has(KEY_value)) {
                                            jsonObject.getString(KEY_value);
                                            text_data.setText(jsonObject.getString(KEY_value));
                                        }
                                        if (jsonObject.has("input_type") && jsonObject.getString("input_type").equalsIgnoreCase("int")) {
                                            text_data.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
                                        }
                                        final TextView finalHeader1 = header;
                                        text_data.addTextChangedListener(new TextWatcher() {
                                            @Override
                                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                            }

                                            @Override
                                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                                try {
                                                    method_attributes.put(jsonObject.getString(KEY_field_to_post), Objects.requireNonNull(text_data.getText()).toString());
                                                    method_name.put(finalHeader1.getTag().toString(), method_attributes);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            @Override
                                            public void afterTextChanged(Editable s) {

                                            }
                                        });
                                    }

                                    break;
                                case "multiselect":
                                    view = View.inflate(Ced_MultiVendor_ShippingMethod.this, R.layout.view_multiselect, null);
                                    text_title = view.findViewById(R.id.text_title);
                                    text_title.setText(jsonObject.getString(KEY_label));
                                    GridLayout multioptionsgrid = view.findViewById(R.id.multioptionsgrid);
                                    MultiVendor_forpaymentmethoods.addView(view);

                                    String saved_value = jsonObject.getString(KEY_value);
                                    if (!saved_value.equalsIgnoreCase("")) {
                                        if (saved_value.contains("\"")) {
                                            saved_value = saved_value.replace("\"", "");
                                        }
                                        if (saved_value.contains(",")) {
                                            selected_country = saved_value.split(",");
                                        } else {
                                            selected_country = new String[]{saved_value};
                                        }
                                    }
                                    JSONArray multiOptionsArray = jsonObject.getJSONArray("values");
                                    final ArrayList<String> selectedOptions = new ArrayList<>();
                                    for (int p = 0; p < multiOptionsArray.length(); p++) {
                                        final CheckBox option = new CheckBox(Ced_MultiVendor_ShippingMethod.this);
                                        option.setButtonDrawable(checkboxDrawable);
                                        option.setTag(multiOptionsArray.getJSONObject(p).getString(KEY_value));
                                        option.setText(multiOptionsArray.getJSONObject(p).getString(KEY_label));
                                        for (int m = 0; m < selected_country.length; m++) {
                                            if (selected_country[m].contains("[")) {
                                                selected_country[m] = selected_country[m].replace("[", "");
                                            }
                                            if (selected_country[m].contains("]")) {
                                                selected_country[m] = selected_country[m].replace("]", "");
                                            }
                                            if (selected_country[m].equalsIgnoreCase(multiOptionsArray.getJSONObject(p).getString(KEY_value))) {
                                                selectedOptions.add(selected_country[m]);
                                                option.setChecked(true);
                                            }
                                        }
                                        final TextView finalHeader2 = header;
                                        option.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                            @Override
                                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                try {
                                                    if (option.isChecked()) {
                                                        if (!selectedOptions.contains(option.getTag().toString()))
                                                            selectedOptions.add(option.getTag().toString());
                                                    } else {
                                                        selectedOptions.remove(option.getTag().toString());
                                                    }
                                                    method_attributes.put(jsonObject.getString(KEY_field_to_post), new JSONArray(selectedOptions));
                                                    method_name.put(finalHeader2.getTag().toString(), method_attributes);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                        multioptionsgrid.addView(option);
                                    }
                                    break;
                            }
                        }
                        method_name.put(header.getTag().toString(), method_attributes);
                        postData_param.put(method_name);
                    }
                }
            }
        }, Ced_MultiVendor_ShippingMethod.this);
        requestResponse.execute(currenturl);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.MultiVendor_save) {
            try {
                saveShippingMethod();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void create_shipping_zones(final String price, final String from_text, final String to_text, String type_text) throws JSONException {
        Log.e("frozen", "saved_zones price = " + price + " from_text= " + from_text + " to_text= " + to_text + " type_text= " + type_text);
        View layout = View.inflate(Ced_MultiVendor_ShippingMethod.this, R.layout.layout_shipping_zone, null);
        ship = layout.findViewById(R.id.ship);
        from = layout.findViewById(R.id.from);
        to = layout.findViewById(R.id.to);
        type = layout.findViewById(R.id.type);
        final AppCompatButton remove = layout.findViewById(R.id.remove);
        remove.setTag(String.valueOf(count));
        ship.setText(price);
        from.setText(from_text);
        to.setText(to_text);
        if (type_text.equalsIgnoreCase("Fixed"))
            type.setSelection(0);
        else if (type_text.equalsIgnoreCase("Per / metric"))
            type.setSelection(1);

        final JSONObject shippingZoneObj = new JSONObject();
        shippingZoneObj.put("price", price);
        shippingZoneObj.put("from", from_text);
        shippingZoneObj.put("to", to_text);
        shipping_zones_layout.addView(layout);
        count++;
        ship.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    ship_edt = Objects.requireNonNull(ship.getText()).toString();
                    shippingZoneObj.put("price", ship_edt);
                    Log.e("frozen", "ship_edt== " + ship_edt + " shippingZoneObj ==" + shippingZoneObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        from.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    from_edt = Objects.requireNonNull(Ced_MultiVendor_ShippingMethod.this.from.getText()).toString();
                    shippingZoneObj.put("from", from_edt);
                    Log.e("frozen", "from_edt== " + from_edt + " shippingZoneObj ==" + shippingZoneObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        to.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    to_edt = Objects.requireNonNull(Ced_MultiVendor_ShippingMethod.this.to.getText()).toString();
                    shippingZoneObj.put("to", to_edt);
                    Log.e("frozen", "to_edt== " + to_edt + " shippingZoneObj ==" + shippingZoneObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    shippingZoneObj.put("type", Ced_MultiVendor_ShippingMethod.this.type.getSelectedItem().toString());
                    Log.e("frozen", "type== " + Ced_MultiVendor_ShippingMethod.this.type.getSelectedItem() + " shippingZoneObj ==" + shippingZoneObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConstraintLayout parent = (ConstraintLayout) remove.getParent();
                Log.e("REpo", "shipping_zone_id= " + remove.getTag().toString());
                try {
                    for (int i = 0; i < shippingZones.length(); i++) {
                        JSONObject obj_removed = shippingZones.getJSONObject(Integer.parseInt(remove.getTag().toString()));
                        JSONObject current_object = shippingZones.getJSONObject(i);
                        Log.e("REpo", "image_id= " + remove.getTag().toString());
                        Log.e("REpo", "current_object = " + current_object);
                        Log.e("REpo", "obj_removed = " + obj_removed);
                        if (current_object.getString("price").equals(obj_removed.getString("price")))
                            if (current_object.getString("from").equals(obj_removed.getString("from")))
                                if (current_object.getString("to").equals(obj_removed.getString("to")))
                                    if (current_object.getString("type").equals(obj_removed.getString("type")))
                                        shippingZones.remove(i);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                parent.setVisibility(View.GONE);
            }
        });
        shippingZones.put(shippingZoneObj);
        Log.e("REpo", "shippingZones= " + shippingZones);
    }

    private void saveShippingMethod() throws JSONException {
        if (shippingZone)
            post_obj.put("shipping_zones", shippingZones);
        Log.w("frozen", "postData_param= " + postData_param);
        saveshippingdata.put("shipping_method_data", postData_param.toString());
        Log.e("frozen", "saveshippingdata== " + saveshippingdata.toString());
        Ced_MultiVendor_ClientRequestResponse clientRequestResponse = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                JSONObject jsonObject = new JSONObject(output.toString()).getJSONObject("data");
                if (jsonObject.getBoolean("success")) {
                    Toast.makeText(Ced_MultiVendor_ShippingMethod.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Ced_MultiVendor_ShippingMethod.this, Ced_MultiVendor_ShippingMethod.class));
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                    finish();
                } else {
                    Toast.makeText(Ced_MultiVendor_ShippingMethod.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            }
        }, Ced_MultiVendor_ShippingMethod.this, "POST", saveshippingdata);
        clientRequestResponse.execute(savesetting);
    }
}