package magentoegypt.locafy.addons

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import magentoegypt.locafy.R
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection
import magentoegypt.locafy.databinding.ActivityAdvanceReportBinding
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity

class AdvanceReport : Ced_MultiVendor_NavigationActivity() {
    var connectionDetector: Ced_MultiVendor_ConnectionDetector? = null

    lateinit var binding: ActivityAdvanceReportBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
/*
        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = this.resources.getColor(R.color.status_bar_color)
        }*/


        connectionDetector = Ced_MultiVendor_ConnectionDetector(applicationContext)
        if (connectionDetector!!.isConnectingToInternet) {
            val content = findViewById<ViewGroup>(R.id.MultiVendor_frame_container)
            layoutInflater.inflate(R.layout.activity_advance_report, content, true)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.backbutton)

        } else {

            val nointernet =
                Intent(applicationContext, Ced_MultiVendor_NoInternetconnection::class.java)
            nointernet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            nointernet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(nointernet)
            overridePendingTransition(
                R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out
            )
        }

    }
}