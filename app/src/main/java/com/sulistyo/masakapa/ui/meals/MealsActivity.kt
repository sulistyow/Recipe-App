package com.sulistyo.masakapa.ui.meals

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.sulistyo.masakapa.R
import com.sulistyo.masakapa.adapter.GenericAdapter
import com.sulistyo.masakapa.adapter.GenericAdapterBindingInterface
import com.sulistyo.masakapa.databinding.ActivityMealsBinding
import com.sulistyo.masakapa.databinding.ItemMealsBinding
import com.sulistyo.masakapa.helper.Constant.Companion.CATEGORY_NAME
import com.sulistyo.masakapa.helper.Constant.Companion.MEAL_ID
import com.sulistyo.masakapa.helper.Constant.Companion.MEAL_IMG
import com.sulistyo.masakapa.helper.Constant.Companion.MEAL_NAME
import com.sulistyo.masakapa.model.Meal
import com.sulistyo.masakapa.ui.detail.DetailMealsActivity

class MealsActivity : AppCompatActivity() {

    lateinit var bind: ActivityMealsBinding
    private lateinit var mealsViewModel: MealsViewModel
    private lateinit var mAdapter: GenericAdapter<Meal>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = ActivityMealsBinding.inflate(layoutInflater)
        setContentView(bind.root)
        val categoryName = intent.getStringExtra(CATEGORY_NAME).toString()
        mealsViewModel = ViewModelProvider(this)[MealsViewModel::class.java]
        showLoading()
        mealsViewModel.getMealsByCategory(categoryName)

        mAdapter = GenericAdapter(R.layout.item_meals, MealsInterface())
        bind.rvMeals.apply {
            adapter = mAdapter
        }

        mealsViewModel.observeMeal().observe(
            this
        ) { t ->
            if (t == null) {
                hideLoading()
                Toast.makeText(
                    applicationContext,
                    "Data Tidak Tersedia",
                    Toast.LENGTH_SHORT
                ).show()
                onBackPressed()
            } else {
                mAdapter.updateData(t, false)
                bind.tvCategory.text = categoryName
                hideLoading()
            }
        }
    }

    inner class MealsInterface : GenericAdapterBindingInterface<Meal> {
        override fun bindData(item: Meal, view: View) {
            val bind = ItemMealsBinding.bind(view)
            bind.tvName.text = item.strMeal
            Glide.with(this@MealsActivity).load(item.strMealThumb).into(bind.ivPoster)

            view.setOnClickListener {
                val intent = Intent(applicationContext, DetailMealsActivity::class.java)
                intent.putExtra(MEAL_ID, item.idMeal.toString())
                intent.putExtra(MEAL_NAME, item.strMeal)
                intent.putExtra(MEAL_IMG, item.strMealThumb)
                startActivity(intent)
            }
        }

        override fun getDataId(item: Meal): Int {
            return item.idMeal
        }

    }


    private fun showLoading() {
        bind.apply {
            rvMeals.visibility = View.GONE
            progress.visibility = View.VISIBLE
        }
    }

    private fun hideLoading() {
        bind.apply {
            rvMeals.visibility = View.VISIBLE
            progress.visibility = View.GONE
        }
    }
}