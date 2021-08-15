package br.com.pavesi.businesscard.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import br.com.pavesi.businesscard.App
import br.com.pavesi.businesscard.databinding.ActivityMainBinding
import br.com.pavesi.businesscard.util.Image

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val mainViewModel: MainViewModel by viewModels {
        MainViewModel.MainViewModelFactory((application as App).repository)
    }

    private val adapter by lazy { BusinessCardAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.rvCards.adapter = adapter
        getAllBusinessCard()
        insertListeners()
    }

    private fun insertListeners() {
        binding.fab.setOnClickListener {
            val intent = Intent(this@MainActivity, AddBusinessCardActivity::class.java)
            startActivity(intent)
        }

        adapter.listenerShare = { card ->
            Image.share(this@MainActivity, card)
        }
    }

    private fun getAllBusinessCard() {
        mainViewModel.getAll().observe(this, { businessCards ->
            adapter.submitList(businessCards)
        })
    }
}