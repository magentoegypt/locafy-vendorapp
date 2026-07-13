/*
 *
 *   Copyright/* *
 *             * CedCommerce
 *             *
 *             * NOTICE OF LICENSE
 *             *
 *             * This source file is subject to the End User License Agreement (EULA)
 *             * that is bundled with this package in the file LICENSE.txt.
 *             * It is also available through the world-wide-web at this URL:
 *             * http://cedcommerce.com/license-agreement.txt
 *             *
 *             * @category  Ced
 *             * @package   MultiVendor
 *             * @author    CedCommerce Core Team <connect@cedcommerce.com >
 *             * @copyright Copyright CEDCOMMERCE (http://cedcommerce.com/)
 *             * @license   http://cedcommerce.com/license-agreement.txt
 *
 *
 *
 */

package magentoegypt.locafy.manage_orders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import magentoegypt.locafy.base_app.Ced_MultiVendor_FontSetting;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_UnAuthourizedRequestError;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;
import magentoegypt.locafy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by developer on 27/6/16.
 */


public class Ced_MultiVendor_CreateComment extends Ced_MultiVendor_NavigationActivity {
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    String url, out, status_url = "";
    HashMap<String, String> postdata;
    Ced_MultiVendor_FontSetting fontSetting;
    JSONObject jsonObject;
    String vendor_id, hashkey, post_id, status_label, status_value;
    JSONArray status, items;
    EditText comment_txt;
    Spinner status_edt;
    CheckBox NotifyCustomerbyEmail, VisibleonFrontend;
    Button submit_comment;
    LinearLayout commentstatus_linear;
    TextView status_head;
    List statuslabellist, statusvaluelist;
    CharSequence[] arrayOfInt;
    CharSequence[] arrayOfInt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_bar_color));
        }
        connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(getApplicationContext());
        fontSetting = new Ced_MultiVendor_FontSetting();
        postdata = new HashMap<>();
        statuslabellist = new ArrayList();
        statusvaluelist = new ArrayList();
        statuslabellist.add(getString(R.string.plsselectstatus));
        statusvaluelist.add("");

        if (connectionDetector.isConnectingToInternet()) {
            ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
            getLayoutInflater().inflate(R.layout.ced_multivendor_activity_comment_view, content, true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);
            submit_comment = findViewById(R.id.MultiVendor_submit_comment);
            NotifyCustomerbyEmail = findViewById(R.id.MultiVendor_NotifyCustomerbyEmail);
            VisibleonFrontend = findViewById(R.id.MultiVendor_VisibleonFrontend);
            comment_txt = findViewById(R.id.MultiVendor_comment_txt);
            status_edt = findViewById(R.id.MultiVendor_status_edt);
            commentstatus_linear = findViewById(R.id.MultiVendor_commentstatus_linear);
            status_head = findViewById(R.id.MultiVendor_status_head);

            // fontSetting.setfontforEditText(status_edt,"Roboto-Regular.ttf",getApplicationContext());
            fontSetting.setfontforEditText(comment_txt, "Roboto-Regular.ttf", getApplicationContext());
            fontSetting.setFontforTextviews(status_head, "Roboto-Bold.ttf", getApplicationContext());
            fontSetting.setfontforButtons(submit_comment, "Roboto-Medium.ttf", getApplicationContext());

            if (session.getvendorname() != null) {
                setname(session.getvendorname());
                setprofilepic(session.getvendorpic());
                changetitle("Create Comment");
            }
            if (getIntent().getStringExtra("invoice_id") != null) {
                post_id = getIntent().getStringExtra("invoice_id");
                postdata.put("invoice_id", post_id);
                url = session.getBase_Url() + "vorderapi/vinvoice/addComment";
            }
            if (getIntent().getStringExtra("creditmemo_id") != null) {
                post_id = getIntent().getStringExtra("creditmemo_id");
                postdata.put("creditmemo_id", post_id);
                url = session.getBase_Url() + "vorderapi/vcreditmemo/addComment";
            }
            if (getIntent().getStringExtra("shipment_id") != null) {
                post_id = getIntent().getStringExtra("shipment_id");
                postdata.put("shipment_id", post_id);
                url = session.getBase_Url() + "vorderapi/vshipment/addComment";
            }

            if (getIntent().getStringExtra("order_id") != null) {
                post_id = getIntent().getStringExtra("order_id");
                postdata.put("order_id", post_id);
                commentstatus_linear.setVisibility(View.VISIBLE);
                status_url = session.getBase_Url() + "vorderapi/vorders/viewCommentStatus";
                requestStatus();
                url = session.getBase_Url() + "vorderapi/vorders/addComment";
            }

            vendor_id = session.getVendorid();
            final HashMap<String, String> user = session.getUserDetails();
            hashkey = user.get(Ced_MultiVendor_VendorSessionManagement.Key_HAsh);
            postdata.put("hashkey", hashkey);
            postdata.put("vendor_id", vendor_id);

            NotifyCustomerbyEmail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (NotifyCustomerbyEmail.isChecked()) {
                        postdata.put("is_customer_notified", "true");
                    }
                }
            });
            VisibleonFrontend.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (VisibleonFrontend.isChecked()) {
                        postdata.put("is_visible_on_front", "true");
                    }
                }
            });

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, statuslabellist);
            status_edt.setAdapter(adapter);

            status_edt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0) {
                        status_label = "";
                        status_value = "";
                    } else {
                        status_label = (String) arrayOfInt[position];
                        status_value = (String) arrayOfInt2[position];
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    status_label = "";
                    status_value = "";
                }
            });

          /*  status_edt.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    AppConstant.lockButton(v);

                    Dialog levelDialog1 = new Dialog(Ced_MultiVendor_CreateComment.this);
                    final AlertDialog.Builder builder = new AlertDialog.Builder(Ced_MultiVendor_CreateComment.this);
                    builder.setTitle(Html.fromHtml("<font color='#000000'>Select Your  Comment Status:</font>"));

                    builder.setSingleChoiceItems(arrayOfInt, -1, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int postion)
                                {
                                    status_label = (String) arrayOfInt[postion];
                                    status_value = (String) arrayOfInt2[postion];
                                    status_edt.setText(status_label);
                                    dialog.dismiss();

                                }
                            }
                    );
                    levelDialog1 = builder.create();
                    levelDialog1.show();
                }
            });*/
            submit_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("frozen", "postdata1=" + postdata);
                    if (comment_txt.getText().toString().isEmpty()) {
                        comment_txt.setError(getResources().getString(R.string.plsentercomment));
                        comment_txt.requestFocus();
                        Log.e("frozen", "postdata2=" + postdata);
                    } else {
                        postdata.put("comment", comment_txt.getText().toString());
                        Log.e("frozen", "postdata3=" + postdata);
                        if (getIntent().getStringExtra("order_id") != null) {
                            Log.e("frozen", "postdata4=" + postdata);
                            if (status_label.equalsIgnoreCase("") || status_value.equalsIgnoreCase("")) {
                                status_edt.setSelection(0);
                                status_edt.requestFocus();
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.plsselectstatus), Toast.LENGTH_SHORT).show();
                                Log.e("frozen", "postdata5=" + postdata);
                            } else {
                                postdata.put("status", status_value);
                                Log.e("frozen", "postdata6=" + postdata);
                                request();
                            }
                        } else {
                            Log.e("frozen", "postdata7=" + postdata);
                            request();
                        }
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

    private void requestStatus() {
        Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                if (functionalityList.getExtensionAddon()) {
                    out = output.toString();
                    fetchstatus();
                } else {
                    Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }
            }


        }, Ced_MultiVendor_CreateComment.this, "POST", postdata);
        response.execute(status_url);
    }

    private void fetchstatus() throws JSONException {
        jsonObject = new JSONObject(out);
        if (jsonObject.has("vendor_approved")) {
            logout();
        } else {
            if (jsonObject.getJSONObject("data").getBoolean("success")) {

                status = jsonObject.getJSONObject("data").getJSONArray("status");
                for (int j = 0; j < status.length(); j++) {
                    JSONObject object = null;
                    object = status.getJSONObject(j);
                    statuslabellist.add(object.getString("label"));
                    statusvaluelist.add(object.getString("value"));
                }
            }
        }

        arrayOfInt = (CharSequence[]) statuslabellist.toArray(new CharSequence[statuslabellist.size()]);
        arrayOfInt2 = (CharSequence[]) statusvaluelist.toArray(new CharSequence[statusvaluelist.size()]);
//        status_edt.setText(arrayOfInt[0]);
    }

    private void request() {
        Log.e("frozen", "postdata8=" + postdata);
        Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                if (functionalityList.getExtensionAddon()) {
                    out = output.toString();
                    afterpost();
                } else {
                    Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }
            }


        }, Ced_MultiVendor_CreateComment.this, "POST", postdata);
        response.execute(url);
    }

    private void afterpost() throws JSONException {
        jsonObject = new JSONObject(out);
        if (jsonObject.has("vendor_approved")) {
            logout();
        } else {
            if (jsonObject.getJSONObject("data").getBoolean("success")) {

                if (getIntent().getStringExtra("invoice_id") != null) {
                    Intent intent = new Intent(Ced_MultiVendor_CreateComment.this, Ced_MultiVendor_InvoiceView.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("invoice_id", getIntent().getStringExtra("invoice_id"));
                    startActivity(intent);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }
                if (getIntent().getStringExtra("shipment_id") != null) {
                    Intent intent = new Intent(Ced_MultiVendor_CreateComment.this, Ced_MultiVendor_ShipmentView.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("shipment_id", getIntent().getStringExtra("shipment_id"));
                    startActivity(intent);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }
                if (getIntent().getStringExtra("creditmemo_id") != null) {
                    Intent intent = new Intent(Ced_MultiVendor_CreateComment.this, Ced_MultiVendor_CreditMemoView.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("creditmemo_id", getIntent().getStringExtra("creditmemo_id"));
                    startActivity(intent);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }
                if (getIntent().getStringExtra("order_id") != null) {
                    Intent intent = new Intent(Ced_MultiVendor_CreateComment.this, Ced_MultiVendor_Information_View.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("order_id", getIntent().getStringExtra("order_id"));
                    startActivity(intent);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }

                Toast.makeText(getApplicationContext(), jsonObject.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(getApplicationContext(), jsonObject.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onResume() {
        Ced_MultiVendor_ConnectionDetector connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        if (connectionDetector.isConnectingToInternet()) {
            setname(session.getvendorname());
            setprofilepic(session.getvendorpic());
            changetitle("Create Comment");
            //  invalidateOptionsMenu();
            super.onResume();

        } else {
            Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
            super.onResume();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
