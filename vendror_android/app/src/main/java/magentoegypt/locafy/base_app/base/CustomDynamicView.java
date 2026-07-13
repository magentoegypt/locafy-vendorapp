package magentoegypt.locafy.base_app.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import magentoegypt.locafy.R;
import magentoegypt.locafy.vendor_notification.app.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CustomDynamicView {
    JSONArray filedArray;
    List<HashMap<String, Object>> customViews;
    View view;
    LayoutInflater li;
    Activity activity;
    HashMap<String, Object> params = new HashMap<>();
    LinearLayout layout;
    public ProgressBar progressBar;


    public CustomDynamicView(Activity activity, JSONArray filedArray, LinearLayout layout) {
        this.filedArray = filedArray;
        customViews = new ArrayList<>();
        this.activity = activity;
        this.layout = layout;
        try {
            bindViews();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void bindViews() throws JSONException {
        li = (LayoutInflater) MyApplication.getInstance().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < filedArray.length(); i++) {
            HashMap<String, Object> map = new HashMap<>();
            JSONObject productObj = filedArray.getJSONObject(i);
            String type = productObj.getString("type");
            if(productObj.getString("name").equalsIgnoreCase("email"))
                continue;
            if (productObj.has("is_required"))
                map.put("isRequired", productObj.getBoolean("is_required"));
            else map.put("isRequired", false);
            map.put("type", productObj.getString("type"));
            map.put("key", productObj.getString("name"));
            map.put("label", productObj.has("label") ? productObj.getString("label") : "");

            params.put(productObj.getString("name"), productObj.has("value") ? productObj.getString("value") : "");

            switch (type) {
                case "hidden": {

                }
                break;
                case "date": {
                    view = li.inflate(R.layout.inflater_textview, null, false);

                    TextView label = view.findViewById(R.id.dynamic_label);
                    label.setText(productObj.getString("label"));

                    TextView value = view.findViewById(R.id.dynamic_tv_value);
                    if (productObj.has("value"))
                        value.setText(productObj.getString("value"));

                    map.put("view", view);
                    map.put("nestedView", value);
                    customViews.add(map);

                    value.setOnClickListener(v -> {
                        showDateDialog(value);
                    });


                }
                break;
                case "textfield":
                case "textarea": {
                    view = li.inflate(R.layout.inflater_edittext, null, false);
                    TextView label = view.findViewById(R.id.dynamic_label);
                    label.setText(productObj.getString("label"));

                    EditText value = view.findViewById(R.id.dynamic_tv_value);
                    if (productObj.has("value"))
                        value.setText(productObj.getString("value"));

                    map.put("view", view);
                    map.put("nestedView", value);
                    customViews.add(map);


                }
                break;
                case "number": {
                    view = li.inflate(R.layout.inflater_edittext, null, false);
                    TextView label = view.findViewById(R.id.dynamic_label);
                    label.setText(productObj.getString("label"));

                    EditText value = view.findViewById(R.id.dynamic_tv_value);
                    value.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    if (productObj.has("value"))
                        value.setText(productObj.getString("value"));

                    map.put("view", view);
                    map.put("nestedView", value);
                    customViews.add(map);
                }
                break;
                case "select": {
                    view = li.inflate(R.layout.inflater_spinner, null, false);

                    TextView label = view.findViewById(R.id.dynamic_label);
                    label.setText(productObj.getString("label"));

                    if (productObj.has("values")) {
                        map.put("view", view);
                        customViews.add(map);
                        Spinner spinner = view.findViewById(R.id.spinnerSelectAndSell);
                        map.put("nestedView", spinner);
                        JSONArray options = productObj.getJSONArray("values");
                        String[] spinnerItems = new String[options.length()];
                        for (int j = 0; j < options.length(); j++) {
                            spinnerItems[j] = options.getJSONObject(j).getString("label");
                        }


                        int _preSelectedValue = -1;

                        if (productObj.has("value")) {
                            for (int k = 0; k < options.length(); k++) {
                                String value = "" + options.getJSONObject(k).getInt("value");
                                Log.d("_preSelectedValue", ": " + value);
                                if (value.equalsIgnoreCase(productObj.getString("value"))) {
                                    _preSelectedValue = k;
                                    Log.d("_preSelectedValue", ": " + _preSelectedValue);
                                }
                            }
                        }

                        binsSpinnerData(spinner, spinnerItems, _preSelectedValue);
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                //map.put("spinnerValue", parent.getAdapter().getItem(position));
                                try {
                                    map.put("spinnerValue", "" + options.getJSONObject(position).getInt("value"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                    }
                }
                break;

            }
        }


        for (int i = 0; i < getCustomViewMap().size(); i++) {
            HashMap<String, Object> viewMap = getCustomViewMap().get(i);
            layout.addView((View) viewMap.get("view"));
        }
    }

    private void showDateDialog(TextView tv) {

        LayoutInflater inflater = (LayoutInflater) MyApplication.getInstance().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.date_picker, null, false);

        final DatePicker myDatePicker = (DatePicker) view.findViewById(R.id.myDatePicker);

        myDatePicker.setCalendarViewShown(false);
        myDatePicker.setMinDate(System.currentTimeMillis());

        new AlertDialog.Builder(activity).setView(view)
                .setTitle("Set Date")
                .setPositiveButton("Go", (dialog, id) -> {
                    int month = myDatePicker.getMonth() + 1;
                    int day = myDatePicker.getDayOfMonth();
                    int year = myDatePicker.getYear();
                    showToast(month + "/" + day + "/" + year);
                    tv.setText(MessageFormat.format("{0}/{1}/{2}", month, day, year));
                    dialog.cancel();

                }).show();
    }

    private void showToast(String s) {
        //showSnackBar(s);

        Toast.makeText(MyApplication.getInstance(), s, Toast.LENGTH_SHORT).show();
    }

    private void binsSpinnerData(Spinner viewSpinner, String[] options, int _preSelectedValue) throws JSONException {
        Log.d("_preSelectedValue", "binsSpinnerData: ");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, R.layout.spinner_textview, options);
        viewSpinner.setAdapter(adapter);
        if (_preSelectedValue != -1) {
            viewSpinner.setSelection(_preSelectedValue, true);
        }

    }

    protected List<HashMap<String, Object>> getCustomViewMap() {
        return customViews;
    }

    public Boolean validateForm() {
        for (int i = 0; i < customViews.size(); i++) {
            Map<String, Object> map = customViews.get(i);
            boolean isRequired = (boolean) map.get("isRequired");

            View view = (View) customViews.get(i).get("nestedView");
            if (view instanceof TextView) {
                Log.d("instance " + i, "instance : TextView");
                TextView textView = (TextView) view;
                String value = Objects.requireNonNull(textView).getText().toString();
                if (isRequired)
                    if (TextUtils.isEmpty(value)) {
                        showToast((String) customViews.get(i).get("label") + " is required");
                        textView.setError("required !!");
                        return false;
                    }

                params.put((String) customViews.get(i).get("key"), value);

            } else if (view instanceof EditText) {
                Log.d("instance " + i, "instance : EditText");
                EditText textView = (EditText) view;
                String value = Objects.requireNonNull(textView).getText().toString();
                if (isRequired)
                    if (TextUtils.isEmpty(value)) {
                        showToast((String) customViews.get(i).get("label") + " is required");
                        textView.setError("required !!");
                        return false;
                    }
                String key = (String) customViews.get(i).get("key");
                params.put(key, value);


            } else if (view instanceof Spinner) {
                String spinnerText = (String) customViews.get(i).get("spinnerValue");
                if (isRequired)
                    if (Objects.requireNonNull(spinnerText).equalsIgnoreCase("-select-") || TextUtils.isEmpty(spinnerText)) {
                        showToast("select " + (String) customViews.get(i).get("label"));
                        return false;
                    }
                params.put((String) customViews.get(i).get("key"), spinnerText);

            } else {
                return true;
            }

        }
        return true;
    }


    public HashMap<String, Object> paramMap() {
        return params;
    }

}
