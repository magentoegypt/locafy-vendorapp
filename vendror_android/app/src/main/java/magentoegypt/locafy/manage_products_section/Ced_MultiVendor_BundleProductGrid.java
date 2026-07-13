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

package magentoegypt.locafy.manage_products_section;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import magentoegypt.locafy_constant.Ced_MultiVendor_GlobalVariables;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_UnAuthourizedRequestError;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.R;
import magentoegypt.locafy_constant.AppConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Ced_MultiVendor_BundleProductGrid extends Ced_MultiVendor_NavigationActivity {

    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    LinearLayout manageproductlist;
    String Currenturl;
    HashMap<String, String> dataforproducts;
    String Jstring;
    LinearLayout filtersection;
    Dialog listDialog;
    String datafilterjson = "";
    String current = "1";
    boolean load = true;
    String tabs;
    String product_id;
    String type;
    String attribute_set;
    String type_spinner;
    ArrayList<String> attributenames;
    HashMap<String, String> attributeidvalue;
    HashMap<String, String> attributevalueid;
    ArrayList<HashMap<String, String>> latestfiveorderlist;
    Ced_MultiVendor_BundleProductAdapter bundleProductAdapter;
    int index;
    TextView totalpro;
    Spinner sortby;
    TextView MultiVendor_save;
    private boolean dynamicPrice=false;
    private JSONObject selectedProducts=new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_bar_color));
        }
        latestfiveorderlist = new ArrayList<>();
        connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(getApplicationContext());
        dataforproducts = new HashMap<>();
        attributeidvalue = new HashMap<>();
        attributevalueid = new HashMap<>();
        attributenames = new ArrayList<>();
        if (connectionDetector.isConnectingToInternet()) {
            Currenturl = session.getBase_Url() + "vproductapi/vproducts/bundleGrid";
            ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
            getLayoutInflater().inflate(R.layout.ced_multivendor_activity_bundle_options_products, content, true);
            //getSupportActionBar().hide();
            manageproductlist = findViewById(R.id.MultiVendor_manageproductlist);
            filtersection = findViewById(R.id.MultiVendor_filtersection);
            sortby = findViewById(R.id.MultiVendor_sortby);
            totalpro = findViewById(R.id.MultiVendor_totalpro);
            MultiVendor_save = findViewById(R.id.MultiVendor_save);

            MultiVendor_save.setOnClickListener(view -> {
                AppConstant.lockButton(view);
                onBackPressed();
            });

        /*    filtersection.setOnClickListener(v -> {
                AppConstant.lockButton(v);
                showfilter();
            });*/

            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {

                if (getIntent().getStringExtra("filter") != null) {
                    datafilterjson = getIntent().getStringExtra("filter");
                    dataforproducts.put("filter", datafilterjson);
                }

                if (getIntent().hasExtra("dynamic_price")){
                    dynamicPrice=true;
                }
                else {
                    dynamicPrice=false;
                }

                tabs = getIntent().getStringExtra("tabs");
                product_id = getIntent().getStringExtra("product_id");
                type = getIntent().getStringExtra("type");
                attribute_set = getIntent().getStringExtra("attribute_set");
                type_spinner = getIntent().getStringExtra("type_spinner");
                index = getIntent().getIntExtra("index", 0);

                if (Ced_MultiVendor_GlobalVariables.bundle_selections_data.length()>0){
                    try {
                        JSONArray jsonArray=Ced_MultiVendor_GlobalVariables.bundle_selections_data.getJSONArray(String.valueOf(index));
                        for (int i=0;i<jsonArray.length();i++){
                            selectedProducts.put(jsonArray.getJSONObject(i).getString("product_id"),jsonArray.getJSONObject(i));
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.i("REpo", "onCreate_150: "+type_spinner);
                Log.i("REpo", "onCreate_151: "+index);
            }
            try {
                dataforproducts.put("vendor_id", session.getVendorid());
                dataforproducts.put("hashkey", session.getHahkey());
                dataforproducts.put("product_id", product_id);
                dataforproducts.put("type", type);
                if (attribute_set != null) {
                    dataforproducts.put("attribute_set", attribute_set);
                }
            }
            catch (Exception e) {
              e.printStackTrace();
            }

            sortby.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    current = sortby.getSelectedItem().toString();
                    if (manageproductlist.getChildCount() > 0) {
                        manageproductlist.removeAllViews();
                    }
                    try {
                        applydata(current);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else {
            Intent nointernet = new Intent(Ced_MultiVendor_BundleProductGrid.this, Ced_MultiVendor_NoInternetconnection.class);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(nointernet);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }

    }

    @Override
    protected void onResume() {
        Ced_MultiVendor_ConnectionDetector connectionDetector = new Ced_MultiVendor_ConnectionDetector(Ced_MultiVendor_BundleProductGrid.this);
        if (connectionDetector.isConnectingToInternet()) {

            super.onResume();
        } else {
            Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            super.onResume();
        }
    }

    private void applydata(String current) throws JSONException {
        Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(output -> {
            Jstring = output.toString();
            if (functionalityList.getExtensionAddon()) {
                JSONObject jsonObject = new JSONObject(Jstring);
                if (jsonObject.has("vendor_approved")) {
                    logout();
                }
                else {
                    String success = jsonObject.getJSONObject("data").getString("success");
                    if (success.equals("true")) {
//                        final String bundle_price_type = jsonObject.getJSONObject("data").getString("bundle_price_type");
                        JSONArray latest_order = jsonObject.getJSONObject("data").getJSONArray("products");
                        totalpro.setText(jsonObject.getJSONObject("data").getString("count"));

                        for (int l = 0; l < latest_order.length(); l++) {
                            View related_row = View.inflate(getApplicationContext(), R.layout.ced_multivendor_bundleproductlist, null);
                            CheckBox selectrelated = related_row.findViewById(R.id.MultiVendor_selectrelated);
                            TextView product_id = related_row.findViewById(R.id.MultiVendor_product_id);
                            TextView sku = related_row.findViewById(R.id.MultiVendor_sku);
                            TextView RegularPrice = related_row.findViewById(R.id.MultiVendor_RegularPrice);
                            TextView product_name = related_row.findViewById(R.id.MultiVendor_product_name);
                            LinearLayout pricesection = related_row.findViewById(R.id.MultiVendor_pricesection);
                            LinearLayout defaultqtysection = related_row.findViewById(R.id.MultiVendor_defaultqtysection);
                            EditText qty_ = related_row.findViewById(R.id.MultiVendor_qty);
                            EditText price = related_row.findViewById(R.id.MultiVendor_price);
                            Spinner price_type = related_row.findViewById(R.id.MultiVendor_type);
                            Spinner usercanchangeqty = related_row.findViewById(R.id.MultiVendor_usercanchangeqty);

                            JSONObject orderrow = latest_order.getJSONObject(l);

                            product_id.setText(orderrow.getString("product_id"));
                            sku.setText(orderrow.getString("sku"));
                            RegularPrice.setText(orderrow.getString("regular_price"));
                            product_name.setText(orderrow.getString("product_name"));
                            selectrelated.setText("#" + orderrow.getString("product_name"));


                            if (dynamicPrice) {
                                pricesection.setVisibility(View.GONE);
                            }
                            else {
                                pricesection.setVisibility(View.VISIBLE);
                            }

                            if (type_spinner != null) {
                                if (type_spinner.equals("select") || type_spinner.equals("radio")) {
                                    defaultqtysection.setVisibility(View.VISIBLE);
                                }
                                else {
                                    defaultqtysection.setVisibility(View.GONE);
                                }
                            }

                            try {

                               /* if (Ced_MultiVendor_GlobalVariables.bundle_selections_data.length() > 0) {
                                    if (Ced_MultiVendor_GlobalVariables.bundle_selections_data.has(String.valueOf(index))) {
                                        try {
                                            JSONArray jsonArray = Ced_MultiVendor_GlobalVariables.bundle_selections_data.getJSONArray(String.valueOf(index));
                                            Log.i("REpo", "applydata_404: "+jsonArray);
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject object = jsonArray.getJSONObject(i);
                                                selectrelated.setChecked(object.getString("product_id").equals(orderrow.getString("product_id")));
                                            }
                                        }
                                        catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    else {
                                        selectrelated.setChecked(false);
                                    }
                                }
                                else {
                                    selectrelated.setChecked(false);
                                }*/

                                if(selectedProducts.length()>0){
                                    if (selectedProducts.has(orderrow.getString("product_id"))){
                                        selectrelated.setChecked(true);
                                    }
                                }

                                selectrelated.setOnCheckedChangeListener((buttonView, isChecked) -> {

                                    if (isChecked) {
                                        qty_.setEnabled(false);
                                        if (Ced_MultiVendor_GlobalVariables.bundle_selections_data.length() > 0) {
                                            if (Ced_MultiVendor_GlobalVariables.bundle_selections_data.has(String.valueOf(index))) {
                                                try {
                                                    JSONArray jsonArray = Ced_MultiVendor_GlobalVariables.bundle_selections_data.getJSONArray(String.valueOf(index));
                                                    if (jsonArray.length() > 0) {
                                                        for (int i = 0; i < jsonArray.length(); i++) {
                                                            JSONObject jsonObject12 = jsonArray.getJSONObject(i);
                                                            if (!jsonObject12.getString("product_id").equals(product_id.getText().toString())) {
                                                                JSONObject object = new JSONObject();
                                                                try {
                                                                    object.put("selection_id", "");
                                                                    object.put("option_id", "");
                                                                    object.put("product_id", product_id.getText().toString());
                                                                    object.put("delete", "");
                                                                    if (pricesection.getVisibility()==View.VISIBLE) {
                                                                        object.put("selection_price_value", price.getText().toString());
                                                                        object.put("selection_price_type", price_type.getSelectedItemPosition());
                                                                    }
                                                                    else {
                                                                        object.put("selection_price_value", "");
                                                                        object.put("selection_price_type", "");
                                                                    }
                                                                    object.put("selection_qty", qty_.getText().toString());
                                                                    object.put("selection_can_change_qty", usercanchangeqty.getSelectedItemPosition());
                                                                    jsonArray.put(object);
                                                                }
                                                                catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        }
                                                        Ced_MultiVendor_GlobalVariables.bundle_selections_data.put(String.valueOf(index), jsonArray);
                                                    }
                                                    else {
                                                        JSONObject object = new JSONObject();
                                                        try {
                                                            object.put("selection_id", "");
                                                            object.put("option_id", "");
                                                            object.put("product_id", product_id.getText().toString());
                                                            object.put("delete", "");
                                                            if (pricesection.getVisibility()==View.VISIBLE) {
                                                                object.put("selection_price_value", price.getText().toString());
                                                                object.put("selection_price_type", price_type.getSelectedItemPosition());
                                                            }
                                                            else {
                                                                object.put("selection_price_value", "");
                                                                object.put("selection_price_type", "");
                                                            }
                                                            object.put("selection_qty", qty_.getText().toString());
                                                            object.put("selection_can_change_qty", usercanchangeqty.getSelectedItemPosition());
                                                            JSONArray array = new JSONArray();
                                                            array.put(object);
                                                            Log.i("ASd", array.toString());
                                                            Ced_MultiVendor_GlobalVariables.bundle_selections_data.put(String.valueOf(index), array);
                                                        }
                                                        catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }
                                                catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            else {
                                                JSONObject object = new JSONObject();
                                                try {
                                                    object.put("selection_id", "");
                                                    object.put("option_id", "");
                                                    object.put("product_id", product_id.getText().toString());
                                                    object.put("delete", "");

                                                    if (pricesection.getVisibility()==View.VISIBLE) {
                                                        object.put("selection_price_value", price.getText().toString());
                                                        object.put("selection_price_type", price_type.getSelectedItemPosition());
                                                    }
                                                    else {
                                                        object.put("selection_price_value", "");
                                                        object.put("selection_price_type", "");
                                                    }

                                                    object.put("selection_qty", qty_.getText().toString());
                                                    object.put("selection_can_change_qty", usercanchangeqty.getSelectedItemPosition());

                                                    JSONArray array = new JSONArray();
                                                    array.put(object);
                                                    Ced_MultiVendor_GlobalVariables.bundle_selections_data.put(String.valueOf(index), array);

                                                }
                                                catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                        else {
                                            JSONObject object = new JSONObject();
                                            try {
                                                object.put("selection_id", "");
                                                object.put("option_id", "");
                                                object.put("product_id", product_id.getText().toString());
                                                object.put("delete", "");

                                                if (pricesection.getVisibility()==View.VISIBLE) {
                                                    object.put("selection_price_value", price.getText().toString());
                                                    object.put("selection_price_type", price_type.getSelectedItemPosition());
                                                }
                                                else {
                                                    object.put("selection_price_value", "");
                                                    object.put("selection_price_type", "");
                                                }

                                                object.put("selection_qty", qty_.getText().toString());
                                                object.put("selection_can_change_qty", usercanchangeqty.getSelectedItemPosition());

                                                JSONArray array = new JSONArray();
                                                array.put(object);
                                                Ced_MultiVendor_GlobalVariables.bundle_selections_data.put(String.valueOf(index), array);
                                            }
                                            catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                    else {
                                        qty_.setEnabled(true);
                                        try {
                                            JSONArray array = new JSONArray();
                                            JSONArray jsonArray = Ced_MultiVendor_GlobalVariables.bundle_selections_data.getJSONArray(String.valueOf(index));
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject object = jsonArray.getJSONObject(i);
                                                if (!object.getString("product_id").equals(product_id.getText().toString())) {
                                                    array.put(object);
                                                }
                                            }
                                            Ced_MultiVendor_GlobalVariables.bundle_selections_data.put(String.valueOf(index), array);
                                        }
                                        catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    Log.i("REpo", "applydata_603:index "+index+" # "+Ced_MultiVendor_GlobalVariables.bundle_selections_data);
                                });

                                manageproductlist.addView(related_row);
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        JSONArray sets = jsonObject.getJSONObject("data").getJSONArray("sets").getJSONArray(0);
                        for (int i = 0; i < sets.length(); i++) {
                            JSONObject jsonObject1 = sets.getJSONObject(i);
//                                attributeidvalue.put(jsonObject1.getString("value"), jsonObject1.getString("id"));
//                                attributevalueid.put(jsonObject1.getString("id"), jsonObject1.getString("value"));
//                                if (!(attributenames.contains(jsonObject1.getString("value")))) {
//                                    attributenames.add(jsonObject1.getString("value"));
//                                }
                            attributeidvalue.put(jsonObject1.getString("label"), jsonObject1.getString("value"));
                            attributevalueid.put(jsonObject1.getString("value"), jsonObject1.getString("label"));
                            if (!(attributenames.contains(jsonObject1.getString("label")))) {
                                attributenames.add(jsonObject1.getString("label"));
                            }

                        }
                    }
                    else {
                        if (jsonObject.getJSONObject("data").has("sets")) {
                            JSONArray sets = jsonObject.getJSONObject("data").getJSONArray("sets").getJSONArray(0);
                            for (int i = 0; i < sets.length(); i++) {
                                JSONObject jsonObject1 = sets.getJSONObject(i);
//                                    attributeidvalue.put(jsonObject1.getString("value"), jsonObject1.getString("id"));
//                                    attributevalueid.put(jsonObject1.getString("id"), jsonObject1.getString("value"));
//                                    if (!(attributenames.contains(jsonObject1.getString("value")))) {
//                                        attributenames.add(jsonObject1.getString("value"));
//                                    }
                                attributeidvalue.put(jsonObject1.getString("label"), jsonObject1.getString("value"));
                                attributevalueid.put(jsonObject1.getString("value"), jsonObject1.getString("label"));
                                if (!(attributenames.contains(jsonObject1.getString("label")))) {
                                    attributenames.add(jsonObject1.getString("label"));
                                }

                            }
                        }

                        if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                            Log.i("attributeidvalue", "" + attributeidvalue);
                            Log.i("attributevalueid", "" + attributevalueid);
                        }
                        Toast.makeText(getApplicationContext(), jsonObject.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
                    }
                }
            }
            else {
                Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
            }
        }, Ced_MultiVendor_BundleProductGrid.this, "POST", dataforproducts);
        crr.execute(Currenturl + "/page/" + current);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
