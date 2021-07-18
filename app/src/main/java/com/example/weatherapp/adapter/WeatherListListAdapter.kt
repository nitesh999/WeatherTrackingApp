package com.example.chatapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ItemWeatherBinding
import com.example.weatherapp.network.Weather

class WeatherListAdapter(val onWeatherClick: (Int) -> Unit) : RecyclerView.Adapter<WeatherListViewHolder>() {

    /**
     * The users that our Adapter will show
     */
    var weatherList: List<Weather> = emptyList()
        set(value) {
            field = value
            // For an extra challenge, update this to use the paging library.

            // Notify any registered observers that the data set has changed. This will cause every
            // element in our RecyclerView to be invalidated.
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherListViewHolder {
        val withDataBinding: ItemWeatherBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            WeatherListViewHolder.LAYOUT, parent, false
        )
        return WeatherListViewHolder(withDataBinding)
    }

    override fun getItemCount(): Int {
        return weatherList.size
    }

    override fun onBindViewHolder(holder: WeatherListViewHolder, position: Int) {
        val networkWeather = weatherList[position]
        holder.viewDataBinding.let {
            it.text1.text = networkWeather.main
            it.text2.text = networkWeather.description
            holder.itemView.setOnClickListener {
                onWeatherClick(position)
            }
        }
    }
}

class WeatherListViewHolder(val viewDataBinding: ItemWeatherBinding) :
    RecyclerView.ViewHolder(viewDataBinding.root) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_weather
    }
}

