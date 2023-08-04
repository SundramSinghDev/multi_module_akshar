package com.pronted.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.base.*
import com.pronted.databinding.FragmentProfileBinding
import com.pronted.presentation.authentication.OtpViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.pronted.R
import com.pronted.dto.login.UserAppList
import com.pronted.presentation.home.ChildActivity
import com.pronted.presentation.userapps.SwitchRoleActivity
import com.pronted.utils.extentions.*
import io.paperdb.Paper

class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    private val profileImageViewModel: OtpViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = inflateFragment(
            inflater,
            container,
            R.layout.fragment_profile
        ) as FragmentProfileBinding
        observeClick(root)
        return binding.root
    }

    override fun init() {

        binding.run {
            btnLogout.setOnClickListener {
                context?.let {
                    logout(it, true)
                }
            }
            Paper.book().read<UserAppList>(USER_SELECTED_ROLE)?.let {
                if (it.profileImage.isNullOrBlank()) {
                    if (it.appName == SPECTRUM_ROLE)
                        fetchUserProfileImage(it.userUniqueId)
                } else {
                    binding.profileImage = it.profileImage
                }

                val userName = if (it.appName == SMART_PARENT) it.studentName else it.employeeName
                tvProfileName.text =
                    if (userName.length <= 25) userName else "$userName.substring(0, 25)..."
            }
            tvProfileName.setOnClickListener {
                context?.let {
                    startNewActivity(it, SwitchRoleActivity::class.java, clearTop = true, finish = true)
                }
            }
            /* if (Paper.book().read<String?>(EMPLOYEE_IMAGE) != null)
                 binding.profileImage = Paper.book().read(EMPLOYEE_IMAGE)
             else
                 fetchEmployeeProfileImage()*/
        }
    }

    private fun fetchUserProfileImage(userUniqueId: Int) {

    }

    override fun resume() {
        (activity as BaseActivity<*>).title(getString(R.string.profile))
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

}