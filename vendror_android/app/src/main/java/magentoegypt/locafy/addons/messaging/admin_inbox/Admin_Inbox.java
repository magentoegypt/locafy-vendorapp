package magentoegypt.locafy.addons.messaging.admin_inbox;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.reflect.TypeToken;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.Ced_ClientRequestResponseRest_New;
import magentoegypt.locafy.R;
import magentoegypt.locafy_constant.AppConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

public class Admin_Inbox extends Ced_MultiVendor_NavigationActivity {

    private final JSONObject postdata = new JSONObject();
    private final Type type = new TypeToken<List<Admin_Inbox_Model>>() {
    }.getType();
    LinearLayoutManager linearLayoutManager;
    private String admin_inbox_url = "";
    private RecyclerView admin_message_list;
    private Admin_Inbox_Adapter adapter;
    private AppCompatTextView error_message;
    private int page_num = 1;
    private String datafilterjson = "";
    private boolean firstcall = true;
    private AppCompatEditText sender_value, reciever_value, subject_value;
    private AppCompatTextView created_at_to, created_at_from, updated_at_to, updated_at_from;
    private AppCompatButton resetfilter, applyfilter;
    private JSONArray admin_inbox_array;
    private List<Admin_Inbox_Model> adminInboxList;
    private boolean noRecords=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
        getLayoutInflater().inflate(R.layout.admin_inbox_listing, content, true);
        admin_inbox_url = session.getBase_Url() + "rest/V1/vmessgingapi/admininbox";
        FloatingActionButton compose_message = findViewById(R.id.compose_message);
        admin_message_list = findViewById(R.id.loan_documents_list);
        ImageView filter_button = findViewById(R.id.filter_btn);
        error_message = findViewById(R.id.error_message);
        linearLayoutManager = new LinearLayoutManager(Admin_Inbox.this);
        admin_message_list.setLayoutManager(linearLayoutManager);

        if (getIntent().getStringExtra("filter") != null) {
            datafilterjson = getIntent().getStringExtra("filter");
            try {
                postdata.put("filter", datafilterjson);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try {
            postdata.put("vendor_id", session.getVendorid());
            request_page_date();
        }

        catch (JSONException e) {
            e.printStackTrace();
        }

        filter_button.setOnClickListener(view -> {
            final Dialog filter_dialog = new Dialog(Admin_Inbox.this,R.style.PauseDialog);
//                filter_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            filter_dialog.setTitle(getResources().getString(R.string.alert_name));
            filter_dialog.setContentView(R.layout.admin_inbox_filter);

            created_at_to = filter_dialog.findViewById(R.id.created_at_to);
            created_at_from = filter_dialog.findViewById(R.id.created_at_from);
            updated_at_from = filter_dialog.findViewById(R.id.updated_at_from);
            updated_at_to = filter_dialog.findViewById(R.id.updated_at_to);

            sender_value = filter_dialog.findViewById(R.id.sender_value);
            reciever_value = filter_dialog.findViewById(R.id.reciever_value);
            subject_value = filter_dialog.findViewById(R.id.subject_value);

            resetfilter = filter_dialog.findViewById(R.id.resetfilter);
            applyfilter = filter_dialog.findViewById(R.id.applyfilter);

            created_at_from.setOnClickListener(view16 -> AppConstant.getdate(Admin_Inbox.this, created_at_from));

            created_at_to.setOnClickListener(view15 -> AppConstant.getdate(Admin_Inbox.this, created_at_to));
            updated_at_from.setOnClickListener(view14 -> AppConstant.getdate(Admin_Inbox.this, updated_at_from));

            updated_at_to.setOnClickListener(view13 -> AppConstant.getdate(Admin_Inbox.this, updated_at_to));

            if (!datafilterjson.isEmpty()) {
                try {
                    JSONObject jsonObject = new JSONObject(datafilterjson);
                    sender_value.setText(jsonObject.getString("sender_name"));
                    reciever_value.setText(jsonObject.getString("receiver_name"));
                    created_at_from.setText(jsonObject.getJSONObject("created_at").getString("from"));
                    created_at_to.setText(jsonObject.getJSONObject("created_at").getString("to"));
                    updated_at_from.setText(jsonObject.getJSONObject("updated_at").getString("from"));
                    updated_at_to.setText(jsonObject.getJSONObject("updated_at").getString("to"));
                    subject_value.setText(jsonObject.getString("subject"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            applyfilter.setOnClickListener(view12 -> {
                JSONObject mainfilter = new JSONObject();
                try {
                    filter_dialog.dismiss();
                    mainfilter.put("sender_name", Objects.requireNonNull(sender_value.getText()).toString());
                    mainfilter.put("receiver_name", Objects.requireNonNull(reciever_value.getText()).toString());

                    JSONObject periodjsonObject = new JSONObject();
                    periodjsonObject.put("from", created_at_from.getText().toString());
                    periodjsonObject.put("to", created_at_to.getText().toString());
                    mainfilter.put("created_at", periodjsonObject);

                    periodjsonObject = new JSONObject();
                    periodjsonObject.put("from", updated_at_from.getText().toString());
                    periodjsonObject.put("to", updated_at_to.getText().toString());
                    mainfilter.put("updated_at", periodjsonObject);

                    mainfilter.put("subject", Objects.requireNonNull(subject_value.getText()).toString());

                    Intent intent = new Intent(getApplicationContext(), Admin_Inbox.class);
                    intent.putExtra("filter", mainfilter.toString());
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            resetfilter.setOnClickListener(view1 -> {
                filter_dialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), Admin_Inbox.class);
                intent.putExtra("filter", "");
                startActivity(intent);
            });
            filter_dialog.show();
        });

        admin_message_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (noRecords){
                    noRecords=false;
                    finish();
                    startActivity(getIntent());
                }
                else {
                    if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                        Log.i("REpo", "1 canscroll");
                        ++page_num;
                        try {
                            request_page_date();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        compose_message.setOnClickListener(v -> {
            Intent intent = new Intent(Admin_Inbox.this, Admin_Inbox_ComposeView.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
    }

    public void request_page_date() throws JSONException {
        postdata.put("page", "" + page_num);
        Ced_ClientRequestResponseRest_New response = new Ced_ClientRequestResponseRest_New(output -> {
            Log.i("REpo", "225_output : " + output);
            JSONObject jsonObject = new JSONObject(output.toString());
            JSONObject jsonObject_data = jsonObject.getJSONObject("vendor_data");
            if (jsonObject_data.getString("success").equals("true")) {
                if (firstcall) {
                    firstcall = false;
                    admin_inbox_array = jsonObject_data.getJSONArray("inbox_list");
                    adminInboxList = converter.fromJson(admin_inbox_array.toString(), type);
                    adapter = new Admin_Inbox_Adapter(Admin_Inbox.this, adminInboxList);
                    admin_message_list.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                else {
                    admin_inbox_array = jsonObject_data.getJSONArray("inbox_list");
                    adminInboxList = converter.fromJson(admin_inbox_array.toString(), type);
                    adapter.list.addAll(adminInboxList);
                    adapter.notifyDataSetChanged();
                }
                Log.i("REpo", "processFinish: " + adminInboxList);
            }
            else {
                if (firstcall) {
                    if (jsonObject_data.has("message")) {
                        admin_message_list.setVisibility(View.GONE);
                        error_message.setVisibility(View.VISIBLE);
                        error_message.setText(jsonObject_data.getString("message"));
                        Log.i("REpo", "" + jsonObject_data.getString("message"));
                    }
                }
                else {
                    noRecords=true;
                    Toast.makeText(Admin_Inbox.this, getResources().getString(R.string.nomoredata), Toast.LENGTH_SHORT).show();
                    Log.i("REpo", "" + jsonObject_data.getString("message"));
                }
            }
        }, Admin_Inbox.this, "POST", "" + postdata);
        response.execute(admin_inbox_url);
    }
}