package com.example.tasklist.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.example.tasklist.R
import com.example.tasklist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		val binding: ActivityMainBinding = DataBindingUtil.setContentView(
			this, R.layout.activity_main
		)

		supportFragmentManager.commit {
			setReorderingAllowed(true)
			add<SignInFragment>(R.id.fragment_container_view)
		}
	}
}