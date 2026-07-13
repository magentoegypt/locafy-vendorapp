package magentoegypt.locafy.addons.advance_Report

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.gson.JsonObject
import magentoegypt.locafy.addons.advance_Report.adapters.OutOfStockAdapter
import magentoegypt.locafy.addons.advance_Report.advance_report_view_model.AdvanceReportViewModel
import magentoegypt.locafy.addons.advance_Report.appBase.AppUtils
import magentoegypt.locafy.addons.advance_Report.appBase.BaseFragmentWithViewModel
import magentoegypt.locafy.addons.advance_Report.appBase.FilterViews
import magentoegypt.locafy.addons.advance_Report.models.Product
import magentoegypt.locafy.databinding.FragmentOutOfStockProductsBinding


class OutOfStockProducts : BaseFragmentWithViewModel<AdvanceReportViewModel>() {

    private val TAG = "OutOfStockProducts"

    lateinit var binding: FragmentOutOfStockProductsBinding
    private var adapter = OutOfStockAdapter(this)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOutOfStockProductsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun init() {
        backStackObserverKey = "OutOfStockProducts"
        loadOutOfStockData()
        bindRecyclerAdapter()
        swipeRefresh()
        setListener()
    }

    private fun loadOutOfStockData() {
        data["page"] = currentPage
        parameters["data"] = data
        viewModel?.loadOutOfStockData(parameters)
    }

    private fun bindRecyclerAdapter() {
        binding.recOutOfStock.adapter = adapter
    }

    private fun setListener() {
        binding.fabOutOfStock.setOnClickListener {
            showFilterDialog(
                viewList = arrayOf(
                    FilterViews.SKU,
                    FilterViews.PRODUCT_NAME,
                    FilterViews.QUANTITY_RANGE,
                    FilterViews.PRODUCT_EDITABLE
                )
            )
        }
    }

    private fun swipeRefresh() {
        binding.swipeOutOfStock.setOnRefreshListener {
            currentPage = 1
            resultCount = 0
            loadOutOfStockData()
        }
    }

    override fun setVModel(): AdvanceReportViewModel {
        return ViewModelProvider(this)[AdvanceReportViewModel::class.java]
    }

    override fun observers() {
        viewModel?.observerOutOfStockData()?.observe(viewLifecycleOwner) {
            if (binding.swipeOutOfStock.isRefreshing) {
                binding.swipeOutOfStock.isRefreshing = false
                adapter.clearList()
            }
            showLog("observers: $it", tag = TAG)
            if (it != null) {
                var message = "";
                val vendorData = it.getAsJsonObject("vendor_data")
                if (vendorData.get("success").asBoolean)
                    bindToAdapter(vendorData)
                else {
                    message = vendorData.get("message").asString
                    showLog("response is not success $it", tag = TAG)
                }

                    showNoDataFoundLay(
                        binding.recOutOfStock,
                        binding.tvLogo,
                        message,
                        !vendorData.get("success").asBoolean
                    )

            }
        }
    }


    private fun bindToAdapter(it: JsonObject?) {
        val productList: MutableList<Product> = ArrayList()
        for (jsonObject in it?.getAsJsonArray("product")!!) {
            val product: Product = Gson().fromJson(
                jsonObject.asJsonObject.toString(), Product::class.java
            )
            productList.add(product)
        }


        adapter.addData(productList)
        AppUtils.hideDialog()
    }

    override fun onLoadMore(obj: Any) {
        currentPage += 1
        loadOutOfStockData()
    }

    override fun backStackObserver(jsonObj: HashMap<String, Any>) {
        Log.d("anbs", "backStackObserver: $jsonObj")
    }

    override fun updateLoading(isLoading: Boolean) {
        if (isLoading) binding.progressBar.visibility =
            View.VISIBLE else binding.progressBar.visibility = View.GONE
    }

    override fun filterData(json: JsonObject) {
        adapter.clearList()
        val filter = JsonObject()
        filter.add("qty", json.get("qty"))
        filter.add("sku", json.get("sku"))
        filter.add("name", json.get("name"))
        filter.add("type", json.get("product_type"))
        data["filter"] = filter.toString()
        Log.d(TAG, "filterData: $filter")
        loadOutOfStockData()
    }

    override fun onResetFilterClicked() {
        adapter.clearList()
        loadOutOfStockData()
    }
}