package tk.burdukowsky.mpc_hc_android

import android.content.Context
import android.util.AttributeSet
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton

class CommandButton(context: Context?, attrs: AttributeSet?) : AppCompatButton(context, attrs) {

    init {
        context?.let {
            it.theme.obtainStyledAttributes(
                attrs,
                R.styleable.CommandButton,
                0, 0
            ).apply {
                try {
                    val command = getInteger(R.styleable.CommandButton_command, 0)
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

}
