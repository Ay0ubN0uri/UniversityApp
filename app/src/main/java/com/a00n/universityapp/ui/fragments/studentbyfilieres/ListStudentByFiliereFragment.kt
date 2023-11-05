package com.a00n.universityapp.ui.fragments.studentbyfilieres

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.a00n.universityapp.R
import com.a00n.universityapp.data.entities.Filiere
import com.a00n.universityapp.data.entities.Student
import com.a00n.universityapp.databinding.FragmentListStudentByFiliereBinding
import com.a00n.universityapp.ui.adapters.FiliereDropdownAdapter
import com.a00n.universityapp.ui.adapters.StudentListAdapter
import com.a00n.universityapp.ui.fragments.filieres.ListFiliereViewModel
import com.a00n.universityapp.ui.fragments.students.ListStudentViewModel

class ListStudentByFiliereFragment : Fragment() {

    private lateinit var viewModel: ListStudentByFiliereViewModel
    private lateinit var filiereViewModel: ListFiliereViewModel
    private lateinit var studentViewModel: ListStudentViewModel
    private var _binding: FragmentListStudentByFiliereBinding? = null
    private val binding get() = _binding!!

    private val adapter: StudentListAdapter by lazy {
        StudentListAdapter(onClickListener = { _, _ -> })
    }

    private lateinit var filiereAdapter: FiliereDropdownAdapter
    private lateinit var studentsList: List<Student>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListStudentByFiliereBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[ListStudentByFiliereViewModel::class.java]
        filiereViewModel = ViewModelProvider(this)[ListFiliereViewModel::class.java]
        studentViewModel = ViewModelProvider(this)[ListStudentViewModel::class.java]
        binding.studentByFiliereRecycleView.adapter = adapter
        toggleViews(false)
        binding.emptyListLinearLayout.visibility = View.GONE
        showShimmer()
        filiereAdapter = FiliereDropdownAdapter(mutableListOf())
        filiereViewModel.getFilieresList().observe(viewLifecycleOwner) { filieres ->
            if (filieres != null) {
                val filiere = Filiere(0, "All Filieres", "All Filieres")
                val allFilieres = mutableListOf<Filiere>()
                allFilieres.add(filiere)
                allFilieres.addAll(filieres)
                filiereAdapter.setData(allFilieres)
            } else {
                Log.i("info", "onCreateView: Error has occured")
            }
        }
        filiereViewModel.fetchFilieres()
        studentViewModel.getStudentsList().observe(viewLifecycleOwner) { students ->
            Handler(Looper.getMainLooper()).postDelayed({
                hideShimmer()
                if (students != null) {
                    studentsList = students
                    adapter.submitList(students)
                } else {
                    Log.i("info", "onCreateView: Error has occured")
                }
            }, 1000)
        }
        studentViewModel.fetchStudents()
        viewModel.selectedFiliere.observe(viewLifecycleOwner) { filiere ->
            if (filiere != null) {
                if (filiere.id == 0) {
                    adapter.submitList(studentsList)
                } else {
                    val filtred = studentsList.filter { student ->
                        student.filiere == filiere
                    }
                    adapter.submitList(filtred)
                }
            }
        }
        binding.studentByFiliereAutoCompleteTextView.setAdapter(filiereAdapter)
        binding.studentByFiliereAutoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            viewModel.selectedFiliere.value =
                filiereAdapter.getItem(position) ?: return@setOnItemClickListener
        }
        return binding.root
    }


    private fun toggleViews(visible: Boolean) {
        binding.emptyListLinearLayout.visibility = if (visible) View.VISIBLE else View.GONE
    }


    private fun showShimmer() = binding.studentByFiliereRecycleView.showShimmer()
    private fun hideShimmer() = binding.studentByFiliereRecycleView.hideShimmer()

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}