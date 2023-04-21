package com.example.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.android.gms.common.SignInButton
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class login : AppCompatActivity() {
    private lateinit var emailInput: EditText
    private  lateinit var  emailContainer: TextInputLayout
    private  lateinit var passwordInput: EditText
    private lateinit var passwordContainer: TextInputLayout
    private lateinit var auth: FirebaseAuth
    private lateinit var signInButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        // Initialize Firebase Auth
        auth = Firebase.auth
//        binding
        emailInput=findViewById(R.id.emailLoginInput)
        emailContainer=findViewById(R.id.emailRegContainer)

        passwordInput=findViewById(R.id.passwordLoginInput)
        passwordContainer=findViewById(R.id.passwordRegContainer)

        signInButton=findViewById(R.id.loginInButton)


        emailInput.addTextChangedListener(validateRegister)
        passwordInput.addTextChangedListener(validateRegister)

        var progressBar=findViewById<ProgressBar>(R.id.loginProgressBar)
        signInButton.setOnClickListener {
            signInButton.isEnabled=false
            progressBar.isVisible=true
            emailInput.isEnabled=false
            passwordInput.isEnabled=false
            val password=passwordInput.text.toString().trim()
            val email=emailInput.text.toString().trim()
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val intent= Intent(this, home::class.java)
                        startActivity(intent)
                        finish()
                        progressBar.isVisible=false

                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
//                        progressBar.isVisible=false
//                        signUpButton.isEnabled=true
                    }
                }

        }

//        loginf here link
        val  registerLink: TextView =findViewById(R.id.registerLink)
        registerLink.setOnClickListener {
            startActivity(Intent(this,register::class.java))
        }


    }
    private val validateRegister=object: TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            val email=emailInput.text.toString().trim().lowercase()
            val password=passwordInput.text.toString().trim().lowercase()

            fun validateEmail(): Boolean {
                if(email.isBlank()){
                    emailContainer.isErrorEnabled=true
                    emailContainer.error="Email is required"
                    signInButton.isEnabled=false
                    return false
                }else{
                    emailContainer.isErrorEnabled=false
                    signInButton.isEnabled=true
                    return true
                }
            }

            fun validatePassword(): Boolean {

                if (password.isBlank()) {
                    passwordContainer.isErrorEnabled = true
                    passwordContainer.error = "Password is required"
                    signInButton.isEnabled = false
                    return false

                }else if(password.length<8){
                    passwordContainer.isErrorEnabled = true
                    passwordContainer.error = "Weak password (8-12)"
                    signInButton.isEnabled = false
                    return false

                } else {
                    passwordContainer.isErrorEnabled = false
                    return true
                    signInButton.isEnabled = true

                }
            }


            signInButton.isEnabled=validateEmail()&&validatePassword()
        }

        override fun afterTextChanged(p0: Editable?) {

        }

    }


}

