package magentoegypt.locafy.vendor_product_review_and_rating.ManageReview;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_ClientRequestResponseRest_New;
import magentoegypt.locafy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ReviewItemView extends Ced_MultiVendor_NavigationActivity {
    String viewreviewurl, deleteReviewurl, savereviewurl;
    TextView surname, evolutionsummary, assessment;
    RatingBar summaryrating;
    LinearLayout detailclassification_layout, cms_storeview;
    Spinner situation_spinner;
    HashMap<String, ArrayList> reviews_available;
    ArrayList<HashMap<String, String>> reviews_to_fill;
    private AppCompatTextView author, product;
    private String rating_id, rating_code, status_id = "";
    private ConstraintLayout parent;
    private int id;
    private List<String> stringList;
    private JSONArray storeview;
    List<String> status_option_value_list;
    Map<String, String> status_option_key_and_value_map;
    private final static String TAG = "ReviewItemView";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup content = findViewById(R.id.MultiVendor_frame_container);
        getLayoutInflater().inflate(R.layout.activity_review_item_view, content, true);
        viewreviewurl = session.getBase_Url() + "rest/V1/vproductreviewapi/viewReview";
        savereviewurl = session.getBase_Url() + "rest/V1/vproductreviewapi/saveReview";
        deleteReviewurl = session.getBase_Url() + "rest/V1/vproductreviewapi/deleteReview";
        id = Resources.getSystem().getIdentifier("btn_check_holo_light", "drawable", "android");
        parent = findViewById(R.id.parent);
        product = findViewById(R.id.product);
        author = findViewById(R.id.author);
        summaryrating = findViewById(R.id.summaryrating);
        detailclassification_layout = findViewById(R.id.detailclassification_layout);
        situation_spinner = findViewById(R.id.situation_spinner);
        surname = findViewById(R.id.surname);
        evolutionsummary = findViewById(R.id.evolutionsummary);
        assessment = findViewById(R.id.assessment);
        AppCompatButton save = findViewById(R.id.save);
        AppCompatButton reset = findViewById(R.id.reset);
        AppCompatButton delete = findViewById(R.id.delete);
        cms_storeview = findViewById(R.id.MultiVendor_cms_storeview_layout);
        CheckBox storeviewbox = findViewById(R.id.MultiVendor_cms_storeviewtext);
        storeviewbox.setButtonDrawable(id);
        storeviewbox.setButtonDrawable(id);
        stringList = new ArrayList<>();
        storeview = new JSONArray();

        storeviewbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cms_storeview.setVisibility(View.VISIBLE);
                } else {
                    cms_storeview.setVisibility(View.GONE);
                }
            }
        });
        final JSONObject postdata = new JSONObject();

        try {
            postdata.put("vendor_id", session.getVendorid());
            postdata.put("review_id", getIntent().getStringExtra("REVIEW_ID"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request_page_date(postdata);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteReview(postdata);
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ratingViewIntent = new Intent(getApplicationContext(), ReviewItemView.class);
                ratingViewIntent.putExtra("REVIEW_ID", getIntent().getStringExtra("REVIEW_ID"));
                ratingViewIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(ratingViewIntent);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
//                    postdata.put("ratings", "[\"2.0\",\"5.0\",\"4.0\"]");
                    postdata.put("stores", storeview.toString());
                    postdata.put("title", evolutionsummary.getText().toString());
                    postdata.put("nickname", surname.getText().toString());
                    postdata.put("detail", assessment.getText().toString());
                    final JSONObject reviewinfo = new JSONObject();
                    if (status_option_key_and_value_map.size() > 0) {
                        if (status_option_key_and_value_map.containsKey(situation_spinner.getSelectedItem().toString().trim())) {
                            postdata.put("status_id", status_option_key_and_value_map.get(situation_spinner.getSelectedItem().toString().trim()));
                        }
                    }

//                    switch (situation_spinner.getSelectedItem().toString()) {
//                        case "Approved":
//                            postdata.put("status_id", "1");
//                            break;
//                        case "Pending":
//                            postdata.put("status_id", "2");
//                            break;
//                        case "Not Approved":
//                            postdata.put("status_id", "3");
//                            break;
//                    }
                    for (int i = 0; i < detailclassification_layout.getChildCount(); i++) {
                        ConstraintLayout linearLayout = (ConstraintLayout) detailclassification_layout.getChildAt(i);
//                        LinearLayout linearLayout1 = (LinearLayout) linearLayout.getChildAt(0);
                        AppCompatRatingBar ratingBar = (AppCompatRatingBar) linearLayout.getChildAt(1);
                        TextView textView = (TextView) linearLayout.getChildAt(0);
                        String rating_id = textView.getTag().toString();
                        int review_stars = (int) ratingBar.getRating();
                        int ratings_to_send = (Integer.parseInt(rating_id) - 1) * 5 + review_stars;
//                        int ratings_to_send = review_stars;
                        try {
                            reviewinfo.put(textView.getTag().toString(), String.valueOf(ratings_to_send));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Iterator<String> keys = reviewinfo.keys();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        if (reviewinfo.getString(key).equals("0")) {
                            Toast.makeText(ReviewItemView.this, getResources().getString(R.string.rating_empty_msg), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    postdata.put("ratings", reviewinfo.toString());
                    saveReview(postdata);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void saveReview(JSONObject postdata) {
        Ced_ClientRequestResponseRest_New response = new Ced_ClientRequestResponseRest_New(new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                try {
                    JSONObject vendor_data = new JSONObject(output.toString()).getJSONObject("vendor_data");
                    Toast.makeText(ReviewItemView.this, vendor_data.getString("message"), Toast.LENGTH_SHORT).show();
                    if (vendor_data.getBoolean("status")) {
                        Intent ratingViewIntent = new Intent(getApplicationContext(), ReviewItemView.class);
                        ratingViewIntent.putExtra("REVIEW_ID", getIntent().getStringExtra("REVIEW_ID"));
                        ratingViewIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(ratingViewIntent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, this, "POST", postdata.toString(), true);
        response.execute(savereviewurl);
    }

    private void deleteReview(JSONObject postdata) {
        Ced_ClientRequestResponseRest_New response = new Ced_ClientRequestResponseRest_New(new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                try {
                    JSONObject vendor_data = new JSONObject(output.toString()).getJSONObject("vendor_data");
                    Toast.makeText(ReviewItemView.this, vendor_data.getString("message"), Toast.LENGTH_SHORT).show();
                    if (vendor_data.getBoolean("status")) {
                        Intent intent = new Intent(getApplicationContext(), ManageProductReview.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, this, "POST", postdata.toString(), true);
        response.execute(deleteReviewurl);
    }

    private void request_page_date(JSONObject postdata) {
        Ced_ClientRequestResponseRest_New response = new Ced_ClientRequestResponseRest_New(new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                try {
                    JSONObject vendor_data = new JSONObject(output.toString()).getJSONObject("vendor_data");
                    if (vendor_data.getBoolean("status")) {
                        parent.setVisibility(View.VISIBLE);
                        JSONObject review_info = vendor_data.getJSONObject("review_info");
                        status_id = review_info.getString("status_id");
                        JSONObject status_options_json_object = vendor_data.getJSONObject("status_options");
                        Iterator<String> keys = status_options_json_object.keys();
                        status_option_value_list = new ArrayList<>();
                        status_option_key_and_value_map = new HashMap<>();
                        while (keys.hasNext()) {
                            String key = keys.next();
                            status_option_key_and_value_map.put(status_options_json_object.getString(key), key);
                            status_option_value_list.add(status_options_json_object.getString(key));
                        }
                        if (status_option_value_list.size() > 0) {
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(ReviewItemView.this, R.layout.simple_list_item_1, status_option_value_list);
                            situation_spinner.setAdapter(adapter);

                            for (int i = 0; i < status_option_key_and_value_map.size(); i++) {
                                if (status_option_key_and_value_map.containsKey(status_id)) {
                                    int pos = Integer.parseInt(status_option_key_and_value_map.get(status_id));
                                    situation_spinner.setSelection(--pos);
                                }

                            }
                        }

//                        switch (status_id) {
//                            case "Approved":
//                                situation_spinner.setSelection(0);
//                                break;
//                            case "Pending":
//                                situation_spinner.setSelection(1);
//                                break;
//                            case "Not Approved":
//                                situation_spinner.setSelection(2);
//                                break;
//                        }
                        product.setText(review_info.getString("product_name"));
                        author.setText(review_info.getString("author"));
                        surname.setText(review_info.getString("nickname"));
                        evolutionsummary.setText(review_info.getString("title"));
                        assessment.setText(review_info.getString("detail"));
//                        JSONObject summaryrating_title = review_info.getJSONObject("summary_rating");
                        summaryrating.setRating(Float.valueOf(review_info.getInt("summary_rating") / 5));
                        JSONArray rating_options = vendor_data.getJSONArray("rating_options");

                        JSONArray store_id = vendor_data.getJSONArray("store_id");
                        JSONArray stores_ids = review_info.getJSONArray("stores");
                        for (int s = 0; s < stores_ids.length(); s++)
                            stringList.add(stores_ids.getString(s));
                        HashMap<String, String> hashMap2;
                        for (int i = 0; i < store_id.length(); i++) {
                            JSONObject stores = store_id.getJSONObject(i);
                            hashMap2 = new HashMap<>();
                            final LinearLayout mainwebsite = new LinearLayout(ReviewItemView.this);
                            mainwebsite.setOrientation(LinearLayout.VERTICAL);
                            if (stores.get("value") instanceof JSONArray) {
                                JSONArray jsonArray = stores.getJSONArray("value");
                                if (jsonArray.isNull(0)) {
                                    final TextView defaultbox = new TextView(ReviewItemView.this);
                                    defaultbox.setText(stores.getString("label"));
                                    defaultbox.setTypeface(Typeface.DEFAULT_BOLD);
                                    mainwebsite.addView(defaultbox, 0);
                                } else {
                                    final TextView magetext = new TextView(ReviewItemView.this);
                                    magetext.setText(stores.getString("label"));
                                    magetext.setTypeface(Typeface.DEFAULT_BOLD);
                                    mainwebsite.addView(magetext, 0);
                                    for (int s = 0; s < jsonArray.length(); s++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(s);
                                        final String objlabel = jsonObject.getString("label");
                                        String objvalue = jsonObject.getString("value");
                                        hashMap2.put(objlabel, objvalue);
                                        final LinearLayout checkboxlinear = new LinearLayout(ReviewItemView.this);
                                        checkboxlinear.setOrientation(LinearLayout.VERTICAL);
                                        final CheckBox magebox = new CheckBox(ReviewItemView.this);
                                        magebox.setButtonDrawable(id);
                                        magebox.setText(objlabel);
                                        if (stringList.contains(objvalue)) {
                                            magebox.setChecked(true);
                                            storeview.put(hashMap2.get(objlabel));
                                        }
                                        final HashMap<String, String> finalHashMap = hashMap2;
                                        magebox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                            @Override
                                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                if (isChecked) {
                                                    storeview.put(finalHashMap.get(objlabel));
                                                } else {
                                                    try {
                                                        for (int i = 0; i < storeview.length(); i++) {
                                                            if (Objects.requireNonNull(finalHashMap.get(objlabel)).equalsIgnoreCase(storeview.getString(i))) {
                                                                storeview.remove(i);
                                                            }
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }
                                        });
                                        checkboxlinear.addView(magebox);
                                        mainwebsite.addView(checkboxlinear);
                                    }
                                }
                            } else {
                                final CheckBox mainwebsitename = new CheckBox(ReviewItemView.this);
                                mainwebsitename.setButtonDrawable(id);
                                final String key = stores.getString("label");
                                String value = String.valueOf(stores.getInt("value"));
                                hashMap2.put(key, value);
                                mainwebsitename.setText(stores.getString("label"));
                                if (stringList.contains(value)) {
                                    mainwebsitename.setChecked(true);
                                    storeview.put(hashMap2.get(key));
                                }
                                final HashMap<String, String> finalHashMap1 = hashMap2;
                                mainwebsitename.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        if (isChecked) {
                                            storeview.put(finalHashMap1.get(key));
                                        } else {
                                            try {
                                                for (int i = 0; i < storeview.length(); i++) {
                                                    if (Objects.requireNonNull(finalHashMap1.get(key)).equalsIgnoreCase(storeview.getString(i))) {
                                                        storeview.remove(i);
                                                    }
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                });
                                mainwebsite.addView(mainwebsitename, 0);
                            }
                            cms_storeview.addView(mainwebsite);
                        }
                        JSONArray detailed_rating = review_info.getJSONArray("detailed_rating");
                        setRating(rating_options, detailed_rating);
//                        if (detailed_rating.length() > 0) {
//                            for (int k = 0; k < detailed_rating.length(); k++) {
//                                JSONObject data = detailed_rating.getJSONObject(k);
//                                View v = getLayoutInflater().inflate(R.layout.classification_item_1, null);
//                                AppCompatTextView value = v.findViewById(R.id.value);
//                                AppCompatTextView name = v.findViewById(R.id.name);
//                                AppCompatRatingBar rating = v.findViewById(R.id.rating);
//                                name.setText(data.getString("rating_code"));
//                                value.setText(data.getString("value"));
//                                int rating_star = Integer.parseInt(data.getString("value"));
//                                Log.e(TAG, "rating= " + rating_star);
//                                rating.setRating(rating_star);
////                                detailclassification_layout.addView(v);
//                            }
//                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, this, "POST", postdata.toString(), true);
        response.execute(viewreviewurl);
    }

    private void setRating(JSONArray rating_options, JSONArray detailed_rating) {
        try {
            reviews_available = new HashMap<>();
            reviews_to_fill = new ArrayList<>();
            for (int i = 0; i < rating_options.length(); i++) {
                JSONObject object = rating_options.getJSONObject(i);
                if (object.has("rating-id")) {
                    rating_id = object.getString("rating-id");
                }
                if (object.has("rating-code")) {
                    rating_code = object.getString("rating-code");
                    HashMap hashMap = new HashMap();
                    hashMap.put("rating_id", rating_id);
                    hashMap.put("rating_code", rating_code);
                    reviews_to_fill.add(hashMap);
                    reviews_available.put(rating_id + "#" + rating_code, reviews_to_fill);
                }
            }


            Iterator iterator = reviews_available.entrySet().iterator();
            while (iterator.hasNext()) {
                View v = getLayoutInflater().inflate(R.layout.classification_item_1, null);
                AppCompatTextView name = v.findViewById(R.id.name);
                AppCompatRatingBar ratingBar = v.findViewById(R.id.rating);
                Map.Entry entry = (Map.Entry) iterator.next();
                String key = String.valueOf(entry.getKey());
                String[] parts = key.split("#");
                name.setTag(parts[0]);
                name.setText(parts[1]);
                detailclassification_layout.addView(v);

            }
            for (int i = 0; i < detailclassification_layout.getChildCount(); i++) {
                ConstraintLayout linearLayout = (ConstraintLayout) detailclassification_layout.getChildAt(i);
                AppCompatRatingBar ratingBar = (AppCompatRatingBar) linearLayout.getChildAt(1);
                TextView textView = (TextView) linearLayout.getChildAt(0);
                String rating_id = textView.getTag().toString();
                if (detailed_rating.length() > 0) {
                    for (int k = 0; k < detailed_rating.length(); k++) {
                        if (detailed_rating.getJSONObject(k).getString("rating_code").trim().equalsIgnoreCase(textView.getText().toString().trim())) {
                            ratingBar.setRating(Float.parseFloat(detailed_rating.getJSONObject(k).getString("value")));
                            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                                @Override
                                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                                    if (rating < 3) {
                                        Drawable stars = ratingBar.getProgressDrawable();
                                        stars.setColorFilter(Color.parseColor("#ebebeb"), PorterDuff.Mode.SRC_ATOP);
                                        LayerDrawable star = (LayerDrawable) ratingBar.getProgressDrawable();
                                        star.getDrawable(2).setColorFilter(Color.parseColor("#DD2022"), PorterDuff.Mode.SRC_ATOP);
                                    }
                                    if (rating >= 3 && rating < 4) {
                                        Drawable stars = ratingBar.getProgressDrawable();
                                        stars.setColorFilter(Color.parseColor("#ebebeb"), PorterDuff.Mode.SRC_ATOP);
                                        LayerDrawable star = (LayerDrawable) ratingBar.getProgressDrawable();
                                        star.getDrawable(2).setColorFilter(Color.parseColor("#DB701B"), PorterDuff.Mode.SRC_ATOP);
                                    }
                                    if (rating >= 4) {
                                        Drawable stars = ratingBar.getProgressDrawable();
                                        stars.setColorFilter(Color.parseColor("#ebebeb"), PorterDuff.Mode.SRC_ATOP);
                                        LayerDrawable star = (LayerDrawable) ratingBar.getProgressDrawable();
                                        star.getDrawable(2).setColorFilter(Color.parseColor("#136A42"), PorterDuff.Mode.SRC_ATOP);
                                    }
                                }
                            });
                        }
                    }
                }
                Log.i(TAG, "setRating: ratinBar " + ratingBar.getRating() + "\n TAG " + textView.getTag().toString() + "\n NAME " + textView.getText().toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), ManageProductReview.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}