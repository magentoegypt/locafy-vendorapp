package magentoegypt.locafy.vendor_cms.Ced_MultiVendor_StaticBlock;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_UnAuthourizedRequestError;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_ClientRequestResponseRest_New;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;
import magentoegypt.locafy.R;
import magentoegypt.locafy_constant.AppConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cedcoss on 6/1/18.
 */

public class Ced_MultiVendor_Block_RecyclerAdapter extends RecyclerView.Adapter<Ced_MultiVendor_Block_RecyclerAdapter.ViewHolder> {
    Activity context;
    ArrayList<HashMap<String, String>> BlockArraylist = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> datahashmap;
    String delete_url = "";
    String editcurrent_url = "";
    Ced_MultiVendor_VendorSessionManagement vendorSessionManagement;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;

    public Ced_MultiVendor_Block_RecyclerAdapter(Activity context, ArrayList<HashMap<String, String>> BlockArraylist) {
        this.context = context;
        this.BlockArraylist = BlockArraylist;
        datahashmap = new HashMap<String, String>();
        vendorSessionManagement = new Ced_MultiVendor_VendorSessionManagement(context);
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(context);
        delete_url = vendorSessionManagement.getBase_Url() + "rest/all/V1/vcmsblock/delete";
//        delete_url=vendorSessionManagement.getBase_Url()+"vcmsapi/vcmsblock/delete";

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ced_multivendor_block_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        HashMap<String, String> blockhash = new HashMap<String, String>();
        blockhash = BlockArraylist.get(position);

        holder.blockpageid.setText(blockhash.get("blockid"));
        holder.blocktitle.setText(blockhash.get("title"));
        holder.blockidentifier.setText(blockhash.get("identifier"));
        if (blockhash.get("isactive").equals("1")) {
            holder.blockactive.setText("Enabled");
        } else if (blockhash.get("isactive").equals("0")) {
            holder.blockactive.setText("Disabled");
        }
        if (blockhash.get("isapprove").equals("1")) {
            holder.blockapprove.setText("Approved");
        } else if (blockhash.get("isapprove").equals("0")) {
            holder.blockapprove.setText("Pending");
        }
        holder.block_creationtime.setText(blockhash.get("creationtime"));
        holder.block_updationtime.setText(blockhash.get("updationtime"));
        holder.blockedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        holder.blockdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(R.string.alert_name);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setMessage(R.string.confirm_first);
                builder.setNegativeButton(R.string.no,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                datahashmap.put("block_id", holder.blockpageid.getText().toString());
                                datahashmap.put("vendor_id", vendorSessionManagement.getVendorid());
//                                datahashmap.put("hashkey", vendorSessionManagement.getHahkey());
                                Ced_ClientRequestResponseRest_New crr = new Ced_ClientRequestResponseRest_New(new AsyncResponse() {
                                    @Override
                                    public void processFinish(Object output) throws JSONException {
                                        String Jstring = output.toString();
                                        if (functionalityList.getExtensionAddon()) {
                                            JSONObject object = new JSONObject(Jstring);
                                            String success = object.getString("success");
                                            if (success.equals("true")) {
                                                Toast.makeText(context, object.getString("message"), Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(context, Ced_MultiVendor_BlockListing.class);
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
                                }, context, "POST", datahashmap.toString(),false,true);
                                crr.execute(delete_url);
                            }
                        }
                );
                builder.show();
            }
        });

        holder.blockedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppConstant.lockButton(view);
                Intent intent = new Intent(context, Ced_MultiVendor_UpdateBlock.class);
                intent.putExtra("blockid", holder.blockpageid.getText().toString());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return BlockArraylist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView blockpageid, blocktitle, blockactive, blockidentifier, block_creationtime, block_updationtime, blockapprove;
        TextView blockedit, blockdelete;

        public ViewHolder(View itemView) {
            super(itemView);
            blockpageid = (TextView) itemView.findViewById(R.id.MultiVendor_block_pageid);
            blocktitle = (TextView) itemView.findViewById(R.id.MultiVendor_block_title);
            blockidentifier = (TextView) itemView.findViewById(R.id.MultiVendor_block_urlkey);
            blockactive = (TextView) itemView.findViewById(R.id.MultiVendor_block_status);
            blockapprove = (TextView) itemView.findViewById(R.id.MultiVendor_block_approve);
            block_creationtime = (TextView) itemView.findViewById(R.id.MultiVendor_block_creationtime);
            block_updationtime = (TextView) itemView.findViewById(R.id.MultiVendor_block_updationtime);
            blockedit = (TextView) itemView.findViewById(R.id.MultiVendor_edit);
            blockdelete = (TextView) itemView.findViewById(R.id.MultiVendor_delete);
        }
    }
}
