package fr.esir.nsoc.tsen.forms;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import fr.esir.nsoc.tsen.objects.User;



public class login {
	private static final String USERNAME_FIELD  = "username";
    private static final String PASSWORD_FIELD  = "password";
	
	private String              info;
    private Map<String, String> errors      = new HashMap<String, String>();
    
    
    public String getInfo() {
        return info;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
    /*
    public User connect( Universe universe, HttpServletRequest request ) {
    	String username = getParam(request, USERNAME_FIELD);
    	String password = getParam(request, PASSWORD_FIELD);

    	User user = null;

        try {
        	user = usernameValidation( universe, username );
        } catch ( Exception e ) {
            setError( USERNAME_FIELD, e.getMessage() );
        }

        try {
        	passwordValidation( user, password );
        } catch ( Exception e ) {
        	setError( PASSWORD_FIELD, e.getMessage() );
        	user = null;
        }

        if ( errors.isEmpty() ) {
            info = "Login success";
        } else {
            info = "Login failed";
        }

        return user;
    }
    
    private User usernameValidation(Universe universe, String username) throws Exception {
    	if ( username != null ) 
    	{
            if ( username.length() > 20 ) 
            {
                throw new Exception( "Username not valid (too long, max 20)" );
            }
        } else {
            throw new Exception( "No username recieved" );
        }
    	User user = universe.getUsers().get(username);
    	if (user == null)
    	{
    		throw new Exception( "This username does not exist" );
    	}
    	return user;
    }
    
    private void passwordValidation(User user, String Password) throws Exception {
    	if (user == null)
    	{    		
    		return;
    	}
    	if ( Password == null ) 
    	{
            throw new Exception( "No password recieved" );
        }
    	if (!user.getPassword().equals(Password))
    	{
    		throw new Exception( "Wrong password" );
    	}
    }
    
    
    */
    private void setError( String field, String message ) {
        errors.put( field, message );
    }
    private static String getParam( HttpServletRequest request, String name ) {
        String value = request.getParameter( name );
        if ( value == null || value.trim().length() == 0 ) {
            return null;
        } else {
            return value;
        }
    }

}
