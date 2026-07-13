package magentoegypt.locafy.addons.faq;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProviders;

import com.google.gson.Gson;
import magentoegypt.locafy.R;
import magentoegypt.locafy.addons.faq.viewModel.FaqViewModel;
import magentoegypt.locafy.api_request_response_section.Ced_NewLoader;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import magentoegypt.locafy.base_app.base.CustomDynamicView;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddNewFAQ extends Ced_MultiVendor_NavigationActivity {
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    FaqViewModel viewModel;
    Ced_NewLoader cedNewLoader;
    Map<String, Object> objectMap = new HashMap<>();
    String vendorId = "";
    CustomDynamicView customDynamicView;
    LinearLayout _fieldView;
    Button buttonSaveFaqData;

    Map<String, Object> data = new HashMap<>();

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
            getLayoutInflater().inflate(R.layout.activity_add_new_faq, content, true);
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);


            init();
            observer();
            setListener();


        }
        else {
            Intent nointernet = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(nointernet);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }


    }

    private void observer() {
        String btnText = "";
        if (getIntent().hasExtra("faq_id")) {
            data.put("faq_id", getIntent().getStringExtra("faq_id"));
            btnText = getString(R.string.update_faq_product);
        } else btnText = getString(R.string.save);

        buttonSaveFaqData.setText(btnText);
        data.put("vendor_id", vendorId);
        objectMap.put("parameters", data);

        viewModel.fetchFields(objectMap).observe(this, o -> {
            Log.d("fetchFields", ": " + o);
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(new Gson().toJson(o));
                if (jsonArray.length() > 0) {
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    if (jsonObject.getBoolean("success")) {
                        generateDynamicFields(jsonObject.getJSONArray("data"));
                    } else showToast(jsonObject.getString("msg"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        });
    }

    private void generateDynamicFields(JSONArray data) {
        if (data.length() > 0) {
            customDynamicView = new CustomDynamicView(this, data, _fieldView);
            buttonSaveFaqData.setVisibility(View.VISIBLE);
        }
        else {
            showToast("No fields Data found !!");
            buttonSaveFaqData.setVisibility(View.GONE);
        }
    }

    private void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private void setListener() {
        findViewById(R.id.updateBtn).setOnClickListener(v -> {
            if (customDynamicView.validateForm()) {
                Map<String, Object> faq_Data = customDynamicView.paramMap();
                data.put("product_id", faq_Data.get("product_id"));
                faq_Data.remove("product_id");
                data.put("faq_Data", faq_Data);
                objectMap.put("parameters", data);
                Log.d("customDynamicView", ": " + objectMap);
                viewModel.saveFaqData(objectMap).observe(this, o -> {
                    try {
                        handleSaveFaqDataResponse(o);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
            }
        });
    }

    private void handleSaveFaqDataResponse(Object o) throws JSONException {
        JSONArray jsonArray = new JSONArray(new Gson().toJson(o));
        if (jsonArray.length() > 0) {
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            Toast.makeText(this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
            if (jsonObject.getBoolean("success")) {
//                onBackPressed();
                Intent intent = new Intent(this, FaqListScreen.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                finish();
            }

        }
    }

    private void init() {
        vendorId = session.getVendorid();
        buttonSaveFaqData = findViewById(R.id.updateBtn);
        _fieldView = findViewById(R.id.mainFieldView);
        cedNewLoader = new Ced_NewLoader(AddNewFAQ.this);
        viewModel = ViewModelProviders.of(AddNewFAQ.this).get((FaqViewModel.class));

        viewModel.error.observe(AddNewFAQ.this, s -> Toast.makeText(AddNewFAQ.this, s, Toast.LENGTH_SHORT).show());

        viewModel.loading.observe(AddNewFAQ.this, isLoading -> {
            updateLoader(isLoading);
            Log.d("orderViewModel", "loading: " + isLoading);
        });
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

}