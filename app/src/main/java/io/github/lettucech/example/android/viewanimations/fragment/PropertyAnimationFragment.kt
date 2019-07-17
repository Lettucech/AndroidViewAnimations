package io.github.lettucech.example.android.viewanimations.fragment

import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.marginRight
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import io.github.lettucech.example.android.viewanimations.R
import io.github.lettucech.example.android.viewanimations.util.AnimatorType
import io.github.lettucech.example.android.viewanimations.util.InterpolatorType
import io.github.lettucech.example.android.viewanimations.viewmodel.LogConsoleViewModel
import kotlinx.android.synthetic.main.fragment_property_animation.*

/**
 * Created by Brian Ho on 2019-07-12.
 */
class PropertyAnimationFragment : Fragment() {

    private var logViewModel: LogConsoleViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logViewModel = ViewModelProviders.of(requireActivity()).get(LogConsoleViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_property_animation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val animatorAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            AnimatorType.toLabelArray()
        )
        animatorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_animator.adapter = animatorAdapter

        val interpolatorAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            ArrayList<String>().apply {
                add(" - ")
                addAll(InterpolatorType.toLabelArray())
            }
        )
        interpolatorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_interpolator.adapter = interpolatorAdapter


        btn_start_animation.setOnClickListener {
            when (spinner_animator.selectedItemPosition) {
                0 -> {
                    logViewModel?.addLog("Triggered ValueAnimation")
                    startValueAnimation()
                }
            }
        }
    }

    private fun getSelectedInterpolator(): TimeInterpolator? {
        return if (spinner_interpolator.selectedItemPosition == 0) {
            null
        } else {
            val selectedType =
                InterpolatorType.values()[spinner_interpolator.selectedItemPosition - 1]
            InterpolatorType.createInterpolator(selectedType)
        }
    }

    private fun startValueAnimation() {
        val animationValue =
            (resources.displayMetrics.widthPixels - view_animation_object.width - view_animation_object.marginRight * 2).toFloat()
        logViewModel?.addLog("Set animation value")

        val animation = ValueAnimator.ofFloat(animationValue).apply {
            duration = 1000
            interpolator = getSelectedInterpolator()
            addUpdateListener { updatedAnimation ->
                logViewModel?.addLog("Animated value = " + updatedAnimation.animatedValue)
                view_animation_object.translationX = updatedAnimation.animatedValue as Float
            }
            logViewModel?.addLog(
                String.format(
                    "Creating animator\nDuration = %s\nInterpolator = %s",
                    duration,
                    interpolator::class.java.simpleName
                )
            )
        }

        animation.start()
        logViewModel?.addLog("Start animation")
    }
}

