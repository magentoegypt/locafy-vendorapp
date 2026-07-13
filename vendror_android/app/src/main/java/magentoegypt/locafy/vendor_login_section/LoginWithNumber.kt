package magentoegypt.locafy.vendor_login_section

import android.app.Activity
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.gson.Gson
import magentoegypt.locafy.R
import magentoegypt.locafy.databinding.ActivityCedMultivendorNewLoginBinding
import magentoegypt.locafy.vendor_login_section.mvvm.LoginViewModel
import org.json.JSONArray
import org.json.JSONException
import java.util.*

abstract class LoginWithNumber(
    val activity: Activity,
    val loginBinding: ActivityCedMultivendorNewLoginBinding,
    private val lifeCycleOwner: Ced_Multivendor_New_Login,
    private val loginViewModel: LoginViewModel
) {
    @kotlin.jvm.JvmField
    var selectedPosition: Int = 0
    val TAG = "LoginWithNumber"
    private val COUNTRY_ID = "IN"
    private val TYPE_LOGIN = "login"
    private val SUBMIT_OTP = "Submit OTP"
    var number: String? = null
    var parameters: Map<String, Any>? = null
    var isOtpSent = false
    private var OTP = ""

    init {
        initVisibility()
        initTabChangeListener()

        initListener()
    }


    private fun initVisibility() {
        loginBinding.tab.visibility = View.VISIBLE
    }

    private fun initListener() {}


    private fun initTabChangeListener() {
        loginBinding.tab.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                selectedPosition = tab.position
                loginBinding.position = tab.position
                var btnMsg = ""
                btnMsg = if (selectedPosition == 0) {
                    //loginBinding.otpLay.visibility = View.GONE
                    activity.getString(R.string.Login)
                } else {
                    if (isOtpSent) {
                        //loginBinding.otpLay.visibility = View.VISIBLE
                        activity.getString(R.string.submit_otp)
                    } else {
                        //loginBinding.otpLay.visibility = View.GONE
                        activity.getString(R.string.Login)
                    }
                }
                loginBinding.login.text = btnMsg
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }


    open fun loginWithNumber() {
        val text: String = loginBinding.login.text.toString()
        Log.d(TAG, ": $text")
        if (text.equals("login", ignoreCase = true)) {
            validateNumber()
        } else if (text.equals(SUBMIT_OTP, ignoreCase = true)) {
            OTP = Objects.requireNonNull(loginBinding.edtUserOTP.text).toString()
            if (!TextUtils.isEmpty(OTP)) {
                verifyOTP()
            } else onError("enter OTP")
        }
    }

    private fun validateNumber() {
        number = Objects.requireNonNull(loginBinding.edtUserNumber.text).toString()
        if (TextUtils.isEmpty(number)) {
            onError(activity.getString(R.string.mobile_number_is_empty))
        } else {
            onError(activity.getString(R.string.sending_otp))
            parameters = HashMap()
            val validateMap: MutableMap<String, Any> = HashMap()
            validateMap["country_id"] = COUNTRY_ID
            validateMap["mobile"] = number!!
            validateMap["type"] = TYPE_LOGIN
            (parameters as HashMap<String, Any>)["parameters"] = validateMap
            loginViewModel.validateNumber(parameters)?.observe(lifeCycleOwner) { o ->
                Log.d(TAG, "validateNumber: " + validateResponse(o))
                if (validateResponse(o)) {
                    sendOTP(parameters as HashMap<String, Any>)
                }
            }
        }
    }

    private fun sendOTP(sendOTPMap: MutableMap<String, Any>) {
        sendOTPMap.remove("type")
        loginViewModel.sendOTP(sendOTPMap)?.observe(lifeCycleOwner) { o ->
            Log.d(TAG, "sendOTP: " + validateResponse(o))
            if (validateResponse(o)) {
                isOtpSent = true
                loginBinding.edtUserOTP.visibility = View.VISIBLE
                loginBinding.login.text = SUBMIT_OTP
            }
        }
    }

    open fun verifyOTP() {
        parameters = HashMap()
        val map: MutableMap<String, Any> = HashMap()
        map["otp"] = OTP
        map["country_id"] = COUNTRY_ID
        map["mobile"] = number!!
        (parameters as HashMap<String, Any>)["parameters"] = map
        loginViewModel.verifyOTP(parameters)?.observe(lifeCycleOwner) { o ->
            if (validateResponse(o)) {
                onOtpVerified(number)
            }
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

    abstract fun onError(msg: String?)
    protected abstract fun onOtpVerified(number: String?)
}