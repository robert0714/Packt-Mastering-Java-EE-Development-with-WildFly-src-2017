package it.vige.businesscomponents.injection.inject.impl;
 
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Default; 

import it.vige.businesscomponents.injection.inject.Writer;
import it.vige.businesscomponents.injection.inject.profile.Profile;
import it.vige.businesscomponents.injection.inject.profile.ProfileType;

  
@Profile(ProfileType.COMMENT)
public class CommentWriter implements Writer {


	public ProfileType type() {
        System.out.println("Writer is comment");
        return ProfileType.COMMENT;
	}

}
