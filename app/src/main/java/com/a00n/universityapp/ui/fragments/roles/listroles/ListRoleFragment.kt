package com.a00n.universityapp.ui.fragments.roles.listroles

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
import com.a00n.universityapp.data.entities.Role
import com.a00n.universityapp.databinding.FragmentListRoleBinding
import com.a00n.universityapp.ui.adapters.RoleListAdapter
import com.a00n.universityapp.utils.SwipeGesture
import com.google.android.material.snackbar.Snackbar

class ListRoleFragment : Fragment() {

    private var _binding: FragmentListRoleBinding? = null
    private val binding get() = _binding!!
    private val adapter: RoleListAdapter by lazy {
        RoleListAdapter(onClickListener = { _, role ->
            Log.i(
                "info",
                "hello: $role"
            )
            val popup =
                LayoutInflater.from(context).inflate(R.layout.role_save_edit, null, false)
            val roleName = popup.findViewById<EditText>(R.id.roleNameEditText)
            roleName.setText(role.name)
            val dialog = AlertDialog.Builder(context)
                .setTitle("Update a Role")
                .setView(popup)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Update") { _, _ ->
                    val role = Role(role.id, roleName.text.toString())
                    Log.i("info", "bind: $role")
                    Snackbar.make(
                        (context as Activity).findViewById(android.R.id.content),
                        role.name,
                        Snackbar.LENGTH_LONG
                    ).show()
                    viewModel.updateRole(role)
                }
                .create()
            dialog.show()
        })
    }

    private lateinit var viewModel: ListRoleViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListRoleBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[ListRoleViewModel::class.java]
        binding.roleRecycleView.adapter = adapter
        toggleViews(false)
        binding.emptyListLinearLayout.visibility = View.GONE
        showShimmer()
        binding.fab.setOnClickListener {
            val popup =
                LayoutInflater.from(requireContext()).inflate(R.layout.role_save_edit, null, false)
//            val popupBinding = RoleSaveEditBinding.inflate(LayoutInflater.from(requireContext()),R.layout.role_save_edit,this,false)
            val roleName = popup.findViewById<EditText>(R.id.roleNameEditText)
            val dialog = AlertDialog.Builder(requireContext())
                .setTitle("Add a Role")
                .setView(popup)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Add") { _, _ ->
                    Log.i("info", "onCreateView: ${roleName.text.toString()}")
                    val role = Role(0, roleName.text.toString())
                    viewModel.addRole(role)
                }
                .create()
            dialog.show()
        }
        viewModel.roleAdded.observe(viewLifecycleOwner) {
            it.let { added ->
                var msg = "Error adding a new role"
                if (added) {
                    msg = "Role Added Successfully"
                }
                Snackbar.make(binding.root, msg, Snackbar.LENGTH_LONG)
                    .show()
            }
        }
        viewModel.roleUpdated.observe(viewLifecycleOwner) {
            it.let { added ->
                var msg = "Error updating a new role"
                if (added) {
                    msg = "Role Updated Successfully"
                }
                Snackbar.make(binding.root, msg, Snackbar.LENGTH_LONG)
                    .show()
            }
        }
        viewModel.getRolesList().observe(viewLifecycleOwner) { roles ->
            Handler(Looper.getMainLooper()).postDelayed({
                hideShimmer()
                if (roles != null) {
                    Log.i("info", "onCreateView: $roles")
                    if (roles.isEmpty()) {
                        toggleViews(true)
                    } else {
                        toggleViews(false)
                    }
                    adapter.submitList(roles)
                } else {
                    adapter.submitList(null)
                    toggleViews(true)
                    Log.i("info", "onCreateView: Error has occured")
                }
            }, 1000)
        }
        viewModel.fetchRoles()
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
                                viewModel.deleteRole(diff[0])
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
        itemTouchHelper.attachToRecyclerView(binding.roleRecycleView)
    }

    private fun toggleViews(visible: Boolean) {
        binding.emptyListLinearLayout.visibility = if (visible) View.VISIBLE else View.GONE
    }


    private fun showShimmer() = binding.roleRecycleView.showShimmer()
    private fun hideShimmer() = binding.roleRecycleView.hideShimmer()


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}