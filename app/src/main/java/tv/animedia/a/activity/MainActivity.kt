package tv.animedia.a.activity

import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import tv.animedia.a.R
import tv.animedia.a.api.ApiService
import tv.animedia.a.helper.single
import tv.animedia.a.ui.adapters.EpisodeAdapter
import tv.animedia.a.ui.adapters.SeriesAdapter


class MainActivity : AppCompatActivity() {

    val api = ApiService.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        index_recycler.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false)

        val self = this
        api.index()
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
                index_recycler.adapter = SeriesAdapter(it.filterNotNull())
            }, {
                it.printStackTrace()
            })

        last_episodes_recycler.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false)

        last_episodes_recycler.isNestedScrollingEnabled = false

        api.lastEpisodes()
            .single()
            .map { it.response }
            .subscribeOn(Schedulers.io())
            .subscribe({
                last_episodes_recycler.adapter = EpisodeAdapter(it.filterNotNull())
            }, {
                it.printStackTrace()
            })


    }
}
