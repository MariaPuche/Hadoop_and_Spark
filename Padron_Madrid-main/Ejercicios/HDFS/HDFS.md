# **HDFS**
*A continuación, se va a realizar una inspección de la tablas externas o gestinadas.*

### **1) Crear dos documento diferentes de texto en el almacenamiento local que contenga una secuencia de números distribuidos en filas y separadosr por columnas, llámado datos**

*Se han creado manualmente, en la carpeta compartida de la VM* 

### **2) Crear un directorio en HDFS**

    hdfs dfs -mkdir /test

*Si quiero comprobar que se ha creado, listar las los directorios que hay en HDFS*

    hdfs dfs -ls

### **3) Mueve tu fichero datos1 al directorio test en HDFS con un comando desde consola.**
*Como se va a copiar el fichero desde local a hdfs se utiliza la función put.*

    hdfs dfs -put home/compartida/datos.txt /test

*En el caso de que se quiera copiar un fichero que se encuentra dentro de hdfs a otra carpeta de hdfs*

    hdfs dfs -cp /user/cloudera/datos.txt /test

## **4) HDFS y Hive**
 **4.1) Desde Hive, crea una nueva database por ejemplo con el nombre numeros.**

    CREATE DATABASE numeros;
    USE numeros;
 
 ### **4.2) Crear una tabla numeros_tbl que no sea externa y sin argumento location con tres columnas numéricas, campos separados por coma y delimitada por filas.**

     CREATE TABLE numeros_tbl (
        numero1 INT, 
        numero2 INT, 
        numero3 INT)
    ROW FORMAT DELIMITED FIELDS TERMINATED BY ',';

### **4.3) Carga los datos del fichero de texto datos1 almacenado en HDFS en la tabla numeros_tbl**
      LOAD DATA INPATH '/test/datos.txt' INTO TABLE numeros_tbl;

### **4.4) ¿Qué ocurre con el fichero datos.txt que se había almacenado en /test (HDFS)?
    hdfs dfs -ls /testhdf
*Ahora no aparece el archivo, cuando se cargan los datos, desaparecen los archivos de HDFS de la ruta /test, sin embargo, se van a /user/hive/warehouse/numeros.db/numeros_tbl/datos.txt*

    hdfs dfs -ls /user/hive/warehouse/numeros.db

### **4.5) ¿Qué ocurre si borras la tabla?**
    DROP TABLE numeros_tbl;
    hdfs dfs -ls /user/hive/warehouse/numeros.db
*Desaparecen los datos de las dos localizaciones.*

### **4.6) Crear una tabla numeros_tbl externa y sin argumento location con tres columnas numéricas, campos separados por coma y delimitada por filas.**
    CREATE EXTERNAL TABLE numeros_tbl (
        numero1 INT, 
        numero2 INT, 
        numero3 INT)
    ROW FORMAT DELIMITED FIELDS TERMINATED BY ',';

### **4.7) Carga los datos del fichero de texto datos2 almacenado en HDFS en la tabla externa numeros_tbl**

    hdfs dfs -cp /user/cloudera/datos2.txt # Hay que volver a hacerlo, se borra cuando introducimos los datos dentro de la tabla

    hdfs dfs -ls /test # Comprobar que se ha creado

    LOAD DATA INPATH '/test/datos2.txt' INTO TABLE numeros_tbl;

*Se puede comprobar que se han introducido los datos con:*

    SELECT * FROM numeros_tbl;
*Igual que antes, se observa que el documento datos2.txt desaparece del directorio /test, sin embargo, se encuentra en el directorio: user/hive/warehouse/numeros.db/numeros_tbl*

### **4.8) ¿Qué ocurre si borramos la tabla?**
    DROP TABLE numeros;
    hdfs dfs -ls /user/hive/warehouse/numeros.db/numeros_tbl

*En este caso, no se borra el fichero de la nueva ruta. En conclusión, una clara diferencia de las tablas externas y las que no lo son es que para las citadas primero no se borra el fichero de la segunda localización, mientras que para las otras si se borra. Lo que sería comun es que en ambos caso se borra el fichero de la localización inicial (/test)*

###  **4.9) Borra el fichero datos del directo que esté y vuelve a introducir datos.txt en el directorio que creamos al inicio**
*Para borrar el archivo:*

    hdfs dfs -rm /user/hive/warehouse/numeros.db/numeros_tbl/datos2.txt
    hdfs dfs -ls /user/hive/warehouse/numeros.db/numeros_tbl
*Para copiar el archivo:*

    hdfs dfs -cp /user/cloudera/datos.txt /test

### **4.10) Volver a crear la tabla externa numeros pero ahora con location que haga referencia a /test**
    CREATE EXTERNAL TABLE numeros_tbl (
        numero1 INT, 
        numero2 INT, 
        numero3 INT)
    ROW FORMAT DELIMITED FIELDS TERMINATED BY ',' LOCATION '/test';

    SELECT * FROM numeros_tbl;

*Se observa que la tabla se ha creado con los datos que hay en la ruta /test.*

### **4.11) Inserta el fichero "datos2" en el mismo directorio de HDFS que "datos1"  y vuelve a hacer la consulta anterior sobre la misma tabla.**
     hdfs dfs -cp /user/cloudera/datos2.txt /test
     SELECT * FROM numeros_tbl;
*Se han añadido los datos que hay en el fichero datos2.txt a la tabla numeos_tbl.*

*En resumen, si no añadimos location hay que cargar los datos con LOAD DATA, mientras que si lo añadimos, los datos se cargan de forma implicita cuando introducimos los ficheros en la ruta indicada.*





