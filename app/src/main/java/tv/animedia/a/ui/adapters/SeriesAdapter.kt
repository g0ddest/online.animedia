package tv.animedia.a.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_image.view.*
import tv.animedia.a.R
import tv.animedia.a.api.model.Series
import tv.animedia.a.state.SharedState


class SeriesAdapter(private val state: SharedState, private val data: List<Series?>): RecyclerView.Adapter<SeriesAdapter.SeriesViewHolder>() {

    class SeriesViewHolder(private val context: Context,
                           override val containerView: View
    ) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        fun bind(state: SharedState, series: Series){
            containerView.image.contentDescription = series.title
            Glide.with(context).load(series.pic)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(containerView.image)
            containerView.image.setOnClickListener {
                state.series(series)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesViewHolder {
        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image, parent, false)
        return SeriesViewHolder(parent.context, layout)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holderSeries: SeriesViewHolder, position: Int) {
        if(data[position] != null)
            holderSeries.bind(state, data[position]!!)
    }

}