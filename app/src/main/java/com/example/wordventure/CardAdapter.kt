package com.example.wordventure

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CardAdapter(
    private val context: Context,
    private val cardList: List<CardData>
) : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.myisland_card_item, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val card = cardList[position]
        holder.bind(card)
    }

    override fun getItemCount(): Int = cardList.size

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardImageView: ImageView = itemView.findViewById(R.id.cardImageView)
//        private val cardNameTextView: TextView = itemView.findViewById(R.id.cardNameTextView)

        fun bind(card: CardData) {
//            cardNameTextView.text = card.name
            Glide.with(context).load(card.imgUrl).into(cardImageView) // Glide로 이미지 로드
        }
    }
}
