package magentoegypt.locafy.vendor_login_section.mvvm;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import magentoegypt.locafy.base_app.base.RequestWatcher;
import magentoegypt.locafy.base_app.base.RetrofitInstance;

import java.util.Map;

public class LoginRepo extends RequestWatcher {
    LoginApi apiInterface;

    public LoginRepo() {
        this.apiInterface = RetrofitInstance.getInstance().create(LoginApi.class);
    }


    public LiveData<Object> validateNumber(Map<String, Object> parameters) {
        MutableLiveData<Object> _validateNumberResponse = new MutableLiveData<>();
        enqueueApi(apiInterface.validateNumber(parameters), _validateNumberResponse);
        return _validateNumberResponse;

    }

    public LiveData<Object> validateSignUpNumber(Map<String, Object> parameters) {
        MutableLiveData<Object> _validateNumberResponse = new MutableLiveData<>();
        enqueueApi(apiInterface.validateSignUpNumber(parameters), _validateNumberResponse);
        return _validateNumberResponse;

    }

    public LiveData<Object> sendOTP(Map<String, Object> parameters) {
        MutableLiveData<Object> _sendOTPResponse = new MutableLiveData<>();
        enqueueApi(apiInterface.sendOTP(parameters), _sendOTPResponse);
        return _sendOTPResponse;
    }


    public LiveData<Object> verifyOTP(Map<String, Object> parameters) {
        MutableLiveData<Object> _verifyOTPResponse = new MutableLiveData<>();
        enqueueApi(apiInterface.verifyOTP(parameters), _verifyOTPResponse);
        return _verifyOTPResponse;
    }

    public LiveData<Object> getSetting() {
        MutableLiveData<Object> _verifyOTPResponse = new MutableLiveData<>();
        enqueueApi(apiInterface.getSetting(), _verifyOTPResponse);
        return _verifyOTPResponse;
    }

    public LiveData<Object> get_enabled_addons() {
        MutableLiveData<Object> _verifyOTPResponse = new MutableLiveData<>();
        enqueueApi(apiInterface.get_enabled_addons(), _verifyOTPResponse);
        return _verifyOTPResponse;
    }
}
