package br.com.pavesi.businesscard

import android.app.Application
import br.com.pavesi.businesscard.data.AppDatabase
import br.com.pavesi.businesscard.data.BusinessCardRepository

class App: Application() {

    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { BusinessCardRepository(database.businessDao()) }

}