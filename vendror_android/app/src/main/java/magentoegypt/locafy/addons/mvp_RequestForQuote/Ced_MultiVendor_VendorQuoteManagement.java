package magentoegypt.locafy.addons.mvp_RequestForQuote;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_UnAuthourizedRequestError;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;
import magentoegypt.locafy.R;
import magentoegypt.locafy_constant.AppConstant;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by cedcoss on 17/02/21.
 */

public class Ced_MultiVendor_VendorQuoteManagement extends Ced_MultiVendor_NavigationActivity {
    public static final String KEY_quote_increment_id = "quote_increment_id";
    public static final String KEY_created_at = "created_at";
    public static final String KEY_customer_email = "customer_email";
    public static final String KEY_status = "status";
    static final String KEY_quote_id = "quote_id";
    String quote_url;
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorSessionManagement vendorSessionManagement;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    String out, message, hashkey, vendor_id;
    HashMap<String, String> postdata;
    JSONObject jsonObject;
    AppCompatImageView filtersection;
    TextView text_msg, Count_total;
    String datafilterjson = "";
    int current = 1;
    JSONArray quote_info;
    ArrayList<HashMap<String, String>> prod_attr_info;
    Ced_RFQ_ListAdapter ced_rfq_listAdapter;
    String quote_id, quote_increment_id, created_at, customer_email, status;
    Dialog listDialog;
    LinearLayoutManager layoutManager;
    JSONObject req;
    Boolean select_date_creation = false;
    private RecyclerView recycler;
    private boolean loading = true;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        quote_url = session.getBase_Url() + "vrfqapi/quote/item";
        connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        vendorSessionManagement = new Ced_MultiVendor_VendorSessionManagement(getApplicationContext());
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(getApplicationContext());
        postdata = new HashMap<>();
        prod_attr_info = new ArrayList<>();
        req = new JSONObject();
        if (connectionDetector.isConnectingToInternet()) {
            ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
            getLayoutInflater().inflate(R.layout.ced_multivendor_activity_vendor_quote_list, content, true);
            if (vendorSessionManagement.getvendorname() != null) {
                setname(vendorSessionManagement.getvendorname());
                setprofilepic(vendorSessionManagement.getvendorpic());
                changetitle("Vendor Quote Management");
            }
            recycler = findViewById(R.id.recycler);
            layoutManager = new LinearLayoutManager(this);
            recycler.setLayoutManager(layoutManager);
            recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    if (!recyclerView.canScrollVertically(1) && loading) {
                        current++;
                        loading = false;
                        request();
                    }
                }
            });
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
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
//            postdata.put("hashkey", "2a1f678300166f9b941de29a25f4a4bf");
            postdata.put("hashkey", vendorSessionManagement.getHahkey());
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

    private void showfilter() throws JSONException {
        listDialog = new Dialog(this, R.style.PauseDialog);
        listDialog.setTitle(getResources().getString(R.string.alert_name));
        listDialog.setCanceledOnTouchOutside(true);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") final View vi = Objects.requireNonNull(inflater).inflate(R.layout.ced_multivendor_rfq_list_filter, null);
        final AppCompatEditText MultiVendor_edt_QuoteIncrementId = vi.findViewById(R.id.MultiVendor_edt_QuoteIncrementId);
        final AppCompatEditText MultiVendor_edt_CustomerEmail = vi.findViewById(R.id.MultiVendor_edt_CustomerEmail);
        final AppCompatEditText MultiVendor_edt_CreatedAt = vi.findViewById(R.id.MultiVendor_edt_CreatedAt);
        final AppCompatEditText MultiVendor_edt_CreatedAtTo = vi.findViewById(R.id.MultiVendor_edt_CreatedAtTo);
        final Spinner MultiVendor_edt_rfq_status = vi.findViewById(R.id.MultiVendor_edt_rfq_status);
        AppCompatButton setfilter = vi.findViewById(R.id.MultiVendor_setfilter);
        AppCompatButton unsetfilter = vi.findViewById(R.id.MultiVendor_unsetfilter);
        if (!(datafilterjson.isEmpty())) {
            JSONObject object = new JSONObject(datafilterjson);
            MultiVendor_edt_QuoteIncrementId.setText(object.getString("quote_increment_id"));
            MultiVendor_edt_CustomerEmail.setText(object.getString("customer_email"));
            MultiVendor_edt_CreatedAt.setText(object.getJSONObject("created_at").getString("from"));
            MultiVendor_edt_CreatedAtTo.setText(object.getJSONObject("created_at").getString("to"));
            switch (object.getString("status")) {
                case "0":
                    MultiVendor_edt_rfq_status.setSelection(1);
                    break;
                case "1":
                    MultiVendor_edt_rfq_status.setSelection(2);
                    break;
                case "2":
                    MultiVendor_edt_rfq_status.setSelection(3);
                    break;
                case "3":
                    MultiVendor_edt_rfq_status.setSelection(4);
                    break;
                case "4":
                    MultiVendor_edt_rfq_status.setSelection(5);
                    break;
                case "5":
                    MultiVendor_edt_rfq_status.setSelection(6);
                    break;
                case "6":
                    MultiVendor_edt_rfq_status.setSelection(7);
                    break;
                case "7":
                    MultiVendor_edt_rfq_status.setSelection(8);
                    break;
                default:
                    MultiVendor_edt_rfq_status.setSelection(0);
            }
        }
        MultiVendor_edt_CreatedAt.setOnClickListener(v -> {
            select_date_creation = true;
            AppConstant.lockButton(v);
            AppConstant.setDateFrom_yyyymmdd(Ced_MultiVendor_VendorQuoteManagement.this, MultiVendor_edt_CreatedAt);
        });

        MultiVendor_edt_CreatedAtTo.setOnClickListener(v -> {
            AppConstant.lockButton(v);
            if (select_date_creation) {
                AppConstant.setDateTo_y_m_d(Ced_MultiVendor_VendorQuoteManagement.this, MultiVendor_edt_CreatedAtTo, MultiVendor_edt_CreatedAt);
            } else {
                Toast.makeText(Ced_MultiVendor_VendorQuoteManagement.this, R.string.please_fill_both_dates, Toast.LENGTH_SHORT).show();
            }
        });

        setfilter.setOnClickListener(v -> {
            if (Objects.requireNonNull(MultiVendor_edt_CustomerEmail.getText()).toString().isEmpty() &&
                    Objects.requireNonNull(MultiVendor_edt_QuoteIncrementId.getText()).toString().isEmpty() &&
                    Objects.requireNonNull(MultiVendor_edt_CreatedAt.getText()).toString().isEmpty() &&
                    Objects.requireNonNull(MultiVendor_edt_CreatedAtTo.getText()).toString().isEmpty() &&
                    MultiVendor_edt_rfq_status.getSelectedItem().toString().equals("Select")
            ) {
                Toast.makeText(Ced_MultiVendor_VendorQuoteManagement.this, getResources().getString(R.string.PleaseSelectdesiredFilter), Toast.LENGTH_SHORT).show();
            } else {
                try {
                    req.put("quote_increment_id", Objects.requireNonNull(MultiVendor_edt_QuoteIncrementId.getText()).toString());
                    req.put("customer_email", MultiVendor_edt_CustomerEmail.getText().toString());
                    switch (MultiVendor_edt_rfq_status.getSelectedItem().toString()) {
                        case "Select":
                            req.put("status", "");
                            break;
                        case "Pending":
                            req.put("status", "0");
                            break;
                        case "Updated":
                            req.put("status", "1");
                            break;
                        case "Approved":
                            req.put("status", "2");
                            break;
                        case "Rejected":
                            req.put("status", "3");
                            break;
                        case "Complete Proposal":
                            req.put("status", "4");
                            break;
                        case "Partial Proposal":
                            req.put("status", "5");
                            break;
                        case "Ordered":
                            req.put("status", "6");
                            break;
                        case "Complete":
                            req.put("status", "7");
                            break;
                    }
                    JSONObject created_at = new JSONObject();
                    created_at.put("from", Objects.requireNonNull(MultiVendor_edt_CreatedAt.getText()).toString());
                    created_at.put("to", Objects.requireNonNull(MultiVendor_edt_CreatedAtTo.getText()).toString());
                    req.put("created_at", created_at);
                    listDialog.dismiss();
                    if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                        Log.i("filter", req.toString());
                    }
                    Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_VendorQuoteManagement.class);
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
            Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_VendorQuoteManagement.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        });

        listDialog.setContentView(vi);
        listDialog.setCancelable(true);
        listDialog.show();
    }

    private void request() {
        Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse((AsyncResponse) output -> {
            if (functionalityList.getExtensionAddon()) {
                out = output.toString();
                quotelist();
            } else {
                Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
            }
        }, Ced_MultiVendor_VendorQuoteManagement.this, "POST", postdata);
        response.execute(quote_url + "/page/" + current);
    }

    private void quotelist() throws JSONException {
        jsonObject = new JSONObject(out);
        if (jsonObject.getJSONObject("data").getBoolean("success")) {
            quote_info = jsonObject.getJSONObject("data").getJSONArray("quote_info");
            String count = "You Have " + jsonObject.getJSONObject("data").getString("count") + " Quotes Requests";
            Count_total.setText(count);

            if (jsonObject.getJSONObject("data").has("currency_symbol"))
                session.saveCurrencySymbol(String.valueOf(jsonObject.getJSONObject("data").get("currency_symbol")));

            for (int i = 0; i < quote_info.length(); i++) {
                JSONObject jsonObject = quote_info.getJSONObject(i);
                quote_id = jsonObject.getString(KEY_quote_id);
                quote_increment_id = jsonObject.getString(KEY_quote_increment_id);
                created_at = jsonObject.getString(KEY_created_at);
                customer_email = jsonObject.getString(KEY_customer_email);
                status = jsonObject.getString(KEY_status);
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(KEY_quote_id, quote_id);
                hashMap.put(KEY_quote_increment_id, quote_increment_id);
                hashMap.put(KEY_created_at, created_at);
                hashMap.put(KEY_customer_email, customer_email);
                hashMap.put(KEY_status, status);
                prod_attr_info.add(hashMap);
            }
            JSONArray status = jsonObject.getJSONObject("data").getJSONArray("status");
            ced_rfq_listAdapter = new Ced_RFQ_ListAdapter(Ced_MultiVendor_VendorQuoteManagement.this, prod_attr_info, status);
            recycler.setAdapter(ced_rfq_listAdapter);
            ced_rfq_listAdapter.notifyDataSetChanged();
            loading = true;
        }
        else {
            loading = false;
            message = jsonObject.getJSONObject("data").getString("message");
             /*text_msg.setText(message);
            text_msg.setVisibility(View.VISIBLE);*/
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        if (connectionDetector.isConnectingToInternet()) {
            setname(vendorSessionManagement.getvendorname());
            setprofilepic(vendorSessionManagement.getvendorpic());
            changetitle("Vendor Quote Management");
        }
        else {
            Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }
        super.onResume();
    }
}