package com.example.aleppocollage.ui.base

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import androidx.core.os.ConfigurationCompat
import com.example.aleppocollage.util.Common
import io.paperdb.Paper
import java.util.*

class LocaleUtil(base: Context) : ContextWrapper(base) {
    companion object {
        private val supportedLocales = listOf("en", "ar")
        private const val OPTION_PHONE_LANGUAGE = "sys_def"

        /**
         * returns the locale to use depending on the preference value
         * when preference value = "sys_def" returns the locale of current system
         * else it returns the locale code e.g. "en", "ar" etc.
         */
        private fun getLocaleFromLang(lang: String): Locale {
            val localeCode = if (lang != OPTION_PHONE_LANGUAGE) {
                lang
            } else {
                val systemLang = ConfigurationCompat.getLocales(Resources.getSystem().configuration)
                    .get(0).language
                if (systemLang in supportedLocales) {
                    systemLang
                } else {
                    "ar"
                }
            }

            var appCountry = Paper.book().read(Common.APP_COUNTRY, "")
            return Locale(localeCode, appCountry)
        }

        fun getLocalizedConfiguration(context: Context, prefLocaleCode: String): Configuration {

            getLocalizedResources(context.resources, prefLocaleCode)

            val locale = getLocaleFromLang(prefLocaleCode)
            return getLocalizedConfiguration(locale)
        }

        private fun getLocalizedConfiguration(locale: Locale): Configuration {
            val config = Configuration()
            return config.apply {
                config.setLayoutDirection(locale)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    config.setLocale(locale)
                    val localeList = LocaleList(locale)
                    LocaleList.setDefault(localeList)
                    config.setLocales(localeList)
                } else {
                    config.setLocale(locale)
                }
            }
        }

        fun getLocalizedContext(baseContext: Context, prefLocaleCode: String): Context {
            val currentLocale = getLocaleFromLang(prefLocaleCode)
            val baseLocale = getLocaleFromConfiguration(baseContext.resources.configuration)
            Locale.setDefault(currentLocale)
            return if (!baseLocale.toString().equals(currentLocale.toString(), ignoreCase = true)) {
                val config = getLocalizedConfiguration(baseContext, prefLocaleCode)
                baseContext.createConfigurationContext(config)
                baseContext
            } else {
                baseContext
            }
        }

        fun applyLocalizedContext(baseContext: Context, prefLocaleCode: String) {
            val currentLocale = getLocaleFromLang(prefLocaleCode)
            val baseLocale = getLocaleFromConfiguration(baseContext.resources.configuration)
            Locale.setDefault(currentLocale)
            if (!baseLocale.toString().equals(currentLocale.toString(), ignoreCase = true)) {
                val config = getLocalizedConfiguration(baseContext, prefLocaleCode)
                baseContext.resources.updateConfiguration(
                    config,
                    baseContext.resources.displayMetrics)
            }
        }

        @Suppress("DEPRECATION")
        private fun getLocaleFromConfiguration(configuration: Configuration): Locale {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                configuration.locales.get(0)
            } else {
                configuration.locale
            }
        }

        fun getLocalizedResources(resources: Resources, prefLocaleCode: String): Resources {
            val locale = getLocaleFromLang(prefLocaleCode)
            val config = resources.configuration
            @Suppress("DEPRECATION") config.locale = locale
            config.setLayoutDirection(locale)

            @Suppress("DEPRECATION") resources.updateConfiguration(config, resources.displayMetrics)
            return resources
        }
    }
}