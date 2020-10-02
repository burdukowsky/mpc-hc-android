package tk.burdukowsky.mpc_hc_android

import android.content.Context
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceViewHolder

class EditTextWithButtonPreference(context: Context?) :
    EditTextPreference(context) {

    private var onButtonClickListener: Runnable = Runnable { }

    init {
        widgetLayoutResource = R.layout.edit_text_with_button_preference_widget_layout
    }

    fun setOnButtonClickListener(onButtonClickListener: Runnable) {
        this.onButtonClickListener = onButtonClickListener
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder?) {
        super.onBindViewHolder(holder)

        holder?.let {
            it.findViewById(R.id.button).setOnClickListener { onButtonClickListener.run() }
        }
    }

}
