package magentoegypt.locafy.vendor_cms.Ced_MultiVendor_StaticBlock;

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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class Ced_MultiVendor_BlockListing extends Ced_MultiVendor_NavigationActivity {

    public static EditText MultiVendor_edt_start_date_creation;
    public static EditText MultiVendor_edt_end_date_creation;
    public static EditText MultiVendor_edt_start_date_update;
    public static EditText MultiVendor_edt_end_date_update;
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    Ced_MultiVendor_VendorSessionManagement vendorSessionManagement;
    RecyclerView recyclerView_blockList;
    String blocklisting_url, dropdownoptions = "";
    ArrayList<HashMap<String, String>> BlockArraylist;
    Ced_MultiVendor_Block_RecyclerAdapter Block_Adapter;
    Button Addnewblock;
    LinearLayout filtersection;
    LinearLayout nodatafound;
    LinearLayout blocksection;
    Dialog listDialog;
    Calendar newCalendar;
    String is_active = "0";
    String filterstring = "";
    boolean paginate = true;
    int current = 1;
    Boolean select_date_creation = false;
    Boolean select_date = false;
    JSONArray layout_data, store_ids, status;
    ArrayList<String> status_list;
    HashMap<String, String> status_hashmap, status_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        blocklisting_url = session.getBase_Url() + "rest/all/V1/vcmsblock/listing";
        dropdownoptions = session.getBase_Url() + "rest/all/V1/vcms/dropdownoptions";
//        blocklisting_url = session.getBase_Url() + "vcmsapi/vcmsblock/listing";
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(getApplicationContext());
        vendorSessionManagement = new Ced_MultiVendor_VendorSessionManagement(getApplicationContext());
        BlockArraylist = new ArrayList<>();
        layout_data = new JSONArray();
        store_ids = new JSONArray();
        status = new JSONArray();
        status_list = new ArrayList<>();
        status_data = new HashMap<>();
        status_hashmap = new HashMap<>();
        if (connectionDetector.isConnectingToInternet()) {
            ViewGroup content = (ViewGroup) findViewById(R.id.MultiVendor_frame_container);
            getLayoutInflater().inflate(R.layout.ced_multivendor_blocklisting, content, true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);
            recyclerView_blockList = (RecyclerView) findViewById(R.id.MultiVendor_Blocklist);
            Addnewblock = (Button) findViewById(R.id.MultiVendor_Add_newblock);
            blocksection = (LinearLayout) findViewById(R.id.MultiVendor_blocksection);
            nodatafound = (LinearLayout) findViewById(R.id.MultiVendor_nodata);
            filtersection = (LinearLayout) findViewById(R.id.MultiVendor_filtersection);
            filtersection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppConstant.lockButton(v);
                    showfilter();
                }
            });
            if (getIntent().getStringExtra("filter") != null) {
                Log.e("frozen", "getIntent filter= " + Ced_MultiVendor_VendorSplash.filterdata);
                filterstring = getIntent().getStringExtra("filter");
            }
            getListingData();
            getFilterData();
            //==ADD NEW BLOCK==
            Addnewblock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppConstant.lockButton(view);
                    Intent intent = new Intent(Ced_MultiVendor_BlockListing.this, Ced_MultiVendor_NewBlock.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }
            });
        } else {
            Intent nointernet = new Intent(Ced_MultiVendor_BlockListing.this, Ced_MultiVendor_NoInternetconnection.class);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(nointernet);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
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
                        Toast.makeText(Ced_MultiVendor_BlockListing.this, object.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, Ced_MultiVendor_BlockListing.this);
        crr.execute(dropdownoptions);
    }

    private void getListingData() {
        try {
            final JSONObject postdata = new JSONObject();
            if (Ced_MultiVendor_VendorSplash.filterdata != null && Ced_MultiVendor_VendorSplash.filterdata.length() > 0) {
                postdata.put("filter", Ced_MultiVendor_VendorSplash.filterdata);
            }
            JSONObject o1 = new JSONObject();
//            o1.put("block_id", "0");
            o1.put("page", "1");
            o1.put("vendor_id", session.getVendorid());
            postdata.put("data", o1);
            Ced_ClientRequestResponseRest_New response = new Ced_ClientRequestResponseRest_New(new AsyncResponse() {
                @Override
                public void processFinish(Object output) {
                    try {
                        String string = output.toString();
                        JSONObject object = new JSONObject(string);
                        String success = object.getString("success");
                        if (success.equals("true")) {
                            nodatafound.setVisibility(View.GONE);
                            //blocksection.setVisibility(View.VISIBLE);
                            recyclerView_blockList.setVisibility(View.VISIBLE);
                            JSONArray blocklist = object.getJSONArray("cms_list");
                            for (int i = 0; i < blocklist.length(); i++) {
                                JSONObject block = blocklist.getJSONObject(i);
                                HashMap<String, String> block_data = new HashMap<String, String>();
                                block_data.put("blockid", block.getString("block_id"));
                                block_data.put("title", block.getString("title"));
                                block_data.put("identifier", block.getString("identifier"));
                                block_data.put("content", block.getString("content"));
                                block_data.put("vendorid", block.getString("vendor_id"));
                                block_data.put("isapprove", block.getString("is_approve"));
                                block_data.put("creationtime", block.getString("creation_time"));
                                block_data.put("updationtime", block.getString("update_time"));
                                block_data.put("isactive", block.getString("is_active"));
                                BlockArraylist.add(block_data);
                            }
                            Block_Adapter = new Ced_MultiVendor_Block_RecyclerAdapter(Ced_MultiVendor_BlockListing.this, BlockArraylist);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Ced_MultiVendor_BlockListing.this);
                            recyclerView_blockList.setLayoutManager(mLayoutManager);
                            recyclerView_blockList.setAdapter(Block_Adapter);
                            recyclerView_blockList.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                @Override
                                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                    super.onScrolled(recyclerView, dx, dy);
                                    if (!recyclerView.canScrollVertically(1) && paginate) {
                                        try {
                                            current = current + 1;
                                            if (postdata.getJSONObject("data").has("page")) {
                                                postdata.getJSONObject("data").getString("page");
                                            }
                                            postdata.getJSONObject("data").put("page", String.valueOf(current));
                                            Ced_ClientRequestResponseRest_New scroll_request = new Ced_ClientRequestResponseRest_New(new AsyncResponse() {
                                                @Override
                                                public void processFinish(Object output) throws JSONException {
                                                    String string = output.toString();
                                                    JSONObject object = new JSONObject(string);
                                                    String success = object.getString("success");
                                                    if (!success.equalsIgnoreCase("true")) {
                                                        paginate = false;
                                                        Toast.makeText(Ced_MultiVendor_BlockListing.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        scrolldata(string);
                                                    }
                                                }
                                            }, Ced_MultiVendor_BlockListing.this, "POST", postdata.toString(), false, false);
                                            scroll_request.execute(blocklisting_url);
//                                        scroll_request.execute(blocklisting_url + "/page/" + current);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } else {
//                                        Log.i("recycler", recyclerView.canScrollVertically(1) + " can scroll ");
                                    }
                                }
                            });
                        } else {
                            nodatafound.setVisibility(View.VISIBLE);
                            // blocksection.setVisibility(View.GONE);
                            recyclerView_blockList.setVisibility(View.GONE);
                            Toast.makeText(Ced_MultiVendor_BlockListing.this, object.getString("message"), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, Ced_MultiVendor_BlockListing.this, "POST", postdata.toString(), false, false);
            response.execute(blocklisting_url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showfilter() {
        try {
            listDialog = new Dialog(this, R.style.PauseDialog);
            listDialog.setTitle(getResources().getString(R.string.alert_name));
            LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = li.inflate(R.layout.ced_multivendor_filter_at_blocklisting, null, false);
            final EditText blockid = (EditText) v.findViewById(R.id.MultiVendor_block_id);
            final EditText blocktitle = (EditText) v.findViewById(R.id.MultiVendor_block_title);
            final EditText blockidentifier = (EditText) v.findViewById(R.id.MultiVendor_block_identifier);
            MultiVendor_edt_start_date_creation = (EditText) v.findViewById(R.id.MultiVendor_block_creationtime1);
            MultiVendor_edt_end_date_creation = (EditText) v.findViewById(R.id.MultiVendor_block_creationtime2);
            MultiVendor_edt_start_date_update = (EditText) v.findViewById(R.id.MultiVendor_block_updationtime1);
            MultiVendor_edt_end_date_update = (EditText) v.findViewById(R.id.MultiVendor_block_updationtime2);
            final Spinner block_isactive = (Spinner) v.findViewById(R.id.MultiVendor_block_isactive);
            for (int k = 0; k < status.length(); k++) {
                JSONObject jsonObject = status.getJSONObject(k);
                status_list.add(jsonObject.getString("label"));
                status_hashmap.put(jsonObject.getString("label"), jsonObject.getString("key"));
                status_data.put(jsonObject.getString("key"), jsonObject.getString("label"));
            }
            block_isactive.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.simple_list_item_1, status_list));
            final TextView setfilter = (TextView) v.findViewById(R.id.MultiVendor_setfilter);
            final TextView unsetfilter = (TextView) v.findViewById(R.id.MultiVendor_unsetfilter);
            if (!(filterstring.isEmpty())) {
                JSONObject object = new JSONObject(filterstring);
                if (object.has("block_id")) {
                    blockid.setText(object.getString("block_id"));
                }
                if (object.has("title")) {
                    blocktitle.setText(object.getString("title"));
                }
                if (object.has("is_active")) {
                    if (!object.getString("is_active").equalsIgnoreCase(""))
                        block_isactive.setSelection(status_list.indexOf(status_data.get(object.getString("is_active"))));
                    Log.d("frozen", "saved status spn= " + status_list.indexOf(status_data.get(object.getString("is_active"))));
                }
                if (object.has("identifier")) {
                    blockidentifier.setText(object.getString("identifier"));
                }
                if (object.has("created_at")) {
                    if (object.getJSONObject("created_at").has("from")) {
                        MultiVendor_edt_start_date_creation.setText(object.getJSONObject("created_at").getString("from"));
                    }
                    if (object.getJSONObject("created_at").has("to")) {
                        MultiVendor_edt_start_date_creation.setText(object.getJSONObject("created_at").getString("to"));
                    }
                }
                if (object.has("updated_at")) {
                    if (object.getJSONObject("updated_at").has("from")) {
                        MultiVendor_edt_end_date_update.setText(object.getJSONObject("updated_at").getString("from"));
                    }
                    if (object.getJSONObject("updated_at").has("to")) {
                        MultiVendor_edt_end_date_update.setText(object.getJSONObject("updated_at").getString("to"));
                    }
                }
            }
            block_isactive.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.d("frozen", "onItemSelected=1= " + status_hashmap.get(adapterView.getSelectedItem().toString()));
                    is_active = status_hashmap.get(adapterView.getSelectedItem().toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            MultiVendor_edt_start_date_creation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppConstant.lockButton(v);
                    select_date_creation = true;
                    AppConstant.setDateAndTimeFrom(Ced_MultiVendor_BlockListing.this, MultiVendor_edt_start_date_creation);
                }
            });

            MultiVendor_edt_end_date_creation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppConstant.lockButton(v);
                    if (select_date_creation) {
                        AppConstant.setDateAndTimeTo(Ced_MultiVendor_BlockListing.this, MultiVendor_edt_end_date_creation, MultiVendor_edt_start_date_creation);
                    } else {
                        Toast.makeText(Ced_MultiVendor_BlockListing.this, R.string.please_fill_both_dates, Toast.LENGTH_SHORT).show();
                    }
                }
            });

            MultiVendor_edt_end_date_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppConstant.lockButton(v);
                    if (select_date) {
                        AppConstant.setDateAndTimeTo(Ced_MultiVendor_BlockListing.this, MultiVendor_edt_end_date_update, MultiVendor_edt_start_date_update);
                    } else {
                        Toast.makeText(Ced_MultiVendor_BlockListing.this, R.string.please_fill_both_dates, Toast.LENGTH_SHORT).show();
                    }
                }
            });

            MultiVendor_edt_start_date_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppConstant.lockButton(view);
                    select_date = true;
                    AppConstant.setDateAndTimeFrom(Ced_MultiVendor_BlockListing.this, MultiVendor_edt_start_date_update);
                }
            });

            unsetfilter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppConstant.lockButton(v);
                    blockid.setText("");
                    blocktitle.setText("");
                    blockidentifier.setText("");
                    MultiVendor_edt_start_date_creation.setText("");
                    MultiVendor_edt_end_date_creation.setText("");
                    MultiVendor_edt_start_date_update.setText("");
                    MultiVendor_edt_end_date_update.setText("");
                    listDialog.hide();
                    Ced_MultiVendor_VendorSplash.filterdata = new JSONObject();
                    Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_BlockListing.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                }
            });

            setfilter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppConstant.lockButton(v);
                    try {
                        listDialog.dismiss();
                        JSONObject filter = new JSONObject();
                        JSONObject creationtime = new JSONObject();
                        JSONObject updationtime = new JSONObject();
                        if (blockid.getText().toString().length() > 0) {
                            filter.put("block_id", blockid.getText().toString());
                        }
                        if (blocktitle.getText().toString().length() > 0) {
                            filter.put("title", blocktitle.getText().toString());
                        }
                        filter.put("is_active", is_active);
                        if (blockidentifier.getText().toString().length() > 0) {
                            filter.put("identifier", blockidentifier.getText().toString());
                        }
                        if (MultiVendor_edt_start_date_creation.getText().toString().length() > 0) {
                            creationtime.put("from", MultiVendor_edt_start_date_creation.getText().toString());
                        }
                        if (MultiVendor_edt_end_date_creation.getText().toString().length() > 0) {
                            creationtime.put("to", MultiVendor_edt_end_date_creation.getText().toString());
                        }
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
                        Ced_MultiVendor_VendorSplash.filterdata = filter;
                        Log.e("frozen", "filter= " + Ced_MultiVendor_VendorSplash.filterdata);
                        Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_BlockListing.class);
                        intent.putExtra("filter", filter.toString());
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        overridePendingTransition(R.anim.ced_multivendor_slide_in_sequence, R.anim.ced_multivendor_slide_out_sequence);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.i("errrors", e.toString());
                    }
                }
            });
            listDialog.setContentView(v);
            listDialog.setCancelable(true);
            listDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("errrors", e.toString());
        }
    }

    public void scrolldata(String string) {
        try {
            JSONObject object = new JSONObject(string);
            String success = object.getString("success");
            if (success.equals("true")) {
                JSONArray blocklist = object.getJSONArray("cms_list");
                for (int i = 0; i < blocklist.length(); i++) {
                    JSONObject block = blocklist.getJSONObject(i);
                    HashMap<String, String> block_data = new HashMap<String, String>();
                    block_data.put("blockid", block.getString("block_id"));
                    block_data.put("title", block.getString("title"));
                    block_data.put("identifier", block.getString("identifier"));
                    block_data.put("content", block.getString("content"));
                    block_data.put("vendorid", block.getString("vendor_id"));
                    block_data.put("creationtime", block.getString("creation_time"));
                    block_data.put("updationtime", block.getString("update_time"));
                    block_data.put("isactive", block.getString("is_active"));
                    block_data.put("isapprove", block.getString("is_approve"));
                    BlockArraylist.add(block_data);
                }
                Ced_MultiVendor_Block_RecyclerAdapter adapter = new Ced_MultiVendor_Block_RecyclerAdapter(Ced_MultiVendor_BlockListing.this, BlockArraylist);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Ced_MultiVendor_BlockListing.this);
                recyclerView_blockList.setLayoutManager(mLayoutManager);
                recyclerView_blockList.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                recyclerView_blockList.scrollToPosition(BlockArraylist.size() - 1);
                paginate = true;
            } else {
                paginate = false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}