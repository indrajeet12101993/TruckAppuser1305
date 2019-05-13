package com.truckintransit.user.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.truckintransit.user.R
import com.truckintransit.user.callbackInterface.ListnerForNaviagtionItem
import kotlinx.android.synthetic.main.item_add_drivers.view.*

class AlldriversRecyclerAdapter(var mcontext: Context, var mList:ArrayList<String>, var mlistner: ListnerForNaviagtionItem) : RecyclerView.Adapter<AlldriversRecyclerAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(mcontext,mList[position], mlistner,  position)
    }

    override fun getItemCount(): Int = mList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_add_drivers, parent, false)

        return ViewHolder(view)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(mcontext: Context,item: String, listener: ListnerForNaviagtionItem, position: Int) {


            itemView.tv_status.setText(item)
            if(position%2==0){
                itemView.relative.setBackgroundResource(R.drawable.square_blue)

            }else{
                itemView.relative.setBackgroundResource(R.drawable.square)


            }


            itemView.setOnClickListener {
                listener.itemSelcectPosition(position)
            }
        }
    }
}