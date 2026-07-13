package magentoegypt.locafy.addons.advance_Report

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import magentoegypt.locafy.addons.advance_Report.adapters.VendorSalesAdapter
import magentoegypt.locafy.addons.advance_Report.advance_report_view_model.AdvanceReportViewModel
import magentoegypt.locafy.addons.advance_Report.appBase.AppUtils
import magentoegypt.locafy.addons.advance_Report.appBase.BaseFragmentWithViewModel
import magentoegypt.locafy.addons.advance_Report.appBase.FilterViews
import magentoegypt.locafy.addons.advance_Report.models.Product
import magentoegypt.locafy.addons.advance_Report.models.StatusModel
import magentoegypt.locafy.databinding.FragmentVendorSalesReportBinding

class VendorSalesReport : BaseFragmentWithViewModel<AdvanceReportViewModel>() {
    private val TAG = "VendorSalesReport"
    private lateinit var binding: FragmentVendorSalesReportBinding
    private val adapter = VendorSalesAdapter(this)
    private var spinnerLabel: MutableList<StatusModel> = ArrayList()
    private var labels: MutableList<String> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVendorSalesReportBinding.inflate(layoutInflater)
        backStackObserverKey = "filterSales"
        return binding.root
    }


    override fun init() {
        loadVendorSalesData()
        bindRecyclerAdapter()
        swipeRefresh()
        setListener()
    }

    private fun setListener() {
        binding.fabFilter.setOnClickListener {
            showFilterDialog(
                arrayOf(
                    FilterViews.DATE,
                    FilterViews.SELLING_PRICE_RANGE,
                 //   FilterViews.COURIER_NAME,
                    FilterViews.PRODUCTS,
                    FilterViews.STATUS_SPINNER
                ), items = labels
            )
        }
    }

    private fun loadVendorSalesData() {
        data["page"] = currentPage
        parameters["data"] = data
        Log.d(TAG, "parameters: $parameters")
        viewModel?.loadVendorSalesReport(parameters)
    }

    private fun swipeRefresh() {
        binding.swipeVendorProductSales.setOnRefreshListener {
            currentPage = 1
            resultCount = 0
            loadVendorSalesData()
        }
    }

    private fun bindRecyclerAdapter() {
        binding.recVendorSalesReport.adapter = adapter
    }

    override fun setVModel(): AdvanceReportViewModel {
        return ViewModelProvider(this)[AdvanceReportViewModel::class.java]
    }

    override fun observers() {
        viewModel?.observerVendorSalesReport()?.observe(viewLifecycleOwner) {
            if (binding.swipeVendorProductSales.isRefreshing) {
                binding.swipeVendorProductSales.isRefreshing = false
                adapter.clearList()
            }
            showLog("observers: $it", tag = TAG)
            if (it != null) {
                var message = "";
                val vendorData = it.getAsJsonObject("vendor_data")
                if (vendorData.get("success").asBoolean)
                    bindToAdapter(vendorData)
                else {
                    showLog("response is not success $it", tag = TAG)
                    message = vendorData.get("message").asString
                }

                    showNoDataFoundLay(
                        binding.recVendorSalesReport,
                        binding.tvLogo,
                        message,
                        !vendorData.get("success").asBoolean
                    )

            }
        }
    }

    private fun bindToAdapter(it: JsonObject?) {

        spinnerLabel.clear()
        labels.clear()
        val productList: MutableList<Product> = ArrayList()

        for (jsonObject in it?.getAsJsonArray("payment_status")!!) {
            val status: StatusModel = Gson().fromJson(
                jsonObject.asJsonObject.toString(), StatusModel::class.java
            )
            spinnerLabel.add(status)
            labels.add(status.label)
        }

        for (jsonObject in it.getAsJsonArray("product")!!) {
            val product: Product = Gson().fromJson(
                jsonObject.asJsonObject.toString(), Product::class.java
            )
            product.orderPaymentState = getPaymentStatus(product.orderPaymentState)
            productList.add(product)
        }


        adapter.addData(productList)

        resultCount += productList.size
        updateResultCount(resultCount, binding.tvResultCount)

        AppUtils.hideDialog()
        binding.fabFilter.isEnabled = true
    }

    private fun getPaymentStatus(status: String): String {
        var value = ""
        for (statusMode: StatusModel in spinnerLabel) {
            if (statusMode.value == status)
                value = statusMode.label
        }
        return value

    }

    override fun onLoadMore(obj: Any) {
        currentPage += 1
        loadVendorSalesData()
    }

    override fun updateLoading(isLoading: Boolean) {
        if (isLoading) binding.progressBar.visibility =
            View.VISIBLE else binding.progressBar.visibility = View.GONE
    }

    override fun filterData(json: JsonObject) {
        adapter.clearList()
        val filter = JsonObject()
        filter.add("product_qty", json.get("products"))
        filter.add("created_at", json.get("created_at"))
        filter.add("base_order_total", json.get("sellingPriceRange"))
        filter.addProperty("order_payment_state", getStatus(json.get("status")))
        filter.add("tracking_number", json.get("trackingNumber"))
        filter.add("courier_name", json.get("courier_name"))
        data["filter"] = filter.toString()
        loadVendorSalesData()
    }

    private fun getStatus(get: JsonElement): String {
        var value = ""
        val status = get.toString().replace("\"", "")
        Log.d(TAG, "getStatus: $status")
        for (statusModel: StatusModel in spinnerLabel) {
            if (status == statusModel.label)
                value = statusModel.value

        }
        return value
    }

    override fun onResetFilterClicked() {
        adapter.clearList()
        loadVendorSalesData()
    }

}