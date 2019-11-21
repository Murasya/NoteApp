package jp.co.murakoso.noteapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val intent = getIntent()
        val questions = resources.getStringArray(R.array.questions)
        val answers = intent.extras?.getStringArray(AnswerActivity.ANSWERS)

        if (!answers.isNullOrEmpty()) {
            question1_textView.text = questions[0]
            answer1_textView.text = answers[0]
            question2_textView.text = questions[1]
            answer2_textView.text = answers[1]
            question3_textView.text = questions[2]
            answer3_textView.text = answers[2]
        }


    }
}
