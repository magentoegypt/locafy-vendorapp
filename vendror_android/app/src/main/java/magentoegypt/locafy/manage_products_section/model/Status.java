
package magentoegypt.locafy.manage_products_section.model;


import com.google.gson.annotations.Expose;

@SuppressWarnings("unused")
public class Status {

    @Expose
    private String label;
    @Expose
    private Long value;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return label;
    }
}
