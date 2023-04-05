package com.bluest.secureestate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PatternMatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.bluest.secureestate.databinding.ActivityForgotpassBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPass : AppCompatActivity() {
    private lateinit var firebaseauth:FirebaseAuth
    private lateinit var binding: ActivityForgotpassBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityForgotpassBinding.inflate(layoutInflater)
        val view = binding.root
        super.onCreate(savedInstanceState)
        setContentView(view)

        firebaseauth = FirebaseAuth.getInstance()

        binding.reset.setOnClickListener {
            val semail = binding.eMailpass.text.toString()

            if (android.util.Patterns.EMAIL_ADDRESS.matcher(semail).matches()) {
                firebaseauth.sendPasswordResetEmail(semail)
                    .addOnCompleteListener {task->
                        if(task.isSuccessful){
                            Toast.makeText(this, "Check your email", Toast.LENGTH_SHORT).show()
                            finish()
                        }else{
                            Toast.makeText(this, task.exception!!.message.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
            }else{
                Toast.makeText(this, "Enter a valid email", Toast.LENGTH_SHORT).show()
            }
        }
    }
}