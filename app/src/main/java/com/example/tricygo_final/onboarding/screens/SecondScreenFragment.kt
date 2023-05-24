package com.example.tricygo_final.onboarding.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.tricygo_final.R
import com.example.tricygo_final.databinding.FragmentSecondScreenBinding


class SecondScreenFragment : Fragment() {
    private lateinit var binding : FragmentSecondScreenBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSecondScreenBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewPager  = activity?.findViewById<ViewPager2>(R.id.viewPager)
        binding.button.setOnClickListener {
            viewPager?.currentItem = 2
        }
    }
}