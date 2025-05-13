import java.io.*;
import java.net.*;

public class Client {
    public static final String HOST = "localhost";
    public static final int PORT = 5000;

    public static void main(String[] args) {
        try (Socket socket = new Socket(HOST, PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in))) {

            // Mostrar mensaje de bienvenida del servidor
            System.out.println(in.readLine());
            String userMsg;
            while (true) {
                System.out.print("Tú: ");
                userMsg = stdin.readLine();
                if (userMsg == null) break;
                out.println(userMsg);
                String response = in.readLine();
                System.out.println("Servidor: " + response);
                if ("¡Adiós! Hasta luego.".equals(response)) {
                    break;
                }
            }

        } catch (UnknownHostException e) {
            System.err.println("Host desconocido: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error de I/O: " + e.getMessage());
        }
    }
}
