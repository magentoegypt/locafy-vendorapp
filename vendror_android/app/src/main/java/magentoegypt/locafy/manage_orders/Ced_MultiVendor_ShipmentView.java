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
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import magentoegypt.locafy.base_app.Ced_MultiVendor_FontSetting;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_UnAuthourizedRequestError;
import magentoegypt.locafy.manage_orders.order_view_comman_adapter.CommanViewAdapter;
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
import java.util.List;
import java.util.Map;

/**
 * Created by developer on 25/6/16.
 */

public class Ced_MultiVendor_ShipmentView extends Ced_MultiVendor_NavigationActivity {


    static final String KEY_NAME = "name";
    static final String KEY_SKU = "sku";
    static final String KEY_QTY = "qty_shipped";
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    String url, out, addShipmentTracking, removeShipmentTracking = "";
    HashMap<String, String> postdata;
    Ced_MultiVendor_FontSetting fontSetting;
    JSONObject jsonObject;
    String vendor_id, hashkey, post_id;
    JSONArray invoicedata, items, account_data, comments, carriers, shipment_tracks;
    String statusLabel, storeName, orderStoreDate, header_meassage, CustomerName, CustomerEmail,
            CustomerGroup, ship_to, street, city, state, pincode, country, mobile,
            bill_to, bill_street, bill_city, bill_state, bill_pincode, bill_country,
            bill_mobile, payment_method_title, cc_type, cc_last4, cc_owner, message_at_last, order_total, name,
            sku, price, qty_ordered, discount_amount, tax_amount,
            row_total, subtotal, label, value, date_time, notified,
            comment, shipping_method, shipping_price, carrier_front, title_front, number_front, track_id;
    String carrier_label, carrier_value = "";
    TextView orderdate_head, orderDate, order_status_head, OrderStatus,
            PurchasedFrom_head, PurchasedFrom, CustomerName_head, Customer_Name, Email_head, Email,
            CustomerGroup_head, Customer_Group, billing_ship_to,
            billing_street, billing_city, billing_state, billing_pincode,
            billing_country, billing_mobile, shipping_ship_to,
            shipping_street, shipping_city, shipping_state, shipping_pincode, shipping_country, shipping_phone,
            PaymentMethod, CreditCardType_head, CreditCardType,
            CreditCardNumber_head, CreditCardNumber,
            NameontheCard_head, NameontheCard, shipping_amount,
            ShippingMethod, Carrier_Head, Title_Head, Number_Head, MessageAtLast;
    EditText Carrier, Number, Title;
    Button order_increment_id, ProductInfo_head, add_tracking;
    ArrayList<HashMap<String, String>> Invoiceinfo;
    ArrayList<HashMap<String, String>> dyanmicinfo, dynamic_comment_list, shipment_inform;
    Ced_MultiVendor_ShipmentViewAdapter shipmentViewAdapter;
    RecyclerView invoice_view_list;
    HashMap<String, ArrayList> AddNew_collection, addnew_comment, add_new_ship;

    NestedScrollView mainscroll;
    FloatingActionButton fab;
    CardView view7, view11;
    LinearLayout account_info_dynamic, dynamic_comment, add_shipment_carrier, shipment_trackings, Shipping_Address_linear;

    List<String> Shippinglabellist;
    List<String> Shippingvaluelist;

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
            ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
            getLayoutInflater().inflate(R.layout.ced_multivendor_activity_invoice_view, content, true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);

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
            Carrier_Head = findViewById(R.id.MultiVendor_Carrier_Head);
            Title_Head = findViewById(R.id.MultiVendor_Title_Head);
            Number_Head = findViewById(R.id.MultiVendor_Number_Head);
            MessageAtLast = findViewById(R.id.MultiVendor_MessageAtLast);


            invoice_view_list = findViewById(R.id.MultiVendor_invoice_view_list);
            invoice_view_list.setLayoutManager(new LinearLayoutManager(this));
            order_increment_id = findViewById(R.id.MultiVendor_order_increment_id);
            mainscroll = findViewById(R.id.MultiVendor_mainscroll);
            account_info_dynamic = findViewById(R.id.MultiVendor_account_info_dynamic);
            Shipping_Address_linear = findViewById(R.id.Shipping_Address_linear);
            add_shipment_carrier = findViewById(R.id.MultiVendor_add_shipment_carrier);
            shipment_trackings = findViewById(R.id.MultiVendor_shipment_trackings);
            view7 = findViewById(R.id.MultiVendor_view7);
            view11 = findViewById(R.id.MultiVendor_view11);
            dynamic_comment = findViewById(R.id.MultiVendor_dynamic_comment);

            ProductInfo_head = findViewById(R.id.MultiVendor_ProductInfo_head);
            ProductInfo_head.setText(getResources().getString(R.string.ItemsShipped));

            Carrier = findViewById(R.id.MultiVendor_Carrier);
            Number = findViewById(R.id.MultiVendor_Number);
            Title = findViewById(R.id.MultiVendor_Title);
            add_tracking = findViewById(R.id.MultiVendor_add_tracking);
            view7.setVisibility(View.GONE);

            if (session.getvendorname() != null) {
                setname(session.getvendorname());
                setprofilepic(session.getvendorpic());
                changetitle("Shipment View");
            }
            vendor_id = session.getVendorid();
            final HashMap<String, String> user = session.getUserDetails();
            hashkey = user.get(Ced_MultiVendor_VendorSessionManagement.Key_HAsh);
            postdata.put("hashkey", hashkey);
            postdata.put("vendor_id", vendor_id);
            url = session.getBase_Url() + "vorderapi/vshipment/view";
            addShipmentTracking = session.getBase_Url() + "vorderapi/vshipment/addShipmentTracking";
            removeShipmentTracking = session.getBase_Url() + "vorderapi/vshipment/removeShipmentTracking";
            post_id = getIntent().getStringExtra("shipment_id");
            postdata.put("shipment_id", post_id);
            fab = findViewById(R.id.MultiVendor_fab);
            fab.setOnClickListener(v -> {
                AppConstant.lockButton(v);
                Intent create_attribute = new Intent(Ced_MultiVendor_ShipmentView.this, Ced_MultiVendor_CreateComment.class);
                create_attribute.putExtra("shipment_id", post_id);
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


            fontSetting.setFontforTextviews(MessageAtLast, "Roboto-Regular.ttf", getApplicationContext());


            fontSetting.setfontforButtons(order_increment_id, "Roboto-Black.ttf", getApplicationContext());
            fontSetting.setfontforButtons(ProductInfo_head, "Roboto-Black.ttf", getApplicationContext());

            request();

        }
        else {
            Intent nointernet = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(nointernet);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }

    }

    private void request() {
        Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(output -> {
            if (functionalityList.getExtensionAddon()) {
                out = output.toString();
                new Handler().postDelayed(() -> {
                    //applybarchart();
                    show();
                }, 500);

                shipmentviewdata();
            }
            else {
                Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
            }
        }, Ced_MultiVendor_ShipmentView.this, "POST", postdata);
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
                AddNew_collection = new HashMap<>();
                addnew_comment = new HashMap<>();
                add_new_ship = new HashMap<>();
                dyanmicinfo = new ArrayList<>();
                dynamic_comment_list = new ArrayList<>();
                shipment_inform = new ArrayList<>();
                invoicedata = jsonObject.getJSONObject("data").getJSONArray("shipmentData");
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
                    MessageAtLast.setText(c.getString("message_at_last"));
                    if (c.has("account_data")) {
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
                        if (c.has("carriers")) {
                            add_shipment_carrier.setVisibility(View.VISIBLE);
                            carriers = c.getJSONArray("carriers");
                            for (int m = 0; m < carriers.length(); m++) {
                                JSONObject object = null;
                                object = carriers.getJSONObject(m);
                                Shippingvaluelist.add(object.getString("value"));
                                Shippinglabellist.add(object.getString("label"));

                            }
                        }
                        if (c.has("shipment_tracks")) {
                            shipment_tracks = c.getJSONArray("shipment_tracks");
                            for (int n = 0; n < shipment_tracks.length(); n++) {
                                JSONObject object = null;
                                object = shipment_tracks.getJSONObject(n);
                                carrier_front = object.getString("carrier");
                                title_front = object.getString("title");
                                number_front = object.getString("number");
                                track_id = object.getString("track_id");
                                HashMap hashMap = new HashMap();
                                hashMap.put("carrier", carrier_front);
                                hashMap.put("title", title_front);
                                hashMap.put("number", number_front);
                                hashMap.put("track_id", track_id);
                                shipment_inform.add(hashMap);
                                add_new_ship.put(carrier_front + "#" + title_front + "#" + number_front + "#" + track_id, shipment_inform);


                            }
                            Iterator iterator3 = add_new_ship.entrySet().iterator();
                            while (iterator3.hasNext()) {
                                View view3 = View.inflate(this, R.layout.ced_multivendor_dynamic_shipment_views, null);
                                final TextView carrier_head = view3.findViewById(R.id.MultiVendor_carrier_head);
                                final TextView carrier_front = view3.findViewById(R.id.MultiVendor_carrier_front);
                                final TextView title_head = view3.findViewById(R.id.MultiVendor_title_head);
                                final TextView title_front = view3.findViewById(R.id.MultiVendor_title_front);
                                final TextView number_head = view3.findViewById(R.id.MultiVendor_number_head);
                                final TextView number_front = view3.findViewById(R.id.MultiVendor_number_front);
                                final TextView track_id = view3.findViewById(R.id.MultiVendor_track_id);


                                final Button delete_shipment_carrier = view3.findViewById(R.id.MultiVendor_delete_shipment_carrier);
                                delete_shipment_carrier.setOnClickListener(v -> {
                                    AppConstant.lockButton(v);
                                    postdata.put("track_id", track_id.getText().toString());
                                    removeshipment();
                                });

                                fontSetting.setFontforTextviews(carrier_head, "Roboto-Black.ttf", getApplicationContext());
                                fontSetting.setFontforTextviews(title_head, "Roboto-Black.ttf", getApplicationContext());
                                fontSetting.setFontforTextviews(number_head, "Roboto-Black.ttf", getApplicationContext());
                                fontSetting.setFontforTextviews(carrier_front, "Roboto-Regular.ttf", getApplicationContext());
                                fontSetting.setFontforTextviews(title_front, "Roboto-Regular.ttf", getApplicationContext());
                                fontSetting.setFontforTextviews(number_front, "Roboto-Regular.ttf", getApplicationContext());

                                Map.Entry entry = (Map.Entry) iterator3.next();
                                String key = String.valueOf(entry.getKey());
                                String[] parts = key.split("#");
                                if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                    Log.i("parts", parts[2]);
                                }
                                carrier_front.setText(parts[0]);
                                title_front.setText(parts[1]);
                                number_front.setText(parts[2]);
                                track_id.setText(parts[3]);


                                shipment_trackings.addView(view3);
                            }

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
                        Log.i("parts", parts[2]);
                        textView2.setText(parts[0]);
                        textView4.setText(parts[1]);
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

                    items = c.getJSONArray("items");
                    if (items.length() > 0) {
                        invoice_view_list.setAdapter(new CommanViewAdapter(items, Ced_MultiVendor_ShipmentView.this));
                        invoice_view_list.setNestedScrollingEnabled(false);
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
                order_increment_id.setText(getString(R.string.shipment)+post_id+"\n"+header_meassage);
                PaymentMethod.setText(payment_method_title);
                Carrier.setOnClickListener(v -> {
                    AppConstant.lockButton(v);
                    final CharSequence[] arrayOfInt = Shippinglabellist.toArray(new CharSequence[Shippinglabellist.size()]);
                    final CharSequence[] arrayOfInt2 = Shippingvaluelist.toArray(new CharSequence[Shippingvaluelist.size()]);

                    Dialog levelDialog1 = new Dialog(Ced_MultiVendor_ShipmentView.this);
                    final AlertDialog.Builder builder = new AlertDialog.Builder(Ced_MultiVendor_ShipmentView.this);
                    builder.setTitle(Html.fromHtml("<font color='#000000'>Select Your  Shipping Carrier:</font>"));

                    builder.setSingleChoiceItems(arrayOfInt2, -1, (dialog, postion) -> {
                                carrier_label = (String) arrayOfInt[postion];
                                carrier_value = (String) arrayOfInt2[postion];
                                Carrier.setText(carrier_label);
                                Title.setText(carrier_label);
                                dialog.dismiss();

                            }

                    );
                    levelDialog1 = builder.create();
                    levelDialog1.show();

                });
                add_tracking.setOnClickListener(v -> {
                    AppConstant.lockButton(v);
                    if (Number.getText().toString().isEmpty()) {
                        Number.setError(getResources().getString(R.string.TrackingNocannotbeEmpty));
                        Number.requestFocus();

                    } else {
                        postdata.put("number", Number.getText().toString());
                        postdata.put("title", Title.getText().toString());
                        postdata.put("carrier", carrier_value);
                        addshipment();
                    }
                });


            } else {

            }
        }
    }

    private void removeshipment() {
        Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(output -> {
            if (functionalityList.getExtensionAddon()) {
                out = output.toString();
                addshipmentresult();
            } else {
                Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
            }
        }, Ced_MultiVendor_ShipmentView.this, "POST", postdata);
        response.execute(removeShipmentTracking);
    }

    private void addshipment() {
        Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(output -> {
            if (functionalityList.getExtensionAddon()) {
                out = output.toString();
                addshipmentresult();
            } else {
                Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
            }
        }, Ced_MultiVendor_ShipmentView.this, "POST", postdata);
        response.execute(addShipmentTracking);
    }

    private void addshipmentresult() throws JSONException {
        jsonObject = new JSONObject(out);
        if (jsonObject.has("vendor_approved")) {
            logout();
        } else {
            if (jsonObject.getJSONObject("data").getBoolean("success")) {

                Intent intent = new Intent(Ced_MultiVendor_ShipmentView.this, Ced_MultiVendor_ShipmentView.class);
                intent.putExtra("shipment_id", post_id);
                startActivity(intent);
                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                Toast.makeText(getApplicationContext(), jsonObject.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), jsonObject.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
            }

        }

    }

    @Override
    protected void onResume() {
        Ced_MultiVendor_ConnectionDetector connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        if (connectionDetector.isConnectingToInternet()) {
            setname(session.getvendorname());
            setprofilepic(session.getvendorpic());
            changetitle("Shipment View");
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
