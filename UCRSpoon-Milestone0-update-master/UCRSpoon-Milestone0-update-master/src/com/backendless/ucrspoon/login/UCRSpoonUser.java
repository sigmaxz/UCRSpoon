package com.backendless.ucrspoon.login;

import com.backendless.BackendlessUser;

public class UCRSpoonUser extends BackendlessUser
{
  public String getEmail()
  {
    return super.getEmail();
  }

  public void setEmail( String email )
  {
    super.setEmail( email );
  }

  public String getPassword()
  {
    return super.getPassword();
  }

  public String getFriends()
  {
    return (String) super.getProperty( "Friends" );
  }

  public void setFriends( String Friends )
  {
    super.setProperty( "Friends", Friends );
  }

  public Integer getType()
  {
    return (Integer) super.getProperty( "Type" );
  }

  public void setType( Integer Type )
  {
    super.setProperty( "Type", Type );
  }

  public Boolean getIsRestaurant()
  {
    return (Boolean) super.getProperty( "isRestaurant" );
  }

  public void setIsRestaurant( Boolean isRestaurant )
  {
    super.setProperty( "isRestaurant", isRestaurant );
  }

  public String getName()
  {
    return (String) super.getProperty( "name" );
  }

  public void setName( String name )
  {
    super.setProperty( "name", name );
  }

  public Integer getUserID()
  {
    return (Integer) super.getProperty( "userID" );
  }

  public void setUserID( Integer userID )
  {
    super.setProperty( "userID", userID );
  }
}