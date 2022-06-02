# **WORDCOUNT**

En este ejercicio simplemente ejecutaremos un Job consistente en la ejecución del wordcount en MapReduce sobre el dataset shakespeare. Por simplicidad, los ficheros .class y el jar ya están creados.  

Wordcount cuenta el número de palabras distintas que hay en un texto dado.

## **Pasos previos**

1) Descargar la carpeta wordcount y meterlo en la carpeta compartida.

2) Copiar en la ruta “/home/cloudera/ejercicio1” la carpeta "wordcount" y su contenido.

        cp /mnt/hgfs/wordcount /home/cloudera/ejercicio1

3) Comprobar que se ha copiado correctamente

        ls /home/cloudera/ejercicio1

4) Examinar el contenido de la carpeta wordcount.

        ls /home/cloudera/ejercicio1/wordcount
    
5) Examinar el contenido de los tres ficheros java.

        cat /home/cloudera/ejercicio1/wordcount/WordMapper.java 
        cat /home/cloudera/ejercicio1/wordcount/WordCount.java
        cat /home/cloudera/ejercicio1/wordcount/SumReducer.java  

La carpeta wordcount, como hemos visto, ya contiene los javas compilados y  el jar creado, por lo que solo tenemos que ejecutar el submit del job hadoop usando nuestro fichero JAR para contar las ocurrencias de palabras contenidas en nuestra carpeta “shakespeare”. Nuestro jar contiene las clases java compiladas dentro de un paquete llamado “solutions”, por eso se le llama de este modo.hadoop.

## **Pasos para ejecutar el job del wordcount en MapReduce sobre el dataset Shakespeare.**
1) Situarnos en el directorio worcounts.

        cd /home/cloudera/ejercicio1/wordcount 

2)  Ejecutar el submit del job hadoop usando nuestro fichero JAR.

        hadoop jar wc.jar solution.WordCount shakespeare /user/cloudera/wordcounts 

3) Comprobar el resultado de nuestro MapReduce

        hdfs dfs -ls /user/cloudera/wordcounts 

Se obtienen dos documentos: uno con el nombre _SUCESS y otro part-r-00000

4) Observar el contenido del fichero part-r-00000.

        hdfs fs -cat /user/cloudera/wordcounts/part-r-00000 | less 

5)  Volvemos a ejecutar el job de nuevo 

        hadoop jar wc.jar solution.WordCount shakespeare/poems /user/cloudera/pwords   

6) Borramos la salida producida por nuestros jobs.

        hdfs dfs -rm -r /user/cloudera/wordcounts /user/cloudera/pwords 

7) Ejecutamos nuevamente nuestro job 

        hadoop jar wc.jar solution.WordCount shakespeare /user/cloudera/count2 

8) Mientras se ejecuta, en otro terminal ejecutamos lo siguiente, para ver la lista de Jobs que se están ejecutando.

        mapred job –list 

9) Si conocemos la id de un job, lo podemos matar. Recordemos que cerrando un terminal no se mata el job. Para ello, ejecutamos en otra terminal lo siguiente 

        mapred job -kill jobid
