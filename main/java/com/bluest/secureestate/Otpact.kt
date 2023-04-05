package com.bluest.secureestate

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

class Otpact : AppCompatActivity() {

    private lateinit var firebaseauth: FirebaseAuth
    private lateinit var verifyBtn: Button
    private lateinit var resendotp: TextView
    private lateinit var inputotp1: EditText
    private lateinit var inputotp2: EditText
    private lateinit var inputotp3: EditText
    private lateinit var inputotp4: EditText
    private lateinit var inputotp5: EditText
    private lateinit var inputotp6: EditText
    private lateinit var otp: String
    private lateinit var resedtoken: PhoneAuthProvider.ForceResendingToken
    private lateinit var phnnumber: String
    private lateinit var prgb: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otpact)

        otp = intent.getStringExtra("OTP").toString()
        resedtoken = intent.getParcelableExtra("resendToken")!!
        phnnumber = intent.getStringExtra("numbered")!!

        init()
        prgb.visibility = View.INVISIBLE
        addTextChangeListner()
        resendOTPvisibility()

        resendotp.setOnClickListener {
            resendverificationCode()
            resendOTPvisibility()
        }

        verifyBtn.setOnClickListener {
            val typedOTP =
                (inputotp1.text.toString() + inputotp2.text.toString() + inputotp3.text.toString() + inputotp4.text.toString() + inputotp5.text.toString() + inputotp6.text.toString())

            if (typedOTP.isNotEmpty()) {
                if (typedOTP.length == 6) {
                    val credential: PhoneAuthCredential =
                        PhoneAuthProvider.getCredential(otp, typedOTP)
                    prgb.visibility = View.VISIBLE
                    signInWithPhoneAuthCredential(credential)
                } else {
                    Toast.makeText(this, "Please Enter Valid OTP", Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(this, "Please Enter OTP", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun resendOTPvisibility() {
        inputotp1.setText("")
        inputotp2.setText("")
        inputotp3.setText("")
        inputotp4.setText("")
        inputotp5.setText("")
        inputotp6.setText("")
        resendotp.visibility = View.INVISIBLE
        resendotp.isEnabled = false

        Handler(Looper.myLooper()!!).postDelayed({
            resendotp.visibility = View.VISIBLE
            resendotp.isEnabled = true
        }, 60000)
    }

    private fun resendverificationCode() {
        val options = PhoneAuthOptions.newBuilder(firebaseauth)
            .setPhoneNumber(phnnumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)
            .setForceResendingToken(resedtoken)// OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        firebaseauth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    prgb.visibility = View.VISIBLE
                    Toast.makeText(this, "Authenticated Successfully", Toast.LENGTH_SHORT).show()
                    sendToMain()
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.d(
                        ContentValues.TAG,
                        "signInWithPhoneAuthCredential: ${task.exception.toString()}"
                    )
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        Toast.makeText(this, "Code entered is invalid", Toast.LENGTH_SHORT).show()
                    }
                    // Update UI
                }
            }
    }

    private fun sendToMain() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun addTextChangeListner() {
        inputotp1.addTextChangedListener(EditTextWatcher(inputotp1))
        inputotp2.addTextChangedListener(EditTextWatcher(inputotp2))
        inputotp3.addTextChangedListener(EditTextWatcher(inputotp3))
        inputotp4.addTextChangedListener(EditTextWatcher(inputotp4))
        inputotp5.addTextChangedListener(EditTextWatcher(inputotp5))
        inputotp6.addTextChangedListener(EditTextWatcher(inputotp6))
    }

    private fun init() {
        prgb = findViewById(R.id.phoneProgressBar1)
        firebaseauth = FirebaseAuth.getInstance()
        resendotp = findViewById(R.id.resendTextView)
        verifyBtn = findViewById(R.id.verifyOTP)
        inputotp1 = findViewById(R.id.otpEditText1)
        inputotp2 = findViewById(R.id.otpEditText2)
        inputotp3 = findViewById(R.id.otpEditText3)
        inputotp4 = findViewById(R.id.otpEditText4)
        inputotp5 = findViewById(R.id.otpEditText5)
        inputotp6 = findViewById(R.id.otpEditText6)
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
                Log.d("TAG", "onVerificationFailed: $e")
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                Log.d("TAG", "onVerificationFailed: $e")
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
            otp = verificationId
            resedtoken = token
        }
    }

    inner class EditTextWatcher(private val view: View) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            val text = s.toString()
            when (view.id) {
                R.id.otpEditText1 -> if (text.length == 1) inputotp2.requestFocus()
                R.id.otpEditText2 -> if (text.length == 1) inputotp3.requestFocus() else if (text.isEmpty()) inputotp1.requestFocus()
                R.id.otpEditText3 -> if (text.length == 1) inputotp4.requestFocus() else if (text.isEmpty()) inputotp2.requestFocus()
                R.id.otpEditText4 -> if (text.length == 1) inputotp5.requestFocus() else if (text.isEmpty()) inputotp3.requestFocus()
                R.id.otpEditText5 -> if (text.length == 1) inputotp6.requestFocus() else if (text.isEmpty()) inputotp4.requestFocus()
                R.id.otpEditText6 -> if (text.isEmpty()) inputotp1.requestFocus()
            }
        }

    }
}