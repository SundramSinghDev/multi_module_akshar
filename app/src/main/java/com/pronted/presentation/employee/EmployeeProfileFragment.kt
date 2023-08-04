package com.pronted.presentation.employee

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.base.BaseActivity
import com.base.BaseFragment
import com.base.Trace
import com.base.inflateFragment
import com.pronted.R
import com.pronted.databinding.FragmentEmployeeProfileBinding
import com.pronted.dto.employee.Employee
import com.pronted.dto.login.ImageAction
import com.pronted.presentation.home.ChildActivity
import com.pronted.presentation.timeline.AppDataViewModel
import com.pronted.presentation.userapps.ImagesViewModel
import com.pronted.utils.SecurityResponseLabel
import com.pronted.utils.databinding.loadImage
import com.pronted.utils.extentions.*
import io.paperdb.Paper
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel

class EmployeeProfileFragment : BaseFragment<FragmentEmployeeProfileBinding>() {

    private val imagesViewModel: ImagesViewModel by viewModel()
    private val appDataViewModel: AppDataViewModel by activityViewModels()
    private var employeeList = arrayListOf<Employee>()
    private lateinit var employeeModel: Employee
    private var position = 0
    private var rightAnimation = false
    private var isEdited = false
    private var isInitializing = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = inflateFragment(
            inflater, container, R.layout.fragment_employee_profile
        ) as FragmentEmployeeProfileBinding
        return binding.root
    }

    override fun init() {
        binding.run {
            position = arguments?.getInt(NAV_OBJECT2) ?: 0
            isInitializing = true
            Paper.book().read<ArrayList<String>>(SECURITY_GROUP_LIST)
                ?.let { securityGroupList ->
                    tvEdit.visibility =
                        if (securityGroupList.contains(SecurityResponseLabel.ME_EMPLOYEE_PROFILE_EDIT)) View.VISIBLE else View.GONE
                }
            previous.setOnClickListener {
                position = --position
                rightAnimation = false
                loadProfileData()
                updateNavigation()
            }
            next.setOnClickListener {
                position = ++position
                rightAnimation = true
                loadProfileData()
                updateNavigation()
            }
            tvEdit.setOnClickListener {

                context?.let {
                    isEdited = true
                    val bundle = bundleOf(
                        Pair(NAV_DESTINATION, R.id.editEmployeeProfile),
                        Pair(NAV_OBJECT, employeeModel),
                        Pair(NAV_OBJECT2, position)
                    )
                    if(it is ChildActivity){
                        (context as ChildActivity).getNavController().navigate(R.id.editEmployeeProfile,bundle)
                    }else {
                        startNewActivity(
                            it, ChildActivity::class.java, bundle = bundle
                        )
                    }
                }
            }

            (arguments?.getSerializable(NAV_OBJECT) as ArrayList<Employee>)?.let {
                appDataViewModel.setEmployees(it)
            }
            //loadProfileData()
            //updateNavigation()
        }
    }


    private fun loadProfileData() {
        binding.run {
            context.let {
                if(position < employeeList.size) {
                    employeeModel = employeeList[position]
                    employee = employeeModel
                    //loadProfileImage(employeeModel.imageUrl)
                    val animation = AnimationUtils.loadAnimation(
                        it,
                        if (rightAnimation) R.anim.left_slide else R.anim.right_slide
                    )
                    clDetails1.animation = animation
                    clDetails2.animation = animation
                    clDetails3.animation = animation
                    tvProfileName.text = employeeModel.fullName.capitalizeWords()
                    if (employeeModel.imageUrl.isEmpty())
                        fetchProfileImage(employeeModel.employeeProfileId.toString())
                    else
                        loadProfileImage(employeeModel.imageUrl)
                }
            }
        }
    }

    private fun loadProfileImage(url:String = "") {
        binding.run {
            context?.let {
                if (url.isNotEmpty()) {
                    ivProfile.visibility = View.VISIBLE
                    ivTextProfile.visibility = View.GONE
                    AppCompatResources.getDrawable(it, R.drawable.ic_user)
                        ?.let { default ->
                            loadImage(ivProfile, url, default)
                        }
                } else {
                    //fetchProfileImage(employeeModel.employeeProfileId.toString())
                    ivProfile.visibility = View.GONE
                    ivTextProfile.visibility = View.VISIBLE
                    tvShortName.text =
                        employeeModel.fullName.substring(0, 2).uppercase()
                    when (position % 3) {
                        0 -> {
                            ivTextBg.setImageResource(R.color.light_pink)
                            tvShortName.setTextColor(it.getColor(R.color.pink))
                        }
                        1 -> {
                            ivTextBg.setImageResource(R.color.light_blue1)
                            tvShortName.setTextColor(it.getColor(R.color.light_blue))
                        }
                        else -> {
                            ivTextBg.setImageResource(R.color.light_green1)
                            tvShortName.setTextColor(it.getColor(R.color.green_normal))
                        }

                    }
                }
            }
        }
    }

    override fun resume() {
        (activity as BaseActivity<*>).title(getString(R.string.view_profile))
        appDataViewModel.employeesList.observe(this) {
            if(isEdited || isInitializing) {
                employeeList = it
                Trace.i("List count" + employeeList.size + "position: " + position)
                loadProfileData()
                updateNavigation()
                isEdited = false
                isInitializing = false
            }
        }
    }

    private fun updateNavigation() {
        binding.run {
            context?.let {
                if(employeeList.isNotEmpty() && employeeList.size > 1){
                    clNavigation.visibility = View.VISIBLE
                    if (position == 0) {
                        previous.isEnabled = false
                        previous.background = ResourcesCompat.getDrawable(
                            resources, R.drawable.btn_secondary_background_disabled, it.theme
                        )
                    }else if(position == employeeList.size - 1){
                        next.isEnabled = false
                        next.background = ResourcesCompat.getDrawable(
                            resources, R.drawable.btn_secondary_background_disabled, it.theme
                        )
                    }
                    else{
                        previous.isEnabled = true
                        previous.background = ResourcesCompat.getDrawable(
                            resources, R.drawable.btn_secondary_background, it.theme
                        )
                        next.isEnabled = true
                        next.background = ResourcesCompat.getDrawable(
                            resources, R.drawable.btn_secondary_background, it.theme
                        )
                    }

                }else{
                    clNavigation.visibility = View.GONE
                }

            }
        }

    }

    private fun fetchProfileImage(profileId: String) {
        lifecycleScope.launchWhenResumed {
            imagesViewModel.fetchProfileImage(
                module = EMPLOYEES,
                profileId = profileId
            ).collectLatest { imageAction ->
                when (imageAction) {
                    is ImageAction.Success -> {
                        Trace.e(imageAction.imageResponse.imageUrl)
                        employeeList[position].imageUrl = imageAction.imageResponse.imageUrl ?: ""
                        loadProfileImage(imageAction.imageResponse.imageUrl ?: "")

                    }
                    is ImageAction.Fail -> {
                        Trace.i(
                            imageAction.errorResponse.message
                                ?: getString(R.string.something_went_wrong_try_again)
                        )
                        loadProfileImage()
                    }
                }
            }
        }
    }
}