package com.a00n.universityapp.ui.adapters

import android.util.Log
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.a00n.universityapp.data.entities.Role


@BindingAdapter("addRolesList")
fun TextView.addRolesList(roles: List<Role>) {
//    Log.i("info", "addRolesList: $roles")
    if(roles.isEmpty()){
        this.text = ""
    }
    else{
        this.text = roles.joinToString(", ") {
            it.name
        }
    }

}