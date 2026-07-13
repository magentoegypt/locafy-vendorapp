package magentoegypt.locafy.manage_ticket;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;

import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_ClientRequestResponseRest_New;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;
import magentoegypt.locafy.R;
import magentoegypt.locafy_constant.AppConstant;

import org.json.JSONException;
import org.json.JSONObject;

public class Create_New_Ticket extends Ced_MultiVendor_NavigationActivity implements View.OnClickListener {

    private Ced_MultiVendor_VendorSessionManagement vendorSessionManagement;
    private AppCompatEditText content, subject, order, customer_email, customer_name;
    private AppCompatSpinner priority, status, department;
    private AppCompatButton add_new_tkt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup container = findViewById(R.id.MultiVendor_frame_container);
        getLayoutInflater().inflate(R.layout.new_ticket_layout, container, true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        vendorSessionManagement = new Ced_MultiVendor_VendorSessionManagement(Create_New_Ticket.this);

        add_new_tkt = findViewById(R.id.add_new_tkt);
        add_new_tkt.setOnClickListener(this);

        priority = findViewById(R.id.priority);
        status = findViewById(R.id.status);
        department = findViewById(R.id.department);
        customer_name = findViewById(R.id.customer_name);
        customer_email = findViewById(R.id.customer_email);
        order = findViewById(R.id.order);
        subject = findViewById(R.id.subject);
        content = findViewById(R.id.content);

        customer_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                customer_name.setError(null);
            }
        });
        customer_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                customer_email.setError(null);
            }
        });
        subject.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                subject.setError(null);
            }
        });

        setUpSpinners();
    }

    private void setUpSpinners() {
        ArrayAdapter departmetAdapter = new ArrayAdapter(Create_New_Ticket.this, R.layout.spinner_item, AppConstant.departmentList);
        department.setAdapter(departmetAdapter);
        ArrayAdapter statusAdapter = new ArrayAdapter(Create_New_Ticket.this, R.layout.spinner_item, AppConstant.statusList);
        status.setAdapter(statusAdapter);
        ArrayAdapter priorityAdapter = new ArrayAdapter(Create_New_Ticket.this, R.layout.spinner_item, AppConstant.priorityList);
        priority.setAdapter(priorityAdapter);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.add_new_tkt){
            try {
                if (customer_name.getText().toString().trim().length() == 0) {
                    customer_name.setError(getResources().getString(R.string.nameisrequired));
                } else if (customer_email.getText().toString().trim().length() == 0) {
                    customer_email.setError(getResources().getString(R.string.emailisrequired));
                } else if (subject.getText().toString().trim().length() == 0) {
                    subject.setError(getResources().getString(R.string.emptysubject));
                } else {
                    saveTicket();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveTicket() throws JSONException {
        String saveURL = session.getBase_Url() + "rest/V1/vsupportapi/saveTicket";
        JSONObject ticketdata = new JSONObject();
        ticketdata.put("vendor_id", vendorSessionManagement.getVendorid());
        ticketdata.put("customer_name", customer_name.getText().toString());
        ticketdata.put("customer_email", customer_email.getText().toString());
        ticketdata.put("order_id", order.getText().toString());
        ticketdata.put("subject", subject.getText().toString());
        ticketdata.put("department", AppConstant.departmentMap.get(department.getSelectedItem().toString()));
        ticketdata.put("status", AppConstant.statusMap.get(status.getSelectedItem().toString()));
        ticketdata.put("priority", AppConstant.priorityMap.get(priority.getSelectedItem().toString()));
        ticketdata.put("message", content.getText().toString());

        Ced_ClientRequestResponseRest_New crr = new Ced_ClientRequestResponseRest_New(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                Log.i("REpo", "output.toString() : " + output.toString());
                JSONObject jsonObject = new JSONObject(output.toString());
                if (jsonObject.getString("success").equals("true")) {
                    Toast.makeText(Create_New_Ticket.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(Create_New_Ticket.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }

            }
        }, Create_New_Ticket.this, "POST", ticketdata.toString());
        crr.execute(saveURL);
    }
}
