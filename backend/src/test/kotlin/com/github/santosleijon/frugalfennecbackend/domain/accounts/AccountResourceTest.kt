package com.github.santosleijon.frugalfennecbackend.domain.accounts

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class AccountResourceTest {
    @Autowired
    lateinit var accountResource: AccountResource

    @Test
    fun `A new account can be created and retrieved`() {
        val accountName = "Test account"
        val newAccount = accountResource.create(accountName)

        Assertions.assertThat(newAccount).isNotNull
        Assertions.assertThat(newAccount.id).isNotNull
        Assertions.assertThat(newAccount.name).isEqualTo(accountName)

        val foundAccount = accountResource.get(newAccount.id)

        Assertions.assertThat(foundAccount).isNotNull
        Assertions.assertThat(foundAccount?.id).isNotNull
        Assertions.assertThat(foundAccount?.name).isEqualTo(accountName)
    }

    @Test
    fun `An account name can be updated`() {
        val account = accountResource.create("Original account name")

        val updatedName = "Updated account name"
        val updatedAccount = accountResource.updateName(account.id, updatedName)

        Assertions.assertThat(account.id).isEqualTo(updatedAccount?.id)
        Assertions.assertThat(updatedAccount?.name).isEqualTo(updatedName)
    }
}