package com.clipforge.ai.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.clipforge.ai.data.local.entity.ProjectEntity
import com.clipforge.ai.data.local.entity.ClipEntity
import com.clipforge.ai.data.local.entity.ExportEntity
import com.clipforge.ai.data.local.dao.ProjectDao
import com.clipforge.ai.data.local.dao.ClipDao
import com.clipforge.ai.data.local.dao.ExportDao

@Database(
    entities = [
        ProjectEntity::class,
        ClipEntity::class,
        ExportEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ClipForgeDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao
    abstract fun clipDao(): ClipDao
    abstract fun exportDao(): ExportDao
}
