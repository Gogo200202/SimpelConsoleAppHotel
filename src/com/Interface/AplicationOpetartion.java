package com.Interface;

import org.json.simple.JSONObject;

import java.util.List;

public interface AplicationOpetartion {
    String getRoomByRoomNumber(int roomNumber);

    void editRoomAvailability(int roomNumber,boolean IsAvailable);

    Boolean chekIfRoomIsAveilabel(int roomNumber);
    public List<JSONObject> allAvailableRooms();
    public void booking(int roomNumber,String bookingOn, String bookingEnd);

    public void unBooking(int roomNumber);

    public void addRoom(int flor,String Type,int CancellationFee,int PricePerNight);

    public String showCuretnUserName();

    public List<String> userBookedRooms();

    public  List<JSONObject> searchBy(String search,String value);

}
