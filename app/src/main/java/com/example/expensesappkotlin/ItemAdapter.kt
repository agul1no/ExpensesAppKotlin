package com.example.expensesappkotlin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class ItemAdapter (val context: Context, val expenses: ArrayList<ExpenseModel>) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    /** Inflates the item views which were designed in xml layout file
     * create a new link viewholder and initializes some private fields to be used by the recycler view **/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
        LayoutInflater.from(context).inflate(
            R.layout.item_row,
            parent,
            false
            )
        )
    }

    /** Binds each item in the ArrayList to a view
     * called when recyclerView needs a new Link Viewholder of the give type **/
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        //val item = expenses.get(position)
        //holder.tv_name.text = item

        //holder.tv_name.text = expenses[position].toString()
        holder.tv_name.text = expense1.name
        holder.tv_amount.text = expense1.amount.toString()

    }

    override fun getItemCount(): Int {
        return expenses.size
    }

    /** A ViewHoleder describes an item view and metadata about its place within **/
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        // holds the TextView that will add each item to

        val cardView: CardView
        val tv_name: TextView
        val tv_amount: TextView
        val tv_euro: TextView

        init {
            cardView = view.findViewById(R.id.cardView)
            tv_name = view.findViewById(R.id.tv_name)
            tv_amount = view.findViewById(R.id.tv_amount)
            tv_euro = view.findViewById(R.id.tv_euro)
        }
    }
}