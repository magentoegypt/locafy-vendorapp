package magentoegypt.locafy.addons.mvp_RequestForQuote;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_UnAuthourizedRequestError;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
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
 * Created by cedcoss on 24/1/18.
 */

public class Ced_MultiVendor_Vendor_ViewPO_List extends Ced_MultiVendor_NavigationActivity {

    static final String KEY_po_id = "po_id";
    static final String KEY_po_increment_id = "po_increment_id";
    static final String KEY_po_customer_id = "po_customer_id";
    static final String KEY_po_price = "po_price";
    static final String KEY_po_qty = "po_qty";
    static final String KEY_quote_increment_id = "quote_increment_id";
    static final String KEY_created_at = "created_at";
    static final String KEY_status = "status";
    private static LayoutInflater inflater = null;
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorSessionManagement vendorSessionManagement;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    String out, url, message, hashkey, vendor_id;
    HashMap<String, String> postdata;
    JSONObject jsonObject;
    String po_item_url;
    ListView MultiVendor_quote_management_list;
    LinearLayout filtersection;
    TextView text_msg, Count_total;
    String datafilterjson = "";
    int current = 1;
    JSONArray po_info;
    ArrayList<HashMap<String, String>> prod_attr_info;
    String po_id, po_increment_id, po_customer_id, po_price, po_qty, created_at, quote_increment_id, status;
    Ced_ViewPO_ListAdapter ced_viewPO_listAdapter;
    Dialog listDialog;
    Calendar newCalendar;
    JSONObject req;
    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        vendorSessionManagement = new Ced_MultiVendor_VendorSessionManagement(getApplicationContext());
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(getApplicationContext());
        postdata = new HashMap<>();
        prod_attr_info = new ArrayList<>();
        po_item_url = session.getBase_Url() + "vrfqapi/po/item";
        req = new JSONObject();
        if (connectionDetector.isConnectingToInternet()) {
            ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
            getLayoutInflater().inflate(R.layout.ced_multivendor_activity_vendor_view_po_list, content, true);


            if (vendorSessionManagement.getvendorname() != null) {
                setname(vendorSessionManagement.getvendorname());
                setprofilepic(vendorSessionManagement.getvendorpic());
                changetitle("Vendor PO Management");
            }

            MultiVendor_quote_management_list = findViewById(R.id.MultiVendor_quote_management_list);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);

            filtersection = findViewById(R.id.MultiVendor_filtersection);
            text_msg = findViewById(R.id.MultiVendor_text_msg);
            Count_total = findViewById(R.id.MultiVendor_Count_total);
            filtersection.setOnClickListener(v -> {
                try {
                    showfilter();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });

            final HashMap<String, String> user = vendorSessionManagement.getUserDetails();
            hashkey = user.get(Ced_MultiVendor_VendorSessionManagement.Key_HAsh);
            vendor_id = vendorSessionManagement.getVendorid();
            postdata.put("vendor_id", vendor_id);
            postdata.put("hashkey", "2a1f678300166f9b941de29a25f4a4bf");
            if (getIntent().getStringExtra("filter") != null) {
                datafilterjson = getIntent().getStringExtra("filter");
                postdata.put("filter", datafilterjson);


            }

            request();

            MultiVendor_quote_management_list.setOnItemClickListener((parent, view, position, id) -> {
                TextView attr_id = view.findViewById(R.id.MultiVendor_po_id);
                Intent edt_attr = new Intent(Ced_MultiVendor_Vendor_ViewPO_List.this, Ced_MultiVendor_Vendor_Po_list_view.class);
                edt_attr.putExtra("po_id", attr_id.getText().toString());
                startActivity(edt_attr);
                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
            });


        } else {
            Intent nointernet = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(nointernet);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }

    }

    private void showfilter() throws JSONException {

        listDialog = new Dialog(this, R.style.PauseDialog);
        listDialog.setTitle(getResources().getString(R.string.alert_name));
        listDialog.setCanceledOnTouchOutside(true);
        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View vi = inflater.inflate(R.layout.ced_multivendor_view_po_list_filter, null);

        final EditText MultiVendor_edt_POIncrementId = vi.findViewById(R.id.MultiVendor_edt_POIncrementId);
//        final EditText MultiVendor_edt_CustomerId = (EditText) vi.findViewById(R.id.MultiVendor_edt_CustomerId);
//        final EditText MultiVendor_edt_POPrice = (EditText) vi.findViewById(R.id.MultiVendor_edt_POPrice);
//        final EditText MultiVendor_edt_POQty = (EditText) vi.findViewById(R.id.MultiVendor_edt_POQty);
        final EditText MultiVendor_edt_QuoteIncrementId = vi.findViewById(R.id.MultiVendor_edt_QuoteIncrementId);
        final EditText MultiVendor_edt_QuoteId = vi.findViewById(R.id.MultiVendor_edt_QuoteId);
        final EditText MultiVendor_edt_CreatedAt = vi.findViewById(R.id.MultiVendor_edt_CreatedAt);
        final Spinner MultiVendor_edt_po_status = vi.findViewById(R.id.MultiVendor_edt_po_status);
        Button setfilter = vi.findViewById(R.id.MultiVendor_setfilter);
        Button unsetfilter = vi.findViewById(R.id.MultiVendor_unsetfilter);
        if (!(datafilterjson.isEmpty())) {
            JSONObject object = null;
            object = new JSONObject(datafilterjson);
            Log.i("asfff", object.toString());
            MultiVendor_edt_POIncrementId.setText(object.getString("po_increment_id"));
//            MultiVendor_edt_CustomerId.setText(object.getString("po_customer_id"));
//            MultiVendor_edt_POPrice.setText(object.getString("po_price"));
//            MultiVendor_edt_POQty.setText(object.getString("po_qty"));
            MultiVendor_edt_QuoteId.setText(object.getString("quote_id"));
//            MultiVendor_edt_QuoteIncrementId.setText(object.getString("quote_increment_id"));
            MultiVendor_edt_CreatedAt.setText(object.getJSONObject("created_at").getString("from"));
            /*if (object.getString("status").equals("0")) {
                MultiVendor_edt_po_status.setSelection(1);
            } else if (object.getString("status").equals("1")) {
                MultiVendor_edt_po_status.setSelection(2);
            } else if (object.getString("status").equals("2")) {
                MultiVendor_edt_po_status.setSelection(3);
            } else if (object.getString("status").equals("3")) {
                MultiVendor_edt_po_status.setSelection(4);
            } else {
                MultiVendor_edt_po_status.setSelection(0);
            }*/
            if (object.getString("status").equals("1")) {
                MultiVendor_edt_po_status.setSelection(1);
            } else if (object.getString("status").equals("3")) {
                MultiVendor_edt_po_status.setSelection(2);
            } else
                MultiVendor_edt_po_status.setSelection(0);
        }

        MultiVendor_edt_CreatedAt.setOnClickListener(v -> AppConstant.setDateFrom(Ced_MultiVendor_Vendor_ViewPO_List.this, MultiVendor_edt_CreatedAt));

        setfilter.setOnClickListener(v -> {
            if (MultiVendor_edt_POIncrementId.getText().toString().isEmpty() &&
                    /*MultiVendor_edt_CustomerId.getText().toString().isEmpty() &&
                    MultiVendor_edt_POPrice.getText().toString().isEmpty() &&
                    MultiVendor_edt_POQty.getText().toString().isEmpty() &&*/
                    MultiVendor_edt_QuoteId.getText().toString().isEmpty() &&
                    MultiVendor_edt_QuoteIncrementId.getText().toString().isEmpty() &&
                    MultiVendor_edt_CreatedAt.getText().toString().isEmpty() &&
                    MultiVendor_edt_po_status.getSelectedItem().toString().equals("Select")
            ) {
                Toast.makeText(Ced_MultiVendor_Vendor_ViewPO_List.this, getResources().getString(R.string.PleaseSelectdesiredFilter), Toast.LENGTH_SHORT).show();
            } else {
                try {
                    req.put("po_increment_id", MultiVendor_edt_POIncrementId.getText().toString());
//                        req.put("po_customer_id", MultiVendor_edt_CustomerId.getText().toString());
//                        req.put("po_price", MultiVendor_edt_POPrice.getText().toString());
//                        req.put("po_qty", MultiVendor_edt_POQty.getText().toString());
                    req.put("quote_id", MultiVendor_edt_QuoteId.getText().toString());
                    // req.put("quote_increment_id", MultiVendor_edt_QuoteIncrementId.getText().toString());
                    JSONObject created_at = new JSONObject();
                    created_at.put("from", MultiVendor_edt_CreatedAt.getText().toString());
                    created_at.put("to", MultiVendor_edt_CreatedAt.getText().toString());
                    req.put("created_at", created_at);
//                        req.put("created_at", MultiVendor_edt_CreatedAt.getText().toString());
                    if (MultiVendor_edt_po_status.getSelectedItem().toString().equals("Select")) {
                        req.put("status", "");
                    } else if (MultiVendor_edt_po_status.getSelectedItem().toString().equals("Pending")) {
                        req.put("status", "0");
                    } else if (MultiVendor_edt_po_status.getSelectedItem().toString().equals("Not Yet Ordered")) {
                        req.put("status", "1");
                    } else if (MultiVendor_edt_po_status.getSelectedItem().toString().equals("Declined")) {
                        req.put("status", "2");
                    } else if (MultiVendor_edt_po_status.getSelectedItem().toString().equals("Ordered")) {
                        req.put("status", "3");
                    }

                    listDialog.dismiss();
                    if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                        Log.i("filter", req.toString());
                    }
                    Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_Vendor_ViewPO_List.class);
                    intent.putExtra("filter", req.toString());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        unsetfilter.setOnClickListener(v -> {
            postdata.remove("filter");
            datafilterjson = "";
            listDialog.dismiss();
            Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_Vendor_ViewPO_List.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        });
        listDialog.setContentView(vi);
        listDialog.setCancelable(true);
        listDialog.show();
    }

    private void request() {
        Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(output -> {
            if (functionalityList.getExtensionAddon()) {
                out = output.toString();
                polist();
            } else {
                Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
            }
        }, Ced_MultiVendor_Vendor_ViewPO_List.this, "POST", postdata);
        response.execute(po_item_url + "/page/" + current);
    }

    private void polist() throws JSONException {
        JSONObject jsonObject = new JSONObject(out);
        if (jsonObject.getJSONObject("data").getBoolean("success")) {
            Count_total.setText("You Have " + jsonObject.getJSONObject("data").getString("count") + " PO");
            po_info = jsonObject.getJSONObject("data").getJSONArray("po_info");
            JSONArray status_json_arr = jsonObject.getJSONObject("data").getJSONArray("status");
            for (int i = 0; i < po_info.length(); i++) {
                JSONObject c = null;
                c = po_info.getJSONObject(i);
                po_id = c.getString(KEY_po_id);
                po_increment_id = c.getString(KEY_po_increment_id);
//                po_customer_id = c.getString(KEY_po_customer_id);
//                po_price = c.getString(KEY_po_price);
//                po_qty = c.getString(KEY_po_qty);
                quote_increment_id = c.getString(KEY_quote_increment_id);
                created_at = c.getString(KEY_created_at);
                status = c.getString(KEY_status);
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(KEY_po_id, po_id);
                hashMap.put(KEY_po_increment_id, po_increment_id);
//                hashMap.put(KEY_po_customer_id, po_customer_id);
//                hashMap.put(KEY_po_price, po_price);
//                hashMap.put(KEY_po_qty, po_qty);
                hashMap.put(KEY_quote_increment_id, quote_increment_id);
                hashMap.put("quote_id", c.getString("quote_id"));
                hashMap.put(KEY_created_at, created_at);
                hashMap.put(KEY_status, status);
                prod_attr_info.add(hashMap);
            }
            ced_viewPO_listAdapter = new Ced_ViewPO_ListAdapter(Ced_MultiVendor_Vendor_ViewPO_List.this, prod_attr_info,status_json_arr);
//            ced_viewPO_listAdapter = new Ced_ViewPO_ListAdapter(Ced_MultiVendor_Vendor_ViewPO_List.this, prod_attr_info);
            MultiVendor_quote_management_list.setAdapter(ced_viewPO_listAdapter);
            MultiVendor_quote_management_list.setDivider(new ColorDrawable(getResources().getColor(R.color.transparent)));
            MultiVendor_quote_management_list.setDividerHeight(0);
        } else {
            message = jsonObject.getJSONObject("data").getString("message");
            text_msg.setText(message);
            text_msg.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

        }


    }

    @Override
    protected void onResume() {
        if (connectionDetector.isConnectingToInternet()) {
            setname(vendorSessionManagement.getvendorname());
            setprofilepic(vendorSessionManagement.getvendorpic());
            changetitle("Vendor PO Management");
            //     invalidateOptionsMenu();
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
}