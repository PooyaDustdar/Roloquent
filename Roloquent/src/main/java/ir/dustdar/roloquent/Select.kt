package ir.dustdar.roloquent

import android.database.sqlite.SQLiteException
import com.google.gson.Gson

class Select<T : Any> {
    private var kclass: Class<T>
    private var options: ArrayList<Option> = ArrayList()
    private var fields: ArrayList<String> = arrayListOf("*")
    private var classInstans: T


    private val where: String
        get() {
            var whereBuilder = ""
            if (options.size > 0) {
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
            }
            return whereBuilder
        }

    private val selectQuery: String
        get() {
            var filds = ""
            fields.forEach {
                filds += "$it,"
            }
            if (!filds.isEmpty())
                filds = filds.substring(0, filds.length - 1)

            return "SELECT $filds FROM `${kclass.name}` $where;"
        }

    private val updateQuery: String
        get() {
            var fieldsData = ""
            fields.forEach {
                val field = kclass.getDeclaredField(it)
                field.isAccessible = true

                val value = field.get(classInstans)
                fieldsData += when (value) {
                    is String -> "`$it` = '$value',"
                    is Int -> "`$it` =$value,"
                    else -> ""
                }
            }
            fieldsData = fieldsData.substring(0, fieldsData.length - 1)

            return "UPDATE `${kclass.name}` SET $fieldsData $where;"
        }


    val list: ArrayList<T>?
        get() {
            val retention: ArrayList<T> = ArrayList()
            val array: ArrayList<String> = connection.getTablesName()
            if (kclass.name in array) {
                val cursor = connection.db.rawQuery(selectQuery, null)
                if (cursor.moveToFirst())
                    do {
                        val gson = Gson()
                        var json = "{"
                        cursor.columnNames.forEach {
                            val columnIndex = cursor.getColumnIndex(it)
                            val value = cursor.getString(columnIndex)
                            json += "\"$it\":\"$value\","
                        }
                        json = json.substring(0, json.length - 1)
                        json += "}"
                        val gsondata = gson.fromJson(json, kclass)
                        retention.add(gsondata)
                    } while (cursor.moveToNext())
                return retention
            } else
                throw Exceptions.TableNotFoundException("Table '${kclass.name}' is not found.")
        }


    fun Update(data: T): Boolean {
        classInstans = data
        return try {
            connection.db.execSQL(updateQuery)
            true
        } catch (e: SQLiteException) {
            false
        }

    }

    constructor(classIndet: Class<T>) {
        this.kclass = classIndet
        this.classInstans = classIndet.newInstance()
    }

    constructor(classIndet: Class<T>, fields: ArrayList<String>) {
        this.fields = fields
        this.kclass = classIndet
        this.classInstans = classIndet.newInstance()

    }

    constructor(classIndet: Class<T>, options: List<Option>) {
        this.options = ArrayList(options)
        this.kclass = classIndet
        this.classInstans = classIndet.newInstance()

    }

    constructor(classIndet: Class<T>, field: String) {
        this.fields.add(field)
        this.kclass = classIndet
        this.classInstans = classIndet.newInstance()

    }

    constructor(classIndet: Class<T>, option: Option?) {
        if (option != null)
            this.options.add(option)
        this.kclass = classIndet
        this.classInstans = classIndet.newInstance()
    }

    constructor(
        classIndet: Class<T>,
        fields: ArrayList<String> = ArrayList(),
        options: ArrayList<Option> = ArrayList()
    ) {
        this.options = options
        this.fields = fields
        this.kclass = classIndet
        this.classInstans = classIndet.newInstance()
    }

    constructor(classIndet: Class<T>, fields: ArrayList<String> = ArrayList(), option: Option? = null) {
        if (option != null)
            this.options.add(option)
        this.fields = fields
        this.kclass = classIndet
        this.classInstans = classIndet.newInstance()
    }

    constructor(classIndet: Class<T>, field: String = "*", options: ArrayList<Option> = ArrayList()) {
        this.options = options
        fields = arrayListOf(field)
        this.kclass = classIndet
        this.classInstans = classIndet.newInstance()
    }

    constructor(classIndet: Class<T>, field: String = "*", option: Option? = null) {
        if (option != null)
            this.options.add(option)
        fields = arrayListOf(field)
        this.kclass = classIndet
        this.classInstans = classIndet.newInstance()
    }

}
