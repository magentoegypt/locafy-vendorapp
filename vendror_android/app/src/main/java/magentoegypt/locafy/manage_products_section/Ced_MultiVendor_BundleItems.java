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

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import magentoegypt.locafy_constant.Ced_MultiVendor_GlobalVariables;
import magentoegypt.locafy.R;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_UnAuthourizedRequestError;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Ced_MultiVendor_BundleItems extends Ced_MultiVendor_NavigationActivity {
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    RelativeLayout addbundleptions;
    TextView save;
    Spinner ShipBundleItems;
    LinearLayout bundleoptions;
    LinearLayout preselectedbundleoptions;
    ScrollView mainvendorscroll;
    String tabs;
    String product_id;
    String type;
    String attribute_set;
    JSONObject bundle_options_data;
    JSONObject prebundle_options_data;
    HashMap<String, String> dataforproducts;
    HashMap<String, String> getBundleDataParams;
    String updateurl;
    String getBundleDataUrl;
    JSONObject prebundle_selections_data;
    String selected_websitetopost;
    private boolean dynamicPrice=false;
    private String shipmentType="0";

    HashMap<String,String> shipBundleMap=new HashMap<>();
    HashMap<String,String> selection_price_typeMap=new HashMap<>();
    HashMap<String,String> input_typeMap=new HashMap<>();
    ArrayList<String> shipBundleMapList=new ArrayList<>();
    ArrayList<String> selection_price_typeMapList=new ArrayList<>();
    ArrayList<String> input_typeMapList=new ArrayList<>();
    ArrayList<String> input_typeMapValueList=new ArrayList<>();
    private int shipmentTypePosition=0;


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
        dataforproducts = new HashMap<>();
        getBundleDataParams = new HashMap<>();
        prebundle_selections_data = new JSONObject();
        selected_websitetopost = getIntent().getStringExtra("selectedwebsite");
        if (connectionDetector.isConnectingToInternet()) {
            try {
                setname(session.getvendorname());
                setprofilepic(session.getvendorpic());
                setTitle(getString(R.string.bundle_items));
                ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
                getLayoutInflater().inflate(R.layout.ced_multivendor_bundle, content, true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);
                addbundleptions = findViewById(R.id.MultiVendor_addbundleptions);
                bundleoptions = findViewById(R.id.MultiVendor_bundleoptions);
                preselectedbundleoptions = findViewById(R.id.MultiVendor_preselectedbundleoptions);
                ShipBundleItems = findViewById(R.id.MultiVendor_ShipBundleItems);
                mainvendorscroll = findViewById(R.id.MultiVendor_mainvendorscroll);
                save = findViewById(R.id.MultiVendor_save);
                bundle_options_data = new JSONObject();
                prebundle_options_data = new JSONObject();
                updateurl = session.getBase_Url() + "vendorapi/vproducts/update";
                getBundleDataUrl = session.getBase_Url()+ "vproductapi/vproducts/bundleData";
                save.setOnClickListener(v -> {

                    try {
                        JSONObject jsonObject1 = new JSONObject();

                        if (bundleoptions.getChildCount() > 0) {
                            for (int i = 0; i < bundleoptions.getChildCount(); i++) {
                                CardView cardView = (CardView) bundleoptions.getChildAt(i);
                                LinearLayout layout = (LinearLayout) cardView.getChildAt(0);
                                LinearLayout linearLayout = (LinearLayout) layout.getChildAt(0);
                                LinearLayout tittlr_linear = (LinearLayout) linearLayout.getChildAt(1);
                                LinearLayout type_linear = (LinearLayout) linearLayout.getChildAt(2);
                                LinearLayout required_linear = (LinearLayout) linearLayout.getChildAt(3);
                                LinearLayout tposition_linear = (LinearLayout) linearLayout.getChildAt(4);
                                EditText tittle = (EditText) tittlr_linear.getChildAt(1);
                                Spinner Spinner_type = (Spinner) type_linear.getChildAt(1);
                                Spinner required = (Spinner) required_linear.getChildAt(1);
                                EditText position = (EditText) tposition_linear.getChildAt(1);
                                JSONObject object = new JSONObject();
                                try {
                                    object.put("title", tittle.getText().toString());
                                    object.put("option_id", "");
                                    object.put("delete", "");
                                    object.put("position", "");
                                    object.put("type", input_typeMap.get(Spinner_type.getSelectedItem().toString()));
                                    object.put("required", required.getSelectedItemPosition());

                                    if (Ced_MultiVendor_GlobalVariables.bundle_selections_data.has(String.valueOf(i))) {
                                        bundle_options_data.put(String.valueOf(i), object.toString());
                                    }

                                }
                                catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        else {
                            Log.i("REpo", "onCreate_156: "+bundleoptions.getChildCount() );
                        }

                        if (preselectedbundleoptions.getChildCount() > 0) {
                            for (int j = 0; j < preselectedbundleoptions.getChildCount(); j++) {
                                CardView cardView = (CardView) preselectedbundleoptions.getChildAt(j);
                                TextView option_id = (TextView) cardView.getChildAt(1);
                                TextView bundle_price_type = (TextView) cardView.getChildAt(2);
                                LinearLayout parentOptionLayout = (LinearLayout) cardView.getChildAt(0);

                                LinearLayout linearLayout = (LinearLayout) parentOptionLayout.getChildAt(0);
                                LinearLayout tittlr_linear = (LinearLayout) linearLayout.getChildAt(1);
                                LinearLayout type_linear = (LinearLayout) linearLayout.getChildAt(2);
                                LinearLayout required_linear = (LinearLayout) linearLayout.getChildAt(3);
                                LinearLayout tposition_linear = (LinearLayout) linearLayout.getChildAt(4);
                                EditText tittle = (EditText) tittlr_linear.getChildAt(1);
                                Spinner Spinner_type = (Spinner) type_linear.getChildAt(1);
                                Spinner required = (Spinner) required_linear.getChildAt(1);
                                EditText position = (EditText) tposition_linear.getChildAt(1);

                                CheckBox checkBox=(CheckBox)linearLayout.getChildAt(0);

                                if (checkBox.isChecked()) {
                                    JSONObject optionObject = new JSONObject();
                                    optionObject.put("title", tittle.getText().toString());
                                    optionObject.put("option_id", option_id.getText().toString());
                                    optionObject.put("delete", "");
                                    optionObject.put("position", "");
                                    optionObject.put("type", input_typeMap.get(Spinner_type.getSelectedItem().toString()));
                                    optionObject.put("required", required.getSelectedItemPosition());
                                    prebundle_options_data.put(String.valueOf(j), optionObject.toString());

                                    LinearLayout selectedoptions = (LinearLayout) parentOptionLayout.getChildAt(1);
                                    JSONArray jsonArray = new JSONArray();
                                    for (int i = 0; i < selectedoptions.getChildCount(); i++) {
                                        RelativeLayout relativeLayout = (RelativeLayout) selectedoptions.getChildAt(i);
                                        TextView selection_id = (TextView) relativeLayout.getChildAt(3);
                                        CardView card_selection = (CardView) relativeLayout.getChildAt(0);

                                        LinearLayout layout1 = (LinearLayout) card_selection.getChildAt(0);
                                        CheckBox checkBox2=(CheckBox)layout1.getChildAt(0);
                                        LinearLayout layout2 = (LinearLayout) layout1.getChildAt(1);
                                        LinearLayout price_section_linear = (LinearLayout) layout1.getChildAt(2);
                                        EditText price = (EditText) price_section_linear.getChildAt(1);
                                        Spinner priceType = (Spinner) price_section_linear.getChildAt(3);
                                        LinearLayout default_section_linear = (LinearLayout) layout1.getChildAt(3);
                                        Spinner usercanchangeqty = (Spinner) default_section_linear.getChildAt(1);

                                        LinearLayout layout3 = (LinearLayout) layout2.getChildAt(2);
                                        TextView selected_product_id = (TextView) layout3.getChildAt(0);
                                        EditText qty = (EditText) layout3.getChildAt(4);

                                        if (checkBox2.isChecked()) {
                                            JSONObject object_selection = new JSONObject();
                                            object_selection.put("selection_id", selection_id.getText().toString());
                                            object_selection.put("option_id", option_id.getText().toString());
                                            object_selection.put("product_id", selected_product_id.getText().toString());
                                            object_selection.put("delete", "");
                                            object_selection.put("selection_price_value", price.getText().toString());
                                            object_selection.put("selection_price_type", priceType.getSelectedItemPosition());

                                            object_selection.put("selection_qty", qty.getText().toString());
                                            object_selection.put("selection_can_change_qty", usercanchangeqty.getSelectedItemPosition());
                                            jsonArray.put(object_selection);
                                        }
                                    }
                                    prebundle_selections_data.put(String.valueOf(j), jsonArray);
                                }

                            }
                        }
                        else {
                            Log.i("REpo", "onCreate_236: "+preselectedbundleoptions.getChildCount());
                        }

                        jsonObject1.put("prebundle_options_data", prebundle_options_data);
                        jsonObject1.put("prebundle_selections_data", prebundle_selections_data);
                        jsonObject1.put("bundle_options_data", bundle_options_data);
                        jsonObject1.put("bundle_selections_data",Ced_MultiVendor_GlobalVariables.bundle_selections_data);

                        if (jsonObject1.length()>0) {
                            dataforproducts.put("bundle_options", jsonObject1.toString());
                            dataforproducts.put("shipment_type", ""+shipBundleMap.get(ShipBundleItems.getSelectedItem().toString()));
                        }
                        else {
                            Log.i("REpo", "onCreate_257: "+jsonObject1);
                        }

                        dataforproducts.put("vendor_id", session.getVendorid());
                        dataforproducts.put("hashkey", session.getHahkey());
                        dataforproducts.put("product_id", product_id);
                        dataforproducts.put("websites", selected_websitetopost);
//                        dataforproducts.put("type", type);
//                        dataforproducts.put("attribute_set", attribute_set);

                        Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(output -> {
                            String Jstring = output.toString();
                            if (functionalityList.getExtensionAddon()) {
                                JSONObject jsonObject = new JSONObject(Jstring);
                                if (jsonObject.has("vendor_approved")) {
                                    logout();
                                } else {
                                    String success = jsonObject.getJSONObject("data").getString("success");
                                    if (success.equals("true")) {
                                        Ced_MultiVendor_GlobalVariables globalVariables = new Ced_MultiVendor_GlobalVariables();
                                        globalVariables.clearallvalues();
                                        Intent related = new Intent(getApplicationContext(), Ced_MultiVendor_ManageProducts.class);
                                        related.putExtra("Navigation", "productcreate");
                                        startActivity(related);
                                        overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                                    } else {
                                        Toast.makeText(getApplicationContext(), jsonObject.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
                                    }
                                }
                            } else {
                                Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                            }
                        }, Ced_MultiVendor_BundleItems.this, "POST", dataforproducts);
                        crr.execute(updateurl);

                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }

                });


                Bundle bundle = getIntent().getExtras();
                if (bundle != null) {
                    tabs = getIntent().getStringExtra("tabs");
                    product_id = getIntent().getStringExtra("product_id");
                    type = getIntent().getStringExtra("type");
                    attribute_set = getIntent().getStringExtra("attribute_set");
                }
                getBundleDataParams.put("vendor_id", session.getVendorid());
                getBundleDataParams.put("hashkey", session.getHahkey());
                getBundleDataParams.put("product_id", product_id);
                Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(output -> {
                    String Jstring = output.toString();
                    if (functionalityList.getExtensionAddon()) {
                        JSONObject jsonObject = new JSONObject(Jstring);
                        if (jsonObject.has("vendor_approved")) {
                            logout();
                        }
                        else {
                            JSONObject dataObj=jsonObject.getJSONObject("data");
                            String success = dataObj.getString("success");

                            if (success.equals("true")) {

                                if (dataObj.has("dynamic_price_type")&&dataObj.getString("dynamic_price_type").equals("1")){
                                    dynamicPrice=false;
                                }
                                else {
                                    dynamicPrice=true;
                                }

                                if (dataObj.has("shipment_type")){
                                    shipmentType=dataObj.getString("shipment_type");
                                }

                                if (dataObj.has("ship_bundle_items")){
                                    JSONArray ship_bundle_items=dataObj.getJSONArray("ship_bundle_items");
                                    for(int i=0;i<ship_bundle_items.length();i++){
                                        shipBundleMap.put(ship_bundle_items.getJSONObject(i).getString("label"),ship_bundle_items.getJSONObject(i).getString("value"));
                                        shipBundleMapList.add(ship_bundle_items.getJSONObject(i).getString("label"));
                                        if (ship_bundle_items.getJSONObject(i).getString("value").equals(shipmentType)) {
                                            shipmentTypePosition = i;
                                        }
                                    }

                                    ArrayAdapter arrayAdapter=new ArrayAdapter(Ced_MultiVendor_BundleItems.this,R.layout.spinner_item,shipBundleMapList);
                                    ShipBundleItems.setAdapter(arrayAdapter);
                                    ShipBundleItems.setSelection(shipmentTypePosition);
                                }

                                if (dataObj.has("selection_price_type")){
                                    JSONArray selection_price_type=dataObj.getJSONArray("selection_price_type");
                                    for(int i=0;i<selection_price_type.length();i++){
                                        selection_price_typeMap.put(selection_price_type.getJSONObject(i).getString("label"),selection_price_type.getJSONObject(i).getString("value"));
                                        selection_price_typeMapList.add(selection_price_type.getJSONObject(i).getString("label"));
                                    }
                                }

                                if (dataObj.has("input_type")){
                                    JSONArray input_type=dataObj.getJSONArray("input_type");
                                    for(int i=0;i<input_type.length();i++){
                                        input_typeMap.put(input_type.getJSONObject(i).getString("label"),input_type.getJSONObject(i).getString("value"));
                                        input_typeMapList.add(input_type.getJSONObject(i).getString("label"));
                                        input_typeMapValueList.add(input_type.getJSONObject(i).getString("value"));
                                    }
                                }

                                JSONArray bundle_data = dataObj.getJSONArray("bundle_data");
                                for (int i = 0; i < bundle_data.length(); i++) {
                                    JSONObject object = bundle_data.getJSONObject(i);

                                    View custom_option_layout_row = View.inflate(getApplicationContext(), R.layout.ced_multivendor_preselectedbundle_option_layout_row, null);
                                    EditText tittle = custom_option_layout_row.findViewById(R.id.MultiVendor_tittle);
                                    TextView option_id = custom_option_layout_row.findViewById(R.id.MultiVendor_option_id);
                                    TextView bundle_price_type = custom_option_layout_row.findViewById(R.id.MultiVendor_bundle_price_type);
                                    EditText sortorder = custom_option_layout_row.findViewById(R.id.MultiVendor_sortorder);
                                    Spinner type = custom_option_layout_row.findViewById(R.id.MultiVendor_type);
                                    Spinner required = custom_option_layout_row.findViewById(R.id.MultiVendor_required);
                                    LinearLayout selectedoptions = custom_option_layout_row.findViewById(R.id.MultiVendor_selectedoptions);

                                    option_id.setText(object.getString("option_id"));
                                    bundle_price_type.setText(object.getString("bundle_price_type"));
                                    tittle.setText(object.getString("option_title"));
                                    required.setSelection(Integer.parseInt(object.getString("is_option_required")));
                                    sortorder.setText(object.getString("option_position"));

                                    ArrayAdapter arrayAdapter=new ArrayAdapter(Ced_MultiVendor_BundleItems.this,R.layout.spinner_item,input_typeMapList);
                                    type.setAdapter(arrayAdapter);
                                    type.setSelection(input_typeMapValueList.indexOf(object.getString("option_type")));

                                    JSONArray option_selection = object.getJSONArray("option_selection");
                                    for (int j = 0; j < option_selection.length(); j++) {
                                        JSONObject object1 = option_selection.getJSONObject(j);
                                        View bundle_option_layout_row = View.inflate(getApplicationContext(), R.layout.ced_multivendor_preselectedbundleproductlist, null);
                                        TextView selection_id = bundle_option_layout_row.findViewById(R.id.MultiVendor_selection_id);
                                        TextView product_id = bundle_option_layout_row.findViewById(R.id.MultiVendor_product_id);
                                        TextView sku = bundle_option_layout_row.findViewById(R.id.MultiVendor_sku);
                                        TextView RegularPrice = bundle_option_layout_row.findViewById(R.id.MultiVendor_RegularPrice);
                                        TextView product_name = bundle_option_layout_row.findViewById(R.id.MultiVendor_product_name);
                                        EditText qty = bundle_option_layout_row.findViewById(R.id.MultiVendor_qty);
                                        LinearLayout pricesection = bundle_option_layout_row.findViewById(R.id.MultiVendor_pricesection);
                                        EditText price = bundle_option_layout_row.findViewById(R.id.MultiVendor_price);
                                        Spinner type_selection_price = bundle_option_layout_row.findViewById(R.id.MultiVendor_type);
                                        LinearLayout defaultqtysection = bundle_option_layout_row.findViewById(R.id.MultiVendor_defaultqtysection);
                                        Spinner usercanchangeqty = bundle_option_layout_row.findViewById(R.id.MultiVendor_usercanchangeqty);

//                                        ArrayAdapter pricetypeAdapter=new ArrayAdapter(Ced_MultiVendor_BundleItems.this,R.layout.spinner_item,selection_price_typeMapList);
//                                        type_selection_price.setAdapter(pricetypeAdapter);
//                                        type_selection_price.setSelection(Integer.parseInt(selection_price_typeMapList.get(Integer.parseInt(object.getString("selection_price_type")))));

//                                        if (object1.getString("selection_price_type").equals("1")) {
                                        if (dynamicPrice) {
                                            pricesection.setVisibility(View.GONE);
                                        }
                                        else {
                                            pricesection.setVisibility(View.VISIBLE);
                                            price.setText(object1.get("selection_price")!=null?object1.getString("selection_price"):"");
                                            if(object1.has("selection_price_type")) {
                                                try {
                                                    type_selection_price.setSelection(object1.getString("selection_price_type") != "null" ? Integer.parseInt(object1.getString("selection_price_type")) : 0);
                                                }
                                                catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                catch (NumberFormatException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }

                                        product_name.setText(object1.getString("selection_name"));
                                        sku.setText(object1.getString("selection_sku"));
                                        qty.setText(object1.getString("default_qty"));
                                        selection_id.setText(object1.getString("selection_id"));
                                        product_id.setText(object1.getString("product_id"));
                                        RegularPrice.setText(object1.get("selection_price")!=null?object1.getString("selection_price"):"");

                                        if (object.getString("option_type").equals("select") || object.getString("option_type").equals("radio")) {
                                            if (object1.getString("user_can_change_qty").equals("0")) {
                                                usercanchangeqty.setSelection(0);
                                            }
                                            else {
                                                usercanchangeqty.setSelection(1);
                                            }
                                            defaultqtysection.setVisibility(View.VISIBLE);
                                        }
                                        else {
                                            defaultqtysection.setVisibility(View.GONE);
                                        }
                                        selectedoptions.addView(bundle_option_layout_row);
                                    }
                                    preselectedbundleoptions.addView(custom_option_layout_row);
                                }
                            }
                            else {

                                if (dataObj.has("dynamic_price_type")&&dataObj.getString("dynamic_price_type").equals("1")){
                                    dynamicPrice=false;
                                }
                                else {
                                    dynamicPrice=true;
                                }

                                if (dataObj.has("shipment_type")){
                                    shipmentType=dataObj.getString("shipment_type");
                                }

                                if (dataObj.has("ship_bundle_items")){
                                    JSONArray ship_bundle_items=dataObj.getJSONArray("ship_bundle_items");
                                    for(int i=0;i<ship_bundle_items.length();i++){
                                        shipBundleMap.put(ship_bundle_items.getJSONObject(i).getString("label"),ship_bundle_items.getJSONObject(i).getString("value"));
                                        shipBundleMapList.add(ship_bundle_items.getJSONObject(i).getString("label"));
                                        if (ship_bundle_items.getJSONObject(i).getString("value").equals(shipmentType)) {
                                            shipmentTypePosition = i;
                                        }
                                    }

                                    ArrayAdapter arrayAdapter=new ArrayAdapter(Ced_MultiVendor_BundleItems.this,R.layout.spinner_item,shipBundleMapList);
                                    ShipBundleItems.setAdapter(arrayAdapter);
                                    ShipBundleItems.setSelection(shipmentTypePosition);
                                }

                                if (dataObj.has("selection_price_type")){
                                    JSONArray selection_price_type=dataObj.getJSONArray("selection_price_type");
                                    for(int i=0;i<selection_price_type.length();i++){
                                        selection_price_typeMap.put(selection_price_type.getJSONObject(i).getString("label"),selection_price_type.getJSONObject(i).getString("value"));
                                        selection_price_typeMapList.add(selection_price_type.getJSONObject(i).getString("label"));
                                    }
                                }

                                if (dataObj.has("input_type")){
                                    JSONArray input_type=dataObj.getJSONArray("input_type");
                                    for(int i=0;i<input_type.length();i++){
                                        input_typeMap.put(input_type.getJSONObject(i).getString("label"),input_type.getJSONObject(i).getString("value"));
                                        input_typeMapList.add(input_type.getJSONObject(i).getString("label"));
                                    }
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
                }, Ced_MultiVendor_BundleItems.this, "POST", getBundleDataParams);
                crr.execute(getBundleDataUrl);

                addbundleptions.setOnClickListener(v -> {
                    View custom_option_layout_row = View.inflate(getApplicationContext(), R.layout.ced_multivendor_bundle_option_layout_row, null);
                    LinearLayout foroptions = custom_option_layout_row.findViewById(R.id.MultiVendor_foroptions);
                    RelativeLayout deleteoptions = custom_option_layout_row.findViewById(R.id.MultiVendor_deleteoptions);
                    EditText optionTittle=custom_option_layout_row.findViewById(R.id.MultiVendor_tittle);
                    Spinner inputType=custom_option_layout_row.findViewById(R.id.MultiVendor_type);
                    ArrayAdapter arrayAdapter=new ArrayAdapter(Ced_MultiVendor_BundleItems.this,R.layout.spinner_item,input_typeMapList);
                    inputType.setAdapter(arrayAdapter);

                    deleteoptions.setOnClickListener(v12 -> {
                        CardView cardView = (CardView) deleteoptions.getParent().getParent().getParent();
                        int index = bundleoptions.indexOfChild(cardView);
                        if (Ced_MultiVendor_GlobalVariables.bundle_selections_data.has(String.valueOf(index))) {
                            Ced_MultiVendor_GlobalVariables.bundle_selections_data.remove(String.valueOf(index));
                        }
                        else {
                            Log.i("REpo", "onCreate_489: "+Ced_MultiVendor_GlobalVariables.bundle_selections_data);
                        }
                        bundleoptions.removeView(cardView);
                    });

                    foroptions.setOnClickListener(v1 -> {
                        CardView view = (CardView) foroptions.getParent().getParent();
                        int index = bundleoptions.indexOfChild(view);
                        if (optionTittle.getText().toString().isEmpty()) {
                            optionTittle.setError(getResources().getString(R.string.empty));
                            optionTittle.requestFocus();
                        }
                        else {
                            Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_BundleProductGrid.class);
                            intent.putExtra("product_id", product_id);
                            intent.putExtra("attribute_set", attribute_set);
                            intent.putExtra("type", type);
                            intent.putExtra("type_spinner", input_typeMap.get(inputType.getSelectedItem().toString()));
                            intent.putExtra("index", index);
                            if (dynamicPrice) {
                                intent.putExtra("dynamic_price", true);
                            }
                            startActivity(intent);
                        }

                    });
                    bundleoptions.addView(custom_option_layout_row);
                    mainvendorscroll.post(() -> mainvendorscroll.fullScroll(ScrollView.FOCUS_DOWN));
                });

            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            Intent nointernet = new Intent(Ced_MultiVendor_BundleItems.this, Ced_MultiVendor_NoInternetconnection.class);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(nointernet);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }

    }

    @Override
    protected void onResume() {
        Ced_MultiVendor_ConnectionDetector connectionDetector = new Ced_MultiVendor_ConnectionDetector(Ced_MultiVendor_BundleItems.this);
        if (connectionDetector.isConnectingToInternet()) {
            setname(session.getvendorname());
            setprofilepic(session.getvendorpic());
            changetitle(getString(R.string.bundle_items));
            //  invalidateOptionsMenu();
            super.onResume();
        }
        else {
            Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            super.onResume();
        }
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
