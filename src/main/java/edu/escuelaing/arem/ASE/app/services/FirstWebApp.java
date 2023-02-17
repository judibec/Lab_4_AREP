package edu.escuelaing.arem.ASE.app.services;

import edu.escuelaing.arem.ASE.app.HttpServer;
import edu.escuelaing.arem.ASE.app.spark.Route;
import edu.escuelaing.arem.ASE.app.spark.Spark;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class FirstWebApp {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InvocationTargetException, IllegalAccessException {
        HttpServer server = HttpServer.getInstance();
//        Spark.get("",(req,res)->{
//            res.setType("application/json");
//            return res.getResponse();
//            return "{\"nombre\": \"juan\"}";
//            String header = res.getHeader();
//            String body = res.getResponse();
//            return header+body;
//        });
        server.run(args);
    }
}
