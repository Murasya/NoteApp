package jp.co.murakoso.noteapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_answer.*

class AnswerActivity : AppCompatActivity() {
    companion object {
        const val ANSWERS = "jp.co.murakoso.noteapp.ANSWERS"
        var answers:Array<String?> = arrayOfNulls(5)
        var i = 0
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answer)

        answer_button_answer.setOnClickListener {
            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra(ANSWERS, answers)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        val text = answer_editText.text.toString()

        answers[i++] = text
    }
}
