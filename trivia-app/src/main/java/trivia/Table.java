package trivia;

import java.util.*;
import org.javalite.activejdbc.Model;

import org.json.JSONObject;

public class Table extends Model {


    public JSONObject toJson() {
        int guestId;
        JSONObject result = new JSONObject();
        int ownerId = this.getInteger("owner_id");
        User owner = User.findById(ownerId);
        result.put("id",this.getInteger("id"));
        result.put("owner_id",ownerId);
        result.put("owner_username",owner.getString("username"));
        if (this.getBoolean("is_full")) {
            guestId = this.getInteger("guest_id");
            User guest = User.findById(guestId);
            result.put("guest_username",guest.getString("username"));
            result.put("guest_id",guestId);
            result.put("is_full",true);
        }
        else {
            result.put("guest_id","null");
            result.put("guest_username","null");
            result.put("is_full",false);
        }
        return result;
    }


    public void initialize(int ownerId) {
        this.set("owner_id",ownerId);
        this.set("is_full",false);
        this.saveIt();
    }

    public void setOwnerUser(int ownerId) {
        this.set("owner_id",ownerId);
        this.saveIt();
    }

    public void setGuestUser(int guestId) {
        this.set("guest_id",guestId);
        this.set("is_full",true);
        this.saveIt();
    }

    public void deleteTable() {
        this.deleteCascade();
    }

}
