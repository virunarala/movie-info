package com.tinker.movieinfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.fragment.app.Fragment
import com.tinker.movieinfo.databinding.ActivityMainBinding
import com.tinker.movieinfo.history.SearchHistoryFragment
import com.tinker.movieinfo.movies.MoviesFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setupBottomNav()

        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
    }

    private fun setupBottomNav() {
        val fragmentManager = supportFragmentManager

        val moviesFragment = MoviesFragment()
        val searchHistoryFragment = SearchHistoryFragment()

        binding.bottomNav.setOnItemSelectedListener { item ->
            val fragment: Fragment = when(item.itemId) {
                R.id.search -> moviesFragment
                R.id.history -> searchHistoryFragment
                else -> moviesFragment
            }
            fragmentManager.beginTransaction().replace(R.id.host_fragment,fragment).commit()
            true

        }
    }

    fun showBottomNav() {
        binding.bottomNav.visibility = View.VISIBLE
    }

    fun hideBottomNav() {
        binding.bottomNav.visibility = View.GONE
    }

}