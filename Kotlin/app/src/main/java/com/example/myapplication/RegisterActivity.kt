package com.example.myapplication

import android.content.*
import androidx.appcompat.app.*
import android.os.Bundle
import android.text.method.*
import android.widget.*
import androidx.appcompat.widget.*
import org.json.JSONObject
import com.android.volley.*
import com.android.volley.toolbox.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val btnRegister:AppCompatButton=findViewById(R.id.btnRegister)
        val btnSignin:TextView=findViewById(R.id.txtSignin)
        val emailtxt:EditText=findViewById(R.id.EmailInp)
        val passwordtxt:EditText=findViewById(R.id.PasswordInp)
        val usernametxt:EditText=findViewById(R.id.UsernameInput)
        val showhidepss:ImageView=findViewById(R.id.showhidepassbtn)

        var v2=false

        //btn to Login Page :
        btnSignin.setOnClickListener(){
            val intent=Intent(this@RegisterActivity,LoginActivity::class.java)
            startActivity(intent)
        }

        //toogle txt password type :
        showhidepss.setOnClickListener(){
            if(v2 != true){
                v2=true
                showhidepss.setBackgroundResource(R.drawable.hide)
                passwordtxt.transformationMethod= HideReturnsTransformationMethod.getInstance()
            } else {
                v2=false
                showhidepss.setBackgroundResource(R.drawable.view)
                passwordtxt.transformationMethod= PasswordTransformationMethod.getInstance()
            }
        }

        //btn Register Actions :
        btnRegister.setOnClickListener(){
            val username=usernametxt.text.toString()
            val email=emailtxt.text.toString().trim()
            val password=passwordtxt.text.toString().trim()


                val url:String="http://172.16.1.107/Login-Register-With-Kotlin/API-PHP/Operations/Register.php"

                val params=HashMap<String,String>()
                params["username"]=username
                params["email"]=email
                params["password"]=password
                val jO=JSONObject(params as Map<*, *>)

                val rq:RequestQueue=Volley.newRequestQueue(this@RegisterActivity)

                val jor=JsonObjectRequest(Request.Method.POST,url,jO,Response.Listener { res->
                    try {
                        if(res.getString("success").equals("1")){
                            val builder= AlertDialog.Builder(this@RegisterActivity)
                            builder.setTitle("Message d'Information :")
                            builder.setMessage("SuccessFull Register .")
                            builder.setPositiveButton("Ok",{ dialogInterface: DialogInterface, i: Int ->
                                startActivity(Intent(this@RegisterActivity,LoginActivity::class.java))
                            }).create()
                            builder.show()

                            emailtxt.text.clear()
                            usernametxt.text.clear()
                            passwordtxt.text.clear()

                        } else { alert("Message d'Erreur !",res.getString("message")) }

                    }catch (e:Exception){
                        alert("Message d'Erreur !",""+e.message)
                    }
                },Response.ErrorListener { err->
                    alert("Message d'Erreur !",""+err.message)
                })

                rq.add(jor)
        }
    }

    private fun alert(title:String,message:String){
        val builder= AlertDialog.Builder(this@RegisterActivity)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("Ok",{ dialogInterface: DialogInterface, i: Int -> }).create()
        builder.show()
    }
}