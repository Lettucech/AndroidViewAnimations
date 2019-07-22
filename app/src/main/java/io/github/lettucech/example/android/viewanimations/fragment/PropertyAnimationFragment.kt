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
                when (spinner_animator.getItemAtPosition(position)) {
                    AnimatorType.ANIMATOR_SET.label,
                    AnimatorType.ANIMATE_LAYOUT_CHANGES.label,
                    AnimatorType.STATE_LIST_ANIMATOR.label -> spinner_interpolator.isEnabled = false
                    else -> spinner_interpolator.isEnabled = true
                }
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
            btn_start_animation.isEnabled = false
            when (spinner_animator.selectedItem) {
                AnimatorType.VALUE_ANIMATOR.label -> {
                    logViewModel?.addLog("Triggered Value Animation")
                    startValueAnimation()
                }
                AnimatorType.OBJECT_ANIMATOR.label -> {
                    logViewModel?.addLog("Triggered Object Animation")
                    startObjectAnimation()
                }
                AnimatorType.ANIMATOR_SET.label -> {
                    logViewModel?.addLog("Triggered Animator Set")
                    startAnimatorSet()
                }
                AnimatorType.ANIMATE_LAYOUT_CHANGES.label -> {
                    logViewModel?.addLog("Triggered Animate Layout Changes")
                    startAnimateLayoutChanges()
                }
                AnimatorType.STATE_LIST_ANIMATOR.label -> {
                    logViewModel?.addLog("Triggered State List Animator")
                    startStateListAnimator()
                }
            }
        }
    }

    private fun resetAnimationObject() {
        view_animation_object.translationX = 0f
        view_animation_object.scaleX = 1f
        view_animation_object.scaleY = 1f
        view_animation_object.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorPrimary
            )
        )
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
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    btn_start_animation.isEnabled = true
                }
            })
            val interpolatorClassName = interpolator::class.java.simpleName
            logViewModel?.addLog("Set:\nDuration = $duration\nInterpolator = $interpolatorClassName")
        }

        resetAnimationObject()
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
                btn_start_animation.isEnabled = true
            }
        })

        logViewModel?.addLog("Reset animation object translationX to 0f")
        resetAnimationObject()

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
                    btn_start_animation.isEnabled = true
                }
            })
        }

        logViewModel?.addLog("Reset animation object translationX to 0f")
        resetAnimationObject()
        animatorSet.start()
    }

    private fun startAnimateLayoutChanges() {
        logViewModel?.addLog("Create LayoutTransition and assign to root layout")
        layout_root.layoutTransition = LayoutTransition().apply {
            setDuration(1000)
            addTransitionListener(object : LayoutTransition.TransitionListener {
                var goneAnimation = true

                override fun startTransition(
                    p0: LayoutTransition?,
                    p1: ViewGroup?,
                    p2: View?,
                    p3: Int
                ) {
                    logViewModel?.addLog("Create LayoutTransition and assign to root layout")
                }

                override fun endTransition(
                    p0: LayoutTransition?,
                    p1: ViewGroup?,
                    p2: View?,
                    p3: Int
                ) {
                    if (!goneAnimation) {
                        logViewModel?.addLog("Remove LayoutTransition from to root layout")
                        layout_root.layoutTransition = null
                        btn_start_animation.isEnabled = true
                    } else {
                        goneAnimation = false
                    }
                }

            })
        }
        resetAnimationObject()

        logViewModel?.addLog("Set animation object visibility to GONE")
        view_animation_object.visibility = View.GONE


        logViewModel?.addLog("Set animation object visibility to VISIBLE")
        view_animation_object.visibility = View.VISIBLE
    }

    private fun startStateListAnimator() {
        resetAnimationObject()

        view_animation_object.stateListAnimator =
            AnimatorInflater.loadStateListAnimator(requireContext(), R.xml.animate_scale)

        view_animation_object.setOnClickListener {
            it.setOnClickListener(null)
            textView_instruction.visibility = View.GONE
            textView_instruction.text = null
            btn_start_animation.isEnabled = true
        }

        textView_instruction.visibility = View.VISIBLE
        textView_instruction.text = "Press the square"
    }
}

