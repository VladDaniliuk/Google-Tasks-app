package com.example.tasklist.viewModel

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.tasklist.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class SignInFragment: Fragment(R.layout.fragment_sign_in) {

	 private val RC_SIGN_IN: Int = 1001

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		/*val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail()
			.build()
		val mGoogleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
		val signInIntent = mGoogleSignInClient.signInIntent
		startActivityForResult(signInIntent,RC_SIGN_IN)*/
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		if (requestCode == RC_SIGN_IN && resultCode == Activity.RESULT_OK) Toast.makeText(
				this.requireContext(), requestCode.toString(), Toast.LENGTH_LONG).show()
	}

	fun onSignIn() {
		
		/*val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail()
				.build()
		val mGoogleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
		val signInIntent = mGoogleSignInClient.signInIntent
		startActivityForResult(signInIntent,RC_SIGN_IN)*/
	}
}