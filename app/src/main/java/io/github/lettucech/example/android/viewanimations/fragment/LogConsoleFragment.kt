package io.github.lettucech.example.android.viewanimations.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.lettucech.example.android.viewanimations.R
import io.github.lettucech.example.android.viewanimations.adapter.LogAdapter
import io.github.lettucech.example.android.viewanimations.model.CustomLog
import io.github.lettucech.example.android.viewanimations.viewmodel.LogConsoleViewModel
import kotlinx.android.synthetic.main.fragment_log.*

/**
 * Created by Brian Ho on 2019-07-17.
 */
class LogConsoleFragment : Fragment() {
    private var logViewModel: LogConsoleViewModel? = null
    private var logAdapter = LogAdapter()
    private var adapterDataObserver: RecyclerView.AdapterDataObserver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logViewModel = ViewModelProviders.of(requireActivity()).get(LogConsoleViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_log, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logViewModel?.getLogPanelOpened()?.observe(this, Observer<Boolean> {
            if (it) {
                imageView_clear_log.visibility = View.VISIBLE
                imageView_stick_to_bottom.visibility = View.VISIBLE
            } else {
                imageView_clear_log.visibility = View.GONE
                imageView_stick_to_bottom.visibility = View.GONE
            }
        })

        recyclerView_log.apply {
            itemAnimator = null
            adapter = logAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapterDataObserver = object : RecyclerView.AdapterDataObserver() {
                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    (recyclerView_log.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(
                        positionStart - 1,
                        0
                    )
                }
            }
            adapterDataObserver?.let { logAdapter.registerAdapterDataObserver(it) }
        }

        logViewModel?.getLogList()?.observe(this, Observer<ArrayList<CustomLog>> {
            if (it.size > 0) {
                logAdapter.addLog(it[it.size - 1])
            }
        })
    }

    override fun onDestroy() {
        adapterDataObserver?.let { logAdapter.unregisterAdapterDataObserver(it) }
        super.onDestroy()
    }
}