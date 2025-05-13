

import java.awt.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class Cliente extends JFrame {
    //declaracion de los componenetes graficos
    private JTextField num1Field, num2Field;
    private JComboBox<String> operaciones;
    private JLabel resultadoLabel;
    //declaracion de socket
    private Socket socket;
    //declaracion de los flujos de entrada y salida
    private DataOutputStream salida;
    private DataInputStream entrada;


       public static void main(String[] args) {
        new Cliente();
    }

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

        // Etiqueta y combo de operaciones que se pueden realizar
        JLabel operacionLabel = new JLabel("Seleccione la operación:");
        operacionLabel.setBounds(100, 80, 200, 30);
        add(operacionLabel);

        operaciones = new JComboBox<>(new String[]{"suma", "resta", "multiplicacion", "division", "porcentaje"});
        operaciones.setBounds(300, 80, 200, 30);
        add(operaciones);

        // Etiqueta y campo del número 1
        JLabel num1Label = new JLabel("Número 1:");
        num1Label.setBounds(100, 130, 100, 30);
        add(num1Label);

        num1Field = new JTextField();
        num1Field.setBounds(300, 130, 200, 30);
        add(num1Field);

        // Etiqueta y campo del número 2
        JLabel num2Label = new JLabel("Número 2:");
        num2Label.setBounds(100, 180, 100, 30);
        add(num2Label);

        num2Field = new JTextField();
        num2Field.setBounds(300, 180, 200, 30);
        add(num2Field);

        // Botón calcular para realizar cualquiera de las operaciones
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

        // Acción del botón calcular, cada vez que el ususario lo toque se enviara algo al servidor ya que ya esta conectado al servidor
        calcularBtn.addActionListener(e -> enviarOperacion());

        setVisible(true);
    }

    private void conectarConServidor() {
        try {
            //aqui se intancia el socket como tal, se le manda el ip y el puerto
            socket = new Socket("10.153.157.181", 5000);
            //creacion de los objetos de flujo de entrda y salida
            salida = new DataOutputStream(socket.getOutputStream());
            entrada = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "No se pudo conectar al servidor.");
            System.exit(1);
        }
    }

    private void enviarOperacion() {

        //en este metodo lo que hacemos es obtener los valores de dichos numeros
        try {
            double num1 = Double.parseDouble(num1Field.getText());
            double num2 = Double.parseDouble(num2Field.getText());
            //obtenemos el tipo de operacion
            String operacion = operaciones.getSelectedItem().toString();
            //enviamos al servidor el numero 1
            salida.writeDouble(num1);
            //enviamos al servidor el numero 2
            salida.writeDouble(num2);
            //enviamos al servidor el tipo de operacion
            salida.writeUTF(operacion);
            //creacion de la variable resultado y esto lo igualamos a lo que recibimos del servidor
            double resultado = entrada.readDouble();
            //colocamos el resultado junto al label
            resultadoLabel.setText("Resultado: " + resultado);
        } catch (NumberFormatException e) {
            resultadoLabel.setText("Ingrese solo números válidos.");
        } catch (IOException e) {
            resultadoLabel.setText("Error de comunicación con el servidor.");
        }
    }

 
}