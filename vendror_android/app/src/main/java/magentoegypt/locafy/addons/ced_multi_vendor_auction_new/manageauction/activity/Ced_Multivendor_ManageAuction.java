package magentoegypt.locafy.addons.ced_multi_vendor_auction_new.manageauction.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
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
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import magentoegypt.locafy.R;
import magentoegypt.locafy.addons.ced_multi_vendor_auction_new.manageauction.adapter.ManageAuction_Adapter;
import magentoegypt.locafy.api_request_response_section.Ced_ClientRequestResponseRest_New;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Ced_Multivendor_ManageAuction extends Ced_MultiVendor_NavigationActivity {
    ArrayAdapter<String> spinneradapter;
    //    ArrayList<String> sellproduct_value = new ArrayList<>();
    JSONObject sellproduct_label_value = new JSONObject();
    ArrayList<String> sellproduct_label = new ArrayList<>();
    public int page = 1;
    private String getRatingListUrl = "";
    private JSONObject postdata = new JSONObject();
    private RecyclerView recyclerView;
    TextView msg;
    private boolean filterSelected = false;
    ImageView filter_button;
    String selected_sellproduct = "";
    FloatingActionButton add_auction;
    JSONArray status_label;
    Ced_MultiVendor_VendorSessionManagement vendorSessionManagement;
    public JSONArray datalist = new JSONArray();
    Boolean load = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
        getLayoutInflater().inflate(R.layout.activity_ced__multivendor__manage_auction, content, true);
        vendorSessionManagement = new Ced_MultiVendor_VendorSessionManagement(Ced_Multivendor_ManageAuction.this);
        add_auction = findViewById(R.id.add_auction);

        recyclerView = findViewById(R.id.recyclerView);
        filter_button = findViewById(R.id.filter_button);
        msg = findViewById(R.id.msg);
        getRatingListUrl = session.getBase_Url() + "rest/V1/vauctionapi/getAuctionList";
        try {
            postdata.put("vendor_id", vendorSessionManagement.getVendorid());
            request_page_date(page);

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE && load) {
                        load = false;
                        ++page;
                        request_page_date(page);

                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                }
            });

            add_auction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Ced_Multivendor_ManageAuction.this, AddAuction.class);
                    intent.putExtra("status_label", status_label.toString());
                    startActivity(intent);
                }
            });

            filter_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final View filterlayout = getLayoutInflater().inflate(R.layout.filter_manageauction, null);
                    final AlertDialog.Builder alertdialog = new AlertDialog.Builder(Ced_Multivendor_ManageAuction.this);
                    alertdialog.setView(filterlayout);
                    final Dialog dialog = alertdialog.create();
                    AppCompatButton filter = filterlayout.findViewById(R.id.filter);
                    AppCompatButton clearfilter = filterlayout.findViewById(R.id.clearfilter);
                    final AppCompatEditText prod_id_from = filterlayout.findViewById(R.id.prod_id_from);
                    final AppCompatEditText prod_id_to = filterlayout.findViewById(R.id.prod_id_to);
                    final AppCompatEditText name = filterlayout.findViewById(R.id.name);
                    final AppCompatEditText startprice_from = filterlayout.findViewById(R.id.startprice_from);
                    final AppCompatEditText startprice_to = filterlayout.findViewById(R.id.startprice_to);
                    final AppCompatEditText max_price_to = filterlayout.findViewById(R.id.max_price_to);
                    final AppCompatEditText max_price_from = filterlayout.findViewById(R.id.max_price_from);
                    final AppCompatEditText status = filterlayout.findViewById(R.id.status);
                    final Spinner sellproduct = filterlayout.findViewById(R.id.sellproduct);
                    final AppCompatTextView startdate = filterlayout.findViewById(R.id.startdate);
                    final AppCompatTextView enddate = filterlayout.findViewById(R.id.enddate);
                    set_sellproduct_spinner(sellproduct, status_label);
                    try {
                        if (postdata.has("filter")) {
                            JSONObject object = new JSONObject(postdata.getString("filter"));
                            if (object.has("product_id")) {
                                JSONObject product_id = object.getJSONObject("product_id");
                                prod_id_from.setText(product_id.getString("from"));
                                prod_id_to.setText(product_id.getString("to"));
                            }
                            if (object.has("starting_price")) {
                                JSONObject starting_price = object.getJSONObject("starting_price");
                                startprice_from.setText(starting_price.getString("from"));
                                startprice_to.setText(starting_price.getString("to"));
                            }
                            if (object.has("max_price")) {
                                JSONObject max_price = object.getJSONObject("max_price");
                                max_price_from.setText(max_price.getString("from"));
                                max_price_to.setText(max_price.getString("to"));
                            }
                            if (object.has("product_name")) {
                                name.setText(object.getString("product_name"));
                            }
                            if (object.has("status")) {
                                status.setText(object.getString("status"));
                            }
                            if (object.has("start_datetime")) {
                                startdate.setText(object.getString("start_datetime"));
                            }

                            if (object.has("end_datetime")) {
                                enddate.setText(object.getString("end_datetime"));
                            }
                            if (object.has("sellproduct")) {
                                int spinnerPosition = spinneradapter.getPosition(sellproduct_label_value.getString(object.getString("sellproduct")));
                                sellproduct.setSelection(spinnerPosition);
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    startdate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectdate_fromcalender(startdate, "yyyy-MM-dd HH:mm:ss");
                        }
                    });

                    enddate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectdate_fromcalender(enddate, "yyyy-MM-dd HH:mm:ss");
                        }
                    });
                    filter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //----------------------------------------------------------------------
                            try {

                                final JSONObject filterparam = new JSONObject();
                                JSONObject product_id_param = new JSONObject();
                                JSONObject startprice_param = new JSONObject();
                                JSONObject max_price_param = new JSONObject();

                                product_id_param.put("from", prod_id_from.getText().toString());
                                product_id_param.put("to", prod_id_to.getText().toString());
                                filterparam.put("product_id", product_id_param);

                                startprice_param.put("from", startprice_from.getText().toString());
                                startprice_param.put("to", startprice_to.getText().toString());
                                filterparam.put("starting_price", startprice_param);

                                max_price_param.put("from", max_price_from.getText().toString());
                                max_price_param.put("to", max_price_to.getText().toString());
                                filterparam.put("max_price", max_price_param);

                                filterparam.put("product_name", name.getText().toString());
                                filterparam.put("status", status.getText().toString());
                                filterparam.put("start_datetime", startdate.getText().toString());
                                filterparam.put("end_datetime", enddate.getText().toString());
                                filterparam.put("sellproduct", selected_sellproduct);

                                postdata.put("filter", filterparam.toString());
                                page = 1;
                                datalist = new JSONArray();
                                request_page_date(page);
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
                            prod_id_from.setText("");
                            prod_id_to.setText("");
                            name.setText("");
                            startprice_from.setText("");
                            startprice_to.setText("");
                            max_price_to.setText("");
                            max_price_from.setText("");
                            status.setText("");
                            startdate.setText("");
                            enddate.setText("");
                            selected_sellproduct = "";
                            sellproduct.setSelection(0);
                            postdata.remove("filter");
                            page = 1;
                            datalist = new JSONArray();
                            request_page_date(page);
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

    private void set_sellproduct_spinner(Spinner sellproduct, JSONArray status_label) {
        try {
            if (status_label != null) {
                ArrayList<String> sellproduct_value = new ArrayList<>();
                ArrayList<String> sellproduct_label = new ArrayList<>();
                for (int k = 0; k < status_label.length(); k++) {
                    JSONObject object1 = status_label.getJSONObject(k);
                    sellproduct_label.add(object1.getString("label"));
                    sellproduct_value.add(object1.getString("value"));
                    sellproduct_label_value.put(object1.getString("label"), object1.getString("value"));
                }
                spinneradapter = new ArrayAdapter<String>(Ced_Multivendor_ManageAuction.this, R.layout.simple_spinner_dropdown_item, sellproduct_value);
                sellproduct.setAdapter(spinneradapter);
                sellproduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        // selected_sellproduct[0] = parent.getSelectedItem().toString();
                        selected_sellproduct = sellproduct_label.get(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void request_page_date(final int page) {
        try {
            postdata.put("page", page);
            Ced_ClientRequestResponseRest_New response = new Ced_ClientRequestResponseRest_New(new AsyncResponse() {
                @SuppressLint("RestrictedApi")
                @Override
                public void processFinish(Object output) throws JSONException {
                    JSONObject vendor_data = new JSONObject(output.toString()).getJSONObject("vendor_data");
//                    if (vendor_data.getBoolean("success") && vendor_data.get("auction_list")!=null)
                    if (vendor_data.getBoolean("success")) {
                        if (vendor_data.get("auction_list") != null) {
                            load = true;
                            msg.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            JSONArray auction_listArray = vendor_data.getJSONArray("auction_list");
                            for (int k = 0; k < auction_listArray.length(); k++) {
                                datalist.put(auction_listArray.getJSONObject(k));
                            }
                            status_label = vendor_data.getJSONArray("status_label");
                            ManageAuction_Adapter adapter = new ManageAuction_Adapter(datalist, Ced_Multivendor_ManageAuction.this, status_label);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            add_auction.setVisibility(View.VISIBLE);
                        } else {
                            status_label = vendor_data.getJSONArray("status_label");
                        }
                    } else {
                        status_label = vendor_data.getJSONArray("status_label");
                        load = false;
                        if (page == 1) {
                            msg.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            if (vendor_data.has("message")) {
                                msg.setText(vendor_data.getString("message"));
                            }
                        }
                    }
                }
            }, this, "POST", "" + postdata);
            response.execute(getRatingListUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}