# **Práctica Impala**

*Usando los datos del Padrón de Madrid, se van a realizar diferentes ejercicios utilizando Impala. Para poder llevarlos a cabo, se va a trabajar en cloudera, creando un contenedor de esta con docker. Para poder trabajar con la interfaz de hue, se configura con el puerto 8888* 

Antes de nada, se va a ejecutar el comando invalidade metadata  para la base de datos datos_padron, creada en Hive.

    INVALIDATE METADATA; // Base de datos entera
    INVALIDATE METADATA datos_padron.padron_original;

### **1) Calcular el total de EspanolesHombres, espanolesMujeres, ExtranjerosHombres y ExtranjerosMujeres agrupado por DESC_DISTRITO y DESC_BARRIO.**
    SELECT desc_distrito, desc_barrio, 
        count(espanoleshombres) as 'Espanoles_Hombre', 
        count(espanolesmujeres) as 'Espanoles_Mujer', 
        count(extranjeroshombres) as 'Extranjeros_Hombre', 
        count(extranjerosmujeres) as' Extranejeros_Mujer'
    FROM padron_txt
    GROUP BY desc_distrito, desc_barrio;
