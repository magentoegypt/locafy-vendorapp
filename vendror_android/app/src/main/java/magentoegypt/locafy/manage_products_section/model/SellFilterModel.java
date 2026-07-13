
package magentoegypt.locafy.manage_products_section.model;

import java.util.List;
import com.google.gson.annotations.Expose;


@SuppressWarnings("unused")
public class SellFilterModel {

    @Expose
    private List<Status> status;
    @Expose
    private List<Website> website;

    public List<Status> getStatus() {
        return status;
    }

    public void setStatus(List<Status> status) {
        this.status = status;
    }

    public List<Website> getWebsite() {
        return website;
    }

    public void setWebsite(List<Website> website) {
        this.website = website;
    }

}
