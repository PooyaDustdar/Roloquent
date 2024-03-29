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
                AddTableTest.TblTestIntegerLength(5, "pooya", "dustdar"),
                AddTableTest.TblTestIntegerLength(6, "mohammad", "nasrabadi"),
                AddTableTest.TblTestIntegerLength(7, "ali", "nasrabadi")

            )
        )
    }

    @Test
    fun selectAll() {
        var selectedall = Select(AddTableTest.TblTestIntegerLength::class.java).list
    }

    @Test
    fun selectRow() {
        var selected = Select(
            AddTableTest.TblTestIntegerLength::class.java,
            arrayListOf("id", "name")
        ).list
    }

    @Test
    fun selectColumn() {
        var selected = Select(
            AddTableTest.TblTestIntegerLength::class.java, "id", Where("id", value = 5)
        ).list
    }

    @Test
    fun selectColumnRow() {
        var selected = Select(
            AddTableTest.TblTestIntegerLength::class.java,
            arrayListOf("id", "name"),
            arrayListOf(
                Where("id", value = 5),
                WhereOr("family", value = "nasrabadi"),
                Where("name", value = "ali")
            )
        ).list
    }

    @After
    fun after() {
        connection.removeTableIfExists(AddTableTest.TblTestIntegerLength::class.java)
    }

    var context: Context = ApplicationProvider.getApplicationContext<G>()
    var connection: Connection = Connection(context, "dbname")

}