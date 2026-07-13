package magentoegypt.locafy.addons.mvp_RequestForQuote;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;
import magentoegypt.locafy.R;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.HashMap;

public class MessageActivity extends Ced_MultiVendor_NavigationActivity implements View.OnClickListener {

    private EditText mMessageSdt;
    private TextView mSendMessage;
    private HashMap<String, String> post_param;
    private Ced_MultiVendor_VendorSessionManagement sessionManagement;
    private String quote_id, message_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
        getLayoutInflater().inflate(R.layout.activity_message, content, true);
        post_param = new HashMap<>();
        message_url = session.getBase_Url()+ "vrfqapi/quote/chat";
        sessionManagement = new Ced_MultiVendor_VendorSessionManagement(this);
        mMessageSdt = findViewById(R.id.message_sdt);
        mSendMessage = findViewById(R.id.send_message);
        mSendMessage.setOnClickListener(this);
        if (getIntent().hasExtra("quote_id")) {
            quote_id = getIntent().getStringExtra("quote_id");
        }
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.send_message){
            if (TextUtils.isEmpty(mMessageSdt.getText().toString().trim())) {
                Toast.makeText(this, R.string.message_validation, Toast.LENGTH_SHORT).show();
            } else {
                sendNewMessage(mMessageSdt.getText().toString());
            }
        }
    }

    private void sendNewMessage(String message) {
        post_param.put("vendor_id", sessionManagement.getVendorid());
        post_param.put("hashkey", sessionManagement.getHahkey());
        post_param.put("quote_id", quote_id);
        post_param.put("message", message);
        Ced_MultiVendor_ClientRequestResponse requestResponse = new Ced_MultiVendor_ClientRequestResponse(output -> {
            JSONObject jsonObject = new JSONObject(output.toString()).getJSONObject("data");
            if (jsonObject.getBoolean("success")) {
                Toast.makeText(MessageActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                EventBus.getDefault().post("sent");
                finish();
            } else {
                Toast.makeText(MessageActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
            }
        }, MessageActivity.this, "POST", post_param);
        requestResponse.execute(message_url);
    }
}