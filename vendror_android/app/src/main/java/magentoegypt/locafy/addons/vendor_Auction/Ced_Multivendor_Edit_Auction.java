package magentoegypt.locafy.addons.vendor_Auction;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import  magentoegypt.locafy.R;
import  magentoegypt.locafy_constant.AppConstant;
import  magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import  magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import  magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import  magentoegypt.locafy.api_request_response_section.AsyncResponse;
import  magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import  magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by cedcoss on 23/1/18.
 */

public class Ced_Multivendor_Edit_Auction extends Ced_MultiVendor_NavigationActivity

{
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorSessionManagement vendorSessionManagement;
    String id;
    String determine_activty;
    String info_url;
    String save_url;
    HashMap<String, String> dataforproducts;

    TextView product_id;
    TextView product_name;
    EditText startprice;
    EditText maxprice;
    Spinner spinner;
    Calendar newCalendar;
    TextView save;
    TextView cancle;
    String productid = "";
    String productname = "";
    LinearLayout parent_linear;
    public static EditText MultiVendor_edt_start_date;
    public static EditText MultiVendor_edt_end_date;
    Boolean select_date = false;
    final Calendar myCalendar = Calendar.getInstance();
    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState)

    {

        super.onCreate(savedInstanceState);
        connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        vendorSessionManagement = new Ced_MultiVendor_VendorSessionManagement(getApplicationContext());

        id = getIntent().getStringExtra("id");
        determine_activty = getIntent().getStringExtra("activity");
        productid = getIntent().getStringExtra("product_id");
        productname = getIntent().getStringExtra("product_name");

        if (connectionDetector.isConnectingToInternet()) {
            ViewGroup content = (ViewGroup) findViewById(R.id.MultiVendor_frame_container);
            getLayoutInflater().inflate(R.layout.ced_multivendor_edit_auction, content, true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);
            info_url = session.getBase_Url() + "vauctionapi/auction/info";
            save_url = session.getBase_Url() + "vauctionapi/auction/save";
            dataforproducts = new HashMap<>();

            product_id = (TextView) findViewById(R.id.MultiVendor_product_id);
            product_name = (TextView) findViewById(R.id.MultiVendor_prod_name);
            startprice = (EditText) findViewById(R.id.MultiVendor_starting_price);
            maxprice = (EditText) findViewById(R.id.MultiVendor_maxprice);
            MultiVendor_edt_start_date = (EditText) findViewById(R.id.MultiVendor_start_date);
            MultiVendor_edt_end_date = (EditText) findViewById(R.id.MultiVendor_end_date);
            spinner = (Spinner) findViewById(R.id.MultiVendor_status);
            parent_linear = (LinearLayout) findViewById(R.id.parent_linear);

            save = (TextView) findViewById(R.id.MultiVendor_save);
            cancle = (TextView) findViewById(R.id.MultiVendor_cancle);
            product_id.setText(productid);
            product_name.setText(productname);

            dataforproducts.put("hashkey", vendorSessionManagement.getHahkey());
            dataforproducts.put("vendor_id", vendorSessionManagement.getVendorid());
            dataforproducts.put("auction_id", id);

            /**********************************DatePicker******************************************************************/

            final Calendar myCalendar = Calendar.getInstance();

            final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    // TODO Auto-generated method stub
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateLabel();
                }

                private void updateLabel() {

                    String myFormat = "MM/dd/yyyy"; //In which you need put here
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                    MultiVendor_edt_start_date.setText(sdf.format(myCalendar.getTime()));

                }
            };
           /* startdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new DatePickerDialog(Ced_Multivendor_Edit_Auction.this, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });
            enddate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    newCalendar = Calendar.getInstance();
                    dateFormatter = new SimpleDateFormat("MM/dd/yy", Locale.US);
                    new DatePickerDialog(Ced_Multivendor_Edit_Auction.this, new DatePickerDialog.OnDateSetListener() {

                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            newCalendar.set(Calendar.YEAR, year);
                            newCalendar.set(Calendar.MONTH, monthOfYear);
                            newCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                            enddate.setText(dateFormatter.format(newCalendar.getTime()));

                        }

                    },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH)).show();
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
                        AppConstant.lockButton(view);
                        showToDatePickerDialog(view);

                    } else

                    {
                        Toast.makeText(Ced_Multivendor_Edit_Auction.this, R.string.please_fill_both_dates, Toast.LENGTH_SHORT).show();
                    }
                }
            });


            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //save
                    AppConstant.lockButton(view);
                    dataforproducts.put("product_id", product_id.getText().toString());
                    dataforproducts.put("product_name", product_name.getText().toString());
                    dataforproducts.put("starting_price", startprice.getText().toString());
                    dataforproducts.put("max_price", maxprice.getText().toString());
                    dataforproducts.put("start_datetime", MultiVendor_edt_start_date.getText().toString());
                    dataforproducts.put("end_datetime", MultiVendor_edt_end_date.getText().toString());
                    dataforproducts.put("status", spinner.getSelectedItem().toString());

                    dataforproducts.put("vendor_auction_status", "Approved");


                    saveDataRequest(dataforproducts);
                }
            });

            cancle.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    AppConstant.lockButton(view);
                    if (determine_activty.equalsIgnoreCase("list_auction")) {
                        //cancle
                        Intent in = new Intent(Ced_Multivendor_Edit_Auction.this, Ced_Multivendor_List_Auction.class);
                        startActivity(in);
                    }

                    if (determine_activty.equalsIgnoreCase("add_auction")) {
                        Intent in = new Intent(Ced_Multivendor_Edit_Auction.this, Ced_Multivendor_Add_Auction.class);
                        startActivity(in);
                    }
                }
            });
            Log.i("vaibhavactivity", determine_activty);

            if (determine_activty.equalsIgnoreCase("list_auction")) {
                parent_linear.setVisibility(View.GONE);
                request_info_data(dataforproducts);
            }


        } else {
            Intent nointernet = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(nointernet);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }

    }

    private void saveDataRequest(HashMap data) {
        final Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                JSONObject main = new JSONObject(output.toString());
                if (main.getJSONObject("data").getBoolean("success")) {
                    Toast.makeText(Ced_Multivendor_Edit_Auction.this, "" + main.getJSONObject("data").getString("message"), Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(Ced_Multivendor_Edit_Auction.this, Ced_Multivendor_List_Auction.class);
                    startActivity(in);
                } else {
                    Toast.makeText(Ced_Multivendor_Edit_Auction.this, "" + main.getJSONObject("data").getString("message"), Toast.LENGTH_SHORT).show();
                }
            }
        }, Ced_Multivendor_Edit_Auction.this, "POST", data);
        crr.execute(save_url);

    }

    private void request_info_data(HashMap dataforproducts) {
        final Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                JSONObject root = new JSONObject(output.toString());

                Log.d("Vaibhav", root + "");


                if (root.getJSONObject("data").getString("success").equalsIgnoreCase("true")) {
                    parent_linear.setVisibility(View.VISIBLE);
                    JSONObject auction_info = root.getJSONObject("data").getJSONObject("auction_info");


                    Log.d("edt_auc-product_id", "" + auction_info.getString("product_id"));
                    Log.d("edt_auc-product_name", "" + auction_info.getString("product_name"));
                    Log.d("edt_auc-starting_price", "" + auction_info.getString("starting_price"));
                    Log.d("edt_auc-max_price", "" + auction_info.getString("max_price"));
                    Log.d("edt_auc-start_datetime", "" + auction_info.getString("start_datetime"));
                    Log.d("edt_auc-end_datetime", "" + auction_info.getString("end_datetime"));

                    Log.d("edt_auc-status", "" + auction_info.getString("status"));


                    if (auction_info.getString("product_id") != "null") {
                        product_id.setText(auction_info.getString("product_id"));
                    }

                    if (auction_info.getString("product_name") != "null") {
                        product_name.setText(auction_info.getString("product_name"));
                    }

                    if (auction_info.getString("starting_price") != "null") {
                        startprice.setText(auction_info.getString("starting_price"));
                    }

                    if (auction_info.getString("max_price") != "null") {
                        maxprice.setText(auction_info.getString("max_price"));
                    }

                    if (auction_info.getString("start_datetime") != "null") {
                        MultiVendor_edt_start_date.setText(auction_info.getString("start_datetime"));
                    }

                    if (auction_info.getString("end_datetime") != "null") {
                        MultiVendor_edt_end_date.setText(auction_info.getString("end_datetime"));
                    }

                    if (auction_info.getString("status").equalsIgnoreCase("Processing")) {
                        spinner.setSelection(1);
                    } else if (auction_info.getString("status").equalsIgnoreCase("Cancelled")) {
                        spinner.setSelection(2);
                    } else if (auction_info.getString("status").equalsIgnoreCase("Completed")) {
                        spinner.setSelection(3);
                    } else if (auction_info.getString("status").equalsIgnoreCase("NotStarted")) {
                        spinner.setSelection(4);
                    } else if (auction_info.getString("status").equalsIgnoreCase("BidClosed")) {
                        spinner.setSelection(4);
                    } else {
                        spinner.setSelection(0);
                    }

                } else {

                    Toast.makeText(getApplicationContext(), root.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
                }
            }
        }, Ced_Multivendor_Edit_Auction.this, "POST", dataforproducts);
        crr.execute(info_url);

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
