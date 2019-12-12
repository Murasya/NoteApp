package jp.co.murakoso.noteapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_question.*

class QuestionActivity : AppCompatActivity() {
    companion object {
        var answers: MutableList<String> = mutableListOf()
        var i = 0
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)
        val questions: Array<String> = resources.getStringArray(R.array.questions)
        question_text.text = questions.elementAtOrElse(i++){questions[4]}

        answer_button.setOnClickListener {
            val text = answer_editText.text.toString()
            answers.add(text)
            val intent = Intent(this, QuestionActivity::class.java)
            startActivity(intent)
        }
        if(i > 5){
            finish_button.visibility = View.VISIBLE
            finish_button.setOnClickListener {
                i = 0
                val intent = Intent(this, ResultActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
