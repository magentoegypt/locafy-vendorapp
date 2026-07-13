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

package magentoegypt.locafy.vendor_reports_section;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import magentoegypt.locafy.addons.inventory.OutOfStockActivity;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_UnAuthourizedRequestError;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;
import magentoegypt.locafy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by developer on 10/5/16.
 */
public class Ced_MultiVendor_ProductReport_list extends Ced_MultiVendor_NavigationActivity {
    static final String KEY_PRODUCT_NAME = "product_name";
    static final String KEY_SKU = "sku";
    static final String KEY_SALES_ITEM = "sales_items";
    static final String KEY_TOTAL_SALES = "total_sales";
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    HashMap<String, String> postdata;
    String from, to, vendor_id, out, hashkey;
    ListView product_report_list;
    String url = "";
    JSONObject jsonObject;
    JSONArray product_report;
    String product_name, sku, sales_items, total_sales;
    ArrayList<HashMap<String, String>> Productinfo;
    Ced_MultiVendor_ProductReportListAdapter productReportListAdapter;
    int current = 1;
    boolean load = true;
    TextView text_msg, Count_total;
    private int visible = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
            window.setStatusBarColor(this.getResources().getColor(R.color.status_bar_color));
        }
        connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(getApplicationContext());
        postdata = new HashMap<>();
        Productinfo = new ArrayList<>();
        url = session.getBase_Url() + "vendorapi/vreport/report";

        if (connectionDetector.isConnectingToInternet()) {
            ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
            getLayoutInflater().inflate(R.layout.ced_multivendor_activity_product_report_list, content, true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);
            if (session.getvendorname() != null) {
                setname(session.getvendorname());
                setprofilepic(session.getvendorpic());
                changetitle("Product Reports");
            }
            final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
            pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    pullToRefresh.setRefreshing(true);
                    Intent intent = new Intent(Ced_MultiVendor_ProductReport_list.this, Ced_MultiVendor_ProductReport_list.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    intent.putExtra("to", getIntent().getStringExtra("to"));
                    intent.putExtra("from", getIntent().getStringExtra("from"));
                    startActivity(intent);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }
            });
            final HashMap<String, String> user = session.getUserDetails();
            hashkey = user.get(Ced_MultiVendor_VendorSessionManagement.Key_HAsh);
            from = getIntent().getStringExtra("from");
            to = getIntent().getStringExtra("to");
            vendor_id = session.getVendorid();
            postdata.put("vendor_id", vendor_id);
            postdata.put("from", from);
            postdata.put("to", to);
            postdata.put("hashkey", hashkey);
            text_msg = findViewById(R.id.MultiVendor_text_msg);
            product_report_list = findViewById(R.id.MultiVendor_product_report_list);
            Count_total = findViewById(R.id.MultiVendor_Count_total);
            request();
        } else {
            Intent nointernet = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(nointernet);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }
    }

    private void request() {
        Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                if (functionalityList.getExtensionAddon()) {
                    out = output.toString();
                    if (out.contains("NO_PRODUCT")) {
                        text_msg.setText(R.string.NoReortatinthisperiod);
                        text_msg.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), R.string.NoReortatinthisperiod, Toast.LENGTH_LONG).show();
                        //  Toast.makeText(getApplicationContext(),"You have no more orders to see",Toast.LENGTH_SHORT).show();
                    } else {
                        prodreportdata();
                    }
                } else {
                    Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }
            }
        }, Ced_MultiVendor_ProductReport_list.this, "POST", postdata);
        response.execute(url + "/page/" + current);

    }

    private void prodreportdata() {
        try {

            if (!out.equalsIgnoreCase("\"NO_PRODUCT\"")) {

                jsonObject = new JSONObject(out);

                if (jsonObject.has("vendor_approved")) {
                    logout();
                } else {
                    if (jsonObject.getJSONObject("data").getBoolean("success")) {
                        product_report = jsonObject.getJSONObject("data").getJSONArray("product_report");
                        Count_total.setText(getString(R.string.you_have_txt) + " " + jsonObject.getJSONObject("data").getString("count") + " " + getString(R.string.prod_order_during_this_period));
                        for (int i = 0; i < product_report.length(); i++) {
                            JSONObject c = null;
                            c = product_report.getJSONObject(i);
                            product_name = c.getString(KEY_PRODUCT_NAME);
                            sku = c.getString(KEY_SKU);
                            sales_items = c.getString(KEY_SALES_ITEM);
                            total_sales = c.getString(KEY_TOTAL_SALES);
                            HashMap<String, String> product = new HashMap<String, String>();
                            product.put(KEY_PRODUCT_NAME, product_name);
                            product.put(KEY_SKU, sku);
                            product.put(KEY_SALES_ITEM, sales_items);
                            product.put(KEY_TOTAL_SALES, total_sales);
                            Productinfo.add(product);
                        }
                        productReportListAdapter = new Ced_MultiVendor_ProductReportListAdapter(Ced_MultiVendor_ProductReport_list.this, Productinfo);
                        product_report_list.setDivider(new ColorDrawable(getResources().getColor(R.color.transparent)));
                        product_report_list.setDividerHeight(0);
                        product_report_list.setAdapter(productReportListAdapter);
                        product_report_list.setOnScrollListener(new AbsListView.OnScrollListener() {
                            @Override
                            public void onScrollStateChanged(AbsListView view, int scrollState) {

                            }

                            @Override
                            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                                if ((firstVisibleItem + visibleItemCount) != 0) {

                                    if (((firstVisibleItem + visibleItemCount) == totalItemCount) && load) {
                                        current = current + 1;
                                        load = false;
                                        visible = firstVisibleItem;
                                        Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
                                            @Override
                                            public void processFinish(Object output) throws JSONException {
                                                out = output.toString();

                                                if (out.contains("NO_PRODUCT")) {
                                                    if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                                        Log.i("jsonnull", out);
                                                    }
                                                    load = false;
                                                } else {

                                                    scrolldata();
                                                }
                                            }
                                        }, Ced_MultiVendor_ProductReport_list.this, "POST", postdata);
                                        response.execute(url + "/page/" + current);
                                    }
                                }
                            }
                        });

                    } else {
                        Toast.makeText(getApplicationContext(), jsonObject.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                Toast.makeText(this, "No Products Found", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void scrolldata() throws JSONException {
        jsonObject = new JSONObject(out);
        if (jsonObject.has("vendor_approved")) {
            logout();
        } else {
            if (jsonObject.getJSONObject("data").getBoolean("success")) {
                product_report = jsonObject.getJSONObject("data").getJSONArray("product_report");
                for (int i = 0; i < product_report.length(); i++) {
                    JSONObject c = null;
                    c = product_report.getJSONObject(i);
                    product_name = c.getString(KEY_PRODUCT_NAME);
                    sku = c.getString(KEY_SKU);
                    sales_items = c.getString(KEY_SALES_ITEM);
                    total_sales = c.getString(KEY_TOTAL_SALES);
                    HashMap<String, String> product = new HashMap<String, String>();
                    product.put(KEY_PRODUCT_NAME, product_name);
                    product.put(KEY_SKU, sku);
                    product.put(KEY_SALES_ITEM, sales_items);
                    product.put(KEY_TOTAL_SALES, total_sales);
                    Productinfo.add(product);
                }
                productReportListAdapter = new Ced_MultiVendor_ProductReportListAdapter(Ced_MultiVendor_ProductReport_list.this, Productinfo);
                product_report_list.setDivider(new ColorDrawable(getResources().getColor(R.color.transparent)));
                product_report_list.setDividerHeight(0);
                int cp = product_report_list.getFirstVisiblePosition();
                product_report_list.setAdapter(productReportListAdapter);
                product_report_list.setSelectionFromTop(cp + 1, 0);
                productReportListAdapter.notifyDataSetChanged();
                load = true;
            } else {
                Toast.makeText(getApplicationContext(), jsonObject.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onResume() {
        Ced_MultiVendor_ConnectionDetector connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        if (connectionDetector.isConnectingToInternet()) {
            setname(session.getvendorname());
            setprofilepic(session.getvendorpic());
            changetitle("Product Reports");
            // invalidateOptionsMenu();
            super.onResume();

        } else {
            Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
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
