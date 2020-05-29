package artifact.preferences.daemon

import android.content.Context
import artifact.preferences.contract.PreferencesFile

object InitializationDaemon {

    private lateinit var context: Context

    private val preferenceFiles: MutableList<PreferencesFile> = mutableListOf()

    private fun initializePreferenceFiles() {
        preferenceFiles.forEach {
            it.init(context)
        }
    }

    fun init(context: Context) {
        if (this::context.isInitialized)
            return
        this.context = context
        initializePreferenceFiles()
    }

    fun attachPreferenceFile(file: PreferencesFile) {
        preferenceFiles.add(file)
        if (this::context.isInitialized)
            file.init(context)
    }
}