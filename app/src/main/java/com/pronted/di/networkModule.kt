package com.pronted.di

import android.content.Context
import com.pronted.BuildConfig
import com.pronted.app.ProntedApp
import com.pronted.dto.login.UserAppList
import com.pronted.utils.DateUtil
import com.pronted.utils.extentions.ACCESS_TOKEN
import com.pronted.utils.extentions.USER_ACCESS_APPS
import com.base.Preference
import com.base.Trace
import com.google.gson.GsonBuilder
import com.pronted.utils.extentions.DYNAMIC_HEADERS
import com.pronted.utils.extentions.USER_SELECTED_ROLE
import io.paperdb.Paper
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

private const val READ_TIME_OUT = 60L
private const val CONNECTION_TIME_OUT = 60L

val networkModule = module {
    // Provide Gson
    single { GsonBuilder().serializeNulls().create() }

    // Provide HttpLoggingInterceptor
    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    // Provide OkHttpClient
    single {
        val cacheDir = File((get<Context>() as ProntedApp).cacheDir, "http")
        val cache = Cache(cacheDir, 10 * 1024 * 1024) // 10 MB
        OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor(get<HttpLoggingInterceptor>())
            .addInterceptor(Interceptor {
                val original = it.request()
                val builder = it.request().newBuilder().apply {
                    if(!Preference.instance.getBoolean(DYNAMIC_HEADERS)) {
                        addHeader(
                            "Authorization",
                            Preference.instance.getString(ACCESS_TOKEN) ?: ""
                        )
                        addHeader(
                            "SCHOOL_CODE",
                            Paper.book().read<UserAppList>(USER_SELECTED_ROLE)?.schoolCode ?: ""
                        )
                        Trace.i("SCHOOL_CODE: "+Paper.book().read<UserAppList>(USER_SELECTED_ROLE)?.schoolCode)
                        addHeader(
                            "APP_NAME",
                            Paper.book().read<UserAppList>(USER_SELECTED_ROLE)?.appName ?: ""
                        )
                        Trace.i("APP_NAME: "+Paper.book().read<UserAppList>(USER_SELECTED_ROLE)?.appName)
                        if (Paper.book()
                                .read<UserAppList>(USER_SELECTED_ROLE)?.appName.equals("SmartParent")
                        ) {
                            addHeader(
                                "STUDENT_PROFILE_ID",
                                Paper.book()
                                    .read<UserAppList>(USER_SELECTED_ROLE)?.userUniqueId.toString()
                            )
                            Trace.i("STUDENT_PROFILE_ID: "+Paper.book().read<UserAppList>(USER_SELECTED_ROLE)?.userUniqueId)
                        }


                    }
                    addHeader("DEVICE_ID", DateUtil.getDeviceID(get<Context>() as ProntedApp))
                }
                builder.method(original.method, original.body)
                it.proceed(builder.build())
            })
            .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
            .connectTimeout(CONNECTION_TIME_OUT, TimeUnit.SECONDS)
            .build()
    }

    factory {
        Retrofit.Builder().apply {
            client(get())
            baseUrl(BuildConfig.BASE_URL)
            addConverterFactory(GsonConverterFactory.create(get()))
        }
    }

    // Provide Retrofit
    single<Retrofit> {
        get<Retrofit.Builder>().build()
    }
}
