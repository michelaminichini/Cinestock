package progetto.cinema.cinestock.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import progetto.cinema.cinestock.models.login.TmdbApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val apiKey = "e96d473555668ee67739012c7f140604"

    private val tmdbApiService = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(TmdbApiService::class.java)

    fun register(username: String, password: String) = viewModelScope.launch {

        // Chiamata all'API TMDb per ottenere un token di richiesta
        val tokenResponse = tmdbApiService.createRequestToken(apiKey)
        val requestToken = tokenResponse.request_token

    }

    fun login(username: String, password: String) = viewModelScope.launch {

        // Chiamata all'API TMDb per creare una sessione ospite
        val guestSessionResponse = tmdbApiService.createGuestSession(apiKey)
        val guestSessionId = guestSessionResponse.guest_session_id

    }
}