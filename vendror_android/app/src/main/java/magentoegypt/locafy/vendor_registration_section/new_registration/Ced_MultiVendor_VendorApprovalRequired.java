/*
 *
 *   Copyright/* *
 *             * CedCommerce
 *             *
 *             * NOTICE OF LICENSE
 *             *
 *             * This source file is subject to the End User License Agreement (EULA)
 *             * that is bundled with this package in the file LICENSE.txt.
 *             * It is also available through the world-wide-web at this URL:
 *             * http://cedcommerce.com/license-agreement.txt
 *             *
 *             * @category  Ced
 *             * @package   MultiVendor
 *             * @author    CedCommerce Core Team <connect@cedcommerce.com >
 *             * @copyright Copyright CEDCOMMERCE (http://cedcommerce.com/)
 *             * @license   http://cedcommerce.com/license-agreement.txt
 *
 *
 *
 */

package magentoegypt.locafy.vendor_registration_section.new_registration;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import magentoegypt.locafy.R;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;


public class Ced_MultiVendor_VendorApprovalRequired extends Activity {
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    TextView vendorapplicationsection;
    TextView hellovendor;
    Ced_MultiVendor_VendorSessionManagement ced_multiVendor_vendorSessionManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ced_multiVendor_vendorSessionManagement = new Ced_MultiVendor_VendorSessionManagement(this);
        try {
            if (connectionDetector.isConnectingToInternet()) {
                setContentView(R.layout.ced_multivendor_activity_vendor_approval_required);
                vendorapplicationsection = (TextView) findViewById(R.id.MultiVendor_vendorapplicationsection);
                hellovendor = (TextView) findViewById(R.id.MultiVendor_hellovendor);
                Intent message = getIntent();
                vendorapplicationsection.setText(message.getStringExtra("message"));
                if (message.getStringExtra("firstname") != null && message.getStringExtra("lastname") != null) {
                    hellovendor.setText(getResources().getString(R.string.hellovendortext) + message.getStringExtra("firstname") + message.getStringExtra("lastname"));
                } else {
                    hellovendor.setText(getResources().getString(R.string.hellovendortext));
                }
            } else {
                Intent nointernet = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
                nointernet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                nointernet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(nointernet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        Ced_MultiVendor_ConnectionDetector connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        if (connectionDetector.isConnectingToInternet()) {

            super.onResume();
        } else {
            Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            super.onResume();
        }
    }

}