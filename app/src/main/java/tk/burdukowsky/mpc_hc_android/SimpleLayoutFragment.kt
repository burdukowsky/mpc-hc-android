package tk.burdukowsky.mpc_hc_android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class SimpleLayoutFragment : Fragment() {

    class ButtonInfo(val cmd: Command, val icon: Int)

    private val rows: List<List<ButtonInfo>> = listOf(
        listOf(
            ButtonInfo(Command.play, R.drawable.ic_play),
            ButtonInfo(Command.pause, R.drawable.ic_pause)
        ),
        listOf(
            ButtonInfo(Command.volume_down, R.drawable.ic_volume_down),
            ButtonInfo(Command.volume_up, R.drawable.ic_volume_up)
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_simple_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val root = view.findViewById(R.id.simple_layout_root) as LinearLayout

        for (row in rows) {
            val rowLayout =
                LinearLayout(root.context, null, R.style.LinearLayoutWithHorizontalDivider)
            rowLayout.apply {
                layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
                orientation = LinearLayout.HORIZONTAL
            }

            for (button in row) {
                val buttonView = CommandButton(
                    rowLayout.context,
                    null,
                    R.style.Widget_App_Button_SimpleLayout_IconOnly
                ).apply {
                    icon = ContextCompat.getDrawable(view.context, button.icon)
                    setCommand(button.cmd)
                }
                rowLayout.addView(buttonView)
            }

            root.addView(rowLayout)
        }

        // todo: https://stackoverflow.com/questions/3477422/what-does-layoutinflater-in-android-do/41500409#41500409
        // todo: https://stackoverflow.com/a/40706476
    }

}
