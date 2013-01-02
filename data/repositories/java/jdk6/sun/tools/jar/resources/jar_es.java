package sun.tools.jar.resources;

import java.util.ListResourceBundle;

public final class jar_es extends ListResourceBundle
{
    protected final Object[][] getContents()
    {
        return new Object[][]
        {
            { "error.bad.cflag", "La marca 'c' necesita la especificaci\u00F3n de archivos de manifiesto o de entrada." },
            { "error.bad.eflag", "la marca 'e' y el manifiesto con el atributo 'Main-Class' no pueden especificarse \na la vez." },
            { "error.bad.option", "Se debe especificar una de las opciones -{ctxu}." },
            { "error.bad.uflag", "La marca 'u' necesita la especificaci\u00F3n de archivos de manifiesto, de entrada o marca 'e'." },
            { "error.cant.open", "no es posible abrir: {0} " },
            { "error.create.dir", "{0} : no fue posible crear el directorio" },
            { "error.illegal.option", "Opci\u00F3n no permitida: {0}" },
            { "error.incorrect.length", "longitud incorrecta al procesar: {0}" },
            { "error.nosuch.fileordir", "{0} : no existe tal archivo o directorio." },
            { "error.write.file", "Error al escribir un archivo jar existente" },
            { "out.added.manifest", "manifest agregado" },
            { "out.adding", "agregando: {0}" },
            { "out.create", "  creado: {0}" },
            { "out.deflated", " (desinflado {0}%)" },
            { "out.extracted", "extra\u00EDdo: {0}" },
            { "out.ignore.entry", "ignorando entrada {0}" },
            { "out.inflated", " inflado: {0}" },
            { "out.size", " (entrada = {0}) (salida = {1})" },
            { "out.stored", " (almacenado 0%)" },
            { "out.update.manifest", "manifest actualizado" },
            { "usage", "Uso: jar {ctxui}[vfm0Me] [archivo-jar] [archivo-manifiesto] [punto-entrada] [-C dir] archivos...\nOpciones:\n    -c  crear archivo de almacenamiento\n    -t  crear la tabla de contenido del archivo de almacenamiento\n    -x  extraer el archivo mencionado (o todos) del archivo de almacenamiento\n    -u  actualizar archivo de almacenamiento existente\n    -v  generar salida detallada de los datos de salida est\u00E1ndar\n    -f  especificar nombre del archivo de almacenamiento\n    -m  incluir informaci\u00F3n de un archivo de manifiesto especificado\n    -e  especificar punto de entrada de la aplicaci\u00F3n para aplicaci\u00F3n aut\u00F3noma \n        que se incluye dentro de un archivo jar ejecutable\n    -0  s\u00F3lo almacenar; no utilizar compresi\u00F3n ZIP\n    -M  no crear un archivo de manifiesto para las entradas\n    -i  generar informaci\u00F3n de \u00EDndice para los archivos jar especificados\n    -C  cambiar al directorio especificado e incluir el archivo siguiente\nSi alg\u00FAn archivo coincide tambi\u00E9n con un directorio, ambos se procesar\u00E1n.\nEl nombre del archivo de manifiesto, el nombre del archivo de almacenamiento y el nombre del punto de entrada se\nespecifican en el mismo orden que las marcas 'm', 'f' y 'e'.\n\nEjemplo 1: para archivar dos archivos de clases en un archivo de almacenamiento llamado classes.jar: \n       jar cvf classes.jar Foo.class Bar.class \nEjemplo 2: utilice un archivo de manifiesto ya creado, 'mymanifest', y archive todos los\n           archivos del directorio foo/ en 'classes.jar': \n       jar cvfm classes.jar mymanifest -C foo/ .\n" },
        };
    }
}
