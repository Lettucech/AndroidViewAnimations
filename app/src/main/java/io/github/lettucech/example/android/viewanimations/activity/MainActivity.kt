package io.github.lettucech.example.android.viewanimations.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import io.github.lettucech.example.android.viewanimations.R
import io.github.lettucech.example.android.viewanimations.viewmodel.LogViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var logViewModel: LogViewModel? = null
    private var logcatOpened = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        logViewModel = ViewModelProviders.of(this).get(LogViewModel::class.java)

        imageView_log_toggle.setOnClickListener {
            if (logcatOpened) {
                guideline_log_fragment.setGuidelinePercent(1f)
            } else {
                guideline_log_fragment.setGuidelinePercent(0.5f)
            }
            logcatOpened = !logcatOpened
        }
    }
}
