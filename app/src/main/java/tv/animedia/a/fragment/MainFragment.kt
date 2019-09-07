package tv.animedia.a.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_main.*
import tv.animedia.a.R
import tv.animedia.a.api.ApiService
import tv.animedia.a.helper.single
import tv.animedia.a.state.SharedState
import tv.animedia.a.ui.adapters.EpisodeAdapter
import tv.animedia.a.ui.adapters.SeriesAdapter

class MainFragment : Fragment() {

    var api: ApiService? = null
    private lateinit var state: SharedState

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (api == null) api = ApiService.create()
        state = activity?.run {
            ViewModelProviders.of(this)[SharedState::class.java]
        } ?: throw Exception("Invalid Activity")

        index_recycler.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false)

        val self = this
        api!!.index()
            .single()
            .map { it.response }
            .subscribeOn(Schedulers.io())
            .subscribe({
                it.filterNotNull().first().apply {
                    Glide.with(self).load(this.pic)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontAnimate()
                        .into(hero_image)
                    hero_image.contentDescription = this.title
                }
                index_recycler.adapter = SeriesAdapter(findNavController(), state, it.filterNotNull())
            }, {
                it.printStackTrace()
            })

        last_episodes_recycler.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false)

        last_episodes_recycler.isNestedScrollingEnabled = false

        api!!.lastEpisodes()
            .single()
            .map { it.response }
            .subscribeOn(Schedulers.io())
            .subscribe({
                last_episodes_recycler.adapter = EpisodeAdapter(findNavController(), state, it.filterNotNull())
            }, {
                it.printStackTrace()
            })
    }
}
