package com.example.tricygo_final.admin.tabs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.example.tricygo_final.R
import com.example.tricygo_final.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding
    private var clicked = false

    private val rotateOpen : Animation by lazy { AnimationUtils.loadAnimation(this.requireContext(),R.anim.rotate_open) }
    private val rotateclose : Animation by lazy { AnimationUtils.loadAnimation(this.requireContext(),R.anim.rotate_close) }
    private val fromBottom : Animation by lazy { AnimationUtils.loadAnimation(this.requireContext(),R.anim.from_bottom_animation) }
    private val toBottom : Animation by lazy { AnimationUtils.loadAnimation(this.requireContext(),R.anim.to_bottom_anim) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomSheetDialog = MyBottomSheetDialogFragment()
        val addTradeIn = AddTradeIn()
        val addOns = AddOns()
        binding.btnAddTricycle.setOnClickListener {
            bottomSheetDialog.show(requireActivity().supportFragmentManager, "MyBottomSheetDialog")
        }

        binding.btnAddOns.setOnClickListener {
            addOns.show(requireActivity().supportFragmentManager, "MyBottomSheetDialog")
        }
        binding.btnAddTradeIn.setOnClickListener {
            addTradeIn.show(requireActivity().supportFragmentManager, "MyBottomSheetDialog")
        }
        binding.fabAdd.setOnClickListener{
            btnsClicked()
        }
    }

    private fun btnsClicked() {
        setVisibilty(clicked)
        setAnimation(clicked)
        setClickable(clicked)
        clicked = !clicked
    }

    private fun setAnimation(clicked:Boolean) {
        if (!clicked){
            binding.btnAddTricycle.visibility = View.VISIBLE
            binding.btnAddOns.visibility = View.VISIBLE
            binding.btnAddTradeIn.visibility = View.VISIBLE

        }else{
            binding.btnAddTricycle.visibility = View.INVISIBLE
            binding.btnAddOns.visibility = View.INVISIBLE
            binding.btnAddTradeIn.visibility = View.INVISIBLE
        }
    }

    private fun setVisibilty(clicked:Boolean) {
        if (!clicked){
            binding.btnAddTricycle.startAnimation(fromBottom)
            binding.btnAddOns.startAnimation(fromBottom)
            binding.btnAddTradeIn.startAnimation(fromBottom)
            binding.fabAdd.startAnimation(rotateOpen)
        }else{
            binding.btnAddTricycle.startAnimation(toBottom)
            binding.btnAddOns.startAnimation(toBottom)
            binding.btnAddTradeIn.startAnimation(toBottom)
            binding.fabAdd.startAnimation(rotateclose)
        }
    }
    private fun setClickable(clicked: Boolean) {
        binding.btnAddTricycle.isClickable = !clicked
        binding.btnAddOns.isClickable = !clicked
        binding.btnAddTradeIn.isClickable = !clicked
    }


}