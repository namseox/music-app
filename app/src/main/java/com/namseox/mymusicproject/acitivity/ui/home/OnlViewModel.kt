package com.namseox.mymusicproject.acitivity.ui.home

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.common.api.Api
import com.hieubui00.musicplayer.util.ApiHelper
import com.namseox.mymusicproject.model.AllCategory
import com.namseox.mymusicproject.model.Song
import com.namseox.mymusicproject.remote.api.ApiClient
import com.namseox.mymusicproject.remote.api.ZingMp3Service
import com.namseox.mymusicproject.remote.response.BaseResponse
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.random.Random

class OnlViewModel : ViewModel() {
    private val api: ZingMp3Service = ApiClient.getApiService("http://13.212.58.90:5000/")
    var songCharts: MutableLiveData<List<Song>> = MutableLiveData()

    fun getSong(){
        api.getSong().enqueue(object : Callback<List<Song>>{
            override fun onResponse(call: Call<List<Song>>, response: Response<List<Song>>) {
                songCharts.postValue(response.body())
                Log.d("qwer",songCharts.toString())
            }

            override fun onFailure(call: Call<List<Song>>, t: Throwable) {
                Log.e("xxx", "onFailure: ${t.message}", )
            }

        }
        )
    }








}

