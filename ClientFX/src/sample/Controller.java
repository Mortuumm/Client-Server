package sample;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;

import java.io.*;
import java.net.Socket;
import java.util.Optional;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller {
    public Label label;
    public TextField textField;
    private static Logger log = Logger.getLogger(Controller.class.getName());
    private String city;

    public void initialize() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Метеосервер");
        dialog.setHeaderText("Введите свой город");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(s -> city = s);
    }

    public void buttonClicked(ActionEvent actionEvent) {
        Socket socket = null;
        try {
            FileHandler xmlFile = new FileHandler ("logClient.xml", true);
            log.addHandler (xmlFile);
            FileHandler txtFile = new FileHandler ("logClient.%u.%g.txt", true);
            SimpleFormatter txtFormatter = new SimpleFormatter ();
            txtFile.setFormatter (txtFormatter);
            log.addHandler (txtFile);

            socket = new Socket("localhost", 3214);
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
            printWriter.write(textField.getText() + "+" + city + "\n");
            printWriter.flush();

            Pattern tempPattern = Pattern.compile("[0-9]");
            Matcher matcher = tempPattern.matcher(textField.getText());
            if (!matcher.find()) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String temp = bufferedReader.readLine();
                bufferedReader.close();
                label.setText("Температура в городе " + textField.getText()  + temp + " °C");
            }

            printWriter.close();
        } catch (IOException e) {
            log.log(Level.SEVERE, "Ошибка клиента", e);
            System.out.println( "клиента ошибка : " + e);
        }
    }
}
