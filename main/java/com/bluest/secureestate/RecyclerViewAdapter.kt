package com.bluest.secureestate


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter(val propertyList:List<String>) : RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.property_layout,parent,false)
        return MyViewHolder(listItem)
    }

    override fun getItemCount(): Int {
        return propertyList.size
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val property = propertyList[position]
        holder.heading.text = property
        val context: Context = holder.myView.context
        holder.myView.setOnClickListener {
            val intent = Intent(context,EachActivity::class.java)
            intent.putExtra("resId", R.id.post)
            intent.putExtra("rsId", R.id.heading)
            context.startActivity(intent)
            Toast.makeText(context,"Hello",Toast.LENGTH_SHORT).show()
        }

    }
}

class MyViewHolder(val view:View):RecyclerView.ViewHolder(view){
    val heading: TextView = view.findViewById(R.id.heading)
    val myView: LinearLayout = view.findViewById(R.id.post)

}