## **1) Calcular el total de EspanolesHombres, espanolesMujeres, ExtranjerosHombres y ExtranjerosMujeres agrupado por DESC_DISTRITO y DESC_BARRIO en la tabla padron_txt.**
    SELECT desc_distrito, desc_barrio, 
        sum(espanoleshombres), 
        sum(espanolesmujeres), 
        sum(extranjeroshombres), 
        sum(extranjerosmujeres)
    FROM padron_txt
    GROUP BY desc_distrito, desc_barrio;

## *2) Calcular el total de EspanolesHombres, EspanolesMujeres, ExtranjerosHombres y ExtranjerosMujeres agrupado por DESC_DISTRITO y DESC_BARRIO para los distritos CENTRO, LATINA, CHAMARTIN, TETUAN, VICALVARO y BARAJAS.*

    SELECT desc_distrito, 
            desc_barrio, 
            count(espanoleshombres) as 'n_esp_hombres', 
            count(espanolesmujeres) as 'n_esp_mujeres', 
            count(extranjeroshombres) as 'n_ex_hombres', 
            count(extranjerosmujeres) as 'n_ex_mujeres'
    FROM padron_txt_2
    WHERE desc_distrito IN ('CENTRO','LATINA','CHAMARTIN','TETUAN','VICALVARO','BARAJAS')
    GROUP BY desc_distrito,desc_barrio

## **3) Llevar a cabo la consulta en padron_parquet y padron_partitionado**

    SELECT desc_distrito, 
            desc_barrio, 
            count(espanoleshombres) as n_esp_hombres, 
            count(espanolesmujeres) as n_esp_mujeres, 
            count(extranjeroshombres) as n_ex_hombres, 
            count(extranjerosmujeres) as n_ex_mujeres 
    FROM padron_parquet
    WHERE desc_distrito IN ('CENTRO','LATINA','CHAMARTIN','TETUAN','VICALVARO','BARAJAS')
    GROUP BY desc_distrito,desc_barrio

SELECT desc_distrito, 
            desc_barrio, 
            count(espanoleshombres) as n_esp_hombres, 
            count(espanolesmujeres) as n_esp_mujeres, 
            count(extranjeroshombres) as n_ex_hombres, 
            count(extranjerosmujeres) as n_ex_mujeres 
    FROM padron_particionado
    WHERE desc_distrito IN ('CENTRO','LATINA','CHAMARTIN','TETUAN','VICALVARO','BARAJAS')
    GROUP BY desc_distrito,desc_barrio

## **4) Hacer consultas de agregación (Max, Min, Avg, Count) tal cual el ejemplo anterior con las 3 tablas (padron_txt_2, padron_parquet_2 y padron_particionado)**

    SELECT desc_distrito, 
            desc_barrio, 
            avg(espanoleshombres) as media_esp_hombres, 
            max(espanolesmujeres) as max_esp_mujeres, 
            min(extranjeroshombres) as min_ex_hombres, 
            count(extranjerosmujeres) as n_ex_mujeres 
    FROM padron_particionado 
    WHERE desc_distrito IN ('CENTRO','LATINA','CHAMARTIN','TETUAN','VICALVARO','BARAJAS') 
    GROUO BY desc_distrito,desc_barrio

    SELECT desc_distrito, 
            desc_barrio, 
            avg(espanoleshombres) as media_esp_hombres, 
            max(espanolesmujeres) as max_esp_mujeres, 
            min(extranjeroshombres) as min_ex_hombres, 
            count(extranjerosmujeres) as n_ex_mujeres 
    FROM padron_parquet_2
    WHERE desc_distrito IN ('CENTRO','LATINA','CHAMARTIN','TETUAN','VICALVARO','BARAJAS') 
    GROUO BY desc_distrito,desc_barrio

    SELECT desc_distrito, 
            desc_barrio, 
            avg(espanoleshombres) as media_esp_hombres, 
            max(espanolesmujeres) as max_esp_mujeres, 
            min(extranjeroshombres) as min_ex_hombres, 
            count(extranjerosmujeres) as n_ex_mujeres 
    FROM padron_txt_2 
    WHERE desc_distrito IN ('CENTRO','LATINA','CHAMARTIN','TETUAN','VICALVARO','BARAJAS') 
    GROUO BY desc_distrito,desc_barrio