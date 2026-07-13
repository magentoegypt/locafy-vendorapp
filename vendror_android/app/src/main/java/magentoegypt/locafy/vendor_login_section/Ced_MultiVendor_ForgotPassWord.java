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

package magentoegypt.locafy.vendor_login_section;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import magentoegypt.locafy.api_request_response_section.Ced_NewLoader;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorSplash;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_UnAuthourizedRequestError;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.vendor_login_section.mvvm.LoginViewModel;
import magentoegypt.locafy.vendor_registration_section.ValidateAndSendOtp;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;
import magentoegypt.locafy.R;
import magentoegypt.locafy_constant.AppConstant;
import com.hbb20.CountryCodePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Ced_MultiVendor_ForgotPassWord extends AppCompatActivity {
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    Ced_MultiVendor_VendorSessionManagement session;
    EditText email;
    String forgotpassurl = "";
    HashMap<String, String> dataforfogotpass;
    String Jstring;
    Button submit;

    ValidateAndSendOtp loginWithNumber;
    Ced_NewLoader cedNewLoader;
    CountryCodePicker codePicker;

    boolean isMobile = false;

    private static boolean isValidEmail(String target) {
        boolean valid = false;
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (target.matches(emailPattern)) {
            valid = true;
        }
        return valid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        session = new Ced_MultiVendor_VendorSessionManagement(getApplicationContext());
        connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(getApplicationContext());
        forgotpassurl = session.getBase_Url() + "vendorapi/index/forgotPassword";

        if (connectionDetector.isConnectingToInternet()) {
            setContentView(R.layout.ced_multivendor_activity_forgot_pass_word);
            setupLoginWithNumber();
            codePicker = findViewById(R.id.countryPicker);
            email = findViewById(R.id.MultiVendor_forgotemail);
            submit = findViewById(R.id.MultiVendor_submit);
            RadioGroup rg = (RadioGroup) findViewById(R.id.radiosForgot);

            rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
            {
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if(checkedId == R.id.forgot_with_mobile){
                        email.setHint(R.string.mobile_number);
                        email.setInputType(InputType.TYPE_CLASS_PHONE);
                        email.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                        codePicker.setVisibility(View.VISIBLE);
                        isMobile = true;
                    }else if(checkedId == R.id.forgot_with_email){
                        email.setHint(R.string.emailhint);
                        email.setInputType(InputType.TYPE_CLASS_TEXT);
                        email.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        codePicker.setVisibility(View.GONE);
                        isMobile = false;
                    }
                }
            });
            email.setHint(R.string.emailhint);
            dataforfogotpass = new HashMap<String, String>();
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        if (isMobile) {
                            if (email.getText().toString().isEmpty()) {
                                email.setError(getString(R.string.mobile_number_validation));
                            }else if (email.getText().toString().charAt(0) == '0') {
                                email.setError(getString(R.string.please_enter_mobile_number));
                            }else if (email.getText().toString().length() != 10) {
                                email.setError(getString(R.string.please_enter_the_valid_number));
                            }else{
                                loginWithNumber.showEnterNumberDialog(email.getText().toString(),codePicker.getSelectedCountryNameCode());
                            }
                        } else {
                            AppConstant.lockButton(v);
                            dataforfogotpass.put("email",email.getText().toString());
                            forgotWithEmail();
                        }
                    } catch (Exception e) {
                        Intent main = new Intent(Ced_MultiVendor_ForgotPassWord.this, Ced_MultiVendor_VendorSplash.class);
                        main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(main);
                        overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
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

    void forgotWithEmail(){
        if (email.getText().toString().isEmpty()) {
            email.setError(getResources().getString(R.string.pleaseentervalidemail));
        } else {

            Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
                @Override
                public void processFinish(Object output) throws JSONException {
                    Jstring = output.toString();

                    if (functionalityList.getExtensionAddon()) {
                        JSONObject forgotpass_object = new JSONObject(Jstring);
                        JSONArray forgotpass_array = forgotpass_object.getJSONObject("data").getJSONArray("customer");
                        String success = forgotpass_array.getJSONObject(0).getString("success");
                        if (success.equals("true")) {
                            Toast.makeText(Ced_MultiVendor_ForgotPassWord.this, forgotpass_array.getJSONObject(0).getString("message"), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), Ced_Multivendor_New_Login.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                        } else {
                            if (success.equals("false")) {
                                Toast.makeText(Ced_MultiVendor_ForgotPassWord.this, forgotpass_array.getJSONObject(0).getString("message"), Toast.LENGTH_LONG).show();

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
            }, Ced_MultiVendor_ForgotPassWord.this, "POST", dataforfogotpass);
            crr.execute(forgotpassurl);


        }
    }

    void forgotWithMobile(String mobile){
        long unixTime = System.currentTimeMillis() / 1000L;
            Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
                @Override
                public void processFinish(Object output) throws JSONException {
                    Jstring = output.toString();

                    if (functionalityList.getExtensionAddon()) {
                        JSONObject forgotpass_object = new JSONObject(Jstring);
                        JSONArray forgotpass_array = forgotpass_object.getJSONObject("data").getJSONArray("customer");
                        String success = forgotpass_array.getJSONObject(0).getString("success");
                        if (success.equals("true")) {
                            String reset_url = forgotpass_array.getJSONObject(0).getString("reset_url");
                         //   Toast.makeText(Ced_MultiVendor_ForgotPassWord.this, forgotpass_array.getJSONObject(0).getString("message"), Toast.LENGTH_LONG).show();
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(reset_url));
                            startActivity(browserIntent);
//                            Intent intent = new Intent(getApplicationContext(), Ced_Multivendor_New_Login.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//                            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                        } else {
                            if (success.equals("false")) {
                                Toast.makeText(Ced_MultiVendor_ForgotPassWord.this, forgotpass_array.getJSONObject(0).getString("message"), Toast.LENGTH_LONG).show();

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
            }, Ced_MultiVendor_ForgotPassWord.this, "GET", dataforfogotpass);
            crr.execute(session.getBase_Url() + "wapplogin/index/forgotPassword?mobile="+mobile+"&_t="+unixTime);
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

    private void setupLoginWithNumber() {
        LoginViewModel loginViewModel = ViewModelProviders.of(Ced_MultiVendor_ForgotPassWord.this).get((LoginViewModel.class));
        cedNewLoader = new Ced_NewLoader(Ced_MultiVendor_ForgotPassWord.this);

        loginWithNumber = new ValidateAndSendOtp(this,  loginViewModel) {
            @Override
            public void onError(@Nullable String msg) {
                Toast.makeText(Ced_MultiVendor_ForgotPassWord.this, msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void onOtpVerified(@Nullable String number) {
                try {
                    forgotWithMobile(number);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        loginWithNumber.setCed_MultiVendor_ForgotPassWord(Ced_MultiVendor_ForgotPassWord.this);

        loginViewModel.error.observe(Ced_MultiVendor_ForgotPassWord.this, s -> Toast.makeText(Ced_MultiVendor_ForgotPassWord.this, s, Toast.LENGTH_SHORT).show());

        loginViewModel.loading.observe(Ced_MultiVendor_ForgotPassWord.this, this::updateLoader);
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

}
