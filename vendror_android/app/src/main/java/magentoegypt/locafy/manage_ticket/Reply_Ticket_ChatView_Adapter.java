package magentoegypt.locafy.manage_ticket;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import magentoegypt.locafy.R;
import java.util.List;

class Reply_Ticket_ChatView_Adapter extends RecyclerView.Adapter<Reply_Ticket_Holder> {
    List<Ticket_ChatModel> ticketChatList;
    Context context;

    public Reply_Ticket_ChatView_Adapter(Context context, List<Ticket_ChatModel> ticketChatList) {
        this.context=context;
        this.ticketChatList=ticketChatList;
    }

    @NonNull
    @Override
    public Reply_Ticket_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater= LayoutInflater.from(parent.getContext());
        ViewGroup mainGroup = (ViewGroup) layoutInflater.inflate(R.layout.reply_tkt_chat_item, parent, false);
        Reply_Ticket_Holder listHolder = new Reply_Ticket_Holder(mainGroup, context);
        return listHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Reply_Ticket_Holder holder, int position) {
        try {
//            Log.i("REpo", "33_onBindViewHolder: "+ticketChatList.size());
            holder.createView(ticketChatList.get(position));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return ticketChatList.size();
    }
}
