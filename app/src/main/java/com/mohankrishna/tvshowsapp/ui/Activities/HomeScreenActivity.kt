package com.mohankrishna.tvshowsapp.ui.Activities

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mohankrishna.tvshowsapp.ui.Fragments.Search_ShowFragment
import com.mohankrishna.tvshowsapp.ui.Fragments.TrendingShowsFragment
import com.mohankrishna.tvshowsapp.R
import com.mohankrishna.tvshowsapp.databinding.ActivityHomeScreenBinding
import com.mohankrishna.tvshowsapp.utils.InternetModeProvider
import com.mohankrishna.tvshowsapp.viewModels.HomeScreenViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeScreenActivity : AppCompatActivity(){
    val  myViewModel:HomeScreenViewModel by viewModel()
    val internetModeProvider:InternetModeProvider by inject()
    lateinit var binding: ActivityHomeScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this,R.layout.activity_home_screen)

        if(!internetModeProvider.isNetworkConnected){
            Toast.makeText(this,"No Internet Connection", Toast.LENGTH_SHORT).show()
        }

        if (supportFragmentManager.findFragmentByTag("TrendingShowsFrag") == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment, TrendingShowsFragment(), "TrendingShowsFrag")
                .commit()
        }
        if (supportFragmentManager.findFragmentByTag("SearchFragment") == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment2, Search_ShowFragment(), "SearchFragment")
                .commit()
        }
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                binding.searchView.clearFocus()
                binding.fragment.visibility= View.GONE
                binding.fragment2.visibility=View.VISIBLE
                myViewModel.searchKey.value=query
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                myViewModel.searchKey.value=newText
                binding.fragment.visibility= View.GONE
                binding.fragment2.visibility=View.VISIBLE
                return false
            }
        })
    }

}