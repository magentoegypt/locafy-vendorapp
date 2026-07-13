package magentoegypt.locafy.vendor_transaction.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderDetail {
    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("grand_total")
    @Expose
    private String grandTotal;
    @SerializedName("commission_fee")
    @Expose
    private String commissionFee;
    @SerializedName("order_paymode")
    @Expose
    private String orderPaymode;
    @SerializedName("vendor_payment")
    @Expose
    private String vendorPayment;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(String grandTotal) {
        this.grandTotal = grandTotal;
    }

    public String getCommissionFee() {
        return commissionFee;
    }

    public void setCommissionFee(String commissionFee) {
        this.commissionFee = commissionFee;
    }

    public String getOrderPaymode() {
        return orderPaymode;
    }

    public void setOrderPaymode(String orderPaymode) {
        this.orderPaymode = orderPaymode;
    }

    public String getVendorPayment() {
        return vendorPayment;
    }

    public void setVendorPayment(String vendorPayment) {
        this.vendorPayment = vendorPayment;
    }
}
