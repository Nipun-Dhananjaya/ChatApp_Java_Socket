import com.playtech.util.Server;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.util.ArrayList;

public class AppInitializer extends Application {
    public static void main(String[] args) {
        /*Thread t1= new Thread(()->{
            try {
                ServerSocket serverSocket = Server.getServerSocket();
                Socket socket = serverSocket.accept();
                DataInputStream dataInputStream=new DataInputStream(socket.getInputStream());
                DataOutputStream dataOutputStream=new DataOutputStream(socket.getOutputStream());
                while (true){
                    message= dataInputStream.readUTF();
                    dataOutputStream.writeUTF(message);
                    System.out.println("mess:"+message);
                    dataOutputStream.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        t1.start();*/
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/LoginForm.fxml"))));
        primaryStage.show();
    }
}
