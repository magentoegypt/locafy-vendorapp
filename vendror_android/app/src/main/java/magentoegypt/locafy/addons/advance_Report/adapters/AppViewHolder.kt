package magentoegypt.locafy.addons.advance_Report.adapters

import androidx.recyclerview.widget.RecyclerView
import magentoegypt.locafy.databinding.OutOfStockProductViewBinding
import magentoegypt.locafy.databinding.PaymentReportViewBinding
import magentoegypt.locafy.databinding.ReturnReportViewBinding
import magentoegypt.locafy.databinding.VendorSalesReportViewBinding


class AppViewHolder : RecyclerView.ViewHolder {

    lateinit var vendorSales: VendorSalesReportViewBinding
    lateinit var outOfStock: OutOfStockProductViewBinding
    lateinit var paymentReport: PaymentReportViewBinding
    lateinit var returnreport: ReturnReportViewBinding

    constructor(vendorSales: VendorSalesReportViewBinding) : super(vendorSales.root) {
        this.vendorSales = vendorSales
    }

    constructor(outOfStock: OutOfStockProductViewBinding) : super(outOfStock.root) {
        this.outOfStock = outOfStock
    }

    constructor(paymentReport: PaymentReportViewBinding) : super(paymentReport.root) {
        this.paymentReport = paymentReport
    }

    constructor(returnreport: ReturnReportViewBinding) : super(returnreport.root) {
        this.returnreport = returnreport
    }

    /*constructor(vendorSalesNested: CustomAdapterViewBinding) : super(vendorSalesNested.root) {
        this.vendorSalesNested = vendorSalesNested
    }*/
}