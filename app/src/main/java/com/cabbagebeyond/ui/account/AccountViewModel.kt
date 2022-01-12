package com.cabbagebeyond.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabbagebeyond.data.dao.CharacterDao
import com.cabbagebeyond.data.dao.UserDao
import com.cabbagebeyond.data.dao.WorldDao
import com.cabbagebeyond.data.repository.CharacterRepository
import com.cabbagebeyond.data.repository.UserRepository
import com.cabbagebeyond.data.repository.WorldRepository
import com.cabbagebeyond.model.Character
import com.cabbagebeyond.model.World
import com.cabbagebeyond.util.FirebaseUtil
import kotlinx.coroutines.launch

class AccountViewModel : ViewModel() {

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

    private lateinit var userRepository: UserRepository
    private lateinit var characterRepository: CharacterRepository
    private lateinit var worldRepository: WorldRepository

    init {
        val email = FirebaseUtil.auth.currentUser?.email ?: ""
        userRepository = UserRepository(UserDao())
        characterRepository = CharacterRepository(CharacterDao())
        worldRepository = WorldRepository(WorldDao())

        viewModelScope.launch {
            val user = userRepository.getUserByEmail(email).getOrNull()
            val characters = characterRepository.getCharactersOfUser(user?.id ?: "").getOrDefault(listOf())
            _worlds.value = worldRepository.getWorlds().getOrDefault(listOf())

            _account.value = Account(user?.name ?: "", email, "", characters)
        }
    }
}