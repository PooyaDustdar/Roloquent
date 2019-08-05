package ir.dustdar.roloquent

open class Option(var column: String, var Oprator: String = "=", var value: String)


class Where(column: String, Oprator: String = "=", value: Any) : Option(column, Oprator, value.toString())
class WhereOr(column: String, Oprator: String = "=", value: String) : Option(column,Oprator,value)