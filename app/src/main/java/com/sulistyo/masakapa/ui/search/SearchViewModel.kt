package com.sulistyo.masakapa.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sulistyo.masakapa.data.remote.RetrofitInstance
import com.sulistyo.masakapa.model.RandomMealResponse
import com.sulistyo.masakapa.ui.home.TAG
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel : ViewModel() {
    private val searchedMealLiveData = MutableLiveData<RandomMealResponse>()

    fun searchMealDetail(name: String) {
        RetrofitInstance.foodApi.getMealByName(name).enqueue(object : Callback<RandomMealResponse> {
            override fun onResponse(
                call: Call<RandomMealResponse>,
                response: Response<RandomMealResponse>
            ) {
                searchedMealLiveData.value = response.body()
            }

            override fun onFailure(call: Call<RandomMealResponse>, t: Throwable) {
                Log.e(TAG, t.message.toString())
            }

        })
    }

    fun observeSearchLiveData(): LiveData<RandomMealResponse> {
        return searchedMealLiveData
    }
}