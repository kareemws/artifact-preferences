package artifact.preferences.daemon

import android.content.Context
import artifact.preferences.contract.PreferencesFile

object InitializationDaemon {

    private lateinit var context: Context

    private val preferenceFiles: MutableList<Pair<PreferencesFile, Boolean>> = mutableListOf()

    private fun initializePreferenceFiles() {
        preferenceFiles.forEach {
            it.first.init(context, it.second)
        }
        preferenceFiles.clear()
    }

    fun init(context: Context) {
        if (this::context.isInitialized)
            return
        this.context = context
        initializePreferenceFiles()
    }

    fun attachPreferenceFile(file: PreferencesFile, clearOnInit: Boolean = false) {
        if (this::context.isInitialized)
            file.init(context, clearOnInit)
        else
            preferenceFiles.add(Pair(file, clearOnInit))
    }
}