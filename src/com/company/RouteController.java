package com.company;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class RouteController {

    public static final String ip = "localhost";
    public static final int port = 1405;

    private List<RouteController> routeControllers;
    private List<AbstractMap.SimpleEntry<UUID, DatagramSocket>> sockets;
    private ServerSocket serverSocket;
    private UUID uuid;

    public RouteController(){
        uuid = UUID.randomUUID();
        routeControllers = new ArrayList<RouteController>();
        sockets = new ArrayList<AbstractMap.SimpleEntry<UUID, DatagramSocket>>();

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connect(RouteController routeController){
        if(routeControllers.contains(routeController) == false){
            routeControllers.add(routeController);
            routeController.connect(this);
            try {
                if(uuid.compareTo(routeController.getUuid()) > 0){
                    Socket socket = new Socket(ip, port);
                    DatagramSocket datagramSocket = new DatagramSocket(1405);
                    sockets.add(new AbstractMap.SimpleEntry<UUID, DatagramSocket>(routeController.getUuid(), socket));
                }else{
                    sockets.add(new AbstractMap.SimpleEntry<UUID, DatagramSocket>(routeController.getUuid(), serverSocket.accept()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public UUID getUuid() {
        return uuid;
    }
//comment
    public boolean hasSocket(UUID uuid){
        boolean hasSocket = false;
        Iterator<AbstractMap.SimpleEntry<UUID, Socket>> iterator = sockets.iterator();
        while(iterator.hasNext()){
            AbstractMap.SimpleEntry<UUID, Socket> map = iterator.next();
            UUID id = map.getKey();
            if(id.equals(uuid)){
                hasSocket = true;
                System.out.println("------------------------------------------------");
                System.out.println("Has found rc");
                System.out.println("------------------------------------------------");
                break;
            }
        }
        return hasSocket;
    }

    @Override
    public String toString() {
        StringBuilder toString = new StringBuilder();
        Iterator<AbstractMap.SimpleEntry<UUID, Socket>> iterator = sockets.iterator();
        while(iterator.hasNext()){
            AbstractMap.SimpleEntry<UUID, Socket> map = iterator.next();
            UUID id = map.getKey();
                toString.append(id.toString());
        }       
        return "";
    }
}
