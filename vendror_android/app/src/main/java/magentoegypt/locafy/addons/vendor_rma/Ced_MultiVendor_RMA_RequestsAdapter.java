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
 * Created by cedcoss on 9/1/18.
 */

public class Ced_MultiVendor_RMA_RequestsAdapter extends BaseAdapter {
    Activity act;
    Ced_MultiVendor_FontSetting fontSetting;
    private ArrayList<HashMap<String,String>> data;
    JSONArray arrayData;
    private static LayoutInflater inflater=null;

    /*public Ced_MultiVendor_RMA_RequestsAdapter(Activity ordersList, ArrayList<HashMap<String, String>> orderinfo) {
        act=ordersList;
        data=orderinfo;
        fontSetting=new Ced_MultiVendor_FontSetting();
    }*/

    public Ced_MultiVendor_RMA_RequestsAdapter(Activity ordersList, JSONArray arrayData) {
        act=ordersList;
        this.arrayData=arrayData;
        fontSetting=new Ced_MultiVendor_FontSetting();
    }

    @Override
    public int getCount() {
        return arrayData.length();
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
            convertView=inflater.inflate(R.layout.ced_multivendor_rma_request_list_item,null);
        }

        TextView rmarequestid= (TextView) convertView.findViewById(R.id.rmarequestid);

        TextView purchased_point= (TextView) convertView.findViewById(R.id.purchased_point);
        fontSetting.setFontforTextviews(purchased_point,"Roboto-Regular.ttf",act);

        TextView order_id= (TextView) convertView.findViewById(R.id.order_id);
        fontSetting.setFontforTextviews(order_id,"Roboto-Regular.ttf",act);

        TextView rma_id= (TextView) convertView.findViewById(R.id.rma_id);
        fontSetting.setFontforTextviews(rma_id,"Roboto-Regular.ttf",act);

        TextView customer_name= (TextView) convertView.findViewById(R.id.customer_name);
        fontSetting.setFontforTextviews(customer_name,"Roboto-Regular.ttf",act);

        TextView customer_email= (TextView) convertView.findViewById(R.id.customer_email);
        fontSetting.setFontforTextviews(customer_email,"Roboto-Regular.ttf",act);

        TextView status= (TextView) convertView.findViewById(R.id.status);
        fontSetting.setFontforTextviews(status,"Roboto-Regular.ttf",act);

        TextView resolution_requested= (TextView) convertView.findViewById(R.id.resolution_requested);
        fontSetting.setFontforTextviews(resolution_requested,"Roboto-Regular.ttf",act);

        TextView updated_at= (TextView) convertView.findViewById(R.id.updated_at);
        fontSetting.setFontforTextviews(resolution_requested,"Roboto-Regular.ttf",act);


        try {
            JSONObject requestData=arrayData.getJSONObject(position);

            rmarequestid.setText(requestData.getString("id"));
            purchased_point.setText(requestData.getString("purchased_point"));
            order_id.setText(requestData.getString("order_id"));
            rma_id.setText(requestData.getString("rma_id"));
            customer_name.setText(requestData.getString("customer_name"));
            customer_email.setText(requestData.getString("customer_email"));
            status.setText(requestData.getString("status"));
            resolution_requested.setText(requestData.getString("resolution_requested"));
            updated_at.setText(requestData.getString("updated_at"));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }
}