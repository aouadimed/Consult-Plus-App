package com.example.consultplus.model

class User()
{
    internal var id : String ?= null
    internal var name: String? = null
    internal var email: String? = null
    internal var password: String? = null
    internal var genders: String? = null
    internal var birthdate: String? = null
    internal var adresse: String? = null
    internal var role: String? = null
    internal var firstname: String? = null
    internal var lastname: String? = null
    internal var specialite: String? = null
    internal var experience: String? = null
    internal var patient: String? = null
    internal var description: String? = null

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

    // Getter password
    fun getGender(): String? {
        return genders
    }
    // Setter password
    fun setGender(newgender: String?) {
        genders = newgender
    }

    fun getDateofbirth(): String? {
        return birthdate
    }
    // Setter password
    fun setDateofbirth(newdateofbirth: String?) {
        birthdate = newdateofbirth
    }

    fun getAdresse(): String? {
        return adresse
    }
    // Setter password
    fun setAdresse(newadresse: String?) {
        adresse = newadresse
    }

    fun getRole(): String? {
        return role
    }
    // Setter password
    fun setRole(newrole: String?) {
        role = newrole
    }

    fun getFirst(): String? {
        return firstname
    }

    fun getLast(): String? {
        return lastname
    }

    fun getS(): String? {
        return specialite
    }
    fun getex(): String? {
        return experience
    }
    fun getpatient(): String? {
        return patient
    }
    fun getdes(): String? {
        return description
    }
}