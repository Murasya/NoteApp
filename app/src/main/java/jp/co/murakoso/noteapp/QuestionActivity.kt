package jp.co.murakoso.noteapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_question.*

class QuestionActivity : AppCompatActivity() {
    companion object {
        var i = 0
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        val questions: Array<String> = resources.getStringArray(R.array.questions)
        question_text.text = questions[i++]

        answer_button.setOnClickListener {
            val intent = Intent(this, AnswerActivity::class.java)
            startActivity(intent)
        }
    }
}
