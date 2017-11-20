package trivia;

import java.util.*;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.validation.UniquenessValidator;

import org.json.JSONObject;
import org.json.JSONArray;

public class Table extends Model {

    //Bloque estatico para poder utilizar los metodos de validacion correspondientes en las clases de Testing.
    static {
        validatePresenceOf("owner_id").message("Please, provide the owner_id");
        validateWith(new UniquenessValidator("owner_id")).message("You are inside a Table as a owner already.");
    }

    /**
     * Returns a JSONObject contaiing all the data of the current Table object.
     * @pre. this != null
     * @return a JSONObject contaiing all the data of the current Table object.
     * @post a JSONObject contaiing all the data of the current Table object, must be returned.
    */
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

    public int getTableId() {
        return this.getInteger("id");
    }


    public void initialize(int ownerId) {
        this.set("owner_id",ownerId);
        this.set("is_full",false);
        this.saveIt();
    }

    /**
     * returns the owner's id of the current object.
     * @pre. this != null
     * @return an int value that is the owner_id of this table.
     * @post. the owner_id of the table must be returned.
     */
    public int getOwnerId() {
        return this.getInteger("owner_id");
    }

    /**
     * returns the guest's id of the current object.
     * @pre. this != null
     * @return an int value that is the guest_id of this table.
     * @post. the guest_id of the table must be returned.
     */
    public int getGuestId() {
        return this.getInteger("guest_id");
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

    /**
     * this method deletes the guest user from the current object.
     * @pre. this != null
     * @post. the guest_id of the table must be deleted.
     */
    public void deleteGuestUser() {
        this.set("guest_id",null);
        this.set("is_full",false);
        this.saveIt();
    }

    public void deleteTable() {
        this.deleteCascade();
    }

    /**
     * returns the tables where the user is currently at.
     * @param userId
     * @return a list with the tables the user is currently at,.
     * @post. a list with the tables the user is currently at, must be returned.
     */
    public static List<Table> getUserTable(int userId) {
        List<Table> tablesList = Table.findBySQL("SELECT * FROM tables WHERE owner_id = "+userId+" or guest_id = "+userId);
        return tablesList;
    }

    /**
     * returns a json array with all the tables.
     * @pre true
     * @return a json array with all the tables.
     * @post. a json array with all the tables must be returned.
     */
    public static JSONArray getTables() {
        JSONArray tableArray = new JSONArray();
        List<Table> tablesList = Table.findAll();
        for(Table t: tablesList){
            tableArray.put(t.toJson());
        }
        return tableArray;
    }

}
