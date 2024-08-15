package com.example.weatherappliction

import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : MainActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
//         SearchView()



    }
//    private fun SearchView(){
//        val searchView = findViewById<SearchView>(R.id.search_bar)
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                if (query != null) {
//
//                    fetchWeatherData(query)
//                }
//                return true
//
//            }
//
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                return true
//            }
//        })
//
//
//
//    }
//    private fun goback() {
//
//        val searchView = findViewById<SearchView>(R.id.search_bar)
//        searchView.setOnClickListener {
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//        }
//
//    }


    }
