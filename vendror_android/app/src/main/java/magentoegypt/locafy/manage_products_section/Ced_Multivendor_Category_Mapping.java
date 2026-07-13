package magentoegypt.locafy.manage_products_section;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import magentoegypt.locafy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;

public class Ced_Multivendor_Category_Mapping extends AppCompatActivity {


    String category = "";


    LinearLayout categoryselection;
    JSONArray catfilters;
    HashSet<String> selected_category_ids;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ced__multivendor__category__mapping);
        categoryselection = (LinearLayout) findViewById(R.id.MultiVendor_categoryselection);
        selected_category_ids = new HashSet<>();

        /******************Categories************************************************/

        try {
            catfilters = new JSONArray(category);

        if (catfilters.length() > 0) {
            JSONObject current_object = null;
            View category_view = null;
            TextView subcategories = null;
            CheckBox main_cat = null;
            ImageView cat_icon = null;
            String maincatname = null;
            String maincatid = null;
            String parts[] = new String[2];
            for (int i = 0; i < catfilters.length(); i++) {
                current_object = catfilters.getJSONObject(i);
                category_view = View.inflate(Ced_Multivendor_Category_Mapping.this, R.layout.ced_multivendor_maincategory, null);
                subcategories = (TextView) category_view.findViewById(R.id.subcategories);
                main_cat = (CheckBox) category_view.findViewById(R.id.main_cat);
                cat_icon = (ImageView) category_view.findViewById(R.id.cat_icon);
                parts = current_object.getString("main_category").split("#");
                maincatname = parts[1];
                maincatid = parts[0];
                if (current_object.has("sub_categories")) {
                    subcategories.setText(current_object.getJSONArray("sub_categories").toString());
                } else {
                    cat_icon.setVisibility(View.GONE);
                }
                main_cat.setText(maincatname);
                main_cat.setId(Integer.parseInt(maincatid));
                final CheckBox finalMain_cat = main_cat;
                main_cat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            selected_category_ids.add(String.valueOf(finalMain_cat.getId()));
                        } else {
                            selected_category_ids.remove(String.valueOf(finalMain_cat.getId()));
                        }
                    }
                });
                final boolean[] open = {false};
                final ImageView finalCat_icon = cat_icon;
                final ImageView finalCat_icon1 = cat_icon;
                cat_icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LinearLayout parent = (LinearLayout) finalCat_icon.getParent();
                        LinearLayout subparent = (LinearLayout) parent.getParent();
                        RelativeLayout mainparent = (RelativeLayout) subparent.getParent();
                        TextView subcatjson = (TextView) mainparent.getChildAt(0);
                        LinearLayout subcats = (LinearLayout) subparent.getChildAt(1);
                        if (open[0]) {
                            subcats.removeAllViews();
                            open[0] = false;
                            finalCat_icon1.setImageResource(R.drawable.expand);
                        } else {
                            try {
                                if (!(subcatjson.getText().toString().isEmpty())) {
                                    JSONArray array = new JSONArray(subcatjson.getText().toString());
                                    createsubcat(array, subcats);
                                    open[0] = true;
                                    finalCat_icon1.setImageResource(R.drawable.collapse);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                categoryselection.addView(category_view);
            }

        }
            /******************Categories************************************************/
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

    }

    private void createsubcat(JSONArray array, LinearLayout subcats) {
        try {
            JSONObject current_object = null;
            View category_view = null;
            TextView subcategories = null;
            CheckBox main_cat = null;
            ImageView cat_icon = null;
            String maincatname = null;
            String maincatid = null;
            String parts[] = new String[2];
            for (int i = 0; i < array.length(); i++) {
                current_object = array.getJSONObject(i);
                category_view = View.inflate(Ced_Multivendor_Category_Mapping.this, R.layout.ced_multivendor_maincategory, null);
                subcategories = (TextView) category_view.findViewById(R.id.subcategories);
                main_cat = (CheckBox) category_view.findViewById(R.id.main_cat);
                cat_icon = (ImageView) category_view.findViewById(R.id.cat_icon);
                parts = current_object.getString("main_category").split("#");
                maincatname = parts[1];
                maincatid = parts[0];
                if (current_object.has("sub_categories")) {
                    subcategories.setText(current_object.getJSONArray("sub_categories").toString());
                } else {
                    cat_icon.setVisibility(View.GONE);
                }
                main_cat.setText(maincatname);
                main_cat.setId(Integer.parseInt(maincatid));
                final CheckBox finalMain_cat = main_cat;
                main_cat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            selected_category_ids.add(String.valueOf(finalMain_cat.getId()));
                        } else {
                            selected_category_ids.remove(String.valueOf(finalMain_cat.getId()));
                        }
                    }
                });
                final boolean[] open = {false};
                final ImageView finalCat_icon = cat_icon;
                final ImageView finalCat_icon1 = cat_icon;
                cat_icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LinearLayout parent = (LinearLayout) finalCat_icon.getParent();
                        LinearLayout subparent = (LinearLayout) parent.getParent();
                        RelativeLayout mainparent = (RelativeLayout) subparent.getParent();
                        TextView subcatjson = (TextView) mainparent.getChildAt(0);
                        LinearLayout subcats = (LinearLayout) subparent.getChildAt(1);
                        if (open[0]) {
                            subcats.removeAllViews();
                            open[0] = false;
                            finalCat_icon1.setImageResource(R.drawable.expand);
                        } else {
                            try {
                                if (!(subcatjson.getText().toString().isEmpty())) {
                                    JSONArray array = new JSONArray(subcatjson.getText().toString());
                                    createsubcat(array, subcats);
                                    open[0] = true;
                                    finalCat_icon1.setImageResource(R.drawable.collapse);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                subcats.addView(category_view);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
