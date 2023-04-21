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
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class register : AppCompatActivity() {
    private lateinit var emailInput:EditText
    private lateinit var emailContainer:TextInputLayout
    private lateinit var passwordInput:EditText
    private lateinit var passwordContainer:TextInputLayout
    private lateinit var comfirmPasswordInput:EditText
    private lateinit var comfirmpasswordContainer:TextInputLayout
    private lateinit var signUpButton: Button
    private lateinit var auth: FirebaseAuth;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = Firebase.auth


        emailInput=findViewById(R.id.emailLoginInput)
        emailContainer=findViewById(R.id.emailRegContainer)


        passwordInput=findViewById(R.id.passwordLoginInput)
        passwordContainer=findViewById(R.id.passwordRegContainer)


        comfirmPasswordInput=findViewById(R.id.passwordLoginInput)
        comfirmpasswordContainer=findViewById(R.id.emailRegContainer)


        signUpButton=findViewById(R.id.registerButton)

        emailInput.addTextChangedListener(validateRegister)
        passwordInput.addTextChangedListener(validateRegister)
        comfirmPasswordInput.addTextChangedListener(validateRegister)
        val progressBar=findViewById<ProgressBar>(R.id.registerProgressBar)

        signUpButton.setOnClickListener {
            signUpButton.isEnabled=false

            emailInput.isEnabled=true

            progressBar.isVisible=true

            comfirmPasswordInput.isEnabled=true

            // adding to fairebase
            val email=emailInput.text.toString().trim()
            val password=passwordInput.text.toString().trim()
            //sign up new user
            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this) { task ->
                if (task.isSuccessful){
                    val intent=Intent(this,login::class.java)
                    startActivity(intent)
                    finish()
                    progressBar.isVisible=false

                }else{
                    Toast.makeText(this,"not recognise your credentials",Toast.LENGTH_SHORT).show()
                    progressBar.isVisible=false
                   signUpButton.isEnabled=true
                }
            }

        }
        val loginLink=findViewById<TextView>(R.id.registerLink)
        loginLink.setOnClickListener {
            val log=Intent(this,login::class.java)
            startActivity(log)
            finish()
        }

    }
    private val validateRegister=object:TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
          val email=emailInput.text.toString().trim().lowercase()
            val password=passwordInput.text.toString().trim().lowercase()
            val comfirPassword=comfirmPasswordInput.text.toString().trim().lowercase()
            fun validateEmail() :Boolean{
                if (email.isBlank()){
                    emailContainer.isErrorEnabled=true
                    emailContainer.error="first put email please"
                    signUpButton.isEnabled=false
                    return false
                }
                else{
                    emailContainer.isErrorEnabled=false
                    signUpButton.isEnabled=true
                    return true
                }

            }
            fun validatePassword():Boolean{
                if (password.isBlank()){
                    passwordContainer.isErrorEnabled=true
                    passwordContainer.error="password is required please"
                    signUpButton.isEnabled=false
                    return false
                }
                else if(password.length<7){
                    passwordContainer.isErrorEnabled=true
                    passwordContainer.error="weak password include figures and letters maximum 8 characters"
                    signUpButton.isEnabled=false
                    return false
                }
                else{
                    passwordContainer.isErrorEnabled=false

                    return true
                    signUpButton.isEnabled=true
                }
            }
            fun validateConfirmPassword():Boolean{
                if(comfirPassword.isBlank()){
                    comfirmpasswordContainer.isErrorEnabled=true
                    comfirmpasswordContainer.error="confirm password is required"
                    signUpButton.isEnabled=false
                    return false
                }else if(comfirPassword!=password){
                    comfirmpasswordContainer.isErrorEnabled=true
                    comfirmpasswordContainer.error="password mismatch"
                    passwordContainer.isErrorEnabled = true
                    passwordContainer.error = "Password is mismatch"
                    signUpButton.isEnabled = false
                    return false
                }else{
                    comfirmpasswordContainer.isErrorEnabled=false
                    signUpButton.isEnabled=true
                    return true
                }
            }
            signUpButton.isEnabled=validateEmail()&&validatePassword()&&validateConfirmPassword()
        }

        override fun afterTextChanged(p0: Editable?) {

        }
    }
}