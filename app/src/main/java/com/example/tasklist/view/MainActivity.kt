package com.example.tasklist.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.tasklist.R
import com.example.tasklist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

	private lateinit var navController: NavController

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		val binding: ActivityMainBinding = DataBindingUtil.setContentView(
			this, R.layout.activity_main
		)

		val host: NavHostFragment = supportFragmentManager
			.findFragmentById(R.id.nav_graph) as NavHostFragment? ?: return
		navController = host.navController

		supportFragmentManager.commit {
			setReorderingAllowed(true)
			add<SignInFragment>(R.id.fragment_container_view)
		}
	}
}