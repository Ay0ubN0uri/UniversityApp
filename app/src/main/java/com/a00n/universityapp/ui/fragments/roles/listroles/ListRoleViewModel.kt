package com.a00n.universityapp.ui.fragments.roles.listroles

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.a00n.universityapp.data.entities.Role
import com.a00n.universityapp.data.remote.MyRequestQueue
import com.a00n.universityapp.utils.Constants
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject

class ListRoleViewModel(private val application: Application) : AndroidViewModel(application) {

    private val rolesList = MutableLiveData<List<Role>?>()
    val roleAdded = MutableLiveData<Boolean>()
    val roleUpdated = MutableLiveData<Boolean>()
    val gson = Gson()


    fun getRolesList(): MutableLiveData<List<Role>?> {
        return rolesList
    }


    fun addRole(role: Role) {
        val url: String = Constants.ADD_ROLE_URL
        val jsonRole = gson.toJson(role)
        val request = JsonObjectRequest(
            Request.Method.POST, url, JSONObject(jsonRole),
            { response ->
                roleAdded.value = true
                Log.i("info", "addRole: ${response.toString()}")
                fetchRoles()
            },
            {
                roleAdded.value = false
                Log.i("info", "Error: ${it.message}")
            }
        )
        MyRequestQueue.getInstance(application.applicationContext).addToRequestQueue(request)
    }

    fun deleteRole(role: Role) {
        val url: String = "${Constants.DELETE_ROLE_URL}/${role.id}"
        val stringReq = StringRequest(
            Request.Method.DELETE, url,
            { response ->
                Log.i("info", "deleteRole: ${response.toString()}")
            },
            {
                Log.i("info", "Error: ${it.message}")
            }
        )
        MyRequestQueue.getInstance(application.applicationContext).addToRequestQueue(stringReq)
    }

    fun fetchRoles() {
        val url: String = Constants.ALL_ROLES_URL

        val stringReq = StringRequest(
            Request.Method.GET, url,
            { response ->
                val roles: List<Role> = Gson().fromJson(
                    response.toString(),
                    object : TypeToken<List<Role>>() {}.type
                )
                rolesList.value = roles
            },
            {
                rolesList.value = null
                Log.i("info", "getUsers: ${it.message}")
            }
        )
        MyRequestQueue.getInstance(application.applicationContext).addToRequestQueue(stringReq)
    }

    fun updateRole(role: Role) {
        val url: String = "${Constants.UPDATE_ROLE_URL}/${role.id}"
        val jsonRole = gson.toJson(role)
        val request = JsonObjectRequest(
            Request.Method.PUT, url, JSONObject(jsonRole),
            { response ->
                roleUpdated.value = true
                Log.i("info", "deleteRole: ${response.toString()}")
                fetchRoles()
            },
            {
                roleUpdated.value = false
                Log.i("info", "Error: ${it.message}")
            }
        )
        MyRequestQueue.getInstance(application.applicationContext).addToRequestQueue(request)
    }
}