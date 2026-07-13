package magentoegypt.locafy.addons.advance_Report.models

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("order_id") var orderId: String,
    @SerializedName("order_date") var orderDate: String,
    @SerializedName("product_qty") var productQty: String,
    @SerializedName("quantity") var quantity: String,
    @SerializedName("vendor_payment") var baseOrderTotal: String,
    @SerializedName("order_payment_state") var orderPaymentState: String,
    @SerializedName("title") var title: String,
    @SerializedName("tracking_number") var trackingNumber: String,
    @SerializedName("sku") var sku: String,
    @SerializedName("product_name") var product_name: String = "",
    @SerializedName("product_type") var product_type: String = "",
    @SerializedName("ordered_qty") var ordered_qty: String = "",
    @SerializedName("type_id") var type_id: String = "",
    @SerializedName("views") var views: String = "",
    @SerializedName("name") var name: String = "",
    @SerializedName("status") var status: String = "",
    @SerializedName("transaction_date") var transactionDate: String = "",
    @SerializedName("transaction_id") var transactionId: String = "",
    @SerializedName("amount") var amount: String = "",
    @SerializedName("order_description") var orderDescription: String = "",
//    @SerializedName("status") var status: String = "",
)
