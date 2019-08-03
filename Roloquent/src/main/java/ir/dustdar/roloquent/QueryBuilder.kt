package ir.dustdar.roloquent

import java.lang.reflect.Field
import ir.dustdar.roloquent.Exceptions.*


internal fun Field.getQuery(): String {
    val annotation = getAnnotation(ir.dustdar.roloquent.Field::class.java)
    var field: String
    if (annotation != null) {
        field = when (this.type.name) {
            "java.lang.String" -> when {
                annotation.length.toInt() == 0 -> "`$name` TEXT"
                else -> "`$name` VARCHAR(" + annotation.length + ")"
            }
            "java.lang.Integer" -> {
                if (annotation.length > 0)
                    throw InvalidTableException("You Can't Set Length to Integer type.")
                "`$name` INTEGER"
            }
            "int" -> when {
                annotation.length.toInt() == 0 ->
                    throw InvalidTableException("You Must to be set Length to Int type.")
                annotation.length < 3 ->
                    "`$name` Tinyint(" + annotation.length + ")"
                annotation.length < 5 ->
                    "`$name` Smallint(" + annotation.length + ")"
                annotation.length < 10 ->
                    "`$name` Int(" + annotation.length + ")"
                else ->
                    "`$name` Bigint(" + annotation.length + ")"
            }
            else -> ""
        }
        if (!annotation.nullable) {
            field = "$field NOT NULL"
        }

        if (annotation.primary_key) {
            if (this.type.name != "java.lang.Integer") {
                throw InvalidTableException("The PRIMARY KEY field can only of Integer type.")
            }

            //...
            field = "$field PRIMARY KEY"
        }

        if (annotation.auto_increment) {
            if (this.type.name != "java.lang.Integer") {
                throw InvalidTableException("The AUTOINCREMENT field can only of Integer type.")
            } else if (!annotation.primary_key) {
                throw InvalidTableException("The AUTOINCREMENT field Must be primary_key.")
            }

            //...
            field = "$field AUTOINCREMENT"
        }

        if (annotation.unique) {
            field = "$field UNIQUE"
        }


    } else {
        field = when (this.type.name) {
            "java.lang.String" -> "`$name` TEXT"
            "java.lang.Integer" -> "`$name` INTEGER"
            "int" -> "`$name` Bigint(12)"
            else -> ""
        }
    }
    return field
}

internal fun Class<*>.getQuery(): String {
    var fields = ""
    this.declaredFields.forEach {
        fields += it.getQuery() + ","
    }
    fields = fields.substring(0, fields.length - 1)
    return "`$name` ($fields)"
}
