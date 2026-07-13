package magentoegypt.locafy.addons.advance_Report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import magentoegypt.locafy.addons.advance_Report.adapters.ReturnProduct
import magentoegypt.locafy.addons.advance_Report.advance_report_view_model.AdvanceReportViewModel
import magentoegypt.locafy.addons.advance_Report.appBase.AppUtils
import magentoegypt.locafy.addons.advance_Report.appBase.BaseFragmentWithViewModel
import magentoegypt.locafy.addons.advance_Report.appBase.FilterViews
import magentoegypt.locafy.addons.advance_Report.models.Product
import magentoegypt.locafy.databinding.FragmentReturnReportBinding
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONObject


class ReturnReport : BaseFragmentWithViewModel<AdvanceReportViewModel>() {
    private val TAG = "ReturnReport"
    lateinit var binding: FragmentReturnReportBinding
    private var adapter = ReturnProduct(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentReturnReportBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun init() {
        loadData()
        bindRecyclerAdapter()
        swipeRefresh()
        setListener()
    }

    private fun loadData() {
        data["page"] = currentPage
        parameters["data"] = data
        viewModel?.loadReturnProductData(parameters)
    }

    private fun bindRecyclerAdapter() {
        binding.recVendorSalesReport.adapter = adapter
    }

    private fun swipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            currentPage = 1
            resultCount = 0
            loadData()

        }
    }

    private fun setListener() {
        binding.fabFilter.setOnClickListener {
            val spinnerItems = listOf(
                "Cancelled"
            )
            showFilterDialog(
                viewList = arrayOf(
                    FilterViews.ORDER_DATE,
                    FilterViews.ORDER_ID,
                    FilterViews.REPORT_ORDER_STATUS,
                    ), items = spinnerItems
            )
        }
    }

    override fun setVModel(): AdvanceReportViewModel {
        return ViewModelProvider(this)[AdvanceReportViewModel::class.java]
    }

    private fun bindToAdapter(it: JsonObject?) {
        val productList: MutableList<Product> = ArrayList()
        for (jsonObject in it?.getAsJsonArray("product")!!) {
            val product: Product = Gson().fromJson(
                jsonObject.asJsonObject.toString(), Product::class.java
            )
            productList.add(product)
        }


        resultCount += productList.size
        updateResultCount(resultCount, binding.tvResultCount)

        adapter.addData(productList)
        AppUtils.hideDialog()
    }

    override fun observers() {
        viewModel?.observerReturnProductData()?.observe(viewLifecycleOwner) {
            if (binding.swipeRefresh.isRefreshing) {
                binding.swipeRefresh.isRefreshing = false
                adapter.clearList()
            }
            showLog("observers: $it", tag = TAG)
            if (it != null) {
                var message = "";
                val vendorData = it.getAsJsonObject("vendor_data")
                if (vendorData.get("success").asBoolean) bindToAdapter(vendorData)
                else {
                    message = vendorData.get("message").asString
                    showLog("response is not success $it", tag = TAG)
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

    override fun updateLoading(isLoading: Boolean) {
        if (isLoading) binding.progressBar.visibility =
            View.VISIBLE else binding.progressBar.visibility = View.GONE
    }

    override fun filterData(json: JsonObject) {
        adapter.clearList()

        val filter = JSONObject()
        val created_at = JSONObject()
        created_at.put("from", json.get("order_date").asString)
        created_at.put("to", json.get("order_date").asString)
        val creditmemo_status = arrayOf("Select", "Pending", "Approved", "Complete", "Cancelled")
        filter.put("created_at", created_at)
        filter.put("order_id", json.get("order_id").asString)
        val  position:Int = json.get("order_status").asInt
        if (creditmemo_status[position] == "Select") {
            filter.put("status", "")
        } else {
            filter.put("status", creditmemo_status[position])
        }
       // filter.add("status", json.get("order_status"))

        data["filter"] = filter.toString()

        loadData()
    }

    override fun onLoadMore(obj: Any) {
        currentPage += 1
        loadData()
    }

    override fun onResetFilterClicked() {
        adapter.clearList()
        loadData()
    }
}