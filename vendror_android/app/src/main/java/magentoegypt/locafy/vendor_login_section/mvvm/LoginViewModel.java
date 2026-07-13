package magentoegypt.locafy.vendor_login_section.mvvm;

import androidx.lifecycle.LiveData;

import magentoegypt.locafy.base_app.base.BaseViewModel;

import java.util.Map;

public class LoginViewModel extends BaseViewModel<LoginRepo> {
    LoginRepo repo;

    public LoginViewModel() {
        repo = new LoginRepo();
        setRepo(repo);
    }

    public LiveData<Object> validateNumber(Map<String, Object> parameters) {
        return repo.validateNumber(parameters);
    }

    public LiveData<Object> validateSignUpNumber(Map<String, Object> parameters) {
        return repo.validateSignUpNumber(parameters);
    }

    public LiveData<Object> sendOTP(Map<String, Object> sendOTPMap) {
        return repo.sendOTP(sendOTPMap);
    }

    public LiveData<Object> verifyOTP(Map<String, Object> parameters) {
        return repo.verifyOTP(parameters);
    }

    public LiveData<Object> getSetting() {
        return repo.getSetting();
    }

    public LiveData<Object> get_enabled_addons() {
        return repo.get_enabled_addons();
    }
}
