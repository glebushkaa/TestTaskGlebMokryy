package ua.gleb.android.tesktaskglebmokryy.presenter

import android.os.Bundle
import android.view.WindowManager
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.AndroidEntryPoint
import ua.gleb.android.tesktaskglebmokryy.R
import ua.gleb.android.tesktaskglebmokryy.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val binding by viewBinding<ActivityMainBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setGradientStatusBar(R.drawable.bar_gradient)
        MobileAds.initialize(this)
    }

    private fun setGradientStatusBar(@DrawableRes id: Int) {
        val background = ContextCompat.getDrawable(this, id)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.setBackgroundDrawable(background)
    }
}