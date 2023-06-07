package com.sulistyo.masakapa.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sulistyo.masakapa.data.remote.RetrofitInstance
import com.sulistyo.masakapa.model.MealDetail
import com.sulistyo.masakapa.model.RandomMealResponse
import com.sulistyo.masakapa.ui.home.TAG
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {

    private val mutableMealDetail = MutableLiveData<List<MealDetail>>()

    fun getMealById(id: String) {
        RetrofitInstance.foodApi.getMealById(id).enqueue(object : Callback<RandomMealResponse> {
            override fun onResponse(
                call: Call<RandomMealResponse>,
                response: Response<RandomMealResponse>
            ) {
                mutableMealDetail.value = response.body()!!.meals
            }

            override fun onFailure(call: Call<RandomMealResponse>, t: Throwable) {
                Log.e(TAG, t.message.toString())
            }

        })
    }

    fun observeMealDetail(): LiveData<List<MealDetail>> {
        return mutableMealDetail
    }

}