package magentoegypt.locafy.vendor_deals;

import static magentoegypt.locafy.base_app.UtilityMethods.getDealStatusValue;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import magentoegypt.locafy.base_app.Ced_MultiVendor_FontSetting;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_UnAuthourizedRequestError;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.R;
import magentoegypt.locafy_constant.AppConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class Ced_MultiVendor_Create_Deal extends Ced_MultiVendor_NavigationActivity {
    public EditText MultiVendor_edt_start_date, MultiVendor_edt_end_date;
    HashMap<String, String> postdata;
    String deal_create_url = "";
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    Ced_MultiVendor_FontSetting fontSetting;
    TextView product_id_title, product_name_title, deal_status_title, deal_from_title, deal_to_title;
    AppCompatEditText deal_price;
    AppCompatTextView product_name, product_id;
    Button back_btn, create_btn, update_btn;
    Spinner deal_status;
    String product_id_s, deal_id, product_name_s, deal_from_s, deal_to_s, deal_price_s, deal_status_s;
    Boolean select_date = false;
    private DatePickerDialog.OnDateSetListener datePickerDialog;
    private DatePickerDialog dialog;
    ArrayList<JSONObject> dealStatusList = new ArrayList<>();
    ArrayList<String> dealStatusLabel = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
        getLayoutInflater().inflate(R.layout.ced_multivendor_create_deal_settings, content, true);
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(Ced_MultiVendor_Create_Deal.this);
        fontSetting = new Ced_MultiVendor_FontSetting();
        postdata = new HashMap<>();
        product_id_title = findViewById(R.id.product_id_title);
        product_name_title = findViewById(R.id.product_name_title);
        deal_status_title = findViewById(R.id.deal_status_title);
        deal_from_title = findViewById(R.id.deal_from_title);
        deal_to_title = findViewById(R.id.deal_to_title);
        product_id = findViewById(R.id.product_id);
        product_name = findViewById(R.id.product_name);
        back_btn = findViewById(R.id.back_btn);
        update_btn = findViewById(R.id.update_btn);
        create_btn = findViewById(R.id.create_btn);
        deal_status = findViewById(R.id.deal_status);
        MultiVendor_edt_start_date = findViewById(R.id.deal_from_date);
        MultiVendor_edt_end_date = findViewById(R.id.deal_to_date);
        deal_price = findViewById(R.id.deal_price);
        Bundle extras = getIntent().getExtras();

        try {
            JSONArray createDealArrayStatus = new JSONArray(session.getCreateDealStatus());
            bindDealSpinnerData(createDealArrayStatus);
            Log.d("Amirkhan", "onCreate: " + createDealArrayStatus);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (Objects.equals(Objects.requireNonNull(extras).getString("navigate"), "create_deal")) {
            product_id.setText(extras.getString("product_id"));
            product_name.setText(extras.getString("product_name"));
        } else if ((Objects.equals(extras.getString("navigate"), "update_deal"))) {
            create_btn.setVisibility(View.GONE);
            update_btn.setVisibility(View.VISIBLE);
            product_id_title.setText(getString(R.string.product_id));
            product_id.setText(extras.getString("prod_id"));
            deal_id = extras.getString("deal_id");
            product_name.setText(extras.getString("product_name"));
            deal_price.setText(extras.getString("deal_price"));
            if (Objects.requireNonNull(extras.getString("deal_status")).toLowerCase().equals("enabled")) {
                deal_status.setSelection(1);
            } else if (Objects.requireNonNull(extras.getString("deal_status")).toLowerCase().equals("disabled")) {
                deal_status.setSelection(2);
            } else if (Objects.requireNonNull(extras.getString("deal_status")).toLowerCase().equals("expired")) {
                deal_status.setSelection(3);
            } else {
                deal_status.setSelection(0);
            }
            if (!Objects.equals(extras.getString("deal_to"), "")) {
                MultiVendor_edt_end_date.setText(extras.getString("deal_to"));
            }
            if (!Objects.equals(extras.getString("deal_from"), "")) {
                MultiVendor_edt_start_date.setText(extras.getString("deal_from"));
            }
        }
        back_btn.setOnClickListener(view -> {
            AppConstant.lockButton(view);
            onBackPressed();
        });

        MultiVendor_edt_start_date.setOnClickListener(view -> {
            select_date = true;
            showTruitonDatePickerDialog(view);
        });

        MultiVendor_edt_end_date.setOnClickListener(view -> {
            if (select_date) {
                showToDatePickerDialog(view);
            } else {
                Toast.makeText(Ced_MultiVendor_Create_Deal.this, R.string.please_fill_both_dates, Toast.LENGTH_SHORT).show();
            }
        });

        create_btn.setOnClickListener(view -> {
            AppConstant.lockButton(view);
            deal_create_url = session.getBase_Url() + "vdealapi/deal/create";
            view.startAnimation(AnimationUtils.loadAnimation(Ced_MultiVendor_Create_Deal.this, R.anim.click_effect));
            if (deal_status.getSelectedItemPosition() == 0) {
                Toast.makeText(Ced_MultiVendor_Create_Deal.this, "Please Select Deal Status.", Toast.LENGTH_SHORT).show();
            } else if (product_id.getText().toString().isEmpty()) {
                product_id.setError(getResources().getString(R.string.empty_field));
            } else if (product_name.getText().toString().isEmpty()) {
                product_name.setError(getResources().getString(R.string.empty_field));
            } else if (Objects.requireNonNull(deal_price.getText()).toString().isEmpty()) {
                Toast.makeText(Ced_MultiVendor_Create_Deal.this, R.string.deal_price_validation, Toast.LENGTH_SHORT).show();
            } else if (MultiVendor_edt_start_date.getText().toString().isEmpty()) {
                Toast.makeText(Ced_MultiVendor_Create_Deal.this, getResources().getString(R.string.dateinvalid), Toast.LENGTH_SHORT).show();
            } else if (MultiVendor_edt_end_date.getText().toString().isEmpty()) {
                Toast.makeText(Ced_MultiVendor_Create_Deal.this, getResources().getString(R.string.datetovalid), Toast.LENGTH_SHORT).show();
            } else {
                deal_status_s = getDealStatusValue(deal_status.getSelectedItem().toString(), dealStatusList);
                product_id_s = product_id.getText().toString();
                product_name_s = product_name.getText().toString();
                deal_from_s = MultiVendor_edt_start_date.getText().toString();
                deal_to_s = MultiVendor_edt_end_date.getText().toString();
                deal_price_s = deal_price.getText().toString();
                postdata.put("vendor_id", session.getVendorid());
                postdata.put("hashkey", session.getHahkey());
                postdata.put("product_id", product_id_s);
                postdata.put("product_name", product_name_s);
                postdata.put("deal_price", deal_price_s);
                postdata.put("start_date", deal_from_s);
                postdata.put("end_date", deal_to_s);
                postdata.put("status", deal_status_s);
                create_deal(postdata);
            }
        });
        update_btn.setOnClickListener(view -> {
            deal_create_url = session.getBase_Url() + "vdealapi/deal/update";
            view.startAnimation(AnimationUtils.loadAnimation(Ced_MultiVendor_Create_Deal.this, R.anim.click_effect));
            if (deal_status.getSelectedItemPosition() == 0) {
                Toast.makeText(Ced_MultiVendor_Create_Deal.this, "Please Select Deal Status.", Toast.LENGTH_SHORT).show();
            } else if (product_id.getText().toString().isEmpty()) {
                product_id.setError(getResources().getString(R.string.empty_field));
            } else if (product_name.getText().toString().isEmpty()) {
                product_name.setError(getResources().getString(R.string.empty_field));
            } else if (Objects.requireNonNull(deal_price.getText()).toString().isEmpty()) {
                Toast.makeText(Ced_MultiVendor_Create_Deal.this, R.string.deal_price_validation, Toast.LENGTH_SHORT).show();
            } else if (MultiVendor_edt_start_date.getText().toString().isEmpty()) {
                Toast.makeText(Ced_MultiVendor_Create_Deal.this, getResources().getString(R.string.dateinvalid), Toast.LENGTH_SHORT).show();
            } else if (MultiVendor_edt_start_date.getText().toString().isEmpty()) {
                Toast.makeText(Ced_MultiVendor_Create_Deal.this, getResources().getString(R.string.dateinvalid), Toast.LENGTH_SHORT).show();
            } else {
                deal_status_s = getDealStatusValue(deal_status.getSelectedItem().toString(), dealStatusList);
                product_id_s = product_id.getText().toString();
                product_name_s = product_name.getText().toString();
                deal_from_s = MultiVendor_edt_start_date.getText().toString();
                deal_to_s = MultiVendor_edt_end_date.getText().toString();
                deal_price_s = deal_price.getText().toString();
                postdata.put("vendor_id", session.getVendorid());
                postdata.put("hashkey", session.getHahkey());
                postdata.put("deal_id", deal_id);
                // postdata.put("product_name", product_name_s);
                postdata.put("deal_price", deal_price_s);
                postdata.put("start_date", deal_from_s);
                postdata.put("end_date", deal_to_s);
                postdata.put("status", deal_status_s);
                postdata.put("product_id", product_id_s);
                create_deal(postdata);
            }
        });
    }

    private void bindDealSpinnerData(JSONArray createDealArrayStatus) throws JSONException {
        dealStatusLabel.clear();
        dealStatusList.clear();

        dealStatusLabel.add(getString(R.string.select_status));
        dealStatusList.add(new JSONObject());

        for (int a = 0; a < createDealArrayStatus.length(); a++) {
            JSONObject jsonObject = createDealArrayStatus.getJSONObject(a);
            dealStatusList.add(jsonObject);
            dealStatusLabel.add(jsonObject.getString("value"));
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dealStatusLabel);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        deal_status.setAdapter(arrayAdapter);

    }

    public void create_deal(HashMap<String, String> object) {
        Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(output -> {
            if (functionalityList.getVendor_Deals()) {
                Log.i("9044_Response", "" + output);
                JSONObject jsonObject = new JSONObject(output.toString());
                JSONObject jsonObject_data = jsonObject.getJSONObject("data");
                if (jsonObject_data.getString("success").equals("true")) {
                    if (jsonObject_data.has("message")) {
                        Log.i("REpo", "" + jsonObject_data.getString("message"));
                        Toast.makeText(Ced_MultiVendor_Create_Deal.this, jsonObject_data.getString("message"), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_Deal_Listing.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                    }
                }
                else {
                    Log.i("REpo", "" + jsonObject_data.getString("message"));
                    Toast.makeText(Ced_MultiVendor_Create_Deal.this, jsonObject_data.getString("message"), Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
            }
        }, Ced_MultiVendor_Create_Deal.this, "POST", object);
        response.execute(deal_create_url);
    }

    public void showTruitonDatePickerDialog(View v) {
        final Calendar myCalendar = Calendar.getInstance();
        datePickerDialog = (datePicker, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "yyyy/MM/dd";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat,  Locale.US);
            MultiVendor_edt_start_date.setText(sdf.format(myCalendar.getTime()));
        };
        dialog = new DatePickerDialog(this, datePickerDialog, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dialog.show();
    }

    public void showToDatePickerDialog(View v) {
        final Calendar myCalendar = Calendar.getInstance();
        datePickerDialog = (datePicker, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "yyyy/MM/dd";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            MultiVendor_edt_end_date.setText(sdf.format(myCalendar.getTime()));
        };
        dialog = new DatePickerDialog(this, datePickerDialog, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dialog.show();
    }
}