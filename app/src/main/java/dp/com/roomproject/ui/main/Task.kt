package dp.com.roomproject.ui.main

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

//Steps
//Create  a data class
//Change this data class to room by adding @Entity attribute
//Define a field as PK , note autoGenerate only works if the filed is Integer
//Also make sure change data class val types to var as Room needs getters.
//Then Register with DataBase ..Add to AppDataBase @DataBase version []
//@Entity
//data class Task(@PrimaryKey(autoGenerate = true)
//    var id: Int = 0,
//    var title: String = "",
//    var completed: Boolean = false,
//)

//When Room sees this class it creates a Table called Task

@Entity(
    foreignKeys = [(ForeignKey(
        entity = User::class, parentColumns = ["id"], childColumns = ["userId"], onDelete = ForeignKey.SET_NULL
    ))]
)
data class Task(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var title: String = "",
    var completed: Boolean = false,
    var userId: Int? // Our FK
) {

    @Ignore
    constructor(
        title: String = "",
        completed: Boolean = false,
        userId: Int? = null
    ) : this(0, title, completed, userId)
}