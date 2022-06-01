# **Puesta en marcha de la máquina virtual**

## **Habilitar carpeta compartida**
Una vez que se ha montado la máquina virtual (VM) de cloudera en el pc, hay que habilitar la transferencia de ficheros entre la VM y nuestros sistema de archivos local. Para ello, se va a crear una carpeta compartida. El procedimiento a seguir es el siguiente:

1) Crear la carpeta que queremos compartir en el sistema local del pc.

2) Habilitar la opción Shared folders en los settings de la VMWare asociada a nuestra máquina virtual.

3) Dispositivos -> Insertar imagen de CD de las <<Guest Additions>>

4) Maquina -> Configuración -> Carpetas compartidas -> Seleccionar ruta y nombre de la carpeta a compartir y seleccionar automontar.

5) Escribir en consola:

        sudo usermod -a -G vboxsf cloudera

6) Reiniciar la consola (Paso muy importante).

Una vez que se vuelva abrir la VM ya estará habilitada la carpeta compartida en la ruta indicada en el paso 4.

## **Cambiar el idioma de la VM.**

Seguir el siguiente recorrido dentro de la VM.

    System -> Preferences -> Keyboard -> Layouts (Spanish)