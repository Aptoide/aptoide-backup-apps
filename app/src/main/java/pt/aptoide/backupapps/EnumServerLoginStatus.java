/**
 * EnumServerLoginStatus,		part of aptoide
 * Copyright (C) 2011  Duarte Silveira
 * duarte.silveira@caixamagica.pt
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*/

package pt.aptoide.backupapps;

import android.content.Context;

/**
 * EnumServerLoginStatus, typeSafes Server Login's Status in Aptoide
 *
 * @author dsilveira
 * @since 3.0
 *
 */
public enum EnumServerLoginStatus {
	SUCCESS,
	PREVIOUS_LOGIN_STILL_FINISHING_UP,
	REPO_SERVICE_UNAVAILABLE,
	LOGIN_SERVICE_UNAVAILABLE,
	BAD_LOGIN,
	REPO_NOT_FROM_DEVELOPPER,
	BAD_REPO_PRIVACY_LOGIN,
    NO_DEFAULT_REPO,
    REPO_NAME_ALREADY_EXISTS;

	public static EnumServerLoginStatus reverseOrdinal(int ordinal){
		return values()[ordinal];
	}

	public String toString(Context context){
		switch (this) {
			case BAD_LOGIN:
				return context.getString(R.string.check_login);
			case REPO_NOT_FROM_DEVELOPPER:
				return context.getString(R.string.repo_not_associated_with_user);
			case REPO_SERVICE_UNAVAILABLE:
				return  context.getString(R.string.repo_service_unavailable);
			case BAD_REPO_PRIVACY_LOGIN:
				return context.getString(R.string.check_repo_login);
			case LOGIN_SERVICE_UNAVAILABLE:
				return  context.getString(R.string.login_service_unavailable);
			case PREVIOUS_LOGIN_STILL_FINISHING_UP:
				return context.getString(R.string.updating_repo_please_wait);
            case NO_DEFAULT_REPO:
                return context.getString(R.string.no_default_repo);
            case REPO_NAME_ALREADY_EXISTS:
                return context.getString(R.string.already_existent_store_name);
			default:
				return context.getString(R.string.server_error);
		}
	}
}
