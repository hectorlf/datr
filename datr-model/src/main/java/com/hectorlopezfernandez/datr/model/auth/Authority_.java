package com.hectorlopezfernandez.datr.model.auth;

import com.hectorlopezfernandez.datr.model.PersistentObject_;
import com.hectorlopezfernandez.datr.model.auth.Principal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2014-10-04T21:54:05")
@StaticMetamodel(Authority.class)
public final class Authority_ extends PersistentObject_ {

    public static volatile SingularAttribute<Authority, Principal> principal;
    public static volatile SingularAttribute<Authority, String> authority;
    public static volatile SingularAttribute<Authority, String> username;

}