import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.sql.functions.regexp_extract

object Depuración_y_consultas extends App {

  // Crear la sesión de Spark
  val spark = SparkSession
    .builder()
    .master("local[*]")
    .appName("Server Logs deNASA")
    .getOrCreate()

  // Lectura de todos los Logs
  val nasaText = spark.read.text("access.log")

  nasaText.show(5, false)

  // División de campos en diferentes columnas con expresiones regulares
  val nasa_df = nasaText.select(regexp_extract(col("value"), """(\S+)""", 1).alias("host"),
    regexp_extract(col("value"), """(\d{2}/\w{3}/\d{4}:\d{2}:\d{2}:\d{2})""", 1).alias("timestamp"),
    regexp_extract(col("value"), """"(\S+) (\S+)\s*(\S+)?\s*"""", 1).alias("method"),
    regexp_extract(col("value"), """"(\S+) (\S+)\s*(\S+)?\s*"""", 2).alias("endpoint"),
    regexp_extract(col("value"), """"(\S+) (\S+)\s*(\S+)?\s*"""", 3).alias("protocol"),
    regexp_extract(col("value"), """\s(\d{3})\s""", 1).cast("integer").alias("status"),
    regexp_extract(col("value"), """\s(\S+)$""", 1).cast("integer").alias("content_size"))

  // Imprimir el esquema de los datos
  nasa_df.printSchema()

  // Mostrar los datos
  nasa_df.show(5, false)

  // Limpieza
  val nasa_clean_df = nasa_df
    .withColumn("host", when(col("host") === "", "Sin especificar").otherwise(col("host")))
    .withColumn("timestamp", to_timestamp(col("timestamp"), "dd/MMM/yyyy:HH:mm:ss"))
    .withColumn("method", when(col("method") === "", "Sin especificar").otherwise(col("method")))
    .withColumn("endpoint", when(col("endpoint") === "", "Sin especificar").otherwise(col("endpoint")))
    .withColumn("protocol", when(col("protocol") === "", "Sin especificar").otherwise(col("protocol")))

  nasa_clean_df.printSchema()
  nasa_clean_df.show(5,false)

  // Guardar nasa_clean_df en formato parquet
  nasa_clean_df.write.format("parquet").mode("overwrite").save("C:/Users/maria.puche/Desktop/Nasa/nasa_parquet/")

  // Leer nasa_parquet
  val nasa_parquetDF = spark.read.format("parquet").load("C:/Users/maria.puche/Desktop/Nasa/nasa_parquet/")

  // ¿Cuáles son los distintos protocolos web utilizados? Agrúpalos.
  nasa_parquetDF
      .select(col("protocol"))
      .distinct()
      .show()

  // ¿Cuáles son los códigos de estado más comunes en la web?
  nasa_parquetDF
    .select("status")
    .groupBy("status")
    .agg(count("status").alias("total"))
    .orderBy(desc("total"))
    .show(4,false)

  // ¿Cuáles son los métodos de petición más utilizados?
  nasa_parquetDF
    .select("method")
    .groupBy("method")
    .agg(count("method").alias("total"))
    .orderBy(desc("total"))
    .show(4,false)

  // ¿Qué recurso tuvo la mayor transferencia de bytes de la página web?
  nasa_parquetDF
    .select(col("endpoint"),col("content_size"))
    .orderBy(desc("content_size"))
    .show(1, false)

  // ¿Cuál es el recurso con más registros en nuestro log?
  nasa_parquetDF
    .select(col("endpoint"))
    .groupBy(col("endpoint"))
    .agg(count(col("endpoint")).alias("total"))
    .orderBy(desc("total"))
    .show(1,false)

  // ¿Qué días la web recibió más tráfico?
  nasa_parquetDF
    .select(col("timestamp"))
    .groupBy(col("timestamp").cast("date").alias("Timestamp"))
    .agg(count("*").alias("Trafico"))
    .orderBy(desc("Trafico"))
    .show(1, false)

  // ¿Cuáles son los hosts más frecuentes?
  nasa_parquetDF
    .select(col("host"))
    .groupBy(col("host"))
    .agg(count("*").alias("Visitas"))
    .orderBy(desc("Visitas"))
    .show(5, false)

  // ¿A qué horas se produce el mayor número de tráfico en la web? A las 15:00
  nasa_parquetDF
    .select(col("timestamp"))
    .groupBy(hour(col("timestamp")).alias("Hours"))
    .agg(count("*").alias("Trafico"))
    .orderBy(desc("Trafico"))
   .show(24)

  // ¿Cuál es el número de errores 404 que ha habido cada día?
  nasa_parquetDF
    .select(col("timestamp"), col("status"))
    .where(col("status") === 404)
    .groupBy(dayofmonth(col("timestamp")).alias("days"))
    .agg(count("*").alias("Errors"))
    .orderBy(desc("Errors"))
    .show(31)
}
