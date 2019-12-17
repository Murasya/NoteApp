package jp.co.murakoso.noteapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginTop
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_result.*

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

        // QuestionActivityから来た場合
        if (!answers.isNullOrEmpty()) {
            user?.let {
                val uid = user.uid
                val questionAndAnswer = mutableMapOf(
                    "uid" to uid
                )
                // 質問と答えをマップで保存
                for((index, qa) in dbElement.withIndex()){
                    questionAndAnswer[qa.first] = questions.getOrElse(index){questions[4]}
                    questionAndAnswer[qa.second] = answers[index]
                }
                // dbへ挿入
                db.collection("questionAndAnswer")
                    .add(questionAndAnswer)
                    .addOnSuccessListener { documentReference ->
                        Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
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

        backTitle_button.setOnClickListener {
            val intent2 = Intent(this, MainActivity::class.java)
            startActivity(intent2)
        }
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