package ir.dustdar.roloquent

import android.util.Log

class InsertOrPeplace {
    private lateinit var Query: String
    private lateinit var tableName: String
    private val connection = Connection.db

    constructor(fields: Array<String>, values: Array<String>, tableName: String) {

    }

    constructor(fields: Array<String>, values: Array<Array<String>>, tableName: String) {

    }

    constructor(values: Array<Array<String>>, tableName: String) {

    }

    constructor(values: Map<String, String>, tableName: String) {

    }

    constructor(values: Array<String>, tableName: String) {
        this.tableName = tableName
        Query = "INSERT OR REPLACE INTO `$tableName` VALUES ("
        for (value in values) {
            if (value == null)
                Query += "null,"
            else
                Query += "\"$value\","
        }
        Query = Query.substring(0, Query.length - 1)
        Query += ");"
    }

    fun run(): InsertOrPeplace {
        connection.execSQL(Query)
        Log.e("SQLITE", Query)
        return this
    }

    fun getid(): Int {
        val cursor = connection.rawQuery("SELECT `ID` FROM $tableName ORDER BY `ID` DESC LIMIT 1", null)
        return if (cursor.moveToFirst()) {
            cursor.getInt(0)
        } else 0
    }
}
