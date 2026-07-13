package magentoegypt.locafy.dashboard_enhancements;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import magentoegypt.locafy_constant.Ced_MultiVendor_GlobalVariables;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.R;
import magentoegypt.locafy_constant.AppConstant;
import magentoegypt.locafy_constant.FileUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import static magentoegypt.locafy_constant.AppConstant.pictureField;

public class Ced_Multivendor_bussiness_details extends Ced_MultiVendor_NavigationActivity {
    String url;
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    ArrayList<String> values;
    LinearLayout linear_content, linear_save_section;
    Button btn_save;
    String UpdateUrl;
    String infoUrl;
    ImageView image;
    int writeresult, readresult;
    HashMap<String, String> business_Type;
    ArrayList<String> business_type_spinner_data;

    HashMap<String, String> address_Proof;
    ArrayList<String> address_proof_spinner_data;

    HashMap<String, String> map_saveData;
    HashMap<String, String> map_saveData1;
    String profileuri;
    String image_name;
    Boolean flag = false;
    String encodedGstfile = "";
    String gsturi = "nouri";
    String checqueuri = "nouri";
    String tanuri = "nouri";
    HashMap<String, String> map_info;
    int count = 0;
    int image_count = 0;
    JSONObject required_field;
    ArrayList<String> address_proof_list;
    HashMap<String, String> address_proof_hashmap, address_proof_hashmap_value_key;
    ArrayList<String> business_type_list;
    HashMap<String, String> business_type_hashmap;
    String file_post_value = "";
    Spinner bank_city;
    private int SELECT_PHOTO = 1;
    private final String encodedCinfile = "";
    private final String encodedTinfile = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());


        if (connectionDetector.isConnectingToInternet()) {
            super.onCreate(savedInstanceState);
            final ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
            getLayoutInflater().inflate(R.layout.activity_ced__multivendor_business_detials, content, true);

            linear_content = findViewById(R.id.linear_content);
            linear_save_section = findViewById(R.id.linear_save_section);
            btn_save = findViewById(R.id.btn_save);
            writeresult = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            readresult = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
            map_saveData = new HashMap<>();
            map_saveData1 = new HashMap<>();
            required_field = new JSONObject();
            business_Type = new HashMap<>();
            business_type_spinner_data = new ArrayList<>();

            address_Proof = new HashMap<>();
            address_proof_spinner_data = new ArrayList<>();
            business_type_hashmap = new HashMap<>();
            address_proof_hashmap = new HashMap<>();
            address_proof_hashmap_value_key = new HashMap<>();


            map_info = new HashMap<>();
            map_info.put("vendor_id", session.getVendorid());
            map_info.put("hashkey", session.getHahkey());

            /*url ="http://192.168.0.217/training/m2_2/vendorapi/additional/fields";
            UpdateUrl="http://192.168.0.217/training/m2_2/vendorapi/index/update";*/

            url = session.getBase_Url() + "/vendorapi/additional/fields";
            UpdateUrl = session.getBase_Url() + "/vendorapi/index/update";
            // infoUrl = session.getBase_Url() + "/vendorapi/index/info";

            map_saveData.put("vendor_id", session.getVendorid());
            map_saveData1.put("vendor_id", session.getVendorid());
            map_saveData1.put("hashkey", session.getHahkey());
            map_saveData.put("hashkey", session.getHahkey());
            requestData();


            btn_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppConstant.lockButton(view);
                    if (required_field.length() > 0) {
                        JSONArray jsonArray = required_field.names();
                        ArrayList<String> required_field_values = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                String values = required_field.getString(jsonArray.get(i).toString());
                                required_field_values.add(values);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        Toast.makeText(getApplicationContext(), getString(R.string.please_check_these_fields_are_empty) + required_field_values, Toast.LENGTH_SHORT).show();
                    } else {
                        updateData();
                    }
                }
            });
        } else {
            Intent nointernet = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(nointernet);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }

    }

    private void updateData() {
        Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {

            @Override
            public void processFinish(Object output) throws JSONException {
                JSONObject data = new JSONObject(output.toString());
                String sucess = data.getJSONObject("data").getString("success");
                if (sucess.equals("true")) {
                    Ced_MultiVendor_GlobalVariables.noti = data.getJSONObject("data").getString("valerts");
                    Ced_MultiVendor_GlobalVariables.progress = data.getJSONObject("data").getInt("profile_complete");
                    Toast.makeText(Ced_Multivendor_bussiness_details.this, data.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
                    Intent intent1 = new Intent(Ced_Multivendor_bussiness_details.this, Ced_Multivendor_bussiness_details.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                } else {
                    Toast.makeText(Ced_Multivendor_bussiness_details.this, data.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
                }
            }
        }, Ced_Multivendor_bussiness_details.this, "POST", map_saveData);
        crr.execute(UpdateUrl);
    }

    private void requestData() {

        final Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                JSONObject root = new JSONObject(output.toString());
                JSONArray store_fields = root.getJSONArray("business_fields");
                View view = null;
                TextView MultiVendor_label;
                TextView link_value;
                Spinner spn_delivery;
                EditText MultiVendor_textattribuite;

                for (int i = 0; i < store_fields.length(); i++) {
                    final JSONObject store_details_obj = store_fields.getJSONObject(i);
                    if (store_details_obj.getBoolean("is_required")) {
                        required_field.put(store_details_obj.getString("value"), store_details_obj.getString("key"));
                    }

                    if (store_details_obj.getString("type").equalsIgnoreCase("text")) {
                        view = View.inflate(getApplicationContext(), R.layout.dny_edittext_lyt, null);
                        MultiVendor_label = view.findViewById(R.id.MultiVendor_label);

                        MultiVendor_textattribuite = view.findViewById(R.id.MultiVendor_textattribuite);
                        if (store_details_obj.getString("input_type").equalsIgnoreCase("int")) {
                            MultiVendor_textattribuite.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
                        }
                        if (store_details_obj.getBoolean("is_required")) {
                            MultiVendor_label.setText(store_details_obj.getString("key") + "*");
                        } else {
                            MultiVendor_label.setText(store_details_obj.getString("key"));
                        }
                        MultiVendor_textattribuite.setTag(store_details_obj.getString("value"));
                        if (store_details_obj.has("saved_value")) {
                            MultiVendor_textattribuite.setText(store_details_obj.getString("saved_value"));
                            required_field.remove(MultiVendor_textattribuite.getTag().toString());
                        }

                        final EditText finalMultiVendor_textattribuite = MultiVendor_textattribuite;
                        final TextView finalMultiVendor_label = MultiVendor_label;
                        MultiVendor_textattribuite.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                if (charSequence.length() > 0) {
                                    map_saveData.put(finalMultiVendor_textattribuite.getTag().toString(), charSequence.toString());
                                    if (required_field.has(finalMultiVendor_textattribuite.getText().toString())) {
                                        required_field.remove(finalMultiVendor_textattribuite.getTag().toString());
                                    }
                                }
                            }

                            @Override
                            public void afterTextChanged(Editable editable) {

                            }
                        });
                        linear_content.addView(view);
                    }


                    if (store_details_obj.getString("type").equalsIgnoreCase("drop_down")) {
                        view = View.inflate(getApplicationContext(), R.layout.dny_spn_layout, null);
                        MultiVendor_label = view.findViewById(R.id.label_txt);

                        if (store_details_obj.getBoolean("is_required")) {
                            MultiVendor_label.setText(store_details_obj.getString("key") + "*");
                        } else {
                            MultiVendor_label.setText(store_details_obj.getString("key"));
                        }


                        spn_delivery = view.findViewById(R.id.spn_delivery);
                        spn_delivery.setTag(store_details_obj.getString("value"));

                        String saved_value = store_details_obj.getString("saved_value");

                        if (store_details_obj.getString("value").equalsIgnoreCase("business_city")) {
                            bank_city = spn_delivery;
                        }
                        JSONArray address_proof = null;
                        if (!store_details_obj.getString("value").equals("business_city")) {
                            address_proof = root.getJSONArray(store_details_obj.getString("value"));

                            Log.d("test-data", "" + address_proof_list);

                            address_proof_list = new ArrayList<>();
                            // address_proof_hashmap = new HashMap();
                            address_proof_hashmap_value_key = new HashMap<>();


                            for (int k = 0; k < address_proof.length(); k++) {
                                JSONObject jsonObject = address_proof.getJSONObject(k);
                                address_proof_list.add(jsonObject.getString("key"));
                                address_proof_hashmap.put(jsonObject.getString("key"), jsonObject.getString("value"));
                                address_proof_hashmap_value_key.put(jsonObject.getString("value"), jsonObject.getString("key"));
                            }

                            spn_delivery.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_textview, address_proof_list));

                            Log.d("vaibhav", "" + address_proof_hashmap);
                        }

                        /********************************************/

                        if (store_details_obj.getString("value").equalsIgnoreCase("business_state")) {
                            final int[] business = {0};

                            String saved_value1 = store_details_obj.getString("saved_value");
                            ArrayAdapter<String> dropdown = new ArrayAdapter<String>(Ced_Multivendor_bussiness_details.this, R.layout.spinner_textview, address_proof_list);
                            spn_delivery.setAdapter(dropdown);


                            Log.d("vaibhav_test", "" + address_proof_list);
                            Log.d("vaibhav_test", "" + address_proof_hashmap_value_key);
                            Log.d("vaibhav_test", "" + address_proof_list.indexOf(address_proof_hashmap_value_key.get(saved_value)));
                            Log.d("vaibhav_test", "" + saved_value1);
                            Log.d("vaibhav_test", "" + address_proof_hashmap_value_key.get(saved_value));


                            final Spinner finalSpn_delivery = spn_delivery;
                            spn_delivery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                                    map_saveData.put(adapterView.getTag().toString(), address_proof_hashmap.get(adapterView.getSelectedItem().toString()));

                                    if (required_field.has(adapterView.getTag().toString())) {
                                        required_field.remove(adapterView.getTag().toString());
                                    }
                                    city_request(address_proof_hashmap.get(finalSpn_delivery.getSelectedItem()), business[0]++);

                                    Log.d("vaibhav", "" + address_proof_hashmap.get(finalSpn_delivery.getSelectedItem()));


                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }


                            });

                            if (!saved_value.equalsIgnoreCase(""))
                                spn_delivery.setSelection(address_proof_list.indexOf(address_proof_hashmap_value_key.get(saved_value)));
                            Log.d("test---", "" + address_proof_list.indexOf(address_proof_hashmap_value_key.get(saved_value)));

                        } else {
                            Log.d("test-data", "" + address_proof_list);
                            Log.d("test-data", "" + store_details_obj.getString("value"));

                            spn_delivery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    map_saveData.put(adapterView.getTag().toString(), address_proof_hashmap.get(adapterView.getSelectedItem().toString()));

                                    if (required_field.has(adapterView.getTag().toString())) {
                                        required_field.remove(adapterView.getTag().toString());
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });

                            if (!saved_value.equalsIgnoreCase(""))
                                spn_delivery.setSelection(address_proof_list.indexOf(address_proof_hashmap_value_key.get(saved_value)));
//                            Log.d("test---", "" + address_proof_list.indexOf(address_proof_hashmap_value_key.get(saved_value)));
                        }

                        /********************************************/

                        linear_content.addView(view);
                    }




                    /*if (store_details_obj.getString("type").equalsIgnoreCase("drop_down")) {
                        view = View.inflate(getApplicationContext(), R.layout.dny_spn_layout, null);
                        MultiVendor_label = view.findViewById(R.id.label_txt);

                        if (store_details_obj.getBoolean("is_required")) {
                            MultiVendor_label.setText(store_details_obj.getString("key") + "*");
                        } else {
                            MultiVendor_label.setText(store_details_obj.getString("key"));
                        }

                        spn_delivery = view.findViewById(R.id.spn_delivery);
                        spn_delivery.setTag(store_details_obj.getString("value"));

                        String saved_value = store_details_obj.getString("saved_value");


                        address_proof_list = new ArrayList<>();
                        address_proof_hashmap = new HashMap<>();
                        address_proof_hashmap_value_key = new HashMap<>();

                        JSONArray address_proof = root.getJSONArray(store_details_obj.getString("value"));

                        Log.d("test-data", "" + address_proof_list);


                        for (int k = 0; k < address_proof.length(); k++) {
                            JSONObject jsonObject = address_proof.getJSONObject(k);
                            address_proof_list.add(jsonObject.getString("key"));
                            address_proof_hashmap.put(jsonObject.getString("key"), jsonObject.getString("value"));
                            address_proof_hashmap_value_key.put(jsonObject.getString("value"), jsonObject.getString("key"));
                        }

                        spn_delivery.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_list_item_1, address_proof_list));

                        Log.d("test-data", "" + address_proof_list);
                        Log.d("test-data", "" + store_details_obj.getString("value"));

                        spn_delivery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                map_saveData.put(adapterView.getTag().toString(), address_proof_hashmap.get(adapterView.getSelectedItem().toString()));
                                if (required_field.has(adapterView.getTag().toString())) {
                                    required_field.remove(adapterView.getTag().toString());
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                        if (!saved_value.equalsIgnoreCase(""))
                            spn_delivery.setSelection(address_proof_list.indexOf(address_proof_hashmap_value_key.get(saved_value)));
                        Log.d("test---", "" + address_proof_list.indexOf(address_proof_hashmap_value_key.get(saved_value)));

                        linear_content.addView(view);
                    }*/


                    if (store_details_obj.getString("type").equalsIgnoreCase("image")) {
                        view = View.inflate(getApplicationContext(), R.layout.view_image_field, null);
                        MultiVendor_label = view.findViewById(R.id.text_title);
                        if (store_details_obj.getBoolean("is_required"))
                            MultiVendor_label.setText(store_details_obj.getString("key") + "*");
                        else
                            MultiVendor_label.setText(store_details_obj.getString("key"));

                        final ImageView imageView = view.findViewById(R.id.picture_field);
                        imageView.setTag(store_details_obj.getString("value"));
                        if (store_details_obj.has("saved_value")) {
                            if (store_details_obj.has("saved_value") && !TextUtils.isEmpty(store_details_obj.getString("saved_value"))) {
                                Picasso.with(getApplicationContext()).
                                        load(store_details_obj.getString("saved_value")).
                                        error(getResources().getDrawable(R.drawable.placeholder))
                                        .placeholder(getResources().getDrawable(R.drawable.placeholder))
                                        .into(imageView);
                            }
                        }

                        TextView browse_picture = view.findViewById(R.id.browse_picture);
                        browse_picture.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pictureField = imageView;

                                if (writeresult == PackageManager.PERMISSION_GRANTED && readresult == PackageManager.PERMISSION_GRANTED) {
                                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                                    photoPickerIntent.setType("image/*");
                                    startActivityForResult(photoPickerIntent, 1);
                                } else {

                                    if (ActivityCompat.shouldShowRequestPermissionRationale(Ced_Multivendor_bussiness_details.this, Manifest.permission.READ_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(Ced_Multivendor_bussiness_details.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                        Toast.makeText(getApplicationContext(), "Storage permission allows us to access data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
                                    } else {
                                        ActivityCompat.requestPermissions(Ced_Multivendor_bussiness_details.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                                    }
                                }
                            }
                        });

                        linear_content.addView(view);
                    }

                    if (store_details_obj.getString("type").equalsIgnoreCase("file")) {
                        view = View.inflate(getApplicationContext(), R.layout.file_layout_dny, null);
                        link_value = view.findViewById(R.id.link_value);
                        MultiVendor_label = view.findViewById(R.id.text_title);
                        if (store_details_obj.getBoolean("is_required"))
                            MultiVendor_label.setText(store_details_obj.getString("key") + "*");
                        else
                            MultiVendor_label.setText(store_details_obj.getString("key"));

                        link_value.setTag(store_details_obj.getString("value"));
                        if (store_details_obj.has("saved_value")) {
                            if (store_details_obj.has("saved_value") && !TextUtils.isEmpty(store_details_obj.getString("saved_value"))) {
                                //   link_value.setText(store_details_obj.getString("saved_value"));
                                link_value.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        try {
                                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(store_details_obj.getString("saved_value")));
                                            startActivity(intent);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });


                            } else {
                                link_value.setText("No File Attached");
                            }
                        }

                        TextView browse_picture1 = view.findViewById(R.id.browse_picture);
                        final TextView finalLink_value = link_value;
                        browse_picture1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (writeresult == PackageManager.PERMISSION_GRANTED && readresult == PackageManager.PERMISSION_GRANTED) {
                                    Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                                    /*   photoPickerIntent.setType("image/*");*/
                                    file_post_value = finalLink_value.getTag().toString();
                                    photoPickerIntent.setType("application/*");
                                    startActivityForResult(photoPickerIntent, 2);
                                } else {

                                    if (ActivityCompat.shouldShowRequestPermissionRationale(Ced_Multivendor_bussiness_details.this, Manifest.permission.READ_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(Ced_Multivendor_bussiness_details.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                        Toast.makeText(Ced_Multivendor_bussiness_details.this, "Storage permission allows us to access data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
                                    } else {
                                        ActivityCompat.requestPermissions(Ced_Multivendor_bussiness_details.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                                    }
                                }
                            }
                        });
                        linear_content.addView(view);
                    }

                }
                linear_save_section.setVisibility(View.VISIBLE);
            }
        }, Ced_Multivendor_bussiness_details.this, "POST", map_info);
        crr.execute(url);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case 1:
                if (resultCode != RESULT_CANCELED) {
                    if (resultCode == RESULT_OK) {
                        Toast.makeText(this, "File Attached Successfully", Toast.LENGTH_SHORT).show();
                        try {
                            final Uri imageUri = imageReturnedIntent.getData();
                            gsturi = getRealPathFromURI(Ced_Multivendor_bussiness_details.this, imageUri);
                            if (calculateFileSize(gsturi) > 5.0) {
                                Toast.makeText(this, "Image should be less than 5 MB", Toast.LENGTH_SHORT).show();
                            } else {
                                String[] imagename = getRealPathFromURI(Ced_Multivendor_bussiness_details.this, imageUri).split("/");
                                if (getResources().getString(R.string.Enable_Log).equals("yes")) {
                                    Log.i("filename", "" + imagename[imagename.length - 1]);
                                }
                                image_name = imagename[imagename.length - 1];
                                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                                pictureField.setImageBitmap(selectedImage);
                                encodedGstfile = encodeImage(selectedImage);
                                JSONObject jsonObject = new JSONObject();
                                try {
                                    jsonObject.put("type", "file");
                                    jsonObject.put("name", image_name);
                                    jsonObject.put("base64_encoded_data", encodedGstfile);
                                    map_saveData.put(pictureField.getTag().toString(), jsonObject.toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Log.i("encodedimage", encodedGstfile);
                            }

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                    }
                }
                break;

            case 2:
                if (resultCode != RESULT_CANCELED) {
                    try {
                        Toast.makeText(this, "File Attached Successfully", Toast.LENGTH_SHORT).show();
                        final Uri imageUri = imageReturnedIntent.getData();
                        imageUri.getPath();
                        String filePath = "";
                        String file_name = "";
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            File file = new File(imageUri.getPath());//create path from uri
                            final String[] split = file.getPath().split(":");//split the path.
                            filePath = split[1];//assign it to a string(your choice).
                        } else {
                            filePath = FileUtils.getPath(getApplicationContext(), imageUri);
                        }

                        if (calculateFileSize(filePath) > 5.0) {
                            Toast.makeText(this, "Image should be less than 5 MB", Toast.LENGTH_SHORT).show();
                        } else {
                            assert imageUri != null;
                            file_name = getFileName(imageUri);

                            File file1 = new File(filePath);
                            String[] imagename = imageUri.getPath().split("/");
                            byte[] bytes = loadFile(file1);
                            String base64file = Base64.encodeToString(bytes, Base64.DEFAULT);
                            file_name = getFileName(imageUri);
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("type", "file");
                                jsonObject.put("name", file_name);
                                jsonObject.put("base64_encoded_data", base64file);
                                map_saveData.put(file_post_value, jsonObject.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    } catch (Exception e) {
                        Toast.makeText(this, "Not allowed from this location", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    SELECT_PHOTO = 1;
                    startActivityForResult(photoPickerIntent, 1);

                } else {

                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_LONG).show();
                }
                break;
            case 2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("application/*");
                    SELECT_PHOTO = 2;
                    startActivityForResult(photoPickerIntent, 2);

                } else {

                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_LONG).show();
                }
                break;
            case 3:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    SELECT_PHOTO = 3;
                    startActivityForResult(photoPickerIntent, 3);
                } else {

                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    private void city_request(String code, final int value) {
        String get_city = session.getBase_Url() + "rest/V1/mobiconnect/module/getcity";

        int bool_bank_city, bool_business_city = 0;


        Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {

                ArrayList citylabellist;
                ArrayList citycodelist;
                final HashMap label_codes_city;
                HashMap id_codes_city;

                citylabellist = new ArrayList<String>();
                citycodelist = new ArrayList<String>();
                label_codes_city = new HashMap<String, String>();
                id_codes_city = new HashMap<String, String>();


                JSONArray main = new JSONArray(output.toString());
                JSONObject inner = main.getJSONObject(0);
                JSONArray cities = inner.getJSONArray("cities");
                JSONObject inner_json;

                for (int i = 0; i < cities.length(); i++) {
                    inner_json = cities.getJSONObject(i);
                    citylabellist.add(inner_json.getString("label"));
                    citycodelist.add(inner_json.getString("city_id"));
                    label_codes_city.put(inner_json.getString("label"), inner_json.getString("city_id"));
                }


                if (value >= 1) {
                    ArrayAdapter<String> dropdown = new ArrayAdapter<String>(Ced_Multivendor_bussiness_details.this, R.layout.spinner_textview, citylabellist);
                    bank_city.setAdapter(dropdown);
                }


                bank_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        map_saveData.put(adapterView.getTag().toString(), label_codes_city.get(adapterView.getSelectedItem().toString()).toString());

                        if (required_field.has(adapterView.getTag().toString())) {
                            required_field.remove(adapterView.getTag().toString());
                        }
//                            Log.i("REpo","Select : "+jsonObject.getString(KEY_field_to_post)+" "+optionmap.get(select_option.getSelectedItem()));

                        Log.d("params", "" + map_saveData);


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });


            }
        }, Ced_Multivendor_bussiness_details.this);
        crr.execute(get_city + "/" + code);

    }

}
