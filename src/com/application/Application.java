package com.application;

import com.database.DataBase;
import com.Interface.AplicationOpetartion;
import com.Interface.LoginAndRegisterOperations;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;

public class Application implements AplicationOpetartion, LoginAndRegisterOperations {

    private DataBase db;

    private JSONObject User;

    private List<JSONArray> allRooms() {
        JSONParser jsonParser=new JSONParser();


        List<JSONArray> allRooms=this.db.readAll("rooms").stream().map(x-> {
            try {
                JSONArray json=(JSONArray)jsonParser.parse(x);
                return json;
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }).toList();


        return allRooms;
    }

    public Application(){
        this.db=new DataBase();
    }
    @Override
    public String getRoomByRoomNumber(int roomNumber) {
        try {

            int flor=(roomNumber/100)-1;

            String line = this.db.readOneLine( "rooms",flor);
            JSONParser jsonParser=new JSONParser();
            Object object=jsonParser.parse(line);
            JSONArray rooms=(JSONArray) object;
            int room=roomNumber%100-1;

            if(room<rooms.toArray().length){
                return rooms.get(room).toString();
            }else {
                System.out.printf("Room not found");
            }

        }  catch (org.json.simple.parser.ParseException asd){

            System.out.printf("Not valid parsing");
        }
        catch (IndexOutOfBoundsException flor){
            System.out.printf("Not valid Room flor");
        }

        return null;
    }

    @Override
    public void editRoomAvailability(int roomNumber,boolean available) {

        try {
            int flor=(roomNumber/100)-1;

            List<String> listFlors = this.db.readAll("rooms");


            JSONParser jsonParser=new JSONParser();

            Object object=jsonParser.parse(listFlors.get(flor));
            listFlors.remove(flor);
            JSONArray  rooms=(JSONArray) object;
            int room=roomNumber%100-1;

            if(room<rooms.toArray().length){
                JSONObject curentRoom= (JSONObject) rooms.get(room);
                curentRoom.put("Status",available);
                listFlors.add(flor,rooms.toString());
                this.db.write("rooms",listFlors);
            }else {
                System.out.printf("Room not found");
            }

        }catch (Exception e){

        }

    }

    @Override
    public Boolean chekIfRoomIsAveilabel(int roomNumber) {
        var room=getRoomByRoomNumber(roomNumber);
        try {
            JSONParser jsonParser=new JSONParser();
            JSONObject object=(JSONObject) jsonParser.parse(room);
            return (Boolean) object.get("Status");
        }catch (Exception e){

        }

        return null;
    }


    @Override
    public List<JSONObject> allAvailableRooms(){
        JSONParser jsonParser=new JSONParser();
        List<JSONObject> allAvailabelRooms=new ArrayList<>();
        var allrooms=allRooms();

        for (var flors:allrooms) {
            for (var room:flors) {

                JSONObject json=(JSONObject) room;

                if(!(boolean) ((JSONObject) room).get("Status")){
                    allAvailabelRooms.add(json);
                }
            }
        }

        return allAvailabelRooms;
    }
    @Override
    public void booking(int roomNumber,String bookingOn, String bookingEnd){
        if(chekIfRoomIsAveilabel(roomNumber)){
            System.out.printf("This room is occupied");
            return;
        }

        JSONObject jsonUser=(JSONObject)this.User;
        JSONArray a= (JSONArray) jsonUser.get("bookRooms");
        JSONObject room=new JSONObject();
        room.put("RoomNumber",roomNumber);
        a.add(room);

        List<String> allUsers=this.db.readAll("users");
        int i;
        JSONParser parser=new JSONParser();
        for (i = 0; i <allUsers.size() ; i++) {
            try {
                var user= (JSONObject) parser.parse(allUsers.get(i));
                if(user.get("userName").equals(this.User.get("userName"))){
                    System.out.printf("You boked room %d \n",roomNumber);
                    allUsers.remove(i);
                    allUsers.add(this.User.toString());
                    this.db.write("users",allUsers);
                    editRoomAvailability(roomNumber,true);
                    break;
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        int flor=(roomNumber/100)-1;
        var corentRooms=this.db.readOneLine("rooms",flor);


        try {
            JSONArray jsonRoom=(JSONArray) parser.parse(corentRooms);
            int indexRoom=(roomNumber%100)-1;
            var curentRoom= (JSONObject) jsonRoom.get(indexRoom);
            curentRoom.put("bookedOn",bookingOn);
            curentRoom.put("bookedEnd",bookingEnd);
            var allRooms=this.db.readAll("rooms");
            allRooms.remove(flor);
            allRooms.add(jsonRoom.toString());
            this.db.write("rooms",allRooms);

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }




    }
    @Override
    public void unBooking(int roomNumber){
        if(!chekIfRoomIsAveilabel(roomNumber)){
            System.out.printf("This room is occupied");
            return;
        }
        JSONParser parser=new JSONParser();
        JSONObject jsonUser=(JSONObject)this.User;
        JSONArray a= (JSONArray) jsonUser.get("bookRooms");
        int j;
        for ( j = 0; j < a.size(); j++) {
            JSONObject user= null;
            try {
                user = (JSONObject) parser.parse(a.get(j).toString());
                if((long)user.get("RoomNumber")==roomNumber){
                    a.remove(j);
                    break;

                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

        }

        List<String> allUsers=this.db.readAll("users");
        int i;

        for (i = 0; i <allUsers.size() ; i++) {
            try {
                var user= (JSONObject) parser.parse(allUsers.get(i));
                if(user.get("userName").equals(this.User.get("userName"))){
                    System.out.printf("You un boked room %d \n",roomNumber);
                    allUsers.remove(i);
                    allUsers.add(this.User.toString());
                    this.db.write("users",allUsers);
                    editRoomAvailability(roomNumber,false);
                    break;
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        int flor=(roomNumber/100)-1;
        var corentRooms=this.db.readOneLine("rooms",flor);


        try {
            JSONArray jsonRoom=(JSONArray) parser.parse(corentRooms);
            int indexRoom=(roomNumber%100)-1;
            var curentRoom= (JSONObject) jsonRoom.get(indexRoom);
            curentRoom.put("bookedOn","");
            curentRoom.put("bookedEnd","");
            var allRooms=this.db.readAll("rooms");
            allRooms.remove(flor);
            allRooms.add(jsonRoom.toString());
            this.db.write("rooms",allRooms);

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }



    }

    @Override
    public List<String> userBookedRooms(){
        List<String> allUserRooms=new ArrayList<>();
        JSONArray jsonUser= (JSONArray)this.User.get("bookRooms");
        for (var rooms:jsonUser) {
            JSONObject jsonCurenrom=(JSONObject)rooms;
            allUserRooms.add(jsonCurenrom.get("RoomNumber").toString());
        }
        return allUserRooms;
    }

    @Override
    public void addRoom(int flor,String Type,int CancellationFee,int PricePerNight){
        flor=flor-1;

        JSONParser parser=new JSONParser();
        var allRooms=this.db.readAll("rooms");
        JSONObject newRoom=new JSONObject();
        newRoom.put("Status",false);
        newRoom.put("Type",Type);
        newRoom.put("CancellationFee",CancellationFee);
        newRoom.put("PricePerNight",PricePerNight);
        newRoom.put("bookedOn","");
        newRoom.put("bookedEnd","");


        try {
            JSONArray florArray=new JSONArray();
            if(flor<allRooms.size()){
                florArray= (JSONArray) parser.parse(allRooms.get(flor));
                allRooms.remove(flor);
            }
            int roomNumber=((flor+1)*100)+(florArray.size()+1);
            newRoom.put("RoomNumber",roomNumber);

            florArray.add(newRoom);
            allRooms.add(flor, florArray.toString());
            this.db.write("rooms",allRooms);

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public String showCuretnUserName(){
        return (String) this.User.get("userName");
    }


    @Override
    public boolean login(String userName, String password) {
        List<String> allUsers=this.db.readAll("users");
        for (var user:allUsers) {
            JSONParser jsonParser=new JSONParser();

            try {
                JSONObject userJsno=(JSONObject) jsonParser.parse(user);
                if(userJsno.get("userName").equals(userName)&&userJsno.get("password").equals(password)){
                    this.User=userJsno;
                    return true;

                }
            }catch (Exception e){

            }

        }
        return false;
    }

    @Override
    public boolean register(String userName, String password) {

        JSONArray jarr = new JSONArray();
        JSONObject register=new JSONObject();
        register.put("password",password);
        register.put("userName",userName);
        register.put("bookRooms",jarr);


        List<String>allUsers=this.db.readAll("users");
        boolean flag=true;
        for (var user:allUsers) {
            JSONParser jsonParser=new JSONParser();

            try {
                JSONObject userJsno=(JSONObject) jsonParser.parse(user);
                if(userJsno.get("userName").equals(userName)){
                    flag=false;
                    System.out.println("This userName already exist ");
                    break;
                }
            }catch (Exception e){

            }

        }
        if(flag){

            this.User=register;
            allUsers.add(register.toString());
            this.db.write("users",allUsers);
            return true;
        }

        return false;


    }

    @Override
    public  List<JSONObject> searchBy(String search,String value){

        search=search.toLowerCase();
        List<JSONObject>result=new ArrayList<>();

        var allRoom=this.db.readAll("rooms");
        JSONParser parser=new JSONParser();
        for (var flor: allRoom) {
            try {
                JSONArray curentFolrJson=(JSONArray) parser.parse(flor);
                if(search.equals("type")){
                    for (var room:curentFolrJson) {

                        JSONObject curentRoom=(JSONObject)room;
                        if((boolean)curentRoom.get("Status")){
                            continue;
                        }
                        if(curentRoom.get("Type").toString().equalsIgnoreCase(value)){
                            result.add(curentRoom);
                        }

                    }

                }else if(search.equals("price")){

                    Integer integerPrice=Integer.parseInt(value);
                    for (var room:curentFolrJson) {
                        JSONObject curentRoom=(JSONObject)room;
                        if((boolean)curentRoom.get("Status")){
                            continue;
                        }
                        if((long)curentRoom.get("PricePerNight")<=integerPrice){
                            result.add(curentRoom);
                        }

                    }
                }

            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        return result;

    }


}
