# Plugin development

_Quick guide to help you start with plugin development for OBS Scene Queue_


### Project setup

1. Always start with the latest version of OBS Scene Queue, if possible the latest SNAPSHOT version. OBS Scene Queue is not in the Maven repository, so clone the OBS Scene Queue project and use `mvn clean install` to get it in your local Maven repository.
1. Create a new Maven Java/Kotlin project in your favourite IDE.

1. Add/edit the `pom.xml` file with the `obs-scene-que` dependency:
    ```xml
    <dependency>
        <groupId>nl.sajansen</groupId>
        <artifactId>obs-scene-que</artifactId>
        <version>${obs-scene-que.version}</version>
        <scope>provided</scope>
    </dependency>
    ```
1. Make sure you package your project JAR with dependencies:
    ```xml
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-assembly-plugin</artifactId>
        <version>3.3.0</version>
        <executions>
            <execution>
                <id>make-assembly</id>
                <phase>package</phase>
                <goals>
                    <goal>single</goal>
                </goals>
                <configuration>
                    <descriptorRefs>
                        <descriptorRef>jar-with-dependencies</descriptorRef>
                    </descriptorRefs>
                </configuration>
            </execution>
        </executions>
    </plugin>
    ```
1. If you are using Kotlin (version 1.3.70), you can mark the `kotlin-stdlib` dependency as `provided`: 
    ```xml
    <dependency>
        <groupId>org.jetbrains.kotlin</groupId>
        <artifactId>kotlin-stdlib</artifactId>
        <version>1.3.70</version>
        <scope>provided</scope>
    </dependency>
    ```

1. Create your folder structure with your package name. My plugin folder structures look something like this:
    ```
    src
    └───main
    │   └───kotlin
    │   │   └───nl
    │   │       └───sajansen
    │   │           └───exampleplugin
    │   └───resources
    │       └───nl
    │           └───sajansen
    │               └───exampleplugin
    └───test
        └───kotlin
        │   └───nl
        │       └───sajansen
        │           └───exampleplugin
        └───resources
            └───nl
                └───sajansen
                    └───exampleplugin
   ```

### Plugin setup

1. Create your main plugin (entry) class (in `src/main/kotlin/nl/sajansen/exampleplugin/`) named after your plugin and ending with `Plugin`. For example, `ExamplePlugin.kt` or `ExamplePlugin.java`, or maybe `MyUniquePluginNamePlugin.kt` or `MyUniquePluginNamePlugin.java`.
1. Make sure this class implements at least one of the possible BasePlugin interfaces. Possibilities are:
    - `DetailPanelBasePlugin`
    - `QueItemBasePlugin`
1. Implement all of its (required) methods.

Tip: have a look at already developed plugins for inspiration or explanation of some methods.

### (Optional) Queue items setup

1. Create one or more classes which will be your queue items. These classes must implement the `QueItem` interface and implement all of its methods.
1. Somewhere in your plugin sources panel, add a transferHandle which extends the default `QueItemTransferHandler` in order to use drag-and-drop to add your queue items to the queue.

### Finalizing

1. When your plugin is ready and all tests are green, make sure your dependency versions are correct (using the latest release version of OBS Scene Queue)
1. Package the plugin using `mvn clean package`
1. You (and others) can use your plugin my copying the final jar (`exampleplugin-with-dependencis.jar`) into their plugins folder.
    > If this folder doesn't exist, you can create a new folder `plugins` in the same directory as OBS Scene Queue (or specify in the settings where it will be located).
