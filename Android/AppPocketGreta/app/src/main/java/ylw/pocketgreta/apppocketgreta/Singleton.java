package ylw.pocketgreta.apppocketgreta;

public final class Singleton {
    private static Singleton instance;
    public String tokenAuth;
    public String username;

    private Singleton(String tokenAuth, String username) {
        // Этот код эмулирует медленную инициализацию.
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        this.tokenAuth = tokenAuth;
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public String getTokenAuth() {
        return this.tokenAuth;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setTokenAuth(String tokenAuth) {
        this.tokenAuth = tokenAuth;
    }

    public static Singleton getInstance(String tokenAuth, String username) {
        if (instance == null) {
            instance = new Singleton(tokenAuth, username);
        }
        return instance;
    }
    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton(null, null);
        }
        return instance;
    }
}