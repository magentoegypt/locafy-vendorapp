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

package magentoegypt.locafy.vendor_transaction;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import magentoegypt.locafy.addons.inventory.OutOfStockActivity;
import com.google.gson.GsonBuilder;
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
import magentoegypt.locafy.vendor_transaction.adapter.Ced_MultiVendor_TransactionListingAdapter;
import magentoegypt.locafy.vendor_transaction.model.listing.Data;
import magentoegypt.locafy.vendor_transaction.model.listing.TransactionDataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by developer on 5/5/16.
 */
public class Ced_MultiVendor_ListTransaction extends Ced_MultiVendor_NavigationActivity {
    static final String KEY_CREATED_AT = "created_at";
    static final String KEY_PAYMENT_ID = "payment_id";
    static final String KEY_PAYMENT_MODE = "payment_mode";
    static final String KEY_AMOUNT_DESC = "amount_desc";
    static final String KEY_TRANSACTION_ID = "transaction_id";
    static final String KEY_AMOUNT = "amount";
    static final String KEY_ADJUSTMENT_AMOUNT = "adjustment_amount";
    static final String KEY_NET_AMOUNT = "net_amount";
    private static LayoutInflater inflater = null;
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    String url = "";
    Button Transaction_statistics_head, TotalEarnedAmount_head, TotalPendingAmount_head,
            PendingTransfers_head;
    TextView TotalEarnedAmount, TotalPendingAmount, PendingTransfers;
    LinearLayout filtersection;
    JSONObject jsonObject;
    ListView transaction_list;
    RecyclerView MultiVendor_transaction_rv;
    String created_at, payment_id, payment_mode, transaction_id, amount, hashkey,
            adjustment_amount, net_amount, vendor_id, out, total_earned,
            pending_amount, pending_transfer, payment_msg, amount_desc;
    HashMap<String, String> postdata;
    JSONArray transactiondata;
    ArrayList<HashMap<String, String>> transaction_array;
    Ced_MultiVendor_TransactionListingAdapter transactionadapter;
    int current = 1;
    boolean load = true;
    Dialog listDialog;
    JSONObject req;
    Button filter, reset_filter, Transactionslist_head;
    EditText fromdate, todate, payment_modes, TransactionId, Amount, to_Amount,
            AdjustmentAmount, to_AdjustmentAmount, from_NetAmount, to_NetAmount;
    String datafilterjson = "";
    TextView createdAt_text, payment_mode_txt, TransactionId_txt, Amount_txt,
            AdjustmentAmount_txt, NetAmount_text, Count_total;
    Ced_MultiVendor_FontSetting fontSetting;
    //  SwipeRefreshLayout swipe_refresh_layout;
    TextView text_msg;
    int count_number = 0;
    private int visible = 1;
    private boolean select_date = false;
    LinearLayout listing_parent;

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
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(getApplicationContext());
        req = new JSONObject();
        postdata = new HashMap<>();
        transaction_array = new ArrayList<>();
        fontSetting = new Ced_MultiVendor_FontSetting();
        url = session.getBase_Url() + "vendorapi/vtransaction/payment";
        if (connectionDetector.isConnectingToInternet()) {
            ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
            getLayoutInflater().inflate(R.layout.ced_multivendor_activity_view_transaction, content, true);
            if (session.getvendorname() != null) {
                setname(session.getvendorname());
                setprofilepic(session.getvendorpic());
                changetitle("View Transactions");
            }
            final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
            pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    pullToRefresh.setRefreshing(true);
                    Intent intent = new Intent(Ced_MultiVendor_ListTransaction.this, Ced_MultiVendor_ListTransaction.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }
            });
            transaction_list = findViewById(R.id.MultiVendor_transaction_list);
            LinearLayoutManager lm = new LinearLayoutManager(this);
            MultiVendor_transaction_rv = findViewById(R.id.MultiVendor_transaction_rv);
            MultiVendor_transaction_rv.setLayoutManager(lm);
            text_msg = findViewById(R.id.MultiVendor_text_msg);
            Transactionslist_head = findViewById(R.id.MultiVendor_Transactionslist_head);
            filtersection = findViewById(R.id.MultiVendor_filtersection);
            Count_total = findViewById(R.id.MultiVendor_Count_total);
            listing_parent = findViewById(R.id.listing_parent);
/*            swipe_refresh_layout = (SwipeRefreshLayout) findViewById(R.id.MultiVendor_swipe_refresh_layout);
            swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    swipe_refresh_layout.setRefreshing(true);
                    Intent intent = new Intent(Ced_MultiVendor_ListTransaction.this, Ced_MultiVendor_ListTransaction.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                }
            });*/
            filtersection.setOnClickListener(v -> showfilter());
            final HashMap<String, String> user = session.getUserDetails();
            hashkey = user.get(Ced_MultiVendor_VendorSessionManagement.Key_HAsh);
            postdata.put("hashkey", hashkey);
            vendor_id = session.getVendorid();
            postdata.put("vendor_id", vendor_id);
            if (getIntent().getStringExtra("filter") != null) {
                datafilterjson = getIntent().getStringExtra("filter");
                postdata.put("filter", datafilterjson);
            }
            fontSetting.setfontforButtons(Transactionslist_head, "Roboto-Bold.ttf", getApplicationContext());
            request();
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
        final View v = inflater.inflate(R.layout.ced_multivendor_viewtransaction_filter, null);

        createdAt_text = v.findViewById(R.id.MultiVendor_createdAt_text);
        payment_mode_txt = v.findViewById(R.id.MultiVendor_payment_mode_txt);
        TransactionId_txt = v.findViewById(R.id.MultiVendor_TransactionId_txt);
        Amount_txt = v.findViewById(R.id.MultiVendor_Amount_txt);
        AdjustmentAmount_txt = v.findViewById(R.id.MultiVendor_AdjustmentAmount_txt);
        NetAmount_text = v.findViewById(R.id.MultiVendor_NetAmount_text);


        fromdate = v.findViewById(R.id.MultiVendor_fromdate);
        todate = v.findViewById(R.id.MultiVendor_todate);
        payment_modes = v.findViewById(R.id.MultiVendor_payment_modes);
        TransactionId = v.findViewById(R.id.MultiVendor_TransactionId);
        Amount = v.findViewById(R.id.MultiVendor_Amount);
        to_Amount = v.findViewById(R.id.MultiVendor_to_Amount);
        AdjustmentAmount = v.findViewById(R.id.MultiVendor_AdjustmentAmount);
        to_AdjustmentAmount = v.findViewById(R.id.MultiVendor_to_AdjustmentAmount);
        from_NetAmount = v.findViewById(R.id.MultiVendor_from_NetAmount);
        to_NetAmount = v.findViewById(R.id.MultiVendor_to_NetAmount);
        filter = v.findViewById(R.id.MultiVendor_filter);
        reset_filter = v.findViewById(R.id.MultiVendor_reset_filter);

        fontSetting.setFontforTextviews(createdAt_text, "Roboto-Bold.ttf", getApplicationContext());
        fontSetting.setFontforTextviews(payment_mode_txt, "Roboto-Bold.ttf", getApplicationContext());
        fontSetting.setFontforTextviews(TransactionId_txt, "Roboto-Bold.ttf", getApplicationContext());
        fontSetting.setFontforTextviews(Amount_txt, "Roboto-Bold.ttf", getApplicationContext());
        fontSetting.setFontforTextviews(AdjustmentAmount_txt, "Roboto-Bold.ttf", getApplicationContext());
        fontSetting.setFontforTextviews(NetAmount_text, "Roboto-Bold.ttf", getApplicationContext());

        fontSetting.setfontforButtons(filter, "Roboto-Bold.ttf", getApplicationContext());
        fontSetting.setfontforButtons(reset_filter, "Roboto-Bold.ttf", getApplicationContext());

        fontSetting.setfontforEditText(fromdate, "Roboto-Light.ttf", getApplicationContext());
        fontSetting.setfontforEditText(todate, "Roboto-Light.ttf", getApplicationContext());
        fontSetting.setfontforEditText(payment_modes, "Roboto-Light.ttf", getApplicationContext());
        fontSetting.setfontforEditText(TransactionId, "Roboto-Light.ttf", getApplicationContext());
        fontSetting.setfontforEditText(Amount, "Roboto-Light.ttf", getApplicationContext());
        fontSetting.setfontforEditText(to_Amount, "Roboto-Light.ttf", getApplicationContext());
        fontSetting.setfontforEditText(AdjustmentAmount, "Roboto-Light.ttf", getApplicationContext());
        fontSetting.setfontforEditText(to_AdjustmentAmount, "Roboto-Light.ttf", getApplicationContext());
        fontSetting.setfontforEditText(from_NetAmount, "Roboto-Light.ttf", getApplicationContext());
        fontSetting.setfontforEditText(to_NetAmount, "Roboto-Light.ttf", getApplicationContext());

        if (!(datafilterjson.isEmpty())) {

            try {
                JSONObject object = null;
                object = new JSONObject(datafilterjson);
                if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                    Log.i("object", "" + object);
                }
                TransactionId.setText(object.getString("transaction_id"));
                payment_modes.setText(object.getString("payment_method"));
                Amount.setText(object.getJSONObject("amount").getString("from"));
                to_Amount.setText(object.getJSONObject("amount").getString("to"));
                AdjustmentAmount.setText(object.getJSONObject("fee").getString("from"));
                to_AdjustmentAmount.setText(object.getJSONObject("fee").getString("to"));
                fromdate.setText(object.getJSONObject("created_at").getString("from"));
                todate.setText(object.getJSONObject("created_at").getString("to"));
                from_NetAmount.setText(object.getJSONObject("net_amount").getString("from"));
                to_NetAmount.setText(object.getJSONObject("net_amount").getString("to"));

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        fromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_date = true;
                AppConstant.setDateFrom(Ced_MultiVendor_ListTransaction.this, fromdate);
            }
        });
        todate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (select_date) {
                    AppConstant.setDateTo(Ced_MultiVendor_ListTransaction.this, todate, fromdate);
                } else {
                    Toast.makeText(Ced_MultiVendor_ListTransaction.this, R.string.please_fill_both_dates, Toast.LENGTH_SHORT).show();
                }
            }
        });
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fromdate.getText().toString().isEmpty()
                        && todate.getText().toString().isEmpty()
                        && payment_modes.getText().toString().isEmpty()
                        && TransactionId.getText().toString().isEmpty()
                        && Amount.getText().toString().isEmpty()
                        && to_Amount.getText().toString().isEmpty()
                        && AdjustmentAmount.getText().toString().isEmpty()
                        && to_AdjustmentAmount.getText().toString().isEmpty()
                        && from_NetAmount.getText().toString().isEmpty()
                        && to_NetAmount.getText().toString().isEmpty()) {
                    Toast.makeText(Ced_MultiVendor_ListTransaction.this, "Please select the desired filter", Toast.LENGTH_SHORT).show();

                } else {

                    try {
                        JSONObject datejsonObject = new JSONObject();
                        datejsonObject.put("from", fromdate.getText().toString());
                        datejsonObject.put("to", todate.getText().toString());
                        JSONObject pendingamountjsonObject = new JSONObject();
                        pendingamountjsonObject.put("from", from_NetAmount.getText().toString());
                        pendingamountjsonObject.put("to", to_NetAmount.getText().toString());
                        req.put("created_at", datejsonObject);
                        req.put("net_amount", pendingamountjsonObject);
                        req.put("transaction_id", TransactionId.getText().toString());
                        req.put("payment_method", payment_modes.getText().toString());
                        JSONObject amount_obj = new JSONObject();
                        amount_obj.put("from", Amount.getText().toString());
                        amount_obj.put("to", to_Amount.getText().toString());
                        req.put("amount", amount_obj);
                        JSONObject feeobj = new JSONObject();
                        feeobj.put("from", AdjustmentAmount.getText().toString());
                        feeobj.put("to", to_AdjustmentAmount.getText().toString());
                        req.put("fee", feeobj);
                        if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                            Log.i("data", req.toString());
                        }
                        listDialog.dismiss();
                        Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_ListTransaction.class);
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
                fromdate.setText("");
                todate.setText("");
                from_NetAmount.setText("");
                to_NetAmount.setText("");
                TransactionId.setText("");
                payment_modes.setText("");
                Amount.setText("");
                AdjustmentAmount.setText("");
                postdata.remove("filter");
                datafilterjson = "";
                listDialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_ListTransaction.class);
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
                    //    swipe_refresh_layout.setRefreshing(false);
                    out = output.toString();
                    transactionlistdata();
                } else {
                    Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }
            }
        }, Ced_MultiVendor_ListTransaction.this, "POST", postdata);
        response.execute(url + "/page/" + current);
    }

    private void transactionlistdata() {
        try {
            jsonObject = new JSONObject(out);
            if (jsonObject.has("vendor_approved")) {
                logout();
            } else {
                if (jsonObject.has("data")) {
                    if (jsonObject.getJSONObject("data").getBoolean("success")) {
                        Data data = new GsonBuilder().create().fromJson(jsonObject.getJSONObject("data").toString(), Data.class);
                        if (data.getSuccess()) {
                            inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View vi = inflater.inflate(R.layout.ced_multivendor_activity_view_transaction_head, null);
                            listing_parent.addView(vi, 0);
                            Transaction_statistics_head = vi.findViewById(R.id.MultiVendor_Transaction_statistics_head);
                            TotalEarnedAmount_head = vi.findViewById(R.id.MultiVendor_TotalEarnedAmount_head);
                            TotalPendingAmount_head = vi.findViewById(R.id.MultiVendor_TotalPendingAmount_head);
                            PendingTransfers_head = vi.findViewById(R.id.MultiVendor_PendingTransfers_head);
                            TotalEarnedAmount = vi.findViewById(R.id.MultiVendor_TotalEarnedAmount);
                            TotalPendingAmount = vi.findViewById(R.id.MultiVendor_TotalPendingAmount);
                            PendingTransfers = vi.findViewById(R.id.MultiVendor_PendingTransfers);

                            setFont(Transaction_statistics_head, TotalPendingAmount_head, TotalEarnedAmount_head, PendingTransfers_head, TotalEarnedAmount, TotalPendingAmount, PendingTransfers);

                            TotalEarnedAmount.setText(data.getEarnedAmount());
                            TotalPendingAmount.setText(data.getPendingAmount());
                            PendingTransfers.setText(data.getPendingTransfer());
                            payment_msg = data.getPaymentMsg();
                            if (data.getCount() != null && data.getCount().length() > 0) {
                                count_number = Integer.parseInt(data.getCount());
                                Count_total.setText(getString(R.string.you_have_txt) + " " + count_number + " " + getResources().getString(R.string.Transactions_title));
                            }
                            boolean payment;
                            String payment_msg_en = "You dont have any transaction yet";
                            String payment_msg_ar = "لا توجد معاملات متاحة";
                            if (payment_msg_en.equalsIgnoreCase(payment_msg))
                                payment = false;
                            else payment = !payment_msg_ar.equalsIgnoreCase(payment_msg);
                            if (payment) {
                                if (data.getTransactiondata() != null) {
                                    List<TransactionDataModel> transactionDataModelList = data.getTransactiondata();
                                    transactionadapter = new Ced_MultiVendor_TransactionListingAdapter(Ced_MultiVendor_ListTransaction.this, transactionDataModelList);
                                    MultiVendor_transaction_rv.setAdapter(transactionadapter);

                                    MultiVendor_transaction_rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                        @Override
                                        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                                            super.onScrollStateChanged(recyclerView, newState);
                                            if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE && load) {
                                                current = current + 1;
                                                Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(output -> {
                                                    out = output.toString();
                                                    JSONObject object = new JSONObject(out);
                                                    if (object.getJSONObject("data").getString("transactiondata").equals("NO_ORDER")) {
                                                        if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                                            Log.i("jsonnull", out);
                                                        }
                                                        Toast.makeText(Ced_MultiVendor_ListTransaction.this, object.getJSONObject("data").getString("transactiondata"), Toast.LENGTH_SHORT).show();
                                                        load = false;
                                                    } else {
                                                        scrollData();
                                                    }
                                                }, Ced_MultiVendor_ListTransaction.this, "POST", postdata);
                                                response.execute(url + "/page/" + current);
                                            }
                                        }

                                        @Override
                                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                            super.onScrolled(recyclerView, dx, dy);

                                        }
                                    });
                                } else {
                                    text_msg.setText(payment_msg);
                                    text_msg.setVisibility(View.VISIBLE);
                                    Toast.makeText(getApplicationContext(), payment_msg, Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }else if (jsonObject.getJSONObject("data").has("message")) {
                        text_msg.setText(jsonObject.getJSONObject("data").getString("message"));
                        text_msg.setVisibility(View.VISIBLE);
                    }
                } else {
                    text_msg.setText(jsonObject.getJSONObject("data").getString("message"));
                    text_msg.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), jsonObject.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setFont(View... view) {
        for (int i = 0; i < view.length; i++) {
            if (view[i] instanceof Button) {
                Button button = (Button) view[i];
                fontSetting.setfontforButtons(button, "Roboto-Bold.ttf", getApplicationContext());
            }
            if (view[i] instanceof TextView) {
                TextView textView = (TextView) view[i];
                fontSetting.setFontforTextviews(textView, "Roboto-Black.ttf", getApplicationContext());
            }
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private void scrollData() throws JSONException {
        if (jsonObject.has("vendor_approved")) {
            logout();
        } else if (jsonObject.has("data")) {
            Data data = new GsonBuilder().create().fromJson(jsonObject.getJSONObject("data").toString(), Data.class);
            if (data.getSuccess()) {
                if (data.getTransactiondata() != null) {
                    load = true;
                    List<TransactionDataModel> transactionDataModelList = data.getTransactiondata();
                    transactionadapter.data.addAll(transactionDataModelList);
                    transactionadapter.notifyDataSetChanged();
                } else {
                    load = false;
                }
            } else {
                load = false;
                Toast.makeText(getApplicationContext(), jsonObject.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
            }
        } else {
            load = false;
        }
    }

    @Override
    protected void onResume() {
        Ced_MultiVendor_ConnectionDetector connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        if (connectionDetector.isConnectingToInternet()) {
            setname(session.getvendorname());
            setprofilepic(session.getvendorpic());
            changetitle("View Transactions");
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
