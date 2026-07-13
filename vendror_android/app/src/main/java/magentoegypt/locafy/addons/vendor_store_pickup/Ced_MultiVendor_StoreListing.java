package magentoegypt.locafy.addons.vendor_store_pickup;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
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

import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;
import magentoegypt.locafy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Ced_MultiVendor_StoreListing extends Ced_MultiVendor_NavigationActivity {

    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    Ced_MultiVendor_VendorSessionManagement vendorSessionManagement;
    String storelisting_url = "";
    Button Addnewstore;
    LinearLayout filtersection;
    Dialog listDialog;
    RecyclerView recyclerView_storeList;
    Ced_MultiVendor_StoreListing_Adapter Store_Adapter;
    HashMap<String, String> storeHashmap;
    ArrayList<HashMap<String, String>> StoreArraylist;
    boolean paginate = true;
    int current = 1;
    String isactive = "";
    String filterstring = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storelisting_url = session.getBase_Url() + "vstorepickupapi/store/listing";
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(getApplicationContext());
        vendorSessionManagement = new Ced_MultiVendor_VendorSessionManagement(getApplicationContext());
        storeHashmap = new HashMap<>();
        StoreArraylist = new ArrayList<>();

        if (connectionDetector.isConnectingToInternet()) {
            ViewGroup content = (ViewGroup) findViewById(R.id.MultiVendor_frame_container);
            getLayoutInflater().inflate(R.layout.ced_multivendor_storelisting, content, true);
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);
            recyclerView_storeList = (RecyclerView) findViewById(R.id.MultiVendor_storelist);
            Addnewstore = (Button) findViewById(R.id.MultiVendor_Add_newstore);
            filtersection = (LinearLayout) findViewById(R.id.MultiVendor_filtersection);
            filtersection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showfilter();
                }
            });

            storeHashmap.put("vendor_id", session.getVendorid());
//            storeHashmap.put("hashkey", getResources().getString(R.string.header));
            storeHashmap.put("hashkey", session.getHahkey());
            if (getIntent().getStringExtra("filter") != null) {
                filterstring = getIntent().getStringExtra("filter");
                storeHashmap.put("filter", filterstring);
            }
            Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
                @Override
                public void processFinish(Object output) {
                    try {
                        String string = output.toString();
                        JSONObject object = new JSONObject(string);
                        JSONObject data = object.getJSONObject("data");
                        String status = data.getString("status");
                        if (status.equals("true")) {
                            JSONArray storelist = data.getJSONArray("store_lists");
                            for (int i = 0; i < storelist.length(); i++) {
                                JSONObject store = storelist.getJSONObject(i);
                                HashMap<String, String> store_data = new HashMap<>();
                                store_data.put("store_id", store.getString("store_id"));
                                store_data.put("store_name", store.getString("store_name"));
                                store_data.put("store_manager", store.getString("store_manager_mame"));
                                store_data.put("store_manager_email", store.getString("store_manager_email"));
                                store_data.put("store_status", store.getString("status"));
                                StoreArraylist.add(store_data);
                            }
                            Store_Adapter = new Ced_MultiVendor_StoreListing_Adapter(Ced_MultiVendor_StoreListing.this, StoreArraylist);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            recyclerView_storeList.setLayoutManager(mLayoutManager);
                            recyclerView_storeList.setAdapter(Store_Adapter);

                            recyclerView_storeList.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                @Override
                                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                    super.onScrolled(recyclerView, dx, dy);
                                    if (!recyclerView.canScrollVertically(1) && paginate) {
                                        current = current + 1;
                                        paginate = false;

                                        Ced_MultiVendor_ClientRequestResponse scroll_request = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
                                            @Override
                                            public void processFinish(Object output) throws JSONException {
                                                String string = output.toString();
                                                JSONObject object = new JSONObject(string);
                                                JSONObject data = object.getJSONObject("data");
                                                String status = data.getString("status");
                                                if (status.equalsIgnoreCase("true")) {
                                                    paginate = false;
                                                } else {
                                                    scrolldata(string);
                                                }
                                            }
                                        }, Ced_MultiVendor_StoreListing.this, "POST", storeHashmap);
                                        scroll_request.execute(storelisting_url + "/page/" + current);
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(Ced_MultiVendor_StoreListing.this, data.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, Ced_MultiVendor_StoreListing.this, "POST", storeHashmap);
            response.execute(storelisting_url);

            Addnewstore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Ced_MultiVendor_StoreListing.this, Ced_MultiVendor_NewStore.class);
                    startActivity(intent);
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

    private void showfilter() {
        try {
            listDialog = new Dialog(this, R.style.PauseDialog);
            listDialog.setTitle(getResources().getString(R.string.alert_name));
            LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = Objects.requireNonNull(li).inflate(R.layout.ced_multivendor_filter_at_storelisting, null, false);
            final EditText pickupid = (EditText) v.findViewById(R.id.MultiVendor_store_pickupid);
            final EditText storename = (EditText) v.findViewById(R.id.MultiVendor_store_name);
            final EditText storemanagername = (EditText) v.findViewById(R.id.MultiVendor_store_manname);
            final EditText storemanageremail = (EditText) v.findViewById(R.id.MultiVendor_store_email);
            final Spinner storeisactive = (Spinner) v.findViewById(R.id.MultiVendor_store_isactive);
            final TextView setfilter = (TextView) v.findViewById(R.id.MultiVendor_setfilter);
            final TextView unsetfilter = (TextView) v.findViewById(R.id.MultiVendor_unsetfilter);

            if (!filterstring.isEmpty()) {
                JSONObject object = new JSONObject(filterstring);
                if (object.has("pickup_id")) {
                    pickupid.setText(object.getString("pickup_id"));
                }
                if (object.has("store_name")) {
                    storename.setText(object.getString("store_name"));
                }
                if (object.has("store_manager_name")) {
                    storemanagername.setText(object.getString("store_manager_name"));
                }
                if (object.has("store_manager_email")) {
                    storemanageremail.setText(object.getString("store_manager_email"));
                }
                if (object.has("is_active")) {
                    ArrayAdapter Adapterdesign = (ArrayAdapter) storeisactive.getAdapter();
                    int spinnerPosition = Adapterdesign.getPosition(object.getString("is_active"));
                    storeisactive.setSelection(spinnerPosition);
                }
            }
            storeisactive.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (adapterView.getSelectedItem().toString().equals("Enabled")) {
                        isactive = "1";
                    } else {
                        if (adapterView.getSelectedItem().toString().equals("Disabled")) {
                            isactive = "0";
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            unsetfilter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pickupid.setText("");
                    storename.setText("");
                    storemanageremail.setText("");
                    storemanagername.setText("");
                    storeisactive.setSelection(0);
                    Intent intent = new Intent(Ced_MultiVendor_StoreListing.this, Ced_MultiVendor_StoreListing.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                }
            });

            setfilter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        listDialog.dismiss();
                        JSONObject filter = new JSONObject();
                        if (pickupid.getText().toString().length() > 0) {
                            filter.put("pickup_id", pickupid.getText().toString());
                        }
                        if (storename.getText().toString().length() > 0) {
                            filter.put("store_name", storename.getText().toString());
                        }
                        if (storemanageremail.getText().toString().length() > 0) {
                            filter.put("store_manager_email", storemanageremail.getText().toString());
                        }
                        if (storemanagername.getText().toString().length() > 0) {
                            filter.put("store_manager_name", storemanagername.getText().toString());
                        }
                        if (!isactive.equals(getString(R.string.select_txt))) {
                            filter.put("is_active", isactive);
                        }
                        Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_StoreListing.class);
                        intent.putExtra("filter", filter.toString());
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                    } catch (Exception e) {
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

    public void scrolldata(String string) {
        try {
            JSONObject object = new JSONObject(string);
            JSONObject data = object.getJSONObject("data");
            String status = data.getString("status");
            if (status.equals("true")) {
                JSONArray storelist = data.getJSONArray("store_lists");
                for (int i = 0; i < storelist.length(); i++) {
                    JSONObject store = storelist.getJSONObject(i);
                    HashMap<String, String> store_data = new HashMap<>();
                    store_data.put("store_id", store.getString("store_id"));
                    store_data.put("store_name", store.getString("store_name"));
                    store_data.put("store_manager", store.getString("store_manager_mame"));
                    store_data.put("store_manager_email", store.getString("store_manager_email"));
                    store_data.put("store_status", store.getString("status"));
                    StoreArraylist.add(store_data);
                }
                Ced_MultiVendor_StoreListing_Adapter adapter = new Ced_MultiVendor_StoreListing_Adapter(Ced_MultiVendor_StoreListing.this, StoreArraylist);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Ced_MultiVendor_StoreListing.this);
                recyclerView_storeList.setLayoutManager(mLayoutManager);
                recyclerView_storeList.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                recyclerView_storeList.scrollToPosition(StoreArraylist.size() - 1);
                paginate = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}