package magentoegypt.locafy.vendor_transaction.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PaymentDetailDataModel {
    @SerializedName("vendorname")
    @Expose
    private String vendorname;
    @SerializedName("paymentmethod")
    @Expose
    private String paymentmethod;
    @SerializedName("paymentdetail")
    @Expose
    private String paymentdetail;
    @SerializedName("order_detail")
    @Expose
    private List<OrderDetail> orderDetail = null;
    @SerializedName("transactionId")
    @Expose
    private String transactionId;
    @SerializedName("createdat")
    @Expose
    private String createdat;
    @SerializedName("transactionmode")
    @Expose
    private String transactionmode;
    @SerializedName("transactiontype")
    @Expose
    private String transactiontype;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("net_amount")
    @Expose
    private String netAmount;
    @SerializedName("adjustment_amount")
    @Expose
    private String adjustmentAmount;
    @SerializedName("notes")
    @Expose
    private String notes;

    public String getVendorname() {
        return vendorname;
    }

    public void setVendorname(String vendorname) {
        this.vendorname = vendorname;
    }

    public String getPaymentmethod() {
        return paymentmethod;
    }

    public void setPaymentmethod(String paymentmethod) {
        this.paymentmethod = paymentmethod;
    }

    public String getPaymentdetail() {
        return paymentdetail;
    }

    public void setPaymentdetail(String paymentdetail) {
        this.paymentdetail = paymentdetail;
    }

    public List<OrderDetail> getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(List<OrderDetail> orderDetail) {
        this.orderDetail = orderDetail;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getCreatedat() {
        return createdat;
    }

    public void setCreatedat(String createdat) {
        this.createdat = createdat;
    }

    public String getTransactionmode() {
        return transactionmode;
    }

    public void setTransactionmode(String transactionmode) {
        this.transactionmode = transactionmode;
    }

    public String getTransactiontype() {
        return transactiontype;
    }

    public void setTransactiontype(String transactiontype) {
        this.transactiontype = transactiontype;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(String netAmount) {
        this.netAmount = netAmount;
    }

    public String getAdjustmentAmount() {
        return adjustmentAmount;
    }

    public void setAdjustmentAmount(String adjustmentAmount) {
        this.adjustmentAmount = adjustmentAmount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
