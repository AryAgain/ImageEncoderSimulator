# ImageEncoderSimulator

A protocol simulation to effectively encode and transport image files. 
It is implemented through using various concepts as follow:
- The packets are divided into multiple smaller chunks to be sent over the network
- Each packet has a header which will tell the sequence number of that packet
- At the receiver end the receiver will check the sequence number and accordingly
remake the image file back
- If any sequence number is not received, it will be dropped and the image will be
made from the rest of the packets as the transfer is over a longer path. This will
further prevent from any congestion on the network.
- Each packet if needs to be made more reliable to have clear picture can implement
nAck, which will be sent for dropped package to be resent by the server


#### Steps to run : 

**Client/Sender:**
The module which encodes the image file into bits and sends to the processing server.
```
javac Client1.java
java Client1 -server localhost -port 2233 -f img.jpg
```

**Client/Sender:**
The module which decodes the trasported file back into original image file for viewing.
```
javac Server.java
java Server -port 2233
```


