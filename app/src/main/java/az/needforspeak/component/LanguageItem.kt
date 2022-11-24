package az.needforspeak.component

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import az.needforspeak.R

class LanguageItem(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    private var layout: View = inflate(context, R.layout.language_item, this)
    var title: TextView = findViewById(R.id.title)
    var icon: ImageView = findViewById(R.id.icon)
    var select: ImageView = findViewById(R.id.select)

    init {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.LanguagesView)
        icon.setImageDrawable(attributes.getDrawable(R.styleable.LanguagesView_languagesImage))
        select.setImageDrawable(attributes.getDrawable(R.styleable.LanguagesView_languagesRightIcon))
        title.text = attributes.getString(R.styleable.LanguagesView_languagesTitle)
        attributes.recycle()
        select.setImageResource(R.drawable.ic_select)
    }

    fun setOnClick(click: OnClickListener) {
        layout.setOnClickListener(click)
    }

    fun setIconVisibility(visibility: Int) {
        select.visibility = visibility
    }

}