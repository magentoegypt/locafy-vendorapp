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

import static magentoegypt.locafy.manage_orders.Ced_MultiVendor_InvoiceListing.KEY_VENDOR_PAYMENT;

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
import android.widget.AdapterView;
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

/**
 * Created by developer on 23/6/16.
 */
public class Ced_MultiVendor_CreditMemoListing extends Ced_MultiVendor_NavigationActivity {
    static final String KEY_INCREMENT_ID = "increment_id";
    static final String KEY_ORDER_INCREMENT_ID = "order_increment_id";
    static final String KEY_CREATED_AT = "created_at";
    static final String KEY_ORDER_CREATED_AT = "order_created_at";
    static final String KEY_BILLING_NAME = "billing_name";
    static final String KEY_GRAND_TOTAL = "grand_total";
    static final String KEY_ENTITY_ID = "entity_id";
    static final String KEY_STATE = "state";
    public static EditText MultiVendor_edt_start_date_create;
    public static EditText MultiVendor_edt_end_date_create;
    public static EditText MultiVendor_edt_start_date_order;
    public static EditText MultiVendor_edt_end_date_order;
    private static LayoutInflater inflater = null;
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    HashMap<String, String> postdata;
    String url = "";
    String out = "";
    String hashkey, vendor_id;
    Ced_MultiVendor_FontSetting fontSetting;
    TextView text_msg, Count_total;
    SwipeRefreshLayout swipe_refresh_layout;
    int current = 1;
    JSONObject jsonObject;
    ArrayList<HashMap<String, String>> CredtMemoinfo;
    JSONArray CredtMemodetail;
    ListView CredtMemolist;
    String message, increment_id, order_increment_id, created_at, order_created_at,
            billing_name, grand_total, entity_id, state;
    boolean load = true;
    Ced_MultiVendor_CreditMemoListingAdapter creditMemoListingAdapter;
    LinearLayout filtersection;
    Dialog listDialog;
    TextView txt_invoice_id, txt_invoice_date, txt_bill_name, txt_order_id,
            txt_order_date, txt_Amount, txt_invoicestatus;
    EditText edt_invoice_id, edt_bill_name,
            edt_order_id, edt_Amount_from,
            edt_Amount_to;
    Spinner edt_invoice_status;
    String datafilterjson = "";
    Calendar newCalendar;
    Button setfilter, unsetfilter;
    JSONObject req;
    Boolean select_date_create = false;
    Boolean select_date_order = false;
    private int visible = 1;
    private SimpleDateFormat dateFormatter;
    private int count = 0;

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
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(getApplicationContext());
        fontSetting = new Ced_MultiVendor_FontSetting();
        postdata = new HashMap<>();
        CredtMemoinfo = new ArrayList<>();
        req = new JSONObject();

        url = session.getBase_Url() + "vorderapi/vcreditmemo/item";
        if (connectionDetector.isConnectingToInternet()) {
            ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
            getLayoutInflater().inflate(R.layout.ced_multivendor_activity_creditmemo_list, content, true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);

            if (session.getvendorname() != null) {
                setname(session.getvendorname());
                setprofilepic(session.getvendorpic());
                changetitle("CreditMemo");
            }
            text_msg = findViewById(R.id.MultiVendor_text_msg);
            Count_total = findViewById(R.id.MultiVendor_Count_total);
            CredtMemolist = findViewById(R.id.MultiVendor_CredtMemolist);
            swipe_refresh_layout = findViewById(R.id.MultiVendor_swipe_refresh_layout);
            swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    swipe_refresh_layout.setRefreshing(true);
                    Intent intent = new Intent(Ced_MultiVendor_CreditMemoListing.this, Ced_MultiVendor_CreditMemoListing.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }

            });

            filtersection = findViewById(R.id.MultiVendor_filtersection);
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


    private void showfilter() {
        listDialog = new Dialog(this, R.style.PauseDialog);
        listDialog.setTitle(getResources().getString(R.string.alert_name));
        listDialog.setCanceledOnTouchOutside(true);
        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View vi2 = inflater.inflate(R.layout.ced_multivendor_creditmemolist_filter, null);

        txt_invoice_id = vi2.findViewById(R.id.MultiVendor_txt_invoice_id);
        txt_invoice_date = vi2.findViewById(R.id.MultiVendor_txt_invoice_date);
        txt_invoice_id = vi2.findViewById(R.id.MultiVendor_txt_bill_name);
        txt_bill_name = vi2.findViewById(R.id.MultiVendor_txt_bill_name);
        txt_order_id = vi2.findViewById(R.id.MultiVendor_txt_order_id);
        txt_order_date = vi2.findViewById(R.id.MultiVendor_txt_order_date);
        txt_Amount = vi2.findViewById(R.id.MultiVendor_txt_Amount);
        txt_invoicestatus = vi2.findViewById(R.id.MultiVendor_txt_invoicestatus);

        edt_invoice_id = vi2.findViewById(R.id.MultiVendor_edt_invoice_id);
        MultiVendor_edt_start_date_create = vi2.findViewById(R.id.MultiVendor_edt_invoice_date_from);
        MultiVendor_edt_end_date_create = vi2.findViewById(R.id.MultiVendor_edt_invoice_date_to);
        edt_bill_name = vi2.findViewById(R.id.MultiVendor_edt_bill_name);
        edt_order_id = vi2.findViewById(R.id.MultiVendor_edt_order_id);
        MultiVendor_edt_start_date_order = vi2.findViewById(R.id.MultiVendor_edt_order_date_from);
        MultiVendor_edt_end_date_order = vi2.findViewById(R.id.MultiVendor_edt_order_date_to);
        edt_Amount_from = vi2.findViewById(R.id.MultiVendor_edt_Amount_from);
        edt_Amount_to = vi2.findViewById(R.id.MultiVendor_edt_Amount_to);

        edt_invoice_status = vi2.findViewById(R.id.MultiVendor_edt_invoice_status);
        setfilter = vi2.findViewById(R.id.MultiVendor_setfilter);
        unsetfilter = vi2.findViewById(R.id.MultiVendor_unsetfilter);

        if (!(datafilterjson.isEmpty())) {
            JSONObject object = null;
            try {
                object = new JSONObject(datafilterjson);
                edt_invoice_id.setText(object.getString("increment_id"));
                edt_order_id.setText(object.getString("order_increment_id"));
                edt_bill_name.setText(object.getString("billing_name"));
                if (object.getString("state").equals("")) {
                    edt_invoice_status.setSelection(0);
                } else if (object.getString("state").equals("Pending")) {
                    edt_invoice_status.setSelection(1);
                } else if (object.getString("state").equals("Refunded")) {
                    edt_invoice_status.setSelection(2);
                } else if (object.getString("state").equals("Canceled")) {
                    edt_invoice_status.setSelection(3);
                }

                MultiVendor_edt_start_date_create.setText(object.getJSONObject("created_at").getString("from"));
                MultiVendor_edt_end_date_create.setText(object.getJSONObject("created_at").getString("to"));

                MultiVendor_edt_start_date_order.setText(object.getJSONObject("order_created_at").getString("from"));
                MultiVendor_edt_end_date_order.setText(object.getJSONObject("order_created_at").getString("to"));

                edt_Amount_from.setText(object.getJSONObject("grand_total").getString("from"));
                edt_Amount_to.setText(object.getJSONObject("grand_total").getString("to"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        MultiVendor_edt_start_date_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select_date_order = true;
                AppConstant.setDateFrom(Ced_MultiVendor_CreditMemoListing.this, MultiVendor_edt_start_date_order);

            }
        });


        MultiVendor_edt_end_date_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (select_date_order) {

                    AppConstant.setDateTo(Ced_MultiVendor_CreditMemoListing.this, MultiVendor_edt_end_date_order, MultiVendor_edt_start_date_order);

                } else {
                    Toast.makeText(Ced_MultiVendor_CreditMemoListing.this, R.string.please_fill_both_dates, Toast.LENGTH_SHORT).show();
                }
            }
        });

        MultiVendor_edt_start_date_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select_date_create = true;
                AppConstant.setDateFrom(Ced_MultiVendor_CreditMemoListing.this, MultiVendor_edt_start_date_create);
            }
        });

        MultiVendor_edt_end_date_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (select_date_create) {
                    AppConstant.setDateTo(Ced_MultiVendor_CreditMemoListing.this, MultiVendor_edt_end_date_create, MultiVendor_edt_start_date_create);
                } else {
                    Toast.makeText(Ced_MultiVendor_CreditMemoListing.this, R.string.please_fill_both_dates, Toast.LENGTH_SHORT).show();

                }
            }
        });

        setfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_invoice_id.getText().toString().isEmpty() &&
                        MultiVendor_edt_start_date_create.getText().toString().isEmpty() &&
                        MultiVendor_edt_end_date_create.getText().toString().isEmpty() &&
                        edt_bill_name.getText().toString().isEmpty() &&
                        edt_order_id.getText().toString().isEmpty() &&
                        MultiVendor_edt_start_date_order.getText().toString().isEmpty() &&
                        MultiVendor_edt_end_date_order.getText().toString().isEmpty() &&
                        edt_Amount_from.getText().toString().isEmpty() &&
                        edt_Amount_to.getText().toString().isEmpty() &&
                        edt_invoice_status.getSelectedItemPosition() == 0
                ) {
                    Toast.makeText(Ced_MultiVendor_CreditMemoListing.this, getResources().getString(R.string.PleaseSelectdesiredFilter), Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        req.put("increment_id", edt_invoice_id.getText().toString());
                        req.put("order_increment_id", edt_order_id.getText().toString());
                        req.put("billing_name", edt_bill_name.getText().toString());
                        String[] creditmemo_status = {"Select","Pending","Refunded","Canceled"};
                        if (creditmemo_status[edt_invoice_status.getSelectedItemPosition()].equals("Select")) {
                            req.put("state", "");
                        } else {
                            req.put("state", creditmemo_status[edt_invoice_status.getSelectedItemPosition()]);
                        }
                        JSONObject created_at = new JSONObject();
                        created_at.put("from", MultiVendor_edt_start_date_create.getText().toString());
                        created_at.put("to", MultiVendor_edt_end_date_create.getText().toString());
                        req.put("created_at", created_at);
                        JSONObject order_created_at = new JSONObject();
                        order_created_at.put("from", MultiVendor_edt_start_date_order.getText().toString());
                        order_created_at.put("to", MultiVendor_edt_end_date_order.getText().toString());
                        req.put("order_created_at", order_created_at);
                        JSONObject grand_total = new JSONObject();
                        grand_total.put("from", edt_Amount_from.getText().toString());
                        grand_total.put("to", edt_Amount_to.getText().toString());
                        req.put("grand_total", grand_total);
                        listDialog.dismiss();
                        if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                            Log.i("filter", req.toString());
                        }
                        Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_CreditMemoListing.class);
                        intent.putExtra("filter", req.toString());
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        });
        unsetfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postdata.remove("filter");
                datafilterjson = "";
                listDialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_CreditMemoListing.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
            }
        });

        listDialog.setContentView(vi2);
        listDialog.setCancelable(true);
        listDialog.show();
    }

    private void request() {
        Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                if (functionalityList.getExtensionAddon()) {
                    out = output.toString();
                    if (out.equals("NO_CREDITMEMO")) {
                        text_msg.setText(getResources().getString(R.string.noInvoicetolist));
                        text_msg.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), R.string.noInvoicetolist, Toast.LENGTH_LONG).show();
                        //  Toast.makeText(getApplicationContext(),"You have no more orders to see",Toast.LENGTH_SHORT).show();
                    } else {
                        CreditMemolistdata();
                    }
                } else {
                    Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }
            }
        }, Ced_MultiVendor_CreditMemoListing.this, "POST", postdata);
        response.execute(url + "/page/" + current);
    }

    private void CreditMemolistdata() throws JSONException {
        jsonObject = new JSONObject(out);
        if (jsonObject.has("vendor_approved")) {
            logout();
        } else {
            if (jsonObject.getJSONObject("data").getBoolean("success")) {
                CredtMemodetail = jsonObject.getJSONObject("data").getJSONArray("creditmemo");
                Count_total.setText(getString(R.string.you_have_txt) + " " + jsonObject.getJSONObject("data").getString("count") + " " + getString(R.string.CreditMemo));
                count = Integer.parseInt(jsonObject.getJSONObject("data").getString("count"));
                for (int i = 0; i < CredtMemodetail.length(); i++) {
                    JSONObject c = null;
                    c = CredtMemodetail.getJSONObject(i);
                    increment_id = c.getString(KEY_INCREMENT_ID);
                    order_increment_id = c.getString(KEY_ORDER_INCREMENT_ID);
                    created_at = c.getString(KEY_CREATED_AT);
                    order_created_at = c.getString(KEY_ORDER_CREATED_AT);
                    billing_name = c.getString(KEY_BILLING_NAME);
                    if (c.has(KEY_VENDOR_PAYMENT)) {
                        grand_total = c.getString(KEY_VENDOR_PAYMENT);
                    }

                    entity_id = c.getString(KEY_ENTITY_ID);
                    state = c.getString(KEY_STATE);
                    HashMap hashMap = new HashMap();
                    hashMap.put(KEY_INCREMENT_ID, increment_id);
                    hashMap.put(KEY_ORDER_INCREMENT_ID, order_increment_id);
                    hashMap.put(KEY_CREATED_AT, created_at);
                    hashMap.put(KEY_ORDER_CREATED_AT, order_created_at);
                    hashMap.put(KEY_BILLING_NAME, billing_name);
                    hashMap.put(KEY_GRAND_TOTAL, grand_total);
                    hashMap.put(KEY_ENTITY_ID, entity_id);
                    hashMap.put(KEY_STATE, state);
                    CredtMemoinfo.add(hashMap);
                }
                creditMemoListingAdapter = new Ced_MultiVendor_CreditMemoListingAdapter(Ced_MultiVendor_CreditMemoListing.this, CredtMemoinfo);
                CredtMemolist.setAdapter(creditMemoListingAdapter);
                CredtMemolist.setDivider(new ColorDrawable(getResources().getColor(R.color.transparent)));
                CredtMemolist.setDividerHeight(0);
                CredtMemolist.setOnScrollListener(new AbsListView.OnScrollListener() {
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

                                            scrolldata();
                                        }
                                    }
                                }, Ced_MultiVendor_CreditMemoListing.this, "POST", postdata);
                                response.execute(url + "/page/" + current);
                            }
                        }
                    }
                });
                CredtMemolist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        TextView Invoice_id = view.findViewById(R.id.MultiVendor_CreditMemo_id);
                        String CreditMemo_id = Invoice_id.getText().toString();
                        Intent orderview = new Intent(Ced_MultiVendor_CreditMemoListing.this, Ced_MultiVendor_CreditMemoView.class);
                        orderview.putExtra("creditmemo_id", CreditMemo_id);
                        startActivity(orderview);
                        overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                    }
                });
            } else {
                message = jsonObject.getJSONObject("data").getString("message");
                text_msg.setText(message);
                text_msg.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void scrolldata() throws JSONException {
        jsonObject = new JSONObject(out);
        if (jsonObject.has("vendor_approved")) {
            logout();
        } else {
            if (jsonObject.getJSONObject("data").getBoolean("success")) {
                CredtMemodetail = jsonObject.getJSONObject("data").getJSONArray("creditmemo");
                int counter = count + Integer.parseInt(jsonObject.getJSONObject("data").getString("count"));
                count = counter;
                Count_total.setText(getString(R.string.you_have_txt) + " " + counter + " " + getString(R.string.CreditMemo));
                for (int i = 0; i < CredtMemodetail.length(); i++) {
                    JSONObject c = null;
                    c = CredtMemodetail.getJSONObject(i);
                    increment_id = c.getString(KEY_INCREMENT_ID);
                    order_increment_id = c.getString(KEY_ORDER_INCREMENT_ID);
                    created_at = c.getString(KEY_CREATED_AT);
                    order_created_at = c.getString(KEY_ORDER_CREATED_AT);
                    billing_name = c.getString(KEY_BILLING_NAME);
                    if (c.has(KEY_VENDOR_PAYMENT)) {
                        grand_total = c.getString(KEY_VENDOR_PAYMENT);
                    }

                    entity_id = c.getString(KEY_ENTITY_ID);
                    state = c.getString(KEY_STATE);
                    HashMap hashMap = new HashMap();
                    hashMap.put(KEY_INCREMENT_ID, increment_id);
                    hashMap.put(KEY_ORDER_INCREMENT_ID, order_increment_id);
                    hashMap.put(KEY_CREATED_AT, created_at);
                    hashMap.put(KEY_ORDER_CREATED_AT, order_created_at);
                    hashMap.put(KEY_BILLING_NAME, billing_name);
                    hashMap.put(KEY_GRAND_TOTAL, grand_total);
                    hashMap.put(KEY_ENTITY_ID, entity_id);
                    hashMap.put(KEY_STATE, state);
                    CredtMemoinfo.add(hashMap);
                }
                creditMemoListingAdapter = new Ced_MultiVendor_CreditMemoListingAdapter(Ced_MultiVendor_CreditMemoListing.this, CredtMemoinfo);
                int cp = CredtMemolist.getFirstVisiblePosition();
                // CredtMemolist.setAdapter(creditMemoListingAdapter);
                CredtMemolist.setDivider(new ColorDrawable(getResources().getColor(R.color.transparent)));
                CredtMemolist.setDividerHeight(0);
                CredtMemolist.setSelectionFromTop(cp + 1, 0);
                creditMemoListingAdapter.notifyDataSetChanged();
                load = true;
            } else {

            }
        }
    }

    @Override
    protected void onResume() {
        Ced_MultiVendor_ConnectionDetector connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        if (connectionDetector.isConnectingToInternet()) {
            setname(session.getvendorname());
            setprofilepic(session.getvendorpic());
            changetitle("Credit Memo");
            //        invalidateOptionsMenu();
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