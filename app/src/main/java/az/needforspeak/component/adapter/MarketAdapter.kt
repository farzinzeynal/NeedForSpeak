package az.needforspeak.component.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import az.needforspeak.R
import az.needforspeak.model.local.MarketModel
import az.needforspeak.model.local.UserModel
import coil.load
import com.google.android.material.imageview.ShapeableImageView

class MarketAdapter(context: Context, onClick: (MarketModel) -> Unit) :
    RecyclerView.Adapter<MarketAdapter.ViewHolder>(), Filterable {
    val context = context
    val onClick = onClick
    private val dataSet: MutableList<MarketModel> = mutableListOf()
    private var dataSetFilter: MutableList<MarketModel> = mutableListOf()
    fun addData(data: MutableList<MarketModel>) {
        dataSet.clear()
        dataSetFilter.clear()
        dataSet.addAll(data)
        dataSetFilter.addAll(data)
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image_market: ImageView
        val title_market: TextView
        init {
            // Define click listener for the ViewHolder's View.
            image_market = view.findViewById(R.id.image_market)
            title_market = view.findViewById(R.id.title_market)
        }

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.market_list, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        if(dataSetFilter[position].image is Int) {
            viewHolder.image_market.setImageResource(dataSetFilter[position].image as Int)
        }else if(dataSetFilter[position].image is String) {
            viewHolder.image_market.load(dataSetFilter[position].image)
        }
        viewHolder.title_market.text = dataSetFilter[position].title
        viewHolder.itemView.setOnClickListener {
            onClick(dataSetFilter[position])
        }

    }

    override fun getItemCount() = dataSetFilter.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                if (charString.isEmpty()) dataSetFilter = dataSet else {
                    val filteredList = ArrayList<MarketModel>()
                    dataSet
                        .filter {
                            (it.title?.contains(constraint!!,ignoreCase = true))
                        }
                        .forEach { filteredList.add(it) }
                    dataSetFilter = filteredList

                }
                return FilterResults().apply { values = dataSetFilter }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                dataSetFilter = if (results?.values == null)
                    ArrayList()
                else
                    results.values as ArrayList<MarketModel>
                notifyDataSetChanged()
            }
        }
    }
}