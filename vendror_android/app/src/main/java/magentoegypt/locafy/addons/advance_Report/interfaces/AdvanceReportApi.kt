package magentoegypt.locafy.addons.advance_Report.interfaces

import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AdvanceReportApi {

    @POST("rest/V1/vreport/getSales")
    fun loadSalesReportData(@Body map: HashMap<String, Any>): Call<JsonElement?>

    @POST("rest/V1/getOutStock")
    fun loadOutOfStockData(@Body map: HashMap<String, Any>): Call<JsonElement?>

    @POST("rest/V1/vreport/getSoldProduct")
    fun loadSoldProductData(@Body map: HashMap<String, Any>): Call<JsonElement?>

    @POST("rest/V1/vreport/getViews")
    fun loadProductViewsData(@Body map: HashMap<String, Any>): Call<JsonElement?>

    @POST("rest/V1/vreport/getPayment")
    fun loadVendorPaymentReport(@Body map: HashMap<String, Any>): Call<JsonElement?>

    @POST("rest/V1/vreport/getReturnProduct")
    fun loadReturnProductData(@Body map: HashMap<String, Any>): Call<JsonElement?>

}