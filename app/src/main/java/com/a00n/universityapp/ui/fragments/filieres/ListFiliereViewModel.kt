package com.a00n.universityapp.ui.fragments.filieres

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.a00n.universityapp.data.entities.Filiere
import com.a00n.universityapp.data.remote.MyRequestQueue
import com.a00n.universityapp.utils.Constants
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject

class ListFiliereViewModel(private val application: Application) : AndroidViewModel(application) {
    private val filieresList = MutableLiveData<List<Filiere>?>()
    val filiereAdded = MutableLiveData<Boolean>()
    val filiereUpdated = MutableLiveData<Boolean>()
    val gson = Gson()


    fun getFilieresList(): MutableLiveData<List<Filiere>?> {
        return filieresList
    }


    fun addFiliere(filiere: Filiere) {
        val url: String = Constants.ADD_FILIERE_URL
        val jsonFiliere = gson.toJson(filiere)
        val request = JsonObjectRequest(
            Request.Method.POST, url, JSONObject(jsonFiliere),
            { response ->
                filiereAdded.value = true
                Log.i("info", "addFiliere: ${response.toString()}")
                fetchFilieres()
            },
            {
                filiereAdded.value = false
                Log.i("info", "Error: ${it.message}")
            }
        )
        MyRequestQueue.getInstance(application.applicationContext).addToRequestQueue(request)
    }

    fun deleteFiliere(filiere: Filiere) {
        val url: String = "${Constants.DELETE_FILIERE_URL}/${filiere.id}"
        val stringReq = StringRequest(
            Request.Method.DELETE, url,
            { response ->
                Log.i("info", "deleteFiliere: ${response.toString()}")
            },
            {
                Log.i("info", "Error: ${it.message}")
            }
        )
        MyRequestQueue.getInstance(application.applicationContext).addToRequestQueue(stringReq)
    }

    fun fetchFilieres() {
        val url: String = Constants.ALL_FILIERES_URL

        val stringReq = StringRequest(
            Request.Method.GET, url,
            { response ->
                val filieres: List<Filiere> = Gson().fromJson(
                    response.toString(),
                    object : TypeToken<List<Filiere>>() {}.type
                )
                filieresList.value = filieres
            },
            {
                filieresList.value = null
                Log.i("info", "getUsers: ${it.message}")
            }
        )
        MyRequestQueue.getInstance(application.applicationContext).addToRequestQueue(stringReq)
    }

    fun updateFiliere(filiere: Filiere) {
        val url: String = "${Constants.UPDATE_FILIERE_URL}/${filiere.id}"
        val jsonFiliere = gson.toJson(filiere)
        val request = JsonObjectRequest(
            Request.Method.PUT, url, JSONObject(jsonFiliere),
            { response ->
                filiereUpdated.value = true
                Log.i("info", "deleteFiliere: ${response.toString()}")
                fetchFilieres()
            },
            {
                filiereUpdated.value = false
                Log.i("info", "Error: ${it.message}")
            }
        )
        MyRequestQueue.getInstance(application.applicationContext).addToRequestQueue(request)
    }
}