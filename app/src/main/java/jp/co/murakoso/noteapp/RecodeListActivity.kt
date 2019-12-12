package jp.co.murakoso.noteapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RecodeListActivity : AppCompatActivity() {
    companion object {
        const val TAG = "RecodeListActivity"
        var dbData = mapOf<String, Any>()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recode_list)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "RecodeListActivity"

        val db = FirebaseFirestore.getInstance()
        val user = FirebaseAuth.getInstance().currentUser
        db.collection("questionAndAnswer")
            .whereEqualTo("uid", user?.uid)
            .get()
            .addOnSuccessListener { result ->
                // layout 設定
                val layout = LinearLayout(this)
                layout.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                layout.gravity = Gravity.CENTER
                layout.orientation = LinearLayout.VERTICAL
                setContentView(layout)

                for(document in result){
                    Log.d(TAG, "${document.id} => ${document.data}")
                    // ボタンの配置
                    val intent = Intent(this, ResultActivity::class.java)
                    val data = document.data.map{it.key to it.value}.toMap()
                    val button = Button(this)
                    button.text = data["answer0"].toString()
                    button.setOnClickListener {
                        dbData = data
                        startActivity(intent)
                    }
                    layout.addView(button)
                }
            }
            .addOnFailureListener {
                Log.w(TAG, "Error getting documents")
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}
