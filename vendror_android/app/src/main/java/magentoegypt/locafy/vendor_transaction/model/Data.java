package magentoegypt.locafy.vendor_transaction.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("payment_detail")
    @Expose
    private List<PaymentDetailDataModel> paymentDetail = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<PaymentDetailDataModel> getPaymentDetail() {
        return paymentDetail;
    }

    public void setPaymentDetail(List<PaymentDetailDataModel> paymentDetail) {
        this.paymentDetail = paymentDetail;
    }

}
