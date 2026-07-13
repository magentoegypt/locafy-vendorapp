package magentoegypt.locafy.vendor_deals;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cedcoss on 9/1/18.
 */

public class Ced_MultiVendor_Create_and_Edit_deal extends Ced_MultiVendor_NavigationActivity {
    HashMap<String, String> postdata;
    String message_save_url = "";
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    ArrayList<Create_Deal_Product_List_Model> arrayListl;
    Ced_MultiVendor_FontSetting fontSetting;
    RecyclerView products_listing_for_deal;
    String s_product_id, s_product_name, s_product_type;
    String s_product_status;
    String s_product_quantity;
    String s_product_price;
    GridLayoutManager layoutManager;
    LinearLayout product_filter;
    Button titlebar;
    Create_Product_Listing_RecycleViewAdapter adapter;
    TextView product_id_title, product_name_tv, product_price_title,
            product_qty_title, deal_status_title, admin_status_title, response_false;
    AppCompatEditText price_edt, quantity_edt, product_id_edt, product_name_edt;
    Spinner product_type_spinner, status_spinner;
    TextView dialog_reset_tv_btn, dialog_apply_tv_btn;
    RelativeLayout product_filter_dialog_view;
    String datafilterjson = "";
    Toolbar mToolbar;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    int page_num = 1;
    int count = 0;
    private boolean request = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
        getLayoutInflater().inflate(R.layout.ced_multivendor_create_and_edit_deals, content, true);
        arrayListl = new ArrayList<>();
        message_save_url = session.getBase_Url() + "vdealapi/deal/listing";

        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(Ced_MultiVendor_Create_and_Edit_deal.this);
        products_listing_for_deal = findViewById(R.id.products_listing_for_deal);
        product_filter = findViewById(R.id.product_filter);
        titlebar = findViewById(R.id.title_bar);
        layoutManager = new GridLayoutManager(Ced_MultiVendor_Create_and_Edit_deal.this, 1);
//        layoutManager.setAutoMeasureEnabled(true);
        products_listing_for_deal.setHasFixedSize(false);
        response_false = findViewById(R.id.response_false);
        products_listing_for_deal.setLayoutManager(layoutManager);
        products_listing_for_deal.setNestedScrollingEnabled(false);
        fontSetting = new Ced_MultiVendor_FontSetting();
        mToolbar = new Toolbar(Ced_MultiVendor_Create_and_Edit_deal.this);
        titlebar.setText(R.string.CreateDeals);
        // hidelogo("Create Deals");
        postdata = new HashMap<>();
        if (getIntent().getStringExtra("filter") != null) {
            datafilterjson = getIntent().getStringExtra("filter");
            postdata.put("filter", datafilterjson);
        }
        postdata.put("vendor_id", session.getVendorid());
        postdata.put("hashkey", session.getHahkey());
        create_deal_url(page_num, request);

        product_filter.setOnClickListener(view -> {
            final Dialog filter_dialog = new Dialog(Ced_MultiVendor_Create_and_Edit_deal.this);
            filter_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            filter_dialog.setContentView(R.layout.product_filter_dialog_layout);
            product_filter_dialog_view = filter_dialog.findViewById(R.id.product_filter_dialog_view);
            product_id_title = filter_dialog.findViewById(R.id.product_id_title);
            product_name_tv = filter_dialog.findViewById(R.id.product_name_title);
            product_price_title = filter_dialog.findViewById(R.id.product_price_title);
            product_qty_title = filter_dialog.findViewById(R.id.product_qty_title);
            deal_status_title = filter_dialog.findViewById(R.id.deal_status_title);
            admin_status_title = filter_dialog.findViewById(R.id.status_title);
            product_id_edt = filter_dialog.findViewById(R.id.product_id_edt);
            product_name_edt = filter_dialog.findViewById(R.id.product_name_edt);
            price_edt = filter_dialog.findViewById(R.id.product_price_edt);
            quantity_edt = filter_dialog.findViewById(R.id.quantity_edt);
            product_type_spinner = filter_dialog.findViewById(R.id.product_type_spinner);
            status_spinner = filter_dialog.findViewById(R.id.status_spinner);
            dialog_reset_tv_btn = filter_dialog.findViewById(R.id.dialog_reset_btn);
            dialog_apply_tv_btn = filter_dialog.findViewById(R.id.dialog_apply_btn);
            if (!datafilterjson.isEmpty()) {
                try {
                    JSONObject object = new JSONObject(datafilterjson);
                    product_id_edt.setText(object.getString("entity_id"));
                    product_name_edt.setText(object.getString("sku"));
                    price_edt.setText(object.getString("price"));
                    quantity_edt.setText(object.getString("qty"));
                    if (object.getString("type_id").equals("Simple")) {
                        product_type_spinner.setSelection(1);
                    } else if (object.getString("type_id").equals("Virtual")) {
                        product_type_spinner.setSelection(2);
                    } else if (object.getString("type_id").equals("Downloadable")) {
                        product_type_spinner.setSelection(3);
                    } else if (object.getString("type_id").equals("Grouped")) {
                        product_type_spinner.setSelection(4);
                    } else if (object.getString("type_id").equals("Bundle")) {
                        product_type_spinner.setSelection(5);
                    } else if (object.getString("type_id").equals("Configurable")) {
                        product_type_spinner.setSelection(6);
                    } else {
                        product_type_spinner.setSelection(0);
                    }
                    if (object.getString("status").equals("1")) {
                        status_spinner.setSelection(1);
                    } else if (object.getString("status").equals("2")) {
                        status_spinner.setSelection(2);
                    } else {
                        status_spinner.setSelection(0);
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
                        mainfilter.put("qty", quantity_edt.getText());
                        mainfilter.put("entity_id", product_id_edt.getText());
                        mainfilter.put("sku", product_name_edt.getText());
                        mainfilter.put("price", price_edt.getText());
                        if (!status_spinner.getSelectedItem().toString().equals("Select Status")) {
                            if (status_spinner.getSelectedItem().toString().equals("Enable")) {
                                mainfilter.put("status", "1");
                            } else if (status_spinner.getSelectedItem().toString().equals("Disable")) {
                                mainfilter.put("status", "2");
                            }
                        } else {
                            mainfilter.put("status", "");
                        }
                        if (!product_type_spinner.getSelectedItem().toString().equals("Select Type")) {
                            mainfilter.put("type_id", product_type_spinner.getSelectedItem());
                        } else {
                            mainfilter.put("type_id", "");
                        }
                        Log.i("9044_filter_string", "" + mainfilter.toString());
                        Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_Create_and_Edit_deal.class);
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
                    Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_Create_and_Edit_deal.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("filter", "");
                    startActivity(intent);
                }
            });
            filter_dialog.show();
        });

        products_listing_for_deal.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        ++page_num;
                        create_deal_url(page_num, request);
                    }
                }
            }
        });
    }

    public void create_deal_url(int page_count, final boolean call_request) {
        postdata.put("page", "" + page_count);
        if (call_request) {
            Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
                @Override
                public void processFinish(Object output) throws JSONException {
                    if (functionalityList.getVendor_Deals()) {
                        Log.i("9044_Response", "" + output);
                        JSONObject jsonObject = new JSONObject(output.toString());
                        JSONObject jsonObject_data = jsonObject.getJSONObject("data");
                        if (jsonObject_data.getString("success").equals("true")) {
                            JSONArray vendor_list_array = jsonObject_data.getJSONArray("vendordeal_productlist");
                            //hidelogo("Create Deals- Products(" + vendor_list_array.length() + ")");
                            String titlebar_txt;
                            if (count != 0) {
                                titlebar_txt = "Create Deals- Products(" + (vendor_list_array.length() + count) + ")";
                            } else {
                                titlebar_txt = "Create Deals- Products(" + vendor_list_array.length() + ")";
                            }
                            titlebar.setText(titlebar_txt);
                            count = vendor_list_array.length();
                            for (int vendor_deal_count = 0; vendor_deal_count < vendor_list_array.length(); vendor_deal_count++) {
                                Log.i("9044_product_name", "" + vendor_list_array.length());
                                s_product_id = vendor_list_array.getJSONObject(vendor_deal_count).getString("product_id");
                                s_product_name = vendor_list_array.getJSONObject(vendor_deal_count).getString("product_name");
                                s_product_quantity = vendor_list_array.getJSONObject(vendor_deal_count).getString("qty");
                                s_product_status = vendor_list_array.getJSONObject(vendor_deal_count).getString("check_status");
                                s_product_type = vendor_list_array.getJSONObject(vendor_deal_count).getString("type");
                                s_product_price = vendor_list_array.getJSONObject(vendor_deal_count).getString("price");
                                arrayListl.add(new Create_Deal_Product_List_Model(
                                        s_product_id,
                                        s_product_name,
                                        s_product_type,
                                        s_product_price,
                                        s_product_quantity,
                                        s_product_status
                                ));
                            }
                            adapter = new Create_Product_Listing_RecycleViewAdapter(Ced_MultiVendor_Create_and_Edit_deal.this, arrayListl);
                            products_listing_for_deal.setAdapter(adapter);


                            JSONArray createDealArray = jsonObject_data.getJSONArray("deal_status");
                            session.saveCreateDealStatus(createDealArray.toString());

                            adapter.notifyDataSetChanged();
                        } else {
                            if (jsonObject_data.has("message")) {
                                Log.i("REpo", "" + jsonObject_data.getString("message"));
                                // Toast.makeText(Ced_MultiVendor_Create_and_Edit_deal.this, "" + jsonObject_data.getString("message"), Toast.LENGTH_SHORT).show();
                                if (page_num == 1) {
                                    response_false.setVisibility(View.VISIBLE);
                                    response_false.setText(jsonObject_data.getString("message"));
                                }
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


            }, Ced_MultiVendor_Create_and_Edit_deal.this, "POST", postdata);
            response.execute(message_save_url);
        }
    }
}