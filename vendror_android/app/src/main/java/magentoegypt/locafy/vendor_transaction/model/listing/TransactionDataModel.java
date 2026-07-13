package magentoegypt.locafy.vendor_transaction.model.listing;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransactionDataModel {
    @SerializedName("payment_id")
    @Expose
    private String paymentId;



    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("transaction_id")
    @Expose
    private String transactionId;
    @SerializedName("payment_mode")
    @Expose
    private String paymentMode;
    @SerializedName("transaction_type")
    @Expose
    private String transactionType;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("adjustment_amount")
    @Expose
    private String adjustmentAmount;
    @SerializedName("net_amount")
    @Expose
    private String netAmount;
    @SerializedName("amount_desc")
    @Expose
    private String amountDesc;

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAdjustmentAmount() {
        return adjustmentAmount;
    }

    public void setAdjustmentAmount(String adjustmentAmount) {
        this.adjustmentAmount = adjustmentAmount;
    }

    public String getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(String netAmount) {
        this.netAmount = netAmount;
    }

    public String getAmountDesc() {
        return amountDesc;
    }


    public void setAmountDesc(String amountDesc) {
        this.amountDesc = amountDesc;
    }
}
