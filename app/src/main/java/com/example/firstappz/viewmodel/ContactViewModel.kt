package com.example.firstappz.viewmodel

import android.app.Application
import android.os.Build
import android.text.Html
import android.text.Spanned
import androidx.core.text.HtmlCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.firstappz.data.Contact
import com.example.firstappz.data.ContactDao
import com.example.firstappz.databinding.FragmentContactBinding
import kotlinx.coroutines.*

class ContactViewModel(
    private val contactDao: ContactDao,
    private val binding: FragmentContactBinding,
    application: Application
) : AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private var contact = MutableLiveData<Contact?>()

    private val contacts = contactDao.get()
    val contactString = Transformations.map(contacts) { contacts ->
        formatContact(contacts)
    }

    private fun formatContact(contact: List<Contact>): Spanned {
        val sb = StringBuilder()
        sb.apply {
            //append(resources.getString(R.string.title))
            contact.forEach {
                append(it.id)
                append(" : ")
                append(it.name)
                append("<br>")
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(sb.toString(), Html.FROM_HTML_MODE_LEGACY)
        } else {
            return HtmlCompat.fromHtml(sb.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
//fun add
    fun onContactAdd() {
        uiScope.launch {
            val newContact = Contact()
            newContact.name = binding.editTextTextPersonName.text.toString()
            insert(newContact)
        }
    }
    private suspend fun insert(contact: Contact) {
        withContext(Dispatchers.IO) {
            contactDao.insert(contact)
        }
    }


//fun clear
    fun onClear() {
        uiScope.launch {
            clear()
            contact.value = null
        }
    }
    private val _navigateToSleepQuality = MutableLiveData<Contact>()

    val navigateToSleepQuality: LiveData<Contact>
        get() = _navigateToSleepQuality

    fun doneNavigating() {
        _navigateToSleepQuality.value = null
    }

    suspend fun clear() {
        withContext(Dispatchers.IO) {
            contactDao.clear()
        }
    }



}