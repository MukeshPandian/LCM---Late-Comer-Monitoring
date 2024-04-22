package com.example.notice

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.notice.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val signInEmail : EditText = findViewById(R.id.EmailSt)
        val signInPassword : EditText = findViewById(R.id.passET)


        firebaseAuth = FirebaseAuth.getInstance()
        binding.textView.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.button.setOnClickListener {
            val email = binding.EmailSt.text.toString()
            val pass = binding.passET.text.toString()

            if (email.isEmpty() && pass.isEmpty()) {
                if (email.isEmpty()) {
                    signInEmail.error = "Enter Your Email Address"
                }
                if (pass.isEmpty()) {
                    signInPassword.error = "Enter Your Password"
                }
            }else if(email.isEmpty() || !email.contains("@klu.ac.in")) {
                signInEmail.error = "Enter KLU Email"
                Toast.makeText(this, "Enter KLU Email", Toast.LENGTH_SHORT).show()
            }else if(pass.length < 6) {
                signInEmail.error = "Enter Your Password"
                Toast.makeText(this, "Enter Your Password", Toast.LENGTH_SHORT).show()
            }else{

                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
    override fun onStart() {
        super.onStart()
        if(firebaseAuth.currentUser != null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
