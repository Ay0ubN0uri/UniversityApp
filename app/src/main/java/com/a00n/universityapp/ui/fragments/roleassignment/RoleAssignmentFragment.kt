package com.a00n.universityapp.ui.fragments.roleassignment

import android.opengl.Visibility
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.a00n.universityapp.R
import com.a00n.universityapp.data.entities.Filiere
import com.a00n.universityapp.data.entities.Role
import com.a00n.universityapp.data.entities.Student
import com.a00n.universityapp.databinding.FragmentRoleAssignmentBinding
import com.a00n.universityapp.ui.adapters.RoleMultiSelectDropdownAdapter
import com.a00n.universityapp.ui.adapters.StudentDropdownAdapter
import com.a00n.universityapp.ui.adapters.StudentListAdapter
import com.a00n.universityapp.ui.fragments.roles.listroles.ListRoleViewModel
import com.a00n.universityapp.ui.fragments.students.ListStudentViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar

class RoleAssignmentFragment : Fragment() {


    private lateinit var viewModel: RoleAssignmentViewModel
    private lateinit var studentViewModel: ListStudentViewModel
    private lateinit var roleViewModel: ListRoleViewModel
    private var _binding: FragmentRoleAssignmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var studentAdapter: StudentDropdownAdapter
    private lateinit var roleAdapter: RoleMultiSelectDropdownAdapter
    private val allRoles = arrayOf(
        Role(1, "admin"),
        Role(2, "user"),
        Role(3, "manager"),
    )
    private var selectedRoles = mutableSetOf<Role>()
    private var dropdownRoles = mutableListOf<Role>()// allRoles.toMutableList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRoleAssignmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[RoleAssignmentViewModel::class.java]
        roleViewModel = ViewModelProvider(this)[ListRoleViewModel::class.java]
        viewModel.selectedStudent.observe(viewLifecycleOwner) { it ->
            it?.let { student ->
                Log.i("info", "A00n: ${student.roles}")
                selectedRoles = student.roles.toMutableSet()
                selectedRoles.forEach { role ->
                    addChip(role)
                }
                roleAdapter.selectedItems = selectedRoles
            }
        }
        studentViewModel = ViewModelProvider(this)[ListStudentViewModel::class.java]
        roleViewModel.getRolesList().observe(viewLifecycleOwner) { roles ->
            if (roles != null) {
                Log.i("info", "onCreateView: $roles")
                dropdownRoles = roles.toMutableList()
                roleAdapter.setData(dropdownRoles.toMutableList())
                roleAdapter.setOriginalData(dropdownRoles.toMutableList())
            } else {
                Log.i("info", "onCreateView: Error has occured")
            }
        }
        roleViewModel.fetchRoles()
        roleAdapter = RoleMultiSelectDropdownAdapter(dropdownRoles)
        binding.roleAssignmentAutoCompleteTextView.setAdapter(roleAdapter)
        studentAdapter = StudentDropdownAdapter(mutableListOf())
        binding.roleAssignmentAutoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = roleAdapter.getItem(position) ?: return@setOnItemClickListener
            if (selectedRoles.contains(selectedItem)) {
                selectedRoles.remove(selectedItem)
                roleAdapter.selectedItems = selectedRoles
                removeChip(selectedItem)
            } else {
                selectedRoles.add(selectedItem)
                roleAdapter.selectedItems = selectedRoles
                addChip(selectedItem)
            }
            binding.roleAssignmentAutoCompleteTextView.setText("") // Clear the text when an item is selected
        }
        binding.roleAssignmentAutoCompleteTextView.setOnClickListener {
            binding.roleAssignmentAutoCompleteTextView.showDropDown()
        }
        binding.studentAutoCompleteTextView.setAdapter(studentAdapter)
        binding.studentAutoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            selectedRoles.forEach { role -> removeChip(role) }
            selectedRoles.clear()
            roleAdapter.selectedItems.clear()
            viewModel.selectedStudent.value =
                studentAdapter.getItem(position) ?: return@setOnItemClickListener
        }
        studentViewModel.getStudentsList().observe(viewLifecycleOwner) { students ->
            if (students != null) {
                Log.i("info", "onCreateView: $students")
                studentAdapter.setData(students.toMutableList())
            } else {
                Log.i("info", "onCreateView: Error has occured")
            }
        }
        studentViewModel.fetchStudents()
        viewModel.roleAssigned.observe(viewLifecycleOwner) {
            it.let { assigned ->
                var msg = "Error assigning a new role"
                if (assigned) {
                    msg = "Role Assigned Successfully"
                    Snackbar.make(binding.root, msg, Snackbar.LENGTH_LONG)
                        .setAction("View") {
                            findNavController().navigate(R.id.action_roleAssignmentFragment_to_listStudentFragment)
                        }
                        .show()
                } else {
                    Snackbar.make(binding.root, msg, Snackbar.LENGTH_LONG)
                        .show()
                }
                binding.animationView.cancelAnimation()
                binding.animationView.visibility = View.GONE
            }
        }
        binding.AssignBtn.setOnClickListener {
            Log.i("info", "setOnClickListener: ${viewModel.selectedStudent.value}")
            Log.i("info", "setOnClickListener: $selectedRoles")
            val student = viewModel.selectedStudent.value
            if (student == null) {
                Snackbar.make(binding.root, "Please select a student", Snackbar.LENGTH_LONG).show()
            } else {
                binding.animationView.visibility = View.VISIBLE
                binding.animationView.playAnimation()
                Handler(Looper.getMainLooper()).postDelayed({
                    viewModel.assignRolesToStudent(student, selectedRoles.toList())
                }, 2000)
            }
        }
        return binding.root
    }


    // Method to add a chip to the ChipGroup
    private fun addChip(role: Role) {
        val chip = Chip(requireContext())
        chip.text = role.name
        chip.isCloseIconVisible = true

        // Listen for click on the close icon of the chip
        chip.setOnCloseIconClickListener { removeItem(role) }

        binding.chipGroup.addView(chip) // Add the chip to the ChipGroup
    }

    // Method to remove an item from the selected items and update the UI
    private fun removeItem(role: Role) {
        selectedRoles.remove(role)
        roleAdapter.selectedItems = selectedRoles
        removeChip(role)
    }

    // Method to remove a chip from the ChipGroup based on the text
    private fun removeChip(role: Role) {
        for (i in 0 until binding.chipGroup.childCount) {
            val child: View = binding.chipGroup.getChildAt(i)
            if (child is Chip) {
                if (child.text.toString() == role.name) {
                    binding.chipGroup.removeView(child)
                    break
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}