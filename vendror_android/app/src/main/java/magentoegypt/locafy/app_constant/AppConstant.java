package magentoegypt.locafy_constant;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatTextView;

import magentoegypt.locafy.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;

/**
 * Created by cedcoss on 3/1/19.
 */

public class AppConstant {
    public static String KEY_data = "data";
    public static String KEY_success = "success";
    public static String KEY_vendor_attributes = "vendor_attributes";
    public static String KEY_is_required = "is_required";
    public static String KEY_field_name = "field_name";
    public static String KEY_type = "type";
    public static String KEY_field_to_post = "field_to_post";
    public static String KEY_saved_value = "saved_value";
    public static String KEY_options = "options";
    public static String KEY_label = "label";
    public static String KEY_value = "value";
    public static ImageView pictureField = null;
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);


    public static Date from_date;
    public static HashMap<String, String> statusMap = new HashMap<>();
    public static ArrayList<String> statusList = new ArrayList<>();
    public static HashMap<String, String> signatureMap = new HashMap<>();
    public static ArrayList<String> signatureList = new ArrayList<>();
    public static HashMap<String, String> departmentMap = new HashMap<>();
    public static ArrayList<String> departmentList = new ArrayList<>();
    public static HashMap<String, String> priorityMap = new HashMap<>();
    public static ArrayList<String> priorityList = new ArrayList<>();
    public static HashMap<String, String> customerMap = new HashMap<>();
    public static ArrayList<String> customerList = new ArrayList<>();

    public static SimpleDateFormat time_format_hh_mm_ss = new SimpleDateFormat("HH:mm:ss", Locale.US);
    Timer timer;

    public static void setDateFrom(final Context context, final View edit_view) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, (view, year, monthOfYear, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            if (edit_view instanceof EditText) {
                ((EditText) edit_view).setText(simpleDateFormat.format(calendar.getTime()));
                from_date = calendar.getTime();
            }

            if (edit_view instanceof TextView) {
                ((TextView) edit_view).setText(simpleDateFormat.format(calendar.getTime()));
                from_date = calendar.getTime();
            }

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
        DatePicker datePicker = datePickerDialog.getDatePicker();
        datePicker.setMaxDate(new Date().getTime());
        // return calendar.getTime();
    }

    public static void setDateTo(final Context context, final View edit_view, EditText multiVendor_edt_start_date_creation) {
        if (!multiVendor_edt_start_date_creation.getText().toString().isEmpty()) {
            final Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(context, (view, year, monthOfYear, dayOfMonth) -> {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                if (edit_view instanceof EditText) {
                    ((EditText) edit_view).setText(simpleDateFormat.format(calendar.getTime()));
                }

            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

            datePickerDialog.show();
            DatePicker datePicker = datePickerDialog.getDatePicker();
            datePicker.setMinDate(from_date.getTime());
            datePicker.setMaxDate(new Date().getTime());
        } else {
            Toast.makeText(context,context.getString(R.string.please_fill_both_dates), Toast.LENGTH_SHORT).show();
        }
        // return calendar.getTime();
    }

    public static void setDateFromNoMax(final Context context, final View edit_view) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, (view, year, monthOfYear, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            if (edit_view instanceof EditText) {
                ((EditText) edit_view).setText(simpleDateFormat.format(calendar.getTime()));
                from_date = calendar.getTime();
            }

            if (edit_view instanceof TextView) {
                ((TextView) edit_view).setText(simpleDateFormat.format(calendar.getTime()));
                from_date = calendar.getTime();
            }

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
        //    DatePicker datePicker = datePickerDialog.getDatePicker();
        //  datePicker.setMaxDate(new Date().getTime());

        // return calendar.getTime();
    }

    public static void lockButton(final View view) {
        view.setEnabled(false);
        view.postDelayed(() -> view.setEnabled(true), 10000);
    }

    public static void getdate(Context context, final AppCompatTextView editText) {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        final DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                (view, year, monthOfYear, dayOfMonth) -> {
                    if (monthOfYear > 9) {
                        editText.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
                    else {
                        editText.setText(year + "-" + "0" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.setOnCancelListener(dialogInterface -> editText.setText(""));
        datePickerDialog.setCancelable(false);
        datePickerDialog.show();
    }
    public static void setDateAndTimeTo(final Context context, final View edit_view, EditText multiVendor_edt_start_date_creation) {
        if (!multiVendor_edt_start_date_creation.getText().toString().isEmpty()) {
            final Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(context, (view, year, monthOfYear, dayOfMonth) -> {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                if (edit_view instanceof EditText) {
                    if (monthOfYear > 9) {
                        ((EditText) edit_view).setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth + " " + time_format_hh_mm_ss.format(calendar.getTime()));
//                            editText.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth + " " + java.time.LocalTime.now());
                    } else {
                        ((EditText) edit_view).setText(year + "-" + "0" + (monthOfYear + 1) + "-" + dayOfMonth + " " + time_format_hh_mm_ss.format(calendar.getTime()));
//                            editText.setText(year + "-" + "0" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
                }

            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

            datePickerDialog.show();
            DatePicker datePicker = datePickerDialog.getDatePicker();
            datePicker.setMinDate(from_date.getTime());
            datePicker.setMaxDate(new Date().getTime());
        } else {
            Toast.makeText(context, context.getString(R.string.please_fill_both_dates), Toast.LENGTH_SHORT).show();
        }
        // return calendar.getTime();
    }

    public static void setDateAndTimeFrom(final Context context, final View edit_view) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, (view, year, monthOfYear, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            if (edit_view instanceof EditText) {
                if (monthOfYear > 9) {
                    ((EditText) edit_view).setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth + " " + time_format_hh_mm_ss.format(calendar.getTime()));
                } else {
                    ((EditText) edit_view).setText(year + "-" + "0" + (monthOfYear + 1) + "-" + dayOfMonth + " " + time_format_hh_mm_ss.format(calendar.getTime()));
                }
                from_date = calendar.getTime();
            }

            if (edit_view instanceof TextView) {
                if (monthOfYear > 9) {
                    ((EditText) edit_view).setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth + " " + time_format_hh_mm_ss.format(calendar.getTime()));
                } else {
                    ((EditText) edit_view).setText(year + "-" + "0" + (monthOfYear + 1) + "-" + dayOfMonth + " " + time_format_hh_mm_ss.format(calendar.getTime()));
                }
                from_date = calendar.getTime();
            }

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
        DatePicker datePicker = datePickerDialog.getDatePicker();
        datePicker.setMaxDate(new Date().getTime());
        // return calendar.getTime();
    }

    public static SimpleDateFormat yyyy_mm_dd_format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    public static void setDateFrom_yyyymmdd(final Context context, final View edit_view) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, (view, year, monthOfYear, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            if (edit_view instanceof EditText) {
                ((EditText) edit_view).setText(yyyy_mm_dd_format.format(calendar.getTime()));
                from_date = calendar.getTime();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
        DatePicker datePicker = datePickerDialog.getDatePicker();
        datePicker.setMaxDate(new Date().getTime());
        // return calendar.getTime();
    }

    public static void setDateTo_y_m_d(final Context context, final View edit_view, EditText multiVendor_edt_start_date_creation) {
        if (!multiVendor_edt_start_date_creation.getText().toString().isEmpty()) {
            final Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(context, (view, year, monthOfYear, dayOfMonth) -> {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                if (edit_view instanceof EditText) {
                    ((EditText) edit_view).setText(yyyy_mm_dd_format.format(calendar.getTime()));
                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
            DatePicker datePicker = datePickerDialog.getDatePicker();
            datePicker.setMinDate(from_date.getTime());
            datePicker.setMaxDate(new Date().getTime());
        } else {
            Toast.makeText(context,context.getString(R.string.please_fill_both_dates), Toast.LENGTH_SHORT).show();
        }
        // return calendar.getTime();
    }
}


