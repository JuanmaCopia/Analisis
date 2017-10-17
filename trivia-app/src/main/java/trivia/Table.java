package trivia;

import java.util.*;
import org.javalite.activejdbc.Model;

public class Table extends Model {


    public void setOwnerUser(int ownerUserId) {
        this.set("owner_id",ownerUserId);
        this.saveIt();
    }

    public void setGuestUser(int guestUserId) {
        this.set("guest_id",guestUserId);
        this.set("is_full",true);
        this.saveIt();
    }

    public void deleteTable() {
        this.deleteCascade();
    }

}
