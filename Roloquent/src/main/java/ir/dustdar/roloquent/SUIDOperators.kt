package ir.dustdar.roloquent


var connection = Connection.sqLiteDatabase

inline fun <reified T : Any> Insert(date: T) {
    var Query = "INSERT INTO `${T::class.java.name}` ("
    T::class.java.declaredFields.forEach {
        Query += "`" + it.name + "`,"
    }
    Query = Query.substring(0, Query.length - 1) + ") VALUES ("
    date::class.java.declaredFields.forEach {
        it.isAccessible = true
        Query += "'${it.get(date)}',"
    }
    Query = Query.substring(0, Query.length - 1) + ");"
    connection.execSQL(Query)
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
            val value = it.get(date[0])
            Query += "'$value',"
        }
        Query = Query.substring(0, Query.length - 1) + "),"
    }
    Query = Query.substring(0, Query.length - 1) + ";"
    connection.execSQL(Query)
}


inline fun <reified T : Any> Select(date: Class<T>, option: Option): ArrayList<T> {
    return Select(date, options = arrayListOf(option))
}

inline fun <reified T : Any> Select(date: Class<T>, field: String, option: Option): ArrayList<T> {
    return Select(date, arrayOf(field), options = arrayListOf(option))
}


inline fun <reified T : Any> Select(date: Class<T>, field: String): ArrayList<T> {
    return Select(date, fields = arrayOf(field))
}


inline fun <reified T : Any> Select(
    date: Class<T>,
    fields: Array<String> = arrayOf("*"),
    options: ArrayList<Option> = ArrayList()
): ArrayList<T> {
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
                if (whereBuilder.isEmpty()) {
                    whereBuilder += "WHERE `${it.column}` ${it.Oprator} '${it.value}'"
                } else {
                    whereBuilder += " AND `${it.column}` ${it.Oprator} '${it.value}'"
                }
            }
        }
        whereBuilder
    } else ""

    var Query = "SELECT $filds FROM `${T::class.java.name}` $where;"
    return arrayListOf()
}