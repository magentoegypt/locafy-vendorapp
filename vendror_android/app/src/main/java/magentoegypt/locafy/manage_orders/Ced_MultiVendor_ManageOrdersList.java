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

package magentoegypt.locafy.manage_orders;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by developer on 28/6/16.
 */
public class Ced_MultiVendor_ManageOrdersList extends Ced_MultiVendor_NavigationActivity {
    static final String KEY_ORDER_ID = "order_id";
    static final String KEY_ID = "id";
    static final String KEY_CREATED_AT = "created_at";
    static final String KEY_BILLING_NAME = "billing_name";
    static final String KEY_GRAND_TOTAL = "order_total";

    static final String KEY_GRAND_TOTAL_EARNED = "grand_total_earned";
    static final String KEY_VENDOR_TOTAL = "vendor_order_total";
    static final String KEY_COMMISSION = "shop_commission_fee";
    static final String KEY_NET_Earned = "net_vendor_earn";
    static final String KEY_PAYMENT_STATE = "payment_state";
    static final String KEY_ORDER_PAYMENT_STATE = "order_payment_state";
    static final String KEY_STORE_NAME = "store_name";
    public static EditText MultiVendor_edt_start_date;
    public static EditText MultiVendor_edt_end_date;
    private static LayoutInflater inflater = null;
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    ListView orderlist;
    String url = "";
    String out = "";
    String hashkey, vendor_id;
    JSONObject jsonObject;
    ArrayList<HashMap<String, String>> Orderinfo;
    JSONArray orderdetail;
    Boolean select_date = false;
    String id, order_id, created_at, billing_name, grand_total, shop_commission_fee, net_vendor_earn, store_name, payment_state, order_payment_status, message;
    Ced_MultiVendor_ManageOrdersListAdapter orderAdapter;
    EditText edt_order_id, edt_bill_name, edt_grandtotal_from, edt_grandtotal_to, edt_commission_from, edt_commission_to, edt_netearned_from, edt_netearned_to;
    Spinner edt_orderpaymentstatus, edt_vendorpaymentstatus;
    Button setfilter, unsetfilter, OrderList_txt;
    HashMap<String, String> postdata;
    JSONObject data, req;
    Boolean flag = true;
    Calendar newCalendar;
    LinearLayout filtersection;
    Dialog listDialog;
    String datafilterjson = "";
    int current = 1;
    boolean load = true;
    String order_status_url = "";
    JSONArray order_payment_statuss, vendor_payment_status;
    List<String> spinier_list, spinier_list2;
    HashMap<String, String> spinier_list_hashmap, spinier_list2_hashmap;
    Ced_MultiVendor_FontSetting fontSetting;
    TextView txt_order_id, txt_purchased_on, txt_bill_name, txt_grand_total,
            txt_commission, txt_netearned, txt_orderpaymentstatus, txt_vendorpaymentstatus;
    ArrayAdapter<String> adp, adp2;
    SwipeRefreshLayout swipe_refresh_layout;
    TextView text_msg, Count_total;
    String order_payment_value = "";
    String payment_status_value = "";
    Date date_from;
    private SimpleDateFormat dateFormatter;
    private int visible = 1;
    private int count = 0;

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

        url = session.getBase_Url() + "vorderapi/vorders/item";
        order_status_url = session.getBase_Url() + "vorderapi/vorders/orderstatus";
        spinier_list = new ArrayList<String>();
        spinier_list2 = new ArrayList<String>();
        connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(getApplicationContext());
        fontSetting = new Ced_MultiVendor_FontSetting();
        Orderinfo = new ArrayList<HashMap<String, String>>();
        data = new JSONObject();
        req = new JSONObject();
        postdata = new HashMap<>();
        spinier_list_hashmap = new HashMap<>();
        spinier_list2_hashmap = new HashMap<>();
        if (connectionDetector.isConnectingToInternet()) {
            ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
            getLayoutInflater().inflate(R.layout.ced_multivendor_activity_order_list, content, true);
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
                    Intent intent = new Intent(Ced_MultiVendor_ManageOrdersList.this, Ced_MultiVendor_ManageOrdersList.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    if(getIntent().getStringExtra("isFrom") != null){
                        String isFrom = getIntent().getStringExtra("isFrom");
                        if(isFrom.equalsIgnoreCase("CancelOrders")){
                            intent.putExtra("isFrom","CancelOrders");
                            intent.putExtra("filter","{\"increment_id\":\"\",\"created_at\":{\"from\":\"\",\"to\":\"\"},\"billing_name\":\"\",\"order_total\":{\"from\":\"\",\"to\":\"\"},\"shop_commission_fee\":{\"from\":\"\",\"to\":\"\"},\"order_payment_state\":\"3\"}");
                        }
                    }
                    startActivity(intent);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }

            });
            fontSetting.setfontforButtons(OrderList_txt, "Roboto-Bold.ttf", getApplicationContext());
            filtersection = findViewById(R.id.MultiVendor_filtersection);
            if(getIntent().getStringExtra("isFrom") != null){
                String isFrom = getIntent().getStringExtra("isFrom");
                if(isFrom.equalsIgnoreCase("CancelOrders")){
                    filtersection.setVisibility(View.GONE);
                    OrderList_txt.setText(R.string.list_of_canceled_orders);
                }
            }
            filtersection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showfilter();
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

        } else {
            Intent nointernet = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(nointernet);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }
    }

    private void getStatus() {

        HashMap status_params = new HashMap();

        status_params.put("vendor_id", session.getVendorid());
        status_params.put("hashkey", session.getHahkey());


        Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                if (functionalityList.getExtensionAddon()) {
                    JSONObject object = new JSONObject(output.toString());
                    order_payment_statuss = object.getJSONObject("data").getJSONArray("order_payment_status");
                    vendor_payment_status = object.getJSONObject("data").getJSONArray("vendor_payment_status");
                    for (int i = 0; i < order_payment_statuss.length(); i++) {
                        JSONObject objects = order_payment_statuss.getJSONObject(i);
                        spinier_list.add(objects.getString("label"));
                        spinier_list_hashmap.put(objects.getString("label"), objects.getString("value"));
                    }
                    for (int i = 0; i < vendor_payment_status.length(); i++) {
                        JSONObject objects = vendor_payment_status.getJSONObject(i);
                        spinier_list2.add(objects.getString("label"));
                        spinier_list2_hashmap.put(objects.getString("label"), objects.getString("value"));
                    }
                    ArrayAdapter<String> orderadapter = new ArrayAdapter<>(Ced_MultiVendor_ManageOrdersList.this, R.layout.simple_spinner_dropdown_item, spinier_list);
                    edt_orderpaymentstatus.setAdapter(orderadapter);
                    ArrayAdapter<String> vendorpaymentstatus = new ArrayAdapter<>(Ced_MultiVendor_ManageOrdersList.this, R.layout.simple_spinner_dropdown_item, spinier_list2);
                    edt_vendorpaymentstatus.setAdapter(vendorpaymentstatus);
                    /*adp = new ArrayAdapter<String>
                            (Ced_MultiVendor_ManageOrdersList.this, android.R.layout.simple_dropdown_item_1line, spinier_list);
                    adp2 = new ArrayAdapter<String>
                            (Ced_MultiVendor_ManageOrdersList.this, android.R.layout.simple_dropdown_item_1line, spinier_list2);

                    edt_orderpaymentstatus.setAdapter(adp);
                    edt_vendorpaymentstatus.setAdapter(adp2);*/

                } else {
                    Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }
            }
        }, Ced_MultiVendor_ManageOrdersList.this, "POST", status_params);
        response.execute(order_status_url);
    }

    private void showfilter() {

        listDialog = new Dialog(this, R.style.PauseDialog);
        listDialog.setTitle(getResources().getString(R.string.alert_name));
        listDialog.setCanceledOnTouchOutside(true);
        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View vi = inflater.inflate(R.layout.ced_multivendor_orderlist_filter, null);


        if (spinier_list.size() > 0) {

            spinier_list.clear();
            spinier_list2.clear();
        }
        getStatus();
        txt_order_id = vi.findViewById(R.id.MultiVendor_txt_order_id);
        txt_purchased_on = vi.findViewById(R.id.MultiVendor_txt_purchased_on);
        txt_bill_name = vi.findViewById(R.id.MultiVendor_txt_bill_name);
        txt_grand_total = vi.findViewById(R.id.MultiVendor_txt_grand_total);
        txt_commission = vi.findViewById(R.id.MultiVendor_txt_commission);
        /*txt_netearned= (TextView) vi.findViewById(R.id.MultiVendor_txt_netearned);*/
        txt_orderpaymentstatus = vi.findViewById(R.id.MultiVendor_txt_orderpaymentstatus);
        txt_vendorpaymentstatus = vi.findViewById(R.id.MultiVendor_txt_vendorpaymentstatus);
        edt_order_id = vi.findViewById(R.id.MultiVendor_edt_order_id);
        MultiVendor_edt_start_date = vi.findViewById(R.id.MultiVendor_edt_purchasedon);
        MultiVendor_edt_end_date = vi.findViewById(R.id.MultiVendor_edt_purchasedon_to);
        edt_bill_name = vi.findViewById(R.id.MultiVendor_edt_bill_name);
        edt_grandtotal_from = vi.findViewById(R.id.MultiVendor_edt_grandtotal_from);
        edt_grandtotal_to = vi.findViewById(R.id.MultiVendor_edt_grandtotal_to);
        edt_commission_from = vi.findViewById(R.id.MultiVendor_edt_commission_from);
        edt_commission_to = vi.findViewById(R.id.MultiVendor_edt_commission_to);
        /*edt_netearned_from= (EditText) vi.findViewById(R.id.MultiVendor_edt_netearned_from);*/
        /*edt_netearned_to= (EditText) vi.findViewById(R.id.MultiVendor_edt_netearned_to);*/
        edt_orderpaymentstatus = vi.findViewById(R.id.MultiVendor_edt_orderpaymentstatus);
        edt_vendorpaymentstatus = vi.findViewById(R.id.MultiVendor_edt_vendorpaymentstatus);
        setfilter = vi.findViewById(R.id.MultiVendor_setfilter);
        unsetfilter = vi.findViewById(R.id.MultiVendor_unsetfilter);

        edt_orderpaymentstatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                order_payment_value = spinier_list_hashmap.get(adapterView.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        edt_vendorpaymentstatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                payment_status_value = spinier_list2_hashmap.get(adapterView.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        fontSetting.setFontforTextviews(txt_order_id, "Roboto-Medium.ttf", getApplicationContext());
        fontSetting.setFontforTextviews(txt_purchased_on, "Roboto-Medium.ttf", getApplicationContext());
        fontSetting.setFontforTextviews(txt_bill_name, "Roboto-Medium.ttf", getApplicationContext());
        fontSetting.setFontforTextviews(txt_grand_total, "Roboto-Medium.ttf", getApplicationContext());
        fontSetting.setFontforTextviews(txt_commission, "Roboto-Medium.ttf", getApplicationContext());
        // fontSetting.setFontforTextviews(txt_netearned,"Roboto-Medium.ttf",getApplicationContext());
        fontSetting.setFontforTextviews(txt_orderpaymentstatus, "Roboto-Medium.ttf", getApplicationContext());
        fontSetting.setFontforTextviews(txt_vendorpaymentstatus, "Roboto-Medium.ttf", getApplicationContext());

        fontSetting.setfontforEditText(edt_order_id, "Roboto-Light.ttf", getApplicationContext());
        fontSetting.setfontforEditText(MultiVendor_edt_start_date, "Roboto-Light.ttf", getApplicationContext());
        fontSetting.setfontforEditText(MultiVendor_edt_end_date, "Roboto-Light.ttf", getApplicationContext());
        fontSetting.setfontforEditText(edt_bill_name, "Roboto-Light.ttf", getApplicationContext());
        fontSetting.setfontforEditText(edt_grandtotal_from, "Roboto-Light.ttf", getApplicationContext());
        fontSetting.setfontforEditText(edt_grandtotal_to, "Roboto-Light.ttf", getApplicationContext());
        fontSetting.setfontforEditText(edt_commission_from, "Roboto-Light.ttf", getApplicationContext());
        fontSetting.setfontforEditText(edt_commission_to, "Roboto-Light.ttf", getApplicationContext());
//        fontSetting.setfontforEditText(edt_netearned_from, "Roboto-Light.ttf", getApplicationContext());
//        fontSetting.setfontforEditText(edt_netearned_to,"Roboto-Light.ttf",getApplicationContext());

        fontSetting.setfontforButtons(setfilter, "Roboto-Bold.ttf", getApplicationContext());
        fontSetting.setfontforButtons(unsetfilter, "Roboto-Bold.ttf", getApplicationContext());

        if (!(datafilterjson.isEmpty())) {
            JSONObject object = null;
            try {
                object = new JSONObject(datafilterjson);
                edt_order_id.setText(object.getString("increment_id"));
                MultiVendor_edt_start_date.setText(object.getJSONObject("created_at").getString("from"));
                MultiVendor_edt_end_date.setText(object.getJSONObject("created_at").getString("to"));
                edt_bill_name.setText(object.getString("billing_name"));
                edt_grandtotal_from.setText(object.getJSONObject("order_total").getString("from"));
                edt_grandtotal_to.setText(object.getJSONObject("order_total").getString("to"));
                edt_commission_from.setText(object.getJSONObject("shop_commission_fee").getString("from"));
                edt_commission_to.setText(object.getJSONObject("shop_commission_fee").getString("to"));
              /*  edt_netearned_from.setText(object.getJSONObject("net_vendor_earn").getString("from"));
                edt_netearned_to.setText(object.getJSONObject("net_vendor_earn").getString("to"));*/
                if (object.has("order_payment_state")) {
                    switch (object.getString("order_payment_state")) {
                        case "Pending":
                            edt_orderpaymentstatus.setSelection(2);
                            break;
                        case "Paid":
                            edt_orderpaymentstatus.setSelection(3);
                            break;
                        case "Canceled":
                            edt_orderpaymentstatus.setSelection(4);
                            break;
                        default:
                            edt_orderpaymentstatus.setSelection(1);

                    }
                }
                if (object.has("payment_state")) {
                    switch (object.getString("payment_state")) {
                        case "Pending":
                            edt_vendorpaymentstatus.setSelection(2);
                            break;
                        case "Paid":
                            edt_vendorpaymentstatus.setSelection(3);
                            break;
                        case "Canceled":
                            edt_vendorpaymentstatus.setSelection(4);
                            break;
                        case "Refund":
                            edt_vendorpaymentstatus.setSelection(5);
                            break;
                        case "Refunded":
                            edt_vendorpaymentstatus.setSelection(6);
                            break;
                        default:
                            edt_vendorpaymentstatus.setSelection(1);

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

/*        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

            private void updateLabel() {

                String myFormat = "MM/dd/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                MultiVendor_edt_start_date.setText(sdf.format(myCalendar.getTime()));
            }
        };
        MultiVendor_edt_end_date.setOnClickListener(new View.OnClickListener() {
                                                  @Override
                                                  public void onClick(View v) {
                                                      newCalendar = Calendar.getInstance();
                                                      dateFormatter = new SimpleDateFormat("MM/dd/yy", Locale.US);
                                                      new DatePickerDialog(Ced_MultiVendor_ManageOrdersList.this, new DatePickerDialog.OnDateSetListener() {

                                                          public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                                              newCalendar.set(Calendar.YEAR, year);
                                                              newCalendar.set(Calendar.MONTH, monthOfYear);
                                                              newCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                                              MultiVendor_edt_end_date.setText(dateFormatter.format(newCalendar.getTime()));

                                                          }

                                                      },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH)).show();
                                                  }
                                              }
        );
        MultiVendor_edt_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Ced_MultiVendor_ManageOrdersList.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });*/


        MultiVendor_edt_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppConstant.setDateFrom(Ced_MultiVendor_ManageOrdersList.this, MultiVendor_edt_start_date);
                select_date = true;
            }
        });


        MultiVendor_edt_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (select_date) {
                    AppConstant.setDateTo(Ced_MultiVendor_ManageOrdersList.this, MultiVendor_edt_end_date, MultiVendor_edt_start_date);
                } else {
                    Toast.makeText(Ced_MultiVendor_ManageOrdersList.this, R.string.please_fill_both_dates, Toast.LENGTH_SHORT).show();
                }
            }
        });

        setfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    edt_order_id = vi.findViewById(R.id.MultiVendor_edt_order_id);
                    MultiVendor_edt_start_date = vi.findViewById(R.id.MultiVendor_edt_purchasedon);
                    MultiVendor_edt_end_date = vi.findViewById(R.id.MultiVendor_edt_purchasedon_to);
                    edt_bill_name = vi.findViewById(R.id.MultiVendor_edt_bill_name);
                    edt_grandtotal_from = vi.findViewById(R.id.MultiVendor_edt_grandtotal_from);
                    edt_grandtotal_to = vi.findViewById(R.id.MultiVendor_edt_grandtotal_to);
                    edt_commission_from = vi.findViewById(R.id.MultiVendor_edt_commission_from);
                    edt_commission_to = vi.findViewById(R.id.MultiVendor_edt_commission_to);
                    /*edt_netearned_from= (EditText) vi.findViewById(R.id.MultiVendor_edt_netearned_from);*/
                    /*edt_netearned_to= (EditText) vi.findViewById(R.id.MultiVendor_edt_netearned_to);*/
                    edt_orderpaymentstatus = vi.findViewById(R.id.MultiVendor_edt_orderpaymentstatus);
                    edt_vendorpaymentstatus = vi.findViewById(R.id.MultiVendor_edt_vendorpaymentstatus);
                    if (edt_order_id.getText().toString().isEmpty() &&
                            MultiVendor_edt_start_date.getText().toString().isEmpty() &&
                            MultiVendor_edt_end_date.getText().toString().isEmpty() &&
                            edt_bill_name.getText().toString().isEmpty() &&
                            edt_grandtotal_from.getText().toString().isEmpty() &&
                            edt_grandtotal_to.getText().toString().isEmpty() &&
                            edt_commission_from.getText().toString().isEmpty() &&
                            edt_commission_to.getText().toString().isEmpty() &&
                            /*edt_netearned_from.getText().toString().isEmpty()&&
                            edt_netearned_to.getText().toString().isEmpty()&&*/
                            edt_orderpaymentstatus.getSelectedItem().toString().isEmpty() &&
                            edt_vendorpaymentstatus.getSelectedItem().toString().isEmpty()) {
                        Toast.makeText(Ced_MultiVendor_ManageOrdersList.this, getResources().getString(R.string.PleaseSelectdesiredFilter), Toast.LENGTH_SHORT).show();

                    } else {
                        JSONObject purchasejsonObject = new JSONObject();
                        purchasejsonObject.put("from", MultiVendor_edt_start_date.getText().toString());
                        purchasejsonObject.put("to", MultiVendor_edt_end_date.getText().toString());
                        req.put("increment_id", edt_order_id.getText().toString());
                        req.put("created_at", purchasejsonObject);
                        req.put("billing_name", edt_bill_name.getText().toString());
                        JSONObject grand_total_jsonObject = new JSONObject();
                        grand_total_jsonObject.put("from", edt_grandtotal_from.getText().toString());
                        grand_total_jsonObject.put("to", edt_grandtotal_to.getText().toString());
                        req.put("order_total", grand_total_jsonObject);
                        JSONObject commission_jsonObject = new JSONObject();
                        commission_jsonObject.put("from", edt_commission_from.getText().toString());
                        commission_jsonObject.put("to", edt_commission_to.getText().toString());
                        req.put("shop_commission_fee", commission_jsonObject);
                        JSONObject netearned_jsonObject = new JSONObject();
                        /*netearned_jsonObject.put("from",edt_netearned_from.getText().toString());
                        netearned_jsonObject.put("to", edt_netearned_to.getText().toString());
                        req.put("net_vendor_earn",netearned_jsonObject);*/
                        if (!edt_orderpaymentstatus.getSelectedItem().toString().equals("Please Select Option"))
                            req.put("order_payment_state", order_payment_value);
                        if (!edt_vendorpaymentstatus.getSelectedItem().toString().equals("Please Select Option"))
                            req.put("payment_state", payment_status_value);
                        /*req.put("data", data.toString());
                        /**/

                        /*request();*/
                        listDialog.dismiss();
                        Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_ManageOrdersList.class);
                        intent.putExtra("filter", req.toString());
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        unsetfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edt_order_id = vi.findViewById(R.id.MultiVendor_edt_order_id);
                MultiVendor_edt_start_date = vi.findViewById(R.id.MultiVendor_edt_purchasedon);
                MultiVendor_edt_end_date = vi.findViewById(R.id.MultiVendor_edt_purchasedon_to);
                edt_bill_name = vi.findViewById(R.id.MultiVendor_edt_bill_name);
                edt_grandtotal_from = vi.findViewById(R.id.MultiVendor_edt_grandtotal_from);
                edt_grandtotal_to = vi.findViewById(R.id.MultiVendor_edt_grandtotal_to);
                edt_commission_from = vi.findViewById(R.id.MultiVendor_edt_commission_from);
                edt_commission_to = vi.findViewById(R.id.MultiVendor_edt_commission_to);
                /*edt_netearned_from= (EditText) vi.findViewById(R.id.MultiVendor_edt_netearned_from);*/
                /*edt_netearned_to= (EditText) vi.findViewById(R.id.MultiVendor_edt_netearned_to);*/

                edt_order_id.setText("");
                MultiVendor_edt_start_date.setText("");
                MultiVendor_edt_end_date.setText("");
                edt_bill_name.setText("");
                edt_grandtotal_from.setText("");
                edt_grandtotal_to.setText("");
                edt_commission_from.setText("");
                edt_commission_to.setText("");
                /*edt_netearned_from.setText("");*/
                /*edt_netearned_to.setText("");*/

                postdata.remove("filter");
                datafilterjson = "";
                listDialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_ManageOrdersList.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
            }
        });
        listDialog.setContentView(vi);
        listDialog.setCancelable(true);
        listDialog.show();

    }

    private void request() {
        Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                if (functionalityList.getExtensionAddon()) {
                    out = output.toString();
                    if (out.equals("NO_ORDER")) {
                        text_msg.setText(R.string.NoOrdersToList);
                        text_msg.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), R.string.NoOrdersToList, Toast.LENGTH_LONG).show();
                        //  Toast.makeText(getApplicationContext(),"You have no more orders to see",Toast.LENGTH_SHORT).show();
                    } else {
                        orderlistdata();
                    }
                } else {
                    Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }
            }
        }, Ced_MultiVendor_ManageOrdersList.this, "POST", postdata);
        response.execute(url + "/page/" + current);

    }

    private void orderlistdata() {
        try {
            jsonObject = new JSONObject(out);
            if (jsonObject.has("vendor_approved")) {
                logout();
            } else {
                if (jsonObject.getJSONObject("data").getBoolean("success")) {
                    orderdetail = jsonObject.getJSONObject("data").getJSONArray("orderdata");
                    count = Integer.parseInt(jsonObject.getJSONObject("data").getString("count"));
                    Count_total.setText(getString(R.string.you_have_txt) + " " + jsonObject.getJSONObject("data").getString("count") + " " + getResources().getString(R.string.Orders));
                    for (int i = 0; i < orderdetail.length(); i++) {
                        JSONObject c = null;
                        c = orderdetail.getJSONObject(i);
                        id = c.getString(KEY_ID);
                        order_id = c.getString(KEY_ORDER_ID);
                        created_at = c.getString(KEY_CREATED_AT);
                        billing_name = c.getString(KEY_BILLING_NAME);
                        grand_total = c.getString("grand_total_earned");
                        shop_commission_fee = c.getString(KEY_COMMISSION);
                        net_vendor_earn = c.getString(KEY_NET_Earned);
                        order_payment_status = c.getString(KEY_ORDER_PAYMENT_STATE);
                        payment_state = c.getString(KEY_PAYMENT_STATE);


                        HashMap<String, String> order = new HashMap<String, String>();
                        order.put(KEY_ID, id);
                        order.put(KEY_ORDER_ID, order_id);
                        order.put(KEY_CREATED_AT, created_at);
                        order.put(KEY_BILLING_NAME, billing_name);
                        order.put(KEY_GRAND_TOTAL, grand_total);
                        order.put(KEY_COMMISSION, shop_commission_fee);
                        order.put(KEY_NET_Earned, net_vendor_earn);
                        order.put(KEY_ORDER_PAYMENT_STATE, order_payment_status);
                        order.put(KEY_PAYMENT_STATE, payment_state);
                        if (c.has("store_name")) {
                            store_name = c.getString(KEY_STORE_NAME);
                            order.put(KEY_STORE_NAME, store_name);
                        }
                        Orderinfo.add(order);

                    }
                    orderAdapter = new Ced_MultiVendor_ManageOrdersListAdapter(Ced_MultiVendor_ManageOrdersList.this, Orderinfo);
                    orderlist.setAdapter(orderAdapter);
                    orderlist.setDivider(new ColorDrawable(getResources().getColor(R.color.transparent)));
                    orderlist.setDividerHeight(0);

                    orderlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            TextView order_id = view.findViewById(R.id.MultiVendor_order_id);
                            String orderid = order_id.getText().toString();
                            Intent orderview = new Intent(Ced_MultiVendor_ManageOrdersList.this, Ced_MultiVendor_ManageOrderview.class);
                            orderview.putExtra("order_id", orderid);
                            startActivity(orderview);
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
                                            if (out.equals("NO_ORDER")) {

                                                load = false;
                                            } else {
                                                load = true;
                                                scrolldata();
                                            }
                                        }
                                    }, Ced_MultiVendor_ManageOrdersList.this, "POST", postdata);
                                    response.execute(url + "/page/" + current);
                                }
                            }
                        }
                    });

                } else {
                    message = jsonObject.getJSONObject("data").getString("message");
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void scrolldata() throws JSONException {

        jsonObject = new JSONObject(out);
        if (jsonObject.has("vendor_approved")) {
            logout();
        } else {
            if (jsonObject.getJSONObject("data").getBoolean("success")) {
                orderdetail = jsonObject.getJSONObject("data").getJSONArray("orderdata");
                int counter = count + Integer.parseInt(jsonObject.getJSONObject("data").getString("count"));
                Count_total.setText(getString(R.string.you_have_txt) + " " + counter + " " + getResources().getString(R.string.Orders));
                count = counter;
                for (int i = 0; i < orderdetail.length(); i++) {
                    JSONObject c = null;
                    c = orderdetail.getJSONObject(i);
                    id = c.getString(KEY_ID);
                    order_id = c.getString(KEY_ORDER_ID);
                    created_at = c.getString(KEY_CREATED_AT);
                    billing_name = c.getString(KEY_BILLING_NAME);
                    grand_total = c.getString(KEY_GRAND_TOTAL_EARNED);
                    shop_commission_fee = c.getString(KEY_COMMISSION);
                    net_vendor_earn = c.getString(KEY_NET_Earned);
                    order_payment_status = c.getString(KEY_ORDER_PAYMENT_STATE);
                    payment_state = c.getString(KEY_PAYMENT_STATE);
                    HashMap<String, String> order = new HashMap<String, String>();
                    order.put(KEY_ID, id);
                    order.put(KEY_ORDER_ID, order_id);
                    order.put(KEY_CREATED_AT, created_at);
                    order.put(KEY_BILLING_NAME, billing_name);
                    order.put(KEY_GRAND_TOTAL, grand_total);
                    order.put(KEY_COMMISSION, shop_commission_fee);
                    order.put(KEY_NET_Earned, net_vendor_earn);
                    order.put(KEY_ORDER_PAYMENT_STATE, order_payment_status);
                    order.put(KEY_PAYMENT_STATE, payment_state);
                    if (c.has("store_name")) {
                        store_name = c.getString(KEY_STORE_NAME);
                        order.put(KEY_STORE_NAME, store_name);
                    }
                    Orderinfo.add(order);

                }
                orderAdapter = new Ced_MultiVendor_ManageOrdersListAdapter(Ced_MultiVendor_ManageOrdersList.this, Orderinfo);
                int cp = orderlist.getFirstVisiblePosition();
                orderlist.setAdapter(orderAdapter);
                orderlist.setDivider(new ColorDrawable(getResources().getColor(R.color.transparent)));
                orderlist.setDividerHeight(0);
                orderlist.setSelectionFromTop(cp + 1, 0);
                orderAdapter.notifyDataSetChanged();
                load = true;
            } else {
                /*Toast.makeText(getApplicationContext(),jsonObject.getJSONObject("data").getString("message"),Toast.LENGTH_LONG).show();*/
            }
        }
    }


    @Override
    protected void onResume() {
        Ced_MultiVendor_ConnectionDetector connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        if (connectionDetector.isConnectingToInternet()) {
            setname(session.getvendorname());
            setprofilepic(session.getvendorpic());
            changetitle("Orders");
            //     invalidateOptionsMenu();

            super.onResume();

        } else {
            Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
            super.onResume();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

