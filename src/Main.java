import com.application.Application;
import org.json.simple.JSONObject;

import java.lang.String;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
           Application a=new Application();

        Scanner sc=new Scanner(System.in);
        while (true){

            System.out.println("For login enter 1 for register enter 2:");
            String option=sc.nextLine();

            if(option.equals("1")){
                System.out.printf("Enter user name: ");
                String userName=sc.nextLine();
                System.out.printf("Enter user Password: ");
                String password=sc.nextLine();
                if(a.login(userName,password)){
                    System.out.println("in");
                    break;
                }
            }

            if(option.equals("2")){
                System.out.printf("Enter user name: ");
                String userName=sc.nextLine();
                System.out.printf("Enter user Password: ");
                String password=sc.nextLine();
                if(a.register(userName,password)){
                    System.out.println("You are register");
                    break;
                }

            }

        }

        while (true){
            System.out.println("To see exit pres 0");
            System.out.println("To see all available rooms pres 1");
            System.out.println("To book room pleas enter pres 2");
            System.out.println("To un book room pleas enter pres 3");
            System.out.println("To see your booked rooms pleas enter pres 4");
            if(a.showCuretnUserName().equals("admin")){
                System.out.println("To add new room pres 5");
            }

            String comand=sc.nextLine();
            if(comand.equals("0")){
                break;
            }else if(comand.equals("1")){
                var allrooms=a.allAvailableRooms();

                for (var rooms:allrooms ) {
                    JSONObject jsonroom=(JSONObject) rooms;
                    System.out.printf("Room number %d, Prise %d ,Type %s \n",rooms.get("RoomNumber"),rooms.get("PricePerNight"),rooms.get("Type"));
                }

            }else if(comand.equals("2")){

                System.out.print("Enter room number: ");
                int numberRoom=Integer.parseInt(sc.nextLine());
                a.booking(numberRoom);

            }else if(comand.equals("3")){

                System.out.print("Enter room number to unhook: ");
                int numberRoom=Integer.parseInt(sc.nextLine());
                a.unBooking(numberRoom);

            }else if(comand.equals("4")){

                System.out.println("user booked rooms");

                for (var rooms: a.userBookedRooms()) {
                    System.out.println(rooms);
                }

            }else if(a.showCuretnUserName().equals("admin")&&comand.equals("5")){
                System.out.println("Enter flore: ");
                int flor=Integer.parseInt(sc.nextLine());
                System.out.println("Enter RoomNumber: ");
                int RoomNumber=Integer.parseInt(sc.nextLine());
                System.out.println("Enter type: ");
                String type=sc.nextLine();
                System.out.println("Enter CancellationFee: ");
                int CancellationFee=Integer.parseInt(sc.nextLine());
                System.out.println("Enter PricePerNight: ");
                int PricePerNight=Integer.parseInt(sc.nextLine());
                a.addRoom(flor,type,CancellationFee,RoomNumber,PricePerNight);
            }



        }








    }





}






