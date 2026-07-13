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

package magentoegypt.locafy.navigation_drawer.Activity;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static magentoegypt.locafy.addons.advance_Report.appBase.AppConstant.ACTIVITY_NAME;
import static magentoegypt.locafy.addons.advance_Report.appBase.AppConstant.OUT_OF_STOCK_PRODUCT_ACTIVITY;
import static magentoegypt.locafy.addons.advance_Report.appBase.AppConstant.PAYMENT_REPORT_ACTIVITY;
import static magentoegypt.locafy.addons.advance_Report.appBase.AppConstant.PRODUCT_SALES_ACTIVITY;
import static magentoegypt.locafy.addons.advance_Report.appBase.AppConstant.PRODUCT_VIEWS_ACTIVITY;
import static magentoegypt.locafy.addons.advance_Report.appBase.AppConstant.RETURN_REPORT_ACTIVITY;
import static magentoegypt.locafy.addons.advance_Report.appBase.AppConstant.SOLD_PRODUCT_ACTIVITY;
import static magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement.Key_Email;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.api_request_response_section.RestNotificatioRequest;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_UnAuthourizedRequestError;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorSplash;
import magentoegypt.locafy.base_app.UtilityMethods;
import magentoegypt.locafy.vendor_profile_section.Ced_MultiVendor_EditVendorProfileSection;
import com.bumptech.glide.Glide;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.network.connectionclass.ConnectionClassManager;
import com.facebook.network.connectionclass.ConnectionQuality;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import magentoegypt.locafy.R;
import magentoegypt.locafy.addons.advance_Report.AdvanceReportHomeActivity;
import magentoegypt.locafy.addons.ced_multi_vendor_auction_new.auctionwinner.Ced_Multivendor_AuctionWinners;
import magentoegypt.locafy.addons.ced_multi_vendor_auction_new.manageauction.activity.Ced_Multivendor_ManageAuction;
import magentoegypt.locafy.addons.faq.FaqListScreen;
import magentoegypt.locafy.addons.inventory.LowStock;
import magentoegypt.locafy.addons.inventory.OutOfStockActivity;
import magentoegypt.locafy.addons.inventory.SaveInventory;
import magentoegypt.locafy.addons.messaging.admin_inbox.Admin_Inbox;
import magentoegypt.locafy.addons.messaging.customer_inbox.Customer_Inbox;
import magentoegypt.locafy.addons.mvp_RequestForQuote.Ced_MultiVendor_VendorQuoteManagement;
import magentoegypt.locafy.addons.mvp_RequestForQuote.Ced_MultiVendor_Vendor_ViewPO_List;
import magentoegypt.locafy.addons.mvp_RequestForQuote.mvp_RFQ_Setting;
import magentoegypt.locafy.addons.vendor_member_ship_plans.Ced_Multivendor_Membership_Plan;
import magentoegypt.locafy.addons.vendor_member_ship_plans.Ced_Multivendor_Plan_History;
import magentoegypt.locafy.addons.vendor_pincode_checker.AssignedPincode.mvp_AssignedPincodeList;
import magentoegypt.locafy.addons.vendor_pincode_checker.PincodeList.mvp_PincodeListing;
import magentoegypt.locafy.addons.vendor_rma.Ced_MultiVendor_ManageRMARequest;
import magentoegypt.locafy.addons.vendor_select_n_sell.Add_Product_list;
import magentoegypt.locafy.addons.vendor_select_n_sell.Product_list;
import magentoegypt.locafy.addons.vendor_store_pickup.Ced_MultiVendor_StoreListing;
import magentoegypt.locafy_constant.Ced_MultiVendor_GlobalVariables;
import magentoegypt.locafy.base_app.Ced_Load_Language;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.base_app.StoreSelection;
import magentoegypt.locafy.manage_attribute.Ced_MultiVendor_VendorProductAttribute;
import magentoegypt.locafy.manage_attribute.Ced_MultiVendor_VendorProductAttributeSet;
import magentoegypt.locafy.manage_orders.Ced_MultiVendor_CreditMemoListing;
import magentoegypt.locafy.manage_orders.Ced_MultiVendor_InvoiceListing;
import magentoegypt.locafy.manage_orders.Ced_MultiVendor_ManageOrdersList;
import magentoegypt.locafy.manage_orders.Ced_MultiVendor_OrdersList;
import magentoegypt.locafy.manage_orders.Ced_MultiVendor_ShipmentListing;
import magentoegypt.locafy.manage_products_section.Ced_MultiVendor_AddVendorProduct;
import magentoegypt.locafy.manage_products_section.Ced_MultiVendor_ManageProducts;
import magentoegypt.locafy.manage_ticket.Manage_Ticket;
import magentoegypt.locafy.navigation_drawer.Adapter.Ced_MultiVendor_NavigationExapandableListAdapter;
import magentoegypt.locafy.navigation_drawer.Adapter.NavigationRecAdapter;
import magentoegypt.locafy.navigation_drawer.models.Ced_MultiVendor_NavDrawerItem;
import magentoegypt.locafy.navigation_drawer.models.NavigationChild;
import magentoegypt.locafy.navigation_drawer.models.NavigationModel;
import magentoegypt.locafy.navigation_drawer.utils.ScreenNavigation;
import magentoegypt.locafy.vendor_cms.Ced_MultiVendor_Cms.Ced_MultiVendor_CmsListing;
import magentoegypt.locafy.vendor_cms.Ced_MultiVendor_StaticBlock.Ced_MultiVendor_BlockListing;
import magentoegypt.locafy.vendor_dashboard.Ced_MultiVendor_VendorDashboard;
import magentoegypt.locafy.vendor_deals.Ced_MultiVendor_Create_and_Edit_deal;
import magentoegypt.locafy.vendor_deals.Ced_MultiVendor_Deal_Listing;
import magentoegypt.locafy.vendor_deals.Ced_MultiVendor_Deal_Settings;
import magentoegypt.locafy.vendor_login_section.Ced_Multivendor_New_Login;
import magentoegypt.locafy.vendor_notification.Notification;
import magentoegypt.locafy.vendor_notification.app.MyApplication;
import magentoegypt.locafy.vendor_product_review_and_rating.ManageRating.ManageProductRating;
import magentoegypt.locafy.vendor_product_review_and_rating.ManageReview.ManageProductReview;
import magentoegypt.locafy.vendor_profile_section.Ced_MultiVendor_ProfileStatus;
import magentoegypt.locafy.vendor_profile_section.Ced_MultiVendor_VendorProfile;
import magentoegypt.locafy.vendor_reports_section.Ced_MultiVendor_OrderReport;
import magentoegypt.locafy.vendor_reports_section.Ced_MultiVendor_ProductReport;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;
import magentoegypt.locafy.vendor_settings.Ced_MultiVendor_Settings;
import magentoegypt.locafy.vendor_settings.Ced_MultiVendor_ShippingMethod;
import magentoegypt.locafy.vendor_settings.Ced_MultiVendor_ShippingSetting;
import magentoegypt.locafy.vendor_transaction.Ced_MultiVendor_ListTransaction;
import magentoegypt.locafy.vendor_transaction.mvp_adv_req_payments;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import kotlin.Suppress;

public class Ced_MultiVendor_NavigationActivity extends AppCompatActivity {
    public static final int MY_CAMERA_PERMISSION_CODE = 100;
    public static final int CAMERA_REQUEST = 1888;

    private final int Navigation_CAMERA_REQUEST = 111;
    public static Uri photoUrl = null;
    public int PICK_IMAGE_REQUEST = 1;
    public static float density;
    public static String imagePath;
    public Gson converter = new Gson();
    public int writeresult, readresult, cameraresult;
    public TextView vendorname,emailText;
    public ImageView vendorpic;
    public Ced_MultiVendor_VendorSessionManagement session;
    public JSONObject status_array_spinner;
    public JSONArray status_spinner, type_spinner;
    View profilecount;
    View notifCount;
    View vendorview;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    ArrayList<String> headersection;
    HashMap<String, ArrayList<String>> listdatachild;
    ArrayList<String> childRFQ,childProduct,childInventory, childsection_two, childsection_three, childsection_four, childsection_five, childsection_six, childsection_seven, childsection_eight, childReviewAndRating;
    ArrayList<String> childsection_nine, childsection_ten, childsection_eleven, childsection_twele, childsection_thirty, childsection_fourty, childsection_fifty, childsection_order, childsection_auction, childsection_sixty, childsection_favouriteseller, childsection_seventy, childsection_eighty, childsection_ninety;
    ArrayList<String> childsection_manage_pincode;

    TextView headerText;
    CircleImageView headerLogo;

    IntentFilter intentFilter;
    BroadcastReceiver error_receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "Seller not Exist", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Ced_MultiVendor_NavigationActivity.this, Ced_Multivendor_New_Login.class));
            finishAffinity();
        }
    };
    private DrawerLayout mDrawerLayout;
    private ExpandableListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private ArrayList<Ced_MultiVendor_NavDrawerItem> navDrawerItems;
    private Ced_MultiVendor_NavigationExapandableListAdapter drawerAdapter;
    private Toolbar mToolbar;
    private ConnectionClassManager mConnectionClassManager;
    private ConnectionChangedListener mListener;
    private ConnectionQuality mConnectionClass = ConnectionQuality.UNKNOWN;


    private NavigationView navigationView;
    private RecyclerView navigationRecView;
    private NavigationRecAdapter navigationRecAdapter;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(error_receiver);
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ced_multivendor_nav_main);
        mConnectionClassManager = ConnectionClassManager.getInstance();
        mListener = new ConnectionChangedListener();
        intentFilter = new IntentFilter("logout");

        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getAttributes().flags &= (~WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        writeresult = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        readresult = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        cameraresult = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);

        headersection = new ArrayList<>();
        childRFQ = new ArrayList<>();
        childProduct = new ArrayList<>();
        childInventory = new ArrayList<>();
        childsection_two = new ArrayList<>();
        childsection_three = new ArrayList<>();
        childsection_four = new ArrayList<>();
        childsection_five = new ArrayList<>();
        childsection_order = new ArrayList<>();
        childsection_six = new ArrayList<>();
        childsection_seven = new ArrayList<>();
        childsection_eight = new ArrayList<>();
        childsection_nine = new ArrayList<>();
        childsection_ten = new ArrayList<>();
        childsection_eleven = new ArrayList<>();
        childsection_twele = new ArrayList<>();
        childsection_thirty = new ArrayList<>();
        childsection_fourty = new ArrayList<>();
        childsection_fifty = new ArrayList<>();
        childsection_auction = new ArrayList<>();
        childsection_sixty = new ArrayList<>();
        childsection_seventy = new ArrayList<>();
        childsection_eighty = new ArrayList<>();
        childsection_ninety = new ArrayList<>();
        childsection_favouriteseller = new ArrayList<>();
        childsection_manage_pincode = new ArrayList<>();
        childReviewAndRating = new ArrayList<>();
        listdatachild = new HashMap<>();
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(getApplicationContext());
        session = new Ced_MultiVendor_VendorSessionManagement(this);
        mToolbar = findViewById(R.id.MageNative_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ham_menu);

/*        navigationView = findViewById(R.id.navigationView);
//        navigationRecView = navigationView.findViewById(R.id.navigationRec);

        headerText = navigationView.findViewById(R.id.headerTextTv);
        headerLogo = navigationView.findViewById(R.id.profile_image);*/

//        setUpNavRec();

        mDrawerLayout = findViewById(R.id.MultiVendor_drawer_layout);
        mDrawerList = findViewById(R.id.MultiVendor_slidermenu);
        mDrawerList.setVerticalScrollBarEnabled(false);
        mDrawerList.setDivider(new ColorDrawable(getResources().getColor(R.color.transparent)));
        mDrawerList.setDividerHeight(0);
        navDrawerItems = new ArrayList<>();


        density = getResources().getDisplayMetrics().densityDpi;
        status_array_spinner = new JSONObject();
        status_spinner = new JSONArray();
        type_spinner = new JSONArray();

        if (mConnectionClassManager.getCurrentBandwidthQuality().toString().equals("POOR")) {
            Toast.makeText(getApplicationContext(), "Low Network connection is detected", Toast.LENGTH_LONG).show();
        }

        if (session.getStoreLocale() != null) {
            new Ced_Load_Language().setLanguagetoLoad(session.getStoreLocale(), this);
        }

        headersection.add(getResources().getString(R.string.dashboard));
        navDrawerItems.add(new Ced_MultiVendor_NavDrawerItem(getResources().getString(R.string.dashboard), R.drawable.dashboard));

        if (functionalityList.getVendor_Profile()) {
            headersection.add(getResources().getString(R.string.VendorProfile));
            navDrawerItems.add(new Ced_MultiVendor_NavDrawerItem(getResources().getString(R.string.VendorProfile), R.drawable.profile));
        }

        if (functionalityList.getNew_Product()) {
            headersection.add(getResources().getString(R.string.Products));
            navDrawerItems.add(new Ced_MultiVendor_NavDrawerItem(getResources().getString(R.string.Products), R.drawable.manage_products));
            childProduct.add(getResources().getString(R.string.NewProduct));
            childProduct.add(getResources().getString(R.string.ManageProducts));
           // childProduct.add(getResources().getString(R.string.productattribute));
        }
        if (functionalityList.getManage_Attribute()) {
            childProduct.add(getResources().getString(R.string.ManageAttribute));
        }
        if (functionalityList.getManage_Attribute_Set()) {
            childProduct.add(getResources().getString(R.string.ManageAttributeSet));
        }
//        if (functionalityList.getVendor_Product_Attribute()) {
//            Log.i("subvendor", "getVendor_Product_Attribute");
//            headersection.add(getResources().getString(R.string.productattribute));
//            navDrawerItems.add(new Ced_MultiVendor_NavDrawerItem(getResources().getString(R.string.productattribute), R.drawable.productattribute));
//            childsection_three.add(getResources().getString(R.string.ManageAttribute));
//            childsection_three.add(getResources().getString(R.string.ManageAttributeSet));
//        }

        Log.i("subvendor", "Vendor Inventory");
     //   headersection.add(getResources().getString(R.string.vendor_inventory));
     //   navDrawerItems.add(new Ced_MultiVendor_NavDrawerItem(getResources().getString(R.string.vendor_inventory), R.drawable.selectandsell));
        childsection_eighty.add(getResources().getString(R.string.inventory_manage));
        childsection_eighty.add(getResources().getString(R.string.out_of_stock));
        childsection_eighty.add(getResources().getString(R.string.low_stock_product));
        childProduct.addAll(childsection_eighty);
        if (functionalityList.getOrders()) {
            Log.i("subvendor", "getOrders");
            headersection.add(getResources().getString(R.string.manageorders));
            navDrawerItems.add(new Ced_MultiVendor_NavDrawerItem(getResources().getString(R.string.manageorders), R.drawable.orders));
            if (!session.getSubVendorId().equalsIgnoreCase("")) {
                if (functionalityList.getManage_Orders()) {
                    childsection_four.add(getResources().getString(R.string.ManageOrders));
                    childsection_four.add(getResources().getString(R.string.CancelOrders));
                }
                if (functionalityList.getManage_Invoice())
                    childsection_four.add(getResources().getString(R.string.ManageInvoice));
                if (functionalityList.getManage_Shipment())
                    childsection_four.add(getResources().getString(R.string.ManageShipment));
                if (functionalityList.getManage_Credit_Memo())
                    childsection_four.add(getResources().getString(R.string.ManageCreditMemo));
            } else {
                childsection_four.add(getResources().getString(R.string.ManageOrders));
                childsection_four.add(getResources().getString(R.string.CancelOrders));
                childsection_four.add(getResources().getString(R.string.ManageInvoice));
                childsection_four.add(getResources().getString(R.string.ManageShipment));
                childsection_four.add(getResources().getString(R.string.ManageCreditMemo));
            }
            childsection_four.add(getResources().getString(R.string.managermarequest));
        }

        if (functionalityList.get_Transactions()) {
            headersection.add(getResources().getString(R.string.Transactions));
            navDrawerItems.add(new Ced_MultiVendor_NavDrawerItem(getResources().getString(R.string.Transactions), R.drawable.transaction));
            childsection_nine.add(getResources().getString(R.string.RequestPaymentsMenu));
            childsection_nine.add(getResources().getString(R.string.ViewTransactaions));
            childsection_nine.add(getResources().getString(R.string.TransactaionSettings));
        }

//        if (functionalityList.getRatingAndReview()) {
//            headersection.add(getResources().getString(R.string.ProductRatingandreview));
//            navDrawerItems.add(new Ced_MultiVendor_NavDrawerItem(getResources().getString(R.string.ProductRatingandreview), R.drawable.ic_baseline_fact_check_24));
//        }

        if (functionalityList.get_Reports()) {
            headersection.add(getResources().getString(R.string.Reports));
            navDrawerItems.add(new Ced_MultiVendor_NavDrawerItem(getResources().getString(R.string.Reports), R.drawable.report));
            if (!session.getSubVendorId().equalsIgnoreCase("")) {
                if (functionalityList.Order_Reports())
                    childsection_ten.add(getResources().getString(R.string.OrderReports));
                if (functionalityList.Products_Report())
                    childsection_ten.add(getResources().getString(R.string.ProductReports));
            } else {
                childsection_ten.add(getResources().getString(R.string.OrderReports));
                childsection_ten.add(getResources().getString(R.string.ProductReports));
            }
        }

        Log.i("subvendor", "Advance Report module");
      //  headersection.add(getResources().getString(R.string.vendor_advance_report));
      //  navDrawerItems.add(new Ced_MultiVendor_NavDrawerItem(getResources().getString(R.string.vendor_advance_report), R.drawable.selectandsell));
        childsection_ninety.add(getResources().getString(R.string.vendor_sales_report));
     //   childsection_ninety.add(getResources().getString(R.string.product_views));
        childsection_ninety.add(getResources().getString(R.string.payment_report));
        childsection_ninety.add(getResources().getString(R.string.out_of_stock_products));
      //  childsection_ninety.add(getResources().getString(R.string.sold_products));
        childsection_ninety.add(getResources().getString(R.string.return_report));
        childsection_ten.addAll(childsection_ninety);
//        if (functionalityList.getRMA()) {
//            Log.i("subvendor", "getRMA");
//            headersection.add(getResources().getString(R.string.rma));
//            navDrawerItems.add(new Ced_MultiVendor_NavDrawerItem(getResources().getString(R.string.rma), R.drawable.rma));
//            childsection_two.add(getResources().getString(R.string.managermarequest));
//        }

        if (functionalityList.getVedndorCms()) {
            Log.i("subvendor", "getVedndorCms");
            headersection.add(getResources().getString(R.string.vendorcms));
            navDrawerItems.add(new Ced_MultiVendor_NavDrawerItem(getResources().getString(R.string.vendorcms), R.drawable.cms));
            if (!session.getSubVendorId().equalsIgnoreCase("")) {
                if (functionalityList.Manage_Vendor_CMS())
                    childsection_fourty.add(getResources().getString(R.string.managevendorcms));
                if (functionalityList.Manage_Static_Blocks())
                    childsection_fourty.add(getResources().getString(R.string.managestaticblocks));
            } else {
                childsection_fourty.add(getResources().getString(R.string.managevendorcms));
                childsection_fourty.add(getResources().getString(R.string.managestaticblocks));
            }
        }

        if (functionalityList.getVendor_Deals()) {
            Log.i("subvendor", "getVendor_Deals");
            headersection.add(getResources().getString(R.string.vendordeals));
            navDrawerItems.add(new Ced_MultiVendor_NavDrawerItem(getResources().getString(R.string.vendordeals), R.drawable.deal));

            childsection_eight.add(getResources().getString(R.string.createdeals));
            childsection_eight.add(getResources().getString(R.string.listdeals));
            childsection_eight.add(getResources().getString(R.string.dealsettings));
        }

        if (functionalityList.getMessaging()) {
            headersection.add(getResources().getString(R.string.messaging));
            navDrawerItems.add(new Ced_MultiVendor_NavDrawerItem(getResources().getString(R.string.messaging), R.drawable.messaging));
            childsection_five.add(getResources().getString(R.string.vendorcustomer));
            childsection_five.add(getResources().getString(R.string.vendoradmin));
        }

        if (functionalityList.getRequestForQuote()) {
            headersection.add(getResources().getString(R.string.requestforquote));
            navDrawerItems.add(new Ced_MultiVendor_NavDrawerItem(getResources().getString(R.string.requestforquote), R.drawable.add_icon));
            childRFQ.add(getResources().getString(R.string.managequotes));
            childRFQ.add(getResources().getString(R.string.manage_proposal));
            childRFQ.add(getResources().getString(R.string.rfq_setting));
        }

        if (functionalityList.getMemberShipPlans()) {
            Log.i("subvendor", "getMemberShipPlans");
            headersection.add(getResources().getString(R.string.membershipplans));
            navDrawerItems.add(new Ced_MultiVendor_NavDrawerItem(getResources().getString(R.string.membershipplans), R.drawable.member_ship_plans));
            if (!session.getSubVendorId().equalsIgnoreCase("")) {
                if (functionalityList.MemberShip_Plan())
                    childsection_six.add(getResources().getString(R.string.membershipplan));
                if (functionalityList.Plan_History())
                    childsection_six.add(getResources().getString(R.string.planhistory));
            } else {
                childsection_six.add(getResources().getString(R.string.membershipplan));
                childsection_six.add(getResources().getString(R.string.planhistory));
            }
        }

//        if (functionalityList.getPincodeChecker()) {
//            Log.i("subvendor", "getPincodeChecker");
//            headersection.add(getResources().getString(R.string.pincodeChecker));
//            navDrawerItems.add(new Ced_MultiVendor_NavDrawerItem(getResources().getString(R.string.pincodeChecker), R.drawable.storepickup));
//            childsection_manage_pincode.add(getResources().getString(R.string.manage_pincode));
//            childsection_manage_pincode.add(getResources().getString(R.string.assigned_pincode));
//        }


        Log.i("subvendor", "Vendor FAQ");
       // headersection.add(getResources().getString(R.string.faq));
      //  navDrawerItems.add(new Ced_MultiVendor_NavDrawerItem(getResources().getString(R.string.faq), R.drawable.selectandsell));
        childsection_seventy.add(getResources().getString(R.string.add_faq_product));

        headersection.add(getResources().getString(R.string.change_store));
        navDrawerItems.add(new Ced_MultiVendor_NavDrawerItem(getResources().getString(R.string.change_store), R.drawable.settings));
//        if (functionalityList.get_Settings()) {
//            headersection.add(getResources().getString(R.string.Settings));
//            navDrawerItems.add(new Ced_MultiVendor_NavDrawerItem(getResources().getString(R.string.Settings), R.drawable.settings));
//            childsection_eleven.add(getResources().getString(R.string.TransactaionSettings));
//            if (functionalityList.getTransaction_Settings())
//                childsection_eleven.add(getResources().getString(R.string.TransactaionSettings));
//            if (functionalityList.getShipping_Settings())
//                childsection_eleven.add(getResources().getString(R.string.ShippingSettings));
//            if (functionalityList.getShipping_Methods())
//                childsection_eleven.add(getResources().getString(R.string.ShippingMethods));
//        }

        if (functionalityList.getStorePickup()) {
            Log.i("subvendor", "getStorePickup");
            headersection.add(getResources().getString(R.string.storepickup));
            navDrawerItems.add(new Ced_MultiVendor_NavDrawerItem(getResources().getString(R.string.storepickup), R.drawable.storepickup));
            childsection_twele.add(getResources().getString(R.string.viewstores));
        }

        if (childProduct.size() > 0) {
            listdatachild.put(getResources().getString(R.string.Products), childProduct);
        }
        if (childsection_two.size() > 0) {
            listdatachild.put(getResources().getString(R.string.rma), childsection_two);
        }
        if (childsection_three.size() > 0) {
            listdatachild.put(getResources().getString(R.string.productattribute), childsection_three);
        }
        if (childsection_four.size() > 0) {
            listdatachild.put(getResources().getString(R.string.manageorders), childsection_four);
        }
        if (childsection_five.size() > 0) {
            listdatachild.put(getResources().getString(R.string.messaging), childsection_five);
        }
        if (childRFQ.size() > 0) {
            listdatachild.put(getResources().getString(R.string.requestforquote), childRFQ);
        }
        if (childsection_six.size() > 0) {
            listdatachild.put(getResources().getString(R.string.membershipplans), childsection_six);
        }
        if (childsection_seven.size() > 0) {
            listdatachild.put(getResources().getString(R.string.promotions), childsection_seven);
        }
        if (childsection_eight.size() > 0) {
            listdatachild.put(getResources().getString(R.string.vendordeals), childsection_eight);
        }
        if (childsection_nine.size() > 0) {
            listdatachild.put(getResources().getString(R.string.Transactions), childsection_nine);
        }
        if (childsection_ten.size() > 0) {
            listdatachild.put(getResources().getString(R.string.Reports), childsection_ten);
        }
        if (childsection_eleven.size() > 0) {
            listdatachild.put(getResources().getString(R.string.Settings), childsection_eleven);
        }
        if (childsection_twele.size() > 0) {
            listdatachild.put(getResources().getString(R.string.storepickup), childsection_twele);
        }
        if (childsection_thirty.size() > 0) {
            listdatachild.put(getResources().getString(R.string.advancereport), childsection_thirty);
        }
        if (childsection_fourty.size() > 0) {
            listdatachild.put(getResources().getString(R.string.vendorcms), childsection_fourty);
        }
        if (childsection_fifty.size() > 0) {
            listdatachild.put(getResources().getString(R.string.selectandsell), childsection_fifty);
        }
        if (childsection_seventy.size() > 0) {
          //  listdatachild.put(getResources().getString(R.string.faq), childsection_seventy);
        }
//        if (childsection_eighty.size() > 0) {
//            listdatachild.put(getResources().getString(R.string.vendor_inventory), childsection_eighty);
//        }
//        if (childsection_ninety.size() > 0) {
//            listdatachild.put(getResources().getString(R.string.vendor_advance_report), childsection_ninety);
//        }
        if (childsection_manage_pincode.size() > 0) {
            listdatachild.put(getResources().getString(R.string.pincodeChecker), childsection_manage_pincode);
        }
//        if (childsection_auction.size() > 0) {
//            listdatachild.put(getResources().getString(R.string.auction), childsection_auction);
//        }
        drawerAdapter = new Ced_MultiVendor_NavigationExapandableListAdapter(getApplicationContext(), headersection, listdatachild, navDrawerItems);

        vendorview = View.inflate(this, R.layout.ced_multivendor_vendorprofileindrawer, null);
        vendorname = vendorview.findViewById(R.id.MultiVendor_title);
        emailText = vendorview.findViewById(R.id.emailText);
        vendorname.setOnClickListener(v -> {
//                if (TextUtils.isEmpty(session.getSubVendorId())) {
            Intent profile = new Intent(Ced_MultiVendor_NavigationActivity.this, Ced_MultiVendor_VendorProfile.class);
            profile.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(profile);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
            /*}
            else {
                Intent profile = new Intent(Ced_MultiVendor_NavigationActivity.this, Ced_Sub_Vendor_profile.class);
                profile.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(profile);
                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
            }*/
        });
        vendorpic = vendorview.findViewById(R.id.MultiVendor_icon);

        if (session.isLoggedIn()) {
            setprofilepic(session.getvendorpic());
            setname(session.getvendorname());
            emailText.setText(session.getUserDetails().get(Key_Email));
        }

        vendorpic.setOnClickListener(v -> showGallary(false));
        vendorview.setOnClickListener(null);
        mDrawerList.addHeaderView(vendorview);
        mDrawerList.setGroupIndicator(null);
        mDrawerList.setAdapter(drawerAdapter);
        mDrawerList.setOnItemClickListener((parent, view, position, id) -> mDrawerLayout.closeDrawers());
        mDrawerList.setOnGroupExpandListener(groupPosition -> {
            if (!(listdatachild.containsKey(headersection.get(groupPosition)))) {
                displayView(headersection.get(groupPosition));
            }
        });
        mDrawerList.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
       //     Toast.makeText(getApplicationContext(), headersection.get(groupPosition) + " : " + listdatachild.get(headersection.get(groupPosition)).get(childPosition), Toast.LENGTH_SHORT).show();
            displayView(listdatachild.get(headersection.get(groupPosition)).get(childPosition));
            mDrawerLayout.closeDrawers();
            return false;
        });

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.alert_name, R.string.alert_name);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

    }

    private void setUpNavRec() {

        Log.d("TAG", "setUpNavRec: ");
        navigationRecAdapter = new NavigationRecAdapter(this, MyApplication.navigationItemList, obj -> {
            mDrawerLayout.close();
            if (obj instanceof NavigationChild) {
                NavigationChild child = (NavigationChild) obj;
                handleNavigation(child.getKey());
            } else if (obj instanceof NavigationModel) {
                NavigationModel child = (NavigationModel) obj;
                handleNavigation(child.getParentKey());
            }
        });
        navigationRecView.setAdapter(navigationRecAdapter);


        TextView emailText = navigationView.findViewById(R.id.emailText);
        headerText.setText(session.getvendorname());
        emailText.setText(session.getUserDetails().get(Key_Email));

//        Glide.with(this)
//                .load(session.getvendorpic())
//                .placeholder(R.mipmap.ic_launcher)
//                .error(R.mipmap.ic_launcher)
//                .into(headerLogo);
    }

    private void handleNavigation(String key) {
        new ScreenNavigation(key, this);
    }

    private void displayView(String position) {
      //  Toast.makeText(getApplicationContext(), position, Toast.LENGTH_LONG).show();
        Intent advanceReport = new Intent(this, AdvanceReportHomeActivity.class);

        if (position.equalsIgnoreCase(getString(R.string.dashboard))) {
            Intent intent = new Intent(this, Ced_MultiVendor_VendorDashboard.class);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        } else if (position.equalsIgnoreCase(getString(R.string.VendorProfile))) {
            Intent intent = new Intent(this, Ced_MultiVendor_VendorProfile.class);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        } else if (position.equalsIgnoreCase(getString(R.string.NewProduct))) {
            Intent intent = new Intent(this, Ced_MultiVendor_AddVendorProduct.class);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        } else if (position.equalsIgnoreCase(getString(R.string.ManageProducts))) {
            Intent intent = new Intent(this, Ced_MultiVendor_ManageProducts.class);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        } else if (position.equalsIgnoreCase(getString(R.string.ManageAttribute))) {
            Intent intent = new Intent(this, Ced_MultiVendor_VendorProductAttribute.class);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        } else if (position.equalsIgnoreCase(getString(R.string.ManageAttributeSet))) {
            Intent intent = new Intent(this, Ced_MultiVendor_VendorProductAttributeSet.class);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        } else if (position.equalsIgnoreCase(getString(R.string.ManageOrders))) {
            Intent intent = new Intent(this, Ced_MultiVendor_ManageOrdersList.class);
            intent.putExtra("isFrom","ManageOrders");
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }else if (position.equalsIgnoreCase(getString(R.string.CancelOrders))) {
            Intent intent = new Intent(this, Ced_MultiVendor_ManageOrdersList.class);
            intent.putExtra("isFrom","CancelOrders");
            intent.putExtra("filter","{\"increment_id\":\"\",\"created_at\":{\"from\":\"\",\"to\":\"\"},\"billing_name\":\"\",\"order_total\":{\"from\":\"\",\"to\":\"\"},\"shop_commission_fee\":{\"from\":\"\",\"to\":\"\"},\"order_payment_state\":\"3\"}");
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        } else if (position.equalsIgnoreCase(getString(R.string.ManageInvoice))) {
            Intent intent = new Intent(this, Ced_MultiVendor_InvoiceListing.class);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        } else if (position.equalsIgnoreCase(getString(R.string.ManageShipment))) {
            Intent intent = new Intent(this, Ced_MultiVendor_ShipmentListing.class);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        } else if (position.equalsIgnoreCase(getString(R.string.ManageCreditMemo))) {
            Intent intent = new Intent(this, Ced_MultiVendor_CreditMemoListing.class);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        } else if (position.equalsIgnoreCase(getString(R.string.RequestPaymentsMenu))) {
            Intent intent = new Intent(this, mvp_adv_req_payments.class);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        } else if (position.equalsIgnoreCase(getString(R.string.ViewTransactaions))) {
            Intent intent = new Intent(this, Ced_MultiVendor_ListTransaction.class);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        } else if (position.equalsIgnoreCase(getString(R.string.OrderReports))) {
            Intent intent = new Intent(this, Ced_MultiVendor_OrderReport.class);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        } else if (position.equalsIgnoreCase(getString(R.string.ProductReports))) {
            Intent intent = new Intent(this, Ced_MultiVendor_ProductReport.class);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        } else if (position.equalsIgnoreCase(getString(R.string.TransactaionSettings))) {
            Intent intent = new Intent(this, Ced_MultiVendor_Settings.class);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        } else if (position.equalsIgnoreCase(getString(R.string.ShippingSettings))) {
            Intent intent = new Intent(this, Ced_MultiVendor_ShippingSetting.class);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        } else if (position.equalsIgnoreCase(getString(R.string.ShippingMethods))) {
            Intent intent = new Intent(this, Ced_MultiVendor_ShippingMethod.class);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        } else if (position.equalsIgnoreCase(getString(R.string.manage_pincode))) {
            Intent intent = new Intent(this, mvp_PincodeListing.class);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        } else if (position.equalsIgnoreCase(getString(R.string.assigned_pincode))) {
            Intent intent = new Intent(this, mvp_AssignedPincodeList.class);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }else if (position.equalsIgnoreCase(getString(R.string.change_store))) {
            Intent intent = new Intent(Ced_MultiVendor_NavigationActivity.this, StoreSelection.class);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        } else if (position.equalsIgnoreCase(getString(R.string.viewstores))) {
            Intent intent = new Intent(this, Ced_MultiVendor_StoreListing.class);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        } else if (position.equalsIgnoreCase(getString(R.string.ProductRatingandreview))) {
            Intent intent = new Intent(this, ManageProductReview.class);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        } else if (position.equalsIgnoreCase(getString(R.string.createdeals))) {
            Intent intent = new Intent(this, Ced_MultiVendor_Create_and_Edit_deal.class);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        } else if (position.equalsIgnoreCase(getString(R.string.listdeals))) {
            Intent intent = new Intent(this, Ced_MultiVendor_Deal_Listing.class);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        } else if (position.equalsIgnoreCase(getString(R.string.dealsettings))) {
            Intent intent = new Intent(this, Ced_MultiVendor_Deal_Settings.class);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        } else if (position.equalsIgnoreCase(getString(R.string.vendoradmin))) {
            Intent intent = new Intent(this, Admin_Inbox.class);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        } else if (position.equalsIgnoreCase(getString(R.string.vendorcustomer))) {
            Intent intent = new Intent(this, Customer_Inbox.class);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        } else if (position.equalsIgnoreCase(getString(R.string.managevendorcms))) {
            Intent intent = new Intent(this, Ced_MultiVendor_CmsListing.class);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        } else if (position.equalsIgnoreCase(getString(R.string.managestaticblocks))) {
            Intent intent = new Intent(this, Ced_MultiVendor_BlockListing.class);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        } else if (position.equalsIgnoreCase(getString(R.string.ManageRating))) {
            Intent intent = new Intent(this, ManageProductRating.class);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        } else if (position.equalsIgnoreCase(getString(R.string.managermarequest))) {
            Intent intent = new Intent(this, Ced_MultiVendor_ManageRMARequest.class);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        } else if (position.equalsIgnoreCase(getString(R.string.Orders))) {
            Intent intent = new Intent(this, Ced_MultiVendor_OrdersList.class);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        } else if (position.equalsIgnoreCase(getString(R.string.ManageTickets))) {
            Intent intent = new Intent(this, Manage_Ticket.class);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        } else if (position.equalsIgnoreCase(getString(R.string.addproduct))) {
            Intent intent = new Intent(this, Add_Product_list.class);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        } else if (position.equalsIgnoreCase(getString(R.string.productlist))) {
            Intent intent = new Intent(this, Product_list.class);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        } else if (position.equalsIgnoreCase(getString(R.string.membershipplan))) {
            Intent intent = new Intent(this, Ced_Multivendor_Membership_Plan.class);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        } else if (position.equalsIgnoreCase(getString(R.string.planhistory))) {
            Intent intent = new Intent(this, Ced_Multivendor_Plan_History.class);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        } else if (position.equalsIgnoreCase(getString(R.string.manageauction))) {
            Intent intent = new Intent(this, Ced_Multivendor_ManageAuction.class);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        } else if (position.equalsIgnoreCase(getString(R.string.auctionwinners))) {
            Intent intent = new Intent(this, Ced_Multivendor_AuctionWinners.class);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        } else if (position.equalsIgnoreCase(getString(R.string.add_faq_product))) {
            Intent intent = new Intent(this, FaqListScreen.class);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        } else if (position.equalsIgnoreCase(getString(R.string.inventory_manage))) {
            Intent intent = new Intent(this, SaveInventory.class);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        } else if (position.equalsIgnoreCase(getString(R.string.out_of_stock))) {
            Intent intent = new Intent(this, OutOfStockActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        } else if (position.equalsIgnoreCase(getString(R.string.low_stock_product))) {
            Intent intent = new Intent(Ced_MultiVendor_NavigationActivity.this, LowStock.class).putExtra("type", "low_stock");
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        } else if (position.equalsIgnoreCase(getString(R.string.managequotes))) {
            Intent intent = new Intent(this, Ced_MultiVendor_VendorQuoteManagement.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        } else if (position.equalsIgnoreCase(getString(R.string.manage_proposal))) {
            Intent manage_proposal = new Intent(this, Ced_MultiVendor_Vendor_ViewPO_List.class);
            manage_proposal.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(manage_proposal);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        } else if (position.equalsIgnoreCase(getString(R.string.rfq_setting))) {
            Intent rfq_setting = new Intent(this, mvp_RFQ_Setting.class);
            rfq_setting.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(rfq_setting);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        } else if (position.equalsIgnoreCase(getString(R.string.vendor_sales_report))) {
            advanceReport.putExtra(ACTIVITY_NAME, PRODUCT_SALES_ACTIVITY);
            advanceReport.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(advanceReport);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        } else if (position.equalsIgnoreCase(getString(R.string.product_views))) {
            advanceReport.putExtra(ACTIVITY_NAME, PRODUCT_VIEWS_ACTIVITY);
            advanceReport.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(advanceReport);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        } else if (position.equalsIgnoreCase(getString(R.string.payment_report))) {
            advanceReport.putExtra(ACTIVITY_NAME, PAYMENT_REPORT_ACTIVITY);
            advanceReport.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(advanceReport);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        } else if (position.equalsIgnoreCase(getString(R.string.out_of_stock_products))) {
            advanceReport.putExtra(ACTIVITY_NAME, OUT_OF_STOCK_PRODUCT_ACTIVITY);
            advanceReport.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(advanceReport);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        } else if (position.equalsIgnoreCase(getString(R.string.sold_products))) {
            advanceReport.putExtra(ACTIVITY_NAME, SOLD_PRODUCT_ACTIVITY);
            advanceReport.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(advanceReport);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        } else if (position.equalsIgnoreCase(getString(R.string.return_report))) {
            advanceReport.putExtra(ACTIVITY_NAME, RETURN_REPORT_ACTIVITY);
            advanceReport.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(advanceReport);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ced_multivendor_menu_login, menu);
        MenuItem item = menu.findItem(R.id.MultiVendor_notification);
        MenuItem item2 = menu.findItem(R.id.MultiVendor_profile);
        MenuItemCompat.setActionView(item, R.layout.ced_multivendor_feed_update_count);
        MenuItemCompat.setActionView(item2, R.layout.ced_multivendor_profilestatus);
        profilecount = MenuItemCompat.getActionView(item2);
        profilecount.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_ProfileStatus.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
        notifCount = MenuItemCompat.getActionView(item);
        TextView notification = notifCount.findViewById(R.id.MultiVendor_notitext);
        notification.setText(Ced_MultiVendor_GlobalVariables.noti);
        notifCount.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Notification.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        if(item.getItemId() == R.id.MultiVendor_action_signout){
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.alert_name);
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setMessage(R.string.do_you_want_to_logout);
            builder.setNegativeButton(R.string.no,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            removeTokenToServer(session.getDeviceToken(),session.getVendorid());
                            session.ClearVendorId();
                            session.ClearSubVendor();
                            session.logoutUser();
                            /* FacebookSdk.sdkInitialize(getApplicationContext());*/
                            FacebookSdk.setAutoInitEnabled(true);
                            FacebookSdk.fullyInitialize();
                            LoginManager.getInstance().logOut();
                            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                        }
                    }
            );
            builder.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        //boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void setTitle(CharSequence title) {
       /* this.headerText.setText(title);
        this.headerText.setVisibility(View.VISIBLE);
        Log.d("TAG", "setTitle: " + title);*/
        //headerLogo.setVisibility(View.GONE);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (this.mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void setname(String name) {
        vendorname.setText(getResources().getString(R.string.hellovendortext) + " " + name);
    }

    public void setprofilepic(String picurl) {
        if(Ced_MultiVendor_NavigationActivity.photoUrl == null) {
            Glide.with(getApplicationContext())
                    .load(picurl)
                    .placeholder(R.drawable.guest)
                    .error(R.drawable.guest)
                    .into(vendorpic);
        }else{
            try {
                final InputStream imageStream = getContentResolver().openInputStream(Ced_MultiVendor_NavigationActivity.photoUrl);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                vendorpic.setImageBitmap(selectedImage);
            }catch (Exception e){}
        }
    }

    public void changetitle(String tittle) {
        /*actionBar.setTitle(Html.fromHtml("<b><small>" + tittle + "</small></b>"));*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        mConnectionClassManager.register(mListener);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(error_receiver, intentFilter,RECEIVER_EXPORTED);
        } else {
            registerReceiver(error_receiver, intentFilter);
        }
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        if (session.getStoreLocale() != null) {
            new Ced_Load_Language().setLanguagetoLoad(session.getStoreLocale(), this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(String message_event) {
        Ced_MultiVendor_GlobalVariables.noti = message_event;
        invalidateOptionsMenu();
    }

    public void showImagePOP(ImageView image) {
        Dialog builder = new Dialog(Ced_MultiVendor_NavigationActivity.this);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(dialogInterface -> {
            //nothing;
        });
        ImageView imageView = new ImageView(Ced_MultiVendor_NavigationActivity.this);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        Drawable mDrawable = image.getDrawable();
        Bitmap mBitmap = ((BitmapDrawable) mDrawable).getBitmap();
        imageView.setImageBitmap(mBitmap);

        if (density <= 160) {
            builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                    200,
                    400));
        }
        if (density > 160 && density <= 240) {
            builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                    300,
                    500));
        }
        if (density > 240 && density <= 360) {
            builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                    400,
                    600));
        }
        if (density > 360 && density <= 480) {
            builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                    600,
                    800));
        }
        if (density > 480) {
            builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                    700,
                    900));
        }
        builder.show();
        showGallary(false);
    }

    public void showGallary(boolean isFromNotification) {
         if(isFromNotification)
             mDrawerLayout.open();
         Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, Navigation_CAMERA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        if(requestCode != Navigation_CAMERA_REQUEST)
            return;
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if (resultCode == RESULT_OK) {
            try {
                Ced_MultiVendor_NavigationActivity.photoUrl = imageReturnedIntent.getData();
                String profileuri = UtilityMethods.getRealPathFromURI(this, Ced_MultiVendor_NavigationActivity.photoUrl);
                String[] imagename = profileuri.split("/");
                if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                    Log.i("filename", "" + imagename[imagename.length - 1].trim());
                }
                final InputStream imageStream = getContentResolver().openInputStream(Ced_MultiVendor_NavigationActivity.photoUrl);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                vendorpic.setImageBitmap(selectedImage);
                String encodedLogoImage = encodeImage(selectedImage,"");
                HashMap<String, String> dataforvendorprofile = new HashMap<String, String>();
                dataforvendorprofile.put("vendor_id", session.getVendorid());
                dataforvendorprofile.put("hashkey", session.getHahkey());
                try {
                    JSONObject logo = new JSONObject();
                    logo.put("type", "file");
                    logo.put("name", "profile.jpeg");
                    logo.put("base64_encoded_data", encodedLogoImage);
                    dataforvendorprofile.put("profile_picture", logo.toString());
                }catch (Exception e){

                }
                uploadPhotoApi(dataforvendorprofile);
                Log.i("encodedimage", encodedLogoImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    void uploadPhotoApi(HashMap<String, String> dataforvendorprofile){
        String updateURL = session.getBase_Url() + "vendorapi/index/update";
        Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {

            @Override
            public void processFinish(Object output) throws JSONException {
               String  Jstring = output.toString();
            }
        }, this, "POST", dataforvendorprofile);
        crr.execute(updateURL);
    }

    public void logout() {
        Toast.makeText(getApplicationContext(), "You are disapproved by the admin", Toast.LENGTH_LONG).show();
        session.ClearVendorId();
        session.logoutUser();
        session.ClearSubVendor();
        overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
    }

    @NonNull
    public byte[] loadFile(@NonNull File file) throws IOException {
        InputStream is = new FileInputStream(file);
        long length = file.length();
        byte[] bytes = new byte[(int) length];
        int offset = 0;
        int numRead;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }
        is.close();
        return bytes;
    }

    public void capture_image() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            Dexter.withActivity(Ced_MultiVendor_NavigationActivity.this)
                    .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).
                    withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                if (pictureIntent.resolveActivity(getPackageManager()) != null) {
                                    //Create a file to store the image
                                    File photoFile = null;
                                    try {
                                        photoFile = createImageFile();
                                    } catch (IOException ex) {
                                        ex.printStackTrace();
                                    }
                                    if (photoFile != null) {
                                        Log.e("REpo", "photoFile== " + photoFile.toString());
                                        // Uri photoURI = FileProvider.getUriForFile(this, "com.beenfix.sellerapp.provider", photoFile);
                                        Uri photoURI = FileProvider.getUriForFile(Ced_MultiVendor_NavigationActivity.this, "magentoegypt.locafy.provider", photoFile);
                                        pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                        startActivityForResult(pictureIntent, CAMERA_REQUEST);
                                    }
                                }
                            }
                        }
                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).onSameThread().check();
        }else{
            Dexter.withActivity(Ced_MultiVendor_NavigationActivity.this)
                    .withPermissions(CAMERA,READ_MEDIA_IMAGES).
                    withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                if (pictureIntent.resolveActivity(getPackageManager()) != null) {
                                    //Create a file to store the image
                                    File photoFile = null;
                                    try {
                                        photoFile = createImageFile();
                                    } catch (IOException ex) {
                                        ex.printStackTrace();
                                    }
                                    if (photoFile != null) {
                                        Log.e("REpo", "photoFile== " + photoFile.toString());
                                        // Uri photoURI = FileProvider.getUriForFile(this, "com.beenfix.sellerapp.provider", photoFile);
                                        Uri photoURI = FileProvider.getUriForFile(Ced_MultiVendor_NavigationActivity.this, "magentoegypt.locafy.provider", photoFile);
                                        pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                        startActivityForResult(pictureIntent, CAMERA_REQUEST);
                                    }
                                }
                            }
                        }
                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).onSameThread().check();
        }

    }

    public File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        imagePath = image.getAbsolutePath();
        return image;
    }

    public String getPath(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    public static String encodeImage(Bitmap bm, String origin) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 40, baos);
        byte[] b = baos.toByteArray();

        Bitmap compressedBitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        compressedBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytearray = baos.toByteArray();
        String encImage = "";
        if (origin.equals("mvp_bank_detials")) {
            encImage = Base64.encodeToString(b, Base64.DEFAULT);
        } else {
            encImage = Base64.encodeToString(bytearray, Base64.DEFAULT);
        }
        return encImage;
    }

    public static float calculateFileSize(String filepath) {
        //String filepathstr=filepath.toString();
        File file = new File(filepath);
        long fileSizeInBytes = file.length();
        float fileSizeInKB = fileSizeInBytes / 1024;
        // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
        float fileSizeInMB = fileSizeInKB / 1024;
        return fileSizeInMB;
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);
        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap getResizedBitmapScale(Bitmap bm, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = height*scaleWidth;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(newWidth, scaleHeight);
        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, newWidth, (int) scaleHeight, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    private class ConnectionChangedListener implements ConnectionClassManager.ConnectionClassStateChangeListener {
        @Override
        public void onBandwidthStateChange(ConnectionQuality bandwidthState) {
            mConnectionClass = bandwidthState;
            runOnUiThread(() -> {
                if (mConnectionClass.toString().equals("POOR")) {
                    Toast.makeText(getApplicationContext(), "Low Network connection is detected", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void selectdate_fromcalender(final TextView tittle, final String sdfformat) {
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.DatePickerButton, (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            SimpleDateFormat sdf = new SimpleDateFormat(sdfformat, Locale.US);
            tittle.setText(sdf.format(myCalendar.getTime()));
        }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void removeTokenToServer(String token, String vendor_id) {
        HashMap<String, String> jsonObject = new HashMap<>();
        jsonObject.put("Token", token);
        jsonObject.put("vendor_id", vendor_id);
        jsonObject.put("type", "2");
        RestNotificatioRequest request = new RestNotificatioRequest(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {

            }
        }, getApplicationContext(), "POST", jsonObject);
        request.execute(session.getBase_Url() + "vendorapi/index/removevendordevice");
    }
}