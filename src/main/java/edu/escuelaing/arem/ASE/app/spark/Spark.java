package edu.escuelaing.arem.ASE.app.spark;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Spark {

    public static Map<String, Response> cache = new HashMap<>();

    public static void get(String path, Route route){
        Request req = new Request();
        Response res = new Response();
        String body = route.get(req,res);
        res.setBody(body);
        res.setPath(path);
        cache.put(path,res);
    }

    public static String setCache (String path){
        Response res = new Response();
        String path2 = "src/main/resources"+path;
        byte[] content = new byte[0];
        try {
            Path file = Paths.get(path2);
            content = Files.readAllBytes(file);
            String type = (path2).split("\\.")[1];
            res.setType("text/"+type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        res.setBody(new String(content));
        cache.put(path, res);
        return res.getResponse();
    }


    public static String post(String value, String key){
        Response res = new Response();
        String body = "{"+key+":"+value+"}";
        res.setBody(body);
        res.setType("application/json");
        cache.put(key,res);
        return res.getResponse();
    }
}
