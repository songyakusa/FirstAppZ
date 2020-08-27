package com.example.firstappz.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.firstappz.data.ContactDao
import com.example.firstappz.databinding.FragmentContactBinding

class ContactViewModel(
    val contactDao: ContactDao,
    private val binding: FragmentContactBinding,
    application: Application
) : AndroidViewModel(application) {
}