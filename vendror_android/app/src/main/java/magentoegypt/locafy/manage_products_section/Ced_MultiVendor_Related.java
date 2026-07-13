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
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import magentoegypt.locafy.base_app.Ced_Load_Language;
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

public class Ced_MultiVendor_Related extends Ced_MultiVendor_NavigationActivity {
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    TextView savecontinue;
    TextView save;
    String tabs;
    String product_id;
    String type;
    LinearLayout manageproductlist;
    String Currenturl;
    String updateurl;
    HashMap<String, String> dataforproducts;
    HashMap<String, String> dataforrelated;
    String Jstring;
    LinearLayout filtersection;
    Dialog listDialog;
    String datafilterjson = "";
    int current = 1;
    ArrayList<String> attributenames;
    HashMap<String, String> attributeidvalue;
    HashMap<String, String> attributevalueid;
    ArrayList<HashMap<String, String>> relatedproductlist;
    String attribute_set;
    Spinner sortby;
    InteractiveScrollView mainscroll;
    Ced_MultiVendor_FontSetting fontSetting;
    TextView pages;
    TextView products;
    TextView totalpro;
    JSONArray latest_order;
    boolean isLoading = true;
    String selected_websitetopost = "";

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
        dataforproducts = new HashMap<String, String>();
        dataforrelated = new HashMap<String, String>();
        attributeidvalue = new HashMap<String, String>();
        attributevalueid = new HashMap<String, String>();
        attributenames = new ArrayList<String>();
        relatedproductlist = new ArrayList<HashMap<String, String>>();
        latest_order = new JSONArray();
        fontSetting = new Ced_MultiVendor_FontSetting();
        selected_websitetopost = getIntent().getStringExtra("selectedwebsite");
        if (session.getStoreLocale() != null) {
            new Ced_Load_Language().setLanguagetoLoad(session.getStoreLocale(), this);
        }
        if (connectionDetector.isConnectingToInternet()) {
            try {
                setname(session.getvendorname());
                setprofilepic(session.getvendorpic());
                setTitle(getString(R.string.Related_Products));
                //setTitle("Producto relacionado");
                ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
                getLayoutInflater().inflate(R.layout.ced_multivendor_activity_related, content, true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);
                Currenturl = session.getBase_Url() + "vproductapi/vproducts/relatedProduct";
                updateurl = session.getBase_Url() + "vendorapi/vproducts/update";
                Bundle bundle = getIntent().getExtras();
                if (bundle != null) {
                    if (getIntent().getStringExtra("filter") != null) {
                        datafilterjson = getIntent().getStringExtra("filter");
                        dataforproducts.put("filter", datafilterjson);
                        if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                            Log.i("datafilterjson", "" + datafilterjson);
                        }
                    }
                    tabs = getIntent().getStringExtra("tabs");
                    product_id = getIntent().getStringExtra("product_id");
                    type = getIntent().getStringExtra("type");
                    attribute_set = getIntent().getStringExtra("attribute_set");
                    if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                        Log.i("attribute_set", "" + attribute_set);
                    }
                }
                /**************************************************************/
                pages = findViewById(R.id.MultiVendor_pages);
                totalpro = findViewById(R.id.MultiVendor_totalpro);
                products = findViewById(R.id.MultiVendor_products);
                save = findViewById(R.id.MultiVendor_save);
                sortby = findViewById(R.id.MultiVendor_sortby);
                savecontinue = findViewById(R.id.MultiVendor_savecontinue);
                fontSetting.setFontforTextviews(save, "Roboto-Medium.ttf", getApplicationContext());
                fontSetting.setFontforTextviews(savecontinue, "Roboto-Medium.ttf", getApplicationContext());
                fontSetting.setFontforTextviews(pages, "Roboto-Medium.ttf", getApplicationContext());
                fontSetting.setFontforTextviews(products, "Roboto-Medium.ttf", getApplicationContext());
                fontSetting.setFontforTextviews(totalpro, "Roboto-Regular.ttf", getApplicationContext());
                manageproductlist = findViewById(R.id.MultiVendor_manageproductlist);
                filtersection = findViewById(R.id.MultiVendor_filtersection);
                mainscroll = findViewById(R.id.MultiVendor_mainscroll);
                filtersection.setOnClickListener(v -> showfilter());
                if (session.getStoreId() != null)
                    dataforproducts.put("store_id", session.getStoreId());
                dataforproducts.put("vendor_id", session.getVendorid());
                dataforproducts.put("product_id", product_id);
                dataforrelated.put("websites", selected_websitetopost);
                dataforproducts.put("hashkey", session.getHahkey());
                if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                    Log.i("dataforproducts", "" + dataforproducts);
                }
                applydata(current);
        /*        sortby.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                    {

                        current = sortby.getSelectedItem().toString();
                        if(manageproductlist.getChildCount()>0)
                        {
                            manageproductlist.removeAllViews();
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });*/
                //mainscroll.setSmoothScrollingEnabled(true);
                mainscroll.setOnBottomReachedListener(() -> {
                    if (isLoading) {
                        current++;
                        ScrollData(current);
                    }

                });
                save.setOnClickListener(v -> {

                    dataforrelated.put("product_id", product_id);
                    dataforrelated.put("websites", selected_websitetopost);
                    dataforrelated.put("vendor_id", session.getVendorid());
                    dataforrelated.put("hashkey", session.getHahkey());

                    try {
                        if (Ced_MultiVendor_GlobalVariables.relatedproductids.size() > 0) {
                            String dataforrelatedstring = "";
                            Iterator iterator = new Ced_MultiVendor_GlobalVariables().relatedproductids.iterator();
                            while (iterator.hasNext()) {
                                String value = (String) iterator.next();
                                dataforrelatedstring = value + dataforrelatedstring;

                            }
                            JSONObject object = new JSONObject();
                            object.put("related", dataforrelatedstring);
                            JSONObject object1 = new JSONObject();
                            object1.put("links", object);
                            dataforrelated.put("relatedproducts", object1.toString());
//                                dataforrelated.put("delete_category", "false");

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
                        }, Ced_MultiVendor_Related.this, "POST", dataforrelated);
                        crr.execute(updateurl);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                });
                savecontinue.setOnClickListener(v -> {
                    try {

                        dataforrelated.put("product_id", product_id);
                        dataforrelated.put("vendor_id", session.getVendorid());
                        dataforrelated.put("hashkey", session.getHahkey());

                        try {
                            if (Ced_MultiVendor_GlobalVariables.relatedproductids.size() > 0) {
                                String dataforrelatedstring = "";
                                Iterator iterator = new Ced_MultiVendor_GlobalVariables().relatedproductids.iterator();
                                while (iterator.hasNext()) {
                                    String value = (String) iterator.next();
                                    dataforrelatedstring = value + dataforrelatedstring;

                                }
                                JSONObject object = new JSONObject();
                                object.put("related", dataforrelatedstring);
                                JSONObject object1 = new JSONObject();
                                object1.put("links", object);
                                dataforrelated.put("relatedproducts", object1.toString());
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
                                            JSONObject tab = new JSONObject(tabs);
                                            if (tab.has("upsell")) {
                                                Intent related = new Intent(Ced_MultiVendor_Related.this, Ced_MultiVendor_upsell.class);
                                                related.putExtra("tabs", tab.toString());
                                                related.putExtra("product_id", product_id);
                                                related.putExtra("selectedwebsite", selected_websitetopost);
                                                related.putExtra("type", type);
                                                related.putExtra("attribute_set", attribute_set);
                                                startActivity(related);
                                                overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                                            } else {
                                                if (tab.has("crosssell")) {
                                                    Intent related = new Intent(Ced_MultiVendor_Related.this, Ced_MultiVendor_crosssell.class);
                                                    related.putExtra("tabs", tab.toString());
                                                    related.putExtra("product_id", product_id);
                                                    related.putExtra("selectedwebsite", selected_websitetopost);
                                                    related.putExtra("type", type);
                                                    related.putExtra("attribute_set", attribute_set);
                                                    startActivity(related);
                                                    overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                                                } else {
                                                    if (tab.has("customer_options")) {
                                                        Intent related = new Intent(Ced_MultiVendor_Related.this, Ced_MultiVendor_CustomOption.class);
                                                        related.putExtra("tabs", tab.toString());
                                                        related.putExtra("product_id", product_id);
                                                        related.putExtra("selectedwebsite", selected_websitetopost);
                                                        related.putExtra("type", type);
                                                        related.putExtra("attribute_set", attribute_set);
                                                        startActivity(related);
                                                        overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                                                    } else {
                                                        if (type.equals("configurable")) {
                                                            Intent related = new Intent(getApplicationContext(), Ced_MultiVendor_Configurable.class);
                                                            related.putExtra("tabs", tabs);
                                                            related.putExtra("product_id", product_id);
                                                            related.putExtra("selectedwebsite", selected_websitetopost);
                                                            related.putExtra("type", type);
                                                            related.putExtra("attribute_set", attribute_set);
                                                            startActivity(related);
                                                            overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                                                        }
                                                        if (type.equals("downloadable")) {
                                                            Intent related = new Intent(getApplicationContext(), Ced_MultiVendor_Downloadable.class);
                                                            related.putExtra("tabs", tabs);
                                                            related.putExtra("product_id", product_id);
                                                            related.putExtra("selectedwebsite", selected_websitetopost);
                                                            related.putExtra("type", type);
                                                            related.putExtra("attribute_set", attribute_set);
                                                            startActivity(related);
                                                            overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                                                        }
                                                        if (type.equals("bundle")) {
                                                            Intent related = new Intent(getApplicationContext(), Ced_MultiVendor_BundleItems.class);
                                                            related.putExtra("tabs", tabs);
                                                            related.putExtra("product_id", product_id);
                                                            related.putExtra("selectedwebsite", selected_websitetopost);
                                                            related.putExtra("type", type);
                                                            related.putExtra("attribute_set", attribute_set);
                                                            startActivity(related);
                                                            overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                                                        }
                                                        if (type.equals("grouped")) {
                                                            Intent related = new Intent(getApplicationContext(), Ced_MultiVendor_GroupItems.class);
                                                            related.putExtra("tabs", tabs);
                                                            related.putExtra("product_id", product_id);
                                                            related.putExtra("selectedwebsite", selected_websitetopost);
                                                            related.putExtra("type", type);
                                                            related.putExtra("attribute_set", attribute_set);
                                                            startActivity(related);
                                                            overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                                                        }
                                                        if (type.equals("simple")) {
                                                            Ced_MultiVendor_GlobalVariables globalVariables = new Ced_MultiVendor_GlobalVariables();
                                                            globalVariables.clearallvalues();
                                                            Intent related = new Intent(getApplicationContext(), Ced_MultiVendor_ManageProducts.class);
                                                            related.putExtra("Navigation", "productcreate");
                                                            related.putExtra("selectedwebsite", selected_websitetopost);
                                                            startActivity(related);
                                                            overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                                                        }
                                                        if (type.equals("virtual")) {
                                                            Ced_MultiVendor_GlobalVariables globalVariables = new Ced_MultiVendor_GlobalVariables();
                                                            globalVariables.clearallvalues();
                                                            Intent related = new Intent(getApplicationContext(), Ced_MultiVendor_ManageProducts.class);
                                                            related.putExtra("Navigation", "productcreate");
                                                            related.putExtra("selectedwebsite", selected_websitetopost);
                                                            startActivity(related);
                                                            overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                                                        }
                                                    }
                                                }
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
                            }, Ced_MultiVendor_Related.this, "POST", dataforrelated);
                            crr.execute(updateurl);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                /********************************************************************************/
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Intent nointernet = new Intent(Ced_MultiVendor_Related.this, Ced_MultiVendor_NoInternetconnection.class);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(nointernet);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }


    }


    private void ScrollData(int current) {
        /**************************************GetRelatedProducts*************************************************/
        Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(output -> {
            Jstring = output.toString();
            if (functionalityList.getExtensionAddon()) {
                JSONObject jsonObject = new JSONObject(Jstring);
                if (jsonObject.has("vendor_approved")) {
                    logout();
                } else {
                    String success = jsonObject.getJSONObject("data").getString("success");
                    if (success.equals("true")) {
                        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("products");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            latest_order.put(jsonObject1);
                        }

                        totalpro.setText(jsonObject.getJSONObject("data").getString("count"));
                        for (int l = 0; l < latest_order.length(); l++) {
                            View related_row = View.inflate(getApplicationContext(), R.layout.ced_multivendor_relatedproductlist, null);
                            JSONObject orderrow = latest_order.getJSONObject(l);
                            TextView productIDtag = related_row.findViewById(R.id.MultiVendor_productIDtag);
                            TextView skutag = related_row.findViewById(R.id.MultiVendor_skutag);
                            TextView Productypetag = related_row.findViewById(R.id.MultiVendor_Productypetag);
                            TextView RegularPricetag = related_row.findViewById(R.id.MultiVendor_RegularPricetag);
                            TextView ProductNametag = related_row.findViewById(R.id.MultiVendor_ProductNametag);
                            TextView statustag = related_row.findViewById(R.id.MultiVendor_statustag);
                            TextView attributesetnametag = related_row.findViewById(R.id.MultiVendor_attributesetnametag);

                            TextView product_id = related_row.findViewById(R.id.MultiVendor_product_id);
                            TextView sku = related_row.findViewById(R.id.MultiVendor_sku);
                            TextView Productype = related_row.findViewById(R.id.MultiVendor_Productype);
                            TextView RegularPrice = related_row.findViewById(R.id.MultiVendor_RegularPrice);
                            TextView product_name = related_row.findViewById(R.id.MultiVendor_product_name);
                            TextView Status = related_row.findViewById(R.id.MultiVendor_Status);
                            TextView attribuetsetname = related_row.findViewById(R.id.MultiVendor_attribuetsetname);
                            /********************************fonts***********************************/
                            fontSetting.setFontforTextviews(productIDtag, "Roboto-Medium.ttf", getApplicationContext());
                            fontSetting.setFontforTextviews(skutag, "Roboto-Medium.ttf", getApplicationContext());
                            fontSetting.setFontforTextviews(Productypetag, "Roboto-Medium.ttf", getApplicationContext());
                            fontSetting.setFontforTextviews(RegularPricetag, "Roboto-Medium.ttf", getApplicationContext());
                            fontSetting.setFontforTextviews(ProductNametag, "Roboto-Medium.ttf", getApplicationContext());
                            fontSetting.setFontforTextviews(statustag, "Roboto-Medium.ttf", getApplicationContext());
                            fontSetting.setFontforTextviews(attributesetnametag, "Roboto-Medium.ttf", getApplicationContext());

                            fontSetting.setFontforTextviews(product_id, "Roboto-Regular.ttf", getApplicationContext());
                            fontSetting.setFontforTextviews(sku, "Roboto-Regular.ttf", getApplicationContext());
                            fontSetting.setFontforTextviews(Productype, "Roboto-Regular.ttf", getApplicationContext());
                            fontSetting.setFontforTextviews(RegularPrice, "Roboto-Regular.ttf", getApplicationContext());
                            fontSetting.setFontforTextviews(product_name, "Roboto-Regular.ttf", getApplicationContext());
                            fontSetting.setFontforTextviews(Status, "Roboto-Regular.ttf", getApplicationContext());
                            fontSetting.setFontforTextviews(attribuetsetname, "Roboto-Regular.ttf", getApplicationContext());

                            /********************************fonts***********************************/

                            final CheckBox selectrelated = related_row.findViewById(R.id.MultiVendor_selectrelated);
                            fontSetting.setfontforCheckbox(selectrelated, "Roboto-Medium.ttf", getApplicationContext());
                            if (Ced_MultiVendor_GlobalVariables.relatedproductids.contains("#" + orderrow.getString("product_id"))) {
                                selectrelated.setChecked(true);
                            }
                            if (orderrow.getString("selected").equals("true")) {
                                if (!(Ced_MultiVendor_GlobalVariables.relatedproductids.contains("#" + orderrow.getString("product_id")))) {
                                    Ced_MultiVendor_GlobalVariables.relatedproductids.add("#" + orderrow.getString("product_id"));
                                    selectrelated.setChecked(true);
                                }

                            }
                            selectrelated.setOnCheckedChangeListener((buttonView, isChecked) -> {
                                LinearLayout linearLayout = (LinearLayout) selectrelated.getParent();
                                LinearLayout firstchild = (LinearLayout) linearLayout.getChildAt(1);
                                LinearLayout thirdchild = (LinearLayout) firstchild.getChildAt(2);
                                TextView proid = (TextView) thirdchild.getChildAt(0);
                                if (isChecked) {
                                    if (!(Ced_MultiVendor_GlobalVariables.relatedproductids.contains(proid.getText().toString()))) {
                                        Ced_MultiVendor_GlobalVariables.relatedproductids.add(proid.getText().toString());
                                    }
                                    if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                        Log.i("relatedproductids", "" + Ced_MultiVendor_GlobalVariables.relatedproductids);
                                    }
                                } else {
                                    Ced_MultiVendor_GlobalVariables.relatedproductids.remove(proid.getText().toString());
                                    if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                        Log.i("relatedproductids", "" + Ced_MultiVendor_GlobalVariables.relatedproductids);
                                    }
                                }
                            });

                            selectrelated.setText("#" + orderrow.getString("product_name"));
                            product_id.setText("#" + orderrow.getString("product_id"));
                            sku.setText(orderrow.getString("sku"));
                            Productype.setText(orderrow.getString("type"));
                            RegularPrice.setText(orderrow.getString("regular_price"));
                            product_name.setText(orderrow.getString("product_name"));
                            Status.setText(orderrow.getString("status"));
                            attribuetsetname.setText(orderrow.getString("set_name"));
                            manageproductlist.addView(related_row);
                        }
                        manageproductlist.setOnScrollChangeListener((view, i, i1, i2, i3) -> {

                        });

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
                        if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                            Log.i("attributeidvalue", "" + attributeidvalue);
                            Log.i("attributevalueid", "" + attributevalueid);
                        }

                    } else {
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
            } else {
                Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
            }
        }, Ced_MultiVendor_Related.this, "POST", dataforproducts);
        crr.execute(Currenturl + "/page/" + current);
        /***************************************************************************************/
    }


    private void applydata(int current) {
        /**************************************GetRelatedProducts*************************************************/
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
                        totalpro.setText(jsonObject.getJSONObject("data").getString("count"));
                        for (int l = 0; l < latest_order.length(); l++) {
                            View related_row = View.inflate(getApplicationContext(), R.layout.ced_multivendor_relatedproductlist, null);
                            JSONObject orderrow = latest_order.getJSONObject(l);
                            TextView productIDtag = related_row.findViewById(R.id.MultiVendor_productIDtag);
                            TextView skutag = related_row.findViewById(R.id.MultiVendor_skutag);
                            TextView Productypetag = related_row.findViewById(R.id.MultiVendor_Productypetag);
                            TextView RegularPricetag = related_row.findViewById(R.id.MultiVendor_RegularPricetag);
                            TextView ProductNametag = related_row.findViewById(R.id.MultiVendor_ProductNametag);
                            TextView statustag = related_row.findViewById(R.id.MultiVendor_statustag);
                            TextView attributesetnametag = related_row.findViewById(R.id.MultiVendor_attributesetnametag);

                            TextView product_id = related_row.findViewById(R.id.MultiVendor_product_id);
                            TextView sku = related_row.findViewById(R.id.MultiVendor_sku);
                            TextView Productype = related_row.findViewById(R.id.MultiVendor_Productype);
                            TextView RegularPrice = related_row.findViewById(R.id.MultiVendor_RegularPrice);
                            TextView product_name = related_row.findViewById(R.id.MultiVendor_product_name);
                            TextView Status = related_row.findViewById(R.id.MultiVendor_Status);
                            TextView attribuetsetname = related_row.findViewById(R.id.MultiVendor_attribuetsetname);
                            /********************************fonts***********************************/
                            fontSetting.setFontforTextviews(productIDtag, "Roboto-Medium.ttf", getApplicationContext());
                            fontSetting.setFontforTextviews(skutag, "Roboto-Medium.ttf", getApplicationContext());
                            fontSetting.setFontforTextviews(Productypetag, "Roboto-Medium.ttf", getApplicationContext());
                            fontSetting.setFontforTextviews(RegularPricetag, "Roboto-Medium.ttf", getApplicationContext());
                            fontSetting.setFontforTextviews(ProductNametag, "Roboto-Medium.ttf", getApplicationContext());
                            fontSetting.setFontforTextviews(statustag, "Roboto-Medium.ttf", getApplicationContext());
                            fontSetting.setFontforTextviews(attributesetnametag, "Roboto-Medium.ttf", getApplicationContext());

                            fontSetting.setFontforTextviews(product_id, "Roboto-Regular.ttf", getApplicationContext());
                            fontSetting.setFontforTextviews(sku, "Roboto-Regular.ttf", getApplicationContext());
                            fontSetting.setFontforTextviews(Productype, "Roboto-Regular.ttf", getApplicationContext());
                            fontSetting.setFontforTextviews(RegularPrice, "Roboto-Regular.ttf", getApplicationContext());
                            fontSetting.setFontforTextviews(product_name, "Roboto-Regular.ttf", getApplicationContext());
                            fontSetting.setFontforTextviews(Status, "Roboto-Regular.ttf", getApplicationContext());
                            fontSetting.setFontforTextviews(attribuetsetname, "Roboto-Regular.ttf", getApplicationContext());

                            /********************************fonts***********************************/

                            final CheckBox selectrelated = related_row.findViewById(R.id.MultiVendor_selectrelated);
                            fontSetting.setfontforCheckbox(selectrelated, "Roboto-Medium.ttf", getApplicationContext());
                            if (Ced_MultiVendor_GlobalVariables.relatedproductids.contains("#" + orderrow.getString("product_id"))) {
                                selectrelated.setChecked(true);
                            }
                            if (orderrow.getString("selected").equals("true")) {
                                if (!(Ced_MultiVendor_GlobalVariables.relatedproductids.contains("#" + orderrow.getString("product_id")))) {
                                    Ced_MultiVendor_GlobalVariables.relatedproductids.add("#" + orderrow.getString("product_id"));
                                    selectrelated.setChecked(true);
                                }

                            }
                            selectrelated.setOnCheckedChangeListener((buttonView, isChecked) -> {
                                LinearLayout linearLayout = (LinearLayout) selectrelated.getParent();
                                LinearLayout firstchild = (LinearLayout) linearLayout.getChildAt(1);
                                LinearLayout thirdchild = (LinearLayout) firstchild.getChildAt(2);
                                TextView proid = (TextView) thirdchild.getChildAt(0);
                                if (isChecked) {
                                    if (!(Ced_MultiVendor_GlobalVariables.relatedproductids.contains(proid.getText().toString()))) {
                                        Ced_MultiVendor_GlobalVariables.relatedproductids.add(proid.getText().toString());
                                    }
                                    if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                        Log.i("relatedproductids", "" + Ced_MultiVendor_GlobalVariables.relatedproductids);
                                    }
                                } else {
                                    Ced_MultiVendor_GlobalVariables.relatedproductids.remove(proid.getText().toString());
                                    if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                        Log.i("relatedproductids", "" + Ced_MultiVendor_GlobalVariables.relatedproductids);
                                    }
                                }
                            });

                            selectrelated.setText("#" + orderrow.getString("product_name"));
                            product_id.setText("#" + orderrow.getString("product_id"));
                            sku.setText(orderrow.getString("sku"));
                            Productype.setText(orderrow.getString("type"));
                            RegularPrice.setText(orderrow.getString("regular_price"));
                            product_name.setText(orderrow.getString("product_name"));
                            Status.setText(orderrow.getString("status"));
                            attribuetsetname.setText(orderrow.getString("set_name"));
                            manageproductlist.addView(related_row);
                        }
                        manageproductlist.setOnScrollChangeListener((view, i, i1, i2, i3) -> {

                        });

                        JSONArray sets = jsonObject.getJSONObject("data").getJSONArray("sets")/*.getJSONArray(0)*/;
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
                        if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                            Log.i("attributeidvalue", "" + attributeidvalue);
                            Log.i("attributevalueid", "" + attributevalueid);
                        }

                    }
                    else {
                        if (jsonObject.getJSONObject("data").has("sets")) {
                            JSONArray sets = jsonObject.getJSONObject("data").getJSONArray("sets").getJSONArray(0);
                            for (int i = 0; i < sets.length(); i++) {
                                JSONObject jsonObject1 = sets.getJSONObject(i);
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
            } else {
                Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
            }
        }, Ced_MultiVendor_Related.this, "POST", dataforproducts);
        crr.execute(Currenturl + "/page/" + current);
        /***************************************************************************************/
    }

    @Override
    protected void onResume() {
        Ced_MultiVendor_ConnectionDetector connectionDetector = new Ced_MultiVendor_ConnectionDetector(Ced_MultiVendor_Related.this);
        if (connectionDetector.isConnectingToInternet()) {
            setname(session.getvendorname());
            setprofilepic(session.getvendorpic());
            changetitle(getString(R.string.Related_Products));
            //      invalidateOptionsMenu();
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
            View v = li.inflate(R.layout.ced_multivendor_header_at_relatedproductlisting, null, false);
            final TextView setfilter = v.findViewById(R.id.MultiVendor_setfilter);//
            final TextView unsetfilter = v.findViewById(R.id.MultiVendor_unsetfilter);//
            final EditText from_price = v.findViewById(R.id.MultiVendor_from_price);//
            final EditText to_price = v.findViewById(R.id.MultiVendor_to_price);//
            final EditText to_product_id = v.findViewById(R.id.MultiVendor_edt_product_id);//
            final EditText edt_productname = v.findViewById(R.id.MultiVendor_edt_productname);//
            final EditText edt_productsku = v.findViewById(R.id.MultiVendor_edt_productsku);//
            final Spinner edt_status = v.findViewById(R.id.MultiVendor_edt_status);//
            final Spinner edt_producttype = v.findViewById(R.id.MultiVendor_edt_producttype);//

            final Spinner edt_attributesetname = v.findViewById(R.id.MultiVendor_edt_attributesetname);//
            ArrayAdapter<String> adp_attribute = new ArrayAdapter<String>(Ced_MultiVendor_Related.this, android.R.layout.simple_dropdown_item_1line, attributenames);
            edt_attributesetname.setAdapter(adp_attribute);
            if (!(datafilterjson.isEmpty())) {
                JSONObject object = new JSONObject(datafilterjson);
                from_price.setText(object.getJSONObject("price").getString("from"));
                to_price.setText(object.getJSONObject("price").getString("to"));
                to_product_id.setText(object.getString("entity_id"));
                edt_productname.setText(object.getString("name"));
                edt_productsku.setText(object.getString("sku"));
                if (object.getString("status").equals("Enabled")) {
                    edt_status.setSelection(1);
                }
                if (object.getString("status").equals("Disabled")) {
                    edt_status.setSelection(0);
                }
                if (object.getString("status").equals("Disapproved")) {
                    edt_status.setSelection(2);
                }
                if (object.getString("type_id").equals("Simple")) {
                    edt_producttype.setSelection(1);
                }
                if (object.getString("type_id").equals("Virtual")) {
                    edt_producttype.setSelection(2);
                }
                if (object.getString("type_id").equals("Downloadable")) {
                    edt_producttype.setSelection(3);
                }
                if (object.getString("type_id").equals("Grouped")) {
                    edt_producttype.setSelection(4);
                }
                if (object.getString("type_id").equals("Bundle")) {
                    edt_producttype.setSelection(5);
                }
                ArrayList<String> attributes = new ArrayList<String>();
                // Log.i("attributes", "" + attributevalueid.get(object.getString("attribute_set_id")));
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
                    ArrayAdapter<String> adp_attribute_1 = new ArrayAdapter<String>(Ced_MultiVendor_Related.this, android.R.layout.simple_dropdown_item_1line, attributes);
                    edt_attributesetname.setAdapter(adp_attribute_1);
                }


            }
            unsetfilter.setOnClickListener(v12 -> {
                from_price.setText("");
                to_price.setText("");
                to_product_id.setText("");
                edt_productname.setText("");
                edt_productsku.setText("");
                edt_producttype.setSelection(0);
                edt_status.setSelection(0);
                edt_attributesetname.setSelection(0);
                datafilterjson = "";
                final JSONObject mainfilter = new JSONObject();
                JSONObject pricejsonObject = new JSONObject();
                try {
                    listDialog.dismiss();
                  /*  pricejsonObject.put("from", from_price.getText().toString());
                    pricejsonObject.put("to", to_price.getText().toString());
                    mainfilter.put("price", pricejsonObject);
                    mainfilter.put("entity_id", to_product_id.getText().toString());
                    mainfilter.put("name", edt_productname.getText().toString());
                    mainfilter.put("status", edt_status.getSelectedItem());
                    mainfilter.put("sku", edt_productsku.getText().toString());
                    mainfilter.put("type_id", edt_producttype.getSelectedItem());
                    mainfilter.put("attribute_set_id", " ");*/
                    Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_Related.class);
                    //     intent.putExtra("filter", mainfilter.toString());
                    intent.putExtra("tabs", tabs);
                    intent.putExtra("product_id", product_id);
                    intent.putExtra("type", type);
                    intent.putExtra("attribute_set", attribute_set);
                    intent.putExtra("selectedwebsite", selected_websitetopost);
                    intent.putExtra("attributenames", attributenames.toString());
                    intent.putExtra("attributevalueid", attributevalueid.toString());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                } catch (Exception e) {
                    Intent main = new Intent(Ced_MultiVendor_Related.this, Ced_MultiVendor_VendorSplash.class);
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
                    mainfilter.put("status", edt_status.getSelectedItem());
                    mainfilter.put("sku", edt_productsku.getText().toString());
                    mainfilter.put("type_id", edt_producttype.getSelectedItem());
                    if (attributenames.size() > 0) {
                        mainfilter.put("attribute_set_id", attributeidvalue.get(edt_attributesetname.getSelectedItem()));
                    } else {
                        mainfilter.put("attribute_set_id", " ");
                    }
                    Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_Related.class);
                    intent.putExtra("filter", mainfilter.toString());
                    intent.putExtra("tabs", tabs);
                    intent.putExtra("product_id", product_id);
                    intent.putExtra("type", type);
                    intent.putExtra("attribute_set", attribute_set);
                    intent.putExtra("selectedwebsite", selected_websitetopost);
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
