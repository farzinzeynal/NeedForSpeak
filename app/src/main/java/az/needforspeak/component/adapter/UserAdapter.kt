package az.needforspeak.component.adapter

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import az.needforspeak.R
import az.needforspeak.model.local.*
import az.needforspeak.utils.getDpToPixel
import az.needforspeak.utils.getNavOptions
import coil.load
import coil.size.Scale
import com.google.android.material.imageview.ShapeableImageView
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


class UserAdapter(context: Context, onClick: (user: UserModel) -> Unit) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>(), Filterable {
    val context = context
    val onClick = onClick
    private val dataSet: MutableList<UserModel> = mutableListOf()
    private var dataSetFilter: MutableList<UserModel> = mutableListOf()
    fun addData(data: MutableList<UserModel>) {
        dataSet.clear()
        dataSetFilter.clear()
        dataSet.addAll(data)
        dataSetFilter.addAll(data)
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val photo_profile: ShapeableImageView
        val plate_number: TextView
        val fullname: TextView
        init {
            // Define click listener for the ViewHolder's View.
            photo_profile = view.findViewById(R.id.photo_profile)
            plate_number = view.findViewById(R.id.plate_number)
            fullname = view.findViewById(R.id.fullname)
        }

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.user_list, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.photo_profile.load(dataSetFilter[position].photo)
        viewHolder.plate_number.text = dataSetFilter[position].plateNumber
        viewHolder.fullname.text = dataSetFilter[position].name + " " + dataSetFilter[position].surname

        viewHolder.itemView.setOnClickListener {
            onClick.invoke(dataSetFilter[position])
        }
    }

    override fun getItemCount() = dataSetFilter.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                if (charString.isEmpty()) dataSetFilter = dataSet else {
                    val filteredList = ArrayList<UserModel>()
                    dataSet
                        .filter {
                            (it.plateNumber?.contains(constraint!!,ignoreCase = true))
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
                    results.values as ArrayList<UserModel>
                notifyDataSetChanged()
            }
        }
    }
}