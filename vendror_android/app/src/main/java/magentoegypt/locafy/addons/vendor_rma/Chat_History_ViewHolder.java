package magentoegypt.locafy.addons.vendor_rma;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import magentoegypt.locafy.R;

/**
 * Created by cedcoss on 10/1/18.
 */

class Chat_History_ViewHolder extends RecyclerView.ViewHolder{

    LinearLayout itemLayout;
    TextView message ,sender ,message_date;

    Context context;

    public Chat_History_ViewHolder(View itemView,final Context context) {
        super(itemView);

        this.context=context;
        itemLayout=(LinearLayout)itemView.findViewById(R.id.item_layout);
        message = (TextView) itemView.findViewById(R.id.message);
        sender = (TextView) itemView.findViewById(R.id.sender);
        message_date = (TextView) itemView.findViewById(R.id.message_date);
    }


}
