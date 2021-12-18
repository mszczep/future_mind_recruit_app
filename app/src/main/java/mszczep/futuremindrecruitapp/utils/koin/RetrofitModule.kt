package mszczep.futuremindrecruitapp.utils

import mszczep.futuremindrecruitapp.model.IRequests
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Koin module responsible for injecting networking logic
 */
val retrofitModule = module {
    factory { provideOkHttpClient() }
    factory { provideApi(get()) }
    single { provideRetrofit(get())}
}

/**
 * Retrofit provider; creates a retrofit instance
 * @return Retrofit instance
 */
fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder().baseUrl("https://recruitment-task.futuremind.dev").client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create()).build()
}

/**
 * Retrofit requests interface provider
 * @param retrofit A retrofit instance
 * @return Retrofit requests interface instance
 */
fun provideApi(retrofit: Retrofit): IRequests = retrofit.create(IRequests::class.java)

/**
 * An OkHttpClient provider
 * @return OkHttpClient instance
 */
fun provideOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .readTimeout(10, TimeUnit.SECONDS)
        .connectTimeout(10, TimeUnit.SECONDS)
        .build()
}