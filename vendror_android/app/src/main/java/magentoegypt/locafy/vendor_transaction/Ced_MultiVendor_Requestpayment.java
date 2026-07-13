package magentoegypt.locafy.vendor_transaction;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import magentoegypt.locafy.base_app.Ced_MultiVendor_FontSetting;
import magentoegypt.locafy.vendor_dashboard.Ced_MultiVendor_VendorDashboard;
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
import java.util.Locale;

public class Ced_MultiVendor_Requestpayment extends Ced_MultiVendor_NavigationActivity {

    static final String KEY_CREATED_AT = "created_at";
    static final String KEY_ORDER_ID = "order_id";
    static final String KEY_PENDING_AMOUNT = "pending_amount";
    static final String KEY_ACTION = "action";
    static final String KEY_PAYMENT_ID = "payment_id";
    private static LayoutInflater inflater = null;
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorSessionManagement vendorSessionManagement;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    String url, mass_rqst_url = "";
    TextView TotalPendingAmount, TotalRequestedAmount;
    String out, hashkey, vendor_id, pendingAmount, payment_msg, requestedAmount, created_at, payment_id, order_id, pending_amount, action;
    HashMap<String, String> postdata;
    JSONObject jsonObject;
    JSONArray pending_payment;
    ArrayList<HashMap<String, String>> payment_array;
    ListView pending_list;
    Ced_MultiVendor_RequestPaymentAdapter requestPaymentAdapter;
    int current = 1;
    boolean load = true;
    LinearLayout filtersection;
    Calendar newCalendar;
    Dialog listDialog;
    EditText from_date, to_date, from_pending_amount, to_pending_amount, Order_id;
    Button filter, reset_filter, mass_payment_rqst, Pending_amount_head, Payment_statistics_head, TotalPendingAmount_head, TotalRequestedAmount_head;
    String datafilterjson = "";
    JSONObject req;
    Ced_MultiVendor_FontSetting fontSetting;
    TextView oder_date, Pending_amount, increment_id;
    SwipeRefreshLayout swipe_refresh_layout;
    LinearLayout linear_attach;
    TextView text_msg;
    private int visible = 1;
    private SimpleDateFormat dateFormatter;

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
        connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        vendorSessionManagement = new Ced_MultiVendor_VendorSessionManagement(getApplicationContext());
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(getApplicationContext());
        postdata = new HashMap<>();
        payment_array = new ArrayList<>();
        req = new JSONObject();
        url = session.getBase_Url() + "vendorapi/vtransaction/paymentrequest";
        mass_rqst_url = session.getBase_Url() + "vendorapi/vtransaction/massRequestPayment";
        fontSetting = new Ced_MultiVendor_FontSetting();

        if (connectionDetector.isConnectingToInternet()) {
            ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
            getLayoutInflater().inflate(R.layout.ced_multivendor_activity_request_payment, content, true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);
            if (vendorSessionManagement.getvendorname() != null) {
                setname(vendorSessionManagement.getvendorname());
                setprofilepic(vendorSessionManagement.getvendorpic());
                changetitle("Request Payment");
            }
            vendor_id = vendorSessionManagement.getVendorid();
            postdata.put("vendor_id", vendor_id);
            final HashMap<String, String> user = vendorSessionManagement.getUserDetails();
            hashkey = user.get(Ced_MultiVendor_VendorSessionManagement.Key_HAsh);
            postdata.put("hashkey", hashkey);
            swipe_refresh_layout = findViewById(R.id.MultiVendor_swipe_refresh_layout);
            swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    swipe_refresh_layout.setRefreshing(true);
                    Intent intent = new Intent(Ced_MultiVendor_Requestpayment.this, Ced_MultiVendor_Requestpayment.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                }
            });
            pending_list = findViewById(R.id.MultiVendor_pending_list);
            text_msg = findViewById(R.id.MultiVendor_text_msg);
            linear_attach = findViewById(R.id.MultiVendor_linear_attach);
            Pending_amount_head = findViewById(R.id.MultiVendor_Pending_amount_head);
            filtersection = findViewById(R.id.MultiVendor_filtersection);
            filtersection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppConstant.lockButton(v);
                    showfilter();
                }
            });
            if (getIntent().getStringExtra("filter") != null) {
                datafilterjson = getIntent().getStringExtra("filter");
                postdata.put("filter", datafilterjson);

            }
            fontSetting.setfontforButtons(Pending_amount_head, "Roboto-Bold.ttf", getApplicationContext());

            request();
            TotalPendingAmount = findViewById(R.id.MultiVendor_TotalPendingAmount);
            TotalRequestedAmount = findViewById(R.id.MultiVendor_TotalRequestedAmount);

        } else {
            Intent nointernet = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(nointernet);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }

    }

    private void showfilter() {
        listDialog = new Dialog(this, R.style.PauseDialog);
        listDialog.setTitle(getResources().getString(R.string.alert_name));
        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View v = inflater.inflate(R.layout.ced_multivendor_requestpayment_filter, null);

        oder_date = v.findViewById(R.id.MultiVendor_oder_date);
        Pending_amount = v.findViewById(R.id.MultiVendor_Pending_amount);
        increment_id = v.findViewById(R.id.MultiVendor_increment_id);

        from_date = v.findViewById(R.id.MultiVendor_from_date);
        to_date = v.findViewById(R.id.MultiVendor_to_date);
        from_pending_amount = v.findViewById(R.id.MultiVendor_from_pending_amount);
        to_pending_amount = v.findViewById(R.id.MultiVendor_to_pending_amount);
        Order_id = v.findViewById(R.id.MultiVendor_Order_id);

        filter = v.findViewById(R.id.MultiVendor_filter);
        reset_filter = v.findViewById(R.id.MultiVendor_reset_filter);

        fontSetting.setFontforTextviews(oder_date, "Roboto-Medium.ttf", getApplicationContext());
        fontSetting.setFontforTextviews(Pending_amount, "Roboto-Medium.ttf", getApplicationContext());
        fontSetting.setFontforTextviews(increment_id, "Roboto-Medium.ttf", getApplicationContext());

        fontSetting.setfontforButtons(filter, "Roboto-Bold.ttf", getApplicationContext());
        fontSetting.setfontforButtons(reset_filter, "Roboto-Bold.ttf", getApplicationContext());

        fontSetting.setfontforEditText(from_date, "Roboto-Light.ttf", getApplicationContext());
        fontSetting.setfontforEditText(to_date, "Roboto-Light.ttf", getApplicationContext());
        fontSetting.setfontforEditText(from_pending_amount, "Roboto-Light.ttf", getApplicationContext());
        fontSetting.setfontforEditText(to_pending_amount, "Roboto-Light.ttf", getApplicationContext());
        fontSetting.setfontforEditText(Order_id, "Roboto-Light.ttf", getApplicationContext());

        if (!(datafilterjson.isEmpty())) {

            try {
                JSONObject object = null;
                object = new JSONObject(datafilterjson);
                if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                    Log.i("object", "" + object);
                }
                Order_id.setText(object.getString("order_id"));
                from_date.setText(object.getJSONObject("created_at").getString("from"));
                to_date.setText(object.getJSONObject("created_at").getString("to"));
                from_pending_amount.setText(object.getJSONObject("amount").getString("from"));
                to_pending_amount.setText(object.getJSONObject("amount").getString("to"));

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstant.lockButton(v);
                newCalendar = Calendar.getInstance();
                dateFormatter = new SimpleDateFormat("MM/dd/yy", Locale.US);
                new DatePickerDialog(Ced_MultiVendor_Requestpayment.this, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        newCalendar.set(Calendar.YEAR, year);
                        newCalendar.set(Calendar.MONTH, monthOfYear);
                        newCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        from_date.setText(dateFormatter.format(newCalendar.getTime()));

                    }

                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstant.lockButton(v);
                newCalendar = Calendar.getInstance();
                dateFormatter = new SimpleDateFormat("MM/dd/yy", Locale.US);
                new DatePickerDialog(Ced_MultiVendor_Requestpayment.this, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        newCalendar.set(Calendar.YEAR, year);
                        newCalendar.set(Calendar.MONTH, monthOfYear);
                        newCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        to_date.setText(dateFormatter.format(newCalendar.getTime()));
                    }

                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstant.lockButton(v);
                if (from_date.getText().toString().isEmpty()
                        && to_date.getText().toString().isEmpty()
                        && from_pending_amount.getText().toString().isEmpty()
                        && to_pending_amount.getText().toString().isEmpty()
                        && Order_id.getText().toString().isEmpty()) {
                    Toast.makeText(Ced_MultiVendor_Requestpayment.this, "Please select the desired filter", Toast.LENGTH_SHORT).show();

                } else {

                    try {
                        JSONObject datejsonObject = new JSONObject();
                        datejsonObject.put("from", from_date.getText().toString());
                        datejsonObject.put("to", to_date.getText().toString());
                        JSONObject pendingamountjsonObject = new JSONObject();
                        pendingamountjsonObject.put("from", from_pending_amount.getText().toString());
                        pendingamountjsonObject.put("to", to_pending_amount.getText().toString());
                        req.put("created_at", datejsonObject);
                        req.put("amount", pendingamountjsonObject);
                        req.put("order_id", Order_id.getText().toString());
                        if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                            Log.i("data", req.toString());
                        }
                        listDialog.dismiss();
                        Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_Requestpayment.class);
                        intent.putExtra("filter", req.toString());
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        reset_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstant.lockButton(v);
                from_date.setText("");
                to_date.setText("");
                from_pending_amount.setText("");
                to_pending_amount.setText("");
                Order_id.setText("");
                postdata.remove("filter");
                datafilterjson = "";
                listDialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_Requestpayment.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        listDialog.setContentView(v);
        listDialog.setCancelable(true);
        listDialog.setCanceledOnTouchOutside(true);
        listDialog.show();

    }

    private void request() {
        Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                if (functionalityList.getExtensionAddon()) {
                    out = output.toString();
                    requestpaymentdata();
                } else {
                    Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }
            }
        }, Ced_MultiVendor_Requestpayment.this, "POST", postdata);
        response.execute(url + "/page/" + current);


    }

    private void requestpaymentdata() {
        try {
            jsonObject = new JSONObject(out);
            if (jsonObject.has("vendor_approved")) {
                logout();
            } else {
                if (jsonObject.getJSONObject("data").getBoolean("success")) {
                    pendingAmount = jsonObject.getJSONObject("data").getString("pendingAmount");
                    requestedAmount = jsonObject.getJSONObject("data").getString("requestedAmount");
                    payment_msg = jsonObject.getJSONObject("data").getString("payment_msg");
                    if (!payment_msg.equals("No Transactions Available")) {
                        pending_payment = jsonObject.getJSONObject("data").getJSONArray("pending_payment");
                        for (int i = 0; i < pending_payment.length(); i++) {
                            JSONObject c = null;
                            c = pending_payment.getJSONObject(i);
                            created_at = c.getString(KEY_CREATED_AT);
                            order_id = c.getString(KEY_ORDER_ID);
                            pending_amount = c.getString(KEY_PENDING_AMOUNT);
                            action = c.getString(KEY_ACTION);
                            payment_id = c.getString(KEY_PAYMENT_ID);
                            HashMap<String, String> requst_payment = new HashMap<String, String>();
                            requst_payment.put(KEY_CREATED_AT, created_at);
                            requst_payment.put(KEY_ORDER_ID, order_id);
                            requst_payment.put(KEY_PENDING_AMOUNT, pending_amount);
                            requst_payment.put(KEY_ACTION, action);
                            requst_payment.put(KEY_PAYMENT_ID, payment_id);
                            payment_array.add(requst_payment);
                        }

                        requestPaymentAdapter = new Ced_MultiVendor_RequestPaymentAdapter(Ced_MultiVendor_Requestpayment.this, payment_array);
                        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View vi = inflater.inflate(R.layout.ced_multivendor_activity_request_payment_head, null);
                        pending_list.addHeaderView(vi);
                        mass_payment_rqst = vi.findViewById(R.id.MultiVendor_mass_payment_rqst);
                        Payment_statistics_head = vi.findViewById(R.id.MultiVendor_Payment_statistics_head);
                        TotalPendingAmount_head = vi.findViewById(R.id.MultiVendor_TotalPendingAmount_head);
                        TotalRequestedAmount_head = vi.findViewById(R.id.MultiVendor_TotalRequestedAmount_head);

                        mass_payment_rqst.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AppConstant.lockButton(v);
                                mass_request();
                            }
                        });
                        TotalPendingAmount = vi.findViewById(R.id.MultiVendor_TotalPendingAmount);
                        TotalRequestedAmount = vi.findViewById(R.id.MultiVendor_TotalRequestedAmount);

                        fontSetting.setfontforButtons(mass_payment_rqst, "Roboto-Bold.ttf", getApplicationContext());
                        fontSetting.setfontforButtons(Payment_statistics_head, "Roboto-Bold.ttf", getApplicationContext());
                        fontSetting.setfontforButtons(TotalPendingAmount_head, "Roboto-Medium.ttf", getApplicationContext());
                        fontSetting.setfontforButtons(TotalRequestedAmount_head, "Roboto-Medium.ttf", getApplicationContext());

                        fontSetting.setFontforTextviews(TotalPendingAmount, "Roboto-Black.ttf", getApplicationContext());
                        fontSetting.setFontforTextviews(TotalRequestedAmount, "Roboto-Black.ttf", getApplicationContext());

                        TotalPendingAmount.setText(pendingAmount);
                        TotalRequestedAmount.setText(requestedAmount);
                        pending_list.setDivider(new ColorDrawable(getResources().getColor(R.color.transparent)));
                        pending_list.setDividerHeight(0);
                        pending_list.setAdapter(requestPaymentAdapter);
                        pending_list.setOnScrollListener(new AbsListView.OnScrollListener() {
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
                                                JSONObject object = new JSONObject(out);
                                                if (object.getJSONObject("data").getString("pending_payment").equals("No Transactions Available")) {

                                                    load = false;
                                                } else {

                                                    scrolldata();
                                                }
                                            }
                                        }, Ced_MultiVendor_Requestpayment.this, "POST", postdata);
                                        response.execute(url + "/page/" + current);
                                    }
                                }
                            }
                        });
                    } else {
                        text_msg.setText(R.string.NoTransactionsAvailable);
                        text_msg.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), R.string.NoTransactionsAvailable, Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), jsonObject.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
                    Intent HomeIntent = new Intent(this, Ced_MultiVendor_VendorDashboard.class);
                    HomeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    HomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(HomeIntent);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);

                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void mass_request() {
        Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                if (functionalityList.getExtensionAddon()) {
                    out = output.toString();
                    JSONObject object = new JSONObject(out);
                    if (object.getJSONObject("data").getBoolean("success")) {
                        Toast.makeText(getApplicationContext(), object.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
                        Intent rqstPay = new Intent(getApplicationContext(), Ced_MultiVendor_Requestpayment.class);
                        rqstPay.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        rqstPay.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(rqstPay);
                    } else {
                        Toast.makeText(getApplicationContext(), object.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();

                    }

                } else {
                    Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }
            }
        }, Ced_MultiVendor_Requestpayment.this, "POST", postdata);
        response.execute(mass_rqst_url);

    }

    private void scrolldata() throws JSONException {
        jsonObject = new JSONObject(out);
        if (jsonObject.has("vendor_approved")) {
            logout();
        } else {
            if (jsonObject.getJSONObject("data").getBoolean("success")) {
                pendingAmount = jsonObject.getJSONObject("data").getString("pendingAmount");
                requestedAmount = jsonObject.getJSONObject("data").getString("requestedAmount");

                pending_payment = jsonObject.getJSONObject("data").getJSONArray("pending_payment");
                for (int i = 0; i < pending_payment.length(); i++) {
                    JSONObject c = null;
                    c = pending_payment.getJSONObject(i);
                    created_at = c.getString(KEY_CREATED_AT);
                    order_id = c.getString(KEY_ORDER_ID);
                    pending_amount = c.getString(KEY_PENDING_AMOUNT);
                    action = c.getString(KEY_ACTION);
                    payment_id = c.getString(KEY_PAYMENT_ID);
                    HashMap<String, String> requst_payment = new HashMap<String, String>();
                    requst_payment.put(KEY_CREATED_AT, created_at);
                    requst_payment.put(KEY_ORDER_ID, order_id);
                    requst_payment.put(KEY_PENDING_AMOUNT, pending_amount);
                    requst_payment.put(KEY_ACTION, action);
                    requst_payment.put(KEY_PAYMENT_ID, payment_id);
                    payment_array.add(requst_payment);
                }
                requestPaymentAdapter = new Ced_MultiVendor_RequestPaymentAdapter(Ced_MultiVendor_Requestpayment.this, payment_array);
                int cp = pending_list.getFirstVisiblePosition();
                pending_list.setDivider(new ColorDrawable(getResources().getColor(R.color.transparent)));
                pending_list.setDividerHeight(0);
                pending_list.setAdapter(requestPaymentAdapter);
                pending_list.setSelectionFromTop(cp + 1, 0);
                requestPaymentAdapter.notifyDataSetChanged();
                load = true;
            } else {
                Toast.makeText(getApplicationContext(), jsonObject.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onResume() {
        Ced_MultiVendor_ConnectionDetector connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());

        if (connectionDetector.isConnectingToInternet()) {

            setname(vendorSessionManagement.getvendorname());
            setprofilepic(vendorSessionManagement.getvendorpic());
            changetitle("Request Payement");
            //   invalidateOptionsMenu();
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
