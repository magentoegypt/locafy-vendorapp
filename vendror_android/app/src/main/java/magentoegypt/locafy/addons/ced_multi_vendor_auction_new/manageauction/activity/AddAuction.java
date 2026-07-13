package magentoegypt.locafy.addons.ced_multi_vendor_auction_new.manageauction.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.RecyclerView;

import magentoegypt.locafy.R;
import magentoegypt.locafy.addons.ced_multi_vendor_auction_new.manageauction.adapter.AuctionProductList_Adapter;
import magentoegypt.locafy.api_request_response_section.Ced_ClientRequestResponseRest_New;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddAuction extends Ced_MultiVendor_NavigationActivity {
    private int page = 1;
    private String getproductlistUrl = "";
    private JSONObject postdata = new JSONObject();
    private RecyclerView recyclerView;
    TextView msg;
    JSONArray datalist = new JSONArray();
    private boolean filterSelected = false;
    ImageView filter_button;
    String selected_filter_status = "";
    FloatingActionButton add_auction;
    Ced_MultiVendor_VendorSessionManagement vendorSessionManagement;
    JSONArray status_label, type_spinner_labels;
    ArrayAdapter<String> spinneradapter;
    String selected_typespinner;
    JSONObject typeproduct_label_value;
//    final JSONObject typeproduct_label_value = new JSONObject();
//    final ArrayList<String> typeproduct_label = new ArrayList<>();
//    final ArrayList<String> typeproduct_value = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
        getLayoutInflater().inflate(R.layout.activity_add_auction, content, true);
        vendorSessionManagement = new Ced_MultiVendor_VendorSessionManagement(AddAuction.this);
        recyclerView = findViewById(R.id.recyclerView);
        filter_button = findViewById(R.id.filter_button);
        msg = findViewById(R.id.msg);
        getproductlistUrl = session.getBase_Url() + "rest/V1/vauctionapi/getProductList";
        try {
            status_label = new JSONArray(getIntent().getStringExtra("status_label").toString());
            postdata.put("vendor_id", vendorSessionManagement.getVendorid());
            request_page_data(page);
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                        ++page;
                        request_page_data(page);
                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                }
            });

            filter_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final View filterlayout = getLayoutInflater().inflate(R.layout.filter_addauctionproductlist, null);
                    final AlertDialog.Builder alertdialog = new AlertDialog.Builder(AddAuction.this);
                    alertdialog.setView(filterlayout);
                    final Dialog dialog = alertdialog.create();
                    AppCompatButton filter = filterlayout.findViewById(R.id.filter);
                    AppCompatButton clearfilter = filterlayout.findViewById(R.id.clearfilter);
                    final AppCompatEditText sku = filterlayout.findViewById(R.id.sku);
                    final Spinner typespinner = filterlayout.findViewById(R.id.typespinner);
                    set_type_spinner(typespinner, type_spinner_labels);
                    try {
                        if (postdata.has("filter")) {
                            JSONObject object = new JSONObject(postdata.getString("filter"));

                            if (object.has("product_sku")) {
                                sku.setText(object.getString("product_sku"));
                            }
                            if (object.has("type")) {
                                int spinnerPosition = spinneradapter.getPosition(typeproduct_label_value.getString(object.getString("type")));
                                typespinner.setSelection(spinnerPosition);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    filter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //----------------------------------------------------------------------
                            try {

                                final JSONObject filterparam = new JSONObject();
                                filterparam.put("product_sku", sku.getText().toString());
                                filterparam.put("type", selected_typespinner);

                                postdata.put("filter", filterparam.toString());
                                page = 1;
                                datalist = new JSONArray();
                                request_page_data(page);
                                dialog.hide();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //----------------------------------------------------------------------

                        }
                    });

                    clearfilter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sku.setText("");
                            selected_typespinner = "";
                            typespinner.setSelection(0);
                            postdata.remove("filter");
                            page = 1;
                            datalist = new JSONArray();
                            request_page_data(page);
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void set_type_spinner(Spinner typespinner, JSONArray type_spinner_labels) {
        try {
            ArrayList<String> typeproduct_label = new ArrayList<>();
            ArrayList<String> typeproduct_value = new ArrayList<>();
            typeproduct_label_value = new JSONObject();
            for (int k = 0; k < type_spinner_labels.length(); k++) {
                JSONObject object1 = type_spinner_labels.getJSONObject(k);
                typeproduct_value.add(object1.getString("value"));
                typeproduct_label.add(object1.getString("label"));
                typeproduct_label_value.put(object1.getString("label"), object1.getString("value"));
            }
            spinneradapter = new ArrayAdapter<String>(AddAuction.this, R.layout.simple_spinner_dropdown_item, typeproduct_value);
            typespinner.setAdapter(spinneradapter);
            typespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selected_typespinner = typeproduct_label.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void request_page_data(final int page) {
        try {
            postdata.put("page", page);
            Ced_ClientRequestResponseRest_New response = new Ced_ClientRequestResponseRest_New(new AsyncResponse() {
                @Override
                public void processFinish(Object output) throws JSONException {
                    JSONObject vendor_data = new JSONObject(output.toString()).getJSONObject("vendor_data");
                    if (vendor_data.getBoolean("success") && vendor_data.get("product_list") != null) {
                        msg.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        JSONArray product_listArray = vendor_data.getJSONArray("product_list");
                        type_spinner_labels = vendor_data.getJSONArray("type");
                        for (int k = 0; k < product_listArray.length(); k++) {
                            datalist.put(product_listArray.getJSONObject(k));
                        }
                        AuctionProductList_Adapter adapter = new AuctionProductList_Adapter(datalist, AddAuction.this, status_label);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    } else {
                        if (page == 1) {
                            msg.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            if (vendor_data.has("message")) {
                                msg.setText(vendor_data.getString("message"));
                            }
                        }
                    }
                }
            }, this, "POST", "" + postdata, true);
            response.execute(getproductlistUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}