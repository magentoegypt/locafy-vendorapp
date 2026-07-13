package magentoegypt.locafy.addons.advance_Report.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import magentoegypt.locafy.addons.advance_Report.models.Product
import magentoegypt.locafy.addons.advance_Report.interfaces.AddOnScrollMoreListener
import magentoegypt.locafy.databinding.OutOfStockProductViewBinding

class OutOfStockAdapter(private val loadMore: AddOnScrollMoreListener) :

    RecyclerView.Adapter<AppViewHolder>() {
    private val dataList: MutableList<Product> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val inflater =
            LayoutInflater.from(parent.context)
        val binding: OutOfStockProductViewBinding = OutOfStockProductViewBinding.inflate(
            inflater, parent,
            false
        )
        return AppViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        try {
            val product: Product = dataList[position]
            holder.outOfStock.product = product
            Log.d("TAG", "onBindViewHolder: $product")
        }
        catch (e: Exception) {
            Log.d("TAG", "Weeor : ${e.localizedMessage}")
        }
        if (position == itemCount - 1 && itemCount >2) {
            loadMore.onLoadMore(position)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addData(list: List<Product>) {
        dataList.addAll(list)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addItem(item: Product) {
        dataList.add(item)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearList() {
        dataList.clear()
        notifyDataSetChanged()
    }
}