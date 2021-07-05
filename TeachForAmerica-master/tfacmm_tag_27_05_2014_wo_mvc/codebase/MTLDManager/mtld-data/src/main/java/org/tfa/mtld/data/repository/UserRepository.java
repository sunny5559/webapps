package org.tfa.mtld.data.repository;

import org.tfa.mtld.data.model.User;

/**
 * This interface specifies the methods required for User repository.
 */
public interface UserRepository {
	public User userLogin(String userId, String password) throws Exception;

	public void updateUserLoggedInStatus(Boolean isLoggedIn, Integer userId)
			throws Exception;

	public void resetUserLoggedInFlag() throws Exception;
}
