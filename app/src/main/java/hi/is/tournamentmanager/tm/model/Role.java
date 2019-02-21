package hi.is.tournamentmanager.tm.model;

public class Role {

    private Long id;

    private User user;
    private String role;

    public Role(){}

    public Role(User user, String role) {
        this.user = user;
        this.role = role;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user;}

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
