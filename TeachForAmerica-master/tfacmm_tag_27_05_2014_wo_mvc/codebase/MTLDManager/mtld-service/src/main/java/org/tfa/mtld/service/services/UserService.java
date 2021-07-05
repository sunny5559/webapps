package org.tfa.mtld.service.services;

import org.tfa.mtld.data.model.User;
import org.tfa.mtld.service.bean.UserBean;

/**
 * This interface specifies the methods required for User.
 */
public interface UserService {
	public User userLogin(UserBean userBean) throws Exception;
	public void updateUserLoggedInStatus(Boolean isLoggedIn, Integer userId) throws Exception;
	public void resetUserLoggedInFlag()throws Exception;
}
