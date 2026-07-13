package magentoegypt.locafy.manage_ticket;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import magentoegypt.locafy.R;

class Ticket_Item_Holder extends RecyclerView.ViewHolder {
    Context context;
    AppCompatTextView vendor_id,tkt_id,create_time,customer_name,customer_email,department,subject,num_of_message,order,priority,status;
    AppCompatButton reply;

    public Ticket_Item_Holder(View itemView, Context context) {
        super(itemView);
        this.context=context;

        vendor_id=itemView.findViewById(R.id.vendor_id);
        tkt_id=itemView.findViewById(R.id.tkt_id);
        create_time=itemView.findViewById(R.id.create_time);
        customer_name=itemView.findViewById(R.id.customer_name);
        customer_email=itemView.findViewById(R.id.customer_email);
        department=itemView.findViewById(R.id.department);
        subject=itemView.findViewById(R.id.subject);
        num_of_message=itemView.findViewById(R.id.num_of_message);
        order=itemView.findViewById(R.id.order);
        priority=itemView.findViewById(R.id.priority);
        status=itemView.findViewById(R.id.status);

        reply=itemView.findViewById(R.id.reply);

    }

    public void createView(final Ticket_Data_Model ticket_data_model) {
        vendor_id.setText(ticket_data_model.getEntity_id());
        tkt_id.setText(ticket_data_model.getTicket_id());
        create_time.setText(ticket_data_model.getCreated_time());
        customer_name.setText(ticket_data_model.getCustomer_name());
        customer_email.setText(ticket_data_model.getCustomer_email());
        department.setText(ticket_data_model.getDepartment());
        subject.setText(ticket_data_model.getSubject());
        num_of_message.setText(ticket_data_model.getNum_msg());
        order.setText(ticket_data_model.getOrder_id());
        priority.setText(ticket_data_model.getPriority());
        status.setText(ticket_data_model.getStatus());

        reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,Reply_Ticket_ChatView.class);

                intent.putExtra("tkt_id",String.valueOf(ticket_data_model.getTicket_id()));
                intent.putExtra("subject",ticket_data_model.getSubject());
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                context.startActivity(intent);
            }
        });

    }
}
