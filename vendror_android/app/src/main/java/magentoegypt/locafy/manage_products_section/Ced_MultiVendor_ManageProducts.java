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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import magentoegypt.locafy.addons.inventory.LowStock;
import magentoegypt.locafy.base_app.Ced_MultiVendor_FontSetting;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorSplash;
import magentoegypt.locafy.vendor_dashboard.Ced_MultiVendor_VendorDashboard;
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
import java.util.List;

public class Ced_MultiVendor_ManageProducts extends Ced_MultiVendor_NavigationActivity {

    static ArrayList<String> ids = new ArrayList<>();
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    RecyclerView manageproductlist;
    String Currenturl;
    HashMap<String, String> dataforproducts;
    String Jstring;
    public static String store_id = "0";
    LinearLayout filtersection;
    Button addnewproduct;
    Dialog listDialog;
    TextView allStoreBtn;
    String datafilterjson = "";
    Ced_MultiVendor_FontSetting fontSetting;
    TextView countsproducts;
    ArrayList<HashMap<String, String>> latestfiveorderlist;
    int current = 1;
    boolean load = true;
    String navigation = " ";
    ArrayList<String> filter_list = new ArrayList<>();
    HashMap<String, String> status_values = new HashMap<>();
    Boolean paginate = true;
    ArrayList<String> filter_type_list = new ArrayList<>();
    HashMap<String, String> type_hasmap = new HashMap<>();
    String filter_values = "";
    String type_values = "";
    int type_selected_index;
    RecyclerView.LayoutManager manager;
    Spinner spnr_updt_free_shipping;
    Button btn_updt_free_shipping;
    HashMap<String, String> map_free_shipping;
    String status = "";
    TextView shipping_status_label;
    String selected_websitetopost;
    Ced_MultiVendor_ProductListAdapter_new productListAdapter;

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
        dataforproducts = new HashMap<>();
        latestfiveorderlist = new ArrayList<>();
        manager = new LinearLayoutManager(Ced_MultiVendor_ManageProducts.this);
        selected_websitetopost = getIntent().getStringExtra("selected_websitetopost");
        if (connectionDetector.isConnectingToInternet()) {
            Currenturl = session.getBase_Url() + "vendorapi/vproducts/item";
            map_free_shipping = new HashMap<>();
            ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
            getLayoutInflater().inflate(R.layout.ced_multivendor_activity_manage_products, content, true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);
            final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
            pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    pullToRefresh.setRefreshing(true);
                    Intent intent = new Intent(Ced_MultiVendor_ManageProducts.this, Ced_MultiVendor_ManageProducts.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }
            });
            allStoreBtn = findViewById(R.id.allStoreBtn);
            manageproductlist = findViewById(R.id.MultiVendor_manageproductlist);
            manageproductlist.setLayoutManager(manager);
            shipping_status_label = findViewById(R.id.shipping_status_label);
            filtersection = findViewById(R.id.MultiVendor_filtersection);
            countsproducts = findViewById(R.id.MultiVendor_countsproducts);
            addnewproduct = findViewById(R.id.MultiVendor_addnewproduct);
            btn_updt_free_shipping = findViewById(R.id.btn_updt_free_shipping);
            spnr_updt_free_shipping = findViewById(R.id.spnr_updt_free_shipping);
            spnr_updt_free_shipping.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (spnr_updt_free_shipping.getSelectedItem().toString().equalsIgnoreCase("Enabled")) {
                        status = "1";
                    } else {
                        status = "0";
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            btn_updt_free_shipping.setOnClickListener(v -> {
                if (ids.size() > 0) {
                    try {
                        String id_string = "";
                        //map_free_shipping.put("page",(current-1)+"");
                        current = 1;
                        // map_free_shipping.put("vendor_id",session.getVendorid());
                        dataforproducts.put("status", status);
                        id_string = ids.toString();
                        id_string = id_string.replace("[", "");
                        id_string = id_string.replace("]", "");
                        /*for (int i =0;i<ids.size();i++)
                        {
                            id_string += ids.get(i)+",";
                        }*/
                        dataforproducts.put("ids", id_string);
                        Log.d("ids_test", "" + id_string);
                        latestfiveorderlist.clear();     //clear the adapter list
                        paginate = true;// re-pagination
                        filter_list.clear();
                        filter_type_list.clear();
                        requestforProducts();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(Ced_MultiVendor_ManageProducts.this, "" + getResources().getString(R.string.select_product), Toast.LENGTH_SHORT).show();
                }
            });

            fontSetting.setfontforButtons(addnewproduct, "Roboto-Medium.ttf", Ced_MultiVendor_ManageProducts.this);
            fontSetting.setfontforButtons(btn_updt_free_shipping, "Roboto-Medium.ttf", Ced_MultiVendor_ManageProducts.this);
            fontSetting.setFontforTextviews(countsproducts, "Roboto-Medium.ttf", Ced_MultiVendor_ManageProducts.this);
            fontSetting.setFontforTextviews(shipping_status_label, "Roboto-Medium.ttf", Ced_MultiVendor_ManageProducts.this);
            filtersection.setOnClickListener(v -> {
                AppConstant.lockButton(v);
                showfilter();
            });
            addnewproduct.setOnClickListener(v -> {
                AppConstant.lockButton(v);
                Intent add = new Intent(Ced_MultiVendor_ManageProducts.this, Ced_MultiVendor_AddVendorProduct.class);
                add.putExtra("selectedwebsite", selected_websitetopost);
                startActivity(add);
                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
            });
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                if (getIntent().getStringExtra("filter") != null) {
                    datafilterjson = getIntent().getStringExtra("filter");
                    type_selected_index = getIntent().getIntExtra("selected", 0);
                    dataforproducts.put("filter", datafilterjson);
                }
                if (getIntent().getStringExtra("Navigation") != null) {
                    navigation = getIntent().getStringExtra("Navigation");
                }

            }
            try {

                dataforproducts.put("vendor_id", session.getVendorid());
                dataforproducts.put("hashkey", session.getHahkey());
                Log.i("dataforproducts", "" + dataforproducts);
                setname(session.getvendorname());
                setprofilepic(session.getvendorpic());
                changetitle("Products");
                requestforProducts();
                if(Ced_MultiVendor_ManageProducts.store_id.equalsIgnoreCase("0"))
                    allStoreBtn.setText("All Store");
                else if(Ced_MultiVendor_ManageProducts.store_id.equalsIgnoreCase("3"))
                    allStoreBtn.setText("Arabic");
                else if(Ced_MultiVendor_ManageProducts.store_id.equalsIgnoreCase("7"))
                    allStoreBtn.setText("English");
                allStoreBtn.setOnClickListener(v -> {
                    List<String> storeNames = new ArrayList<>();
                    List<String> storeIds = new ArrayList<>();
                    // Add "All Store" as first option
                    storeNames.add("All Store");
                    storeIds.add("0");
                    // Add "All Store" as first option
                    storeNames.add("Arabic");
                    storeIds.add("3");
                    // Add "All Store" as first option
                    storeNames.add("English");
                    storeIds.add("7");
                    // Convert to CharSequence[]
                    CharSequence[] storeArray = storeNames.toArray(new CharSequence[0]);
                    AlertDialog.Builder builder = new AlertDialog.Builder(Ced_MultiVendor_ManageProducts.this);
                    builder.setTitle(R.string.select_store);
                    builder.setItems(storeArray, (dialog, which) -> {
                        allStoreBtn.setText(storeNames.get(which));
                        Ced_MultiVendor_ManageProducts.store_id = storeIds.get(which);
                        Intent intent = new Intent(Ced_MultiVendor_ManageProducts.this, Ced_MultiVendor_ManageProducts.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                        overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                    });

                    builder.setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.dismiss());
                    AlertDialog dialog = builder.create();
                    // Optional: Handle iPad-style tablet positioning if needed using a DialogFragment
                    dialog.show();
                });
                manageproductlist.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        if (!recyclerView.canScrollVertically(1) && paginate) {
                            current = current + 1;
                            paginate = false;
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

                                            JSONArray productsArray = jsonObject.getJSONObject("data").getJSONArray("products");
                                            for (int l = 0; l < productsArray.length(); l++) {
                                                JSONObject orderrow = productsArray.getJSONObject(l);
                                                HashMap<String, String> dataofrow = new HashMap<String, String>();
                                                dataofrow.put("product_id", orderrow.getString("product_id"));
//                                                    dataofrow.put("product_id", "#" + orderrow.getString("product_id"));
                                                dataofrow.put("type", orderrow.getString("type"));
                                                dataofrow.put("product_name", orderrow.getString("product_name"));
                                                dataofrow.put("regular_price", orderrow.getString("vendor_price"));
                                                dataofrow.put("product_image", orderrow.getString("product_image"));
                                                dataofrow.put("qty", orderrow.getString("qty"));
                                                dataofrow.put("status", orderrow.getString("status"));
                                                dataofrow.put("sku", orderrow.getString("sku"));
                                                // dataofrow.put("website",orderrow.getString("website"));
//                                                latestfiveorderlist.add(dataofrow);
                                                productListAdapter.productData.add(dataofrow);
                                            }

                                            int size = productListAdapter.productData.size();
                                            if (size > 0) {
                                                countsproducts.setVisibility(View.VISIBLE);
                                                countsproducts.setText(getString(R.string.showing_txt) + " " + size + getString(R.string.products_txt));
                                            }

//                                            Ced_MultiVendor_ProductListAdapter_new productListAdapter = new Ced_MultiVendor_ProductListAdapter_new(Ced_MultiVendor_ManageProducts.this, latestfiveorderlist, selected_websitetopost);
//                                            manageproductlist.setAdapter(productListAdapter);
                                            productListAdapter.notifyDataSetChanged();

                                            paginate = true;
                                        }
                                    }
                                } else {
                                    Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                                }
                            }, Ced_MultiVendor_ManageProducts.this, "POST", dataforproducts);
                            crr.execute(Currenturl + "/page/" + current);
                        } else {
//                            Log.i("recycler", manageproductlist.canScrollVertically(1) + " can scroll ");
                        }
                    }
                });
/*                manageproductlist.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {

                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                        if (firstVisibleItem == 0) {
                            getSupportActionBar().show();
                        } else {
                            getSupportActionBar().hide();
                        }
                        if ((firstVisibleItem + visibleItemCount) != 0) {

                            if (((firstVisibleItem + visibleItemCount) == totalItemCount) && load)
                            {
                                current = current + 1;
                                load = false;
                                Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {

                                    @Override
                                    public void processFinish(Object output) throws JSONException {
                                        Jstring = output.toString();
                                        if (functionalityList.getExtensionAddon()) {
                                            JSONObject jsonObject = new JSONObject(Jstring);
                                            if (jsonObject.has("vendor_approved")) {
                                                logout();
                                            } else {
                                                String success = jsonObject.getJSONObject("data").getString("success");
                                                if (success.equals("true")) {
                                                    JSONArray latest_order = jsonObject.getJSONObject("data").getJSONArray("products");

                                                    for (int l = 0; l < latest_order.length(); l++) {
                                                        JSONObject orderrow = latest_order.getJSONObject(l);
                                                        HashMap<String, String> dataofrow = new HashMap<String, String>();
                                                        dataofrow.put("product_id", "#" + orderrow.getString("product_id"));
                                                        dataofrow.put("type", orderrow.getString("type"));
                                                        dataofrow.put("product_name", orderrow.getString("product_name"));
                                                        dataofrow.put("regular_price", orderrow.getString("regular_price"));
                                                        dataofrow.put("product_image", orderrow.getString("product_image"));
                                                        dataofrow.put("qty", orderrow.getString("qty"));
                                                        dataofrow.put("status", orderrow.getString("status"));
                                                        dataofrow.put("sku", orderrow.getString("sku"));
                                                        // dataofrow.put("website",orderrow.getString("website"));
                                                        latestfiveorderlist.add(dataofrow);
                                                    }
                                                    int size = latestfiveorderlist.size();
                                                    if (size > 0) {
                                                        countsproducts.setVisibility(View.VISIBLE);
                                                        countsproducts.setText(getString(R.string.showing_txt)+" "  + size +  getString(R.string.products_txt));
                                                    }
                                                    Ced_MultiVendor_ProductListAdapter_new productListAdapter = new Ced_MultiVendor_ProductListAdapter_new(Ced_MultiVendor_ManageProducts.this, latestfiveorderlist);
                                                    int cp = manageproductlist.getFirstVisiblePosition();
                                                    manageproductlist.setDivider(new ColorDrawable(getResources().getColor(R.color.transparent)));
                                                    manageproductlist.setAdapter(productListAdapter);
                                                    manageproductlist.setDividerHeight(0);
                                                    manageproductlist.setSelectionFromTop(cp + 1, 0);
                                                    productListAdapter.notifyDataSetChanged();
                                                    load = true;

                                                }

                                            }
                                        } else {
                                            Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                                        }

                                    }
                                }, Ced_MultiVendor_ManageProducts.this, "POST", dataforproducts);
                                crr.execute(Currenturl + "/page/" + current);
                            }
                        }


                    }
                });*/
            } catch (Exception e) {
                Intent main = new Intent(getApplicationContext(), Ced_MultiVendor_VendorSplash.class);
                main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(main);
                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
            }
        } else {
            Intent nointernet = new Intent(Ced_MultiVendor_ManageProducts.this, Ced_MultiVendor_NoInternetconnection.class);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(nointernet);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }
    }

    private void requestforProducts() {
        dataforproducts.put("store_id", Ced_MultiVendor_ManageProducts.store_id);
        Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(output -> {
            Jstring = output.toString();
            if (functionalityList.getExtensionAddon()) {
                JSONObject jsonObject = new JSONObject(Jstring);
                if (jsonObject.has("vendor_approved")) {
                    logout();
                } else {
                    String success = jsonObject.getJSONObject("data").getString("success");
                    status_array_spinner = jsonObject.getJSONObject("data").getJSONObject("status_array");
                    JSONArray filter_data = status_array_spinner.names();
                    filter_list.add(getString(R.string.select_txt));
                    for (int i = 0; i < filter_data.length(); i++) {
                        String status_names = status_array_spinner.getString(filter_data.get(i).toString());
                        filter_list.add(status_names);
                        status_values.put(status_names, filter_data.get(i).toString());
                    }
                    JSONArray type_array = jsonObject.getJSONObject("data").getJSONArray("type");
                    type_spinner = type_array;
                    filter_type_list.add(getString(R.string.select_txt));
                    for (int i = 0; i < type_array.length(); i++) {
                        JSONObject jsonObject1 = type_array.getJSONObject(i);
                        String status_names = jsonObject1.getString("value");
                        filter_type_list.add(status_names);
                        type_hasmap.put(jsonObject1.getString("value"), jsonObject1.getString("key"));
                    }
                    if (success.equals("true")) {
                        JSONArray latest_order = jsonObject.getJSONObject("data").getJSONArray("products");
                        for (int l = 0; l < latest_order.length(); l++) {
                            JSONObject orderrow = latest_order.getJSONObject(l);
                            HashMap<String, String> dataofrow = new HashMap<String, String>();
                            dataofrow.put("product_id", "#" + orderrow.getString("product_id"));
                            dataofrow.put("type", orderrow.getString("type"));
                            dataofrow.put("product_name", orderrow.getString("product_name"));
                            dataofrow.put("regular_price", orderrow.getString("vendor_price"));
                            dataofrow.put("product_image", orderrow.getString("product_image"));
                            dataofrow.put("qty", orderrow.getString("qty"));
                            dataofrow.put("status", orderrow.getString("status"));
                            dataofrow.put("sku", orderrow.getString("sku"));
                            //  dataofrow.put("website",orderrow.getString("website"));
                            latestfiveorderlist.add(dataofrow);
                        }

                        int size = latestfiveorderlist.size();

                        if (size > 0) {
                            countsproducts.setVisibility(View.VISIBLE);
                            countsproducts.setText(getString(R.string.showing_txt) + " " + size + getString(R.string.products_txt));
                        }

                        productListAdapter = new Ced_MultiVendor_ProductListAdapter_new(Ced_MultiVendor_ManageProducts.this, latestfiveorderlist, selected_websitetopost);
                        /*manageproductlist.setDivider(new ColorDrawable(getResources().getColor(R.color.transparent)));*/
                        manageproductlist.setAdapter(productListAdapter);
                        ids.clear();
                        productListAdapter.notifyDataSetChanged();

                        /*manageproductlist.setDividerHeight(0);*/
                    }
                    else {
                        Toast.makeText(getApplicationContext(), jsonObject.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
                        countsproducts.setVisibility(View.VISIBLE);
                        countsproducts.setText(jsonObject.getJSONObject("data").getString("message"));
                    }
                }
            } else {
                Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
            }
        }, Ced_MultiVendor_ManageProducts.this, "POST", dataforproducts);
        crr.execute(Currenturl + "/page/" + current);
    }

    @Override
    protected void onResume() {
        Ced_MultiVendor_ConnectionDetector connectionDetector = new Ced_MultiVendor_ConnectionDetector(Ced_MultiVendor_ManageProducts.this);
        if (connectionDetector.isConnectingToInternet()) {
            setname(session.getvendorname());
            setprofilepic(session.getvendorpic());
            changetitle("Products");
            //   invalidateOptionsMenu();
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

    private void showfilter() {
        try {
            listDialog = new Dialog(this, R.style.PauseDialog);
            listDialog.setTitle(getResources().getString(R.string.alert_name));
            LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = li.inflate(R.layout.ced_multivendor_header_at_productlisting, null, false);
//            final Spinner page = v.findViewById(R.id.MultiVendor_sortby);
            final TextView txt_price = v.findViewById(R.id.MultiVendor_txt_price);
            final TextView productsku = v.findViewById(R.id.MultiVendor_productsku);
            final TextView txt_qty = v.findViewById(R.id.MultiVendor_txt_qty);
            final TextView product_id = v.findViewById(R.id.MultiVendor_product_id);
            final TextView productname = v.findViewById(R.id.MultiVendor_productname);
            final TextView status = v.findViewById(R.id.MultiVendor_status);
            final TextView producttype = v.findViewById(R.id.MultiVendor_producttype);
            final TextView setfilter = v.findViewById(R.id.MultiVendor_setfilter);
            final TextView unsetfilter = v.findViewById(R.id.MultiVendor_unsetfilter);
            final EditText from_price = v.findViewById(R.id.MultiVendor_from_price);
            final EditText to_price = v.findViewById(R.id.MultiVendor_to_price);
            final EditText from_qty = v.findViewById(R.id.MultiVendor_from_qty);
            final EditText to_qty = v.findViewById(R.id.MultiVendor_to_qty);
            final EditText from_product_id = v.findViewById(R.id.MultiVendor_from_product_id);
            final EditText to_product_id = v.findViewById(R.id.MultiVendor_to_product_id);
            final EditText edt_productname = v.findViewById(R.id.MultiVendor_edt_productname);
            final EditText edt_productsku = v.findViewById(R.id.MultiVendor_edt_productsku);
            final Spinner edt_status = v.findViewById(R.id.MultiVendor_edt_status);
            edt_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (!adapterView.getSelectedItem().toString().equalsIgnoreCase(getString(R.string.select_txt))) {
                        filter_values = status_values.get(adapterView.getSelectedItem().toString());
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            edt_status.setAdapter(new ArrayAdapter<>(Ced_MultiVendor_ManageProducts.this, R.layout.simple_list_item_1, filter_list));
            final Spinner edt_producttype = v.findViewById(R.id.MultiVendor_edt_producttype);
            edt_producttype.setAdapter(new ArrayAdapter<>(Ced_MultiVendor_ManageProducts.this, R.layout.simple_list_item_1, filter_type_list));
            edt_producttype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (!adapterView.getSelectedItem().toString().equalsIgnoreCase(getString(R.string.select_txt))) {
                        type_values = type_hasmap.get(adapterView.getSelectedItem().toString());
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            fontSetting.setFontforTextviews(txt_price, "Roboto-Medium.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(txt_qty, "Roboto-Medium.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(product_id, "Roboto-Medium.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(productname, "Roboto-Medium.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(status, "Roboto-Medium.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(producttype, "Roboto-Medium.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(setfilter, "Roboto-Medium.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(unsetfilter, "Roboto-Medium.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(productsku, "Roboto-Medium.ttf", getApplicationContext());
            if (!(datafilterjson.isEmpty())) {
                JSONObject object = new JSONObject(datafilterjson);
                from_price.setText(object.getJSONObject("price").getString("from"));
                to_price.setText(object.getJSONObject("price").getString("to"));
                from_qty.setText(object.getJSONObject("qty").getString("from"));
                to_qty.setText(object.getJSONObject("qty").getString("to"));
                from_product_id.setText(object.getJSONObject("entity_id").getString("from"));
                to_product_id.setText(object.getJSONObject("entity_id").getString("to"));
                edt_productname.setText(object.getString("name"));
                edt_productsku.setText(object.getString("sku"));
            }
            unsetfilter.setOnClickListener(v12 -> {
                AppConstant.lockButton(v12);
                from_price.setText("");
                to_price.setText("");
                from_qty.setText("");
                to_qty.setText("");
                from_product_id.setText("");
                to_product_id.setText("");
                edt_productname.setText("");
                edt_productsku.setText("");
                edt_producttype.setSelection(0);
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
                    mainfilter.put("sku", edt_productsku.getText().toString());
                    mainfilter.put("check_status", filter_values);
                    mainfilter.put("type_id", type_values);
                    Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_ManageProducts.class);
                    intent.putExtra("filter", mainfilter.toString());
                    intent.putExtra("Navigation", navigation);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } catch (JSONException e) {
                    Intent main = new Intent(Ced_MultiVendor_ManageProducts.this, Ced_MultiVendor_VendorSplash.class);
                    main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(main);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }
            });
            setfilter.setOnClickListener(v1 -> {
                AppConstant.lockButton(v1);
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
                    mainfilter.put("check_status", filter_values);
                    mainfilter.put("sku", edt_productsku.getText().toString());
                    mainfilter.put("type_id", type_values);
                    Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_ManageProducts.class);
                    intent.putExtra("filter", mainfilter.toString());
                    intent.putExtra("selected", type_selected_index);
                    intent.putExtra("Navigation", navigation);
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
                if (navigation.equals("productcreate")) {
                    Intent intent = new Intent(Ced_MultiVendor_ManageProducts.this, Ced_MultiVendor_VendorDashboard.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    this.finish();
                    return true;
                } else {
                    this.finish();
                    return true;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (navigation.equals("productcreate")) {
            Intent intent = new Intent(Ced_MultiVendor_ManageProducts.this, Ced_MultiVendor_VendorDashboard.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            this.finish();
        } else {
            this.finish();
        }
        super.onBackPressed();
    }
}