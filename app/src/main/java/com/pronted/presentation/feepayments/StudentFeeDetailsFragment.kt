package com.pronted.presentation.feepayments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.base.*
import com.google.android.material.tabs.TabLayoutMediator
import com.pronted.R
import com.pronted.databinding.FragmentStudentFeeDetailsBinding
import com.pronted.dto.login.UserAppList
import com.pronted.dto.student.StudentProfileAction
import com.pronted.dto.student.StudentProfileResponse
import com.pronted.presentation.profile.StudentViewModel
import com.pronted.utils.extentions.*
import io.paperdb.Paper
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel

class StudentFeeDetailsFragment : BaseFragment<FragmentStudentFeeDetailsBinding>() {

    private var studentProfile: StudentProfileResponse? = null
    private val studentViewModel: StudentViewModel by viewModel()
    private lateinit var feePaymentsViewPager: FeePaymentsViewPager
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = inflateFragment(
            inflater, container, R.layout.fragment_student_fee_details
        ) as FragmentStudentFeeDetailsBinding
        return binding.root
    }

    override fun init() {
        studentProfile = if (Build.VERSION.SDK_INT >= 33) {
            arguments?.getSerializable(NAV_OBJECT, StudentProfileResponse::class.java)
        }else{
            arguments?.getSerializable(NAV_OBJECT) as StudentProfileResponse?
        }

        binding.run {
            if (studentProfile == null && Paper.book()
                    .read<UserAppList>(USER_SELECTED_ROLE)?.appName == SMART_PARENT
            ) {
                fetchStudentProfile()
            } else {
                initViews()
            }
        }
    }

    private fun initViews() {
        binding.run {

            chipName.text = studentProfile?.fullName?.capitalizeWords()?: ""
            chipClassName.text =
                "${studentProfile?.classroom?.courseName} ${studentProfile?.classroom?.classroomName}"
            feePaymentsViewPager = createFeeViewPagerAdapter()
            feeViewPager.adapter = feePaymentsViewPager
            TabLayoutMediator(
                feeTabLayout, feeViewPager
            ) { tab, position ->
                tab.text = resources.getStringArray(R.array.tab_fee_payments)[position]
            }.attach()
            feeViewPager.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageScrollStateChanged(state: Int) {
                    if (state == ViewPager2.SCROLL_STATE_IDLE) {
                        Trace.e("fee details pager changed")

                    }
                }
            })
        }
    }

    private fun createFeeViewPagerAdapter(): FeePaymentsViewPager {
        return FeePaymentsViewPager(
            context as BaseActivity<*>, studentProfile
        )
    }

    override fun resume() {
        (activity as BaseActivity<*>).title(getString(R.string.fee_details))
    }

    /**
     * Fetch student profile
     */
    private fun fetchStudentProfile() {
        lifecycleScope.launchWhenResumed {
            showLoader()
            Preference.instance.putBoolean(DYNAMIC_HEADERS, false)
            studentViewModel.fetchStudentProfile()
                .collectLatest { profileAction ->
                    dismissLoader()
                    when (profileAction) {
                        is StudentProfileAction.Success -> {
                            Trace.e("Student profile: " + profileAction.studentProfileResponse)
                            studentProfile = profileAction.studentProfileResponse
                            Paper.book()
                                .write(STUDENT_PROFILE, profileAction.studentProfileResponse)
                           initViews()
                        }
                        is StudentProfileAction.Fail -> {
                            Trace.i(
                                profileAction.errorResponse.message ?: getString(
                                    R.string.something_went_wrong_try_again
                                )
                            )
                        }
                    }
                }
        }
    }


}