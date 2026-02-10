package br.com.claus.sellvia

import android.app.Application
import br.com.claus.sellvia.core.di.module.loginmodule
import br.com.claus.sellvia.core.di.module.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class SellviaApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@SellviaApplication)
            modules(networkModule, loginmodule)
        }
    }
}