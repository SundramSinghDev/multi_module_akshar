package com.pronted.presentation.employee

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.BaseActivity
import com.base.BaseFragment
import com.base.Trace
import com.base.inflateFragment
import com.pronted.R
import com.pronted.databinding.FragmentEmployeesBinding
import com.pronted.dto.employee.Employee
import com.pronted.dto.employee.EmployeesAction
import com.pronted.presentation.home.ChildActivity
import com.pronted.presentation.listener.ItemClickListener
import com.pronted.utils.extentions.NAV_DESTINATION
import com.pronted.utils.extentions.NAV_OBJECT
import com.pronted.utils.extentions.NAV_OBJECT2
import com.pronted.utils.extentions.startNewActivity
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel

class EmployeesFragment : BaseFragment<FragmentEmployeesBinding>() {

    private lateinit var employeeAdapter: EmployeeAdapter
    private val employeeViewModel: EmployeeViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = inflateFragment(
            inflater, container, R.layout.fragment_employees
        ) as FragmentEmployeesBinding
        return binding.root
    }

    override fun init() {
        binding.run {
            context?.let {
                rvEmployees.setHasFixedSize(true)
                rvEmployees.layoutManager = LinearLayoutManager(
                    it,
                    LinearLayoutManager.VERTICAL,
                    false
                )
                employeeAdapter =
                    EmployeeAdapter(
                        it,
                        this@EmployeesFragment,
                        employeeItemClickListener
                    )
                rvEmployees.adapter = employeeAdapter
                fetchEmployees()
            }
        }
    }

    override fun resume() {
        (activity as BaseActivity<*>).title(getString(R.string.employees))
    }

    private val employeeItemClickListener = object : ItemClickListener<Employee> {
        @SuppressLint("NotifyDataSetChanged")
        override fun onClicked(item: Employee, positoin: Int) {
            Trace.i("Selected student: $item")
            context?.let {
                startNewActivity(
                    it, ChildActivity::class.java, bundle = bundleOf(
                        Pair(NAV_DESTINATION, R.id.employeeProfileFragment),
                        Pair(NAV_OBJECT, employeeAdapter.list),
                        Pair(NAV_OBJECT2, positoin)
                    )
                )
            }
        }
    }

    private fun fetchEmployees() {
        lifecycleScope.launchWhenResumed {
            showLoader()
            employeeViewModel.fetchEmployees()
                .collectLatest { empAction ->
                    dismissLoader()
                    when (empAction) {
                        is EmployeesAction.Success -> {
                            Trace.e("Employees details: " + empAction.response)
                            employeeAdapter.list = empAction.response
                            binding.hasEmployees = empAction.response.isNotEmpty()
                        }
                        is EmployeesAction.Fail -> {
                            Trace.i(
                                empAction.errorResponse.message ?: getString(
                                    R.string.something_went_wrong_try_again
                                )
                            )
                            employeeAdapter.list = arrayListOf()
                            binding.hasEmployees = false
                        }
                    }
                }
        }
    }

}