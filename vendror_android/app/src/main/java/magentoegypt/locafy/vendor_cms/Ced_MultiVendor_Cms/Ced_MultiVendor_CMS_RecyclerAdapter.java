package magentoegypt.locafy.vendor_cms.Ced_MultiVendor_Cms;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import magentoegypt.locafy.R;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_ClientRequestResponseRest_New;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_UnAuthourizedRequestError;
import magentoegypt.locafy_constant.AppConstant;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.base_app.OnBottomReachedListener;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cedcoss on 6/1/18.
 */

public class Ced_MultiVendor_CMS_RecyclerAdapter extends RecyclerView.Adapter<Ced_MultiVendor_CMS_RecyclerAdapter.ViewHolder> {

    Activity context;
    ArrayList<HashMap<String, String>> CmsArraylist;
    HashMap<String, String> datahashmap;
    String delete_url = "";
    Ced_MultiVendor_VendorSessionManagement vendorSessionManagement;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    JSONArray layout_list;
    OnBottomReachedListener onBottomReachedListener;

    public Ced_MultiVendor_CMS_RecyclerAdapter(Activity context, ArrayList<HashMap<String, String>> CmsArraylist, JSONArray layout_list) {
        this.context = context;
        this.CmsArraylist = CmsArraylist;
        this.layout_list = layout_list;
        datahashmap = new HashMap<String, String>();
        vendorSessionManagement = new Ced_MultiVendor_VendorSessionManagement(context);
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(context);
        delete_url = vendorSessionManagement.getBase_Url() + "rest/all/V1/vcmspage/delete";
//        delete_url = vendorSessionManagement.getBase_Url() + "vcmsapi/vcmspage/delete";
    }

    public void setOnBottomReachedListener(OnBottomReachedListener onBottomReachedListener) {
        this.onBottomReachedListener = onBottomReachedListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ced_multivendor_cms_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (CmsArraylist.size() >= 3) {
            if (position == CmsArraylist.size() - 1) {
                try {
                    onBottomReachedListener.onBottomReached(position);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        HashMap<String, String> cmshash = CmsArraylist.get(position);
        Log.i("hello", "cmshash== " + cmshash);

        holder.cmspageid.setText(cmshash.get("page_id"));
        if (cmshash.get("is_approve").equals("true")) {
            holder.cmsapprove.setText(context.getResources().getText(R.string.approved));
        } else if (cmshash.get("is_approve").equals("false")) {
            holder.cmsapprove.setText(context.getResources().getText(R.string.pending));
        }
        holder.cmstitle.setText(cmshash.get("title"));
        holder.cmspagelayout.setText(cmshash.get("page_layout"));
        if (cmshash.get("is_active").equals("true")) {
            holder.cmsactive.setText(context.getResources().getText(R.string.enabled));
        } else if (cmshash.get("is_active").equals("false")) {
            holder.cmsactive.setText(context.getResources().getText(R.string.disabled));
        }
        holder.cmsidentifier.setText(cmshash.get("identifier"));
        holder.cms_creationtime.setText(cmshash.get("creation_time"));
        holder.cms_updationtime.setText(cmshash.get("update_time"));
        holder.cmsdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstant.lockButton(v);
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(R.string.alert_name);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setMessage(R.string.confirm_first);
                builder.setNegativeButton(R.string.no,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, int which) {
                                datahashmap.put("page_id", holder.cmspageid.getText().toString());
                                datahashmap.put("vendor_id", vendorSessionManagement.getVendorid());
//                                datahashmap.put("hashkey", vendorSessionManagement.getHahkey());
                                Ced_ClientRequestResponseRest_New crr = new Ced_ClientRequestResponseRest_New(new AsyncResponse() {
                                    @Override
                                    public void processFinish(Object output) throws JSONException {
                                        String Jstring = output.toString();
                                        if (functionalityList.getExtensionAddon()) {
                                            JSONObject object = new JSONObject(Jstring);
                                            String status = object.getString("success");
                                            if (status.equals("true")) {
                                                dialog.dismiss();
                                                Toast.makeText(context, object.getString("message"), Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(context, Ced_MultiVendor_CmsListing.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                context.startActivity(intent);
                                            } else {
                                                Toast.makeText(context, object.getString("message"), Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            Intent intent = new Intent(context, Ced_MultiVendor_UnAuthourizedRequestError.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            context.startActivity(intent);
                                        }
                                    }
                                }, context, "POST", datahashmap.toString(), false, true);
                                crr.execute(delete_url);
                            }
                        }
                );
                builder.show();
            }
        });
        holder.cmsedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppConstant.lockButton(view);
                Intent intent = new Intent(context, Ced_MultiVendor_UpdateCms.class);
                intent.putExtra("page_id", holder.cmspageid.getText().toString());
                intent.putExtra("layout_data", layout_list.toString());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return CmsArraylist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView cmspageid, cmsapprove, cmstitle, cmspagelayout, cmsactive, cmsidentifier, cms_creationtime, cms_updationtime;
        TextView cmsedit, cmsdelete;

        public ViewHolder(View itemView) {
            super(itemView);
            cmspageid = itemView.findViewById(R.id.MultiVendor_cms_pageid);
            cmsapprove = itemView.findViewById(R.id.MultiVendor_cms_approve);
            cmstitle = itemView.findViewById(R.id.MultiVendor_cms_title);
            cmspagelayout = itemView.findViewById(R.id.MultiVendor_cms_pagelayout);
            cmsactive = itemView.findViewById(R.id.MultiVendor_cms_status);
            cmsidentifier = itemView.findViewById(R.id.MultiVendor_cms_urlkey);
            cms_creationtime = itemView.findViewById(R.id.MultiVendor_cms_creationtime);
            cms_updationtime = itemView.findViewById(R.id.MultiVendor_cms_updationtime);
            cmsedit = itemView.findViewById(R.id.MultiVendor_edit);
            cmsdelete = itemView.findViewById(R.id.MultiVendor_delete);
        }
    }
}
