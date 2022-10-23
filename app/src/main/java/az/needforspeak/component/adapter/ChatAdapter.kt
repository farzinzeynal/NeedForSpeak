package az.needforspeak.component.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import az.needforspeak.R
import az.needforspeak.model.local.ChatModel
import az.needforspeak.model.local.UserModel
import coil.load
import com.google.android.material.imageview.ShapeableImageView


class ChatAdapter(context: Context, onClick: (chat: ChatModel) -> Unit) :
    RecyclerView.Adapter<ChatAdapter.ViewHolder>(), Filterable {
    val context = context
    val onClick = onClick
    private val dataSet: MutableList<ChatModel> = mutableListOf()
    private var dataSetFilter: MutableList<ChatModel> = mutableListOf()
    fun addData(data: MutableList<ChatModel>) {
        dataSet.clear()
        dataSetFilter.clear()
        dataSet.addAll(data)
        dataSetFilter.addAll(data)
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val photo_profile: ShapeableImageView
        val plate_number: TextView
        val message: TextView
        init {
            // Define click listener for the ViewHolder's View.
            photo_profile = view.findViewById(R.id.photo_profile)
            plate_number = view.findViewById(R.id.plate_number)
            message = view.findViewById(R.id.message)
        }

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.chat_list, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.photo_profile.load(dataSetFilter[position].photo)
        viewHolder.plate_number.text = dataSetFilter[position].plateNumber
        viewHolder.message.text = dataSetFilter[position].message
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
                    val filteredList = ArrayList<ChatModel>()
                    dataSet
                        .filter {
                            (it.plateNumber?.contains(constraint!!,ignoreCase = true)) or
                                    ((it.name + " " + it.surname).contains(constraint!!, ignoreCase = true))
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
                    results.values as ArrayList<ChatModel>
                notifyDataSetChanged()
            }
        }
    }
}