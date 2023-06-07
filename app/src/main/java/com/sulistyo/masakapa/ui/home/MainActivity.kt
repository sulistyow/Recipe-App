package com.sulistyo.masakapa.ui.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.sulistyo.masakapa.R
import com.sulistyo.masakapa.adapter.CategoryAdapter
import com.sulistyo.masakapa.adapter.CategoryAdapterBindingInterface
import com.sulistyo.masakapa.databinding.ActivityMainBinding
import com.sulistyo.masakapa.databinding.ItemRecipeCategoryBinding
import com.sulistyo.masakapa.helper.Constant.Companion.CATEGORY_NAME
import com.sulistyo.masakapa.model.Category
import com.sulistyo.masakapa.model.CategoryResponse
import com.sulistyo.masakapa.ui.meals.MealsActivity
import com.sulistyo.masakapa.ui.search.SearchActivity

class MainActivity : AppCompatActivity() {

    private var doubleClick: Boolean = false
    private lateinit var bind: ActivityMainBinding
    private lateinit var mAdapter: CategoryAdapter
    private lateinit var mainViewModel: MainActivityViewModel
    var categories: MutableList<Category> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)

        mainViewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        mAdapter = CategoryAdapter(R.layout.item_recipe_category, CategoryInterface())
        bind.rvMain.apply {
            adapter = mAdapter
        }

        showLoading()

        mainViewModel.observeCategories().observe(this, object : Observer<CategoryResponse> {
            override fun onChanged(t: CategoryResponse?) {
                categories = t!!.categories as MutableList<Category>
                mAdapter.updateData(categories, true)
                hideLoading()
            }
        })

        bind.btSearch.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }

        bind.btSort.setOnClickListener {
            sortDialog()
        }

        bind.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                mAdapter.filter.filter(s)
            }

        })

    }

    private fun sortDialog() {
        val options = arrayOf("A-Z", "Z-A")

        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Sort")
            .setItems(options) { dialogInterface, i ->
                if (i == 0) {
                    // ASCENDING
                    dialogInterface.dismiss()
                    categories.sortWith(compareBy { it.strCategory })
                    mAdapter.updateData(categories, true)
                } else if (i == 1) {
                    // DESCENDING
                    dialogInterface.dismiss()
                    categories.sortWith(compareBy { it.strCategory })
                    categories.reverse()
                    mAdapter.updateData(categories, true)
                }
            }.show()
    }

    private fun showLoading() {
        bind.apply {
            rvMain.visibility = View.GONE
            progress.visibility = View.VISIBLE
        }
    }

    private fun hideLoading() {
        bind.apply {
            rvMain.visibility = View.VISIBLE
            progress.visibility = View.GONE
        }
    }

    inner class CategoryInterface : CategoryAdapterBindingInterface<Category> {
        override fun bindData(item: Category, view: View) {
            val bind = ItemRecipeCategoryBinding.bind(view)
            bind.tvName.text = item.strCategory
            Glide.with(this@MainActivity).load(item.strCategoryThumb).into(bind.ivPoster)

            bind.itemView.setOnClickListener {
                val intent = Intent(this@MainActivity, MealsActivity::class.java)
                intent.putExtra(CATEGORY_NAME, item.strCategory)
                startActivity(intent)
            }
        }

        override fun getDataId(item: Category): Int {
            return item.idCategory
        }

    }

    override fun onBackPressed() {
        if (doubleClick) {
            finish()
        }
        this.doubleClick = true
        Toast.makeText(this, "Click again to close app", Toast.LENGTH_SHORT).show()
        Handler().postDelayed({ doubleClick = false }, 2000)
    }
}