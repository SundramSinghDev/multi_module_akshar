package com.pronted.presentation.studentmarks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.base.BaseActivity
import com.base.BaseFragment
import com.base.inflateFragment
import com.pronted.R
import com.pronted.databinding.FragmentStudentMarksBinding

class StudentMarksFragment : BaseFragment<FragmentStudentMarksBinding>() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = inflateFragment(
            inflater, container, R.layout.fragment_student_marks
        ) as FragmentStudentMarksBinding
        return binding.root
    }
    override fun init() {
        binding.run {
            tvTitle.text = getString(R.string.marks_report)
        }
    }
    override fun resume() {
        (activity as BaseActivity<*>).title(getString(R.string.marks_report))
    }
}