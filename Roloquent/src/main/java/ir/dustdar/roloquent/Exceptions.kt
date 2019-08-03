package ir.dustdar.roloquent

class Exceptions {
    class TableNotFoundException(s: String) : Exception(s)
    class DuplicateTableException(s: String) : Exception(s)
    class InvalidTableException(s: String) : Exception(s)
    class InvaleidSelectQuery(s: String) : Exception(s)
}
