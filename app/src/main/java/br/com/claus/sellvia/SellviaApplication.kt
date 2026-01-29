package br.com.claus.sellvia

import android.app.Application
import br.com.claus.sellvia.core.di.module.loginModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class SellviaApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@SellviaApplication)
            modules(loginModule)
        }
    }
}