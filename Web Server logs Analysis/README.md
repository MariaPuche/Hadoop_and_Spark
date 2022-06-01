# Web-Server-logs-Analysis

De forma general, un server log es un archivo de log generado por el servidor con una lista de las actividades que se ejecutan. En este ejemplo, hay un web server log el cuál mantiene un historial de las peticiones realizadas a la página. Este tipo tienen un formato standard. Se utilizan, generalmente, para analizarlos y sacar conclusiones de ellos, como localizar ataques, detectar errores comunes, etc.

En este caso, se analiza el dataset de los web server logs de la NASA (https://www.kaggle.com/datasets/adchatakora/nasa-http-access-logs), que están compuestos por ese tipo de registros:

133.43.96.45 - - [01/Aug/1995:00:00:23 -0400] "GET /images/launch-logo.gif HTTP/1.0" 200 1713

Por lo que tenemos los siguientes campos:
- *Host:* 133.43.96.45
- *User-identifier:* En este dataset, todos estos campos estarán con un “-“ que significa que faltan esos datos, por lo que obviaremos este campo.
- *Userid:* al igual que el anterior campo, también será obviado.
-  *Date* (01/Aug/1995:00:00:23 -0400): En formato dd/MMM/yyyy:HH:mm:ss y el campo final “-0400” sería el timezone que en este caso omitiremos, además haremos una transformación de los meses a forma numérica.
-  *Request Method* (GET): Los distintos métodos de petición.
-  *Resource* (/images/launch-logo.gif): El recurso al que se accede en esta petición.
-  *Protocol* (HTTP/1.0): El protocolo utilizado al ser logs de 1995, seguramente sea el único protocolo utilizado.
-  *HTTP status code* (200): Los distintos códigos de estado de HTTP.
-  *Size* (1713): El tamaño del objeto recibido por el cliente en bytes. En casos de error del cliente, este campo no se encuentra por lo que al igual que en los userid, será indicado con un “-“, tenerlo en cuenta.

El caso de estudio es analizar estos tipos de web server log con **Scala** y **Python**. Para el primero, se ha creado un proyecto con el nombre *Nasa* en **IntelliJ** y se han subido los archivos. Sin embargo, para python, se ha utilizado los notebooks de **Databricks**.

Primero, se debe cargar el archivo como un archivo de texto normal y realizar las transformaciones pertinentes para obtener un dataset limpio y estructurado, para ello se utilizarán expresiones regulares. 

Luego, se guardará el nuevo DataFrame ya estructurado en formato parquet. Por último, se realizarán una serie de consultas a nuestro dataset:
- ¿Cuáles son los distintos protocolos web utilizados? Agrúpalos.
- ¿Cuáles son los códigos de estado más comunes en la web? Agrúpalos y ordénalos 
para ver cuál es el más común.
- ¿Y los métodos de petición (verbos) más utilizados?
- ¿Qué recurso tuvo la mayor transferencia de bytes de la página web?
- Además, queremos saber que recurso de nuestra web es el que más tráfico recibe. Es 
decir, el recurso con más registros en nuestro log.
- ¿Qué días la web recibió más tráfico?
- ¿Cuáles son los hosts son los más frecuentes?
- ¿A qué horas se produce el mayor número de tráfico en la web?
- ¿Cuál es el número de errores 404 que ha habido cada día?
