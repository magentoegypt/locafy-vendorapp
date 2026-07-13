package magentoegypt.locafy.vendor_transaction.model.listing;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("transactiondata")
    @Expose
    private List<TransactionDataModel> transactiondata = null;
    @SerializedName("payment_msg")
    @Expose
    private String paymentMsg;
    @SerializedName("count")
    @Expose
    private String count;
    @SerializedName("pendingTransfer")
    @Expose
    private String pendingTransfer;
    @SerializedName("pendingAmount")
    @Expose
    private String pendingAmount;
    @SerializedName("paidAmount")
    @Expose
    private String paidAmount;
    @SerializedName("earnedAmount")
    @Expose
    private String earnedAmount;
    @SerializedName("canceledAmount")
    @Expose
    private String canceledAmount;
    @SerializedName("refundableAmount")
    @Expose
    private String refundableAmount;
    @SerializedName("refundedAmount")
    @Expose
    private String refundedAmount;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<TransactionDataModel> getTransactiondata() {
        return transactiondata;
    }

    public void setTransactiondata(List<TransactionDataModel> transactiondata) {
        this.transactiondata = transactiondata;
    }

    public String getPaymentMsg() {
        return paymentMsg;
    }

    public void setPaymentMsg(String paymentMsg) {
        this.paymentMsg = paymentMsg;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getPendingTransfer() {
        return pendingTransfer;
    }

    public void setPendingTransfer(String pendingTransfer) {
        this.pendingTransfer = pendingTransfer;
    }

    public String getPendingAmount() {
        return pendingAmount;
    }

    public void setPendingAmount(String pendingAmount) {
        this.pendingAmount = pendingAmount;
    }

    public String getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(String paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getEarnedAmount() {
        return earnedAmount;
    }

    public void setEarnedAmount(String earnedAmount) {
        this.earnedAmount = earnedAmount;
    }

    public String getCanceledAmount() {
        return canceledAmount;
    }

    public void setCanceledAmount(String canceledAmount) {
        this.canceledAmount = canceledAmount;
    }

    public String getRefundableAmount() {
        return refundableAmount;
    }

    public void setRefundableAmount(String refundableAmount) {
        this.refundableAmount = refundableAmount;
    }

    public String getRefundedAmount() {
        return refundedAmount;
    }

    public void setRefundedAmount(String refundedAmount) {
        this.refundedAmount = refundedAmount;
    }
}
