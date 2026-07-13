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

package magentoegypt.locafy.vendor_notification;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.MenuItemCompat;

import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorSplash;
import magentoegypt.locafy_constant.Ced_MultiVendor_GlobalVariables;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.manage_products_section.Ced_MultiVendor_AddVendorProduct;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_NoInternetconnection;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_UnAuthourizedRequestError;
import magentoegypt.locafy.manage_orders.Ced_MultiVendor_ManageOrderview;
import magentoegypt.locafy.vendor_profile_section.Ced_MultiVendor_ProfileStatus;
import magentoegypt.locafy.vendor_profile_section.Ced_MultiVendor_VendorProfile;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.vendor_settings.Ced_MultiVendor_Settings;
import magentoegypt.locafy.vendor_settings.Ced_MultiVendor_ShippingSetting;
import magentoegypt.locafy.vendor_settings.ShippingMethodNew;
import magentoegypt.locafy.R;
import magentoegypt.locafy_constant.AppConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Notification extends Ced_MultiVendor_NavigationActivity {
    private final HashMap<String, String> post_data = new HashMap<>();
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    String Jstring;
    String currenturl = "";
    ArrayList<HashMap<String, String>> tagsandlabel;
    ListView notificationlistview;
    NotificationAdapters notificationAdapters;
    ImageView notify_image;
    int current = 1;
    private boolean load = true;
    private JSONArray orderAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_bar_color));
        }
        try {
            connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
            functionalityList = new Ced_MultiVendor_VendorFunctionalityList(getApplicationContext());

            if (connectionDetector.isConnectingToInternet()) {
                ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
                getLayoutInflater().inflate(R.layout.ced_multivendor_activity_notification, content, true);
                setname(session.getvendorname());
                setprofilepic(session.getvendorpic());
                changetitle("Notification");
                tagsandlabel = new ArrayList<HashMap<String, String>>();
                notificationlistview = findViewById(R.id.MultiVendor_notificationlistview);
                notify_image = findViewById(R.id.MultiVendor_notify_image);
                //    currenturl = session.getBase_Url() + "vendorapi/index/vendornotification/vendor_id/" + session.getVendorid() + "/hashkey/" + session.getHahkey();
                currenturl = session.getBase_Url() + "vendorapi/index/notification";
                orderAlert = new JSONArray();
                post_data.put("hashkey", session.getHahkey());
                post_data.put("vendor_id", session.getVendorid());
                notificationRequestData(current);


                notificationlistview.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView absListView, int i) {

                    }

                    @Override
                    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                        if ((firstVisibleItem + visibleItemCount) != 0) {

                            if (((firstVisibleItem + visibleItemCount) == totalItemCount) && load) {
                                current++;
                                load = false;
                                ScrollData(current);
                            }
                        }
                    }
                });

                notificationlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        TextView linkto = view.findViewById(R.id.MultiVendor_link);
                        TextView order_link = view.findViewById(R.id.MultiVendor_link_order);
                        TextView tag = view.findViewById(R.id.MultiVendor_tag);
                        String link = linkto.getText().toString();
                        String order_link_to = order_link.getText().toString();

                        if (tag.getText().toString().equalsIgnoreCase("Add Profile Picture ") || tag.getText().toString().equalsIgnoreCase("إضافة صورة الملف الشخصي")) {
                            showGallary(true);
                        }else if (order_link_to.equalsIgnoreCase("order")) {
                            /*Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_VendorProfile.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                            finish();*/
                            Intent orderview = new Intent(getApplicationContext(), Ced_MultiVendor_ManageOrderview.class);
                            orderview.putExtra("order_id", link);
                            startActivity(orderview);
                            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                        }else if (link.equals("Vendor Profile") || link.equals("الملف الشخصي للبائع")) {
                            Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_VendorProfile.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                            finish();
                        }else if (link.equals("Add New Product") || link.equals("إضافة منتج جديد")) {
                            Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_AddVendorProduct.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                            finish();
                        }else if (link.equals("Payment Setting")) {
                            Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_Settings.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                            finish();
                        }else if (link.equals("Shipping Method")) {
                            Intent intent = new Intent(getApplicationContext(), ShippingMethodNew.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                            finish();
                        }else if (link.equals("Shipping Origin")) {
                            Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_ShippingSetting.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                            finish();
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
        } catch (Exception e) {
            Intent main = new Intent(getApplicationContext(), Ced_MultiVendor_VendorSplash.class);
            main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(main);
            overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
        }


    }

    private void notificationRequestData(final int page_number) {

        Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                Jstring = output.toString();
                if (functionalityList.getExtensionAddon()) {
                    JSONObject object = new JSONObject(Jstring);
                    if (object.has("vendor_approved")) {
                        logout();
                    } else {
                        if (object.getString("alert_count").equals("0")) {
                            Toast.makeText(getApplicationContext(), "You have 0 Notification", Toast.LENGTH_LONG).show();
                            Ced_MultiVendor_GlobalVariables.noti = object.getString("alert_count");
                            chagecount();
                            notify_image.setVisibility(View.VISIBLE);
                        } else {
                            load = true;
                            JSONArray vendorAlert = object.getJSONArray("vendorAlert");
                            orderAlert = object.getJSONArray("orderAlert");
                            if (orderAlert.length() == 0) {
                                load = false;
                            } else {
                                if (object.has("orderAlert") && orderAlert.length() > 0) {
                                    for (int i = 0; i < orderAlert.length(); i++) {
                                        HashMap<String, String> data = new HashMap<String, String>();
                                        JSONObject jsonObject = orderAlert.getJSONObject(i);
                                        data.put("tag", jsonObject.getString("title"));
                                        data.put("link_to", jsonObject.getString("reference_id"));
                                        data.put("link_to_order", "order");
                                        tagsandlabel.add(data);
                                    }
                                }


                            }
                            Ced_MultiVendor_GlobalVariables.noti = object.getString("alert_count");
                            for (int alert = 0; alert < vendorAlert.length(); alert++) {
                                HashMap<String, String> data = new HashMap<String, String>();
                                JSONObject jsonObject = vendorAlert.getJSONObject(alert);
                                data.put("tag", jsonObject.getString("tag"));
                                data.put("link_to", jsonObject.getString("link_to"));
                                tagsandlabel.add(data);
                            }
                            notificationAdapters = new NotificationAdapters(Notification.this, tagsandlabel);
                            notificationlistview.setDivider(new ColorDrawable(getResources().getColor(R.color.transparent)));
                            notificationlistview.setDividerHeight(0);
                            notificationlistview.setAdapter(notificationAdapters);
                            chagecount();
                        }

                    }
                } else {
                    Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }


            }
        }, Notification.this, "POST", post_data);
        crr.execute(currenturl + "/page/" + page_number);

    }

    private void ScrollData(final int page_number) {

        Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                Jstring = output.toString();
                if (functionalityList.getExtensionAddon()) {
                    JSONObject object = new JSONObject(Jstring);
                    if (object.has("vendor_approved")) {
                        logout();
                    } else {
                      /*  if (object.getString("alert_count").equals("0")) {
                            Toast.makeText(getApplicationContext(), "You have 0 Notification", Toast.LENGTH_LONG).show();
                            Ced_MultiVendor_GlobalVariables.noti = object.getString("alert_count");
                            chagecount();
                            notify_image.setVisibility(View.VISIBLE);
                        } else {*/
                        load = true;
                        //   JSONArray vendorAlert = object.getJSONArray("vendorAlert");
                        JSONArray jsonArray = object.getJSONArray("orderAlert");
                        if (jsonArray.length() == 0) {
                            load = false;
                        } else {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                orderAlert.put(jsonObject);
                            }


                            if (object.has("orderAlert") && orderAlert.length() > 0) {
                                for (int i = 0; i < orderAlert.length(); i++) {
                                    HashMap<String, String> data = new HashMap<String, String>();
                                    JSONObject jsonObject = orderAlert.getJSONObject(i);
                                    data.put("tag", jsonObject.getString("title"));
                                    data.put("link_to", jsonObject.getString("reference_id"));
                                    data.put("link_to_order", "order");
                                    tagsandlabel.add(data);
                                }
                            }


                            notificationAdapters = new NotificationAdapters(Notification.this, tagsandlabel);
                            notificationlistview.setDivider(new ColorDrawable(getResources().getColor(R.color.transparent)));
                            notificationlistview.setDividerHeight(0);
                            notificationAdapters.notifyDataSetChanged();

                            chagecount();

                        }


                    }
                } else {
                    Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }


            }
        }, Notification.this, "POST", post_data);
        crr.execute(currenturl + "/page/" + page_number);

    }

    @Override
    protected void onResume() {
        Ced_MultiVendor_ConnectionDetector connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        if (connectionDetector.isConnectingToInternet()) {
            setname(session.getvendorname());
            setprofilepic(session.getvendorpic());
            invalidateOptionsMenu();
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
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.ced_multivendor_menu_login, menu);
        MenuItem item = menu.findItem(R.id.MultiVendor_notification);
        MenuItem item2 = menu.findItem(R.id.MultiVendor_profile);
        MenuItemCompat.setActionView(item, R.layout.ced_multivendor_feed_update_count);
        MenuItemCompat.setActionView(item2, R.layout.ced_multivendor_profilestatus);
        View profilecount = MenuItemCompat.getActionView(item2);
        profilecount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstant.lockButton(v);
                Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_ProfileStatus.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);


            }
        });
        View notifCount = MenuItemCompat.getActionView(item);
        TextView notification = notifCount.findViewById(R.id.MultiVendor_notitext);
        notification.setText(Ced_MultiVendor_GlobalVariables.noti);
        notifCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstant.lockButton(v);
                Intent intent = new Intent(getApplicationContext(), Notification.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        return true;
    }

    public void chagecount() {
        invalidateOptionsMenu();
    }
}
