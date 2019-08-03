package ir.dustdar.roloquent

import android.content.Context
import android.content.pm.PackageManager
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import ir.dustdar.roloquent.Exceptions.DuplicateTableException
import java.io.FileOutputStream
import java.io.IOException
import ir.dustdar.roloquent.Exceptions.*
open class Connection(private val context: Context, dbname: String) {
    private val databaseFile: String = context.getDatabasePath(dbname).path
    init {
        sqLiteDatabase = context.openOrCreateDatabase(databaseFile, Context.MODE_PRIVATE, null)
        try {
            sqLiteDatabase.version = context.packageManager.getPackageInfo(context.packageName, 0).versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        Log.e("db", "opened")
    }
    @Throws(IOException::class)
    fun importDatabase(dbName: String) {
        val mInputStream = context.assets.open(dbName)
        val mOutputStream = FileOutputStream(databaseFile)
        val buffer = ByteArray(INT)
        var length = mInputStream.read(buffer)
        while (length > 0) {
            mOutputStream.write(buffer, 0, length)
            length = mInputStream.read(buffer)
        }
        mOutputStream.close()
        mInputStream.close()
    }
    fun getTablesName(): ArrayList<String> {
        val list: ArrayList<String> = ArrayList()
        var cursor = sqLiteDatabase.rawQuery(
            "SELECT name FROM sqlite_master WHERE type ='table' AND name NOT LIKE 'sqlite_%';",
            null
        )
        while (cursor.moveToNext()) {
            list.add(cursor.getString(cursor.getColumnIndex("name")))
        }
        return list
    }
    private var tables: ArrayList<Class<*>> = ArrayList()
    fun addTableIfNotExists(table: Class<*>) {
        try {
            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS" + table.getQuery())
        } catch (e: SQLException) {
            Log.e("ErrorOnCreateTable", e.toString())
        } finally {
            Log.v("CreateTable", "CREATE TABLE IF NOT EXISTS " + table.getQuery())
        }
    }
    fun addTable(table: Class<*>) {
        if (table !in tables) {
            if (table.name in getTablesName())
                throw DuplicateTableException(
                    "The " + table.name + " table is exists.\n" +
                            "Each table can only be Create once.\n" +
                            "please use addTablesIfNotExists to add Table"
                )
            tables.add(table)
            try {
                sqLiteDatabase.execSQL("CREATE TABLE " + table.getQuery())
            } catch (e: SQLException) {
                Log.e("ErrorOnCreateTable", e.toString())
            } finally {
                Log.v("CreateTable", "CREATE TABLE " + table.getQuery())
            }
        } else {
            throw DuplicateTableException("The " + table.name + " table is a duplicate.\n" +
                    "Each table can only be added once.")
        }
    }
    fun removeTableIfExists(table: Class<*>) {
        try {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS `${table.name}`;")
        } catch (e: SQLException) {
            Log.e("ErrorOnCreateTable", e.toString())
        } finally {
            Log.v("CreateTable", "CREATE TABLE IF EXISTS `${table.name}`;")
        }
        if (table in tables) {
            tables.remove(table)
        }
    }
    fun removeTable(table: Class<*>) {

        if (table.name !in getTablesName())
            throw TableNotFoundException(
                "The " + table.name + " table is not exists.\n" +
                        "Each table can only be Create once.\n" +
                        "please use addTablesIfNotExists to add Table")
        try {
            sqLiteDatabase.execSQL("DROP TABLE `${table.name}`;")
        } catch (e: SQLException) {
            Log.e("ErrorOnCreateTable", e.toString())
        } finally {
            Log.v("CreateTable", "CREATE TABLE " + table.getQuery())
        }

        if (table in tables) {
            tables.remove(table)
        }
    }
    override fun toString(): String {
        return "Connection{" +
                "Context=" + context +
                ", databaseFile='" + databaseFile + '\''.toString() +
                '}'.toString()
    }
    companion object {
        private val INT = 1024
        internal lateinit var sqLiteDatabase: SQLiteDatabase
    }
}