package magentoegypt.locafy.addons.vendor_member_ship_plans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MembershipPlan_ItemDataModel {

    @SerializedName("membership_id")
    @Expose
    private final String membership_id;

    @SerializedName("membership_name")
    @Expose
    private final String membership_name;

    @SerializedName("membership_image")
    @Expose
    private final String membership_image;

    @SerializedName("price")
    @Expose
    private final String price;

    @SerializedName("special_price")
    @Expose
    private final String special_price;

    @SerializedName("already_subscribed")
    @Expose
    private final String already_subscribed;

    @SerializedName("show_addtocart")
    @Expose
    private final String show_addtocart;

    public MembershipPlan_ItemDataModel(String membership_id, String membership_name, String membership_image, String price,
                                        String special_price, String already_subscribed, String show_addtocart) {
        this.membership_id = membership_id;
        this.membership_name = membership_name;
        this.membership_image = membership_image;
        this.price = price;
        this.special_price = special_price;
        this.already_subscribed = already_subscribed;
        this.show_addtocart = show_addtocart;
    }

    public String getShow_addtocart() {
        return show_addtocart;
    }

    public String getAlready_subscribed() {
        return already_subscribed;
    }

    public String getSpecial_price() {
        return special_price;
    }

    public String getPrice() {
        return price;
    }

    public String getMembership_image() {
        return membership_image!=null?membership_image:"";
    }

    public String getMembership_name() {
        return membership_name;
    }

    public String getMembership_id() {
        return membership_id;
    }
}