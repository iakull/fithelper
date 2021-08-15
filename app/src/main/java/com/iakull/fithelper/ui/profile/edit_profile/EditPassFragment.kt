package com.iakull.fithelper.ui.profile.edit_profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.iakull.fithelper.R
import com.iakull.fithelper.databinding.FragmentEditPassBinding
import com.iakull.fithelper.util.popBackStack
import com.iakull.fithelper.util.toast
import kotlinx.android.synthetic.main.fragment_edit_pass.*

class EditPassFragment : Fragment() {

    private var auth = FirebaseAuth.getInstance()
    private lateinit var binding: FragmentEditPassBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEditPassBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.saveBtn.setOnClickListener {
            changePassword()
        }
        return binding.root
    }

    private fun changePassword() {
        if (oldPass.text!!.isNotBlank() &&
                newPass.text!!.isNotBlank() &&
                confirmNewPass.text!!.isNotBlank()
        ) {
            if (newPass.text.toString() == confirmNewPass.text.toString()) {
                val user = auth.currentUser
                if (user != null && user.email != null) {
                    val credential = EmailAuthProvider.getCredential(user.email!!, oldPass.text.toString())
                    user.reauthenticate(credential)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    user.updatePassword(newPass.text.toString())
                                            .addOnCompleteListener { task ->
                                                if (task.isSuccessful) {
                                                    toast(getString(R.string.pass_changed))
                                                    popBackStack()
                                                }
                                            }
                                } else {
                                    oldPass.text = null
                                    newPass.text = null
                                    confirmNewPass.text = null
                                    toast(getString(R.string.pass_incorrect))
                                }
                            }
                }
            } else {
                toast(getString(R.string.pass_different))
            }
        } else {
            toast(getString(R.string.fill_pass))
        }
    }
}