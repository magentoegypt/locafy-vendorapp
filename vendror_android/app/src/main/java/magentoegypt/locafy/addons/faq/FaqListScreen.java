package magentoegypt.locafy.addons.faq;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import magentoegypt.locafy.R;
import magentoegypt.locafy.addons.faq.adapters.FaqListAdapter;
import magentoegypt.locafy.addons.faq.viewModel.FaqViewModel;
import magentoegypt.locafy.api_request_response_section.Ced_NewLoader;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import magentoegypt.locafy_constant.AppConstant;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FaqListScreen extends Ced_MultiVendor_NavigationActivity implements EditAndDeleteListener {
    private static final String TAG = "FaqListScreen";
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    FaqViewModel viewModel;
    Ced_NewLoader cedNewLoader;
    Map<String, Object> objectMap = new HashMap<>();
    RecyclerView recyclerView;
    TextView text_msg;
    FaqListAdapter adapter;
    Map<String, Object> requestMap = new HashMap<>();
    JSONObject filterMap = new JSONObject();
    int pageNumber = 1;
    Boolean fromFilter = false;
    String vendorId = "";


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
            getLayoutInflater().inflate(R.layout.activity_faq_list_screen, content, true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);

            init();
            bindAdapter();
            loadFaqData();
            setListener();

        } else {
            Intent nointernet = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(nointernet);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }

    }

    private void setListener() {
        findViewById(R.id.filter_button_faq).setOnClickListener(view -> showFilterDialog());
        findViewById(R.id.addFaq).setOnClickListener(view -> addNewFaq());
    }

    private void addNewFaq() {
        startActivity(new Intent(this, AddNewFAQ.class));
    }

    private void bindAdapter() {
        adapter = new FaqListAdapter(this,this);
        text_msg = findViewById(R.id.faqtext_msg);
        recyclerView = findViewById(R.id.recFaqList);
        recyclerView.setAdapter(adapter);
    }

    private void loadFaqData() {
        requestMap.put("vendor_id", vendorId);
        requestMap.put("current_page", pageNumber);
        requestMap.put("page_size", "10");

        objectMap.put("parameters", requestMap);
        Log.d(TAG, "objectMap: " + objectMap);
        viewModel.loadFaqData(objectMap);


    }


    private void init() {

        vendorId = session.getVendorid();
        cedNewLoader = new Ced_NewLoader(FaqListScreen.this);
        viewModel = ViewModelProviders.of(FaqListScreen.this).get((FaqViewModel.class));

        viewModel.observeFaqData().observe(FaqListScreen.this, this::handleResponse);


        viewModel.error.observe(FaqListScreen.this, s -> Toast.makeText(FaqListScreen.this, s, Toast.LENGTH_SHORT).show());

        viewModel.loading.observe(FaqListScreen.this, this::updateLoader);
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }

    private void handleResponse(Object object) {
        Log.d(TAG, "data : " + object);
        if (fromFilter)
            adapter.clear();
        try {
            JSONArray jsonArray = new JSONArray(new Gson().toJson(object));
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            if (jsonObject.getBoolean("success")) {
                text_msg.setVisibility(View.GONE);
                adapter.addList(jsonObject.getJSONArray("faq_data"));
            }
            else {
                text_msg.setVisibility(View.VISIBLE);
                text_msg.setText(jsonObject.getString("msg"));
                Toast.makeText(this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
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
        if (type == 1) {
            showDeleteDialog(obj);
        }
        else if (type == 0) {

            try {
                startActivity(new Intent(this, AddNewFAQ.class).putExtra("faq_id", obj.getString("id")));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onLoadMoreClick() {
        pageNumber = pageNumber + 1;
        loadFaqData();
    }

    private void showDeleteDialog(JSONObject jsonObject) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.alert))
                .setMessage(R.string.do_you_really_want_to_delete_this)
                .setPositiveButton(getString(R.string.yes), (dialog, id) -> {
                    dialog.cancel();
                    Map<String, Object> deleteMap = new HashMap<>();
                    Map<String, Object> params = new HashMap<>();
                    params.put("vendor_id", vendorId);
                    try {
                        params.put("faq_id", jsonObject.get("id"));
                        deleteMap.put("parameters", params);
                        viewModel.deleteFAQ(deleteMap);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(this, getString(R.string.deleting), Toast.LENGTH_SHORT).show();

                })
                .setNegativeButton(getString(R.string.no), (dialogInterface, i) -> dialogInterface.cancel())
                .show();
    }

    public void showFilterDialog() {
        final View filter_dialog = FaqListScreen.this.getLayoutInflater().inflate(R.layout.faq_filter_screen, null);
        final AlertDialog.Builder alert = new AlertDialog.Builder(FaqListScreen.this);
        alert.setView(filter_dialog);
        final Dialog d = alert.create();

        Spinner spinner = filter_dialog.findViewById(R.id.isactive_spn);
        List<String> list = Arrays.asList(getResources().getStringArray(R.array.store_enabledis));  //new ArrayList<>(getA);

      //  list.add("disable");
      //  list.add("enable");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_spinner_item, list);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    filterMap.put("is_active", "" + i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        d.show();

        filter_dialog.findViewById(R.id.dialog_apply_btn).setOnClickListener(view -> {
            d.hide();
            bindDataToMap(filter_dialog);
        });

        filter_dialog.findViewById(R.id.dialog_reset_btn).setOnClickListener(view -> {
            d.hide();
            requestMap.remove("filter");
            loadFaqData();
        });
        final TextView created_at_from = filter_dialog.findViewById(R.id.createdAt_edt);
        final TextView created_at_to = filter_dialog.findViewById(R.id.createdAt_to_date);


        created_at_from.setOnClickListener(view -> AppConstant.setDateFrom(FaqListScreen.this, created_at_from));
        created_at_to.setOnClickListener(view -> AppConstant.setDateFrom(FaqListScreen.this, created_at_to));
    }

    private void bindDataToMap(View filter_dialog) {
        EditText etId = filter_dialog.findViewById(R.id.id_edt);
        EditText etpId = filter_dialog.findViewById(R.id.assesment_edt);
        EditText etTitle = filter_dialog.findViewById(R.id.title_edit);
        EditText etCustomerEmail = filter_dialog.findViewById(R.id.customer_email_edt);
        EditText etFromDate = filter_dialog.findViewById(R.id.createdAt_edt);
        EditText etToDate = filter_dialog.findViewById(R.id.createdAt_to_date);

        try {

            filterMap.put("title", "" + etTitle.getText().toString());
            filterMap.put("email_id", "" + etCustomerEmail.getText().toString());

            JSONObject dateMap = new JSONObject();
            dateMap.put("to", "" + etToDate.getText().toString());
            dateMap.put("from", "" + etFromDate.getText().toString());

            filterMap.put("publish_date", dateMap);
            filterMap.put("product_id", "" + etpId.getText().toString());
            filterMap.put("id", "" + etId.getText().toString());

            requestMap.put("filter", filterMap.toString());
            pageNumber = 1;
            objectMap.put("parameters", requestMap);
            Log.d(TAG, "filter mao: " + objectMap);
            fromFilter = true;
            loadFaqData();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "filter error : " + e.getLocalizedMessage());
        }

    }
}