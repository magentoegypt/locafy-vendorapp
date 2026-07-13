/*
 *  /**
 *      * CedCommerce
 *      *
 *      * NOTICE OF LICENSE
 *      *
 *      * This source file is subject to the End User License Agreement (EULA)
 *      * that is bundled with this package in the file LICENSE.txt.
 *      * It is also available through the world-wide-web at this URL:
 *      * http://cedcommerce.com/license-agreement.txt
 *      *
 *      * @category  Ced
 *      * @package   MageNative
 *      * @author    CedCommerce Core Team <connect@cedcommerce.com >
 *      * @copyright Copyright CEDCOMMERCE (http://cedcommerce.com/)
 *      * @license      http://cedcommerce.com/license-agreement.txt
 *
 */

package magentoegypt.locafy.vendor_notification.fcm;

/*
public class MageNative_MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "MageNative_MyFirebaseIn";
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    Ced_MultiVendor_VendorSessionManagement vendorSessionManagement;
    String vendor_id;
    String url = "";
    */
/*private static final String TAG = "MyFirebaseIIDService";*//*


    @Override
    public void onCreate() {
        super.onCreate();
        vendorSessionManagement = new Ced_MultiVendor_VendorSessionManagement(this);
    }

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        vendorSessionManagement.DeviceToken(refreshedToken);
        Log.d(TAG, "Refreshed token: " + refreshedToken);

    }


}*/
