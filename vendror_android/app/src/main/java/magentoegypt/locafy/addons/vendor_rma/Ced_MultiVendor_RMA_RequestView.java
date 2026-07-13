package magentoegypt.locafy.addons.vendor_rma;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by cedcoss on 10/1/18.
 */
public class Ced_MultiVendor_RMA_RequestView extends Ced_MultiVendor_NavigationActivity {
    public static boolean permissionsgranted = false;
    public static JSONArray chat_history;
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    String url = "";
    Button rmarequestid, order_info, customer_info, item_info, chat_information, chat_info_title, activity_info_title;
    ListView activity_info_list, chat_info_list, item_info_list;
    Button saverequest, send_message, browse_file;
    EditText chat_message;
    TextView file_name, chat_seeall;
    TextView orderid,orderStatus_text, MultiVendor_PurchasedFrom, reason, package_condition;
    TextView customer_name, customer_email, customer_group, MultiVendor_billing_ship_to, MultiVendor_billing_street, MultiVendor_billing_city,
            MultiVendor_billing_state, MultiVendor_billing_pincode, MultiVendor_billing_country, MultiVendor_billing_mobile;
    Spinner orderStatus_spinner, resolution_requested;
    ArrayList<String> orderstatus, resolution;
    ScrollView mainscroll;
    /*********************************************Imageupload**************************************/
    int writeresult, readresult, id;
    ArrayList<String> uris;
    String fileURI = "nouri";
    /*********************************************Imageupload**************************************/
    String str_vendor_id, str_post_id, hashkey, out;
    String str_rmarequestid, order_status, resolution_req;
    Ced_MultiVendor_FontSetting fontSetting;
    HashMap<String, String> postdata;
    JSONObject jsonObject;
    ArrayList<HashMap<String, String>> Orderinfo;
    private LinearLayoutCompat newChatSection;

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
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
        Orderinfo = new ArrayList<>();
        uris = new ArrayList<String>();
        postdata = new HashMap<>();
        chat_history = new JSONArray();
        orderstatus = new ArrayList<>();
        resolution = new ArrayList<>();

        if (connectionDetector.isConnectingToInternet()) {
            ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
            getLayoutInflater().inflate(R.layout.ced_multivendor_activity_rma_request_view, content, true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);

            if (session.getvendorname() != null) {
                setname(session.getvendorname());
                setprofilepic(session.getvendorpic());
                changetitle("Order View");
            }

            /*************************************************imageupload***************************/
            writeresult = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            readresult = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
            id = Resources.getSystem().getIdentifier("btn_check_holo_light", "drawable", "android");

            /*************************************************************************************/

            str_vendor_id = session.getVendorid();
            final HashMap<String, String> user = session.getUserDetails();
            hashkey = user.get(Ced_MultiVendor_VendorSessionManagement.Key_HAsh);
            postdata.put("hashkey", hashkey);
            postdata.put("vendor_id", str_vendor_id);

            url = session.getBase_Url() + "vrmapi/index/view";

            str_post_id = getIntent().getStringExtra("rmarequestid");
            postdata.put("rma_id", str_post_id);

            rmarequestid = findViewById(R.id.rmarequestid);
            order_info = findViewById(R.id.order_info);
            customer_info = findViewById(R.id.customer_info);
            item_info = findViewById(R.id.item_info);
            chat_information = findViewById(R.id.chat_information);
            chat_info_title = findViewById(R.id.chat_info_title);
            activity_info_title = findViewById(R.id.activity_info_title);
            orderStatus_text = findViewById(R.id.orderStatus_text);
            newChatSection = findViewById(R.id.newChatSection);
            saverequest = findViewById(R.id.saverequest);
            send_message = findViewById(R.id.send_message);
            browse_file = findViewById(R.id.browse_file);

            activity_info_list = findViewById(R.id.activity_info_list);
            chat_info_list = findViewById(R.id.chat_info_list);
            item_info_list = findViewById(R.id.item_info_list);

            chat_message = findViewById(R.id.chat_message);
            file_name = findViewById(R.id.file_name);
            chat_seeall = findViewById(R.id.chat_seeall);

            orderid = findViewById(R.id.orderid);
            MultiVendor_PurchasedFrom = findViewById(R.id.MultiVendor_PurchasedFrom);
            orderStatus_spinner = findViewById(R.id.orderStatus_spinner);
            reason = findViewById(R.id.reason);
            package_condition = findViewById(R.id.package_condition);
            resolution_requested = findViewById(R.id.resolution_requested);

            customer_name = findViewById(R.id.customer_name);
            customer_email = findViewById(R.id.customer_email);
            customer_group = findViewById(R.id.customer_group);
            MultiVendor_billing_ship_to = findViewById(R.id.MultiVendor_billing_ship_to);
            MultiVendor_billing_street = findViewById(R.id.MultiVendor_billing_street);
            MultiVendor_billing_city = findViewById(R.id.MultiVendor_billing_city);
            MultiVendor_billing_state = findViewById(R.id.MultiVendor_billing_state);
            MultiVendor_billing_pincode = findViewById(R.id.MultiVendor_billing_pincode);
            MultiVendor_billing_country = findViewById(R.id.MultiVendor_billing_country);
            MultiVendor_billing_mobile = findViewById(R.id.MultiVendor_billing_mobile);

            mainscroll = findViewById(R.id.MultiVendor_mainscroll);
            fontSetting.setfontforButtons(rmarequestid, "Roboto-Bold.ttf", getApplicationContext());

            str_rmarequestid = getIntent().getStringExtra("rma_id");
            rmarequestid.setText("#" + str_rmarequestid);

            String configval_url = session.getBase_Url() + "vrmapi/index/configval";

            Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
                @Override
                public void processFinish(Object output) throws JSONException {

                    JSONObject configval = new JSONObject(output.toString());

                    JSONObject status = configval.getJSONObject("status");
                    Iterator<?> statuskeys = status.keys();
                    while (statuskeys.hasNext()) {
                        orderstatus.add(status.getString(statuskeys.next().toString()));
                    }

                    JSONObject resolutiondata = configval.getJSONObject("resolution");
                    Iterator<?> resolutionkeys = resolutiondata.keys();
                    while (resolutionkeys.hasNext()) {
                        resolution.add(resolutiondata.getString(resolutionkeys.next().toString()));
                    }

                    request();

                }


            }, Ced_MultiVendor_RMA_RequestView.this, "GET");
            response.execute(configval_url);

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
                    Log.i("REpo", "requestview : " + output.toString());
                    out = output.toString();
                    requestviewdata();
                } else {
                    Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }
            }


        }, Ced_MultiVendor_RMA_RequestView.this, "POST", postdata);
        response.execute(url);
    }

    private void requestviewdata() {
        try {
            jsonObject = new JSONObject(out);
            if (jsonObject.getJSONObject("data").getBoolean("success")) {

                JSONObject order_info = jsonObject.getJSONObject("data").getJSONObject("order_info");
                orderid.setText(order_info.getString("order_id"));
                MultiVendor_PurchasedFrom.setText(order_info.getString("purchased_point"));

                reason.setText(order_info.getString("reason"));
                package_condition.setText(order_info.getString("package_condition"));

                ArrayAdapter<String> orderadapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_dropdown_item, orderstatus);
                orderStatus_spinner.setAdapter(orderadapter);
                orderStatus_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        order_status = parent.getSelectedItem().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                order_status = order_info.getString("status");
                orderStatus_text.setText(order_status);
                if (/*order_status.equals("Cancelled") && */order_status.equals("Completed")) {
                    saverequest.setVisibility(View.GONE);
                    newChatSection.setVisibility(View.GONE);
                }
                else {
                    saverequest.setVisibility(View.VISIBLE);
                    newChatSection.setVisibility(View.VISIBLE);
                }

                int setPos1 = orderadapter.getPosition(order_status);
                orderStatus_spinner.setSelection(setPos1);
                Log.i("REpo", "spinner : " + setPos1 + "\n" + order_status + "\n" + orderstatus);
                ArrayAdapter<String> resolutionadapter;
//                if (resolution.size() > 0) {
                resolution_req = order_info.getString("resolution_requested");
                ArrayList<String> res_status = new ArrayList<>();
                res_status.add(resolution_req);
                resolutionadapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_dropdown_item, res_status);
//                resolutionadapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_dropdown_item, resolution);
                resolution_requested.setAdapter(resolutionadapter);
                resolution_requested.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        resolution_req = parent.getSelectedItem().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
//                }


                int setPos2 = resolutionadapter.getPosition(resolution_req);
                resolution_requested.setSelection(setPos2);
                Log.i("REpo", "spinner : " + setPos2 + "\n" + resolution_req + "\n" + resolution);

                JSONObject customer_info = jsonObject.getJSONObject("data").getJSONObject("customer_info");
                customer_name.setText(customer_info.getString("customer_name"));
                customer_email.setText(customer_info.getString("customer_email"));
                customer_group.setText(customer_info.getString("group"));
                JSONObject customerAddr = customer_info.getJSONObject("address");
                MultiVendor_billing_ship_to.setText(customerAddr.getString("bill_to"));
                MultiVendor_billing_street.setText(customerAddr.getString("street"));
                MultiVendor_billing_city.setText(customerAddr.getString("city"));
                MultiVendor_billing_state.setText(customerAddr.getString("state"));
                MultiVendor_billing_pincode.setText(customerAddr.getString("pincode"));
                MultiVendor_billing_country.setText(customerAddr.getString("country"));
                MultiVendor_billing_mobile.setText(customerAddr.getString("mobile"));

                JSONArray item_info = jsonObject.getJSONObject("data").getJSONArray("item_info");
                Ced_MultiVendor_ItemInfoAdapter itemInfoAdapter = new Ced_MultiVendor_ItemInfoAdapter(Ced_MultiVendor_RMA_RequestView.this, item_info);
                item_info_list.setAdapter(itemInfoAdapter);
                //  setListViewHeightBasedOnChildren(item_info_list);

                storagepermission();
                if (jsonObject.getJSONObject("data").has("chat_history")) {
                    chat_history = jsonObject.getJSONObject("data").getJSONArray("chat_history");
                }

                Ced_MultiVendor_ChatHistoryAdapter chatHistoryAdapter = new Ced_MultiVendor_ChatHistoryAdapter(Ced_MultiVendor_RMA_RequestView.this, chat_history);
                chat_info_list.setAdapter(chatHistoryAdapter);
                setListViewHeightBasedOnChildren(chat_info_list);

                chat_seeall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppConstant.lockButton(v);
                        Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_Viewall_Chat.class);
                        startActivity(intent);
                    }
                });

                JSONArray activity_history = jsonObject.getJSONObject("data").getJSONArray("activity_history");
                Ced_MultiVendor_ActivityHistoryAdapter actHistoryAdapter = new Ced_MultiVendor_ActivityHistoryAdapter(Ced_MultiVendor_RMA_RequestView.this, activity_history);
                activity_info_list.setAdapter(actHistoryAdapter);
                setListViewHeightBasedOnChildren(activity_info_list);

                show();
                saverequest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        AppConstant.lockButton(v);
                        updatedata();
                    }
                });

                send_message.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppConstant.lockButton(v);
                        try {
                            sendMessage();
                            /*Intent i = new Intent(Ced_MultiVendor_RMA_RequestView.this,Ced_MultiVendor_RMA_RequestView.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            overridePendingTransition(R.anim.ced_multivendor_slide_in,R.anim.ced_multivendor_slide_out);*/
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                browse_file.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppConstant.lockButton(v);
                        addimageLayout();
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), jsonObject.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage() throws JSONException {

        String chatUrl = session.getBase_Url() + "vrmapi/index/chat";
        postdata.put("chat", chat_message.getText().toString());

        if (!fileURI.equals("nouri")) {
            JSONObject imageobj = new JSONObject();
            String encoded_id = encodefiletoBase64(fileURI);
            imageobj.put("name", file_name.getText().toString());
            imageobj.put("base64_encoded_data", encoded_id);
            Log.i("REpo", "image" + imageobj);
            postdata.put("image", imageobj.toString());
        }

        Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                if (functionalityList.getExtensionAddon()) {
                    Log.i("REpo", "requestview : " + output.toString());
                    if (new JSONObject(output.toString()).getJSONObject("data").getString("success").equalsIgnoreCase("true")) {
                        Toast.makeText(Ced_MultiVendor_RMA_RequestView.this, "" + new JSONObject(output.toString()).getJSONObject("data").getString("message"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Ced_MultiVendor_RMA_RequestView.this, "" + new JSONObject(output.toString()).getJSONObject("data").getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }
            }


        }, Ced_MultiVendor_RMA_RequestView.this, "POST", postdata);
        response.execute(chatUrl);
    }

    String encodefiletoBase64(String fileUri) {
        File file = new File(fileUri);
        int size = (int) file.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    public void storagepermission() {
        if (writeresult == PackageManager.PERMISSION_GRANTED && readresult == PackageManager.PERMISSION_GRANTED) {
            permissionsgranted = true;
        } else {
            AlertDialog.Builder alertdialog = new AlertDialog.Builder(Ced_MultiVendor_RMA_RequestView.this)
                    .setTitle(getResources().getString(R.string.Providepermission))
                    .setMessage(getResources().getString(R.string.Storagepermissionallow))
                    .setCancelable(true)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivityForResult(intent, 2);
                        }
                    })
                    .setNegativeButton(android.R.string.no, null);
            AlertDialog alert = alertdialog.create();
            alert.show();
        }
    }

    public void addimageLayout() {
        if (writeresult == PackageManager.PERMISSION_GRANTED && readresult == PackageManager.PERMISSION_GRANTED) {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, 1);
        } else {
            Toast.makeText(Ced_MultiVendor_RMA_RequestView.this, "Storage permission allows us to access data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivityForResult(intent, 1);
        }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, 1);
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_LONG).show();
                }
                break;

            case 2:
                Log.i("REpo", "onRequestPermissionsResult ");
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (1) {
            case 1:
                if (resultCode == RESULT_OK) {
                    try {
                        final Uri imageUri = imageReturnedIntent.getData();
                        String imageuristring = getRealPathFromURI(Ced_MultiVendor_RMA_RequestView.this, imageUri);
                        Log.i("REpo", "imageuri : " + imageuristring);
                        uris.add(imageuristring);
                        String[] imagename = getRealPathFromURI(Ced_MultiVendor_RMA_RequestView.this, imageUri).split("/");
                        file_name.setText(imagename[imagename.length - 1]);
                        Log.i("REpo", "imageName : " + imagename[imagename.length - 1] + "\n url : " + imageuristring);

                        fileURI = imageuristring;

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 2:

                Log.i("REpo", "onActivityResult ");
                break;

        }
    }

    private void updatedata() {

        postdata.put("status", order_status);
        postdata.put("resolution_requested", resolution_req);

        postdata.put("order_id", orderid.getText().toString());
        postdata.put("customer_name", customer_name.getText().toString());
        postdata.put("customer_email", customer_email.getText().toString());

        String updateUrl = session.getBase_Url() + "vrmapi/index/update";
        Ced_MultiVendor_ClientRequestResponse response = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                JSONObject obj = new JSONObject(output.toString());
                if (obj.getJSONObject("data").has("success") && obj.getJSONObject("data").getBoolean("success")) {
                    Intent intent = new Intent(Ced_MultiVendor_RMA_RequestView.this, Ced_MultiVendor_RMA_RequestView.class);
                    intent.putExtra("rmarequestid", str_post_id);
                    intent.putExtra("rma_id", str_rmarequestid);
                    startActivity(intent);
                    finish();
                }
                Toast.makeText(getApplicationContext(), obj.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();


            }


        }, Ced_MultiVendor_RMA_RequestView.this, "POST", postdata);
        response.execute(updateUrl);
    }

    public void show() {
        mainscroll.scrollTo(0, 0);
        rmarequestid.requestFocus();

    }

    @Override
    protected void onResume() {
        Ced_MultiVendor_ConnectionDetector connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        if (connectionDetector.isConnectingToInternet()) {
            setname(session.getvendorname());
            setprofilepic(session.getvendorpic());
            writeresult = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            readresult = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
            changetitle("Order View");
            //invalidateOptionsMenu();
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