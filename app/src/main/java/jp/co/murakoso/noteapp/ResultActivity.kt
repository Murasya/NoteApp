package jp.co.murakoso.noteapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.activity_result.*
import java.util.*
import kotlin.collections.ArrayList

class ResultActivity : AppCompatActivity() {
    companion object {
        const val TAG = "resultActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Result"

        val db = FirebaseFirestore.getInstance()
        val user = FirebaseAuth.getInstance().currentUser

        val questions = resources.getStringArray(R.array.questions)
        val answers = QuestionActivity.answers
        val data = RecodeListActivity.dbData
        val dbElement = Array(answers.size) {Pair("question$it","answer$it")}
        var uuidString = UUID.randomUUID().toString()

        // QuestionActivityから来た場合
        if (!answers.isNullOrEmpty()) {
            user?.let {
                val uid = user.uid
                val questionAndAnswer = mutableMapOf(
                    "id" to uuidString,
                    "uid" to uid
                )
                // 質問と答えをマップで保存
                for((index, qa) in dbElement.withIndex()){
                    questionAndAnswer[qa.first] = questions.getOrElse(index){questions[4]}
                    questionAndAnswer[qa.second] = answers[index]
                }
                // dbへ挿入
                db.collection("questionAndAnswer").document(uuidString)
                    .set(questionAndAnswer)
                    .addOnSuccessListener {
                        Log.d(TAG, "DocumentSnapshot added.")
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error adding document", e)
                    }
            }
            // 画面出力
            for((index, answer) in answers.withIndex()) {
                val qText = getTextView(questions.getOrElse(index){questions[4]})
                qText.textSize = 16f
                linearLayout.addView(qText)
                val aText = getTextView(answer)
                aText.textSize = 16f
                linearLayout.addView(aText)
            }
            // RecodeListActivity から来た場合
        } else if (!data.isNullOrEmpty()){
            Log.d(TAG, data.toString())
            uuidString = data["id"].toString()
            val questions2 = data.filterKeys { it.contains("question") }.toSortedMap().toList()
            val answers2 = data.filterKeys { it.contains("answer") }.toSortedMap().toList()
            for((index, value) in questions2.withIndex()) {
                val qText = getTextView(value.second.toString())
                qText.textSize = 16f
                linearLayout.addView(qText)
                val aText = getTextView(answers2[index].second.toString())
                aText.textSize = 16f
                linearLayout.addView(aText)
            }
        }

        // タグ
        var tags: ArrayList<String> = ArrayList()
        // 元々あったタグをDBから取得して表示
        db.collection("questionAndAnswer").document(uuidString)
            .get()
            .addOnSuccessListener { document ->
                val tagAny = document.data?.get("tag")
                if(tagAny is ArrayList<*>){
                    Log.d(TAG, tagAny.joinToString(separator = " "))
                    getTagView(tagAny.joinToString(separator = " "))
                    val temp = tagAny.map{ it.toString()}
                    tags.addAll(temp)
                }
            }
            .addOnFailureListener {
                Log.d(TAG, "No data")
            }
        // 新しくタグを追加
        adapt_button.setOnClickListener {
            val text = tag_editText.text.toString()
            tag_editText.setText("")
            val tagNew = getTagView(text)
            tags.addAll(tagNew)
            db.collection("questionAndAnswer").document(uuidString)
                .set(mapOf("tag" to tags), SetOptions.merge())
                .addOnSuccessListener { Log.d(TAG, "Document snapshot successfully written!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e)}
        }

        backTitle_button.setOnClickListener {
            val intent2 = Intent(this, MainActivity::class.java)
            startActivity(intent2)
        }
    }
    private fun getTagView(text: String): List<String>{
        //textを#で分けて，空白のタグを排除，無駄な空白を消して，先頭に#をつける
        val tags = text.split("#").filterNot { it == "" }.map {it.trim()}.map{"#$it"}
        for(tag in tags){
            if(tag == "") continue
            val tagView = getTextView(tag)
            tagView.textSize = 16f
            tagView.setPadding(0, 0, 10, 0)
            tagLayout.addView(tagView)
        }
        return tags
    }
    private fun getTextView(text: String): TextView {
        val textView = TextView(this)
        val dp = resources.displayMetrics.density
        textView.textSize = dp
        textView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        textView.text = text
        return textView
    }
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}