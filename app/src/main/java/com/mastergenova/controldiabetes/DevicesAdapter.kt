package com.mastergenova.controldiabetes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DevicesAdapter (private val devices: ArrayList<String>): RecyclerView.Adapter<DevicesAdapter.DevicesViewHolder>(){

    class DevicesViewHolder(val view: View): RecyclerView.ViewHolder(view){
        val name = view.findViewById(R.id.txtName) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DevicesAdapter.DevicesViewHolder{
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_devices, parent, false) as View



        return DevicesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DevicesViewHolder, position: Int) {
        holder.name.text = devices[position]
    }

    override fun getItemCount(): Int {
        return devices.size
    }

}