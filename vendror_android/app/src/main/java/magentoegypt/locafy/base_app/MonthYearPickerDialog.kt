package magentoegypt.locafy.base_app

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import magentoegypt.locafy.R
import magentoegypt.locafy.databinding.DialogMonthYearPickerBinding
import java.util.Calendar
import java.util.Date

class MonthYearPickerDialog(val date: Date = Date()) : DialogFragment() {

    companion object {
        private const val MIN_YEAR = 1970
        private const val MAX_YEAR = 2070
    }

    private lateinit var binding: DialogMonthYearPickerBinding

    private var listener: DatePickerDialog.OnDateSetListener? = null

    fun setListener(listener: DatePickerDialog.OnDateSetListener?) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogMonthYearPickerBinding.inflate(requireActivity().layoutInflater)
        val cal: Calendar = Calendar.getInstance().apply { time = date }

        binding.pickerMonth.run {
            minValue = 0
            maxValue = 11
            value = cal.get(Calendar.MONTH)
            displayedValues = arrayOf(getString(R.string.jan),getString(R.string.feb),getString(R.string.mar),
                getString(R.string.apr),getString(R.string.may),getString(R.string.jun)
                ,getString(R.string.jul), getString(R.string.aug),getString(R.string.sep),
                getString(R.string.oct),getString(R.string.nov),getString(R.string.dec))
        }

        binding.pickerYear.run {
            val year = cal.get(Calendar.YEAR)
            minValue = MIN_YEAR
            maxValue = MAX_YEAR
            value = year
        }

        return AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.alert_name))
            .setIcon(R.mipmap.ic_launcher)
            .setView(binding.root)
            .setPositiveButton(getString(R.string.ok)) { _, _ -> listener?.onDateSet(null, binding.pickerYear.value, binding.pickerMonth.value, 1) }
            .setNegativeButton(getString(R.string.cancel)) { _, _ -> dialog?.cancel() }
            .create()
    }
}