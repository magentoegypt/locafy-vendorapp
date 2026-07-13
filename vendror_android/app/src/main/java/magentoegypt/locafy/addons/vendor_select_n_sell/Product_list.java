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

package magentoegypt.locafy.addons.vendor_select_n_sell;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import magentoegypt.locafy.base_app.Ced_MultiVendor_FontSetting;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorSplash;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_UnAuthourizedRequestError;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;
import magentoegypt.locafy.R;
import magentoegypt.locafy_constant.AppConstant;
import magentoegypt.locafy.manage_products_section.model.SellFilterModel;
import magentoegypt.locafy.manage_products_section.model.Status;
import magentoegypt.locafy.manage_products_section.model.Website;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by cedcoss on 2/2/17.
 */
public class Product_list extends Ced_MultiVendor_NavigationActivity {
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    Ced_MultiVendor_VendorSessionManagement vendorSessionManagement;
    //    ListView manageproductlist;
    RecyclerView manageproductlist;
    String Currenturl;
    HashMap<String, String> dataforproducts;
    String Jstring;
    LinearLayout filtersection;
    Button addnewproduct;
    Dialog listDialog;
    String datafilterjson = "";
    Ced_MultiVendor_FontSetting fontSetting;
    TextView countsproducts;
    ArrayList<HashMap<String, String>> latestfiveorderlist;
    int old_size = 0;
    int current = 1;
    boolean load = true;
    String navigation = " ";
    List<Status> statusList;
    List<Website> websiteList;
    String out;
    String filter_url = "";
    String status_value = "";
    String website_value = "";
    LinearLayoutManager linearLayoutManager;
    private int page_num = 1;
    private boolean firstcall = true;
    New_Ced_MultiVendor_ProductListSelectNSellAdapter productListAdapter;
    private JSONObject status_json_object = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_bar_color));
        }
        fontSetting = new Ced_MultiVendor_FontSetting();
        connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(getApplicationContext());
        vendorSessionManagement = new Ced_MultiVendor_VendorSessionManagement(getApplicationContext());
        dataforproducts = new HashMap<String, String>();
        if (connectionDetector.isConnectingToInternet()) {
            setname(vendorSessionManagement.getvendorname());
            setprofilepic(vendorSessionManagement.getvendorpic());
            changetitle("Select and Sell");
            Currenturl = session.getBase_Url() + "vmultisellerapi/product/item";
            filter_url = session.getBase_Url() + "vmultisellerapi/product/ConfigVal";
            ViewGroup content = (ViewGroup) findViewById(R.id.MultiVendor_frame_container);
            getLayoutInflater().inflate(R.layout.ced_multivendor_activity_add_products_list_selectnsel, content, true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);

            linearLayoutManager = new LinearLayoutManager(this);
            manageproductlist = findViewById(R.id.MultiVendor_manageproductlist);
            manageproductlist.setLayoutManager(linearLayoutManager);

            filtersection = (LinearLayout) findViewById(R.id.MultiVendor_filtersection);

            countsproducts = (TextView) findViewById(R.id.MultiVendor_countsproducts);
            addnewproduct = (Button) findViewById(R.id.MultiVendor_addnewproduct);
            fontSetting.setfontforButtons(addnewproduct, "Roboto-Medium.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(countsproducts, "Roboto-Medium.ttf", getApplicationContext());

            filtersection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppConstant.lockButton(v);
                    showfilter();
                }
            });
            addnewproduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppConstant.lockButton(v);
                    Intent add = new Intent(Product_list.this, Add_Product_list.class);
                    startActivity(add);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }
            });
            if (getIntent().getStringExtra("filter") != null) {
                datafilterjson = getIntent().getStringExtra("filter");
                dataforproducts.put("filter", datafilterjson);
            }
            dataforproducts.put("vendor_id", vendorSessionManagement.getVendorid());
            dataforproducts.put("hashkey", vendorSessionManagement.getHahkey());
            dataforproducts.put("vendor_id", vendorSessionManagement.getVendorid());

           /* dataforproducts.put("hashkey", "2a1f678300166f9b941de29a25f4a4bf");
            dataforproducts.put("vendor_id", "1");*/
            Ced_MultiVendor_ClientRequestResponse request = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
                @Override
                public void processFinish(Object output) throws JSONException {
                    SellFilterModel sellFilterModel = new Gson().fromJson(output.toString(), SellFilterModel.class);
                    websiteList = sellFilterModel.getWebsite();
                    statusList = sellFilterModel.getStatus();

                }
            }, Product_list.this, "GET");
            request.execute(filter_url);
            request();
        } else {
            Intent nointernet = new Intent(Product_list.this, Ced_MultiVendor_NoInternetconnection.class);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(nointernet);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }
    }

    private void request() {
        /*Toast.makeText(this, "Request 1", Toast.LENGTH_SHORT).show();*/
        Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                out = output.toString();
                get_data();
            }
        }, Product_list.this, "POST", dataforproducts);
        response.execute(Currenturl + "/page/" + current);
    }

    private void get_data() throws JSONException {
        latestfiveorderlist = new ArrayList<HashMap<String, String>>();
        if (functionalityList.getExtensionAddon()) {
            JSONObject jsonObject = new JSONObject(out);
            if (jsonObject.has("vendor_approved")) {
                logout();
            } else {
                String success = jsonObject.getJSONObject("data").getString("success");
                if (success.equals("true")) {
                    JSONArray latest_order = jsonObject.getJSONObject("data").getJSONArray("product_array");
                    if (jsonObject.getJSONObject("data").has("product_status")) {
                        status_json_object = jsonObject.getJSONObject("data").getJSONObject("product_status");
                    }
                    for (int l = 0; l < latest_order.length(); l++) {
                        JSONObject orderrow = latest_order.getJSONObject(l);
                        HashMap<String, String> dataofrow = new HashMap<String, String>();
                        dataofrow.put("product_id", "#" + orderrow.getString("entity_id"));
                        dataofrow.put("type", "1");
                        dataofrow.put("product_name", orderrow.getString("name"));
                        dataofrow.put("regular_price", orderrow.getString("price"));
                        dataofrow.put("product_image", orderrow.getString("image"));
                        dataofrow.put("qty", orderrow.getString("qty"));

                        dataofrow.put("status", orderrow.getString("status"));

                        dataofrow.put("sku", orderrow.getString("sku"));
                        if (orderrow.has("websites")) {
                            if (orderrow.getString("websites").length() > 0) {
                                if (orderrow.getString("websites").equals("1")) {
                                    dataofrow.put("website", "Main website");
                                } else {
                                    dataofrow.put("website", "Default Website");
                                }
                            }
                        }
                        latestfiveorderlist.add(dataofrow);
                    }
                    int size = latestfiveorderlist.size();
                    if (size > 0) {
                        old_size = old_size + size;
                        countsproducts.setVisibility(View.VISIBLE);
                        countsproducts.setText(getString(R.string.showing_txt) + " " + old_size + getString(R.string.products_txt));
                    }
//                    Ced_MultiVendor_ProductListSelectNSellAdapter productListAdapter = new Ced_MultiVendor_ProductListSelectNSellAdapter(Product_list.this, latestfiveorderlist);
//                    manageproductlist.setDivider(new ColorDrawable(getResources().getColor(R.color.transparent)));
//                    manageproductlist.setAdapter(productListAdapter);
//                    manageproductlist.setDividerHeight(0);

                    if (firstcall) {
                        firstcall = false;
                        productListAdapter = new New_Ced_MultiVendor_ProductListSelectNSellAdapter(Product_list.this, latestfiveorderlist,status_json_object);
                        manageproductlist.setAdapter(productListAdapter);
                    } else {
                        productListAdapter.data.addAll(latestfiveorderlist);
                    }
                    productListAdapter.notifyDataSetChanged();
                } else {
                    if (firstcall) {
                        if (jsonObject.getJSONObject("data").has("message")) {
                            Toast.makeText(getApplicationContext(), jsonObject.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
                            countsproducts.setVisibility(View.VISIBLE);
                            countsproducts.setText(jsonObject.getJSONObject("data").getString("message"));
                        }
                    } else {
                        Toast.makeText(Product_list.this, getResources().getString(R.string.nomoredata), Toast.LENGTH_SHORT).show();
                        Log.i("REpo", "" + jsonObject.getJSONObject("data").getString("message"));
                    }
                }
            }
        } else {
            Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }

        manageproductlist.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Log.i("REpo", "1 canscroll");
                    current = current + 1;
                    try {
                        request();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

//        manageproductlist.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//
//                if (firstVisibleItem == 0) {
//                    getSupportActionBar().show();
//                } else {
//                    getSupportActionBar().hide();
//                }
//                if ((firstVisibleItem + visibleItemCount) != 0) {
//
//                    if (((firstVisibleItem + visibleItemCount) == totalItemCount) && load) {
//                        current = current + 1;
//                        load = false;
//                        Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
//
//                            @Override
//                            public void processFinish(Object output) throws JSONException {
//                                Jstring = output.toString();
//                                if (functionalityList.getExtensionAddon()) {
//                                    JSONObject jsonObject = new JSONObject(Jstring);
//                                    if (jsonObject.has("vendor_approved")) {
//                                        logout();
//                                    } else {
//                                        String success = jsonObject.getJSONObject("data").getString("success");
//                                        if (success.equals("true")) {
//                                            JSONArray latest_order = jsonObject.getJSONObject("data").getJSONArray("product_array");
//
//                                            for (int l = 0; l < latest_order.length(); l++) {
//                                                JSONObject orderrow = latest_order.getJSONObject(l);
//                                                HashMap<String, String> dataofrow = new HashMap<String, String>();
//                                                dataofrow.put("product_id", "#" + orderrow.getString("entity_id"));
//                                                dataofrow.put("type", orderrow.getString("type"));
//                                                dataofrow.put("product_name", orderrow.getString("name"));
//                                                dataofrow.put("regular_price", orderrow.getString("price"));
//                                                dataofrow.put("product_image", orderrow.getString("image"));
//                                                dataofrow.put("qty", orderrow.getString("qty"));
//                                                dataofrow.put("status", orderrow.getString("status"));
//                                                dataofrow.put("sku", orderrow.getString("sku"));
//                                                latestfiveorderlist.add(dataofrow);
//                                            }
//                                            int size = latestfiveorderlist.size();
//                                            if (size > 0) {
//                                                countsproducts.setVisibility(View.VISIBLE);
//                                                countsproducts.setText(getString(R.string.showing_txt) + " " + size + getString(R.string.products_txt));
//                                            }
//                                            Ced_MultiVendor_ProductListSelectNSellAdapter productListAdapter = new Ced_MultiVendor_ProductListSelectNSellAdapter(Product_list.this, latestfiveorderlist);
//                                            int cp = manageproductlist.getFirstVisiblePosition();
//                                            manageproductlist.setDivider(new ColorDrawable(getResources().getColor(R.color.transparent)));
//                                            manageproductlist.setAdapter(productListAdapter);
//                                            manageproductlist.setDividerHeight(0);
//                                            manageproductlist.setSelectionFromTop(cp + 1, 0);
//                                            productListAdapter.notifyDataSetChanged();
//                                            load = true;
//                                        }
//                                    }
//                                } else {
//                                    Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    startActivity(intent);
//                                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
//                                }
//
//                            }
//                        }, Product_list.this, "POST", dataforproducts);
//                        crr.execute(Currenturl + "/page/" + current);
//                    }
//                }
//
//
//            }
//        });
    }


    private void showfilter() {
        try {
            listDialog = new Dialog(this, R.style.PauseDialog);
            listDialog.setTitle(getResources().getString(R.string.alert_name));
            LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = li.inflate(R.layout.ced_multivendor_filter_at_snsproductlisting, null, false);
            final Spinner page = (Spinner) v.findViewById(R.id.MultiVendor_sortby);
            final TextView txt_price = (TextView) v.findViewById(R.id.MultiVendor_txt_price);
            final TextView txt_qty = (TextView) v.findViewById(R.id.MultiVendor_txt_qty);
            final TextView product_id = (TextView) v.findViewById(R.id.MultiVendor_product_id);
            final TextView productname = (TextView) v.findViewById(R.id.MultiVendor_productname);
            final TextView status = (TextView) v.findViewById(R.id.MultiVendor_status);
            final TextView setfilter = (TextView) v.findViewById(R.id.MultiVendor_setfilter);
            final TextView unsetfilter = (TextView) v.findViewById(R.id.MultiVendor_unsetfilter);
            final EditText from_price = (EditText) v.findViewById(R.id.MultiVendor_from_price);
            final EditText to_price = (EditText) v.findViewById(R.id.MultiVendor_to_price);
            final EditText from_qty = (EditText) v.findViewById(R.id.MultiVendor_from_qty);
            final EditText to_qty = (EditText) v.findViewById(R.id.MultiVendor_to_qty);
            final EditText from_product_id = (EditText) v.findViewById(R.id.MultiVendor_from_product_id);
            final EditText to_product_id = (EditText) v.findViewById(R.id.MultiVendor_to_product_id);
            final EditText edt_productname = (EditText) v.findViewById(R.id.MultiVendor_edt_productname);
            final Spinner edt_status = (Spinner) v.findViewById(R.id.MultiVendor_edt_status);
            edt_status.setAdapter(new ArrayAdapter<Status>(Product_list.this, R.layout.simple_list_item_1, statusList));
            //final  EditText edt_sku = (EditText)v.findViewById(R.id.MultiVendor_edt_productsku);
            final Spinner websites_spinner = (Spinner) v.findViewById(R.id.MultiVendor_website_spnr);
            websites_spinner.setAdapter(new ArrayAdapter<Website>(Product_list.this, R.layout.simple_list_item_1, websiteList));

            fontSetting.setFontforTextviews(txt_price, "Roboto-Medium.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(txt_qty, "Roboto-Medium.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(product_id, "Roboto-Medium.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(productname, "Roboto-Medium.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(status, "Roboto-Medium.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(setfilter, "Roboto-Medium.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(unsetfilter, "Roboto-Medium.ttf", getApplicationContext());

            edt_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Status status = (Status) adapterView.getItemAtPosition(i);
                    status_value = String.valueOf(status.getValue());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }

            });
            websites_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Website website = (Website) adapterView.getItemAtPosition(i);
                    website_value = String.valueOf(website.getValue());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            if (!(datafilterjson.isEmpty())) {
                JSONObject object = new JSONObject(datafilterjson);
                from_price.setText(object.getJSONObject("price").getString("from"));
                to_price.setText(object.getJSONObject("price").getString("to"));
                from_qty.setText(object.getJSONObject("qty").getString("from"));
                to_qty.setText(object.getJSONObject("qty").getString("to"));
                from_product_id.setText(object.getJSONObject("entity_id").getString("from"));
                to_product_id.setText(object.getJSONObject("entity_id").getString("to"));
                edt_productname.setText(object.getString("name"));

                /*if (object.getString("check_status").equals("Approved")) {
                    edt_status.setSelection(1);
                }
                if (object.getString("check_status").equals("Pending")) {
                    edt_status.setSelection(2);
                }
                if (object.getString("check_status").equals("Disapproved")) {
                    edt_status.setSelection(3);
                }
                if (object.getString("websites").equals("Select")) {
                    websites_spinner.setSelection(0);
                }
                if (object.getString("websites").equals("Main Website")) {
                    websites_spinner.setSelection(1);
                }
                if (object.getString("websites").equals("magenative")) {
                    websites_spinner.setSelection(2);
                }
*/

            }
            unsetfilter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppConstant.lockButton(v);
                    from_price.setText("");
                    to_price.setText("");
                    from_qty.setText("");
                    to_qty.setText("");
                    status_value = "";
                    website_value = "";
                    from_product_id.setText("");
                    to_product_id.setText("");
                    edt_productname.setText("");
                    edt_status.setSelection(0);
                    //edt_sku.setText("");
                    datafilterjson = "";
                    final JSONObject mainfilter = new JSONObject();
                    JSONObject pricejsonObject = new JSONObject();
                    try {
                        listDialog.dismiss();
                        pricejsonObject.put("from", from_price.getText().toString());
                        pricejsonObject.put("to", to_price.getText().toString());
                        mainfilter.put("price", pricejsonObject);
                        JSONObject qtyjsonObject = new JSONObject();
                        qtyjsonObject.put("from", from_qty.getText().toString());
                        qtyjsonObject.put("to", to_qty.getText().toString());
                        mainfilter.put("qty", qtyjsonObject);
                        JSONObject proidjsonObject = new JSONObject();
                        proidjsonObject.put("from", from_product_id.getText().toString());
                        proidjsonObject.put("to", to_product_id.getText().toString());
                        mainfilter.put("entity_id", proidjsonObject);
                        mainfilter.put("name", edt_productname.getText().toString());
                        mainfilter.put("check_status", status_value);
                        //mainfilter.put("sku",edt_sku.getText().toString());
                        mainfilter.put("websites", "1");

                        Intent intent = new Intent(getApplicationContext(), Product_list.class);
//                        intent.putExtra("filter", mainfilter.toString());
                        intent.putExtra("Navigation", navigation);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    } catch (JSONException e) {
                        Intent main = new Intent(Product_list.this, Ced_MultiVendor_VendorSplash.class);
                        main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(main);
                        overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                    }


                }
            });

            setfilter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppConstant.lockButton(v);
                    final JSONObject mainfilter = new JSONObject();
                    JSONObject pricejsonObject = new JSONObject();
                    try {
                        listDialog.dismiss();
                        pricejsonObject.put("from", from_price.getText().toString());
                        pricejsonObject.put("to", to_price.getText().toString());
                        mainfilter.put("price", pricejsonObject);
                        JSONObject qtyjsonObject = new JSONObject();
                        qtyjsonObject.put("from", from_qty.getText().toString());
                        qtyjsonObject.put("to", to_qty.getText().toString());
                        mainfilter.put("qty", qtyjsonObject);
                        JSONObject proidjsonObject = new JSONObject();
                        proidjsonObject.put("from", from_product_id.getText().toString());
                        proidjsonObject.put("to", to_product_id.getText().toString());
                        mainfilter.put("entity_id", proidjsonObject);
                        mainfilter.put("name", edt_productname.getText().toString());
                        mainfilter.put("check_status", status_value);
                        //mainfilter.put("sku",edt_sku.getText().toString());
                        if (websites_spinner.getSelectedItem().toString().equals("Select"))
                            mainfilter.put("websites", "1");
                        else
                            mainfilter.put("websites", website_value);

                        Intent intent = new Intent(getApplicationContext(), Product_list.class);
                        intent.putExtra("filter", mainfilter.toString());
                        intent.putExtra("Navigation", navigation);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


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
        Ced_MultiVendor_ConnectionDetector connectionDetector = new Ced_MultiVendor_ConnectionDetector(Product_list.this);
        if (connectionDetector.isConnectingToInternet()) {
            setname(vendorSessionManagement.getvendorname());
            setprofilepic(vendorSessionManagement.getvendorpic());
            changetitle("Select and Sell");
            // invalidateOptionsMenu();
            super.onResume();
        } else {
            Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            super.onResume();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
