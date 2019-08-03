package ir.dustdar.roloquent

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class SelectTest {

    @Before
    fun before() {
        connection.addTableIfNotExists(AddTableTest.TblTestIntegerLength::class.java)
        Insert(
            arrayOf(
                AddTableTest.TblTestIntegerLength(Integer(5), "pooya", "dustdar"),
                AddTableTest.TblTestIntegerLength(Integer(6), "mohammad", "nasrabadi"),
                AddTableTest.TblTestIntegerLength(Integer(7), "ali", "nasrabadi")

            )
        )
    }

    @Test
    fun selectAll() {
        var selectedall = Select<AddTableTest.TblTestIntegerLength>()
    }

    @Test
    fun selectRow() {
        var selected = Select<AddTableTest.TblTestIntegerLength>(arrayOf("id", "name"))
    }

    @Test
    fun selectColumn() {
        var selected = Select<AddTableTest.TblTestIntegerLength>(
            options = arrayListOf(
                Where("id", value = 5),
                WhereOr("family", value = "nasrabadi"),
                Where("name", value = "ali")
            )
        )
    }

    @Test
    fun selectColumnRow() {
        var selected = Select<AddTableTest.TblTestIntegerLength>(
            arrayOf("id", "name"),
            arrayListOf(
                Where("id", value = 5),
                WhereOr("family", value = "nasrabadi"),
                Where("name", value = "ali")
            )
        )
    }

    @Test
    fun UpdateColumnRow() {
        Select<AddTableTest.TblTestIntegerLength>(
            arrayOf("id", "name"),
            arrayListOf(
                Where("id", value = 5),
                WhereOr("family", value = "nasrabadi"),
                Where("name", value = "ali")
            )
        ).Update(AddTableTest.TblTestIntegerLength(Integer(9),"",""))
    }

    @After
    fun after() {
        connection.removeTableIfExists(AddTableTest.TblTestIntegerLength::class.java)
    }

    var context: Context = ApplicationProvider.getApplicationContext<G>()
    var connection: Connection = Connection(context, "dbname")

}