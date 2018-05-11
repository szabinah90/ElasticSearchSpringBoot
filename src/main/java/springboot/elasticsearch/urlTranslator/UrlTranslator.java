package springboot.elasticsearch.urlTranslator;

public class UrlTranslator {
    public String replaceAccents(String in) {
        return in
                .replace("%20", " ")
                .replace(".+", ".")
                .replace("%3A", ":")
                .replace("&#xE1", "á")
                .replace("&#xE9", "é")
                .replace("&#xED", "í")
                .replace("&#xF3", "ó")
                .replace("&#xF6", "ö")
                .replace("&#xF6", "ő")
                .replace("&#xFA", "ú")
                .replace("&#xFC", "ü")
                .replace("&#xFC", "ű")

                .replace("&#xC1", "Á")
                .replace("&#xC9", "É")
                .replace("&#xCD", "Í")
                .replace("&#xD3", "Ó")
                .replace("&#xD6", "Ö")
                .replace("&#xD6", "Ő")
                .replace("&#xDA", "Ú")
                .replace("&#xDC", "Ü")
                .replace("&#xDC", "Ű");
    }
}
