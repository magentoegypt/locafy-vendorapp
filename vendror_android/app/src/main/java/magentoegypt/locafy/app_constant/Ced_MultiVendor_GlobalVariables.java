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

package magentoegypt.locafy_constant;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.DatePicker;
import android.widget.EditText;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class Ced_MultiVendor_GlobalVariables {
    public static String Pagination = "5";
    public static boolean page10 = true;
    public static boolean page20 = true;
    public static boolean pageall = true;
    public static int mYear, mMonth, mDay;
    public static String date;
    public static int progress = 0;
    public static String noti = "0";
    public static String profile_count_val = "0";
    public static ArrayList<String> relatedproductids = new ArrayList<String>();
    public static ArrayList<String> upsellproductids = new ArrayList<String>();
    public static ArrayList<String> crossellproductids = new ArrayList<String>();
    public static ArrayList<String> groupedproductids = new ArrayList<String>();
    public static HashMap<String, ArrayList<String>> bundleproductids = new HashMap<String, ArrayList<String>>();
    public static ArrayList<String> valuefordisableconfig = new ArrayList<String>();
    public static ArrayList<String> Configurableproductids = new ArrayList<String>();
    public static JSONObject grpupedproductqty = new JSONObject();
    public static JSONObject config_attribute_value = new JSONObject();
    public static JSONObject configurable_data = new JSONObject();
    public static JSONObject bundle_selections_data = new JSONObject();

    public void clearallvalues() {
        relatedproductids.clear();
        upsellproductids.clear();
        crossellproductids.clear();
        groupedproductids.clear();
        Configurableproductids.clear();
        valuefordisableconfig.clear();
        bundleproductids.clear();
        grpupedproductqty = new JSONObject();
        config_attribute_value = new JSONObject();
        configurable_data = new JSONObject();
        bundle_selections_data = new JSONObject();
    }


    public static void getdate(Context context, final EditText editText) {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        final DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        editText.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                editText.setText("");
            }
        });
        datePickerDialog.setCancelable(false);
        datePickerDialog.show();
    }

}
