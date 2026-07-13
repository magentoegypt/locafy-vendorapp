/*
 *
 *   Copyright /*
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
package magentoegypt.locafy.vendor_registration_section.customer_as_vendor_registration;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import magentoegypt.locafy.api_request_response_section.RestNotificatioRequest;
import magentoegypt.locafy.base_app.Ced_MultiVendor_FontSetting;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorSplash;
import magentoegypt.locafy_constant.Ced_MultiVendor_GlobalVariables;
import magentoegypt.locafy.vendor_dashboard.Ced_MultiVendor_VendorDashboard;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.vendor_login_section.Ced_Multivendor_New_Login;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_UnAuthourizedRequestError;
import magentoegypt.locafy.vendor_registration_section.new_registration.Ced_MultiVendor_VendorApprovalRequired;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;
import magentoegypt.locafy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Ced_MultiVendor_CustomerAsVendorRegistration extends Activity {
    Ced_MultiVendor_FontSetting fontSetting;
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorSessionManagement vendorSessionManagement;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    String email = "";
    String saveurl = "";
    String Jstring = "";
    EditText vendor_publicname;
    EditText vendor_shopurl;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        fontSetting = new Ced_MultiVendor_FontSetting();
        connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        vendorSessionManagement = new Ced_MultiVendor_VendorSessionManagement(getApplicationContext());
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(getApplicationContext());
        final String customer_id = getIntent().getStringExtra("customer_id");
        email = getIntent().getStringExtra("email");
        saveurl = vendorSessionManagement.getBase_Url() + "vendorapi/index/approvalPost";
        if (connectionDetector.isConnectingToInternet()) {
            try {
                setContentView(R.layout.ced_multivendor_customerasvendor);
                vendor_publicname = findViewById(R.id.MultiVendor_vendor_publicname);
                vendor_shopurl = findViewById(R.id.MultiVendor_vendor_shopurl);
                submit = findViewById(R.id.MultiVendor_submit);
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (vendor_publicname.getText().toString().isEmpty()) {
                            vendor_publicname.setError(getResources().getString(R.string.empty));
                            vendor_publicname.requestFocus();
                        } else {
                            if (vendor_shopurl.getText().toString().isEmpty()) {
                                vendor_shopurl.setError(getResources().getString(R.string.empty));
                                vendor_shopurl.requestFocus();
                            } else {
                                try {
                                    HashMap<String, String> dataforregister = new HashMap<String, String>();
                                    JSONObject registerjson = new JSONObject();
                                    JSONObject publicnamejson = new JSONObject();
                                    publicnamejson.put("key", "public_name");
                                    publicnamejson.put("value", vendor_publicname.getText().toString());
                                    JSONArray insideregister = new JSONArray();
                                    insideregister.put(publicnamejson);
                                    JSONObject shopurljson = new JSONObject();
                                    shopurljson.put("key", "shop_url");
                                    shopurljson.put("value", vendor_shopurl.getText().toString());
                                    insideregister.put(shopurljson);
                                    registerjson.put("vendor", insideregister);
                                    if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                        Log.i("registerjson", "" + registerjson);
                                    }

                                    dataforregister.put("approveaccount", registerjson.toString());
                                    dataforregister.put("customer_id", customer_id);
                                    try {
                                        Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
                                            @Override
                                            public void processFinish(Object output) throws JSONException {
                                                Jstring = output.toString();
                                                if (functionalityList.getExtensionAddon()) {
                                                    JSONObject mainobject = new JSONObject(Jstring);
                                                    JSONObject vendorobject = mainobject.getJSONObject("data");
                                                    JSONArray vendorarray = vendorobject.getJSONArray("customer");
                                                    JSONObject vendordata = vendorarray.getJSONObject(0);
                                                    if (vendordata.getString("success").equals("true")) {
                                                        if (vendordata.has("hashkey")) {
                                                            if (vendordata.has("isConfirmationRequired")) {
                                                                if (vendordata.getString("isConfirmationRequired").equals("NO")) {
                                                                    Toast.makeText(getApplicationContext(), vendordata.getString("message"), Toast.LENGTH_LONG).show();
                                                                    vendorSessionManagement.createLoginSession(vendordata.getString("hashkey"), email);
                                                                    vendorSessionManagement.saveVendorId(vendordata.getString("vendor_id"));
                                                                    vendorSessionManagement.saveCustomerId(vendordata.getString("customer_id"));
                                                                    vendorSessionManagement.savevendorname(vendordata.getString("vendor_name"));
                                                                    vendorSessionManagement.savevendorpic(vendordata.getString("profile_picture"));
                                                                    sendRegistrationToServer(vendorSessionManagement.getDeviceToken(), vendordata.getString("vendor_id"));
                                                                    Ced_MultiVendor_GlobalVariables.progress = Integer.parseInt(vendordata.getString("profile_complete"));
                                                                    Ced_MultiVendor_GlobalVariables.noti = vendordata.getString("valerts");
                                                                    JSONArray vendor_navigation = vendordata.getJSONArray("vendor_link");
                                                                    for (int i = 0; i < vendor_navigation.length(); i++) {
                                                                        if (vendor_navigation.getString(i).equals("Vendor Profile")) {
                                                                            functionalityList.Vendor_Profile(true);
                                                                        }
                                                                        if (vendor_navigation.getString(i).equals("New Product")) {
                                                                            functionalityList.New_Product(true);
                                                                        }
                                                                        if (vendor_navigation.getString(i).equals("Manage Products")) {
                                                                            functionalityList.Manage_Products(true);
                                                                        }
                                                                        if (vendor_navigation.getString(i).equals("Orders")) {
                                                                            functionalityList.Orders(true);
                                                                        }
                                                                        if (vendor_navigation.getString(i).equals("Request Payments")) {
                                                                            functionalityList.Request_Payments(true);
                                                                        }
                                                                        if (vendor_navigation.getString(i).equals("View Transactions")) {
                                                                            functionalityList.View_Transactions(true);
                                                                        }
                                                                        if (vendor_navigation.getString(i).equals("Order Reports")) {
                                                                            functionalityList.Order_Reports(true);
                                                                        }
                                                                        if (vendor_navigation.getString(i).equals("Product Reports")) {
                                                                            functionalityList.Product_Reports(true);
                                                                        }
                                                                        if (vendor_navigation.getString(i).equals("Transaction Settings")) {
                                                                            functionalityList.Transaction_Settings(true);
                                                                        }
                                                                        if (vendor_navigation.getString(i).equals("Vendor Product Attribute")) {
                                                                            functionalityList.Vendor_Product_Attribute(true);
                                                                        }
                                                                        if (vendor_navigation.getString(i).equals("Shipping Settings")) {
                                                                            functionalityList.Shipping_Settings(true);
                                                                        }
                                                                        if (vendor_navigation.getString(i).equals("Shipping Methods")) {
                                                                            functionalityList.Shipping_Methods(true);
                                                                        }

                                                                    }
                                                                    Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_VendorDashboard.class);
                                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                    startActivity(intent);
                                                                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                                                                }
                                                            }
                                                        } else {
                                                            if (vendordata.has("isConfirmationRequired")) {
                                                                if (vendordata.getString("isConfirmationRequired").equals("YES")) {

                                                                    Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_VendorApprovalRequired.class);
                                                                    intent.putExtra("message", vendordata.getString("message"));
                                                                    intent.putExtra("firstname", vendordata.getString("first_name"));
                                                                    intent.putExtra("lastname", vendordata.getString("last_name"));
                                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                    startActivity(intent);
                                                                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                                                                }
                                                            }
                                                        }

                                                    } else {
                                                        Toast.makeText(getApplicationContext(), vendordata.getString("message"), Toast.LENGTH_LONG).show();
                                                    }

                                                } else {
                                                    Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);
                                                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                                                }


                                            }
                                        }, Ced_MultiVendor_CustomerAsVendorRegistration.this, "POST", dataforregister);
                                        crr.execute(saveurl);
                                    } catch (Exception e) {
                                        Intent main = new Intent(Ced_MultiVendor_CustomerAsVendorRegistration.this, Ced_MultiVendor_VendorSplash.class);
                                        main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(main);
                                        overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                                    }
                                } catch (JSONException e) {
                                    Intent main = new Intent(Ced_MultiVendor_CustomerAsVendorRegistration.this, Ced_MultiVendor_VendorSplash.class);
                                    main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(main);
                                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                                }

                            }
                        }

                    }
                });

            } catch (Exception e) {
                /*e.printStackTrace();*/
                Intent main = new Intent(getApplicationContext(), Ced_Multivendor_New_Login.class);
                main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(main);
                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
            }
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

    private void sendRegistrationToServer(String token, String vendor_id) {
        vendorSessionManagement = new Ced_MultiVendor_VendorSessionManagement(getApplicationContext());
        HashMap<String, String> jsonObject = new HashMap<>();
        jsonObject.put("Token", token);
        jsonObject.put("vendor_id", vendor_id);
        jsonObject.put("type", "2");
        RestNotificatioRequest request = new RestNotificatioRequest(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {

            }
        }, getApplicationContext(), "POST", jsonObject);
        request.execute(vendorSessionManagement.getBase_Url() + "vendorapi/index/vendordevice");
    }

}
