package model;

import javafx.scene.control.Label;
import javafx.scene.text.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CityTemp  extends Thread{
    private Socket socket;
    private Thread rxThread;
    private String tempBack;
    private Map<String, String> cityTemp = new HashMap<>();
    private static Logger log = Logger.getLogger(CityTemp.class.getName());
    public CityTemp(String ip, int port)throws IOException {
        this( new Socket(ip,port));
    }


    public CityTemp(Socket socket) throws IOException{
        this.socket = socket;
        cityTemp.put("Москва", "12");
        cityTemp.put("Воронеж", "10");
        cityTemp.put("Липецк", "-11");

    //rxThread = new Thread(new Runnable() {
       // @Override
        //public void run() {
            try {

                FileHandler fh = new FileHandler("%tLogApp");
                log.addHandler(fh);
                if (!socket.isClosed() && socket.getInputStream().read() == 0) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String temp = bufferedReader.readLine();
                    System.out.println(temp);
                    PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
                    printWriter.write(cityTemp.get(temp));
                    printWriter.flush();
                    bufferedReader.close();
                    System.out.println("Теущая температура "+ cityTemp.get(temp));
                }
            } catch (IOException e) {
                log.log(Level.SEVERE,
                        "Ошибочка сервера",
                        e);
            }
      //  }
   // });
       // rxThread.start();

    }
}
