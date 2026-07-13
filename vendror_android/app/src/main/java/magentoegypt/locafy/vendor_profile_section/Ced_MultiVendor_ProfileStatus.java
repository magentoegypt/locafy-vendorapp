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

package magentoegypt.locafy.vendor_profile_section;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import magentoegypt.locafy.base_app.Ced_MultiVendor_FontSetting;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorSplash;
import magentoegypt.locafy_constant.Ced_MultiVendor_GlobalVariables;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import magentoegypt.locafy.R;

public class Ced_MultiVendor_ProfileStatus extends Ced_MultiVendor_NavigationActivity {
    Ced_MultiVendor_FontSetting fontSetting;
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    ProgressBar progress;
    TextView profilestatus;
    TextView tagprofile;
    CardView profilestatussection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_bar_color));
        }
        try {
            fontSetting = new Ced_MultiVendor_FontSetting();
            connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
            if (connectionDetector.isConnectingToInternet()) {
                ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
                getLayoutInflater().inflate(R.layout.ced_multivendor_activity_profile_status, content, true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);
                setname(session.getvendorname());
                setprofilepic(session.getvendorpic());
                changetitle("Profile Status");
                progress = findViewById(R.id.MultiVendor_progress);
                profilestatussection = findViewById(R.id.MultiVendor_profilestatussection);
                profilestatus = findViewById(R.id.MultiVendor_profilestatus);
                tagprofile = findViewById(R.id.MultiVendor_tagprofile);
                progress.setProgress(Ced_MultiVendor_GlobalVariables.progress);
                profilestatus.setText(Ced_MultiVendor_GlobalVariables.progress + getString(R.string.completed));
                profilestatussection.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent profile = new Intent(Ced_MultiVendor_ProfileStatus.this, Ced_MultiVendor_VendorProfile.class);
                        profile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(profile);
                        overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                        finish();
                    }
                });
                fontSetting.setFontforTextviews(profilestatus, "Roboto-Medium.ttf", getApplicationContext());
                fontSetting.setFontforTextviews(tagprofile, "Roboto-Medium.ttf", getApplicationContext());
            } else {
                Intent nointernet = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
                nointernet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                nointernet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(nointernet);
                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
            }
        } catch (Exception e) {
            Intent main = new Intent(getApplicationContext(), Ced_MultiVendor_VendorSplash.class);
            main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(main);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }


    }

    @Override
    protected void onResume() {
        Ced_MultiVendor_ConnectionDetector connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        if (connectionDetector.isConnectingToInternet()) {
            setname(session.getvendorname());
            setprofilepic(session.getvendorpic());
            changetitle("Profile Status");
            //      invalidateOptionsMenu();
            super.onResume();

        } else {
            Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
            super.onResume();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}