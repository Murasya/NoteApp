package jp.co.murakoso.noteapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {
    companion object {
        const val TAG = "ResultActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val intent = getIntent()
        val db = FirebaseFirestore.getInstance()
        val user = FirebaseAuth.getInstance().currentUser

        val questions = resources.getStringArray(R.array.questions)
        val answers = intent.extras?.getStringArray(AnswerActivity.ANSWERS)

        if (!answers.isNullOrEmpty()) {
            user?.let {
                val uid = user.uid
                val questionAndAnswer = hashMapOf(
                    "uid" to uid,
                    "question0" to questions[0],
                    "answer0" to answers[0],
                    "question1" to questions[1],
                    "answer1" to answers[1],
                    "question2" to questions[2],
                    "answer2" to answers[2]
                )
                db.collection("questionAndAnswer")
                    .add(questionAndAnswer)
                    .addOnSuccessListener { documentReference ->
                        Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error adding document", e)
                    }
            }
            question1_textView.text = questions[0]
            answer1_textView.text = answers[0]
            question2_textView.text = questions[1]
            answer2_textView.text = answers[1]
            question3_textView.text = questions[2]
            answer3_textView.text = answers[2]
        }
    }
}
