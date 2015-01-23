package rest.dimmableSwitch;


import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by mathi_000 on 21/01/2015.
 */

@XmlRootElement
public class UserDimmableSwitch {

    private String user;
    private String UserPref;


    public UserDimmableSwitch(String u,String pref){
        user = u;
        UserPref = pref ;

    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public void setUserPref(String userPref) {
        UserPref = userPref;
    }

    public String getUserPref() {
        return UserPref;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {

        UserDimmableSwitch udm ;

        if(!(obj instanceof UserDimmableSwitch)){return false;}
        else{udm = (UserDimmableSwitch) obj;}
        if(obj == null){return false;}
        else if(user.compareTo(udm.getUser())==0 && UserPref.compareTo(udm.getUserPref())==0) {return false;}
        return true;
    }
}
