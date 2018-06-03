package shoreline.be;

/**
 * Contains relevant information about an individual User
 * 
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class User {
    
    private String lastName, firstName, userName;
    private int id, userLevel;

    /**
     * Constructor for User
     * 
     * @param lastName Last name
     * @param firstName First name
     * @param userName Username
     * @param id Id of User
     */
    public User(String lastName, String firstName, String userName, int id, int userLevel) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.userName = userName;
        this.id = id;
        this.userLevel = userLevel;
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

    /**
     * Sets the id of User
     * @param id Id to be set
     */
    public void setId(int id) {
        this.id = id;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(int userLevel) {
        this.userLevel = userLevel;
    }
    
}
