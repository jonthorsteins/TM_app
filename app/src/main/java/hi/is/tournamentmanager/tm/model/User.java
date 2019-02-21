package hi.is.tournamentmanager.tm.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class User implements Serializable {

    private Long id;

    private String username;
    private String password;

    private Set<Role> roles = new HashSet<>();
    private boolean enabled = true;

    private Set<Tournament> tournaments = new HashSet<>();


    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) {this.password = password; }

    public boolean isEnabled() {return enabled; }
    public void setEnabled(boolean enabled) {this.enabled = enabled; }

    public Set<Role> getRoles() { return roles; }
    public void setRoles(Set<Role> roles) { this.roles = roles; }
}