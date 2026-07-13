package magentoegypt.locafy.addons.advance_Report.appBase

import android.app.AlertDialog
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import magentoegypt.locafy.R


open class FilterBaseFragment : Fragment() {

    protected fun selectDate(textInputLayout: TextInputEditText) {
        val inflater = this.layoutInflater
        val view: View = inflater.inflate(R.layout.date_picker, null, false)
        val myDatePicker: DatePicker = view.findViewById(R.id.myDatePicker)
        myDatePicker.calendarViewShown = false
        AlertDialog.Builder(requireActivity()).setView(view)
            .setTitle(getString(R.string.select_date))
            .setPositiveButton(getString(R.string.go)) { dialog, _ ->
                val month = myDatePicker.month + 1
                val day = myDatePicker.dayOfMonth
                val year = myDatePicker.year
                val date = "$month/$day/$year"
                showToast("$month/$day/$year")
                dialog.cancel()
            }.show()
    }

    protected fun showToast(msg: String) {
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show()
    }


}