package com.pronted.presentation.home

import android.app.Dialog
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.Constraints
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.base.BaseActivity
import com.base.Preference
import com.base.Trace
import com.base.inflateActivity
import com.google.android.material.navigation.NavigationView
import com.pronted.R
import com.pronted.databinding.ActivitySmartParentBinding
import com.pronted.databinding.DialogLogoutBinding
import com.pronted.dto.login.UserAppList
import com.pronted.presentation.timeline.AppDataViewModel
import com.pronted.presentation.userapps.ImagesViewModel
import com.pronted.presentation.userapps.SwitchRoleActivity
import com.pronted.utils.EventFlow
import com.pronted.utils.databinding.loadImage
import com.pronted.utils.extentions.*
import io.paperdb.Paper
import org.koin.androidx.viewmodel.ext.android.viewModel

class SmartParentActivity : BaseActivity<ActivitySmartParentBinding>() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val imagesViewModel: ImagesViewModel by viewModel()
    private val appDataViewModel: AppDataViewModel by viewModels()
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            inflateActivity(this, R.layout.activity_smart_parent) as ActivitySmartParentBinding
        setContentView(binding.root)
        //remove below line to support night mode as well
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        init()
        //setCollector()
    }

    private fun init() {
        val drawerLayout: DrawerLayout = binding.drawerLayoutParent
        val navView: NavigationView = binding.navView
        navController = findNavController(R.id.nav_host_fragment_activity_parent)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_time_line,
                R.id.navigation_timetable,
                R.id.navigation_noticeboard,
                R.id.navigation_fee_payments,
                R.id.navigation_student_marks,
                R.id.navigation_student_profile,
                R.id.navigation_schedule_exam
            ), drawerLayout
        )
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.menu.findItem(R.id.navigation_logout).setOnMenuItemClickListener {
            if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            openLogoutDialog()
            true
        }

        binding.run {
            ivNotification.setOnClickListener {
                //todo navigate notification fragment
            }
            setNavHeaderImage()
            /* ivProfile.setOnClickListener {
                 startNewActivity(
                     this@SmartParentActivity, ChildActivity::class.java, bundle = bundleOf(
                         Pair(NAV_DESTINATION, R.id.profileScreen)
                     )
                 )
             }*/
        }
        Trace.e("Access Token: " + Preference.instance.getString(ACCESS_TOKEN))
        Paper.book().read<UserAppList>(USER_SELECTED_ROLE)?.let {
            val userName = it.studentName
            setNavHeaderName(if (userName.length <= 25) userName else "$userName.substring(0, 25)...")
        }

        appDataViewModel.securityGroups.observe(this) {
            Trace.e("on change of Security groups:$it")

        }
        appDataViewModel.studentProfile.observe(this){

        }
        appDataViewModel.studentProfileImage.observe(this){
            setNavHeaderImage(it)
        }


    }

    fun getNavController(): NavController {
        return navController
    }

    private fun openLogoutDialog() {
        val dialogLogoutBinding = DialogLogoutBinding.inflate(layoutInflater)
        val dialog = Dialog(this).apply {
            setContentView(dialogLogoutBinding.root)
            window?.setLayout(Constraints.LayoutParams.MATCH_PARENT, Constraints.LayoutParams.WRAP_CONTENT)
            }
        dialog.show()
        dialogLogoutBinding.run {
            imgCancel.setOnClickListener {
                dialog.dismiss()
            }
            btnLogout.setOnClickListener {
                logout(this@SmartParentActivity, true)
                dialog.dismiss()
            }
        }
    }
    fun navigateFeePayments(){
        binding.navView.menu.findItem(R.id.navigation_fee_payments).isChecked = true
    }
    private fun setNavHeaderName(name: String) {
        val header: View = binding.navView.getHeaderView(0)
        val textView = header.findViewById<AppCompatTextView?>(R.id.tv_profile_name)
        textView.text = name
        textView.setOnClickListener {
            Paper.book().delete(SECURITY_GROUP_LIST)
            Paper.book().delete(USER_SELECTED_ROLE)
            startNewActivity(this, SwitchRoleActivity::class.java, clearTop = true, finish = true)
        }
    }

    private fun setNavHeaderImage(imageUrl: String? = null) {
        val header: View = binding.navView.getHeaderView(0)
        val imageVIew: ImageView = header.findViewById(R.id.iv_profile)
        AppCompatResources.getDrawable(this, R.drawable.ic_user)
            ?.let { loadImage(imageVIew, imageUrl, it) }
        imageVIew.setOnClickListener {
            //todo add upload image feature
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.bottom_nav_menu_parent, menu)
        return false
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_parent)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun title(title: String) {
        binding.tvTitle.text = title
    }

    /**
     * Set collector
     */
    private fun setCollector() {
        lifecycleScope.launchWhenResumed {
            EventFlow.getEvent<Boolean>(EVENT_LOGOUT).collect {
                logout(this@SmartParentActivity, true)
            }
        }
    }

    fun refreshActivity(){
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }





}