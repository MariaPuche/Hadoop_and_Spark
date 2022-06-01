## **Particiones**
### **1) Crear tabla (Hive) padron_particionado particionada por campos DESC_DISTRITO y DESC_BARRIO cuyos datos estén en formato parquet.**

    CREATE TABLE padron_particionado(
        cod_distrito STRING,
        cod_dist_barrio STRING,
        cod_barrio STRING,
        cod_dist_seccion STRING,
        cod_seccion STRING, 
        cod_edad_int STRING,
        EspanolesHombres STRING, 
        EspanolesMujeres STRING, 
        ExtranjerosHombres STRING,
        ExtranjerosMujeres STRING )
    PARTITIONED BY (desc_distrito string,desc_barrio string)
    STORED AS PARQUET;

### **2) Insertar datos (en cada partición) dinámicamente (con Hive) en la tabla recién creada a partir de un select de la tabla padron_parquet_2**

    set hive.exec.dynamic.partition.mode=nonstrict   
    
    INSERT OVERWRITE TABLE padron_particionado 
        partition (desc_distrito, desc_barrio) 
        SELECT 
            cod_distrito, 
            cod_dist_barriO, 
            cod_dist_seccion, 
            cod_seccion, 
            cod_edad_int, 
            cod_barrio,
            espanoleshombres, 
            espanolesmujeres, 
            extranjeroshombres, 
            extranjerosmujeres,
            desc_barrio,
            desc_distrito
    FROM padron_parquet_2;




