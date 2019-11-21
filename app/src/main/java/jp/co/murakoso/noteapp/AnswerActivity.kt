package jp.co.murakoso.noteapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_answer.*

class AnswerActivity : AppCompatActivity() {
    companion object {
        const val TAG = "AnswerActivity"
        const val ANSWERS = "jp.co.murakoso.noteapp.ANSWERS"
        const val LOOP_NUM = 3
        var answers:Array<String?> = arrayOfNulls(LOOP_NUM)
        var i = 0
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answer)

        answer_button_answer.setOnClickListener {
            val text = answer_editText.text.toString()
            answers[i++] = text
            if (i == LOOP_NUM){
                val intent = Intent(this, ResultActivity::class.java)
                intent.putExtra(ANSWERS, answers)
                startActivity(intent)
            } else {
                val intent = Intent(this, QuestionActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
