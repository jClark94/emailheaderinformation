package emailheaderinformation.model;

/**
 * Created by jaclark on 03/05/16.
 */
public class Username {
    private final String context;

    public String getUsername () {
        return username;
    }

    public String getContext () {
        return context;
    }

    private final String username;

    public Username (String context, String username) {
        this.context = context;
        this.username = username;
    }
}
