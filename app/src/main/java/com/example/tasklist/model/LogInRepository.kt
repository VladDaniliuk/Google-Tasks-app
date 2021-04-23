package com.example.tasklist.model

import android.app.Activity
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import com.example.tasklist.R
import com.example.tasklist.api.model.body.GoogleAuthTokenBody
import com.example.tasklist.viewModel.SignInViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

interface LogInRepository {
	fun onLogIn(context: Fragment, viewModel: SignInViewModel)
}

class LogInRepositoryImpl @Inject constructor(
	private val preferenceManager: PreferenceManager,
	private val retrofit: RetrofitF
) : LogInRepository {
	override fun onLogIn(context: Fragment, viewModel: SignInViewModel) {

		val startForResult =
			context.registerForActivityResult(
				ActivityResultContracts
					.StartActivityForResult()
			) { result: ActivityResult ->
				if (result.resultCode == Activity.RESULT_OK) {
					val task: Task<GoogleSignInAccount> =
						GoogleSignIn.getSignedInAccountFromIntent(result.data)
					if (task.isSuccessful) {
						val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)
						val authCode = account?.serverAuthCode
						retrofit.retrofitService.getToken(
							GoogleAuthTokenBody(
								context.getString(R.string.default_web_client_id),
								context.getString(R.string.secret),
								authCode,
								"authorization_code",
								""
							)
						)
							.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
							.subscribe({ v ->
								preferenceManager.setToken(v)
								findNavController(context.requireView())
									.navigate(R.id.taskListListFragment)
							},
								{ Log.e("LOG", null, it) }
							)
					} else {
						viewModel.onCancelSignIn()
					}
				} else {
					viewModel.onCancelSignIn()
				}
			}

		val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
			.requestScopes(Scope("https://www.googleapis.com/auth/tasks"))
			.requestServerAuthCode(context.getString(R.string.default_web_client_id))
			.requestEmail()
			.build()
		val mGoogleSignInClient: GoogleSignInClient =
			GoogleSignIn.getClient(context.requireActivity(), gso)
		val signInIntent = mGoogleSignInClient.signInIntent
		viewModel.onStartSignIn()


		startForResult.launch(signInIntent)
	}
}