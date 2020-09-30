package tk.burdukowsky.mpc_hc_android

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation

class ChooseLayoutFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Navigation
            .findNavController(requireActivity(), R.id.nav_host_fragment)
            .navigate(calculateActionId())
    }

    private fun calculateActionId(): Int {
        if (AppPreferences.getCurrentHost() == null)
            return R.id.action_choose_layout_to_settings

        return when (AppPreferences.getLayout()) {
            Layout.SIMPLE -> R.id.action_choose_layout_to_simple
            Layout.STRETCH -> R.id.action_choose_layout_to_stretch
        }
    }

}
