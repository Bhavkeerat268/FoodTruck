package com.training.foodtruck.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.training.foodtruck.R
import com.training.foodtruck.model.Faq

class FaqAdaptor(val context: Context,val faqList:List<Faq>):RecyclerView.Adapter<FaqAdaptor.FaqViewHolder>() {
    class FaqViewHolder(view: View):RecyclerView.ViewHolder(view)
    {
        val txtQues:TextView=view.findViewById(R.id.txtQuestion)
        val txtAns:TextView=view.findViewById(R.id.txtAnswer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.single_row_faq,parent,false)
        return FaqViewHolder(view)
    }

    override fun getItemCount(): Int {
      return faqList.size
    }

    override fun onBindViewHolder(holder: FaqViewHolder, position: Int) {
        val faq=faqList[position]
        holder.txtQues.text=faq.txtQues
        holder.txtAns.text=faq.txtAns
    }
}