package magentoegypt.locafy.addons.vendor_rma;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import magentoegypt.locafy.base_app.Ced_MultiVendor_FontSetting;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_UnAuthourizedRequestError;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
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
import java.util.List;

/**
 * Created by cedcoss on 6/1/18.
 */

public class Ced_MultiVendor_ManageRMARequest extends Ced_MultiVendor_NavigationActivity {

    public static EditText MultiVendor_edt_start_date;
    public static EditText MultiVendor_edt_end_date;
    private static LayoutInflater inflater = null;
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    ListView orderlist;
    String url = "";
    String hashkey, vendor_id;
    ArrayList<HashMap<String, String>> Orderinfo;
    HashMap<String, String> postdata;
    JSONObject data, req;
    LinearLayout filtersection;
    String datafilterjson = "";
    String order_status_url = "";
    List<String> spinier_list, spinier_list2;
    Ced_MultiVendor_FontSetting fontSetting;
    SwipeRefreshLayout swipe_refresh_layout;
    TextView text_msg, Count_total;
    Button OrderList_txt;
    String out = "";
    int current = 1;
    boolean load = true;
    JSONObject jsonObject;
    Dialog listDialog;
    Boolean select_date = false;
    EditText rma_id_filter, resolution_requested, customer_name_filter, customer_email_filter, edt_order_id;
    Spinner purchase_point,status_filter;
    Button setfilter, unsetfilter;
    Calendar newCalendar;
    LinearLayout nodatafound;
    TextView nodatafoundText;
    private int visible = 1;
    private SimpleDateFormat dateFormatter;
    private JSONArray store_list_json_array;
    private String store_list = "";

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
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

        url = session.getBase_Url() + "vrmapi/index/info";

        spinier_list = new ArrayList<String>();
        spinier_list2 = new ArrayList<String>();
        connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(getApplicationContext());
        fontSetting = new Ced_MultiVendor_FontSetting();
        Orderinfo = new ArrayList<HashMap<String, String>>();
        data = new JSONObject();
        req = new JSONObject();
        postdata = new HashMap<>();


        if (connectionDetector.isConnectingToInternet()) {
            ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
            getLayoutInflater().inflate(R.layout.ced_multivendor_manage_rma_request, content, true);
            nodatafound = findViewById(R.id.nodatafound);
            nodatafoundText = findViewById(R.id.nodatafoundText);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);

            if (session.getvendorname() != null) {
                setname(session.getvendorname());
                setprofilepic(session.getvendorpic());
                changetitle("Orders");
            }
            orderlist = findViewById(R.id.MultiVendor_orderlist);
            text_msg = findViewById(R.id.MultiVendor_text_msg);
            Count_total = findViewById(R.id.MultiVendor_Count_total);
            OrderList_txt = findViewById(R.id.MultiVendor_OrderList_txt);
            swipe_refresh_layout = findViewById(R.id.MultiVendor_swipe_refresh_layout);
            swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    swipe_refresh_layout.setRefreshing(true);
                    Intent intent = new Intent(Ced_MultiVendor_ManageRMARequest.this, Ced_MultiVendor_ManageRMARequest.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                }

            });
            fontSetting.setfontforButtons(OrderList_txt, "Roboto-Bold.ttf", getApplicationContext());

            filtersection = findViewById(R.id.MultiVendor_filtersection);
            filtersection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppConstant.lockButton(v);
                    try {
                        showfilter();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            final HashMap<String, String> user = session.getUserDetails();
            hashkey = user.get(Ced_MultiVendor_VendorSessionManagement.Key_HAsh);
            vendor_id = session.getVendorid();
            postdata.put("vendor_id", vendor_id);
            postdata.put("hashkey", hashkey);
            if (getIntent().getStringExtra("filter") != null) {
                datafilterjson = getIntent().getStringExtra("filter");
                postdata.put("filter", datafilterjson);
            }

            request();
            if (getIntent().getStringExtra("store_list") != null) {
                store_list = getIntent().getStringExtra("store_list");
            }
        } else {
            Intent nointernet = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(nointernet);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }
    }

    private void showfilter() throws JSONException {
        try {

            listDialog = new Dialog(this, R.style.PauseDialog);
            listDialog.setTitle(getResources().getString(R.string.alert_name));
            listDialog.setCanceledOnTouchOutside(true);
            inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View vi = inflater.inflate(R.layout.ced_multivendor_rma_filter, null);

            if (spinier_list.size() > 0) {

                spinier_list.clear();
                spinier_list2.clear();
            }

            MultiVendor_edt_start_date = vi.findViewById(R.id.MultiVendor_edt_purchasedon);
            MultiVendor_edt_end_date = vi.findViewById(R.id.MultiVendor_edt_purchasedon_to);
            edt_order_id = vi.findViewById(R.id.MultiVendor_edt_order_id);

            rma_id_filter = vi.findViewById(R.id.rma_id);
            purchase_point = vi.findViewById(R.id.purchase_point);
            resolution_requested = vi.findViewById(R.id.resolution_requested);
            customer_name_filter = vi.findViewById(R.id.customer_name);
            customer_email_filter = vi.findViewById(R.id.customer_email);
            status_filter = vi.findViewById(R.id.status);

            setfilter = vi.findViewById(R.id.MultiVendor_setfilter);
            unsetfilter = vi.findViewById(R.id.MultiVendor_unsetfilter);


            if (!(datafilterjson.isEmpty())) {
                JSONObject object = null;
                try {
                    object = new JSONObject(datafilterjson);
                    MultiVendor_edt_start_date.setText(object.getJSONObject("updated_at").getString("from"));
                    MultiVendor_edt_end_date.setText(object.getJSONObject("updated_at").getString("to"));
                    edt_order_id.setText(object.getString("order_id"));
                    rma_id_filter.setText(object.getString("rma_id"));
//                purchase_point.setText(object.getString("store_id"));
                    resolution_requested.setText(object.getString("resolution_requested"));
                    customer_name_filter.setText(object.getString("customer_name"));
                    customer_email_filter.setText(object.getString("customer_email"));
                   // status_filter.setText(object.getString("status"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            purchase_point.setSelection(2);
            final String[] store_id = {""};
            if (!store_list.equals("") && store_list.length() > 0) {
                JSONArray store_list_array = new JSONArray(store_list);
                List<String> store_array_list = new ArrayList<>();
                ArrayAdapter<String> store_list_adapter;
                if (store_list_array.length() > 0) {
                    for (int i = 0; i < store_list_array.length(); i++) {
                        store_array_list.add(store_list_array.getJSONObject(i).getString("label"));
                        if (store_list_array.getJSONObject(i).getJSONArray("value").length() > 0) {
                            JSONObject obj = (JSONObject) store_list_array.getJSONObject(i).getJSONArray("value").get(0);
                            store_array_list.add(obj.getString("label"));
                        }
                    }
                    store_list_adapter = new ArrayAdapter<String>(Ced_MultiVendor_ManageRMARequest.this, R.layout.simple_list_item_1, store_array_list);
                    purchase_point.setAdapter(store_list_adapter);
                }

                purchase_point.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        try {
                            store_id[0] = "";
                            if (store_array_list.contains(adapterView.getSelectedItem().toString())) {
                                for (int j = 0; j < store_list_array.length(); j++) {
                                    if (adapterView.getSelectedItem().toString().equalsIgnoreCase(store_list_array.getJSONObject(j).getString("label"))) {
                                        if (store_list_array.getJSONObject(i).getJSONArray("value").length() > 0) {
                                            JSONObject obj = (JSONObject) store_list_array.getJSONObject(j).getJSONArray("value").get(0);
                                            if (adapterView.getSelectedItem().toString().equalsIgnoreCase(obj.getString("label"))) {
                                                store_id[0] = obj.getString("value");
                                            }
                                        }
                                    } else {
                                        if (store_list_array.getJSONObject(j).getJSONArray("value").length() > 0) {
                                            JSONObject obj = (JSONObject) store_list_array.getJSONObject(j).getJSONArray("value").get(0);
                                            if (adapterView.getSelectedItem().toString().equalsIgnoreCase(obj.getString("label"))) {
                                                store_id[0] = obj.getString("value");
                                            }
                                        }
                                    }
                                }
                                Log.i("STOREID", "onItemSelected: " + store_id[0]);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

            }

            MultiVendor_edt_start_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppConstant.lockButton(view);
                    select_date = true;
                    AppConstant.setDateFrom(Ced_MultiVendor_ManageRMARequest.this, MultiVendor_edt_start_date);
                }
            });


            MultiVendor_edt_end_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppConstant.lockButton(view);
                    if (select_date) {
                        AppConstant.setDateTo(Ced_MultiVendor_ManageRMARequest.this, MultiVendor_edt_end_date, MultiVendor_edt_start_date);
                    } else {
                        Toast.makeText(Ced_MultiVendor_ManageRMARequest.this, R.string.please_fill_both_dates, Toast.LENGTH_SHORT).show();
                    }
                }
            });
            setfilter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        AppConstant.lockButton(v);
                        MultiVendor_edt_start_date = vi.findViewById(R.id.MultiVendor_edt_purchasedon);
                        MultiVendor_edt_end_date = vi.findViewById(R.id.MultiVendor_edt_purchasedon_to);
                        edt_order_id = vi.findViewById(R.id.MultiVendor_edt_order_id);
//                        purchase_point = vi.findViewById(R.id.purchase_point);
                        rma_id_filter = vi.findViewById(R.id.rma_id);
                        resolution_requested = vi.findViewById(R.id.resolution_requested);
                        customer_name_filter = vi.findViewById(R.id.customer_name);
                        customer_email_filter = vi.findViewById(R.id.customer_email);
                        status_filter = vi.findViewById(R.id.status);
                        setfilter = vi.findViewById(R.id.MultiVendor_setfilter);
                        unsetfilter = vi.findViewById(R.id.MultiVendor_unsetfilter);

                        if (edt_order_id.getText().toString().isEmpty() &&
                                MultiVendor_edt_start_date.getText().toString().isEmpty() &&
                                MultiVendor_edt_end_date.getText().toString().isEmpty() &&
                                edt_order_id.getText().toString().isEmpty() &&
                                rma_id_filter.getText().toString().isEmpty() &&
                                /* purchase_point.getText().toString().isEmpty() &&*/
                                resolution_requested.getText().toString().isEmpty() &&
                                customer_name_filter.getText().toString().isEmpty() &&
                                customer_email_filter.getText().toString().isEmpty() &&
                                status_filter.getSelectedItemPosition() == 0) {
                            Toast.makeText(Ced_MultiVendor_ManageRMARequest.this, getResources().getString(R.string.PleaseSelectdesiredFilter), Toast.LENGTH_SHORT).show();

                        } else {

                            if (!MultiVendor_edt_start_date.getText().toString().isEmpty() && MultiVendor_edt_end_date.getText().toString().isEmpty() || MultiVendor_edt_start_date.getText().toString().isEmpty() && !MultiVendor_edt_end_date.getText().toString().isEmpty()) {
                                Toast.makeText(Ced_MultiVendor_ManageRMARequest.this, getResources().getString(R.string.pleaseselectbothdates), Toast.LENGTH_SHORT).show();
                            } else {
                                JSONObject updatedAtObject = new JSONObject();
                                updatedAtObject.put("from", MultiVendor_edt_start_date.getText().toString().trim());
                                updatedAtObject.put("to", MultiVendor_edt_end_date.getText().toString().trim());

                                req.put("order_id", edt_order_id.getText().toString().trim());
                                req.put("rma_id", rma_id_filter.getText().toString().trim());
                                req.put("store_id", store_id[0].trim());
                                req.put("resolution_requested", resolution_requested.getText().toString().trim());
                                req.put("customer_name", customer_name_filter.getText().toString().trim());
                                req.put("customer_email", customer_email_filter.getText().toString().trim());
                                String[] creditmemo_status = {"Select","Pending","Approved","Complete","Cancelled"};
                                if (creditmemo_status[status_filter.getSelectedItemPosition()].equals("Select")) {
                                    req.put("status", "");
                                } else {
                                    req.put("status", creditmemo_status[status_filter.getSelectedItemPosition()]);
                                }
                                req.put("updated_at", updatedAtObject);

                                listDialog.dismiss();
                                Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_ManageRMARequest.class);
                                intent.putExtra("filter", req.toString());
                                intent.putExtra("store_list", store_list);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            unsetfilter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppConstant.lockButton(v);
                    edt_order_id = vi.findViewById(R.id.MultiVendor_edt_order_id);
                    MultiVendor_edt_start_date = vi.findViewById(R.id.MultiVendor_edt_purchasedon);
                    MultiVendor_edt_end_date = vi.findViewById(R.id.MultiVendor_edt_purchasedon_to);
                    rma_id_filter = vi.findViewById(R.id.rma_id);
                    purchase_point = vi.findViewById(R.id.purchase_point);
                    resolution_requested = vi.findViewById(R.id.resolution_requested);
                    customer_name_filter = vi.findViewById(R.id.customer_name);
                    customer_email_filter = vi.findViewById(R.id.customer_email);
                    status_filter = vi.findViewById(R.id.status);

                    edt_order_id.setText("");
                    MultiVendor_edt_start_date.setText("");
                    MultiVendor_edt_end_date.setText("");
                    rma_id_filter.setText("");
                    purchase_point.setSelection(2);
                    resolution_requested.setText("");
                    customer_name_filter.setText("");
                    customer_email_filter.setText("");
                    status_filter.setSelection(0);
                    postdata.remove("filter");
                    datafilterjson = "";
                    listDialog.dismiss();
                    Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_ManageRMARequest.class);
                    intent.putExtra("store_list", store_list);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }
            });
            listDialog.setContentView(vi);
            listDialog.setCancelable(true);
            listDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void request() {
        Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {

                if (functionalityList.getExtensionAddon()) {
                    out = output.toString();
                    Log.i("REpo", "RMA_response" + out);
                    listRMA();
                } else {
                    Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }
            }
        }, Ced_MultiVendor_ManageRMARequest.this, "POST", postdata);
        response.execute(url + "/page/" + current);
    }

    private void listRMA() {

        try {
            jsonObject = new JSONObject(out);
            if (jsonObject.getJSONObject("data").getBoolean("success")) {
                Count_total.setText(getString(R.string.you_have_txt) + " " + jsonObject.getJSONObject("data").getString("count") + " " + getString(R.string.rmatxt));

                JSONArray rma_list = jsonObject.getJSONObject("data").getJSONArray("rma_list");

                if (jsonObject.getJSONObject("data").has("store_list")) {
                    store_list = jsonObject.getJSONObject("data").getJSONArray("store_list").toString();
                }

                Ced_MultiVendor_RMA_RequestsAdapter RMA_Request_Adapter = new Ced_MultiVendor_RMA_RequestsAdapter(Ced_MultiVendor_ManageRMARequest.this, rma_list);

                orderlist.setAdapter(RMA_Request_Adapter);
                orderlist.setDivider(new ColorDrawable(getResources().getColor(R.color.transparent)));
                orderlist.setDividerHeight(0);

                orderlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        TextView rmarequestid = view.findViewById(R.id.rmarequestid);
                        TextView rma_id = view.findViewById(R.id.rma_id);
                        Intent requestview = new Intent(Ced_MultiVendor_ManageRMARequest.this, Ced_MultiVendor_RMA_RequestView.class);
                        requestview.putExtra("rmarequestid", rmarequestid.getText().toString());
                        requestview.putExtra("rma_id", rma_id.getText().toString());
                        startActivity(requestview);
                        overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                    }
                });

                orderlist.setOnScrollListener(new AbsListView.OnScrollListener() {
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
                                        if (out.contains("No product records found")) {
                                            Log.i("REpo", "pagination_response : " + out);
                                            load = false;
                                        } else {
                                            scrolldata();
                                        }

                                    }
                                }, Ced_MultiVendor_ManageRMARequest.this, "POST", postdata);
                                response.execute(url + "/page/" + current);
                            }
                        }
                    }
                });
            } else {
                String message = jsonObject.getJSONObject("data").getString("message");
                nodatafound.setVisibility(View.VISIBLE);
                nodatafoundText.setText(message);
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void scrolldata() throws JSONException {

        jsonObject = new JSONObject(out);
        if (jsonObject.getJSONObject("data").getBoolean("success")) {
            Count_total.setText(getString(R.string.you_have_txt) + " " + jsonObject.getJSONObject("data").getString("count") + " " + getString(R.string.request_txt));

            JSONArray rma_list = jsonObject.getJSONObject("data").getJSONArray("rma_list");

            Ced_MultiVendor_RMA_RequestsAdapter RMA_Request_Adapter = new Ced_MultiVendor_RMA_RequestsAdapter(Ced_MultiVendor_ManageRMARequest.this, rma_list);

            int cp = orderlist.getFirstVisiblePosition();
            orderlist.setAdapter(RMA_Request_Adapter);
            orderlist.setDivider(new ColorDrawable(getResources().getColor(R.color.transparent)));
            orderlist.setDividerHeight(0);
            orderlist.setSelectionFromTop(cp + 1, 0);
            RMA_Request_Adapter.notifyDataSetChanged();
            load = true;
        } else {
            String message = jsonObject.getJSONObject("data").getString("message");
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }

    }

}