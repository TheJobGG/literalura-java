package com.aluralatam.literalura.main;

import com.aluralatam.literalura.model.*;
import com.aluralatam.literalura.repository.AutorRepository;
import com.aluralatam.literalura.repository.LibroRepository;
import com.aluralatam.literalura.service.ConsumoAPI;
import com.aluralatam.literalura.service.ConversorDatos;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Principal {

    private LibroRepository libroRepository;
    private AutorRepository autorRepository;

//    Constructor para pasar los objetos de repository y manejar operaciones en la base de datos
    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }


//    Para recibir datos desde el teclado en consola
    private final Scanner scanner = new Scanner(System.in);

//    Para hacer peticiones http
    private final ConsumoAPI consumoApi = new ConsumoAPI();

//    Conversor de Json a objetos o modelos Java
    private final ConversorDatos conversorDatos = new ConversorDatos();

//    Url base de la api
    private final String URL_BASE = "https://gutendex.com/books/";


    //    Método inicial para mostrar el menú y acceder a las operaciones
    public void mostrarMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar libro por título
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma
                    
                    0 - Salir
                    """;
            try{
                System.out.println(menu);
                opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1:
                        buscarLibroPorTitulo();
                        break;
                    case 2:
                        listarLibrosRegistrados();
                        break;
                    case 3:
                        listarAutoresRegistrados();
                        break;
                    case 4:
                        listarAutoresVivosEnDeterminadoAnio();
                        break;
                    case 5:
                        listarLibrosPorIdioma();
                        break;
                    case 0:
                        System.out.println("Cerrando la aplicación...");
                        break;
                    default:
                        System.out.println("Opción inválida.");
                }
            }catch (InputMismatchException e){
                scanner.nextLine();
                System.out.println("\nSelecciona una opción válida.");
                System.out.println("=============================");
            }
        }

    }

    private void buscarLibroPorTitulo() {
        System.out.println("Ingresa el título del libro que deseas buscar: ");
        String tituloLibroABuscar = scanner.nextLine();

        String urlAConsultar = URL_BASE + "?search=" + URLEncoder.encode(tituloLibroABuscar, StandardCharsets.UTF_8);

        var json = consumoApi.buscarLibroEnApi(urlAConsultar);

        DatosJsonGutendex datosJsonGutendex = conversorDatos.obtenerDatos(json, DatosJsonGutendex.class);

        List<DatosLibro> listaLibros = datosJsonGutendex.results();

        if (listaLibros.size() <= 0) {
            System.out.println("No se encontró ningún libro, intenta con otro título.\n");
            return;
        }

        DatosLibro libroEncontrado = listaLibros.get(0);

        Libro busquedaEnDbDeLibro  = libroRepository.buscaLibroPorTitulo(libroEncontrado.titulo());

//        Evaluamos si se encontró el libro en la base de datos, de serlo así mostramos el libro y listo
        if ( busquedaEnDbDeLibro != null) {
            System.out.println(busquedaEnDbDeLibro);
            return;
        }

//        Si no se encontró el libro preparamos los datos para guardarlos en la db.

        // Crear una lista de autores a partir de la respuesta de la API
        List<Autor> listaAutores = new ArrayList<>();

        for (DatosAutor datosAutor : libroEncontrado.authors()) {
            Autor autor = new Autor(datosAutor.nombre(), datosAutor.fechaNacimiento(), datosAutor.fechaFallecimiento());
            listaAutores.add(autor);
        }

        // Se crea el objeto Libro y se asigna a la lista de autores
        Libro libro = new Libro();
        libro.setTitulo(libroEncontrado.titulo());
        libro.setIdioma(libroEncontrado.idioma().get(0));
        libro.setNumeroDescargas(libroEncontrado.numeroDescargas());
        libro.setAutores(listaAutores);

        List<Libro> listaLibrosAGuardar = new ArrayList<>();

        listaLibrosAGuardar.add(libro);

        // Se asocia el libro a cada autor
        for (Autor autor : listaAutores) {
            autor.setLibro(listaLibrosAGuardar);
        }

        // Guardar libro y autores en la base de datos
        libroRepository.save(libro);

//       Mostrar libro guardado....
        System.out.println(libro);

    }



    private void listarLibrosRegistrados(){
        List<Libro> listaDeLibros = libroRepository.findAll();
        listaDeLibros.forEach(System.out::println);
    }

    private void listarAutoresRegistrados(){
        List<Autor> listaDeAutores = autorRepository.findAll();
        listaDeAutores.forEach(System.out::println);
    }

    private void listarAutoresVivosEnDeterminadoAnio(){
        System.out.println("Ingresa el año en que deseas ver los autores que estaban vivos: ");
        int anio = scanner.nextInt();
        List<Autor> autoresVivosEnAnio = autorRepository.autoresVivosEnAnio(anio);

        if(autoresVivosEnAnio.size() <= 0){
            System.out.println("\n------------------------------------------------------------");
            System.out.println("No hay autores vivos registrados en el año " + anio + ". Prueba con otro");
            System.out.println("------------------------------------------------------------\n");
            return;
        }

        System.out.println("\n------------------------------------------------------------\n");
        autoresVivosEnAnio.forEach(System.out::println);
        System.out.println("------------------------------------------------------------\n");
    }

    private void listarLibrosPorIdioma(){
        List<String> idiomas = libroRepository.idiomas();

        System.out.println("(Códigos disponibles...)");
        idiomas.forEach(i -> {
            String nombreDeIdioma = DiccionarioIdiomas.obtenerNombreDelIdioma(i);
            System.out.println(i + ": " + nombreDeIdioma);
        });

        System.out.println("\nElige uno de los códigos de idioma anteriores parar buscar los libros: ");
        System.out.println("");
        String codigoIdioma = scanner.nextLine();
        if(!idiomas.contains(codigoIdioma)){
            System.out.println("El código " + codigoIdioma + " no existe...");
            System.out.println("Selecciona un código disponible, por ejemplo \"es\" para Español");
            System.out.println("------------------------------------------------------------\n");
            return;
        }

        System.out.println("Idioma elegido: " + codigoIdioma + " - " + DiccionarioIdiomas.obtenerNombreDelIdioma(codigoIdioma));

        List<Libro> librosPorIdioma = libroRepository.librosPorIdioma(codigoIdioma);
        librosPorIdioma.forEach(System.out::println);

    }
}
