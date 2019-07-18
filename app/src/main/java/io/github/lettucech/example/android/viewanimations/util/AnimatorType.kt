package io.github.lettucech.example.android.viewanimations.util

/**
 * Created by Brian Ho on 2019-07-16.
 */
enum class AnimatorType(val label: String) {
    VALUE_ANIMATOR("ValueAnimator"),
    OBJECT_ANIMATOR("ObjectAnimator"),
    ANIMATOR_SET("AnimatorSet");

    companion object {
        fun toLabelArray() = ArrayList<String>().apply {
            values().iterator().forEach {
                add(it.label)
            }
        }
    }
}