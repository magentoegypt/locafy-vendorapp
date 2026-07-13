package magentoegypt.locafy.addons.vendor_rma;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import magentoegypt.locafy.base_app.Ced_MultiVendor_FontSetting;
import magentoegypt.locafy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cedcoss on 10/1/18.
 */

class Ced_MultiVendor_ItemInfoAdapter extends BaseAdapter {

    private Context act;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;
    Ced_MultiVendor_FontSetting fontSetting;
    JSONArray item_info;

    public Ced_MultiVendor_ItemInfoAdapter(Activity orderview, ArrayList<HashMap<String, String>> orderinfo) {
        act = orderview;
        data = orderinfo;
        fontSetting = new Ced_MultiVendor_FontSetting();
    }

    public Ced_MultiVendor_ItemInfoAdapter(Activity orderview, JSONArray item_info) {
        act = orderview;
        this.item_info = item_info;
        fontSetting = new Ced_MultiVendor_FontSetting();
    }

    @Override
    public int getCount() {
        return item_info.length();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.ced_multivendor_iteminfo_items_list, null);
        }

        TextView prod_name_head = (TextView) convertView.findViewById(R.id.MultiVendor_prod_name_head);
        TextView prod_name = (TextView) convertView.findViewById(R.id.MultiVendor_prod_name);
        TextView prod_price_head = (TextView) convertView.findViewById(R.id.MultiVendor_prod_price_head);
        TextView prod_price = (TextView) convertView.findViewById(R.id.MultiVendor_prod_price);
        TextView prod_qty_head = (TextView) convertView.findViewById(R.id.MultiVendor_prod_qty_head);
        TextView prod_qty = (TextView) convertView.findViewById(R.id.MultiVendor_prod_qty);
        TextView prod_sku = (TextView) convertView.findViewById(R.id.MultiVendor_prod_sku);
        TextView prod_sku_head = (TextView) convertView.findViewById(R.id.MultiVendor_prod_sku_head);
        TextView row_total = (TextView) convertView.findViewById(R.id.row_total);
        TextView row_total_txt = convertView.findViewById(R.id.row_total_txt);

        fontSetting.setFontforTextviews(prod_name_head, "Roboto-Medium.ttf", act);
        fontSetting.setFontforTextviews(prod_price_head, "Roboto-Medium.ttf", act);
        fontSetting.setFontforTextviews(prod_qty_head, "Roboto-Medium.ttf", act);
        fontSetting.setFontforTextviews(prod_sku_head, "Roboto-Medium.ttf", act);
        fontSetting.setFontforTextviews(row_total_txt, "Roboto-Medium.ttf", act);

        fontSetting.setFontforTextviews(prod_name, "Roboto-Regular.ttf", act);
        fontSetting.setFontforTextviews(prod_price, "Roboto-Regular.ttf", act);
        fontSetting.setFontforTextviews(prod_qty, "Roboto-Regular.ttf", act);
        fontSetting.setFontforTextviews(prod_sku, "Roboto-Regular.ttf", act);
        fontSetting.setFontforTextviews(row_total, "Roboto-Regular.ttf", act);

        try {
            JSONObject item_data = item_info.getJSONObject(position);
            prod_name.setText(item_data.getString("product_name"));
            prod_price.setText(item_data.getString("price"));
            prod_qty.setText(item_data.getString("rma_qty"));
            prod_sku.setText(item_data.getString("product_sku"));
            row_total.setText(item_data.getString("row_total"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }

}


