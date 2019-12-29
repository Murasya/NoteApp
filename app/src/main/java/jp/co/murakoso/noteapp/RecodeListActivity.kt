package jp.co.murakoso.noteapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_recode_list.*

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
        var viewList = ArrayList<Button>()
        db.collection("questionAndAnswer")
            .whereEqualTo("uid", user?.uid)
            .get()
            .addOnSuccessListener { result ->
                for(document in result){
                    Log.d(TAG, "${document.id} => ${document.data}")
                    // ボタンの配置
                    val data = document.data.map{it.key to it.value}.toMap()
                    val button = Button(this)
                    button.isAllCaps = false
                    viewList.add(button)
                    val buttonText = data["answer0"].toString() + " " + data["tag"].toString()
                    button.text = buttonText
                    button.setOnClickListener {
                        val intent = Intent(this, ResultActivity::class.java)
                        dbData = data
                        startActivity(intent)
                    }
                    layout.addView(button)
                }
            }
            .addOnFailureListener {
                Log.w(TAG, "Error getting documents")
            }

        search_button.setOnClickListener {
            for(view in viewList)
                layout.removeView(view)
            var searchText = "#${tag_editText.text}".replace("##", "#")
            Log.d(TAG, searchText)

            if(searchText == "#"){
                db.collection("questionAndAnswer")
                    .whereEqualTo("uid", user?.uid)
                    .get()
                    .addOnSuccessListener { result ->
                        for(document in result){
                            Log.d(TAG, "${document.id} => ${document.data}")
                            // ボタンの配置
                            val data = document.data.map{it.key to it.value}.toMap()
                            val button = Button(this)
                            button.isAllCaps = false
                            viewList.add(button)
                            val buttonText = data["answer0"].toString() + " " + data["tag"].toString()
                            button.text = buttonText
                            button.setOnClickListener {
                                val intent = Intent(this, ResultActivity::class.java)
                                dbData = data
                                startActivity(intent)
                            }
                            layout.addView(button)
                        }
                    }
                    .addOnFailureListener {
                        Log.w(TAG, "Error getting documents")
                    }
            } else {
                db.collection("questionAndAnswer")
                    .whereEqualTo("uid", user?.uid)
                    .whereArrayContains("tag", searchText)
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            Log.d(TAG, "${document.id} => ${document.data}")
                            // ボタンの配置
                            val data = document.data.map { it.key to it.value }.toMap()
                            val button = Button(this)
                            button.isAllCaps = false
                            viewList.add(button)
                            val buttonText =
                                data["answer0"].toString() + " " + data["tag"].toString()
                            button.text = buttonText
                            button.setOnClickListener {
                                val intent = Intent(this, ResultActivity::class.java)
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
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}
