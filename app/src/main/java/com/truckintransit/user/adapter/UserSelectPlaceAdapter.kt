package com.truckintransit.user.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.truckintransit.user.R
import com.truckintransit.user.callbackInterface.ListnerForUserSelectPlace
import com.truckintransit.user.pojo.service.Service
import kotlinx.android.synthetic.main.item.view.*





class UserSelectPlaceAdapter(var mcontext:Context, var mList:ArrayList<Service>, var mlistner: ListnerForUserSelectPlace) : RecyclerView.Adapter<UserSelectPlaceAdapter.ViewHolder>() {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(mcontext,mList[position], mlistner,  position)
    }

    override fun getItemCount(): Int = mList.count()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user_select, parent, false)

        return ViewHolder(view)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(mcontext: Context, item: Service, listener: ListnerForUserSelectPlace, position: Int) {

            itemView.tv_item.text = item.name
            if(item.isCloured){
                itemView.linear_layout.setBackgroundColor(ContextCompat.getColor(mcontext, R.color.yellow))
            }else{
                itemView.linear_layout.setBackgroundColor(ContextCompat.getColor(mcontext, R.color.navigation_text_colour))
            }



            itemView.setOnClickListener {
                //itemView.setBackgroundColor(ContextCompat.getColor(mcontext, R.color.yellow))

                 // for(mList)
               // itemView.linear_layout.setBackgroundColor(ContextCompat.getColor(mcontext, R.color.yellow))
                listener.itemSelectPlace(position)
            }



        }
    }
}