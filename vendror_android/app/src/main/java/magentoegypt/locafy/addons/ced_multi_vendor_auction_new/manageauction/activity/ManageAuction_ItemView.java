package magentoegypt.locafy.addons.ced_multi_vendor_auction_new.manageauction.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import magentoegypt.locafy.R;
import magentoegypt.locafy.api_request_response_section.Ced_ClientRequestResponseRest_New;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ManageAuction_ItemView extends Ced_MultiVendor_NavigationActivity {
    private AppCompatEditText name, originalprice, startprice, max_price;
    private AppCompatTextView startdate, enddate;
    Spinner sellproduct;
    ConstraintLayout main;
    AppCompatButton save;
    String Url;
    Ced_MultiVendor_VendorSessionManagement vendorSessionManagement;
    String selected_sellproduct;
    ArrayAdapter<String> spinneradapter;
    String frompage;
    JSONArray status_label;
    ArrayList<String> sellproduct_label = new ArrayList<>();
    ArrayList<String> sellproduct_value = new ArrayList<>();
    JSONObject sellproduct__label_value = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vendorSessionManagement = new Ced_MultiVendor_VendorSessionManagement(ManageAuction_ItemView.this);
        ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
        getLayoutInflater().inflate(R.layout.activity_manage_auction__item_view, content, true);
        main = findViewById(R.id.main);
        save = findViewById(R.id.save);
        sellproduct = findViewById(R.id.sellproduct);
        name = findViewById(R.id.name);
        originalprice = findViewById(R.id.originalprice);
        startprice = findViewById(R.id.startprice);
        max_price = findViewById(R.id.max_price);
        startdate = findViewById(R.id.startdate);
        enddate = findViewById(R.id.enddate);
        sellproduct = findViewById(R.id.sellproduct);
        Url = session.getBase_Url() + "rest/V1/vauctionapi/auctionView";
        try {
            frompage = getIntent().getStringExtra("frompage");
            if (getIntent().hasExtra("id")) // edit auction case otherwise add product to auction case
            {
                startprice.setEnabled(false);
                JSONObject postdata = new JSONObject();
                postdata.put("vendor_id", vendorSessionManagement.getVendorid());
                postdata.put("id", getIntent().getStringExtra("id"));
                request(postdata);
            }
            if (getIntent().hasExtra("name")) {
                name.setText(getIntent().getStringExtra("name"));
            }
            if (getIntent().hasExtra("price")) {
                originalprice.setText(getIntent().getStringExtra("price"));
            }
            set_sellproduct_spinner();
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
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (startprice.getText().toString().isEmpty()) {
                        startprice.setError(getResources().getString(R.string.empty));
                    } else if (max_price.getText().toString().isEmpty()) {
                        max_price.setError(getResources().getString(R.string.empty));
                    } else if (startdate.getText().toString().isEmpty()) {
                        startdate.setError(getResources().getString(R.string.empty));
                    } else if (enddate.getText().toString().isEmpty()) {
                        enddate.setError(getResources().getString(R.string.empty));
                    } else {
                        try {
                            JSONObject param = new JSONObject();
                            param.put("vendor_id", vendorSessionManagement.getVendorid());
                            if (frompage.equalsIgnoreCase("addauction_product")) {
                                param.put("product_id", getIntent().getStringExtra("product_id"));
                            } else {
                                param.put("id", getIntent().getStringExtra("id"));
                            }
                            param.put("product_name", name.getText().toString());
                            param.put("price", originalprice.getText().toString());
                            param.put("starting_price", startprice.getText().toString());
                            param.put("max_price", max_price.getText().toString());
                            param.put("start_datetime", startdate.getText().toString());
                            param.put("end_datetime", enddate.getText().toString());
                            param.put("sellproduct", selected_sellproduct);
                            Ced_ClientRequestResponseRest_New response = new Ced_ClientRequestResponseRest_New(new AsyncResponse() {
                                @Override
                                public void processFinish(Object output) throws JSONException {
                                    JSONObject vendor_data = new JSONObject(output.toString()).getJSONObject("vendor_data");
                                    if (vendor_data.getBoolean("success")) {
                                       /* Intent orderlist=null;
                                        if(frompage.equalsIgnoreCase("manageauction_adpter"))
                                        {
                                            orderlist = new Intent(ManageAuction_ItemView.this, Ced_Multivendor_ManageAuction.class);
                                        }
                                        if(frompage.equalsIgnoreCase("addauction_product"))
                                        {
                                            orderlist = new Intent(ManageAuction_ItemView.this, AddAuction.class);
                                            orderlist.putExtra("status_label",status_label.toString());
                                        }*/
                                        Intent orderlist = new Intent(ManageAuction_ItemView.this, Ced_Multivendor_ManageAuction.class);
                                        orderlist.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        startActivity(orderlist);
                                        overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                                    } else {
                                        finish();
                                    }

                                    if (vendor_data.has("message")) {
                                        Toast.makeText(getApplicationContext(), vendor_data.getString("message"), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, ManageAuction_ItemView.this, "POST", param.toString());
                            response.execute(session.getBase_Url() + "rest/V1/vauctionapi/saveAuction");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void set_sellproduct_spinner() throws JSONException {

        status_label = new JSONArray(getIntent().getStringExtra("status_label"));
        for (int k = 0; k < status_label.length(); k++) {
            JSONObject object1 = status_label.getJSONObject(k);
            sellproduct_label.add(object1.getString("label"));
            sellproduct_value.add(object1.getString("value"));
            sellproduct__label_value.put(object1.getString("label"), object1.getString("value"));
        }
        spinneradapter = new ArrayAdapter<String>(ManageAuction_ItemView.this, R.layout.simple_spinner_dropdown_item, sellproduct_value);
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

    private void request(JSONObject postdata) throws JSONException {
        Ced_ClientRequestResponseRest_New response = new Ced_ClientRequestResponseRest_New(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                JSONObject vendor_data = new JSONObject(output.toString()).getJSONObject("vendor_data");
                if (vendor_data.getBoolean("success") && vendor_data.get("auction_list") != null) {
                    /*product_id,status*/
                    JSONObject auction_info = vendor_data.getJSONObject("auction_info");
                    name.setText(auction_info.getString("product_name"));
                    startprice.setText(auction_info.getString("starting_price"));
                    max_price.setText(auction_info.getString("max_price"));
                    originalprice.setText(auction_info.getString("price"));
                    startdate.setText(auction_info.getString("start_datetime"));
                    enddate.setText(auction_info.getString("end_datetime"));
                    /*sellproduct.setText(auction_info.getString("sellproduct"));*/
                    if (auction_info.has("sellproduct")) {
                        int spinnerPosition = spinneradapter.getPosition(sellproduct__label_value.getString(auction_info.getString("sellproduct")));
                        sellproduct.setSelection(spinnerPosition);
                    }
                } else {
                    if (vendor_data.has("message")) {
                        Toast.makeText(getApplicationContext(), vendor_data.getString("message"), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        }, this, "POST", "" + postdata, true);
        response.execute(Url);
    }
}