package hedera.starter.cowUser.models;

public class UserCreateDTO {
    private int idUserType;
    private String name;
    private String email;
    private String password;

    public UserCreateDTO() { }

    public UserCreateDTO(int idUserType, String name, String email, String password) {
        this.idUserType = idUserType;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public int getIdUserType() {
        return idUserType;
    }

    public void setIdUserType(int idUserType) {
        this.idUserType = idUserType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
