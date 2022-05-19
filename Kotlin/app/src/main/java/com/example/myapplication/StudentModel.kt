package com.example.myapplication

import java.util.*

data class StudentModel(
     var id: Int = getAutoID(),
     var name: String = "",
     var email : String = ""
){
    companion object {
    fun getAutoID():Int{
        val random = Random()
        return random.nextInt(100)
        }
    }
}