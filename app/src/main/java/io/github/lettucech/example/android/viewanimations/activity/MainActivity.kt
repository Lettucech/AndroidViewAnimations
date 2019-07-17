package io.github.lettucech.example.android.viewanimations.activity

import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import io.github.lettucech.example.android.viewanimations.R
import io.github.lettucech.example.android.viewanimations.viewmodel.LogConsoleViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var logViewModel: LogConsoleViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        logViewModel = ViewModelProviders.of(this).get(LogConsoleViewModel::class.java)

        imageView_log_toggle.setOnClickListener {
            logViewModel?.getLogPanelOpened()?.value?.let {
                if (it) {
                    guideline_log_fragment.setGuidelinePercent(1f)
                    imageView_log_toggle.setImageResource(R.drawable.ic_log_console_trigger_expended)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        (imageView_log_toggle.drawable as AnimatedVectorDrawable).start()
                    } else {
                        (imageView_log_toggle.drawable as AnimatedVectorDrawableCompat).start()
                    }
                    logViewModel?.setLogPanelOpened(false)
                } else {
                    guideline_log_fragment.setGuidelinePercent(0.5f)
                    imageView_log_toggle.setImageResource(R.drawable.ic_log_console_trigger_collapsed)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        (imageView_log_toggle.drawable as AnimatedVectorDrawable).start()
                    } else {
                        (imageView_log_toggle.drawable as AnimatedVectorDrawableCompat).start()
                    }
                    logViewModel?.setLogPanelOpened(true)
                }
            }
        }
    }
}
