package ir.dustdar.roloquent

internal class Where(column: String, Oprator: String = "=", value: Any) : Option(column, Oprator, value.toString())
