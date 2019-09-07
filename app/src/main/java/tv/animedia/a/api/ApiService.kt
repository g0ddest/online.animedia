package tv.animedia.a.api

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import tv.animedia.a.api.model.SeriesInfo
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.OkHttpClient
import com.google.gson.GsonBuilder
import io.reactivex.Single
import retrofit2.http.Path
import tv.animedia.a.api.model.EpisodesContainer

interface ApiService {
    @GET("mobile-index")
    fun index(): Single<SeriesInfo>

    @GET("mobile-episode-new")
    fun lastEpisodes(): Single<EpisodesContainer>

    @GET("mobile-anime/{anime}")
    fun series(@Path("anime") animeId: Long): Single<SeriesInfo>

    @GET("mobile-anime/{anime}/{season}")
    fun season(@Path("anime") animeId: Long, @Path("season") season: Int): Single<EpisodesContainer>

    companion object Factory {
        fun create(): ApiService =
            Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(
                    OkHttpClient.Builder()
                        .addInterceptor(
                            HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
                        )
                        .build()
                )
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder()
                    .setLenient()
                    .create()))
                .baseUrl("https://ios.animedia.tv/api/")
                .build()
                .create(ApiService::class.java)
    }
}