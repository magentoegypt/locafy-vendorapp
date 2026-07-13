/*
 * Copyright/**
 *          * CedCommerce
 *           *
 *           * NOTICE OF LICENSE
 *           *
 *           * This source file is subject to the End User License Agreement (EULA)
 *           * that is bundled with this package in the file LICENSE.txt.
 *           * It is also available through the world-wide-web at this URL:
 *           * http://cedcommerce.com/license-agreement.txt
 *           *
 *           * @category  Ced
 *           * @package   MageNative
 *           * @author    CedCommerce Core Team <connect@cedcommerce.com >
 *           * @copyright Copyright CEDCOMMERCE (http://cedcommerce.com/)
 *           * @license      http://cedcommerce.com/license-agreement.txt
 *
 */
package magentoegypt.locafy.addons.vendor_member_ship_plans;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;

import magentoegypt.locafy.vendor_dashboard.Ced_MultiVendor_VendorDashboard;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;

public class Ced_WebAppInterface {
    private final Context mContext;
    private final Ced_MultiVendor_VendorSessionManagement session;

    public Ced_WebAppInterface(Context c) {
        mContext = c;
        session = new Ced_MultiVendor_VendorSessionManagement(mContext);
    }

    @JavascriptInterface
    public void ContinueShopping(String status) {
        if (status.equals("true")) {
            Log.i("ContinueShopping", "IN");
        }
    }

    @JavascriptInterface
    public void VContinueShopping(String status) {
        if (status.equals("true")) {
            Log.i("REpo", "VContinueShopping"+status);
            Intent intent = new Intent(mContext, Ced_MultiVendor_VendorDashboard.class);
            mContext.startActivity(intent);
        }
        else {
            Log.i("REpo", "VContinueShopping_else"+status);
        }
    }

    @JavascriptInterface
    public void orderSuccessEvent(String status) {
        if (status.equals("true")) {
            Log.i("Ordersuccess", "IN");
        }
    }

    @JavascriptInterface
    public void orderViewEvent(String orderid) {
        Log.i("OrderView", "IN");
    }
}