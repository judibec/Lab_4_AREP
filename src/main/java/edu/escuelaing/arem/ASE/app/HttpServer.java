package edu.escuelaing.arem.ASE.app;

import edu.escuelaing.arem.ASE.app.controller.annotations.Component;
import edu.escuelaing.arem.ASE.app.controller.annotations.RequestMapping;
import edu.escuelaing.arem.ASE.app.spark.Response;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.*;
import java.io.*;
import java.util.*;

/**
 * Clase main que levanta el servidor y carga la pagina
 */
public class HttpServer {
    private static HttpServer instance = new HttpServer();
    private Map<String,Method> services = new HashMap<>();
    private Response res;
    private static OutputStream outputStream = null;
    private final String root = "edu/escuelaing/arem/ASE/app/controller";

    private HttpServer(){}

    public static HttpServer getInstance(){
        return instance;
    }

    public void run(String[] args) throws IOException, ClassNotFoundException, InvocationTargetException, IllegalAccessException {
//        String className = args[0];
        List<Class<?>> classes = getClasses();
        for (Class<?> clasS:classes){
            if(clasS.isAnnotationPresent(Component.class)){
                Class<?> c = Class.forName(clasS.getName());
                Method[] m = c.getMethods();
                for (Method me: m){
                    if(me.isAnnotationPresent(RequestMapping.class)){
                        String key = me.getAnnotation(RequestMapping.class).value();
                        services.put(key,me);
                    }
                }
            }

        }

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(34000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 34000.");
            System.exit(1);
        }

        boolean running = true;
        while(running) {
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String inputLine, outputLine = null;
            String title = "";
            boolean firs_line = true;
            String request = "/simple";
            String verb = "";
            outputStream = clientSocket.getOutputStream();
            while ((inputLine = in.readLine()) != null) {
//                System.out.println("Received: " + inputLine);
                if(firs_line){
                    request = inputLine.split(" ")[1];
                    verb = inputLine.split(" ")[0];
                    firs_line = false;
                }
                if(inputLine.contains("title?name")){
                    String[] firstSplit = inputLine.split("=");
                    title = (firstSplit[1].split("HTTP"))[0];
                }
                if (!in.ready()) {
                    break;
                }
            }
            if (Objects.equals(verb, "GET")) {
                System.out.println(request);
                if(services.containsKey(request)){
                    outputLine = services.get(request).invoke(null).toString();
                }
//                if (Spark.cache.containsKey(request)) {
//                    outputLine = Spark.cache.get(request).getResponse();
//                } else if (!Spark.cache.containsKey(request) && !request.contains("favicon")) {
//                    outputLine = Spark.setCache(request);
//                }
//            }else if (Objects.equals(verb, "POST")) {
//                if(!request.contains("favicon")){
//                    String value = request.split("=")[1];
//                    String key = request.split("=")[0];
//                    key = key.split("\\?")[1];
//                    outputLine = Spark.post(value,key);
//
//                }
            }
            else if(!Objects.equals(title, "")){
                outputLine = APIanswer(title);
            }else {
                outputLine = htmlOriginal();
            }
            out.println(outputLine);
            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }

    /**
     * Crea una tabla en formato HTML
     * @param apiAnswer recibe el String que tiene formato de JSON de la respuesta de la pelicula
     * @return String con sintaxis HTML para generar una tabla con el JSON que entra
     */
    private static String createTable(String apiAnswer){
        String[] apiDatini = apiAnswer.split(":");
//        ArrayList<String> apiDatafin = (ArrayList<String>) Arrays.stream(apiDatini).toList();
        String tabla = "<table> \n";
        for (int i = 0;i<(apiDatini.length);i++) {
                String[] temporalAnswer = apiDatini[i].split(",");
                tabla+="<td>" + Arrays.toString(Arrays.copyOf(temporalAnswer, temporalAnswer.length - 1)).replace("[","").replace("]","").replace("}","") + "</td>\n</tr>\n";
                tabla+="<tr>\n<td>" +  temporalAnswer[temporalAnswer.length-1].replace("{","").replace("[","") + "</td>\n";


//                tabla += "<tr>\n<td>" + apiDatini[i] + "</td>\n";

        }
        tabla += "</table>";
        return tabla;

    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    private static String htmlOriginal(){
        return "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n"
                + "<!DOCTYPE html>\n" +
                "<html>\n" +
                "    <head>\n" +
                "        <title>Form Example</title>\n" +
                "        <meta charset=\"UTF-8\">\n" +
                "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    </head>\n" +
                "    <body>\n" +
                "        <h1>Search your movie GET</h1>\n" +
                "        <form action=\"/hello\">\n" +
                "            <label for=\"name\">Title:</label><br>\n" +
                "            <input type=\"text\" id=\"name\" name=\"name\" value=\"John\"><br><br>\n" +
                "            <input type=\"button\" value=\"Submit\" onclick=\"loadGetMsg()\">\n" +
                "        </form> \n" + "<br>"+
                "        <div id=\"getrespmsg\"></div>\n" +
                "\n" +
                "        <script>\n" +
                "            function loadGetMsg() {\n" +
                "                let nameVar = document.getElementById(\"name\").value;\n" +
                "                const xhttp = new XMLHttpRequest();\n" +
                "                xhttp.onload = function() {\n" +
                "                    document.getElementById(\"getrespmsg\").innerHTML =\n" +
                "                    this.responseText;\n" +
                "                }\n" +
                "                xhttp.open(\"GET\", \"/title?name=\"+nameVar);\n" +
                "                xhttp.send();\n" +
                "            }\n" +
                "        </script>\n" +
                "\n" +
                "</html>";
    }

    private static String APIanswer(String title) throws IOException {
        return "HTTP/1.1 200 OK\r\n"
                + "Content-Type: application/json\r\n"
                + "\r\n" +
                "<style>\n" +
                "table, th, td {\n" +
                "  border:1px solid black;\n" +
                "}\n" +
                "</style>"+
                createTable(Cache.findTitle(title));
    }

    private List<Class<?>> getClasses(){
        List<Class<?>> classes = new ArrayList<>();
        try{
            for (String cp: classPaths()){
                File file = new File(cp + "/" + root);
                if(file.exists() && file.isDirectory()){
                    for (File cf: Objects.requireNonNull(file.listFiles())){
                        if(cf.isFile() && cf.getName().endsWith(".class")){
                            String rootTemp = root.replace("/",".");
                            String className = rootTemp+"."+cf.getName().replace(".class","");
                            Class<?> clasS =  Class.forName(className);
                            classes.add(clasS);
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return classes;
    }

    private ArrayList<String> classPaths(){
        String classPath = System.getProperty("java.class.path");
        String[] classPaths =  classPath.split(System.getProperty("path.separator"));
        return new ArrayList<>(Arrays.asList(classPaths));
    }

}
