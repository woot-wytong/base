package com.wyt.demo.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.blankj.utilcode.util.Utils
import com.wyt.demo.room.bean.User

@Database(
    entities = [User::class],
    version = 1,
    exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {

    abstract fun dao(): Dao
    abstract fun rxDao():DaoRxJava

    companion object {
        @JvmStatic
        val default: AppDataBase
            get() = buildDatabase()

        private fun buildDatabase(): AppDataBase {
            return Room.databaseBuilder(
                Utils.getApp().applicationContext,
                AppDataBase::class.java,
                "database.db"
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}