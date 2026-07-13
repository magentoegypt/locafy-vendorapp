package magentoegypt.locafy.addons.vendor_pincode_checker.PincodeList;

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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_ClientRequestResponseRest_New;
import magentoegypt.locafy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class mvp_PincodeListing extends Ced_MultiVendor_NavigationActivity {

    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    String getPincodeList_url = "";
    LinearLayout filtersection;
    Dialog listDialog;
    RecyclerView recyclerView_storeList;
    mvp_PincodeList_Adapter Store_Adapter;
    JSONObject storeHashmap;
    ArrayList<HashMap<String, String>> StoreArraylist;
    boolean paginate = true;
    int current = 1;
    String is_cancod, is_canship = "none";
    String filterstring = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPincodeList_url = session.getBase_Url() + "rest/V1/pcapi/getPincodeList";
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(getApplicationContext());
        storeHashmap = new JSONObject();
        StoreArraylist = new ArrayList<>();

        if (connectionDetector.isConnectingToInternet()) {
            ViewGroup content = (ViewGroup) findViewById(R.id.MultiVendor_frame_container);
            getLayoutInflater().inflate(R.layout.activity_pincode_listing, content, true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);
            recyclerView_storeList = (RecyclerView) findViewById(R.id.pincode_list);
            filtersection = (LinearLayout) findViewById(R.id.MultiVendor_filtersection);
            filtersection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showfilter();
                }
            });

            try {
                storeHashmap.put("vendor_id", session.getVendorid());
                storeHashmap.put("page", String.valueOf(current));
                if (getIntent().getStringExtra("filter") != null) {
                    filterstring = getIntent().getStringExtra("filter");
                    storeHashmap.put("filter", filterstring);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            storeHashmap.put("hashkey", getResources().getString(R.string.header));
            Ced_ClientRequestResponseRest_New response = new Ced_ClientRequestResponseRest_New(new AsyncResponse() {
                @Override
                public void processFinish(Object output) throws JSONException {
                    try {
                        String string = output.toString();
                        JSONObject object = new JSONObject(string);
                        JSONObject data = object.getJSONObject("vendor_data");
                        String status = data.getString("success");
                        if (status.equals("true")) {
                            JSONArray storelist = data.getJSONArray("pincode_list");
                            for (int i = 0; i < storelist.length(); i++) {
                                JSONObject store = storelist.getJSONObject(i);
                                HashMap<String, String> store_data = new HashMap<String, String>();
                                store_data.put("pincode_id", store.getString("id"));
                                store_data.put("zipcode", store.getString("zipcode"));
                                store_data.put("can_cod", store.getString("can_cod"));
                                store_data.put("can_ship", store.getString("can_ship"));
                                store_data.put("days_to_deliver", store.getString("days_to_deliver"));
                                StoreArraylist.add(store_data);
                            }
                            Store_Adapter = new mvp_PincodeList_Adapter(mvp_PincodeListing.this, StoreArraylist);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            recyclerView_storeList.setLayoutManager(mLayoutManager);
                            recyclerView_storeList.setAdapter(Store_Adapter);

                            recyclerView_storeList.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                @Override
                                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                    super.onScrolled(recyclerView, dx, dy);
                                    if (!recyclerView.canScrollVertically(1) && paginate) {
                                        current = current + 1;
                                        try {
                                            storeHashmap.put("page", String.valueOf(current));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        paginate = false;

                                        Ced_ClientRequestResponseRest_New scroll_request = new Ced_ClientRequestResponseRest_New(new AsyncResponse() {
                                            @Override
                                            public void processFinish(Object output) throws JSONException {
                                                String string = output.toString();
                                                JSONObject object = new JSONObject(string);
                                                JSONObject data = object.getJSONObject("vendor_data");
                                                String status = data.getString("success");
                                                if (status.equalsIgnoreCase("true")) {
                                                    paginate = false;
                                                } else {
                                                    scrolldata(string);
                                                }
                                            }
                                        }, mvp_PincodeListing.this, "POST", storeHashmap.toString(), false, true);
                                        scroll_request.execute(getPincodeList_url);
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(mvp_PincodeListing.this, data.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, mvp_PincodeListing.this, "POST", storeHashmap.toString(), false, true);
            response.execute(getPincodeList_url);
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
            View v = li.inflate(R.layout.ced_multivendor_filter_at_managepincode, null, false);
            final EditText zipcode = (EditText) v.findViewById(R.id.MultiVendor_zipcode);
            final Spinner canship = (Spinner) v.findViewById(R.id.MultiVendor_canship);
            final Spinner cancod = (Spinner) v.findViewById(R.id.MultiVendor_cancod);
            final EditText daystodeliver = (EditText) v.findViewById(R.id.MultiVendor_daystodeliver);
            final TextView setfilter = (TextView) v.findViewById(R.id.MultiVendor_setfilter);
            final TextView unsetfilter = (TextView) v.findViewById(R.id.MultiVendor_unsetfilter);

            if (!filterstring.isEmpty()) {
                JSONObject object = new JSONObject(filterstring);
                if (object.has("zipcode")) {
                    zipcode.setText(object.getString("zipcode"));
                }

                if (object.has("days_to_deliver")) {
                    daystodeliver.setText(object.getString("days_to_deliver"));
                }

                if (object.has("can_cod")) {
                    ArrayAdapter can_cod_adp = (ArrayAdapter) cancod.getAdapter();
                    int spinnerPosition = can_cod_adp.getPosition(object.getString("can_cod"));
                    cancod.setSelection(spinnerPosition);
                }

                if (object.has("can_ship")) {
                    ArrayAdapter can_ship_adp = (ArrayAdapter) canship.getAdapter();
                    int spinnerPosition = can_ship_adp.getPosition(object.getString("can_ship"));
                    canship.setSelection(spinnerPosition);
                }
            }

            cancod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (adapterView.getSelectedItem().toString().equals("Yes")) {
                        is_cancod = "1";
                    } else if (adapterView.getSelectedItem().toString().equals("No")) {
                        is_cancod = "0";
                    } else
                        is_cancod = "none";
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            canship.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (adapterView.getSelectedItem().toString().equals("Yes")) {
                        is_canship = "1";
                    } else if (adapterView.getSelectedItem().toString().equals("No")) {
                        is_canship = "0";
                    } else
                        is_canship = "none";
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            unsetfilter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    zipcode.setText("");
                    cancod.setSelection(0);
                    canship.setSelection(0);
                    daystodeliver.setText("");

                    Intent intent = new Intent(mvp_PincodeListing.this, mvp_PincodeListing.class);
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
                        if (zipcode.getText().toString().length() > 0) {
                            filter.put("zipcode", zipcode.getText().toString());
                        }
                        if (daystodeliver.getText().toString().length() > 0) {
                            filter.put("days_to_deliver", daystodeliver.getText().toString());
                        }
                        if (is_cancod.equals("0") || is_cancod.equals("1"))
                            filter.put("can_cod", is_cancod);
                        if (is_canship.equals("0") || is_canship.equals("1"))
                            filter.put("can_ship", is_canship);
                        Intent intent = new Intent(getApplicationContext(), mvp_PincodeListing.class);
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
            JSONObject data = object.getJSONObject("vendor_data");
            String status = data.getString("success");
            if (status.equals("true")) {
                JSONArray storelist = data.getJSONArray("pincode_list");
                for (int i = 0; i < storelist.length(); i++) {
                    JSONObject store = storelist.getJSONObject(i);
                    HashMap<String, String> store_data = new HashMap<String, String>();
                    store_data.put("pincode_id", store.getString("id"));
                    store_data.put("zipcode", store.getString("zipcode"));
                    store_data.put("can_cod", store.getString("can_cod"));
                    store_data.put("can_ship", store.getString("can_ship"));
                    store_data.put("days_to_deliver", store.getString("days_to_deliver"));
                    StoreArraylist.add(store_data);
                }
                mvp_PincodeList_Adapter adapter = new mvp_PincodeList_Adapter(mvp_PincodeListing.this, StoreArraylist);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mvp_PincodeListing.this);
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