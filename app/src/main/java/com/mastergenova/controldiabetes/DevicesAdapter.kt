package com.mastergenova.controldiabetes

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DevicesAdapter (private val devices: Array<String>): RecyclerView.Adapter<DevicesAdapter.DevicesViewHolder>(){

    class DevicesViewHolder(val textView: TextView): RecyclerView.ViewHolder(textView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DevicesAdapter.DevicesViewHolder{
        val textView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_devices, parent, false) as TextView

        return DevicesViewHolder(textView)
    }

    override fun onBindViewHolder(holder: DevicesViewHolder, position: Int) {
        holder.textView.text = devices[position]
    }

    override fun getItemCount(): Int {
        return devices.size
    }

}