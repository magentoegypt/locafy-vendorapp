package magentoegypt.locafy.manage_ticket;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import magentoegypt.locafy.R;

import java.util.List;

class Manage_Ticket_Adapter extends RecyclerView.Adapter<Ticket_Item_Holder> {

    List<Ticket_Data_Model> ticketDataList;
    Context context;

    public Manage_Ticket_Adapter( Context context, List<Ticket_Data_Model> ticketDataList)
    {
        this.context=context;
        this.ticketDataList=ticketDataList;
    }

    @NonNull
    @Override
    public Ticket_Item_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        ViewGroup mainGroup = (ViewGroup) layoutInflater.inflate(R.layout.ticket_adapter_item, parent, false);
        Ticket_Item_Holder listHolder = new Ticket_Item_Holder(mainGroup, context);
        return listHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Ticket_Item_Holder holder, int position) {
        try {
            holder.createView(ticketDataList.get(position));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return ticketDataList.size();
    }
}
