package dp.com.roomproject

import android.arch.lifecycle.Observer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import dp.com.roomproject.ui.main.MainFragment
import dp.com.roomproject.ui.main.Task
import dp.com.roomproject.ui.main.TaskDao
import kotlinx.android.synthetic.main.main_fragment.message
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private lateinit var dataBase: AppDataBase
    private lateinit var dao: TaskDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
        dataBase = AppDataBase.getInstance(this)
        dao = dataBase.taskDao()

        val task = Task(title = "Sample")
        addTask(task)

        //refreshTaskList()

        dao.observeTasks().observe(this, Observer<List<Task>>{
            it?.forEach {
                message.setText(it.title)
            }
        })

        val task2 = Task(title = "Sample2")
        addTask(task2)

        dao.getTask(1).observe(this, Observer { it: Task? ->
            if(it == null) {
                //when no task is done finish and exit
                finish()
                return@Observer
            }
            message.setText(it.title)
        })

        thread {
            dao.deleteTask(task)
        }

    }

    fun addTask(task:Task){
        thread {
            dao.insert(task)
        }
    }

    fun refreshTaskList(){
        thread {
            val tasks: List<Task> = dao.getAll()
            runOnUiThread {
                tasks.forEach {
                    message.setText(it.title)
                }
            }
        }
    }

}
