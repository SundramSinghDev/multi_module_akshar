package com.pronted.presentation.feepayments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.BaseFragment
import com.base.Preference
import com.base.Trace
import com.base.inflateFragment
import com.pronted.R
import com.pronted.databinding.FragmentFeeDetailsPagerBinding
import com.pronted.dto.feepayments.StudentFeeDetailsAction
import com.pronted.dto.feepayments.data.FeesDetailModel
import com.pronted.dto.student.StudentProfileResponse
import com.pronted.presentation.home.ChildActivity
import com.pronted.presentation.listener.FeeDetailsClickListener
import com.pronted.utils.DateUtil
import com.pronted.utils.extentions.*
import com.pronted.utils.toDateString
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class FeeDetailsPagerFragment : BaseFragment<FragmentFeeDetailsPagerBinding>() {
    private val feePaymentsViewModel: FeePaymentsViewModel by viewModel()
    private var tabPosition = 0
    private var studentProfile: StudentProfileResponse? = null
    private lateinit var feeDetailsAdapter: FeeDetailsAdapter
    private val currentDate by lazy {
        Calendar.getInstance().time.toDateString(DateUtil.y4M2d2)
    }

    companion object {
        fun newInstance(page: Int, studentProfile: StudentProfileResponse?): Fragment {
            val fragment = FeeDetailsPagerFragment()
            val bundle = Bundle()
            bundle.putInt("var_arg", page)
            studentProfile?.let {
                bundle.putSerializable(STUDENT_PROFILE, it)
            }
            fragment.arguments = bundle
            return fragment
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = inflateFragment(
            inflater, container, R.layout.fragment_fee_details_pager
        ) as FragmentFeeDetailsPagerBinding
        return binding.root
    }

    override fun init() {
        tabPosition = arguments?.getInt("var_arg") ?: 0
        studentProfile = arguments?.getSerializable(STUDENT_PROFILE) as StudentProfileResponse
        binding.run {
            studentProfile?.let {
                fetchStudentFeeDetails(it.studentProfileId)
            }
            hasFeeDetails = false
            rvFeeList.setHasFixedSize(true)
            rvFeeList.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )
            context?.let {
                feeDetailsAdapter = FeeDetailsAdapter(it, feeDetailsClickListener)
                rvFeeList.adapter = feeDetailsAdapter
            }

        }

    }

    private val feeDetailsClickListener = object : FeeDetailsClickListener<FeesDetailModel> {
        override fun onPayClick(item: FeesDetailModel) {
            Trace.i("on pay click")
            context?.let {
                val bundle = bundleOf(
                    Pair(NAV_DESTINATION, R.id.feeDetailsSummary),
                    Pair(NAV_OBJECT, studentProfile),
                    Pair(NAV_OBJECT2, item)
                )
                if(it is ChildActivity){
                    (context as ChildActivity).getNavController().navigate(R.id.proceedFeePayment,bundle)
                }else {
                    startNewActivity(
                        it, ChildActivity::class.java, bundle = bundle
                    )
                }
            }

        }

        override fun onShowSummary(item: FeesDetailModel) {
            Trace.i("on summary click: " + item)
            studentProfile?.let {
                /*val action = StudentFeeDetailsFragmentDirections.actionFeeDetailsToSummary()
                action.feeDetailModel = item
                action.studentProfile = studentProfile
                (context as ChildActivity).getNavController().navigate(action)*/
                context?.let { it1 ->
                    val bundle = bundleOf(
                        Pair(NAV_DESTINATION, R.id.feeDetailsSummary),
                        Pair(NAV_OBJECT, studentProfile),
                        Pair(NAV_OBJECT2, item)
                    )
                    if(it1 is ChildActivity){
                        (context as ChildActivity).getNavController().navigate(R.id.feeDetailsSummary,bundle)
                    }else {
                        startNewActivity(
                            it1, ChildActivity::class.java, bundle = bundle
                        )
                    }
                }
            }
        }
    }

    private fun fetchStudentFeeDetails(studentProfileId: Int) {
        lifecycleScope.launchWhenResumed {
            Preference.instance.putBoolean(DYNAMIC_HEADERS, false)
            feePaymentsViewModel.fetchStudentFeeDetails(studentProfileId)
                .collectLatest { feeAction ->
                    when (feeAction) {
                        is StudentFeeDetailsAction.Success -> {
                            Trace.e("Student fee details: " + feeAction.feeList)
                            feeDetailsAdapter.list = feeAction.feeList
                            binding.hasFeeDetails = feeAction.feeList.isNotEmpty()
                        }
                        is StudentFeeDetailsAction.Fail -> {
                            Trace.i(
                                feeAction.errorResponse.message ?: getString(
                                    R.string.something_went_wrong_try_again
                                )
                            )
                            binding.hasFeeDetails = false
                        }
                    }
                }
        }
    }

}