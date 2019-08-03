package ir.dustdar.roloquent


class Delete(private val tblName: String) {
    private var Query: String? = null
    private val connection = Connection.db

    init {
        Query = "DELETE FROM `$tblName` "

    }

    fun where(filed: String, value: String): Delete {
        return where(filed, "=", value)
    }

    fun where(filed: String, operator: String, value: String): Delete {
        Query += "WHERE `$filed`$operator\"$value\""
        return this
    }

    fun whereQuery(filed: String, value: String): Delete {
        return whereQuery(filed, "=", value)
    }

    fun whereQuery(filed: String, operator: String, value: String): Delete {
        Query += "WHERE `$filed`$operator($value)"
        return this
    }

    fun andWhere(filed: String, value: String): Delete {
        return andWhere(filed, "=", value)
    }

    fun andWhere(filed: String, operator: String, value: String): Delete {
        if (Query != null) {
            Query += " AND `$filed`$operator \"$value\" "
        }
        return this
    }

    fun orWhere(filed: String, value: String): Delete {
        return orWhere(filed, "=", value)
    }

    fun orWhere(filed: String, operator: String, value: String): Delete {
        if (Query != null) {
            Query += " OR `$filed`$operator \"$value\" "
        }
        return this
    }

    fun run() {
        connection.execSQL(Query)
    }
}
