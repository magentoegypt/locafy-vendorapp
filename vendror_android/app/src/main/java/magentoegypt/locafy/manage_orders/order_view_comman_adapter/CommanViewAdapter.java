package magentoegypt.locafy.manage_orders.order_view_comman_adapter;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import magentoegypt.locafy.manage_orders.Ced_MultiVendor_CreditMemoView;
import magentoegypt.locafy.manage_orders.Ced_MultiVendor_Information_View;
import magentoegypt.locafy.manage_orders.Ced_MultiVendor_InvoiceView;
import magentoegypt.locafy.manage_orders.Ced_MultiVendor_ShipmentView;
import magentoegypt.locafy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class CommanViewAdapter extends RecyclerView.Adapter<CommanViewAdapter.ViewHolder> {

    JSONArray items;
    Activity activity;

    public CommanViewAdapter(JSONArray items, Activity activity) {
        this.items = items;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.commanview_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {

            JSONObject item_list = items.getJSONObject(position);
            if (activity instanceof Ced_MultiVendor_Information_View) {
                holder.txt_product.setText(item_list.getString("name"));
                holder.txt_product_sku.setText(item_list.getString("sku"));
                holder.txt_item_status.setText(item_list.getString("item_status"));
                holder.txt_original_price.setText(item_list.getString("original_price"));
                holder.txt_price.setText(item_list.getString("vendor_price"));
                holder.txt_qty.setText(item_list.getString("qty_ordered"));
                holder.txt_subtotal.setText(item_list.getString("vendor_row_total"));
                holder.tax_amount.setText(item_list.getString("tax_amount"));
                holder.tax_percent.setText(item_list.getString("tax_percent"));
                holder.txt_discount_txt.setText(item_list.getString("discount_amount"));
                holder.txt_row_total.setText(item_list.getString("row_total"));

                if (item_list.has("cgstAmt"))
                    holder.txt_cgst.setText(item_list.getString("cgstAmt"));
                else
                    holder.cgst_lyt.setVisibility(View.GONE);

                if (item_list.has("sgstAmt"))
                    holder.txt_sgst.setText(item_list.getString("sgstAmt"));
                else
                    holder.sgst_lyt.setVisibility(View.GONE);

                if (item_list.has("igstAmt"))
                    holder.txt_igst.setText(item_list.getString("igstAmt"));
                else
                    holder.igst_lyt.setVisibility(View.GONE);

                if (item_list.has("igstRate"))
                    holder.txt_igst_rate.setText(item_list.getString("igstRate"));
                else
                    holder.igst_rate_lyt.setVisibility(View.GONE);
            }
            if (activity instanceof Ced_MultiVendor_InvoiceView) {
                // single invoice view
                holder.txt_product.setText(item_list.getString("name"));
                holder.txt_product_sku.setText(item_list.getString("sku"));
                holder.txt_price.setText(item_list.getString("price"));
                holder.txt_qty.setText(item_list.getString("qty_ordered"));
                holder.txt_subtotal.setText(item_list.getString("subtotal"));
                holder.tax_amount.setText(item_list.getString("tax_amount"));
                holder.txt_discount_txt.setText(item_list.getString("discount_amount"));
                holder.txt_row_total.setText(item_list.getString("row_total"));


                if (item_list.has("cgstAmt"))
                    holder.txt_cgst.setText(item_list.getString("cgstAmt"));
                else
                    holder.cgst_lyt.setVisibility(View.GONE);

                if (item_list.has("sgstAmt"))
                    holder.txt_sgst.setText(item_list.getString("sgstAmt"));
                else
                    holder.sgst_lyt.setVisibility(View.GONE);

                if (item_list.has("igstAmt"))
                    holder.txt_igst.setText(item_list.getString("igstAmt"));
                else
                    holder.igst_lyt.setVisibility(View.GONE);

                /*Hide Views*/
                holder.item_status_lyt.setVisibility(View.GONE);
                holder.original_price_lyt.setVisibility(View.GONE);
                holder.tax_percent_lyt.setVisibility(View.GONE);
                holder.igst_rate_lyt.setVisibility(View.GONE);

            }

            if (activity instanceof Ced_MultiVendor_ShipmentView) {
                holder.txt_product.setText(item_list.getString("name"));
                holder.txt_product_sku.setText(item_list.getString("sku"));
                holder.txt_qty.setText(item_list.getString("qty_shipped"));

                /*Hide Views*/
                holder.item_status_lyt.setVisibility(View.GONE);
                holder.original_price_lyt.setVisibility(View.GONE);
                holder.price_lyt.setVisibility(View.GONE);
                holder.subtotal_lyt.setVisibility(View.GONE);
                holder.tax_amt_lyt.setVisibility(View.GONE);
                holder.tax_percent_lyt.setVisibility(View.GONE);
                holder.discount_lyt.setVisibility(View.GONE);
                holder.row_total_lyt.setVisibility(View.GONE);
                holder.igst_rate_lyt.setVisibility(View.GONE);
                holder.sgst_lyt.setVisibility(View.GONE);
                holder.cgst_lyt.setVisibility(View.GONE);
                holder.igst_lyt.setVisibility(View.GONE);
            }

            if (activity instanceof Ced_MultiVendor_CreditMemoView) {
                holder.txt_product.setText(item_list.getString("name"));
                holder.txt_product_sku.setText(item_list.getString("sku"));
                holder.txt_price.setText(item_list.getString("price"));
                holder.txt_qty.setText(item_list.getString("qty_ordered"));
                holder.txt_subtotal.setText(item_list.getString("subtotal"));
                holder.tax_amount.setText(item_list.getString("tax_amount"));
                holder.txt_discount_txt.setText(item_list.getString("discount_amount"));
                holder.txt_row_total.setText(item_list.getString("row_total"));

                if (item_list.has("cgstAmt"))
                    holder.txt_cgst.setText(item_list.getString("cgstAmt"));
                else
                    holder.cgst_lyt.setVisibility(View.GONE);

                if (item_list.has("sgstAmt"))
                    holder.txt_sgst.setText(item_list.getString("sgstAmt"));
                else
                    holder.sgst_lyt.setVisibility(View.GONE);

                if (item_list.has("igstAmt"))
                    holder.txt_igst.setText(item_list.getString("igstAmt"));
                else
                    holder.igst_lyt.setVisibility(View.GONE);

                /*Hide Views*/
                holder.item_status_lyt.setVisibility(View.GONE);
                holder.original_price_lyt.setVisibility(View.GONE);
                holder.tax_percent_lyt.setVisibility(View.GONE);
                holder.igst_rate_lyt.setVisibility(View.GONE);
            }

//            if (item_list.has("additonal_options")) {
//                View view;
//                TextView txt_label, txt_label_value;
//                JSONArray additonal_options = item_list.getJSONArray("additonal_options");
//                for (int i = 0; i < additonal_options.length(); i++) {
//                    JSONObject jsonObject = additonal_options.getJSONObject(i);
//                    view = View.inflate(activity, R.layout.additional_information_layout, null);
//                    txt_label = view.findViewById(R.id.txt_label);
//                    txt_label_value = view.findViewById(R.id.txt_label_value);
//                    txt_label.setText(jsonObject.getString("label"));
//                    txt_label_value.setText(jsonObject.getString("value"));
//                    holder.additional_parent.addView(view);
//                }
//
//            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return items.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_product, txt_item_status, txt_original_price, txt_price, txt_qty, txt_subtotal, tax_amount,
                tax_percent, txt_discount_txt, txt_row_total, txt_igst, txt_cgst, txt_sgst, txt_product_sku, txt_igst_rate;
        LinearLayout additional_parent, product_lyt, sku_lyt, item_status_lyt, original_price_lyt, price_lyt,
                qty_lyt, subtotal_lyt, tax_amt_lyt, tax_percent_lyt, discount_lyt, row_total_lyt, igst_lyt, igst_rate_lyt, cgst_lyt, sgst_lyt;

        public ViewHolder(View itemView) {
            super(itemView);

            txt_product = itemView.findViewById(R.id.txt_product);
            txt_item_status = itemView.findViewById(R.id.txt_item_status);
            txt_original_price = itemView.findViewById(R.id.txt_original_price);
            txt_price = itemView.findViewById(R.id.txt_price);
            txt_qty = itemView.findViewById(R.id.txt_qty);
            txt_subtotal = itemView.findViewById(R.id.txt_subtotal);
            tax_amount = itemView.findViewById(R.id.tax_amount);
            tax_percent = itemView.findViewById(R.id.tax_percent);
            txt_discount_txt = itemView.findViewById(R.id.txt_discount_txt);
            txt_row_total = itemView.findViewById(R.id.txt_row_total);
            txt_igst = itemView.findViewById(R.id.txt_igst);
            txt_cgst = itemView.findViewById(R.id.txt_cgst);
            txt_sgst = itemView.findViewById(R.id.txt_sgst);
            txt_product_sku = itemView.findViewById(R.id.txt_product_sku);
            txt_igst_rate = itemView.findViewById(R.id.txt_igst_rate);
            additional_parent = itemView.findViewById(R.id.additional_parent);

            /*Linear Layout*/
            product_lyt = itemView.findViewById(R.id.product_lyt);
            sku_lyt = itemView.findViewById(R.id.sku_lyt);
            item_status_lyt = itemView.findViewById(R.id.item_status_lyt);
            original_price_lyt = itemView.findViewById(R.id.original_price_lyt);
            price_lyt = itemView.findViewById(R.id.price_lyt);
            qty_lyt = itemView.findViewById(R.id.qty_lyt);
            subtotal_lyt = itemView.findViewById(R.id.subtotal_lyt);
            tax_amt_lyt = itemView.findViewById(R.id.tax_amt_lyt);
            tax_percent_lyt = itemView.findViewById(R.id.tax_percent_lyt);
            discount_lyt = itemView.findViewById(R.id.discount_lyt);
            row_total_lyt = itemView.findViewById(R.id.row_total_lyt);
            igst_lyt = itemView.findViewById(R.id.igst_lyt);
            igst_rate_lyt = itemView.findViewById(R.id.igst_rate_lyt);
            cgst_lyt = itemView.findViewById(R.id.cgst_lyt);
            sgst_lyt = itemView.findViewById(R.id.sgst_lyt);
        }
    }
}