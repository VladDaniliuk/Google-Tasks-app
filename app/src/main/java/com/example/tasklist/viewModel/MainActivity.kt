package com.example.tasklist.viewModel

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.example.tasklist.R
import com.example.tasklist.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

class MainActivity : AppCompatActivity() {
	private lateinit var mGoogleSignInClient: GoogleSignInClient

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		val binding: ActivityMainBinding = DataBindingUtil.setContentView(
			this, R.layout.activity_main
		)

		supportFragmentManager.commit {
			setReorderingAllowed(true)
			add<SignInFragment>(R.id.fragment_container_view)
			/*val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail()
			.build()

		mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
		val signInIntent = mGoogleSignInClient.signInIntent
		startActivityForResult(signInIntent, 1)*/
		}

		fun onSignIn(view: View) {
			val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail()
					.build()
			val mGoogleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(view.context, gso)
			val signInIntent = mGoogleSignInClient.signInIntent
			startActivityForResult(signInIntent,1)
		}



	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)

		if (requestCode == 1) {
			val task: com.google.android.gms.tasks.Task<GoogleSignInAccount>? =
					GoogleSignIn.getSignedInAccountFromIntent(data)
			handleSignInResult(task)
		}
	}
	fun handleSignInResult(completedTask: com.google.android.gms.tasks.Task<GoogleSignInAccount>?) {
		try {
			val account: GoogleSignInAccount? = completedTask?.getResult(ApiException::class.java)

			// Signed in successfully, show authenticated UI.
		} catch (e: ApiException) {
			// The ApiException status code indicates the detailed failure reason.
			// Please refer to the GoogleSignInStatusCodes class reference for more information
		}
	}
}