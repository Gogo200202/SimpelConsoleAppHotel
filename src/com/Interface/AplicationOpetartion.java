package com.Interface;

import org.json.simple.JSONObject;

import java.util.List;

public interface AplicationOpetartion {
    String getRoomByRoomNumber(int roomNumber);

    void editRoomAvailability(int roomNumber,boolean IsAvailable);

    Boolean chekIfRoomIsAveilabel(int roomNumber);
    public List<JSONObject> allAvailableRooms();
    public void booking(int roomNumber);

    public void unBooking(int roomNumber);

    public List<String> userBookedRooms();

}
