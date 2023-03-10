package online.addressbook.utils;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.XStream;
import lombok.extern.slf4j.Slf4j;
import online.addressbook.model.GroupData;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class GroupDataGenerator {
    @Parameter(names = "-c", description = "Amount of groups to create, i.e. \"-c 10\"")
    public int count;
    @Parameter(names = "-f", description = "File format, xml or json, i.e. \"-f json\"")
    public String format;

    public static void main(String[] args) throws IOException {
        GroupDataGenerator generator = new GroupDataGenerator();
        JCommander jc = new JCommander(generator);
        try {
            jc.parse(args);
        } catch (ParameterException e) {
            jc.usage();
        }
        generator.run();
    }

    private void run() throws IOException {
        List<GroupData> groups = generateGroups(count);
        String file = String.format("src/test/resources/groups.%s", format);
        switch (format) {
            case "xml" -> {
                saveAsXml(groups, new File(file));
                logSuccess(file);
            }
            case "json" -> {
                saveAsJson(groups, new File(file));
                logSuccess(file);
            }
            default -> log.warn("Unrecognized format: " + format);
        }
    }

    private void saveAsJson(List<GroupData> groups, File file) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        String json = gson.toJson(groups);
        try (Writer writer = new FileWriter(file)) {
            writer.write(json);
        }
    }

    private void saveAsXml(List<GroupData> groups, File file) throws IOException {
        XStream xStream = new XStream();
        xStream.processAnnotations(GroupData.class);
        String xml = xStream.toXML(groups);
        try (Writer writer = new FileWriter(file)) {
            writer.write(xml);
        }
    }

    private List<GroupData> generateGroups(int count) {
        List<GroupData> groups = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            groups.add(new GroupData().withName(String.format("test%s", i))
                    .withHeader(String.format("header%s", i))
                    .withFooter(String.format("footer%s", i)));
        }
        return groups;
    }

    private void logSuccess(String path) {
        log.info(String.format("Generated %s groups in %s", count, path));
    }
}
