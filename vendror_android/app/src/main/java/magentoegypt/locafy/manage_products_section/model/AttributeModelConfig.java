package magentoegypt.locafy.manage_products_section.model;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class AttributeModelConfig {

    private String title;
    private String attributeCode;
    private String attributeId;
    private String attributeLabel;
    private List<AttributeModelConfig> attributeArray = new ArrayList<>();
    private boolean isChecked;
    private boolean isEnable;
    private boolean isUserCustom;
    private String label;
    private String value;
    private String oldprice;
    private String price;
    private String quantity;
    private Bitmap image;
    private String imageUrl;
    private String namelabel;
    private String skulabel;
    private String attributeWithValue;
    private String thirdtabImageOption;
    private int thirdtabImageAttributeIndex;
    private String thirdtabPriceOption;
    private int thirdtabPriceAttributeIndex;
    private String thirdtabQuantityOption;
    private int thirdtabQuantityAttributeIndex;
    private String assicoiatProductSku;
    private String assicoiatProductId;
    private String oldweight;
    private String weight;
    private String defaultimage;

    private List<String> attributeIdsArr = new ArrayList<>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAttributeCode() {
        return attributeCode;
    }

    public void setAttributeCode(String attributeCode) {
        this.attributeCode = attributeCode;
    }

    public String getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(String attributeId) {
        this.attributeId = attributeId;
    }

    public String getAttributeLabel() {
        return attributeLabel;
    }

    public void setAttributeLabel(String attributeLabel) {
        this.attributeLabel = attributeLabel;
    }

    public List<AttributeModelConfig> getAttributeArray() {
        return attributeArray;
    }

    public void setAttributeArray(List<AttributeModelConfig> attributeArray) {
        this.attributeArray = attributeArray;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isUserCustom() {
        return isUserCustom;
    }

    public void setUserCustom(boolean userCustom) {
        isUserCustom = userCustom;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPrice() {
        if(price == null || price.isEmpty() || price.equalsIgnoreCase("null"))
            return "0";
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        if(quantity == null || quantity.isEmpty() || quantity.equalsIgnoreCase("null"))
            return "0";
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getNamelabel() {
        return namelabel;
    }

    public void setNamelabel(String namelabel) {
        this.namelabel = namelabel;
    }

    public String getSkulabel() {
        return skulabel;
    }

    public void setSkulabel(String skulabel) {
        this.skulabel = skulabel;
    }

    public String getAttributeWithValue() {
        return attributeWithValue;
    }

    public void setAttributeWithValue(String attributeWithValue) {
        this.attributeWithValue = attributeWithValue;
    }

    public String getThirdtabImageOption() {
        if(thirdtabImageOption == null || thirdtabImageOption.isEmpty())
            return "Skip";
        return thirdtabImageOption;
    }

    public void setThirdtabImageOption(String thirdtabImageOption) {
        this.thirdtabImageOption = thirdtabImageOption;
    }

    public int getThirdtabImageAttributeIndex() {
        return thirdtabImageAttributeIndex;
    }

    public void setThirdtabImageAttributeIndex(int thirdtabImageAttributeIndex) {
        this.thirdtabImageAttributeIndex = thirdtabImageAttributeIndex;
    }

    public String getThirdtabPriceOption() {
        if(thirdtabPriceOption == null || thirdtabPriceOption.isEmpty())
            return "Skip";
        return thirdtabPriceOption;
    }

    public void setThirdtabPriceOption(String thirdtabPriceOption) {
        this.thirdtabPriceOption = thirdtabPriceOption;
    }

    public int getThirdtabPriceAttributeIndex() {
        return thirdtabPriceAttributeIndex;
    }

    public void setThirdtabPriceAttributeIndex(int thirdtabPriceAttributeIndex) {
        this.thirdtabPriceAttributeIndex = thirdtabPriceAttributeIndex;
    }

    public String getThirdtabQuantityOption() {
        if(thirdtabQuantityOption == null || thirdtabQuantityOption.isEmpty())
            return "Skip";
        return thirdtabQuantityOption;
    }

    public void setThirdtabQuantityOption(String thirdtabQuantityOption) {
        this.thirdtabQuantityOption = thirdtabQuantityOption;
    }

    public int getThirdtabQuantityAttributeIndex() {
        return thirdtabQuantityAttributeIndex;
    }

    public void setThirdtabQuantityAttributeIndex(int thirdtabQuantityAttributeIndex) {
        this.thirdtabQuantityAttributeIndex = thirdtabQuantityAttributeIndex;
    }

    public String getAssicoiatProductSku() {
        return assicoiatProductSku;
    }

    public void setAssicoiatProductSku(String assicoiatProductSku) {
        this.assicoiatProductSku = assicoiatProductSku;
    }

    public String getAssicoiatProductId() {
        if(assicoiatProductId == null)
        {
            return  "";
        }
        return assicoiatProductId;
    }

    public void setAssicoiatProductId(String assicoiatProductId) {
        this.assicoiatProductId = assicoiatProductId;
    }

    public String getWeight() {
        if(weight == null || weight.isEmpty() || weight.equalsIgnoreCase("0")  || weight.equalsIgnoreCase("null"))
            return "1";
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    // Getter and Setter
    public List<String> getAttributeIdsArr() {
        return attributeIdsArr;
    }

    public void setAttributeIdsArr(List<String> attributeIdsArr) {
        this.attributeIdsArr = attributeIdsArr;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public String getOldprice() {
        if(oldprice == null || oldprice.isEmpty() || oldprice.equalsIgnoreCase("null"))
            return "0";
        return oldprice;
    }

    public void setOldprice(String oldprice) {
        this.oldprice = oldprice;
    }

    public String getOldweight() {
        if(oldweight == null || oldweight.isEmpty() || oldweight.equalsIgnoreCase("0")  || oldweight.equalsIgnoreCase("null"))
            return "1";
        return oldweight;
    }

    public void setOldweight(String oldweight) {
        this.oldweight = oldweight;
    }

    public String getDefaultimage() {
        return defaultimage;
    }

    public void setDefaultimage(String defaultimage) {
        this.defaultimage = defaultimage;
    }
}
