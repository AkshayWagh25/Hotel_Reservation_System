import javax.sound.midi.SysexMessage;
import java.sql.*;
import java.util.Scanner;

public class hotelReservation {
    private static final String url = "jdbc:mysql://localhost:3306/hotel_db";
    private static final String username = "root";
    private static final String password = "akshay@123";
    public static void main(String[] args) throws ClassNotFoundException , SQLException {
          try{
              Class.forName("com.mysql.cj.jdbc.Driver");
          }
          catch(ClassNotFoundException e){
              System.out.println(e.getMessage());
          }
       try {
           Connection conn = DriverManager.getConnection(url, username, password);
           Statement st = conn.createStatement();
           while(true){
               System.out.println();
               System.out.println("HOTEL RESERVATION SYSTEM");
               System.out.println("1.RESERVE A ROOM");
               System.out.println("2.VIEW RESERVATION");
               System.out.println("3.GET A ROOM NUMBER");
               System.out.println("4.UPDATE RESERVATION");
               System.out.println("5.DELETE A RESERVATION");
               System.out.println("6.EXIT");
               Scanner sc = new Scanner(System.in);
               System.out.print("ENTER YOUR CHOICE :");
               int choice = sc.nextInt();
               switch (choice){
                   case 1 : reserveRoom(conn ,st , sc);
                   break;
                   case 2 : viewReservation(conn ,st);
                   break;
                   case 3: getRoom(conn , st ,sc);
                   break;
                   case 4 : updateReservation(conn , st, sc);
                   break;
                   case 5 : deleteReservation(conn , st , sc);
                   break;
                   case 6: exit();
                          sc.close();
                          return;
                   default:
                       System.out.print("PLEASE ENTER VALID CHOICE ");
               }
               System.out.println();
               System.out.println("-------------------------------------");
           }


       }
       catch(SQLException e){
           System.out.println(e.getMessage());
       }
       catch(InterruptedException  e){
           throw new RuntimeException(e);
       }

    }
    private static void reserveRoom(Connection conn ,  Statement st , Scanner sc) throws SQLException{
        try{
            System.out.print("ENTER GUEST NAME : ");
            String guestName = sc.next();
            System.out.print("ENTER ROOM NUMBER : ");
            int roomNumber = sc.nextInt();
            System.out.print("ENTER CONTACT NUMBER : ");
            String contact = sc.next();

            // we are inserting data here --> st.executeUpdate();
            String query = "INSERT INTO reservation (guest_name, room_number, contact_number) VALUES ('" + guestName + "', " + roomNumber + ", '" + contact + "')";           int  rowsAffected = st.executeUpdate(query);
           if(rowsAffected>0){
               System.out.println("RESERVATION SUCCESSFULLY!!");
           }
           else{
               System.out.println("RESERVATION FAILED !!");

            }

        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private static void viewReservation (Connection conn , Statement st ) throws  SQLException{
        String query = " SELECT reservation_id , guest_name , room_number , contact_number , reservation_date from  reservation";
        try{
            ResultSet rs = st.executeQuery(query);
            while(rs.next()){
                int resID = rs.getInt("reservation_id");
                String gName = rs.getString("guest_name");
                int room_number = rs.getInt("room_number");
                String contact = rs.getString("contact_number");
                String resDate = rs.getTimestamp("reservation_date").toString();
                System.out.println("RES_ID : "+resID+" -->"+ "| GUESTNAME : "+gName+"-->"+ "| ROOM_NUMBER :"+room_number+ "-->"+ "|CONTACT :"+contact+"-->"+ "|resDATE :"+resDate);

            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

        System.out.println();





    }
    private static void getRoom(Connection conn , Statement st , Scanner sc) throws SQLException{
        int resID = sc.nextInt();
        String Gname = sc.next();
        String query = "SELECT room_number,reservation_id from reservation where reservation_id = "+ resID+" and guest_name = '" + Gname + "'" ;

        try{
            ResultSet rs = st.executeQuery(query);
            if(rs.next()){
                int resId = rs.getInt("reservation_id");
//                String gname = rs.getString("guest_name");
                int roomNumber = rs.getInt("room_number");
                System.out.println("Room number for requestID : "+resId+" and guestName : "+Gname+" is "+ roomNumber);

            }
            else{
                System.out.println("ROOM IS NOT AVAILABLE FOR ID "+resID);
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }


    }

    private static void updateReservation(Connection conn , Statement st , Scanner sc) throws  SQLException{
        System.out.print("enter reservation id which you want to update :");
        int id = sc.nextInt();

        if(!reservationExists(conn , id,st)){
            System.out.println("Reservation not found for this id");
            return;
        }
        System.out.println("Enter new Guest Name");
        String newName = sc.next();
        System.out.println("Enter new Room number");
        int newRoomNumber = sc.nextInt();
        System.out.println("Enter new Mobile Number");
        String newcontactNumber = sc.next();

        String query = "UPDATE reservation set guest_name = '"+newName+"',"+"room_number = "+newRoomNumber+","+"contact_number = '"+newcontactNumber+"'"+"WHERE reservation_id =" + id;

        try{
            int rowsAffected = st.executeUpdate(query);
        if(rowsAffected>0){
            System.out.println("data updated!!!");
        }
    }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

    }

    private static void deleteReservation(Connection conn , Statement st, Scanner sc){
        System.out.print("ENTER ID which YOU WANT TO DELETE :");
        int id = sc.nextInt();

        if(!reservationExists(conn , id,st)){
            System.out.println("Reservation not found for this id");
            return;
        }

        String query =  " delete from reservation where reservation_id = "+id;
        try{
            int rowsAffected = st.executeUpdate(query);
            if(rowsAffected>0){
                System.out.println("id deleted!!!");
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

    }
    private static boolean reservationExists(Connection conn , int id , Statement st){
        String query = "Select reservation_id from reservation where reservation_id ="+id;

        try{
            ResultSet rs = st.executeQuery(query);
            return rs.next();

        }
        catch(SQLException e){
           e.getMessage();
            return false;
        }
    }
    private static void exit() throws InterruptedException{
        System.out.print("Exiting system");
        int i = 5;

        while(i!=0){
            System.out.print(".");
            Thread.sleep(480);
            i--;

        }
        System.out.println();
        System.out.println("THANK YOU FOR CHOOSING HOTEL RESERVATION SYSTEM");
    }

}
