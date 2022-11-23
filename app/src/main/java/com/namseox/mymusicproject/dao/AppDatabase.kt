//package com.namseox.mymusicproject.dao
//
//import android.content.Context
//import androidx.room.Database
//import androidx.room.Room
//import androidx.room.RoomDatabase
//import androidx.sqlite.db.SupportSQLiteDatabase
//import com.namseox.mymusicproject.model.Playlist
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//
//
//@Database(
//    entities = [Playlist::class],
//    version = 2,
//    exportSchema = false
//)
//abstract class AppDatabase : RoomDatabase() {
//    abstract fun getPlayListDao(): SongDao
//
//    companion object {
//        private var INSTANCE: AppDatabase? = null
//
//        @Synchronized
//        fun getInstance(ctx: Context, scope: CoroutineScope): AppDatabase {
//            if (INSTANCE == null) {
//                INSTANCE = Room.databaseBuilder(
//                    ctx.applicationContext,
//                    AppDatabase::class.java,
//                    "playlist.db"
//                ).fallbackToDestructiveMigration()
//                    .addCallback(object : RoomDatabase.Callback(){
//                        override fun onOpen(db: SupportSQLiteDatabase) {
//                            super.onOpen(db)
//                        }
//                        override fun onCreate(db: SupportSQLiteDatabase) {
//                            super.onCreate(db) //insert data here
//
//                            scope.launch(Dispatchers.IO) {
//                                insertFakeData()
//
//                            }
//                        }
//                        private suspend fun insertFakeData() {
//                            val listDao= INSTANCE!!.getPlayListDao()
//                            for (i in 1..100){
//                                listDao.getPlayList()
//                            }
//
//                        }
//
//
//
//                        override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
//                            super.onDestructiveMigration(db)
//                        }
//                    }).build()
//
//            }
//            return INSTANCE!!
//        }
//    }
//
//}