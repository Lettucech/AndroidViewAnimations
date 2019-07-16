package io.github.lettucech.example.android.viewanimations.util

import android.animation.TimeInterpolator
import android.view.animation.*

/**
 * Created by Brian Ho on 2019-07-16.
 */
enum class InterpolatorType(val label: String) {
    ACCELERATE_DECELERATE_INTERPOLATOR("AccelerateDecelerateInterpolator"),
    ACCELERATE_INTERPOLATOR("AccelerateInterpolator"),
    ANTICIPATE_INTERPOLATOR("AnticipateInterpolator"),
    ANTICIPATE_OVERSHOOT_INTERPOLATOR("AnticipateOvershootInterpolator"),
    BOUNCE_INTERPOLATOR("BounceInterpolator"),
    CYCLE_INTERPOLATOR("CycleInterpolator"),
    DECELERATE_INTERPOLATOR("DecelerateInterpolator"),
    LINEAR_INTERPOLATOR("LinearInterpolator"),
    OVERSHOOT_INTERPOLATOR("OvershootInterpolator");

    companion object {
        fun toLabelArray() = ArrayList<String>().apply {
            InterpolatorType.values().iterator().forEach {
                add(it.label)
            }
        }

        fun createInterpolator(type : InterpolatorType) : TimeInterpolator? {
            return when(type) {
                ACCELERATE_DECELERATE_INTERPOLATOR -> AccelerateDecelerateInterpolator()
                ACCELERATE_INTERPOLATOR -> AccelerateInterpolator()
                ANTICIPATE_INTERPOLATOR -> AnticipateInterpolator()
                ANTICIPATE_OVERSHOOT_INTERPOLATOR -> AnticipateOvershootInterpolator()
                BOUNCE_INTERPOLATOR -> BounceInterpolator()
                CYCLE_INTERPOLATOR -> CycleInterpolator(3f)
                DECELERATE_INTERPOLATOR -> DecelerateInterpolator()
                LINEAR_INTERPOLATOR -> LinearInterpolator()
                OVERSHOOT_INTERPOLATOR -> OvershootInterpolator()
            }
        }
    }
}