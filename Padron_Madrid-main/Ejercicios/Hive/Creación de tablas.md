## **1 - Creacción de tablas en formato texto**
### **1.1) Crear una base de datos "datos_padron**

    CREATE DATABASE datos_padron;

*Una vez creada, se comprueba si se ha creado correctamente*

    SHOW DATABASES;

### **1.2) Crear la tabla de datos padron_txt con todos los campos del fichero CSV y cargar los datos**

*Antes de crear la tabla, es muy importante situarnos en la base de datos donde se pretENDen crear las tablas, para ello:*

    USE datos_padron;

*Ahora si pasamos a crear la tabla:*

    CREATE EXTERNAL TABLE IF NOT EXISTS padron_txt ( 
    cod_distrito STRING,
    desc_distrito STRING,
    cod_dist_barrio STRING,
    desc_barrio STRING,
    cod_barrio STRING,
    cod_dist_seccion STRING,
    cod_seccion STRING, 
    cod_edad_int STRING,
    EspanolesHombres INT, 
    EspanolesMujeres INT, 
    ExtranjerosHombres INT,
    ExtranjerosMujeres INT )
    ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.OpenCSVSerde'
    WITH SERDEPROPERTIES ("separatorChar" = "\073", "quoteChar" = '"')
    STORED AS TEXTFILE TBLPROPERTIES ("skip.header.line.count"="1");

*INCISO: quotechar = indica que los campos estan entre comillas*
    
*Para comprobar si se ha creado la tabla, mostramos las tablas que hay en la base de datos datos_padron.*

    SHOW TABLES;
*Por último, se cargan los datos que se encuentran en un csv en nuestro sistema local en la tabla usando LOAD DATA LOCAL INPATH. Pero antes, hay que introducir los datos en el contenedor que estamos utilizando. Para ello, en una consola nueva:*

    docker cp C:/Users/maria.puche/Rango_Edades_Seccion_202205.csv cloudera:/usr/Rango_Edades_Seccion_202205.csv

*Una vez tenemos los datos dentro de nuestro contenedor:*

    LOAD DATA LOCAL INPATH '/usr/Rango_Edades_Seccion_202205.csv' INTO TABLE padron_txt;  

*Se puede verificar que se han introducidos los datos en la tabla padron_txt con el siguiente comando:*

    SELECT * FROM padron_txt;
*Se observa que la tabla está compuesta por 238348 filas.*

### **1.3) Eliminar los espacios innecesarios que aparecen en la tabla padron_txt guardando la tabla resultado como padron_txt_2**
*Para ello se va a utilizar la función trim(), que permite borrar los espacios en blanco antes y después de un String. A parte de esta, hay otras variaciones de la función, como LTRIM y RTRIM, que eliminan los espacios del lado izquierdo y derecho, respectivamente.* 

    CREATE TABLE padron_txt_2 AS 
    SELECT  cod_distrito as cod_distrito, 
            trim(desc_distrito) as desc_distrito,
            cod_dist_barrio as cod_dist_barrio, 
            trim(desc_barrio) as desc_barrio, 
            cod_barrio as cod_barrio,  
            cod_dist_seccion as cod_dist_seccion, 
            cod_seccion as cod_seccion, 
            cod_edad_int as cod_edad_int, 
            CAST(espanoleshombres as INT), 
            CAST(espanolesmujeres as INT), 
            CAST(extranjeroshombres as INT), 
            CAST(extranjerosmujeres as INT)
    FROM padron_txt;

### **1.4) Diferencia entre LOAD DATA INPATH y LOAD DATA LOCAL INPATH**

*Al utilizar el comando LOCAL, se especifica que los datos que se estan cargando se encuentran en el sistema de archivos local. Mientras, que si no se usa LOCAL, el archivo que se pretENDe cargar está cargado en una ubicación de ruta HDFS.*

*Además, a esta función se puede se le puede incluir la opción OVERWRITE, para sobrescribir los datos de la tabla.*

### **1.5) En ambas tablas, los datos nulos se represetan por un espacio nulo y no por un identificador de nulos compresible. Por tanto, crear una nueva tabla que tome los valores nulos como cero.**

*Primero, se comprueba que hay espacios en blanco en las variables numerias (últimas 4 de la tabla)*

    SELECT 
        LENGTH(espanoleshombres), 
        LENGTH(espanolesmujeres), 
        LENGTH(extranjeroshombres), 
        LENGTH(extranjerosmujeres) 
    FROM padron_txt;

*En la salida, se puede apreciar que las celdas vacias tienen longitud 0, mientras que las otras tienen longitud 1. Por otra parte, como no se pueden crear tablas con el mismo nombre, vamos a cambiarle el nombre a padron_original y la nueva se nombrará como padron_txt.*

    ALTER TABLE padron_txt RENAME TO padron_original;

*Ahora si pasamos a sustituir las celdar con longitud 0 por un cero.*

    CREATE TABLE padron_txt AS SELECT
        cod_distrito, 
        desc_distrito,
        cod_dist_barrio,
        desc_barrio,
        cod_dist_seccion,
        cod_seccion,
        cod_edad_int, 
     CASE WHEN LENGTH(espanoleshombres)=0 THEN 0 ELSE espanoleshombres 
     END AS espanoleshombres, 
     CASE WHEN LENGTH(espanolesmujeres)=0 THEN 0 ELSE espanolesmujeres END AS espanolesmujeres, 
     CASE WHEN LENGTH(extranjeroshombres)=0 THEN 0 ELSE extranjeroshombres END AS extranjeroshombres, 
     CASE WHEN LENGTH(extranjerosmujeres)=0 THEN 0 ELSE extranjerosmujeres END AS extranjerosmujeres
    FROM padron_original;

*De nuevo, para comprobar que se ha realizado bien:*

    SELECT * FROM padron_txt;

*La tabla padron_original se puede borrar:*
    
    DROP TABLE padron_original;

*Se realiza el mismo procedimiento para la tabla padron_txt_2*

    ALTER TABLE padron_txt_2 RENAME TO padron_original;

*Ahora si pasamos a sustituir las celdar con longitud 0 por un cero.*

    CREATE TABLE padron_txt_2 AS SELECT
        cod_distrito, 
        desc_distrito,
        cod_dist_barrio,
        desc_barrio,
        cod_barrio,
        cod_dist_seccion,
        cod_seccion,
        cod_edad_int, 
     CASE WHEN LENGTH(espanoleshombres)=0 THEN 0 ELSE espanoleshombres 
     END AS espanoleshombres, 
     CASE WHEN LENGTH(espanolesmujeres)=0 THEN 0 ELSE espanolesmujeres END AS espanolesmujeres, 
     CASE WHEN LENGTH(extranjeroshombres)=0 THEN 0 ELSE extranjeroshombres END AS extranjeroshombres, 
     CASE WHEN LENGTH(extranjerosmujeres)=0 THEN 0 ELSE extranjerosmujeres END AS extranjerosmujeres
    FROM padron_original;

*De nuevo, para comprobar que se ha realizado bien:*

    SELECT * FROM padron_txt_2;

*La tabla padron_original se puede borrar:*
    
    DROP TABLE padron_original;

*Una vez terminado el apartado 1, deben haber dos tablas:*

    SHOW TABLES;

* *Padron_ txt -> Conserva los espacios innecesarios, no tiene comillas envolviendo los campos y los campos nulos son tratados como valor 0.*
* *Padron_txt2 -> No tiene espacios innecesarios, sin comillas envolviendo los campos y con los campos nulos como valor 0.*

## **2- Creación de tablas en formato columnar parquet**
### **2.1) ¿Qué es CTAS?**
*Es una consulta CREATE TABLE AS SELECT (CTAS) que crea una nueva tabla a partir de los resultados de la instrucción select de otra consulta.*

### **2.2) Crear la tabla padron_parquet (cuyos datos se almacenan en el formato columnas paquete) a través de la tabla padrn_txt mediante un CTAS.**
    CREATE TABLE padron_parquet STORED AS PARQUET AS SELECT 
        cod_distrito as cod_distrito, 
        desc_distrito as desc_distrito,
        cod_dist_barrio as cod_dist_barrio, 
        desc_barrio as desc_barrio, 
        cod_barrio as cod_barrio,  
        cod_dist_seccion as cod_dist_seccion, 
        cod_seccion as cod_seccion, 
        cod_edad_int as cod_edad_int, 
        CAST(espanoleshombres as INT), 
        CAST(espanolesmujeres as INT), 
        CAST(extranjeroshombres as INT), 
        CAST(extranjerosmujeres as INT)
    FROM padron_txt;

### **2.3) Crear la tabla padron_parquet_2 a través de la tabla padron_txt_2 mediante un CTAS.**

    CREATE TABLE padron_parquet_2 STORED AS PARQUET AS SELECT
            cod_distrito as cod_distrito, 
            desc_distrito as desc_distrito,
            cod_dist_barrio as cod_dist_barrio, 
            desc_barrio as desc_barrio, 
            cod_barrio as cod_barrio,  
            cod_dist_seccion as cod_dist_seccion, 
            cod_seccion as cod_seccion, 
            cod_edad_int as cod_edad_int, 
            CAST(espanoleshombres as INT), 
            CAST(espanolesmujeres as INT), 
            CAST(extranjeroshombres as INT), 
            CAST(extranjerosmujeres as INT)
        FROM padron_txt_2;

### **2.4) Crear otra tabla en formato parquet desde cero.**
     CREATE EXTERNAL TABLE IF NOT EXISTS parquet_prueba ( 
         cod_distrito INT, 
         desc_distrito STRING,
         cod_dist_barrio STRING,
         desc_barrio STRING,
         cod_barrio STRING,
         cod_dist_seccion STRING,
         cod_seccion STRING, 
         cod_edad_int STRING,
         EspanolesHombres INT, 
         EspanolesMujeres INT, 
         ExtranjerosHombres INT,
         ExtranjerosMujeres INT )
    ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.OpenCSVSerde'
    WITH SERDEPROPERTIES ("separatorChar" = "\073", "quoteChar" = '"')
    STORED AS PARQUET 
    TBLPROPERTIES ("skip.header.line.count"="1");

    LOAD DATA LOCAL INPATH '/usr/Rango_Edades_Seccion_202205.csv' INTO TABLE parquet_prueba;

### **2.5) Eliminar las bases de datos con tablas y vacias**
*En ese caso, no se utiliza la misma instrucción para borrar bases de datos con tablas y vacias. En primer lugar, si se quisera borrar la base de datos datos_padron, habría que utiñizar el siguiente comando:*
    DROP DATABASE datos_padron CASCADE;

*Sin embargo, si la base de datos se encuentra vacía no hace falta introducir el termini CASCADE. Un ejemplo sería:*

    DROP DATABASE datos_vacios;
