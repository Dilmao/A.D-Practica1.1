package code;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class backUpCompleto {
    public static void createCopy() {
        Path rutaOrigen = Path.of("src/resources/original/");
        Path rutaDestino = Path.of("src/resources/completo/");

        // Se comprueba si el archivo existe
        if (Files.exists(rutaOrigen)) {

            // Se comprueba si el archivo es un direcorio
            if (Files.isDirectory(rutaOrigen)) {

                // Se comprueba si se puede leer el directorio
                if (Files.isReadable(rutaOrigen)) {

                    // Se crea la copia de seguridad
                    try {
                        copiarCarpeta(rutaOrigen, rutaDestino);
                        System.out.println("Copia de seguridad completada");
                    } catch (IOException e) {
                        System.err.println(">>> Error durante la creacion de la copia de seguridad");
                    }

                } else {
                    System.err.println(">>> Error: No se tienen permisos para modificar el archivo");
                }

            } else {
                System.err.println(">>> Error: El archivo seleccionado no es un directorio");
            }

        } else {
            System.err.println(">>> Error: No se ha encontrado el archivo");
        }
    }

    private static void copiarCarpeta(Path origen, Path destino) throws IOException{
        // Recorre el árbol de archivos y directorios de la ruta de origen
        Files.walkFileTree(origen, new SimpleFileVisitor<>() {

            // Antes de visitar un directorio
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                // Obtiene la ruta de destino del directorio actual
                Path targetDir = destino.resolve(origen.relativize(dir));

                // Crea los directorios en la ruta de destino si no existen
                Files.createDirectories(targetDir);
                return FileVisitResult.CONTINUE;
            }

            // Al visitar un archivo
            @Override
            // Copia el archivo desde la ruta de origen a la ruta de destino
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.copy(file, destino.resolve(origen.relativize(file)), StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
