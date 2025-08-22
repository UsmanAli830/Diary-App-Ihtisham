package com.example.diarywithlock.security


import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class SecureStorage(context: Context) {

    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    private val prefs = EncryptedSharedPreferences.create(
        "secure_prefs",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    companion object {
        private const val KEY_PIN = "pin_code"
        private const val KEY_FINGERPRINT_ENABLED = "fingerprint_enabled"
    }

    fun setPin(pin: String) {
        prefs.edit().putString(KEY_PIN, pin).apply()
    }

    fun getPin(): String? {
        return prefs.getString(KEY_PIN, null)
    }

    fun isPinSet(): Boolean {
        return prefs.contains(KEY_PIN)
    }

    fun checkPin(pin: String): Boolean {
        return getPin() == pin
    }

    fun setFingerprintEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_FINGERPRINT_ENABLED, enabled).apply()
    }

    fun isFingerprintEnabled(): Boolean {
        return prefs.getBoolean(KEY_FINGERPRINT_ENABLED, false)
    }
}
