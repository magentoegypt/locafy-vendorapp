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

package magentoegypt.locafy.vendor_notification.app;

import android.content.Context;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import magentoegypt.locafy.R;
import magentoegypt.locafy.navigation_drawer.models.NavigationModel;

import org.acra.ACRA;
import org.acra.ACRAConfiguration;
import org.acra.ACRAConfigurationException;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by developer on 17/5/16.
 */
//@ReportsCrashes(formKey = "", // will not be used
//        mailTo = "abhishekdubey@cedcoss.com",
//        mode = ReportingInteractionMode.TOAST
//        resToastText = R.string.crash_toast_text
//)


public class MyApplication extends MultiDexApplication {
    public static final String TAG = MyApplication.class
            .getSimpleName();

    private static MyApplication mInstance;

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public static List<NavigationModel> navigationItemList = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        final ACRAConfiguration config = ACRA.getNewDefaultConfig(this);
        try {
            config.setMode(ReportingInteractionMode.TOAST);
            config.setMailTo("sajidnawaz993@gmail.com");
            config.setResToastText(R.string.crash_toast_text);
        } catch (ACRAConfigurationException e) {
            e.printStackTrace();
        }
        ACRA.init(this);
        ACRA.setConfig(config);
        mInstance = this;

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
