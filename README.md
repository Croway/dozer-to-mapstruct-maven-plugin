# Dozer to MapStruct Maven Plugin

Given a plugin configuration like

```
<plugin>
    <groupId>it.croway</groupId>
    <artifactId>dozer-to-mapstruct</artifactId>
    <configuration>
        <outputDirectory>${project.build.directory}/generated-sources/mapstruct</outputDirectory>
        <dozerMappingDirectories>src/main/resources</dozerMappingDirectories>
    </configuration>
    <executions>
        <execution>
            <id>convert</id>
            <phase>validate</phase>
            <goals>
                <goal>convert</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

this plugin will convert dozer xml files located in `dozerMappingDirectories` to MapStruct Java Interfaces in `outputDirectory`

For example the following Dozer XML mapping:

```
<?xml version="1.0" encoding="UTF-8"?>
<mappings xmlns="http://dozer.sourceforge.net"
		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		xsi:schemaLocation="http://dozer.sourceforge.net
      http://dozer.sourceforge.net/schema/beanmapping.xsd">
	<mapping>
		<class-a>com.example.dozer.models.SourceObject</class-a>
		<class-b>com.example.dozer.models.DestinationObject</class-b>
		<field>
			<a>students</a>
			<b>pupils</b>
		</field>
	</mapping>
</mappings>
```

is converted to:

```
package mapstruct;		

@org.mapstruct.Mapper
interface SourceObjectDestinationObjectMapper {
	SourceObjectDestinationObjectMapper INSTANCE = org.mapstruct.factory.Mappers.getMapper(SourceObjectDestinationObjectMapper.class);

	@org.mapstruct.Mapping(source = "students"
		, target = "pupils"
		)
	com.example.dozer.models.DestinationObject SourceObjectToDestinationObject(com.example.dozer.models.SourceObject from);
}
```