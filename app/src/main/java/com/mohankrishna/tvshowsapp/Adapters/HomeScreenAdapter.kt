package com.mohankrishna.tvshowsapp.Adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mohankrishna.tvshowsapp.ui.Activities.DetailsScreenActivity
import com.mohankrishna.tvshowsapp.ModelClass.Result
import com.mohankrishna.tvshowsapp.MyApplication
import com.mohankrishna.tvshowsapp.R
import com.mohankrishna.tvshowsapp.databinding.SingleCardViewLayoutBinding


class HomeScreenAdapter(var trendingTvShowsList:List<Result>):
    RecyclerView.Adapter<HomeScreenAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater= LayoutInflater.from(parent.context)
        val binding= SingleCardViewLayoutBinding.inflate(inflater,parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var item=trendingTvShowsList.get(position)
        holder.bind(item)

    }

    override fun getItemCount(): Int {
        return trendingTvShowsList.size
    }
    fun updateProductsList(it: List<Result>) {
        if (it != null) {
            trendingTvShowsList=it
        }
        notifyDataSetChanged()
    }

    class MyViewHolder (private var singleCardViewLayoutBinding: SingleCardViewLayoutBinding): RecyclerView.ViewHolder(singleCardViewLayoutBinding.root) {
        fun bind(item:Result){
            singleCardViewLayoutBinding.title.text=item.name
            val imageUrl = "http://image.tmdb.org/t/p/w500${item?.poster_path}"



            MyApplication.getContext()?.let {
                Glide.with(it)
                    .load(imageUrl)
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.error)
                    .into(singleCardViewLayoutBinding.imagePoster)
            }

            if(item?.is_favourite==null){
                singleCardViewLayoutBinding.notFavourite.visibility=View.VISIBLE
                singleCardViewLayoutBinding.favourite.visibility=View.GONE
            }else{
                singleCardViewLayoutBinding.favourite.visibility=View.VISIBLE
                singleCardViewLayoutBinding.notFavourite.visibility=View.GONE
            }

            singleCardViewLayoutBinding.myCardView.setOnClickListener(object : View.OnClickListener {
                override fun onClick(p0: View?) {
                    val intent = Intent(MyApplication.getContext(), DetailsScreenActivity::class.java)
                    intent.putExtra("selectedData",item)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    (MyApplication.getContext())?.startActivity(intent)
                }
            })

        }
    }
}
