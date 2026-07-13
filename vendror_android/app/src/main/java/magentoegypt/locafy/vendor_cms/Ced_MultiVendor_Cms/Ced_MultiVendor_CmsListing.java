package magentoegypt.locafy.vendor_cms.Ced_MultiVendor_Cms;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorSplash;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_ClientRequestResponseRest_New;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;
import magentoegypt.locafy.R;
import magentoegypt.locafy_constant.AppConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import static android.widget.Toast.LENGTH_SHORT;

public class Ced_MultiVendor_CmsListing extends Ced_MultiVendor_NavigationActivity {

    public static EditText MultiVendor_edt_start_date_creation;
    public static EditText MultiVendor_edt_end_date_creation;
    public static EditText MultiVendor_edt_start_date_update;
    public static EditText MultiVendor_edt_end_date_update;
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    Ced_MultiVendor_VendorSessionManagement vendorSessionManagement;
    RecyclerView recyclerView_cmsList;
    String cmslisting_url, dropdownoptions = "";
    ArrayList<HashMap<String, String>> CmsArraylist;
    Ced_MultiVendor_CMS_RecyclerAdapter Cms_Adapter;
    Button Addnewcms;
    LinearLayout filtersection;
    Dialog listDialog;
    LinearLayout cmssection;
    LinearLayout nodatafound;
    String is_active = "";
    String page_layout = "";
    String filterstring = "";
    boolean paginate = true;
    int current = 1;
    Boolean select_date_creation = false;
    JSONArray layout_data, store_ids, status;
    ArrayList<String> status_list, pageLayout_list;
    HashMap<String, String> status_hashmap, status_data, pageLayout_data, pageLayout_hashmap;
    private JSONObject response_data;
    private ArrayList<String> layout_list;
    private HashMap<String, String> hashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cmslisting_url = session.getBase_Url() + "rest/all/V1/vcmspage/listing";
//        cmslisting_url = session.getBase_Url() + "rest/all/V1/vcmspage/listing";
//        cmslisting_url = session.getBase_Url() + "vcmsapi/vcmspage/listing";
//        dropdownoptions = session.getBase_Url() + "rest/all/V1/vcms/dropdownoptions";
        dropdownoptions = session.getBase_Url() + "rest/all/V1/vcms/dropdownoptions";
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(getApplicationContext());
        vendorSessionManagement = new Ced_MultiVendor_VendorSessionManagement(getApplicationContext());
        CmsArraylist = new ArrayList<>();
        layout_data = new JSONArray();
        store_ids = new JSONArray();
        status = new JSONArray();
        status_list = new ArrayList<>();
        pageLayout_list = new ArrayList<>();
        status_data = new HashMap<>();
        pageLayout_data = new HashMap<>();
        status_hashmap = new HashMap<>();
        pageLayout_hashmap = new HashMap<>();
        Calendar date = Calendar.getInstance();
        SimpleDateFormat format1 = new SimpleDateFormat("HH:mm:ss", Locale.US);
        String formatted = format1.format(date.getTime());
        Log.i("DATEANDTIME", "onCreate: " + formatted);
        if (connectionDetector.isConnectingToInternet()) {
            ViewGroup content = (ViewGroup) findViewById(R.id.MultiVendor_frame_container);
            getLayoutInflater().inflate(R.layout.ced_multivendor_cmslisting, content, true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);
            recyclerView_cmsList = (RecyclerView) findViewById(R.id.MultiVendor_Cmslist);
            Addnewcms = (Button) findViewById(R.id.MultiVendor_Add_newcms);
            cmssection = (LinearLayout) findViewById(R.id.MultiVendor_cmssection);
            nodatafound = (LinearLayout) findViewById(R.id.MultiVendor_nodata);
            filtersection = (LinearLayout) findViewById(R.id.MultiVendor_filtersection);
            filtersection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showfilter();
                }
            });
            response_data = new JSONObject();
            if (getIntent().getStringExtra("filter") != null) {
                filterstring = getIntent().getStringExtra("filter");
            }
            getListingData();
            getFilterData();

            Addnewcms.setOnClickListener(view -> {
                Intent intent = new Intent(Ced_MultiVendor_CmsListing.this, Ced_MultiVendor_NewCms.class);
                intent.putExtra("layout_data", layout_data.toString());
                startActivity(intent);
                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
            });
        } else {
            Intent nointernet = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(nointernet);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }
    }

    private void getListingData() {
        try {
            final JSONObject postdata = new JSONObject();
            if (Ced_MultiVendor_VendorSplash.filterdata != null && Ced_MultiVendor_VendorSplash.filterdata.length() > 0) {
                postdata.put("filter", Ced_MultiVendor_VendorSplash.filterdata);
            }
            JSONObject o1 = new JSONObject();
            o1.put("page", String.valueOf(current));
            o1.put("vendor_id", session.getVendorid());
            postdata.put("data", o1);
            Ced_ClientRequestResponseRest_New response = new Ced_ClientRequestResponseRest_New(output -> {
                try {
                    String string = output.toString();
                    JSONObject object = new JSONObject(string);
                    String success = object.getString("success");
                    if (success.equals("true")) {
                        if (object.has("page_layout")) {
                            response_data = object.getJSONObject("page_layout");
                            layout_list = new ArrayList<>();
                            hashMap = new HashMap<>();
                            layout_list.add(getString(R.string.select_txt));
                            JSONArray jsonArray = response_data.names();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                hashMap.put(response_data.getString(jsonArray.getString(i)), jsonArray.getString(i));
                                layout_list.add(response_data.getString(jsonArray.getString(i)));
                            }
                        }
                        nodatafound.setVisibility(View.GONE);
                        // cmssection.setVisibility(View.VISIBLE);
                        recyclerView_cmsList.setVisibility(View.VISIBLE);

                        JSONArray cmslist = object.getJSONArray("cms_list");
                        if (cmslist.length() > 0) {
                            for (int i = 0; i < cmslist.length(); i++) {
                                JSONObject cms = cmslist.getJSONObject(i);
                                HashMap<String, String> cms_data = new HashMap<String, String>();
                                cms_data.put("page_id", cms.getString("page_id"));
                                cms_data.put("is_approve", cms.getString("is_approve"));
                                cms_data.put("title", cms.getString("title"));
                                cms_data.put("page_layout", cms.getString("page_layout"));
                                cms_data.put("is_active", cms.getString("is_active"));
                                cms_data.put("identifier", cms.getString("identifier"));
                                cms_data.put("creation_time", cms.getString("creation_time"));
                                cms_data.put("update_time", cms.getString("update_time"));
                                CmsArraylist.add(cms_data);
                            }
                            Cms_Adapter = new Ced_MultiVendor_CMS_RecyclerAdapter(Ced_MultiVendor_CmsListing.this, CmsArraylist, layout_data);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            recyclerView_cmsList.setLayoutManager(mLayoutManager);
                            recyclerView_cmsList.setAdapter(Cms_Adapter);
                            recyclerView_cmsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                @Override
                                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                    super.onScrolled(recyclerView, dx, dy);
                                    if (!recyclerView.canScrollVertically(1) && paginate) {
                                        try {
                                            current = current + 1;
                                            if (postdata.getJSONObject("data").has("page")) {
                                                postdata.getJSONObject("data").remove("page");
                                            }
                                            postdata.getJSONObject("data").put("page", String.valueOf(current));
                                            if (paginate) {
                                                Ced_ClientRequestResponseRest_New scroll_request = new Ced_ClientRequestResponseRest_New(new AsyncResponse() {
                                                    @Override
                                                    public void processFinish(Object output1) throws JSONException {
                                                        String string1 = output1.toString();
                                                        JSONObject object1 = new JSONObject(string1);
                                                        String success1 = object1.getString("success");
                                                        if (!success1.equalsIgnoreCase("true")) {
                                                            paginate = false;
                                                            Toast.makeText(Ced_MultiVendor_CmsListing.this, "" + object1.getString("message"), LENGTH_SHORT).show();
                                                        } else {
                                                            scrolldata(string1);
                                                        }
                                                    }
                                                }, Ced_MultiVendor_CmsListing.this, "POST", postdata.toString(), false, false);
                                                scroll_request.execute(cmslisting_url);
//                                            scroll_request.execute(cmslisting_url + "/page/" + current);
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
//                                    current = current + 1;
//                                    paginate = false;

//                                    Ced_MultiVendor_ClientRequestResponse scroll_request = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
//                                        @Override
//                                        public void processFinish(Object output) throws JSONException {
//                                            String string = output.toString();
//                                            JSONObject object = new JSONObject(string);
//                                            String success = object.getString("success");
//                                            if (!success.equalsIgnoreCase("true")) {
//                                                paginate = false;
//                                            } else {
//                                                scrolldata(string);
//                                            }
//                                        }
//                                    }, Ced_MultiVendor_CmsListing.this, "POST", postdata.toString());
//                                    scroll_request.execute(cmslisting_url + "/page/" + current);
                                    } else {
                                        Log.i("recycler", recyclerView.canScrollVertically(1) + " can scroll ");
                                    }
                                }
                            });
//                            Cms_Adapter.setOnBottomReachedListener(position -> {
//                                try {
//                                    current = current + 1;
//                                    if (postdata.getJSONObject("data").has("page")) {
//                                        postdata.getJSONObject("data").remove("page");
//                                    }
//                                    postdata.getJSONObject("data").put("page", String.valueOf(current));
//                                    if (paginate) {
//                                        Ced_ClientRequestResponseRest_New scroll_request = new Ced_ClientRequestResponseRest_New(new AsyncResponse() {
//                                            @Override
//                                            public void processFinish(Object output1) throws JSONException {
//                                                String string1 = output1.toString();
//                                                JSONObject object1 = new JSONObject(string1);
//                                                String success1 = object1.getString("success");
//                                                if (!success1.equalsIgnoreCase("true")) {
//                                                    paginate = false;
//                                                } else {
//                                                    scrolldata(string1);
//                                                }
//                                            }
//                                        }, Ced_MultiVendor_CmsListing.this, "POST", postdata.toString(), false, false);
//                                        scroll_request.execute(cmslisting_url);
////                                            scroll_request.execute(cmslisting_url + "/page/" + current);
//                                    }else{
//                                        Log.i("CED_MULTI_VENDOR_CMS", "getListingData: ");
//                                    }
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            });
                        }
                        recyclerView_cmsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                super.onScrolled(recyclerView, dx, dy);
                                if (!recyclerView.canScrollVertically(1) && paginate) {
                                    try {
                                        current = current + 1;
                                        if (postdata.getJSONObject("data").has("page")) {
                                            postdata.getJSONObject("data").remove("page");
                                        }
                                        postdata.getJSONObject("data").put("page", String.valueOf(current));
                                        if (paginate) {
                                            Ced_ClientRequestResponseRest_New scroll_request = new Ced_ClientRequestResponseRest_New(new AsyncResponse() {
                                                @Override
                                                public void processFinish(Object output1) throws JSONException {
                                                    String string1 = output1.toString();
                                                    JSONObject object1 = new JSONObject(string1);
                                                    String success1 = object1.getString("success");
                                                    if (!success1.equalsIgnoreCase("true")) {
                                                        paginate = false;
                                                    } else {
                                                        scrolldata(string1);
                                                    }
                                                }
                                            }, Ced_MultiVendor_CmsListing.this, "POST", postdata.toString(), false, false);
                                            scroll_request.execute(cmslisting_url);
//                                            scroll_request.execute(cmslisting_url + "/page/" + current);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
//                                    current = current + 1;
//                                    paginate = false;

//                                    Ced_MultiVendor_ClientRequestResponse scroll_request = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
//                                        @Override
//                                        public void processFinish(Object output) throws JSONException {
//                                            String string = output.toString();
//                                            JSONObject object = new JSONObject(string);
//                                            String success = object.getString("success");
//                                            if (!success.equalsIgnoreCase("true")) {
//                                                paginate = false;
//                                            } else {
//                                                scrolldata(string);
//                                            }
//                                        }
//                                    }, Ced_MultiVendor_CmsListing.this, "POST", postdata.toString());
//                                    scroll_request.execute(cmslisting_url + "/page/" + current);
                                } else {
                                    Log.i("recycler", recyclerView.canScrollVertically(1) + " can scroll ");
                                }
                            }
                        });
                    } else {
                        if (object.has("page_layout")) {
                            response_data = object.getJSONObject("page_layout");
                            layout_list = new ArrayList<>();
                            hashMap = new HashMap<>();
                            layout_list.add(getString(R.string.select_txt));
                            JSONArray jsonArray = response_data.names();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                hashMap.put(response_data.getString(jsonArray.getString(i)), jsonArray.getString(i));
                                layout_list.add(response_data.getString(jsonArray.getString(i)));
                            }
                        }
                        nodatafound.setVisibility(View.VISIBLE);
                        // cmssection.setVisibility(View.GONE);
                        recyclerView_cmsList.setVisibility(View.GONE);
                        Toast.makeText(Ced_MultiVendor_CmsListing.this, object.getString("message"), LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, Ced_MultiVendor_CmsListing.this, "POST", postdata.toString(), false, false);
            response.execute(cmslisting_url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getFilterData() {
        Ced_ClientRequestResponseRest_New crr = new Ced_ClientRequestResponseRest_New(new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                try {
                    String string = output.toString();
                    JSONObject object = new JSONObject(string);
                    String success = object.getString("is_success");
                    if (success.equals("true")) {
                        JSONObject dropdown_data = object.getJSONObject("dropdown_data");
                        Log.e("frozen", "names= " + dropdown_data.names());
                        JSONArray names = dropdown_data.names();
                        if (names.length() > 0)
                            for (int n = 0; n < names.length(); n++) {
                                switch (names.getString(n)) {
                                    case "layout_data":
                                        layout_data = dropdown_data.getJSONArray(names.getString(n));
                                        break;
                                    case "store_ids":
                                        store_ids = dropdown_data.getJSONArray(names.getString(n));
                                        break;
                                    case "status":
                                        status = dropdown_data.getJSONArray(names.getString(n));
                                        break;
                                    default:
                                        Log.e("frozen", "extra data = " + dropdown_data.getJSONArray(names.getString(n)));
                                }
                            }
                    } else {
                        Toast.makeText(Ced_MultiVendor_CmsListing.this, object.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, Ced_MultiVendor_CmsListing.this);
        crr.execute(dropdownoptions);
    }

    private void showfilter() {
        try {
            listDialog = new Dialog(this, R.style.PauseDialog);
            listDialog.setTitle(getResources().getString(R.string.alert_name));
            LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = li.inflate(R.layout.ced_multivendor_filter_at_cmslisting, null, false);
            final EditText cmspageid = (EditText) v.findViewById(R.id.MultiVendor_cms_pageid);
            final EditText cmspagetitle = (EditText) v.findViewById(R.id.MultiVendor_cms_title);
            final EditText cmsidentifier = (EditText) v.findViewById(R.id.MultiVendor_cms_identifier);
            MultiVendor_edt_start_date_creation = (EditText) v.findViewById(R.id.MultiVendor_cms_creationtime1);
            MultiVendor_edt_end_date_creation = (EditText) v.findViewById(R.id.MultiVendor_cms_creationtime2);
            MultiVendor_edt_start_date_update = (EditText) v.findViewById(R.id.MultiVendor_cms_updationtime1);
            MultiVendor_edt_end_date_update = (EditText) v.findViewById(R.id.MultiVendor_cms_updationtime2);
            final Spinner cms_pagelayout = (Spinner) v.findViewById(R.id.MultiVendor_cms_pagelayout);
            for (int k = 0; k < layout_data.length(); k++) {
                JSONObject jsonObject = layout_data.getJSONObject(k);
                pageLayout_list.add(jsonObject.getString("label"));
                pageLayout_hashmap.put(jsonObject.getString("label"), jsonObject.getString("key"));
                pageLayout_data.put(jsonObject.getString("key"), jsonObject.getString("label"));
            }
            cms_pagelayout.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.simple_list_item_1, pageLayout_list));
            final TextView setfilter = (TextView) v.findViewById(R.id.MultiVendor_setfilter);
            final TextView unsetfilter = (TextView) v.findViewById(R.id.MultiVendor_unsetfilter);
            final Spinner cms_isactive = (Spinner) v.findViewById(R.id.MultiVendor_cms_isactive);
            for (int k = 0; k < status.length(); k++) {
                JSONObject jsonObject = status.getJSONObject(k);
                status_list.add(jsonObject.getString("label"));
                status_hashmap.put(jsonObject.getString("label"), jsonObject.getString("key"));
                status_data.put(jsonObject.getString("key"), jsonObject.getString("label"));
            }
            cms_isactive.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.simple_list_item_1, status_list));

//            cms_pagelayout.setAdapter(new ArrayAdapter<>(Ced_MultiVendor_CmsListing.this, R.layout.simple_list_item_1, layout_list));
            if (!(filterstring.isEmpty())) {
                JSONObject object = new JSONObject(filterstring);
                if (object.has("page_id")) {
                    cmspageid.setText(object.getString("page_id"));
                }
                if (object.has("title")) {
                    cmspagetitle.setText(object.getString("title"));
                }

                if (object.has("is_active")) {
                    if (!object.getString("is_active").equalsIgnoreCase(""))
                        cms_isactive.setSelection(status_list.indexOf(status_data.get(object.getString("is_active"))));
                    Log.d("frozen", "saved status spn= " + status_list.indexOf(status_data.get(object.getString("is_active"))));
                }

                if (object.has("page_layout")) {
                    if (!object.getString("page_layout").equalsIgnoreCase(""))
                        cms_pagelayout.setSelection(pageLayout_list.indexOf(pageLayout_data.get(object.getString("page_layout"))));
                    Log.d("frozen", "saved page_layout spn= " + pageLayout_list.indexOf(pageLayout_data.get(object.getString("page_layout"))));
                    /*ArrayAdapter Adapterdesign = (ArrayAdapter) cms_pagelayout.getAdapter();
                    int spinnerPosition = Adapterdesign.getPosition(object.getString("page_layout"));
                    cms_pagelayout.setSelection(spinnerPosition);*/
                }

                if (object.has("identifier")) {
                    cmsidentifier.setText(object.getString("identifier"));
                }

                if (object.has("created_at")) {
                    if (object.getJSONObject("created_at").has("from")) {
                        MultiVendor_edt_start_date_creation.setText(object.getJSONObject("created_at").getString("from"));
                    }

                    if (object.getJSONObject("created_at").has("to")) {
                        MultiVendor_edt_end_date_creation.setText(object.getJSONObject("created_at").getString("to"));
                    }
                }

                if (object.has("updated_at")) {
                    if (object.getJSONObject("updated_at").has("from")) {
                        MultiVendor_edt_start_date_update.setText(object.getJSONObject("updated_at").getString("from"));
                    }
                    if (object.getJSONObject("updated_at").has("to")) {
                        MultiVendor_edt_end_date_update.setText(object.getJSONObject("updated_at").getString("to"));
                    }
                }
            }

            cms_isactive.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.d("frozen", "is_active onItemSelected= " + status_hashmap.get(adapterView.getSelectedItem().toString()));
                    is_active = status_hashmap.get(adapterView.getSelectedItem().toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            cms_pagelayout.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.d("frozen", "page_layout onItemSelected= " + pageLayout_hashmap.get(adapterView.getSelectedItem().toString()));
                    page_layout = pageLayout_hashmap.get(adapterView.getSelectedItem().toString());
//                    page_layout = /*hashMap.get(*/cms_pagelayout.getSelectedItem().toString()/*)*/;
                    // page_layout = adapterView.getSelectedItem().toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            MultiVendor_edt_start_date_creation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    select_date_creation = true;
                    AppConstant.setDateAndTimeFrom(Ced_MultiVendor_CmsListing.this, MultiVendor_edt_start_date_creation);
                }
            });

            MultiVendor_edt_end_date_creation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (select_date_creation) {
                        AppConstant.setDateAndTimeTo(Ced_MultiVendor_CmsListing.this, MultiVendor_edt_end_date_creation, MultiVendor_edt_start_date_creation);

                    } else {
                        Toast.makeText(Ced_MultiVendor_CmsListing.this, R.string.please_fill_both_dates, Toast.LENGTH_SHORT).show();
                    }
                }
            });

            MultiVendor_edt_start_date_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    select_date_creation = true;
                    AppConstant.setDateAndTimeFrom(Ced_MultiVendor_CmsListing.this, MultiVendor_edt_start_date_update);
                }
            });

            MultiVendor_edt_end_date_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (select_date_creation) {
                        AppConstant.setDateAndTimeTo(Ced_MultiVendor_CmsListing.this, MultiVendor_edt_end_date_update, MultiVendor_edt_start_date_update);
                    } else {
                        Toast.makeText(Ced_MultiVendor_CmsListing.this, R.string.please_fill_both_dates, Toast.LENGTH_SHORT).show();
                    }
                }
            });

            unsetfilter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cmspageid.setText("");
                    cmspagetitle.setText("");
                    cmsidentifier.setText("");
                    MultiVendor_edt_start_date_creation.setText("");
                    MultiVendor_edt_end_date_creation.setText("");
                    MultiVendor_edt_start_date_update.setText("");
                    MultiVendor_edt_end_date_update.setText("");
                    cms_pagelayout.setSelection(0);
                    Ced_MultiVendor_VendorSplash.filterdata = new JSONObject();
                    Intent intent = new Intent(Ced_MultiVendor_CmsListing.this, Ced_MultiVendor_CmsListing.class);
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
                        JSONObject creationtime = new JSONObject();
                        JSONObject updationtime = new JSONObject();
                        if (cmspageid.getText().toString().length() > 0) {
                            filter.put("page_id", cmspageid.getText().toString());
                        }
                        if (cmspagetitle.getText().toString().length() > 0) {
                            filter.put("title", cmspagetitle.getText().toString());
                        }
                        if (cmsidentifier.getText().toString().length() > 0) {
                            filter.put("identifier", cmsidentifier.getText().toString());
                        }
                        if (MultiVendor_edt_start_date_creation.getText().toString().length() > 0) {
                            creationtime.put("from", MultiVendor_edt_start_date_creation.getText().toString());
                        }
                        if (MultiVendor_edt_end_date_creation.getText().toString().length() > 0)
                            creationtime.put("to", MultiVendor_edt_end_date_creation.getText().toString());
                        if (creationtime.has("from") || creationtime.has("to")) {
                            filter.put("created_at", creationtime);
                        }
                        if (MultiVendor_edt_start_date_update.getText().toString().length() > 0) {
                            updationtime.put("from", MultiVendor_edt_start_date_update.getText().toString());
                        }
                        if (MultiVendor_edt_end_date_update.getText().toString().length() > 0) {
                            updationtime.put("to", MultiVendor_edt_end_date_update.getText().toString());
                        }
                        if (updationtime.has("from") || updationtime.has("to")) {
                            filter.put("updated_at", updationtime);
                        }
                        filter.put("is_active", is_active);
                        filter.put("page_layout", page_layout);
                        Ced_MultiVendor_VendorSplash.filterdata = filter;
                        Intent intent = new Intent(Ced_MultiVendor_CmsListing.this, Ced_MultiVendor_CmsListing.class);
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
            paginate = true;
            JSONObject object = new JSONObject(string);
            String success = object.getString("success");
            if (success.equals("true")) {
                JSONArray cmslist = object.getJSONArray("cms_list");
                if (cmslist.length() > 0) {
                    for (int i = 0; i < cmslist.length(); i++) {
                        JSONObject cms = cmslist.getJSONObject(i);
                        HashMap<String, String> cms_data = new HashMap<>();
                        cms_data.put("page_id", cms.getString("page_id"));
                        cms_data.put("is_approve", cms.getString("is_approve"));
                        cms_data.put("title", cms.getString("title"));
                        cms_data.put("page_layout", cms.getString("page_layout"));
                        cms_data.put("is_active", cms.getString("is_active"));
                        cms_data.put("identifier", cms.getString("identifier"));
                        cms_data.put("creation_time", cms.getString("creation_time"));
                        cms_data.put("update_time", cms.getString("update_time"));
                        CmsArraylist.add(cms_data);
                    }
                    Ced_MultiVendor_CMS_RecyclerAdapter adapter = new Ced_MultiVendor_CMS_RecyclerAdapter(Ced_MultiVendor_CmsListing.this, CmsArraylist, layout_data);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Ced_MultiVendor_CmsListing.this);
                    recyclerView_cmsList.setLayoutManager(mLayoutManager);
                    recyclerView_cmsList.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    recyclerView_cmsList.scrollToPosition(CmsArraylist.size() - 1);
                    paginate = true;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}