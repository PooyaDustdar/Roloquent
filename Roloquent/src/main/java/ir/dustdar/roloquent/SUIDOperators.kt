package ir.dustdar.roloquent

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


var connection = Connection


inline fun <reified T : Any> Insert(date: T) {
    Insert(arrayOf(date))
}

inline fun <reified T : Any> Insert(date: Array<T>) {
    var Query = "INSERT INTO `${T::class.java.name}` ("
    T::class.java.declaredFields.forEach {
        Query += "`" + it.name + "`,"
    }
    Query = Query.substring(0, Query.length - 1)
    Query += ") VALUES "
    date.forEach { itis ->
        Query += "("
        val fields = itis::class.java.declaredFields
        fields.forEach {
            it.isAccessible = true
            val value = it.get(itis)
            Query += "'$value',"
        }
        Query = Query.substring(0, Query.length - 1) + "),"
    }
    Query = Query.substring(0, Query.length - 1) + ";"
    connection.db.execSQL(Query)
}

