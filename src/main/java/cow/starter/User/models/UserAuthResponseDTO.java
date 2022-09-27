package cow.starter.User.models;
public class UserAuthResponseDTO {
    private String token;
    private UserFullInfoDTO user;

    public UserAuthResponseDTO() { }

    public UserAuthResponseDTO(String token, UserFullInfoDTO user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserFullInfoDTO getUser() {
        return user;
    }

    public void setUser(UserFullInfoDTO user) {
        this.user = user;
    }
}
