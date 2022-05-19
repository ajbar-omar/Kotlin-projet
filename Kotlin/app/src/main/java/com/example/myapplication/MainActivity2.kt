package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity2 : AppCompatActivity() {
    private lateinit var edName: EditText
    private lateinit var edEmail: EditText
    private lateinit var btnAdd: Button
    private lateinit var btnView: Button
    private lateinit var btnUpdate: Button

    private lateinit var sqliteHelper: SQLiteHelper
    private lateinit var recyclerView: RecyclerView
    private var adapter: StudentAdapter? = null
    private var std: StudentModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        initView()
        initRecyclerVew()
        sqliteHelper = SQLiteHelper(this)

        btnAdd.setOnClickListener {addStudent() }
        btnView.setOnClickListener {getStudents() }
        btnUpdate.setOnClickListener {updateStudent() }
        //Ok now we need to delete record.

        adapter?.setOnClickItem {
            Toast.makeText(this, it.name, Toast.LENGTH_SHORT).show()
            // OK now I need To update record.
            edName.setText(it.name)
            edEmail.setText(it.email)
            std = it
        }
        adapter?.setOnClickDeleteItem {
            deleteStudent(it.id)
        }
    }

    private fun getStudents() {
        val stdList = sqliteHelper.getAllStudent()
        Log.e("pppp","${stdList.size}")

        // Now I need to display data in RecyclerView

        adapter?.addItems(stdList)
    }

    private fun addStudent() {
        val name = edName.text.toString()
        val email = edEmail.text.toString()

        if(name.isEmpty()  || email.isEmpty()){
            Toast.makeText(this , "Please enter requierd field", Toast.LENGTH_SHORT).show()
        } else{
            val std = StudentModel(id=1,name = name, email = email )
            val status = sqliteHelper.insertStudent(std)
            //Check insert success or not success
                   if (status >-1){
                       Toast.makeText(this, "Student Added...", Toast.LENGTH_SHORT).show()
                    clearEditText()
                    getStudents()
                  }else {
                     Toast.makeText(this,"Record not saved", Toast.LENGTH_SHORT).show()

                   }
        }
    }

    private fun updateStudent(){
      val name = edName.text.toString()
      val email = edEmail.text.toString()

      //check record not change
      if (name == std?.name && email == std?.email){
            Toast.makeText(this, "Record not changed...", Toast.LENGTH_SHORT).show()
            return
        }

        if(std == null){
            return
            val std = StudentModel(id = std!!.id, name = name, email= email)
            val status = sqliteHelper.updateStudent(std)
            if (status > -1)
                clearEditText()
            getStudents()

        }else {
            Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show()
        }

    }

    private fun deleteStudent(id:Int){

        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to delete item?")
        builder.setCancelable( true)
        builder.setPositiveButton("Yes"){ dialog, _ ->
            sqliteHelper.deleteStudentById(id)
            getStudents()
            dialog.dismiss()
        }
       builder.setNegativeButton("No"){ dialog, _ ->

           dialog.dismiss()
       }

     val alert = builder.create()
     alert.show()
    }

    private fun clearEditText() {
        edName.setText("")
        edEmail.setText("")
        edName.requestFocus()
    }

    private fun initRecyclerVew(){
     recyclerView.layoutManager = LinearLayoutManager(this)
     adapter = StudentAdapter()
     recyclerView.adapter = adapter
    }


    private fun initView() {
        edName = findViewById(R.id.edName)
        edEmail = findViewById(R.id.edEmail)
        btnAdd = findViewById(R.id.btnAdd)
        btnView  = findViewById(R.id.btnView)
        btnUpdate = findViewById(R.id.btnUpdate)
        recyclerView = findViewById(R.id.recyclerView)

    }
}