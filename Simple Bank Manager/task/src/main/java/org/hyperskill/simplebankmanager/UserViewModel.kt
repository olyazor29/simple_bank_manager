package org.hyperskill.simplebankmanager

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserViewModel : ViewModel() {
    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> get() = _userName

    private val _balance = MutableLiveData<Double>()
    val balance: LiveData<Double> get() = _balance

    fun setUserName(name: String) {
        _userName.value = name
    }

    fun setBalance(balance: Double) {
        _balance.value = balance
    }
}