import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class Servidor {

    public static void main(String[] args) {
        try (ServerSocket servidor = new ServerSocket(5000)) {
            System.out.println("Servidor iniciado. Esperando cliente...");

            // Acepta una sola conexión (no se usa multicliente ni hilos)
            Socket socket = servidor.accept();
            System.out.println("Cliente conectado.");

            DataInputStream entrada = new DataInputStream(socket.getInputStream());
            DataOutputStream salida = new DataOutputStream(socket.getOutputStream());

            while (true) {
                double num1 = entrada.readDouble();
                double num2 = entrada.readDouble();
                String operacion = entrada.readUTF();

                double resultado = calcular(num1, num2, operacion);
                salida.writeDouble(resultado);
            }

        } catch (IOException e) {
            System.out.println("Servidor cerrado.");
        }
    }

    private static double calcular(double a, double b, String op) {
        return switch (op) {
            case "suma" -> a + b;
            case "resta" -> a - b;
            case "multiplicacion" -> a * b;
            case "division" -> (b != 0) ? a / b : 0;
            case "porcentaje" -> (a * b) / 100;
            default -> 0;
        };
    }
}

