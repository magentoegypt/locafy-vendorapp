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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import magentoegypt.locafy_constant.AppConstant;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by developer on 28/6/16.
 */
public class Ced_MultiVendor_ManageOrderview extends Ced_MultiVendor_NavigationActivity {
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    String url, out, header_meassage, button_label = "";
    HashMap<String, String> postdata;
    Ced_MultiVendor_FontSetting fontSetting;
    String vendor_id, post_id, hashkey;
    ImageView generate_button;
    Button CommentHistory, Shipments, CreditMemo, Invoices, Information, order_increment_id;
    JSONObject jsonObject;
    JSONArray orderdata, buttons;
    List<String> buttonlabellist;
    ArrayList<JSONObject> buttonlabelValue;
    String confirm_url = "", cancel_url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
            window.setStatusBarColor(this.getResources().getColor(R.color.status_bar_color));
        }
        connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(getApplicationContext());
        fontSetting = new Ced_MultiVendor_FontSetting();
        postdata = new HashMap<>();
        buttonlabellist = new ArrayList<>();
        buttonlabelValue = new ArrayList<>();
        if (connectionDetector.isConnectingToInternet()) {
            ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
            getLayoutInflater().inflate(R.layout.ced_multivendor_activity_manageorder_view, content, true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);
            if (session.getvendorname() != null) {
                setname(session.getvendorname());
                setprofilepic(session.getvendorpic());
                changetitle("Orders");
            }
            vendor_id = session.getVendorid();
            final HashMap<String, String> user = session.getUserDetails();
            hashkey = user.get(Ced_MultiVendor_VendorSessionManagement.Key_HAsh);
            postdata.put("hashkey", hashkey);
            postdata.put("vendor_id", vendor_id);
            url = session.getBase_Url() + "vorderapi/vorders/viewinitOrder";
            confirm_url = session.getBase_Url() + "vslapi/order/confirm";
            cancel_url = session.getBase_Url() + "vslapi/order/cancel";
            post_id = getIntent().getStringExtra("order_id");
            postdata.put("order_id", post_id);
            request();
            generate_button = findViewById(R.id.MultiVendor_generate_button);
            Information = findViewById(R.id.MultiVendor_Information);
            Invoices = findViewById(R.id.MultiVendor_Invoices);
            CreditMemo = findViewById(R.id.MultiVendor_CreditMemo);
            Shipments = findViewById(R.id.MultiVendor_Shipments);
            CommentHistory = findViewById(R.id.MultiVendor_CommentHistory);
            order_increment_id = findViewById(R.id.MultiVendor_order_increment_id);
            Information.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppConstant.lockButton(v);
                    Intent information_view = new Intent(Ced_MultiVendor_ManageOrderview.this, Ced_MultiVendor_Information_View.class);
                    information_view.putExtra("order_id", post_id);
                    startActivity(information_view);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }
            });
            Invoices.setOnClickListener(v -> {
                AppConstant.lockButton(v);
                Intent information_view = new Intent(Ced_MultiVendor_ManageOrderview.this, Ced_MultiVendor_Invoice_Information.class);
                information_view.putExtra("order_id", post_id);
                startActivity(information_view);
                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
            });
            CreditMemo.setOnClickListener(v -> {
                AppConstant.lockButton(v);
                Intent information_view = new Intent(Ced_MultiVendor_ManageOrderview.this, Ced_MultiVendor_CreditMemo_Information.class);
                information_view.putExtra("order_id", post_id);
                startActivity(information_view);
                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
            });
            Shipments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppConstant.lockButton(v);
                    Intent information_view = new Intent(Ced_MultiVendor_ManageOrderview.this, Ced_MultiVendor_Shipment_Information.class);
                    information_view.putExtra("order_id", post_id);
                    startActivity(information_view);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }
            });
            CommentHistory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppConstant.lockButton(v);
                    Intent information_view = new Intent(Ced_MultiVendor_ManageOrderview.this, Ced_MultiVendor_CommentsHistory.class);
                    information_view.putExtra("order_id", post_id);
                    startActivity(information_view);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }
            });
            fontSetting.setfontforButtons(order_increment_id, "Roboto-Black.ttf", getApplicationContext());
            fontSetting.setfontforButtons(Information, "Roboto-Black.ttf", getApplicationContext());
            fontSetting.setfontforButtons(Invoices, "Roboto-Black.ttf", getApplicationContext());
            fontSetting.setfontforButtons(CreditMemo, "Roboto-Black.ttf", getApplicationContext());
            fontSetting.setfontforButtons(Shipments, "Roboto-Black.ttf", getApplicationContext());
            fontSetting.setfontforButtons(CommentHistory, "Roboto-Black.ttf", getApplicationContext());
        } else {
            Intent nointernet = new Intent(getApplicationContext(), Ced_MultiVendor_NoInternetconnection.class);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            nointernet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(nointernet);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }
    }

    private void request() {
        Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                if (functionalityList.getExtensionAddon()) {
                    out = output.toString();

                    orderviewdata();

                } else {
                    Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }
            }
        }, Ced_MultiVendor_ManageOrderview.this, "POST", postdata);
        response.execute(url);
    }

    private void orderviewdata() throws JSONException {
        jsonObject = new JSONObject(out);
        if (jsonObject.has("vendor_approved")) {
            logout();
        } else {
            Log.e("frozen", "data=" + jsonObject.getJSONObject("data"));
            if (jsonObject.getJSONObject("data").getBoolean("success")) {
                if (jsonObject.getJSONObject("data").has("valerts")) {
                    EventBus.getDefault().post(jsonObject.getJSONObject("data").getString("valerts"));
                }

                orderdata = jsonObject.getJSONObject("data").getJSONArray("ordersData");
                for (int i = 0; i < orderdata.length(); i++) {
                    JSONObject c = null;
                    c = orderdata.getJSONObject(i);
                    header_meassage = c.getString("header_meassage");
                    if (!jsonObject.getJSONObject("data").has("sla_button")) {
                        if (c.has("buttons")) {
                            buttonlabellist.clear();
                            buttonlabelValue.clear();
                            buttons = c.getJSONArray("buttons");
                            for (int j = 0; j < buttons.length(); j++) {
                                JSONObject object = null;
                                object = buttons.getJSONObject(j);
                                buttonlabellist.add(object.getString("label"));
                                buttonlabelValue.add(object);
                            }
                        } else {
                            generate_button.setVisibility(View.GONE);

                        }

                    } else if (jsonObject.getJSONObject("data").has("sla_button")) {
                        final Dialog dialog = new Dialog(Ced_MultiVendor_ManageOrderview.this);
                        // Include dialog.xml file
                        dialog.setContentView(R.layout.dialog);
                        // Set dialog title
                        LinearLayout btn_section = dialog.findViewById(R.id.btn_section);
                        // dialog.setTitle("Change Order Status");
                        dialog.setTitle("Cambiar estado del pedido");
                        dialog.setCancelable(false);
                        for (int sla_btn = 0; sla_btn < jsonObject.getJSONObject("data").getJSONArray("sla_button").length(); sla_btn++) {
                            final Button btn = new Button(Ced_MultiVendor_ManageOrderview.this);
                            btn.setText(jsonObject.getJSONObject("data").getJSONArray("sla_button").getString(sla_btn));
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    AppConstant.lockButton(view);
                                    if (btn.getText().toString().equals("Confirm")) {
                                        Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
                                            @Override
                                            public void processFinish(Object output) throws JSONException {
                                                JSONObject jsonObject = new JSONObject(output.toString());
                                                if (jsonObject.getJSONObject("data").getString("success").equals("true")) {
                                                    Toast.makeText(Ced_MultiVendor_ManageOrderview.this, jsonObject.getJSONObject("data").getString("message"), Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(Ced_MultiVendor_ManageOrderview.this, jsonObject.getJSONObject("data").getString("message"), Toast.LENGTH_SHORT).show();
                                                }
//                                                Intent intent = new Intent(Ced_MultiVendor_ManageOrderview.this, Ced_MultiVendor_ManageOrdersList.class);
//                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                                startActivity(intent);
                                            }
                                        }, Ced_MultiVendor_ManageOrderview.this, "POST", postdata);
                                        response.execute(confirm_url);
                                        dialog.dismiss();
                                    } else if (btn.getText().toString().equals("Cancel")) {
                                        Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
                                            @Override
                                            public void processFinish(Object output) throws JSONException {

                                                JSONObject jo = new JSONObject(output.toString());

                                                if (jo.getJSONObject("data").getString("success").equals("true")) {
                                                    Toast.makeText(Ced_MultiVendor_ManageOrderview.this, jo.getJSONObject("data").getString("message"), Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(Ced_MultiVendor_ManageOrderview.this, jo.getJSONObject("data").getString("message"), Toast.LENGTH_SHORT).show();
                                                }
                                                Intent intent = new Intent(Ced_MultiVendor_ManageOrderview.this, Ced_MultiVendor_ManageOrdersList.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);

                                            }
                                        }, Ced_MultiVendor_ManageOrderview.this, "POST", postdata);
                                        response.execute(cancel_url);
                                        dialog.dismiss();
                                    }
                                }
                            });
                            btn_section.addView(btn);
                        }
                       // dialog.show();
                    }
                }
                order_increment_id.setText(header_meassage);
                generate_button.setOnClickListener(v -> {
                    AppConstant.lockButton(v);

                    final CharSequence[] arrayOfInt2 = buttonlabellist.toArray(new CharSequence[buttonlabellist.size()]);

                    Dialog levelDialog1 = new Dialog(Ced_MultiVendor_ManageOrderview.this);
                    final AlertDialog.Builder builder = new AlertDialog.Builder(Ced_MultiVendor_ManageOrderview.this);
//                        builder.setTitle(Html.fromHtml("<font color='#000000'>Choose Action </font>"));
                    builder.setTitle(Html.fromHtml(getString(R.string.choose_action_txt)));

                    builder.setSingleChoiceItems(arrayOfInt2, -1, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int postion) {

                                    button_label = (String) arrayOfInt2[postion];
                                    if (getLabelValue(button_label).equals("creditmemo")) {
                                        Intent credit_intent = new Intent(Ced_MultiVendor_ManageOrderview.this, Ced_MultiVendor_CreateCreditMemo.class);
                                        credit_intent.putExtra("order_id", post_id);
                                        startActivity(credit_intent);
                                        overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                                    }
                                    if (getLabelValue(button_label).equals("invoice")) {
                                        Intent invoice_creation = new Intent(Ced_MultiVendor_ManageOrderview.this, Ced_MultiVendor_CreateInvoice.class);
                                        invoice_creation.putExtra("order_id", post_id);
                                        startActivity(invoice_creation);
                                        overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);

                                    }
                                    if (getLabelValue(button_label).equals("ship")) {
                                        Intent shipment_creation = new Intent(Ced_MultiVendor_ManageOrderview.this, Ced_MultiVendor_CreateShipment.class);
                                        shipment_creation.putExtra("order_id", post_id);
                                        startActivity(shipment_creation);
                                        overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                                    }
                                    dialog.dismiss();
                                }


                            }

                    );
                    levelDialog1 = builder.create();
                    levelDialog1.show();

                });

            } else {

            }
        }
    }

    private String getLabelValue(String button_label) {
        String value = "";
        for (int a = 0; a < buttonlabelValue.size(); a++) {
            JSONObject jsonObject = buttonlabelValue.get(a);
            try {
                if (jsonObject.getString("label").equals(button_label))
                    value = jsonObject.getString("value");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.d("TAG", "getLabelValue: "+value);
        return value;
    }


    @Override
    protected void onResume() {
        Ced_MultiVendor_ConnectionDetector connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        if (connectionDetector.isConnectingToInternet()) {
            setname(session.getvendorname());
            setprofilepic(session.getvendorpic());
            changetitle("Orders");
            // invalidateOptionsMenu();

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
