package com.truckintransit.user.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.truckintransit.user.R
import com.truckintransit.user.callbackInterface.ListnerForUserSelectVehicle
import com.truckintransit.user.pojo.service.Truckloaddetail
import kotlinx.android.synthetic.main.item_vehicle_list.view.*


class VehicleListAdapter(var mcontext: Context, var mList:ArrayList<Truckloaddetail>, var mlistner: ListnerForUserSelectVehicle) : RecyclerView.Adapter<VehicleListAdapter.ViewHolder>() {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(mcontext,mList[position], mlistner,  position)
    }

    override fun getItemCount(): Int = mList.count()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_vehicle_list, parent, false)

        return ViewHolder(view)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(mcontext: Context, item: Truckloaddetail, listener: ListnerForUserSelectVehicle, position: Int) {

            itemView.tv_item.text = item.description
            itemView.tv_itemweight.text = item.load_kg
            if(item.isCloured){
                itemView.tv_item.setTextColor(ContextCompat.getColor(mcontext, R.color.white))
                itemView.tv_itemweight.setTextColor(ContextCompat.getColor(mcontext, R.color.white))

            }else{
                itemView.tv_item.setTextColor(ContextCompat.getColor(mcontext, R.color.yellowtransparet))
                itemView.tv_itemweight.setTextColor(ContextCompat.getColor(mcontext, R.color.yellowtransparet))

            }

            Glide.with(mcontext).load(item.truck_image).into(itemView.iv_vehicle)
            itemView.setOnClickListener {

                listener.itemSelectVehicle(position)
            }



        }
    }
}