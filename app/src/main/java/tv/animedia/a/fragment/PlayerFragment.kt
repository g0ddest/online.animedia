package tv.animedia.a.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.fragment_player.*
import tv.animedia.a.R
import tv.animedia.a.state.SharedState

class PlayerFragment : Fragment() {

    private lateinit var state: SharedState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        state = activity?.run {
            ViewModelProviders.of(this)[SharedState::class.java]
        } ?: throw Exception("Invalid Activity")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        state.selectedEpisode.observe(this, Observer {
            if(it.url_video != null) {
                // Create a data source factory.
                val dataSourceFactory =
                    DefaultHttpDataSourceFactory(Util.getUserAgent(activity, "animedia-android"))
                // Create a HLS media source pointing to a playlist uri.
                val hlsMediaSource =
                    HlsMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse("https:${it.url_video}"))
                // Create a player instance.
                val player = ExoPlayerFactory.newSimpleInstance(activity)
                player_view.player = player
                // Prepare the player with the HLS media source.
                player.prepare(hlsMediaSource)
                player.playWhenReady = true
            }
        })
        return inflater.inflate(R.layout.fragment_player, container, false)
    }

}
