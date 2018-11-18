package dp.com.roomproject.ui.main

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Relation
import android.arch.persistence.room.Transaction
import android.arch.persistence.room.Update

@Dao
interface TaskDao {
    @Insert
    fun insert(task: Task):Long

    @Insert
    fun insertAll(tasks:List<Task>):List<Long>

    @Query("SELECT * FROM Task")
    fun getAll():List<Task>

    @Query("SELECT * FROM Task")
    fun observeTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM Task WHERE id =:taskId")
    fun getTask(taskId:Int): LiveData<Task>

    @Delete
    fun deleteTask(task:Task)

    @Query("DELETE FROM Task WHERE id =:taskId")
    fun deleteTask(taskId: Int )

    @Update
    fun updateTask(task: Task)

    @Query("UPDATE  Task SET title =:taskTitle, completed=:taskIsCompleted  WHERE id =:taskId")
    fun updateTask(taskId: Int ,taskTitle:String,taskIsCompleted:Boolean)

    /*
    //how to define a SQL Joint to make sure a user assigned with the task.
    //lets define a class

    @Query("SELECT Task.*, User.id as user_id, User.name as user_name " + "FROM Task LEFT OUTER JOIN User ON Task.userId == User.id " +  "WHERE Task.id = :taskId")
    fun getTaskAndUser(taskId: Int): LiveData<UserTask>

    /*
        @Query("SELECT Task.*, User.* " + "FROM Task LEFT OUTER JOIN User ON Task.userId == User.id" +  "WHERE Task.id = :taskID")
        fun getTaskAndUser(taskId: Int): LiveData<UserTask>

        We want all fields from User and Task Table and want to Join User and Task Table Together such that Task.userId == User.id.
        The result set of this is constrained by WHERE clause for the taskID

        With this Room Will Complain there are multiple fields with same coulmn name . Both Task and User has a field called id and they conflict

        Error message is shown below

        /Users/Envoy/KiVi_Android_Code/RoomProject/app/build/tmp/kapt3/stubs/debug/dp/com/roomproject/ui/main/TaskDao.java:47: error: no viable alternative at input 'OUTER'
        public abstract android.arch.lifecycle.LiveData<dp.com.roomproject.ui.main.TaskDao.UserTask> getTaskAndUser(int taskId);

        So we rename one of the conflicting fields

        How ? Add a prefix and tell the @Embedded attribute about the prefix .
         Step 1 rename
         @Query("SELECT Task.*, User.id as user_id, User.name as user_name * " + "FROM LEFT OUTER JOIN User ON Task.userId == User.id" +  "WHERE Task.id = :taskID")

         STep 2 add that to the prefixed
           data class UserTask(@Embedded(prefix = "user_") var user: User?, @Embedded var task: Task)

     */
    data class UserTask(@Embedded(prefix = "user_") var user: User?, @Embedded var task: Task)
*/

    @Transaction
    @Query("SELECT * FROM User")
    fun getAllUsersAndTasks():LiveData<List<UserAndTasks>>

    //using Relation
    class UserAndTasks {
        @Embedded
        lateinit var user: User

        //Parent is User  and Entity is Task
        @Relation(parentColumn = "id", entityColumn = "userId")
        lateinit var tasks: List<Task>

    }
}

