package com.bluest.secureestate

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.bluest.secureestate.databinding.ActivityPhonelgnBinding
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit


class PhoneLogin : AppCompatActivity() {
    private lateinit var firebaseauth: FirebaseAuth
    private lateinit var number: String
    private lateinit var phonenumber: EditText
    private lateinit var sendotpbtn: Button
    private  lateinit var prgb:ProgressBar

    private lateinit var binding: ActivityPhonelgnBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityPhonelgnBinding.inflate(layoutInflater)
        val view = binding.root
        super.onCreate(savedInstanceState)
        setContentView(view)

        init()
        binding.sendOTPBtn.setOnClickListener {
            number = phonenumber.text.trim().toString()
            if (number.isNotEmpty()){
                if(number.length==10){
                    number = "+91$number"
                    prgb.visibility = View.VISIBLE
                    val options = PhoneAuthOptions.newBuilder(firebaseauth)
                        .setPhoneNumber(number)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
                        .build()
                    PhoneAuthProvider.verifyPhoneNumber(options)
                }else{
                    Toast.makeText(this,"Please Enter Valid number",Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this,"Phone number cannot be empty",Toast.LENGTH_SHORT).show()
            }
        }


    }
    private fun init(){
        prgb = findViewById(R.id.phoneProgressBar)
        prgb.visibility = View.INVISIBLE
       sendotpbtn = findViewById(R.id.sendOTPBtn)
       phonenumber = findViewById(R.id.phoneEditTextNumber)
        firebaseauth = FirebaseAuth.getInstance()
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        firebaseauth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(this,"Authenticated Successfully",Toast.LENGTH_SHORT).show()
                    sendToMain()
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.d(TAG, "signInWithPhoneAuthCredential: ${task.exception.toString()}")
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        Toast.makeText(this,"Code entered is invalid",Toast.LENGTH_SHORT).show()
                    }
                    // Update UI
                }
            }
    }
    private fun sendToMain(){
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }
   private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {

            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.

            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                Log.d("TAG","onVerificationFailed: ${e.toString()}")
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                Log.d("TAG","onVerificationFailed: ${e.toString()}")
            }

            // Show a message and update the UI
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            val intent = Intent(this@PhoneLogin,Otpact::class.java)
            intent.putExtra("OTP",verificationId)
            intent.putExtra("resendToken",token)
            intent.putExtra("numbered",number)
            startActivity(intent)
            prgb.visibility = View.INVISIBLE
        }
    }

    override fun onStart() {
        super.onStart()
        if(firebaseauth.currentUser != null){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }
}