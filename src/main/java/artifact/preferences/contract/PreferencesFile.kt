package artifact.preferences.contract

import android.content.Context
import android.content.SharedPreferences
import artifact.preferences.model.Property
import com.google.gson.Gson
import java.lang.reflect.Type

abstract class PreferencesFile {
    protected abstract val fileName: String
    protected abstract val mode: Int

    private lateinit var sharedPreferenceInstance: SharedPreferences

    private val properties: ArrayList<Property<*>> = ArrayList()

    open fun init(context: Context) {
        sharedPreferenceInstance = context.getSharedPreferences(fileName, mode)
        properties.forEach {
            it.init()
        }
    }

    fun <T> retrieveValue(propertyName: String, type: Type): T? {
        val serializedValue = sharedPreferenceInstance.getString(propertyName, null)
        return serializedValue?.let {
            Gson().fromJson<T>(it, type)
        }
    }

    fun <T> storeValue(propertyName: String, value: T) {
        val serializedValue = Gson().toJson(value)
        with(sharedPreferenceInstance.edit()) {
            putString(propertyName, serializedValue)
            apply()
        }
    }

    fun deleteValue(propertyName: String) {
        with(sharedPreferenceInstance.edit()) {
            remove(propertyName)
            apply()
        }
    }

    fun attachProperty(property: Property<*>) {
        properties.add(property)
        if (this::sharedPreferenceInstance.isInitialized)
            property.init()
    }

    fun clearFile() {
        with(sharedPreferenceInstance.edit()) {
            clear()
            commit()
        }
    }
}