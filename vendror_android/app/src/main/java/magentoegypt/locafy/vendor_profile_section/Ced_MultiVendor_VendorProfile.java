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

package magentoegypt.locafy.vendor_profile_section;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import magentoegypt.locafy.api_request_response_section.RestNotificatioRequest;
import com.bumptech.glide.Glide;
import magentoegypt.locafy.base_app.Ced_MultiVendor_FontSetting;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_ConnectionDetector;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_UnAuthourizedRequestError;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.R;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

public class Ced_MultiVendor_VendorProfile extends Ced_MultiVendor_NavigationActivity {
    Ced_MultiVendor_FontSetting fontSetting;
    Ced_MultiVendor_ConnectionDetector connectionDetector;
    String Jstring;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    String CurrentUrl;
    HashMap<String, String> dataforvendorprofile;

    ScrollView mainvendorprofilescroll;

    LinearLayout dynamic_fields;

    TextView edit,delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fontSetting = new Ced_MultiVendor_FontSetting();
        connectionDetector = new Ced_MultiVendor_ConnectionDetector(getApplicationContext());
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(getApplicationContext());
        dataforvendorprofile = new HashMap<String, String>();

        final ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
        getLayoutInflater().inflate(R.layout.ced_multivendor_activity_vendor_profile_dynamic, content, true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);

        changetitle(getResources().getString(R.string.VendorProfile));

        CurrentUrl = session.getBase_Url() + "vendorapi/index/infos";
        dataforvendorprofile.put("vendor_id", session.getVendorid());
        dataforvendorprofile.put("hashkey", session.getHahkey());

        mainvendorprofilescroll = findViewById(R.id.MultiVendor_mainvendorprofilescroll);
        edit = findViewById(R.id.MultiVendor_edit);
        delete = findViewById(R.id.MultiVendor_delete);
        dynamic_fields = findViewById(R.id.dynamic_fields);

       /* vendorprofilepic.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showImagePOP(vendorprofilepic);
            }
        });
*/
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_EditVendorProfileDynamic.class);
                startActivity(intent);
                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(Ced_MultiVendor_VendorProfile.this);
                builder.setTitle(R.string.alert_name);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setMessage(R.string.do_you_want_to_delete_account);
                builder.setNegativeButton(R.string.no,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                removeVendorToServer(session.getVendorid());
                            }
                        }
                );
                builder.show();
            }
        });

        Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                Jstring = output.toString();
                mainvendorprofilescroll.setVisibility(View.VISIBLE);
                if (functionalityList.getExtensionAddon()) {
                    JSONObject mainobject = new JSONObject(Jstring);
                    LinearLayout.LayoutParams lm;
                    JSONObject data = mainobject.getJSONObject("data");
                    String success = mainobject.getString("success");

                    if (success.equals("true")) {
                        if (mainobject.has("badges")) {
                            JSONObject badges_obj = mainobject.getJSONObject("badges");
                            TextView header_text = new TextView(Ced_MultiVendor_VendorProfile.this);
                            lm = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            header_text.setPadding(10, 10, 10, 10);
                            header_text.setBackgroundColor(getResources().getColor(R.color.secondary_color));
                            header_text.setGravity(Gravity.CENTER);
                            header_text.setLayoutParams(lm);
                            header_text.setText(badges_obj.getString("label"));
                            header_text.setTextColor(getResources().getColor(R.color.white));
                            dynamic_fields.addView(header_text);

                            View view = View.inflate(Ced_MultiVendor_VendorProfile.this, R.layout.badges_layout, null);
                            ImageView order_image, review;
                            TextView order_count, review_count;
                            RatingBar ratting_bar;

                            order_image = view.findViewById(R.id.order_image);
                            review = view.findViewById(R.id.review);
                            order_count = view.findViewById(R.id.order_count);
                            review_count = view.findViewById(R.id.review_count);
                            ratting_bar = view.findViewById(R.id.ratting_bar);
                            if (badges_obj.has("order")) {
                                if (badges_obj.has("order") && !TextUtils.isEmpty(badges_obj.getString("order"))) {
                                    Picasso.with(Ced_MultiVendor_VendorProfile.this)
                                            .load(badges_obj.getString("order"))
                                            .placeholder(getResources().getDrawable(R.drawable.placeholder))
                                            .error(getResources().getDrawable(R.drawable.placeholder))
                                            .into(order_image);
                                }


                            }
                            if (badges_obj.has("order_count")) {
                                order_count.setText(badges_obj.getString("order_count"));
                            }


                            if (badges_obj.has("review")) {
                                if (badges_obj.has("review") && !TextUtils.isEmpty(badges_obj.getString("review"))) {
                                    Picasso.with(Ced_MultiVendor_VendorProfile.this)
                                            .load(badges_obj.getString("review"))
                                            .placeholder(getResources().getDrawable(R.drawable.placeholder))
                                            .error(getResources().getDrawable(R.drawable.placeholder))
                                            .into(review);
                                }

                            }


                            if (badges_obj.has("review_count")) {
                                float review_values = Integer.parseInt(badges_obj.getString("review_count"));
                                float rating_val = (review_values / 20);


                                ratting_bar.setRating(rating_val);


                                review_count.setText(badges_obj.getString("review_count"));

                            }

                            dynamic_fields.addView(view);
                        }


                        Iterator<String> keys = data.keys();

                        while (keys.hasNext()) {
                            String key = keys.next();
                            TextView header_text = new TextView(Ced_MultiVendor_VendorProfile.this);
                            lm = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            header_text.setPadding(10, 10, 10, 10);
                            header_text.setBackgroundColor(getResources().getColor(R.color.secondary_color));
                            header_text.setGravity(Gravity.CENTER);
                            header_text.setLayoutParams(lm);
                            header_text.setText(key);
                            header_text.setTextColor(getResources().getColor(R.color.white));
                            dynamic_fields.addView(header_text);
                            if (data.get(key) instanceof JSONArray) {
                                LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View view = null;
                                final JSONArray object = data.getJSONArray(key);
                                for (int i = 0; i < object.length(); i++) {
                                    if (object.getJSONObject(i).getString("label").equalsIgnoreCase("Shop Url Done")) {
                                        continue;
                                    }
                                    if (object.getJSONObject(i).getString("type").equalsIgnoreCase("image")) {
                                        view = li.inflate(R.layout.view_image, null, false);
                                        TextView heading = view.findViewById(R.id.header);
                                        ImageView image = view.findViewById(R.id.image);
                                        heading.setText(object.getJSONObject(i).getString("label"));

                                        String image_url = object.getJSONObject(i).getString("value");
                                        Log.d("image_url", "" + image_url);

                                      /*  RequestOptions options = new RequestOptions()
                                                .centerCrop()
                                                .placeholder(R.drawable.placeholder)
                                                .error(R.drawable.placeholder)
                                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                .priority(Priority.HIGH);*/

                                        Glide.with(getApplicationContext())
                                                .load(image_url)
                                                .placeholder(getResources().getDrawable(R.drawable.placeholder))
                                                .into(image);
                                        dynamic_fields.addView(view);
                                    } else if (object.getJSONObject(i).getString("type").equalsIgnoreCase("file")) {
                                        view = li.inflate(R.layout.vendor_profile_file, null, false);

                                        TextView heading = view.findViewById(R.id.header);
                                        final TextView link_to = view.findViewById(R.id.link_to);
                                        heading.setText(object.getJSONObject(i).getString("label"));
                                        if (object.getJSONObject(i).has("value") && !TextUtils.isEmpty(object.getJSONObject(i).getString("value"))) {
                                            link_to.setTag(object.getJSONObject(i).getString("value"));
                                            link_to.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link_to.getTag().toString()));
                                                    startActivity(intent);
                                                }
                                            });
                                        } else {
                                            link_to.setText("No File Attached");
                                        }


                                        String image_url = object.getJSONObject(i).getString("value");
                                        Log.d("image_url", "" + image_url);
                                        dynamic_fields.addView(view);

                                    } else {
                                        view = li.inflate(R.layout.view_profiletext, null, false);
                                        TextView txt_value = view.findViewById(R.id.txt_value);
                                        TextView txt_name = view.findViewById(R.id.txt_name);
                                      /*  TextView header_label = view.findViewById(R.id.header_label);
                                        header_label.setText(key);*/
                                        txt_name.setText(object.getJSONObject(i).getString("label"));
                                        txt_value.setText(object.getJSONObject(i).getString("value"));
                                        dynamic_fields.addView(view);

                                    }
                                }
                            } else {
                                Log.i("REpo", "else instanceof");
                            }

                        }
                    } else {
                        Toast.makeText(getApplicationContext(), data.getString("message"), Toast.LENGTH_LONG).show();
                    }


                } else {
                    Intent intent = new Intent(getApplicationContext(), Ced_MultiVendor_UnAuthourizedRequestError.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }
            }
        }, Ced_MultiVendor_VendorProfile.this, "POST", dataforvendorprofile);
        crr.execute(CurrentUrl);

    }

    private void removeVendorToServer(String vendor_id) {

        HashMap<String, String> jsonObject = new HashMap<>();
        jsonObject.put("vendor_id", vendor_id);
        Ced_MultiVendor_ClientRequestResponse request = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                session.ClearVendorId();
                session.ClearSubVendor();
                session.logoutUser();
                /* FacebookSdk.sdkInitialize(getApplicationContext());*/
                FacebookSdk.setAutoInitEnabled(true);
                FacebookSdk.fullyInitialize();
                LoginManager.getInstance().logOut();
                overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
            }
        }, this, "POST", jsonObject);
        request.execute(session.getBase_Url() + "rest/V1/vendorapi/deletevendor");
    }


    @Override
    protected void onResume() {
        super.onResume();
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