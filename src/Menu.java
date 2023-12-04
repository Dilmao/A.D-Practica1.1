import java.util.Scanner;

public class Menu {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean salir = false;
        String opcion;

        while (!salir) {
            // Mostrar el menú al usuario
            System.out.println("""
                        \n*****************************************************************
                        1. Haer copia completa
                        2. Hacer copia incremental
                        0. Salir
                        *****************************************************************""");
            opcion = sc.next();

            switch (opcion) {
                case "0" -> salir = true;                           // Salir del bucle
                case "1" -> code.backUpCompleto.createCopy();       // Ejecutar la copia completa
                case "2" -> code.backUpIncremental.createCopy();    // Ejecutar la copia incremental
                default -> System.out.println("Opción no valida");  // Mensaje para opcion no reconocida
            }
        }
    }
}