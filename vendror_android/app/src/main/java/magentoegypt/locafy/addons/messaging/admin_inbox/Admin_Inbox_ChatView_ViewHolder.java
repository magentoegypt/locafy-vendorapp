package magentoegypt.locafy.addons.messaging.admin_inbox;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import magentoegypt.locafy.R;
import magentoegypt.locafy.addons.messaging.Chat_Image_Adapter;

import java.util.Objects;

/**
 * Created by cedcoss on 9/1/18.
 */

public class Admin_Inbox_ChatView_ViewHolder extends RecyclerView.ViewHolder {
    AppCompatTextView from, createdat, message;
    RecyclerView images_list;
    View view;
    Context context;

    public Admin_Inbox_ChatView_ViewHolder(View vi, final Context context) {
        super(vi);
        view = vi;
        this.from = vi.findViewById(R.id.from);
        this.createdat = vi.findViewById(R.id.createdat);
        this.message = vi.findViewById(R.id.message);
        this.images_list = vi.findViewById(R.id.images_list);
        this.context = context;
    }

    public void createView(final Admin_Inbox_Chatview_Model model) {
        createdat.setText(model.getCreated_at());
        message.setText(Html.fromHtml(model.getMessage()));
        String text = "From : " + model.getSender_name();
        from.setText(text);
        if (model.getSender() != null && model.getSender().equals("admin") || Objects.requireNonNull(model.getSender()).equals("customer")) {
            view.setBackgroundColor(context.getResources().getColor(R.color.tovendor));
        }
        else {
            view.setBackgroundColor(context.getResources().getColor(R.color.fromvendor));
        }
        if (model.getImage() != null) {
            images_list.setVisibility(View.VISIBLE);
            Chat_Image_Adapter chat_image_adapter = new Chat_Image_Adapter(context, model.getImage());
            images_list.setAdapter(chat_image_adapter);
        } else {
            images_list.setVisibility(View.GONE);
            Log.i("REpo", "43_createView: " + model.getImage());
        }
    }
}