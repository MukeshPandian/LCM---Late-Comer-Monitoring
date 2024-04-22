package com.example.notice

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.notice.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySignUpBinding
    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var database : FirebaseDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        val signUpName : EditText = findViewById(R.id.name)
        val signUpPhone : EditText = findViewById(R.id.phone)
        val signUpEmail : EditText = findViewById(R.id.email)
        val signUpPassword : EditText = findViewById(R.id.pass)
        val signUpConfirmPassword : EditText = findViewById(R.id.confirmPass)

        binding.textView.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
        binding.button.setOnClickListener {
            val name = binding.name.text.toString()
            val email = binding.email.text.toString()
            val phone = binding.phone.text.toString()
            val pass = binding.pass.text.toString()
            val confirmPass = binding.confirmPass.text.toString()

            if (name.isEmpty() && phone.isEmpty() && email.isEmpty() && pass.isEmpty() && confirmPass.isEmpty()) {
                if(name.isEmpty()){
                    signUpName.error = "Enter Your Name"
                }
                if(phone.isEmpty()){
                    signUpPhone.error = "Enter Your Phone Number"
                }
                if(email.isEmpty()){
                    signUpEmail.error = "Enter Your Email"
                }
                if(pass.isEmpty()){
                    signUpPassword.error = "Enter Your Password"
                }
                if(confirmPass.isEmpty()){
                    signUpConfirmPassword.error = "Re Enter Your Password"
                }
                Toast.makeText(this,"Enter Valid Details", Toast.LENGTH_SHORT).show()
            }else if(email.isEmpty() || !email.contains("@klu.ac.in")){
                signUpEmail.error = "Enter KLU Email"
                Toast.makeText(this,"Enter KLU Email", Toast.LENGTH_SHORT).show()
            }else if(phone.length != 10) {
                signUpPhone.error = "Enter Valid Phone Number"
                Toast.makeText(this, "Enter Valid Phone Number", Toast.LENGTH_SHORT).show()
            }else if(pass.length < 6){
                signUpPassword.error = "password more than 6 characters"
                Toast.makeText(this,"password more than 6 characters", Toast.LENGTH_SHORT).show()
            }else if(pass != confirmPass) {
                signUpConfirmPassword.error = "password not matched, try again"
                Toast.makeText(this, "password not matched, try again", Toast.LENGTH_SHORT).show()
            }else {

                firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener { it ->
                    if (it.isSuccessful) {

                        val databaseRef = database.reference.child("users").child(firebaseAuth.currentUser!!.uid)
                        val users = Users(name, phone, email, firebaseAuth.currentUser!!.uid)

                        databaseRef.setValue(users).addOnCompleteListener {
                            if(it.isSuccessful){
                                val intent = Intent(this, SignInActivity::class.java)
                                startActivity(intent)
                            }
                        }
                    } else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
