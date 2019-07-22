package io.github.lettucech.example.android.viewanimations.util

/**
 * Created by Brian Ho on 2019-07-16.
 */
enum class AnimatorType(val label: String) {
    VALUE_ANIMATOR("Value Animator"),
    OBJECT_ANIMATOR("Object Animator"),
    ANIMATOR_SET("Animator Set"),
    ANIMATE_LAYOUT_CHANGES("Animate Layout Changes"),
    STATE_LIST_ANIMATOR("State List Animator");

    companion object {
        fun toLabelArray() = ArrayList<String>().apply {
            values().iterator().forEach {
                add(it.label)
            }
        }
    }
}