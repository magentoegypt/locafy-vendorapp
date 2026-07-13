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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import magentoegypt.locafy.base_app.Ced_MultiVendor_FontSetting;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;
import magentoegypt.locafy.R;
import magentoegypt.locafy_constant.AppConstant;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by developer on 10/5/16.
 */
public class Ced_MultiVendor_ProductReport extends Ced_MultiVendor_NavigationActivity {
    public static EditText MultiVendor_start_dateee;
    public static EditText MultiVendor_edt_end_date;
    static Activity activity = null;
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorSessionManagement vendorSessionManagement;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    HashMap<String, String> postdata;
    Button ProductReport_head, showreport;
    TextView start_date_head, end_date_head;
    Spinner period_spinner, vendor_payemnt_status_spinner;
    String to, from;
    List<String> spinier_list;
    Calendar newCalendar;
    Ced_MultiVendor_FontSetting fontSetting;
    Boolean select_date = false;
    private SimpleDateFormat dateFormatter;

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

        activity = this;

        connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        vendorSessionManagement = new Ced_MultiVendor_VendorSessionManagement(getApplicationContext());
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(getApplicationContext());
        postdata = new HashMap<>();
        spinier_list = new ArrayList<String>();
        fontSetting = new Ced_MultiVendor_FontSetting();
        if (connectionDetector.isConnectingToInternet()) {
            ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
            getLayoutInflater().inflate(R.layout.ced_multivendor_activity_product_report, content, true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);
            if (vendorSessionManagement.getvendorname() != null) {
                setname(vendorSessionManagement.getvendorname());
                setprofilepic(vendorSessionManagement.getvendorpic());
                changetitle("Product Reports");
            }
            ProductReport_head = findViewById(R.id.MultiVendor_ProductReport_head);
            showreport = findViewById(R.id.MultiVendor_showreport);
            start_date_head = findViewById(R.id.MultiVendor_start_date_head);
            end_date_head = findViewById(R.id.MultiVendor_end_date_head);
            MultiVendor_start_dateee = findViewById(R.id.MultiVendor_start_dateee);
            MultiVendor_edt_end_date = findViewById(R.id.MultiVendor_Enddate);

            fontSetting.setfontforButtons(ProductReport_head, "Roboto-Bold.ttf", getApplicationContext());
            fontSetting.setfontforButtons(showreport, "Roboto-Bold.ttf", getApplicationContext());

            fontSetting.setFontforTextviews(start_date_head, "Roboto-Medium.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(end_date_head, "Roboto-Medium.ttf", getApplicationContext());

            fontSetting.setfontforEditText(MultiVendor_start_dateee, "Roboto-Regular.ttf", getApplicationContext());
            fontSetting.setfontforEditText(MultiVendor_edt_end_date, "Roboto-Regular.ttf", getApplicationContext());


            MultiVendor_start_dateee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    select_date = true;
                    AppConstant.setDateFrom(Ced_MultiVendor_ProductReport.this, MultiVendor_start_dateee);

                }
            });


            MultiVendor_edt_end_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (select_date) {
                        AppConstant.setDateTo(Ced_MultiVendor_ProductReport.this, MultiVendor_edt_end_date, MultiVendor_start_dateee);

                    } else {
                        Toast.makeText(Ced_MultiVendor_ProductReport.this, R.string.please_fill_both_dates, Toast.LENGTH_SHORT).show();
                    }
                }
            });


            showreport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

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

                        Intent order_report_list = new Intent(Ced_MultiVendor_ProductReport.this, Ced_MultiVendor_ProductReport_list.class);
                        order_report_list.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
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


    @Override
    protected void onResume() {
        Ced_MultiVendor_ConnectionDetector connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        if (connectionDetector.isConnectingToInternet()) {
            setname(vendorSessionManagement.getvendorname());
            setprofilepic(vendorSessionManagement.getvendorpic());
            changetitle("Product Reports");
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
