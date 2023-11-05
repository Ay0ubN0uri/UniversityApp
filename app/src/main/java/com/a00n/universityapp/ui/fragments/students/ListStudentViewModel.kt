package com.a00n.universityapp.ui.fragments.students

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.a00n.universityapp.data.entities.Student
import com.a00n.universityapp.data.remote.MyRequestQueue
import com.a00n.universityapp.utils.Constants
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject

class ListStudentViewModel(private val application: Application) : AndroidViewModel(application) {
    private val studentsList = MutableLiveData<List<Student>?>()
    val studentAdded = MutableLiveData<Boolean>()
    val studentUpdated = MutableLiveData<Boolean>()
    val gson = Gson()


    fun getStudentsList(): MutableLiveData<List<Student>?> {
        return studentsList
    }


    fun addStudent(student: Student) {
        val url: String = Constants.ADD_STUDENT_URL
        val jsonStudent = gson.toJson(student)
        val request = JsonObjectRequest(
            Request.Method.POST, url, JSONObject(jsonStudent),
            { response ->
                studentAdded.value = true
                Log.i("info", "addStudent: ${response.toString()}")
                fetchStudents()
            },
            {
                studentAdded.value = false
                Log.i("info", "Error: ${it.message}")
            }
        )
        MyRequestQueue.getInstance(application.applicationContext).addToRequestQueue(request)
    }

    fun deleteStudent(student: Student) {
        val url: String = "${Constants.DELETE_STUDENT_URL}/${student.id}"
        val stringReq = StringRequest(
            Request.Method.DELETE, url,
            { response ->
                Log.i("info", "deleteStudent: ${response.toString()}")
            },
            {
                Log.i("info", "Error: ${it.message}")
            }
        )
        MyRequestQueue.getInstance(application.applicationContext).addToRequestQueue(stringReq)
    }

    fun fetchStudents() {
        val url: String = Constants.ALL_STUDENTS_URL

        val stringReq = StringRequest(
            Request.Method.GET, url,
            { response ->
                val students: List<Student> = Gson().fromJson(
                    response.toString(),
                    object : TypeToken<List<Student>>() {}.type
                )
                studentsList.value = students
            },
            {
                studentsList.value = null
                Log.i("info", "getUsers: ${it.message}")
            }
        )
        MyRequestQueue.getInstance(application.applicationContext).addToRequestQueue(stringReq)
    }

    fun updateStudent(student: Student) {
        val url: String = "${Constants.UPDATE_STUDENT_URL}/${student.id}"
        val jsonStudent = gson.toJson(student)
        val request = JsonObjectRequest(
            Request.Method.PUT, url, JSONObject(jsonStudent),
            { response ->
                studentUpdated.value = true
                Log.i("info", "deleteStudent: ${response.toString()}")
                fetchStudents()
            },
            {
                studentUpdated.value = false
                Log.i("info", "Error: ${it.message}")
            }
        )
        MyRequestQueue.getInstance(application.applicationContext).addToRequestQueue(request)
    }
}