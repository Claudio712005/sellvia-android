package br.com.claus.sellvia

import android.app.Application
import br.com.claus.sellvia.core.di.module.actionsCategoryModule
import br.com.claus.sellvia.core.di.module.coreModule
import br.com.claus.sellvia.core.di.module.listCategoryModule
import br.com.claus.sellvia.core.di.module.listProductModule
import br.com.claus.sellvia.core.di.module.loginModule
import br.com.claus.sellvia.core.di.module.networkModule
import br.com.claus.sellvia.core.di.module.registryProductModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class SellviaApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@SellviaApplication)
            modules(networkModule, loginModule, listCategoryModule, actionsCategoryModule,
                listProductModule, registryProductModule, coreModule
            )
        }
    }
}