package magentoegypt.locafy.addons.faq.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import magentoegypt.locafy.R;

public class OutOfStockViewHolder extends RecyclerView.ViewHolder {
    public TextView quantity, price, type, name, product,tag;
    public ImageView btnEdit;

    public OutOfStockViewHolder(@NonNull View itemView) {
        super(itemView);
        quantity = itemView.findViewById(R.id.quantity);
        tag = itemView.findViewById(R.id.tec);
        price = itemView.findViewById(R.id.price);
        type = itemView.findViewById(R.id.type);
        name = itemView.findViewById(R.id.name);
        product = itemView.findViewById(R.id.product);
        btnEdit = itemView.findViewById(R.id.btnEdit);

    }
}
