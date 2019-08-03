package ir.dustdar.roloquent


class Properties(private var field: String) {

    fun setKey(): Properties {
        return Properties("$field PRIMARY KEY")
    }

    fun setAutoIncerement(): Properties {
        return Properties("$field AUTOINCREMENT")
    }

    fun setUnique(): Properties {
        return Properties("$field UNIQUE")
    }

    override fun toString(): String {
        return field
    }

    fun setNotNull(): Properties {
        return Properties("$field NOT NULL")
    }
}
