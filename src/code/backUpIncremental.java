package code;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class backUpIncremental {
    public static void createCopy() {
        // Creacion de variables
        Date fecha = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd_HH-mm");
        String txtFecha = dateFormat.format(fecha);
        Date ultimaCopia = null;

        // Se introduce la ruta de los archivos
        Path rutaOrigen = Path.of("src/resources/original/");
        Path rutaDestinoIncremental = Path.of("src/resources/incremental/incremental" + txtFecha+"/");
        Path archivoLog = Path.of("src/resources/incremental/registro.log");

        // Verifica si el archivo de registro existe y lo crea si no
        if (Files.notExists(archivoLog)) {
            try {
                Files.createFile(archivoLog);
            } catch (IOException e) {
                System.err.println(">>> Error en la creación de 'registro.log'");
            }
        }

        // Lee la fecha de la última copia en el archivo de registro
        try {
            String txtUltimaCopia = Files.readString(archivoLog);
            if (!txtUltimaCopia.isEmpty()) {
                ultimaCopia = dateFormat.parse(txtUltimaCopia);
            }
        }catch (IOException e) {
            System.err.println(">>> Error en la lectura de 'registro.log'");
        } catch (ParseException e) {
            System.err.println(">>> Error en la conversion de 'registro.log'");
        }

        // Se crea la carpeta nueva
        try {
            Files.createDirectory(rutaDestinoIncremental);
            System.out.println("Creacion de carpeta exitosa");
        } catch (IOException e) {
            System.err.println(">>> Error en la creacion de la carpeta");
        }

        // Se realiza la copia incremental
        try {
            copiaIncremental copia = new copiaIncremental(ultimaCopia, rutaOrigen, rutaDestinoIncremental);
            Files.walkFileTree(rutaOrigen, copia);
        } catch (IOException e) {
            System.err.println(">>> No hay archivos nuevos o modificados en la ruta de origen: " + rutaOrigen);
        }

        // Se actualiza la fecha de la ultima copia en el archivo 'registro.log'
        try {
            Files.write(archivoLog, txtFecha.getBytes(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            System.err.println(">>> Error en la escritura de 'archivo.log'");
        }
    }

    private static class copiaIncremental extends SimpleFileVisitor<Path> {
        private final Date ultimaCopia;
        private final Path rutaOrigen;
        private final Path rutaDestino;

        public copiaIncremental(Date ultimaCopia, Path rutaOrigen, Path rutaDestino) {
            this.ultimaCopia = ultimaCopia;
            this.rutaOrigen = rutaOrigen;
            this.rutaDestino = rutaDestino;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
            if (attrs.lastModifiedTime().toMillis() > ultimaCopia.getTime()) {
                Path destino = rutaDestino.resolve(rutaOrigen.relativize(file));
                try {
                    Files.copy(file, destino, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    System.err.println(">>> Error en la creacion de la copia incremental");
                }
                System.out.println("Copia de: " + file + " en: " + destino);
            }
            return FileVisitResult.CONTINUE;
        }
    }
}
