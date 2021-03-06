package app.atom.praveen.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import app.atom.praveen.databinding.LoadingDialogLayoutBinding

class SimpleLoadingDialog(context: Context, private val message: String ="Loading...") : Dialog(context) {

    private lateinit var binding:LoadingDialogLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= LoadingDialogLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.messageText.text = message
        setCancelable(false)
    }

    fun setMessage(message: String) {
        binding.messageText.text = message
    }
}