package org.tfa.mtld.service.services;

import java.util.List;



import org.tfa.mtld.data.model.User;
import org.tfa.mtld.service.bean.CMBean;


/**
 * @author Lovely Ram
 * 
 */
public interface  CorpsMemberService {

	
	public void saveCoprsMember(List<CMBean> cmBeans,User sessionUser) throws Exception;
}
