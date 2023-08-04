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
import com.pronted.databinding.ActivitySmartSchoolBinding
import com.pronted.databinding.DialogLogoutBinding
import com.pronted.dto.login.ImageAction
import com.pronted.dto.login.UserAppList
import com.pronted.presentation.timeline.AppDataViewModel
import com.pronted.presentation.userapps.ImagesViewModel
import com.pronted.presentation.userapps.SwitchRoleActivity
import com.pronted.utils.EventFlow
import com.pronted.utils.SecurityResponseLabel
import com.pronted.utils.databinding.loadImage
import com.pronted.utils.extentions.*
import io.paperdb.Paper
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel

class SmartSchoolActivity : BaseActivity<ActivitySmartSchoolBinding>() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val imagesViewModel: ImagesViewModel by viewModel()
    private val appDataViewModel: AppDataViewModel by viewModels()
    private var securityGroupList = arrayListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            inflateActivity(this, R.layout.activity_smart_school) as ActivitySmartSchoolBinding
        setContentView(binding.root)
        //remove below line to support night mode as well
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        init()
        //setCollector()
    }

    private fun init() {
        val drawerLayout: DrawerLayout = binding.drawerLayoutParent
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_school)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_time_line,
                R.id.navigation_timetable,
                R.id.navigation_attendance,
                R.id.navigation_noticeboard,
                R.id.navigation_fee_payments,
                R.id.navigation_student_marks,
                R.id.navigation_students,
                R.id.navigation_employee_profile,
                R.id.navigation_schedule_exam,
                R.id.navigation_message_center,
            ), drawerLayout
        )
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        navView.menu.findItem(R.id.navigation_logout).setOnMenuItemClickListener {
            it.isChecked = true
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
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
                     this@SmartSchoolActivity, ChildActivity::class.java, bundle = bundleOf(
                         Pair(NAV_DESTINATION, R.id.profileScreen)
                     )
                 )
             }*/
        }
        Trace.e("Access Token: " + Preference.instance.getString(ACCESS_TOKEN))
        Paper.book().read<UserAppList>(USER_SELECTED_ROLE)?.let {
            when (it.appName) {
                SPECTRUM_ROLE -> {
                    if (it.profileImage.isNullOrBlank()) {
                        fetchUserProfileImage(it.userUniqueId)
                    } else {
                        it.profileImage?.let { imgUrl ->
                            setNavHeaderImage(imgUrl)
                        }
                    }
                }
            }
            val userName = if (it.appName == SMART_PARENT) it.studentName else it.employeeName
            setNavHeaderName(if (userName.length <= 25) userName else "$userName.substring(0, 25)...")
        }
        appDataViewModel.securityGroups.observe(this) {
            Trace.e("on change of Security groups:$it")
            securityGroupList = it
            configureNavMenu()
        }
    }

    private fun openLogoutDialog() {
        val dialogLogoutBinding = DialogLogoutBinding.inflate(layoutInflater)
        val dialog = Dialog(this).apply {
            setContentView(dialogLogoutBinding.root)
            window?.setLayout(
                Constraints.LayoutParams.MATCH_PARENT,
                Constraints.LayoutParams.WRAP_CONTENT
            )
        }
        dialog.show()
        dialogLogoutBinding.run {
            imgCancel.setOnClickListener {
                dialog.dismiss()
            }
            btnLogout.setOnClickListener {
                logout(this@SmartSchoolActivity, true)
                dialog.dismiss()
            }
        }
    }

    private fun setNavHeaderName(name: String) {
        val header: View = binding.navView.getHeaderView(0)
        val textView = header.findViewById<AppCompatTextView?>(R.id.tv_profile_name)
        textView.text = name
        textView.setOnClickListener {
            Paper.book().delete(SECURITY_GROUP_LIST)
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
        menuInflater.inflate(R.menu.bottom_nav_menu_school, menu)
        return false
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_school)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun configureNavMenu() {
        Paper.book().read<ArrayList<String>>(SECURITY_GROUP_LIST)?.let { securityGroupList ->
            binding.navView.run {
                menu.findItem(R.id.navigation_students).isVisible = securityGroupList.contains(
                    SecurityResponseLabel.ME_STUDENT_PROFILE_VIEW
                )
                menu.findItem(R.id.navigation_timetable).isVisible =
                    securityGroupList.contains("ME_MY_TIME_TABLE_VIEW")
                            || securityGroupList.contains("ME_CLASSWISE_TIME_TABLE_VIEW")

                menu.findItem(R.id.navigation_employee_profile).isVisible =
                    securityGroupList.contains(
                        SecurityResponseLabel.ME_EMPLOYEE_PROFILE_VIEW
                    )

                menu.findItem(R.id.navigation_attendance).isVisible = securityGroupList.contains(
                    SecurityResponseLabel.ME_STUDENT_ATTENDANCE_VIEW
                )

                menu.findItem(R.id.navigation_schedule_exam).isVisible = securityGroupList.contains(
                    SecurityResponseLabel.ME_EXAM_SCHEDULE_VIEW
                )

                menu.findItem(R.id.navigation_student_marks).isVisible = securityGroupList.contains(
                    SecurityResponseLabel.ME_STUDENT_MARKS_VIEW
                )

                menu.findItem(R.id.navigation_student_marks).isVisible = securityGroupList.contains(
                    SecurityResponseLabel.ME_STUDENT_MARKS_VIEW
                )

                menu.findItem(R.id.navigation_noticeboard).isVisible = securityGroupList.contains(
                    SecurityResponseLabel.ME_NOTICE_BOARD_VIEW
                )

                menu.findItem(R.id.navigation_fee_payments).isVisible =
                    securityGroupList.contains(SecurityResponseLabel.ME_FEES_VIEW) || securityGroupList.contains(
                        SecurityResponseLabel.ME_PAYMENTS_VIEW
                    )

                menu.findItem(R.id.navigation_message_center).isVisible =
                    !(!securityGroupList.contains("ME_MC_EMPLOYEE_NOTIFICATION")
                            && !securityGroupList.contains("ME_MC_ABSENT_REPORT") && !securityGroupList.contains(
                        "ME_MC_FEE_REMINDER"
                    )
                            && !securityGroupList.contains("ME_MC_GENERAL_NOTIFICATION") && !securityGroupList.contains(
                        "ME_MC_LATE_ENTRY"
                    )
                            && !securityGroupList.contains("ME_MC_MARKS_REPORT") && !securityGroupList.contains(
                        "ME_MC_STUDENT_NOTIFICATION"
                    ))

            }
        }
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
                logout(this@SmartSchoolActivity, true)
            }
        }
    }

    /**
     * Fetch employee details
     */
    private fun fetchUserProfileImage(employeeProfileId: Int, module: String = EMPLOYEES) {
        lifecycleScope.launchWhenResumed {
            imagesViewModel.fetchProfileImage(headers = null, module, employeeProfileId.toString())
                .collectLatest { empImageAction ->
                    when (empImageAction) {
                        is ImageAction.Success -> {
                            empImageAction.imageResponse.imageUrl?.let { it1 ->
                                // Paper.book().write(EMPLOYEE_IMAGE, it1)
                                setNavHeaderImage(it1)
                            }
                        }
                        is ImageAction.Fail -> {
                            Trace.i(
                                empImageAction.errorResponse.message ?: getString(
                                    R.string.something_went_wrong_try_again
                                )
                            )
                        }
                    }
                }
        }
    }

}