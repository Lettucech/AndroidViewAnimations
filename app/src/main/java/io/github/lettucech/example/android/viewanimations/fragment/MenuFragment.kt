package io.github.lettucech.example.android.viewanimations.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import io.github.lettucech.example.android.viewanimations.R
import kotlinx.android.synthetic.main.fragment_menu.*

/**
 * Created by Brian Ho on 2019-07-12.
 */
class MenuFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_property_animation.setOnClickListener { findNavController().navigate(R.id.action_menuFragment_to_propertyAnimationFragment) }
    }
}