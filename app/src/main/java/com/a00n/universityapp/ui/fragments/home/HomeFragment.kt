package com.a00n.universityapp.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.a00n.universityapp.R
import com.a00n.universityapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.roleDashboardMaterialCardView.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_listRoleFragment)
        }
        binding.studentDashboardMaterialCardView.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_listStudentFragment)
        }
        binding.filiereDashboardMaterialCardView.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_listFiliereFragment)
        }
        binding.studentByFiliereDashbaordMaterialCardView.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_listStudentByFiliereFragment)
        }
        binding.roleAssignmentDashboardMaterialCardView.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_roleAssignmentFragment)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}