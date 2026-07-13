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

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import magentoegypt.locafy.vendor_dashboard.Ced_MultiVendor_VendorDashboard;
import magentoegypt.locafy.vendor_login_section.Ced_Multivendor_New_Login;
import magentoegypt.locafy.manage_orders.Ced_MultiVendor_Orderview;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;
import magentoegypt.locafy.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MageNative_MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMessageService";
    private NotificationUtils notificationUtils;


    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Ced_MultiVendor_VendorSessionManagement vendorSessionManagement = new Ced_MultiVendor_VendorSessionManagement(this);
        vendorSessionManagement.DeviceToken(s);
        Log.d(TAG, "Refreshed token: " + s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Ced_MultiVendor_VendorSessionManagement vendorSessionManagement = new Ced_MultiVendor_VendorSessionManagement(this);
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d("Notification_data", "Message data payload: " + remoteMessage.getData());
        }
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d("Notification_data", "Message Notification Body: " + remoteMessage.getNotification().getBody());
            String msgBody = remoteMessage.getNotification().getBody();
            Log.d("Notification_data", "Array" + msgBody);
        }
        String title;
        String mesg;
        String link_type;
        String link_id;
        Log.i("notification_test", remoteMessage.getData().toString());
       // try {
            title = remoteMessage.getData().get("title");
            mesg = remoteMessage.getData().get("message");
            String imageUri = remoteMessage.getData().get("image");
            String created_at = remoteMessage.getData().get("created_at");
            String vendor_id = remoteMessage.getData().get("vendor_id");
            link_type = remoteMessage.getData().get("link_type");
            link_id = remoteMessage.getData().get("link_id");
            if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                Log.e(TAG, "Title: " + title);
                Log.e(TAG, "message: " + mesg);
                Log.e(TAG, "image: " + imageUri);
                Log.e(TAG, "timestamp: " + created_at);
                Log.e(TAG, "link_type:" + link_type);
                Log.e(TAG, "link_id:" + link_id);
            }
            Intent resultIntent = null;
            if (link_type != null && link_type.equals("order")) {
                resultIntent = new Intent(getApplicationContext(), Ced_MultiVendor_Orderview.class);
                resultIntent.putExtra("order_id", link_id);
                showNotificationMessageWithBigImage(getApplicationContext(), title, mesg, created_at, resultIntent, imageUri);
            } else {
                if (vendorSessionManagement.isLoggedIn()) {
                    resultIntent = new Intent(this, Ced_MultiVendor_VendorDashboard.class);
                    resultIntent.putExtra("order_id", link_id);
                    showNotificationMessageWithBigImage(getApplicationContext(), title, mesg, created_at, resultIntent, imageUri);
                } else {
                    resultIntent = new Intent(this, Ced_Multivendor_New_Login.class);
                    resultIntent.putExtra("order_id", link_id);
                    showNotificationMessageWithBigImage(getApplicationContext(), title, mesg, created_at, resultIntent, imageUri);
                }
            }
//        }catch (Exception e){
//            Log.e("notification_exception", e.getMessage());
//        }
    }

    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }
}