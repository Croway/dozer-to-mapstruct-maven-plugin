package it.croway;


import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Goal which converts Dozer XML mappings to MapStruct.
 */
@Mojo( name = "convert", defaultPhase = LifecyclePhase.PROCESS_SOURCES )
public class DozerMapstructConverterMojo extends AbstractMojo {
    /**
     * Location of the file.
     */
    @Parameter(defaultValue = "${project.build.directory}/generated-sources/mapstruct", property = "outputDirectory", required = true)
    private File outputDirectory;

    @Parameter(name = "dozerMappingDirectories", property = "dozer.mapping.directories", required = true)
    private List<File> dozerMappingDirectories;

    public void execute() {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        for (File inputDirectory : dozerMappingDirectories) {
            File[] files = inputDirectory.listFiles((dir, name) -> name.endsWith(".xml"));

            for (File mapper : files) {
                try (InputStream is = new FileInputStream(mapper)) {

                    DocumentBuilder db = dbf.newDocumentBuilder();

                    Document doc = db.parse(is);

                    String javaFileName = mapper.getName().substring(0, 1).toUpperCase();
                    javaFileName += mapper.getName().substring(1, mapper.getName().indexOf(".xml"));
                    javaFileName = javaFileName.replace("-", "").replace("_", "");

                    outputDirectory.mkdirs();

                    try (FileOutputStream output =
                                 new FileOutputStream(outputDirectory + File.separator + javaFileName + ".java")) {
                        transform(doc, output);
                    }

                } catch (IOException | ParserConfigurationException |
                         SAXException | TransformerException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void transform(Document doc, OutputStream output)
            throws TransformerException {

        TransformerFactory transformerFactory = TransformerFactory.newInstance();

        // add XSLT in Transformer
        Transformer transformer = transformerFactory.newTransformer(
                new StreamSource(DozerMapstructConverterMojo.class.getResourceAsStream("/dozer_mapstruct.xslt")));

        transformer.transform(new DOMSource(doc), new StreamResult(output));

    }
}
