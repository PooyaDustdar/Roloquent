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
                AddTableTest.TblTestIntegerLength(5,"pooya","dustdar"),
                AddTableTest.TblTestIntegerLength(6,"mohammad","nasrabadi")
            )
        )
    }

    @Test
    fun insertItem() {
        Insert(
            AddTableTest.TblTestIntegerLength(7,"ali","nasrabadi")
        )
    }


    @After
    fun after() {

    }

    var context: Context = ApplicationProvider.getApplicationContext<G>()
    var connection: Connection = Connection(context, "dbname")

}