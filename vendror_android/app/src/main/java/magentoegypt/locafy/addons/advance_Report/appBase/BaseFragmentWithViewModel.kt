package magentoegypt.locafy.addons.advance_Report.appBase

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import magentoegypt.locafy.addons.advance_Report.interfaces.AddOnScrollMoreListener
import magentoegypt.locafy.addons.advance_Report.utils.App
import magentoegypt.locafy.addons.advance_Report.utils.SharedPrefs
import magentoegypt.locafy.R
import magentoegypt.locafy.databinding.VendorSalesFilterBinding
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.JsonObject
import org.json.JSONObject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


abstract class BaseFragmentWithViewModel<T : BaseViewModel<BaseAppRepo>> : Fragment(),
    AddOnScrollMoreListener {
    var d: Dialog? = null
    var viewModel: T? = null
    var myView: View? = null
    lateinit var navController: NavController
    private val TAG = "REpo_okHttp"
    val data: HashMap<String, Any> = HashMap()
    val parameters: HashMap<String, Any> = HashMap()
    var session: Ced_MultiVendor_VendorSessionManagement? = null
    lateinit var userPrefs: SharedPrefs
    var backStackObserverKey = ""
    var fragmentName = ""
    var currentPage = 1
    var resultCount = 0
    lateinit var json: JsonObject
    lateinit var dialogBinding: VendorSalesFilterBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        myView = view
        viewModel = setVModel()
        session = Ced_MultiVendor_VendorSessionManagement(requireActivity())

        resultCount = 0
        currentPage = 1
        initDataMap()
        initPrefs()
        backStackData()
        registerApiObserver()
        init()
        observers()
    }


    private fun initPrefs() {
        userPrefs = SharedPrefs(requireContext())
    }

    private fun initDataMap() {
        data["vendor_id"] = session!!.vendorid
        data["hashkey"] = session?.hahkey!!
        parameters["data"] = data
    }

    abstract fun init()
    abstract fun setVModel(): T
    fun showSnackBar(
        msg: String?,
        showRetry: Boolean = false,
        retryText: String = "OK",
        gravity: Int = Gravity.BOTTOM
    ) {
        try {
            val snack = Snackbar.make(myView!!, msg!!, Snackbar.LENGTH_LONG)
            val view = snack.view
            val params = view.layoutParams as CoordinatorLayout.LayoutParams
            params.gravity = gravity
            view.layoutParams = params
            snack.show()
            if (showRetry) snack.setAction(retryText) { view1: View? -> onRetryClick() }
        }
        catch (e: Exception) {
            Log.d(TAG, "showSnackBar: ${e.localizedMessage}")
        }

    }

    private fun showDialogOnApiFailure(msg: String?, title: String?) {
        AlertDialog.Builder(requireActivity()).setTitle(title).setMessage(msg)
            .setIcon(R.drawable.ic_launcher_foreground).setPositiveButton(
                "Retry"
            ) { dialog: DialogInterface, id: Int ->
                dialog.cancel()
                onRetryClick()
            }.setNegativeButton(
                "Close"
            ) { dialogInterface: DialogInterface?, i: Int -> navController.navigateUp() }
            .setCancelable(false).show()
    }

    protected open fun updateLoading(isLoading: Boolean) {
        if (isLoading) AppUtils.showRequestDialog(requireActivity()) else AppUtils.hideDialog()
    }

    private fun registerApiObserver() {
        viewModel!!.error!!.observe(
            requireActivity()
        ) { data: String? ->
            val bundle = Bundle()
            bundle.putString("data", data)
            navController.navigate(R.id.apiFaliureScreen, bundle)
        }
        viewModel!!.loading!!.observe(
            requireActivity()
        ) { isLoading: Boolean ->
            updateLoading(
                isLoading
            )
        }
    }

    fun onRetryClick() {}

    fun setTitle(title: String) {
        navController.getViewModelStoreOwner(R.id.navAdvanceReport)
    }

    abstract fun observers()

    override fun onLoadMore(obj: Any) {
    }

    fun showToast(msg: String) {
        Toast.makeText(App.instance, msg, Toast.LENGTH_SHORT).show()
    }

    private fun backStackData() {
        navController.currentBackStackEntry?.savedStateHandle?.getLiveData<String>(
            backStackObserverKey
        )?.observe(
            viewLifecycleOwner
        ) { result ->
            val jsonObj = JSONObject(result.toString())
            val filterMap: HashMap<String, String> = HashMap()
            jsonObj.keys().forEach {
                filterMap[it.toString()] = jsonObj.getString(it.toString())
            }
            data["filter"] = filterMap.toString()
            backStackObserver(data)

        }
    }

    private fun finishActivity() {

    }

    protected open fun backStackObserver(jsonObj: HashMap<String, Any>) {

    }

    protected open fun showLog(msg: String, tag: String = TAG) {
        if (AppConstant.IS_LOG_ENABLE) Log.d(tag, "log => : $msg")
    }

    protected open fun updateResultCount(resultCount: Int, tvResultCount: TextView) {
        if (resultCount == 0) tvResultCount.text = "0 result(s) found"
        else tvResultCount.text = "Showing $resultCount result(s)"

    }

    protected open fun showNoDataFoundLay(
        recyclerView: RecyclerView, tvLogo: TextView,text:String, status: Boolean = false
    ) {
        if (recyclerView.adapter?.itemCount!! <1) {
            recyclerView.visibility = View.GONE
            tvLogo.visibility = View.VISIBLE
            tvLogo.text = text
        } else {
            recyclerView.visibility = View.VISIBLE
            tvLogo.visibility = View.GONE
            tvLogo.text = text
        }

    }

    fun selectDate(textInputLayout: TextInputLayout) {
        val inflater = this.layoutInflater
        val view: View = inflater.inflate(R.layout.date_picker, null, false)
        val myDatePicker: DatePicker = view.findViewById(R.id.myDatePicker)
        myDatePicker.calendarViewShown = false
        myDatePicker.maxDate = System.currentTimeMillis()

        AlertDialog.Builder(requireActivity()).setView(view).setTitle(getString(R.string.select_date))
            .setPositiveButton(getString(R.string.go)) { dialog, id ->
                val month = myDatePicker.month + 1
                val day = myDatePicker.dayOfMonth
                val year = myDatePicker.year
                var date = "$day/$month/$year"

                if (month<10&&day<10)
                    date = "$year-0$month-0$day"
                else if(month<10)
                    date = "$year-0$month-$day"
                else if(day<10)
                    date = "$year-$month-0$day"
                else{
                    date = "$year-$month-$day"
                }

                dialog.cancel()
                (textInputLayout.editText as? TextInputEditText)?.setText(date)
            }.show()
    }

    private fun bindSpinnerList(textField: VendorSalesFilterBinding, items: List<String>) {

        if (dialogBinding.textInputLayoutCustomerList.visibility == View.VISIBLE) {
            val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
            (textField.textInputLayoutCustomerList.editText as? AutoCompleteTextView)?.setAdapter(
                adapter
            )
        }

        if (dialogBinding.textInputLayoutStatusList.visibility == View.VISIBLE) {
            val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
            (textField.textInputLayoutStatusList.editText as? AutoCompleteTextView)?.setAdapter(
                adapter
            )
        }

    }

    fun showFilterDialog(
        viewList: Array<String>, items: List<String> = listOf(
            requireActivity().getString(R.string.simple),
            requireActivity().getString(R.string.virtual),
            requireActivity().getString(R.string.grouped),
            requireActivity().getString(R.string.bundle),
            requireActivity().getString(R.string.configurble)
        )
    ) {
        dialogBinding = VendorSalesFilterBinding.inflate(requireActivity().layoutInflater)
        val alert = AlertDialog.Builder(activity)
        alert.setTitle(getString(R.string.alert_name))
        alert.setView(dialogBinding.root)
        d = alert.create()
        d?.show()
        updateVisibility(viewList)
        bindSpinnerList(dialogBinding, items)
        dialogBinding.textInputLayoutFrom.setEndIconOnClickListener {
            selectDate(dialogBinding.textInputLayoutFrom)
        }
        dialogBinding.textInputLayoutorderDate.setEndIconOnClickListener {
            selectDate(dialogBinding.textInputLayoutorderDate)
        }
        dialogBinding.textInputLayoutToDate.setEndIconOnClickListener {
            selectDate(dialogBinding.textInputLayoutToDate)
        }
        dialogBinding.btnShowReport.setOnClickListener {
            d?.dismiss()
            validateInputData()
        }
        dialogBinding.btnClearFilter.setOnClickListener {
            d?.dismiss()
            data.remove("filter")
            currentPage = 1
            resultCount = 0
            onResetFilterClicked()
        }

    }

    open fun onResetFilterClicked() {

    }

    private fun updateVisibility(list: Array<String>) {
        dialogBinding.textInputLayoutSku.isVisible = list.contains(FilterViews.SKU)
        dialogBinding.textInputLayoutProductName.isVisible = list.contains(FilterViews.PRODUCT_NAME)
        dialogBinding.textInputLayoutCustomerList.isVisible =
            list.contains(FilterViews.PRODUCT_DROPDOWN)
        dialogBinding.textInputLayoutStatusList.isVisible =
            list.contains(FilterViews.STATUS_SPINNER)
        dialogBinding.textInputLayoutQuantity.isVisible = list.contains(FilterViews.QUANTITY)
        dialogBinding.textInputLayoutDeliveryStatus.isVisible =
            list.contains(FilterViews.DELIVERY_STATUS)
        dialogBinding.textInputLayoutSellingPrice.isVisible =
            list.contains(FilterViews.SELLING_PRICE)
        dialogBinding.textInputLayoutToDate.isVisible = list.contains(FilterViews.DATE)
        dialogBinding.textInputLayoutFrom.isVisible = list.contains(FilterViews.DATE)
        dialogBinding.textInputLayoutorderDate.isVisible = list.contains(FilterViews.ORDER_DATE)
        dialogBinding.textInputLayoutOrderId.isVisible = list.contains(FilterViews.ORDER_ID)
        dialogBinding.status.isVisible = list.contains(FilterViews.REPORT_ORDER_STATUS)
        dialogBinding.textInputLayoutCourierName.isVisible = list.contains(FilterViews.COURIER_NAME)
        dialogBinding.textInputLayoutToAmount.isVisible = list.contains(FilterViews.AMOUNT)
        dialogBinding.textInputLayoutAmountFrom.isVisible = list.contains(FilterViews.AMOUNT)
        dialogBinding.textInputLayoutTransactionId.isVisible =
            list.contains(FilterViews.TRANSACTION_ID)
        dialogBinding.sellingPriceRangView.isVisible =
            list.contains(FilterViews.SELLING_PRICE_RANGE)
        dialogBinding.quantityRangeViews.isVisible = list.contains(FilterViews.QUANTITY_RANGE)
        dialogBinding.textInputLayoutProductTypeEt.isVisible =
            list.contains(FilterViews.PRODUCT_EDITABLE)
        dialogBinding.textInputLayoutProductViews.isVisible =
            list.contains(FilterViews.PRODUCT_VIEWS)
        dialogBinding.textInputLayoutProductCount.isVisible =
            list.contains(FilterViews.PRODUCTS)
        dialogBinding.sellingAmountRangView.isVisible = list.contains(FilterViews.AMOUNT)
        dialogBinding.textInputLayoutTrackingNumber.isVisible =
            list.contains(FilterViews.TRACKING_NUMBER)

    }

    open fun filterData(json: JsonObject) {

    }

    private fun validateInputData() {
        val orderDate = dialogBinding.textInputLayoutorderDate.editText?.text.toString()
        val fromDate = dialogBinding.textInputLayoutFrom.editText?.text.toString()
        val toDate = dialogBinding.textInputLayoutToDate.editText?.text.toString()
        val productType = dialogBinding.tvCustomerList.text.toString()
        val spinnerStatus = dialogBinding.tvStatusList.text.toString()
        val productName = dialogBinding.textInputLayoutProductName.editText?.text.toString()
        val quantity = dialogBinding.textInputLayoutQuantity.editText?.text.toString()
        val sku = dialogBinding.textInputLayoutSku.editText?.text.toString()
        val courierName = dialogBinding.textInputLayoutCourierName.editText?.text.toString()
        val trackingNumber = dialogBinding.textInputLayoutTrackingNumber.editText?.text.toString()
        val sellingPrice = dialogBinding.textInputLayoutSellingPrice.editText?.text.toString()
        val status = dialogBinding.textInputLayoutDeliveryStatus.editText?.text.toString()
        val orderId = dialogBinding.textInputLayoutOrderId.editText?.text.toString()
        val orderStatus = dialogBinding.status.selectedItemPosition

        val tId = dialogBinding.textInputLayoutTransactionId.editText?.text.toString()
        val amountFrom = dialogBinding.textInputLayoutAmountFrom.editText?.text.toString()
        val amountTo = dialogBinding.textInputLayoutToAmount.editText?.text.toString()
        val productTypeEt = dialogBinding.textInputLayoutProductTypeEt.editText?.text.toString()
        val productViews = dialogBinding.textInputLayoutProductViews.editText?.text.toString()
        val products = dialogBinding.textInputLayoutProductCount.editText?.text.toString()

        //Price Range
        val sellingPriceFrom =
            dialogBinding.textInputLayoutSellingPriceFrom.editText?.text.toString()
        val sellingPriceTo = dialogBinding.textInputLayoutSellingPriceTo.editText?.text.toString()

        //Quantity Range
        val quantityFrom =
            dialogBinding.textInputLayoutQuantityFrom.editText?.text.toString()
        val quantityTo = dialogBinding.textInputLayoutQuantityTo.editText?.text.toString()

        when {
            fromDate.isEmpty() && toDate.isNotEmpty() -> {
                showSnackBar(getString(R.string.fromdaterequired))
            }
            toDate.isEmpty() && fromDate.isNotEmpty() -> {
                showSnackBar("TO DATE REQUIRED")
            }
            amountFrom.isEmpty() && amountTo.isNotEmpty() -> {
                showSnackBar("AMOUNT RANGE IS INVALID", retryText = "Dismiss", showRetry = true)
            }
            amountTo.isEmpty() && amountFrom.isNotEmpty() -> {
                showSnackBar("AMOUNT RANGE IS INVALID", retryText = "Dismiss", showRetry = true)
            }
            sellingPriceFrom.isEmpty() && sellingPriceTo.isNotEmpty() -> {
                showSnackBar(
                    "SELLING PRICE RANGE IS INVALID",
                    retryText = "Dismiss",
                    showRetry = true
                )
            }
            sellingPriceTo.isEmpty() && sellingPriceFrom.isNotEmpty() -> {
                showSnackBar(
                    "SELLING PRICE RANGE IS INVALID",
                    retryText = "Dismiss",
                    showRetry = true
                )
            }

            //quantity Validation
            quantityFrom.isEmpty() && quantityTo.isNotEmpty() -> {
                showSnackBar(
                    "QUANTITY RANGE IS INVALID",
                    retryText = "Dismiss",
                    showRetry = true
                )
            }
            quantityTo.isEmpty() && quantityFrom.isNotEmpty() -> {
                showSnackBar(
                    "QUANTITY RANGE IS INVALID",
                    retryText = "Dismiss",
                    showRetry = true
                )
            }

            else -> {
                try {
                    json = JsonObject()

                    val dateMap = JsonObject()
                    dateMap.addProperty("to", "" + toDate)
                    dateMap.addProperty("from", "" +fromDate)
                    Log.d(TAG, "validateInputData: $toDate ,$fromDate")

                    //Selling Price Range
                    val sellingPriceRangeMap = JsonObject()
                    sellingPriceRangeMap.addProperty("to", "" + sellingPriceTo)
                    sellingPriceRangeMap.addProperty("from", "" + sellingPriceFrom)
                    json.add("sellingPriceRange", sellingPriceRangeMap)

                    //Quantity Range
                    val quantityRangeMap = JsonObject()
                    quantityRangeMap.addProperty("to", "" + quantityTo)
                    quantityRangeMap.addProperty("from", "" + quantityFrom)
                    json.add("qty", quantityRangeMap)

                    json.addProperty("order_date", orderDate)
                    json.addProperty("order_status", orderStatus)

                    json.add("created_at", dateMap)
                    json.add("date_search", dateMap)

                    json.addProperty("product_type", productType)
                    json.addProperty("product_type", productTypeEt)
                    json.addProperty("product_views", productViews)
                    json.addProperty("type_id", productType)

                    val s = if (TextUtils.isEmpty(productType)) spinnerStatus else productType
                    json.addProperty("status", s)

                    json.addProperty("product_name", productName)
                    json.addProperty("name", productName)

                    json.addProperty("ordered_qty", quantity)
                    json.addProperty("product_qty", quantity)

                    json.addProperty("sku", sku)
                    json.addProperty("views", "")
                    json.addProperty("order_payment_state", status)
                    json.addProperty("base_order_total", sellingPrice)
                    json.addProperty("order_id", orderId)
                    json.addProperty("order_date", orderDate)
                    json.addProperty("courier_name", courierName)
                    json.addProperty("trackingNumber", trackingNumber)
                    json.addProperty("products", products)

                    val amountMap = JsonObject()
                    amountMap.addProperty("to", "" + amountTo)
                    amountMap.addProperty("from", "" + amountFrom)
                    json.add("amountMap", amountMap)
                    json.addProperty("transaction_id", tId)

                    Log.d(TAG, "validateInputData_456: "+json)

                }
                catch (e: Exception) {
                    Log.d(TAG, "validateInputData: ")
                }
                data["filter"] = json.toString()
                currentPage = 1
                resultCount = 0
                Log.d(TAG, "filterData_462: $data")
                filterData(json)
            }
        }
    }

    private fun getDateInRequestFormat(fromDate: String): String? {
        if (fromDate.isNotEmpty()) {
            val inputPattern = "dd/MM/yyyy"
//            val outputPattern = "dd-MM-yyyy"
            val outputPattern = "yyyy-MM-dd"
            val inputFormat = SimpleDateFormat(inputPattern, Locale.US)
            val outputFormat = SimpleDateFormat(outputPattern, Locale.US)

            var date: Date? = null
            var str: String? = null

            try {
                date = inputFormat.parse(fromDate)
                str = outputFormat.format(date)
            }
            catch (e: ParseException) {
                e.printStackTrace()
            }
            return str
        }
        else
            return ""
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().onBackPressed()
    }
}

