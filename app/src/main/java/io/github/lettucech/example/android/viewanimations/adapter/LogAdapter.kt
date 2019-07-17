package io.github.lettucech.example.android.viewanimations.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.github.lettucech.example.android.viewanimations.R
import io.github.lettucech.example.android.viewanimations.model.CustomLog
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Brian Ho on 2019-07-16.
 */
class LogAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var customLog = ArrayList<CustomLog>()
    private var simpleDateFormat = SimpleDateFormat("hh:mm:ss", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.view_log,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = customLog.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val log = customLog[position]
        val view = holder.itemView
        view.findViewById<TextView>(R.id.textView_time).text = simpleDateFormat.format(log.time)
        view.findViewById<TextView>(R.id.textView_message).text = log.message
    }

    fun addLog(log : CustomLog) {
        customLog.add(log)
        notifyItemInserted(customLog.size)
    }

    private class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}