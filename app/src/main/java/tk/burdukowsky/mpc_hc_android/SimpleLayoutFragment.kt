package tk.burdukowsky.mpc_hc_android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.appcompat.content.res.AppCompatResources

class SimpleLayoutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_simple_layout, container, false)

        val layout: LinearLayout = view.findViewById(R.id.simple_layout_fragment_root_layout)

        val childLayout = LinearLayout(activity)

        val playButton = CommandButton(App.instance, null)
        playButton.command = Command.play.value
        playButton.icon = AppCompatResources.getDrawable(App.instance, R.drawable.ic_play)

        childLayout.addView(playButton)

        layout.addView(
            childLayout,
            LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        )

        return view
    }

}
