package magentoegypt.locafy.addons.advance_Report.advance_report_view_model

import androidx.lifecycle.LiveData
import com.google.gson.JsonObject
import magentoegypt.locafy.addons.advance_Report.appBase.BaseAppRepo
import magentoegypt.locafy.addons.advance_Report.appBase.BaseViewModel

class AdvanceReportViewModel : BaseViewModel<BaseAppRepo>() {

    var repo: Repo = Repo()

    init {
        setRepo(repo)
    }

    fun observerVendorSalesReport(): LiveData<JsonObject?> {
        return repo._observeSalesReport
    }

    fun loadVendorSalesReport(map: HashMap<String, Any>) {
        repo.loadVendorSalesReport(map)
    }

    fun observerOutOfStockData(): LiveData<JsonObject?> {
        return repo._observerOutOfStock
    }


    fun loadOutOfStockData(parameters: HashMap<String, Any>) {
        repo.loadOutOfStockData(parameters)
    }

    fun observerSoldProductData(): LiveData<JsonObject?> {
        return repo.getSoldProductData()
    }

    fun loadSoldProductData(parameters: HashMap<String, Any>) {
        repo.loadSoldProductData(parameters)
    }


    fun observerProductViewsData(): LiveData<JsonObject?> {
        return repo.getProductViewsData()
    }

    fun loadProductViewsData(parameters: HashMap<String, Any>) {
        repo.loadProductViewsData(parameters)
    }

    fun observerVendorPaymentReport(): LiveData<JsonObject?> {
        return repo.getVendorPaymentReport()
    }

    fun loadVendorPaymentReport(parameters: HashMap<String, Any>) {
        repo.loadVendorPaymentReport(parameters)
    }

    fun loadReturnProductData(parameters: HashMap<String, Any>) {
        repo.loadReturnProductData(parameters)

    }

    fun observerReturnProductData(): LiveData<JsonObject?> {
        return repo.getReturnProductData()
    }
}