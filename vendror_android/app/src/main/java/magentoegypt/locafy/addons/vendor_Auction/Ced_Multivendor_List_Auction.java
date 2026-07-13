package magentoegypt.locafy.addons.vendor_Auction;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import  magentoegypt.locafy.R;
import  magentoegypt.locafy_constant.AppConstant;
import  magentoegypt.locafy.base_app.Ced_MultiVendor_FontSetting;
import  magentoegypt.locafy.base_app.Ced_MultiVendor_VendorSplash;
import  magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import  magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import  magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import  magentoegypt.locafy.api_request_response_section.AsyncResponse;
import  magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import  magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by cedcoss on 21/1/18.
 */

public class Ced_Multivendor_List_Auction extends Ced_MultiVendor_NavigationActivity {
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorSessionManagement vendorSessionManagement;
    String url;
    HashMap<String, String> data;
    ArrayList<HashMap<String, String>> adapter_data;
    ListView auction_list;
    int current = 1;
    boolean load = true;
    HashMap<String, String> dataforproducts;


    String id;
    String product_id;
    String product_name;
    String starting_price;
    String max_price;
    String start_date_time;
    String end_date_time;
    String sellproduct;
    String status;
    String vendor_prod_auction_staus;
    Dialog listDialog;
    LinearLayout MultiVendor_filtersection;
    Ced_MultiVendor_FontSetting fontSetting;
    String datafilterjson = "";
    String navigation = " ";
    final Calendar myCalendar = Calendar.getInstance();
    public static EditText MultiVendor_edt_start_date;
    public static EditText MultiVendor_edt_end_date;
    Boolean select_date = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        vendorSessionManagement = new Ced_MultiVendor_VendorSessionManagement(getApplicationContext());
        url = session.getBase_Url()+ "vauctionapi/auction/item";
        dataforproducts = new HashMap<String, String>();
        data = new HashMap<>();
        adapter_data = new ArrayList<>();

        fontSetting = new Ced_MultiVendor_FontSetting();


        if (connectionDetector.isConnectingToInternet()) {
            ViewGroup content = (ViewGroup) findViewById(R.id.MultiVendor_frame_container);
            getLayoutInflater().inflate(R.layout.ced_multivendor_list_auction, content, true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);

            auction_list = (ListView) findViewById(R.id.list_auction_listing);
            MultiVendor_filtersection = (LinearLayout) findViewById(R.id.MultiVendor_filtersection);

            dataforproducts.put("hashkey", vendorSessionManagement.getHahkey());
            dataforproducts.put("vendor_id", vendorSessionManagement.getVendorid());

            MultiVendor_filtersection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppConstant.lockButton(v);
                    showfilter();
                }
            });


            if (getIntent().getStringExtra("filter") != null) {
                datafilterjson = getIntent().getStringExtra("filter");
                dataforproducts.put("filter", getIntent().getStringExtra("filter"));
                Log.i("REpo", "filters-- " + datafilterjson);
            }
            Log.i("REpo", "filters-- " + datafilterjson);

            requestData();

            /********************************************* Pagination **********************************************/

            auction_list.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView absListView, int i) {

                }

                @Override
                public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                    if (firstVisibleItem == 0) {
                        getSupportActionBar().show();
                    } else {
                        getSupportActionBar().hide();
                    }

                    if (((firstVisibleItem + visibleItemCount) == totalItemCount) && load) {
                        current = current + 1;
                        load = false;
                        Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {

                            @Override
                            public void processFinish(Object output) throws JSONException {
                                HashMap<String, String> inner_data;

                                JSONObject jsonObject = new JSONObject(output.toString());

                                String success = jsonObject.getJSONObject("data").getString("success");

                                if (success.equals("true")) {
                                    JSONArray auction_products = jsonObject.getJSONObject("data").getJSONArray("auctioned_product");
                                    JSONObject jo_inner;

                                    for (int i = 0; i < auction_products.length(); i++) {
                                        inner_data = new HashMap<>();
                                        jo_inner = auction_products.getJSONObject(i);

                                        id = jo_inner.getString("id");
                                        product_id = jo_inner.getString("product_id");
                                        product_name = jo_inner.getString("product_name");
                                        starting_price = jo_inner.getString("starting_price");
                                        max_price = jo_inner.getString("max_price");
                                        start_date_time = jo_inner.getString("start_datetime");
                                        end_date_time = jo_inner.getString("end_datetime");
                                        sellproduct = jo_inner.getString("sellproduct");
                                        status = jo_inner.getString("status");
                                        vendor_prod_auction_staus = jo_inner.getString("vendor_auction_status");

                                        inner_data.put("id", id);
                                        inner_data.put("product_id", product_id);
                                        inner_data.put("product_name", product_name);
                                        inner_data.put("starting_price", starting_price);
                                        inner_data.put("max_price", max_price);
                                        inner_data.put("start_date_time", start_date_time);
                                        inner_data.put("end_date_time", end_date_time);
                                        inner_data.put("sellproduct", sellproduct);
                                        inner_data.put("status", status);
                                        inner_data.put("vendor_prod_auction_status", vendor_prod_auction_staus);

                                        Log.d("listdata-id", "" + jo_inner.getString("id"));
                                        Log.d("listdata-product_id", "" + jo_inner.getString("product_id"));
                                        Log.d("listdata-product_name", "" + jo_inner.getString("product_name"));
                                        Log.d("listdata-starting_price", "" + jo_inner.getString("starting_price"));
                                        Log.d("listdata-max_price", "" + jo_inner.getString("max_price"));
                                        Log.d("listdata-start_datetime", "" + jo_inner.getString("start_datetime"));
                                        Log.d("listdata-end_datetime", "" + jo_inner.getString("end_datetime"));
                                        Log.d("listdata-sellproduct", "" + jo_inner.getString("sellproduct"));
                                        Log.d("listdata-status", "" + jo_inner.getString("status"));
                                        Log.d("listdata-auction_status", "" + jo_inner.getString("vendor_auction_status"));

                                        adapter_data.add(inner_data);
                                    }

                                    Ced_Multivendor_Auction_List_Adapter adapter = new Ced_Multivendor_Auction_List_Adapter(Ced_Multivendor_List_Auction.this, adapter_data);
                                    int cp = auction_list.getFirstVisiblePosition();
                                    auction_list.setAdapter(adapter);
                                    auction_list.setSelectionFromTop(cp + 1, 0);
                                    adapter.notifyDataSetChanged();
                                    load = true;
                                }

                            }
                        }, Ced_Multivendor_List_Auction.this, "POST", dataforproducts);
                        crr.execute(url + "/page/" + current);
                    }


                }
            });

            /********************************************* Pagination **********************************************/


        } else {
            Intent nointernet = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(nointernet);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }


    }

    private void requestData() {

        final Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
               try {
                   JSONObject root = new JSONObject(output.toString());
                   final JSONObject data = root.getJSONObject("data");
                   HashMap<String, String> inner_data;


                   if (data.getString("success").equalsIgnoreCase("true")) {
                       JSONArray auction_products = data.getJSONArray("auctioned_product");
                       JSONObject jo_inner;

                       for (int i = 0; i < auction_products.length(); i++) {
                           inner_data = new HashMap<>();
                           jo_inner = auction_products.getJSONObject(i);

                           id = jo_inner.getString("id");
                           product_id = jo_inner.getString("product_id");
                           product_name = jo_inner.getString("product_name");
                           starting_price = jo_inner.getString("starting_price");
                           max_price = jo_inner.getString("max_price");
                           start_date_time = jo_inner.getString("start_datetime");
                           end_date_time = jo_inner.getString("end_datetime");
                           sellproduct = jo_inner.getString("sellproduct");
                           status = jo_inner.getString("status");
                           vendor_prod_auction_staus = jo_inner.getString("vendor_auction_status");

                           inner_data.put("id", id);
                           inner_data.put("product_id", product_id);
                           inner_data.put("product_name", product_name);
                           inner_data.put("starting_price", starting_price);
                           inner_data.put("max_price", max_price);
                           inner_data.put("start_date_time", start_date_time);
                           inner_data.put("end_date_time", end_date_time);
                           inner_data.put("sellproduct", sellproduct);
                           inner_data.put("status", status);
                           inner_data.put("vendor_prod_auction_status", vendor_prod_auction_staus);

                           Log.d("listdata-id", "" + jo_inner.getString("id"));
                           Log.d("listdata-product_id", "" + jo_inner.getString("product_id"));
                           Log.d("listdata-product_name", "" + jo_inner.getString("product_name"));
                           Log.d("listdata-starting_price", "" + jo_inner.getString("starting_price"));
                           Log.d("listdata-max_price", "" + jo_inner.getString("max_price"));
                           Log.d("listdata-start_datetime", "" + jo_inner.getString("start_datetime"));
                           Log.d("listdata-end_datetime", "" + jo_inner.getString("end_datetime"));
                           Log.d("listdata-sellproduct", "" + jo_inner.getString("sellproduct"));
                           Log.d("listdata-status", "" + jo_inner.getString("status"));
                           Log.d("listdata-auction_status", "" + jo_inner.getString("vendor_auction_status"));

                           adapter_data.add(inner_data);
                       }
                       Ced_Multivendor_Auction_List_Adapter adapter = new Ced_Multivendor_Auction_List_Adapter(Ced_Multivendor_List_Auction.this, adapter_data);
                       auction_list.setAdapter(adapter);

                       /****************************************************************************************/

                       /****************************************************************************************/
                   } else {
                       Toast.makeText(getApplicationContext(), data.getString("message"), Toast.LENGTH_LONG).show();
                   }
               }catch (Exception e){
                   e.printStackTrace();
               }
            }
        }, Ced_Multivendor_List_Auction.this, "POST", dataforproducts);
        crr.execute(url + "/page/" + current);
    }


    private void showfilter() {
        try {
            listDialog = new Dialog(this, R.style.PauseDialog);
            listDialog.setTitle(getResources().getString(R.string.alert_name));
            LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = li.inflate(R.layout.ced_multivendor_auction_list_filter, null, false);

            final TextView MultiVendor_txt_product_id = (TextView) v.findViewById(R.id.MultiVendor_txt_product_id);
            final TextView MultiVendor_txt_product_name = (TextView) v.findViewById(R.id.MultiVendor_txt_product_name);
            final TextView MultiVendor_txt_startingprice = (TextView) v.findViewById(R.id.MultiVendor_txt_startingprice);
            final TextView MultiVendor_txt_max_price = (TextView) v.findViewById(R.id.MultiVendor_txt_max_price);
            final TextView MultiVendor_txt_startdate = (TextView) v.findViewById(R.id.MultiVendor_txt_startdate);
            final TextView MultiVendor_txt_end_date = (TextView) v.findViewById(R.id.MultiVendor_txt_end_date);
            final TextView MultiVendor_txt_sellingproduct = (TextView) v.findViewById(R.id.MultiVendor_txt_sellingproduct);
            final TextView MultiVendor_txt_status = (TextView) v.findViewById(R.id.MultiVendor_txt_status);
            final TextView MultiVendor_vendor_auction = (TextView) v.findViewById(R.id.MultiVendor_vendor_auction);

            final EditText MultiVendor_edt_id = (EditText) v.findViewById(R.id.MultiVendor_edt_id);
            final EditText MultiVendor_edt_product_id = (EditText) v.findViewById(R.id.MultiVendor_edt_product_id);
            final EditText MultiVendor_edt_product_name = (EditText) v.findViewById(R.id.MultiVendor_edt_product_name);
            final EditText MultiVendor_edt_startingprice = (EditText) v.findViewById(R.id.MultiVendor_edt_startingprice);
            final EditText MultiVendor_edt_maxprice = (EditText) v.findViewById(R.id.MultiVendor_edt_maxprice);
            MultiVendor_edt_start_date = (EditText) v.findViewById(R.id.MultiVendor_edt_start_date);
            MultiVendor_edt_end_date = (EditText) v.findViewById(R.id.MultiVendor_edt_end_date);
            final Spinner MultiVendor_spnr_sell_product = (Spinner) v.findViewById(R.id.MultiVendor_spnr_sell_product);
            final Spinner MultiVendor_spnr_staus = (Spinner) v.findViewById(R.id.MultiVendor_spnr_staus);
            final Spinner MultiVendor_spnr_vendor_auction = (Spinner) v.findViewById(R.id.MultiVendor_spnr_vendor_auction);

            final TextView MultiVendor_setfilter = (TextView) v.findViewById(R.id.MultiVendor_setfilter);
            final TextView MultiVendor_unsetfilter = (TextView) v.findViewById(R.id.MultiVendor_unsetfilter);




          /*  final DatePickerDialog.OnDateSetListener start_date = new DatePickerDialog.OnDateSetListener()
            {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    // TODO Auto-generated method stub
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateLabel(MultiVendor_edt_start_date);
                }

            };

            MultiVendor_edt_start_date.setOnClickListener(new View.OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    // TODO Auto-generated method stub
                    new DatePickerDialog(Ced_Multivendor_List_Auction.this, start_date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });

            final DatePickerDialog.OnDateSetListener end_date = new DatePickerDialog.OnDateSetListener()
            {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    // TODO Auto-generated method stub
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateLabel(MultiVendor_edt_end_date);
                }

            };

            MultiVendor_edt_end_date.setOnClickListener(new View.OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    // TODO Auto-generated method stub
                    new DatePickerDialog(Ced_Multivendor_List_Auction.this, end_date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });*/


            MultiVendor_edt_start_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppConstant.lockButton(view);
                    select_date = true;
                    showTruitonDatePickerDialog(view);
                }
            });


            MultiVendor_edt_end_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppConstant.lockButton(view);
                    if (select_date) {
                        showToDatePickerDialog(view);

                    } else

                    {
                        Toast.makeText(Ced_Multivendor_List_Auction.this, R.string.please_fill_both_dates, Toast.LENGTH_SHORT).show();
                    }
                }
            });


            fontSetting.setFontforTextviews(MultiVendor_txt_product_name, "Roboto-Medium.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(MultiVendor_txt_startingprice, "Roboto-Medium.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(MultiVendor_txt_max_price, "Roboto-Medium.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(MultiVendor_txt_startdate, "Roboto-Medium.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(MultiVendor_txt_end_date, "Roboto-Medium.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(MultiVendor_txt_sellingproduct, "Roboto-Medium.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(MultiVendor_txt_status, "Roboto-Medium.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(MultiVendor_vendor_auction, "Roboto-Medium.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(MultiVendor_txt_product_id, "Roboto-Medium.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(MultiVendor_setfilter, "Roboto-Medium.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(MultiVendor_unsetfilter, "Roboto-Medium.ttf", getApplicationContext());

            if (!(datafilterjson.isEmpty())) {
                JSONObject object = new JSONObject(datafilterjson);

                MultiVendor_edt_id.setText(object.getString("id"));
                MultiVendor_edt_product_id.setText(object.getString("product_id"));
                MultiVendor_edt_product_name.setText(object.getString("product_name"));
                MultiVendor_edt_startingprice.setText(object.getString("starting_price"));
                MultiVendor_edt_maxprice.setText(object.getString("max_price"));
                MultiVendor_edt_start_date.setText(object.getString("start_datetime"));
                MultiVendor_edt_end_date.setText(object.getString("end_datetime"));

                if (object.getString("sellproduct").equals("Select"))
                    MultiVendor_spnr_sell_product.setSelection(0);
                if (object.getString("sellproduct").equals("Yes"))
                    MultiVendor_spnr_sell_product.setSelection(1);
                if (object.getString("sellproduct").equals("No"))
                    MultiVendor_spnr_sell_product.setSelection(2);

                if (object.getString("status").equals("Select"))
                    MultiVendor_spnr_staus.setSelection(0);
                if (object.getString("status").equals("Processing"))
                    MultiVendor_spnr_staus.setSelection(1);
                if (object.getString("status").equals("Cancelled"))
                    MultiVendor_spnr_staus.setSelection(2);
                if (object.getString("status").equals("Completed"))
                    MultiVendor_spnr_staus.setSelection(3);
                if (object.getString("status").equals("Bid Closed"))
                    MultiVendor_spnr_staus.setSelection(4);

                if (object.getString("status").equals("Select"))
                    MultiVendor_spnr_vendor_auction.setSelection(0);
                if (object.getString("status").equals("Approved"))
                    MultiVendor_spnr_vendor_auction.setSelection(1);
                if (object.getString("status").equals("Disapproved"))
                    MultiVendor_spnr_vendor_auction.setSelection(2);

                /*from_price.setText(object.getJSONObject("price").getString("from"));
                to_price.setText(object.getJSONObject("price").getString("to"));
                from_qty.setText(object.getJSONObject("qty").getString("from"));
                to_qty.setText(object.getJSONObject("qty").getString("to"));
                from_product_id.setText(object.getJSONObject("entity_id").getString("from"));
                to_product_id.setText(object.getJSONObject("entity_id").getString("to"));
                edt_productname.setText(object.getString("name"));
                edt_productsku.setText(object.getString("sku"));

                if(object.getString("check_status").equals("Approved"))
                {
                    edt_status.setSelection(1);
                }
                if(object.getString("check_status").equals("Pending"))
                {
                    edt_status.setSelection(2);
                }
                if(object.getString("check_status").equals("Disapproved"))
                {
                    edt_status.setSelection(3);
                }
                if(object.getString("type_id").equals("Simple"))
                {
                    edt_producttype.setSelection(1);
                }
                if(object.getString("type_id").equals("Virtual"))
                {
                    edt_producttype.setSelection(2);
                }*/

                // Toast.makeText(this, "Not Empty", Toast.LENGTH_SHORT).show();

            }

            MultiVendor_unsetfilter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppConstant.lockButton(v);
                    MultiVendor_edt_id.setText("");
                    MultiVendor_edt_product_id.setText("");
                    MultiVendor_edt_product_name.setText("");
                    MultiVendor_edt_startingprice.setText("");
                    MultiVendor_edt_maxprice.setText("");
                    MultiVendor_edt_start_date.setText("");
                    MultiVendor_edt_end_date.setText("");

                    MultiVendor_spnr_sell_product.setSelection(0);
                    MultiVendor_spnr_staus.setSelection(0);
                    MultiVendor_spnr_vendor_auction.setSelection(0);

                    datafilterjson = "";
                    final JSONObject mainfilter = new JSONObject();
                    JSONObject pricejsonObject = new JSONObject();

                    try {
                        listDialog.dismiss();
                        mainfilter.put("id", MultiVendor_edt_id.getText());
                        mainfilter.put("product_id", MultiVendor_edt_product_id.getText());
                        mainfilter.put("product_name", MultiVendor_edt_product_name.getText());
                        mainfilter.put("starting_price", MultiVendor_edt_startingprice.getText());
                        mainfilter.put("max_price", MultiVendor_edt_maxprice.getText());
                        mainfilter.put("start_datetime", MultiVendor_edt_start_date.getText());
                        mainfilter.put("end_datetime", MultiVendor_edt_end_date.getText());
                        mainfilter.put("sellproduct", MultiVendor_spnr_sell_product.getSelectedItem());
                        mainfilter.put("status", MultiVendor_spnr_staus.getSelectedItem());
                        mainfilter.put("vendor_auction_status", MultiVendor_spnr_vendor_auction.getSelectedItem());


                        Intent intent = new Intent(getApplicationContext(), Ced_Multivendor_List_Auction.class);
                        intent.putExtra("filter", mainfilter.toString());
                        startActivity(intent);

                    } catch (JSONException e) {
                        Intent main = new Intent(Ced_Multivendor_List_Auction.this, Ced_MultiVendor_VendorSplash.class);
                        main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(main);
                        overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                    }


                }
            });

            MultiVendor_setfilter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppConstant.lockButton(v);
                    final JSONObject mainfilter = new JSONObject();
                    JSONObject pricejsonObject = new JSONObject();
                    try {
                        listDialog.dismiss();

                        mainfilter.put("id", MultiVendor_edt_id.getText().toString());
                        mainfilter.put("product_id", MultiVendor_edt_product_id.getText().toString());
                        mainfilter.put("product_name", MultiVendor_edt_product_name.getText().toString());
                        mainfilter.put("starting_price", MultiVendor_edt_startingprice.getText().toString());
                        mainfilter.put("max_price", MultiVendor_edt_maxprice.getText().toString());
                        mainfilter.put("start_datetime", MultiVendor_edt_start_date.getText().toString());
                        mainfilter.put("end_datetime", MultiVendor_edt_end_date.getText().toString());

                        if (MultiVendor_spnr_sell_product.getSelectedItem().equals("Select")) {
                            mainfilter.put("sellproduct", "");
                        } else {
                            mainfilter.put("sellproduct", MultiVendor_spnr_sell_product.getSelectedItem().toString());
                        }


                        if (MultiVendor_spnr_staus.getSelectedItem().equals("Select")) {
                            mainfilter.put("status", "");
                        } else {
                            mainfilter.put("status", MultiVendor_spnr_staus.getSelectedItem().toString());
                        }


                        if (MultiVendor_spnr_vendor_auction.getSelectedItem().equals("Select")) {
                            mainfilter.put("vendor_auction_status", "");
                        } else {
                            mainfilter.put("vendor_auction_status", MultiVendor_spnr_vendor_auction.getSelectedItem().toString());
                        }

                        Intent intent = new Intent(getApplicationContext(), Ced_Multivendor_List_Auction.class);
                        intent.putExtra("filter", mainfilter.toString());
                        /*intent.putExtra("Navigation", navigation);*/
                        /*intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);*/
                        startActivity(intent);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            listDialog.setContentView(v);
            listDialog.setCancelable(true);
            listDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void updateLabel(EditText date) {
        String myFormat = "yyyy/mm/dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        date.setText(sdf.format(myCalendar.getTime()));
    }

    public void showTruitonDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showToDatePickerDialog(View v) {
        DialogFragment newFragment = new ToDatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog;
            datePickerDialog = new DatePickerDialog(getActivity(), this, year,
                    month, day);
            return datePickerDialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            MultiVendor_edt_start_date.setText(day + "-" + (month + 1) + "-" + year);
        }

    }

    public static class ToDatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {
        // Calendar startDateCalendar=Calendar.getInstance();
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog;
            datePickerDialog = new DatePickerDialog(getActivity(), this, year,
                    month, day);
            return datePickerDialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            month = month + 1;
            MultiVendor_edt_end_date.setText(day + "-" + month + "-" + year);
        }
    }


}
