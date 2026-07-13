package magentoegypt.locafy.addons.advance_Report.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import magentoegypt.locafy.addons.advance_Report.models.Product
import magentoegypt.locafy.addons.advance_Report.interfaces.AddOnScrollMoreListener
import magentoegypt.locafy.databinding.VendorSalesReportViewBinding
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone


class VendorSalesAdapter(private val loadMore: AddOnScrollMoreListener) :
    RecyclerView.Adapter<AppViewHolder>() {
    private val dataList: MutableList<Product> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val inflater =
            LayoutInflater.from(parent.context)
        val binding: VendorSalesReportViewBinding = VendorSalesReportViewBinding.inflate(
            inflater, parent,
            false
        )
        return AppViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {

        try {
            val product: Product = dataList[position]
            holder.vendorSales.product = product
            holder.vendorSales.transactionDate.text = convertToEgyptTimeZone(product.orderDate);
        } catch (e: Exception) {
            Log.d("TAG", "onBindViewHolder: ${e.localizedMessage}")
        }
        if (position == itemCount - 1 && itemCount > 2) {
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
        notifyDataSetChanged()
        dataList.clear()
    }
    private fun convertToEgyptTimeZone(dateString: String): String {
        return try {
            // Define the date format
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            // Set the original time zone (e.g., UTC or your device's local time zone)
            dateFormat.timeZone = TimeZone.getTimeZone("UTC")

            // Parse the input date string
            val date = dateFormat.parse(dateString)
            // Set the desired time zone (e.g., Africa/Cairo for Egypt)
             dateFormat.timeZone = TimeZone.getDefault()//getTimeZone("Africa/Cairo")
            // Format the date according to Egypt time zone
           // dateFormat.applyPattern("yyyy-MM-dd");
            dateFormat.format(date!!)
        }catch (e:Exception){
            dateString;
        }
    }
}

