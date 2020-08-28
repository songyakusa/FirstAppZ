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
import kotlinx.android.synthetic.main.fragment_contact.view.*
import kotlinx.coroutines.*
import java.nio.file.Files.delete

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

    private suspend fun insert(contact: Contact) {
        withContext(Dispatchers.IO) {
            contactDao.insert(contact)
        }
    }

    //fun add
    fun onContactAdd() {
        uiScope.launch {
            val newContact = Contact()
            newContact.name = binding.editTextTextPersonName.text.toString()
            insert(newContact)
        }
    }

    //fun clear
    fun onClear() {
        uiScope.launch {
            clear()
            contact.value = null
        }
    }

    //    private val _navigateToContact = MutableLiveData<Contact>()
//
//    val navigateToContact: LiveData<Contact>
//        get() = _navigateToContact
//
//    fun doneNavigating() {
//        _navigateToContact.value = null
//    }
//fub delete
//    suspend fun onDelete() {
//        withContext(Dispatchers.IO) {
//            contactDao.delete()
//        }
//    }


    suspend fun clear() {
        withContext(Dispatchers.IO) {
            contactDao.clear()
        }
    }


}