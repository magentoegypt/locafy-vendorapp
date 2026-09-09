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

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
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
        applySystemBarInsets();

    }

    /**
     * Android 16 enforces edge-to-edge for apps targeting API 36 and ignores
     * windowOptOutEdgeToEdgeEnforcement, so without this every screen would draw
     * underneath the status and navigation bars. Padding the content view by the
     * system bar insets once, application-wide, keeps the existing layouts intact,
     * and painting the strips with status_bar_color reproduces the bar colour the
     * theme used to get from colorPrimaryDark.
     */
    private void applySystemBarInsets() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
                final View content = activity.findViewById(android.R.id.content);
                if (content == null) {
                    return;
                }
                content.setBackgroundColor(ContextCompat.getColor(activity, R.color.status_bar_color));
                ViewCompat.setOnApplyWindowInsetsListener(content, (view, windowInsets) -> {
                    Insets bars = windowInsets.getInsets(
                            WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout());
                    view.setPadding(bars.left, bars.top, bars.right, bars.bottom);
                    return WindowInsetsCompat.CONSUMED;
                });
            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {
            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {
            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {
            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {
            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {
            }
        });
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
