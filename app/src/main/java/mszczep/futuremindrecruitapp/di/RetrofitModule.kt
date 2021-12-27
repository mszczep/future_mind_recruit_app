package mszczep.futuremindrecruitapp.utils

import mszczep.futuremindrecruitapp.model.NetworkService
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val retrofitModule = module {
    factory { provideOkHttpClient() }
    factory { provideApi(get()) }
    single { provideRetrofit(get())}
}

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder().baseUrl("https://recruitment-task.futuremind.dev").client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create()).build()
}

fun provideApi(retrofit: Retrofit): NetworkService = retrofit.create(NetworkService::class.java)

fun provideOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .readTimeout(10, TimeUnit.SECONDS)
        .connectTimeout(10, TimeUnit.SECONDS)
        .build()
}