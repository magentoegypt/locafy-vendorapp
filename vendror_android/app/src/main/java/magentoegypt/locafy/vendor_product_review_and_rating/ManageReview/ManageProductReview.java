package magentoegypt.locafy.vendor_product_review_and_rating.ManageReview;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_ClientRequestResponseRest_New;
import magentoegypt.locafy.R;
import magentoegypt.locafy_constant.AppConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class ManageProductReview extends Ced_MultiVendor_NavigationActivity {
    private final JSONObject postdata = new JSONObject();
    private String getReviewListUrl = "";
    private TextView msg;
    private RecyclerView ratingListRecycler;
    private String datafilterjson = "";
    private JSONObject status_json_obj, stores_json_obj, type_json_obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
        getLayoutInflater().inflate(R.layout.activity_manage_product_review, content, true);
        getReviewListUrl = session.getBase_Url() + "rest/V1/vproductreviewapi/getReviewList";
        msg = findViewById(R.id.msg);
        ratingListRecycler = findViewById(R.id.ratingListRecycler);
        ImageView filterbutton = findViewById(R.id.filter_button);
        try {

            postdata.put("vendor_id", session.getVendorid());
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                if (getIntent().getStringExtra("filter") != null) {
                    datafilterjson = getIntent().getStringExtra("filter");
                    postdata.put("filter", datafilterjson);
                }
            }

            request_page_date();
            filterbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final View filter_dialog = ManageProductReview.this.getLayoutInflater().inflate(R.layout.product_review_filter, null);
                    final AlertDialog.Builder alert = new AlertDialog.Builder(ManageProductReview.this);
                    alert.setView(filter_dialog);
                    final Dialog d = alert.create();
                    d.show();
                    final EditText id_edt = filter_dialog.findViewById(R.id.id_edt);
                    final EditText qualification_edt = filter_dialog.findViewById(R.id.qualification_edt);
                    final EditText nickname_edt = filter_dialog.findViewById(R.id.nickname_edt);
                    final EditText review_edt = filter_dialog.findViewById(R.id.review_edt);
                    final EditText product_edt = filter_dialog.findViewById(R.id.product_edt);
                    final EditText sku_edt = filter_dialog.findViewById(R.id.sku_edt);
                    final TextView created_at_to = filter_dialog.findViewById(R.id.created_at_to);
                    final TextView created_at_from = filter_dialog.findViewById(R.id.created_at_from);
                    final Spinner condition_spn = filter_dialog.findViewById(R.id.condition_spn);
                    final Spinner visibility_spn = filter_dialog.findViewById(R.id.visibility_spn);
                    final Spinner type_spn = filter_dialog.findViewById(R.id.type_spn);

                    try {
                        //visibility as a stores
                        ArrayList<String> store_list = new ArrayList<>();
                       // store_list.add("Select Store ");
                        if (session.getReviewStoreJSONObject() != null && !session.getReviewStoreJSONObject().equals("")) {
                            stores_json_obj = new JSONObject(session.getReviewStoreJSONObject());
                            try {
                                final JSONArray store_keys = stores_json_obj.names();
                                for (int i = 0; i < Objects.requireNonNull(store_keys).length(); i++) {
                                    try {
                                        store_list.add(stores_json_obj.getString(store_keys.getString(i)));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                visibility_spn.setAdapter(new ArrayAdapter<>(ManageProductReview.this, R.layout.simple_list_item_1, store_list));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        ArrayList<String> type_list = new ArrayList<>();
                       // type_list.add("Select Type ");
                        if (session.getReviewAccessTypeJSONObject() != null && !session.getReviewAccessTypeJSONObject().equals("")) {
                            try {
                                type_json_obj = new JSONObject(session.getReviewAccessTypeJSONObject());
                                final JSONArray type_keys = type_json_obj.names();
                                for (int i = 0; i < Objects.requireNonNull(type_keys).length(); i++) {
                                    try {
                                        type_list.add(type_json_obj.getString(type_keys.getString(i)));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                type_spn.setAdapter(new ArrayAdapter<>(ManageProductReview.this, R.layout.simple_list_item_1, type_list));

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        ArrayList<String> status_list = new ArrayList<>();
                      //  status_list.add("Select Status ");
                        if (session.getReviewStatusJSONObject() != null && !session.getReviewStatusJSONObject().equals("")) {
                            try {
                                status_json_obj = new JSONObject(session.getReviewStatusJSONObject());
                                final JSONArray status_keys = status_json_obj.names();
                                for (int i = 0; i < Objects.requireNonNull(status_keys).length(); i++) {
                                    try {
                                        status_list.add(status_json_obj.getString(status_keys.getString(i)));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                condition_spn.setAdapter(new ArrayAdapter<>(ManageProductReview.this, R.layout.simple_list_item_1, status_list));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    created_at_from.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AppConstant.setDateFrom(ManageProductReview.this, created_at_from);
                        }
                    });

                    created_at_to.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AppConstant.setDateFrom(ManageProductReview.this, created_at_to);
                        }
                    });

                    filter_dialog.findViewById(R.id.dialog_reset_btn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v12) {
                            try {
                                id_edt.setText("");
                                qualification_edt.setText("");
                                nickname_edt.setText("");
                                review_edt.setText("");
                                product_edt.setText("");
                                sku_edt.setText("");
                                created_at_to.setText("");
                                created_at_from.setText("");
                                condition_spn.setSelection(0);
                                visibility_spn.setSelection(0);
                                datafilterjson = "";
                                type_spn.setSelection(0);
                                d.dismiss();
                                postdata.remove("filter");
                                request_page_date();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    filter_dialog.findViewById(R.id.dialog_apply_btn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v1) {
                            JSONObject mainfilter = new JSONObject();
                            try {
                                mainfilter.put("review_id", id_edt.getText().toString());
                                JSONObject periodjsonObject = new JSONObject();
                                periodjsonObject.put("from", created_at_from.getText().toString());
                                periodjsonObject.put("to", created_at_to.getText().toString());
                                mainfilter.put("created_at", periodjsonObject);
                                if (Integer.parseInt(String.valueOf(condition_spn.getSelectedItemId())) != 0) {
                                    mainfilter.put("status", Integer.parseInt(String.valueOf(condition_spn.getSelectedItemId())));
                                }
//                                mainfilter.put("status", condition_spn.getSelectedItem());
                                mainfilter.put("title", qualification_edt.getText().toString());
                                mainfilter.put("nickname", nickname_edt.getText().toString());
                                mainfilter.put("detail", review_edt.getText().toString());
                                if (Integer.parseInt(String.valueOf(visibility_spn.getSelectedItemId())) != 0) {
                                    mainfilter.put("store_id", Integer.parseInt(String.valueOf(visibility_spn.getSelectedItemId())));
                                }
                                if (Integer.parseInt(String.valueOf(type_spn.getSelectedItemId())) != 0) {
                                    mainfilter.put("type", Integer.parseInt(String.valueOf(type_spn.getSelectedItemId())));
                                }
//                                mainfilter.put("type", type_spn.getSelectedItem());
                                mainfilter.put("name", product_edt.getText().toString());
                                mainfilter.put("sku", sku_edt.getText().toString());
                                Log.i("9044_filter_string", "" + mainfilter.toString());
                                Intent intent = new Intent(getApplicationContext(), ManageProductReview.class);
                                intent.putExtra("filter", mainfilter.toString());
                                startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            d.dismiss();
                        }
                    });

                    if (!(datafilterjson.isEmpty())) {
                        try {
                            JSONObject object = new JSONObject(datafilterjson);
//                            id_edt.setText(object.getString("review_id"));
                            id_edt.setText(object.has("review_id") ? object.getString("review_id") : "");
//                            qualification_edt.setText(object.getString("title"));
                            qualification_edt.setText(object.has("title") ? object.getString("title") : "");

                            nickname_edt.setText(object.has("nic") ? object.getString("\", nic") : "");
//                            review_edt.setText(object.getString("name"));
                            review_edt.setText(object.has("name") ? object.getString("name") : "");
                            product_edt.setText(object.has("name") ? object.getString("name") : "");
//                            product_edt.setText(object.getString("name"));
                            sku_edt.setText(object.has("name") ? object.getString("name") : "");
//                            sku_edt.setText(object.getString("name"));
                            created_at_to.setText(object.getJSONObject("price").has("to") ? object.getJSONObject("price").getString("to") : "");
//                            created_at_to.setText(object.getJSONObject("price").getString("to"));
                            created_at_from.setText(object.getJSONObject("price").has("from") ? object.getJSONObject("price").getString("from") : "");
//                            created_at_from.setText(object.getJSONObject("price").getString("from"));
                            /*condition_spn.setSelection();
                            visibility_spn.setSelection();
                            guy_spn.setSelection();*/
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void request_page_date() throws JSONException {
        int page = 1;
        postdata.put("page", "" + page);
        Ced_ClientRequestResponseRest_New response = new Ced_ClientRequestResponseRest_New(new AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                JSONObject vendor_data = new JSONObject(output.toString()).getJSONObject("vendor_data");
                if (vendor_data.getBoolean("status")) {
                    msg.setVisibility(View.GONE);
                    JSONArray review_listArray = vendor_data.getJSONArray("review_list");
                    session.saveReviewStoreJSONObject(vendor_data.getJSONObject("stores").toString());
                    session.saveReviewAccessTypeJSONObject(vendor_data.getJSONObject("type").toString());
                    session.saveReviewStatusJSONObject(vendor_data.getJSONObject("status_filter").toString());
                    JSONObject stores = vendor_data.getJSONObject("stores");
                    ReviewListAdapter ratingListAdapter = new ReviewListAdapter(review_listArray, ManageProductReview.this, stores);
                    ratingListRecycler.setAdapter(ratingListAdapter);
                    ratingListAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ManageProductReview.this, vendor_data.getString("message"), Toast.LENGTH_SHORT).show();
                    msg.setVisibility(View.VISIBLE);
                    msg.setText(vendor_data.getString("message"));
                }
            }
        }, this, "POST", "" + postdata, true);
        response.execute(getReviewListUrl);
    }

}