package dp.com.roomproject

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import dp.com.roomproject.ui.main.Task
import dp.com.roomproject.ui.main.TaskDao
import dp.com.roomproject.ui.main.User

@Database(
    version = 1,entities = [
     //list of database entities
     Task::class,User::class

  ]
)
abstract class AppDataBase : RoomDatabase(){

    abstract fun taskDao ():TaskDao

    companion object {
        //keep it singleton until dagger is here to inject for others
        @Volatile private var INSTANCE : AppDataBase?=null
        fun getInstance(context:Context) :AppDataBase {
            return INSTANCE ?: synchronized(this){
                INSTANCE ?: buildDataBase(context).also { INSTANCE = it}
            }
        }

        private fun buildDataBase(context: Context):AppDataBase {
            //Always use application context , not activity context
            return Room.databaseBuilder(context.applicationContext,AppDataBase::class.java,"app-database").build()
        }
    }
}