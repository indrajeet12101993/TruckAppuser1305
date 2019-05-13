package com.truckintransit.user.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.truckintransit.user.R
import com.truckintransit.user.callbackInterface.InterfaceCustomDialogVehicleType
import com.truckintransit.user.pojo.service.Vehiclebodytype


class CustomAdapterForVehicleType(private val cityList: List<Vehiclebodytype>, private val listner: InterfaceCustomDialogVehicleType) :
    RecyclerView.Adapter<CustomAdapterForVehicleType.ViewHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdapterForVehicleType.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.custom_city_list, parent, false)
        return ViewHolder(v)
    }


    //this method is binding the data on the list
    override fun onBindViewHolder(holder: CustomAdapterForVehicleType.ViewHolder, position: Int) {
        holder.bindItems(cityList[position],listner)
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return cityList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(result: Vehiclebodytype, listner: InterfaceCustomDialogVehicleType) {
            val textViewName = itemView.findViewById(R.id.tv_name) as TextView
            textViewName.text = result.name

            itemView.setOnClickListener {
                listner.funOnVehicleType(result)
            }

        }
    }
}