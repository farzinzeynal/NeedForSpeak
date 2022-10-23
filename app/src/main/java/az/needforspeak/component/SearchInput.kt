package az.needforspeak.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.widget.doOnTextChanged
import az.needforspeak.databinding.SearchInputBinding


class SearchInput(context: Context?, attrs: AttributeSet?) :
    LinearLayout(context, attrs) {
    lateinit var views: SearchInputBinding

    init {
        views = SearchInputBinding.inflate(LayoutInflater.from(context), this, true)
        views.searchEditText.doOnTextChanged { text, start, before, count ->
            text?.let {
                if(text.isNotEmpty()) {
                    views.closeImageView.visibility = View.VISIBLE
                }else {
                    views.closeImageView.visibility = View.GONE
                }
                getSearch(text.toString())
            }
        }
        views.closeImageView.setOnClickListener {
            views.searchEditText.setText("")
        }
    }

    lateinit var getSearch: (str: String) -> Unit
    fun setSearch(search: (str: String) -> Unit) {
        getSearch = search
    }
}
