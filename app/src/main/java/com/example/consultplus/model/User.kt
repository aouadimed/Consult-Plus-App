package com.example.consultplus.model

class User()
{
    internal var id : String ?= null
    internal var name: String? = null
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
    fun getFullname(): String? {
        return name
    }
    // Setter name
    fun setFullname(newfullname: String?) {
        name = newfullname
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