# AREP TALLER 4

Creacion de un servidor tipo apache que tiene una funcionalidad similar a SpringBoot

## Iniciando

### Prerequisites

* Git: controlador de versiones y administrador de repositorio
* Java: Ambiente de desarrollo
* Maven: Administrador del ciclo de vida del proyecto

### Installing

Primero debemos clonar el repositorio con el siguiente comando

```
git clone https://github.com/judibec/Lab_4_AREP.git
```

Luego entramos a la carpeta donde se encuenta el archivo ```pom.xml``` y ejecutamos el siguiente comando

```
java -cp ./target/classes edu.escuelaing.arem.ASE.app.services.FirstWebApp
```

Entramos a cualquier Browser y entramos a la direccion http://localhost:34000/ + index, hello o image, ahi podemos ver los diferentes controllers creados

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

Se contruyo un POJO que mediante anotaciones podemos hacer el llamado a varios controller con una anotacion especifica, cada controller tiene su manera de funcionar pues la anotacion funciona como una clase de interfaz funcional.
Los patrones usados son singleton y adaptador

## TEST

