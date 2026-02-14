package br.com.satsolucoes.fieldflow.di

import androidx.room.Room
import br.com.satsolucoes.fieldflow.data.local.database.AppDatabase
import br.com.satsolucoes.fieldflow.data.local.database.Migrations
import br.com.satsolucoes.fieldflow.data.remote.api.MaterialApiService
import br.com.satsolucoes.fieldflow.data.remote.api.MaterialApiServiceProvider
import br.com.satsolucoes.fieldflow.data.remote.api.RetrofitMaterialApiService
import br.com.satsolucoes.fieldflow.data.repository.ConsumoRepositoryImpl
import br.com.satsolucoes.fieldflow.data.repository.MaterialRepositoryImpl
import br.com.satsolucoes.fieldflow.domain.repository.ConsumoRepository
import br.com.satsolucoes.fieldflow.domain.repository.MaterialRepository
import br.com.satsolucoes.fieldflow.presentation.viewmodel.MaterialViewModel
import br.com.satsolucoes.fieldflow.presentation.viewmodel.SyncViewModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

/** Módulo de Rede */
val networkModule = module {
    // Gson configurado com adapter para LocalDateTime
    single<Gson> {
        GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, object : JsonDeserializer<LocalDateTime>, JsonSerializer<LocalDateTime> {
                private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

                override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): LocalDateTime {
                    return LocalDateTime.parse(json.asString, formatter)
                }

                override fun serialize(src: LocalDateTime, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
                    return JsonPrimitive(src.format(formatter))
                }
            })
            .create()
    }

    // OkHttpClient configurado com logging e timeouts
    single<OkHttpClient> {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    // Retrofit API Service (recria o Retrofit a cada chamada com a URL atual)
    single<MaterialApiService>(qualifier = org.koin.core.qualifier.named("retrofit")) {
        RetrofitMaterialApiService(
            okHttpClient = get(),
            gson = get()
        )
    }

    // Provider que alterna entre Fake e Real API baseado na configuração
    single<MaterialApiService> {
        MaterialApiServiceProvider(
            context = androidContext(),
            retrofitApiService = get(qualifier = org.koin.core.qualifier.named("retrofit"))
        )
    }
}

/** Módulo de banco de dados - Room */
val databaseModule = module {
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, AppDatabase.DATABASE_NAME)
            .addMigrations(*Migrations.ALL_MIGRATIONS)
            .build()
    }

    single { get<AppDatabase>().materialDao() }
    single { get<AppDatabase>().consumoDao() }
}

/** Módulo de repositórios */
val repositoryModule = module {
    single<MaterialRepository> { MaterialRepositoryImpl(get(), get()) }
    single<ConsumoRepository> { ConsumoRepositoryImpl(get()) }
}

/** Módulo de ViewModels */
val viewModelModule = module {
    viewModel { MaterialViewModel(get(), get(), androidContext()) }
    viewModel { SyncViewModel(get(), get()) }
}

/** Lista de todos os módulos para inicialização */
val appModules = listOf(databaseModule, networkModule, repositoryModule, viewModelModule)
