package magentoegypt.locafy.addons.advance_Report.activity

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider

import magentoegypt.locafy.addons.advance_Report.advance_report_view_model.AdvanceReportViewModel
import magentoegypt.locafy.addons.advance_Report.appBase.BaseActivityWithViewModel
import magentoegypt.locafy.R
import magentoegypt.locafy.databinding.ActivityOutOfStockProductBinding

class OutOfStockProductActivity : BaseActivityWithViewModel<AdvanceReportViewModel>() {

    lateinit var binding: ActivityOutOfStockProductBinding
    override fun setView(): View {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_out_of_stock_product)
        return binding.root
    }

    override fun init() {

    }

    override fun setVModel(): AdvanceReportViewModel {
        return ViewModelProvider(this)[AdvanceReportViewModel::class.java]
    }

}