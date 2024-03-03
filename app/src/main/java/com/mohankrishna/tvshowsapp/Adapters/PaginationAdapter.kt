package com.mohankrishna.tvshowsapp.Adapters

import android.content.Intent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mohankrishna.tvshowsapp.ui.Activities.DetailsScreenActivity
import com.mohankrishna.tvshowsapp.ModelClass.Result
import com.mohankrishna.tvshowsapp.MyApplication
import com.mohankrishna.tvshowsapp.R
import com.mohankrishna.tvshowsapp.databinding.HorizontalRecyclerviewBinding


class PaginationAdapter: PagingDataAdapter<Result,PaginationAdapter.MyViewHolder>(differCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater= LayoutInflater.from(parent.context)
        val binding= HorizontalRecyclerviewBinding.inflate(inflater,parent,false)
        return MyViewHolder(binding)
    }

    companion object {
        val differCallback = object : DiffUtil.ItemCallback<Result>() {
            override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
                return oldItem.id == oldItem.id
            }

            override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
                return oldItem == newItem
            }
        }
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
        holder.setIsRecyclable(false)
    }

    class MyViewHolder (private var singleCardViewLayoutBinding: HorizontalRecyclerviewBinding): RecyclerView.ViewHolder(singleCardViewLayoutBinding.root) {
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
