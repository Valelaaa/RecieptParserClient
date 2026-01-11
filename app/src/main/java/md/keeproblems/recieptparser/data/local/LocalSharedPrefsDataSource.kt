package md.keeproblems.recieptparser.data.local

import kotlinx.serialization.KSerializer
import javax.inject.Inject

class LocalSharedPrefsDataSource<T : WithId> @Inject constructor(
    private val key: String,
    private val dataStore: DataStoreManager,
    private val serializer: KSerializer<T>
) : LocalDataSource<T> {

    private fun loadList(): MutableList<T> {
        return dataStore.getList(key, serializer).toMutableList()
    }

    override suspend fun getAll(): List<T> = loadList()

    override suspend fun save(item: T): Boolean {
        val list = loadList()
        return if (list.none { it.id == item.id }) {
            list.add(item)
            dataStore.putList(key, list, serializer)
            true
        } else {
            false
        }
    }

    override suspend fun saveAll(items: List<T>): Boolean {
        val list = loadList()
        var added = false
        items.forEach { incoming ->
            if (list.none { it.id == incoming.id }) {
                list.add(incoming)
                added = true
            }
        }
        if (added) {
            dataStore.putList(key, list, serializer)
        }
        return added
    }

    override suspend fun delete(id: String) {
        val list = loadList()
        list.removeAll { it.id == id }
        dataStore.putList(key, list, serializer)
    }

    override suspend fun clear() {
        dataStore.remove(key)
    }
}
