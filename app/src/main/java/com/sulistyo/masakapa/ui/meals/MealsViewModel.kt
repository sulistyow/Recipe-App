package com.sulistyo.masakapa.ui.meals

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sulistyo.masakapa.data.remote.RetrofitInstance
import com.sulistyo.masakapa.model.Meal
import com.sulistyo.masakapa.model.MealsResponse
import com.sulistyo.masakapa.ui.home.TAG
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MealsViewModel : ViewModel() {
    private var mutableMeal = MutableLiveData<List<Meal>>()

    fun getMealsByCategory(category:String){
        RetrofitInstance.foodApi.getMealsByCategory(category).enqueue(object :
            Callback<MealsResponse> {
            override fun onResponse(call: Call<MealsResponse>, response: Response<MealsResponse>) {
                mutableMeal.value = response.body()!!.meals
            }

            override fun onFailure(call: Call<MealsResponse>, t: Throwable) {
                Log.d(TAG,t.message.toString())
            }

        })
    }

    fun observeMeal(): LiveData<List<Meal>> {
        return mutableMeal
    }
}