# **IRIS**
Para aplicar los conocimientos adquiridos sobre Hive, se va a utilizar el dataset IRIS, formado por 150 observaciones de flores de la planta iris. 

En primer lugar, hay que entrar a Hive desde el terminal:

    hive;

Para que se muestren las cabeceras de las tablas en la pantalla se va a modificar la siguiente propiedad:

    set hive.cli.print.header=true; 

Una vez realizada la configuración, pasamos a crear una base de datos:

    CREATE DATABASE cursohivedb;

Para ver las bases de datos que hay creadas:

    SHOW DATABASES;

En el caso que querramos borrar una base de datos que no contiene tablas:

    DROP DATABASE IF EXISTS userdb;

Sin embargo, si la base de datos no está vacía hay que añadirle el comando CASCADE:

    DROP DATABASE IF EXISTS noempty CASCADE;

Para ver las tablas que hay dentro de una base de datos. Primero, hay que situarse en ella y, luego, indicarle que nos las muestre.

    USE cursohivedb;
    SHOW TABLES;

Como era de esperar, se encuenta vacía puesto que se acaba de crear.

Pasamos a crear una tabla llamada iris en la base de datos cursohivedb que contenga 5 columnas (s_length float,s_width float,p_length float,p_width float,clase string) cuyos campos estén separados por comas.

    CREATE TABLE iris ( 
        s_length FLOAT,
        s_width FLOAT,
        p_length FLOAT, 
        p_width FLOAT, 
        clase STRING) 
    ROW FORMAT DELIMITED FIELDS TERMINATED BY ',';

Para asegurarnos que la tabla se ha creado.

    SHOW TABLES;

Además, si se quiere ver el tipado de las columnas;

    DESC iris;

Ahora, se pretende introducir los datos del fichero "iris_completo.txt" que se encuentra en el sistema local en la tabla iris. 

    LOAD DATA LOCAL 
        INPATH '/home/cloudera/hive/iris_completo.txt' 
    INTO TABLE iris; 

Una vez introducidos los datos, se pretende ver el contenido.

    SELECT * FROM iris;

Si unicamente se quieren ver las 5 primeras filas.

    SELECT * FROM iris LIMIT 5;

Por último, antes de ponernos con el análisis de la tabla, insertar en la tabla la siguiente fila (1.0,3.2,4.3,5.7,"Iris-virginica")

    INSERT INTO TABLE iris 
        VALUES (1.0,3.2,4.3,5.7,"Iris-virginica"); 

# **Análisis**

Pasamos a analizar la tabla creada con una serie de consultas.

1) **Mostrar solo aquellas filas cuyo s_length sea mayor que 5.**

        SELECT * 
        FROM iris 
        WHERE s_length > 5;

2) **Seleccionar la media de s_width agrupados por clase.**

        SELECT avg(s_width) 
        FROM iris 
        WHERE s_width IS NOT NULL 
        GROUP BY clase;

3) **Contar el número de ocurrencias de cada clase**

        SELECT COUNT(clase) 
        FROM iris 
        GROUP BY clase;

4) **Seleccionar las clases que tengan más de 45 ocurrencias**

        SELECT clase 
        FROM iris 
        GROUP BY clase 
        HAVING COUNT(*)>45; 

5) **Ejecutar una query que devuelva la clase, p_length y el LEAD de p_length con Offset=1 y Default_Value =0, particionado por clase y ordenado por p_length**

        SELECT clase, p_length, LEAD(p_length,1,0) 
        OVER (PARTITION BY clase ORDER BY p_length) AS LEAD 
        FROM iris; 

6) **Seleccionar la clase, p_length, s_length, p_width, el número de valores distintos de p_length en todo el dataset, el valor máximo de s_length por clase y la media de p_width por clase, ordenado por clase y s_length de manera descendente.**

        SELECT clase, p_length, s_length, p_width,
            COUNT(p_length) over (partition by p_length) as pl_ct, 
            max(s_length) over (partition by clase) as sl_ct, 
            avg(p_width) over (partition by clase) as sl_av 
        FROM iris 
        ORDER BY clase,s_length DESC; 

Todo esto también se puede realizar a través de la interfaz de Hue de la máquina virtual de cloudera.