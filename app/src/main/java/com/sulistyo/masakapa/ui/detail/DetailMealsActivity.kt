package com.sulistyo.masakapa.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.sulistyo.masakapa.databinding.ActivityDetailMealsBinding
import com.sulistyo.masakapa.helper.Constant.Companion.MEAL_ID
import com.sulistyo.masakapa.helper.Constant.Companion.MEAL_IMG
import com.sulistyo.masakapa.helper.Constant.Companion.MEAL_NAME
import com.sulistyo.masakapa.model.MealDetail

class DetailMealsActivity : AppCompatActivity() {
    lateinit var bind: ActivityDetailMealsBinding
    private lateinit var detailViewModel: DetailViewModel
    private var mealId = ""
    private var mealName = ""
    private var mealImg = ""
    private var ytUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = ActivityDetailMealsBinding.inflate(layoutInflater)
        setContentView(bind.root)

        mealId = intent.getStringExtra(MEAL_ID).toString()
        mealName = intent.getStringExtra(MEAL_NAME).toString()
        mealImg = intent.getStringExtra(MEAL_IMG).toString()

        detailViewModel = ViewModelProvider(this)[DetailViewModel::class.java]
        showLoading()
        detailViewModel.getMealById(mealId)
        detailViewModel.observeMealDetail().observe(this, object : Observer<List<MealDetail>> {
            override fun onChanged(t: List<MealDetail>?) {
                setContent(t!![0])
                hideLoading()
            }

        })
        initView()

    }

    private fun setContent(item: MealDetail) {
        ytUrl = item.strYoutube
        bind.apply {
            tvInstructions.text = "Instructions : "
            tvContent.text = item.strInstructions
            tvAreaInfo.visibility = View.VISIBLE
            tvCategoryInfo.visibility = View.VISIBLE
            tvAreaInfo.text = item.strTags
            tvCategoryInfo.text = tvCategoryInfo.text.toString() + item.strCategory
            btYoutube.visibility = View.VISIBLE
        }

        bind.btYoutube.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(ytUrl)))
        }

    }

    private fun initView() {
        bind.apply {
            collapsingToolbar.title = mealName
            Glide.with(applicationContext)
                .load(mealImg)
                .into(imgMealDetail)
        }
    }

    private fun showLoading() {
        bind.apply {
            btYoutube.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        }
    }

    private fun hideLoading() {
        bind.apply {
            btYoutube.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }
    }
}