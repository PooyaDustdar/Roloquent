package ir.dustdar.roloquent


class Update(private val tableName: String) {
    private var Query: String? = null
    private val connection = Connection.sqLiteDatabase

    fun setValues(field: String, value: String): Update {
        Query = "UPDATE `$tableName` SET `$field`=\"$value\""
        return this
    }

    fun where(filed: String, value: String): Update {
        return where(filed, "=", value)
    }

    fun where(filed: String, operator: String, value: String): Update {
        Query += " WHERE `$filed`$operator\"$value\""
        return this
    }

    fun whereQuery(filed: String, value: String): Update {
        return whereQuery(filed, "=", value)
    }

    fun whereQuery(filed: String, operator: String, value: String): Update {
        Query += "`$filed`$operator($value)"
        return this
    }

    fun andWhere(filed: String, value: String): Update {
        return andWhere(filed, "=", value)
    }

    fun andWhere(filed: String, operator: String, value: String): Update {
        if (Query != null) {
            Query += " AND `$filed`$operator \"$value\""
        }
        return this
    }

    fun run() {
        connection.execSQL(Query)
    }
}
