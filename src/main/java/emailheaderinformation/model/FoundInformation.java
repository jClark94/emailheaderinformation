package emailheaderinformation.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jaclark on 03/05/16.
 */
public class FoundInformation {
    private String name;
    private String software;
    private String hardware;

    public void setName (String name) {
        this.name = name;
    }

    public void setSoftware (String software) {
        this.software = software;
    }

    public void setHardware (String hardware) {
        this.hardware = hardware;
    }

    public void addUsername(String context, String username) {
        usernameList.add(new Username(context, username));
    }

    private List<Username> usernameList = new ArrayList<Username>();

    public String getName () {
        return name;
    }
}
