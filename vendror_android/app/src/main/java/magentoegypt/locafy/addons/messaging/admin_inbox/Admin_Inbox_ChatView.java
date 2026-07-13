package magentoegypt.locafy.addons.messaging.admin_inbox;

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
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.Ced_ClientRequestResponseRest_New;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;
import magentoegypt.locafy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

public class Admin_Inbox_ChatView extends Ced_MultiVendor_NavigationActivity {

    private final Gson converter = new Gson();
    private final Type type = new TypeToken<List<Admin_Inbox_Chatview_Model>>() {
    }.getType();
    private final JSONObject messagepostdata = new JSONObject();
    private JSONObject postdata;
    private String view_url = "";
    private Ced_MultiVendor_VendorSessionManagement SessionManagement;
    private int page_num = 1;
    private boolean firstcall = true;
    private String view_id;
    private List<Admin_Inbox_Chatview_Model> AdminChatModelList;
    private Admin_Inbox_ChatView_Adapter adapter;
    private AppCompatTextView error_message, title;
    private RecyclerView chat_list;
    private AppCompatEditText message_text;
    private LinearLayout image_layouts;
    private JSONArray images = null;
    private AppCompatCheckBox notifyreciever;
    private boolean sendrequest = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
        getLayoutInflater().inflate(R.layout.admin_inbox_chat_view, content, true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        view_url = session.getBase_Url() + "rest/V1/vmessgingapi/adminViewMessage";
        SessionManagement = new Ced_MultiVendor_VendorSessionManagement(Admin_Inbox_ChatView.this);
        postdata = new JSONObject();
        images = new JSONArray();
        notifyreciever = findViewById(R.id.notifyreciever);
        title = findViewById(R.id.admin_chat_title);
        chat_list = findViewById(R.id.chat_list);
        error_message = findViewById(R.id.error_message);
        message_text = findViewById(R.id.message_text);
        AppCompatButton send_message = findViewById(R.id.send_message);
        image_layouts = findViewById(R.id.image_layouts);
        AppCompatButton addfiles = findViewById(R.id.addfiles);
        NestedScrollView admin_inbox_parent = findViewById(R.id.admin_inbox_parent);

        if (getIntent().getStringExtra("view_id") != null) {
            try {
                Log.i("REpo", "onCreate: " + getIntent().getStringExtra("subject"));
                title.setText(getIntent().getStringExtra("subject"));
                view_id = getIntent().getStringExtra("view_id");
                postdata.put("vendor_id", SessionManagement.getVendorid());
                postdata.put("id", view_id);
                request_page_date();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        send_message.setOnClickListener(v -> sendmessage());
        addfiles.setOnClickListener(v -> {
            if (writeresult == PackageManager.PERMISSION_GRANTED && readresult == PackageManager.PERMISSION_GRANTED) {
                imageBrowse();
            }
            else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(Admin_Inbox_ChatView.this, Manifest.permission.READ_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(Admin_Inbox_ChatView.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(Admin_Inbox_ChatView.this, "Storage permission allows us to access data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
                }
                else {
                    ActivityCompat.requestPermissions(Admin_Inbox_ChatView.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                }
            }
        });

        admin_inbox_parent.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            View view = v.getChildAt(v.getChildCount() - 1);
            int diff = (view.getBottom() - (v.getHeight() + v.getScrollY()));
            if (diff <= 30) {
                if (sendrequest) {
                    ++page_num;
                    try {
                        request_page_date();
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void imageBrowse() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    public void request_page_date() throws JSONException {
        postdata.put("page", "" + page_num);
        Ced_ClientRequestResponseRest_New response = new Ced_ClientRequestResponseRest_New(output -> {
            JSONObject jsonObject = new JSONObject(output.toString());
            JSONObject jsonObject_data = jsonObject.getJSONObject("vendor_data");
            if (jsonObject_data.getString("success").equals("true")) {
                JSONArray chatInfo = jsonObject_data.getJSONArray("view");
                if (firstcall) {
                    firstcall = false;
                    AdminChatModelList = converter.fromJson(chatInfo.toString(), type);
                    adapter = new Admin_Inbox_ChatView_Adapter(Admin_Inbox_ChatView.this, AdminChatModelList);
                    chat_list.setAdapter(adapter);
                }
                else {
                    AdminChatModelList = converter.fromJson(chatInfo.toString(), type);
                    adapter.list.addAll(AdminChatModelList);
                    adapter.notifyDataSetChanged();
                }
                sendrequest = true;
            }
            else {
                if (firstcall) {
                    if (jsonObject_data.has("message")) {
                        chat_list.setVisibility(View.GONE);
                        error_message.setVisibility(View.VISIBLE);
                        error_message.setText(jsonObject_data.getString("message"));
                    }
                }
                else {
                    Toast.makeText(Admin_Inbox_ChatView.this, getResources().getString(R.string.nomoredata), Toast.LENGTH_SHORT).show();
                }
                sendrequest = false;
            }
        }, Admin_Inbox_ChatView.this, "POST", "" + postdata);
        response.execute(view_url);
    }

    private void sendmessage() {
        if (Objects.requireNonNull(message_text.getText()).length() > 0) {
            String message_url = session.getBase_Url() + "rest/V1/vmessgingapi/admincompose";
            try {
                messagepostdata.put("id", view_id);
                messagepostdata.put("vendor_id", SessionManagement.getVendorid());
                messagepostdata.put("message", message_text.getText().toString());
                messagepostdata.put("subject", title.getText().toString());
                if (images.length() > 0) {
                    messagepostdata.put("image", images.toString());
                } else {
                    if (messagepostdata.has("image"))
                        messagepostdata.remove("image");
                }
                if (notifyreciever.isChecked()) {
                    messagepostdata.put("send_email", "true");
                } else {
                    messagepostdata.put("send_email", "false");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Ced_ClientRequestResponseRest_New response = new Ced_ClientRequestResponseRest_New(output -> {
                JSONObject jsonObject = new JSONObject(output.toString());
                firstcall = true;
                sendrequest = false;
                page_num = 1;
                request_page_date();
                Log.i("REpo", "send message : " + jsonObject);
            }, Admin_Inbox_ChatView.this, "POST", "" + messagepostdata);
            response.execute(message_url);
        } else {
            Toast.makeText(Admin_Inbox_ChatView.this, R.string.messagecant, Toast.LENGTH_SHORT).show();
        }
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
                    remove_image.setOnClickListener(v -> {
                        ((ViewManager) ((ConstraintLayout) remove_image.getParent()).getParent()).removeView((ConstraintLayout) remove_image.getParent());
                        images.remove(Integer.parseInt(text_data.getTag().toString()));
                        Log.i("REpo", "remove onClick: images" + images);
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
}