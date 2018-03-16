import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class RouteController {

    public static final String ip = "localhost";
    public static final int port = 1405;

    private List<RouteController> routeControllers;
    private List<AbstractMap.SimpleEntry<UUID, Socket>> sockets;
    private ServerSocket serverSocket;
    private UUID uuid;

    public RouteController(){
        uuid = UUID.randomUUID();
        routeControllers = new ArrayList<RouteController>();
        sockets = new ArrayList<AbstractMap.SimpleEntry<UUID, Socket>>();
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connect(RouteController routeController){
        if(hasSocket(routeController.getUuid()) == false){
            routeControllers.add(routeController);
            routeController.connect(this);
            try {
                if(uuid.compareTo(routeController.getUuid()) > 0){
                    Socket socket = new Socket(ip, port);
                    sockets.add(new AbstractMap.SimpleEntry<UUID, Socket>(routeController.getUuid(), socket));
                }else{
                    sockets.add(new AbstractMap.SimpleEntry<UUID, Socket>(routeController.getUuid(), serverSocket.accept()));
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
                break;
            }
        }
        return hasSocket;
    }
}
