package com.a00n.universityapp.data.entities

open class User(
    val id: Int,
    val login: String,
    val password: String,
    var roles:List<Role>
){
    constructor(id: Int, login: String, password: String) : this(id, login, password, emptyList())
}
