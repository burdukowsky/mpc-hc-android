package tk.burdukowsky.mpc_hc_android

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation

class ChooseLayoutFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val fragmentId = when (AppPreferences.getLayout()) {
            Layout.SIMPLE -> R.id.SimpleLayoutFragment
            Layout.STRETCH -> R.id.StretchLayoutFragment
        }

        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(fragmentId)
    }

}
