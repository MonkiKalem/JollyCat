package com.example.jollycat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CatAdapter(private val cats: List<Cat>, private val listener: (Cat) -> Unit) : RecyclerView.Adapter<CatAdapter.CatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cat, parent, false)
        return CatViewHolder(view)
    }

    override fun onBindViewHolder(holder: CatViewHolder, position: Int) {
        val cat = cats[position]
        holder.bind(cat)
        holder.itemView.setOnClickListener { listener(cat) } // Set click listener
    }

    override fun getItemCount(): Int {
        return cats.size
    }

    inner class CatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val catName: TextView = itemView.findViewById(R.id.catNameTextView)
        private val catDescription: TextView = itemView.findViewById(R.id.catDescriptionTextView)
        private val catImage: ImageView = itemView.findViewById(R.id.catImageView)
        private val catPrice: TextView = itemView.findViewById(R.id.catPriceTextView)

        fun bind(cat: Cat) {
            catName.text = cat.CatName
            catDescription.text = cat.CatDescription.toString()
            catPrice.text = itemView.context.getString(R.string.price_format, cat.CatPrice)
            Glide.with(itemView.context)
                .load(cat.CatImage)
                .into(catImage)
        }
    }
}

