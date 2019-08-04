package ir.dustdar.roloquent

import java.util.ArrayList

class SelectedList<T> : ArrayList<T> {

    constructor() : super()
    constructor(initialCapacity: Int) : super(initialCapacity)
    constructor(c: Collection<T>) : super(c)

    data class Field(var column:String,var value: String)

    fun Update(data:T){

    }
    fun Update(data: ArrayList<Field>){

    }

}
