package ir.dustdar.roloquent

@Target(AnnotationTarget.FIELD)
annotation class Field(
    val min: Long = 0,
    val max: Long = 0,
    val length: Long = 0,
    val regex: String = "",
    val auto_increment: Boolean = false,
    val primary_key: Boolean = false,
    val nullable: Boolean = true,
    val unique: Boolean = false
)