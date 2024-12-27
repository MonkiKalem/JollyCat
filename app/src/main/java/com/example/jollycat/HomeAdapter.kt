package com.example.jollycat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Define your custom adapter class
class HomeAdapter(private val cats: List<Cat>) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    // ViewHolder holds references to the views for each item in the RecyclerView
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val catNameTextView: TextView = itemView.findViewById(R.id.catNameTextView)
        val catDescriptionTextView: TextView = itemView.findViewById(R.id.catDescriptionTextView)
        // Add references to other views as needed
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cat, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cat = cats[position]
        holder.catNameTextView.text = cat.CatName
        holder.catDescriptionTextView.text = cat.CatDescription
        // Bind other data to views
        // You can also set click listeners here to handle clicks on individual items
        holder.itemView.setOnClickListener {
            // Handle item click
            // For example, navigate to CatDetailActivity with the selected cat data
            val context = holder.itemView.context
            if (context is DashboardActivity) {

            }
        }
    }

    override fun getItemCount(): Int {
        return cats.size
    }
}
