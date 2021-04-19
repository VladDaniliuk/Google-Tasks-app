package com.example.tasklist.view

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.tasklist.R
import com.example.tasklist.databinding.FragmentSignInBinding
import com.example.tasklist.viewModel.SignInViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment: Fragment() {

	private val viewModel: SignInViewModel by viewModels()

	private lateinit var binding: FragmentSignInBinding

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?
							  , savedInstanceState: Bundle?): View {
		binding = FragmentSignInBinding.inflate(inflater, container, false)
		binding.viewModel = viewModel

		binding.lifecycleOwner = viewLifecycleOwner

		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		viewModel.onSignInClick.observe(viewLifecycleOwner) {
			onSignInClick()
		}

		viewModel.onStartSignIn()

		if (GoogleSignIn.getLastSignedInAccount(view.context) == null/*viewModel.getToken == null*/) {
			onSignInClick()
		} else {
			findNavController().navigate(R.id.taskListListFragment)
		}
	}

	private fun onSignInClick() {
		viewModel.onStartSignIn()
		val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail()
				.build()
		val mGoogleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
		val signInIntent = mGoogleSignInClient.signInIntent
		startForResult.launch(signInIntent)
	}

	private val startForResult = registerForActivityResult(ActivityResultContracts
		.StartActivityForResult()) { result: ActivityResult ->

		if (result.resultCode == Activity.RESULT_OK) {
			viewModel.setToken(GoogleSignIn.getLastSignedInAccount(view?.context)?.idToken.toString())
			findNavController().navigate(R.id.taskListListFragment)
		}
		else
			viewModel.onCancelSignIn()
	}
}