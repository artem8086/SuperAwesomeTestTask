package art.soft.test.models;

public class JwtToken {
    public String token;

    public JwtToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
