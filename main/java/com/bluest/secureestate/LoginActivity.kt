package com.bluest.secureestate

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bluest.secureestate.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.*


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient


    @SuppressLint("UseCompatLoadingForDrawables", "SuspiciousIndentation", "SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
//        Visibility of pages activity
        binding.loginphone.text = "Login with Phone!"
        binding.Signup.setOnClickListener {
            binding.Signup.background = resources.getDrawable(R.drawable.switch_trcks, null)
            binding.Signup.setTextColor(resources.getColor(R.color.textColor, null))
            binding.logIn.background = null
            binding.SignupLayout.visibility = View.VISIBLE
            binding.loginphone.visibility = View.VISIBLE
            binding.loginLayout.visibility = View.GONE
            binding.signIn.visibility = View.GONE
            binding.loginphone.text = "Register with Phone!"
            binding.regi.visibility = View.VISIBLE
            binding.logior.visibility = View.GONE
            binding.logIn.setTextColor(resources.getColor(R.color.pinkColor, null))
        }
        binding.logIn.setOnClickListener {
            binding.Signup.background = null
            binding.Signup.setTextColor(resources.getColor(R.color.pinkColor, null))
            binding.logIn.background = resources.getDrawable(R.drawable.switch_trcks, null)
            binding.SignupLayout.visibility = View.GONE
            binding.loginLayout.visibility = View.VISIBLE
            binding.loginphone.visibility = View.VISIBLE
            binding.loginphone.text = "Login with Phone!"
            binding.logior.visibility = View.VISIBLE
            binding.logIn.setTextColor(resources.getColor(R.color.textColor, null))
            binding.regi.visibility = View.GONE
            binding.signIn.visibility = View.VISIBLE

        }
        binding.signIn.setOnClickListener {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }
        binding.forgotpass.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ForgotPass::class.java))
        }
//        Register to the app activity
        firebaseAuth = FirebaseAuth.getInstance()
        binding.regi.setOnClickListener {
            val email = binding.eMails.text.toString()
            val password = binding.Passs.text.toString()
            val repass = binding.rePass.text.toString()
            if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                if (password.length > 8) {
                    if (email.isNotEmpty() && password.isNotEmpty() && repass.isNotEmpty()) {
                        if (password == repass) {
                            firebaseAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        val intent = Intent(this, MainActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        Toast.makeText(this, "Sign Up failed!", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                }
                        } else {
                            Toast.makeText(this, "Passwords is not matching", Toast.LENGTH_SHORT)
                                .show()
                        }
                    } else {
                        Toast.makeText(this, "Empty Fields are not allowed", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(
                        this, "Password must contain at least 8 letters", Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(this, "Enter a valid Email Address", Toast.LENGTH_SHORT).show()
            }

        }
//        Login to the app activity
        binding.signIn.setOnClickListener {
            val email = binding.eMail.text.toString()
            val password = binding.Pass.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Login failed!", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Empty Fields are not allowed", Toast.LENGTH_SHORT).show()
            }
        }

        binding.loginphone.setOnClickListener {
            val intent = Intent(this,PhoneLogin::class.java)
            startActivity(intent)
        }

//        google sign in

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.we_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this , gso)
            googleSignInClient.signOut()

        findViewById<ImageView>(R.id.google).setOnClickListener {
            signInGoogle()
        }
    }

    private fun signInGoogle(){
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if (result.resultCode == Activity.RESULT_OK){

            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleResults(task)
        }
    }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful){
            val account : GoogleSignInAccount? = task.result
            if (account != null){
                updateUI(account)
            }
        }else{
            Toast.makeText(this, task.exception.toString() , Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken , null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful){
                val intent = Intent(this , MainActivity::class.java)
                intent.putExtra("name" , account.displayName)
                startActivity(intent)
            }else{
                Toast.makeText(this, it.exception.toString() , Toast.LENGTH_SHORT).show()

            }
        }
    }

    override fun onStart() {
        super.onStart()
        if(firebaseAuth.currentUser != null){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }
}




