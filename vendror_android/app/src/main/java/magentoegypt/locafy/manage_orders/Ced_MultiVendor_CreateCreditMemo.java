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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import magentoegypt.locafy.base_app.Ced_MultiVendor_FontSetting;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_UnAuthourizedRequestError;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;
import magentoegypt.locafy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by developer on 28/6/16.
 */
public class Ced_MultiVendor_CreateCreditMemo extends Ced_MultiVendor_NavigationActivity {
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
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    String creditsave_url, url, out = "";
    HashMap<String, String> postdata;
    Ced_MultiVendor_FontSetting fontSetting;
    JSONObject jsonObject;
    String vendor_id, hashkey, post_id;
    JSONArray invoicedata, items, account_data, comments;
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
            RefundShipping_head, TaxAmount, TaxAmount_head;
    Button order_increment_id, ProductInfo_head, refund_offline_button, InvoiceTotals;
    EditText RefundShipping, AdjustmentFee, AdjustmentRefund, add_comment;
    ArrayList<HashMap<String, String>> Invoiceinfo;
    ArrayList<HashMap<String, String>> dyanmicinfo, dynamic_comment_list;
    Ced_MultiVendor_CreateCreditMemoAdapter createCreditMemoAdapter;
    ListView invoice_view_list;
    ScrollView mainscroll;
    Boolean qty_error = false;

    LinkedHashMap<String, ArrayList> add_shiping_items;
    HashMap<String, ArrayList> AddNew_collection, addnew_comment;
    CardView view11;
    LinearLayout account_info_dynamic, dynamic_comment, dynamic_listing, Shipping_Address_linear;
    CheckBox chck_email_copy, chck_append_comments;
    TextView orderTotalInclTax,orderTotalExclusiveTax,subTotalInclTax,subTotalExclusiveTax;
    LinearLayout orderTotalInclTaxLayout,orderTotalExclusiveTaxLayout,subTotalInclTaxLayout,subTotalExclusiveTaxLayout;

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

        if (connectionDetector.isConnectingToInternet()) {
            ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
            getLayoutInflater().inflate(R.layout.ced_multivendor_activity_create_creditmemo_view, content, true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);
            if (session.getvendorname() != null) {
                setname(session.getvendorname());
                setprofilepic(session.getvendorpic());
                changetitle("Create CreditMemo");
            }

            orderTotalInclTax = findViewById(R.id.orderTotalInclTax);
            orderTotalExclusiveTax = findViewById(R.id.orderTotalExclusiveTax);
            subTotalInclTax = findViewById(R.id.subTotalInclTax);
            subTotalExclusiveTax = findViewById(R.id.subTotalExclusiveTax);
            orderTotalInclTaxLayout = findViewById(R.id.orderTotalInclTaxLayout);
            orderTotalExclusiveTaxLayout = findViewById(R.id.orderTotalExclusiveTaxLayout);
            subTotalInclTaxLayout = findViewById(R.id.subTotalInclTaxLayout);
            subTotalExclusiveTaxLayout = findViewById(R.id.subTotalExclusiveTaxLayout);

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

            invoice_view_list = findViewById(R.id.MultiVendor_invoice_view_list);
            order_increment_id = findViewById(R.id.MultiVendor_order_increment_id);
            mainscroll = findViewById(R.id.MultiVendor_mainscroll);
            account_info_dynamic = findViewById(R.id.MultiVendor_account_info_dynamic);
            dynamic_comment = findViewById(R.id.MultiVendor_dynamic_comment);
            dynamic_listing = findViewById(R.id.MultiVendor_dynamic_listing);
            Shipping_Address_linear = findViewById(R.id.Shipping_Address_linear);
            ProductInfo_head = findViewById(R.id.MultiVendor_ProductInfo_head);
            refund_offline_button = findViewById(R.id.MultiVendor_refund_offline_button);
            InvoiceTotals = findViewById(R.id.MultiVendor_InvoiceTotals);
            InvoiceTotals.setText(getResources().getString(R.string.RefundTotal));
            ProductInfo_head.setText(getResources().getString(R.string.ItemstoRefund));
            view11 = findViewById(R.id.MultiVendor_view11);
            Subtotal = findViewById(R.id.MultiVendor_Subtotal);

            grandtotalearned = findViewById(R.id.MultiVendor_grandtotalearned);
            AdjustmentFee_head = findViewById(R.id.MultiVendor_AdjustmentFee_head);
            AdjustmentFee = findViewById(R.id.MultiVendor_AdjustmentFee);
            subtotal_head = findViewById(R.id.MultiVendor_subtotal_head);

            grandtotalearned_head = findViewById(R.id.MultiVendor_grandtotalearned_head);


            AdjustmentRefund_head = findViewById(R.id.MultiVendor_AdjustmentRefund_head);
            AdjustmentRefund = findViewById(R.id.MultiVendor_AdjustmentRefund);
            RefundShipping = findViewById(R.id.MultiVendor_RefundShipping);
            add_comment = findViewById(R.id.MultiVendor_add_comment);
            RefundShipping_head = findViewById(R.id.MultiVendor_RefundShipping_head);
            TaxAmount_head = findViewById(R.id.MultiVendor_TaxAmount_head);
            TaxAmount = findViewById(R.id.MultiVendor_TaxAmount);

            chck_email_copy = findViewById(R.id.MultiVendor_chck_email_copy);
            chck_append_comments = findViewById(R.id.MultiVendor_chck_append_comments);


            fontSetting.setFontforTextviews(orderdate_head, "Roboto-Black.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(order_status_head, "Roboto-Black.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(PurchasedFrom_head, "Roboto-Black.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(CustomerName_head, "Roboto-Black.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(Email_head, "Roboto-Black.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(CustomerGroup_head, "Roboto-Black.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(CreditCardType_head, "Roboto-Black.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(CreditCardNumber_head, "Roboto-Black.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(NameontheCard_head, "Roboto-Black.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(AdjustmentFee_head, "Roboto-Black.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(subtotal_head, "Roboto-Black.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(grandtotalearned_head, "Roboto-Black.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(AdjustmentRefund_head, "Roboto-Black.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(RefundShipping_head, "Roboto-Black.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(TaxAmount_head, "Roboto-Black.ttf", getApplicationContext());

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
            fontSetting.setFontforTextviews(grandtotalearned, "Roboto-Regular.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(TaxAmount, "Roboto-Regular.ttf", getApplicationContext());

            fontSetting.setfontforEditText(AdjustmentFee, "Roboto-Regular.ttf", getApplicationContext());
            fontSetting.setfontforEditText(AdjustmentRefund, "Roboto-Regular.ttf", getApplicationContext());
            fontSetting.setfontforEditText(RefundShipping, "Roboto-Regular.ttf", getApplicationContext());
            fontSetting.setfontforEditText(add_comment, "Roboto-Regular.ttf", getApplicationContext());

            fontSetting.setfontforButtons(order_increment_id, "Roboto-Black.ttf", getApplicationContext());
            fontSetting.setfontforButtons(ProductInfo_head, "Roboto-Black.ttf", getApplicationContext());
            fontSetting.setfontforButtons(refund_offline_button, "Roboto-Black.ttf", getApplicationContext());
            fontSetting.setfontforButtons(InvoiceTotals, "Roboto-Black.ttf", getApplicationContext());

            vendor_id = session.getVendorid();
            final HashMap<String, String> user = session.getUserDetails();
            hashkey = user.get(Ced_MultiVendor_VendorSessionManagement.Key_HAsh);
            postdata.put("hashkey", hashkey);
            postdata.put("vendor_id", vendor_id);
            url = session.getBase_Url() + "vorderapi/vorders/create";
            creditsave_url = session.getBase_Url() + "vorderapi/vcreditmemo/save";
            post_id = getIntent().getStringExtra("order_id");
            postdata.put("type", "creditmemo");
            if (getIntent().getStringExtra("invoice_id") != null) {
                postdata.put("invoice_id", getIntent().getStringExtra("invoice_id"));
            }
            postdata.put("order_id", post_id);
            chck_append_comments.setEnabled(false);
            chck_email_copy.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (chck_email_copy.isChecked()) {
                    chck_append_comments.setEnabled(true);
                    postdata.put("send_email", "true");
                } else {
                    chck_append_comments.setEnabled(false);
                }
            });
            chck_append_comments.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (chck_append_comments.isChecked()) {
                    postdata.put("comment_customer_notify", "true");

                }
            });

            request();
            refund_offline_button.setOnClickListener(v -> {
                postdata.put("comment_text", add_comment.getText().toString());
                postdata.put("shipping_amount", RefundShipping.getText().toString());
                postdata.put("adjustment_positive", AdjustmentRefund.getText().toString());
                postdata.put("adjustment_negative", AdjustmentFee.getText().toString());
                List shipment_data = new ArrayList();
                for (int i = 0; i < dynamic_listing.getChildCount(); i++) {
                    LinearLayout linearLayout = (LinearLayout) dynamic_listing.getChildAt(i);
                    CardView cardView = (CardView) linearLayout.getChildAt(0);
                    LinearLayout linearLayout1 = (LinearLayout) cardView.getChildAt(0);
                    TextView Itemid = (TextView) linearLayout1.getChildAt(12);
                    TextView remaining_quantity = (TextView) linearLayout1.getChildAt(12);
                    LinearLayout linearLayout2 = (LinearLayout) linearLayout1.getChildAt(6);
                    LinearLayout linearLayout3 = (LinearLayout) linearLayout1.getChildAt(4);
                    TextView invoiced_qty = (TextView) linearLayout3.getChildAt(1);
                    EditText quantity = (EditText) linearLayout2.getChildAt(1);
                    CheckBox checkBox = (CheckBox) linearLayout1.getChildAt(11);

                    qty_error = Integer.valueOf(remaining_quantity.getText().toString()) < Integer.valueOf(quantity.getText().toString());
                    JSONObject Shipment = new JSONObject();
                    if (checkBox.isChecked()) {
                        try {
                            Shipment.put("back_to_stock", quantity.getText().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            Shipment.put("back_to_stock", "0");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


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
                    Toast.makeText(getApplicationContext(), "Returned Quantity cannot be greater than Invoiced Quantity", Toast.LENGTH_LONG).show();
                } else {
                    postdata.put("items", shipment_data.toString());
                    create_new();
                }

            });

        } else {

        }


    }

    private void create_new() {
        Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(output -> {
            if (functionalityList.getExtensionAddon()) {
                out = output.toString();
                JSONObject object = new JSONObject(out);
                if (object.getJSONObject("data").getBoolean("success")) {

                    Toast.makeText(getApplicationContext(), object.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
                    Intent back_intent = new Intent(Ced_MultiVendor_CreateCreditMemo.this, Ced_MultiVendor_ManageOrderview.class);
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
        }, Ced_MultiVendor_CreateCreditMemo.this, "POST", postdata);
        response.execute(creditsave_url);
    }

    private void request() {
        Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(output -> {
            if (functionalityList.getExtensionAddon()) {
                out = output.toString();
                new Handler().postDelayed(() -> {
                    //applybarchart();
                    show();
                }, 500);
                createcreditmemoviewdata();

            } else {
                Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
            }
        }, Ced_MultiVendor_CreateCreditMemo.this, "POST", postdata);
        response.execute(url);
    }

    private void createcreditmemoviewdata() throws JSONException {
        jsonObject = new JSONObject(out);
        if (jsonObject.has("vendor_approved")) {
            logout();
        } else {
            if (jsonObject.getJSONObject("data").getBoolean("success")) {

                invoicedata = jsonObject.getJSONObject("data").getJSONArray("ordersData");
                AddNew_collection = new HashMap<>();
                addnew_comment = new HashMap<>();
                add_shiping_items = new LinkedHashMap<>();
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
/*
                    RefundShipping_head.setVisibility(c.has("shipment_handled")&&c.getString("shipment_handled").equals("admin") ? View.VISIBLE : View.GONE);
                    RefundShipping.setVisibility(c.has("shipment_handled") ? View.VISIBLE : View.GONE);*/


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
                        textView.setText(parts[0]);
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

                    order_shipping = c.has("order_shipping_without_currency")?c.getString("order_shipping_without_currency"):c.getString("order_shipping");
//                    order_shipping = c.getString("order_shipping");

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
                        add_shiping_items.put(name + "#" + sku + "#" + price + "#" + qty_ordered + "#" + qty_shipped + "#" + discount_amount + "#" + tax_amount + "#" + row_total + "#" + subtotal + "#" + item_id + "#" + qty_invoiced + "#" + qty_refunded, Invoiceinfo);

                    }
                    Iterator iterator4 = add_shiping_items.entrySet().iterator();
                    while (iterator4.hasNext()) {
                        View view_itemtoship = View.inflate(this, R.layout.ced_multivendor_dynamic_items_to_creditmemo, null);
                        final TextView prod_name = view_itemtoship.findViewById(R.id.MultiVendor_prod_name);
                        final TextView prod_sku = view_itemtoship.findViewById(R.id.MultiVendor_prod_sku);
                        final TextView prod_price = view_itemtoship.findViewById(R.id.MultiVendor_prod_price);
                        final TextView prod_qty_ordered = view_itemtoship.findViewById(R.id.MultiVendor_prod_qty_ordered);
                        final TextView prod_qty_shipped = view_itemtoship.findViewById(R.id.MultiVendor_prod_qty_shipped);
                        final TextView prod_qty_invoiced = view_itemtoship.findViewById(R.id.MultiVendor_prod_qty_invoiced);
                        final TextView Subtotal = view_itemtoship.findViewById(R.id.MultiVendor_Subtotal);
                        final TextView TaxAmount = view_itemtoship.findViewById(R.id.MultiVendor_TaxAmount);
                        final TextView DiscountAmount = view_itemtoship.findViewById(R.id.MultiVendor_DiscountAmount);
                        final TextView RowTotal = view_itemtoship.findViewById(R.id.MultiVendor_RowTotal);
                        final TextView item_id = view_itemtoship.findViewById(R.id.MultiVendor_item_id);
                        final TextView remaining_quantity = view_itemtoship.findViewById(R.id.remaining_quantity);
                        final EditText prod_qty_ship = view_itemtoship.findViewById(R.id.MultiVendor_prod_qty_refund);

                        Map.Entry entry = (Map.Entry) iterator4.next();
                        String key = String.valueOf(entry.getKey());
                        String[] parts = key.split("#");
                        prod_name.setText(parts[0]);
                        prod_sku.setText(parts[1]);
                        prod_price.setText(parts[2]);
                        prod_qty_ordered.setText(parts[3]);
                        prod_qty_shipped.setText(parts[4]);
                        DiscountAmount.setText(parts[5]);
                        TaxAmount.setText(parts[6]);
                        RowTotal.setText(parts[7]);
                        Subtotal.setText(parts[8]);
                        item_id.setText(parts[9]);
                        prod_qty_invoiced.setText(parts[10]);
                        prod_qty_ship.setText(String.valueOf(Integer.valueOf(parts[10]) - Integer.valueOf(parts[11])));
                        remaining_quantity.setText(String.valueOf(Integer.valueOf(parts[10]) - Integer.valueOf(parts[11])));

                        view_itemtoship.setVisibility(View.VISIBLE);
                        if (prod_qty_shipped.getText().toString().equals("0")) {
                            LinearLayout linearLayout = (LinearLayout) prod_qty_shipped.getParent();
                            linearLayout.setVisibility(View.GONE);
                        }
                        dynamic_listing.addView(view_itemtoship);
                    }

                    if(c.has("order_subtotal_excl")){
                        subTotalExclusiveTaxLayout.setVisibility(View.VISIBLE);
                        subTotalExclusiveTax.setText(c.getString("order_subtotal_excl"));
                    }
                    else {
                        subTotalExclusiveTaxLayout.setVisibility(View.GONE);
                    }
                    if(c.has("order_subtotal_incl")){
                        subTotalInclTaxLayout.setVisibility(View.VISIBLE);
                        subTotalInclTax.setText(c.getString("order_subtotal_incl"));
                    }
                    else {
                        subTotalInclTaxLayout.setVisibility(View.GONE);
                    }
                    if(c.has("order_total_excl")){
                        orderTotalExclusiveTaxLayout.setVisibility(View.VISIBLE);
                        orderTotalExclusiveTax.setText(c.getString("order_total_excl"));
                    }
                    else {
                        orderTotalExclusiveTaxLayout.setVisibility(View.GONE);
                    }
                    if(c.has("order_total_incl")){
                        orderTotalInclTaxLayout.setVisibility(View.VISIBLE);
                        orderTotalInclTax.setText(c.getString("order_total_incl"));
                    }
                    else {
                        orderTotalInclTaxLayout.setVisibility(View.GONE);
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

                RefundShipping.setText(order_shipping);
                TaxAmount.setText(order_tax);


            }
            else {

            }
        }
    }

    private void show() {
        mainscroll.scrollTo(0, 0);
        order_increment_id.requestFocus();
    }

    @Override
    protected void onResume() {
        Ced_MultiVendor_ConnectionDetector connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        if (connectionDetector.isConnectingToInternet()) {
            setname(session.getvendorname());
            setprofilepic(session.getvendorpic());
            changetitle("Create CreditMemo");
            //   invalidateOptionsMenu();

        } else {
            Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
