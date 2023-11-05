package com.a00n.universityapp.ui.fragments.roleassignment

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.a00n.universityapp.data.entities.Role
import com.a00n.universityapp.data.entities.Student
import com.a00n.universityapp.data.remote.MyRequestQueue
import com.a00n.universityapp.utils.Constants
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject

class RoleAssignmentViewModel(private val application: Application) :
    AndroidViewModel(application) {
    var selectedStudent: MutableLiveData<Student?> = MutableLiveData(null)
    private val gson = Gson()
    val roleAssigned = MutableLiveData<Boolean>()


    fun assignRolesToStudent(student: Student, roles: List<Role>) {
        val url: String = "${Constants.ASSIGN_ROLES_URL}/${student.id}/assign"
        val jsonRoles = gson.toJson(roles)

        val request = JsonArrayRequest(
            Request.Method.POST, url, JSONArray(jsonRoles),
            { response ->
                roleAssigned.value = true
                Log.i("info", "addStudent: ${response.getJSONObject(0)}")
            },
            {
                roleAssigned.value = false
                Log.i("info", "Error: ${it.message}")
            }
        )
        MyRequestQueue.getInstance(application.applicationContext).addToRequestQueue(request)
    }
}