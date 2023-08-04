package com.pronted.presentation.authentication

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.pronted.R
import com.pronted.databinding.ActivityAuthenticationBinding
import com.base.BaseActivity
import com.base.inflateActivity

class AuthenticationActivity : BaseActivity<ActivityAuthenticationBinding>() {

    private val navController: NavController by lazy {
        findNavController(R.id.nav_host_fragment_activity_login)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =
            inflateActivity(this, R.layout.activity_authentication) as ActivityAuthenticationBinding
        setContentView(binding.root)
        //remove below line to support night mode as well
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        init()
    }

    private fun init() {
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
        supportActionBar?.hide()
    }

}