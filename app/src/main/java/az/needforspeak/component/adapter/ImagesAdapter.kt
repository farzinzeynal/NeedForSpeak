package az.needforspeak.component.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import az.needforspeak.R
import az.needforspeak.component.listeners.OnItemClickListener
import az.needforspeak.model.local.Image
import az.needforspeak.utils.getIfExists
import coil.load
import com.bumptech.glide.Glide
import java.io.File
import java.util.*




class ImagesAdapter(
    private val imagesList: ArrayList<Image>,
    private val onItemClickListener: OnImageItemClick<Image>,
    private val context: Context
    ) : androidx.recyclerview.widget.RecyclerView.Adapter<ImagesAdapter.ViewHolder>() {
    private var currentRowIndex: Int = -1

    override fun getItemCount(): Int {
        return imagesList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_item, parent, false)
        return ViewHolder(view)
    }

    // load data in each row element
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") listPosition: Int) {
        try {
            holder.itemView.setOnClickListener {
                currentRowIndex = listPosition
                notifyDataSetChanged()
                imagesList.getIfExists(listPosition)?.let {
                    onItemClickListener.onItemClick(it)
                }
            }
            holder.deleteImage.setOnClickListener {
                onItemClickListener.onDeleteItemClick(listPosition)
            }
            if (currentRowIndex == listPosition) {
                holder.itemView.setBackgroundResource(R.drawable.image_roundy_green_border)
            } else {
                holder.itemView.setBackgroundResource(0)
            }
            val f = File(imagesList[listPosition].imageUrl)
            f.let {
                Glide.with(context).load(it).into(holder.image)
            }
        } catch (e: java.lang.Exception) {
            holder.image.setImageResource(R.drawable.ic_person_full)
        }
    }

    fun getSelectedItem(): Image? {
        return imagesList.getIfExists(currentRowIndex)
    }

    fun notifySelected(itemPos: Int) {
        currentRowIndex = itemPos
        notifyItemChanged(itemPos)
    }

    inner class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.image)
        var deleteImage: ImageView = itemView.findViewById(R.id.deleteImage)
    }

    interface OnImageItemClick<T> : OnItemClickListener<T> {
        fun onDeleteItemClick(position: Int)
    }
}