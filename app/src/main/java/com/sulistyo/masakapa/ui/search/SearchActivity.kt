package com.sulistyo.masakapa.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.sulistyo.masakapa.R
import com.sulistyo.masakapa.adapter.GenericAdapter
import com.sulistyo.masakapa.adapter.GenericAdapterBindingInterface
import com.sulistyo.masakapa.databinding.ActivitySearchBinding
import com.sulistyo.masakapa.databinding.ItemMealsBinding
import com.sulistyo.masakapa.helper.Constant
import com.sulistyo.masakapa.model.MealDetail
import com.sulistyo.masakapa.model.RandomMealResponse
import com.sulistyo.masakapa.ui.detail.DetailMealsActivity

class SearchActivity : AppCompatActivity() {

    lateinit var bind: ActivitySearchBinding
    private lateinit var mAdapter: GenericAdapter<MealDetail>
    private lateinit var mViewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(bind.root)
        mViewModel = ViewModelProvider(this)[SearchViewModel::class.java]
        mAdapter = GenericAdapter(R.layout.item_meals, SearchInterface())
        bind.rvSearch.apply {
            adapter = mAdapter
        }

        bind.btSearch.setOnClickListener {
            if (bind.etSearch.text.isNotEmpty()) {
                showLoading()
                mViewModel.searchMealDetail(bind.etSearch.text.toString())
                mViewModel.observeSearchLiveData()
                    .observe(this, object : Observer<RandomMealResponse> {
                        override fun onChanged(t: RandomMealResponse?) {
                            val results = t!!.meals
                            if (results == null) {
                                Toast.makeText(
                                    this@SearchActivity,
                                    "Data tidak ditemukan",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                mAdapter.updateData(results, true)
                            }
                            hideLoading()
                        }
                    })
            } else {
                Toast.makeText(this, "Masukkan Kata Kunci", Toast.LENGTH_SHORT).show()
            }
        }

    }

    inner class SearchInterface : GenericAdapterBindingInterface<MealDetail> {
        override fun bindData(item: MealDetail, view: View) {
            val bind = ItemMealsBinding.bind(view)
            bind.tvName.text = item.strMeal
            Glide.with(this@SearchActivity).load(item.strMealThumb).into(bind.ivPoster)

            view.setOnClickListener {
                val intent = Intent(this@SearchActivity, DetailMealsActivity::class.java)
                intent.putExtra(Constant.MEAL_ID, item.idMeal)
                intent.putExtra(Constant.MEAL_NAME, item.strMeal)
                intent.putExtra(Constant.MEAL_IMG, item.strMealThumb)
                startActivity(intent)
            }
        }

        override fun getDataId(item: MealDetail): Int {
            return item.idMeal.toInt()
        }

    }

    private fun showLoading() {
        bind.apply {
            rvSearch.visibility = View.GONE
            progress.visibility = View.VISIBLE
        }
    }

    private fun hideLoading() {
        bind.apply {
            rvSearch.visibility = View.VISIBLE
            progress.visibility = View.GONE
        }
    }
}