    /*
     * Client1.java
     *
     * Version:
     *     $Id$
     *
     * Revisions:
     *     $Log$
     */

/**
 * This program provides is a client that sends file
 * 
 */


    import java.util.Scanner;
    import java.util.Vector;

    import javax.imageio.ImageIO;

    import java.awt.image.BufferedImage;
    import java.io.BufferedReader;
    import java.io.ByteArrayOutputStream;
    import java.io.File;
    import java.util.Random;
    import java.io.FileNotFoundException;
    import java.io.InputStreamReader;
    import java.io.PrintWriter;
    import java.net.DatagramPacket;
    import java.net.DatagramSocket;
    import java.net.InetAddress;
    import java.net.Socket;

    public class Client1 {


        public static class Client {
            Socket aSocket;
            String hostName;
            int port;

            /**
             * Initializes the client object
             *
             * @param   name     Name of the host
             * @param   port     Port Number
             */
            public Client(String name, int port) {
                hostName = name;
                this.port = port;
            }

            /**
             * Does the fetching file job
             *
             * @param   fileName    Name of File
             *
             * @return   File content as a Vector of String
             */
            public void doTheJob(String fileName)	{

                try {

                    InetAddress aInetAddress = InetAddress.getByName(hostName);
                    DatagramSocket socket = new DatagramSocket();

                    BufferedImage bImage = ImageIO.read(new File(fileName));
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ImageIO.write(bImage, "jpg", bos );
                    byte [] data = bos.toByteArray();

                    byte [] datatemp = new byte[1016];
                    int seq = 1;
                    int c=1;

                    byte bseq = (byte) seq;

                    datatemp[c-1]=bseq;
                    ++c;


                    for(byte b: data) {
                        if(c==1016) {

                            DatagramPacket packet2 = new DatagramPacket(datatemp, datatemp.length,aInetAddress,port);
                            socket.send(packet2);

                            for(int i=0;i<1016;i++) {
                                datatemp[i]=0;
                            }
                            c=1;
                            ++seq;
                            byte bseq2 = (byte) seq;
                            datatemp[c-1]=bseq2;
                            ++c;


                        }

                        datatemp[c-1]=b;
                        ++c;

                    }

                    if(c>1) {
                        DatagramPacket packet2 = new DatagramPacket(datatemp, datatemp.length,aInetAddress,port);
                        socket.send(packet2);
                    }
                    System.out.println("Before eof");
                    byte[] buf3 = new byte[1016];
                    buf3 = "EOF".getBytes();
                    DatagramPacket packet3 = new DatagramPacket(buf3, buf3.length, aInetAddress, port);
                    socket.send(packet3);



                    socket.close();
                }



                catch (Exception e) {
                    System.out.println (e);
                }
            }
        }



        /**
         * The main program.
         *
         * @param    args    command line arguments (ignored)
         */

        public static void main( String[] args ) {



            String host = args[1];
            int    port = Integer.parseInt(args[3]);
            Client echoSocket = new Client(host, port);
            String fileName = args[5];

            echoSocket.doTheJob(System.getProperty("user.dir")
                    + "/" + fileName);


        }

    }
