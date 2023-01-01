package az.needforspeak.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import az.needforspeak.R
import az.needforspeak.databinding.AccountInfoItemBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.account_info_item.view.*


class AccountInfoView (context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    private var layout: View = inflate(context, R.layout.account_info_item, this)

    private lateinit var bindind: AccountInfoItemBinding
    private var isHidden = false
    private var isEnable = true

    private var textInput: TextInputEditText = findViewById(R.id.inputEditText)
    private var textInputLayout: TextInputLayout = findViewById(R.id.inputEditLayout)
    private var toggleButton: ImageView = findViewById(R.id.toogleImage)


    init {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.AccountInfoView)
        textInputLayout.hint = attributes.getString(R.styleable.AccountInfoView_hint)
        toggleButton.setImageDrawable(attributes.getDrawable(R.styleable.AccountInfoView_toogleIcon))
        isHidden = attributes.getBoolean(R.styleable.AccountInfoView_isHidden,false)
        initToggleButton()
        initTextVisible()
    }

    private fun initTextVisible() {
        if (isHidden) hideText() else unHideText()
    }

    private fun initToggleButton() {
        toggleButton.setOnClickListener {
            if (isHidden){
                toggleButton.setImageResource(R.drawable.ic_eye_not)
                isHidden = false
                hideText()
            }
            else{
                toggleButton.setImageResource(R.drawable.ic_eye)
                isHidden = true
                unHideText()
            }
        }
    }

    fun hideText(){
        textInput.visibility = View.INVISIBLE
    }

    fun unHideText(){
        textInput.visibility = View.VISIBLE
    }


    fun setText(text: String) {
        textInputLayout.editText?.setText(text)
    }

    fun getText(): String{
        return textInputLayout.editText?.text.toString()
    }

    fun setInputEnabled(isEnable: Boolean){
        textInputLayout.isEnabled = isEnable
    }


}