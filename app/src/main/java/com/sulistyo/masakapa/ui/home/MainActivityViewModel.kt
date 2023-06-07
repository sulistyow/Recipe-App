package com.sulistyo.masakapa.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sulistyo.masakapa.data.remote.RetrofitInstance
import com.sulistyo.masakapa.model.CategoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val TAG = "MainMVVM"

class MainActivityViewModel : ViewModel() {

    private val mutableCategory = MutableLiveData<CategoryResponse>()

    init {
        getAllCategories()
    }

    private fun getAllCategories() {
        RetrofitInstance.foodApi.getCategories().enqueue(object : Callback<CategoryResponse> {
            override fun onResponse(
                call: Call<CategoryResponse>,
                response: Response<CategoryResponse>
            ) {
                mutableCategory.value = response.body()
            }

            override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                Log.d(TAG, t.message.toString())
            }
        })
    }

    fun observeCategories(): LiveData<CategoryResponse> {
        return mutableCategory
    }

}