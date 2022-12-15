 /*
  * Server.java
  *
  * Version:
  *     $Id$
  *
  * Revisions:
  *     $Log$
  */

/**
 * Implements the server side for responding to client in Datagram Connection
 *
 */

 import java.net.*;
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.Map;
 import java.util.Scanner;
 import java.util.TreeMap;

 import javax.imageio.ImageIO;

//import com.sun.tools.javac.util.List;

 import java.awt.image.BufferedImage;
 import java.io.*;
 public class Server extends Thread {

     DatagramSocket 	socket;

     /**
      * Initializes the Server object
      *
      * @param   port     Port Number
      */
     public Server(int port)	{
         try {
             socket = new DatagramSocket(port);
             System.out.println ("Listening on port: "
                     + socket.getLocalPort());
         } catch(Exception e) {
             System.out.println(e);
         }
     }

     public void run()	{

         try {

             for(;;) {
                 HashMap<Integer,ArrayList<Byte>> map = new HashMap<>();

                 while(true) {
                     byte buf1[] = new byte[1016];
                     DatagramPacket dp = new DatagramPacket(buf1, buf1.length);
                     socket.receive(dp);


                     String received=new String(dp.getData()).trim();

                     int a=(dp.getData()[0] & 0xFF);
                     System.out.println("image starting receiving with seq number="+a +"byte data="+dp.getData());

                     if (received.equals("EOF")) {
                         System.out.println("EOF FOUND");
                         break;
                     }
                     else {
                         int cv=0;
                         ArrayList<Byte> arrays = new ArrayList<Byte>();
                         for(byte b:dp.getData()) {
                             if(cv>0) {


                                 arrays.add(b);

                             }
                             ++cv;
                         }

                         map.put(a, arrays);

                     }

                 }

                 byte[] data = new byte[2500*2500];
                 int c=0;

                 TreeMap<Integer,ArrayList<Byte>> sorted = new TreeMap<>();

                 // Copy all data from hashMap into TreeMap
                 sorted.putAll(map);

                 // Display the TreeMap which is naturally sorted
                 for (Map.Entry<Integer,ArrayList<Byte>> entry : sorted.entrySet()) {
                     System.out.println("Seq number inserted is = " + entry.getKey());
                     for(byte b:entry.getValue()) {
                         data[c++]=b;
                     }
                 }

                 ByteArrayInputStream bis = new ByteArrayInputStream(data);
                 BufferedImage bImage2 = ImageIO.read(bis);
                 ImageIO.write(bImage2, "jpg", new File(System.getProperty("user.dir")
                         + "/" + "output.jpg") );
                 System.out.println("image created");


             }
         }
         catch(Exception e) {
             System.out.println(e);
             e.printStackTrace();
         }
     }


     public static void main(String argv[]) {

         if(argv.length >= 2) {

             new Server(Integer.parseInt(argv[1])).start();

         }
         else {

             System.out.println("Invalid Command Line Arguments");

         }

     }
 }
