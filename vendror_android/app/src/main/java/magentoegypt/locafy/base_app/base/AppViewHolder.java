package magentoegypt.locafy.base_app.base;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import magentoegypt.locafy.R;

public class AppViewHolder extends RecyclerView.ViewHolder {
    public TextView status, pId, title, customerEmail, createdAt, faqId;
    public ImageView btnEdit, btnDelete;

    public AppViewHolder(@NonNull View itemView) {
        super(itemView);
        status = itemView.findViewById(R.id.tvStatus);
        pId = itemView.findViewById(R.id.tvpId);
        title = itemView.findViewById(R.id.tvtitle);
        customerEmail = itemView.findViewById(R.id.tvCustomerName);
        createdAt = itemView.findViewById(R.id.tvCreatedAt);
        faqId = itemView.findViewById(R.id.tvFaqId);
        btnEdit = itemView.findViewById(R.id.ivEditBtn);
        btnDelete = itemView.findViewById(R.id.ivDeleteBtn);
    }
}
