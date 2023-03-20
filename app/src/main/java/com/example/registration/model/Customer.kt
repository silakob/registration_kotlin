package com.example.registration.model

import android.telephony.mbms.FileInfo
import java.time.LocalDate


class Customer {
    private val id: Long = 0
    private val username: String = ""
    private val password: String = ""
    private val dob: LocalDate? = null
    private val email: String = ""
    private val fullName: String = ""
    private val phone: String = ""
    private val profilePicture: FileInfo? = null

    fun getUsername(): String {
        return username
    }

    fun getPassword(): String {
        return password
    }

    fun getDOB(): LocalDate? {
        return dob
    }

    fun getEmail(): String {
        return email
    }

    fun getFullName(): String {
        return fullName
    }

    fun getPhone(): String {
        return phone
    }

    fun getProfilePicture(): FileInfo? {
        return profilePicture
    }

}