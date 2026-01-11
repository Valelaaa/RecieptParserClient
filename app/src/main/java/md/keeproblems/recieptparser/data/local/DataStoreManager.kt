package md.keeproblems.recieptparser.data.local

import android.content.Context
import androidx.core.content.edit
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer

class DataStoreManager @Inject constructor(context: Context) {

    private val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    private val json = Json { ignoreUnknownKeys = true }
    // Save ANY type (including List)
    internal fun <T> put(key: String, value: T, serializer: KSerializer<T>) {
        val jsonString = json.encodeToString(serializer, value)
        prefs.edit { putString(key, jsonString) }
    }

    // Save List<T>
    internal fun <T> putList(key: String, list: List<T>, serializer: KSerializer<T>) {
        val jsonString = json.encodeToString(ListSerializer(serializer), list)
        prefs.edit { putString(key, jsonString) }
    }

    // Read ANY type
    internal fun <T> get(key: String, default: T, serializer: KSerializer<T>): T {
        val stored = prefs.getString(key, null) ?: return default
        return try {
            json.decodeFromString(serializer, stored)
        } catch (_: Exception) {
            default
        }
    }

    // Read List<T>
    internal fun <T> getList(key: String, serializer: KSerializer<T>): List<T> {
        val stored = prefs.getString(key, null) ?: return emptyList()
        return try {
            json.decodeFromString(ListSerializer(serializer), stored)
        } catch (_: Exception) {
            emptyList()
        }
    }

    fun remove(key: String) = prefs.edit { remove(key) }
    fun clear() = prefs.edit { clear() }
}
