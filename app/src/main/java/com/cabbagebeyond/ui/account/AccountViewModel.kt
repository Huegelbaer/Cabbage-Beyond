package com.cabbagebeyond.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabbagebeyond.data.CharacterDataSource
import com.cabbagebeyond.data.UserDataSource
import com.cabbagebeyond.data.WorldDataSource
import com.cabbagebeyond.model.Character
import com.cabbagebeyond.model.World
import com.cabbagebeyond.util.FirebaseUtil
import kotlinx.coroutines.launch

class AccountViewModel(
    private val userDataSource: UserDataSource,
    private val characterDataSource: CharacterDataSource,
    private val worldDataSource: WorldDataSource
) : ViewModel() {

    data class Account(
        var name: String,
        var email: String,
        var activeWorld: String,
        var characters: List<Character>
    )

    private var _account = MutableLiveData<Account>()
    val account: LiveData<Account>
        get() = _account

    private var _worlds = MutableLiveData<List<World>>()
    val worlds: LiveData<List<World>>
        get() = _worlds

    init {
        val email = FirebaseUtil.auth.currentUser?.email ?: ""

        viewModelScope.launch {
            val user = userDataSource.getUserByEmail(email).getOrNull()
            val characters = characterDataSource.getCharactersOfUser(user?.id ?: "").getOrDefault(listOf())
            _worlds.value = worldDataSource.getWorlds().getOrDefault(listOf())

            _account.value = Account(user?.name ?: "", email, "", characters)
        }
    }
}