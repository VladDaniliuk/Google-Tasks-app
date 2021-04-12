package com.example.tasklist.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.tasklist.R
import com.example.tasklist.databinding.FragmentSignInBinding
import com.example.tasklist.viewModel.SignInViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class SignInFragment: Fragment() {

	private val RC_SIGN_IN: Int = 1001

	private val viewModel: SignInViewModel by viewModels()

	private lateinit var binding: FragmentSignInBinding

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?
							  , savedInstanceState: Bundle?): View {
		binding = FragmentSignInBinding.inflate(inflater, container, false)
		binding.viewModel = viewModel

		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		viewModel.onSignInClick.observe(viewLifecycleOwner) {
			onSignInClick()
		}

		if (GoogleSignIn.getLastSignedInAccount(view.context) == null) {
			onSignInClick()
		} else {
			binding.signInButton.visibility = View.GONE
		}
	}

	private fun onSignInClick() {
		val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail()
				.build()
		val mGoogleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
		val signInIntent = mGoogleSignInClient.signInIntent

		binding.contentLoadingProgressBar2.show()
		binding.signInButton.visibility = View.GONE

		startActivityForResult(signInIntent, RC_SIGN_IN)
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)

		if(requestCode == RC_SIGN_IN && resultCode == Activity.RESULT_OK) {

		} else if(resultCode == Activity.RESULT_CANCELED) {
			binding.contentLoadingProgressBar2.hide()
			binding.signInButton.visibility = View.VISIBLE
		}
	}
}