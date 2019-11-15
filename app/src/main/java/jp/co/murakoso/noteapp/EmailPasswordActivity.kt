package jp.co.murakoso.noteapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_email_password.*

class EmailPasswordActivity : AppCompatActivity(){
    private lateinit var auth: FirebaseAuth
    companion object {
        private const val TAG = "EmailPassword"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_password)

        auth = FirebaseAuth.getInstance()

        emailSignInButton.setOnClickListener {
            val email = fieldEmail.text.toString()
            val password = fieldPassword.text.toString()
            signIn(email, password)
        }
        emailCreateAccountButton.setOnClickListener {
            val email = fieldEmail.text.toString()
            val password = fieldPassword.text.toString()
            createAccount(email, password)
        }
        //signOutButton.setOnClickListener(this)
        //verifyEmailButton.setOnClickListener(this)
    }

    private fun createAccount(email: String, password: String) {

        if(!validateForm()){
            Log.w(TAG, "InvalidForm")
            return
        }
        Log.d(TAG, "createAccount:$email $auth")
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    Toast.makeText(
                        baseContext, "SignUp 成功",
                        Toast.LENGTH_SHORT
                    ).show()
                    //updateUI(user)
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "SignUp 失敗",
                        Toast.LENGTH_SHORT
                    ).show()

                    //updateUI(null)
                }
            }
    }

    private fun signIn(email: String, password: String) {
        Log.d(TAG, "signIn:$email")
        if(!validateForm()){
            Log.w(TAG, "FormInvalid.")
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")

                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

    }

    private fun signOut() {
        auth.signOut()
        Toast.makeText(
            baseContext,
            "SignOut",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun sendEmailVerification() {
        // Disable button
        verifyEmailButton.isEnabled = false

        // Send verification email
        // [START send_email_verification]
        val user = auth.currentUser
        user?.sendEmailVerification()
            ?.addOnCompleteListener(this) { task ->
                // [START_EXCLUDE]
                // Re-enable button
                verifyEmailButton.isEnabled = true

                if (task.isSuccessful) {
                    Toast.makeText(baseContext,
                        "Verification email sent to ${user.email} ",
                        Toast.LENGTH_SHORT).show()
                } else {
                    Log.e(TAG, "sendEmailVerification", task.exception)
                    Toast.makeText(baseContext,
                        "Failed to send verification email.",
                        Toast.LENGTH_SHORT).show()
                }
                // [END_EXCLUDE]
            }
        // [END send_email_verification]
    }

    private fun validateForm(): Boolean {
        var valid = true

        val email = fieldEmail.text.toString()
        if (TextUtils.isEmpty(email)) {
            fieldEmail.error = "Required."
            valid = false
        } else {
            fieldEmail.error = null
        }

        val password = fieldPassword.text.toString()
        if (TextUtils.isEmpty(password)) {
            fieldPassword.error = "Required."
            valid = false
        } else {
            fieldPassword.error = null
        }

        return valid
    }
}
