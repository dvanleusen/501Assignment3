1) host.txt needs to be put in the same directory as package a3

2) Server in host.txt should be the server ip address (listener)

3) UseSameComputer in host.txt should be set to false when using two different computers;
   it should be set to true when using the same computer

4) When server is the ip address of a second computer, but UseSameComputer is true,
   only one computer is used for both sending and receiving

5) Default user port is 9999

6) To compile:
   javac -cp ".;C:\Users\Daniel\workspace\501_A3_V0\src\a3\jdom-1.1.3.jar;" *.java

7) To run:
   java -cp .;C:\Users\Daniel\workspace\501_A3_V0\src\a3\jdom-1.1.3.jar a3.Assignment3

