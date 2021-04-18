package sample;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.Socket;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Controller {
    public Label label;
    public TextField textField;
    private Thread rxThread;
    Socket socket = null;
    private static Logger log = Logger.getLogger(Controller.class.getName());
    public void buttonClicked(ActionEvent actionEvent) {
       // rxThread = new Thread(new Runnable() {
            //@Override
            //public void run() {
                try {
                    FileHandler xmlFile = new FileHandler ("logClient.%u.%g.xml", true);
                    log.addHandler (xmlFile); //Создаём новый FileHandler
                    FileHandler txtFile = new FileHandler ("logClient.%u.%g.txt", true);
                    //Создаём новый класс форматирования
                    SimpleFormatter txtFormatter = new SimpleFormatter ();
                    //устанавливаем Formatter
                    txtFile.setFormatter (txtFormatter);
                    //добавляем ещё один FileHandler в наш логгер
                    log.addHandler (txtFile);
                    socket = new Socket("localhost", 3214);
                    socket.getOutputStream().write(0);
                    PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
                    printWriter.write(textField.getText() + "\n");
                    printWriter.flush();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String temp = bufferedReader.readLine();
                    label.setText("Температура в городе " + textField.getText()  + " " + temp + "°C" );
                    printWriter.write(temp);
                    printWriter.flush();
                    bufferedReader.close();
                    printWriter.close();

                } catch (IOException e) {
                    log.log(Level.SEVERE,
                            "Client Error",
                            e);
                }
            }
  //  });
     //   rxThread.start();
//}
}
