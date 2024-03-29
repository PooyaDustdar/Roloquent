package ir.dustdar.roloquent

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class RemoveTest {

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
        InsertOrUpdate(
            arrayOf(
                AddTableTest.TblTestIntegerLength(5, "mohammad", "nasrabadi"),
                AddTableTest.TblTestIntegerLength(7, "ali", "dustdar"),
                AddTableTest.TblTestIntegerLength(6, "pooya", "nasrabadi")


            )
        )
    }

    @Test
    fun updateAfterSelect() {
        val data = AddTableTest.TblTestIntegerLength()
        data.id = Integer(9)
        val update = Select(
            AddTableTest.TblTestIntegerLength::class.java,
            "id",
            Where("family", value = "nasrabadi")
        ).Remove()
        val listafterupdate = Select(AddTableTest.TblTestIntegerLength::class.java).list
        Assert.assertEquals(9, listafterupdate!![0].id)
    }

    @After
    fun after() {
        connection.removeTableIfExists(AddTableTest.TblTestIntegerLength::class.java)
    }

    var context: Context = ApplicationProvider.getApplicationContext<G>()
    var connection: Connection = Connection(context, "dbname")

}