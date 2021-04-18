package sample;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import model.CityTemp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Controller extends Thread{
    public Text temper;
    private ServerSocket server;
    private Thread rxThread1;
    private Socket socket = null;
    private static Logger log = Logger.getLogger(Controller.class.getName());
    public void btnStart(ActionEvent actionEvent) {
        rxThread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try
                {
                    server = new ServerSocket(3214);
                } catch(
                        IOException e)
                {
                    e.printStackTrace();
                }
                {
                    try {
                        FileHandler xmlFile = new FileHandler ("log.xml", true);
                        log.addHandler (xmlFile); //Создаём новый FileHandler
                        FileHandler txtFile = new FileHandler ("log.log", true);
                        //Создаём новый класс форматирования
                        SimpleFormatter txtFormatter = new SimpleFormatter ();
                        //устанавливаем Formatter
                        txtFile.setFormatter (txtFormatter);
                        //добавляем ещё один FileHandler в наш логгер
                        log.addHandler (txtFile);
                        socket = server.accept();
                        new CityTemp(socket).start();
                    } catch (IOException e) {
                        log.log(Level.SEVERE,
                                "Ошибочка",
                                e);
                    }
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
                    FileHandler xmlFile = new FileHandler ("log.xml", true);
                    log.addHandler (xmlFile); //Создаём новый FileHandler
                    FileHandler txtFile = new FileHandler ("log.log", true);
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