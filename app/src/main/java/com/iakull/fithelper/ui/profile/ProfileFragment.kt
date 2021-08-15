package com.iakull.fithelper.ui.profile

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import com.firebase.ui.auth.AuthUI
import com.iakull.fithelper.BuildConfig
import com.iakull.fithelper.R
import com.iakull.fithelper.RC_SIGN_IN
import com.iakull.fithelper.databinding.FragmentProfileBinding
import com.iakull.fithelper.ui.SharedViewModel
import com.iakull.fithelper.ui.profile.ProfileFragmentDirections.Companion.toEditPassFragment
import com.iakull.fithelper.ui.profile.ProfileFragmentDirections.Companion.toEditProfileFragment
import com.iakull.fithelper.util.PreferencesKeys
import com.iakull.fithelper.util.navigate
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val sharedVM: SharedViewModel by sharedViewModel()
    private val preferences: SharedPreferences by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProfileBinding.inflate(inflater)
        binding.vm = sharedVM
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.editProfile -> {
                    navigate(toEditProfileFragment())
                }
                R.id.editPass -> {
                    navigate(toEditPassFragment())
                }
                R.id.signInSignOut -> signInSignOut()
                R.id.darkTheme -> switchTheme()
            }
            true
        }

        sharedVM.user.observe(viewLifecycleOwner) { user ->
            val editProfileItem = binding.navView.menu.findItem(R.id.editProfile)
            val editPassItem = binding.navView.menu.findItem(R.id.editPass)
            //val adminItem = binding.navView.menu.findItem(R.id.editProfile)
            val signInSignOutItem = binding.navView.menu.findItem(R.id.signInSignOut)

            editPassItem.isVisible = user != null
            editProfileItem.isVisible = user != null
            //adminItem.isVisible = user.name = admin
            signInSignOutItem.title = if (user != null) getString(R.string.sign_out) else getString(R.string.sign_in)
        }
    }

    private fun signInSignOut() {
        if (sharedVM.user.value == null) {
            val providers = listOf(
                    AuthUI.IdpConfig.EmailBuilder().build(),
                    AuthUI.IdpConfig.GoogleBuilder().build()
            )
            activity?.startActivityForResult(AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setIsSmartLockEnabled(!BuildConfig.DEBUG)
                    .setAvailableProviders(providers)
                    .build(), RC_SIGN_IN)
        } else {
            AuthUI.getInstance().signOut(requireContext())
            sharedVM.signOut()
        }
    }

    private fun switchTheme() {
        val isDarkTheme = preferences.getBoolean(PreferencesKeys.IS_DARK_THEME, false)
        preferences.edit().putBoolean(PreferencesKeys.IS_DARK_THEME, !isDarkTheme).apply()
        activity?.recreate()
    }
}