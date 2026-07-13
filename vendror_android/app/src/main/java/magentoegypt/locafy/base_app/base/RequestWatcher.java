package magentoegypt.locafy.base_app.base;


import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import magentoegypt.locafy.vendor_notification.app.MyApplication;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestWatcher {
    public MutableLiveData<Boolean> _loading = new MutableLiveData<>();
    public MutableLiveData<String> _error = new MutableLiveData<>();

    public void enqueueApi(Call<Object> callApi, final MutableLiveData<Object> datObserver) {
        _loading.setValue(true);
        callApi.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                _loading.setValue(false);
                checkAndValidateResponse(response, datObserver);
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable throwable) {
                _loading.setValue(false);
                _error.setValue(throwable.getLocalizedMessage());
                Log.d("error", ": failed to load " + throwable.getLocalizedMessage());
            }
        });
    }

    public void checkAndValidateResponse(Response<Object> response, MutableLiveData<Object> datObserver) {
        switch (response.code()) {
            case 200: {
                if (null != response.body()) {
                    datObserver.setValue(response.body());
                } else {
                    _error.setValue(ResponseError.EMPTY_BODY);
                }
                break;
            }
            case 404: {
                _error.setValue(ResponseError.ERROR_404);
                break;
            }
            case 400: {
                _error.setValue(ResponseError.ERROR_400);
                break;
            }
            case 401: {
                _error.setValue(ResponseError.ERROR_401);
                break;
            }
            case 403: {
                _error.setValue(ResponseError.ERROR_403);
                break;
            }
            case 408: {
                _error.setValue(ResponseError.ERROR_408);
                break;
            }
            case 426: {
                _error.setValue(ResponseError.ERROR_426);
                break;
            }
            case 415: {
                _error.setValue(ResponseError.ERROR_415);
                break;
            }
            case 500: {
                _error.setValue(ResponseError.ERROR_500);
                break;
            }
            case 501: {
                _error.setValue(ResponseError.ERROR_501);
                break;
            }
            case 503: {
                _error.setValue(ResponseError.ERROR_503);
                break;
            }
            case 505: {
                _error.setValue(ResponseError.ERROR_505);
                break;
            }
            default: {
                _error.setValue(ResponseError.FAILED_TO_LOAD);
            }
        }
    }

}

