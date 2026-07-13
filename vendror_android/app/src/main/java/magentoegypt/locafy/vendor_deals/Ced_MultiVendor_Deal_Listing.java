package magentoegypt.locafy.vendor_deals;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import magentoegypt.locafy.base_app.Ced_MultiVendor_FontSetting;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_UnAuthourizedRequestError;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.R;
import magentoegypt.locafy_constant.AppConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Ced_MultiVendor_Deal_Listing extends Ced_MultiVendor_NavigationActivity {
    final ArrayList<Deal_List_Model> arrayListl = new ArrayList<>();
    public EditText MultiVendor_edt_start_date;
    public EditText MultiVendor_edt_end_date;
    HashMap<String, String> postdata;
    String deal_list_url = "";
    RecyclerView deal_listing;
    GridLayoutManager layoutManager;
    Toolbar mToolbar;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    Ced_MultiVendor_FontSetting fontSetting;
    String s_deal_id, s_product_name, s_end_date, s_start_date, s_admin_status, s_status, s_deal_price, s_product_id;
    TextView title_bar;
    LinearLayout deal_list_filter;
    Deals_List_RecycleViewAdapter adapter;
    TextView deal_id_tv, product_name_tv, deal_price_title,
             deal_status_title, admin_status_title;
    EditText deal_id_edt, product_name_edt, price_from_edt, price_to_edt;
    Spinner deal_status_spinner, admin_status_spinner;
    TextView dialog_reset_tv_btn, dialog_apply_tv_btn, reponse_false;
    RelativeLayout dialog_view;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    boolean firstcall = true;
    int page_num = 1;
    String datafilterjson = "";
    Boolean select_date = false;
    ArrayList<String> admin_status = new ArrayList<>();
    HashMap<String, String> admin_status_hasmap = new HashMap<>();
    ArrayList<String> deal_status = new ArrayList<>();
    HashMap<String, String> deal_status_hasmap = new HashMap<>();
    String admin_status_val = "";
    String deal_status_val = "";
    int count1 = 1;
    int count2 = 1;
    private boolean request = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
        getLayoutInflater().inflate(R.layout.ced_multivendor_deal_listing, content, true);
        deal_list_url = session.getBase_Url() + "vdealapi/deal/item";
        layoutManager = new GridLayoutManager(Ced_MultiVendor_Deal_Listing.this, 1);
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(Ced_MultiVendor_Deal_Listing.this);
        deal_list_filter = findViewById(R.id.MultiVendor_filtersection);
        postdata = new HashMap<>();
        reponse_false = findViewById(R.id.reponse_false);
        deal_listing = findViewById(R.id.outofstock_listing);
        mToolbar = new Toolbar(Ced_MultiVendor_Deal_Listing.this);
        layoutManager.setAutoMeasureEnabled(true);
        deal_listing.setHasFixedSize(false);
        deal_listing.setLayoutManager(layoutManager);
        title_bar = findViewById(R.id.title_bar);
        deal_listing.setNestedScrollingEnabled(false);
        title_bar.setText(getString(R.string.deallist));
        //  hidelogo("Deal List");

        if (getIntent().getStringExtra("filter") != null) {
            datafilterjson = getIntent().getStringExtra("filter");
            postdata.put("filter", datafilterjson);
        }
        fontSetting = new Ced_MultiVendor_FontSetting();
        postdata.put("vendor_id", session.getVendorid());
        postdata.put("hashkey", session.getHahkey());

        list_deals(page_num, firstcall);

        deal_list_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog filter_dialog = new Dialog(Ced_MultiVendor_Deal_Listing.this);
                filter_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                filter_dialog.setContentView(R.layout.filter_dialog_layout);
                dialog_view = filter_dialog.findViewById(R.id.dialog_view);
                deal_id_tv = filter_dialog.findViewById(R.id.sku_title);
                product_name_tv = filter_dialog.findViewById(R.id.product_name_title);
                deal_price_title = filter_dialog.findViewById(R.id.prod_type_title);
                deal_status_title = filter_dialog.findViewById(R.id.deal_status_title);
                admin_status_title = filter_dialog.findViewById(R.id.status_title);
                deal_id_edt = filter_dialog.findViewById(R.id.deal_id_edt);
                product_name_edt = filter_dialog.findViewById(R.id.product_name_edt);
                price_from_edt = filter_dialog.findViewById(R.id.price_edt);
                price_to_edt = filter_dialog.findViewById(R.id.price_to_edt);
                MultiVendor_edt_start_date = filter_dialog.findViewById(R.id.period_from_edt);
                MultiVendor_edt_end_date = filter_dialog.findViewById(R.id.period_to_edt);
                deal_status_spinner = filter_dialog.findViewById(R.id.deal_status_spinner);
                admin_status_spinner = filter_dialog.findViewById(R.id.status_spinner);
                dialog_reset_tv_btn = filter_dialog.findViewById(R.id.dialog_reset_btn);
                dialog_apply_tv_btn = filter_dialog.findViewById(R.id.dialog_apply_btn);

                deal_status_spinner.setAdapter(new ArrayAdapter<>(Ced_MultiVendor_Deal_Listing.this, R.layout.simple_list_item_1, deal_status));
                admin_status_spinner.setAdapter(new ArrayAdapter<>(Ced_MultiVendor_Deal_Listing.this, R.layout.simple_list_item_1, admin_status));

                deal_status_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        count1++;
                        if (count1 > 2) {
                            deal_status_val = deal_status_hasmap.get(adapterView.getSelectedItem().toString());
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                admin_status_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        count2++;
                        if (count2 > 2) {
                            admin_status_val = admin_status_hasmap.get(adapterView.getSelectedItem().toString());
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

              /*  MultiVendor_edt_start_date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Ced_MultiVendor_GlobalVariables.getdate(Ced_MultiVendor_Deal_Listing.this, MultiVendor_edt_start_date);
                    }
                });
                MultiVendor_edt_end_date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Ced_MultiVendor_GlobalVariables.getdate(Ced_MultiVendor_Deal_Listing.this, MultiVendor_edt_end_date);
                    }
                });*/

                MultiVendor_edt_start_date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        select_date = true;
                        AppConstant.setDateFromNoMax(Ced_MultiVendor_Deal_Listing.this, MultiVendor_edt_start_date);
                    }
                });

                MultiVendor_edt_end_date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (select_date) {
                            AppConstant.setDateFromNoMax(Ced_MultiVendor_Deal_Listing.this, MultiVendor_edt_end_date);

                        } else {
                            Toast.makeText(Ced_MultiVendor_Deal_Listing.this, R.string.please_fill_both_dates, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                if (!datafilterjson.isEmpty()) {
                    try {
                        JSONObject jsonObject = new JSONObject(datafilterjson);
                        price_from_edt.setText(jsonObject.getJSONObject("deal_price").getString("from"));
                        price_to_edt.setText(jsonObject.getJSONObject("deal_price").getString("to"));
                        MultiVendor_edt_start_date.setText(jsonObject.getJSONObject("end_date").getString("from"));
                        MultiVendor_edt_end_date.setText(jsonObject.getJSONObject("end_date").getString("to"));
                        deal_id_edt.setText(jsonObject.getString("deal_id"));
                        product_name_edt.setText(jsonObject.getString("product_name"));
                        if (jsonObject.getString("admin_status").equals("Approved")) {
                            admin_status_spinner.setSelection(1);
                        } else if (jsonObject.getString("admin_status").equals("Pending")) {
                            admin_status_spinner.setSelection(2);
                        } else if (jsonObject.getString("admin_status").equals("Disapproved")) {
                            admin_status_spinner.setSelection(3);
                        }
                        if (jsonObject.getString("status").equals("Enable")) {
                            deal_status_spinner.setSelection(1);
                        } else if (jsonObject.getString("status").equals("Disable")) {
                            deal_status_spinner.setSelection(2);
                        } else if (jsonObject.getString("status").equals("Expired")) {
                            deal_status_spinner.setSelection(3);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                dialog_apply_tv_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        JSONObject mainfilter = new JSONObject();
                        try {
                            filter_dialog.dismiss();
                            JSONObject pricejsonObject = new JSONObject();
                            pricejsonObject.put("from", price_from_edt.getText().toString());
                            pricejsonObject.put("to", price_to_edt.getText().toString());
                            mainfilter.put("deal_price", pricejsonObject);
                            JSONObject periodjsonObject = new JSONObject();
                            periodjsonObject.put("from", MultiVendor_edt_start_date.getText().toString());
                            periodjsonObject.put("to", MultiVendor_edt_end_date.getText().toString());
                            mainfilter.put("end_date", periodjsonObject);
                            mainfilter.put("deal_id", deal_id_edt.getText().toString());
                            mainfilter.put("product_name", product_name_edt.getText().toString());
                            //    if (!admin_status_spinner.getSelectedItem().toString().equals("Select Status")) {
                            mainfilter.put("admin_status", admin_status_val);
                          /*  } else {
                                mainfilter.put("admin_status", "");
                            }*/
                            //  if (!deal_status_spinner.getSelectedItem().toString().equals("Select Status")) {
                            mainfilter.put("status", deal_status_val);

                           /* } else {
                                mainfilter.put("status", "");
                            }*/
                            Log.i("9044_filter_string", "" + mainfilter.toString());
                            Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_Deal_Listing.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("filter", mainfilter.toString());
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                dialog_reset_tv_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        filter_dialog.dismiss();
                        Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_Deal_Listing.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("filter", "");
                        startActivity(intent);
                    }
                });
                filter_dialog.show();
            }
        });

        deal_listing.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        ++page_num;
                        list_deals(page_num, request);
                    }
                }
            }
        });
    }

    public void list_deals(int page_count, final boolean call_request) {
        postdata.put("page", "" + page_count);
        if (call_request) {
            Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
                @Override
                public void processFinish(Object output) throws JSONException {
                    if (functionalityList.getVendor_Deals()) {
                        Log.i("9044_Response", "" + output);
                        JSONObject jsonObject = new JSONObject(output.toString());
                        JSONObject jsonObject_data = jsonObject.getJSONObject("data");
                        if (jsonObject_data.has("admin_status")) {
                            JSONArray admin_status_arr = jsonObject_data.getJSONArray("admin_status");
                            for (int i = 0; i < admin_status_arr.length(); i++) {
                                JSONObject dealstatus_obj = admin_status_arr.getJSONObject(i);
                                admin_status.add(dealstatus_obj.getString("value"));
                                admin_status_hasmap.put(dealstatus_obj.getString("value"), dealstatus_obj.getString("key"));
                            }
                        }
                        if (jsonObject_data.has("deal_status")) {
                            JSONArray deal_status_arr = jsonObject_data.getJSONArray("deal_status");
                            for (int i = 0; i < deal_status_arr.length(); i++) {
                                JSONObject dealstatus_obj = deal_status_arr.getJSONObject(i);
                                deal_status.add(dealstatus_obj.getString("value"));
                                deal_status_hasmap.put(dealstatus_obj.getString("value"), dealstatus_obj.getString("key"));
                            }
                        }
                        if (jsonObject_data.getString("success").equals("true")) {
                            JSONArray vendor_list_array = jsonObject_data.getJSONArray("vendordeal_list");
                            //hidelogo("Deal List(" + vendor_list_array.length() + ")");
                            String title_bar_txt = "Deal List(" + vendor_list_array.length() + ")";
                            title_bar.setText(title_bar_txt);
                            for (int vendor_deal_count = 0; vendor_deal_count < vendor_list_array.length(); vendor_deal_count++) {
                                Log.i("9044_product_name", "" + vendor_list_array.getJSONObject(vendor_deal_count).getString("product_name"));
                                s_deal_id = vendor_list_array.getJSONObject(vendor_deal_count).getString("deal_id");
                                s_product_name = vendor_list_array.getJSONObject(vendor_deal_count).getString("product_name");
                                s_end_date = vendor_list_array.getJSONObject(vendor_deal_count).getString("end_date");
                                s_start_date = vendor_list_array.getJSONObject(vendor_deal_count).getString("start_date");
                                s_admin_status = vendor_list_array.getJSONObject(vendor_deal_count).getString("admin_status");
                                s_status = vendor_list_array.getJSONObject(vendor_deal_count).getString("status");
                                s_deal_price = vendor_list_array.getJSONObject(vendor_deal_count).getString("deal_price");
                                s_product_id = vendor_list_array.getJSONObject(vendor_deal_count).getString("product_id");
                                arrayListl.add(new Deal_List_Model(
                                        s_deal_id,
                                        s_product_name,
                                        s_end_date,
                                        s_start_date,
                                        s_admin_status,
                                        s_status,
                                        s_deal_price,
                                        s_product_id
                                ));
                            }
                            adapter = new Deals_List_RecycleViewAdapter(Ced_MultiVendor_Deal_Listing.this, arrayListl);
                            deal_listing.setAdapter(adapter);

                            JSONArray createDealArray = jsonObject_data.getJSONArray("deal_status");
                            session.saveCreateDealStatus(createDealArray.toString());




                            adapter.notifyDataSetChanged();
                        } else {
                            if (jsonObject_data.has("message")) {
                                Log.i("REpo", "" + jsonObject_data.getString("message"));
                                if (page_num == 1) {
                                    reponse_false.setText(jsonObject_data.getString("message"));
                                    deal_listing.setVisibility(View.GONE);
                                }
                                // Toast.makeText(Ced_MultiVendor_Deal_Listing.this, "" + jsonObject_data.getString("message"), Toast.LENGTH_SHORT).show();
                                request = false;
                            }
                        }
                    } else {
                        Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                    }
                }


            }, Ced_MultiVendor_Deal_Listing.this, "POST", postdata);
            response.execute(deal_list_url);
        }
    }
}