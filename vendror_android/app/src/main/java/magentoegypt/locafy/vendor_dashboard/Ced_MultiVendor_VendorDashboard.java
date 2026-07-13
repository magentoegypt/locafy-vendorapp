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

package magentoegypt.locafy.vendor_dashboard;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.snackbar.Snackbar;
import magentoegypt.locafy.R;
import magentoegypt.locafy.addons.inventory.LowStock;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_UnAuthourizedRequestError;
import magentoegypt.locafy_constant.Ced_MultiVendor_GlobalVariables;
import magentoegypt.locafy_constant.CustomAutoscrollViewPager;
import magentoegypt.locafy.base_app.Ced_MultiVendor_FontSetting;
import magentoegypt.locafy.base_app.Ced_MultiVendor_LatestfiveOrdersAdapter;
import magentoegypt.locafy.base_app.Ced_MultiVendor_LineView;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorOrderMarkersMap;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorPieChartSlice;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorPiechart;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorSplash;
import magentoegypt.locafy.manage_products_section.Ced_MultiVendor_ManageProducts;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.vendor_notification.Notification;
import magentoegypt.locafy.vendor_profile_section.Ced_MultiVendor_ProfileStatus;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Ced_MultiVendor_VendorDashboard extends Ced_MultiVendor_NavigationActivity {
    private static final int PERMISSION_REQUEST_CODE = 1;
    Ced_MultiVendor_FontSetting fontSetting;
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorSessionManagement vendorSessionManagement;
    Spinner spinneraction;
    Ced_MultiVendor_VendorPiechart vendorPiechart;
    String Jstring;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    String CurrrentUrl;
    HashMap<String, String> datafordashboard;
    TextView approvedproduct;
    TextView pendingproduct;
    TextView disapprovedproduct;
    TextView latestordertag;
    TextView viewdetails;
    TextView salestag;
    TextView producttag;
    TextView countrywiseordertag;
    HashMap<Integer, Integer> days;
    HashMap<Integer, Integer> weeks;
    HashMap<Integer, Integer> months;
    HashMap<Integer, Integer> years;
    ArrayList<HashMap<String, String>> latestfiveorderlist;
    Ced_MultiVendor_LatestfiveOrdersAdapter latestfiveOrdersAdapter;
    ArrayList<HashMap<String, String>> googlemaplist;
    CardView viewdetailscard;
    Ced_MultiVendor_LineView lineView;
    ArrayList<HashMap<String, String>> sliderdata;
    ArrayList<HashMap<String, String>> enhancements_sliderdata;
    ViewPager pager;
    PageIndicator orderlistindicator;
    Ced_MultiVendor_cardsAdapter cardsadapter;
    TextView no_orders_available, no_products_uploaded;
    private ScrollView mainscrolldashboard;
    private View view;

    CardView latestProductsCard,topSellingCard,transactionsCard;
    CustomAutoscrollViewPager orderlist,latestproducts,toptellingPager,transactionPager;
    PageIndicator mIndicator,latestProductIndicator,topsellingIndicator,transactionIndicator ;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_bar_color));
        }
        fontSetting = new Ced_MultiVendor_FontSetting();
        connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        vendorSessionManagement = new Ced_MultiVendor_VendorSessionManagement(getApplicationContext());
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(getApplicationContext());
        datafordashboard = new HashMap<>();
        days = new HashMap<>();
        weeks = new HashMap<>();
        months = new HashMap<>();
        years = new HashMap<>();
        latestfiveorderlist = new ArrayList<>();
        sliderdata = new ArrayList<>();
        enhancements_sliderdata = new ArrayList<>();
        googlemaplist = new ArrayList<>();
        CurrrentUrl = session.getBase_Url() + "vendorapi/index/dashboard/";
        if (connectionDetector.isConnectingToInternet()) {
            ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
            getLayoutInflater().inflate(R.layout.ced_multivendor_activity_vendor_dashboard, content, true);
       /*     if (vendorSessionManagement.getUrlRun()==false)
            {
                Intent gcmregisteration = new Intent(Ced_MultiVendor_VendorDashboard.this, ReceiverActivity.class);
                startActivity(gcmregisteration);
                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                finish();
                vendorSessionManagement.saveUrlRun(Boolean.TRUE);
            }*/
            //           else
            //           {
            final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
            pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    pullToRefresh.setRefreshing(true);
                    Intent intent = new Intent(Ced_MultiVendor_VendorDashboard.this, Ced_MultiVendor_VendorDashboard.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }
            });
            mainscrolldashboard = findViewById(R.id.MultiVendor_mainscrolldashboard);
            lineView = findViewById(R.id.MultiVendor_line_view);
            spinneraction = findViewById(R.id.MultiVendor_spinneraction);
            ArrayAdapter<String> orderadapter = new ArrayAdapter<>(this, R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.array_name));
            spinneraction.setAdapter(orderadapter);
            viewdetails = findViewById(R.id.MultiVendor_viewdetails);
            latestordertag = findViewById(R.id.MultiVendor_latestordertag);
            countrywiseordertag = findViewById(R.id.MultiVendor_countrywiseordertag);
            salestag = findViewById(R.id.MultiVendor_salestag);
            producttag = findViewById(R.id.MultiVendor_producttag);
            approvedproduct = findViewById(R.id.MultiVendor_approvedproduct);
            pendingproduct = findViewById(R.id.MultiVendor_pendingproduct);
            disapprovedproduct = findViewById(R.id.MultiVendor_disapprovedproduct);
            vendorPiechart = findViewById(R.id.MultiVendor_vendorpiechart);
            viewdetailscard = findViewById(R.id.MultiVendor_viewdetailscard);
            pager = findViewById(R.id.MultiVendor_category_bannerList);

            orderlist = findViewById(R.id.MultiVendor_orderlists);
            mIndicator = findViewById(R.id.MultiVendor_category_bannerindicator);

            latestProductsCard = findViewById(R.id.latestProductsCard);
            latestproducts = findViewById(R.id.latestProductsPager);
            latestProductIndicator = findViewById(R.id.latestProductIndicator);

            topSellingCard = findViewById(R.id.topSellingCard);
            toptellingPager = findViewById(R.id.toptellingPager);
            topsellingIndicator = findViewById(R.id.topsellingIndicator);

            transactionsCard = findViewById(R.id.transactionsCard);
            transactionPager = findViewById(R.id.transactionPager);
            transactionIndicator = findViewById(R.id.transactionIndicator);

            orderlistindicator = findViewById(R.id.MultiVendor_orderlistindicator);
            no_orders_available = findViewById(R.id.no_orders_available);
            no_products_uploaded = findViewById(R.id.no_products_uploaded);
/*                mRegistrationBroadcastReceiver = new BroadcastReceiver()
                {
                    @Override
                    public void onReceive(Context context, Intent intent)
                    {
                        if (intent.getAction().equals(Config.REGISTRATION_COMPLETE))
                        {
                            String token = intent.getStringExtra("token");
                        }
                        else if (intent.getAction().equals(Config.SENT_TOKEN_TO_SERVER))
                        {
                        }
                        else if (intent.getAction().equals(Config.PUSH_NOTIFICATION))
                        {
                        }
                    }
                };*/
            viewdetailscard.setOnClickListener(v -> {
                Intent intent = new Intent(Ced_MultiVendor_VendorDashboard.this, Ced_MultiVendor_ManageProducts.class);
                startActivity(intent);
                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
            });

            setDemoListener();
            if (vendorSessionManagement.getvendorname() != null) {
                setname(vendorSessionManagement.getvendorname());
                setprofilepic(vendorSessionManagement.getvendorpic());
                changetitle(getString(R.string.dashboardtag));
                HashMap<String, String> insidesliderdata = new HashMap<>();
                insidesliderdata.put("title", getString(R.string.dashboardtag));
                insidesliderdata.put("link", "dashboard");
                insidesliderdata.put("hint", getString(R.string.hellovendortext)+", " + vendorSessionManagement.getvendorname() + "!");
                sliderdata.add(insidesliderdata);
            }
            /*************************************************applyfonts***********************************************************/
            fontSetting.setFontforTextviews(no_orders_available, "Roboto-Regular.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(no_products_uploaded, "Roboto-Regular.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(approvedproduct, "Roboto-Regular.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(pendingproduct, "Roboto-Regular.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(disapprovedproduct, "Roboto-Regular.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(salestag, "Roboto-Medium.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(producttag, "Roboto-Medium.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(viewdetails, "Roboto-Medium.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(latestordertag, "Roboto-Medium.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(countrywiseordertag, "Roboto-Medium.ttf", getApplicationContext());
            /************************************************************************************************************/
            loadData();
            spinneraction.setSelection(1);
            weekSelect();
            //       }
        }
        else {
            Intent nointernet = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(nointernet);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
            else {
                // repeat the permission or open app details
            }
        }
    }

    void loadData(){
        try {
            datafordashboard.put("vendor_id", vendorSessionManagement.getVendorid());
            datafordashboard.put("hashkey", vendorSessionManagement.getHahkey());
            Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(output -> {
                Jstring = output.toString();
                if (functionalityList.getExtensionAddon()) {
                    mainscrolldashboard.setVisibility(View.VISIBLE);
                    final JSONObject jsonObject = new JSONObject(Jstring);
                    if (jsonObject.has("vendor_approved")) {
                        logout();
                    }
                    else {
                        String success = jsonObject.getJSONObject("data").getString("success");
                        if (success.equals("true")) {
                            if (jsonObject.getJSONObject("data").has("valerts"))
                                Ced_MultiVendor_GlobalVariables.noti = jsonObject.getJSONObject("data").getString("valerts");
                            if (jsonObject.getJSONObject("data").has("profile_complete"))
                                Ced_MultiVendor_GlobalVariables.profile_count_val = jsonObject.getJSONObject("data").getString("profile_complete");
                            if (jsonObject.getJSONObject("data").has("vendor_name"))
                                vendorname.setText(jsonObject.getJSONObject("data").getString("vendor_name"));
                            if (jsonObject.getJSONObject("data").has("profile_picture"))
                                setprofilepic(jsonObject.getJSONObject("data").getString("profile_picture"));
                            invalidateOptionsMenu();

                            JSONObject dashboardObj=jsonObject.getJSONObject("data").getJSONObject("dashboard");
                            JSONObject tiles = dashboardObj.getJSONObject("tiles");
                            HashMap<String, String> pending = new HashMap<>();
                            pending.put("title", tiles.getString("pending_amount"));
                            pending.put("link", "pending");
                            pending.put("hint", getResources().getString(R.string.TotalPendingAmountTag));
                            sliderdata.add(pending);
                            HashMap<String, String> earned = new HashMap<>();
                            earned.put("title", tiles.getString("net_earned"));
                            earned.put("link", "earned");
                            earned.put("hint", getResources().getString(R.string.earnedamounttag));
                            sliderdata.add(earned);
                            HashMap<String, String> orderplace = new HashMap<>();
                            orderplace.put("title", tiles.getString("order_placed"));
                            orderplace.put("link", "orderplace");
                            orderplace.put("hint", getResources().getString(R.string.eorderplacedtag));
                            sliderdata.add(orderplace);
                            HashMap<String, String> sold = new HashMap<>();
                            sold.put("title", tiles.getString("sold"));
                            sold.put("link", "sold");
                            sold.put("hint", getResources().getString(R.string.totalproductsold));
                            sliderdata.add(sold);
                            HashMap<String, String> business_details = new HashMap<>();
                            business_details.put("title", getResources().getString(R.string.business_details));
                            business_details.put("link", "business_details");
                            business_details.put("hint", getResources().getString(R.string.business_details));
                            enhancements_sliderdata.add(business_details);
                            HashMap<String, String> bank_detials = new HashMap<>();
                            bank_detials.put("title", getResources().getString(R.string.bank_details));
                            bank_detials.put("link", "bank_details");
                            bank_detials.put("hint", getResources().getString(R.string.bank_details));
                            enhancements_sliderdata.add(bank_detials);
                            HashMap<String, String> store_details = new HashMap<>();
                            store_details.put("title", getResources().getString(R.string.store_details));
                            store_details.put("link", "store_details");
                            store_details.put("hint", getResources().getString(R.string.store_details));
                            enhancements_sliderdata.add(store_details);
                            cardsadapter = new Ced_MultiVendor_cardsAdapter(getApplicationContext(), sliderdata, Ced_MultiVendor_VendorDashboard.this);
                            pager.setAdapter(cardsadapter);
                            mIndicator.setViewPager(pager);
                            JSONObject piechart = dashboardObj.getJSONObject("piechart");
                            int total = 0;
                            int approvedValue = 0;
                            int disapprovedValue = 0;
                            int pendingValue = 0;
                            if (piechart.get("approved") instanceof String) {
                                double value = Double.parseDouble(piechart.getString("approved").replace("K",""));
                                approvedValue = (int) value;
                                total += (int) value;
                                approvedproduct.setText(piechart.getString("approved") + " "+getString(R.string.approved));
                            }else{
                                total += piechart.getInt("approved");
                                approvedValue = piechart.getInt("approved");
                                approvedproduct.setText(piechart.getInt("approved") + " "+getString(R.string.approved));
                            }
                            if (piechart.get("pending") instanceof String) {
                                double value = Double.parseDouble(piechart.getString("pending").replace("K",""));
                                pendingValue = (int) value;
                                total += (int) value;
                                pendingproduct.setText(piechart.getString("pending") + " "+getString(R.string.pending));
                            }else{
                                total += piechart.getInt("pending");
                                pendingValue = piechart.getInt("pending");
                                pendingproduct.setText(piechart.getInt("pending") + " "+getString(R.string.pending));
                            }

                            if (piechart.get("disapproved") instanceof String) {
                                double value = Double.parseDouble(piechart.getString("disapproved").replace("K",""));
                                disapprovedValue = (int) value;
                                total += (int) value;
                                disapprovedproduct.setText(piechart.getString("disapproved") + " "+getString(R.string.disapprove));
                            }else{
                                total += piechart.getInt("disapproved");
                                disapprovedValue = piechart.getInt("disapproved");
                                disapprovedproduct.setText(piechart.getInt("disapproved") + " "+getString(R.string.disapprove));
                            }

                            List<Ced_MultiVendor_VendorPieChartSlice> cedMultiVendorVendorPieChartSlices = generateFakeSlices(pendingValue, approvedValue, disapprovedValue, total);
                            if (cedMultiVendorVendorPieChartSlices.size() > 0) {
                                no_products_uploaded.setVisibility(View.GONE);
                                vendorPiechart.setValues(cedMultiVendorVendorPieChartSlices);
                                vendorPiechart.expand();
                            } else {
                                no_products_uploaded.setVisibility(View.VISIBLE);
                                no_products_uploaded.setText(getResources().getString(R.string.no_products_uploaded));
                                vendorPiechart.setVisibility(View.GONE);
                            }

                            JSONObject chart = dashboardObj.getJSONObject("chart");
                            JSONObject day = chart.getJSONObject("day");
                            JSONArray daydata = day.getJSONArray("data");
                            for (int d = 0; d < daydata.length(); d++) {
                                JSONArray array = daydata.getJSONArray(d);
                                days.put((Integer) array.get(0), Integer.parseInt(String.valueOf(array.get(1))));
                            }
                            final JSONObject week = chart.getJSONObject("week");
                            JSONArray weekdata = week.getJSONArray("data");
                            for (int w = 0; w < weekdata.length(); w++) {
                                JSONArray array = weekdata.getJSONArray(w);
                                weeks.put((Integer) array.get(0), Integer.parseInt(String.valueOf(array.get(1))));
                            }
                            JSONObject month = chart.getJSONObject("month");
                            JSONArray monthdata = month.getJSONArray("data");
                            for (int m = 0; m < monthdata.length(); m++) {
                                JSONArray array = monthdata.getJSONArray(m);
                                months.put((Integer) array.get(0), Integer.parseInt(String.valueOf(array.get(1))));
                            }
                            JSONObject year = chart.getJSONObject("year");
                            JSONArray yeardata = year.getJSONArray("data");
                            for (int y = 0; y < yeardata.length(); y++) {
                                JSONArray array = yeardata.getJSONArray(y);
                                years.put((Integer) array.get(0), Integer.parseInt(String.valueOf(array.get(1))));
                            }

                            JSONArray latest_order = dashboardObj.getJSONArray("latest_order");
                            if (latest_order.length() == 0) {
                                no_orders_available.setText(getResources().getString(R.string.no_orders_available));
                                no_orders_available.setVisibility(View.VISIBLE);
                                orderlist.setVisibility(View.GONE);
                            }
                            else {
                                no_orders_available.setVisibility(View.GONE);
                                orderlist.setVisibility(View.VISIBLE);
                            }
                            for (int l = 0; l < latest_order.length(); l++) {
                                JSONObject orderrow = latest_order.getJSONObject(l);
                                HashMap<String, String> dataofrow = new HashMap<>();
                                dataofrow.put("order_id", "#" + orderrow.getString("order_id"));
                                dataofrow.put("purchase_on", orderrow.getString("purchase_on"));
                                dataofrow.put("billing_name", orderrow.getString("billing_name"));
                                dataofrow.put("net_earned", orderrow.getString("net_earned"));
                                dataofrow.put("order_status", orderrow.getString("order_status"));
                                latestfiveorderlist.add(dataofrow);
                            }
                            latestfiveOrdersAdapter = new Ced_MultiVendor_LatestfiveOrdersAdapter(Ced_MultiVendor_VendorDashboard.this, latestfiveorderlist, Ced_MultiVendor_VendorDashboard.this);
                            orderlist.setAdapter(latestfiveOrdersAdapter);
                            orderlistindicator.setViewPager(orderlist);

                            if (dashboardObj.has("latest_products")&&dashboardObj.get("latest_products") instanceof JSONArray && dashboardObj.getJSONArray("latest_products").length()>0){
                                latestproducts.setAdapter(new LatestProductAdapter(dashboardObj.getJSONArray("latest_products")));
                                latestProductIndicator.setViewPager(latestproducts);
                            }
                            else {
                                latestproducts.setVisibility(View.GONE);
                                findViewById(R.id.no_latestProduct_available).setVisibility(View.VISIBLE);
                            }

                            if (dashboardObj.has("top_selling_products")&&dashboardObj.get("top_selling_products") instanceof JSONArray&&dashboardObj.getJSONArray("top_selling_products").length()>0){

                                toptellingPager.setAdapter(new TopSellingAdapter(dashboardObj.getJSONArray("top_selling_products")));
                                topsellingIndicator.setViewPager(toptellingPager);
                            }
                            else {
                                toptellingPager.setVisibility(View.GONE);
                                findViewById(R.id.no_topSelling_available).setVisibility(View.VISIBLE);
                            }

                            if (dashboardObj.has("transaction")&&dashboardObj.get("transaction") instanceof JSONArray&&dashboardObj.getJSONArray("transaction").length()>0){

                                transactionPager.setAdapter(new LatestTransactionAdapter(dashboardObj.getJSONArray("transaction")));
                                transactionIndicator.setViewPager(transactionPager);
                            }
                            else {
                                transactionPager.setVisibility(View.GONE);
                                findViewById(R.id.no_transaction_available).setVisibility(View.VISIBLE);
                            }

                            CardView countrywiseorder = findViewById(R.id.MultiVendor_countrywiseorder);
                            countrywiseorder.setOnClickListener(v -> {
                                JSONArray map = null;
                                try {
                                    map = dashboardObj.getJSONArray("map");
                                    if (map.length() == 0) {
                                        Toast.makeText(Ced_MultiVendor_VendorDashboard.this, "You have no Orders Right now.", Toast.LENGTH_LONG).show();
                                    } else {
                                        for (int con = 0; con < map.length(); con++) {
                                            JSONObject object = map.getJSONObject(con);
                                            HashMap<String, String> latandlog = new HashMap<>();
                                            latandlog.put("total", object.getString("total"));
                                            //---------------------------------------------------------------
                                            latandlog.put("country", (object.getString("country_code").toUpperCase()));
                                            googlemaplist.add(latandlog);
                                        }
                                        view = v;
                                        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
                                        if (result == PackageManager.PERMISSION_GRANTED) {
                                            Intent intent2 = new Intent(Ced_MultiVendor_VendorDashboard.this, Ced_MultiVendor_VendorOrderMarkersMap.class);
                                            intent2.putExtra("googlemapdata", googlemaplist);
                                            startActivity(intent2);
                                            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                                        }
                                        else {
                                            if (ActivityCompat.shouldShowRequestPermissionRationale(Ced_MultiVendor_VendorDashboard.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                                                Toast.makeText(Ced_MultiVendor_VendorDashboard.this, "GPS permission allows us to access location data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                                intent.setData(uri);
                                                startActivityForResult(intent, 1);
                                            }
                                            else {

                                                ActivityCompat.requestPermissions(Ced_MultiVendor_VendorDashboard.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
                                            }
                                        }
                                    }
                                }
                                catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            });
                            mainscrolldashboard.setVisibility(View.VISIBLE);
                            lineView.setVisibility(View.VISIBLE);
                            lineView.setDrawDotLine(true); //optional
                            lineView.setShowPopup(Ced_MultiVendor_LineView.SHOW_POPUPS_MAXMIN_ONLY); //optional
                            ArrayList<String> strList = new ArrayList<>();
                            strList.add("0");
                            strList.add("1");
                            strList.add("2");
                            strList.add("3");
                            strList.add("4");
                            strList.add("5");
                            strList.add("6");
                            strList.add("7");
                            strList.add("8");
                            strList.add("9");
                            strList.add("10");
                            strList.add("11");
                            strList.add("12");
                            strList.add("13");
                            strList.add("14");
                            strList.add("15");
                            strList.add("16");
                            strList.add("17");
                            strList.add("18");
                            strList.add("19");
                            strList.add("20");
                            strList.add("21");
                            strList.add("22");
                            strList.add("23");
                            lineView.setBottomTextList(strList);
                            ArrayList<Integer> dataList = new ArrayList<>();
                            for (int i = 0; i < days.size(); i++) {
                                dataList.add(days.get(i));
                            }
                            ArrayList<ArrayList<Integer>> dataLists = new ArrayList<>();
                            dataLists.add(dataList);
                            lineView.setDataList(dataLists);
                            ((TextView) spinneraction.getSelectedView()).setTextColor(getResources().getColor(R.color.white));
                            spinneraction.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    ((TextView) spinneraction.getSelectedView()).setTextColor(getResources().getColor(R.color.white));
                                    if (spinneraction.getSelectedItem().equals("Today") || spinneraction.getSelectedItem().equals(getResources().getString(R.string.today_week))) {
                                        todaySelect();
                                    }
                                    if (spinneraction.getSelectedItem().equals("Week") || spinneraction.getSelectedItem().equals(getResources().getString(R.string.week_txt))) {

                                    }
                                    if (spinneraction.getSelectedItem().equals("Month") || spinneraction.getSelectedItem().equals(getResources().getString(R.string.month_txt))) {
                                        lineView.setVisibility(View.VISIBLE);
                                        lineView.setDrawDotLine(true); //optional
                                        lineView.setShowPopup(Ced_MultiVendor_LineView.SHOW_POPUPS_MAXMIN_ONLY); //optional
                                        ArrayList<String> strList = new ArrayList<>();
                                        strList.add("1");
                                        strList.add("2");
                                        strList.add("3");
                                        strList.add("4");
                                        strList.add("5");
                                        strList.add("6");
                                        strList.add("7");
                                        strList.add("8");
                                        strList.add("9");
                                        strList.add("10");
                                        strList.add("11");
                                        strList.add("12");
                                        strList.add("13");
                                        strList.add("14");
                                        strList.add("15");
                                        strList.add("16");
                                        strList.add("17");
                                        strList.add("18");
                                        strList.add("19");
                                        strList.add("20");
                                        strList.add("21");
                                        strList.add("22");
                                        strList.add("23");
                                        strList.add("24");
                                        strList.add("25");
                                        strList.add("26");
                                        strList.add("27");
                                        strList.add("28");
                                        strList.add("29");
                                        strList.add("30");
                                        strList.add("31");
                                        lineView.setBottomTextList(strList);
                                        ArrayList<Integer> dataList = new ArrayList<>();
                                        Log.e("REpo", "months==" + months.size());
                                        for (int i = 1; i <= months.size(); i++) {
                                            dataList.add(months.get(i));
                                        }
                                        ArrayList<ArrayList<Integer>> dataLists = new ArrayList<>();
                                        dataLists.add(dataList);
                                        lineView.setDataList(dataLists);
                                    }
                                    if (spinneraction.getSelectedItem().equals("Year") || spinneraction.getSelectedItem().equals(getResources().getString(R.string.year_txt))) {
                                        lineView.setVisibility(View.VISIBLE);
                                        lineView.setDrawDotLine(true); //optional
                                        lineView.setShowPopup(Ced_MultiVendor_LineView.SHOW_POPUPS_MAXMIN_ONLY); //optional
                                        ArrayList<String> strList = new ArrayList<>();
                                        strList.add(getString(R.string.jan));
                                        strList.add(getString(R.string.feb));
                                        strList.add(getString(R.string.mar));
                                        strList.add(getString(R.string.apr));
                                        strList.add(getString(R.string.may));
                                        strList.add(getString(R.string.jun));
                                        strList.add(getString(R.string.jul));
                                        strList.add(getString(R.string.aug));
                                        strList.add(getString(R.string.sep));
                                        strList.add(getString(R.string.oct));
                                        strList.add(getString(R.string.nov));
                                        strList.add(getString(R.string.dec));
                                        lineView.setBottomTextList(strList);
                                        ArrayList<Integer> dataList = new ArrayList<>();
                                        for (int i = 1; i <= 12; i++)
                                            dataList.add(years.get(i));
                                        ArrayList<ArrayList<Integer>> dataLists = new ArrayList<>();
                                        dataLists.add(dataList);
                                        lineView.setDataList(dataLists);
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

                            new Handler().postDelayed(() -> {
                                //applybarchart();
                                show();
                            }, 500);
                        }
                        else {
                            vendorSessionManagement.ClearVendorId();
                            vendorSessionManagement.logoutUser();
                            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                            Toast.makeText(getApplicationContext(), jsonObject.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
                        }
                    }
                    spinneraction.setSelection(1);
                    weekSelect();
                   // todaySelect();
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }
            }, Ced_MultiVendor_VendorDashboard.this, "POST", datafordashboard);
            crr.execute(CurrrentUrl);
        }
        catch (Exception e) {
            Intent main = new Intent(getApplicationContext(), Ced_MultiVendor_VendorSplash.class);
            main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(main);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }
    }

    private void todaySelect(){
            lineView.setVisibility(View.VISIBLE);
            lineView.setDrawDotLine(true); //optional
            lineView.setShowPopup(Ced_MultiVendor_LineView.SHOW_POPUPS_MAXMIN_ONLY); //optional
            ArrayList<String> strList = new ArrayList<>();
            strList.add("0");
            strList.add("1");
            strList.add("2");
            strList.add("3");
            strList.add("4");
            strList.add("5");
            strList.add("6");
            strList.add("7");
            strList.add("8");
            strList.add("9");
            strList.add("10");
            strList.add("11");
            strList.add("12");
            strList.add("13");
            strList.add("14");
            strList.add("15");
            strList.add("16");
            strList.add("17");
            strList.add("18");
            strList.add("19");
            strList.add("20");
            strList.add("21");
            strList.add("22");
            strList.add("23");
            lineView.setBottomTextList(strList);
            ArrayList<Integer> dataList = new ArrayList<>();
            for (int i = 0; i < days.size(); i++) {
                dataList.add(days.get(i));
            }
            ArrayList<ArrayList<Integer>> dataLists = new ArrayList<>();
            dataLists.add(dataList);
            lineView.setDataList(dataLists);
        new Handler().postDelayed(() -> {
            //applybarchart();
            show();
        }, 500);
    }

    private void weekSelect(){
        lineView.setVisibility(View.VISIBLE);
        lineView.setDrawDotLine(true); //optional
        lineView.setShowPopup(Ced_MultiVendor_LineView.SHOW_POPUPS_MAXMIN_ONLY); //optional
        ArrayList<String> strList = new ArrayList<>();
        strList.add(getString(R.string.sun));
        strList.add(getString(R.string.mon));
        strList.add(getString(R.string.tue));
        strList.add(getString(R.string.wed));
        strList.add(getString(R.string.thu));
        strList.add(getString(R.string.fri));
        strList.add(getString(R.string.sat));
        lineView.setBottomTextList(strList);
        ArrayList<Integer> dataList = new ArrayList<>();
        for (int i = 0; i < weeks.size(); i++) {
            dataList.add(weeks.get(i));
        }
        ArrayList<ArrayList<Integer>> dataLists = new ArrayList<>();
        dataLists.add(dataList);
        lineView.setDataList(dataLists);
    }

    private void setDemoListener() {
        salestag.setOnClickListener(view -> startActivity(new Intent(Ced_MultiVendor_VendorDashboard.this, LowStock.class)
                .putExtra("type", "low_stock")));
    }

    @Override
    protected void onResume() {
        Ced_MultiVendor_ConnectionDetector connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        if (connectionDetector.isConnectingToInternet()) {
            vendorPiechart.expand();
            setname(vendorSessionManagement.getvendorname());
            setprofilepic(vendorSessionManagement.getvendorpic());
            changetitle(getString(R.string.dashboardtag));
            //   invalidateOptionsMenu();

        } else {
            Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }
        super.onResume();
    }

    private List<Ced_MultiVendor_VendorPieChartSlice> generateFakeSlices(int pending, int approved, int dissapproved, int total) {
        List<Ced_MultiVendor_VendorPieChartSlice> slices = new ArrayList<>();
        if (percentange(pending, total) != 0) {
            Ced_MultiVendor_VendorPieChartSlice slice1 = new Ced_MultiVendor_VendorPieChartSlice();
            slice1.count(percentange(pending, total));
            slice1.color(getResources().getColor(R.color.pending));
            slices.add(slice1);
        }
        if (percentange(approved, total) != 0) {
            Ced_MultiVendor_VendorPieChartSlice slice2 = new Ced_MultiVendor_VendorPieChartSlice();
            slice2.count(percentange(approved, total));
            slice2.color(getResources().getColor(R.color.approved));
            slices.add(slice2);
        }
        if (percentange(dissapproved, total) != 0) {
            Ced_MultiVendor_VendorPieChartSlice slice3 = new Ced_MultiVendor_VendorPieChartSlice();
            slice3.count(percentange(dissapproved, total));
            slice3.color(getResources().getColor(R.color.disapproved));
            slices.add(slice3);
        }
        return slices;
    }

    public int percentange(int i, int total) {
        int percent = 0;
        if (total != 0) {
            percent = (i * 360) / total;
        }
        return percent;
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        mainscrolldashboard.requestLayout();
    }

    public void show() {
        mainscrolldashboard.setVisibility(View.VISIBLE);
        mainscrolldashboard.scrollTo(0, 0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Snackbar.make(view, "Permission Granted, Now you can access location data.", Snackbar.LENGTH_LONG).show();
//                    Intent intent2 = new Intent(getApplicationContext(), Ced_MultiVendor_VendorOrderMarkersMap.class);
//                    intent2.putExtra("googlemapdata", googlemaplist);
//                    startActivity(intent2);
//                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
//                } else {
//                    Snackbar.make(view, "Permission Denied, You cannot access location data.", Snackbar.LENGTH_LONG).show();
//                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ced_multivendor_menu_login, menu);
        MenuItem item = menu.findItem(R.id.MultiVendor_notification);
        MenuItem item2 = menu.findItem(R.id.MultiVendor_profile);
        MenuItemCompat.setActionView(item, R.layout.ced_multivendor_feed_update_count);
        MenuItemCompat.setActionView(item2, R.layout.ced_multivendor_profilestatus);
        View profilecount = MenuItemCompat.getActionView(item2);

        profilecount.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_ProfileStatus.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);

        });
        View notifCount = MenuItemCompat.getActionView(item);
        TextView notification = notifCount.findViewById(R.id.MultiVendor_notitext);
        notification.setText(Ced_MultiVendor_GlobalVariables.noti);
        TextView profile_txt_count = profilecount.findViewById(R.id.MultiVendor_hotlist_hot);
        //    profile_txt_count.setText(Ced_MultiVendor_GlobalVariables.profile_count_val);

        notifCount.setOnClickListener(v -> {
          //  Toast.makeText(getApplicationContext(), "Notification", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), Notification.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
        return true;
    }
}