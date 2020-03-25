package tv.animedia.a.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_series.*
import tv.animedia.a.R
import tv.animedia.a.api.ApiService
import tv.animedia.a.api.model.Series
import tv.animedia.a.helper.single
import tv.animedia.a.state.SharedState
import tv.animedia.a.ui.adapters.EpisodeAdapter


class SeriesFragment : Fragment() {

    private lateinit var state: SharedState
    private lateinit var api: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        api = ApiService.create()
        state = activity?.run {
            ViewModelProviders.of(this)[SharedState::class.java]
        } ?: throw Exception("Invalid Activity")

        state.selectedEpisode.observe(this, Observer {
            if(it.url_video != null && it.url_video != "") {

                val bundle = bundleOf("videoUrl" to "https:${it.url_video}")

                findNavController().navigate(R.id.action_player, bundle)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_series, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        state.selectedSeries.observe(viewLifecycleOwner, Observer<Series> { series ->
            api.series(series.id)
                .single()
                .map { it.response }
                .map { it.first() }
                .subscribeOn(Schedulers.io())
                .subscribe( { seriesDetails ->
                    seriesDetails?.pic?.apply {
                        Glide.with(context!!).load(seriesDetails.pic)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .dontAnimate()
                            .into(hero_image)
                    }
                    series_title.text = seriesDetails?.title
                    state.season(1)

                    state.selectedSeason.observe(viewLifecycleOwner, Observer<Int> { seasonNumber ->
                        selected_season.text =
                            seriesDetails!!.season.first {it.seasonId == seasonNumber}.displayName

                        api.season(series.id, seasonNumber)
                            .single()
                            .map{ it.response }
                            .subscribeOn(Schedulers.io())
                            .subscribe(
                                { episodes ->
                                    series_episodes_recycler.apply {
                                        layoutManager = LinearLayoutManager(
                                            context,
                                            LinearLayoutManager.VERTICAL,
                                            false
                                        )
                                        isNestedScrollingEnabled = false
                                        adapter = EpisodeAdapter(state, episodes.filterNotNull())
                                    }
                                }, {
                                    it.printStackTrace()
                                }

                            )

                    })

                    val dialog = AlertDialog.Builder(context).apply {
                        setTitle("Выберите сезон")

                        setItems(seriesDetails!!.season.map { it.displayName }.toTypedArray()
                        ) { _, which ->
                            state.season(which + 1)
                        }
                    }.create()

                    selected_season.setOnClickListener {
                        dialog.show()
                    }

                }, {
                    it.printStackTrace()
                })

        })
    }
}
