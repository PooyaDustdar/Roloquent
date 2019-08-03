package ir.dustdar.roloquent

import android.content.Context
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import ir.dustdar.roloquent.Exceptions.DuplicateTableException
import ir.dustdar.roloquent.Exceptions.InvalidTableException
import org.junit.After
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.Long.Companion.MAX_VALUE


@RunWith(AndroidJUnit4::class)
class AddTableTest {

    data class TblTestDuplicateTable(@Field(length = MAX_VALUE) var id: Integer)
    data class TblTestIntegerLength(
        @Field var id: Integer,
        @Field var name: String,
        @Field var family: String
    )

    data class TblTestAutoIncerment(@Field(auto_increment = true) var id: Integer)
    data class TblAutoIncerment(@Field(auto_increment = true, primary_key = true) var id: Integer)
    data class TblNullablePrimaryKey(
        @Field(
            primary_key = true,
            nullable = false,
            unique = true,
            auto_increment = true
        ) var id: Integer
    )


    @Test(expected = DuplicateTableException::class)
    fun expectedDuplicateTable() {
        connection.addTable(TblTestDuplicateTable::class.java)
        connection.addTable(TblTestDuplicateTable::class.java)
    }

    @Test
    fun expectedIntegerLength() {
        connection.addTableIfNotExists(TblTestIntegerLength::class.java)
    }

    @Test(expected = InvalidTableException::class)
    fun expectedAutoIncerment() {
        connection.addTableIfNotExists(TblTestAutoIncerment::class.java)
    }

    @Test
    fun expectedNotNullablePrimaryKey() {
        connection.addTableIfNotExists(TblNullablePrimaryKey::class.java)
    }

    @Test
    fun AutoIncerment() {
        connection.addTableIfNotExists(TblAutoIncerment::class.java)
    }

    @Test
    fun getTablesName() {
        Log.e("TablesName", Connection.getTablesName().toString())
    }
    @Test
    fun removeTable() {
        connection.removeTableIfExists(TblNullablePrimaryKey::class.java)
        connection.removeTableIfExists(TblTestAutoIncerment::class.java)
        connection.removeTableIfExists(TblTestIntegerLength::class.java)
        connection.removeTableIfExists(TblAutoIncerment::class.java)
    }

    @After
    fun after() {
//        removeTable()
    }

    var context: Context = ApplicationProvider.getApplicationContext<G>()
    var connection: Connection = Connection(context, "dbname")

}