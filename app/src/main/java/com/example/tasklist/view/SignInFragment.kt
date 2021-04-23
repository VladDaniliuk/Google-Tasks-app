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
import com.example.tasklist.api.model.body.GoogleAuthTokenBody
import com.example.tasklist.databinding.FragmentSignInBinding
import com.example.tasklist.model.LogInRepository
import com.example.tasklist.viewModel.SignInViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignInFragment  : Fragment() {

	@Inject
	lateinit var logInRepository: LogInRepository
	private val viewModel: SignInViewModel by viewModels()

	private lateinit var binding: FragmentSignInBinding

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
	): View {
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

		viewModel.loginEvent.observe(viewLifecycleOwner) {
			findNavController().navigate(R.id.taskListListFragment)
		}

		//viewModel.onStartSignIn()

		if (viewModel.getToken == null) {
			onSignInClick()
		} else {
			findNavController().navigate(R.id.taskListListFragment)
		}
	}

	private fun onSignInClick() {
		/*viewModel.onStartSignIn()
		val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
			.requestScopes(Scope("https://www.googleapis.com/auth/tasks"))
			.requestServerAuthCode(getString(R.string.default_web_client_id))
			.requestEmail()
			.build()
		val mGoogleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
		val signInIntent = mGoogleSignInClient.signInIntent
		startForResult.launch(signInIntent)*/
		logInRepository.onLogIn(this, viewModel)
	}

	/*private val startForResult = registerForActivityResult(
		ActivityResultContracts.StartActivityForResult()
	) { result: ActivityResult ->
		if (result.resultCode == Activity.RESULT_OK) {
			val task: Task<GoogleSignInAccount> =
				GoogleSignIn.getSignedInAccountFromIntent(result.data)
			if (task.isSuccessful) {
				val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)
				val authCode = account?.serverAuthCode
				viewModel.setToken(
					GoogleAuthTokenBody(
						getString(R.string.default_web_client_id),
						getString(R.string.secret),
						authCode,
						"authorization_code",
						""
					)
				)
			}
		} else
			viewModel.onCancelSignIn()
	}*/
}