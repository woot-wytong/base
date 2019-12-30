package com.wyt.demo.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.wyt.demo.room.bean.User

@Dao
interface Dao {
    @Query("SELECT * FROM user")
    fun getAll() :List<User>


    @Insert
    fun insert(users:List<User>)


}