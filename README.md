# AREP TALLER 2

Creacion de un framework que genera peticion GET y POST similar a SPARK 

## Iniciando

### Prerequisites

* Git: controlador de versiones y administrador de repositorio
* Java: Ambiente de desarrollo
* Maven: Administrador del ciclo de vida del proyecto

### Installing

Primero debemos clonar el repositorio con el siguiente comando

```
git clone https://github.com/judibec/AREP_3_SPARK.git
```

Luego entramos a la carpeta donde se encuenta el archivo ```pom.xml``` y ejecutamos el siguiente comando

```
mvn clean package exec:java -D "exec.mainClass"="edu.escuelaing.arem.ASE.app.services.FirstWebApp"
```

Entramos a cualquier Browser y entramos a la direccion http://localhost:34000/ podemos agregar index con diferentes tipos como css,html o js tambien imagen para ver los diferentes archivos que se crearon

Para visualizar el javadoc basta con ejecutar el comando 

```
mvn javadoc:javadoc
```
Y entramos a dicha ruta

```
...\miprimera-app\target\site\apidocs
```
## Built With

* [Maven](https://maven.apache.org/) - Administrador de dependencias

## Version

Version 1.0

## Authors

* **Juan Diego Becerra Peña**: [judibec](https://github.com/judibec)

## Descripcion especifica

Se contruyo una interfaz funcional la cual permite la creacion de las funciones lambda al momento de correr el servidor, estas permiten hacer el GET y el POST sin necesidad de servicios extra, de esta manera tambien lee el tipo del archivo para mostrarlo con el content-type respectivo. 

## TEST

