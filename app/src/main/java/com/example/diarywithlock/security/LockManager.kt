package com.example.diarywithlock.security


import android.content.Context

class LockManager(context: Context) {

    private val secureStorage = SecureStorage(context)
    private val biometricHelper = BiometricHelper(context)

    fun shouldUseFingerprint(): Boolean {
        return secureStorage.isFingerprintEnabled() && biometricHelper.isBiometricAvailable()
    }

    fun isPinRequired(): Boolean {
        return secureStorage.isPinSet()
    }

    fun verifyPin(pin: String): Boolean {
        return secureStorage.checkPin(pin)
    }

    fun savePin(pin: String) {
        secureStorage.setPin(pin)
    }

    fun enableFingerprint(enabled: Boolean) {
        secureStorage.setFingerprintEnabled(enabled)
    }
}
