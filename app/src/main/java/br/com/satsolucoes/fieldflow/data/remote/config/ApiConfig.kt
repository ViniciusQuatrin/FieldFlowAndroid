package br.com.satsolucoes.fieldflow.data.remote.config

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Enum para os modos de API disponíveis
 */
enum class ApiMode {
    FAKE,   // Usa FakeMaterialApiService (dados mockados)
    SERVER  // Usa servidor real via Retrofit
}

/**
 * Configuração centralizada da API.
 * Permite alternar entre modo Fake e Servidor real em runtime.
 */
object ApiConfig {
    private const val PREFS_NAME = "fieldflow_api_config"
    private const val KEY_API_MODE = "api_mode"
    private const val KEY_SERVER_URL = "server_url"

    // URL padrão do servidor (10.0.2.2 é localhost no emulador Android)
    const val DEFAULT_SERVER_URL = "http://10.0.2.2:8081/"

    private lateinit var prefs: SharedPreferences

    private val _apiMode = MutableStateFlow(ApiMode.FAKE)
    val apiMode: StateFlow<ApiMode> = _apiMode.asStateFlow()

    private val _serverUrl = MutableStateFlow(DEFAULT_SERVER_URL)
    val serverUrl: StateFlow<String> = _serverUrl.asStateFlow()

    private val _configChanged = MutableStateFlow(0L)
    val configChanged: StateFlow<Long> = _configChanged.asStateFlow()

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        // Carrega configurações salvas
        val savedMode = prefs.getString(KEY_API_MODE, ApiMode.FAKE.name)
        _apiMode.value = ApiMode.valueOf(savedMode ?: ApiMode.FAKE.name)

        val savedUrl = prefs.getString(KEY_SERVER_URL, DEFAULT_SERVER_URL)
        _serverUrl.value = savedUrl ?: DEFAULT_SERVER_URL
    }

    fun setApiMode(mode: ApiMode) {
        _apiMode.value = mode
        prefs.edit().putString(KEY_API_MODE, mode.name).apply()
        notifyConfigChanged()
    }

    fun setServerUrl(url: String) {
        val normalizedUrl = if (url.endsWith("/")) url else "$url/"
        _serverUrl.value = normalizedUrl
        prefs.edit().putString(KEY_SERVER_URL, normalizedUrl).apply()
        notifyConfigChanged()
    }

    fun isUsingFakeApi(): Boolean = _apiMode.value == ApiMode.FAKE

    fun isUsingServer(): Boolean = _apiMode.value == ApiMode.SERVER

    private fun notifyConfigChanged() {
        _configChanged.value = System.currentTimeMillis()
    }
}

