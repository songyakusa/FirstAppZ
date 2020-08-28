package com.example.firstappz.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ContactDao {
    @Insert
    fun insert(contact: Contact)
    @Query("SELECT * from contact_table")
    fun get(): LiveData<List<Contact>>

    @Query("DELETE FROM contact_table ")
    fun clear()

    @Query("DELETE FROM contact_table WHERE id AND name ")
    fun delete()

}