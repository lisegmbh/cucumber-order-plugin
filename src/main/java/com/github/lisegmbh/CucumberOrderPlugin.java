package com.github.lisegmbh;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.OptionalInt;

@Mojo(name = "execute", defaultPhase = LifecyclePhase.PREPARE_PACKAGE)
public class CucumberOrderPlugin extends AbstractMojo {
    public static final String CHAPTER_TAG_NAME_DEFAULT = "Chapter";

    @Parameter(required = true)
    private String jsonSource;

    @Parameter(defaultValue = CHAPTER_TAG_NAME_DEFAULT)
    private String chapterTagName;

    @Override
    public void execute() throws MojoExecutionException {
        JSONArray json;
        try (FileReader fileReader = new FileReader(jsonSource)) {
            JSONParser parser = new JSONParser();
            json = (JSONArray) parser.parse(fileReader);

            for (Object featureObject : json) {
                JSONObject featureJsonObject = (JSONObject) featureObject;
                JSONArray tagsArray = (JSONArray) featureJsonObject.get("tags");
                OptionalInt chapterNumber = OptionalInt.empty();
                for (Object tagObject : tagsArray) {
                    JSONObject tagJsonObject = (JSONObject) tagObject;
                    String tagName = tagJsonObject.get("name").toString();
                    if (tagName.startsWith("@" + chapterTagName)) {
                        String chapterNumberString = tagName.substring(chapterTagName.length() + 1);
                        chapterNumber = OptionalInt.of(Integer.parseInt(chapterNumberString));
                    }
                }
                if (chapterNumber.isPresent()) {
                    JSONObject commentsObject = new JSONObject();
                    commentsObject.put("line", 1);
                    commentsObject.put("value", "#order: " + chapterNumber.getAsInt());
                    JSONArray commentsArray = new JSONArray();
                    commentsArray.add(commentsObject);
                    featureJsonObject.put("comments", commentsArray);
                }
            }
        } catch (ParseException | IOException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
        try (FileWriter fileWriter = new FileWriter(jsonSource)) {
            fileWriter.write(json.toJSONString());
        } catch (IOException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }
}
