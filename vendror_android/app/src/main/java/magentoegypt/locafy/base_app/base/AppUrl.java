package magentoegypt.locafy.base_app.base;

import magentoegypt.locafy.vendor_notification.app.MyApplication;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;

public class AppUrl {
    public static String BASE_URL = "https://locafy.market/";
  //  public static String BASE_URL = "https://brassandwood.org/";
 //   public static String BASE_URL = "https://demo2.cedcommerce.com/magento/client/hassan/pub/";


    public static String getBaseUrl() {
        Ced_MultiVendor_VendorSessionManagement ced_sessionManagement = new Ced_MultiVendor_VendorSessionManagement(MyApplication.getInstance());
        if (ced_sessionManagement.getBase_Url() == null)
            return BASE_URL;
        else return ced_sessionManagement.getBase_Url();
        //return BASE_URL;
    }

}
