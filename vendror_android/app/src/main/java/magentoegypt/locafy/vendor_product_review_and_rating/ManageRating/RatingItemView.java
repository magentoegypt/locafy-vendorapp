package magentoegypt.locafy.vendor_product_review_and_rating.ManageRating;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_ClientRequestResponseRest_New;
import magentoegypt.locafy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class RatingItemView extends Ced_MultiVendor_NavigationActivity {
    private final JSONObject postdata = new JSONObject();
    private final ArrayList<String> visibleStoresArray = new ArrayList<>();
    private String viewRatingUlr = "";
    private LinearLayout visibleStoreList;
    private AppCompatTextView defaultValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
        getLayoutInflater().inflate(R.layout.activity_rating_item_view, content, true);
        visibleStoreList = findViewById(R.id.visibleStoreList);
        defaultValue = findViewById(R.id.defaultValue);
        AppCompatButton saveStoreRating = findViewById(R.id.saveStoreRating);
        viewRatingUlr = session.getBase_Url() + "rest/V1/vproductreviewapi/viewRating";

        saveStoreRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePageData();
            }
        });

        try {
            postdata.put("vendor_id", session.getVendorid());
            postdata.put("id", getIntent().getStringExtra("RATING_ID"));
            request_page_date();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void savePageData() {
        try {
            String saveRatingUrl = session.getBase_Url() + "rest/V1/vproductreviewapi/saveRating";
//        if (visibleStoresArray.size()>0) {
            postdata.put("stores", visibleStoresArray);
//        }
            Ced_ClientRequestResponseRest_New response = new Ced_ClientRequestResponseRest_New(new AsyncResponse() {
                @Override
                public void processFinish(Object output) throws JSONException {
                    Log.d("REpo", "request_page_date_64: " + output);
                    JSONObject vendor_data = new JSONObject(output.toString()).getJSONObject("vendor_data");
                    Toast.makeText(RatingItemView.this, vendor_data.getString("message"), Toast.LENGTH_SHORT).show();
       /*     if (vendor_data.getString("status").equals("true")){
                finish();
            }*/
                }
            }, this, "POST", "" + postdata, true);
            response.execute(saveRatingUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void request_page_date() {
        try {
            visibleStoreList.removeAllViews();
            Ced_ClientRequestResponseRest_New response = new Ced_ClientRequestResponseRest_New(new AsyncResponse() {
                @Override
                public void processFinish(Object output) throws JSONException {
                    Log.d("REpo", "request_page_date_40: " + output);
                    JSONObject vendor_data = new JSONObject(output.toString()).getJSONObject("vendor_data");
                    if (vendor_data.getString("status").equals("true")) {
                        JSONObject rating_view = vendor_data.getJSONObject("rating_view");
                        defaultValue.setText(rating_view.getString("ratin_code"));
                        JSONObject stores = vendor_data.getJSONObject("stores");
                        JSONArray storeNamesArray = stores.names();

                        for (int i = 0; i < Objects.requireNonNull(storeNamesArray).length(); i++) {
                            View storeTitleView = RatingItemView.this.getLayoutInflater().inflate(R.layout.store_title, null);
                            AppCompatTextView storeTitle = storeTitleView.findViewById(R.id.storeTitle);
                            storeTitle.setText(storeNamesArray.getString(i));
                            visibleStoreList.addView(storeTitleView);

                            JSONObject storeObj = stores.getJSONObject(storeNamesArray.getString(0));
                            JSONArray subStoreNamesArray = storeObj.names();
                            View substoreview = RatingItemView.this.getLayoutInflater().inflate(R.layout.sub_store_title, null);
                            AppCompatTextView subStoreTitle = substoreview.findViewById(R.id.subStoreTitle);
                            subStoreTitle.setText(subStoreNamesArray.getString(0));
                            visibleStoreList.addView(substoreview);

                            JSONArray subStoreLocation = storeObj.getJSONArray(subStoreNamesArray.getString(0));
                            for (int j = 0; j < subStoreLocation.length(); j++) {
                                View subStoreVisibleCheckBoxView = RatingItemView.this.getLayoutInflater().inflate(R.layout.sub_store_location, null);
                                final CheckBox subStoreVisibleCheckBox = subStoreVisibleCheckBoxView.findViewById(R.id.subStoreVisibleCheckBox);
                                subStoreVisibleCheckBox.setText(subStoreLocation.getString(j).split("#")[1]);
                                subStoreVisibleCheckBox.setTag(subStoreLocation.getString(j).split("#")[0]);
                                subStoreVisibleCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        if (subStoreVisibleCheckBox.isChecked()) {
                                            visibleStoresArray.add(subStoreVisibleCheckBox.getTag().toString());
                                        } else {
                                            visibleStoresArray.remove(subStoreVisibleCheckBox.getTag().toString());
                                        }
                                    }
                                });
                                visibleStoreList.addView(subStoreVisibleCheckBox);
                            }
                        }
                    }
                }
            }, this, "POST", "" + postdata, true);
            response.execute(viewRatingUlr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}