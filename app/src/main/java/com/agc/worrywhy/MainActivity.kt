package com.agc.worrywhy

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar_main)

        navigation_bottom.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_routine -> {
                    println("routine")
                    false
                }
                R.id.menu_coping -> {
                    println("coping")
                    false
                }
                R.id.menu_worries -> {
                    popOrGo(R.id.worryListFragment)
                    true
                }
                else -> false
            }
        }
    }

    private fun popOrGo(@IdRes destinationId: Int) {
        findNavController(R.id.nav_host).apply {
            if (currentDestination?.id != destinationId && !popBackStack(
                    destinationId,
                    false
                )
            ) {
                navigate(destinationId)
            }
        }
    }
}


