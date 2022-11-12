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
import az.needforspeak.model.local.SettingsModel
import az.needforspeak.model.local.UserModel
import coil.load
import com.google.android.material.imageview.ShapeableImageView

class SettingsListAdapter(context: Context, onClick: (SettingsModel) -> Unit) :
    RecyclerView.Adapter<SettingsListAdapter.ViewHolder>() {
    val context = context
    val onClick = onClick
    private val dataSet: MutableList<SettingsModel> = mutableListOf()
    fun addData(data: MutableList<SettingsModel>) {
        dataSet.clear()
        dataSet.addAll(data)
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val setting_item_icon: ImageView
        val setting_item_title: TextView
        init {
            // Define click listener for the ViewHolder's View.
            setting_item_icon = view.findViewById(R.id.settings_item_icon)
            setting_item_title = view.findViewById(R.id.settings_item_title)
        }

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.settings_list, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.setting_item_icon.setImageResource(dataSet[position].icon)
        viewHolder.setting_item_title.text = dataSet[position].title

        viewHolder.itemView.setOnClickListener {
            onClick(dataSet[position])
        }

    }

    override fun getItemCount() = dataSet.size

}