package com.a00n.universityapp.data.entities

data class Filiere(
    val id:Int,
    val code:String,
    val name:String
){
    override fun toString(): String {
        return this.code
    }
}
