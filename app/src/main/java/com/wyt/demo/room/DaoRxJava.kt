package com.wyt.demo.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.wyt.demo.room.bean.User
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface DaoRxJava {
    @Query("SELECT * FROM user")
    fun getAll(): Flowable<List<User>>

    @Insert
    fun addUser(user: User): Completable
}