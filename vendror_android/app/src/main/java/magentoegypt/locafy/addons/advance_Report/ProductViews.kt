package magentoegypt.locafy.addons.advance_Report

import android.os.Bundle
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
import magentoegypt.locafy.databinding.FragmentProductViewsBinding

class ProductViews : BaseFragmentWithViewModel<AdvanceReportViewModel>() {

    private val TAG = "ProductViews"
    lateinit var binding: FragmentProductViewsBinding
    private var adapter = OutOfStockAdapter(this)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductViewsBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun init() {
        backStackObserverKey = "OutOfStockProducts"
        loadProductViewsData()
        bindRecyclerAdapter()
        swipeRefresh()
        setListener()
    }

    private fun loadProductViewsData() {
        data["page"] = currentPage
        parameters["data"] = data
        viewModel?.loadProductViewsData(parameters)
    }

    private fun bindRecyclerAdapter() {
        binding.recOutOfStock.adapter = adapter
    }

    private fun setListener() {
        binding.fabOutOfStock.setOnClickListener {
            showFilterDialog(
                viewList = arrayOf(
                    FilterViews.DATE,
                    FilterViews.SKU,
                    FilterViews.PRODUCT_NAME,
                    FilterViews.PRODUCT_EDITABLE,
                    FilterViews.PRODUCT_VIEWS,
                )
            )
        }
    }

    private fun swipeRefresh() {
        binding.swipeProductViews.setOnRefreshListener {
            currentPage = 1
            resultCount = 0
            loadProductViewsData()
        }
    }

    override fun setVModel(): AdvanceReportViewModel {
        return ViewModelProvider(this)[AdvanceReportViewModel::class.java]
    }

    override fun observers() {
        viewModel?.observerProductViewsData()?.observe(viewLifecycleOwner) {
            if (binding.swipeProductViews.isRefreshing) {
                binding.swipeProductViews.isRefreshing = false
                adapter.clearList()
            }
            showLog("observers: $it", tag = TAG)
            if (it != null) {
                var message = "";
                val vendorData = it.getAsJsonObject("vendor_data")
                if (vendorData.get("success").asBoolean) bindToAdapter(vendorData)
                else {
                    showLog("response is not success $it", tag = TAG)
                    message = vendorData.get("message").asString
                }
                showNoDataFoundLay(
                    binding.recOutOfStock, binding.tvLogo,message, !vendorData.get("success").asBoolean
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
        resultCount += productList.size

        updateResultCount(resultCount, binding.tvResultCount)
        AppUtils.hideDialog()
    }


    override fun onLoadMore(obj: Any) {
        currentPage += 1
        loadProductViewsData()
    }


    override fun updateLoading(isLoading: Boolean) {
        if (isLoading) binding.progressBar.visibility =
            View.VISIBLE else binding.progressBar.visibility = View.GONE
    }

    override fun filterData(json: JsonObject) {
        adapter.clearList()
        val filter = JsonObject()
        filter.add("sku", json.get("sku"))
        filter.add("date_search", json.get("created_at"))
        filter.add("name", json.get("name"))
        filter.add("views", json.get("product_views"))
        data["filter"] = filter.toString()
        loadProductViewsData()
    }

    override fun onResetFilterClicked() {
        adapter.clearList()
        loadProductViewsData()
    }
}