package ir.dustdar.roloquent


import android.database.Cursor


class Select internal constructor(Filed: String, tableName: String) {
    private var postions: Int? = null
    var query: String? = null
        private set

    private val connection = Connection.db

    val cursor: Cursor
        get() = connection.rawQuery(query, null)

    init {
        query = "SELECT $Filed FROM `$tableName`"
    }

    fun where(filed: String, value: String): Select {
        return where(filed, "=", value)
    }

    fun where(filed: String, operator: String, value: String): Select {
        if (query != null) {
            query += " WHERE `$filed`$operator \"$value\""
        }
        return this
    }

    fun andWhere(filed: String, value: String): Select {
        return andWhere(filed, "=", value)
    }

    fun andWhere(filed: String, operator: String, value: String): Select {
        if (query != null) {
            query += " AND `$filed`$operator \"$value\""
        }
        return this
    }

    fun orWhere(filed: String, operator: String, value: String): Select {
        if (query != null) {
            query += " or `$filed`$operator \"$value\""
        }
        return this
    }

    fun getString(value: String): String? {
        return getString(null, value, null)
    }

    fun getString(value: String, DefualtValue: String): String? {
        return getString(null, value, DefualtValue)
    }

    fun getString(postion: Int?, value: String, DefualtValue: String?): String? {
        val cursor = cursor
        val index = cursor.getColumnIndex(value)
        if (postion != null && postion >= 0) {
            if (cursor.move(postion)) {
                postions = cursor.position
                return cursor.getString(index)
            }
            return DefualtValue
        } else {
            return if (postions == null) {
                if (cursor.moveToFirst()) {
                    postions = cursor.position
                    return cursor.getString(index)
                }
                DefualtValue
            } else {
                if (cursor.move(postions!!)) {
                    postions = postions!! + 1
                    cursor.getString(index)
                } else DefualtValue
            }

        }
    }

    @JvmOverloads
    fun getStringPosion(postion: PosionSelector, value: String, DefualtValue: String? = null): String? {
        val cursor = cursor
        when (postion) {
            Select.PosionSelector.First -> if (cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndex(value))
            }
            Select.PosionSelector.Last -> if (cursor.moveToLast()) {
                return cursor.getString(cursor.getColumnIndex(value))
            }
        }
        return if (cursor.moveToFirst()) {
            cursor.getString(cursor.getColumnIndex(value))
        } else DefualtValue


    }

    fun getIntegre(value: String): Int? {
        return getIntegre(null, value, null)
    }

    fun getIntegre(value: String, DefualtValue: Int?): Int? {
        return getIntegre(null, value, DefualtValue)
    }

    @JvmOverloads
    fun getIntegrePosion(postion: PosionSelector, value: String, DefualtValue: Int? = null): Int? {
        val cursor = cursor
        var returner = DefualtValue!!
        when (postion) {
            Select.PosionSelector.First -> {
                if (cursor.moveToFirst()) {
                    returner = cursor.getInt(cursor.getColumnIndex(value))
                }
                return returner
            }
            Select.PosionSelector.Last -> {
                if (cursor.moveToLast()) {
                    returner = cursor.getInt(cursor.getColumnIndex(value))
                }
                return returner
            }
        }
        return if (cursor.moveToFirst()) {
            cursor.getInt(cursor.getColumnIndex(value))
        } else DefualtValue


    }

    fun getIntegre(postion: Int?, value: String, DefualtValue: Int?): Int? {
        val cursor = cursor
        if (postion != null && postion >= 0) {
            if (cursor.move(postion)) {
                postions = cursor.position
                return cursor.getInt(cursor.getColumnIndex(value))
            }
        } else {
            if (postions == null) {
                if (cursor.moveToFirst()) {
                    postions = cursor.position
                    return cursor.getInt(cursor.getColumnIndex(value))
                }
            } else {
                if (cursor.move(postions!!)) {
                    postions = postions!! + 1
                    return cursor.getInt(cursor.getColumnIndex(value))
                }
            }

        }
        return DefualtValue
    }

    fun like(search: String): Select {
        if (query != null) {
            query += " WHERE  "
        }
        return this
    }

    enum class PosionSelector {
        Last, First
    }
}
