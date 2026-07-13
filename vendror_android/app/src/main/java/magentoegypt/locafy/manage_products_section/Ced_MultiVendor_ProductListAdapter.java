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
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import magentoegypt.locafy.base_app.Ced_MultiVendor_FontSetting;
import magentoegypt.locafy_constant.Ced_MultiVendor_GlobalVariables;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_UnAuthourizedRequestError;
import magentoegypt.locafy.api_request_response_section.AsyncResponse;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;
import magentoegypt.locafy.R;
import magentoegypt.locafy_constant.AppConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Ced_MultiVendor_ProductListAdapter extends BaseAdapter {
    public static float density;
    private static LayoutInflater inflater = null;
    String Jstring;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    Ced_MultiVendor_VendorSessionManagement vendorSessionManagement;
    HashMap<String, String> dataforproducts;
    String Currenturl;
    String editCurrenturl;
    Ced_MultiVendor_FontSetting fontSetting;
    ArrayList<String> check_list = new ArrayList<>();
    private final Activity activity;
    private final ArrayList<HashMap<String, String>> data;


    public Ced_MultiVendor_ProductListAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        fontSetting = new Ced_MultiVendor_FontSetting();
        activity = a;
        data = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(activity);
        vendorSessionManagement = new Ced_MultiVendor_VendorSessionManagement(activity);
        dataforproducts = new HashMap<String, String>();
        Currenturl = vendorSessionManagement.getBase_Url() + "vendorapi/vproducts/delete";
        editCurrenturl = vendorSessionManagement.getBase_Url() + "vendorapi/vproducts/info";
        density = activity.getResources().getDisplayMetrics().densityDpi;

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
    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = null;
        if (convertView == null) {
            vi = inflater.inflate(R.layout.ced_multivendor_productlist, null);
            ViewHolder viewHolder = new ViewHolder();

            viewHolder.MultiVendor_website_text = vi.findViewById(R.id.MultiVendor_website_text);
            viewHolder.productIDtag = vi.findViewById(R.id.MultiVendor_productIDtag);
            viewHolder.qtytag = vi.findViewById(R.id.MultiVendor_qtytag);
            viewHolder.skutag = vi.findViewById(R.id.MultiVendor_skutag);
            viewHolder.Productypetag = vi.findViewById(R.id.MultiVendor_Productypetag);
            viewHolder.RegularPricetag = vi.findViewById(R.id.MultiVendor_RegularPricetag);
            viewHolder.ProductNametag = vi.findViewById(R.id.MultiVendor_ProductNametag);
            viewHolder.statustag = vi.findViewById(R.id.MultiVendor_statustag);
            viewHolder.product_id = vi.findViewById(R.id.MultiVendor_product_id);
            viewHolder.delete = vi.findViewById(R.id.MultiVendor_delete);
            viewHolder.edit = vi.findViewById(R.id.MultiVendor_edit);
            viewHolder.Productype = vi.findViewById(R.id.MultiVendor_Productype);
            viewHolder.product_name = vi.findViewById(R.id.MultiVendor_product_name);
            viewHolder.RegularPrice = vi.findViewById(R.id.MultiVendor_RegularPrice);
            viewHolder.qty = vi.findViewById(R.id.MultiVendor_qty);
            viewHolder.sku = vi.findViewById(R.id.MultiVendor_sku);
            viewHolder.Status = vi.findViewById(R.id.MultiVendor_Status);
            viewHolder.picurl = vi.findViewById(R.id.MultiVendor_picurl);
            viewHolder.prductimage = vi.findViewById(R.id.MultiVendor_prductimage);
            viewHolder.cb_free_shipping = vi.findViewById(R.id.cb_free_shipping);
            /**************************Regular*****************************/
            fontSetting.setFontforTextviews(viewHolder.product_id, "Roboto-Regular.ttf", activity);
            fontSetting.setFontforTextviews(viewHolder.Productype, "Roboto-Regular.ttf", activity);
            fontSetting.setFontforTextviews(viewHolder.product_name, "Roboto-Regular.ttf", activity);
            fontSetting.setFontforTextviews(viewHolder.RegularPrice, "Roboto-Regular.ttf", activity);
            fontSetting.setFontforTextviews(viewHolder.qty, "Roboto-Regular.ttf", activity);
            fontSetting.setFontforTextviews(viewHolder.sku, "Roboto-Regular.ttf", activity);
            fontSetting.setFontforTextviews(viewHolder.Status, "Roboto-Regular.ttf", activity);
            /*******************************************************/
            /**************************Medium*****************************/
            fontSetting.setFontforTextviews(viewHolder.delete, "Roboto-Medium.ttf", activity);
            fontSetting.setFontforTextviews(viewHolder.edit, "Roboto-Medium.ttf", activity);
            fontSetting.setFontforTextviews(viewHolder.productIDtag, "Roboto-Medium.ttf", activity);
            fontSetting.setFontforTextviews(viewHolder.qtytag, "Roboto-Medium.ttf", activity);
            fontSetting.setFontforTextviews(viewHolder.skutag, "Roboto-Medium.ttf", activity);
            fontSetting.setFontforTextviews(viewHolder.Productypetag, "Roboto-Medium.ttf", activity);
            fontSetting.setFontforTextviews(viewHolder.RegularPricetag, "Roboto-Medium.ttf", activity);
            fontSetting.setFontforTextviews(viewHolder.statustag, "Roboto-Medium.ttf", activity);
            fontSetting.setFontforTextviews(viewHolder.ProductNametag, "Roboto-Medium.ttf", activity);
            /*******************************************************/
            vi.setTag(viewHolder);
        } else {
            vi = convertView;
        }

        HashMap<String, String> song = new HashMap<String, String>();
        song = data.get(position);
        final ViewHolder holder = (ViewHolder) vi.getTag();
        holder.product_id.setText(song.get("product_id"));
        holder.Productype.setText(song.get("type"));
        holder.product_name.setText(song.get("product_name"));
        holder.RegularPrice.setText(song.get("regular_price"));
        holder.Status.setText(song.get("status"));
        if (holder.Status.getText().toString().toLowerCase().equals("pending")) {
            holder.Status.setTextColor(activity.getResources().getColor(R.color.pending));
        }

        if (holder.Status.getText().toString().toLowerCase().equals("approved")) {
            holder.Status.setTextColor(activity.getResources().getColor(R.color.approved));
        }

        if (holder.Status.getText().toString().toLowerCase().equals("not approved")) {
            holder.Status.setTextColor(activity.getResources().getColor(R.color.disapproved));
        }
        holder.qty.setText(song.get("qty"));
        holder.sku.setText(song.get("sku"));
        holder.MultiVendor_website_text.setText(song.get("website"));
        holder.picurl.setText(song.get("product_image"));
        Glide.with(activity)
                .load(song.get("product_image"))
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(holder.prductimage);
        holder.prductimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImage(holder.prductimage, holder.picurl.getText().toString());
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(
                        activity);
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

                                String[] product_id = holder.product_id.getText().toString().split("#");
                                dataforproducts.put("entity_id", product_id[1]);
                                dataforproducts.put("vendor_id", vendorSessionManagement.getVendorid());
                                dataforproducts.put("hashkey", vendorSessionManagement.getHahkey());
                                Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {
                                    @Override
                                    public void processFinish(Object output) throws JSONException {
                                        Jstring = output.toString();
                                        if (functionalityList.getExtensionAddon()) {

                                            JSONObject object = new JSONObject(Jstring);
                                            String success = object.getString("success");
                                            if (success.equals("true")) {
                                                Toast.makeText(activity, object.getString("message"), Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(activity, Ced_MultiVendor_ManageProducts.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                activity.startActivity(intent);
                                                activity.overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                                            } else {
                                                Toast.makeText(activity, object.getString("message"), Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            Intent intent = new Intent(activity, Ced_MultiVendor_UnAuthourizedRequestError.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            activity.startActivity(intent);
                                            activity.overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                                        }
                                    }
                                }, activity, "POST", dataforproducts);
                                crr.execute(Currenturl);
                            }
                        }
                );
                builder.show();
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstant.lockButton(v);
                final String[] product_id = holder.product_id.getText().toString().split("#");
                dataforproducts.put("product_id", product_id[1]);
//                if (vendorSessionManagement.getStoreId() != null)
//                dataforproducts.put("store_id", vendorSessionManagement.getStoreId());

                dataforproducts.put("vendor_id", vendorSessionManagement.getVendorid());
                dataforproducts.put("hashkey", vendorSessionManagement.getHahkey());
                Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(new AsyncResponse() {

                    @Override
                    public void processFinish(Object output) throws JSONException {
                        Jstring = output.toString();
                        if (functionalityList.getExtensionAddon()) {
                            JSONObject object = new JSONObject(Jstring);
                            String success = object.getJSONObject("data").getString("success");
                            if (success.equals("true")) {
                                JSONObject productdata = object.getJSONObject("data").getJSONObject("productdata");
                                String type = productdata.getString("type_id");
                                Intent update = new Intent(activity, EditProductDynamic.class);
                                update.putExtra("type", type);
                                update.putExtra("dataforproductcreation", Jstring);
                                update.putExtra("product_id", product_id[1]);
                                if (functionalityList.getProductAddon()) {
                                    if (type.equals("configurable") && object.getJSONObject("data").get("config_selected_attr") instanceof JSONObject) {
                                        Ced_MultiVendor_GlobalVariables.config_attribute_value = object.getJSONObject("data").getJSONObject("config_selected_attr");
                                    }
                                    String attribute_set = productdata.getString("attribute_set_id");
                                    update.putExtra("attribute_set", attribute_set);
                                }
                                activity.startActivity(update);
                                activity.overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);

                            }


                        } else {
                            Intent intent = new Intent(activity, Ced_MultiVendor_UnAuthourizedRequestError.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            activity.startActivity(intent);
                            activity.overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                        }

                    }
                }, activity, "POST", dataforproducts);
                crr.execute(editCurrenturl);
            }
        });


        holder.cb_free_shipping.setChecked(check_list.contains(position));

        holder.cb_free_shipping.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {

                    if (!check_list.contains(position))
                        check_list.add(position + "");
                    Toast.makeText(activity, "check", Toast.LENGTH_SHORT).show();

                } else {
                    if (check_list.contains(position + ""))
                        check_list.remove(position);
                    Toast.makeText(activity, "uncheck", Toast.LENGTH_SHORT).show();
                }

                Log.d("test_cb", check_list + "");
            }
        });

        return vi;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    public void showImage(ImageView image, String s) {
        Dialog builder = new Dialog(activity);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //nothing;
            }
        });

        ImageView imageView = new ImageView(activity);
        Glide.with(activity)
                .load(s)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(imageView);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);


        if (density <= 160) {
            builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                    200,
                    400));
        }
        if (density > 160 && density <= 240) {
            builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                    300,
                    500));
        }
        if (density > 240 && density <= 360) {
            builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                    400,
                    600));
        }
        if (density > 360 && density <= 480) {
            builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                    600,
                    800));
        }
        if (density > 480) {
            builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                    700,
                    900));
        }

        builder.show();
    }

    static class ViewHolder {
        public TextView productIDtag;
        public TextView qtytag;
        public TextView skutag;
        public TextView Productypetag;
        public TextView RegularPricetag;
        public TextView ProductNametag;
        public TextView statustag;
        public TextView product_id;
        public TextView Productype;
        public TextView product_name;
        public TextView sku;
        public TextView RegularPrice;
        public TextView qty;
        public TextView Status;
        public TextView delete;
        public TextView edit;
        public TextView picurl;
        public TextView MultiVendor_website_text;
        public ImageView prductimage;
        public CheckBox cb_free_shipping;
        public boolean isChecked = false;

        public boolean getisChecked() {
            return isChecked;
        }

        public void setisChecked(boolean isChecked) {
            this.isChecked = isChecked;
        }


    }

}