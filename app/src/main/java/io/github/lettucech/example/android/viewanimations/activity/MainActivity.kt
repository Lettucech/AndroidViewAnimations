package io.github.lettucech.example.android.viewanimations.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.addListener
import androidx.lifecycle.ViewModelProviders
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import io.github.lettucech.example.android.viewanimations.R
import io.github.lettucech.example.android.viewanimations.viewmodel.LogConsoleViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var logViewModel: LogConsoleViewModel? = null
    private var logConsoleAnimating = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        logViewModel = ViewModelProviders.of(this).get(LogConsoleViewModel::class.java)

        imageView_log_toggle.setOnClickListener {
            if (logConsoleAnimating) {
                return@setOnClickListener
            }

            logViewModel?.getLogPanelOpened()?.value?.let {
                val animator: ValueAnimator
                if (it) {
                    animator = ValueAnimator.ofFloat(0.5f, 1f)

                    imageView_log_toggle.setImageResource(R.drawable.ic_log_console_trigger_expended)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        (imageView_log_toggle.drawable as AnimatedVectorDrawable).start()
                    } else {
                        (imageView_log_toggle.drawable as AnimatedVectorDrawableCompat).start()
                    }
                    logViewModel?.setLogPanelOpened(false)
                } else {
                    animator = ValueAnimator.ofFloat(1f, 0.5f)

                    imageView_log_toggle.setImageResource(R.drawable.ic_log_console_trigger_collapsed)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        (imageView_log_toggle.drawable as AnimatedVectorDrawable).start()
                    } else {
                        (imageView_log_toggle.drawable as AnimatedVectorDrawableCompat).start()
                    }
                    logViewModel?.setLogPanelOpened(true)
                }

                animator.addUpdateListener { valueAnimator ->
                    guideline_log_fragment.setGuidelinePercent(valueAnimator.animatedValue as Float)
                }
                animator.addListener {
                    object : AnimatorListenerAdapter() {
                        override fun onAnimationStart(animation: Animator?) {
                            logConsoleAnimating = true
                        }

                        override fun onAnimationEnd(animation: Animator?) {
                            logConsoleAnimating = false
                        }
                    }
                }
                animator.start()
            }
        }
    }
}
