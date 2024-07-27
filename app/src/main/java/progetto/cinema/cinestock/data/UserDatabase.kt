package progetto.cinema.cinestock.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import progetto.cinema.cinestock.models.user.User

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): UserDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "user_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(UserDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class UserDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCE?.let { database ->
                    scope.launch {
                        populateDatabase(database.userDao())
                    }
                }
            }

            suspend fun populateDatabase(userDao: UserDao) {
                // Check if the database is empty before populating it
                val count = userDao.getUserCount()
                if (count == 0) {
                    // Only add users if the database is empty
                    userDao.insert(User(firstName = "John", lastName = "Doe", phoneNumber = "123456789"))
                    userDao.insert(User(firstName = "Jane", lastName = "Doe", phoneNumber = "987654321"))
                }
            }
        }
    }
}