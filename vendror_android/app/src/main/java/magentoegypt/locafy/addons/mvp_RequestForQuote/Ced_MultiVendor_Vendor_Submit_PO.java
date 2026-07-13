package magentoegypt.locafy.addons.mvp_RequestForQuote;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;


import magentoegypt.locafy.api_request_response_section.Ced_ClientRequestResponse;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_UnAuthourizedRequestError;
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
import java.util.Objects;

/**
 * Created by cedcoss on 18/02/21.
 */

public class Ced_MultiVendor_Vendor_Submit_PO extends Ced_MultiVendor_NavigationActivity {
    String quote_id;
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorSessionManagement vendorSessionManagement;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    String out, url, message, hashkey, vendor_id;
    HashMap<String, String> postdata;
    JSONObject jsonObject;
    String create_po;
    TextView MultiVendor_CustomerName, MultiVendor_Email, MultiVendor_CustomerGroup, MultiVendor_shipping_ship_to, MultiVendor_shipping_country, MultiVendor_shipping_state;
    TextView MultiVendor_shipping_city, MultiVendor_shipping_pincode, MultiVendor_shipping_street, MultiVendor_shipping_phone, MultiVendor_grandtotalearned, MultiVendor_TotalDue, Subtotal;
    LinearLayout dynamic_quotes;
    JSONArray item_info;
    Button submit_po;
    String submit_po_url;
    String id, title, sku, proposed_qty, proposal_created_for_qty, subtotal, row_total, quote_item_id, product_id, requested_unit_price, proposed_unit_price;

    ArrayList<HashMap<String, String>> Quote_info;
    HashMap<String, ArrayList> quote_info;
    Float sub_total_value = 0.0f;
    private JSONArray quote_item_id_array = null;
    //    private ArrayList<String> quote_item_id_array = null;
    private String response_status_array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        vendorSessionManagement = new Ced_MultiVendor_VendorSessionManagement(getApplicationContext());
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(getApplicationContext());
        postdata = new HashMap<>();
        quote_info = new HashMap<>();
        Quote_info = new ArrayList<>();
        quote_id = getIntent().getStringExtra("quote_id");
        response_status_array = getIntent().getStringExtra("response_status_array");

        create_po = session.getBase_Url() + "vrfqapi/po/create";
        submit_po_url = session.getBase_Url() + "vrfqapi/po/submitpo";
        if (connectionDetector.isConnectingToInternet()) {
            ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
            getLayoutInflater().inflate(R.layout.ced_multivendor_activity_submit_po, content, true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);
            if (vendorSessionManagement.getvendorname() != null) {
                setname(vendorSessionManagement.getvendorname());
                setprofilepic(vendorSessionManagement.getvendorpic());
                changetitle("Submit PO for Quote");
            }
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
            MultiVendor_grandtotalearned = findViewById(R.id.MultiVendor_grandtotalearned);
            MultiVendor_TotalDue = findViewById(R.id.TotalDue);
            Subtotal = findViewById(R.id.Subtotal);
            submit_po = findViewById(R.id.submit_po);
            dynamic_quotes = findViewById(R.id.dynamic_quotes);

            final HashMap<String, String> user = vendorSessionManagement.getUserDetails();
            hashkey = user.get(Ced_MultiVendor_VendorSessionManagement.Key_HAsh);
            postdata.put("hashkey", vendorSessionManagement.getHahkey());
            postdata.put("vendor_id", vendorSessionManagement.getVendorid());
            postdata.put("quote_id", quote_id);

            request();

            submit_po.setOnClickListener(v -> {
//                    postdata.put("customer_email", MultiVendor_Email.getText().toString());
                String grandtotalofpo = priceWithoutComma(priceWithoutCurrency(MultiVendor_grandtotalearned.getText().toString()));
//                    postdata.put("grandtotalofpo", grandtotalofpo);
                String totaldue = priceWithoutComma(priceWithoutCurrency(MultiVendor_TotalDue.getText().toString()));
//                    postdata.put("totaldue", totaldue);
                String subtotalofpo = priceWithoutComma(priceWithoutCurrency(Subtotal.getText().toString()));
//                    postdata.put("subtotalofpo", subtotalofpo);
                postdata.put("quote_item_ids", quote_item_id_array.toString());
                JSONArray unitprice = new JSONArray();
                JSONArray quoteproducts = new JSONArray();
                JSONArray subtotal = new JSONArray();
                JSONArray row_total = new JSONArray();
                JSONArray products = new JSONArray();
                for (int i = 0; i < dynamic_quotes.getChildCount(); i++) {
                    LinearLayout linearLayout = (LinearLayout) dynamic_quotes.getChildAt(i);
                    CardView cardView = (CardView) linearLayout.getChildAt(0);
                    ConstraintLayout constraintLayout = (ConstraintLayout) cardView.getChildAt(0);
                    AppCompatTextView prod_id = (AppCompatTextView) constraintLayout.getChildAt(0);
                    products.put(priceWithoutComma(priceWithoutCurrency(Objects.requireNonNull(prod_id.getText()).toString())));
                    AppCompatEditText proposed_unit_price = (AppCompatEditText) constraintLayout.getChildAt(17);
                    unitprice.put(priceWithoutComma(priceWithoutCurrency(Objects.requireNonNull(proposed_unit_price.getText()).toString())));
                    AppCompatTextView row_total_txt = (AppCompatTextView) constraintLayout.getChildAt(19);
                    row_total.put(priceWithoutComma(priceWithoutCurrency(Objects.requireNonNull(row_total_txt.getText()).toString())));
                    AppCompatEditText requested_unit_price = (AppCompatEditText) constraintLayout.getChildAt(12);
                    quoteproducts.put(priceWithoutComma(priceWithoutCurrency(Objects.requireNonNull(requested_unit_price.getText()).toString())));
                }
//                    postdata.put("unitprice", unitprice.toString());
                postdata.put("quoteproducts", quoteproducts.toString());
//                    postdata.put("subtotal", row_total.toString());
//                    postdata.put("row_total", row_total.toString());
//                    postdata.put("products", products.toString());
                if (getResources().getString(R.string.Enable_Log).equals("yes"))
                    Log.i("post_data", postdata.toString());
                Ced_ClientRequestResponse response = new Ced_ClientRequestResponse(output -> {
                    out = output.toString();
                    Log.i("OUT", "processFinish: " + out);
                    JSONObject jsonObject = new JSONObject(out);
                    if (jsonObject.getJSONObject("data").getBoolean("success")) {
                        Toast.makeText(getApplicationContext(), jsonObject.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Ced_MultiVendor_Vendor_Submit_PO.this, Ced_MultiVendor_VendorQuote_View.class);
                        intent.putExtra("quote_id", quote_id);
                        intent.putExtra("response_status_array", response_status_array);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), jsonObject.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
                    }
                }, Ced_MultiVendor_Vendor_Submit_PO.this, "POST", postdata);
                response.execute(submit_po_url);
            });
        } else {
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
                view_data();
            } else {
                Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
            }
        }, Ced_MultiVendor_Vendor_Submit_PO.this, "POST", postdata);
        response.execute(create_po);
    }

    private void view_data() throws JSONException {
        jsonObject = new JSONObject(out);
        if (jsonObject.getJSONObject("data").getBoolean("success")) {
            JSONObject customer_info = jsonObject.getJSONObject("data").getJSONObject("customer_info");
            MultiVendor_CustomerName.setText(customer_info.getString("customer_name"));
            MultiVendor_Email.setText(customer_info.getString("customer_email"));
            MultiVendor_CustomerGroup.setText(customer_info.getString("customer_group"));

            JSONObject address_info = jsonObject.getJSONObject("data").getJSONObject("address_info");
            MultiVendor_shipping_ship_to.setText(address_info.getString("ship_to"));
            MultiVendor_shipping_country.setText(address_info.getString("country"));
            String state = address_info.getString("state") + ", ";
            MultiVendor_shipping_state.setText(state);
            MultiVendor_shipping_pincode.setText(address_info.getString("pincode"));
            MultiVendor_shipping_street.setText(address_info.getString("street"));
            String city = address_info.getString("city") + ", ";
            MultiVendor_shipping_city.setText(city);
            String phone = "T: " + address_info.getString("telephone");
            MultiVendor_shipping_phone.setText(phone);

            JSONObject quote_total_info = jsonObject.getJSONObject("data").getJSONObject("quote_total_info");
            Subtotal.setText(quote_total_info.getString("sub_total"));
            MultiVendor_grandtotalearned.setText(quote_total_info.getString("grand_total"));
            MultiVendor_TotalDue.setText(quote_total_info.getString("total_due"));
//            quote_item_id_array = new ArrayList<>();
            quote_item_id_array = new JSONArray();
            item_info = jsonObject.getJSONObject("data").getJSONArray("item_info");
            for (int i = 0; i < item_info.length(); i++) {
                JSONObject c = item_info.getJSONObject(i);
                quote_item_id = c.getString("quote_item_id");
                quote_item_id_array.put(quote_item_id);
                product_id = c.getString("product_id");
                title = c.getString("title");
                sku = c.getString("sku");
                requested_unit_price = c.getString("requested_unit_price");
                proposed_unit_price = c.getString("proposed_unit_price");
                proposed_qty = c.getString("proposed_qty");
                proposal_created_for_qty = c.getString("proposal_created_for_qty");
                row_total = c.getString("subtotal");

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("quote_item_id", quote_item_id);
                hashMap.put("product_id", product_id);
                hashMap.put("title", title);
                hashMap.put("sku", sku);
                hashMap.put("requested_unit_price", requested_unit_price);
                hashMap.put("proposed_unit_price", proposed_unit_price);
                hashMap.put("proposed_qty", proposed_qty);
                hashMap.put("proposal_created_for_qty", proposal_created_for_qty);
                hashMap.put("subtotal", row_total);
                Quote_info.add(hashMap);
                quote_info.put(quote_item_id + "#" + product_id + "#" + title + "#" + sku + "#" + requested_unit_price + "#" + proposed_unit_price + "#" + proposed_qty + "#"
                        + proposal_created_for_qty + "#" + row_total, Quote_info);
            }
            Iterator iterator = quote_info.entrySet().iterator();
            dynamic_quotes.removeAllViews();
            while (iterator.hasNext()) {
                View convertView = View.inflate(this, R.layout.ced_multivendor_quoteviewitems_list, null);
                final AppCompatTextView prod_id = convertView.findViewById(R.id.prod_id);
                final AppCompatTextView MultiVendor_prod_name = convertView.findViewById(R.id.MultiVendor_prod_name);
                final AppCompatTextView MultiVendor_prod_sku = convertView.findViewById(R.id.MultiVendor_prod_sku);
                final AppCompatTextView MultiVendor_ItemStock = convertView.findViewById(R.id.MultiVendor_ItemStock);
                final AppCompatTextView ItemStock = convertView.findViewById(R.id.ItemStock);
                final AppCompatTextView requested_qty_txt = convertView.findViewById(R.id.requested_qty_txt);
                final AppCompatTextView requested_qty = convertView.findViewById(R.id.requested_qty);
                final AppCompatTextView requested_unit_price = convertView.findViewById(R.id.requested_unit_price);
                final AppCompatEditText proposed_qty = convertView.findViewById(R.id.proposed_qty);
                final AppCompatTextView proposal_created_for_qty = convertView.findViewById(R.id.proposal_created_for_qty);
                final AppCompatTextView currency_symbol = convertView.findViewById(R.id.currency_symbol);
                currency_symbol.setText(session.getCurrencySymbol());
                final AppCompatEditText proposed_unit_price = convertView.findViewById(R.id.proposed_unit_price);
                final AppCompatTextView MultiVendor_RowTotal = convertView.findViewById(R.id.MultiVendor_RowTotal);
                requested_qty_txt.setVisibility(View.GONE);
                requested_qty.setVisibility(View.GONE);
                MultiVendor_ItemStock.setVisibility(View.GONE);
                ItemStock.setVisibility(View.GONE);
                Map.Entry entry = (Map.Entry) iterator.next();
                String key = String.valueOf(entry.getKey());
                String[] parts = key.split("#");
                prod_id.setText(parts[1]);
                prod_id.setContentDescription(parts[0]);
                MultiVendor_prod_name.setText(parts[2]);
                MultiVendor_prod_sku.setText(parts[3]);
                requested_unit_price.setText(parts[4]);
                proposed_qty.setText(parts[6]);
                proposal_created_for_qty.setText(parts[7]);
                MultiVendor_RowTotal.setText(parts[8]);
                MultiVendor_RowTotal.setTag(parts[8]);
                proposed_unit_price.setText(priceWithoutCurrency(parts[5]));
                proposed_unit_price.setEnabled(false);
                proposed_qty.setEnabled(true);
                proposed_qty.setBackground(getResources().getDrawable(R.drawable.corner_apptheme));
                proposed_qty.setMinHeight(30);
                proposed_qty.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (!Objects.requireNonNull(proposed_qty.getText()).toString().isEmpty() && !Objects.requireNonNull(proposed_unit_price.getText()).toString().isEmpty()) {
                            Float subtotal = Float.parseFloat(priceWithoutComma(priceWithoutCurrency(Subtotal.getText().toString())));
                            Float rowtotal = Float.parseFloat(priceWithoutComma(priceWithoutCurrency(MultiVendor_RowTotal.getTag().toString())));
                            Float proposed_unitprice = Float.parseFloat(priceWithoutComma(priceWithoutCurrency(proposed_unit_price.getText().toString())));
                            Integer prop_qty = Integer.parseInt((proposed_qty.getText().toString()));
                            Float subtotal_rowtotal = subtotal - rowtotal;
                            Subtotal.setText(String.valueOf(subtotal_rowtotal + prop_qty * proposed_unitprice));
                            MultiVendor_TotalDue.setText(String.valueOf(subtotal_rowtotal + prop_qty * proposed_unitprice));
                            MultiVendor_grandtotalearned.setText(String.valueOf(subtotal_rowtotal + prop_qty * proposed_unitprice));
                            MultiVendor_RowTotal.setText(String.valueOf(prop_qty * proposed_unitprice));
                            MultiVendor_RowTotal.setTag(String.valueOf(prop_qty * proposed_unitprice));
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                dynamic_quotes.addView(convertView);
            }
        }
    }

    @Override
    protected void onResume() {
        if (connectionDetector.isConnectingToInternet()) {
            setname(vendorSessionManagement.getvendorname());
            setprofilepic(vendorSessionManagement.getvendorpic());
            changetitle("Submit PO for Quote");
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

    private String priceWithoutComma(String price) {
        if (price.contains(",")) {
            price = price.replace(",", "");
        }
        return price;
    }

    private String priceWithoutCurrency(String price) {
        if (price.contains("₹")) {
            price = price.replace("₹", "");
        } else if (price.contains("$")) {
            price = price.replace("$", "");
        } else {
            price = price.replaceAll("[^0-9.]", "");
        }
        return price;
    }
}