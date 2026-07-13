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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by developer on 3/5/16.
 */
public class Ced_MultiVendor_Orderview extends Ced_MultiVendor_NavigationActivity {
    static final String KEY_PRODUCT_ID = "product_id";
    static final String KEY_PRODUCT_NAME = "product_name";
    static final String KEY_PRODUCT_PRICE = "product_price";
    static final String KEY_PRODUCT_QTY = "product_qty";
    static final String KEY_PRODUCT_SKU = "product_sku";
    static final String KEY_ROW_SUB_TOTAL = "rowsubtotal";
    static final String KEY_TAX_AMOUNT = "tax_amount";
    static final String KEY_DISCOUNT_AMOUNT = "discount_amount";
    static final String KEY_ROW_TOTAL = "row_total";
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    String url = "";
    Button order_increment_id, order_info_head, ShippingAddress_head, BillingAddress_head,
            ShippingMethod_head, PaymentMethod_head;
    TextView orderdate_head, orderDate, order_status_head, OrderStatus, PurchasedFrom_head,
            PurchasedFrom, billing_ship_to, billing_street, billing_city, billing_state,
            billing_pincode, billing_country, billing_mobile, shipping_ship_to,
            shipping_street, shipping_state, shipping_city, shipping_pincode,
            shipping_country, shipping_phone, TaxAmount_head, TaxAmount,
            Discount, Discount_head, NameontheCard, NameontheCard_head, CreditCardNumber,
            CreditCardNumber_head, CreditCardType, CreditCardType_head, ShippingMethod,
            PaymentMethod, Subtotal, grandtotals, grandtotalearned, CommissionFee, NetEarned,
            subtotal_head, grandtotal_head, grandtotalearned_head, CommissionFee_head, NetEarned_head;
    String out, post_id, product_id, product_name, product_price, product_qty;
    HashMap<String, String> postdata;
    JSONObject jsonObject;
    JSONArray ordetail, ordered_items;
    ArrayList<HashMap<String, String>> Orderinfo;
    Ced_MultiVendor_OrderViewAdapter orderViewAdapter;
    Ced_MultiVendor_FontSetting fontSetting;
    ListView order_view_list;
    ScrollView mainscroll;
    String hashkey;

    String orderdate, order_status, ship_to, street, city, state, pincode, country, mobile, shipping_method,
            bill_to, bill_street, bill_city, bill_state, bill_pincode, bill_country, bill_mobile,
            method_code, method_title, credit_card_type, credit_card_number, name_on_card,
            grandtotal_earned, commision_fee, net_earned, subtotal, shipping, tax_amount,
            discount, grandtotal, ordertitle, purchasedfrom, vendor_id, product_sku, item_subtotal, item_tax_amount, item_discount, rowtotal;

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
        Orderinfo = new ArrayList<>();
        postdata = new HashMap<>();
        if (connectionDetector.isConnectingToInternet()) {
            ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
            getLayoutInflater().inflate(R.layout.ced_multivendor_activity_order_view, content, true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);
            if (session.getvendorname() != null) {
                setname(session.getvendorname());
                setprofilepic(session.getvendorpic());
                changetitle("Order View");
            }
            vendor_id = session.getVendorid();
            final HashMap<String, String> user = session.getUserDetails();
            hashkey = user.get(Ced_MultiVendor_VendorSessionManagement.Key_HAsh);
            postdata.put("hashkey", hashkey);
            postdata.put("vendor_id", vendor_id);
            url = session.getBase_Url() + "vendorapi/vorders/info";
            //  url = session.getBase_Url() + "vorderapi/vorders/viewinitOrder";
            post_id = getIntent().getStringExtra("order_id");
            postdata.put("order_id", post_id);
            mainscroll = findViewById(R.id.MultiVendor_mainscroll);
            order_view_list = findViewById(R.id.MultiVendor_order_view_list);
            order_increment_id = findViewById(R.id.MultiVendor_order_increment_id);
            order_info_head = findViewById(R.id.MultiVendor_order_info_head);
            ShippingAddress_head = findViewById(R.id.MultiVendor_ShippingAddress_head);
            BillingAddress_head = findViewById(R.id.MultiVendor_BillingAddress_head);
            ShippingMethod_head = findViewById(R.id.MultiVendor_ShippingMethod_head);
            PaymentMethod_head = findViewById(R.id.MultiVendor_PaymentMethod_head);
            orderdate_head = findViewById(R.id.MultiVendor_orderdate_head);
            orderDate = findViewById(R.id.MultiVendor_orderDate);
            order_status_head = findViewById(R.id.MultiVendor_order_status_head);
            OrderStatus = findViewById(R.id.MultiVendor_OrderStatus);
            PurchasedFrom_head = findViewById(R.id.MultiVendor_PurchasedFrom_head);
            PurchasedFrom = findViewById(R.id.MultiVendor_PurchasedFrom);
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
            TaxAmount_head = findViewById(R.id.MultiVendor_TaxAmount_head);
            TaxAmount = findViewById(R.id.MultiVendor_TaxAmount);
            Discount = findViewById(R.id.MultiVendor_Discount);
            Discount_head = findViewById(R.id.MultiVendor_Discount_head);
            NameontheCard = findViewById(R.id.MultiVendor_NameontheCard);
            NameontheCard_head = findViewById(R.id.MultiVendor_NameontheCard_head);
            CreditCardNumber = findViewById(R.id.MultiVendor_CreditCardNumber);
            CreditCardNumber_head = findViewById(R.id.MultiVendor_CreditCardNumber_head);
            CreditCardType = findViewById(R.id.MultiVendor_CreditCardType);
            CreditCardType_head = findViewById(R.id.MultiVendor_CreditCardType_head);
            ShippingMethod = findViewById(R.id.MultiVendor_ShippingMethod);
            PaymentMethod = findViewById(R.id.MultiVendor_PaymentMethod);
            Subtotal = findViewById(R.id.MultiVendor_Subtotal);
            grandtotals = findViewById(R.id.MultiVendor_grandtotal);
            grandtotalearned = findViewById(R.id.MultiVendor_grandtotalearned);
            CommissionFee = findViewById(R.id.MultiVendor_CommissionFee);
            NetEarned = findViewById(R.id.MultiVendor_NetEarned);
            subtotal_head = findViewById(R.id.MultiVendor_subtotal_head);
            grandtotal_head = findViewById(R.id.MultiVendor_grandtotal_head);
            grandtotalearned_head = findViewById(R.id.MultiVendor_grandtotalearned_head);
            CommissionFee_head = findViewById(R.id.MultiVendor_CommissionFee_head);
            NetEarned_head = findViewById(R.id.MultiVendor_NetEarned_head);

            fontSetting.setfontforButtons(order_increment_id, "Roboto-Bold.ttf", getApplicationContext());
            fontSetting.setfontforButtons(order_info_head, "Roboto-Bold.ttf", getApplicationContext());
            fontSetting.setfontforButtons(ShippingAddress_head, "Roboto-Bold.ttf", getApplicationContext());
            fontSetting.setfontforButtons(BillingAddress_head, "Roboto-Bold.ttf", getApplicationContext());
            fontSetting.setfontforButtons(ShippingMethod_head, "Roboto-Bold.ttf", getApplicationContext());
            fontSetting.setfontforButtons(PaymentMethod_head, "Roboto-Bold.ttf", getApplicationContext());

            fontSetting.setFontforTextviews(orderdate_head, "Roboto-Medium.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(order_status_head, "Roboto-Medium.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(PurchasedFrom_head, "Roboto-Medium.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(TaxAmount_head, "Roboto-Medium.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(Discount_head, "Roboto-Medium.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(NameontheCard_head, "Roboto-Medium.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(CreditCardNumber_head, "Roboto-Medium.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(CreditCardType_head, "Roboto-Medium.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(subtotal_head, "Roboto-Medium.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(grandtotal_head, "Roboto-Medium.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(grandtotalearned_head, "Roboto-Black.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(CommissionFee_head, "Roboto-Black.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(NetEarned_head, "Roboto-Black.ttf", getApplicationContext());

            fontSetting.setFontforTextviews(orderDate, "Roboto-Regular.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(OrderStatus, "Roboto-Regular.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(PurchasedFrom, "Roboto-Regular.ttf", getApplicationContext());
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
            fontSetting.setFontforTextviews(TaxAmount, "Roboto-Regular.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(Discount, "Roboto-Regular.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(NameontheCard, "Roboto-Regular.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(CreditCardNumber, "Roboto-Regular.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(CreditCardType, "Roboto-Regular.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(ShippingMethod, "Roboto-Regular.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(PaymentMethod, "Roboto-Regular.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(Subtotal, "Roboto-Regular.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(grandtotals, "Roboto-Regular.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(grandtotalearned, "Roboto-Black.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(CommissionFee, "Roboto-Black.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(NetEarned, "Roboto-Black.ttf", getApplicationContext());
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


        }, Ced_MultiVendor_Orderview.this, "POST", postdata);
        response.execute(url);
    }

    private void orderviewdata() {
        try {
            jsonObject = new JSONObject(out);
            if (jsonObject.has("vendor_approved")) {
                logout();
            } else {
                if (jsonObject.getJSONObject("data").getBoolean("success")) {
                    ordetail = jsonObject.getJSONObject("data").getJSONArray("ordetail");
                    for (int i = 0; i < ordetail.length(); i++) {
                        JSONObject c = null;
                        c = ordetail.getJSONObject(i);
                        ordertitle = c.getString("ordertitle");
                        purchasedfrom = c.getString("purchasedfrom");
                        orderdate = c.getString("orderdate");
                        order_status = c.getString("order_status");

                        if (c.has("ship_to")) {
                            ship_to = c.getString("ship_to");
                        }
                        if (c.has("street"))
                            street = c.getString("street");

                        if (c.has("city"))
                            city = c.getString("city");

                        if (c.has("state"))
                            state = c.getString("state");

                        if (c.has("pincode"))
                            pincode = c.getString("pincode");

                        if (c.has("country"))
                            country = c.getString("country");
                        if (c.has("mobile"))
                            mobile = c.getString("mobile");
                        if (c.has("mobile"))
                            shipping_method = c.getString("shipping_method");
                        if (c.has("bill_to"))
                            bill_to = c.getString("bill_to");
                        if (c.has("bill_street"))
                            bill_street = c.getString("bill_street");
                        if (c.has("bill_city"))
                            bill_city = c.getString("bill_city");
                        if (c.has("bill_state"))
                            bill_state = c.getString("bill_state");
                        bill_pincode = c.getString("bill_pincode");
                        bill_country = c.getString("bill_country");
                        bill_mobile = c.getString("bill_mobile");
                        method_code = c.getString("method_code");
                        method_title = c.getString("method_title");
                        credit_card_type = c.getString("credit_card_type");
                        if (!credit_card_type.equals("null")) {
                            credit_card_number = c.getString("credit_card_number");
                            name_on_card = c.getString("name_on_card");
                            CreditCardType_head.setVisibility(View.VISIBLE);
                            CreditCardType.setVisibility(View.VISIBLE);
                            CreditCardNumber_head.setVisibility(View.VISIBLE);
                            CreditCardNumber.setVisibility(View.VISIBLE);
                            NameontheCard_head.setVisibility(View.VISIBLE);
                            NameontheCard.setVisibility(View.VISIBLE);
                            CreditCardType.setText(credit_card_type);
                            CreditCardNumber.setText(credit_card_number);
                            NameontheCard.setText(name_on_card);
                        }
                        ordered_items = c.getJSONArray("ordered_items");
                        for (int j = 0; j < ordered_items.length(); j++) {
                            JSONObject d = null;
                            d = ordered_items.getJSONObject(j);
                            product_id = d.getString(KEY_PRODUCT_ID);
                            product_name = d.getString(KEY_PRODUCT_NAME);
                            product_price = d.getString(KEY_PRODUCT_PRICE);
                            product_qty = d.getString(KEY_PRODUCT_QTY);
                            product_sku = d.getString(KEY_PRODUCT_SKU);
                            item_subtotal = d.getString(KEY_ROW_SUB_TOTAL);
                            item_tax_amount = d.getString(KEY_TAX_AMOUNT);
                            item_discount = d.getString(KEY_DISCOUNT_AMOUNT);
                            rowtotal = d.getString(KEY_ROW_TOTAL);
                            HashMap<String, String> order = new HashMap<String, String>();
                            order.put(KEY_PRODUCT_ID, product_id);
                            order.put(KEY_PRODUCT_NAME, product_name);
                            order.put(KEY_PRODUCT_PRICE, product_price);
                            order.put(KEY_PRODUCT_QTY, product_qty);
                            order.put(KEY_PRODUCT_SKU, product_sku);
                            order.put(KEY_ROW_SUB_TOTAL, item_subtotal);
                            order.put(KEY_TAX_AMOUNT, item_tax_amount);
                            order.put(KEY_DISCOUNT_AMOUNT, item_discount);
                            order.put(KEY_ROW_TOTAL, rowtotal);
                            Orderinfo.add(order);


                        }
                        grandtotal_earned = c.getString("grandtotal_earned");
                        commision_fee = c.getString("commision_fee");
                        net_earned = c.getString("net_earned");
                        subtotal = c.getString("subtotal");
                        shipping = c.getString("shipping");
                        tax_amount = c.getString("tax_amount");
                        discount = c.getString("discount");
                        grandtotal = c.getString("grandtotal");


                    }
                    orderViewAdapter = new Ced_MultiVendor_OrderViewAdapter(Ced_MultiVendor_Orderview.this, Orderinfo);
                    order_view_list.setAdapter(orderViewAdapter);
                    setListViewHeightBasedOnChildren(order_view_list);
                    order_increment_id.setText(ordertitle);
                    orderDate.setText(orderdate);
                    OrderStatus.setText(order_status);
                    PurchasedFrom.setText(purchasedfrom);
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
                    ShippingMethod.setText(shipping_method);
                    PaymentMethod.setText(method_title);
                    TaxAmount.setText(tax_amount);
                    Discount.setText(discount);
                    Subtotal.setText(subtotal);
                    grandtotals.setText(grandtotal_earned);
                    grandtotalearned.setText(grandtotal_earned);
                    CommissionFee.setText(commision_fee);
                    NetEarned.setText(net_earned);

                } else {
                    if (jsonObject.getJSONObject("data").getString("message").equals("This Order doesnt belong to you.Please login with appropriate vendor to view order details")) {
                        Toast.makeText(getApplicationContext(), jsonObject.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
                        logout();

                    }

                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void show() {
        mainscroll.scrollTo(0, 0);
        order_increment_id.requestFocus();

    }

    @Override
    protected void onResume() {
        Ced_MultiVendor_ConnectionDetector connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        if (connectionDetector.isConnectingToInternet()) {
            setname(session.getvendorname());
            setprofilepic(session.getvendorpic());
            changetitle("Order View");
            //    invalidateOptionsMenu();
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
