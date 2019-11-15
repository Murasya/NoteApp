package jp.co.murakoso.noteapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bt_analyze.setOnClickListener {
            val intent = Intent(this, AnalyzeActivity::class.java)
            startActivity(intent)
        }
        bt_recode.setOnClickListener {
            val intent = Intent(this, RecodeListActivity::class.java)
            startActivity(intent)
        }
        createAccountButton.setOnClickListener {
            val intent = Intent(this, EmailPasswordActivity::class.java)
            startActivity(intent)
        }
    }
}
