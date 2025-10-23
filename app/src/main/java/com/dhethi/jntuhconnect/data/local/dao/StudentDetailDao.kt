package com.dhethi.jntuhconnect.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dhethi.jntuhconnect.data.local.entities.StudentDetailsEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface StudentDetailsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDetails(details: StudentDetailsEntity)

    @Query("SELECT * FROM student_details WHERE rollNumber = :rollNumber")
    suspend fun getDetails(rollNumber: String): StudentDetailsEntity?

    @Query("SELECT * FROM student_details ORDER BY last_updated DESC")
    fun getAllDetails(): Flow<List<StudentDetailsEntity>>

    @Query("DELETE FROM student_details WHERE rollNumber = :rollNumber")
    suspend fun deleteDetails(rollNumber: String)

    @Query("DELETE FROM student_details")
    suspend fun deleteAllDetails()
}