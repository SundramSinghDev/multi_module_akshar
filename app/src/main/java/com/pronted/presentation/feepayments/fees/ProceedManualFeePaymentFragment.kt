package com.pronted.presentation.feepayments.fees

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.Constraints
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.*
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.pronted.R
import com.pronted.databinding.DialogEmployeesBinding
import com.pronted.databinding.DialogMenuItemBinding
import com.pronted.databinding.FragmentManualFeePaymentBinding
import com.pronted.dto.employee.*
import com.pronted.dto.feepayments.BankAccount
import com.pronted.dto.feepayments.data.FeesDetailModel
import com.pronted.dto.feepayments.data.ManualFeePayment
import com.pronted.dto.feepayments.data.PaymentListRequest
import com.pronted.presentation.employee.EmployeeViewModel
import com.pronted.presentation.feepayments.FeePaymentsViewModel
import com.pronted.presentation.home.ChildActivity
import com.pronted.presentation.listener.ItemClickListener
import com.pronted.presentation.userapps.BankAccountAdapter
import com.pronted.presentation.employee.EmployeesDialogAdapter
import com.pronted.presentation.userapps.PaymentMethodAdapter
import com.pronted.utils.*
import com.pronted.utils.extentions.*
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class ProceedManualFeePaymentFragment : BaseFragment<FragmentManualFeePaymentBinding>() {
    private var feesDetailModel: FeesDetailModel? = null
    private val materialDateBuilder by lazy {
        MaterialDatePicker.Builder.datePicker()
    }
    private val employeeViewModel: EmployeeViewModel by viewModel()
    private val feePaymentsViewModel: FeePaymentsViewModel by viewModel()
    lateinit var employeesDialogAdapter: EmployeesDialogAdapter
    lateinit var paymentMethodAdapter: PaymentMethodAdapter
    lateinit var feeTemplateAdapter: FeeTemplateAdapter
    lateinit var bankAccountAdapter: BankAccountAdapter
    var employees = arrayListOf<Employee>()
    var employeesDepartments = arrayListOf<EmployeeDepartment>()
    var nameOfBanks = arrayListOf<BankAccount>()
    var feeTempList = arrayListOf<FeeTemp>()
    val manualPaymentModel: ManualPaymentModel by lazy {
        ManualPaymentModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = inflateFragment(
            inflater, container, R.layout.fragment_manual_fee_payment
        ) as FragmentManualFeePaymentBinding
        return binding.root
    }

    override fun resume() {
        (activity as BaseActivity<*>).title(getString(R.string.fee_amp_payment))
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun init() {
        feesDetailModel = if (Build.VERSION.SDK_INT >= 33) {
            arguments?.getSerializable(NAV_OBJECT, FeesDetailModel::class.java)
        } else {
            arguments?.getSerializable(NAV_OBJECT) as FeesDetailModel
        }

        binding.run {
            paymentModel = manualPaymentModel
            tvAcademicYear.text = feesDetailModel?.academicYear
            tvAmount.text = String.format(
                getString(R.string.rupee_symbol_format),
                feesDetailModel?.processingAmount
            )
            setDateTouchListener(paymentDate)
            manualPaymentModel.setPaymentDate(Calendar.getInstance().time.toDateString(DateUtil.y4M2d2))
            receivedBy.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_UP) {
                    if (employees.isNotEmpty())
                        showReceiverDialog()
                    else
                        fetchEmployees(true)
                }
                false
            }
            paymentMethod.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_UP) {
                    if (employeesDepartments.isNotEmpty())
                        showPaymentMethod()
                    else
                        fetchPaymentMethods(true)
                }
                false
            }
            bank.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_UP) {
                    if (nameOfBanks.isNotEmpty())
                        showBanksDialog()
                    else
                        fetchNameOfBanks(true)
                }
                false
            }

            feeReceipt.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_UP) {
                    if (feeTempList.isNotEmpty())
                        showFeeReceiptDialog()
                    else
                        fetchFeeTemplates(true)
                }
                false
            }
            savePayment.setOnClickListener {
                context?.let {
                    feesDetailModel?.let { feesDetailModel ->
                        if (manualPaymentModel.isValid(it)) {
                            val paymentList = arrayListOf<PaymentListRequest>()
                            for (data in feesDetailModel.feeHeadData) {
                                for (term in data.feeTerms) {
                                    if (term.isSelected) {
                                        paymentList.add(
                                            PaymentListRequest(
                                                term.studentFeeId,
                                                term.paidAmount,
                                                term.feeHead.headName,
                                                term.feeTerm.feeTerm
                                            )
                                        )
                                    }
                                }
                            }
                            savePaymentData(
                                manualPaymentModel.getPaymentRequest(
                                    feesDetailModel.studentId,
                                    paymentList
                                )
                            )
                        }
                    }
                }

            }

        }
    }

    /**
     * Add manual fee payments by employee
     */
    private fun savePaymentData(manualFeePayment: ManualFeePayment) {
        lifecycleScope.launchWhenResumed {
            showLoader()
            Preference.instance.putBoolean(DYNAMIC_HEADERS, false)
            feePaymentsViewModel.addManualFeePayment(manualFeePayment)
                .collectLatest { feeAction ->
                    dismissLoader()
                    when (feeAction) {
                        is ManualFeePaymentAction.Success -> {
                            Trace.e("Payment invoice URL: " + feeAction.response)
                            toast(getString(R.string.payment_save_successfully))
                            navigateInvoiceScreen(feeAction.response.invoice)
                        }
                        is ManualFeePaymentAction.Fail -> {
                            Trace.i(
                                feeAction.errorResponse.message ?: getString(
                                    R.string.something_went_wrong_try_again
                                )
                            )
                        }
                    }
                }
        }

    }

    private fun navigateInvoiceScreen(invoiceUrl: String) {
        context?.let {
            val bundle = bundleOf(
                Pair(NAV_DESTINATION, R.id.paymentInvoiceFragment),
                Pair(NAV_OBJECT, invoiceUrl)
            )
            startNewActivity(
                it, ChildActivity::class.java, bundle = bundle, finish = true
            )
            /*if (it is ChildActivity) {
                (context as ChildActivity).getNavController()
                    .navigate(R.id.paymentInvoiceFragment, bundle)
            } else {
                startNewActivity(
                    it, ChildActivity::class.java, bundle = bundle
                )
            }*/
        }
    }

    private lateinit var banksDialog: Dialog
    private fun showBanksDialog() {
        context?.let {
            val dialogBinding = DialogMenuItemBinding.inflate(layoutInflater)
            banksDialog = Dialog(it).apply {
                setContentView(dialogBinding.root)
                window?.setLayout(
                    Constraints.LayoutParams.MATCH_PARENT,
                    Constraints.LayoutParams.WRAP_CONTENT
                )
            }
            dialogBinding.run {
                imgCancel.setOnClickListener {
                    banksDialog.dismiss()
                }
                rvMenu.setHasFixedSize(true)
                rvMenu.layoutManager = LinearLayoutManager(
                    it,
                    LinearLayoutManager.VERTICAL, false
                )
                title.text = it.getString(R.string.select_bank_account)
                bankAccountAdapter = BankAccountAdapter(it, bankAccountClickListener)
                rvMenu.adapter = bankAccountAdapter
                bankAccountAdapter.list = nameOfBanks
                hasItems = nameOfBanks.isNotEmpty()
            }
            banksDialog.show()
        }
    }

    private lateinit var feeReceiptDialog: Dialog
    private fun showFeeReceiptDialog() {
        context?.let {
            val dialogBinding = DialogMenuItemBinding.inflate(layoutInflater)
            feeReceiptDialog = Dialog(it).apply {
                setContentView(dialogBinding.root)
                window?.setLayout(
                    Constraints.LayoutParams.MATCH_PARENT,
                    Constraints.LayoutParams.WRAP_CONTENT
                )
            }
            dialogBinding.run {
                imgCancel.setOnClickListener {
                    feeReceiptDialog.dismiss()
                }
                rvMenu.setHasFixedSize(true)
                rvMenu.layoutManager = LinearLayoutManager(
                    it,
                    LinearLayoutManager.VERTICAL, false
                )
                title.text = it.getString(R.string.select_fee_receipt_template)
                feeTemplateAdapter = FeeTemplateAdapter(it, feeTemplateClickListener)
                rvMenu.adapter = feeTemplateAdapter
                feeTemplateAdapter.list = feeTempList
                hasItems = feeTempList.isNotEmpty()
            }
            feeReceiptDialog.show()
        }
    }

    private lateinit var paymentMethodDialog: Dialog
    private fun showPaymentMethod() {
        context?.let {
            val dialogBinding = DialogMenuItemBinding.inflate(layoutInflater)
            paymentMethodDialog = Dialog(it).apply {
                setContentView(dialogBinding.root)
                window?.setLayout(
                    Constraints.LayoutParams.MATCH_PARENT,
                    Constraints.LayoutParams.WRAP_CONTENT
                )
            }
            dialogBinding.run {
                imgCancel.setOnClickListener {
                    paymentMethodDialog.dismiss()
                }
                rvMenu.setHasFixedSize(true)
                rvMenu.layoutManager = LinearLayoutManager(
                    it,
                    LinearLayoutManager.VERTICAL, false
                )
                title.text = it.getString(R.string.select_payment_method)
                paymentMethodAdapter = PaymentMethodAdapter(it, paymentMethodClickListener)
                rvMenu.adapter = paymentMethodAdapter
                paymentMethodAdapter.list = employeesDepartments
                hasItems = employeesDepartments.isNotEmpty()
            }
            paymentMethodDialog.show()
        }
    }


    private lateinit var employeesDialog: Dialog

    private fun showReceiverDialog() {
        context?.let {
            val dialogBinding = DialogEmployeesBinding.inflate(layoutInflater)
            employeesDialog = Dialog(it).apply {
                setContentView(dialogBinding.root)
                window?.setLayout(
                    Constraints.LayoutParams.MATCH_PARENT,
                    Constraints.LayoutParams.WRAP_CONTENT
                )
            }
            dialogBinding.run {
                imgCancel.setOnClickListener {
                    employeesDialog.dismiss()
                }
                rvEmployees.setHasFixedSize(true)
                rvEmployees.layoutManager = LinearLayoutManager(
                    it,
                    LinearLayoutManager.VERTICAL, false
                )

                employeesDialogAdapter = EmployeesDialogAdapter(it, employeeClickListener)
                rvEmployees.adapter = employeesDialogAdapter
                employeesDialogAdapter.list = employees
                hasEmployees = employees.isNotEmpty()

                etSearch.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(p0: Editable?) {

                    }

                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        if (p0.toString().isNotEmpty())
                            Handler().postDelayed({ filterEmployee(p0.toString()) }, 100)
                        else
                            employeesDialogAdapter.list = employees
                    }
                })
            }
            employeesDialog.show()
        }
    }

    private val bankAccountClickListener =
        object : ItemClickListener<BankAccount> {
            override fun onClicked(item: BankAccount, positoin: Int) {
                banksDialog.dismiss()
                manualPaymentModel.setBankAccount(item)
                binding.bank.setText(item.bankName)
            }
        }

    private val feeTemplateClickListener =
        object : ItemClickListener<FeeTemp> {
            override fun onClicked(item: FeeTemp, positoin: Int) {
                feeReceiptDialog.dismiss()
                binding.feeReceipt.setText(item.templateName)
            }
        }
    private val paymentMethodClickListener =
        object : ItemClickListener<EmployeeDepartment> {
            override fun onClicked(item: EmployeeDepartment, positoin: Int) {
                paymentMethodDialog.dismiss()
                binding.paymentMethod.setText(item.value)
                when (item.value) {
                    "Cash" -> {
                        binding.paymentRefNumLayout.visibility = View.GONE
                    }
                    "Cheque" -> {
                        binding.paymentRefNumLayout.hint = context?.getString(R.string.check_number)
                        binding.paymentRefNumLayout.visibility = View.VISIBLE

                    }
                    else -> {
                        binding.paymentRefNumLayout.hint =
                            context?.getString(R.string.transaction_id)
                        binding.paymentRefNumLayout.visibility = View.VISIBLE
                    }
                }
            }
        }

    private val employeeClickListener =
        object : ItemClickListener<Employee> {
            override fun onClicked(item: Employee, positoin: Int) {
                employeesDialog.dismiss()
                binding.receivedBy.setText(item.fullName)
            }
        }

    private fun filterEmployee(name: String) {
        val filteredEmployees = ArrayList<Employee>()
        for (i in employees.indices) {
            val searchData = employees[i].fullName
            val employeeId = employees[i].employeeId
            val employeeDepartment = employees[i].department
            if (searchData.lowercase().contains(name.lowercase()) || employeeId.lowercase()
                    .contains(name.lowercase())
                || employeeDepartment.lowercase().contains(name.lowercase())
            ) {
                filteredEmployees.add(employees[i])
            }
        }
        //        view.rvDialList.adapter = adapterDialList
        employeesDialogAdapter.list = filteredEmployees
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun setDateTouchListener(v: TextInputEditText) {
        v.setOnTouchListener { view, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                showDatePicker()
            }
            false
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()

        val date = binding.paymentDate.text.toString()
        date.convertToDate(DateUtil.y4M2d2)?.let {
            calendar.time = it.setTo2359()
        }


        materialDateBuilder.setSelection(calendar.timeInMillis)
        val materialDatePicker = materialDateBuilder.build()
        materialDatePicker.addOnPositiveButtonClickListener {
            binding.paymentDate.setText(
                DateUtil.y4M2d2.format(
                    it.toDate(
                        convert = true
                    )
                )
            )
        }
        materialDatePicker.show(parentFragmentManager, "MATERIAL_DATE_PICKER")
    }


    private fun fetchPaymentMethods(showDialog: Boolean = false) {
        lifecycleScope.launchWhenResumed {
            employeeViewModel.fetchPaymentMethods("PAYMENT_METHOD")
                .collectLatest { empAction ->
                    when (empAction) {
                        is EmployeeDepartmentAction.Success -> {
                            Trace.e("Employees departments: " + empAction.response)
                            employeesDepartments = empAction.response
                            if (showDialog)
                                showPaymentMethod()
                        }
                        is EmployeeDepartmentAction.Fail -> {
                            Trace.i(
                                empAction.errorResponse.message ?: getString(
                                    R.string.something_went_wrong_try_again
                                )
                            )
                            employeesDepartments = arrayListOf()
                            if (showDialog)
                                showPaymentMethod()
                        }
                    }
                }
        }
    }

    private fun fetchNameOfBanks(showDialog: Boolean) {
        lifecycleScope.launchWhenResumed {
            feePaymentsViewModel.fetchBanks()
                .collectLatest { banksAction ->
                    when (banksAction) {
                        is BanksAction.Success -> {
                            Trace.e("Banks details: " + banksAction.response)
                            nameOfBanks = banksAction.response
                            if (showDialog)
                                showBanksDialog()
                        }
                        is BanksAction.Fail -> {
                            Trace.i(
                                banksAction.errorResponse.message ?: getString(
                                    R.string.something_went_wrong_try_again
                                )
                            )
                            nameOfBanks = arrayListOf()
                            if (showDialog)
                                showBanksDialog()
                        }
                    }
                }
        }
    }

    private fun fetchFeeTemplates(showDialog: Boolean) {
        lifecycleScope.launchWhenResumed {
            feePaymentsViewModel.fetchFeeReceiptTemplates()
                .collectLatest { feeAction ->
                    when (feeAction) {
                        is FeeReceiptTempAction.Success -> {
                            Trace.e("Banks details: " + feeAction.response)
                            feeTempList = feeAction.response
                            if (showDialog)
                                showFeeReceiptDialog()
                        }
                        is FeeReceiptTempAction.Fail -> {
                            Trace.i(
                                feeAction.errorResponse.message ?: getString(
                                    R.string.something_went_wrong_try_again
                                )
                            )
                            feeTempList = arrayListOf()
                            if (showDialog)
                                showFeeReceiptDialog()
                        }
                    }
                }
        }
    }

    private fun fetchEmployees(showDialog: Boolean) {
        lifecycleScope.launchWhenResumed {
            employeeViewModel.fetchEmployees()
                .collectLatest { empAction ->
                    when (empAction) {
                        is EmployeesAction.Success -> {
                            Trace.e("Employees details: " + empAction.response)
                            employees = empAction.response
                            if (showDialog)
                                showReceiverDialog()
                        }
                        is EmployeesAction.Fail -> {
                            Trace.i(
                                empAction.errorResponse.message ?: getString(
                                    R.string.something_went_wrong_try_again
                                )
                            )
                            employees = arrayListOf()
                            if (showDialog)
                                showReceiverDialog()
                        }
                    }
                }
        }
    }

}