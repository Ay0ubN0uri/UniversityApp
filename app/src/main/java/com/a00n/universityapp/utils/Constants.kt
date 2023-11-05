package com.a00n.universityapp.utils

class Constants {
    companion object {
        private const val URL: String = "http://192.168.43.106:8080/api/v1"
        const val ALL_ROLES_URL: String = "$URL/roles"
        const val DELETE_ROLE_URL: String = "$URL/roles"
        const val ADD_ROLE_URL: String = "$URL/roles"
        const val UPDATE_ROLE_URL: String = "$URL/roles"

        const val ALL_FILIERES_URL: String = "$URL/filieres"
        const val DELETE_FILIERE_URL: String = "$URL/filieres"
        const val ADD_FILIERE_URL: String = "$URL/filieres"
        const val UPDATE_FILIERE_URL: String = "$URL/filieres"


        const val ALL_STUDENTS_URL: String = "$URL/students"
        const val DELETE_STUDENT_URL: String = "$URL/students"
        const val ADD_STUDENT_URL: String = "$URL/students"
        const val UPDATE_STUDENT_URL: String = "$URL/students"
        const val ASSIGN_ROLES_URL: String = "$URL/students"
    }
}