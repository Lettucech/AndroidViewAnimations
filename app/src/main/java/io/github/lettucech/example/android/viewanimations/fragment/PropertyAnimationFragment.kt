package io.github.lettucech.example.android.viewanimations.fragment

import android.animation.*
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
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
        spinner_animator.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                spinner_interpolator.isEnabled = spinner_animator.getItemAtPosition(position) != AnimatorType.ANIMATOR_SET.label
            }

        }

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
            when (spinner_animator.selectedItem) {
                AnimatorType.VALUE_ANIMATOR.label -> {
                    logViewModel?.addLog("Triggered ValueAnimation")
                    startValueAnimation()
                }
                AnimatorType.OBJECT_ANIMATOR.label -> {
                    logViewModel?.addLog("Triggered ObjectAnimation")
                    startObjectAnimation()
                }
                AnimatorType.ANIMATOR_SET.label -> {
                    logViewModel?.addLog("Triggered AnimatorSet")
                    startAnimatorSet()
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
        logViewModel?.addLog("Creating ValueAnimator")
        val animationValue =
            (resources.displayMetrics.widthPixels - view_animation_object.width - view_animation_object.marginRight * 2).toFloat()

        logViewModel?.addLog("Involve ValueAnimator.ofFloat($animationValue)")
        val animator = ValueAnimator.ofFloat(animationValue).apply {
            duration = 1000
            interpolator = getSelectedInterpolator()
            addUpdateListener { updatedAnimation ->
                logViewModel?.addLog("Change view translationX to " + updatedAnimation.animatedValue)
                view_animation_object.translationX = updatedAnimation.animatedValue as Float
            }
            val interpolatorClassName = interpolator::class.java.simpleName
            logViewModel?.addLog("Set:\nDuration = $duration\nInterpolator = $interpolatorClassName")
        }

        animator.start()
        logViewModel?.addLog("Start animation")
    }

    private fun startObjectAnimation() {
        logViewModel?.addLog("Creating ObjectAnimator")
        val animationValue =
            (resources.displayMetrics.widthPixels - view_animation_object.width - view_animation_object.marginRight * 2).toFloat()

        logViewModel?.addLog("Involve ObjectAnimator.ofFloat(view_animation_object, \"translationX\", $animationValue)")
        val animator =
            ObjectAnimator.ofFloat(view_animation_object, "translationX", animationValue).apply {
                duration = 1000
                interpolator = getSelectedInterpolator()
                val interpolatorClassName = interpolator::class.java.simpleName
                logViewModel?.addLog("Set:\nDuration = $duration\nInterpolator = $interpolatorClassName")
            }
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                logViewModel?.addLog("Animation finished object translationX is now " + view_animation_object.translationX)
            }
        })

        logViewModel?.addLog("Reset animation object translationX to 0f")
        view_animation_object.translationX = 0f

        animator.start()
        logViewModel?.addLog("Start animation")
    }

    private fun startAnimatorSet() {
        val animationValue =
            (resources.displayMetrics.widthPixels - view_animation_object.width - view_animation_object.marginRight * 2).toFloat()

        logViewModel?.addLog("Creating move animator")
        logViewModel?.addLog("Involve ObjectAnimator.ofFloat(view_animation_object, \"translationX\", $animationValue)")
        val moveAnimator =
            ObjectAnimator.ofFloat(view_animation_object, "translationX", animationValue).apply {
                duration = 1000
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator?) {
                        logViewModel?.addLog("Play move animator")
                    }
                })
            }

        logViewModel?.addLog("Creating scale X animator")
        logViewModel?.addLog("Involve ObjectAnimator.ofFloat(view_animation_object, \"scaleX\", 1.5f)")
        val scaleXAnimator = ObjectAnimator.ofFloat(view_animation_object, "scaleX", 1.5f).apply {
            duration = 1000
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator?) {
                    logViewModel?.addLog("Play scale X animator")
                }
            })
        }

        logViewModel?.addLog("Creating scale Y animator")
        logViewModel?.addLog("Involve ObjectAnimator.ofFloat(view_animation_object, \"scaleY\", 1.5f)")
        val scaleYAnimator = ObjectAnimator.ofFloat(view_animation_object, "scaleY", 1.5f).apply {
            duration = 1000
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator?) {
                    logViewModel?.addLog("Play scale Y animator")
                }
            })
        }

        logViewModel?.addLog("Creating color animator")
        logViewModel?.addLog("Involve ObjectAnimator.ofFloat(view_animation_object, \"backgroundColor\", colorPrimary, Color.MAGENTA)")
        val colorAnimator = ObjectAnimator.ofArgb(
            view_animation_object,
            "backgroundColor",
            ContextCompat.getColor(requireContext(), R.color.colorPrimary),
            Color.MAGENTA
        ).apply {
            duration = 1000
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator?) {
                    logViewModel?.addLog("Play color animator")
                }
            })
        }

        val animatorSet = AnimatorSet().apply {
            playSequentially(moveAnimator)
            play(scaleXAnimator).with(moveAnimator)
            play(scaleYAnimator).after(moveAnimator)
            play(colorAnimator).with(scaleYAnimator)
        }.apply {
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    logViewModel?.addLog("All animator played")
                }
            })
        }

        logViewModel?.addLog("Reset animation object translationX to 0f")
        view_animation_object.translationX = 0f
        view_animation_object.scaleX = 1f
        view_animation_object.scaleY = 1f
        view_animation_object.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorPrimary
            )
        )
        animatorSet.start()
    }
}

