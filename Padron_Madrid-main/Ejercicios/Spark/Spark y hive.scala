// Databricks notebook source
// MAGIC %md
// MAGIC ## **SPARK Y HIVE**
// MAGIC 
// MAGIC Por último, prueba a hacer los ejercicios sugeridos en la parte de Hive con el csv "Datos Padrón" (incluyendo la importación con Regex) utilizando desde Spark EXCLUSIVAMENTE sentencias spark.sql.
// MAGIC 
// MAGIC ##### **1) Crear la base de datos y la tabla padron_txt.**

// COMMAND ----------

import org.apache.spark.sql.SparkSession


// Crear la base de datos
spark.sql("""CREATE DATABASE IF NOT EXISTS datos_padron""")

// Situarse en la base de datos creada
spark.sql(""" USE datos_padron""")

// Crear la tabla de datos datos_padron
spark.sql("""CREATE TABLE IF NOT EXISTS padron_txt 
  USING CSV OPTIONS (
    header = "true",
    delimiter = ";",
    inferschema = "true",
    path = "/FileStore/shared_uploads/maria.puche@bosonit.com/Rango_Edades_Seccion_202205-1.csv") """)

// Visualizar la tabla creada
spark.sql("""SELECT * FROM padron_txt""").show()

// COMMAND ----------

// MAGIC %md
// MAGIC 
// MAGIC ##### **2) Eliminar los espacios innecesarios guardando la tabla resultado como padron_txt_2**

// COMMAND ----------

// Quitar espacios innecesarios de la tabla padron_txt y crea una nueva tabla a partir de padron_txt
spark.sql(""" CREATE TABLE padron_txt_2 AS 
  SELECT COD_DISTRITO AS Cod_Distrito, 
         TRIM(DESC_DISTRITO) AS Desc_Distrito,
         COD_DIST_BARRIO AS Cod_Dist_Barrio,
         TRIM(DESC_BARRIO) AS Desc_Barrio,
         COD_BARRIO AS Cod_Barrio,
         COD_DIST_SECCION AS Cod_Dist_Seccion,
         COD_SECCION AS Cod_Seccion,
         COD_EDAD_INT AS Cod_Edad_Int,
         EspanolesHombres AS EspanolesHombres,
         EspanolesMujeres AS EspanolesMujeres, 
         ExtranjerosHombres AS ExtranjerosHombres, 
         ExtranjerosMujeres AS ExtranjerosMujeres 
 FROM padron_txt""")

// COMMAND ----------

// MAGIC %md
// MAGIC ##### **3) Sustituir los null por cero en la tabla padron_txt.**

// COMMAND ----------

spark.sql(""" CREATE TABLE padron_txt_new AS 
  SELECT cod_distrito, desc_distrito,cod_dist_barrio,desc_barrio,cod_dist_seccion,cod_seccion,cod_edad_int, 
    CASE WHEN espanoleshombres IS NULL THEN 0 ELSE espanoleshombres END AS espanoleshombres, 
    CASE WHEN espanolesmujeres IS NULL THEN 0 ELSE espanolesmujeres END AS espanolesmujeres, 
    CASE WHEN extranjeroshombres IS NULL THEN 0 ELSE extranjeroshombres END AS extranjeroshombres, 
    CASE WHEN extranjerosmujeres IS NULL THEN 0 ELSE extranjerosmujeres END AS extranjerosmujeres 
 FROM padron_txt;""")

// COMMAND ----------

// Ver la tabla
spark.sql("""SELECT * FROM padron_txt_new""").show(5,false)

// COMMAND ----------

// MAGIC %md
// MAGIC 
// MAGIC ##### **4) Crear la tabla padron_parquet a través de la tabla padron_txt_new mediante un CTAS, cuyos datos seran almacenados en formato parquet.**

// COMMAND ----------

spark.sql("""CREATE TABLE padron_parquet STORED AS PARQUET  AS SELECT * FROM padron_txt_new;""")

// COMMAND ----------

// MAGIC %md
// MAGIC ##### **5) Crear tabla padron_parquet_2 a través de la tabla padron_txt_2, con los datos almacendos en formato parquet.**

// COMMAND ----------

spark.sql("""CREATE TABLE padron_parquet_2 STORED AS PARQUET  AS SELECT * FROM padron_txt_2;""")

// COMMAND ----------

spark.sql("SELECT * FROM padron_parquet_2").show(4,false)

// COMMAND ----------

// MAGIC %md
// MAGIC ##### **6) Crear tabla padron_particionado particionada por campos DESC_DISTRITO y DESC_BARRIO cuyos datos estén en formato parquet.**

// COMMAND ----------

spark.sql(""" DROP TABLE padron_particionado""")

// COMMAND ----------

spark.sql("""CREATE TABLE padron_particionado 
  (COD_DISTRITO STRING, 
  COD_DIST_BARRIO STRING,
  cod_barrio STRING,
  COD_DIST_SECCION STRING, 
  COD_SECCION STRING, 
  COD_EDAD_INT STRING, 
  EspanolesHombres INT, 
  EspanolesMujeres INT, 
  ExtranjerosHombres INT, 
  ExtranjerosMujeres INT) 
PARTITIONED BY (desc_distrito string,desc_barrio string)
STORED AS PARQUET""")

// COMMAND ----------

// MAGIC %md
// MAGIC ##### **7) Insertar datos (en cada partición) dinámicamente en la tabla recién creada a partir de un select de padron_parquet_2.**

// COMMAND ----------

spark.sql("""SET hive.exec.dynamic.partition.mode=non-strict""")

spark.sql("""INSERT OVERWRITE TABLE padron_particionado PARTITION (desc_distrito, desc_barrio) SELECT 
    cod_distrito AS STRING, 
    cod_dist_barrio AS STRING, 
    cod_barrio AS STRING, 
    cod_dist_seccion AS STRING, 
    cod_seccion AS STRING, 
    cod_edad_int AS STRING, 
    espanoleshombres AS INT, 
    espanolesmujeres AS INT, 
    extranjeroshombres AS INT, 
    extranjerosmujeres AS INT,
    Desc_Barrio AS STRING,
    Desc_distrito AS STRING
 FROM padron_parquet_2""")

// COMMAND ----------

// MAGIC %md
// MAGIC ##### **8) Calcular el total de EspanolesHombres, EspanolesMujeres, ExtranjerosHombres y ExtranjerosMujeres agrupado por DESC_DISTRITO y DESC_BARRIO para los distritos CENTRO, LATINA, CHAMARTIN, TETUAN, VICALVARO y BARAJAS.**
