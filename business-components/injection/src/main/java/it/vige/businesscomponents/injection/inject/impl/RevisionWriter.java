package it.vige.businesscomponents.injection.inject.impl;


import it.vige.businesscomponents.injection.inject.Writer;
import it.vige.businesscomponents.injection.inject.profile.Profile;
import it.vige.businesscomponents.injection.inject.profile.ProfileType;

@Profile(ProfileType.REVISION)
public class RevisionWriter implements Writer {

	public ProfileType type() {
        System.out.println("Writer is revision");
        return ProfileType.COMMENT;
	}
}
