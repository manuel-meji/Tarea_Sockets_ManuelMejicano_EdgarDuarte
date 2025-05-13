

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
        setSize(800, 500);
        setLocationRelativeTo(null); // Centra la ventana
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        // Etiqueta de saludo
        JLabel saludoLabel = new JLabel("Bienvenido a la Calculadora por Socket TCP");
        saludoLabel.setFont(new Font("Arial", Font.BOLD, 20));
        saludoLabel.setBounds(200, 20, 500, 30);
        add(saludoLabel);

        // Etiqueta y combo de operaciones
        JLabel operacionLabel = new JLabel("Seleccione la operación:");
        operacionLabel.setBounds(100, 80, 200, 30);
        add(operacionLabel);

        operaciones = new JComboBox<>(new String[]{"suma", "resta", "multiplicacion", "division", "porcentaje"});
        operaciones.setBounds(300, 80, 200, 30);
        add(operaciones);

        // Etiqueta y campo número 1
        JLabel num1Label = new JLabel("Número 1:");
        num1Label.setBounds(100, 130, 100, 30);
        add(num1Label);

        num1Field = new JTextField();
        num1Field.setBounds(300, 130, 200, 30);
        add(num1Field);

        // Etiqueta y campo número 2
        JLabel num2Label = new JLabel("Número 2:");
        num2Label.setBounds(100, 180, 100, 30);
        add(num2Label);

        num2Field = new JTextField();
        num2Field.setBounds(300, 180, 200, 30);
        add(num2Field);

        // Botón calcular
        JButton calcularBtn = new JButton("Calcular");
        calcularBtn.setBounds(300, 240, 200, 40);
        add(calcularBtn);

        // Etiqueta resultado
        resultadoLabel = new JLabel("Resultado: ");
        resultadoLabel.setFont(new Font("Arial", Font.BOLD, 16));
        resultadoLabel.setBounds(300, 300, 300, 30);
        add(resultadoLabel);

        // Conectar al servidor
        conectarConServidor();

        // Acción del botón
        calcularBtn.addActionListener(e -> enviarOperacion());

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
        } catch (NumberFormatException e) {
            resultadoLabel.setText("Ingrese solo números válidos.");
        } catch (IOException e) {
            resultadoLabel.setText("Error de comunicación con el servidor.");
        }
    }

    public static void main(String[] args) {
        new Cliente();
    }
}