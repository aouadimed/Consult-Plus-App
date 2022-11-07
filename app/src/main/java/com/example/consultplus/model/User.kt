package com.example.consultplus.model

class User()
{
    internal var id : String ?= null
    internal var fullname: String? = null
    internal var email: String? = null
    internal var password: String? = null



    // Getter id
    fun getId(): String? {
        return id
    }
    // Setter id
    fun setId(newId: String?) {
        id = newId
    }
    // Getter name
    fun getFullName(): String? {
        return fullname
    }
    // Setter name
    fun setFullName(newfullname: String?) {
        fullname = newfullname
    }

    // Getter email
    fun getEmail(): String? {
        return email
    }
    // Setter email
    fun setEmail(newEmail: String?) {
        email = newEmail
    }

    // Getter password
    fun getPassword(): String? {
        return password
    }
    // Setter password
    fun setPassword(newPassword: String?) {
        password = newPassword
    }


}