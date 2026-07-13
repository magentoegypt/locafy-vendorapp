package magentoegypt.locafy.vendor_login_section;

import static android.Manifest.permission.READ_MEDIA_IMAGES;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorSplash;
import magentoegypt.locafy.base_app.UtilityMethods;
import magentoegypt.locafy.manage_products_section.ProductCreationNew;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.vendor_registration_section.ValidateAndSendOtp;
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
import com.hbb20.CountryCodePicker;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import magentoegypt.locafy.R;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_ClientRequestResponseRest_New;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.api_request_response_section.Ced_NewLoader;
import magentoegypt.locafy.api_request_response_section.RestNotificatioRequest;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_UnAuthourizedRequestError;
import magentoegypt.locafy_constant.AppConstant;
import magentoegypt.locafy_constant.Ced_MultiVendor_GlobalVariables;
import magentoegypt.locafy.base_app.Ced_Load_Language;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.databinding.ActivityCedMultivendorNewLoginBinding;
import magentoegypt.locafy.vendor_dashboard.Ced_MultiVendor_VendorDashboard;
import magentoegypt.locafy.vendor_login_section.mvvm.LoginViewModel;
import magentoegypt.locafy.vendor_registration_section.customer_as_vendor_registration.Ced_MultiVendor_CustomerAsVendorRegistration;
import magentoegypt.locafy.vendor_registration_section.new_registration.RegistrationDynamic;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;

public class Ced_Multivendor_New_Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final int RC_SIGN_IN = 0;
    private static final String TAG = "Ced_Multivendor_New_Log";
    EditText vendor_name;
    EditText vendor_password;
    TextView login;
    String CurrrentUrl = "";
    String social_login_url;
    String Jstring = "";
    HashMap dataforlogin;
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    Ced_MultiVendor_VendorSessionManagement vendorSessionManagement;
    Boolean showPass = false;
    ImageView maskpass;
    TextView signup;
    String url = "";
    CallbackManager callbackManager;
    LoginButton facebook_login_button;
    boolean flag = true;
    LinearLayout social_login_linear;
    private GoogleApiClient mGoogleApiClient;
    private LinearLayout social_login_google;
    Ced_NewLoader cedNewLoader;

    CountryCodePicker codePicker;

    ValidateAndSendOtp loginWithNumber;
    ActivityCedMultivendorNewLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);*/
        FacebookSdk.setAutoInitEnabled(true);
        FacebookSdk.fullyInitialize();
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_ced__multivendor__new__login);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_ced__multivendor__new__login);

        givePermission();

        vendor_name = findViewById(R.id.email);
        vendor_password = findViewById(R.id.user_password);
        Ced_MultiVendor_NavigationActivity.photoUrl = null;
      //  vendor_name.setText("sajidnawaz992@gmail.com");
      // vendor_password.setText("12345678");
        login = findViewById(R.id.login);
        codePicker = findViewById(R.id.countryPicker);
        codePicker.setCountryForNameCode("eg");
        codePicker.setClickable(false);
        codePicker.setEnabled(false);
        dataforlogin = new HashMap<String, String>();
        connectionDetector = new Ced_MultiVendor_ConnectionDetector(this);
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(this);
        vendorSessionManagement = new Ced_MultiVendor_VendorSessionManagement(this);
        maskpass = findViewById(R.id.maskpass);
        signup = findViewById(R.id.signup);
        if (vendorSessionManagement.getStoreLocale() != null) {
            new Ced_Load_Language().setLanguagetoLoad(vendorSessionManagement.getStoreLocale(), this);
        }
        RadioGroup rg = (RadioGroup) findViewById(R.id.radiosLogin);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.login_with_otp){
                    vendor_password.setVisibility(View.GONE);
                    codePicker.setVisibility(View.VISIBLE);
                    vendor_name.setHint(R.string.mobile_number);
                    vendor_name.setInputType(InputType.TYPE_CLASS_PHONE);
                    vendor_name.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                    vendor_name.setText("");
                }else if(checkedId == R.id.login_with_password){
                    vendor_password.setVisibility(View.VISIBLE);
                    codePicker.setVisibility(View.GONE);
                    vendor_name.setHint(R.string.emailhint);
                    vendor_name.setInputType(InputType.TYPE_CLASS_TEXT);
                    vendor_name.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    vendor_name.setText("");
                }
            }
        });
        if (connectionDetector.isConnectingToInternet()) {
//            social_login_url = vendorSessionManagement.getBase_Url() + "vsocialloginapi/customer/register";
            social_login_url = vendorSessionManagement.getBase_Url() + "rest/V1/vsocialloginapi/create";
            CurrrentUrl = vendorSessionManagement.getBase_Url() + "vendorapi/index/login";
            url = vendorSessionManagement.getBase_Url() + "vendorapi/index/vendordevice";
            social_login_google = findViewById(R.id.MageNative_social_login_google);

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
            social_login_google.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppConstant.lockButton(v);
                    signInFromGoogle();
                }
            });

            signInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppConstant.lockButton(v);
                    signInFromGoogle();
                }
            });
            /*****************************************************/
            /*****************************************************/
            /*Facebook Login Setup*/

            setupLoginWithNumber();

            social_login_linear = findViewById(R.id.MageNative_social_login_linear);
            facebook_login_button = findViewById(R.id.MultiVendor_facebook_login_button);
            facebook_login_button.setReadPermissions("email");
            /*facebook_login_button.setReadPermissions("public_profile");*/
            facebook_login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    AccessToken accessToken = loginResult.getAccessToken();
                    Profile profile = Profile.getCurrentProfile();
                    if (AccessToken.getCurrentAccessToken().getToken() != null) {
                        checkFacebookLogin();
                    }
                }

                @Override
                public void onCancel() {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.loginattemptcancled), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onError(FacebookException exception) {
                    Toast.makeText(getApplicationContext(), "" + exception, Toast.LENGTH_LONG).show();
                }
            });
            /***************************************************************/

            social_login_linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    facebook_login_button.performClick();
                }
            });

            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            signup.setOnClickListener(view -> {
                AppConstant.lockButton(view);
                Intent createaccount = new Intent(Ced_Multivendor_New_Login.this, RegistrationDynamic.class);
              /*  Intent createaccount = new Intent(Ced_Multivendor_New_Login.this, Ced_MultiVendor_VendorRegistration.class);
                createaccount.putExtra("email", vendor_email.getText().toString());
                createaccount.putExtra("number", MultiVendor_vendor_phone.getText().toString());
                createaccount.putExtra("country_id", country_id);
                Log.d("vaibhav", "" + vendor_email.getText().toString() + "->" + MultiVendor_vendor_phone.getText().toString());*/
                startActivity(createaccount);
                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
            });

            vendor_password.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!s.toString().isEmpty()) {
                        maskpass.setVisibility(View.VISIBLE);
                    } else {
                        maskpass.setVisibility(View.GONE);
                    }

                }
            });

            maskpass.setOnClickListener(new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View view) {
                    if (showPass) {
                        maskpass.setImageDrawable(getDrawable(R.drawable.eye));
                        vendor_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        showPass = false;
                    } else {
                        maskpass.setImageDrawable(getDrawable(R.drawable.hide_eye));
                        vendor_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        showPass = true;
                    }
                    vendor_password.setSelection(vendor_password.getText().length());
                }
            });

            login.setOnClickListener(v -> {
                AppConstant.lockButton(v);

                try {
                    if (vendor_password.getVisibility() == View.GONE) {
                        if (vendor_name.getText().toString().isEmpty()) {
                            vendor_name.setError(getString(R.string.mobile_number_validation));
                        }else if (vendor_name.getText().toString().charAt(0) == '0') {
                            vendor_name.setError(getString(R.string.please_enter_mobile_number));
                        }else if (vendor_name.getText().toString().length() != 10) {
                            vendor_name.setError(getString(R.string.please_enter_the_valid_number));
                        }else{
                            loginWithNumber.showEnterNumberDialog(vendor_name.getText().toString(),codePicker.getSelectedCountryNameCode());
                        }
                    } else loginWithEmail();
                } catch (Exception e) {
                    e.printStackTrace();
                    showMsg("something went wrong !! \n" + e.getLocalizedMessage());
                }


            });
        } else {
            Intent nointernet = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(nointernet);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }
        vendor_password.setVisibility(View.GONE);
        codePicker.setVisibility(View.VISIBLE);
        vendor_name.setHint(R.string.mobile_number);
        vendor_name.setInputType(InputType.TYPE_CLASS_PHONE);
        vendor_name.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        vendor_name.setText("");
    }

    private void loginMobile(String mobile) {
        Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(output -> {
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
        }, Ced_Multivendor_New_Login.this, "GET", dataforlogin);
        crr.execute(vendorSessionManagement.getBase_Url() + "wapplogin/index/login?mobile="+mobile);
    }

    private void loginWithEmail() {
        if (vendor_name.getText().toString().isEmpty()) {
            vendor_name.setError(getString(R.string.emaillogin_validation));
        } else {
            if (vendor_password.getText().toString().isEmpty()) {
                vendor_password.setError(getResources().getString(R.string.pleaseenterpassword));
            } else {
                dataforlogin.put("email", vendor_name.getText().toString());
                dataforlogin.put("password", vendor_password.getText().toString());
                Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(output -> {
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
                }, Ced_Multivendor_New_Login.this, "POST", dataforlogin);
                crr.execute(CurrrentUrl);
            }
        }
    }

    private void signInFromGoogle() {

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void setupLoginWithNumber() {
        LoginViewModel loginViewModel = ViewModelProviders.of(Ced_Multivendor_New_Login.this).get((LoginViewModel.class));
        cedNewLoader = new Ced_NewLoader(Ced_Multivendor_New_Login.this);

        loginWithNumber = new ValidateAndSendOtp(this,  loginViewModel) {
            @Override
            public void onError(@Nullable String msg) {
                Toast.makeText(Ced_Multivendor_New_Login.this, msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void onOtpVerified(@Nullable String number) {
                try {
                    loginMobile(number);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        loginWithNumber.setCed_Multivendor_New_Login(Ced_Multivendor_New_Login.this);

        loginViewModel.error.observe(Ced_Multivendor_New_Login.this, s -> Toast.makeText(Ced_Multivendor_New_Login.this, s, Toast.LENGTH_SHORT).show());

        loginViewModel.loading.observe(Ced_Multivendor_New_Login.this, this::updateLoader);
    }

    private void updateLoader(Boolean isLoading) {
        if (isLoading) {
            if (null != cedNewLoader)
                cedNewLoader.show();
        } else {
            if ((cedNewLoader != null) && cedNewLoader.isShowing()) {
                cedNewLoader.dismiss();
            }
        }
    }

    private void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

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
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("email", email);
                    hashMap.put("firstname", firstname);
                    hashMap.put("lastname", lastname);
                    if (object.has("id")) {
                        Log.e("REpo", "fb unique customer account id == " + object.getString("id"));
                        hashMap.put("token", object.getString("id"));
                    }
                    hashMap.put("type", "facebook");

                    Ced_ClientRequestResponseRest_New clientRequestResponse = new Ced_ClientRequestResponseRest_New(new AsyncResponse() {
                        @Override
                        public void processFinish(Object output) throws JSONException {
                            Jstring = output.toString();
                            if (functionalityList.getExtensionAddon()) {
                                checkSocialLogin();
                            } else {
                                Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                            }
                        }
                    }, Ced_Multivendor_New_Login.this, "POST", hashMap.toString());
                    clientRequestResponse.execute(social_login_url);

//                    Ced_MultiVendor_ClientRequestResponse clientRequestResponse = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
//                        @Override
//                        public void processFinish(Object output) throws JSONException {
//                            Jstring = output.toString();
//                            if (functionalityList.getExtensionAddon()) {
//                                checkSocialLogin();
//                            } else {
//                                Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//                                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
//                            }
//                        }
//                    }, Ced_Multivendor_New_Login.this, "POST", hashMap);
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
                Log.d("google_info", "handleSignInResult:" + result.getStatus());
                Log.d("google_info", "handleSignInResult:" + result);
            }
            Log.d("google_info", "handleSignInResult:" + result.isSuccess());
            Log.d("google_info", "handleSignInResult:" + result.getStatus());
            Log.d("google_info", "handleSignInResult:" + result);
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

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("email", email);
                hashMap.put("firstname", name);
                hashMap.put("lastname", name);
                hashMap.put("token", acct.getId());
                hashMap.put("type", "google");

                JSONObject params = new JSONObject();
                params.put("email", email);
                params.put("firstname", name);
                params.put("lastname", name);
                params.put("token", acct.getId());
                params.put("type", "google");
                Ced_ClientRequestResponseRest_New clientRequestResponse = new Ced_ClientRequestResponseRest_New(output -> {
                    Jstring = output.toString();
                    if (functionalityList.getExtensionAddon()) {
                        checkSocialLogin();
                    } else {
                        Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                    }
                }, Ced_Multivendor_New_Login.this, "POST", params.toString());
                clientRequestResponse.execute(social_login_url);
//            Ced_MultiVendor_ClientRequestResponse clientRequestResponse = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
//                @Override
//                public void processFinish(Object output) throws JSONException {
//                    Jstring = output.toString();
//                    if (functionalityList.getExtensionAddon()) {
//                        checkSocialLogin();
//                    } else {
//                        Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                        overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
//                    }
//                }
//            }, Ced_Multivendor_New_Login.this, "POST", hashMap);
//            clientRequestResponse.execute(social_login_url);
            } else {
                // Signed out, show unauthenticated UI.

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void givePermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            Dexter.withActivity(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CAMERA).withListener(new MultiplePermissionsListener() {
                @Override
                public void onPermissionsChecked(MultiplePermissionsReport report) {
                    if (report.areAllPermissionsGranted()) {
                        Toast.makeText(Ced_Multivendor_New_Login.this, R.string.permission_granted, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                    token.continuePermissionRequest();
                }
            }).onSameThread().check();
        }else{
            Dexter.withActivity(this).withPermissions( Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.READ_MEDIA_AUDIO,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CAMERA).withListener(new MultiplePermissionsListener() {
                @Override
                public void onPermissionsChecked(MultiplePermissionsReport report) {
                    if (report.areAllPermissionsGranted()) {
                        Toast.makeText(Ced_Multivendor_New_Login.this, R.string.permission_granted, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                    token.continuePermissionRequest();
                }
            }).onSameThread().check();
        }

    }

    private void checkSocialLogin() throws JSONException {
        JSONObject login_object = new JSONObject(Jstring);
        JSONObject vendor_data = login_object.getJSONObject("vendor_data");
        JSONObject vendor_info = vendor_data.getJSONObject("vendor_info");
        boolean success = vendor_data.getBoolean("success");
        if (success) {
            String[] array = vendor_info.getString("vendor_name").split(" ");
            vendorSessionManagement.saveCustomerId(vendor_info.getString("customer_id"));
            vendorSessionManagement.createLoginSession(vendor_info.getString("hashkey"), vendor_name.getText().toString());

//            JSONArray vendor_link = vendor_info.getJSONArray("vendor_link");

            if (vendor_info.has("vendor_navigation")) {
                JSONArray vendor_navigation = vendor_info.getJSONArray("vendor_navigation");
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
                      //  functionalityList.Shipping_Settings(check_is_in_array("Shipping Settings", vendor_navigation));
                      //  functionalityList.Shipping_Methods(check_is_in_array("Shipping Methods", vendor_navigation));
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

            if (vendor_info.has("vendor_id")) {
                if (vendor_info.getString("vendor_id").equalsIgnoreCase("null")) {
                    if (vendor_info.has("is_vendor")) {
                        Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_CustomerAsVendorRegistration.class);
                        intent.putExtra("customer_id", vendor_info.getString("is_vendor"));
                        intent.putExtra("email", vendor_name.getText().toString());
                        startActivity(intent);
                        overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                        Toast.makeText(Ced_Multivendor_New_Login.this, vendor_data.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.i("logout", "vendor id ");
                    vendorSessionManagement.saveVendorId(vendor_info.getString("vendor_id"));
                    sendRegistrationToServer(vendorSessionManagement.getDeviceToken(), vendor_info.getString("vendor_id"));
                    functionalityList.AuctionApi(true);
                    functionalityList.RequestForQuote(true);
                    functionalityList.RMA(true);
                    functionalityList.POApi(true);
                    functionalityList.Messaging(true);
                    functionalityList.Vendor_Deals(true);
                    functionalityList.StorePickup(true);
                    functionalityList.AdvanceReport(true);
                    functionalityList.AssoiciatedSubVendors(true);
                    vendorSessionManagement.savevendorname(array[0]);
                    vendorSessionManagement.savevendorpic(vendor_info.getString("profile_picture"));
                    Ced_MultiVendor_GlobalVariables.progress = Integer.parseInt(vendor_info.getString("profile_complete"));
                    if (!vendor_info.getString("valerts").equalsIgnoreCase("null"))
                        Ced_MultiVendor_GlobalVariables.noti = vendor_info.getString("valerts");
                    Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_VendorDashboard.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("name", array[0]);
                    intent.putExtra("picurl", vendor_info.getString("profile_picture"));
                    intent.putExtra("vendor_id", vendor_info.getString("vendor_id"));
                    startActivity(intent);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }
            } else {
                if (vendor_info.has("is_vendor")) {
                    Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_CustomerAsVendorRegistration.class);
                    intent.putExtra("customer_id", vendor_info.getString("is_vendor"));
                    intent.putExtra("email", vendor_name.getText().toString());
                    startActivity(intent);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                    Toast.makeText(Ced_Multivendor_New_Login.this, vendor_data.getString("message"), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            Toast.makeText(Ced_Multivendor_New_Login.this, vendor_data.getString("message"), Toast.LENGTH_LONG).show();
        }
    }

    private void checklogin() throws JSONException {
        JSONObject login_object = new JSONObject(Jstring);
        JSONArray login_array = login_object.getJSONObject("data").getJSONArray("customer");
        boolean success = login_array.getJSONObject(0).getBoolean("success");
        if (success) {

            String multistep_done = "";
            if (login_array.getJSONObject(0).has("multistep_done")) {
                multistep_done = login_array.getJSONObject(0).getString("multistep_done");
            }
            JSONArray vendor_navigation = login_array.getJSONObject(0).getJSONArray("vendor_link");
            if (multistep_done.equalsIgnoreCase("0")) {
                Intent intent = new Intent(getApplicationContext(), RegistrationDynamic.class);
                intent.putExtra("vendor_id", login_array.getJSONObject(0).getString("vendor_id"));
                intent.putExtra("email", vendor_name.getText().toString());
                intent.putExtra("hashkey", login_array.getJSONObject(0).getString("hashkey"));
                intent.putExtra("Jstring", Jstring);
                startActivity(intent);
                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
            }else if (login_array.getJSONObject(0).has("vendor_id")) {
                String[] array = login_array.getJSONObject(0).getString("vendor_name").split(" ");
                vendorSessionManagement.saveCustomerId(login_array.getJSONObject(0).getString("customer_id"));
                vendorSessionManagement.createLoginSession(login_array.getJSONObject(0).getString("hashkey"), vendor_name.getText().toString());
                Log.i("logout", "vendor id ");
                vendorSessionManagement.saveVendorId(login_array.getJSONObject(0).getString("vendor_id"));
                sendRegistrationToServer(vendorSessionManagement.getDeviceToken(), login_array.getJSONObject(0).getString("vendor_id"));
//                functionalityList.AuctionApi(true);
//                functionalityList.RequestForQuote(true);
//                functionalityList.RMA(true);
//                functionalityList.POApi(true);
//                functionalityList.Messaging(true);
//                functionalityList.Vendor_Deals(true);
//                functionalityList.StorePickup(true);
//                functionalityList.AdvanceReport(true);
//                functionalityList.AssoiciatedSubVendors(true);
                if (login_array.getJSONObject(0).has("sub_vendor_id")) {
                    Log.i("logout", "sub vendor id ");
                    vendorSessionManagement.saveSubVendorId(login_array.getJSONObject(0).getString("sub_vendor_id"));
//                    functionalityList.AuctionApi(false);
//                    functionalityList.RequestForQuote(false);
//                    functionalityList.RMA(false);
//                    functionalityList.POApi(false);
//                    functionalityList.Messaging(false);
//                    functionalityList.Vendor_Deals(false);
//                    functionalityList.StorePickup(false);
//                    functionalityList.AdvanceReport(false);
//                    functionalityList.AssoiciatedSubVendors(false);
                    vendorSessionManagement.savevendorname(array[0]);
                    vendorSessionManagement.savevendorpic(login_array.getJSONObject(0).getString("profile_picture"));
                }
                vendorSessionManagement.savevendorname(array[0]);
                vendorSessionManagement.savevendorpic(login_array.getJSONObject(0).getString("profile_picture"));
                Ced_MultiVendor_GlobalVariables.progress = Integer.parseInt(login_array.getJSONObject(0).getString("profile_complete"));
                Ced_MultiVendor_GlobalVariables.noti = login_array.getJSONObject(0).getString("valerts");
                Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_VendorDashboard.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("name", array[0]);
                intent.putExtra("picurl", login_array.getJSONObject(0).getString("profile_picture"));
                intent.putExtra("vendor_id", login_array.getJSONObject(0).getString("vendor_id"));
                startActivity(intent);
                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
            }
        } else {
            if (login_array.getJSONObject(0).has("is_vendor")) {
                Intent intent = new Intent(getApplicationContext(), RegistrationDynamic.class);
                intent.putExtra("customer_id", login_array.getJSONObject(0).getString("is_vendor"));
                intent.putExtra("email", vendor_name.getText().toString());
                startActivity(intent);
                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
            }
            String email = "v-relations@locafy.market";
            String new_email = "v\u2060-\u2060relations@\u2060locafy.\u2060market";
            String message = login_array.getJSONObject(0).getString("message");
            try {
                message = message.replace(email,new_email);
            }catch (Exception e){}
            UtilityMethods.showDialogOk(this,message, new UtilityMethods.OnSelectedListener() {
                @Override
                public void onSelected(String value) {

                }
            });
           // Toast.makeText(Ced_Multivendor_New_Login.this, login_array.getJSONObject(0).getString("message"), Toast.LENGTH_LONG).show();
        }
    }

    public boolean check_is_in_array(String check_to, JSONArray tab_array) throws JSONException {
        boolean flag = false;
        for (int i = 0; i < tab_array.length(); i++) {
            if (tab_array.getString(i).equals(check_to)) {
                flag = true;
            }
        }
        return flag;
    }

    private void sendRegistrationToServer(String token, String vendor_id) {
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(getApplicationContext());
        vendorSessionManagement = new Ced_MultiVendor_VendorSessionManagement(getApplicationContext());
        vendor_id = vendorSessionManagement.getVendorid();
        url = vendorSessionManagement.getBase_Url() + "vendorapi/index/vendordevice";
        HashMap<String, String> jsonObject = new HashMap<>();

        jsonObject.put("Token", token);
        jsonObject.put("vendor_id", vendor_id);
        jsonObject.put("type", "2");

        RestNotificatioRequest request = new RestNotificatioRequest(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                Log.d(TAG, "processFinish: " + output);
            }
        }, getApplicationContext(), "POST", jsonObject);
        request.execute(url);

    }

    public void TermsCondition(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(vendorSessionManagement.getBase_Url() + "seller-terms-condition/"));
        startActivity(browserIntent);
    }

    public void ForgotPassword(View view) {
        Intent createaccount = new Intent(Ced_Multivendor_New_Login.this, Ced_MultiVendor_ForgotPassWord.class);
        startActivity(createaccount);
        overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
