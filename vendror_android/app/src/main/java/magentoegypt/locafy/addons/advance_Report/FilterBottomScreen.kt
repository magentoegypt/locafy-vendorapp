package magentoegypt.locafy.addons.advance_Report

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.gson.JsonObject
import magentoegypt.locafy.addons.advance_Report.advance_report_view_model.AdvanceReportViewModel
import magentoegypt.locafy.addons.advance_Report.appBase.BaseFragmentWithViewModel
import magentoegypt.locafy.databinding.FragmentFilterBottomScreenBinding


open class FilterBottomScreen : BaseFragmentWithViewModel<AdvanceReportViewModel>() {

    private val TAG = "FilterBottomScreen"
    lateinit var binding: FragmentFilterBottomScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilterBottomScreenBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun init() {
        initListener()
        //bindCustomerList(binding.textInputLayoutCustomerList)
    }

    private fun initListener() {
        binding.ivCloseBtn.setOnClickListener {

        }

        binding.textInputLayoutFrom.setEndIconOnClickListener {
            selectDate(binding.textInputLayoutFrom)
        }

        binding.textInputLayoutToDate.setEndIconOnClickListener {
            selectDate(binding.textInputLayoutToDate)
        }

        binding.btnShowReport.setOnClickListener {
            validateInputData()

        }
        binding.btnClearFilter.setOnClickListener {
            showToast("Clicked on export CSV")
        }
    }

    private fun validateInputData() {
        val fromDate = binding.textInputLayoutFrom.editText?.text.toString()
        val toDate = binding.textInputLayoutToDate.editText?.text.toString()
        val productType = binding.tvCustomerList.text.toString()
        val productName = binding.textInputLayoutProductName.editText?.text.toString()
        val quantity = binding.textInputLayoutQuantity.editText?.text.toString()
        val sku = binding.textInputLayoutSku.editText?.text.toString()
        when {
            fromDate.isEmpty() && toDate.isNotEmpty() -> {
                showSnackBar("FROM DATE REQUIRED")
            }
            toDate.isEmpty() && fromDate.isNotEmpty() -> {
                showSnackBar("TO DATE REQUIRED")
            }

            else -> {
                try {
                    json = JsonObject()
                    val dateMap = JsonObject()
                    dateMap.addProperty("to", "" + toDate)
                    dateMap.addProperty("from", "" + fromDate)
                    json.addProperty("publish_date", dateMap.toString())
                    json.addProperty("product_type", productType)
                    json.addProperty("product_name", productName)
                    json.addProperty("quantity", quantity)
                    json.addProperty("sku", sku)
                } catch (e: Exception) {
                    Log.d(TAG, "validateInputData: ")
                }

                setBackStackData()
            }
        }

    }

    private fun setBackStackData() {
        navController.previousBackStackEntry?.savedStateHandle?.set("filterSales", json.toString())
        navController.popBackStack()
    }


    override fun setVModel(): AdvanceReportViewModel {
        return ViewModelProvider(this)[AdvanceReportViewModel::class.java]
    }

    override fun observers() {
        //bindCustomerList(binding.textInputLayoutCustomerList)

    }


}