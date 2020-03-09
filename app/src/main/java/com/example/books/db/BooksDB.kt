package com.example.books.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Book::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class BooksDB: RoomDatabase(){
    abstract fun booksDao(): BooksDao

    companion object {
        var booksdb: BooksDB? = null

        fun createDatabase(context: Context): BooksDB {
            if(booksdb == null){
                booksdb = Room.databaseBuilder(context.applicationContext, BooksDB::class.java, "books_db").allowMainThreadQueries().build()
            }
            return booksdb as BooksDB
        }
    }
}