package magentoegypt.locafy.addons.advance_Report.appBase

import android.app.ProgressDialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import magentoegypt.locafy.R


class AppUtils {

    companion object {
        private var progressDialog: ProgressDialog? = null

        fun showRequestDialog(activity: Context) {
            try {
                progressDialog = ProgressDialog.show(activity, null, null, false, true)
                progressDialog!!.window!!
                    .setBackgroundDrawable(
                        ColorDrawable(
                            activity.resources
                                .getColor(R.color.transparent)
                        )
                    )
                progressDialog!!.setContentView(R.layout.progress_bar)
                progressDialog!!.show()

            } catch (e: Exception) {
                e.printStackTrace()
            }


        }


        fun hideDialog() {
            try {
                if (progressDialog != null && progressDialog!!.isShowing) {
                    progressDialog!!.dismiss()
                    progressDialog = null
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


}