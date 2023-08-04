package com.pronted.presentation.authentication

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.pronted.presentation.userapps.SwitchRoleActivity
import com.pronted.databinding.FragmentSplashScreenBinding
import com.base.*
import com.pronted.R
import com.pronted.dto.login.UserAppList
import com.pronted.presentation.home.SmartParentActivity
import com.pronted.presentation.home.SmartSchoolActivity
import com.pronted.utils.extentions.*
import io.paperdb.Paper

@SuppressLint("CustomSplashScreen")
class SplashScreen : BaseFragment<FragmentSplashScreenBinding>() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = inflateFragment(
            inflater,
            container,
            R.layout.fragment_splash_screen
        ) as FragmentSplashScreenBinding
        observeClick(root)
        return binding.root
    }

    override fun init() {
        createChannels()
        coroutineDelay(2000) {
            when {
                Preference.instance.getBoolean(IS_LOGGED_IN) -> {
                    context?.let {context ->
                        if (Paper.book().read<UserAppList>(USER_SELECTED_ROLE) != null) {
                            Paper.book().read<UserAppList>(USER_SELECTED_ROLE)?.let {
                                when(it.appName){
                                    SMART_PARENT ->{
                                        startNewActivity(context, SmartParentActivity::class.java, finish = true)
                                    }
                                    SPECTRUM_ROLE ->{
                                        startNewActivity(context, SmartSchoolActivity::class.java, finish = true)
                                    }
                                }
                            }
                        } else {
                            startNewActivity(
                                context, SwitchRoleActivity::class.java, finish = true
                            )
                        }
                    }
                }
                else -> {
                    findNavController().navigate(R.id.action_splashScreen_loginWithPhoneNumber)
                }
            }
        }
    }

    private fun createChannels() {
        val connectionChannel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME, NotificationManager.IMPORTANCE_NONE
        )
        connectionChannel.enableLights(false)
        connectionChannel.enableVibration(false)
        connectionChannel.setShowBadge(false)
        connectionChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val notificationManager =
            (context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
        notificationManager.createNotificationChannel(connectionChannel)
    }
}