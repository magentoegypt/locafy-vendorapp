package magentoegypt.locafy.addons.inventory;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import magentoegypt.locafy.R;
import magentoegypt.locafy.addons.inventory.viewModels.InventoryViewModel;
import magentoegypt.locafy.api_request_response_section.Ced_NewLoader;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SaveInventory extends Ced_MultiVendor_NavigationActivity {

    Ced_MultiVendor_ConnectionDetector connectionDetector;
    InventoryViewModel viewModel;
    Ced_NewLoader cedNewLoader;
    Map<String, Object> objectMap = new HashMap<>();
    Map<String, Object> data = new HashMap<>();
    String vendorId = "";
    TextInputEditText etInventory;

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
            getLayoutInflater().inflate(R.layout.activity_save_inventory, content, true);
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);

            init();
            observers();
            setListener();

        } else {
            Intent nointernet = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(nointernet);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }
    }

    private void observers() {
        viewModel.loadMinimumInventory(objectMap);
        viewModel.observeMinimumInventory().observe(SaveInventory.this, this::handleMinimumInventoryData);

        viewModel.observeSaveInventory().observe(SaveInventory.this, this::handleResponse);


    }

    private void handleMinimumInventoryData(Object o) {
        try {
            JSONObject jsonObject = new JSONObject(new Gson().toJson(o));
            JSONObject vendorData = jsonObject.getJSONObject("vendor_data");
            if (vendorData.getBoolean("success")) {
                if (vendorData.has("minimum_quantity"))
                    etInventory.setText(MessageFormat.format("{0}", vendorData.getInt("minimum_quantity")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setListener() {
        findViewById(R.id.btnSaveInventory).setOnClickListener(view -> {
            String inventoryNumber = Objects.requireNonNull(etInventory.getText()).toString();
            if (!TextUtils.isEmpty(inventoryNumber)) {
                saveInventory(inventoryNumber);
            } else Toast.makeText(this, "enter minimum products number", Toast.LENGTH_SHORT).show();
        });
    }

    private void saveInventory(String inventoryNumber) {
        data.put("minimum_quantity", inventoryNumber);
        objectMap.put("data", data);
        viewModel.saveInventory(objectMap);
    }


    private void init() {
        etInventory = findViewById(R.id.etInventoryNumber);

        vendorId = session.getVendorid();
        data.put("vendor_id", vendorId);
        objectMap.put("data", data);

        cedNewLoader = new Ced_NewLoader(SaveInventory.this);
        viewModel = ViewModelProviders.of(SaveInventory.this).get((InventoryViewModel.class));


        viewModel.error.observe(SaveInventory.this, s -> Toast.makeText(SaveInventory.this, s, Toast.LENGTH_SHORT).show());

        viewModel.loading.observe(SaveInventory.this, this::updateLoader);
    }

    private void handleResponse(Object o) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(new Gson().toJson(o));
            if (jsonObject.has("success")) {
                if (jsonObject.has("message"))
                    Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

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