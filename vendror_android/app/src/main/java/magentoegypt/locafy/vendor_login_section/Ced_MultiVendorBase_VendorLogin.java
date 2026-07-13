package magentoegypt.locafy.vendor_login_section;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import magentoegypt.locafy.R;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_ClientRequestResponseRest_New;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_UnAuthourizedRequestError;
import magentoegypt.locafy_constant.Ced_MultiVendor_GlobalVariables;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorSplash;
import magentoegypt.locafy.navigation_drawer.models.NavigationModel;
import magentoegypt.locafy.vendor_dashboard.Ced_MultiVendor_VendorDashboard;
import magentoegypt.locafy.vendor_notification.app.MyApplication;
import magentoegypt.locafy.vendor_registration_section.customer_as_vendor_registration.Ced_MultiVendor_CustomerAsVendorRegistration;
import magentoegypt.locafy.vendor_registration_section.new_registration.Ced_MultiVendor_VendorRegistration;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class Ced_MultiVendorBase_VendorLogin extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 0;
    TextView register;
    TextView forgotpass;
    EditText vendor_name;
    EditText vendor_password;
    Button login;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    HashMap<String, String> dataforlogin;
    Ced_MultiVendor_VendorSessionManagement vendorSessionManagement;
    String Jstring;
    String CurrrentUrl, social_login_url;
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    CallbackManager callbackManager;
    LoginButton facebook_login_button;
    boolean flag = true;
    LinearLayout social_login_linear;
    private GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);*/
        FacebookSdk.setAutoInitEnabled(true);
        FacebookSdk.fullyInitialize();
        callbackManager = CallbackManager.Factory.create();
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        vendorSessionManagement = new Ced_MultiVendor_VendorSessionManagement(getApplicationContext());
        social_login_url = vendorSessionManagement.getBase_Url() + "vsocialloginapi/customer/register";
        connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        if (connectionDetector.isConnectingToInternet()) {
            getFbHash();
            setContentView(R.layout.ced_multivendor_activity_vendor_login_base);
            functionalityList = new Ced_MultiVendor_VendorFunctionalityList(getApplicationContext());
            dataforlogin = new HashMap<String, String>();
            vendor_name = findViewById(R.id.email);
            vendor_password = findViewById(R.id.MultiVendor_vendor_password);
            login = findViewById(R.id.login);
            register = findViewById(R.id.signup);
            forgotpass = findViewById(R.id.forgotpassword);
            social_login_linear = findViewById(R.id.MultiVendor_social_login_linear);
            social_login_linear.setVisibility(View.GONE);
            /* if (functionalityList.getSocialLogin()) {
                social_login_linear.setVisibility(View.VISIBLE);
            }*/

            /****************************************************/
            /*Google Login Setup*/


            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
            SignInButton signInButton = findViewById(R.id.MultiVendor_sign_in_button);
            signInButton.setSize(SignInButton.SIZE_WIDE);
            signInButton.setColorScheme(SignInButton.COLOR_DARK);
            signInButton.setScopes(gso.getScopeArray());
            signInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signInFromGoogle();
                }
            });
            /*****************************************************/
            /*****************************************************/
            /*Facebook Login Setup*/
            facebook_login_button = findViewById(R.id.MultiVendor_facebook_login_button);
            facebook_login_button.setReadPermissions("email");
            /*facebook_login_button.setReadPermissions("public_profile");*/
            facebook_login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    AccessToken accessToken = loginResult.getAccessToken();
                    Profile profile = Profile.getCurrentProfile();
                    if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                        Log.i("facebook", "" + AccessToken.getCurrentAccessToken().getToken());
                    }
//                name = profile.getName();
                    //   link = profile.getLinkUri().toString();
                    //      pic = "https://graph.facebook.com/" + profile.getId() + "/picture?type=large";
                    if (AccessToken.getCurrentAccessToken().getToken() != null) {
                        checkFacebookLogin();
                    }
                }

                @Override
                public void onCancel() {
                    Toast.makeText(Ced_MultiVendorBase_VendorLogin.this, "Login attempt canceled.", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onError(FacebookException error) {

                }
            });
            /***************************************************************/

            if (vendorSessionManagement.isregistraionenable()) {
                register.setVisibility(View.VISIBLE);
            }
            CurrrentUrl = vendorSessionManagement.getBase_Url() + "vendorapi/index/login";
            register.setOnClickListener(v -> {
                Intent createaccount = new Intent(Ced_MultiVendorBase_VendorLogin.this, Ced_MultiVendor_VendorRegistration.class);
                startActivity(createaccount);
                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
            });
            forgotpass.setOnClickListener(v -> {
                Intent createaccount = new Intent(Ced_MultiVendorBase_VendorLogin.this, Ced_MultiVendor_ForgotPassWord.class);
                startActivity(createaccount);
                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
            });
            login.setOnClickListener(v -> {
                if (vendor_name.getText().toString().isEmpty()) {
                    vendor_name.setError(getResources().getString(R.string.empty));
                } else {
                    if (vendor_password.getText().toString().isEmpty()) {
                        vendor_password.setError(getResources().getString(R.string.empty));
                    } else {
                        try {
                            dataforlogin.put("email", vendor_name.getText().toString());
                            dataforlogin.put("password", vendor_password.getText().toString());
                            Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {

                                @Override
                                public void processFinish(Object output) throws JSONException {
                                    Jstring = output.toString();
                                    if (functionalityList.getExtensionAddon()) {
                                        checklogin();
                                    } else {
                                        Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                                    }
                                }
                            }, Ced_MultiVendorBase_VendorLogin.this, "POST", dataforlogin);
                            crr.execute(CurrrentUrl);
                        } catch (Exception e) {
                            Intent main = new Intent(Ced_MultiVendorBase_VendorLogin.this, Ced_MultiVendor_VendorSplash.class);
                            main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(main);
                            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                        }
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


    private void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void signInFromGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void getVendorNavigationArray(JSONArray jsonArray) {

        MyApplication.navigationItemList.clear();
        JSONArray navigationArray = new JSONArray();
        if (null == jsonArray)
            return;
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
    }


    private void checklogin() throws JSONException {
        JSONObject login_object = new JSONObject(Jstring);
        JSONArray login_array = login_object.getJSONObject("data").getJSONArray("customer");
        String success = login_array.getJSONObject(0).getString("success");
        if (success.equals("true")) {
            String[] array = login_array.getJSONObject(0).getString("vendor_name").split(" ");
            vendorSessionManagement.saveCustomerId(login_array.getJSONObject(0).getString("customer_id"));
            vendorSessionManagement.createLoginSession(login_array.getJSONObject(0).getString("hashkey"), vendor_name.getText().toString());
            if (login_array.getJSONObject(0).has("vendor_id")) {
                vendorSessionManagement.saveVendorId(login_array.getJSONObject(0).getString("vendor_id"));
            }
            vendorSessionManagement.savevendorname(array[0]);
            vendorSessionManagement.savevendorpic(login_array.getJSONObject(0).getString("profile_picture"));
            Ced_MultiVendor_GlobalVariables.progress = Integer.parseInt(login_array.getJSONObject(0).getString("profile_complete"));
            Ced_MultiVendor_GlobalVariables.noti = login_array.getJSONObject(0).getString("valerts");
            JSONArray vendor_navigation = login_array.getJSONObject(0).getJSONArray("vendor_link");
            getVendorNavigationArray(vendor_navigation);
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
                    functionalityList.Vendor_Product_Attribute(Ced_MultiVendor_VendorSplash.Ced_VProductAttributeApi.equals("Ced_VProductAttributeApi"));
                }
                if (vendor_navigation.getString(i).equals("Shipping Settings")) {
                    functionalityList.Shipping_Settings(Ced_MultiVendor_VendorSplash.Ced_VUPSShippingApi.equals("Ced_VUPSShippingApi"));
                }
                if (vendor_navigation.getString(i).equals("Shipping Methods")) {
                    functionalityList.Shipping_Methods(Ced_MultiVendor_VendorSplash.Ced_VUPSShippingApi.equals("Ced_VUPSShippingApi"));
                }
                if (vendor_navigation.getString(i).equals("Manage Orders")) {
                    functionalityList.Manage_Orders(Ced_MultiVendor_VendorSplash.Ced_VOrderApi.equals("Ced_VOrderApi"));
                }
                if (vendor_navigation.getString(i).equals("Manage Invoice")) {
                    functionalityList.Manage_Invoice(Ced_MultiVendor_VendorSplash.Ced_VOrderApi.equals("Ced_VOrderApi"));
                }
                if (vendor_navigation.getString(i).equals("Manage Shipment")) {
                    functionalityList.Manage_Shipment(Ced_MultiVendor_VendorSplash.Ced_VOrderApi.equals("Ced_VOrderApi"));
                }
                if (vendor_navigation.getString(i).equals("Manage Credit Memo")) {
                    functionalityList.Manage_Credit_Memo(Ced_MultiVendor_VendorSplash.Ced_VOrderApi.equals("Ced_VOrderApi"));
                }
                if (vendor_navigation.getString(i).equals("Manage Attribute")) {
                    functionalityList.Manage_Attribute(Ced_MultiVendor_VendorSplash.Ced_VProductAttributeApi.equals("Ced_VProductAttributeApi"));
                }else{
                    functionalityList.Manage_Attribute(false);
                }
                if (vendor_navigation.getString(i).equals("Manage Attribute Set")) {
                    functionalityList.Manage_Attribute_Set(Ced_MultiVendor_VendorSplash.Ced_VProductAttributeApi.equals("Ced_VProductAttributeApi"));
                }else{
                    functionalityList.Manage_Attribute_Set(false);
                }
                if (vendor_navigation.getString(i).equals("Transactions")) {
                    functionalityList.Transactions(true);
                }
                if (vendor_navigation.getString(i).equals("Settings")) {
                    functionalityList.Settings(true);
                }
                if (vendor_navigation.getString(i).equals("Reports")) {
                    functionalityList.Reports(true);
                }
                if (vendor_navigation.getString(i).equals("Simple")) {
                    functionalityList.SimpleProduct(true);
                }
                if (vendor_navigation.getString(i).equals("Virtual")) {
                    functionalityList.VirtualProduct(true);
                }
                if (vendor_navigation.getString(i).equals("Bundle")) {
                    functionalityList.BundleProduct(true);
                }
                if (vendor_navigation.getString(i).equals("Grouped")) {
                    functionalityList.GroupedProduct(true);
                }
                if (vendor_navigation.getString(i).equals("Configurable")) {
                    functionalityList.ConfigurableProduct(true);
                }
            }
            Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_VendorDashboard.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("name", array[0]);
            intent.putExtra("picurl", login_array.getJSONObject(0).getString("profile_picture"));
            intent.putExtra("vendor_id", login_array.getJSONObject(0).getString("vendor_id"));
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        } else {
            if (login_array.getJSONObject(0).has("is_vendor")) {
                Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_CustomerAsVendorRegistration.class);
                intent.putExtra("customer_id", login_array.getJSONObject(0).getString("is_vendor"));
                intent.putExtra("email", vendor_name.getText().toString());
                startActivity(intent);
                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                Toast.makeText(Ced_MultiVendorBase_VendorLogin.this, login_array.getJSONObject(0).getString("message"), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(Ced_MultiVendorBase_VendorLogin.this, login_array.getJSONObject(0).getString("message"), Toast.LENGTH_LONG).show();
            }
        }
    }

    /*********************Check FaceBook Login *****************************/
    private void checkFacebookLogin() {
        /*LoginManager.getInstance().logInWithReadPermissions(VendorLogin.this, Arrays.asList("public_profile","email"));*/
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                    Log.i("LoginActivity", response.toString());
                }
                try {
                    String email = object.getString("email");
                    String firstname = object.getString("first_name");
                    String lastname = object.getString("last_name");
                    HashMap hashMap = new HashMap();
                    hashMap.put("email", email);
                    hashMap.put("firstname", firstname);
                    hashMap.put("lastname", lastname);
                    Ced_ClientRequestResponseRest_New clientRequestResponse = new Ced_ClientRequestResponseRest_New(new AsyncResponse() {
                        @Override
                        public void processFinish(Object output) throws JSONException {
                            Jstring = output.toString();
                            if (functionalityList.getExtensionAddon()) {
                                checklogin();
                            } else {
                                Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                            }
                        }
                    }, Ced_MultiVendorBase_VendorLogin.this, "POST", hashMap.toString());
                    clientRequestResponse.execute(social_login_url);
//                    Ced_MultiVendor_ClientRequestResponse clientRequestResponse = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
//                        @Override
//                        public void processFinish(Object output) throws JSONException {
//                            Jstring = output.toString();
//                            if (functionalityList.getExtensionAddon()) {
//                                checklogin();
//                            } else {
//                                Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//                                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
//                            }
//                        }
//                    }, Ced_MultiVendorBase_VendorLogin.this, "POST", hashMap);
//                    clientRequestResponse.execute(social_login_url);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,email,last_name");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void getFbHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                    Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        try {
            if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                Log.d("google_info", "handleSignInResult:" + result.isSuccess());
            }
            if (result.isSuccess()) {
                // Signed in successfully, show authenticated UI.
                GoogleSignInAccount acct = result.getSignInAccount();
                String email = acct.getEmail();
                String id = acct.getId();
                String name = acct.getDisplayName();
                Uri image = acct.getPhotoUrl();
                if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                    Log.i("google_info:", " " + id);
                    Log.i("google_info:", " " + email);
                    Log.i("google_info:", " " + name);
                    Log.i("google_info:", " " + image);
                }
                updateUI(true);
                HashMap hashMap = new HashMap();
                hashMap.put("email", email);
                hashMap.put("firstname", name);
                JSONObject params = new JSONObject();
                params.put("email", email);
                params.put("firstname", name);
                params.put("lastname", name);
                params.put("token", acct.getId());
                params.put("type", "google");
                Ced_ClientRequestResponseRest_New clientRequestResponse = new Ced_ClientRequestResponseRest_New(new AsyncResponse() {
                    @Override
                    public void processFinish(Object output) throws JSONException {
                        Jstring = output.toString();
                        if (functionalityList.getExtensionAddon()) {
                            checklogin();
                        } else {
                            Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                        }
                    }
                }, Ced_MultiVendorBase_VendorLogin.this, "POST", params.toString());
                clientRequestResponse.execute(social_login_url);
//            Ced_MultiVendor_ClientRequestResponse clientRequestResponse = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
//                @Override
//                public void processFinish(Object output) throws JSONException {
//                    Jstring = output.toString();
//                    if (functionalityList.getExtensionAddon()) {
//                        checklogin();
//                    } else {
//                        Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                        overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
//                    }
//                }
//            }, Ced_MultiVendorBase_VendorLogin.this, "POST", hashMap);
//            clientRequestResponse.execute(social_login_url);
            } else {
                // Signed out, show unauthenticated UI.
                updateUI(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateUI(boolean signedIn) {
        if (signedIn) {

        } else {

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

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}