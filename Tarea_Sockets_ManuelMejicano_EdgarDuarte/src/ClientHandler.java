import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
       private final Socket socket;

        ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            System.out.println("Cliente conectado: " + socket.getInetAddress());
            try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true, java.nio.charset.StandardCharsets.UTF_8)) {

                // Enviar mensaje de bienvenida
                out.println("¡Bienvenido! Escribe tus mensajes (escribe 'bye' para salir).  ");

                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("Cliente dice: " + message);
                    if (message.equalsIgnoreCase("bye")) {
                        out.println("¡Adiós! Hasta luego.");
                        break;
                    }
                    String response = processMessage(message);
                    out.println(response);
                    System.out.println("Servidor responde: " + response);
                }
            } catch (IOException e) {
                System.err.println("Error con el cliente: " + e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (IOException ignored) {}
                System.out.println("Conexión cerrada: " + socket.getInetAddress());
            }
        }

        private String processMessage(String msg) {
            String lower = msg.trim().toLowerCase();
            if (lower.contains("hola")) {
                return "¡Hola! ¿Cómo estás hoy?";
            }
            if (lower.contains("tiempo") || lower.contains("hora")) {
                return "La hora actual del servidor es: " + java.time.LocalTime.now();
            }
            if (lower.matches(".*\\d+\\s*\\+\\s*\\d+.*")) {
                String[] parts = lower.split("\\+");
                try {
                    int a = Integer.parseInt(parts[0].trim());
                    int b = Integer.parseInt(parts[1].trim());
                    return "Resultado: " + (a + b);
                } catch (NumberFormatException e) {
                    return "No pude calcular la suma. Asegúrate de usar 'num1 + num2'.";
                }
            }
            return "Eco: " + msg;
        }
    }

