package magentoegypt.locafy.addons.mvp_RequestForQuote;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;


import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_ClientRequestResponse;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;
import magentoegypt.locafy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by cedcoss on 25/1/18.
 */

public class Ced_MultiVendor_Vendor_Po_list_view extends Ced_MultiVendor_NavigationActivity {

    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorSessionManagement vendorSessionManagement;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    String out, url, message, hashkey, vendor_id;
    HashMap<String, String> postdata;
    JSONObject jsonObject;
    String po_id;
    String po_view_url;
    Button MultiVendor_po_increment_id;
    TextView PODate, POStatus, MultiVendor_CustomerName, MultiVendor_Email, MultiVendor_CustomerGroup, MultiVendor_shipping_ship_to, MultiVendor_shipping_country, MultiVendor_shipping_state, MultiVendor_shipping_city;
    TextView MultiVendor_shipping_pincode, MultiVendor_shipping_street, MultiVendor_shipping_phone, MultiVendor_shipping_description, MultiVendor_shipping_charges, Subtotal, MultiVendor_ShippingAndHandling, MultiVendor_grandtotalearned;
    JSONArray item_info;
    String id, title, sku, po_price, quote_qty, po_qty, remaining_qty, po_row_total;
    ArrayList<HashMap<String, String>> Quote_info;
    HashMap<String, ArrayList> quote_info;
    LinearLayout dynamic_quotes;
    Spinner MultiVendor_spn_rfq_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        vendorSessionManagement = new Ced_MultiVendor_VendorSessionManagement(getApplicationContext());
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(getApplicationContext());
        postdata = new HashMap<>();
        quote_info = new HashMap<>();
        Quote_info = new ArrayList<>();
        po_id = getIntent().getStringExtra("po_id");
        po_view_url = session.getBase_Url() + "vrfqapi/po/viewpo";
        if (connectionDetector.isConnectingToInternet()) {
            ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
            getLayoutInflater().inflate(R.layout.ced_multivendor_activity_po_list_view, content, true);
            if (vendorSessionManagement.getvendorname() != null) {
                setname(vendorSessionManagement.getvendorname());
                setprofilepic(vendorSessionManagement.getvendorpic());
                changetitle("My PO View");
            }
            MultiVendor_po_increment_id = findViewById(R.id.MultiVendor_po_increment_id);
            PODate = findViewById(R.id.PODate);
            POStatus = findViewById(R.id.POStatus);
            MultiVendor_CustomerName = findViewById(R.id.MultiVendor_CustomerName);
            MultiVendor_Email = findViewById(R.id.MultiVendor_Email);
            MultiVendor_CustomerGroup = findViewById(R.id.MultiVendor_CustomerGroup);
            MultiVendor_shipping_ship_to = findViewById(R.id.MultiVendor_shipping_ship_to);
            MultiVendor_shipping_country = findViewById(R.id.MultiVendor_shipping_country);
            MultiVendor_shipping_state = findViewById(R.id.MultiVendor_shipping_state);
            MultiVendor_shipping_city = findViewById(R.id.MultiVendor_shipping_city);
            MultiVendor_shipping_pincode = findViewById(R.id.MultiVendor_shipping_pincode);
            MultiVendor_shipping_street = findViewById(R.id.MultiVendor_shipping_street);
            MultiVendor_shipping_phone = findViewById(R.id.MultiVendor_shipping_phone);
            MultiVendor_shipping_description = findViewById(R.id.MultiVendor_shipping_description);
            MultiVendor_shipping_charges = findViewById(R.id.MultiVendor_shipping_charges);
            Subtotal = findViewById(R.id.Subtotal);
            MultiVendor_ShippingAndHandling = findViewById(R.id.MultiVendor_ShippingAndHandling);
            MultiVendor_grandtotalearned = findViewById(R.id.MultiVendor_grandtotalearned);
            dynamic_quotes = findViewById(R.id.dynamic_quotes);
            final HashMap<String, String> user = vendorSessionManagement.getUserDetails();
            hashkey = user.get(Ced_MultiVendor_VendorSessionManagement.Key_HAsh);
            postdata.put("hashkey", vendorSessionManagement.getHahkey());
            postdata.put("vendor_id", vendorSessionManagement.getVendorid());
            postdata.put("po_id", po_id);
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
        Ced_ClientRequestResponse ced_clientRequestResponse = new Ced_ClientRequestResponse(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                out = output.toString();
                view_data();
            }


        }, Ced_MultiVendor_Vendor_Po_list_view.this, "POST", postdata);
        ced_clientRequestResponse.execute(po_view_url);
    }

    private void view_data() throws JSONException {
        jsonObject = new JSONObject(out);
        if (jsonObject.getJSONObject("data").getBoolean("success")) {
            String po_increment_id_txt = "PO #" + jsonObject.getJSONObject("data").getJSONObject("po_info").getString("po_increment_id");
            MultiVendor_po_increment_id.setText(po_increment_id_txt);
            PODate.setText(jsonObject.getJSONObject("data").getJSONObject("po_info").getString("po_date"));
            POStatus.setText(jsonObject.getJSONObject("data").getJSONObject("po_info").getString("po_status"));
            MultiVendor_CustomerName.setText(jsonObject.getJSONObject("data").getJSONObject("account_info").getString("customer_name"));
            MultiVendor_Email.setText(jsonObject.getJSONObject("data").getJSONObject("account_info").getString("customer_email"));
            MultiVendor_CustomerGroup.setText(jsonObject.getJSONObject("data").getJSONObject("account_info").getString("customer_group"));
            MultiVendor_shipping_ship_to.setText(jsonObject.getJSONObject("data").getJSONObject("account_info").getString("customer_name"));
            MultiVendor_shipping_country.setText(jsonObject.getJSONObject("data").getJSONObject("address_info").getString("country"));
            MultiVendor_shipping_state.setText(jsonObject.getJSONObject("data").getJSONObject("address_info").getString("state"));
            MultiVendor_shipping_pincode.setText(jsonObject.getJSONObject("data").getJSONObject("address_info").getString("pincode"));
            MultiVendor_shipping_street.setText(jsonObject.getJSONObject("data").getJSONObject("address_info").getString("street"));
            MultiVendor_shipping_city.setText(jsonObject.getJSONObject("data").getJSONObject("address_info").getString("city"));
            MultiVendor_shipping_phone.setText(jsonObject.getJSONObject("data").getJSONObject("address_info").getString("telephone"));
            //  MultiVendor_shipping_description.setText(jsonObject.getJSONObject("data").getJSONObject("shipping_info").getString("shipping_description"));
            //  MultiVendor_shipping_charges.setText(jsonObject.getJSONObject("data").getJSONObject("shipping_info").getString("shipping_charges"));
            Subtotal.setText(jsonObject.getJSONObject("data").getJSONObject("quote_total_info").getString("subtotal"));
            //     MultiVendor_ShippingAndHandling.setText(jsonObject.getJSONObject("data").getJSONObject("quote_total_info").getString("shipping_handling"));
            MultiVendor_grandtotalearned.setText(jsonObject.getJSONObject("data").getJSONObject("quote_total_info").getString("grand_total"));

            item_info = jsonObject.getJSONObject("data").getJSONArray("item_info");
            for (int i = 0; i < item_info.length(); i++) {
                JSONObject c = item_info.getJSONObject(i);

                title = c.getString("title");
                sku = c.getString("sku");
                po_qty = c.getString("proposed_qty");
                remaining_qty = c.getString("remaining_qty");
                po_row_total = c.getString("row_total");
                po_price = c.getString("requested_price");
                quote_qty = c.getString("requested_qty");

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("title", title);
                hashMap.put("sku", sku);
                hashMap.put("requested_price", po_price);
                hashMap.put("requested_qty", quote_qty);
                hashMap.put("proposed_qty", po_qty);
                hashMap.put("remaining_qty", remaining_qty);
                hashMap.put("row_total", po_row_total);
                Quote_info.add(hashMap);
                quote_info.put(title + "#" + sku + "#" + po_price + "#" + quote_qty + "#" + po_qty + "#" + remaining_qty + "#" + po_row_total, Quote_info);
            }
            Iterator iterator = quote_info.entrySet().iterator();
            while (iterator.hasNext()) {
                View convertView = View.inflate(this, R.layout.ced_multivendor_po_viewitems_list, null);
                final TextView MultiVendor_prod_name = convertView.findViewById(R.id.MultiVendor_prod_name);
                final TextView MultiVendor_prod_sku = convertView.findViewById(R.id.MultiVendor_prod_sku);
                final TextView requested_qty = convertView.findViewById(R.id.requested_qty);
                final TextView requested_unit_price = convertView.findViewById(R.id.requested_unit_price);
                final TextView POQty = convertView.findViewById(R.id.proposed_qty);
                final TextView RemainingQty = convertView.findViewById(R.id.RemainingQty);
                final TextView PORowTotal = convertView.findViewById(R.id.MultiVendor_RowTotal);

                Map.Entry entry = (Map.Entry) iterator.next();
                String key = String.valueOf(entry.getKey());
                String[] parts = key.split("#");
                MultiVendor_prod_name.setText(parts[0]);
                MultiVendor_prod_sku.setText(parts[1]);
                requested_unit_price.setText(parts[2]);
                requested_qty.setText(parts[3]);
                POQty.setText(parts[4]);
                RemainingQty.setText(parts[5]);
                PORowTotal.setText(parts[6]);
                dynamic_quotes.addView(convertView);
            }
        }
    }
}