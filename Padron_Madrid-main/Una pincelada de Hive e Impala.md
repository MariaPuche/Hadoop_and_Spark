# **Una pincelada de Hive e Impala**

### **1) ¿Qué es hive?**
*Es una tecnología distribuida diseñada y construida sobre Hadoop. Es una herramienta de Hadoop que se usa principalmente para consultas y análisis de grandes datos almacenados con un lenguaje llamao HiveQL, muy similar a SQL,  que internamente transforma las consultas SQL en trabajos MapReduce que ejecutan en Hadoop.*

*Esta diseñado para conjuntos de datos de gran volumen y no está diseñado para procesamiento de datos al vuelo ni ofrece consultas a tiempo real. Se utiliza, principalmente, con trabajos pesados en ETL y buscamos más la robustez que la velocidad.*

*Los datos gestionados por Hive son datos estructurados almacenados en HDFS. Así, optimiza de forma automática el plan de ejecución y usa particionado de tablas en determinadas consultas. También soporta diferentes formatos de ficheros, codificaciones y fuentes de datos como HBase.*

### **2) ¿Que és Impala?**
*Es una herramienta escalable de consultas de procesamiento MPP (procesamiento masivamente paralelo), por lo que no usa el Map/reduce de Hadoop sino que utiliza procesos que se ejecutan en los nodos y que consultan directamente sobre HDFS o HBase. Tiene licencia oper source y se encuentra incluida en las distribuciones de cloudera. Soporta múltiples formatos como Parquet, Json o Avro y tecnologías de almacenamiento como HDFS, Hive, Amazon S3.*

*Impala usa los mismos metadatos, la misma sintaxis SQL y el mismo driver que Hive. Además, también se puede usar desde la interfaz de Hue, por lo que se integra perfectamente con el ecosistema de Hadoop*

*Se utiliza principalmente para realizar consultas exploratorias ligeras interactivas de baja latencia puesto que se pueden conseguir respuestas en tiempos menores de un segundo.*

### **3) Apache Impala vs Hive**
*Ambas tecnologías son adecuadas en entornos empresariales de Data Warehouse en los que nos podemos encontrar consultas repetitivas pero muy pesadas en su primera ejecución, con transformaciones complejas y joins sobre grandes cantidades de datos.*

*Hive es una tecnología cuyos casos de uso están más orientados a realizar consultas y dashboards de BI gracias a sus sistemas de caché. Es mejor para trabajos pesados de tipo ETL (Extract, Transform and Load) donde no nos interesa tanto la velocidad como la robustez de la ejecución, ya que la alta tolerancia a fallos que presenta evita la necesidad de relanzamientos al fallar algún nodo.Además, es tolerante a faloos, su adquirectura si se basa en el Map/Reduce y realiza consultas más lentas pero más robustas.*

*Por otro lado, Impala es una buena solución en entornos de analítica interactiva en los que se requiere el uso de funciones y tienen unos requisitos temporales más estrictos, inferiores a segundos. Se aplicaría mejor en consultas más ligeras donde prime la velocidad sobre la fiabilidad, cualquier fallo en algún nodo o proceso obligaría a relanzar la consulta. Ademñas, tiene un mayor consumo de memoria y de CPU.*

*En modo de resumen, a diferencia de la tolerancia a fallos que tiene Hive, Impala no la posee. Si un nodo falla durante la ejecución, toda la ejecución falla. Esto se evita con el Map - reduce que posee Hive*



### **4) ¿En qué consiste el formate columnas parquet? ¿Cuáles son sus ventajas?**
*Parquet es el formato de almacenamiento en columnas principal del ecosistema Hadoop que admite estructuras anidadas. Se considera muy adecuado para escenarios OLAP, almacenamiento de columnas y escaneo.*

*Destacan dos ventajas principalmente de este formato de almacenamiento:*

- **Relación de compresión alta** -> Facilita el uso de una codificación y compresión eficientes para cada columna, lo que reduce el espacio en disco.

- **Operaciones IO más pequeñas** -> Utilice la inserción de mapas y la inserción de predicados para leer solo las columnas requeridas y omitir las columnas que no cumplan con las condiciones, lo que puede reducir el escaneo de datos innecesario, traer mejoras de rendimiento y volverse más obvias cuando hay más campos de tabla.

### **5) ¿ En qué consiste INVALIDATE METADATA?**
*La instrucción INVALIDATE METADATA marca los metadatos de una o todas las tablas como obsoletas. La próxima vez que Impala realice una consulta en una tabla cuyos metadatos estén invalidados, recargará los metadatos asociados antes de continuar con la consulta. Como esta es una operación muy costosa, cuando sea posible, prefiera REFRESH en lugar de INVALIDATE METADATA.*

*Se requiere INVALIDAR METADATOS cuando se realizan los siguientes cambios fuera de Impala, en Hive y otro cliente de Hive, como SparkSQL:*

- *Los metadatos de las tablas existentes cambian.*
- *Se agregan nuevas tablas e Impala usará las tablas.*
- *Se modifican los privilegios de Sentry a nivel de SERVIDOR o BASE DE DATOS.*
- *Bloquee los cambios de metadatos, pero los archivos siguen siendo los mismos (reequilibrio de HDFS). Los frascos UDF cambian.*
- *Algunas tablas ya no se consultan y desea eliminar sus metadatos del catálogo y las memorias caché del coordinador para reducir los requisitos de memoria.*

*MUY IMPORTANTE: NO ES NECESARIO INVALIDAR DATOS CUANDO ES IMPALA LA QUE REALIZA LOS DATOS.*

### **6) ¿En qué consiste REFRESH?**
*REFRESH vuelve a cargar los metadatos de la tabla desde la base de datos de metastore y realiza una recarga incremental del archivo y bloquea los metadatos desde HDFS NameNode. Se usa para evitar inconsistencias entre Impala y las fuentes de metadatos externas, a saber, Hive Metastore (HMS) y NameNodes.*

*El nombre de la tabla es un parámetro obligatorio y la tabla ya debe existir y ser conocida por Impala. Solo se recargan los metadatos de la tabla especificada.*

*Utilice la declaración REFRESH para cargar los últimos metadatos del metastore para una tabla en particular después de que ocurra uno de los siguientes escenarios fuera de Impala:*

- *Eliminar, agregar o modificar archivos -> Por ejemplo, después de cargar nuevos archivos de datos en el directorio de datos HDFS para la tabla, agregar a un archivo HDFS existente, insertar datos de Hive a través de INSERTAR o CARGAR DATOS.*
- *Eliminar, agregar o modificar particiones* -> Por ejemplo, después de emitir ALTER TABLE u otra instrucción SQL de modificación de tabla en Hive*

### **7) Diferencia entre REFRESH y INVALIDATE METADATA?**
*INVALIDATE METADATA espera recargar los metadatos cuando se necesitan para una consulta posterior, pero recarga todos los metadatos de la tabla, lo que puede ser una operación costosa, especialmente para tablas grandes con muchas particiones. REFRESH vuelve a cargar los metadatos inmediatamente, pero solo carga los datos de ubicación del bloque para los archivos de datos recién agregados, lo que hace que sea una operación menos costosa en general. Si los datos se modificaron de una manera más ampliA, use INVALIDAR METADATOS para evitar una penalización en el rendimiento debido a la reducción de las lecturas locales.*

*El nombre de la tabla es un parámetro obligatorio para REFRESH. Para vaciar los metadatos de todas las tablas, utilice INVALIDATE METADATA. Debido a que REFRESH solo funciona para las tablas que Impala ya conoce, cuando crea una nueva tabla en el shell de Hive, ingrese INVALIDATE METADATA. Una vez que Impala conoce la tabla, puede ejecutar REFRESH table_name después de agregar archivos de datos para esa tabla.*
