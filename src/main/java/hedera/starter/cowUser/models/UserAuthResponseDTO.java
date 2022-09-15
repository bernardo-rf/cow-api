package hedera.starter.cowUser.models;
public class UserAuthResponseDTO {
    private String token;
    private UserDTO user;

    public UserAuthResponseDTO() { }

    public UserAuthResponseDTO(String token, UserDTO user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}
