package tv.animedia.a.activity

import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import tv.animedia.a.R
import tv.animedia.a.state.SharedState


class MainActivity : AppCompatActivity() {

    private lateinit var state: SharedState
    private var fullscreen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        state = ViewModelProviders.of(this)[SharedState::class.java]

        state.fullscreen.observe(this, Observer {
            if(it){
                if(!fullscreen) {
                    fullscreen = true
                    requestedOrientation = SCREEN_ORIENTATION_LANDSCAPE
                }
            }else{
                if(fullscreen) {
                    fullscreen = false
                    requestedOrientation = SCREEN_ORIENTATION_PORTRAIT
                }
            }
        })
    }
}
