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


inline fun <reified T : Any> Select( option: Option): SelectedList<T> {
    return Select(options = arrayListOf(option))
}

inline fun <reified T : Any> Select(field: String, option: Option): SelectedList<T> {
    return Select(arrayOf(field), options = arrayListOf(option))
}


inline fun <reified T : Any> Select(field: String): SelectedList<T> {
    return Select(fields = arrayOf(field))
}


inline fun <reified T : Any> Select(
    fields: Array<String> = arrayOf("*"),
    options: ArrayList<Option> = ArrayList()
): SelectedList<T> {
    val retention: SelectedList<T> = (arrayListOf<T>() as SelectedList<T>)
    val array: ArrayList<String> = connection.getTablesName()
    if (T::class.java.name in array) {
        var filds = ""
        fields.forEach {
            filds += "$it,"
        }
        filds = filds.substring(0, filds.length - 1)
        val where: String = if (options.size > 0) {
            var whereBuilder = ""
            options.forEach {
                if (it::class.java.name == "ir.dustdar.roloquent.WhereOr") {
                    if (whereBuilder == "") {
                        throw Exceptions.InvaleidSelectQuery("you can't use WhereOr in first option")
                    } else {
                        whereBuilder += " OR `${it.column}` ${it.Oprator} '${it.value}'"
                    }
                } else if (it::class.java.name == "ir.dustdar.roloquent.Where") {
                    whereBuilder += if (whereBuilder.isEmpty()) {
                        "WHERE `${it.column}` ${it.Oprator} '${it.value}'"
                    } else {
                        " AND `${it.column}` ${it.Oprator} '${it.value}'"
                    }
                }
            }
            whereBuilder
        } else ""
        val Query = "SELECT $filds FROM `${T::class.java.name}` $where;"
        val cursor = connection.db.rawQuery(Query, null)
        if (cursor.moveToFirst())
            do {
                val gson = Gson()
                val fooType = object : TypeToken<T>() {}.type
                var json = "{"
                cursor.columnNames.forEach {
                    val columnIndex = cursor.getColumnIndex(it)
                    val value = cursor.getString(columnIndex)
                    json += "\"$it\":\"$value\","
                }
                json = json.substring(0, json.length - 1)
                json += "}"
                retention.add(gson.fromJson(json, fooType))
            } while (cursor.moveToNext())
        return retention
    } else
        throw Exceptions.TableNotFoundException("Table '${T::class.java.name}' is not found.")
}