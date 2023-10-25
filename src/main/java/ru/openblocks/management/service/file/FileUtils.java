package ru.openblocks.management.service.file;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.mime.MediaType;

import java.io.InputStream;

@UtilityClass
public class FileUtils {

    private static final TikaConfig tika;

    static {
        try {
            // Initializing of TikaConfig is quite expensive operation,
            // so it is a reasonable idea to init it at once.
            tika = new TikaConfig();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Returns mime-type of file by its content. Content is represented by input stream.
     * Detection is based on library Apache Tika.
     *
     * @param stream   input stream of file
     * @param fileName potentially known file name
     * @return mime-type
     */
    public static String getMimeType(InputStream stream,
                                     String fileName) {
        try {
            Metadata metadata = new Metadata();

            if (StringUtils.isNotBlank(fileName)) {
                metadata.set(TikaCoreProperties.RESOURCE_NAME_KEY, fileName);
            }

            MediaType mediaType = tika.getDetector().detect(TikaInputStream.get(stream), metadata);
            return mediaType.toString();

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
