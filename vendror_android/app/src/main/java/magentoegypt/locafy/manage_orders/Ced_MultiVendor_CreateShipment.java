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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.cardview.widget.CardView;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import magentoegypt.locafy.R;
import magentoegypt.locafy.base_app.Ced_MultiVendor_FontSetting;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_UnAuthourizedRequestError;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by developer on 29/6/16.
 */
public class Ced_MultiVendor_CreateShipment extends Ced_MultiVendor_NavigationActivity {
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    String creditsave_url, url, out = "";
    HashMap<String, String> postdata;
    Ced_MultiVendor_FontSetting fontSetting;
    JSONObject jsonObject;
    String vendor_id, hashkey, post_id;
    JSONArray invoicedata, items, account_data, comments, carriers;
    String statusLabel, storeName, orderStoreDate, header_meassage, CustomerName, CustomerEmail,
            CustomerGroup, ship_to, street, city, state, pincode, country, mobile,
            bill_to, bill_street, bill_city, bill_state, bill_pincode, bill_country,
            bill_mobile, payment_method_title, cc_type, cc_last4, cc_owner, message_at_last, order_total, name,
            sku, price, qty_ordered, discount_amount, tax_amount, row_total,
            subtotal, label, value, date_time, notified,
            comment, shipping_method, shipping_price, order_subtotal, order_commission,
            net_earn, qty_canceled, qty_invoiced, qty_shipped, qty_refunded,
            order_shipping, order_tax, item_id;

    TextView orderdate_head, orderDate, order_status_head, OrderStatus,
            PurchasedFrom_head, PurchasedFrom, CustomerName_head, Customer_Name, Email_head, Email,
            CustomerGroup_head, Customer_Group, billing_ship_to,
            billing_street, billing_city, billing_state, billing_pincode,
            billing_country, billing_mobile, shipping_ship_to,
            shipping_street, shipping_city, shipping_state, shipping_pincode, shipping_country, shipping_phone,
            PaymentMethod, CreditCardType_head, CreditCardType,
            CreditCardNumber_head, CreditCardNumber,
            NameontheCard_head, NameontheCard, shipping_amount,
            ShippingMethod, Subtotal, grandtotals, grandtotalearned, AdjustmentFee_head, subtotal_head, grandtotal_head,
            grandtotalearned_head, CommissionFee_head, NetEarned_head,
            AdjustmentRefund_head, Discount, Discount_head,
            RefundShipping_head;
    Button order_increment_id, ProductInfo_head, refund_offline_button, InvoiceTotals;
    EditText RefundShipping, AdjustmentFee, AdjustmentRefund, add_comment;
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
    static final String KEY_ITEM_ID = "item_id";
    static final String KEY_ORDER_ID = "order_id";
    ArrayList<HashMap<String, String>> Invoiceinfo;
    ArrayList<HashMap<String, String>> dyanmicinfo, dynamic_comment_list;
    Ced_MultiVendor_CreateCreditMemoAdapter createCreditMemoAdapter;
    ListView invoice_view_list;
    ScrollView mainscroll;

    Button add_tracking;
    HashMap<String, ArrayList> AddNew_collection, addnew_comment, add_shiping_items;
    CardView view11;
    LinearLayout account_info_dynamic, dynamic_comment, dynamic_listing, dynamic_shipping_carrier, Shipping_Address_linear;
    CheckBox chck_email_copy, chck_append_comments, createshippinglabel;

    List<String> Shippinglabellist;
    List<String> Shippingvaluelist;
    String carrier_label, carrier_value;
    Boolean qty_error = false;

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
        Invoiceinfo = new ArrayList<>();

        Shippinglabellist = new ArrayList<String>();
        Shippingvaluelist = new ArrayList<String>();
        if (connectionDetector.isConnectingToInternet()) {
            ViewGroup content = (ViewGroup) findViewById(R.id.MultiVendor_frame_container);
            getLayoutInflater().inflate(R.layout.ced_multivendor_activity_create_shipment_view, content, true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);
            if (session.getvendorname() != null) {
                setname(session.getvendorname());
                setprofilepic(session.getvendorpic());
                changetitle("Create Shipment");
            }

            orderdate_head = (TextView) findViewById(R.id.MultiVendor_orderdate_head);
            orderDate = (TextView) findViewById(R.id.MultiVendor_orderDate);
            order_status_head = (TextView) findViewById(R.id.MultiVendor_order_status_head);
            OrderStatus = (TextView) findViewById(R.id.MultiVendor_OrderStatuss);
            PurchasedFrom_head = (TextView) findViewById(R.id.MultiVendor_PurchasedFrom_head);
            PurchasedFrom = (TextView) findViewById(R.id.MultiVendor_PurchasedFrom);
            CustomerName_head = (TextView) findViewById(R.id.MultiVendor_CustomerName_head);
            Customer_Name = (TextView) findViewById(R.id.MultiVendor_CustomerName);
            Email_head = (TextView) findViewById(R.id.MultiVendor_Email_head);
            Email = (TextView) findViewById(R.id.MultiVendor_Email);
            CustomerGroup_head = (TextView) findViewById(R.id.MultiVendor_CustomerGroup_head);
            Customer_Group = (TextView) findViewById(R.id.MultiVendor_CustomerGroup);
            billing_ship_to = (TextView) findViewById(R.id.MultiVendor_billing_ship_to);
            billing_street = (TextView) findViewById(R.id.MultiVendor_billing_street);
            billing_city = (TextView) findViewById(R.id.MultiVendor_billing_city);
            billing_state = (TextView) findViewById(R.id.MultiVendor_billing_state);
            billing_pincode = (TextView) findViewById(R.id.MultiVendor_billing_pincode);
            billing_country = (TextView) findViewById(R.id.MultiVendor_billing_country);
            billing_mobile = (TextView) findViewById(R.id.MultiVendor_billing_mobile);
            shipping_ship_to = (TextView) findViewById(R.id.MultiVendor_shipping_ship_to);
            shipping_street = (TextView) findViewById(R.id.MultiVendor_shipping_street);
            shipping_state = (TextView) findViewById(R.id.MultiVendor_shipping_state);
            shipping_city = (TextView) findViewById(R.id.MultiVendor_shipping_city);
            shipping_pincode = (TextView) findViewById(R.id.MultiVendor_shipping_pincode);
            shipping_country = (TextView) findViewById(R.id.MultiVendor_shipping_country);
            shipping_phone = (TextView) findViewById(R.id.MultiVendor_shipping_phone);
            PaymentMethod = (TextView) findViewById(R.id.MultiVendor_PaymentMethod);
            CreditCardType_head = (TextView) findViewById(R.id.MultiVendor_CreditCardType_head);
            CreditCardType = (TextView) findViewById(R.id.MultiVendor_CreditCardType);
            CreditCardNumber_head = (TextView) findViewById(R.id.MultiVendor_CreditCardNumber_head);
            CreditCardNumber = (TextView) findViewById(R.id.MultiVendor_CreditCardNumber);
            NameontheCard_head = (TextView) findViewById(R.id.MultiVendor_NameontheCard_head);
            NameontheCard = (TextView) findViewById(R.id.MultiVendor_NameontheCard);
            shipping_amount = (TextView) findViewById(R.id.MultiVendor_shipping_amount);
            ShippingMethod = (TextView) findViewById(R.id.MultiVendor_ShippingMethod);


            order_increment_id = (Button) findViewById(R.id.MultiVendor_order_increment_id);
            mainscroll = (ScrollView) findViewById(R.id.MultiVendor_mainscroll);
            account_info_dynamic = (LinearLayout) findViewById(R.id.MultiVendor_account_info_dynamic);
            dynamic_comment = (LinearLayout) findViewById(R.id.MultiVendor_dynamic_comment);
            ProductInfo_head = (Button) findViewById(R.id.MultiVendor_ProductInfo_head);
            refund_offline_button = (Button) findViewById(R.id.MultiVendor_refund_offline_button);
            InvoiceTotals = (Button) findViewById(R.id.MultiVendor_InvoiceTotals);
            add_tracking = (Button) findViewById(R.id.MultiVendor_add_tracking);
            ProductInfo_head.setText(getResources().getString(R.string.ItemstoShip));
            view11 = (CardView) findViewById(R.id.MultiVendor_view11);
            Subtotal = (TextView) findViewById(R.id.MultiVendor_Subtotal);

            dynamic_listing = (LinearLayout) findViewById(R.id.MultiVendor_dynamic_listing);
            Shipping_Address_linear = (LinearLayout) findViewById(R.id.Shipping_Address_linear);
            dynamic_shipping_carrier = (LinearLayout) findViewById(R.id.MultiVendor_dynamic_shipping_carrier);
            grandtotalearned = (TextView) findViewById(R.id.MultiVendor_grandtotalearned);
            /*subtotal_head = (TextView) findViewById(R.id.MultiVendor_subtotal_head);*/

            grandtotalearned_head = (TextView) findViewById(R.id.MultiVendor_grandtotalearned_head);

            add_comment = (EditText) findViewById(R.id.MultiVendor_add_comment);
            chck_email_copy = (CheckBox) findViewById(R.id.MultiVendor_chck_email_copy);
            chck_append_comments = (CheckBox) findViewById(R.id.MultiVendor_chck_append_comments);
            createshippinglabel = (CheckBox) findViewById(R.id.MultiVendor_createshippinglabel);


            vendor_id = session.getVendorid();
            final HashMap<String, String> user = session.getUserDetails();
            hashkey = user.get(Ced_MultiVendor_VendorSessionManagement.Key_HAsh);
            postdata.put("hashkey", hashkey);
            postdata.put("vendor_id", vendor_id);
            url = session.getBase_Url()+ "vorderapi/vorders/create";
            creditsave_url = session.getBase_Url()+ "vorderapi/vshipment/save";
            post_id = getIntent().getStringExtra("order_id");
            postdata.put("order_id", post_id);
            postdata.put("type", "shipment");
            chck_append_comments.setEnabled(false);
            chck_email_copy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (chck_email_copy.isChecked()) {
                        chck_append_comments.setEnabled(true);
                        postdata.put("send_email", "true");
                    } else {
                        chck_append_comments.setEnabled(false);
                    }
                }
            });
            chck_append_comments.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (chck_append_comments.isChecked()) {
                        postdata.put("comment_customer_notify", "true");

                    }
                }
            });
            createshippinglabel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (createshippinglabel.isChecked()) {
                        postdata.put("create_shipping_label", "true");
                    }
                }
            });


            request();
            fontSetting.setFontforTextviews(orderdate_head, "Roboto-Black.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(order_status_head, "Roboto-Black.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(PurchasedFrom_head, "Roboto-Black.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(CustomerName_head, "Roboto-Black.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(Email_head, "Roboto-Black.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(CustomerGroup_head, "Roboto-Black.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(CreditCardType_head, "Roboto-Black.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(CreditCardNumber_head, "Roboto-Black.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(NameontheCard_head, "Roboto-Black.ttf", getApplicationContext());
            /*fontSetting.setFontforTextviews(AdjustmentFee_head,"Roboto-Black.ttf",getApplicationContext());*/
            /*fontSetting.setFontforTextviews(subtotal_head,"Roboto-Black.ttf",getApplicationContext());*/
            /*fontSetting.setFontforTextviews(grandtotalearned_head,"Roboto-Black.ttf",getApplicationContext());*/
            /*fontSetting.setFontforTextviews(AdjustmentRefund_head,"Roboto-Black.ttf",getApplicationContext());*/
            /*fontSetting.setFontforTextviews(RefundShipping_head,"Roboto-Black.ttf",getApplicationContext());*/

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
            /*fontSetting.setFontforTextviews(Subtotal,"Roboto-Regular.ttf",getApplicationContext());*/
            /*fontSetting.setFontforTextviews(grandtotalearned,"Roboto-Regular.ttf",getApplicationContext());*/

            /*fontSetting.setfontforEditText(AdjustmentFee, "Roboto-Regular.ttf", getApplicationContext());*/
            /*fontSetting.setfontforEditText(AdjustmentRefund, "Roboto-Regular.ttf", getApplicationContext());*/
            /*fontSetting.setfontforEditText(RefundShipping, "Roboto-Regular.ttf", getApplicationContext());*/
            fontSetting.setfontforEditText(add_comment, "Roboto-Regular.ttf", getApplicationContext());

            fontSetting.setfontforButtons(order_increment_id, "Roboto-Black.ttf", getApplicationContext());
            fontSetting.setfontforButtons(ProductInfo_head, "Roboto-Black.ttf", getApplicationContext());
            fontSetting.setfontforButtons(refund_offline_button, "Roboto-Black.ttf", getApplicationContext());
            fontSetting.setfontforButtons(InvoiceTotals, "Roboto-Black.ttf", getApplicationContext());


        } else {
            Intent nointernet = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(nointernet);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }

    }

    private void create_new() {

        Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                if (functionalityList.getExtensionAddon()) {
                    out = output.toString();
                    JSONObject object = new JSONObject(out);
                    if (object.getJSONObject("data").getBoolean("success")) {

                        Toast.makeText(getApplicationContext(), object.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
                        Intent back_intent = new Intent(Ced_MultiVendor_CreateShipment.this, Ced_MultiVendor_ManageOrderview.class);
                        back_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        back_intent.putExtra("order_id", post_id);
                        startActivity(back_intent);
                        overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
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
        }, Ced_MultiVendor_CreateShipment.this, "POST", postdata);
        response.execute(creditsave_url);
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
                    shipmentviewdata();

                } else {
                    Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }
            }
        }, Ced_MultiVendor_CreateShipment.this, "POST", postdata);
        response.execute(url);
    }

    private void show() {
        mainscroll.scrollTo(0, 0);
        order_increment_id.requestFocus();
    }

    private void shipmentviewdata() throws JSONException {
        jsonObject = new JSONObject(out);
        if (jsonObject.has("vendor_approved")) {
            logout();
        } else {
            if (jsonObject.getJSONObject("data").getBoolean("success")) {

                invoicedata = jsonObject.getJSONObject("data").getJSONArray("ordersData");
                AddNew_collection = new HashMap<>();
                addnew_comment = new HashMap<>();
                add_shiping_items = new HashMap<>();
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
                    CustomerGroup = c.getString("CustomerGroup");
                    account_data = c.getJSONArray("account_data");
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
                        final TextView textView = (TextView) view2.findViewById(R.id.MultiVendor_dynamic_head);
                        final TextView textView2 = (TextView) view2.findViewById(R.id.MultiVendor_dynamic_value);
                        fontSetting.setFontforTextviews(textView, "Roboto-Black.ttf", getApplicationContext());
                        fontSetting.setFontforTextviews(textView2, "Roboto-Regular.ttf", getApplicationContext());
                        Map.Entry entry = (Map.Entry) iterator.next();
                        String key = String.valueOf(entry.getKey());
                        String parts[] = key.split("#");
                        textView.setText(parts[0].toString());
                        textView2.setText(parts[1].toString());
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

                    order_total = c.has("order_total")?c.getString("order_total"):"";
                    order_subtotal = c.has("order_subtotal")?c.getString("order_subtotal"):"";

                    order_commission = c.getString("order_commission");
                    net_earn = c.getString("net_earn");
                    order_shipping = c.getString("order_shipping");
                    if (c.has("carriers")) {
                        carriers = c.getJSONArray("carriers");
                        for (int k = 0; k < carriers.length(); k++) {
                            JSONObject object = null;
                            object = carriers.getJSONObject(k);
                            Shippingvaluelist.add(object.getString("value"));
                            Shippinglabellist.add(object.getString("label"));

                        }


                    }

                    order_tax = c.getString("order_tax");
                    items = c.getJSONArray("items");
                    for (int j = 0; j < items.length(); j++) {
                        JSONObject d = null;
                        d = items.getJSONObject(j);
                        name = d.getString(KEY_NAME);
                        sku = d.getString(KEY_SKU);
                        price = d.getString(KEY_PRICE);
                        qty_ordered = d.getString(KEY_QTY);
                        qty_invoiced = d.getString(KEY_QTY_INVOICED);
                        qty_shipped = d.getString(KEY_QTY_SHIPPED);
                        qty_refunded = d.getString(KEY_QTY_REFUNDED);
                        qty_canceled = d.getString(KEY_QTY_CANCELED);
                        discount_amount = d.getString(KEY_DISCOUNT);
                        tax_amount = d.getString(KEY_TAX_AMOUNT);
                        row_total = d.getString(KEY_ROW_TOTAL);
                        subtotal = d.getString(KEY_SUBTOTAL);
                        item_id = d.getString(KEY_ITEM_ID);
                        HashMap hashMap = new HashMap();
                        hashMap.put(KEY_NAME, name);
                        hashMap.put(KEY_SKU, sku);
                        hashMap.put(KEY_PRICE, price);
                        hashMap.put(KEY_QTY, qty_ordered);
                        hashMap.put(KEY_QTY_INVOICED, qty_invoiced);
                        hashMap.put(KEY_QTY_SHIPPED, qty_shipped);
                        hashMap.put(KEY_QTY_REFUNDED, qty_refunded);
                        hashMap.put(KEY_QTY_CANCELED, qty_canceled);
                        hashMap.put(KEY_DISCOUNT, discount_amount);
                        hashMap.put(KEY_TAX_AMOUNT, tax_amount);
                        hashMap.put(KEY_ROW_TOTAL, row_total);
                        hashMap.put(KEY_SUBTOTAL, subtotal);
                        hashMap.put(KEY_ITEM_ID, item_id);
                        hashMap.put(KEY_ORDER_ID, post_id);

                        Invoiceinfo.add(hashMap);
                        add_shiping_items.put(name + "#" + sku + "#" + qty_invoiced + "#" + item_id + "#" + qty_shipped + "#" + qty_refunded, Invoiceinfo);

                    }
                    Iterator iterator4 = add_shiping_items.entrySet().iterator();
                    while (iterator4.hasNext()) {
                        View view_itemtoship = View.inflate(this, R.layout.ced_multivendor_dynamic_items_to_ship, null);
                        final TextView prod_name = (TextView) view_itemtoship.findViewById(R.id.MultiVendor_prod_name);
                        final TextView prod_sku = (TextView) view_itemtoship.findViewById(R.id.MultiVendor_prod_sku);
                        final TextView QuantityOrdered = (TextView) view_itemtoship.findViewById(R.id.MultiVendor_QuantityOrdered);
                        final TextView item_id = (TextView) view_itemtoship.findViewById(R.id.MultiVendor_item_id);
                        final TextView remaining_quantity = (TextView) view_itemtoship.findViewById(R.id.remaining_quantity);
                        final EditText prod_qty_ship = (EditText) view_itemtoship.findViewById(R.id.MultiVendor_prod_qty_ship);
                        Map.Entry entry = (Map.Entry) iterator4.next();
                        String key = String.valueOf(entry.getKey());
                        String parts[] = key.split("#");
                        prod_name.setText(parts[0].toString());
                        prod_sku.setText(parts[1].toString());
                        QuantityOrdered.setText(parts[2].toString());
                        prod_qty_ship.setText(String.valueOf(Integer.valueOf(parts[2].toString()) - Integer.valueOf(parts[4].toString()) - Integer.valueOf(parts[5].toString())));
                        remaining_quantity.setText(String.valueOf(Integer.valueOf(parts[2].toString()) - Integer.valueOf(parts[4].toString()) - Integer.valueOf(parts[5].toString())));
                        item_id.setText(parts[3].toString());
                        view_itemtoship.setVisibility(View.VISIBLE);
                        dynamic_listing.addView(view_itemtoship);
                    }


                }
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
                add_tracking.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View tracking_view = View.inflate(Ced_MultiVendor_CreateShipment.this, R.layout.ced_multivendor_dynamic_shiping_carrier, null);
                        final EditText Carrier = (EditText) tracking_view.findViewById(R.id.MultiVendor_Carrier);
                        final EditText Title = (EditText) tracking_view.findViewById(R.id.MultiVendor_Title);
                        final EditText Number = (EditText) tracking_view.findViewById(R.id.MultiVendor_Number);
                        final Button delete_tracking = (Button) tracking_view.findViewById(R.id.MultiVendor_delete_tracking);
                        final TextView carrier_values = (TextView) tracking_view.findViewById(R.id.MultiVendor_carrier_value);
                        delete_tracking.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                LinearLayout linearLayout = (LinearLayout) delete_tracking.getParent();
                                dynamic_shipping_carrier.removeView(linearLayout);
                            }
                        });
                        Carrier.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final CharSequence[] arrayOfInt = (CharSequence[]) Shippinglabellist.toArray(new CharSequence[Shippinglabellist.size()]);
                                final CharSequence[] arrayOfInt2 = (CharSequence[]) Shippingvaluelist.toArray(new CharSequence[Shippingvaluelist.size()]);

                                Dialog levelDialog1 = new Dialog(Ced_MultiVendor_CreateShipment.this);
                                final AlertDialog.Builder builder = new AlertDialog.Builder(Ced_MultiVendor_CreateShipment.this);
                                builder.setTitle(Html.fromHtml("<font color='#000000'>Select Your  Shipping Carrier:</font>"));

                                builder.setSingleChoiceItems(arrayOfInt2, -1, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int postion) {
                                                carrier_label = (String) arrayOfInt[postion];
                                                carrier_value = (String) arrayOfInt2[postion];
                                                Carrier.setText(carrier_label);
                                                Title.setText(carrier_label);
                                                carrier_values.setText(carrier_value);
                                                dialog.dismiss();

                                            }


                                        }

                                );
                                levelDialog1 = builder.create();
                                levelDialog1.show();
                            }
                        });


                        dynamic_shipping_carrier.addView(tracking_view);
                    }
                });

                refund_offline_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        postdata.put("comment_text", add_comment.getText().toString());
                        List req = new ArrayList();
                        List shipment_data = new ArrayList();


                        for (int i = 0; i < dynamic_shipping_carrier.getChildCount(); i++) {
                            LinearLayout linearLayout = (LinearLayout) dynamic_shipping_carrier.getChildAt(i);
                            LinearLayout linearLayout1 = (LinearLayout) linearLayout.getChildAt(0);
                            TextView Carrier = (TextView) linearLayout.getChildAt(5);
                            LinearLayout linearLayout2 = (LinearLayout) linearLayout.getChildAt(1);
                            EditText Title = (EditText) linearLayout2.getChildAt(1);
                            LinearLayout linearLayout3 = (LinearLayout) linearLayout.getChildAt(2);
                            EditText Number = (EditText) linearLayout3.getChildAt(1);

                            try {
                                JSONObject ShippingCarriers = new JSONObject();
                                ShippingCarriers.put("carrier_code", Carrier.getText().toString());
                                ShippingCarriers.put("title", Title.getText().toString());
                                ShippingCarriers.put("number", Number.getText().toString());
                                req.add(ShippingCarriers);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        postdata.put("tracking", req.toString());
                        if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                            Log.i("ShippingCarriers", req.toString());
                        }
                        for (int j = 0; j < dynamic_listing.getChildCount(); j++) {
                            LinearLayout linearLayout = (LinearLayout) dynamic_listing.getChildAt(j);
                            CardView cardView = (CardView) linearLayout.getChildAt(0);
                            LinearLayout linearLayout1 = (LinearLayout) cardView.getChildAt(0);
                            TextView Itemid = (TextView) linearLayout1.getChildAt(6);
                            TextView remaining = (TextView) linearLayout1.getChildAt(8);
                            LinearLayout linearLayout2 = (LinearLayout) linearLayout1.getChildAt(3);
                            EditText quantity = (EditText) linearLayout2.getChildAt(1);
                            LinearLayout linearLayout3 = (LinearLayout) linearLayout1.getChildAt(2);
                            TextView qty_ordered = (TextView) linearLayout3.getChildAt(1);
                            Log.i("qty_ordered", qty_ordered.getText().toString());
                            if (Integer.valueOf(remaining.getText().toString()) < Integer.valueOf(quantity.getText().toString())) {
                                qty_error = true;

                            } else {
                                qty_error = false;
                            }
                            JSONObject Shipment = new JSONObject();
                            try {
                                if (!quantity.getText().toString().equalsIgnoreCase("0")) {
                                    Shipment.put("item_id", Itemid.getText().toString());
                                    Shipment.put("quantity", quantity.getText().toString());
                                    shipment_data.add(Shipment);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        if (qty_error) {
                            Toast.makeText(getApplicationContext(), "Shipping Quantity cannot be greater than Ordered Quantity", Toast.LENGTH_LONG).show();
                        } else {
                            postdata.put("items", shipment_data.toString());
                            create_new();
                        }


                    }
                });

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
            changetitle("Create Shipment");
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
    protected void onPause() {
        super.onPause();

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
