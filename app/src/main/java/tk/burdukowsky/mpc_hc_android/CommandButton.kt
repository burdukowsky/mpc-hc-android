package tk.burdukowsky.mpc_hc_android

import android.content.Context
import android.util.AttributeSet
import android.widget.Toast
import com.google.android.material.button.MaterialButton

class CommandButton(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = com.google.android.material.R.attr.materialButtonStyle
) :
    MaterialButton(context, attrs, defStyleAttr) {

    private var commandValue: Int

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CommandButton,
            0, 0
        ).apply {
            try {
                commandValue = getInteger(R.styleable.CommandButton_command, 0)
            } finally {
                recycle()
            }
        }

        setOnClickListener {
            DoAsync {
                try {
                    CommandService.send(commandValue)
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
    }

    fun setCommand(cmd: Command) {
        this.commandValue = cmd.value
    }

}
