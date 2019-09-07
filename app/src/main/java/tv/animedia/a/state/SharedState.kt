package tv.animedia.a.state

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tv.animedia.a.api.model.Series

class SharedState: ViewModel(){
    var selectedSeries = MutableLiveData<Series>()
    val selectedSeason = MutableLiveData<Int>()

    fun series(series: Series){
        selectedSeries.value = series
    }

    fun season(season: Int){
        selectedSeason.value = season
    }
}