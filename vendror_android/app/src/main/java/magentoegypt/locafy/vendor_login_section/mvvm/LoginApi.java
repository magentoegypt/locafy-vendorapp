package magentoegypt.locafy.vendor_login_section.mvvm;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface LoginApi {
    @POST("rest/V1/mobiconnect/customer/validateNumber")
    Call<Object> validateNumber(@Body Map<String, Object> parameters);

    @POST("rest/V1/vsignupapi/validateNumber")
    Call<Object> validateSignUpNumber(@Body Map<String, Object> parameters);
    @POST("rest/V1/mobiconnect/customer/sendOtp")
    Call<Object> sendOTP(@Body Map<String, Object> parameters);

    @POST("rest/V1/mobiconnect/customer/verifyOtp")
    Call<Object> verifyOTP(@Body Map<String, Object> parameters);

    @GET("vendorapi/index/isRegistrationEnabled")
    Call<Object> getSetting();
    @GET("vendorapi/index/getmoduleList")
    Call<Object> get_enabled_addons();


}
