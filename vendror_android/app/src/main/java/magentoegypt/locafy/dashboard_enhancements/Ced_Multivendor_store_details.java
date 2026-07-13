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

public class Ced_Multivendor_store_details extends Ced_MultiVendor_NavigationActivity {

    String url;
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    ArrayList<String> values;
    LinearLayout linear_content, linear_save_section;
    Button btn_save;
    String UpdateUrl;
    String encodedGstfile = "";
    HashMap<String, String> map_saveData;
    Boolean flag = false;
    HashMap<String, String> map_info;
    ImageView image;
    HashMap<String, String> business_Type;
    ArrayList<String> business_type_spinner_data;
    int writeresult, readresult;
    String image_name;
    ArrayList<String> address_proof_list;
    HashMap<String, String> address_proof_hashmap;
    ArrayList<String> business_type_list;
    HashMap<String, String> business_type_hashmap;
    JSONObject required_field;
    String file_post_value = "";
    String gsturi = "nouri";
    String tanuri = "nouri";
    private final int SELECT_PHOTO = 1;
    private String encodedTinfile = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());

        if (connectionDetector.isConnectingToInternet()) {
            super.onCreate(savedInstanceState);
            ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
            getLayoutInflater().inflate(R.layout.activity_ced__multivendor_store_details, content, true);
            values = new ArrayList<>();
            linear_content = findViewById(R.id.linear_content);
            linear_save_section = findViewById(R.id.linear_save_section);
            btn_save = findViewById(R.id.btn_save);
            map_saveData = new HashMap();
            writeresult = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            readresult = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
            url = session.getBase_Url() + "/vendorapi/additional/fields";
            UpdateUrl = session.getBase_Url() + "/vendorapi/index/update";
            required_field = new JSONObject();
            business_type_spinner_data = new ArrayList<>();

            address_proof_list = new ArrayList<>();
            address_proof_hashmap = new HashMap<>();

            business_type_list = new ArrayList<>();
            business_type_hashmap = new HashMap<>();
            map_info = new HashMap<>();
            map_info.put("vendor_id", session.getVendorid());
            map_info.put("hashkey", session.getHahkey());

            business_Type = new HashMap<>();

            /*url = session.getBase_Url()+"/vendorapi/additional/fields";*/

            requestData();

            map_saveData.put("vendor_id", session.getVendorid());
            map_saveData.put("hashkey", session.getHahkey());

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
                        Toast.makeText(getApplicationContext(), getString(R.string.please_check_these_fields_are_empty)  + required_field_values, Toast.LENGTH_SHORT).show();
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

        final Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {

                JSONObject root = new JSONObject(output.toString());
                if (root.getJSONObject("data").getString("success").equals("true")) {
                    Intent in = new Intent(Ced_Multivendor_store_details.this, Ced_Multivendor_store_details.class);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);

                } else {
                    Toast.makeText(Ced_Multivendor_store_details.this, "" + root.getString("message"), Toast.LENGTH_SHORT).show();
                }

            }
        }, Ced_Multivendor_store_details.this, "POST", map_saveData);
        crr.execute(UpdateUrl);
    }


    private void requestData() {

        final Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                JSONObject root = new JSONObject(output.toString());
                JSONArray store_fields = root.getJSONArray("store_fields");
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

                    if (store_details_obj.getString("type").equalsIgnoreCase("text") || store_details_obj.getString("type").equalsIgnoreCase("textarea")) {
                        view = View.inflate(getApplicationContext(), R.layout.dny_edittext_lyt, null);
                        MultiVendor_label = view.findViewById(R.id.MultiVendor_label);

                        MultiVendor_textattribuite = view.findViewById(R.id.MultiVendor_textattribuite);

                        if (store_details_obj.has("input_type") && store_details_obj.getString("input_type").equalsIgnoreCase("int")) {
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


                        if (store_details_obj.getString("value").equalsIgnoreCase("address_proof")) {
                            address_proof_list = new ArrayList<>();

                            JSONArray address_proof = root.getJSONArray("address_proof");
                            for (int k = 0; k < address_proof.length(); k++) {
                                JSONObject jsonObject = address_proof.getJSONObject(k);
                                address_proof_list.add(jsonObject.getString("key"));
                                address_proof_hashmap.put(jsonObject.getString("key"), jsonObject.getString("value"));
                            }

                            spn_delivery.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_list_item_1, address_proof_list));
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
                        } else {
                            JSONArray bussiness_type = root.getJSONArray("bussiness_type");
                            business_type_list = new ArrayList<>();


                            for (int j = 0; j < bussiness_type.length(); j++) {
                                JSONObject jsonObject = bussiness_type.getJSONObject(j);
                                business_type_list.add(jsonObject.getString("key"));
                                business_type_hashmap.put(jsonObject.getString("key"), jsonObject.getString("value"));
                            }

                            spn_delivery.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_list_item_1, business_type_list));
                            final TextView finalMultiVendor_label1 = MultiVendor_label;
                            spn_delivery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    map_saveData.put(adapterView.getTag().toString(), business_type_hashmap.get(adapterView.getSelectedItem().toString()));
                                    if (required_field.has(adapterView.getTag().toString())) {
                                        required_field.remove(adapterView.getTag().toString());
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });
                        }

                        linear_content.addView(view);
                    }

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

                                    if (ActivityCompat.shouldShowRequestPermissionRationale(Ced_Multivendor_store_details.this, Manifest.permission.READ_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(Ced_Multivendor_store_details.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                        Toast.makeText(getApplicationContext(), "Storage permission allows us to access data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
                                    } else {
                                        ActivityCompat.requestPermissions(Ced_Multivendor_store_details.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
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

                                    if (ActivityCompat.shouldShowRequestPermissionRationale(Ced_Multivendor_store_details.this, Manifest.permission.READ_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(Ced_Multivendor_store_details.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                        Toast.makeText(Ced_Multivendor_store_details.this, "Storage permission allows us to access data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
                                    } else {
                                        ActivityCompat.requestPermissions(Ced_Multivendor_store_details.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                                    }
                                }
                            }
                        });
                        linear_content.addView(view);
                    }

                }
                linear_save_section.setVisibility(View.VISIBLE);
            }
        }, Ced_Multivendor_store_details.this, "POST", map_info);
        crr.execute(url);

    }


    private void prepare_business_type_SpinnerData(JSONArray array) {
        try {
            JSONArray type = array;
            JSONObject inner_jo;

            for (int i = 0; i < type.length(); i++) {
                inner_jo = array.getJSONObject(i);
                business_type_spinner_data.add(inner_jo.getString("key"));
                business_Type.put(inner_jo.getString("key"), inner_jo.getString("value"));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);


        switch (requestCode) {
            case 1:
                if (resultCode != RESULT_CANCELED) {
                    if (resultCode == RESULT_OK) {
                        Toast.makeText(this, "File Attached Successfully", Toast.LENGTH_SHORT).show();
                        try {
                            final Uri imageUri = imageReturnedIntent.getData();
                            gsturi = getRealPathFromURI(Ced_Multivendor_store_details.this, imageUri);
                            if (calculateFileSize(gsturi) > 5.0) {
                                Toast.makeText(this, "Image should be less than 5 MB", Toast.LENGTH_SHORT).show();
                            } else {
                                String[] imagename = getRealPathFromURI(Ced_Multivendor_store_details.this, imageUri).split("/");
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
                            File file1 = new File(filePath);
                            String[] imagename = imageUri.getPath().split("/");
                            byte[] bytes = loadFile(file1);
                            encodedTinfile = Base64.encodeToString(bytes, Base64.DEFAULT);
                            file_name = getFileName(imageUri);
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("type", "file");
                                jsonObject.put("name", file_name);
                                jsonObject.put("base64_encoded_data", encodedTinfile);
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

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }
}
