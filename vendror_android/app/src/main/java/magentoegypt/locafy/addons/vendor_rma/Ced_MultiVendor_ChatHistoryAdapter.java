package magentoegypt.locafy.addons.vendor_rma;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import magentoegypt.locafy.base_app.Ced_MultiVendor_FontSetting;
import magentoegypt.locafy.R;
import magentoegypt.locafy_constant.AppConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cedcoss on 10/1/18.
 */


class Ced_MultiVendor_ChatHistoryAdapter extends BaseAdapter {

    private Context act;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;
    Ced_MultiVendor_FontSetting fontSetting;
    JSONArray item_info;


    public Ced_MultiVendor_ChatHistoryAdapter(Activity orderview, JSONArray item_info) {
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
            convertView = inflater.inflate(R.layout.ced_multivendor_chat_history_items, null);
        }

        LinearLayout itemLayout = (LinearLayout) convertView.findViewById(R.id.item_layout);
        TextView message = (TextView) convertView.findViewById(R.id.message);
        TextView sender = (TextView) convertView.findViewById(R.id.sender);
        TextView message_date = (TextView) convertView.findViewById(R.id.message_date);
        TextView downloadfile = (TextView) convertView.findViewById(R.id.downloadfile);

        fontSetting.setFontforTextviews(message_date, "Roboto-Medium.ttf", act);
        fontSetting.setFontforTextviews(sender, "Roboto-Medium.ttf", act);
        fontSetting.setFontforTextviews(message, "Roboto-Regular.ttf", act);
        fontSetting.setFontforTextviews(downloadfile, "Roboto-Regular.ttf", act);

        try {
            final JSONObject item_data = item_info.getJSONObject(position);

            message_date.setText(item_data.getString("chat_date"));
            sender.setText(item_data.getString("sender"));
            if (item_data.getString("sender").contains("Merchant")) {
                itemLayout.setBackgroundColor(act.getResources().getColor(R.color.merchantcolor));
            } else
                itemLayout.setBackgroundColor(act.getResources().getColor(R.color.vendorcolor));


            message.setText(item_data.getString("chat_text"));

            if (item_data.has("chat_file") && !TextUtils.isEmpty(item_data.getString("chat_file"))) {
                final String fileURL = item_data.getString("chat_file");
                //   if (fileURL.contains(".png")||fileURL.contains(".jpeg")){
                String parts[] = fileURL.split("/");
                final String filename = (parts[parts.length - 1].toString());
                downloadfile.setText(fileURL);
                downloadfile.setVisibility(View.VISIBLE);
                downloadfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppConstant.lockButton(v);
                        Log.i("REpo", "fileurl : " + fileURL);
                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item_data.getString("chat_file")));
                            act.startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                      /*  if (Ced_MultiVendor_RMA_RequestView.permissionsgranted) {
                            DownloadManager.Request r = new DownloadManager.Request(Uri.parse(fileURL));
                            r.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);
                            r.allowScanningByMediaScanner();
                            r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            DownloadManager dm = (DownloadManager) act.getSystemService(DOWNLOAD_SERVICE);
                            try {
                                dm.enqueue(r);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(act, act.getResources().getString(R.string.Storagepermissionsnotgranted), Toast.LENGTH_SHORT).show();
                        }*/
                    }
                });
            } else {
                downloadfile.setVisibility(View.GONE);
            }

            //  }
           /* else {
                downloadfile.setVisibility(View.GONE);
            }*/
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }

}