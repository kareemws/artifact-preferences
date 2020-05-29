package artifact.preferences.model

import artifact.preferences.contract.PreferencesFile
import java.lang.reflect.Type

class Property<T> @JvmOverloads constructor(
    private val name: String,
    private val preferencesFile: PreferencesFile,
    private val type: Type,
    private val initValue: T? = null
) {

    private var isInitialized = false

    var value: T? = null
        set(value) {
            field = when {
                value != null -> {
                    preferencesFile.storeValue(name, value)
                    value
                }
                isInitialized -> {
                    preferencesFile.deleteValue(name)
                    null
                }
                else -> {
                    isInitialized = true
                    preferencesFile.retrieveValue(name, type)
                }
            }
        }

    init {
        preferencesFile.attachProperty(this)
    }

    fun init() {
        value = initValue
    }
}