package magentoegypt.locafy.vendor_product_review_and_rating.ManageRating;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RatingItemModel {

    @SerializedName("rating_id")
    @Expose
    private String rating_id;

    @SerializedName("rating_code")
    @Expose
    private String rating_code;

    @SerializedName("sort_order")
    @Expose
    private String sort_order;

    @SerializedName("is_active")
    @Expose
    private String is_active;

    public String getRating_id() {
        return rating_id;
    }

    public String getRating_code() {
        return rating_code;
    }

    public String getSort_order() {
        return sort_order;
    }

    public String getIs_active() {
        return is_active;
    }
}
