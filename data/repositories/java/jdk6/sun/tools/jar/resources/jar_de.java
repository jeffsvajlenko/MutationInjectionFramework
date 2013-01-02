package sun.tools.jar.resources;

import java.util.ListResourceBundle;

public final class jar_de extends ListResourceBundle
{
    protected final Object[][] getContents()
    {
        return new Object[][]
        {
            { "error.bad.cflag", "Flag 'c' erfordert Angabe von Manifest oder Eingabedateien!" },
            { "error.bad.eflag", "Flag 'e' und Manifest mit dem Attribut 'Main-Class' k\u00F6nnen nicht zusammen angegeben\nwerden." },
            { "error.bad.option", "Eine der Optionen -{ctxu} muss angegeben werden." },
            { "error.bad.uflag", "Flag 'u' erfordert Angabe von Manifest, Flag 'e' oder Eingabedateien!" },
            { "error.cant.open", "\u00D6ffnen nicht m\u00F6glich: {0} " },
            { "error.create.dir", "{0} : Verzeichnis konnte nicht erstellt werden." },
            { "error.illegal.option", "Unzul\u00E4ssige Option: {0}" },
            { "error.incorrect.length", "Falsche L\u00E4nge bei der Verarbeitung von: {0}" },
            { "error.nosuch.fileordir", "{0} : Datei oder Verzeichnis existiert nicht." },
            { "error.write.file", "Fehler beim Schreiben in vorhandener JAR-Datei." },
            { "out.added.manifest", "Manifest wurde hinzugef\u00FCgt." },
            { "out.adding", "Hinzuf\u00FCgen von: {0}" },
            { "out.create", "     erstellt: {0}" },
            { "out.deflated", " (komprimiert {0} %)" },
            { "out.extracted", "   extrahiert: {0}" },
            { "out.ignore.entry", "Eintrag {0} wird ignoriert." },
            { "out.inflated", "dekomprimiert: {0}" },
            { "out.size", " (ein = {0}) (aus = {1})" },
            { "out.stored", " (gespeichert 0 %)" },
            { "out.update.manifest", "Manifest wurde aktualisiert." },
            { "usage", "Syntax: jar {ctxui}[vfm0Me] [jar-Datei] [Manifest-Datei] [Einstiegspunkt] [-C Verzeichnis] Dateien ...\nOptionen:\n    -c  Neues Archiv erstellen\n    -t  Inhaltsverzeichnis f\u00FCr Archiv auflisten\n    -x  Genannte (oder alle) Dateien aus Archiv extrahieren\n    -u  Vorhandenes Archiv aktualisieren\n    -v  Ausf\u00FChrliche Ausgabe f\u00FCr Standardausgabe erzeugen\n    -f  Namen der Archivdatei angeben\n    -m  Manifest-Information von angegebener Manifest-Datei einschlie\u00DFen\n    -e  Geben Sie einen Anwendungs-Einstiegspunkt f\u00FCr die \n        in einer ausf\u00FChrbaren jar-Datei geb\u00FCndelte eigenst\u00E4ndige Anwendung an.\n    -0  Nur speichern (keine ZIP-Komprimierung)\n    -M  Keine Manifest-Datei f\u00FCr die Eintr\u00E4ge erstellen\n    -i  Index-Information f\u00FCr angegebene jar-Dateien erstellen\n    -C  Zum angegebenen Verzeichnis wechseln und folgende Datei einschlie\u00DFen\nFalls eine Datei ein Verzeichnis ist, wird es rekursiv verarbeitet.\nDer Name der Manifest-Datei, der Name der Archivdatei und der Name des Einstiegspunkts werden \nin derselben Reihenfolge wie die Flags 'm', 'f' und 'e' angegeben.\n\nBeispiel 1: So archivieren Sie zwei Klassendateien in ein Archiv mit Namen classes.jar: \n       jar cvf classes.jar Foo.class Bar.class \nBeispiel 2: Verwenden der vorhandenen Manifest-Datei 'mymanifest' und archivieren\n           aller Dateien im Verzeichnis foo/ in 'classes.jar': \n       jar cvfm classes.jar mymanifest -C foo/ .\n" },
        };
    }
}
