package tk.burdukowsky.mpc_hc_android

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class ConfirmationDialogFragment(
    private val message: Int = R.string.confirmation_message,
    private val positiveButtonText: Int = R.string.yes,
    private val negativeButtonText: Int = R.string.no,
    private val positiveOnClickListener: Runnable = Runnable { },
    private val negativeOnClickListener: Runnable = Runnable { }
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            AlertDialog.Builder(it)
                .setMessage(message)
                .setPositiveButton(positiveButtonText) { _, _ -> positiveOnClickListener.run() }
                .setNegativeButton(negativeButtonText) { _, _ -> negativeOnClickListener.run() }
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}