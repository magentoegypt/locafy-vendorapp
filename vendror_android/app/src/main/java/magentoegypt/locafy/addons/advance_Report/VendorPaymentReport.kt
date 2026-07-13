package magentoegypt.locafy.addons.advance_Report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.gson.JsonObject
import magentoegypt.locafy.addons.advance_Report.adapters.VendorPaymentReportAdapter
import magentoegypt.locafy.addons.advance_Report.advance_report_view_model.AdvanceReportViewModel
import magentoegypt.locafy.addons.advance_Report.appBase.AppUtils
import magentoegypt.locafy.addons.advance_Report.appBase.BaseFragmentWithViewModel
import magentoegypt.locafy.addons.advance_Report.appBase.FilterViews
import magentoegypt.locafy.addons.advance_Report.models.Product
import magentoegypt.locafy.addons.advance_Report.models.StatusModel
import magentoegypt.locafy.databinding.FragmentVendorPaymentReportBinding

class VendorPaymentReport : BaseFragmentWithViewModel<AdvanceReportViewModel>() {

    private val TAG = "VendorPaymentReport"
    lateinit var binding: FragmentVendorPaymentReportBinding
    private val adapter = VendorPaymentReportAdapter(this)
    private var spinnerLabel: MutableList<StatusModel> = ArrayList()
    private var labels: MutableList<String> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVendorPaymentReportBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun init() {
        loadVendorPaymentReport()
        bindRecyclerAdapter()
        swipeRefresh()
        setListener()
    }

    private fun setListener() {
        binding.fabFilterPayment.setOnClickListener {
            showFilterDialog(
                arrayOf(
                    FilterViews.DATE,
                    FilterViews.AMOUNT,
                    FilterViews.TRANSACTION_ID
                )
            )
        }
    }

    private fun loadVendorPaymentReport() {
        data["page"] = currentPage
        parameters["data"] = data
        viewModel?.loadVendorPaymentReport(parameters)
    }

    private fun swipeRefresh() {
        binding.swipeVendorPaymentReport.setOnRefreshListener {
            currentPage = 1
            resultCount = 0
            loadVendorPaymentReport()
        }
    }

    private fun bindRecyclerAdapter() {
        binding.recVendorSalesReport.adapter = adapter
    }

    override fun setVModel(): AdvanceReportViewModel {
        return ViewModelProvider(this)[AdvanceReportViewModel::class.java]
    }

    override fun observers() {
        viewModel?.observerVendorPaymentReport()?.observe(viewLifecycleOwner) {
            if (binding.swipeVendorPaymentReport.isRefreshing) {
                binding.swipeVendorPaymentReport.isRefreshing = false
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
            else{
                showLog("else_97")
            }
        }
    }

    private fun bindToAdapter(it: JsonObject?) {
        val productList: MutableList<Product> = ArrayList()

        spinnerLabel.clear()
        labels.clear()

        for (jsonObject in it?.getAsJsonArray("payment_status")!!) {
            val status: StatusModel = Gson().fromJson(
                jsonObject.asJsonObject.toString(), StatusModel::class.java
            )
            spinnerLabel.add(status)
            labels.add(status.label)
        }
        for (jsonObject in it?.getAsJsonArray("product")!!) {
            val product: Product = Gson().fromJson(
                jsonObject.asJsonObject.toString(), Product::class.java
            )
            product.status = getPaymentStatus(product.status)
            productList.add(product)
        }
        adapter.addData(productList)

        resultCount += productList.size
        updateResultCount(resultCount, binding.tvResultCount)

        AppUtils.hideDialog()
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
        loadVendorPaymentReport()
    }

    override fun updateLoading(isLoading: Boolean) {
        if (isLoading) binding.progressBar.visibility =
            View.VISIBLE else binding.progressBar.visibility = View.GONE
    }


    override fun filterData(json: JsonObject) {
        adapter.clearList()
        val filter = JsonObject()
        filter.add("created_at", json.get("created_at"))
        filter.add("amount", json.get("amount"))
        filter.add("transaction_id", json.get("transaction_id"))
        data["filter"] = filter.toString()
        loadVendorPaymentReport()
    }

    override fun onResetFilterClicked() {
        adapter.clearList()
        loadVendorPaymentReport()
    }
}