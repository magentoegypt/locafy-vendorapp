package magentoegypt.locafy.vendor_registration_section

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse
import magentoegypt.locafy.R
import magentoegypt.locafy.databinding.SignDialogNumberBinding
import magentoegypt.locafy.vendor_login_section.Ced_MultiVendor_ForgotPassWord
import magentoegypt.locafy.vendor_login_section.Ced_Multivendor_New_Login
import magentoegypt.locafy.vendor_login_section.mvvm.LoginViewModel
import magentoegypt.locafy.vendor_profile_section.Ced_MultiVendor_EditVendorProfileDynamic
import magentoegypt.locafy.vendor_registration_section.new_registration.RegistrationDynamic
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*


abstract class ValidateAndSendOtp(
    val activity: Activity,
    private val loginViewModel: LoginViewModel
) {


    private val isVerifyOtpEnable = true
    private val TAG = "ValidateAndSendOtp"
    lateinit var nestedView: SignDialogNumberBinding
    var parameters: Map<String, Any>? = null
    var number: String = ""
    var OTP: String = ""
    private val COUNTRY_ID = "IN"
    private val TYPE_LOGIN = "login"
    private var SUBMIT_OTP = "Submit OTP"
    var isOtpSent = false
    var d: Dialog? = null
    public var ced_MultiVendor_EditVendorProfileDynamic: Ced_MultiVendor_EditVendorProfileDynamic? = null;
    public var registrationDynamic: RegistrationDynamic? = null;
    public var ced_Multivendor_New_Login: Ced_Multivendor_New_Login? = null;
    public var ced_MultiVendor_ForgotPassWord: Ced_MultiVendor_ForgotPassWord? = null;
    abstract fun onError(msg: String?)
    protected abstract fun onOtpVerified(number: String?)

    fun showEnterNumberDialog(text: String,nameCode:String) {

        nestedView = SignDialogNumberBinding.inflate(activity.layoutInflater)
        val alert = AlertDialog.Builder(activity)
        SUBMIT_OTP = activity.getString(R.string.submit_otp);
        alert.setTitle(activity.getString(R.string.enter_your_mobile_number_and_verify))
        alert.setView(nestedView.root)
        d = alert.create()
        d?.show()
        nestedView.edtUserNumber.setText(text)
        if (!nestedView.edtUserNumber.text.toString().isEmpty()){
            nestedView.countryPicker.setCountryForNameCode(nameCode)
            number = nestedView.countryPicker.selectedCountryCodeWithPlus + nestedView.edtUserNumber.text.toString()
            if(ced_Multivendor_New_Login != null){
                validateNumber(ced_Multivendor_New_Login!!)
            }else if(ced_MultiVendor_ForgotPassWord != null){
                validateNumber(ced_MultiVendor_ForgotPassWord!!)
            }
        }
        nestedView.btnSubmit.setOnClickListener {
            if (!isVerifyOtpEnable) {
                var replaceNumber = number
                if(replaceNumber.contains("+")){
                    replaceNumber = replaceNumber.replace("+","")
                }
                onOtpVerified(replaceNumber)
            } else {
                val text: String = nestedView.btnSubmit.text.toString()
                if (text.equals(activity.getString(R.string.submit), ignoreCase = true)) {
                    number = nestedView.countryPicker.selectedCountryCodeWithPlus + nestedView.edtUserNumber.text.toString()
                    if(registrationDynamic != null){
                        validateNumber(registrationDynamic!!)
                    }else if(ced_Multivendor_New_Login != null){
                        validateNumber(ced_Multivendor_New_Login!!)
                    }else if(ced_MultiVendor_EditVendorProfileDynamic != null){
                        validateNumber(ced_MultiVendor_EditVendorProfileDynamic!!)
                    }
                  //  sendOtpVerified(number)
                } else if (text.equals(SUBMIT_OTP, ignoreCase = true)) {
                  //  OTP = Objects.requireNonNull(nestedView.edtUserOtp.text).toString()
                    if (!TextUtils.isEmpty(OTP)) {
                        if(OTP.equals(Objects.requireNonNull(nestedView.edtUserOtp.text).toString()))
                        {
                            (d as AlertDialog?)?.dismiss()
                            var replaceNumber = number
                            if(replaceNumber.contains("+")){
                                replaceNumber = replaceNumber.replace("+","")
                            }
                            onOtpVerified(replaceNumber)
                        }
                        else
                            onError(activity.getString(R.string.otp_is_wrong))
                    } else onError(activity.getString(R.string.enter_otp))
                }
            }

        }

        nestedView.btnResendotp.setOnClickListener {
            sendWhatsAppOTP(number)
        }
    }


    private fun validateResponse(o: Any): Boolean {
        return try {
            val array = JSONArray(Gson().toJson(o))
            if (array.length() > 0) {
                val `object` = array.getJSONObject(0).getJSONObject("data").getJSONArray("customer")
                    .getJSONObject(0)
                if (`object`.has("status")) {
                    if (`object`.getBoolean("status")) onError(`object`.getString("message"))
                    `object`.getString("status").equals("true", ignoreCase = true)
                } else {
                    onError("no status Key found !!")
                    false
                }
            } else {
                onError("no data found !!")
                false
            }
        } catch (e: JSONException) {
            e.printStackTrace()
            false
        }
    }

    private fun validateNumber(lifeCycleOwner: LifecycleOwner) {
        number = nestedView.countryPicker.selectedCountryCodeWithPlus + Objects.requireNonNull(nestedView.edtUserNumber.text).toString()
        if (TextUtils.isEmpty(number)) {
            onError(activity.getString(R.string.mobile_number_is_empty))
        } else {
            onError(activity.getString(R.string.sending_otp))
            parameters = HashMap()
            val validateMap: MutableMap<String, Any> = HashMap()
            validateMap["country_id"] = COUNTRY_ID
            validateMap["mobile"] = number
           // validateMap["type"] = TYPE_LOGIN
            (parameters as HashMap<String, Any>)["parameters"] = validateMap
            sendWhatsAppOTP(number)
            isOtpSent = true
            nestedView.edtUserOtp.visibility = View.VISIBLE
            nestedView.btnResendotp.visibility = View.VISIBLE
            val param = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT,
                            1.0f)
            nestedView.btnSubmit.setLayoutParams(param)
            nestedView.btnSubmit.text = SUBMIT_OTP
//            loginViewModel.validateSignUpNumber(parameters)?.observe(lifeCycleOwner) { o ->
//                val array = JSONArray(Gson().toJson(o))
//                if (array.length() > 0) {
//                    val  response = array.getJSONObject(0)
//                    if(response.getBoolean("status")){
//                        sendWhatsAppOTP(number)
//                        isOtpSent = true
//                        nestedView.edtUserOtp.visibility = View.VISIBLE
//                        nestedView.btnResendotp.visibility = View.VISIBLE
//                        val param = LinearLayout.LayoutParams(
//                            0,
//                            LinearLayout.LayoutParams.MATCH_PARENT,
//                            1.0f
//                        )
//                        nestedView.btnSubmit.setLayoutParams(param)
//                        nestedView.btnSubmit.text = SUBMIT_OTP
//                    }
//                    Toast.makeText(activity,response.getString("message") , Toast.LENGTH_SHORT).show()
//                }else{
//                    Toast.makeText(activity,"no data found !!" , Toast.LENGTH_SHORT).show()
//                }
//
//            }
        }
    }

    @Throws(JSONException::class)
     open fun validateNumber(number: String) {
        val vendorSessionManagement = Ced_MultiVendor_VendorSessionManagement(activity)
        val postData = HashMap<String, String>()
        postData["country_id"] = COUNTRY_ID
        postData["mobile"] = number
        val crr = Ced_MultiVendor_ClientRequestResponse({ output ->
            Log.i(TAG, "processFinish: $output")
            val outputString = output.toString()
            val mainobject: JSONObject = JSONObject(outputString)
        }, activity, "POST", postData)
        crr.execute(vendorSessionManagement.getBase_Url() + "rest/V1/vsignupapi/validateNumber")
    }

    @Throws(JSONException::class)
    open fun sendWhatsAppOTP(number: String) {
        val postData = HashMap<String, String>()
        postData["number"] = number
        val rand = Random()
        OTP = rand.nextInt(10000).toString()
        postData["otp"] = OTP
        Log.i(TAG, "OTP: $OTP")
        val crr = Ced_MultiVendor_ClientRequestResponse({ output ->
            Log.i(TAG, "processFinish: $output")
            val outputString = output.toString()
            val mainobject: JSONObject = JSONObject(outputString)
            if (mainobject.has("error")){
                val meta: JSONObject = mainobject.getJSONObject("error");
                val  message = meta.getString("message")
                Toast.makeText(activity,message, Toast.LENGTH_SHORT).show()
//                if (!status){
//                    if(meta.has("developer_message")){
//                        Toast.makeText(activity,meta.getString("developer_message") , Toast.LENGTH_SHORT).show()
//                    }else{
//                        Toast.makeText(activity,"Something went wrong." , Toast.LENGTH_SHORT).show()
//                    }
//                }
            }

        }, activity, "POST", postData)
        crr.execute("https://graph.facebook.com/v20.0/122095038332010689/messages")
    }

}