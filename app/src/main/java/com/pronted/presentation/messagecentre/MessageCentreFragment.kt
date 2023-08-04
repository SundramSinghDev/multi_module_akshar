package com.pronted.presentation.messagecentre

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.base.BaseActivity
import com.base.BaseFragment
import com.base.inflateFragment
import com.pronted.R
import com.pronted.databinding.FragmentMessageCenterBinding

class MessageCentreFragment : BaseFragment<FragmentMessageCenterBinding>() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = inflateFragment(
            inflater, container, R.layout.fragment_message_center
        ) as FragmentMessageCenterBinding
        return binding.root
    }
    override fun init() {
        binding.run {
            tvTitle.text = getString(R.string.message_centre)
        }
    }
    override fun resume() {
        (activity as BaseActivity<*>).title(getString(R.string.message_centre))
    }
}