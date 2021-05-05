package sample;

import javafx.event.ActionEvent;
import model.ThreadsTemp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Controller {
    private ServerSocket server;
    private Thread rxThread1;
    private Socket socket = null;
    private static Logger log = Logger.getLogger(Controller.class.getName());
    public void btnStart(ActionEvent actionEvent) {
        rxThread1 = new Thread(() -> {
            try {
                FileHandler xmlFile = new FileHandler ("logServer.xml", true);
                log.addHandler (xmlFile);
                FileHandler txtFile = new FileHandler ("logServer.%u.%g.txt", true);
                SimpleFormatter txtFormatter = new SimpleFormatter ();
                txtFile.setFormatter (txtFormatter);
                log.addHandler (txtFile);
                server = new ServerSocket(3214);
            } catch (IOException e) {
                log.log(Level.SEVERE, "Ошибка", e);
                e.printStackTrace();
            }
            while (true) {
                Socket socket = null;
                try {
                    socket = server.accept();
                    new ThreadsTemp(socket).start();
                } catch (IOException e) {
                    System.out.println( " ошибка : " + e);
                }
            }
        });
        rxThread1.start();
    }
    public void btnStop(ActionEvent actionEvent) {
        rxThread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try
                {
                    FileHandler xmlFile = new FileHandler ("log.%u.%g.xml", true);
                    log.addHandler (xmlFile); //Создаём новый FileHandler
                    FileHandler txtFile = new FileHandler ("log.%u.%g.txt", true);
                    //Создаём новый класс форматирования
                    SimpleFormatter txtFormatter = new SimpleFormatter ();
                    //устанавливаем Formatter
                    txtFile.setFormatter (txtFormatter);
                    //добавляем ещё один FileHandler в наш логгер
                    log.addHandler (txtFile);
                    server.close();
                    rxThread1.stop();
                    //System.exit(1);
                } catch(
                        IOException e)
                {
                    log.log(Level.SEVERE,
                            "Ошибочка сервера",
                            e);
                }
            }
        });
        rxThread1.start();
    }
}
