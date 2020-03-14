package com.example.books

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.books.db.Book
import com.example.books.db.BooksDB

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = BooksDB.createDatabase(applicationContext)
        val types = listOf("adventure", "horror")
        val types2 = listOf("horror")
        db.booksDao().clearDatabase()
        val books: List<Book> = listOf(Book("book", types), Book("ksiazka", types), Book("aaa", types2))
        db.booksDao().insertBook(books[0], books[1], books[2])
        //mockowe uzupelnienie bazy danych, w przypadku normalnej implementacji zczytanie wartosci z bazy powinno byc zastapione normalna implementacja

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.editCategory -> openSettings()
        }
        return true
    }

    private fun openSettings() {
        supportFragmentManager?.let {
            val fragment = EditFragment()
            if(supportFragmentManager.findFragmentById(R.id.container) != null) {
                Log.i("supertest123","fragment removed")
                supportFragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_left)
                    .remove(supportFragmentManager.findFragmentByTag("fragment")!!).commit();
            } else {
                Log.i("supertest123","fragment added")
                supportFragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_left)
                    .replace(R.id.container, fragment, "fragment")
                    .commit();
            }
        }
    }
}
