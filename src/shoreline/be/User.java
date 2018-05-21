package shoreline.be;

/**
 * Contains relevant information about an individual User
 * 
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class User {
    
    private String lastName, firstName, userName;
    private int id;

    /**
     * Constructor for User
     * 
     * @param lastName Last name
     * @param firstName First name
     * @param userName Username
     * @param id Id of User
     */
    public User(String lastName, String firstName, String userName, int id) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.userName = userName;
        this.id = id;
    }

    /**
     * @return get LastName of User
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @return get FirstName of User
     */
    public String getFirstName() {
        return firstName;
    }
    
    /**
     * @return get UserName of User
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @return Id of User
     */
    public int getId() {
        return id;
    }    
}
