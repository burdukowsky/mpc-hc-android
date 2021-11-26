package tk.burdukowsky.mpc_hc_android

import android.content.Context
import android.util.AttributeSet
import android.widget.Toast
import com.google.android.material.button.MaterialButton

class CommandButton(context: Context, attrs: AttributeSet?) : MaterialButton(context, attrs) {

    var command: Int = 0

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CommandButton,
            0,
            R.style.Widget_App_Button_SimpleLayout_IconOnly
        ).apply {
            try {
                command = getInteger(R.styleable.CommandButton_command, 0)
                setOnClickListener {
                    DoAsync {
                        try {
                            CommandService.send(command)
                        } catch (t: Throwable) {
                            (context as MainActivity).runOnUiThread {
                                Toast.makeText(
                                    context,
                                    R.string.send_command_error,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            } finally {
                recycle()
            }
        }
    }

}
