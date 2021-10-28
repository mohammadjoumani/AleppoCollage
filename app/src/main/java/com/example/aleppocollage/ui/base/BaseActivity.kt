package com.example.aleppocollage.ui.base

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.aleppocollage.util.Common
import io.paperdb.Paper

open class BaseActivity : AppCompatActivity() {

    private lateinit var oldPrefLocaleCode: String

    /**
     * updates the toolbar text locale if it set from the android:label property of Manifest
     */
    private fun resetTitle() {
        try {
            val label = packageManager.getActivityInfo(componentName, PackageManager.GET_META_DATA).labelRes;
            if (label != 0) {
                setTitle(label);
            }
        }
        catch (e: PackageManager.NameNotFoundException) {
        }
    }

    override fun attachBaseContext(newBase: Context) {
        // First Using
        Paper.book().write(Common.APP_LANG, Common.ARABIC_LOCALE_LANG)
        Paper.book().write(Common.APP_COUNTRY, Common.ARABIC_LOCALE_COUNTRY)
        //Paper.book().write(Common.APP_LANG, Common.ENGLISH_LOCALE_LANG)
        //Paper.book().write(Common.APP_COUNTRY, Common.ENGLISH_LOCALE_COUNTRY)
        var appLang = Paper.book().read(Common.APP_LANG, "")
        var appCountry = Paper.book().read(Common.APP_COUNTRY, "")

        oldPrefLocaleCode = appLang

        applyOverrideConfiguration(LocaleUtil.getLocalizedConfiguration(newBase, oldPrefLocaleCode))
        super.attachBaseContext(LocaleUtil.getLocalizedContext(newBase, appLang))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resetTitle()
    }

    override fun onResume() {
        super.onResume()
        var appLang = Paper.book().read(Common.APP_LANG, "")
        val currentLocaleCode = appLang
        if (oldPrefLocaleCode != currentLocaleCode) {
            recreate() //locale is changed, restart the activty to update
            oldPrefLocaleCode = currentLocaleCode
        }
    }
}