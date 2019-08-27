[![](https://jitpack.io/v/lisegmbh/cucumber-order-plugin.svg)](https://jitpack.io/#lisegmbh/cucumber-order-plugin)

# Cucumber Order Plugin

This plugin turns tags from feature files into an order comment to enable sorting in Cukedoktor.

The integration takes place via JitPack.

## Usage with Maven

1. Add [JitPack](https://jitpack.io/#lisegmbh/cucumber-order-plugin) as pluginRepository to your pom.

    ```XML
        <pluginRepositories>
            <pluginRepository>
                <id>jitpack.io</id>
                <url>https://jitpack.io</url>
            </pluginRepository>
        </pluginRepositories>
    ```

2. Add the Plugin to your pom.

   Please note that it must be executed **after Cucumber** and **before Cukecoktor**.
   The plugin is executed in the ``post-integration-test`` phase by default.

    ```XML
        <plugin>
            <groupId>com.github.lisegmbh</groupId>
            <artifactId>cucumber-order-plugin</artifactId>
            <version>1.0.1</version>
            <configuration>
                <jsonSource>${project.build.directory}/cucumber/cucumber.json</jsonSource>
            </configuration>
            <executions>
                <execution>
                    <goals>
                        <goal>execute</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    ```

3. Configuration options

    | Property | Default | Description |
    |----------|---------|-------------|
    | jsonSource | - (mandatory) | Path to the json output from the cucumber plugin|
    | chapterTagName | "Chapter" | Prefix of the tag that can be used in feature files to define  the order|

4. Adjust the feature files accordingly.

    The plugin uses a tag to define the order. The tag is structured as follows:
    >@[ChapterTagName][OrderNumber]

    ```Gherkin
    @Chapter1
    Feature: Example feature
    Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.
    ```

**That should be it. Cukedoctor should now order your features in the order you defined.**
