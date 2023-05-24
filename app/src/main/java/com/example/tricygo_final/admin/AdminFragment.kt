package com.example.tricygo_final.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.tricygo_final.R
import com.example.tricygo_final.admin.tabs.HomeFragment
import com.example.tricygo_final.admin.tabs.OrdersFragment
import com.example.tricygo_final.admin.tabs.ProfileFragment
import com.example.tricygo_final.admin.tabs.SearchFragment
import com.example.tricygo_final.databinding.FragmentAdminBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class AdminFragment : Fragment() {
    private lateinit var binding : FragmentAdminBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
        val homeFragment: Fragment = HomeFragment()
        val searchFragment : Fragment = SearchFragment()
        val ordersFragment: Fragment = OrdersFragment()
        val profileFragment: Fragment = ProfileFragment()


        val bottomNavigationView: BottomNavigationView = binding.bottomNavigation
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            val selectedFragment: Fragment = when (item.itemId) {
                R.id.navigation_home -> homeFragment
                R.id.navigation_search -> searchFragment
                R.id.navigation_orders -> ordersFragment
                R.id.navigation_profile -> profileFragment

                else -> return@setOnNavigationItemSelectedListener false
            }
            fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, selectedFragment)
                .commit()
            true
        }
        // Initially load the HomeFragment
        fragmentManager.beginTransaction()
            .replace(R.id.fragment_container, homeFragment)
            .commit()
    }


}