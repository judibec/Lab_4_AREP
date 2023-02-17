package edu.escuelaing.arem.ASE.app;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import edu.escuelaing.arem.ASE.app.services.FirstWebApp;
import edu.escuelaing.arem.ASE.app.services.RestService;
import edu.escuelaing.arem.ASE.app.spark.Response;
import edu.escuelaing.arem.ASE.app.spark.Spark;

import java.net.*;
import java.io.*;
import java.util.*;

/**
 * Clase main que levanta el servidor y carga la pagina
 */
public class HttpServer {
    private static HttpServer instance = new HttpServer();
    private Map<String,RestService> services = new HashMap<>();
    private Response res;

    private HttpServer(){}

    public static HttpServer getInstance(){
        return instance;
    }

    public void run(String[] args) throws IOException {
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
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received: " + inputLine);
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
                if (Spark.cache.containsKey(request)) {
                    outputLine = Spark.cache.get(request).getResponse();
                } else if (!Spark.cache.containsKey(request) && !request.contains("favicon")) {
                    outputLine = Spark.setCache(request);
                }
            }else if (Objects.equals(verb, "POST")) {
                if(!request.contains("favicon")){
                    String value = request.split("=")[1];
                    String key = request.split("=")[0];
                    key = key.split("\\?")[1];
                    outputLine = Spark.post(value,key);

                }
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

//    private String execute(String serviceName){
//        res.setPath("src/main/resources"+serviceName);
//        String type = serviceName.split("\\.")[1];
//        res.setType(type);
//        return serviceName;
//    }
//
//    public void addServices(String key, RestService service){
//        services.put(key,service);
//    }

}
