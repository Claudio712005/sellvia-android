package br.com.claus.sellvia.core.di.module

import br.com.claus.sellvia.BuildConfig
import br.com.claus.sellvia.data.remote.api.AuthInterceptor
import br.com.claus.sellvia.data.remote.api.LoginService
import br.com.claus.sellvia.data.remote.api.TokenAuthenticator
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

val networkModule = module {

    single<Gson> {
        GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, JsonDeserializer { json, _, _ ->
                LocalDateTime.parse(json.asString, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            })
            .create()
    }

    single {
        AuthInterceptor(get())
    }

    single(named("refreshClient")) {
        OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.MINUTES)
            .readTimeout(5, TimeUnit.MINUTES)
            .writeTimeout(5, TimeUnit.MINUTES)
            .build()
    }

    single(named("refreshRetrofit")) {
        Retrofit.Builder()
            .baseUrl("${BuildConfig.BASE_URL}api/v1.0.0/")
            .client(get(named("refreshClient")))
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build()
    }

    single<LoginService>(named("refreshService")) {
        get<Retrofit>(named("refreshRetrofit"))
            .create(LoginService::class.java)
    }

    single {
        TokenAuthenticator(
            tokenManager = get(),
            loginService = get(named("refreshService"))
        )
    }

    single {
        val logging = HttpLoggingInterceptor().apply {
            level =
                if (BuildConfig.DEBUG)
                    HttpLoggingInterceptor.Level.BODY
                else
                    HttpLoggingInterceptor.Level.NONE
        }

        OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(get<AuthInterceptor>())
            .authenticator(get<TokenAuthenticator>())
            .connectTimeout(5, TimeUnit.MINUTES)
            .readTimeout(5, TimeUnit.MINUTES)
            .writeTimeout(5, TimeUnit.MINUTES)
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl("${BuildConfig.BASE_URL}api/v1.0.0/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build()
    }
}
