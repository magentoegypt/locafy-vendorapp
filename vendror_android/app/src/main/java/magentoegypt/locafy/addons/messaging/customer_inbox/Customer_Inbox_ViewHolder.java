package magentoegypt.locafy.addons.messaging.customer_inbox;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import magentoegypt.locafy.R;
import magentoegypt.locafy.addons.messaging.admin_inbox.Admin_Inbox_Model;

/**
 * Created by cedcoss on 9/1/18.
 */

public class Customer_Inbox_ViewHolder extends RecyclerView.ViewHolder {
    private final AppCompatTextView sender, reciever, created_at, updated_at, subject, newmessage;
    private final AppCompatButton action_view;
    private final Context context;

    public Customer_Inbox_ViewHolder(View vi, final Context context) {
        super(vi);
        this.sender = vi.findViewById(R.id.sender);
        this.reciever = vi.findViewById(R.id.reciever);
        this.created_at = vi.findViewById(R.id.created_at);
        this.updated_at = vi.findViewById(R.id.updated_at);
        this.subject = vi.findViewById(R.id.subject);
        this.newmessage = vi.findViewById(R.id.newmessage);
        this.action_view = vi.findViewById(R.id.action_view);
        this.context = context;
    }

    public void createView(final Admin_Inbox_Model model) {

        sender.setText(model.getSender_name() != null ? model.getSender_name() : "");
        reciever.setText(model.getReceiver_name());
        created_at.setText(model.getCreated_at());
        updated_at.setText(model.getUpdated_at());
        subject.setText(model.getSubject());
        newmessage.setText(model.getMessage_status());

        action_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Customer_Inbox_ChatView.class);
                intent.putExtra("view_id", String.valueOf(model.getId()));
                intent.putExtra("subject", model.getSubject());
                context.startActivity(intent);
            }
        });
    }
}