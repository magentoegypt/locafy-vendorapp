package magentoegypt.locafy.navigation_drawer.models;

public class NavigationChild {

    String key;
    String value;
    String icon;

    public NavigationChild(String key, String value, String icon) {
        this.key = key;
        this.value = value;
        this.icon = icon;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public String getIcon() {
        return icon;
    }


}
