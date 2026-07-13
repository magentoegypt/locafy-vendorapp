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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import magentoegypt.locafy.api_request_response_section.Ced_ClientRequestResponse;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;
import magentoegypt.locafy.R;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by cedcoss on 16/03/21.
 */

public class Ced_MultiVendor_VendorQuote_View extends Ced_MultiVendor_NavigationActivity {

    static final String KEY_id = "id";
    static final String KEY_title = "title";
    static final String KEY_sku = "sku";
    static final String KEY_stock = "stock";
    static final String KEY_row_total = "row_total";
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorSessionManagement vendorSessionManagement;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    String out, hashkey;
    HashMap<String, String> postdata;
    JSONObject jsonObject;
    String view_quote_url;
    String quote_id;
    AppCompatTextView MultiVendor_quoteDate, MultiVendor_QuoteCreatedFrom, MultiVendor_QuoteStatus;
    Button MultiVendor_quote_increment_id;
    TextView MultiVendor_CustomerName, MultiVendor_Email, MultiVendor_CustomerGroup, MultiVendor_shipping_ship_to, MultiVendor_shipping_country, MultiVendor_shipping_state;
    TextView MultiVendor_shipping_city, MultiVendor_shipping_pincode, MultiVendor_shipping_street, MultiVendor_shipping_phone, Subtotal, MultiVendor_grandtotalearned, MultiVendor_TotalDue;
    JSONArray item_info, chat_info;
    String id, title, sku, stock, quote_item_id, product_id, requested_qty, requested_unit_price, proposed_qty, proposal_created_for_qty, proposed_unit_price, row_total, editable;
    String sent_by, chat_date, chat_message;
    LinearLayout dynamic_quotes;
    ArrayList<HashMap<String, String>> Quote_info, chat_details;
    LinkedHashMap<String, ArrayList> quote_info, chats_data;
    LinearLayout MultiVendor_dynamic_comment;
    AppCompatButton createpo, accept, decline;
    FloatingActionButton fab;
    Float sub_total_value = 0.0f;
    AppCompatEditText message;
    Button save_quote;
    String update_url, message_url;
    Spinner MultiVendor_spn_rfq_status;
    boolean accepted, declined = false;
    private String quote_status;
    private String quote_status_value;
    private String response_status_array;
    private JSONObject response_status_json_obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        vendorSessionManagement = new Ced_MultiVendor_VendorSessionManagement(getApplicationContext());
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(getApplicationContext());
        quote_info = new LinkedHashMap<>();
        chats_data = new LinkedHashMap<>();
        postdata = new HashMap<>();
        Quote_info = new ArrayList<>();
        chat_details = new ArrayList<>();

        if (connectionDetector.isConnectingToInternet()) {
            ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
            getLayoutInflater().inflate(R.layout.ced_multivendor_activity_quote_view, content, true);
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);

            view_quote_url = session.getBase_Url() + "vrfqapi/quote/viewquote";
            update_url = session.getBase_Url() + "vrfqapi/quote/update";
            message_url =session.getBase_Url() + "vrfqapi/quote/chat";
            quote_id = getIntent().getStringExtra("quote_id");
            if (getIntent().getStringExtra("response_status_array") != null)
                response_status_array = getIntent().getStringExtra("response_status_array");
            if (vendorSessionManagement.getvendorname() != null) {
                setname(vendorSessionManagement.getvendorname());
                setprofilepic(vendorSessionManagement.getvendorpic());
                changetitle("My Quotes View");
            }
            MultiVendor_quote_increment_id = findViewById(R.id.MultiVendor_quote_increment_id);
            MultiVendor_quoteDate = findViewById(R.id.MultiVendor_quoteDate);
            MultiVendor_QuoteStatus = findViewById(R.id.MultiVendor_QuoteStatus);
            MultiVendor_QuoteCreatedFrom = findViewById(R.id.MultiVendor_QuoteCreatedFrom);
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
            Subtotal = findViewById(R.id.Subtotal);
            MultiVendor_grandtotalearned = findViewById(R.id.MultiVendor_grandtotalearned);
            MultiVendor_TotalDue = findViewById(R.id.MultiVendor_TotalDue);
            MultiVendor_spn_rfq_status = findViewById(R.id.MultiVendor_spn_rfq_status);
            save_quote = findViewById(R.id.save_quote);
            dynamic_quotes = findViewById(R.id.dynamic_quotes);
            createpo = findViewById(R.id.create_po);
            decline = findViewById(R.id.decline);
            accept = findViewById(R.id.accept);
            message = findViewById(R.id.message);
           /* createpo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Ced_MultiVendor_VendorQuote_View.this, Ced_MultiVendor_Vendor_Submit_PO.class);
                    intent.putExtra("quote_id", quote_id);
                    startActivity(intent);
                }
            });*/

            MultiVendor_dynamic_comment = findViewById(R.id.MultiVendor_dynamic_comment);
            fab = findViewById(R.id.MultiVendor_fab);
            fab.setOnClickListener(v -> {
                Intent create_attribute = new Intent(Ced_MultiVendor_VendorQuote_View.this, MessageActivity.class);
                create_attribute.putExtra("quote_id", quote_id);
                startActivity(create_attribute);
                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
            });
        } else {
            Intent nointernet = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(nointernet);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }

        final HashMap<String, String> user = vendorSessionManagement.getUserDetails();
        hashkey = user.get(Ced_MultiVendor_VendorSessionManagement.Key_HAsh);
//        postdata.put("hashkey", "2a1f678300166f9b941de29a25f4a4bf");
        postdata.put("vendor_id", vendorSessionManagement.getVendorid());
        postdata.put("quote_id", quote_id);

        request();

        save_quote.setOnClickListener(v -> {
            /*if (!MultiVendor_spn_rfq_status.getSelectedItem().toString().equals("Select")) {
            } else
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.plsselectstatus), Toast.LENGTH_LONG).show();*/
            postdata.remove("message");

            postdata.put("customer_email", MultiVendor_Email.getText().toString());
            /*postdata.put("grandtotalofpo", priceWithoutCurrency(MultiVendor_grandtotalearned.getText().toString()));
            postdata.put("totaldue", priceWithoutCurrency(MultiVendor_TotalDue.getText().toString()));
            postdata.put("subtotalofpo", priceWithoutCurrency(Subtotal.getText().toString()));*/
            JSONArray unitprice = new JSONArray();
            JSONArray quote_updated_qty = new JSONArray();
            JSONArray quote_item_ids = new JSONArray();
            for (int i = 0; i < dynamic_quotes.getChildCount(); i++) {
                LinearLayout linearLayout = (LinearLayout) dynamic_quotes.getChildAt(i);
                CardView cardView = (CardView) linearLayout.getChildAt(0);
                ConstraintLayout constraintLayout = (ConstraintLayout) cardView.getChildAt(0);

                AppCompatTextView quote_item_id = (AppCompatTextView) constraintLayout.getChildAt(0);
                quote_item_ids.put(quote_item_id.getContentDescription().toString());

                AppCompatEditText proposed_unit_price = (AppCompatEditText) constraintLayout.getChildAt(17);
                unitprice.put(priceWithoutCurrency(Objects.requireNonNull(proposed_unit_price.getText()).toString()));

                AppCompatEditText proposed_qty = (AppCompatEditText) constraintLayout.getChildAt(12);
                quote_updated_qty.put(Objects.requireNonNull(proposed_qty.getText()).toString());
            }
            postdata.put("unitprice", unitprice.toString());
            postdata.put("quote_updated_qty", quote_updated_qty.toString());
            postdata.put("quote_item_ids", quote_item_ids.toString());
            if (!accepted && !declined)
                postdata.put("status", quote_status_value);
//                    postdata.put("status", "4");
            /*if (!accepted && !declined) {
                switch (MultiVendor_spn_rfq_status.getSelectedItem().toString()) {
                    case "Select":
                        postdata.put("status", "");
                        break;
                    case "Pending":
                        postdata.put("status", "0");
                        break;
                    case "Updated":
                        postdata.put("status", "1");
                        break;
                    case "Approved":
                        postdata.put("status", "2");
                        break;
                    case "Rejected":
                        postdata.put("status", "3");
                        break;
                    case "Complete Proposal":
                        postdata.put("status", "4");
                        break;
                    case "Partial Proposal":
                        postdata.put("status", "5");
                        break;
                    case "Ordered":
                        postdata.put("status", "6");
                        break;
                    case "Complete":
                        postdata.put("status", "7");
                        break;
                }
            }*/
            Log.i("post_data", postdata.toString());
            Ced_ClientRequestResponse response = new Ced_ClientRequestResponse(output -> {
                out = output.toString();
                JSONObject jsonObject = new JSONObject(out);
                if (jsonObject.getJSONObject("data").getBoolean("success")) {
                    Toast.makeText(getApplicationContext(), jsonObject.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Ced_MultiVendor_VendorQuote_View.this, Ced_MultiVendor_VendorQuote_View.class);
                    intent.putExtra("quote_id", quote_id);
                    intent.putExtra("response_status_array", response_status_array);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), jsonObject.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
                }
            }, Ced_MultiVendor_VendorQuote_View.this, "POST", postdata);
            response.execute(update_url);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(String message) {
        if (message.equalsIgnoreCase("sent")) {
            request();
        }
    }

    private void request() {
        Ced_ClientRequestResponse ced_clientRequestResponse = new Ced_ClientRequestResponse(output -> {
            out = output.toString();
            view_data();
        }, Ced_MultiVendor_VendorQuote_View.this, "POST", postdata);
        ced_clientRequestResponse.execute(view_quote_url);

    }

    private void view_data() throws JSONException {
        jsonObject = new JSONObject(out);
        if (jsonObject.getJSONObject("data").getBoolean("success")) {
            if (jsonObject.getJSONObject("data").getBoolean("show_po_button")) {
                createpo.setVisibility(View.VISIBLE);
            } else {
                createpo.setVisibility(View.GONE);
            }
            if (jsonObject.getJSONObject("data").has("show_reject_button")) {
                decline.setVisibility(View.VISIBLE);
                decline.setContentDescription(jsonObject.getJSONObject("data").getString("show_reject_button"));
            } else {
                decline.setVisibility(View.GONE);
                declined = false;
            }
            if (jsonObject.getJSONObject("data").has("show_approve_button")) {
                accept.setVisibility(View.VISIBLE);
                accept.setContentDescription(jsonObject.getJSONObject("data").getString("show_approve_button"));
            } else {
                accepted = false;
                accept.setVisibility(View.GONE);
            }
            String quote_increment_id = "Quote #" + jsonObject.getJSONObject("data").getJSONObject("order_info").getString("quote_increment_id");
            MultiVendor_quote_increment_id.setText(quote_increment_id);
            if (jsonObject.getJSONObject("data").getJSONObject("order_info").has("quote_status")) {
                getStatusValueFromResponseStatusJSONObj(jsonObject.getJSONObject("data").getJSONObject("order_info").getString("quote_status"));
                Log.i("QUOTE STATUS", "view_data:quote_status " + jsonObject.getJSONObject("data").getJSONObject("order_info").has("quote_status"));
            }
            MultiVendor_quoteDate.setText(jsonObject.getJSONObject("data").getJSONObject("order_info").getString("quote_date"));
            MultiVendor_QuoteStatus.setText(jsonObject.getJSONObject("data").getJSONObject("order_info").getString("quote_status"));
            MultiVendor_QuoteCreatedFrom.setText(jsonObject.getJSONObject("data").getJSONObject("order_info").getString("quote_created_from"));
            MultiVendor_CustomerName.setText(jsonObject.getJSONObject("data").getJSONObject("customer_info").getString("customer_name"));
            MultiVendor_Email.setText(jsonObject.getJSONObject("data").getJSONObject("customer_info").getString("customer_email"));
            MultiVendor_CustomerGroup.setText(jsonObject.getJSONObject("data").getJSONObject("customer_info").getString("customer_group"));

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

            Subtotal.setText(jsonObject.getJSONObject("data").getJSONObject("quote_total_info").getString("sub_total_of_po"));
            MultiVendor_grandtotalearned.setText(jsonObject.getJSONObject("data").getJSONObject("quote_total_info").getString("grand_total_of_po"));
            MultiVendor_TotalDue.setText(jsonObject.getJSONObject("data").getJSONObject("quote_total_info").getString("total_due"));
            switch (MultiVendor_QuoteStatus.getText().toString()) {
                case "Select":
                    MultiVendor_spn_rfq_status.setSelection(0);
                    break;
                case "Pending": {
                    MultiVendor_spn_rfq_status.setSelection(1);
                    break;
                }
                case "Updated":
                    MultiVendor_spn_rfq_status.setSelection(2);
                    break;
                case "Approved":
                    MultiVendor_spn_rfq_status.setSelection(3);
                    break;
                case "Rejected":
                    MultiVendor_spn_rfq_status.setSelection(4);
                    break;
                case "Complete Proposal":
                    MultiVendor_spn_rfq_status.setSelection(5);
                    break;
                case "Partial Proposal":
                    MultiVendor_spn_rfq_status.setSelection(6);
                    break;
                case "Ordered":
                    MultiVendor_spn_rfq_status.setSelection(7);
                    break;
                case "Complete":
                    MultiVendor_spn_rfq_status.setSelection(8);
                    break;
            }
            item_info = jsonObject.getJSONObject("data").getJSONArray("item_info");
            for (int i = 0; i < item_info.length(); i++) {
                JSONObject c = item_info.getJSONObject(i);
                /*id = c.getString(KEY_id);
                title = c.getString(KEY_title);
                sku = c.getString(KEY_sku);
                stock = c.getString(KEY_stock);
                row_total = c.getString(KEY_row_total);;*/

                quote_item_id = c.getString("quote_item_id");
                product_id = c.getString("product_id");
                title = c.getString("title");
                sku = c.getString("sku");
                stock = c.getString("stock");
                requested_qty = c.getString("requested_qty");
                requested_unit_price = c.getString("requested_unit_price");
                proposed_qty = c.getString("proposed_qty");
                proposal_created_for_qty = c.getString("proposal_created_for_qty");
                proposed_unit_price = c.getString("proposed_unit_price");
                row_total = c.getString("row_total");
                editable = c.getString("editable");

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("quote_item_id", quote_item_id);
                hashMap.put("product_id", product_id);
                hashMap.put("title", title);
                hashMap.put("sku", sku);
                hashMap.put("stock", stock);
                hashMap.put("requested_qty", requested_qty);
                hashMap.put("requested_unit_price", requested_unit_price);
                hashMap.put("proposed_qty", proposed_qty);
                hashMap.put("proposal_created_for_qty", proposal_created_for_qty);
                hashMap.put("proposed_unit_price", proposed_unit_price);
                hashMap.put("row_total", row_total);
                hashMap.put("editable", editable);
                Quote_info.add(hashMap);
                quote_info.put(quote_item_id + "#" + product_id + "#" + title + "#" + sku + "#" + stock + "#" + requested_qty + "#" + requested_unit_price + "#" + proposed_qty + "#" + proposal_created_for_qty
                        + "#" + proposed_unit_price + "#" + row_total + "#" + editable, Quote_info);

            }
            Iterator iterator = quote_info.entrySet().iterator();
            dynamic_quotes.removeAllViews();
            while (iterator.hasNext()) {
                View convertView = View.inflate(this, R.layout.ced_multivendor_quoteviewitems_list, null);
                final AppCompatTextView prod_id = convertView.findViewById(R.id.prod_id);
                final AppCompatTextView currency_symbol = convertView.findViewById(R.id.currency_symbol);
                currency_symbol.setText(session.getCurrencySymbol());
                final AppCompatTextView MultiVendor_prod_name = convertView.findViewById(R.id.MultiVendor_prod_name);
                final AppCompatTextView MultiVendor_prod_sku = convertView.findViewById(R.id.MultiVendor_prod_sku);
                final AppCompatTextView ItemStock = convertView.findViewById(R.id.ItemStock);
                final AppCompatTextView requested_qty = convertView.findViewById(R.id.requested_qty);
                final AppCompatTextView requested_unit_price = convertView.findViewById(R.id.requested_unit_price);
                final AppCompatEditText proposed_qty = convertView.findViewById(R.id.proposed_qty);
                final AppCompatTextView proposal_created_for_qty = convertView.findViewById(R.id.proposal_created_for_qty);
                final AppCompatEditText proposed_unit_price = convertView.findViewById(R.id.proposed_unit_price);
                final AppCompatTextView MultiVendor_RowTotal = convertView.findViewById(R.id.MultiVendor_RowTotal);
/*              0=quote_item_id
                1=product_id
                2=title
                3=sku
                4=stock
                5=requested_qty
                6=requested_unit_price
                7=proposed_qty
                8=proposal_created_for_qty
                9=proposed_unit_price
                10=row_total
                */
                Map.Entry entry = (Map.Entry) iterator.next();
                String key = String.valueOf(entry.getKey());
                final String[] parts = key.split("#");
                if (parts[11].equals("false")) {
                    proposed_qty.setEnabled(false);
                    proposed_unit_price.setEnabled(false);
                } else {
                    proposed_qty.setEnabled(true);
                    proposed_qty.setBackground(getResources().getDrawable(R.drawable.corner_apptheme));
                    proposed_unit_price.setEnabled(true);
                    proposed_unit_price.setBackground(getResources().getDrawable(R.drawable.corner_apptheme));
                }
                prod_id.setContentDescription(parts[0]);
                prod_id.setText(parts[1]);
                MultiVendor_prod_name.setText(parts[2]);
                MultiVendor_prod_sku.setText(parts[3]);
                ItemStock.setText(parts[4]);
                requested_qty.setText(parts[5]);
                requested_unit_price.setText(parts[6]);
                proposed_qty.setText(parts[7]);
                proposal_created_for_qty.setText(parts[8]);
                proposed_unit_price.setText(priceWithoutCurrency(parts[9]));
                MultiVendor_RowTotal.setText(parts[10]);

                proposed_qty.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (!Objects.requireNonNull(proposed_qty.getText()).toString().isEmpty() && !Objects.requireNonNull(proposed_unit_price.getText()).toString().isEmpty()) {
                            MultiVendor_RowTotal.setText(String.valueOf(Integer.parseInt(proposed_qty.getText().toString()) * Float.parseFloat(priceWithoutCurrency(proposed_unit_price.getText().toString()))));
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        /*if (!(s.length() == 0)) {
                            if (Integer.parseInt(Objects.requireNonNull(proposed_qty.getText()).toString()) > Integer.parseInt(parts[6])) {
                                Toast.makeText(Ced_MultiVendor_VendorQuote_View.this, getResources().getString(R.string.cannotbegreater), Toast.LENGTH_LONG).show();
                                proposed_qty.setText(parts[6]);
                            }
                        }*/
                    }
                });
                proposed_unit_price.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (!Objects.requireNonNull(proposed_qty.getText()).toString().isEmpty() && !Objects.requireNonNull(proposed_unit_price.getText()).toString().isEmpty()) {
                            MultiVendor_RowTotal.setText(String.valueOf(Integer.parseInt(proposed_qty.getText().toString()) * Float.parseFloat(priceWithoutCurrency(proposed_unit_price.getText().toString()))));
                        }
                    }
                });

                dynamic_quotes.addView(convertView);
                MultiVendor_RowTotal.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        sub_total_value = 0.0f;
                        for (int i = 0; i < dynamic_quotes.getChildCount(); i++) {
                            LinearLayout linearLayout = (LinearLayout) dynamic_quotes.getChildAt(i);
                            CardView cardView = (CardView) linearLayout.getChildAt(0);
                            ConstraintLayout constraintLayout = (ConstraintLayout) cardView.getChildAt(0);
                            AppCompatTextView MultiVendor_RowTotal = (AppCompatTextView) constraintLayout.getChildAt(19);
                            sub_total_value += Float.parseFloat(MultiVendor_RowTotal.getText().toString());
                            Log.i("hello", "" + sub_total_value);
                        }
                        Subtotal.setText(String.valueOf(sub_total_value));
                        MultiVendor_grandtotalearned.setText(String.valueOf(sub_total_value));
                        MultiVendor_TotalDue.setText(String.valueOf(sub_total_value));
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
            if (jsonObject.getJSONObject("data").has("chat_info")) {
                chat_info = jsonObject.getJSONObject("data").getJSONArray("chat_info");
                for (int j = 0; j < chat_info.length(); j++) {
                    JSONObject object = chat_info.getJSONObject(j);
                    sent_by = object.getString("sent_by");
                    chat_date = object.getString("chat_date");
                    chat_message = object.getString("chat_message");
                    HashMap<String, String> map = new HashMap<>();
                    map.put("sent_by", sent_by);
                    map.put("chat_date", chat_date);
                    if (chat_message.equals("")) {
                        Log.i("rfqkey", "if wxwcutes");
                        map.put("chat_message", "asd");
                        chat_details.add(map);
                        chats_data.put(chat_date + "#" + sent_by + "#" + "asd", chat_details);
                    } else {
                        Log.i("rfqkey", "elsew wxwcutes");
                        map.put("chat_message", chat_message);
                        chat_details.add(map);
                        chats_data.put(chat_date + "#" + sent_by + "#" + chat_message, chat_details);
                    }
                }
                Iterator iterator1 = chats_data.entrySet().iterator();
                Log.i("rfqkey", "" + chat_details.toString());
                Log.i("rfqkey", "iterator size-- " + chats_data.size());
                Log.i("rfqkey2", "iterator size-- " + chats_data.toString());
                MultiVendor_dynamic_comment.removeAllViews();
                while (iterator1.hasNext()) {
                    Log.i("rfqkey", "iterator");
                    View view2 = View.inflate(this, R.layout.ced_multivendor_dynamic_comment_add, null);
                    final TextView textView2 = view2.findViewById(R.id.MultiVendor_Date);
                    final TextView textView3 = view2.findViewById(R.id.MultiVendor_Notified_head);
                    final TextView textView4 = view2.findViewById(R.id.MultiVendor_Notified);
                    final TextView textView6 = view2.findViewById(R.id.MultiVendor_Comment);

                    Map.Entry entry = (Map.Entry) iterator1.next();
                    String key = String.valueOf(entry.getKey());
                    String[] parts = key.split("#");
                    if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                        Log.i("rfqkey", "key------" + key);
                        Log.i("parts", parts[2]);
                    }
                    textView3.setText(R.string.sender_txt);
                    textView2.setText(parts[0]);
                    textView4.setText(parts[1]);
                    if (!parts[2].equals("asd")) {
                        textView6.setText(parts[2]);
                    }

                    MultiVendor_dynamic_comment.addView(view2);
                }
            }
        }
    }

    private void getStatusValueFromResponseStatusJSONObj(String status_label) {
        try {
            if (response_status_array != null) {
                JSONArray jsonArray = new JSONArray(response_status_array);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (jsonObject.has("label") && jsonObject.getString("label").equals(status_label)) {
                        quote_status_value = String.valueOf(jsonObject.getInt("value"));
                        Log.v("STATUS_LABEL", jsonObject.toString());
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        if (connectionDetector.isConnectingToInternet()) {
            setname(vendorSessionManagement.getvendorname());
            setprofilepic(vendorSessionManagement.getvendorpic());
            changetitle("My Quotes View");
        } else {
            Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }
        super.onResume();
    }

    private String priceWithoutCurrency(String priceWithoutCurrency) {
        if (priceWithoutCurrency.contains("₹")) {
            priceWithoutCurrency = priceWithoutCurrency.replace("₹", "");
        } else if (priceWithoutCurrency.contains("$")) {
            priceWithoutCurrency = priceWithoutCurrency.replace("$", "");
        } else {
            priceWithoutCurrency = priceWithoutCurrency.replaceAll("[^0-9.]", "");
        }

        return priceWithoutCurrency;
    }

    public void acceptQuotation(View view) {
        accepted = true;
        postdata.put("status", accept.getContentDescription().toString());
        save_quote.performClick();
    }

    public void createPO(View view) {
        Intent intent = new Intent(Ced_MultiVendor_VendorQuote_View.this, Ced_MultiVendor_Vendor_Submit_PO.class);
        intent.putExtra("quote_id", quote_id);
        intent.putExtra("response_status_array", response_status_array);
        startActivity(intent);
    }


    public void declineQuotation(View view) {
        declined = true;
        postdata.put("status", decline.getContentDescription().toString());
        save_quote.performClick();
    }

    public void sendNewMessage(View view) {
        String msg = Objects.requireNonNull(message.getText()).toString();
        if (msg.isEmpty()) {
            Toast.makeText(this, getResources().getString(R.string.message_validation), Toast.LENGTH_SHORT).show();
        }
        else {
            postdata.put("message", message.getText().toString());
            Ced_MultiVendor_ClientRequestResponse requestResponse = new Ced_MultiVendor_ClientRequestResponse(output -> {
                JSONObject jsonObject = new JSONObject(output.toString()).getJSONObject("data");
                if (jsonObject.getBoolean("success")) {
                    Toast.makeText(Ced_MultiVendor_VendorQuote_View.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    EventBus.getDefault().post("sent");
                    Intent intent = new Intent(Ced_MultiVendor_VendorQuote_View.this, Ced_MultiVendor_VendorQuote_View.class);
                    intent.putExtra("quote_id", quote_id);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    message.setText("");
                } else {
                    Toast.makeText(Ced_MultiVendor_VendorQuote_View.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            }, Ced_MultiVendor_VendorQuote_View.this, "POST", postdata);
            requestResponse.execute(message_url);
        }
    }
}