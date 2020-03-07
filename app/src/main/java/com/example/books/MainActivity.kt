package com.example.books

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

    private fun toast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    private fun openSettings() {
        supportFragmentManager?.let {
            val fragment = EditFragment()
            it.beginTransaction().replace(R.id.mainContainer, fragment).addToBackStack("abcde").commit()
        }
    }
}
