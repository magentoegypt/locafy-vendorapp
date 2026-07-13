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

package magentoegypt.locafy.manage_products_section;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import magentoegypt.locafy.R;
import magentoegypt.locafy.base_app.Ced_MultiVendor_FontSetting;
import magentoegypt.locafy_constant.Ced_MultiVendor_GlobalVariables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Ced_MultiVendor_ConfigurableProductAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    Ced_MultiVendor_FontSetting fontSetting;
    private final Activity activity;
    private final ArrayList<HashMap<String, String>> data;

    public Ced_MultiVendor_ConfigurableProductAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        fontSetting = new Ced_MultiVendor_FontSetting();
        activity = a;
        data = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("NewApi")
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null) {
            vi = inflater.inflate(R.layout.ced_multivendor_configproductlist_item, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.attribuetsetname = (TextView) vi.findViewById(R.id.MultiVendor_attribuetsetname);
            viewHolder.skutag = (TextView) vi.findViewById(R.id.MultiVendor_skutag);
            viewHolder.RegularPricetag = (TextView) vi.findViewById(R.id.MultiVendor_RegularPricetag);
            viewHolder.statustag = (TextView) vi.findViewById(R.id.MultiVendor_statustag);
            viewHolder.product_id = (TextView) vi.findViewById(R.id.MultiVendor_product_id);
            viewHolder.attributesetnametag = (TextView) vi.findViewById(R.id.MultiVendor_attributesetnametag);
            viewHolder.product_name = (TextView) vi.findViewById(R.id.MultiVendor_product_name);
            viewHolder.RegularPrice = (TextView) vi.findViewById(R.id.MultiVendor_RegularPrice);
            viewHolder.sku = (TextView) vi.findViewById(R.id.MultiVendor_sku);
            viewHolder.Status = (TextView) vi.findViewById(R.id.MultiVendor_Status);
            viewHolder.count = (TextView) vi.findViewById(R.id.MultiVendor_count);
            viewHolder.json = (TextView) vi.findViewById(R.id.MultiVendor_json);
            viewHolder.selectrelated = (CheckBox) vi.findViewById(R.id.MultiVendor_selectrelated);
            viewHolder.configoptions = (LinearLayout) vi.findViewById(R.id.MultiVendor_configoptions);
            /**************************Regular*****************************/
            fontSetting.setFontforTextviews(viewHolder.product_id, "Roboto-Regular.ttf", activity);
            fontSetting.setFontforTextviews(viewHolder.product_name, "Roboto-Regular.ttf", activity);
            fontSetting.setFontforTextviews(viewHolder.RegularPrice, "Roboto-Regular.ttf", activity);
            fontSetting.setFontforTextviews(viewHolder.attribuetsetname, "Roboto-Regular.ttf", activity);
            fontSetting.setFontforTextviews(viewHolder.sku, "Roboto-Regular.ttf", activity);
            fontSetting.setFontforTextviews(viewHolder.Status, "Roboto-Regular.ttf", activity);
            /*******************************************************/
            /**************************Medium*****************************/
            fontSetting.setFontforTextviews(viewHolder.attributesetnametag, "Roboto-Medium.ttf", activity);
            fontSetting.setFontforTextviews(viewHolder.skutag, "Roboto-Medium.ttf", activity);
            fontSetting.setFontforTextviews(viewHolder.RegularPricetag, "Roboto-Medium.ttf", activity);
            fontSetting.setFontforTextviews(viewHolder.statustag, "Roboto-Medium.ttf", activity);
            /*******************************************************/
            vi.setTag(viewHolder);
        }
        HashMap<String, String> song = new HashMap<String, String>();
        song = data.get(position);
        final ViewHolder holder = (ViewHolder) vi.getTag();
        holder.product_id.setText(song.get("product_id"));
        holder.product_name.setText(song.get("product_name"));
        holder.RegularPrice.setText(song.get("regular_price"));
        holder.Status.setText(song.get("inventory"));
        holder.attribuetsetname.setText(song.get("set_name"));
        holder.sku.setText(song.get("sku"));
        holder.count.setText(song.get("options"));
        holder.json.setText(song.get("attribute"));
        Log.i("JSON", holder.json.getText().toString());
        if (Ced_MultiVendor_GlobalVariables.Configurableproductids.size() > 0) {
            holder.selectrelated.setChecked(Ced_MultiVendor_GlobalVariables.Configurableproductids.contains(holder.product_id.getText()));
        } else {
            holder.selectrelated.setChecked(false);
        }
        holder.selectrelated.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!(Ced_MultiVendor_GlobalVariables.Configurableproductids.contains(holder.product_id.getText().toString()))) {

                        if (Ced_MultiVendor_GlobalVariables.valuefordisableconfig.size() > 0) {
                            try {

                                JSONArray jsonArray = new JSONArray();
                                String data = "";
                                JSONObject jsonObject = new JSONObject(holder.json.getText().toString());
                                JSONArray array = jsonObject.getJSONArray("attribute");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    JSONObject object1 = new JSONObject();
                                    object1.put("label", object.getString("attribute_label"));
                                    object1.put("attribute_id", object.getString("attribute_id"));
                                    object1.put("value_index", object.getString("value"));
                                    object1.put("is_percent", "0");
                                    object1.put("pricing_value", " ");
                                    data = object.getString("attribute_id") + "#" + object.getString("value") + data;
                                    jsonArray.put(object1);

                                }
                                Log.i("data1", "" + data);
                                if (Ced_MultiVendor_GlobalVariables.valuefordisableconfig.contains(data)) {
                                    holder.selectrelated.setChecked(false);
                                    Toast.makeText(activity, "Same Combination cannot be selected twice", Toast.LENGTH_LONG).show();
                                } else {
                                    Ced_MultiVendor_GlobalVariables.Configurableproductids.add(holder.product_id.getText().toString());
                                    Ced_MultiVendor_GlobalVariables.valuefordisableconfig.add(data);
                                    String[] parts = holder.product_id.getText().toString().split("#");
                                    Ced_MultiVendor_GlobalVariables.configurable_data.put(parts[1], jsonArray);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {

                            Ced_MultiVendor_GlobalVariables.Configurableproductids.add(holder.product_id.getText().toString());
                            try {

                                JSONArray jsonArray = new JSONArray();
                                String data = "";
                                JSONObject jsonObject = new JSONObject(holder.json.getText().toString());
                                JSONArray array = jsonObject.getJSONArray("attribute");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    JSONObject object1 = new JSONObject();
                                    object1.put("label", object.getString("attribute_label"));
                                    object1.put("attribute_id", object.getString("attribute_id"));
                                    object1.put("value_index", object.getString("value"));
                                    object1.put("is_percent", "0");
                                    object1.put("pricing_value", " ");
                                    data = object.getString("attribute_id") + "#" + object.getString("value") + data;
                                    jsonArray.put(object1);

                                }
                                if (activity.getResources().getString(R.string.Enable_Log).equals("yes")) {
                                    Log.i("data2", "" + data);
                                }
                                Ced_MultiVendor_GlobalVariables.valuefordisableconfig.add(data);
                                String[] parts = holder.product_id.getText().toString().split("#");
                                Ced_MultiVendor_GlobalVariables.configurable_data.put(parts[1], jsonArray);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                } else {
                    if ((Ced_MultiVendor_GlobalVariables.Configurableproductids.contains(holder.product_id.getText().toString()))) {
                        Ced_MultiVendor_GlobalVariables.Configurableproductids.remove(holder.product_id.getText().toString());
                        String[] parts = holder.product_id.getText().toString().split("#");
                        try {
                            JSONArray jsonArray = Ced_MultiVendor_GlobalVariables.configurable_data.getJSONArray(parts[1]);
                            String data = "";
                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject object = jsonArray.getJSONObject(j);
                                data = object.getString("attribute_id") + "#" + object.getString("value_index") + data;
                            }
                            Ced_MultiVendor_GlobalVariables.valuefordisableconfig.remove(data);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Ced_MultiVendor_GlobalVariables.configurable_data.remove(parts[1]);
                    }
                }
                if (activity.getResources().getString(R.string.Enable_Log).equals("yes")) {
                    Log.i("valuefordisableconfig", "" + Ced_MultiVendor_GlobalVariables.valuefordisableconfig);
                    Log.i("configurable_data", "" + Ced_MultiVendor_GlobalVariables.configurable_data);
                }


            }
        });
        return vi;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    static class ViewHolder {

        public TextView skutag;
        public TextView RegularPricetag;
        public TextView statustag;
        public TextView product_id;
        public TextView product_name;
        public TextView sku;
        public TextView RegularPrice;
        public TextView Status;
        public TextView attribuetsetname;
        public TextView attributesetnametag;
        public TextView count;
        public TextView json;
        public CheckBox selectrelated;
        public LinearLayout configoptions;
    }


}