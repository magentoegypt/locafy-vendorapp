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
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import magentoegypt.locafy.base_app.Ced_MultiVendor_FontSetting;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorSplash;
import magentoegypt.locafy_constant.Ced_MultiVendor_GlobalVariables;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_UnAuthourizedRequestError;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.R;
import magentoegypt.locafy_constant.InteractiveScrollView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class Ced_MultiVendor_Configurable extends Ced_MultiVendor_NavigationActivity {
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    private TextView save;
    private String tabs;
    private String product_id;
    private String type;
    private LinearLayout manageproductlist;
    private LinearLayout pricesection;
    private String Currenturl;
    private String updateurl;
    private LinkedHashMap<String, String> dataforproducts;
    private HashMap<String, String> dataforrelated;
    private String Jstring;
    private LinearLayout filtersection;
    private Dialog listDialog;
    private String datafilterjson = "";
    private int currentPage = 1;
    ArrayList<String> attributenames;
    HashMap<String, String> attributeidvalue;
    HashMap<String, String> attributevalueid;
    ArrayList<HashMap<String, String>> relatedproductlist;
    String attribute_set;
    Ced_MultiVendor_FontSetting fontSetting;
    TextView pages;
    TextView products;
    TextView totalpro;
    Spinner sortby;
    HashMap<String, ArrayList<String>> dataforattributeconfig;
    FloatingActionButton addprice;
    HashMap<String, String> value_indexhash;
    HashMap<String, String> attributelabel_code;
    JSONArray pricearray;
    HashMap<String, String> attributepricehash;
    HashMap<String, String> attributetypehash;
    HashMap<String, String> attributeidhash;
    HashMap<String, String> attributevalueidhash;
    String selected_websitetopost;
    private InteractiveScrollView MultiVendor_mainscroll;
    private final boolean isLoading = true;
    private JSONArray latest_order;
    private String stock_id;

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
        dataforproducts = new LinkedHashMap<>();
        value_indexhash = new HashMap<>();
        attributelabel_code = new HashMap<>();
        dataforrelated = new HashMap<>();
        attributeidvalue = new HashMap<>();
        attributevalueid = new HashMap<>();
        attributepricehash = new HashMap<>();
        attributetypehash = new HashMap<>();
        attributeidhash = new HashMap<>();
        attributevalueidhash = new HashMap<>();
        attributenames = new ArrayList<>();
        relatedproductlist = new ArrayList<>();
        dataforattributeconfig = new HashMap<>();
        fontSetting = new Ced_MultiVendor_FontSetting();
        pricearray = new JSONArray();
        selected_websitetopost = getIntent().getStringExtra("selectedwebsite");
        if (connectionDetector.isConnectingToInternet()) {
            try {
                setname(session.getvendorname());
                setprofilepic(session.getvendorpic());
                changetitle("Associated Products");
                ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
                getLayoutInflater().inflate(R.layout.ced_multivendor_configurable, content, true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);
                Currenturl = session.getBase_Url() + "vproductapi/vproducts/configurableGrid";
                updateurl = session.getBase_Url() + "vendorapi/vproducts/update";
                Bundle bundle = getIntent().getExtras();
                if (bundle != null) {
                    if (getIntent().getStringExtra("filter") != null) {
                        datafilterjson = getIntent().getStringExtra("filter");
                        dataforproducts.put("filter", datafilterjson);
                    }
                    tabs = getIntent().getStringExtra("tabs");
                    product_id = getIntent().getStringExtra("product_id");
                    type = getIntent().getStringExtra("type");
                    attribute_set = getIntent().getStringExtra("attribute_set");
                    if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                        Log.i("attribute_set", "" + attribute_set);
                    }
                }
                MultiVendor_mainscroll = findViewById(R.id.MultiVendor_mainscroll);
                pages = findViewById(R.id.MultiVendor_pages);
                latest_order = new JSONArray();
                addprice = findViewById(R.id.MultiVendor_addprice);
                totalpro = findViewById(R.id.MultiVendor_totalpro);
                products = findViewById(R.id.MultiVendor_products);
                save = findViewById(R.id.MultiVendor_save);
                sortby = findViewById(R.id.MultiVendor_sortby);
                fontSetting.setFontforTextviews(save, "Roboto-Medium.ttf", getApplicationContext());
                fontSetting.setFontforTextviews(pages, "Roboto-Medium.ttf", getApplicationContext());
                fontSetting.setFontforTextviews(products, "Roboto-Medium.ttf", getApplicationContext());
                fontSetting.setFontforTextviews(totalpro, "Roboto-Regular.ttf", getApplicationContext());
                manageproductlist = findViewById(R.id.MultiVendor_manageproductlist);
                filtersection = findViewById(R.id.MultiVendor_filtersection);
                filtersection.setOnClickListener(v -> showfilter());
                dataforproducts.put("page", ""+ currentPage);
                dataforproducts.put("vendor_id", session.getVendorid());
                dataforproducts.put("product_id", product_id);
                dataforproducts.put("hashkey", session.getHahkey());
                if (Ced_MultiVendor_GlobalVariables.config_attribute_value.length() > 0) {
                    dataforproducts.put("config_attribute", String.valueOf(Ced_MultiVendor_GlobalVariables.config_attribute_value));
                }
                dataforproducts.put("attribute_set", attribute_set);
                dataforproducts.put("type", type);

                applydata(currentPage);

                MultiVendor_mainscroll.setOnBottomReachedListener(() -> {
                    if (isLoading) {
                        currentPage++;
                        ScrollData();
                    }

                });


       /*         sortby.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                    {
                        current = sortby.getSelectedItem().toString();
                        if (manageproductlist.getChildCount() > 0)
                        {
                            manageproductlist.removeAllViews();
                        }
                        try
                        {
                            applydata(current);
                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent)
                    {

                    }
                });*/
                addprice.setOnClickListener(v -> {
                    Log.e("REpo", "addprice clicked== " + dataforattributeconfig);
                    if (dataforattributeconfig.size() > 0) {
                        showpricefilter();
                    }

                });
                save.setOnClickListener(v -> {
//                    if (pricearray.length() > 0) {
                        dataforrelated.put("product_id", product_id);
                        dataforrelated.put("websites", selected_websitetopost);
                        dataforrelated.put("vendor_id", session.getVendorid());
                        dataforrelated.put("hashkey", session.getHahkey());
                        if (Ced_MultiVendor_GlobalVariables.config_attribute_value.length() > 0) {
                            dataforrelated.put("config_attribute", String.valueOf(Ced_MultiVendor_GlobalVariables.config_attribute_value));
                        }
                        dataforrelated.put("attribute_set", attribute_set);
                        dataforrelated.put("type", type);
                        try {
                            if (Ced_MultiVendor_GlobalVariables.Configurableproductids.size() > 0) {
                                String dataforrelatedstring = "";
                                Iterator iterator = new Ced_MultiVendor_GlobalVariables().Configurableproductids.iterator();
                                while (iterator.hasNext()) {
                                    String value = (String) iterator.next();
                                    dataforrelatedstring = value + dataforrelatedstring;
                                }
                                JSONObject object = new JSONObject();
                                object.put("related", dataforrelatedstring);
                                JSONObject object1 = new JSONObject();
                                object1.put("links", object);
                                dataforrelated.put("Configurableproducts", object1.toString());
                                dataforrelated.put("config_data", String.valueOf(Ced_MultiVendor_GlobalVariables.configurable_data));
//                                dataforrelated.put("configurable_attributes_data", pricearray.toString());
                            }
                            Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(output -> {
                                Jstring = output.toString();
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
                                            related.putExtra("selectedwebsite", selected_websitetopost);
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
                            }, Ced_MultiVendor_Configurable.this, "POST", dataforrelated);
                            crr.execute(updateurl);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                   /* } else {
                        Toast.makeText(getApplicationContext(), "Please assign some price first", Toast.LENGTH_LONG).show();
                        showpricefilter();
                    }*/
                });

                /********************************************************************************/
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Intent nointernet = new Intent(Ced_MultiVendor_Configurable.this, Ced_MultiVendor_NoInternetconnection.class);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(nointernet);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }
    }

    private void ScrollData() {
        dataforproducts.put("page", ""+ currentPage);
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
                        JSONArray jsonArray1 = jsonObject.getJSONObject("data").getJSONArray("products");
                        for (int i = 0; i < jsonArray1.length(); i++) {
                            JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                            latest_order.put(jsonObject1);
                        }
                        if (jsonObject.getJSONObject("data").has("options")) {
                            JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("options");
                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject object = jsonArray.getJSONObject(j);
                                attributepricehash.put(object.getString("label"), object.getString("option_price"));
                                attributetypehash.put(object.getString("label"), object.getString("price_type"));
                                attributeidhash.put(object.getString("super_label"), object.getString("product_super_attribute_id"));
                                attributevalueidhash.put(object.getString("label"), object.getString("value-id"));
                            }
                        }
                        totalpro.setText(jsonObject.getJSONObject("data").getString("count"));
                        for (int l = 0; l < latest_order.length(); l++) {
                            View related_row = View.inflate(getApplicationContext(), R.layout.ced_multivendor_configproductlist_item, null);
                            JSONObject orderrow = latest_order.getJSONObject(l);
                            final CheckBox selectrelated = related_row.findViewById(R.id.MultiVendor_selectrelated);
                            TextView product_id = related_row.findViewById(R.id.MultiVendor_product_id);//
                            TextView json = related_row.findViewById(R.id.MultiVendor_json);//
                            TextView count = related_row.findViewById(R.id.MultiVendor_count);//
                            TextView sku = related_row.findViewById(R.id.MultiVendor_sku);//
                            TextView RegularPrice = related_row.findViewById(R.id.MultiVendor_RegularPrice);//
                            TextView Status = related_row.findViewById(R.id.MultiVendor_Status);//
                            TextView attribuetsetname = related_row.findViewById(R.id.MultiVendor_attribuetsetname);
                            TextView skutag = related_row.findViewById(R.id.MultiVendor_skutag);
                            TextView RegularPricetag = related_row.findViewById(R.id.MultiVendor_RegularPricetag);
                            TextView statustag = related_row.findViewById(R.id.MultiVendor_statustag);
                            TextView attributesetnametag = related_row.findViewById(R.id.MultiVendor_attributesetnametag);
                            fontSetting.setFontforTextviews(attribuetsetname, "Roboto-Regular.ttf", getApplicationContext());
                            fontSetting.setFontforTextviews(product_id, "Roboto-Regular.ttf", getApplicationContext());
                            fontSetting.setFontforTextviews(sku, "Roboto-Regular.ttf", getApplicationContext());
                            fontSetting.setFontforTextviews(RegularPrice, "Roboto-Regular.ttf", getApplicationContext());
                            fontSetting.setFontforTextviews(Status, "Roboto-Regular.ttf", getApplicationContext());
                            fontSetting.setFontforTextviews(count, "Roboto-Medium.ttf", getApplicationContext());
                            fontSetting.setFontforTextviews(attributesetnametag, "Roboto-Medium.ttf", getApplicationContext());
                            fontSetting.setFontforTextviews(skutag, "Roboto-Medium.ttf", getApplicationContext());
                            fontSetting.setFontforTextviews(RegularPricetag, "Roboto-Medium.ttf", getApplicationContext());
                            fontSetting.setFontforTextviews(statustag, "Roboto-Medium.ttf", getApplicationContext());
                            fontSetting.setfontforCheckbox(selectrelated, "Roboto-Medium.ttf", getApplicationContext());
                            selectrelated.setOnCheckedChangeListener((buttonView, isChecked) -> {
                                RelativeLayout relativeLayout = (RelativeLayout) selectrelated.getParent();
                                TextView proid = (TextView) relativeLayout.getChildAt(1);
                                TextView json1 = (TextView) relativeLayout.getChildAt(3);
                                if (isChecked) {

                                    if (!(Ced_MultiVendor_GlobalVariables.Configurableproductids.contains(proid.getText().toString()))) {

                                        if (Ced_MultiVendor_GlobalVariables.valuefordisableconfig.size() > 0) {
                                            try {
                                                Log.i("ASd", "sd" + Ced_MultiVendor_GlobalVariables.valuefordisableconfig.toString());
                                                JSONArray jsonArray = new JSONArray();
                                                String data = "";
                                                Log.i("ASd", "sd" + json1.getText().toString());
                                                JSONObject jsonObject12 = new JSONObject(json1.getText().toString());
                                                JSONArray array = jsonObject12.getJSONArray("attribute");
                                                for (int i = 0; i < array.length(); i++) {
                                                    JSONObject object = array.getJSONObject(i);
                                                    JSONObject object1 = new JSONObject();
                                                    object1.put("label", object.getString("attribute_label"));
                                                    object1.put("attribute_id", object.getString("attribute_id"));
                                                    object1.put("value_index", object.getString("value"));
                                                    object1.put("is_percent", "0");
                                                    object1.put("pricing_value", " ");
                                                    data = object.getString("attribute_id") + "#" + object.getString("value") + data;
                                                    jsonArray.put(object1);

                                                }
                                                if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                                    Log.i("data1", "" + data);
                                                }
                                                if (Ced_MultiVendor_GlobalVariables.valuefordisableconfig.contains(data)) {
                                                    selectrelated.setChecked(false);
                                                    Toast.makeText(getApplicationContext(), "Same Combination cannot be selected twice", Toast.LENGTH_LONG).show();
                                                } else {
                                                    Ced_MultiVendor_GlobalVariables.Configurableproductids.add(proid.getText().toString());
                                                    Ced_MultiVendor_GlobalVariables.valuefordisableconfig.add(data);
                                                    Ced_MultiVendor_GlobalVariables.configurable_data.put(proid.getText().toString(), jsonArray);
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        } else {

                                            Ced_MultiVendor_GlobalVariables.Configurableproductids.add(proid.getText().toString());
                                            try {

                                                JSONArray jsonArray = new JSONArray();
                                                String data = "";
                                                JSONObject jsonObject12 = new JSONObject(json1.getText().toString());
                                                JSONArray array = jsonObject12.getJSONArray("attribute");
                                                for (int i = 0; i < array.length(); i++) {
                                                    JSONObject object = array.getJSONObject(i);
                                                    JSONObject object1 = new JSONObject();
                                                    object1.put("label", object.getString("attribute_label"));
                                                    object1.put("attribute_id", object.getString("attribute_id"));
                                                    object1.put("value_index", object.getString("value"));

                                                    object1.put("is_percent", "0");
                                                    object1.put("pricing_value", " ");
                                                    data = object.getString("attribute_id") + "#" + object.getString("value") + data;
                                                    jsonArray.put(object1);

                                                }
                                                if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                                    Log.i("data2", "" + data);
                                                }
                                                Ced_MultiVendor_GlobalVariables.valuefordisableconfig.add(data);
                                                Ced_MultiVendor_GlobalVariables.configurable_data.put(proid.getText().toString(), jsonArray);

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                    }
                                } else {
                                    if ((Ced_MultiVendor_GlobalVariables.Configurableproductids.contains(proid.getText().toString()))) {
                                        Ced_MultiVendor_GlobalVariables.Configurableproductids.remove(proid.getText().toString());
                                        try {
                                            JSONArray jsonArray = Ced_MultiVendor_GlobalVariables.configurable_data.getJSONArray(proid.getText().toString());
                                            String data = "";
                                            for (int j = 0; j < jsonArray.length(); j++) {
                                                JSONObject object = jsonArray.getJSONObject(j);
                                                data = object.getString("attribute_id") + "#" + object.getString("value_index") + data;
                                            }
                                            Ced_MultiVendor_GlobalVariables.valuefordisableconfig.remove(data);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        Ced_MultiVendor_GlobalVariables.configurable_data.remove(proid.getText().toString());
                                    }
                                }
                                if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                    Log.i("valuefordisableconfig", "" + Ced_MultiVendor_GlobalVariables.valuefordisableconfig);
                                    Log.i("configurable_data", "" + Ced_MultiVendor_GlobalVariables.configurable_data);
                                }

                            });
                            if (Ced_MultiVendor_GlobalVariables.Configurableproductids.contains(orderrow.getString("product_id"))) {
                                selectrelated.setChecked(true);
                            }
                            if (orderrow.getString("selected").equals("true")) {
                                if (!(Ced_MultiVendor_GlobalVariables.Configurableproductids.contains(orderrow.getString("product_id")))) {
                                    Ced_MultiVendor_GlobalVariables.Configurableproductids.add(orderrow.getString("product_id"));
                                    try {

                                        JSONArray jsonArray = new JSONArray();
                                        String data = "";
                                        JSONArray array = orderrow.getJSONArray("attribute");
                                        for (int i = 0; i < array.length(); i++) {
                                            JSONObject object = array.getJSONObject(i);
                                            JSONObject object1 = new JSONObject();
                                            object1.put("label", object.getString("attribute_label"));
                                            object1.put("attribute_id", object.getString("attribute_id"));
                                            object1.put("value_index", object.getString("value"));
                                            object1.put("is_percent", "0");
                                            object1.put("pricing_value", " ");
                                            object1.put("pricing_value", " ");
                                            data = object.getString("attribute_id") + "#" + object.getString("value") + data;
                                            jsonArray.put(object1);

                                        }
                                        if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                            Log.i("data1", "" + data);
                                        }
                                        Ced_MultiVendor_GlobalVariables.valuefordisableconfig.add(data);
                                        Ced_MultiVendor_GlobalVariables.configurable_data.put(orderrow.getString("product_id"), jsonArray);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    selectrelated.setChecked(true);
                                }

                            }
                            try {
                                JSONArray array = orderrow.getJSONArray("attribute");
                                String options = "";
                                for (int i = 0; i < array.length(); i++) {

                                    JSONObject object1 = array.getJSONObject(i);
                                    value_indexhash.put(object1.getString("value_label"), object1.getString("value"));
                                    value_indexhash.put(object1.getString("attribute_label"), object1.getString("attribute_id"));
                                    attributelabel_code.put(object1.getString("attribute_label"), object1.getString("attribute_code"));
                                    if (dataforattributeconfig.containsKey(object1.getString("attribute_label"))) {
                                        ArrayList<String> labelconfigattribute = dataforattributeconfig.get(object1.getString("attribute_label"));
                                        if (!(labelconfigattribute.contains(object1.getString("value_label")))) {
                                            labelconfigattribute.add(object1.getString("value_label"));
                                            dataforattributeconfig.put(object1.getString("attribute_label"), labelconfigattribute);
                                        }

                                    } else {
                                        ArrayList<String> labelconfigattribute = new ArrayList<>();
                                        labelconfigattribute.add(object1.getString("value_label"));
                                        dataforattributeconfig.put(object1.getString("attribute_label"), labelconfigattribute);
                                    }
                                    options = object1.getString("attribute_label") + ":" + object1.getString("value_label") + "\n" + options;
                                }
                                if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                    Log.i("options", "" + options);
                                }
                                count.setText(options);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            selectrelated.setText("#" + orderrow.getString("product_id"));
                            product_id.setText(orderrow.getString("product_id"));
                            sku.setText("#" + orderrow.getString("sku"));
                            RegularPrice.setText("#" + orderrow.getString("regular_price"));
                            Status.setText("#" + orderrow.getString("Inventory"));
                            attribuetsetname.setText("#" + orderrow.getString("set_name"));
                            json.setText(orderrow.toString());
                            manageproductlist.addView(related_row);
                        }
                        JSONArray sets = jsonObject.getJSONObject("data").getJSONArray("sets").getJSONArray(0);
                        for (int i = 0; i < sets.length(); i++) {
                            JSONObject jsonObject1 = sets.getJSONObject(i);
                            attributeidvalue.put(jsonObject1.getString("value"), jsonObject1.getString("id"));
                            attributevalueid.put(jsonObject1.getString("id"), jsonObject1.getString("value"));
                            if (!(attributenames.contains(jsonObject1.getString("value")))) {
                                attributenames.add(jsonObject1.getString("value"));
                            }
                        }
                        if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                            Log.i("dataforattributeconfig", "" + dataforattributeconfig);
                            Log.i("value_indexhash", "" + value_indexhash);
                        }
                    } else {
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
        }, Ced_MultiVendor_Configurable.this, "POST", dataforproducts);
        crr.execute(Currenturl);

    }

    private void showpricefilter() {
        try {
            Log.e("REpo", "addprice clicked== showpricefilter");
            listDialog = new Dialog(this, R.style.PauseDialog);
            listDialog.setTitle(getResources().getString(R.string.alert_name));
            LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = li.inflate(R.layout.ced_multivendor_configpricesection, null, false);
            final LinearLayout showprice = v.findViewById(R.id.MultiVendor_showprice);
            TextView saveprice = v.findViewById(R.id.MultiVendor_saveprice);
            for (Map.Entry<String, ArrayList<String>> entry : dataforattributeconfig.entrySet()) {
                Log.e("REpo", "addprice clicked== dataforattributeconfig.entrySet()");
                View sectionforattribute = li.inflate(R.layout.ced_multivendor_sectionforattribute, null, false);
                TextView attributename = sectionforattribute.findViewById(R.id.MultiVendor_attributename);
                attributename.setText(entry.getKey());
                LinearLayout inner_attribute_price_section = sectionforattribute.findViewById(R.id.MultiVendor_inner_attribute_price_section);
                Iterator iterator = entry.getValue().iterator();
                while (iterator.hasNext()) {
                    Log.e("REpo", "addprice clicked== dataforattributeconfig.entrySet()=iterator");
                    String innerattribute = (String) iterator.next();
                    View inner_attribute_price_section_layout = li.inflate(R.layout.ced_multivendor_inner_attribute_price_section_layout, null, false);
                    TextView innerattributename = inner_attribute_price_section_layout.findViewById(R.id.MultiVendor_innerattributename);
                    innerattributename.setText(innerattribute);
                    EditText pricefield = inner_attribute_price_section_layout.findViewById(R.id.MultiVendor_pricefield);
                    Spinner attribute_price_type = inner_attribute_price_section_layout.findViewById(R.id.MultiVendor_attribute_price_type);
                    if (attributepricehash.size() > 0) {
                        pricefield.setText(attributepricehash.get(innerattribute));
                    }
                    if (attributetypehash.size() > 0) {
                        attribute_price_type.setSelection(Integer.parseInt(attributetypehash.get(innerattribute)));
                    }
                    inner_attribute_price_section.addView(inner_attribute_price_section_layout);
                }
                showprice.addView(sectionforattribute);
            }
            saveprice.setOnClickListener(v1 -> {
                try {
                    if (showprice.getChildCount() > 0) {
                        for (int i = 0; i < showprice.getChildCount(); i++) {
                            JSONObject object = new JSONObject();
                            LinearLayout linearLayout = (LinearLayout) showprice.getChildAt(i);
                            TextView view = (TextView) linearLayout.getChildAt(0);
                            if (attributeidhash.containsKey(view.getText().toString())) {
                                object.put("id", attributeidhash.get(view.getText().toString()));
                            } else {
                                object.put("id", "null");
                            }
                            object.put("label", view.getText().toString());
                            object.put("use_default", "null");
                            object.put("position", "null");
                            LinearLayout layout = (LinearLayout) linearLayout.getChildAt(1);
                            JSONArray jsonArray = new JSONArray();
                            for (int j = 0; j < layout.getChildCount(); j++) {
                                JSONObject object1 = new JSONObject();
                                object1.put("attribute_id", value_indexhash.get(view.getText().toString()));
                                LinearLayout linearLayout1 = (LinearLayout) layout.getChildAt(j);
                                TextView view1 = (TextView) linearLayout1.getChildAt(0);
                                object1.put("label", view1.getText().toString());
                                object1.put("value_index", value_indexhash.get(view1.getText().toString()));
                                EditText editText = (EditText) linearLayout1.getChildAt(1);
                                object1.put("pricing_value", editText.getText().toString());
                                attributepricehash.put(view1.getText().toString(), editText.getText().toString());
                                Spinner spinner = (Spinner) linearLayout1.getChildAt(2);
                                if (spinner.getSelectedItem().equals("fixed")) {
                                    object1.put("is_percent", "0");
                                    attributetypehash.put(view1.getText().toString(), "0");
                                } else {
                                    object1.put("is_percent", "1");
                                    attributetypehash.put(view1.getText().toString(), "1");
                                }
                                if (attributeidhash.containsKey(view.getText().toString())) {
                                    object1.put("product_super_attribute_id", attributeidhash.get(view.getText().toString()));
                                }
                                if (attributevalueidhash.containsKey(view1.getText().toString())) {
                                    object1.put("value-id", attributevalueidhash.get(view1.getText().toString()));
                                    object1.put("can_read_price", "true");
                                    object1.put("can_edit_price", "true");
                                }
                                jsonArray.put(object1);
                            }
                            object.put("values", jsonArray);
                            object.put("attribute_id", value_indexhash.get(view.getText().toString()));
                            object.put("attribute_code", attributelabel_code.get(view.getText().toString()));
                            object.put("frontend_label", view.getText().toString());
                            object.put("store_label", view.getText().toString());
                            object.put("html_id", "configurableattribute_" + i);
                            pricearray.put(object);
                        }
                        Log.i("pricearray :", "" + pricearray);
                        Toast.makeText(getApplicationContext(), "Price Successfully Applied , Now Save the Changes.", Toast.LENGTH_LONG).show();
                        listDialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
            listDialog.setContentView(v);
            listDialog.setCancelable(true);
            listDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        Ced_MultiVendor_ConnectionDetector connectionDetector = new Ced_MultiVendor_ConnectionDetector(Ced_MultiVendor_Configurable.this);
        if (connectionDetector.isConnectingToInternet()) {
            setname(session.getvendorname());
            setprofilepic(session.getvendorpic());
            changetitle("Associated Products");
            //     invalidateOptionsMenu();
            super.onResume();
        } else {
            Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            super.onResume();
        }
    }

    private void showfilter() {
        try {
            listDialog = new Dialog(this, R.style.PauseDialog);
            listDialog.setTitle(getResources().getString(R.string.alert_name));
            LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = li.inflate(R.layout.ced_multivendor_header_at_configurableproductlisting, null, false);
            final TextView setfilter = v.findViewById(R.id.MultiVendor_setfilter);//
            final TextView unsetfilter = v.findViewById(R.id.MultiVendor_unsetfilter);//
            final EditText from_price = v.findViewById(R.id.MultiVendor_from_price);//
            final EditText to_price = v.findViewById(R.id.MultiVendor_to_price);//
            final EditText to_product_id = v.findViewById(R.id.MultiVendor_edt_product_id);//
            final EditText edt_productname = v.findViewById(R.id.MultiVendor_edt_productname);//
            final EditText edt_productsku = v.findViewById(R.id.MultiVendor_edt_productsku);//
            final Spinner edt_status = v.findViewById(R.id.MultiVendor_edt_status);//
            final Spinner edt_attributesetname = v.findViewById(R.id.MultiVendor_edt_attributesetname);//
            ArrayAdapter<String> adp_attribute = new ArrayAdapter<>(Ced_MultiVendor_Configurable.this, android.R.layout.simple_dropdown_item_1line, attributenames);
            edt_attributesetname.setAdapter(adp_attribute);

            edt_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (adapterView.getSelectedItem().toString().equalsIgnoreCase("")) {

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            if (!(datafilterjson.isEmpty())) {
                JSONObject object = new JSONObject(datafilterjson);
                from_price.setText(object.getJSONObject("price").getString("from"));
                to_price.setText(object.getJSONObject("price").getString("to"));
                to_product_id.setText(object.getString("entity_id"));
                edt_productname.setText(object.getString("name"));
                edt_productsku.setText(object.getString("sku"));
                if (object.has("inventory")) {
                    if (object.getString("inventory").equals("In Stock")) {
                        edt_status.setSelection(1);
                    }
                    if (object.getString("inventory").equals("Out Of Stock")) {
                        edt_status.setSelection(1);
                    }
                }
                ArrayList<String> attributes = new ArrayList<>();
                if (attributevalueid.size() > 0) {
                    attributes.add(attributevalueid.get(object.getString("attribute_set_id")));
                    Iterator iterator = attributenames.iterator();
                    while (iterator.hasNext()) {
                        String value = (String) iterator.next();
                        if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                            Log.i("attributes", "" + value);
                        }
                        if ((value.equals(attributevalueid.get(object.getString("attribute_set_id"))))) {
                            //continue;
                        } else {
                            attributes.add(value);
                        }

                    }
                    if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                        Log.i("attributes", "" + attributes);
                    }
                    ArrayAdapter<String> adp_attribute_1 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, attributes);
                    edt_attributesetname.setAdapter(adp_attribute_1);
                }
            }

            unsetfilter.setOnClickListener(v12 -> {
                from_price.setText("");
                to_price.setText("");
                to_product_id.setText("");
                edt_productname.setText("");
                edt_productsku.setText("");
//                    edt_status.setSelection(0);
                edt_attributesetname.setSelection(0);
                datafilterjson = "";
                final JSONObject mainfilter = new JSONObject();
                JSONObject pricejsonObject = new JSONObject();
                try {
                    listDialog.dismiss();
                    pricejsonObject.put("from", from_price.getText().toString());
                    pricejsonObject.put("to", to_price.getText().toString());
                    mainfilter.put("price", pricejsonObject);
                    mainfilter.put("entity_id", to_product_id.getText().toString());
                    mainfilter.put("name", edt_productname.getText().toString());
                    //  mainfilter.put("inventory", edt_status.getSelectedItem());
                    mainfilter.put("sku", edt_productsku.getText().toString());
                    mainfilter.put("attribute_set_id", " ");
                    Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_Configurable.class);
                    intent.putExtra("filter", mainfilter.toString());
                    intent.putExtra("tabs", tabs);
                    intent.putExtra("product_id", product_id);
                    intent.putExtra("selectedwebsite", selected_websitetopost);
                    intent.putExtra("type", type);
                    intent.putExtra("attribute_set", attribute_set);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                }
                catch (Exception e) {
                    Intent main = new Intent(Ced_MultiVendor_Configurable.this, Ced_MultiVendor_VendorSplash.class);
                    main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(main);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }
            });

            setfilter.setOnClickListener(v1 -> {
                final JSONObject mainfilter = new JSONObject();
                JSONObject pricejsonObject = new JSONObject();
                try {
                    listDialog.dismiss();
                    pricejsonObject.put("from", from_price.getText().toString());
                    pricejsonObject.put("to", to_price.getText().toString());
                    mainfilter.put("price", pricejsonObject);
                    mainfilter.put("entity_id", to_product_id.getText().toString());
                    mainfilter.put("name", edt_productname.getText().toString());
                    //    mainfilter.put("inventory", edt_status.getSelectedItem());
                    mainfilter.put("sku", edt_productsku.getText().toString());
                    if (attributenames.size() > 0) {
                        mainfilter.put("attribute_set_id", attributeidvalue.get(edt_attributesetname.getSelectedItem()));
                    } else {
                        mainfilter.put("attribute_set_id", " ");
                    }
                    Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_Configurable.class);
                    intent.putExtra("filter", mainfilter.toString());
                    intent.putExtra("tabs", tabs);
                    intent.putExtra("product_id", product_id);
                    intent.putExtra("selectedwebsite", selected_websitetopost);
                    intent.putExtra("type", type);
                    intent.putExtra("attribute_set", attribute_set);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
            listDialog.setContentView(v);
            listDialog.setCancelable(true);
            listDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void applydata(int current) throws JSONException {
        /**************************************GetconfigProducts*************************************************/
        Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(output -> {
            Jstring = output.toString();
            if (functionalityList.getExtensionAddon()) {
                JSONObject jsonObject = new JSONObject(Jstring);
                if (jsonObject.has("vendor_approved")) {
                    logout();
                } else {
                    String success = jsonObject.getJSONObject("data").getString("success");
                    if (success.equals("true")) {
                        latest_order = jsonObject.getJSONObject("data").getJSONArray("products");
                        if (jsonObject.getJSONObject("data").has("options")) {
                            JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("options");
                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject object = jsonArray.getJSONObject(j);
                                attributepricehash.put(object.getString("label"), object.getString("option_price"));
                                attributetypehash.put(object.getString("label"), object.getString("price_type"));
                                attributeidhash.put(object.getString("super_label"), object.getString("product_super_attribute_id"));
                                attributevalueidhash.put(object.getString("label"), object.getString("value-id"));
                            }
                        }
                        totalpro.setText(jsonObject.getJSONObject("data").getString("count"));
                        for (int l = 0; l < latest_order.length(); l++) {
                            View related_row = View.inflate(getApplicationContext(), R.layout.ced_multivendor_configproductlist_item, null);
                            JSONObject orderrow = latest_order.getJSONObject(l);
                            final CheckBox selectrelated = related_row.findViewById(R.id.MultiVendor_selectrelated);
                            TextView product_id = related_row.findViewById(R.id.MultiVendor_product_id);//
                            TextView json = related_row.findViewById(R.id.MultiVendor_json);//
                            TextView count = related_row.findViewById(R.id.MultiVendor_count);//
                            TextView sku = related_row.findViewById(R.id.MultiVendor_sku);//
                            TextView RegularPrice = related_row.findViewById(R.id.MultiVendor_RegularPrice);//
                            TextView Status = related_row.findViewById(R.id.MultiVendor_Status);//
                            TextView attribuetsetname = related_row.findViewById(R.id.MultiVendor_attribuetsetname);
                            TextView skutag = related_row.findViewById(R.id.MultiVendor_skutag);
                            TextView RegularPricetag = related_row.findViewById(R.id.MultiVendor_RegularPricetag);
                            TextView statustag = related_row.findViewById(R.id.MultiVendor_statustag);
                            TextView attributesetnametag = related_row.findViewById(R.id.MultiVendor_attributesetnametag);
                            fontSetting.setFontforTextviews(attribuetsetname, "Roboto-Regular.ttf", getApplicationContext());
                            fontSetting.setFontforTextviews(product_id, "Roboto-Regular.ttf", getApplicationContext());
                            fontSetting.setFontforTextviews(sku, "Roboto-Regular.ttf", getApplicationContext());
                            fontSetting.setFontforTextviews(RegularPrice, "Roboto-Regular.ttf", getApplicationContext());
                            fontSetting.setFontforTextviews(Status, "Roboto-Regular.ttf", getApplicationContext());
                            fontSetting.setFontforTextviews(count, "Roboto-Medium.ttf", getApplicationContext());
                            fontSetting.setFontforTextviews(attributesetnametag, "Roboto-Medium.ttf", getApplicationContext());
                            fontSetting.setFontforTextviews(skutag, "Roboto-Medium.ttf", getApplicationContext());
                            fontSetting.setFontforTextviews(RegularPricetag, "Roboto-Medium.ttf", getApplicationContext());
                            fontSetting.setFontforTextviews(statustag, "Roboto-Medium.ttf", getApplicationContext());
                            fontSetting.setfontforCheckbox(selectrelated, "Roboto-Medium.ttf", getApplicationContext());
                            selectrelated.setOnCheckedChangeListener((buttonView, isChecked) -> {
                                RelativeLayout relativeLayout = (RelativeLayout) selectrelated.getParent();
                                TextView proid = (TextView) relativeLayout.getChildAt(1);
                                TextView json1 = (TextView) relativeLayout.getChildAt(3);
                                if (isChecked) {

                                    if (!(Ced_MultiVendor_GlobalVariables.Configurableproductids.contains(proid.getText().toString()))) {

                                        if (Ced_MultiVendor_GlobalVariables.valuefordisableconfig.size() > 0) {
                                            try {
                                                Log.i("ASd", "sd" + Ced_MultiVendor_GlobalVariables.valuefordisableconfig.toString());
                                                JSONArray jsonArray = new JSONArray();
                                                String data = "";
                                                Log.i("ASd", "sd" + json1.getText().toString());
                                                JSONObject jsonObject12 = new JSONObject(json1.getText().toString());
                                                JSONArray array = jsonObject12.getJSONArray("attribute");
                                                for (int i = 0; i < array.length(); i++) {
                                                    JSONObject object = array.getJSONObject(i);
                                                    JSONObject object1 = new JSONObject();
                                                    object1.put("label", object.getString("attribute_label"));
                                                    object1.put("attribute_id", object.getString("attribute_id"));
                                                    object1.put("value_index", object.getString("value"));
                                                    object1.put("is_percent", "0");
                                                    object1.put("pricing_value", " ");
                                                    data = object.getString("attribute_id") + "#" + object.getString("value") + data;
                                                    jsonArray.put(object1);
                                                }
                                                if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                                    Log.i("data1", "" + data);
                                                }
                                                if (Ced_MultiVendor_GlobalVariables.valuefordisableconfig.contains(data)) {
                                                    selectrelated.setChecked(false);
                                                    Toast.makeText(getApplicationContext(), "Same Combination cannot be selected twice", Toast.LENGTH_LONG).show();
                                                } else {
                                                    Ced_MultiVendor_GlobalVariables.Configurableproductids.add(proid.getText().toString());
                                                    Ced_MultiVendor_GlobalVariables.valuefordisableconfig.add(data);
                                                    Ced_MultiVendor_GlobalVariables.configurable_data.put(proid.getText().toString(), jsonArray);
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        } else {

                                            Ced_MultiVendor_GlobalVariables.Configurableproductids.add(proid.getText().toString());
                                            try {

                                                JSONArray jsonArray = new JSONArray();
                                                String data = "";
                                                JSONObject jsonObject12 = new JSONObject(json1.getText().toString());
                                                JSONArray array = jsonObject12.getJSONArray("attribute");
                                                for (int i = 0; i < array.length(); i++) {
                                                    JSONObject object = array.getJSONObject(i);
                                                    JSONObject object1 = new JSONObject();
                                                    object1.put("label", object.getString("attribute_label"));
                                                    object1.put("attribute_id", object.getString("attribute_id"));
                                                    object1.put("value_index", object.getString("value"));

                                                    object1.put("is_percent", "0");
                                                    object1.put("pricing_value", " ");
                                                    data = object.getString("attribute_id") + "#" + object.getString("value") + data;
                                                    jsonArray.put(object1);

                                                }
                                                if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                                    Log.i("data2", "" + data);
                                                }
                                                Ced_MultiVendor_GlobalVariables.valuefordisableconfig.add(data);
                                                Ced_MultiVendor_GlobalVariables.configurable_data.put(proid.getText().toString(), jsonArray);

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                    }
                                } else {
                                    if ((Ced_MultiVendor_GlobalVariables.Configurableproductids.contains(proid.getText().toString()))) {
                                        Ced_MultiVendor_GlobalVariables.Configurableproductids.remove(proid.getText().toString());
                                        try {
                                            JSONArray jsonArray = Ced_MultiVendor_GlobalVariables.configurable_data.getJSONArray(proid.getText().toString());
                                            String data = "";
                                            for (int j = 0; j < jsonArray.length(); j++) {
                                                JSONObject object = jsonArray.getJSONObject(j);
                                                data = object.getString("attribute_id") + "#" + object.getString("value_index") + data;
                                            }
                                            Ced_MultiVendor_GlobalVariables.valuefordisableconfig.remove(data);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        Ced_MultiVendor_GlobalVariables.configurable_data.remove(proid.getText().toString());
                                    }
                                }
                                if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                    Log.i("valuefordisableconfig", "" + Ced_MultiVendor_GlobalVariables.valuefordisableconfig);
                                    Log.i("configurable_data", "" + Ced_MultiVendor_GlobalVariables.configurable_data);
                                }

                            });
                            if (Ced_MultiVendor_GlobalVariables.Configurableproductids.contains(orderrow.getString("product_id"))) {
                                selectrelated.setChecked(true);
                            }
                            if (orderrow.getString("selected").equals("true")) {
                                if (!(Ced_MultiVendor_GlobalVariables.Configurableproductids.contains(orderrow.getString("product_id")))) {
                                    Ced_MultiVendor_GlobalVariables.Configurableproductids.add(orderrow.getString("product_id"));
                                    try {

                                        JSONArray jsonArray = new JSONArray();
                                        String data = "";
                                        JSONArray array = orderrow.getJSONArray("attribute");
                                        for (int i = 0; i < array.length(); i++) {
                                            JSONObject object = array.getJSONObject(i);
                                            JSONObject object1 = new JSONObject();
                                            object1.put("label", object.getString("attribute_label"));
                                            object1.put("attribute_id", object.getString("attribute_id"));
                                            object1.put("value_index", object.getString("value"));
                                            object1.put("is_percent", "0");
                                            object1.put("pricing_value", " ");
                                            object1.put("pricing_value", " ");
                                            data = object.getString("attribute_id") + "#" + object.getString("value") + data;
                                            jsonArray.put(object1);

                                        }
                                        if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                            Log.i("data1", "" + data);
                                        }
                                        Ced_MultiVendor_GlobalVariables.valuefordisableconfig.add(data);
                                        Ced_MultiVendor_GlobalVariables.configurable_data.put(orderrow.getString("product_id"), jsonArray);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    selectrelated.setChecked(true);
                                }

                            }
                            try {
                                JSONArray array = orderrow.getJSONArray("attribute");
                                String options = "";
                                for (int i = 0; i < array.length(); i++) {

                                    JSONObject object1 = array.getJSONObject(i);
                                    value_indexhash.put(object1.getString("value_label"), object1.getString("value"));
                                    value_indexhash.put(object1.getString("attribute_label"), object1.getString("attribute_id"));
                                    attributelabel_code.put(object1.getString("attribute_label"), object1.getString("attribute_code"));
                                    if (dataforattributeconfig.containsKey(object1.getString("attribute_label"))) {
                                        ArrayList<String> labelconfigattribute = dataforattributeconfig.get(object1.getString("attribute_label"));
                                        if (!(labelconfigattribute.contains(object1.getString("value_label")))) {
                                            labelconfigattribute.add(object1.getString("value_label"));
                                            dataforattributeconfig.put(object1.getString("attribute_label"), labelconfigattribute);
                                        }

                                    } else {
                                        ArrayList<String> labelconfigattribute = new ArrayList<>();
                                        labelconfigattribute.add(object1.getString("value_label"));
                                        dataforattributeconfig.put(object1.getString("attribute_label"), labelconfigattribute);
                                    }
                                    options = object1.getString("attribute_label") + ":" + object1.getString("value_label") + "\n" + options;
                                }
                                if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                    Log.i("options", "" + options);
                                }
                                count.setText(options);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            selectrelated.setText("#" + orderrow.getString("product_id"));
                            product_id.setText(orderrow.getString("product_id"));
                            sku.setText("#" + orderrow.getString("sku"));
                            RegularPrice.setText("#" + orderrow.getString("regular_price"));
                            Status.setText("#" + orderrow.getString("Inventory"));
                            attribuetsetname.setText("#" + orderrow.getString("set_name"));
                            json.setText(orderrow.toString());
                            manageproductlist.addView(related_row);
                        }
                        JSONArray sets = jsonObject.getJSONObject("data").getJSONArray("sets").getJSONArray(0);
                        for (int i = 0; i < sets.length(); i++) {
                            JSONObject jsonObject1 = sets.getJSONObject(i);
                            attributeidvalue.put(jsonObject1.getString("value"), jsonObject1.getString("id"));
                            attributevalueid.put(jsonObject1.getString("id"), jsonObject1.getString("value"));
                            if (!(attributenames.contains(jsonObject1.getString("value")))) {
                                attributenames.add(jsonObject1.getString("value"));
                            }
                        }
                        if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                            Log.i("dataforattributeconfig", "" + dataforattributeconfig);
                            Log.i("value_indexhash", "" + value_indexhash);
                        }
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
        }, Ced_MultiVendor_Configurable.this, "POST", dataforproducts);
        crr.execute(Currenturl);
        /***************************************************************************************/
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
