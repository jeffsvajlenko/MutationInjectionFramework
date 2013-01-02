package sun.tools.jar.resources;

import java.util.ListResourceBundle;

public final class jar_fr extends ListResourceBundle
{
    protected final Object[][] getContents()
    {
        return new Object[][]
        {
            { "error.bad.cflag", "L'indicateur c requiert la sp\u00E9cification d'un fichier manifeste ou d'un fichier d'entr\u00E9e." },
            { "error.bad.eflag", "L'indicateur e et le fichier manifeste portant l'attribut Main-Class ne peuvent pas \u00EAtre sp\u00E9cifi\u00E9s \nensemble !" },
            { "error.bad.option", "Une des options -{ctxu} doit \u00EAtre sp\u00E9cifi\u00E9e." },
            { "error.bad.uflag", "L'indicateur u requiert la sp\u00E9cification d'un fichier manifeste, d'un fichier d'entr\u00E9e ou d'un indicateur e." },
            { "error.cant.open", "impossible d''ouvrir : {0} " },
            { "error.create.dir", "{0} : impossible de cr\u00E9er le r\u00E9pertoire" },
            { "error.illegal.option", "Option incorrecte : {0}" },
            { "error.incorrect.length", "Longueur incorrecte lors du traitement de : {0}" },
            { "error.nosuch.fileordir", "{0} : fichier ou r\u00E9pertoire introuvable" },
            { "error.write.file", "Erreur lors de l'\u00E9criture d'un fichier jar existant" },
            { "out.added.manifest", "manifest ajout\u00E9" },
            { "out.adding", "ajout : {0}" },
            { "out.create", "       cr\u00E9\u00E9e: {0}" },
            { "out.deflated", " ({0}% compress\u00E9s)" },
            { "out.extracted", "    extraite: {0}" },
            { "out.ignore.entry", "entr\u00E9e {0} ignor\u00E9e" },
            { "out.inflated", "d\u00E9compress\u00E9e: {0}" },
            { "out.size", " (entr\u00E9e = {0}) (sortie = {1})" },
            { "out.stored", " (0% stock\u00E9)" },
            { "out.update.manifest", "manifest mis \u00E0 jour" },
            { "usage", "Utilisation : jar {ctxui}[vfm0Me] [fichier-jar] [fichier-manifeste] [point-entr\u00E9e] [-C r\u00E9p] fichiers...\nOptions :\n    -c  cr\u00E9e une nouvelle archive\n    -t  affiche la table des mati\u00E8res de l'archive\n    -x  extrait les fichiers nomm\u00E9s (ou tous les fichiers) de l'archive\n    -u  met \u00E0 jour l'archive existante\n    -v  g\u00E9n\u00E8re une version d\u00E9taill\u00E9e d'une sortie standard\n    -f  sp\u00E9cifie le nom du fichier archive\n    -m  inclut les informations de manifeste \u00E0 partir du fichier de manifeste sp\u00E9cifi\u00E9\n    -e  sp\u00E9cifie le point d'entr\u00E9e d'une application en mode autonome \n        int\u00E9gr\u00E9e \u00E0 un fichier JAR ex\u00E9cutable\n    -0  stockage uniquement, pas de compression ZIP\n    -M  ne cr\u00E9e pas de fichier manifeste pour les entr\u00E9es\n    -i  g\u00E9n\u00E8re les informations d'index des fichiers jar sp\u00E9cifi\u00E9s\n    -C  passe au r\u00E9pertoire sp\u00E9cifi\u00E9 et inclut le fichier suivant\nSi l'un des fichiers est un r\u00E9pertoire, celui-ci est trait\u00E9 r\u00E9cursivement.\nLes noms du fichier manifeste, du fichier archive et du point d'entr\u00E9e sont\nsp\u00E9cifi\u00E9s dans le m\u00EAme ordre que celui des indicateurs m, f et e.\n\nExemple 1 : pour archiver deux fichiers de classe dans une archive intitul\u00E9e classes.jar : \n       jar cvf classes.jar Foo.class Bar.class \nExemple 2 : pour utiliser un fichier manifeste existant 'monmanifeste', puis archiver tous les\n           fichiers du r\u00E9pertoire foo/ dans 'classes.jar' : \n       jar cvfm classes.jar monmanifeste -C foo/ .\n" },
        };
    }
}
