package az.needforspeak.component

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import az.needforspeak.R

class AccountInfoView (context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    private var layout: View = inflate(context, R.layout.account_info_item, this)


    init {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.AccountInfoView)
        //icon.setImageDrawable(attributes.getDrawable(R.styleable.LanguagesView_languagesImage))

    }

    fun setOnClick(click: OnClickListener) {
       // layout.setOnClickListener(click)
    }

    fun setIconVisibility(visibility: Int) {
      //  select.visibility = visibility
    }

}