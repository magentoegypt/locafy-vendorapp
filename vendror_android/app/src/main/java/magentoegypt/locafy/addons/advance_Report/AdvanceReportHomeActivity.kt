package magentoegypt.locafy.addons.advance_Report

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import magentoegypt.locafy.R
import magentoegypt.locafy.addons.advance_Report.advance_report_view_model.AdvanceReportViewModel
import magentoegypt.locafy.addons.advance_Report.appBase.AppConstant.ACTIVITY_NAME
import magentoegypt.locafy.addons.advance_Report.appBase.AppConstant.OUT_OF_STOCK_PRODUCT_ACTIVITY
import magentoegypt.locafy.addons.advance_Report.appBase.AppConstant.PAYMENT_REPORT_ACTIVITY
import magentoegypt.locafy.addons.advance_Report.appBase.AppConstant.PRODUCT_SALES_ACTIVITY
import magentoegypt.locafy.addons.advance_Report.appBase.AppConstant.PRODUCT_VIEWS_ACTIVITY
import magentoegypt.locafy.addons.advance_Report.appBase.AppConstant.RETURN_REPORT_ACTIVITY
import magentoegypt.locafy.addons.advance_Report.appBase.AppConstant.SOLD_PRODUCT_ACTIVITY
import magentoegypt.locafy.addons.advance_Report.appBase.BaseActivityWithViewModel
import magentoegypt.locafy.databinding.AdvanceReportActivityMainBinding

class AdvanceReportHomeActivity : BaseActivityWithViewModel<AdvanceReportViewModel>() {
    private lateinit var binding: AdvanceReportActivityMainBinding
    private lateinit var redirectActivityName: String
    override fun setView(): View {
        binding = DataBindingUtil.setContentView(this, R.layout.advance_report_activity_main)

        return binding.root
    }

    override fun init() {
        if (!intent.hasExtra(ACTIVITY_NAME)) {
            showToast("redirect activity name not found !!")
            return
        }
        redirectActivityName = intent.getStringExtra(ACTIVITY_NAME)!!
        redirectToActivity()
        // setUpTopAppBar(binding.topAppBar)

    }

    private fun redirectToActivity() {
        when (redirectActivityName) {
            PRODUCT_SALES_ACTIVITY -> {
                navController.navigate(R.id.action_homeFragment_to_vendorSalesReport2)
            }
            SOLD_PRODUCT_ACTIVITY -> {
                navController.navigate(R.id.action_homeFragment_to_soldProducts)
            }
            PRODUCT_VIEWS_ACTIVITY -> {
                navController.navigate(R.id.action_homeFragment_to_productViews)
            }
            OUT_OF_STOCK_PRODUCT_ACTIVITY -> {
                navController.navigate(R.id.action_homeFragment_to_outOfStockProducts)
            }
            PAYMENT_REPORT_ACTIVITY -> {
                navController.navigate(R.id.action_homeFragment_to_vendorPaymentReport)
            }
            RETURN_REPORT_ACTIVITY -> {
                navController.navigate(R.id.action_homeFragment_to_returnReport)
            }
        }
    }

    override fun setVModel(): AdvanceReportViewModel {
        return ViewModelProvider(this)[AdvanceReportViewModel::class.java]
    }

    override fun onBackStackChanged() {
        finish()
        super.onBackStackChanged()
    }

}