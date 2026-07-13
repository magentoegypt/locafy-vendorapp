package magentoegypt.locafy.addons.advance_Report.advance_report_view_model

import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import magentoegypt.locafy.addons.advance_Report.appBase.BaseAppRepo
import magentoegypt.locafy.addons.advance_Report.appBase.RetrofitInstance
import magentoegypt.locafy.addons.advance_Report.interfaces.AdvanceReportApi
import magentoegypt.locafy.base_app.base.AppUrl


class Repo : BaseAppRepo() {


  /*  private var api: AdvanceReportApi = RetrofitInstance.getInstance(AppConstant.BASE_URL)!!
        .create(AdvanceReportApi::class.java)*/

    private var api: AdvanceReportApi = RetrofitInstance.getInstance(AppUrl.getBaseUrl())!!
        .create(AdvanceReportApi::class.java)

    val _observeSalesReport: MutableLiveData<JsonObject?> = MutableLiveData()

    fun loadVendorSalesReport(map: HashMap<String, Any>) {
        enqueueApi(api.loadSalesReportData(map), _observeSalesReport)
    }

    val _observerOutOfStock: MutableLiveData<JsonObject?> = MutableLiveData()

    fun loadOutOfStockData(parameters: HashMap<String, Any>) {
        enqueueApi(api.loadOutOfStockData(parameters), _observerOutOfStock)
    }

    private val _observerSoldProductData: MutableLiveData<JsonObject?> = MutableLiveData()
    fun getSoldProductData(): MutableLiveData<JsonObject?> {
        return _observerSoldProductData
    }

    fun loadSoldProductData(parameters: HashMap<String, Any>) {
        enqueueApi(api.loadSoldProductData(parameters), _observerSoldProductData)
    }


    private val _observerProductViewsData: MutableLiveData<JsonObject?> = MutableLiveData()
    fun getProductViewsData(): MutableLiveData<JsonObject?> {
        return _observerProductViewsData
    }

    fun loadProductViewsData(parameters: HashMap<String, Any>) {
        enqueueApi(api.loadProductViewsData(parameters), _observerProductViewsData)

    }


    private val _observerVendorPaymentReport: MutableLiveData<JsonObject?> = MutableLiveData()
    fun getVendorPaymentReport(): MutableLiveData<JsonObject?> {
        return _observerVendorPaymentReport
    }

    fun loadVendorPaymentReport(parameters: HashMap<String, Any>) {
        enqueueApi(api.loadVendorPaymentReport(parameters), _observerVendorPaymentReport)
    }

    private val _observerVendorReturnProductData: MutableLiveData<JsonObject?> = MutableLiveData()
    fun getReturnProductData(): MutableLiveData<JsonObject?> {
        return _observerVendorReturnProductData
    }

    fun loadReturnProductData(parameters: HashMap<String, Any>) {
        enqueueApi(api.loadReturnProductData(parameters), _observerVendorReturnProductData)

    }


}