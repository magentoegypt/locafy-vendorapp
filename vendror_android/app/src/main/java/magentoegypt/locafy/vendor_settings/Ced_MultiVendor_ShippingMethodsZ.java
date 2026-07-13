package magentoegypt.locafy.vendor_settings;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_UnAuthourizedRequestError;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ced_MultiVendor_ShippingMethodsZ extends Ced_MultiVendor_NavigationActivity {

    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    String countries_url = "";
    String shipping_method_url = "";
    String is_active = "";
    Spinner store_active;
    EditText store_title;
    EditText store_methodname;
    EditText shippingprice;
    TextView shipping_save;
    CheckBox countrytag;
    CheckBox country_checkbox;
    int id;

    LinearLayout countries_layout;
    HashMap<String, String> shippingHashmap;

    String country_code = "", state_code = "";
    String country_label, state_label = "";
    HashMap<String, String> mapcountrylabel;
    List<String> countrylabellist;
    List<String> statelabellist;
    List<String> countrycodelist;
    List<String> statecodelist;
    ArrayList<String> countrieslist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        countries_url = session.getBase_Url() + "vendorapi/index/getCountry";
        shipping_method_url = session.getBase_Url() + "vstorepickupapi/setting/shippingmethod";
        connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(getApplicationContext());
        shippingHashmap = new HashMap<String, String>();

        mapcountrylabel = new HashMap<String, String>();
        countrylabellist = new ArrayList<String>();
        statelabellist = new ArrayList<String>();
        countrycodelist = new ArrayList<String>();
        statecodelist = new ArrayList<String>();
        countrieslist = new ArrayList<String>();


        if (connectionDetector.isConnectingToInternet()) {
            ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
            getLayoutInflater().inflate(R.layout.ced_multivendor_shipping_methodsz, content, true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);
            store_active = findViewById(R.id.MultiVendor_store_active);
            store_title = findViewById(R.id.MultiVendor_store_title);
            store_methodname = findViewById(R.id.MultiVendor_store_methodname);
            shippingprice = findViewById(R.id.MultiVendor_store_shippingprice);
            shipping_save = findViewById(R.id.MultiVendor_shipping_method_save);
            countries_layout = findViewById(R.id.MultiVendor_countryselection);
            countrytag = findViewById(R.id.MultiVendor_countrytag);
            id = Resources.getSystem().getIdentifier("btn_check_holo_light", "drawable", "android");
            countrytag.setButtonDrawable(id);


            shippingHashmap.put("vendor_id", session.getVendorid());
            shippingHashmap.put("hashkey", getResources().getString(R.string.header));

            try {
                Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
                    @Override
                    public void processFinish(Object output) throws JSONException {
                        if (functionalityList.getExtensionAddon()) {
                            JSONObject jsonObject = new JSONObject(output.toString());
                            if (jsonObject.has("header") && jsonObject.getString("header").equals("false")) {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.url_not_found), Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            } else {
                                JSONArray jsonArray = jsonObject.getJSONArray("country");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    if (!(object.getString("value").isEmpty())) {
                                        countrycodelist.add(object.getString("value"));
                                        countrylabellist.add(object.getString("label"));
                                        mapcountrylabel.put(object.getString("label").trim(), object.getString("value"));
                                    }
                                }

                                for (Map.Entry<String, String> entry : mapcountrylabel.entrySet()) {
                                    String key = entry.getKey();
                                    String value = entry.getValue();

                                    View view = View.inflate(Ced_MultiVendor_ShippingMethodsZ.this, R.layout.shipping_country_checkboxes, null);
                                    country_checkbox = view.findViewById(R.id.MultiVendor_country_checkbox);
                                    country_checkbox.setButtonDrawable(id);
                                    country_checkbox.setText(key);

                                    country_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                                            if (isChecked) {
                                                countrieslist.add(mapcountrylabel.get(compoundButton.getText().toString()));
                                            } else {
                                                countrieslist.remove(mapcountrylabel.get(compoundButton.getText().toString()));
                                            }

                                        }
                                    });
                                    countries_layout.addView(view);
                                }
                            }

                        } else {
                            Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                }, Ced_MultiVendor_ShippingMethodsZ.this);
                crr.execute(countries_url);

            } catch (Exception e) {
                e.printStackTrace();
            }

/*            Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
                @Override
                public void processFinish(Object output) throws JSONException {
                    try {
                        String string = output.toString();
                        JSONObject object = new JSONObject(string);
                        JSONObject data = object.getJSONObject("data");
                        String success = data.getString("success");
                        if (success.equals("true")) {
                            JSONObject countries = data.getJSONObject("country");

                          final HashMap<String,String> map = new HashMap<String,String>();

                            Iterator iterator = countries.keys();
                            while(iterator.hasNext()){
                                final String key = (String)iterator.next(); //country keys
                                String value = countries.getString(key); //country values
                                map.put(value,key);

                                View view = View.inflate(Ced_MultiVendor_ShippingMethodsZ.this, R.layout.shipping_country_checkboxes, null);
                                country_checkbox = (CheckBox) view.findViewById(R.id.MultiVendor_country_checkbox);
                                country_checkbox.setButtonDrawable(id);
                                country_checkbox.setText(value);
                                country_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                                        if(isChecked) {

                                                countrieslist.add(map.get(compoundButton.getText().toString()));
                                        }
                                        else
                                            {
                                                countrieslist.remove(countrieslist.indexOf(map.get(compoundButton.getText().toString())));
                                            }

                                    }
                                });
                                countries_layout.addView(view);
                            }

                        } else {
                            String message = data.getString("message");
                            Toast.makeText(Ced_MultiVendor_ShippingMethodsZ.this, message, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, Ced_MultiVendor_ShippingMethodsZ.this, "POST", shippingHashmap);
            response.execute(countries_url);*/


            store_active.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (adapterView.getSelectedItem().toString().equals("Yes")) {
                        is_active = "1";
                    } else {
                        is_active = "0";
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


            countrytag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        countries_layout.setVisibility(View.VISIBLE);
                    } else {
                        countries_layout.setVisibility(View.GONE);
                    }
                }
            });


            shipping_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    shippingHashmap.put("title", store_title.getText().toString());
                    shippingHashmap.put("method_name", store_methodname.getText().toString());
                    shippingHashmap.put("store_price", shippingprice.getText().toString());
                    shippingHashmap.put("active", is_active);
                    shippingHashmap.put("allowed_country", countrieslist.toString().replace("[", "").replace("]", ""));
                    //Log.i("hello",""+countrieslist.toString());

                    Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
                        @Override
                        public void processFinish(Object output) throws JSONException {
                            try {
                                String string = output.toString();
                                JSONObject object = new JSONObject(string);
                                JSONObject data = object.getJSONObject("data");
                                String success = data.getString("success");
                                if (success.equals("true")) {
                                    Toast.makeText(Ced_MultiVendor_ShippingMethodsZ.this, data.getString("message"), Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(Ced_MultiVendor_ShippingMethodsZ.this, data.getString("message"), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, Ced_MultiVendor_ShippingMethodsZ.this, "POST", shippingHashmap);
                    response.execute(shipping_method_url);
                }
            });
        } else {
            Intent nointernet = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(nointernet);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }

    }

    private void getcountry() {
        try {
            Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
                @Override
                public void processFinish(Object output) throws JSONException {
                    if (functionalityList.getExtensionAddon()) {
                        JSONObject jsonObject = new JSONObject(output.toString());
                        if (jsonObject.has("header") && jsonObject.getString("header").equals("false")) {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.url_not_found), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else {
                            JSONArray jsonArray = jsonObject.getJSONArray("country");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                if (!(object.getString("value").isEmpty())) {
                                    countrycodelist.add(object.getString("value"));
                                    countrylabellist.add(object.getString("label"));
                                    mapcountrylabel.put(object.getString("label").trim(), object.getString("value"));
                                    /*if (getResources().getString(R.string.Enable_Log).equals("yes"))
                                    {
                                        Log.i("countrycodelist", "" + countrycodelist);
                                        Log.i("countrylabellist",""+countrylabellist);
                                        Log.i("mapcountrylabel",""+mapcountrylabel);
                                    }*/
                                }
                            }


                            /*if(mapcountrylabel.containsKey(country.getText().toString()))
                            {
                                country_code=mapcountrylabel.get(country.getText().toString());
                            }
                            if (getResources().getString(R.string.Enable_Log).equals("yes"))
                            {
                                Log.i("country_code_first",""+country_code);
                            }*/
                        }

                    } else {
                        Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
            }, Ced_MultiVendor_ShippingMethodsZ.this);
            crr.execute(countries_url);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
