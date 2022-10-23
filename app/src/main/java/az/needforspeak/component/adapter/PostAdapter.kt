package az.needforspeak.component.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import az.needforspeak.R
import az.needforspeak.model.local.ChatModel
import az.needforspeak.model.local.PostModel
import az.needforspeak.utils.dp
import coil.load
import com.google.android.material.imageview.ShapeableImageView

class PostAdapter(context: Context, onClick: (chat: PostModel) -> Unit) :
RecyclerView.Adapter<PostAdapter.ViewHolder>(), Filterable {
    val context = context
    private val dataSet: MutableList<PostModel> = mutableListOf()
    private var dataSetFilter: MutableList<PostModel> = mutableListOf()
    fun addData(data: MutableList<PostModel>) {
        dataSet.clear()
        dataSetFilter.clear()
        dataSet.addAll(data)
        dataSetFilter.addAll(data)
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val photo: ShapeableImageView
        val title: TextView
        val date: TextView
        val description: TextView
        val saveLayout: LinearLayout
        val saveIcon: ImageView
        val commentsText: TextView
        val commentsLayout: LinearLayout
        init {
            // Define click listener for the ViewHolder's View.
            photo = view.findViewById(R.id.photo)
            title = view.findViewById(R.id.title)
            date = view.findViewById(R.id.date)
            description = view.findViewById(R.id.description)
            saveLayout = view.findViewById(R.id.saveLayout)
            saveIcon = view.findViewById(R.id.saveIcon)
            commentsText = view.findViewById(R.id.commentsText)
            commentsLayout = view.findViewById(R.id.commentsLayout)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): PostAdapter.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.post_layout, viewGroup, false)

        return PostAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: PostAdapter.ViewHolder, position: Int) {
        viewHolder.photo.load(dataSetFilter[position].image)
        viewHolder.photo.layoutParams.height = ((context.resources.displayMetrics.widthPixels - (20).dp)*0.65).toInt()
        viewHolder.title.text = dataSetFilter[position].title
        viewHolder.description.text = dataSetFilter[position].description
        viewHolder.date.text = dataSetFilter[position].date
        if(dataSetFilter[position].isSaved) {
            viewHolder.saveIcon.setImageResource(R.drawable.ic_star_active)
        }else {
            viewHolder.saveIcon.setImageResource(R.drawable.ic_star)
        }
        dataSetFilter[position].comments?.let {
            viewHolder.commentsText.text = "${it.size} comments"
        }

        viewHolder.commentsText.text
        viewHolder.saveLayout.setOnClickListener {
            if(dataSetFilter[position].isSaved) {

                viewHolder.saveIcon.setImageResource(R.drawable.ic_star)
            }else {
                viewHolder.saveIcon.setImageResource(R.drawable.ic_star_active)
            }
            dataSetFilter[position].isSaved = !dataSetFilter[position].isSaved
        }
        viewHolder.itemView.setOnClickListener {

        }
    }

    override fun getItemCount() = dataSetFilter.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                if (charString.isEmpty()) dataSetFilter = dataSet else {
                    val filteredList = ArrayList<PostModel>()
                    dataSet
                        .filter {
                            it.title?.contains(constraint!!,ignoreCase = true) == true
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
                    results.values as ArrayList<PostModel>
                notifyDataSetChanged()
            }
        }
    }
}