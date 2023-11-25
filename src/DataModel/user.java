package DataModel;

import connectMYSQL.config;

public class user {
    protected int users_id;
    protected String name;
    protected String password;
    protected IdentificationType identification_type;
    protected String phone;
    protected String email;
    protected StatusType status;
    protected StatusRole role;

    // Constructor
    public user(int users_id, String name, String password, IdentificationType identification_type,
            String phone, String email, StatusType status, StatusRole role) {
        this.users_id = users_id;
        this.name = name;
        this.password = password;
        this.identification_type = identification_type;
        this.phone = phone;
        this.email = email;
        this.status = status;
        this.role = role;
    }

    // Getter and Setter methods for all attributes
    public int getUsers_id() {
        return users_id;
    }

    public void setUsers_id(int users_id) {
        this.users_id = users_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public IdentificationType getIdentification_type() {
        return identification_type;
    }

    public void setIdentification_type(IdentificationType identification_type) {
        this.identification_type = identification_type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public StatusType getStatus() {
        return status;
    }

    public void setStatus(StatusType status) {
        this.status = status;
    }

    public StatusRole getRole() {
        return role;
    }

    public void setRole(StatusRole role) {
        this.role = role;
    }

    public enum IdentificationType {
        KTP,
        SIM,
        passport
    }

    public enum StatusType {
        active,
        non_active
    }

    public enum StatusRole {
        Operator,
        Guest
    }

}
