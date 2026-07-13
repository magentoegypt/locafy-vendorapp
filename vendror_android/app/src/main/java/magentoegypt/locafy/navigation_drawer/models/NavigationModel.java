package magentoegypt.locafy.navigation_drawer.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NavigationModel {
    @SerializedName("parent_key")
    String ParentKey;

    @SerializedName("parent_value")
    String parentValue;

    @SerializedName("icon_unicode")
    String iconUnicode;

    @SerializedName("childs")
    List<NavigationChild> children;

    public NavigationModel(String parentKey, String parentValue, String iconUnicode, List<NavigationChild> children) {
        ParentKey = parentKey;
        this.parentValue = parentValue;
        this.iconUnicode = iconUnicode;
        this.children = children;
    }

    public String getParentKey() {
        return ParentKey;
    }

    public String getParentValue() {
        return parentValue;
    }

    public String getIconUnicode() {
        return iconUnicode;
    }

    public List<NavigationChild> getChildren() {
        return children;
    }

    @Override
    public String toString() {
        return "NavigationModel{" +
                "ParentKey='" + ParentKey + '\'' +
                ", parentValue='" + parentValue + '\'' +
                ", iconUnicode='" + iconUnicode + '\'' +
                ", children=" + children +
                '}';
    }
}




