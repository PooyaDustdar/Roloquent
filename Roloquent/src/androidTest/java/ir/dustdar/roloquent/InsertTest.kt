package ir.dustdar.roloquent

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class InsertTest {
    @Test
    fun insertArrayOfItems() {
        Insert(
            arrayOf(
                AddTableTest.TblTestIntegerLength(Integer(5)),
                AddTableTest.TblTestIntegerLength(Integer(5))
            )
        )
    }

    @Test
    fun insertItem() {
        Insert(
            AddTableTest.TblTestIntegerLength(Integer(5))
        )
    }


    @After
    fun after() {

    }

    var context: Context = ApplicationProvider.getApplicationContext<G>()
    var connection: Connection = Connection(context, "dbname")

}