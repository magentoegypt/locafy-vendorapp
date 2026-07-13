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

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import magentoegypt.locafy.addons.inventory.OutOfStockActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import magentoegypt.locafy.base_app.Ced_MultiVendor_FontSetting;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_UnAuthourizedRequestError;
import magentoegypt.locafy.manage_orders.order_view_comman_adapter.CommanViewAdapter;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;
import magentoegypt.locafy.R;
import magentoegypt.locafy_constant.AppConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by developer on 28/6/16.
 */
public class Ced_MultiVendor_Information_View extends Ced_MultiVendor_NavigationActivity {
    static final String KEY_NAME = "name";
    static final String KEY_SKU = "sku";
    static final String KEY_PRICE = "price";
    static final String KEY_QTY = "qty_ordered";
    static final String KEY_QTY_INVOICED = "qty_invoiced";
    static final String KEY_QTY_REFUNDED = "qty_refunded";
    static final String KEY_QTY_SHIPPED = "qty_shipped";
    static final String KEY_QTY_CANCELED = "qty_canceled";
    static final String KEY_DISCOUNT = "discount_amount";
    static final String KEY_TAX_AMOUNT = "tax_amount";
    static final String KEY_ROW_TOTAL = "row_total";
    static final String KEY_SUBTOTAL = "subtotal";
    static final String ADDITIONAL_OPTION = "additonal_options";
    static final String ITEM_STATUS = "item_status";
    static final String ORIGINAL_PRICE = "original_price";
    static final String TAX_PERCENT = "tax_percent";
    static final String IGSTRATE = "igstRate";
    static final String IGSTAMT = "igstAmt";
    static final String CGSTRATE = "cgstRate";
    static final String SGSTRATE = "sgstRate";
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    String url, out = "";
    HashMap<String, String> postdata;
    Ced_MultiVendor_FontSetting fontSetting;
    JSONObject jsonObject;
    JSONArray additional_info;
    String vendor_id, hashkey, post_id;
    JSONArray invoicedata, items, account_data, comments;
    String statusLabel, storeName, orderStoreDate, header_meassage, CustomerName, CustomerEmail,
            CustomerGroup, ship_to, street, city, state, pincode, country, mobile,
            bill_to, bill_street, bill_city, bill_state, bill_pincode, bill_country,
            bill_mobile, payment_method_title, cc_type, cc_last4, cc_owner, message_at_last, order_total,
            label, value, date_time, notified, comment, shipping_method, shipping_price, order_subtotal,
            order_commission, net_earn, order_shipping, order_tax, itemstatus = "", originalprice = "";
    TextView orderdate_head, orderDate, order_status_head, OrderStatus,
            PurchasedFrom_head, PurchasedFrom, CustomerName_head, Customer_Name, Email_head, Email,
            CustomerGroup_head, Customer_Group, billing_ship_to,
            billing_street, billing_city, billing_state, billing_pincode,
            billing_country, billing_mobile, shipping_ship_to,
            shipping_street, shipping_city, shipping_state, shipping_pincode, shipping_country, shipping_phone,
            PaymentMethod, CreditCardType_head, CreditCardType,
            CreditCardNumber_head, CreditCardNumber,
            NameontheCard_head, NameontheCard, shipping_amount,
            ShippingMethod, Subtotal, grandtotals, grandtotalearned, CommissionFee,
            ServiceTax, MarketFee,
            NetEarned, subtotal_head, grandtotal_head,
            grandtotalearned_head, CommissionFee_head, NetEarned_head,
            TaxAmount_head, TaxAmount, Discount, Discount_head, ShippingAndHandling, ShippingAndHandling_head, MessageAtLast;
    Button order_increment_id, ProductInfo_head, InvoiceTotals;
    String tax_percent = "", igstRate = "", igstAmt = "", cgstRate = "", sgstRate = "";

    ArrayList<HashMap<String, String>> Invoiceinfo;
    ArrayList<HashMap<String, String>> dyanmicinfo, dynamic_comment_list;
    Ced_MultiVendor_Information_ViewAdapter information_viewAdapter;
    RecyclerView invoice_view_list;
    ScrollView mainscroll;
    FloatingActionButton fab;
    HashMap<String, ArrayList> AddNew_collection, addnew_comment;
    CardView view11;
    LinearLayout account_info_dynamic, dynamic_comment, Shipping_Address_linear;

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();


    }

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
        fontSetting = new Ced_MultiVendor_FontSetting();
        postdata = new HashMap<>();
        Invoiceinfo = new ArrayList<>();
        if (connectionDetector.isConnectingToInternet()) {
            ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
            getLayoutInflater().inflate(R.layout.ced_multivendor_activity_information_view, content, true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);
            if (session.getvendorname() != null) {
                setname(session.getvendorname());
                setprofilepic(session.getvendorpic());
                changetitle("Order View");
            }
            final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
            pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    pullToRefresh.setRefreshing(true);
                    Intent intent = new Intent(Ced_MultiVendor_Information_View.this, Ced_MultiVendor_Information_View.class);
                    intent.putExtra("order_id", getIntent().getStringExtra("order_id"));
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }
            });
            orderdate_head = findViewById(R.id.MultiVendor_orderdate_head);
            orderDate = findViewById(R.id.MultiVendor_orderDate);
            order_status_head = findViewById(R.id.MultiVendor_order_status_head);
            OrderStatus = findViewById(R.id.MultiVendor_OrderStatuss);
            PurchasedFrom_head = findViewById(R.id.MultiVendor_PurchasedFrom_head);
            PurchasedFrom = findViewById(R.id.MultiVendor_PurchasedFrom);
            CustomerName_head = findViewById(R.id.MultiVendor_CustomerName_head);
            Customer_Name = findViewById(R.id.MultiVendor_CustomerName);
            Email_head = findViewById(R.id.MultiVendor_Email_head);
            Email = findViewById(R.id.MultiVendor_Email);
            CustomerGroup_head = findViewById(R.id.MultiVendor_CustomerGroup_head);
            Customer_Group = findViewById(R.id.MultiVendor_CustomerGroup);
            billing_ship_to = findViewById(R.id.MultiVendor_billing_ship_to);
            billing_street = findViewById(R.id.MultiVendor_billing_street);
            billing_city = findViewById(R.id.MultiVendor_billing_city);
            billing_state = findViewById(R.id.MultiVendor_billing_state);
            billing_pincode = findViewById(R.id.MultiVendor_billing_pincode);
            billing_country = findViewById(R.id.MultiVendor_billing_country);
            billing_mobile = findViewById(R.id.MultiVendor_billing_mobile);
            shipping_ship_to = findViewById(R.id.MultiVendor_shipping_ship_to);
            shipping_street = findViewById(R.id.MultiVendor_shipping_street);
            shipping_state = findViewById(R.id.MultiVendor_shipping_state);
            shipping_city = findViewById(R.id.MultiVendor_shipping_city);
            shipping_pincode = findViewById(R.id.MultiVendor_shipping_pincode);
            shipping_country = findViewById(R.id.MultiVendor_shipping_country);
            shipping_phone = findViewById(R.id.MultiVendor_shipping_phone);
            PaymentMethod = findViewById(R.id.MultiVendor_PaymentMethod);
            CreditCardType_head = findViewById(R.id.MultiVendor_CreditCardType_head);
            CreditCardType = findViewById(R.id.MultiVendor_CreditCardType);
            CreditCardNumber_head = findViewById(R.id.MultiVendor_CreditCardNumber_head);
            CreditCardNumber = findViewById(R.id.MultiVendor_CreditCardNumber);
            NameontheCard_head = findViewById(R.id.MultiVendor_NameontheCard_head);
            NameontheCard = findViewById(R.id.MultiVendor_NameontheCard);
            shipping_amount = findViewById(R.id.MultiVendor_shipping_amount);
            ShippingMethod = findViewById(R.id.MultiVendor_ShippingMethod);
            fab = findViewById(R.id.MultiVendor_fab);
            invoice_view_list = findViewById(R.id.MultiVendor_invoice_view_list);
            invoice_view_list.setLayoutManager(new LinearLayoutManager(this));
            order_increment_id = findViewById(R.id.MultiVendor_order_increment_id);
            mainscroll = findViewById(R.id.MultiVendor_mainscroll);
            account_info_dynamic = findViewById(R.id.MultiVendor_account_info_dynamic);
            dynamic_comment = findViewById(R.id.MultiVendor_dynamic_comment);
            Shipping_Address_linear = findViewById(R.id.Shipping_Address_linear);
            ProductInfo_head = findViewById(R.id.MultiVendor_ProductInfo_head);
            InvoiceTotals = findViewById(R.id.MultiVendor_InvoiceTotals);
            InvoiceTotals.setText(getResources().getString(R.string.OrderTotal));
            ProductInfo_head.setText(getResources().getString(R.string.ItemsOrdered));
            view11 = findViewById(R.id.MultiVendor_view11);
            Subtotal = findViewById(R.id.MultiVendor_Subtotal);

            grandtotalearned = findViewById(R.id.MultiVendor_grandtotalearned);
            CommissionFee = findViewById(R.id.MultiVendor_CommissionFee);


            ServiceTax = findViewById(R.id.MultiVendorServiceTax);
            MarketFee = findViewById(R.id.MultiVendor_MarketFee);


            NetEarned = findViewById(R.id.MultiVendor_NetEarned);
            subtotal_head = findViewById(R.id.MultiVendor_subtotal_head);

            grandtotalearned_head = findViewById(R.id.MultiVendor_grandtotalearned_head);
            CommissionFee_head = findViewById(R.id.MultiVendor_CommissionFee_head);
            NetEarned_head = findViewById(R.id.MultiVendor_NetEarned_head);
            TaxAmount_head = findViewById(R.id.MultiVendor_TaxAmount_head);
            TaxAmount = findViewById(R.id.MultiVendor_TaxAmount);
            Discount = findViewById(R.id.MultiVendor_Discount);
            Discount_head = findViewById(R.id.MultiVendor_Discount_head);
            ShippingAndHandling = findViewById(R.id.MultiVendor_ShippingAndHandling);
            ShippingAndHandling_head = findViewById(R.id.MultiVendor_ShippingAndHandling_head);
            MessageAtLast = findViewById(R.id.MultiVendor_MessageAtLast);

            vendor_id = session.getVendorid();
            final HashMap<String, String> user = session.getUserDetails();
            hashkey = user.get(Ced_MultiVendor_VendorSessionManagement.Key_HAsh);
            postdata.put("hashkey", hashkey);
            postdata.put("vendor_id", vendor_id);
            url = session.getBase_Url() + "vendorapi/vorders/view";
            post_id = getIntent().getStringExtra("order_id");
            postdata.put("order_id", post_id);
            fab.setOnClickListener(v -> {
                AppConstant.lockButton(v);
                Intent create_attribute = new Intent(Ced_MultiVendor_Information_View.this, Ced_MultiVendor_CreateComment.class);
                create_attribute.putExtra("order_id", post_id);
                startActivity(create_attribute);
                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
            });
            fontSetting.setFontforTextviews(orderdate_head, "Roboto-Black.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(order_status_head, "Roboto-Black.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(PurchasedFrom_head, "Roboto-Black.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(CustomerName_head, "Roboto-Black.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(Email_head, "Roboto-Black.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(CustomerGroup_head, "Roboto-Black.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(CreditCardType_head, "Roboto-Black.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(CreditCardNumber_head, "Roboto-Black.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(NameontheCard_head, "Roboto-Black.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(subtotal_head, "Roboto-Black.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(grandtotalearned_head, "Roboto-Black.ttf", getApplicationContext());


            fontSetting.setFontforTextviews(orderDate, "Roboto-Regular.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(OrderStatus, "Roboto-Regular.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(PurchasedFrom, "Roboto-Regular.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(Customer_Name, "Roboto-Regular.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(Email, "Roboto-Regular.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(Customer_Group, "Roboto-Regular.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(billing_ship_to, "Roboto-Regular.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(billing_street, "Roboto-Regular.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(billing_city, "Roboto-Regular.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(billing_state, "Roboto-Regular.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(billing_pincode, "Roboto-Regular.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(billing_country, "Roboto-Regular.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(billing_mobile, "Roboto-Regular.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(shipping_ship_to, "Roboto-Regular.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(shipping_street, "Roboto-Regular.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(shipping_state, "Roboto-Regular.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(shipping_city, "Roboto-Regular.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(shipping_pincode, "Roboto-Regular.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(shipping_country, "Roboto-Regular.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(shipping_phone, "Roboto-Regular.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(PaymentMethod, "Roboto-Regular.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(CreditCardType, "Roboto-Regular.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(CreditCardNumber, "Roboto-Regular.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(NameontheCard, "Roboto-Regular.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(shipping_amount, "Roboto-Regular.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(ShippingMethod, "Roboto-Regular.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(Subtotal, "Roboto-Regular.ttf", getApplicationContext());


            fontSetting.setFontforTextviews(MessageAtLast, "Roboto-Regular.ttf", getApplicationContext());


            fontSetting.setfontforButtons(order_increment_id, "Roboto-Black.ttf", getApplicationContext());
            fontSetting.setfontforButtons(ProductInfo_head, "Roboto-Black.ttf", getApplicationContext());
            fontSetting.setfontforButtons(InvoiceTotals, "Roboto-Black.ttf", getApplicationContext());

            request();
        } else {
            Intent nointernet = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(nointernet);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }

    }

    private void request() {
        Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                if (functionalityList.getExtensionAddon()) {
                    out = output.toString();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //applybarchart();
                            show();
                        }
                    }, 500);
                    orderviewdata();

                } else {
                    Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }
            }
        }, Ced_MultiVendor_Information_View.this, "POST", postdata);
        response.execute(url);
    }

    private void show() {
        mainscroll.scrollTo(0, 0);
        order_increment_id.requestFocus();
    }

    private void orderviewdata() throws JSONException {
        jsonObject = new JSONObject(out);
        if (jsonObject.has("vendor_approved")) {
            logout();
        } else {
            if (jsonObject.getJSONObject("data").getBoolean("success")) {

                invoicedata = jsonObject.getJSONObject("data").getJSONArray("ordersData");
                AddNew_collection = new HashMap<>();
                addnew_comment = new HashMap<>();
                dyanmicinfo = new ArrayList<>();
                dynamic_comment_list = new ArrayList<>();
                for (int i = 0; i < invoicedata.length(); i++) {
                    JSONObject c = null;
                    c = invoicedata.getJSONObject(i);
                    statusLabel = c.getString("statusLabel");
                    storeName = c.getString("storeName");
                    orderStoreDate = c.getString("orderStoreDate");
                    header_meassage = c.getString("header_meassage");
                    CustomerName = c.getString("CustomerName");
                    CustomerEmail = c.getString("CustomerEmail");
                    MessageAtLast.setText(c.getString("message_at_last"));
                    CustomerGroup = c.getString("CustomerGroup");
                    account_data = c.getJSONArray("account_data");

                    if (c.has("marketplace_fees"))
                        MarketFee.setText(c.getString("marketplace_fees"));
                    if (c.has("service_tax"))
                        ServiceTax.setText(c.getString("service_tax"));


                    for (int k = 0; k < account_data.length(); k++) {
                        JSONObject object = null;
                        object = account_data.getJSONObject(k);
                        label = object.getString("label");
                        value = object.getString("value");
                        HashMap hashMap = new HashMap();
                        hashMap.put("label", label);
                        hashMap.put("value", value);
                        dyanmicinfo.add(hashMap);
                        AddNew_collection.put(label + "#" + value, dyanmicinfo);
                    }
                    Iterator iterator = AddNew_collection.entrySet().iterator();
                    while (iterator.hasNext()) {
                        View view2 = View.inflate(this, R.layout.ced_multivendor_dynamic_account_information, null);
                        final TextView textView = view2.findViewById(R.id.MultiVendor_dynamic_head);
                        final TextView textView2 = view2.findViewById(R.id.MultiVendor_dynamic_value);
                        fontSetting.setFontforTextviews(textView, "Roboto-Black.ttf", getApplicationContext());
                        fontSetting.setFontforTextviews(textView2, "Roboto-Regular.ttf", getApplicationContext());
                        Map.Entry entry = (Map.Entry) iterator.next();
                        String key = String.valueOf(entry.getKey());
                        String[] parts = key.split("#");
                        if(parts.length>0)
                        textView.setText(parts[0]);
                        if(parts.length>1)
                        textView2.setText(parts[1]);
                        account_info_dynamic.addView(view2);
                    }
                    if (c.has("name")) {
                        ship_to = c.getString("name");
                        street = c.getString("street");
                        city = c.getString("city");
                        state = c.getString("region");
                        pincode = c.getString("postcode");
                        country = c.getString("country_id");
                        mobile = c.getString("telephone");

                    } else {
                        Shipping_Address_linear.setVisibility(View.GONE);
                    }
                    bill_to = c.getString("bill_name");
                    bill_street = c.getString("bill_street");
                    bill_city = c.getString("bill_city");
                    bill_state = c.getString("bill_region");
                    bill_pincode = c.getString("bill_postcode");
                    bill_country = c.getString("bill_country_id");
                    bill_mobile = c.getString("bill_telephone");
                    payment_method_title = c.getString("payment_method_title");
                    if (c.has("shipping_method")) {
                        shipping_method = c.getString("shipping_method");
                        ShippingMethod.setText(shipping_method);
                        view11.setVisibility(View.VISIBLE);
                        if (c.has("shipping_price")) {
                            shipping_price = c.getString("shipping_price");
                            shipping_amount.setText("Shipping Charges: " + shipping_price);
                        }
                    }

                    comments = c.getJSONArray("comments");
                    for (int l = 0; l < comments.length(); l++) {
                        JSONObject b = null;
                        b = comments.getJSONObject(l);
                        date_time = b.getString("date_time");
                        notified = b.getString("notified");
                        comment = b.getString("comment");
                        HashMap hashMap = new HashMap();
                        hashMap.put("date_time", date_time);
                        hashMap.put("notified", notified);
                        hashMap.put("comment", comment);
                        dynamic_comment_list.add(hashMap);
                        addnew_comment.put(date_time + "#" + notified + "#" + comment, dynamic_comment_list);

                    }
                    Iterator iterator2 = addnew_comment.entrySet().iterator();
                    while (iterator2.hasNext()) {
                        View view2 = View.inflate(this, R.layout.ced_multivendor_dynamic_comment_add, null);
                        final TextView textView = view2.findViewById(R.id.MultiVendor_Date_head);
                        final TextView textView2 = view2.findViewById(R.id.MultiVendor_Date);
                        final TextView textView3 = view2.findViewById(R.id.MultiVendor_Notified_head);
                        final TextView textView4 = view2.findViewById(R.id.MultiVendor_Notified);
                        final TextView textView5 = view2.findViewById(R.id.MultiVendor_Comment_head);
                        final TextView textView6 = view2.findViewById(R.id.MultiVendor_Comment);

                        fontSetting.setFontforTextviews(textView, "Roboto-Medium.ttf", getApplicationContext());
                        fontSetting.setFontforTextviews(textView3, "Roboto-Medium.ttf", getApplicationContext());
                        fontSetting.setFontforTextviews(textView5, "Roboto-Medium.ttf", getApplicationContext());

                        fontSetting.setFontforTextviews(textView2, "Roboto-Regular.ttf", getApplicationContext());
                        fontSetting.setFontforTextviews(textView4, "Roboto-Regular.ttf", getApplicationContext());
                        fontSetting.setFontforTextviews(textView6, "Roboto-Regular.ttf", getApplicationContext());

                        Map.Entry entry = (Map.Entry) iterator2.next();
                        String key = String.valueOf(entry.getKey());
                        String[] parts = key.split("#");

                        if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                            Log.i("REpo", "463_parts" + parts);
                        }
                        if(parts.length>0)
                        textView2.setText(parts[0]);
                        if(parts.length>1)
                        textView4.setText(parts[1]);
                        if(parts.length>2)
                        textView6.setText(parts[2]);

                        dynamic_comment.addView(view2);
                    }
                    cc_type = c.getString("cc_type");
                    if (!cc_type.equals("null")) {
                        CreditCardType_head.setVisibility(View.VISIBLE);
                        CreditCardType.setVisibility(View.VISIBLE);
                        CreditCardNumber_head.setVisibility(View.VISIBLE);
                        CreditCardNumber.setVisibility(View.VISIBLE);
                        NameontheCard_head.setVisibility(View.VISIBLE);
                        NameontheCard.setVisibility(View.VISIBLE);
                        cc_last4 = c.getString("cc_last4");
                        cc_owner = c.getString("cc_owner");
                        CreditCardType.setText(cc_type);
                        CreditCardNumber.setText("XXXX-" + cc_last4);
                        NameontheCard.setText(cc_owner);


                    }
                    message_at_last = c.getString("message_at_last");

                    order_total = c.has("grand_total_earned")?c.getString("grand_total_earned"):"";
                    order_subtotal = c.has("order_subtotal")?c.getString("order_subtotal"):"";
//                    String order_total_str = "0";
//                    String order_commission_str = "0";
//                    if (c.has("order_total"))
//                        order_total_str = c.getString("order_total");
//                    if (c.has("order_commission"))
//                        order_commission_str = c.getString("order_commission");

//                    order_total_str = order_total_str.replace("SAR","");
//                    order_total_str = order_total_str.replace("EJB","");
//                    order_total_str = order_total_str.replace("ر.س.","");
//                    order_total_str = order_total_str.replace("ج.م.","");
//                    order_total_str = order_total_str.replace(" ","");
//                    order_total_str = order_total_str.replace(",","");
//
//                    order_commission_str = order_commission_str.replace("SAR","");
//                    order_commission_str = order_commission_str.replace("EJB","");
//                    order_commission_str = order_commission_str.replace("ر.س.","");
//                    order_commission_str = order_commission_str.replace("ج.م.","");
//                    order_commission_str = order_commission_str.replace(" ","");
//                    order_commission_str = order_commission_str.replace(",","");

                    net_earn = c.getString("net_earn");
                    if(net_earn.contains("SAR"))
                        net_earn = net_earn.replace("SAR","EJB");
                    else if(net_earn.contains("ر.س."))
                        net_earn = net_earn.replace("ر.س.","ج.م.");
//                    try {
//                        order_total_str = order_total_str.replaceAll("[^0-9.]", "");
//                        double order_total_value = Double.parseDouble(order_total_str.replaceAll("[^0-9.]", ""));
//                        double order_commission_value = Double.parseDouble(order_commission_str.replaceAll("[^0-9.]", ""));
//                        order_total =  String.format("%.2f",  order_total_value - order_commission_value);
//                        if(net_earn.contains("EJB"))
//                            order_total = "EJB " + order_total;
//                        else
//                            order_total =   " ج.م. "+order_total  ;
//                    }catch (Exception e){
//                        System.out.println(e.getMessage());
//                    }

                    order_shipping = c.getString("order_shipping");
                    order_tax = c.getString("order_tax");
                    items = c.getJSONArray("items");

                    if (items.length() > 0) {
                        invoice_view_list.setAdapter(new CommanViewAdapter(items, Ced_MultiVendor_Information_View.this));
                    }
                }
//                if(statusLabel.equalsIgnoreCase("تم الإلغاء") || statusLabel.equalsIgnoreCase("Canceled"))
//                    order_total = net_earn;
                OrderStatus.setText(statusLabel);
                PurchasedFrom.setText(storeName);
                orderDate.setText(orderStoreDate);
                Customer_Name.setText(CustomerName);
                Email.setText(CustomerEmail);
                Customer_Group.setText(CustomerGroup);
                billing_ship_to.setText(bill_to);
                billing_street.setText(bill_street);
                billing_city.setText(bill_city);
                billing_state.setText(bill_state);
                billing_pincode.setText(bill_pincode);
                billing_country.setText(bill_country);
                billing_mobile.setText(bill_mobile);
                shipping_ship_to.setText(ship_to);
                shipping_street.setText(street);
                shipping_city.setText(city);
                shipping_state.setText(state);
                shipping_pincode.setText(pincode);
                shipping_country.setText(country);
                shipping_phone.setText(mobile);
                order_increment_id.setText(header_meassage);
                PaymentMethod.setText(payment_method_title);

                if (!order_subtotal.equals("")) {
                    subtotal_head.setVisibility(View.VISIBLE);
                    Subtotal.setVisibility(View.VISIBLE);
                    Subtotal.setText(order_subtotal);
                }
                else {
                    subtotal_head.setVisibility(View.GONE);
                    Subtotal.setVisibility(View.GONE);
                }

                if (!order_total.equals("")) {
                    grandtotalearned.setText(order_total);
                }
                else {
                    grandtotalearned.setVisibility(View.GONE);
                    grandtotalearned_head.setVisibility(View.GONE);
                }

                CommissionFee.setText(order_commission);
                NetEarned.setText(net_earn);
                ShippingAndHandling.setText(order_shipping);
                TaxAmount.setText(order_tax);


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
            changetitle("Order View");
            //  invalidateOptionsMenu();
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
