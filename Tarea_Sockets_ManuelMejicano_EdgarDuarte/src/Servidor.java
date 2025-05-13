import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

    public static void main(String[] args) {
        try (ServerSocket servidor = new ServerSocket(5000)) {
            System.out.println("Servidor iniciado. Esperando cliente...");

            // El código bloquea el servidor hasta que un cliente se conecta
            Socket socket = servidor.accept();
            System.out.println("Cliente conectado.");

            //Se crean los flujos para la comunicación (entrada y salida)
            DataInputStream entrada = new DataInputStream(socket.getInputStream());
            DataOutputStream salida = new DataOutputStream(socket.getOutputStream());

            // Bucle principal de atención al cliente
            // Y este se hace siempre y ciuando el cliente esté conectado
            while (true) {
                double num1 = entrada.readDouble();
                double num2 = entrada.readDouble();

                // Se recibe la operación que el cliente desea 
                String operacion = entrada.readUTF();

                // Se calcula el resultado de la operación usando el método calcular
                double resultado = calcular(num1, num2, operacion);

                // Se evnia el resultado de vuelta al cliente
                salida.writeDouble(resultado);
            }

        } catch (IOException e) {
            //Básicamente sirve para manejar la expepción si el cliente se desconecta
            System.out.println("Servidor cerrado.");
        }
    }

    
    private static double calcular(double a, double b, String op) {
        switch (op) {
            case "suma":
                return a + b;

            case "resta":
                return a - b;

            case "multiplicacion":
                return a * b;

            case "division":
       
                return  a / b ;

            case "porcentaje":
                return (a * b) / 100;

            default:
                return 0;
        }
    }
}

