package com.a00n.universityapp.ui.fragments.filieres

import android.app.Activity
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
import android.widget.EditText
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.a00n.universityapp.R
import com.a00n.universityapp.data.entities.Filiere
import com.a00n.universityapp.data.entities.Role
import com.a00n.universityapp.databinding.FragmentListFiliereBinding
import com.a00n.universityapp.ui.adapters.FiliereListAdapter
import com.a00n.universityapp.ui.adapters.RoleListAdapter
import com.a00n.universityapp.utils.SwipeGesture
import com.google.android.material.snackbar.Snackbar

class ListFiliereFragment : Fragment() {

    private lateinit var viewModel: ListFiliereViewModel
    private var _binding: FragmentListFiliereBinding? = null
    private val binding get() = _binding!!

    private val adapter: FiliereListAdapter by lazy {
        FiliereListAdapter(onClickListener = { _, filiere ->
            Log.i("info", "hello: $filiere")
            val popup =
                LayoutInflater.from(context).inflate(R.layout.filiere_save_edit, null, false)
            val filiereCode = popup.findViewById<EditText>(R.id.filiereCodeEditText)
            val filiereName = popup.findViewById<EditText>(R.id.filiereNameEditText)
            filiereCode.setText(filiere.code)
            filiereName.setText(filiere.name)
            val dialog = AlertDialog.Builder(context)
                .setTitle("Update a Filiere")
                .setView(popup)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Update") { _, _ ->
                    val filiere = Filiere(
                        filiere.id,
                        filiereCode.text.toString(),
                        filiereName.text.toString()
                    )
                    viewModel.updateFiliere(filiere)
                }
                .create()
            dialog.show()
        })
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListFiliereBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[ListFiliereViewModel::class.java]
        binding.filiereRecycleView.adapter = adapter
        toggleViews(false)
        binding.emptyListLinearLayout.visibility = View.GONE
        showShimmer()

        viewModel.filiereAdded.observe(viewLifecycleOwner) {
            it.let { added ->
                var msg = "Error adding a new filiere"
                if (added) {
                    msg = "Filiere Added Successfully"
                }
                Snackbar.make(binding.root, msg, Snackbar.LENGTH_LONG)
                    .show()
            }
        }

        viewModel.filiereUpdated.observe(viewLifecycleOwner) {
            it.let { added ->
                var msg = "Error updating a new filiere"
                if (added) {
                    msg = "Filiere Updated Successfully"
                }
                Snackbar.make(binding.root, msg, Snackbar.LENGTH_LONG)
                    .show()
            }
        }

        binding.fabFiliere.setOnClickListener {
            val popup =
                LayoutInflater.from(requireContext())
                    .inflate(R.layout.filiere_save_edit, null, false)
            val filiereCode = popup.findViewById<EditText>(R.id.filiereCodeEditText)
            val filiereName = popup.findViewById<EditText>(R.id.filiereNameEditText)
            val dialog = AlertDialog.Builder(requireContext())
                .setTitle("Add a Filiere")
                .setView(popup)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Add") { _, _ ->
                    val filiere =
                        Filiere(0, filiereCode.text.toString(), filiereName.text.toString())
                    viewModel.addFiliere(filiere)
                }
                .create()
            dialog.show()
        }
        viewModel.getFilieresList().observe(viewLifecycleOwner) { filieres ->
            Handler(Looper.getMainLooper()).postDelayed({
                hideShimmer()
                if (filieres != null) {
                    Log.i("info", "onCreateView: $filieres")
                    if (filieres.isEmpty()) {
                        toggleViews(true)
                    } else {
                        toggleViews(false)
                    }
                    adapter.submitList(filieres)
                } else {
                    adapter.submitList(null)
                    toggleViews(true)
                    Log.i("info", "onCreateView: Error has occured")
                }
            }, 1000)
        }
        viewModel.fetchFilieres()
        addSwipeDelete()
        return binding.root
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
                    "Role deleted successfully.",
                    Snackbar.LENGTH_LONG
                )
                snackbar.setAction("Undo") {
                    adapter.submitList(originalList)
//                    toggleViews(false)
                }
                snackbar.addCallback(object : Snackbar.Callback() {
                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                        super.onDismissed(transientBottomBar, event)
                        Log.i("info", "onDismissed: $event")
                        if (event == DISMISS_EVENT_TIMEOUT || event == DISMISS_EVENT_CONSECUTIVE) {
                            val diff = (originalList.toSet() subtract list.toSet()).toList()
                            if (diff.isNotEmpty()) {
                                viewModel.deleteFiliere(diff[0])
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
        itemTouchHelper.attachToRecyclerView(binding.filiereRecycleView)
    }


    private fun toggleViews(visible: Boolean) {
        binding.emptyListLinearLayout.visibility = if (visible) View.VISIBLE else View.GONE
    }


    private fun showShimmer() = binding.filiereRecycleView.showShimmer()
    private fun hideShimmer() = binding.filiereRecycleView.hideShimmer()

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}