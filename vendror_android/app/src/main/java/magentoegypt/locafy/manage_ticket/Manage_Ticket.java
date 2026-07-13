package magentoegypt.locafy.manage_ticket;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;


public class Manage_Ticket extends Ced_MultiVendor_NavigationActivity implements View.OnClickListener {

    JSONObject postdata;
    String view_url = "";
    GridLayoutManager layoutManager;
    Ced_MultiVendor_VendorSessionManagement vendorSessionManagement;
    int page_num = 1;
    boolean firstcall = true;
    String view_id;
    HashMap data_id, grp_data_id;
    Type type = new TypeToken<List<Ticket_Data_Model>>() {
    }.getType();
    List<Ticket_Data_Model> ticketDataList;
    Manage_Ticket_Adapter adapter;
    AppCompatTextView error_message;
    FloatingActionButton add_new_tkt;
    RecyclerView ticket_list;
    ImageView filter_btn;
    AppCompatEditText id_from, id_to, ticket_id_value, customer_name_value, customer_email_value, department_value, subject_value, num_of_message, order_value, priority_value, status_value;
    AppCompatTextView created_at_to, created_at_from;
    AppCompatButton resetfilter, applyfilter;
    private boolean request = true;
    private String datafilterjson = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
        getLayoutInflater().inflate(R.layout.manage_ticket_layout, content, true);

        view_url = session.getBase_Url() + "rest/V1/vsupportapi/list";
        layoutManager = new GridLayoutManager(Manage_Ticket.this, 1);
        vendorSessionManagement = new Ced_MultiVendor_VendorSessionManagement(Manage_Ticket.this);
        postdata = new JSONObject();
        data_id = new HashMap();
        grp_data_id = new HashMap();

        error_message = findViewById(R.id.error_message);
        add_new_tkt = findViewById(R.id.add_new_tkt);
        add_new_tkt.setOnClickListener(this);
        ticket_list = findViewById(R.id.ticket_list);
        ticket_list.setLayoutManager(new LinearLayoutManager(Manage_Ticket.this, LinearLayoutManager.VERTICAL, false));
        filter_btn = findViewById(R.id.filter_btn);

        filter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog filter_dialog = new Dialog(Manage_Ticket.this);
                filter_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                filter_dialog.setContentView(R.layout.manage_ticket_filter);

                id_to = filter_dialog.findViewById(R.id.id_to);
                id_from = filter_dialog.findViewById(R.id.id_from);

                created_at_to = filter_dialog.findViewById(R.id.created_at_to);
                created_at_from = filter_dialog.findViewById(R.id.created_at_from);

                status_value = filter_dialog.findViewById(R.id.transaction_id_value);
                priority_value = filter_dialog.findViewById(R.id.priority_value);
                order_value = filter_dialog.findViewById(R.id.order_value);
                num_of_message = filter_dialog.findViewById(R.id.num_of_message);
                department_value = filter_dialog.findViewById(R.id.department_value);
                customer_email_value = filter_dialog.findViewById(R.id.customer_email_value);
                customer_name_value = filter_dialog.findViewById(R.id.customer_name_value);
                ticket_id_value = filter_dialog.findViewById(R.id.ticket_id_value);

                subject_value = filter_dialog.findViewById(R.id.subject_value);

                resetfilter = filter_dialog.findViewById(R.id.resetfilter);
                applyfilter = filter_dialog.findViewById(R.id.applyfilter);

                created_at_from.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AppConstant.getdate(Manage_Ticket.this, created_at_from);
                    }
                });

                created_at_to.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AppConstant.getdate(Manage_Ticket.this, created_at_to);
                    }
                });

                if (!datafilterjson.isEmpty()) {
                    try {
                        JSONObject jsonObject = new JSONObject(datafilterjson);

                        id_from.setText(jsonObject.getJSONObject("id").getString("from"));
                        id_to.setText(jsonObject.getJSONObject("id").getString("to"));
                        ticket_id_value.setText(jsonObject.getString("ticket_id"));
                        created_at_from.setText(jsonObject.getJSONObject("created_time").getString("from"));
                        created_at_to.setText(jsonObject.getJSONObject("created_time").getString("to"));
                        customer_name_value.setText(jsonObject.getString("customer_name"));
                        customer_email_value.setText(jsonObject.getString("customer_email"));
                        department_value.setText(jsonObject.getString("department"));
                        subject_value.setText(jsonObject.getString("subject"));
                        num_of_message.setText(jsonObject.getString("num_msg"));
                        order_value.setText(jsonObject.getString("order_id"));
                        priority_value.setText(jsonObject.getString("priority"));
                        status_value.setText(jsonObject.getString("status"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                applyfilter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        JSONObject mainfilter = new JSONObject();
                        try {
                            JSONObject periodjsonObject = new JSONObject();
                            periodjsonObject.put("from", id_from.getText().toString());
                            periodjsonObject.put("to", id_to.getText().toString());
                            mainfilter.put("id", periodjsonObject);
                            mainfilter.put("ticket_id", ticket_id_value.getText().toString());
                            periodjsonObject = new JSONObject();
                            periodjsonObject.put("from", created_at_from.getText().toString());
                            periodjsonObject.put("to", created_at_to.getText().toString());
                            mainfilter.put("created_time", periodjsonObject);
                            mainfilter.put("customer_name", customer_email_value.getText().toString());
                            mainfilter.put("customer_email", customer_email_value.getText().toString());
                            mainfilter.put("department", department_value.getText().toString());
                            mainfilter.put("subject", subject_value.getText().toString());
                            mainfilter.put("num_msg", num_of_message.getText().toString());
                            mainfilter.put("order_id", order_value.getText().toString());
                            mainfilter.put("priority", priority_value.getText().toString());
                            mainfilter.put("status", status_value.getText().toString());

                            Intent intent = new Intent(getApplicationContext(), Manage_Ticket.class);
                            Log.i("REpo", "174_onClick: " + mainfilter);
                            intent.putExtra("filter", mainfilter.toString());
                            startActivity(intent);
                            filter_dialog.dismiss();
                            finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                resetfilter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        filter_dialog.dismiss();
                        Intent intent = new Intent(getApplicationContext(), Manage_Ticket.class);
                        intent.putExtra("filter", "");
                        startActivity(intent);
                    }
                });

                filter_dialog.show();
            }
        });

        try {
            if (getIntent().getStringExtra("filter") != null) {
                datafilterjson = getIntent().getStringExtra("filter");
                postdata.put("filter", datafilterjson);
            }
            postdata.put("vendor_id", vendorSessionManagement.getVendorid());
            request_page_date(page_num, request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void request_page_date(int page_count, final boolean call_request) {
//        postdata.put("page", "" + page_count);
        if (call_request) {

            Ced_ClientRequestResponseRest_New crr = new Ced_ClientRequestResponseRest_New(new AsyncResponse() {
                @Override
                public void processFinish(Object output) throws JSONException {

                    JSONObject root = new JSONObject(output.toString());

                    if (root.getString("success").equals("true")) {
                        firstcall = false;

                        JSONArray vendor_data = root.getJSONArray("vendor_data");
                        if (vendor_data.length() > 0) {
                            ticketDataList = converter.fromJson(vendor_data.toString(), type);
                            adapter = new Manage_Ticket_Adapter(Manage_Ticket.this, ticketDataList);
                            ticket_list.setAdapter(adapter);
                        } else {
                            ticket_list.setVisibility(View.GONE);
                        }

                        JSONArray spinner_data = null;
                        JSONObject jsonObject = null;
                        spinner_data = root.getJSONArray("status_list");
                        for (int i = 0; i < spinner_data.length(); i++) {
                            jsonObject = spinner_data.getJSONObject(i);
                            AppConstant.statusMap.put(jsonObject.getString("value"), jsonObject.getString("key"));
                            AppConstant.statusList.add(jsonObject.getString("value"));
                        }

                        spinner_data = root.getJSONArray("department_list");
                        for (int i = 0; i < spinner_data.length(); i++) {
                            jsonObject = spinner_data.getJSONObject(i);
                            AppConstant.departmentMap.put(jsonObject.getString("value"), jsonObject.getString("key"));
                            AppConstant.departmentList.add(jsonObject.getString("value"));
                        }

                        spinner_data = root.getJSONArray("priority_list");
                        for (int i = 0; i < spinner_data.length(); i++) {
                            jsonObject = spinner_data.getJSONObject(i);
                            AppConstant.priorityMap.put(jsonObject.getString("value"), jsonObject.getString("key"));
                            AppConstant.priorityList.add(jsonObject.getString("value"));
                        }
                    } else {
                        if (firstcall) {
                            if (root.has("message")) {
                                error_message.setVisibility(View.VISIBLE);
                                error_message.setText(root.getString("message"));
                            }
                        } else {
                            Toast.makeText(Manage_Ticket.this, root.getString("message"), Toast.LENGTH_SHORT).show();
                            request = false;
                        }
                    }

                }
            }, Manage_Ticket.this, "POST", postdata.toString());
            crr.execute(view_url);

        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.add_new_tkt){
            Intent intent = new Intent(Manage_Ticket.this, Create_New_Ticket.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
    }
}
