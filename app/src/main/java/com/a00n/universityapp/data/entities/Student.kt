package com.a00n.universityapp.data.entities

class Student(
    id: Int,
    login: String,
    password: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val filiere: Filiere
) : User(id, login, password){
    override fun toString(): String {
        return "$firstName $lastName"
    }
}
