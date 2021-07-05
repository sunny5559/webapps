package org.tfa.mtld.service.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tfa.mtld.data.model.User;
import org.tfa.mtld.data.repository.UserRepository;
import org.tfa.mtld.service.bean.UserBean;

/**
 * This class is the implementation of the user service interface.
 * implements all the method defined in that interface.
 * @author arun.rathore
 * @version 1.0, 25 March, 2014.
 * 
 */
@Service("UserService")
public class UserServiceImpl implements UserService {

	Logger logger = Logger.getLogger(UserServiceImpl.class);

	@Autowired
	private UserRepository userRepository;


	/**
	 * Method for user login.
	 * 
	 * @param userBean
	 *            The object of the UserBean.
	 * @return UserBean object
	 * @author arun.rathore
	 */
	public User userLogin(UserBean userBean) throws Exception{
		logger.debug("In side userLogin of UserServiceImpl ");
		
		User user = null;
		if (userBean != null && userBean.getLoginId() != null && userBean.getPassword() != null) {
				user = userRepository.userLogin(userBean.getLoginId(),userBean.getPassword());

		}
		return user;
	}
	
	public void updateUserLoggedInStatus(Boolean isLoggedIn, Integer userId) throws Exception{
		logger.debug("In side updateUserStatus of UserServiceImpl ");
		if(isLoggedIn != null && userId !=null){
			userRepository.updateUserLoggedInStatus(isLoggedIn, userId);
		}
	}

	@Override
	public void resetUserLoggedInFlag() throws Exception {
		userRepository.resetUserLoggedInFlag();
		
	}

}
