/*
 *
 *   Copyright/* *
 *             * CedCommerce
 *             *
 *             * NOTICE OF LICENSE
 *             *
 *             * This source file is subject to the End User License Agreement (EULA)
 *             * that is bundled with this package in the file LICENSE.txt.
 *             * It is also available through the world-wide-web at this URL:
 *             * http://cedcommerce.com/license-agreement.txt
 *             *
 *             * @category  Ced
 *             * @package   MultiVendor
 *             * @author    CedCommerce Core Team <connect@cedcommerce.com >
 *             * @copyright Copyright CEDCOMMERCE (http://cedcommerce.com/)
 *             * @license   http://cedcommerce.com/license-agreement.txt
 *
 *
 *
 */

package magentoegypt.locafy.vendor_reports_section;

import static magentoegypt.locafy_constant.AppConstant.simpleDateFormat;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import magentoegypt.locafy.base_app.Ced_MultiVendor_FontSetting;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.base_app.MonthYearPickerDialog;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_UnAuthourizedRequestError;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.R;
import magentoegypt.locafy_constant.AppConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by developer on 9/5/16.
 */
public class Ced_MultiVendor_OrderReport extends Ced_MultiVendor_NavigationActivity {
    public static EditText MultiVendor_start_dateee;
    public static EditText MultiVendor_edt_end_date;
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    String dropdown = "";
    HashMap<String, String> postdata;
    Button OrderReport_head, showreport;
    TextView Period_head, vendorpaymentstaus_head, start_date_head, end_date_head;
    Spinner period_spinner, vendor_payemnt_status_spinner;
    String period_value, VP_status, to, from;
    HashMap<String, String> spinier_list;

    Calendar newCalendar;
    Ced_MultiVendor_FontSetting fontSetting;
    Boolean select_date = false;
    Date startDate = new Date();
    Date endDate = new Date();
    String[] spinner_item = {"Day", "Month", "Year"};
    String[] spinner_item_paymentStatus = {"All" , "Pending" , "Paid", "Refunded" , "Canceled"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
            window.setStatusBarColor(this.getResources().getColor(R.color.status_bar_color));
        }
        connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(getApplicationContext());
        postdata = new HashMap<>();
        spinier_list = new HashMap<>();
      //  spn_label_list = new ArrayList<>();
        fontSetting = new Ced_MultiVendor_FontSetting();
        dropdown = session.getBase_Url() + "vendorapi/vreport/vpaymentStatus";

        if (connectionDetector.isConnectingToInternet()) {
            ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
            getLayoutInflater().inflate(R.layout.ced_multivendor_activity_order_report, content, true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);
            if (session.getvendorname() != null) {
                setname(session.getvendorname());
                setprofilepic(session.getvendorpic());
                changetitle("Order Reports");
            }
            OrderReport_head = findViewById(R.id.MultiVendor_OrderReport_head);
            showreport = findViewById(R.id.MultiVendor_showreport);
            Period_head = findViewById(R.id.MultiVendor_Period_head);
            vendorpaymentstaus_head = findViewById(R.id.MultiVendor_vendorpaymentstaus_head);
            start_date_head = findViewById(R.id.MultiVendor_start_date_head);
            end_date_head = findViewById(R.id.MultiVendor_end_date_head);
            MultiVendor_start_dateee = findViewById(R.id.MultiVendor_start_dateee);
            MultiVendor_edt_end_date = findViewById(R.id.MultiVendor_Enddate);
            period_spinner = findViewById(R.id.MultiVendor_period_spinner);
            vendor_payemnt_status_spinner = findViewById(R.id.MultiVendor_vendor_payemnt_status_spinner);

            fontSetting.setfontforButtons(OrderReport_head, "Roboto-Bold.ttf", getApplicationContext());
            fontSetting.setfontforButtons(showreport, "Roboto-Bold.ttf", getApplicationContext());

            fontSetting.setFontforTextviews(Period_head, "Roboto-Medium.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(vendorpaymentstaus_head, "Roboto-Medium.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(start_date_head, "Roboto-Medium.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(end_date_head, "Roboto-Medium.ttf", getApplicationContext());

            fontSetting.setfontforEditText(MultiVendor_start_dateee, "Roboto-Regular.ttf", getApplicationContext());
            fontSetting.setfontforEditText(MultiVendor_edt_end_date, "Roboto-Regular.ttf", getApplicationContext());

            get_payment_status();

            MultiVendor_start_dateee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    select_date = true;
                    if(period_spinner.getSelectedItemPosition() == 0){
                        AppConstant.setDateFrom(Ced_MultiVendor_OrderReport.this, MultiVendor_start_dateee);
                    }else{
                        final Calendar calendar = Calendar.getInstance();
                        MonthYearPickerDialog monthYearPickerDialog =  new MonthYearPickerDialog(startDate);
                        monthYearPickerDialog.setListener((datePicker, year, monthOfYear, dayOfMonth) -> {
                            calendar.set(Calendar.YEAR, year);
                            calendar.set(Calendar.MONTH, monthOfYear);
                            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            startDate = calendar.getTime();
                            MultiVendor_start_dateee.setText(simpleDateFormat.format(startDate));
                        });
                        monthYearPickerDialog.show(getSupportFragmentManager(),"MonthYearPickerDialog");
                    }
                }
            });


            MultiVendor_edt_end_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (select_date) {
                        if(period_spinner.getSelectedItemPosition() == 0){
                            AppConstant.setDateTo(Ced_MultiVendor_OrderReport.this, MultiVendor_edt_end_date, MultiVendor_start_dateee);
                        }else{
                            final Calendar calendar = Calendar.getInstance();
                            MonthYearPickerDialog monthYearPickerDialog =  new MonthYearPickerDialog(endDate);
                            monthYearPickerDialog.setListener((datePicker, year, monthOfYear, dayOfMonth) -> {
                                calendar.set(Calendar.YEAR, year);
                                calendar.set(Calendar.MONTH, monthOfYear);
                                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                endDate = calendar.getTime();
                                MultiVendor_edt_end_date.setText(simpleDateFormat.format(endDate));
                            });
                            monthYearPickerDialog.show(getSupportFragmentManager(),"MonthYearPickerDialog");
                        }
                    } else {
                        Toast.makeText(Ced_MultiVendor_OrderReport.this, R.string.please_fill_both_dates, Toast.LENGTH_SHORT).show();
                    }
                }
            });

            showreport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("frozen", "period_spinner= " + period_spinner.getSelectedItemPosition());
                    Log.e("frozen", "spinner_item= " + spinner_item[period_spinner.getSelectedItemPosition()]);
                    period_value = spinner_item[period_spinner.getSelectedItemPosition()];
                    VP_status = spinner_item_paymentStatus[vendor_payemnt_status_spinner.getSelectedItemPosition()];//spinier_list.get(vendor_payemnt_status_spinner.getSelectedItem().toString());
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                        Date fromdate = sdf.parse(MultiVendor_start_dateee.getText().toString());
                        Date todate = sdf.parse(MultiVendor_edt_end_date.getText().toString());
                        SimpleDateFormat finalDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                        from = finalDateFormat.format(fromdate);
                        to = finalDateFormat.format(todate);

                    }catch (Exception e){
                          from = MultiVendor_start_dateee.getText().toString();
                          to = MultiVendor_edt_end_date.getText().toString();
                    }
                    if (to.isEmpty() && from.isEmpty()) {
                        Toast.makeText(getApplicationContext(), R.string.please_fill_both_dates, Toast.LENGTH_LONG).show();
                    } else if (to.isEmpty()) {
                        Toast.makeText(getApplicationContext(), R.string.please_fill_both_dates, Toast.LENGTH_LONG).show();
                    } else if (from.isEmpty()) {
                        Toast.makeText(getApplicationContext(), R.string.please_fill_both_dates, Toast.LENGTH_LONG).show();
                    } else {
                        Intent order_report_list = new Intent(Ced_MultiVendor_OrderReport.this, Ced_MultiVendor_OrderReport_list.class);
                        order_report_list.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        order_report_list.putExtra("period", period_value);
                        order_report_list.putExtra("VP_status", VP_status);
                        order_report_list.putExtra("to", to);
                        order_report_list.putExtra("from", from);
                        startActivity(order_report_list);
                    }
                }
            });
        } else {
            Intent nointernet = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(nointernet);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }
    }

    //https://stackoverflow.com/questions/21321789/android-datepicker-change-to-only-month-and-year

    private void get_payment_status() {
        Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                if (functionalityList.getExtensionAddon()) {
                    JSONObject object = new JSONObject(output.toString());
                    JSONArray array = object.getJSONObject("data").getJSONArray("vordersstatus");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject objects = array.getJSONObject(i);
                        spinier_list.put(objects.getString("label"), objects.getString("value"));
                       // spn_label_list.add(objects.getString("label"));
                    }
                    ArrayAdapter<String> adp = new ArrayAdapter<String>
                            (Ced_MultiVendor_OrderReport.this, android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.payment_status));
                    vendor_payemnt_status_spinner.setAdapter(adp);
                } else {
                    Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }
            }
        }, Ced_MultiVendor_OrderReport.this);
        response.execute(dropdown);
    }

    @Override
    protected void onResume() {
        Ced_MultiVendor_ConnectionDetector connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        if (connectionDetector.isConnectingToInternet()) {
            setname(session.getvendorname());
            setprofilepic(session.getvendorpic());
            changetitle("Order Reports");
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
