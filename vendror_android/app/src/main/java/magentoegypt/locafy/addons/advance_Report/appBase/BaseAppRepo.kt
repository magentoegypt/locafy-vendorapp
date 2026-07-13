package magentoegypt.locafy.addons.advance_Report.appBase

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class BaseAppRepo {
    var _error = MutableLiveData<String>()
    var _loading = MutableLiveData<Boolean>()


    fun enqueueApi(callApi: Call<JsonElement?>, datObserver: MutableLiveData<JsonObject?>) {
        _loading.value = true
        callApi.enqueue(object : Callback<JsonElement?> {
            override fun onResponse(call: Call<JsonElement?>, response: Response<JsonElement?>) {
                _loading.value = false
                checkAndValidateResponse(response, datObserver)

            }

            override fun onFailure(call: Call<JsonElement?>, throwable: Throwable) {
                _loading.value = false
                _error.value = throwable.localizedMessage
                Log.d("error", ": failed to load " + throwable.localizedMessage)
            }
        })
    }


    fun checkAndValidateResponse(
        response: Response<JsonElement?>,
        datObserver: MutableLiveData<JsonObject?>
    ) {
        when (response.code()) {
            200 -> {
                if (null != response.body()) {

                    when (response.body()) {
                        is JsonObject -> {
                            val jsonObject: JsonObject =
                                (response.body() as JsonObject).asJsonObject
                            datObserver.value = jsonObject
                        }
                        is JsonArray -> {
                            val jsonArray: JsonArray =
                                (response.body() as JsonArray).asJsonArray
                            val jsonObject: JsonObject = jsonArray[0].asJsonObject
                            datObserver.value = jsonObject
                        }
                    }

                } else {
                    _error.setValue(ResponseError.EMPTY_BODY)
                }
            }
            404 -> {
                _error.setValue(ResponseError.ERROR_404)
            }
            400 -> {
                _error.setValue(ResponseError.ERROR_400)
            }
            401 -> {
                _error.setValue(ResponseError.ERROR_401)
            }
            403 -> {
                _error.setValue(ResponseError.ERROR_403)
            }
            408 -> {
                _error.setValue(ResponseError.ERROR_408)
            }
            426 -> {
                _error.setValue(ResponseError.ERROR_426)
            }
            415 -> {
                _error.setValue(ResponseError.ERROR_415)
            }
            500 -> {
                _error.setValue(ResponseError.ERROR_500)
            }
            501 -> {
                _error.setValue(ResponseError.ERROR_501)
            }
            503 -> {
                _error.setValue(ResponseError.ERROR_503)
            }
            505 -> {
                _error.setValue(ResponseError.ERROR_505)
            }
            else -> {
                _error.setValue(ResponseError.FAILED_TO_LOAD)
            }
        }
    }

}

