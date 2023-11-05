package com.a00n.universityapp.ui.fragments.students

import android.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.a00n.universityapp.R
import com.a00n.universityapp.data.entities.Filiere
import com.a00n.universityapp.data.entities.Role
import com.a00n.universityapp.data.entities.Student
import com.a00n.universityapp.databinding.FragmentListStudentBinding
import com.a00n.universityapp.ui.adapters.FiliereDropdownAdapter
import com.a00n.universityapp.ui.adapters.RoleMultiSelectDropdownAdapter
import com.a00n.universityapp.ui.adapters.StudentListAdapter
import com.a00n.universityapp.ui.fragments.filieres.ListFiliereViewModel
import com.a00n.universityapp.ui.fragments.roles.listroles.ListRoleViewModel
import com.a00n.universityapp.utils.SwipeGesture
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar

class ListStudentFragment : Fragment() {


    private lateinit var viewModel: ListStudentViewModel
    private lateinit var roleViewModel: ListRoleViewModel
    private lateinit var filiereViewModel: ListFiliereViewModel
    private var _binding: FragmentListStudentBinding? = null
    private val binding get() = _binding!!

    private lateinit var roleAdapter: RoleMultiSelectDropdownAdapter
    private lateinit var filiereAdapter: FiliereDropdownAdapter
    private val allRoles = arrayOf(
        Role(1, "admin"),
        Role(2, "user"),
        Role(3, "manager"),
    )
    private var selectedRoles = mutableSetOf<Role>()
    private var dropdownRoles = mutableListOf<Role>()
    private var selectedFiliere: Filiere? = null
    private lateinit var autoCompleteTextView:AutoCompleteTextView
    private lateinit var filiereAutoCompleteTextView:AutoCompleteTextView
    private lateinit var chipGroup:ChipGroup
    private lateinit var studentLoginEditText:EditText
    private lateinit var studentPasswordEditText:EditText
    private lateinit var studentFirstNameEditText:EditText
    private lateinit var studentLastNameEditText:EditText
    private lateinit var studentPhoneNumberEditText:EditText


    private val adapter: StudentListAdapter by lazy {
        StudentListAdapter(onClickListener = { _, student ->
            Log.i("info", "hello: $student")
            selectedRoles = student.roles.toMutableSet()
            selectedFiliere = student.filiere
            val popup = LayoutInflater.from(requireContext())
                .inflate(R.layout.student_save_edit, null, false)
            autoCompleteTextView = popup.findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView)
            chipGroup = popup.findViewById<ChipGroup>(R.id.chipGroup)
            studentLoginEditText = popup.findViewById<EditText>(R.id.studentLoginEditText)
            studentPasswordEditText = popup.findViewById<EditText>(R.id.studentPasswordEditText)
            studentFirstNameEditText = popup.findViewById<EditText>(R.id.studentFirstNameEditText)
            studentLastNameEditText = popup.findViewById<EditText>(R.id.studentLastNameEditText)
            studentPhoneNumberEditText = popup.findViewById<EditText>(R.id.studentPhoneNumberEditText)
            filiereAutoCompleteTextView = popup.findViewById<AutoCompleteTextView>(R.id.filiereAutoCompleteTextView)
            studentLoginEditText.setText(student.login)
            studentPasswordEditText.setText(student.password)
            studentFirstNameEditText.setText(student.firstName)
            studentLastNameEditText.setText(student.lastName)
            studentPhoneNumberEditText.setText(student.phoneNumber)
            roleAdapter = RoleMultiSelectDropdownAdapter(dropdownRoles)
            selectedRoles.forEach { role->
                addChip(role)
            }
            roleAdapter.selectedItems = selectedRoles
            autoCompleteTextView.setText("")
            autoCompleteTextView.setAdapter(roleAdapter)
            filiereAutoCompleteTextView.setAdapter(filiereAdapter)
            filiereAutoCompleteTextView.setText(student.filiere.code,false)
            filiereAutoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
                selectedFiliere = filiereAdapter.getItem(position) ?: return@setOnItemClickListener
            }
            autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
                val selectedItem = roleAdapter.getItem(position) ?: return@setOnItemClickListener
                if (selectedRoles.contains(selectedItem)) {
                    selectedRoles.remove(selectedItem)
                    roleAdapter.selectedItems = selectedRoles
                    removeChip(selectedItem)
                } else {
                    selectedRoles.add(selectedItem)
                    roleAdapter.selectedItems = selectedRoles
                    addChip(selectedItem)
                    autoCompleteTextView.setText("") // Clear the text when an item is selected
                }
            }
            autoCompleteTextView.setOnClickListener {
                autoCompleteTextView.showDropDown()
            }
            val dialog = AlertDialog.Builder(requireContext())
                .setTitle("Update a Student")
                .setView(popup)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Update") { _, _ ->
                    if(selectedFiliere==null) return@setPositiveButton
                    else{
                        val student = Student(
                            student.id,
                            studentLoginEditText.text.toString(),
                            studentPasswordEditText.text.toString(),
                            studentFirstNameEditText.text.toString(),
                            studentLastNameEditText.text.toString(),
                            studentPhoneNumberEditText.text.toString(),
                            selectedFiliere!!
                        )
                        student.roles = selectedRoles.toList()
                        Log.i("info", "onCreateView: ${student.toString()}")
                        Log.i("info", "onCreateView: $selectedRoles")
                        viewModel.updateStudent(student)
                    }

                }
                .create()
            dialog.show()
        })
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListStudentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[ListStudentViewModel::class.java]
        roleViewModel = ViewModelProvider(this)[ListRoleViewModel::class.java]
        filiereViewModel = ViewModelProvider(this)[ListFiliereViewModel::class.java]
        filiereAdapter = FiliereDropdownAdapter(mutableListOf())
        roleViewModel.getRolesList().observe(viewLifecycleOwner) { roles ->
                if (roles != null) {
                    Log.i("info", "onCreateView: $roles")
                    dropdownRoles = roles.toMutableList()
                } else {
                    Log.i("info", "onCreateView: Error has occured")
                }
        }
        roleViewModel.fetchRoles()
        filiereViewModel.getFilieresList().observe(viewLifecycleOwner) { filieres ->
            if (filieres != null) {
                Log.i("info", "onCreateView: $filieres")
                filiereAdapter.setData(filieres.toMutableList())
            } else {
                Log.i("info", "onCreateView: Error has occured")
            }
        }
        filiereViewModel.fetchFilieres()
        binding.studentRecycleView.adapter = adapter
        toggleViews(false)
        binding.emptyListLinearLayout.visibility = View.GONE
        showShimmer()

        binding.fabStudent.setOnClickListener {
            selectedRoles.clear()
            selectedFiliere = null
            val popup = LayoutInflater.from(requireContext())
                .inflate(R.layout.student_save_edit, null, false)
            autoCompleteTextView = popup.findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView)
            chipGroup = popup.findViewById<ChipGroup>(R.id.chipGroup)
            studentLoginEditText = popup.findViewById<EditText>(R.id.studentLoginEditText)
            studentPasswordEditText = popup.findViewById<EditText>(R.id.studentPasswordEditText)
            studentFirstNameEditText = popup.findViewById<EditText>(R.id.studentFirstNameEditText)
            studentLastNameEditText = popup.findViewById<EditText>(R.id.studentLastNameEditText)
            studentPhoneNumberEditText = popup.findViewById<EditText>(R.id.studentPhoneNumberEditText)
            filiereAutoCompleteTextView = popup.findViewById<AutoCompleteTextView>(R.id.filiereAutoCompleteTextView)
            roleAdapter = RoleMultiSelectDropdownAdapter(dropdownRoles)
            autoCompleteTextView.setAdapter(roleAdapter)
            filiereAutoCompleteTextView.setAdapter(filiereAdapter)
            filiereAutoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
                selectedFiliere = filiereAdapter.getItem(position) ?: return@setOnItemClickListener
            }
            autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
                val selectedItem = roleAdapter.getItem(position) ?: return@setOnItemClickListener
                if (selectedRoles.contains(selectedItem)) {
                    selectedRoles.remove(selectedItem)
                    roleAdapter.selectedItems = selectedRoles
                    removeChip(selectedItem)
                } else {
                    selectedRoles.add(selectedItem)
                    roleAdapter.selectedItems = selectedRoles
                    addChip(selectedItem)
                    autoCompleteTextView.setText("") // Clear the text when an item is selected
                }
            }
            autoCompleteTextView.setOnClickListener {
                autoCompleteTextView.showDropDown()
            }

            val dialog = AlertDialog.Builder(requireContext())
                .setTitle("Add a Student")
                .setView(popup)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Add") { _, _ ->
                    if(selectedFiliere==null) return@setPositiveButton
                    else{
                        val student = Student(
                            0,
                            studentLoginEditText.text.toString(),
                            studentPasswordEditText.text.toString(),
                            studentFirstNameEditText.text.toString(),
                            studentLastNameEditText.text.toString(),
                            studentPhoneNumberEditText.text.toString(),
                            selectedFiliere!!
                        )
                        student.roles = selectedRoles.toList()
                        Log.i("info", "onCreateView: ${student.toString()}")
                        Log.i("info", "onCreateView: $selectedRoles")
                        viewModel.addStudent(student)
                    }

                }
                .create()
            dialog.show()
        }

        viewModel.getStudentsList().observe(viewLifecycleOwner) { students ->
            Handler(Looper.getMainLooper()).postDelayed({
                hideShimmer()
                if (students != null) {
                    Log.i("info", "onCreateView: $students")
                    if (students.isEmpty()) {
                        toggleViews(true)
                    } else {
                        toggleViews(false)
                    }
                    adapter.submitList(students)
                } else {
                    adapter.submitList(null)
                    toggleViews(true)
                    Log.i("info", "onCreateView: Error has occured")
                }
            }, 1000)
        }
        viewModel.fetchStudents()
        addSwipeDelete()
        return binding.root
    }

    // Method to add a chip to the ChipGroup
    private fun addChip(role: Role) {
        val chip = Chip(requireContext())
        chip.text = role.name//.toUpperCase()
        chip.isCloseIconVisible = true

        // Listen for click on the close icon of the chip
        chip.setOnCloseIconClickListener { removeItem(role) }

        chipGroup.addView(chip) // Add the chip to the ChipGroup
    }

    // Method to remove an item from the selected items and update the UI
    private fun removeItem(role: Role) {
        selectedRoles.remove(role)
        roleAdapter.selectedItems = selectedRoles
        removeChip(role)
    }

    // Method to remove a chip from the ChipGroup based on the text
    private fun removeChip(role: Role) {
        for (i in 0 until chipGroup.childCount) {
            val child: View = chipGroup.getChildAt(i)
            if (child is Chip) {
                if (child.text.toString() == role.name) {
                    chipGroup.removeView(child)
                    break
                }
            }
        }
    }


    private fun addSwipeDelete() {
        val swipeGesture = object : SwipeGesture(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val originalList = adapter.currentList
                val list = originalList.toMutableList()
                list.removeAt(viewHolder.adapterPosition)
                adapter.submitList(list)
                val snackbar = Snackbar.make(
                    binding.root,
                    "Student deleted successfully.",
                    Snackbar.LENGTH_LONG
                )
                snackbar.setAction("Undo") {
                    adapter.submitList(originalList)
                }
                snackbar.addCallback(object : Snackbar.Callback() {
                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                        super.onDismissed(transientBottomBar, event)
                        Log.i("info", "onDismissed: $event")
                        if (event == DISMISS_EVENT_TIMEOUT || event == DISMISS_EVENT_CONSECUTIVE) {
                            val diff = (originalList.toSet() subtract list.toSet()).toList()
                            if (diff.isNotEmpty()) {
                                viewModel.deleteStudent(diff[0])
                                Log.i("info", "deleting: ${diff[0]}")
                            }
                        }
                    }
                })
                if (list.isEmpty()) {
                    toggleViews(true)
                }
                snackbar.show()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeGesture)
        itemTouchHelper.attachToRecyclerView(binding.studentRecycleView)
    }


    private fun toggleViews(visible: Boolean) {
        binding.emptyListLinearLayout.visibility = if (visible) View.VISIBLE else View.GONE
    }


    private fun showShimmer() = binding.studentRecycleView.showShimmer()
    private fun hideShimmer() = binding.studentRecycleView.hideShimmer()

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}