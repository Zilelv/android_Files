/**
 * ownCloud Android client application
 *
 * @author Abel García de Prada
 * Copyright (C) 2020 ownCloud GmbH.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2,
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.filesfm.android.data

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Context
import android.preference.PreferenceManager
import com.filesfm.android.data.authentication.SELECTED_ACCOUNT
import com.filesfm.android.lib.common.OwnCloudAccount
import com.filesfm.android.lib.common.OwnCloudClient
import com.filesfm.android.lib.common.SingleSessionManager
import com.filesfm.android.lib.resources.users.services.UserService
import com.filesfm.android.lib.resources.users.services.implementation.OCUserService

class ClientManager(
    private val accountManager: AccountManager,
    val context: Context
) {
    private fun getClientForAccount(
        accountName: String?
    ): OwnCloudClient {
        val account: Account? = if (accountName.isNullOrBlank()) {
            getCurrentAccount()
        } else {
            accountManager.accounts.firstOrNull { it.name == accountName }
        }
        val ownCloudAccount = OwnCloudAccount(account, context)
        return SingleSessionManager.getDefaultSingleton().getClientFor(ownCloudAccount, context)
    }

    private fun getCurrentAccount(): Account? {
        val ocAccounts = accountManager.accounts

        val appPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val accountName = appPreferences.getString(SELECTED_ACCOUNT, null)

        // account validation: the saved account MUST be in the list of ownCloud Accounts known by the AccountManager
        accountName?.let { selectedAccountName ->
            ocAccounts.firstOrNull { it.name == selectedAccountName }?.let { return it }
        }

        // take first account as fallback
        return ocAccounts.firstOrNull()
    }

    fun getUserService(accountName: String? = ""): UserService {
        val ownCloudClient = getClientForAccount(accountName)
        return OCUserService(client = ownCloudClient)
    }
}
