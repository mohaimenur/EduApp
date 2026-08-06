package com.example.eduapp.helper

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

/**
 * Utility object for managing application localization at runtime.
 */
object LocaleHelper {
    
    /**
     * Updates the application's locale settings and returns a new Context with the updated configuration.
     * 
     * @param context The base context to apply the locale to.
     * @param languageCode The ISO 639-1 language code (e.g., "en", "zh", "bn", "hi").
     * @return A new context wrapper with the localized configuration applied.
     */
    fun setLocale(context: Context, languageCode: String): Context {
        // Modern approach to creating a Locale from a language tag
        val locale = Locale.forLanguageTag(languageCode)
        Locale.setDefault(locale)
        
        val resources = context.resources
        val config = Configuration(resources.configuration)
        config.setLocale(locale)
        
        // updateConfiguration is required to refresh internal system resources (like dropdowns)
        resources.updateConfiguration(config, resources.displayMetrics)
        
        // Returns a configuration context for use within the Compose hierarchy
        return context.createConfigurationContext(config)
    }
}
