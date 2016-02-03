/**
 * EnumServerLogiCreatenStatus,		part of aptoide
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

import pt.aptoide.backupapps.R;
import android.content.Context;

/**
 * EnumServerLoginCreateStatus, typeSafes Server Login's Creation Status in Aptoide
 * 
 * @author dsilveira
 * @since 3.0
 *
 */
public enum EnumServerLoginCreateStatus {
	SUCCESS,
	PREVIOUS_LOGIN_STILL_FINISHING_UP,
	REPO_SERVICE_UNAVAILABLE,
	LOGIN_CREATE_SERVICE_UNAVAILABLE,
	USERNAME_NOT_PROPER_EMAIL,
	BAD_PASSWORD_HASH,
	BAD_HMAC,
	BAD_USER_AGENT,
	USERNAME_ALREADY_REGISTERED,
	MISSING_PARAMETER,
	UNKNOWN_USERNAME,				// on nickname update
	BAD_LOGIN, 						// on nickname update
	BAD_REPO_NAME,					
	REPO_REQUIRES_AUTHENTICATION,
	REPO_ALREADY_EXISTS,
	REPO_NOT_FROM_DEVELOPPER,
	BAD_REPO_PRIVACY_LOGIN,
	SERVER_ERROR;
	
	public static EnumServerLoginCreateStatus reverseOrdinal(int ordinal){
		return values()[ordinal];
	}
	
	public String toString(Context context) {
		switch (this) {
			case MISSING_PARAMETER:
				return context.getString(R.string.missing_parameter);
			case BAD_HMAC:
				return context.getString(R.string.hmac_problem);
			case BAD_LOGIN:
				return context.getString(R.string.check_login);
			case BAD_PASSWORD_HASH:
				return context.getString(R.string.wrong_password);
			case BAD_REPO_NAME:
				return context.getString(R.string.invalid_repo_name);
			case BAD_REPO_PRIVACY_LOGIN:
				return context.getString(R.string.check_repo_login);
			case BAD_USER_AGENT:
				return context.getString(R.string.user_agent_problem);
			case LOGIN_CREATE_SERVICE_UNAVAILABLE:
				return context.getString(R.string.login_create_service_unavailable);
			case REPO_ALREADY_EXISTS:
				return context.getString(R.string.repo_already_exists);
			case REPO_NOT_FROM_DEVELOPPER:
				return context.getString(R.string.repo_not_associated_with_user);
			case REPO_REQUIRES_AUTHENTICATION:
				return context.getString(R.string.repo_requires_authentication);
			case REPO_SERVICE_UNAVAILABLE:
				return context.getString(R.string.repo_service_unavailable);
			case UNKNOWN_USERNAME:
				return context.getString(R.string.unknown_username);
			case USERNAME_ALREADY_REGISTERED:
				return context.getString(R.string.username_already_registered);
			case USERNAME_NOT_PROPER_EMAIL:
				return context.getString(R.string.username_not_email);
			case SERVER_ERROR:
				return context.getString(R.string.server_error);
			case PREVIOUS_LOGIN_STILL_FINISHING_UP:
				return context.getString(R.string.updating_repo_please_wait);
			case SUCCESS:
				return context.getString(R.string.success);
	
			default:
				return context.getString(R.string.server_error);
		}
	}
}
