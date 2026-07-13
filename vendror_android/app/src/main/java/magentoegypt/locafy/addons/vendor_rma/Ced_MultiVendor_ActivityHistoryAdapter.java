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

class Ced_MultiVendor_ActivityHistoryAdapter extends BaseAdapter {

    private Context act;
    private ArrayList<HashMap<String,String>> data;
    private static LayoutInflater inflater=null;
    Ced_MultiVendor_FontSetting fontSetting;
    JSONArray item_info;


    public Ced_MultiVendor_ActivityHistoryAdapter(Activity orderview, JSONArray item_info) {
        act=orderview;
        this.item_info=item_info;
        fontSetting=new Ced_MultiVendor_FontSetting();
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
        if (convertView==null)
        {
            inflater= (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.ced_multivendor_activity_history_items,null);
        }

        TextView notification = (TextView) convertView.findViewById(R.id.notification);
        TextView date = (TextView) convertView.findViewById(R.id.date);

        fontSetting.setFontforTextviews(notification,"Roboto-Medium.ttf",act);
        fontSetting.setFontforTextviews(date,"Roboto-Regular.ttf",act);

        try {
            JSONObject item_data=item_info.getJSONObject(position);

            date.setText(item_data.getString("date"));

            if (!item_data.getString("notification").equals("")){
                notification.setText(item_data.getString("notification"));
            }
            else {
                notification.setVisibility(View.GONE);
            }


        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }

}