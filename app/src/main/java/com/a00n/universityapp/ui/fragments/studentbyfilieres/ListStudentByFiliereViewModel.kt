package com.a00n.universityapp.ui.fragments.studentbyfilieres

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.a00n.universityapp.data.entities.Filiere

class ListStudentByFiliereViewModel(private val application: Application) :
    AndroidViewModel(application) {

    var selectedFiliere: MutableLiveData<Filiere?> = MutableLiveData(null)

}