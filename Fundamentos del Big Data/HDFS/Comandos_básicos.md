# Comandos básicos de HDFS

A continuación, se van a listar una serie de comandos básicos que se utilizan en HDFS, todo esto se escribe en el terminal de la máquina virtual de cloudera.

## **1) Describir los comandos posibles que contiene FsShell**

    hadoop fs ó hdfs

## **2) Mostrar el contenido del directorio root en HDFS.**

    hdfs dfs -ls /

## **3) Crear un directorio local de trabajo llamado ejercicio1.**

    mkdir home/cloudera/ejercicio1

El directorio se va a crear en la ruta home/cloudera. Para comprobar que se ha creado correctamente, se puede hacer visualmente yendo a la carpeta cloudera y ver que se encuentra la nueva carpeta creada ejercicio1 o con el siguiente comando:

    ls home/cloudera

## **4) Copiar el archivo "shakespeare.tar.gz" en el directorio creado anteriormente, ejercicio1.**

Primero, indicar que el archivo se encuentra en la carpeta compartida con la ruta /mnt/hgfs/.

    cp /mnt/hgfs/shakespeare.tar.gz /home/cloudera/ejercicio1

## **5) Descomprimir el archivo**
Primero entramos en el directorio y luego lo descomprimimos.

    cd /home/cloudera/ejercicio1

    tar zxvf shakespeare.tar.gz 

## **6) Copiar la carpeta que se acaba de descomprimir en HDFS (/user/cloudera/shakespeare)**

    hdfs dfs fs -put shakespeare /user/cloudera/shakespeare

Para comprobar que se ha copiado:

    hdfs dfs -ls /user/cloudera

## **7) Observer el contenido de la carpeta shakespeare en hdfs**

    hdfs dfs -ls /user/cloudera/shakespeare

## **8) Borrar la subcarpeta "glossary" de la carpeta Shakespeare en hdfs**

    hdfs dfs -rm /user/cloudera/shakespeare/glossary

Para comprobar que se ha borrado.

    hdfs dfs -ls /user/cloudera/shakespeare

## **9) Listar las 50 últimas líneas de la subcarpeta "histories".**

    hdfs dfs -cat /user/cloudera/shakespeare/histories | tail -50

## **10) Copia al sistema de ficheros local de tu VM el fichero "poems" en la ruta /home/cloudera/ejercicio1/shakespeare/shakepoems.txt.**

     hadoop fs -get /user/cloudera/shakespeare/poems /home/cloudera/ejercicio1/shakespeare/shakepoems.txt 

     ls /home/cloudera/ejercicio1/shakespeare 

## **11) Muestr las últimas líneas de shakespoems.txt copiado en tu local por pantalla.**

    tail -10 /home/cloudera/ejercicio1/shakespeare/shakepoems.txt