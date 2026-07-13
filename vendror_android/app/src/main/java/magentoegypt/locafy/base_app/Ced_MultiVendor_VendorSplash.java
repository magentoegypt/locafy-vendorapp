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

package magentoegypt.locafy.base_app;

import static androidx.lifecycle.LiveDataKt.observe;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckedTextView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;

import magentoegypt.locafy.vendor_login_section.mvvm.LoginViewModel;
import magentoegypt.locafy.vendor_registration_section.new_registration.RegistrationDynamic;
import com.google.gson.Gson;
import magentoegypt.locafy.R;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_UnAuthourizedRequestError;
import magentoegypt.locafy_constant.Ced_MultiVendor_GlobalVariables;
import magentoegypt.locafy.navigation_drawer.models.NavigationModel;
import magentoegypt.locafy.vendor_dashboard.Ced_MultiVendor_VendorDashboard;
import magentoegypt.locafy.vendor_login_section.Ced_Multivendor_New_Login;
import magentoegypt.locafy.vendor_notification.app.MyApplication;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class
Ced_MultiVendor_VendorSplash extends AppCompatActivity {

    public static String Ced_VOrderApi, Ced_VendorApi, Ced_VProductAttributeApi = "", Ced_VReviewApi, Ced_VSocialLoginApi, Ced_VUPSShippingApi, Ced_VProductApi, Ced;
    Ced_MultiVendor_VendorSessionManagement vendorSessionManagement;
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    HashMap<String, String> dataforaddons;
    String Jstring, Out_string;
    String CurrrentUrl;
    public static JSONObject filterdata;
    String SettingUrl = "";
    Ced_MultiVendor_TypeWriter writer;
    String get_enabled_addons = "";
    Ced_MultiVendor_Addons addons;
    public static JSONArray vendor_navigation = new JSONArray();

    private LoginViewModel loginViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        connectionDetector = new Ced_MultiVendor_ConnectionDetector(Ced_MultiVendor_VendorSplash.this);
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(getApplicationContext());
        dataforaddons = new HashMap<>();
        filterdata = new JSONObject();
        addons = new Ced_MultiVendor_Addons();
        printKeyHash();
        if (connectionDetector.isConnectingToInternet()) {
            loginViewModel = new LoginViewModel();
            setContentView(R.layout.ced_multivendor_activity_vendor_splash);
            vendorSessionManagement = new Ced_MultiVendor_VendorSessionManagement(Ced_MultiVendor_VendorSplash.this);
            //------------------------------------
            writer = (Ced_MultiVendor_TypeWriter) findViewById(R.id.MultiVendor_typewriter);
            vendorSessionManagement = new Ced_MultiVendor_VendorSessionManagement(Ced_MultiVendor_VendorSplash.this);
            writer.setCharacterDelay(100);
            writer.animateText("This is the demo version of the Platinum app.The text will be remove from the live app");
            //----------------------------------------
            SettingUrl = getResources().getString(R.string.base_url) + "vendorapi/index/isRegistrationEnabled";
            get_enabled_addons = getResources().getString(R.string.base_url) + "vendorapi/index/getmoduleList";
            CurrrentUrl = vendorSessionManagement.getBase_Url() + "vendorapi/index/link";
            System.out.println("FCM token: "+vendorSessionManagement.getDeviceToken());
//            loginViewModel.getSetting().observe(this,output -> {
//                try {
//                    JSONObject object = new JSONObject(new Gson().toJson(output));
//                    logText.setText(logText.getText()+"\nsetting response="+new Gson().toJson(output));
//                    if (functionalityList.getExtensionAddon()) {
//                        vendorSessionManagement.registrationenable(object.getString("is_enabled").equals("1"));
//
//                        if (vendorSessionManagement.getVendorid() != null) {
//                            logText.setText(logText.getText()+"\nrequest is calling");
//                            request();
//                        } else {
//                            getaddonsfromurl();
//                        }
//                    } else {
//                        Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                        overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
//                    }
//                } catch (Exception e) {
//                    logText.setText(logText.getText()+"\ngetSetting Exception called");
//                }
//            });
            Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
                @Override
                public void processFinish(Object output) throws JSONException {
                    try {
                        Jstring = output.toString();
                     //   Toast.makeText(getApplicationContext(), "response "+Jstring, Toast.LENGTH_LONG).show();
                     //   Toast.makeText(getApplicationContext(), "getExtensionAddon() "+functionalityList.getExtensionAddon(), Toast.LENGTH_LONG).show();
                        if (functionalityList.getExtensionAddon()) {
                            JSONObject object = new JSONObject(Jstring);
                            vendorSessionManagement.registrationenable(object.getString("is_enabled").equals("1"));

                            if (vendorSessionManagement.getVendorid() != null) {
                                request();
                            } else {
                                getaddonsfromurl();
                            }
                        } else {
                            Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                        }
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(), "Exception "+e.getMessage(), Toast.LENGTH_LONG).show();
                        if (vendorSessionManagement.getVendorid() != null) {
                            request();
                        } else {
                            getaddonsfromurl();
                        }
                    }
                }
            }, Ced_MultiVendor_VendorSplash.this, "Splash");
            crr.execute(SettingUrl);
            if (vendorSessionManagement.getStoreLocale() != null) {
                new Ced_Load_Language().setLanguagetoLoad(vendorSessionManagement.getStoreLocale(), Ced_MultiVendor_VendorSplash.this);
            }
           // show();
        } else {
            Intent nointernet = new Intent(Ced_MultiVendor_VendorSplash.this, Ced_MultiVendor_NoInternetconnection.class);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(nointernet);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }
    }

    public void show() {
        if (vendorSessionManagement.isLoggedIn()) {
            Intent intent = new Intent(Ced_MultiVendor_VendorSplash.this, Ced_MultiVendor_VendorDashboard.class);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        } else {
            if (vendorSessionManagement.getStoreId() != null) {
                Intent intent = new Intent(Ced_MultiVendor_VendorSplash.this, Ced_Multivendor_New_Login.class);
                startActivity(intent);
                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
            } else {
                Intent intent = new Intent(Ced_MultiVendor_VendorSplash.this, StoreSelection.class);
                intent.putExtra("isComingFrom","Splash");
                startActivity(intent);
                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
            }

        }
        finish();
    }

    @Override
    protected void onResume() {
        Ced_MultiVendor_ConnectionDetector connectionDetector = new Ced_MultiVendor_ConnectionDetector(Ced_MultiVendor_VendorSplash.this);
        if (!connectionDetector.isConnectingToInternet()) {
            Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }
        super.onResume();
    }

    public void request() {
        try {
            Log.i("HERE", vendorSessionManagement.getSubVendorId());
            dataforaddons.put("sub_vendor_id", vendorSessionManagement.getSubVendorId());
            dataforaddons.put("vendor_id", vendorSessionManagement.getVendorid());
            dataforaddons.put("hashkey", vendorSessionManagement.getHahkey());
            Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
                @Override
                public void processFinish(Object output) throws JSONException {
                    Jstring = output.toString();
                    Log.i("HERE", "jstring " + Jstring);
                    if (functionalityList.getExtensionAddon()) {
                        JSONObject vendor = new JSONObject(Jstring);
                        if (vendor.has("vendor_approved")) {
                            Log.i("logout", "vendor approved request()");
                            logout();
                        } else {
                            String success = vendor.getJSONObject("data").getString("success");
                            if (success.equals("true")) {
                                if (vendor.getJSONObject("data").has("vendor_navigation")) {
                                    vendor_navigation = vendor.getJSONObject("data").getJSONArray("vendor_navigation");
                                    if (vendor_navigation.length() > 0) {
                                        if (check_is_in_array("Sub-Vendor Profile", vendor_navigation)) {
                                            functionalityList.Vendor_Profile(true);
                                        } else {
                                            Log.i("subvendor", "vendor profile false");
                                            functionalityList.Vendor_Profile(false);
                                        }
                                        if (check_is_in_array("Add Product", vendor_navigation) || check_is_in_array("Product List", vendor_navigation)) {
                                            functionalityList.SelectAndSell(true);
                                            Log.i("subvendor", "vendor select and sell");
                                            if (check_is_in_array("Add Product", vendor_navigation)) {
                                                Log.i("subvendor", "add product true");
                                                functionalityList.AddProduct(true);
                                            } else {
                                                Log.i("subvendor", "add product false");
                                                functionalityList.AddProduct(false);
                                            }
                                            if (check_is_in_array("Product List", vendor_navigation)) {
                                                Log.i("subvendor", "product list true");
                                                functionalityList.ProductList(true);
                                            } else {
                                                Log.i("subvendor", "product list false");
                                                functionalityList.ProductList(false);
                                            }
                                        }
                                        else {
                                            Log.i("subvendor", "vendor select sell false");
                                            functionalityList.SelectAndSell(false);
                                        }

                                        //Store Locator
                                        if (check_is_in_array("Store Locator", vendor_navigation)) {
                                            Log.i("subvendor", "Store Locator true");
//                                            functionalityList.storeLocator(true);
                                        } else {
                                            Log.i("subvendor", "Store Locator false");
//                                            functionalityList.storeLocator(false);
                                        }

                                        if (check_is_in_array("New Product", vendor_navigation)) {
                                            Log.i("subvendor", "new product true");
                                            functionalityList.New_Product(true);
                                        } else {
                                            Log.i("subvendor", "new product false");
                                            functionalityList.New_Product(false);
                                        }
                                        if (check_is_in_array("Manage Products", vendor_navigation)) {
                                            Log.i("subvendor", "manage product true");
                                            functionalityList.Manage_Products(true);
                                        } else {
                                            Log.i("subvendor", "manage product false");
                                            functionalityList.Manage_Products(false);
                                        }
                                        if (check_is_in_array("Product Attributes", vendor_navigation)) {
                                            Log.i("subvendor", "product attribute true");
                                            functionalityList.Vendor_Product_Attribute(Ced_MultiVendor_VendorSplash.Ced_VProductAttributeApi.equals("Ced_VProductAttributeApi"));
                                        } else {
                                            Log.i("subvendor", "product attribute false");
                                            functionalityList.Vendor_Product_Attribute(false);
                                        }
                                        if (check_is_in_array("Manage Vendor CMS", vendor_navigation) || check_is_in_array("Manage Static Blocks", vendor_navigation)) {
                                            Log.i("subvendor", "vendor cms true");
                                            functionalityList.VedndorCms(true);
                                            if (check_is_in_array("Manage Vendor CMS", vendor_navigation)) {
                                                Log.i("subvendor", "manage vendor cms true");
                                                functionalityList.ManageVendorCMS(true);
                                            } else {
                                                Log.i("subvendor", "vendor cms false");
                                                functionalityList.ManageVendorCMS(false);
                                            }
                                            if (check_is_in_array("Manage Static Blocks", vendor_navigation)) {
                                                Log.i("subvendor", "manage static blocks true");
                                                functionalityList.ManageStaticBlocks(true);
                                            } else {
                                                Log.i("subvendor", "manage static blocks false");
                                                functionalityList.ManageStaticBlocks(false);
                                            }
                                        } else {
                                            Log.i("subvendor", "vendor cms false");
                                            functionalityList.VedndorCms(false);
                                        }
                                        if (check_is_in_array("Manage Orders", vendor_navigation) || check_is_in_array("Manage Invoice", vendor_navigation) || check_is_in_array("Manage Shipment", vendor_navigation) || check_is_in_array("Manage Credit Memo", vendor_navigation)) {
                                            Log.i("subvendor", "orders true");
                                            functionalityList.Orders(true);
                                            if (check_is_in_array("Manage Orders", vendor_navigation)) {
                                                Log.i("subvendor", "manage order true");
                                                functionalityList.Manage_Orders(true);
                                            } else {
                                                Log.i("subvendor", "manage orders false");
                                                functionalityList.Manage_Orders(false);
                                            }
                                            if (check_is_in_array("Manage Invoice", vendor_navigation)) {
                                                Log.i("subvendor", "manage invoice true");
                                                functionalityList.Manage_Invoice(true);
                                            } else {
                                                Log.i("subvendor", "manage invoice false");
                                                functionalityList.Manage_Invoice(false);
                                            }
                                            if (check_is_in_array("Manage Shipment", vendor_navigation)) {
                                                Log.i("subvendor", "manage shipment true");
                                                functionalityList.Manage_Shipment(true);
                                            } else {
                                                Log.i("subvendor", "manage shipment false");
                                                functionalityList.Manage_Shipment(false);
                                            }
                                            if (check_is_in_array("Manage Credit Memo", vendor_navigation)) {
                                                Log.i("subvendor", "manage credit memo true");
                                                functionalityList.Manage_Credit_Memo(true);
                                            } else {
                                                Log.i("subvendor", "manage credit memo false");
                                                functionalityList.Manage_Credit_Memo(false);
                                            }
                                        } else {
                                            Log.i("subvendor", "orders false");
                                            functionalityList.Orders(false);
                                        }
                                        functionalityList.Transactions(check_is_in_array("Transactions", vendor_navigation));
                                        if (check_is_in_array("Order Reports", vendor_navigation) || check_is_in_array("Product Reports", vendor_navigation)) {
                                            functionalityList.Reports(true);
                                            functionalityList.OrderReports(check_is_in_array("Order Reports", vendor_navigation));
                                            functionalityList.ProductsReports(check_is_in_array("Product Reports", vendor_navigation));
                                        } else {
                                            functionalityList.Reports(false);
                                        }
                                        if (check_is_in_array("Transaction Settings", vendor_navigation) || check_is_in_array("Shipping Settings", vendor_navigation) || check_is_in_array("Shipping Methods", vendor_navigation)) {
                                            functionalityList.Settings(true);
                                            functionalityList.Transaction_Settings(check_is_in_array("Transaction Settings", vendor_navigation));
                                            functionalityList.Shipping_Settings(check_is_in_array("Shipping Settings", vendor_navigation));
                                            functionalityList.Shipping_Methods(check_is_in_array("Shipping Methods", vendor_navigation));
                                        } else {
                                            functionalityList.Settings(false);
                                        }
                                        if (check_is_in_array("Membership Plan", vendor_navigation) || check_is_in_array("Plan History", vendor_navigation)) {
                                            functionalityList.MemberShipPlans(true);
                                            functionalityList.MemberShipPlan(check_is_in_array("Membership Plan", vendor_navigation));
                                            functionalityList.PlanHistory(check_is_in_array("Plan History", vendor_navigation));
                                        } else {
                                            functionalityList.MemberShipPlans(false);
                                        }
                                        if (check_is_in_array("Promotions", vendor_navigation)) {
                                            functionalityList.Promotions(true);
                                            functionalityList.CatalogPriceRule(check_is_in_array("Catalog Price Rules", vendor_navigation));
                                            functionalityList.ShoppingCartPriceRule(check_is_in_array("Sopping Cart Price Rules", vendor_navigation));
                                        } else {
                                            functionalityList.Promotions(false);
                                        }
                                        functionalityList.AuctionApi(false);
                                        functionalityList.RequestForQuote(false);
                                        functionalityList.RMA(false);
                                        functionalityList.POApi(false);
                                        functionalityList.Messaging(false);
                                        functionalityList.Vendor_Deals(false);
                                        functionalityList.StorePickup(false);
                                        functionalityList.AdvanceReport(false);
                                        functionalityList.AssoiciatedSubVendors(false);

                                        functionalityList.FavouriteSeller(false);
                                        functionalityList.PincodeChecker(false);
                                        functionalityList.ProductRatingAndReview(true);
                                    }
                                        /*for (int i = 0; i < vendor_navigation.length(); i++) {
                                            Log.i("subvendorarray", "array- " + vendor_navigation.getString(i));
                                        }*/
                                }
                                vendorSessionManagement.savevendorname(vendor.getJSONObject("data").getString("vendor_name"));
                                vendorSessionManagement.savevendorpic(vendor.getJSONObject("data").getString("profile_picture"));
                                Ced_MultiVendor_GlobalVariables.progress = Integer.parseInt(vendor.getJSONObject("data").getString("profile_complete"));
                                Ced_MultiVendor_GlobalVariables.noti = vendor.getJSONObject("data").getString("valerts");
                                getaddonsfromurl();

                            } else {
                                vendorSessionManagement.ClearVendorId();
                                vendorSessionManagement.ClearSubVendor();
                                vendorSessionManagement.logoutUser();
                                Log.i("logout", "else logout user");
                                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                              //  Toast.makeText(getApplicationContext(), vendor.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
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
            }, Ced_MultiVendor_VendorSplash.this, "POST", dataforaddons, "Splash");
            crr.execute(CurrrentUrl);
        } catch (Exception e) {
            Intent main = new Intent(Ced_MultiVendor_VendorSplash.this, Ced_MultiVendor_VendorSplash.class);
            main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(main);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }
    }

    private JSONArray getVendorNavigationArray(JSONArray jsonArray) {

        MyApplication.navigationItemList.clear();
        JSONArray navigationArray = new JSONArray();
        if (null == jsonArray)
            return navigationArray;
        for (int a = 0; a < jsonArray.length(); a++) {
            try {
                JSONObject object = jsonArray.getJSONObject(a);
                navigationArray.put(object.getString("parent_value"));
                if (object.has("childs")) {
                    JSONArray childs = object.getJSONArray("childs");
                    if (childs.length() > 0) {
                        for (int b = 0; b < childs.length(); b++) {
                            JSONObject childObject = childs.getJSONObject(b);
                            navigationArray.put(childObject.getString("value"));
                        }
                    }
                }

                Gson gson = new Gson();
                NavigationModel obj = gson.fromJson(object.toString(), NavigationModel.class);

                Log.d("TAG", "getVendorNavigationArrayA: " + obj.getParentKey());
                MyApplication.navigationItemList.add(obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.d("TAG", "getVendorNavigationArrayB: " + MyApplication.navigationItemList.toString());
        return navigationArray;
    }

    private void getaddonsfromurl() {
        Log.i("subvendor", "getaddons called");
        Ced_MultiVendor_ClientRequestResponse clientRequestResponse = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                if(output.toString().isEmpty()){
                    Toast.makeText(MyApplication.getInstance().getApplicationContext(),"Something went wrong.",
                            Toast.LENGTH_SHORT).show();
                }else{
                    GetEnabledAddons(output.toString());
                }
            }
        }, Ced_MultiVendor_VendorSplash.this, "Splash");
        clientRequestResponse.execute(get_enabled_addons);

//        loginViewModel.get_enabled_addons().observe(this,output -> {
//            try {
//                GetEnabledAddons(new Gson().toJson(output));
//            }catch (Exception e){
//                logText.setText(logText.getText()+"\ngetaddonsfromurl Exception called");
//            }
//        });
    }

    private void GetEnabledAddons(String addonsenabled) throws JSONException {
        JSONObject object = new JSONObject(addonsenabled);
        if (object.has("vendor_approved")) {
            Log.i("subvendor", "get enabled if executes");
            Log.i("logout", "vendor approved get enabled");
            logout();
        } else {
            Log.i("subvendor", "get enabled else executes");
            JSONObject enabled_modules = object.getJSONObject("data").getJSONObject("modules");
            if (enabled_modules.has("Ced_VOrderApi")) {
                functionalityList.Orders(true);
            }
            if (enabled_modules.has("Ced_VProductAttributeApi")) {
                functionalityList.Vendor_Product_Attribute(true);
            }
            if (enabled_modules.has("Ced_VSocialLoginApi")) {
                functionalityList.Social_Login(true);
            }
            if (enabled_modules.has("Ced_VProductApi")) {
                functionalityList.ProductAddon(true);
            }
            if (enabled_modules.has("Ced_VRfqApi")) {
                functionalityList.RequestForQuote(true);
            }
            if (enabled_modules.has("Ced_VRmaApi")) {
                functionalityList.RMA(true);
            }
            if (enabled_modules.has("Ced_VMessagingApi")) {
                functionalityList.Messaging(true);
            }else{
                functionalityList.Messaging(false);
            }
            if (enabled_modules.has("Ced_VMembershipApi")) {
                functionalityList.MemberShipPlans(true);
            }else{
                functionalityList.MemberShipPlans(false);
            }
            if (enabled_modules.has("Ced_VPromotionsApi")) {
                functionalityList.Promotions(true);
            }
            if (enabled_modules.has("Ced_VDealApi")) {
                functionalityList.Vendor_Deals(true);
            }
            if (enabled_modules.has("Ced_VSubAccountApi")) {
                Log.i("subvendoraccount", "true");
                functionalityList.AssoiciatedSubVendors(true);
            }
            /*if (enabled_modules.has("Ced_VSubAccountApi"))
            {
                functionalityList.AssoiciatedSubVendors(true);
            }*/
            if (enabled_modules.has("Ced_VStorepickupApi")) {
                functionalityList.StorePickup(true);
            }
            if (enabled_modules.has("Ced_VCmsApi")) {
                functionalityList.VedndorCms(true);
            }else {
                functionalityList.VedndorCms(false);
            }
            if (enabled_modules.has("Ced_VMultiSellerApi")) {
                functionalityList.SelectAndSell(true);
            }
            if (enabled_modules.has("Ced_VAuctionApi")) {
                functionalityList.AuctionApi(true);
            }
            if (enabled_modules.has("Ced_VReportApi")) {
                functionalityList.AdvanceReport(true);
            }
            if (enabled_modules.has("Ced_VPoApi")) {
                functionalityList.POApi(true);
            }

            if (enabled_modules.has("Ced_VPincodeApi")) {
                functionalityList.PincodeChecker(true);
            }

            functionalityList.New_Product(true);
            functionalityList.Manage_Products(true);
            functionalityList.Transactions(true);
            functionalityList.Request_Payments(true);
            functionalityList.View_Transactions(true);
            functionalityList.Reports(true);
            functionalityList.Settings(true);
            functionalityList.Vendor_Profile(true);
            if (enabled_modules.has("Ced_VendorApi")) {
                Ced_VendorApi = "Ced_VendorApi";
            } else {
                Ced_VendorApi = "null";
            }

            if (enabled_modules.has("Ced_VReviewApi")) {
                Ced_VReviewApi = "Ced_VReviewApi";
            } else {
                Ced_VReviewApi = "null";
            }
            if (enabled_modules.has("Ced_VSocialLoginApi")) {
                Ced_VSocialLoginApi = "Ced_VSocialLoginApi";
            } else {
                Ced_VSocialLoginApi = "null";
            }
            if (enabled_modules.has("Ced_VUPSShippingApi")) {
                Ced_VUPSShippingApi = "Ced_VUPSShippingApi";
            } else {
                Ced_VUPSShippingApi = "null";
            }
            if (enabled_modules.has("Ced_VProductApi")) {
                Ced_VProductApi = "Ced_VProductApi";
            } else {
                Ced_VProductApi = "null";
            }
            show();
        }
    }

    public void logout() {
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.vendordisapprovedtext), Toast.LENGTH_LONG).show();
        vendorSessionManagement.ClearVendorId();
        vendorSessionManagement.ClearSubVendor();
        vendorSessionManagement.logoutUser();
        overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
    }

    public boolean check_is_in_array(String check_to, JSONArray tab_array) throws JSONException {
        boolean flag = false;
        for (int i = 0; i < tab_array.length(); i++) {
            if (tab_array.getString(i).equals(check_to)) {
                Log.i("subvendor", "" + check_to + " " + true + "");
                flag = true;
            }
        }
        return flag;
    }


    private void printKeyHash() {
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.beenfix.sellerapp", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("frozen", "KeyHash1: " + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("frozen", "KeyHash2: " + e.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("frozen", "KeyHash3: " + e.toString());
        }
    }
}
