package magentoegypt.locafy.manage_products_section;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import magentoegypt.locafy.manage_products_section.model.AttributeModelConfig;
import com.bumptech.glide.Glide;
import magentoegypt.locafy.base_app.Ced_MultiVendor_FontSetting;
import magentoegypt.locafy_constant.Ced_MultiVendor_GlobalVariables;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.api_request_response_section.network_error_handling.Ced_MultiVendor_UnAuthourizedRequestError;
import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;
import magentoegypt.locafy.R;
import magentoegypt.locafy_constant.AppConstant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Ced_MultiVendor_ProductListAdapter_new extends RecyclerView.Adapter<Ced_MultiVendor_ProductListAdapter_new.ViewHolder> {
    public static float density;
    private static LayoutInflater inflater = null;
    private final Activity activity;
    public final ArrayList<HashMap<String, String>> productData;
    String Jstring;
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    Ced_MultiVendor_VendorSessionManagement vendorSessionManagement;
    HashMap<String, String> dataforproducts;
    String Currenturl;
    String editCurrenturl;
    Ced_MultiVendor_FontSetting fontSetting;
    String selected_websitetopost;
    int id = Resources.getSystem().getIdentifier("btn_check_holo_light", "drawable", "android");
    int totalProductApiCount = 0;
    public Ced_MultiVendor_ProductListAdapter_new(Activity a, ArrayList<HashMap<String, String>> d, String selected_websitetopost) {
        fontSetting = new Ced_MultiVendor_FontSetting();
        activity = a;
        productData = d;
        this.selected_websitetopost = selected_websitetopost;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(activity);
        vendorSessionManagement = new Ced_MultiVendor_VendorSessionManagement(activity);
        dataforproducts = new HashMap<String, String>();
        Currenturl = vendorSessionManagement.getBase_Url() + "vendorapi/vproducts/delete";
        editCurrenturl = vendorSessionManagement.getBase_Url() + "vendorapi/vproducts/info";
        density = activity.getResources().getDisplayMetrics().densityDpi;

    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ced_multivendor_productlist, parent, false);
        return new Ced_MultiVendor_ProductListAdapter_new.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {


        HashMap<String, String> song = new HashMap<String, String>();
        song = productData.get(position);
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
        holder.cb_free_shipping.setButtonDrawable(id);
        Glide.with(activity)
                .load(song.get("product_image"))
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(holder.prductimage);
        holder.prductimage.setOnClickListener(v -> showImage(holder.prductimage, holder.picurl.getText().toString()));
        holder.delete.setOnClickListener(v -> {
            final AlertDialog.Builder builder = new AlertDialog.Builder(
                    activity);
            builder.setTitle(R.string.alert_name);
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setMessage(R.string.confirm_first);
            builder.setNegativeButton(R.string.no,
                    (dialog, which) -> {

                    });
            builder.setPositiveButton(R.string.yes, (dialog, which) -> {

                        String[] product_id = holder.product_id.getText().toString().split("#");
                        dataforproducts.put("entity_id", product_id.length > 1 ? product_id[1] : product_id[0]);
                        dataforproducts.put("vendor_id", vendorSessionManagement.getVendorid());
                        dataforproducts.put("hashkey", vendorSessionManagement.getHahkey());
                        Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(output -> {
                            Jstring = output.toString();
                            if (functionalityList.getExtensionAddon()) {

                                JSONObject object = new JSONObject(Jstring);
                                String success = object.getString("success");
                                if (success.equals("true")) {
                                    Toast.makeText(activity, object.getString("message"), Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(activity, Ced_MultiVendor_ManageProducts.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.putExtra("selectedwebsite", selected_websitetopost);
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
                        }, activity, "POST", dataforproducts);
                        crr.execute(Currenturl);
                    }
            );
            builder.show();
        });
        holder.edit.setOnClickListener(v -> {
            AppConstant.lockButton(v);
            final String[] product_id = holder.product_id.getText().toString().split("#");

            dataforproducts.put("product_id", product_id.length>1?product_id[1]:product_id[0]);

           // if (vendorSessionManagement.getStoreId() != null)
              //  dataforproducts.put("store_id", vendorSessionManagement.getStoreId());

            dataforproducts.put("store_id", Ced_MultiVendor_ManageProducts.store_id);
            dataforproducts.put("vendor_id", vendorSessionManagement.getVendorid());
            dataforproducts.put("hashkey", vendorSessionManagement.getHahkey());
            Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(output -> {
                Jstring = output.toString();
                if (functionalityList.getExtensionAddon()) {
                    JSONObject object = new JSONObject(Jstring);
                    String success = object.getJSONObject("data").getString("success");
                    if (success.equals("true")) {
                        JSONObject data = object.getJSONObject("data");
                        JSONObject productdata = object.getJSONObject("data").getJSONObject("productdata");
                        String type = productdata.getString("type_id");
                        Intent update = new Intent(activity, EditProductDynamic.class);
                        update.putExtra("type", type);
                        update.putExtra("dataforproductcreation", Jstring);
                        update.putExtra("product_id",  product_id.length>1?product_id[1]:product_id[0]);
                        update.putExtra("selectedwebsite", selected_websitetopost);
                        if (functionalityList.getProductAddon()) {
                            List<String> config_selected_attr = new ArrayList<>();
                            String attribute_set = productdata.getString("attribute_set_id");
                            update.putExtra("attribute_set", attribute_set);
                            if (type.equals("configurable") && object.getJSONObject("data").get("config_selected_attr") instanceof JSONObject) {
                                Ced_MultiVendor_GlobalVariables.config_attribute_value = object.getJSONObject("data").getJSONObject("config_selected_attr");
                                JSONObject config_selected =data.getJSONObject("config_selected_attr");
                                Iterator<String> keys = config_selected.keys();
                                while (keys.hasNext()) {
                                    String key = keys.next();
                                    String value = config_selected.getString(key); // or use optString(key)
                                    config_selected_attr.add(value);
                                }
                            }
                            if(data.has("associated_product_ids")) {
                                JSONArray associated_product_ids = data.getJSONArray("associated_product_ids");
                                loadAttributData(type,attribute_set,associated_product_ids,config_selected_attr,update);
                            }else{
                                startActivty(update);
                            }
                        }
                    }
                } else {
                    Intent intent = new Intent(activity, Ced_MultiVendor_UnAuthourizedRequestError.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
                }

            }, activity, "POST", dataforproducts);
            crr.execute(editCurrenturl);
        });

        holder.cb_free_shipping.setChecked(Ced_MultiVendor_ManageProducts.ids.contains(song.get("product_id").replace("#", "")));

        final HashMap<String, String> finalSong = song;
        holder.cb_free_shipping.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (!Ced_MultiVendor_ManageProducts.ids.contains(finalSong.get("product_id").replace("#", ""))) {
                    Ced_MultiVendor_ManageProducts.ids.add(finalSong.get("product_id").replace("#", ""));
                }

            } else {
                Ced_MultiVendor_ManageProducts.ids.remove(finalSong.get("product_id").replace("#", ""));
            }

            Log.d("test_ids", "" + Ced_MultiVendor_ManageProducts.ids);
        });
        holder.setIsRecyclable(false);
    }

    void loadAttributData(String type,String set,JSONArray associated_product_ids,List<String> config_selected_attr,Intent update){
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("set", set);
        data.put("type", type);
        data.put("store_id", vendorSessionManagement.getStoreId());
        data.put("vendor_id", vendorSessionManagement.getVendorid());
        data.put("hashkey", vendorSessionManagement.getHahkey());
        Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(output -> {
            String attributeData = output.toString();
            if (functionalityList.getExtensionAddon()) {
                JSONObject jsonObject = new JSONObject(attributeData);
                String success = jsonObject.getJSONObject("data").getString("success");
                if (success.equals("true")) {
                    update.putExtra("attributeData", attributeData);
                    JSONArray attributes = jsonObject.getJSONObject("data").getJSONArray("attributes");
                    JSONArray config = jsonObject.getJSONObject("data").getJSONArray("config");
                    List<String> attribute_IdArray = new ArrayList<>();

                    for (int index = 0; index < config.length(); index++) {
                        JSONObject attributeArray = config.getJSONObject(index);
                        attribute_IdArray.add(attributeArray.getString("attribute_code"));
                    }

                    if(associated_product_ids.length() > 0){
                        totalProductApiCount = associated_product_ids.length();
                        ConfigAttributeSelectionActivity.attributeModelEditListFinal.clear();
                        for (int i = 0; i < associated_product_ids.length(); i++) {
                            loadSingleProduct(update,attribute_IdArray,config_selected_attr,attributes,associated_product_ids.getString(i));
                        }
                    }else{
                        startActivty(update);
                    }
                }
            } else {
                Intent intent = new Intent(activity, Ced_MultiVendor_UnAuthourizedRequestError.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
            }


        }, activity, "POST", data);
        crr.execute(vendorSessionManagement.getBase_Url() + "vproductapi/vproducts/allowedAttribute");
    }

    void loadSingleProduct(Intent update,List<String> keysName,List<String> config_selected_attr, JSONArray attributes,String product_id){
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("product_id", product_id);
      //  data.put("store_id", vendorSessionManagement.getStoreId());
        data.put("store_id", Ced_MultiVendor_ManageProducts.store_id);
        data.put("vendor_id", vendorSessionManagement.getVendorid());
        data.put("hashkey", vendorSessionManagement.getHahkey());
        Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(output -> {
            Jstring = output.toString();
            if (functionalityList.getExtensionAddon()) {
                JSONObject object = new JSONObject(Jstring);
                String success = object.getJSONObject("data").getString("success");
                if (success.equals("true")) {
                    JSONObject json = object.getJSONObject("data").getJSONObject("productdata");

                    AttributeModelConfig finalModel = new AttributeModelConfig();

                    finalModel.setAssicoiatProductId(json.optString("entity_id"));
                    finalModel.setAssicoiatProductSku(json.optString("sku"));
                    finalModel.setSkulabel(json.optString("sku"));
                    Object status = json.get("status");
                    if (status instanceof String) {
                        finalModel.setEnable((status.toString().equals("1")));
                    } else if (status instanceof Boolean) {
                        finalModel.setEnable(json.getBoolean("status"));
                    } else if (status instanceof Integer) {
                        finalModel.setEnable((status.toString().equals("1")));
                    }
                    finalModel.setPrice(json.optString("vendor_price"));
                    finalModel.setOldprice(json.optString("vendor_price"));
                    // Handle nested stock_item[0].qty
                    JSONArray stockItems = json.optJSONArray("stock_item");
                    if (stockItems != null && stockItems.length() > 0) {
                        finalModel.setQuantity(stockItems.optJSONObject(0).optString("qty"));
                    }
                    finalModel.setWeight(json.optString("weight"));
                    finalModel.setOldweight(json.optString("weight"));
                    finalModel.setNamelabel(json.optString("name"));
                    // Handle media_image[0].image_path
                    JSONArray mediaImages = json.optJSONArray("media_image");
                    if (mediaImages != null && mediaImages.length() > 0) {
                        finalModel.setImageUrl(mediaImages.optJSONObject(0).optString("image_path"));
                    }
                    for (int i = 0; i < mediaImages.length(); i++) {
                        JSONObject media = mediaImages.optJSONObject(i);
                        if(media.optString("default_image").equalsIgnoreCase("true"))
                        {
                            finalModel.setDefaultimage(media.optString("image_name"));
                        }
                    }
                    String title = "";
                    String attributeWithValue = "";
                    for (int i = 0; i < attributes.length(); i++) {
                        JSONObject attributeArray = attributes.optJSONObject(i);
                        String attribute_code = attributeArray.optString("name");
                        String attribute_id = attributeArray.optString("attribute_id");
                        if (!json.optString(attribute_code).isEmpty() && keysName.contains(attribute_code) && config_selected_attr.contains(attribute_id)) {
                            String label = attributeArray.optString("label");
                            if (title.isEmpty()) {
                                title = label;

                                attributeWithValue = label;
                            } else {
                                title += " " + label;
                                attributeWithValue += "::!" + label;
                            }
                            JSONArray values = attributeArray.optJSONArray("values");
                            for (int j = 0; j < values.length(); j++) {
                                JSONObject value = values.optJSONObject(j);
                                if (value != null && value.optString("value").equals(json.optString(attribute_code))) {
                                    title += ":" + value.optString("label");
                                    attributeWithValue += ":;" + value.optString("value");
                                }
                            }
                            finalModel.setAttributeId(attributeArray.optString("attribute_id"));
                            finalModel.getAttributeIdsArr().add(json.optString(attribute_code));
                        }
                    }
                    finalModel.setAttributeWithValue(attributeWithValue);
                    finalModel.setTitle(title);
                    ConfigAttributeSelectionActivity.attributeModelEditListFinal.add(finalModel);
                    totalProductApiCount -= 1;
                    if(totalProductApiCount <= 0){
                        startActivty(update);
                    }
                }
            } else {
                Intent intent = new Intent(activity, Ced_MultiVendor_UnAuthourizedRequestError.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
            }
        }, activity, "POST", data);
        crr.execute(editCurrenturl);
    }

    void startActivty(Intent update){
        activity.startActivity(update);
        activity.overridePendingTransition(R.anim.ced_multivendor_slide_in, R.anim.ced_multivendor_slide_out);
    }

    @Override
    public int getItemCount() {
        return productData.size();
    }

    public void showImage(ImageView image, String s) {
        Dialog builder = new Dialog(activity);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(dialogInterface -> {
            //nothing;
        });

        ImageView imageView = new ImageView(activity);
        Glide.with(activity)
                .load(s)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(imageView);
//        imageView.setScaleType(ImageView.ScaleType.CENTER);


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

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView productIDtag, qtytag, skutag, RegularPricetag, Productypetag, ProductNametag, statustag, product_id, Productype, product_name, sku, RegularPrice, qty, Status, delete, edit, picurl, MultiVendor_website_text;
        ImageView prductimage;
        CheckBox cb_free_shipping;

        public ViewHolder(View itemView) {
            super(itemView);

            MultiVendor_website_text = (TextView) itemView.findViewById(R.id.MultiVendor_website_text);
            productIDtag = (TextView) itemView.findViewById(R.id.MultiVendor_productIDtag);
            qtytag = (TextView) itemView.findViewById(R.id.MultiVendor_qtytag);
            skutag = (TextView) itemView.findViewById(R.id.MultiVendor_skutag);
            Productypetag = (TextView) itemView.findViewById(R.id.MultiVendor_Productypetag);
            RegularPricetag = (TextView) itemView.findViewById(R.id.MultiVendor_RegularPricetag);
            ProductNametag = (TextView) itemView.findViewById(R.id.MultiVendor_ProductNametag);
            statustag = (TextView) itemView.findViewById(R.id.MultiVendor_statustag);
            product_id = (TextView) itemView.findViewById(R.id.MultiVendor_product_id);
            delete = (TextView) itemView.findViewById(R.id.MultiVendor_delete);
            edit = (TextView) itemView.findViewById(R.id.MultiVendor_edit);
            Productype = (TextView) itemView.findViewById(R.id.MultiVendor_Productype);
            product_name = (TextView) itemView.findViewById(R.id.MultiVendor_product_name);
            RegularPrice = (TextView) itemView.findViewById(R.id.MultiVendor_RegularPrice);
            qty = (TextView) itemView.findViewById(R.id.MultiVendor_qty);
            sku = (TextView) itemView.findViewById(R.id.MultiVendor_sku);
            Status = (TextView) itemView.findViewById(R.id.MultiVendor_Status);
            picurl = (TextView) itemView.findViewById(R.id.MultiVendor_picurl);
            prductimage = (ImageView) itemView.findViewById(R.id.MultiVendor_prductimage);
            cb_free_shipping = (CheckBox) itemView.findViewById(R.id.cb_free_shipping);


            fontSetting.setFontforTextviews(productIDtag, "Roboto-Medium.ttf", activity);
            fontSetting.setFontforTextviews(MultiVendor_website_text, "Roboto-Medium.ttf", activity);
            fontSetting.setFontforTextviews(qtytag, "Roboto-Medium.ttf", activity);
            fontSetting.setFontforTextviews(skutag, "Roboto-Medium.ttf", activity);
            fontSetting.setFontforTextviews(Productypetag, "Roboto-Medium.ttf", activity);
            fontSetting.setFontforTextviews(RegularPricetag, "Roboto-Medium.ttf", activity);
            fontSetting.setFontforTextviews(ProductNametag, "Roboto-Medium.ttf", activity);
            fontSetting.setFontforTextviews(statustag, "Roboto-Medium.ttf", activity);
            fontSetting.setFontforTextviews(product_id, "Roboto-Medium.ttf", activity);
            fontSetting.setFontforTextviews(delete, "Roboto-Medium.ttf", activity);
            fontSetting.setFontforTextviews(edit, "Roboto-Medium.ttf", activity);
            fontSetting.setFontforTextviews(Productype, "Roboto-Medium.ttf", activity);
            fontSetting.setFontforTextviews(product_name, "Roboto-Medium.ttf", activity);
            fontSetting.setFontforTextviews(RegularPrice, "Roboto-Medium.ttf", activity);
            fontSetting.setFontforTextviews(qty, "Roboto-Medium.ttf", activity);
            fontSetting.setFontforTextviews(sku, "Roboto-Medium.ttf", activity);
            fontSetting.setFontforTextviews(Status, "Roboto-Medium.ttf", activity);
            fontSetting.setFontforTextviews(picurl, "Roboto-Medium.ttf", activity);
            fontSetting.setfontforCheckbox(cb_free_shipping, "Roboto-Medium.ttf", activity);


        }
    }
}
