package it.croway;

import org.apache.maven.execution.DefaultMavenExecutionRequest;
import org.apache.maven.execution.MavenExecutionRequest;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.maven.plugin.testing.WithoutMojo;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuilder;
import org.apache.maven.project.ProjectBuildingRequest;

import org.junit.jupiter.api.Test;

import org.eclipse.aether.DefaultRepositorySystemSession;

import java.io.File;

public class DozerMapstructConverterMojoTest extends AbstractMojoTestCase {

    /**
     * @throws Exception if any
     */
    @Test
    public void testSomething()
            throws Exception {
        super.setUp();

        File pom = new File( "target/test-classes/project-to-test/");
        assertNotNull(pom);
        assertTrue(pom.exists());

        MavenProject mavenProject = readMavenProject(pom);

        DozerMapstructConverterMojo dozerMapstructConverterMojo = (DozerMapstructConverterMojo) lookupConfiguredMojo(mavenProject, "convert");
        assertNotNull(dozerMapstructConverterMojo);
        dozerMapstructConverterMojo.execute();

        File outputDirectory = ( File ) getVariableValueFromObject(dozerMapstructConverterMojo, "outputDirectory");
        assertNotNull(outputDirectory);
        assertTrue(outputDirectory.exists());

        File generated = new File("target/test-classes/project-to-test/target/generated-sources/mapstruct/Dozermapping.java");
        assertNotNull(generated);
        assertTrue(generated.exists());
        generated = new File("target/test-classes/project-to-test/target/generated-sources/mapstruct/Dozermapping2.java");
        assertNotNull(generated);
        assertTrue(generated.exists());
    }

    /** Do not need the MojoRule. */
    @WithoutMojo
    @Test
    public void testSomethingWhichDoesNotNeedTheMojoAndProbablyShouldBeExtractedIntoANewClassOfItsOwn()
    {
        assertTrue( true );
    }

    public MavenProject readMavenProject( File basedir )
            throws Exception
    {
        File pom = new File( basedir, "pom.xml" );
        MavenExecutionRequest request = new DefaultMavenExecutionRequest();
        request.setBaseDirectory( basedir );
        ProjectBuildingRequest configuration = request.getProjectBuildingRequest();
        configuration.setRepositorySession( new DefaultRepositorySystemSession() );
        MavenProject project = lookup( ProjectBuilder.class ).build( pom, configuration ).getProject();

        return project;
    }
}

