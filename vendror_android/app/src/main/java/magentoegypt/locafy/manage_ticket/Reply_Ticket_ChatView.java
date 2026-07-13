package magentoegypt.locafy.manage_ticket;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.reflect.TypeToken;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_ClientRequestResponseRest_New;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;
import magentoegypt.locafy.R;
import magentoegypt.locafy_constant.AppConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.List;

public class Reply_Ticket_ChatView extends Ced_MultiVendor_NavigationActivity {

    private JSONObject postdata;
    private String view_url = "";
    private final int page_num = 1;
    private boolean firstcall = true;
    private boolean request = true;
    private String ticket_id;
    private Ced_MultiVendor_VendorSessionManagement vendorSessionManagement;

    private final Type type = new TypeToken<List<Ticket_ChatModel>>() {
    }.getType();
    private List<Ticket_ChatModel> ticketChatList;
    private Reply_Ticket_ChatView_Adapter reply_ticket_chatView_adapter;

    private AppCompatTextView error_message, ticket_title, department_text, status_text, tkt_created_at_text, order_text, add_attachment_file;
    private AppCompatButton add_attachment, add_new_tkt;
    private RecyclerView chat_list;
    private AppCompatEditText reply_message;
    private AppCompatSpinner status_value, apply_signature;

    private final JSONObject messagepostdata = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
        getLayoutInflater().inflate(R.layout.reply_tkt_chtview, content, true);
        view_url = session.getBase_Url() + "rest/V1/vsupportapi/viewTicket";
        postdata = new JSONObject();
        vendorSessionManagement = new Ced_MultiVendor_VendorSessionManagement(Reply_Ticket_ChatView.this);

        ticket_title = findViewById(R.id.ticket_title);
        error_message = findViewById(R.id.error_message);
        department_text = findViewById(R.id.department_text);
        status_text = findViewById(R.id.status_text);
        tkt_created_at_text = findViewById(R.id.tkt_created_at_text);
        order_text = findViewById(R.id.order_text);
        add_attachment_file = findViewById(R.id.add_attachment_file);
        add_attachment = findViewById(R.id.add_attachment);
        add_new_tkt = findViewById(R.id.add_new_tkt);
        chat_list = findViewById(R.id.chat_list);
        reply_message = findViewById(R.id.reply_message);
        status_value = findViewById(R.id.transaction_id_value);
        apply_signature = findViewById(R.id.apply_signature);

        chat_list.setLayoutManager(new LinearLayoutManager(Reply_Ticket_ChatView.this, LinearLayout.VERTICAL, false));
//        chat_list.setNestedScrollingEnabled(false);
        if (getIntent().getStringExtra("tkt_id") != null) {
            try {
                ticket_title.setText(getIntent().getStringExtra("subject"));
                ticket_id = getIntent().getStringExtra("tkt_id");

                postdata.put("ticket_id", ticket_id);
                postdata.put("vendor_id", vendorSessionManagement.getVendorid());

                request_page_date(page_num, request);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        add_attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (writeresult == PackageManager.PERMISSION_GRANTED && readresult == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    //sets the select file to all types of files
                    intent.setType("*/*");
                    // Only get openable files
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    //starts new activity to select file and return data
                    startActivityForResult(Intent.createChooser(intent, "Choose File to Upload.."), 2);
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(Reply_Ticket_ChatView.this, Manifest.permission.READ_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(Reply_Ticket_ChatView.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        Toast.makeText(Reply_Ticket_ChatView.this, "Storage permission allows us to access data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
                    } else {
                        ActivityCompat.requestPermissions(Reply_Ticket_ChatView.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                    }
                }
            }
        });
        add_new_tkt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendmessage();
            }
        });
    }

    public void request_page_date(int page_count, final boolean call_request) throws JSONException {
        postdata.put("page", "" + page_count);
        if (call_request) {
            Ced_ClientRequestResponseRest_New response = new Ced_ClientRequestResponseRest_New(new AsyncResponse() {
                @Override
                public void processFinish(Object output) throws JSONException {
                    Log.i("REpo", "143_processFinish: " + output.toString());
                    JSONObject jsonObject = new JSONObject(output.toString());

                    if (jsonObject.getString("success").equals("true")) {
                        firstcall = false;

                        JSONObject vendor_data = jsonObject.getJSONArray("vendor_data").getJSONObject(0);

                        department_text.setText(vendor_data.has("department") ? vendor_data.getString("department") : "");
                        status_text.setText(vendor_data.has("status") ? vendor_data.getString("status") : "");
                        tkt_created_at_text.setText(vendor_data.has("created_time") ? vendor_data.getString("created_time") : "");
                        order_text.setText(vendor_data.has("order_id") ? vendor_data.getString("order_id") : "");

                        JSONArray chatInfo = vendor_data.getJSONArray("ticket_messages");
                        if (chatInfo.length() > 0) {
                            chat_list.setVisibility(View.VISIBLE);
                            ticketChatList = converter.fromJson(chatInfo.toString(), type);
                            reply_ticket_chatView_adapter = new Reply_Ticket_ChatView_Adapter(Reply_Ticket_ChatView.this, ticketChatList);
                            chat_list.setAdapter(reply_ticket_chatView_adapter);
                        } else {
                            chat_list.setVisibility(View.GONE);
                        }

                        JSONArray spinner_data = null;
                        JSONObject spinnerobj = null;

                        spinner_data = jsonObject.getJSONArray("status_list");
                        for (int i = 0; i < spinner_data.length(); i++) {
                            spinnerobj = spinner_data.getJSONObject(i);
                            AppConstant.statusMap.put(spinnerobj.getString("value"), spinnerobj.getString("key"));
                            AppConstant.statusList.add(spinnerobj.getString("value"));
                        }

                        spinner_data = jsonObject.getJSONArray("signature_list");
                        for (int i = 0; i < spinner_data.length(); i++) {
                            spinnerobj = spinner_data.getJSONObject(i);
                            AppConstant.signatureMap.put(spinnerobj.getString("value"), spinnerobj.getString("key"));
                            AppConstant.signatureList.add(spinnerobj.getString("value"));
                        }

                        status_value.setAdapter(new ArrayAdapter(Reply_Ticket_ChatView.this, R.layout.spinner_item, AppConstant.statusList));
                        apply_signature.setAdapter(new ArrayAdapter(Reply_Ticket_ChatView.this, R.layout.spinner_item, AppConstant.signatureList));

                    } else {
                        if (firstcall) {
                            if (jsonObject.has("message")) {
                                chat_list.setVisibility(View.GONE);
                                error_message.setVisibility(View.VISIBLE);
                                error_message.setText(jsonObject.getString("message"));
                            }
                        } else {
                            Toast.makeText(Reply_Ticket_ChatView.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            request = false;
                        }
                    }
                }
            }, Reply_Ticket_ChatView.this, "POST", "" + postdata);
            response.execute(view_url);
        }
    }

    private void sendmessage() {
        if (reply_message.getText().length() > 0) {
            String message_url = session.getBase_Url() + "rest/V1/vsupportapi/replyTicket";
            try {
                messagepostdata.put("vendor_id", vendorSessionManagement.getVendorid());
                messagepostdata.put("message", reply_message.getText().toString());
                messagepostdata.put("ticket_id", ticket_id);
                messagepostdata.put("status", AppConstant.statusMap.get(status_value.getSelectedItem().toString()));
                messagepostdata.put("signature", AppConstant.signatureMap.get(apply_signature.getSelectedItem().toString()));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Ced_ClientRequestResponseRest_New response = new Ced_ClientRequestResponseRest_New(new AsyncResponse() {
                @Override
                public void processFinish(Object output) throws JSONException {
                    JSONObject jsonObject = new JSONObject(output.toString());
                    firstcall = true;
                    request_page_date(page_num, request);
                    Log.i("REpo", "send message : " + jsonObject);
                }
            }, Reply_Ticket_ChatView.this, "POST", "" + messagepostdata);
            response.execute(message_url);
        } else {
            Toast.makeText(Reply_Ticket_ChatView.this, R.string.messagecant, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 2:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    //sets the select file to all types of files
                    intent.setType("*/*");
                    // Only get openable files
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    //starts new activity to select file and return data
                    startActivityForResult(Intent.createChooser(intent, "Choose File to Upload.."), 2);
                } else {
                    Toast.makeText(getApplicationContext(), "" + getResources().getString(R.string.permissiondenied), Toast.LENGTH_LONG).show();
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 2:
                if (resultCode == RESULT_OK) {
                    try {
                        final Uri imageUri = data.getData();
                        Log.i("FilePath: ", "" + imageUri.getPath());
                        Log.i("FilePath: ", "" + imageUri.getPath().replace("/document/raw:", ""));
                        File file = new File(imageUri.getPath().replace("/document/raw:", ""));

                        byte[] bytes = loadFile(file);
                        String base64file = Base64.encodeToString(bytes, Base64.DEFAULT);

                        String[] imagename = imageUri.getPath().split("/");

                        add_attachment_file.setText(imagename[imagename.length - 1]);

                        JSONObject imageObject = new JSONObject();
                        imageObject.put("name", imagename[imagename.length - 1]);
                        imageObject.put("base64_encoded_data", base64file);
                        messagepostdata.put("image", imageObject.toString());


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
