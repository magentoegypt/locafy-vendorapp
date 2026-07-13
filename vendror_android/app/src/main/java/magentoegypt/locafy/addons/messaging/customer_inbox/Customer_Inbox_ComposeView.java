package magentoegypt.locafy.addons.messaging.customer_inbox;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_ClientRequestResponseRest_New;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;
import magentoegypt.locafy.R;
import magentoegypt.locafy_constant.AppConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;


public class Customer_Inbox_ComposeView extends Ced_MultiVendor_NavigationActivity {

    private final JSONObject messagepostdata = new JSONObject();
    private Ced_MultiVendor_VendorSessionManagement SessionManagement;
    private AppCompatEditText message_text, subject_text;
    private LinearLayout image_layouts;
    private String customer_id = "";
    private JSONArray images = null;
    private AppCompatCheckBox notifyreciever;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
        getLayoutInflater().inflate(R.layout.customer_inbox_compose_view, content, true);
        SessionManagement = new Ced_MultiVendor_VendorSessionManagement(Customer_Inbox_ComposeView.this);

        subject_text = findViewById(R.id.subject_text);
        message_text = findViewById(R.id.message_text);
        AppCompatButton send_message = findViewById(R.id.send_message);
        image_layouts = findViewById(R.id.image_layouts);
        AppCompatButton addfiles = findViewById(R.id.addfiles);
        Spinner customer_list = findViewById(R.id.customer_list);
        notifyreciever = findViewById(R.id.notifyreciever);
        images = new JSONArray();

        send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendmessage();
            }
        });

        addfiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (writeresult == PackageManager.PERMISSION_GRANTED && readresult == PackageManager.PERMISSION_GRANTED) {
                    imageBrowse();
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(Customer_Inbox_ComposeView.this, Manifest.permission.READ_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(Customer_Inbox_ComposeView.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        Toast.makeText(Customer_Inbox_ComposeView.this, "Storage permission allows us to access data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
                    } else {
                        ActivityCompat.requestPermissions(Customer_Inbox_ComposeView.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                    }
                }
            }
        });

        ArrayAdapter email_Adp = new ArrayAdapter(Customer_Inbox_ComposeView.this, R.layout.spinner_item, AppConstant.customerList);
        customer_list.setAdapter(email_Adp);
        customer_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                customer_id = AppConstant.customerMap.get(parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void sendmessage() {
        if (Objects.requireNonNull(message_text.getText()).length() > 0) {

            String message_url = session.getBase_Url() + "rest/V1/vmessgingapi/customercompose";
            try {
                messagepostdata.put("vendor_id", SessionManagement.getVendorid());
                messagepostdata.put("subject", Objects.requireNonNull(subject_text.getText()).toString());
                messagepostdata.put("message", message_text.getText().toString());
                messagepostdata.put("customer_id", customer_id);

                if (images.length() > 0) {
                    messagepostdata.put("image", images.toString());
                } else {
                    if (messagepostdata.has("image"))
                        messagepostdata.remove("image");
                }

                if (notifyreciever.isChecked()) {
                    messagepostdata.put("send_email", "true");
                }
                else {
                    messagepostdata.put("send_email", "false");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Ced_ClientRequestResponseRest_New response = new Ced_ClientRequestResponseRest_New(new AsyncResponse() {
                @Override
                public void processFinish(Object output) throws JSONException {
                    JSONObject jsonObject = new JSONObject(output.toString());
                    Log.i("REpo", "send message : " + jsonObject);
                    if (jsonObject.getJSONObject("vendor_data").getString("success").equals("true")) {
                        Toast.makeText(Customer_Inbox_ComposeView.this, jsonObject.getJSONObject("vendor_data").getString("message"), Toast.LENGTH_SHORT).show();
                        Intent back_to_customer_inbox = new Intent(Customer_Inbox_ComposeView.this, Customer_Inbox.class);
                        back_to_customer_inbox.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(back_to_customer_inbox);
                        finish();
                    } else {
                        Toast.makeText(Customer_Inbox_ComposeView.this, jsonObject.getJSONObject("vendor_data").getString("message"), Toast.LENGTH_SHORT).show();
                    }
                }
            }, Customer_Inbox_ComposeView.this, "POST", "" + messagepostdata);
            response.execute(message_url);
        } else {
            Toast.makeText(Customer_Inbox_ComposeView.this, R.string.messagecant, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 2) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                imageBrowse();
            } else {
                Toast.makeText(getApplicationContext(), "" + getResources().getString(R.string.permissiondenied), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST) {
                try {
                    Uri picUri = data.getData();
                    String filePath = getPath(picUri);
                    final InputStream imageStream = getContentResolver().openInputStream(Objects.requireNonNull(picUri));
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    String base64file = encodeImage(selectedImage, "Customer_Inbox_ChatView");
                    JSONObject imageObject = new JSONObject();
                    View child = getLayoutInflater().inflate(R.layout.image_layout, null);
                    final AppCompatTextView text_data = child.findViewById(R.id.text_data);
                    final AppCompatImageButton remove_image = child.findViewById(R.id.remove_image);
                    remove_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((ViewManager) ((ConstraintLayout) remove_image.getParent()).getParent()).removeView((ConstraintLayout) remove_image.getParent());
                            images.remove(Integer.parseInt(text_data.getTag().toString()));
                            Log.i("REpo", "remove onClick: images" + images);
                        }
                    });
                    String[] imagename = filePath.split("/");
                    image_layouts.addView(child);
                    text_data.setText(imagename[imagename.length - 1]);
                    text_data.setTag(images.length());
                    try {
                        imageObject.put("name", imagename[imagename.length - 1]);
                        imageObject.put("base64_encoded_data", base64file);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    images.put(imageObject);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void imageBrowse() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

}