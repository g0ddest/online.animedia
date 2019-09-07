package tv.animedia.a.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_episode.view.*
import tv.animedia.a.R
import tv.animedia.a.api.model.Episode
import tv.animedia.a.api.model.Series
import tv.animedia.a.state.SharedState

class EpisodeAdapter(private val state: SharedState, private val data: List<Episode?>): RecyclerView.Adapter<EpisodeAdapter.EpisodeViewHolder>() {

    class EpisodeViewHolder(private val context: Context,
                           override val containerView: View
    ) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        fun bind(state: SharedState, episode: Episode){
            // if from main
            if (episode.url_video == null || episode.url_video == "") {
                containerView.series_title.text = episode.title.trim()
                containerView.series_title.setOnClickListener {
                    state.series(Series(
                        id = episode.animeId
                    ))
                }
                containerView.episode_title.text = episode.name.trim()
            } else {
                containerView.series_title.text = episode.name.trim()
                containerView.episode_title.visibility = View.GONE
            }
            containerView.episode_image.contentDescription = "${episode.title} ${episode.name}"
            Glide.with(context).load("https:${episode.pic}")
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(containerView.episode_image)
            containerView.episode_image.setOnClickListener {
                state.episode(episode)
                // if from main
                if(episode.url_video == null || episode.url_video == "")
                    state.series(Series(
                        id = episode.animeId
                    ))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_episode, parent, false)
        return EpisodeViewHolder(parent.context, layout)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holderSeries: EpisodeViewHolder, position: Int) {
        if(data[position] != null)
            holderSeries.bind(state, data[position]!!)
    }

}