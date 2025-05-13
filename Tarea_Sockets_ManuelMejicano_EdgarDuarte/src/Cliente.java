// Archivo: Cliente.java
import java.awt.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class Cliente extends JFrame {

    private JTextField num1Field, num2Field;
    private JComboBox<String> operaciones;
    private JLabel resultadoLabel;

    private Socket socket;
    private DataOutputStream salida;
    private DataInputStream entrada;

    public Cliente() {
        setTitle("Calculadora TCP");
        setSize(400, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 2));

        add(new JLabel("Bienvenido, elija una operación:"));

        operaciones = new JComboBox<>(new String[]{"suma", "resta", "multiplicacion", "division", "porcentaje"});
        add(operaciones);

        add(new JLabel("Número 1:"));
        num1Field = new JTextField();
        add(num1Field);

        add(new JLabel("Número 2:"));
        num2Field = new JTextField();
        add(num2Field);

        JButton calcularBtn = new JButton("Calcular");
        add(calcularBtn);

        resultadoLabel = new JLabel("Resultado:");
        add(resultadoLabel);

        calcularBtn.addActionListener(e -> enviarOperacion());

        conectarConServidor();

        setVisible(true);
    }

    private void conectarConServidor() {
        try {
            socket = new Socket("localhost", 5000);
            salida = new DataOutputStream(socket.getOutputStream());
            entrada = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "No se pudo conectar al servidor.");
            System.exit(1);
        }
    }

    private void enviarOperacion() {
        try {
            double num1 = Double.parseDouble(num1Field.getText());
            double num2 = Double.parseDouble(num2Field.getText());
            String operacion = operaciones.getSelectedItem().toString();

            salida.writeDouble(num1);
            salida.writeDouble(num2);
            salida.writeUTF(operacion);

            double resultado = entrada.readDouble();
            resultadoLabel.setText("Resultado: " + resultado);
        } catch (Exception e) {
            resultadoLabel.setText("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new Cliente();
    }
}
