package org.telegram.banbot.shared;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class Chat {

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private String Users;
    
    public Chat(String users) {
        this.Users = users;
        
    }

    // Accessors for the fields. JDO doesn't use these, but your application does.

    public Key getKey() {
        return key;
    }
    public void setKey(Key key) {
        this.key = key;
    }

    public String getUsers() {
        return Users;
    }
    public void setUsers(String Users) {
        this.Users = Users;
    }
}
