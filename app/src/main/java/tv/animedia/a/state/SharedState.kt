package tv.animedia.a.state

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tv.animedia.a.api.model.Series

class SharedState: ViewModel(){
    var selectedSeries = MutableLiveData<Series>()

    fun series(series: Series){
        selectedSeries.value = series
    }
}