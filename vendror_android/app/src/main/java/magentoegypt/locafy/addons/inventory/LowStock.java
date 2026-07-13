package magentoegypt.locafy.addons.inventory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import magentoegypt.locafy.R;
import magentoegypt.locafy.addons.faq.AddNewFAQ;
import magentoegypt.locafy.addons.faq.EditAndDeleteListener;
import magentoegypt.locafy.addons.inventory.adapters.LowStockProductAdapter;
import magentoegypt.locafy.addons.inventory.viewModels.InventoryViewModel;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.api_request_response_section.Ced_NewLoader;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_UnAuthourizedRequestError;
import magentoegypt.locafy.manage_products_section.Ced_MultiVendor_ManageProducts;
import magentoegypt.locafy_constant.Ced_MultiVendor_GlobalVariables;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.manage_products_section.EditProductDynamic;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LowStock extends Ced_MultiVendor_NavigationActivity implements EditAndDeleteListener {

    private static final String TAG = "LowStock";
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    InventoryViewModel viewModel;
    Ced_NewLoader cedNewLoader;
    Map<String, Object> objectMap = new HashMap<>();
    Map<String, Object> data = new HashMap<>();
    RecyclerView recyclerView;
    LowStockProductAdapter adapter;
    TextView text_msg;
    Map<String, Object> requestMap = new HashMap<>();

    JSONObject filterMap = new JSONObject();
    int pageNumber = 1;
    Boolean fromFilter = false;
    String vendorId = "3";
    Button tag;


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
        if (connectionDetector.isConnectingToInternet()) {
            ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
            getLayoutInflater().inflate(R.layout.activity_low_stock, content, true);
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);

            init();
            bindAdapter();
            setListener();
            final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
            pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    pullToRefresh.setRefreshing(true);
                    Intent intent = new Intent(LowStock.this, LowStock.class).putExtra("type", "low_stock");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
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

    @Override
    protected void onResume() {
        super.onResume();
        if (null != adapter)
            adapter.clear();
        loadOutOfStockData();
    }

    private void setListener() {
        findViewById(R.id.filter_button_low_stock).setOnClickListener(view -> showFilterDialog());
        findViewById(R.id.saveInventary).setOnClickListener(view -> saveInventory());
    }

    private void saveInventory() {
        startActivity(new Intent(this, AddNewFAQ.class));
    }

    private void bindAdapter() {
        adapter = new LowStockProductAdapter(this);
        text_msg = findViewById(R.id.stock_text_msg);
        recyclerView = findViewById(R.id.recLowStock);
        recyclerView.setAdapter(adapter);
    }

    private void loadOutOfStockData() {
        requestMap.put("vendor_id", session.getVendorid());
        requestMap.put("page", pageNumber);
        objectMap.put("data", requestMap);
        Log.d(TAG, "objectMap: " + objectMap);

        if (getIntent().hasExtra("type")) {
            Log.d(TAG, "type Received : " + getIntent().getStringExtra("type"));
            if (Objects.requireNonNull(getIntent().getStringExtra("type")).equalsIgnoreCase("low_stock")) {
                viewModel.loadLowStockData(objectMap);
                tag.setText(getString(R.string.low_stock_product));
            } else if (Objects.requireNonNull(getIntent().getStringExtra("type")).equalsIgnoreCase("out_of_stock")) {
                viewModel.loadOutOfStockData(objectMap);
                tag.setText(getString(R.string.out_of_stock));
            }
        } else Toast.makeText(this, "no key found to load data !!", Toast.LENGTH_SHORT).show();


    }


    private void init() {
        tag = findViewById(R.id.saveInventary);
        vendorId = session.getVendorid();
        cedNewLoader = new Ced_NewLoader(LowStock.this);
        viewModel = ViewModelProviders.of(LowStock.this).get((InventoryViewModel.class));

        viewModel.observeLowStockData().observe(LowStock.this, this::handleResponse);

        viewModel.error.observe(LowStock.this, s -> Toast.makeText(LowStock.this, s, Toast.LENGTH_SHORT).show());

        viewModel.loading.observe(LowStock.this, this::updateLoader);
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }

    private void handleResponse(Object object) {
        Log.d(TAG, "data : " + object);
        if (fromFilter)
            adapter.clear();
        try {
            JSONObject jsonObject = new JSONObject(new Gson().toJson(object));
            JSONObject vendorData = jsonObject.getJSONObject("vendor_data");
            if (vendorData.getBoolean("success")) {
                text_msg.setVisibility(View.GONE);
                adapter.addList(vendorData.getJSONArray("products"));
            } else {
                text_msg.setVisibility(View.VISIBLE);
                text_msg.setText(vendorData.getString("message"));
                Toast.makeText(this, vendorData.getString("message"), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, ": " + e.getLocalizedMessage());
        }

        fromFilter = false;
    }

    private void updateLoader(Boolean isLoading) {
        if (isLoading) {
            if (null != cedNewLoader)
                cedNewLoader.show();
        } else {
            if ((cedNewLoader != null) && cedNewLoader.isShowing()) {
                cedNewLoader.dismiss();
            }
        }
    }

    @Override
    public void onClick(JSONObject obj, int type) {
        //navigate to edit Product Screen here
        try {
            editProduct(obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void editProduct(JSONObject obj) throws JSONException {

        Ced_MultiVendor_VendorFunctionalityList functionalityList = new Ced_MultiVendor_VendorFunctionalityList(this);
        HashMap<String, String> dataforproducts = new HashMap<>();
        Ced_MultiVendor_VendorSessionManagement vendorSessionManagement = new Ced_MultiVendor_VendorSessionManagement(this);

        String editCurrenturl = vendorSessionManagement.getBase_Url() + "vendorapi/vproducts/info";

        final String product_id = obj.getString("product_id");
        dataforproducts.put("product_id", product_id);
        dataforproducts.put("store_id", Ced_MultiVendor_ManageProducts.store_id);
        if (vendorSessionManagement.getStoreId() != null)
            dataforproducts.put("store_id", vendorSessionManagement.getStoreId());
        dataforproducts.put("vendor_id", vendorSessionManagement.getVendorid());
        dataforproducts.put("hashkey", vendorSessionManagement.getHahkey());
        Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {

            @Override
            public void processFinish(Object output) throws JSONException {
                String Jstring = output.toString();
                if (functionalityList.getExtensionAddon()) {
                    JSONObject object = new JSONObject(Jstring);
                    String success = object.getJSONObject("data").getString("success");
                    if (success.equals("true")) {
                        JSONObject productdata = object.getJSONObject("data").getJSONObject("productdata");
                        String type = productdata.getString("type_id");

                        Intent update = new Intent(LowStock.this, EditProductDynamic.class);
                        update.putExtra("type", type);
                        update.putExtra("dataforproductcreation", Jstring);
                        update.putExtra("product_id", product_id);
                        update.putExtra("selectedwebsite", "");
                        if (functionalityList.getProductAddon()) {
                            if (type.equals("configurable") && object.getJSONObject("data").get("config_selected_attr") instanceof JSONObject) {
                                Ced_MultiVendor_GlobalVariables.config_attribute_value = object.getJSONObject("data").getJSONObject("config_selected_attr");
                            }
                            String attribute_set = productdata.getString("attribute_set_id");
                            update.putExtra("attribute_set", attribute_set);
                        }
                        startActivity(update);
                        overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);

                    }


                } else {
                    Intent intent = new Intent(LowStock.this, Ced_MultiVendor_UnAuthourizedRequestError.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }

            }
        }, LowStock.this, "POST", dataforproducts);
        crr.execute(editCurrenturl);
    }

    @Override
    public void onLoadMoreClick() {
        pageNumber = pageNumber + 1;
        loadOutOfStockData();
    }

    public void showFilterDialog() {
        final View filter_dialog = LowStock.this.getLayoutInflater().inflate(R.layout.inventory_filter_screen, null);
        final AlertDialog.Builder alert = new AlertDialog.Builder(LowStock.this);
        alert.setView(filter_dialog);
        final Dialog d = alert.create();

        d.show();

        filter_dialog.findViewById(R.id.dialog_apply_btn).setOnClickListener(view -> {
            d.hide();
            bindDataToMap(filter_dialog);
        });

        filter_dialog.findViewById(R.id.dialog_reset_btn).setOnClickListener(view -> {
            d.hide();
            requestMap.remove("filter");
            loadOutOfStockData();
        });

    }

    private void bindDataToMap(View filter_dialog) {
        EditText quantity = filter_dialog.findViewById(R.id.id_edt);
        EditText name = filter_dialog.findViewById(R.id.title_edit);
        EditText product = filter_dialog.findViewById(R.id.customer_email_edt);
        EditText etFromDate = filter_dialog.findViewById(R.id.createdAt_edt);
        EditText etToDate = filter_dialog.findViewById(R.id.createdAt_to_date);
        EditText assesment_edt = filter_dialog.findViewById(R.id.assesment_edt);

        try {

            filterMap.put("product_id", "" + product.getText().toString());
            filterMap.put("name", "" + name.getText().toString());
            filterMap.put("type", "" + assesment_edt.getText().toString());

            JSONObject dateMap = new JSONObject();
            dateMap.put("to", "" + etToDate.getText().toString());
            dateMap.put("from", "" + etFromDate.getText().toString());

            filterMap.put("price", dateMap);
            filterMap.put("qty", "" + quantity.getText().toString());

            requestMap.put("filter", filterMap.toString());
            pageNumber = 1;
            objectMap.put("parameters", requestMap);
            Log.d(TAG, "filter map: " + objectMap);
            fromFilter = true;
            loadOutOfStockData();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "filter error : " + e.getLocalizedMessage());
        }

    }
}