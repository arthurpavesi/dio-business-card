package br.com.pavesi.businesscard.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import br.com.pavesi.businesscard.App
import br.com.pavesi.businesscard.R
import br.com.pavesi.businesscard.data.BusinessCard
import br.com.pavesi.businesscard.databinding.ActivityAddBusinessCardBinding

class AddBusinessCardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBusinessCardBinding

    private val mainViewModel: MainViewModel by viewModels {
        MainViewModel.MainViewModelFactory((application as App).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBusinessCardBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        insertListeners()
    }

    private fun insertListeners() {
        binding.btnClose.setOnClickListener {
            finish()
        }

        binding.btnConfirm.setOnClickListener {
            val businessCard = BusinessCard(name = binding.tilName.editText?.text.toString(),
                                            phone = binding.tilPhone.editText?.text.toString(),
                                            email = binding.tilEmail.editText?.text.toString(),
                                            company = binding.tilCompany.editText?.text.toString(),
                                            backgroundColor = binding.tilColor.editText?.text.toString()
            )

            mainViewModel.insert(businessCard)
            Toast.makeText(this, R.string.label_show_success, Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}