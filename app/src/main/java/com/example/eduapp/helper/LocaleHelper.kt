package com.example.eduapp.helper

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

/**
 * Utility object for managing application localization at runtime.
 */
object LocaleHelper {
    
    fun setLocale(context: Context, languageCode: String): Context {
        // Modern approach to creating a Locale from a language tag
        val locale = Locale.forLanguageTag(languageCode)
        Locale.setDefault(locale)
        
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        
        // Returns a configuration context for use within the Compose hierarchy
        return context.createConfigurationContext(config)
    }
}
