/*
 * Copyright © 2021-2022, RezzedUp <https://github.com/LeafCommunity/ConfigValues>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.configvalues.bukkit.data;

import community.leaf.configvalues.bukkit.DefaultYamlValue;
import community.leaf.configvalues.bukkit.ExampleYamlValue;
import community.leaf.configvalues.bukkit.YamlValue;
import community.leaf.configvalues.bukkit.migrations.Migration;
import community.leaf.configvalues.bukkit.util.Comments;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import pl.tlinkowski.annotation.basic.NullOr;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class YamlDataFile implements UpdatableYamlDataSource
{
    private final Path filePath;
    private final YamlConfiguration data;
    private final Consumer<Exception> exceptions;
    
    private int reloads = 0;
    private boolean isUpdated = false;
    private boolean isNewlyCreated = false;
    private @NullOr Exception invalidReason = null;
    private @NullOr Runnable reloadHandler = null;
    
    public YamlDataFile(Path directoryPath, String name)
    {
        this(directoryPath, name, Load.NOW);
    }
    
    public YamlDataFile(Path directoryPath, String name, Load load)
    {
        this(directoryPath, name, load, Throwable::printStackTrace);
    }
    
    public YamlDataFile(Path directoryPath, String name, Load load, Consumer<Exception> exceptions)
    {
        this.filePath = directoryPath.resolve(name);
        this.data = new YamlConfiguration();
        this.exceptions = exceptions;
        
        if (load == Load.NOW) { reload(); }
    }
    
    public Path getFilePath() { return filePath; }
    
    @Override
    public YamlConfiguration data() { return data; }
    
    @Override
    public boolean isUpdated() { return isUpdated; }
    
    @Override
    public void updated(boolean state) { this.isUpdated = state; }
    
    public boolean isNewlyCreated() { return isNewlyCreated; }
    
    public boolean isInvalid() { return invalidReason != null; }
    
    public @NullOr Exception getInvalidReason() { return invalidReason; }
    
    public final int totalReloads() { return reloads; }
    
    protected void reloadsWith(Runnable reloadHandler)
    {
        Objects.requireNonNull(reloadHandler, "reloadHandler");
        this.reloadHandler = reloadHandler;
        
        // Run right away if config data has already loaded at least once
        if (reloads > 0) { reloadHandler.run(); }
    }
    
    public final void reload()
    {
        reloads++;
        invalidReason = null;
        
        if (Files.isRegularFile(filePath))
        {
            try
            {
                data.loadFromString(Files.readString(filePath));
            }
            catch (InvalidConfigurationException | IOException | RuntimeException e)
            {
                invalidReason = e;
                exceptions.accept(e);
            }
        }
        
        if (reloadHandler != null) { reloadHandler.run(); }
    }
    
    public String toYamlString() { return data.saveToString(); }
    
    public void save()
    {
        boolean pre = Files.isRegularFile(getFilePath());
        write(getFilePath(), toYamlString(), exceptions);
        boolean post = Files.isRegularFile(getFilePath());
        
        this.isNewlyCreated = !pre && post;
    }
    
    public void backupThenSave(Path backupsDirectoryPath, String additionalNameInfo)
    {
        String fileName = filePath.getFileName().toString();
        int lastDot = fileName.lastIndexOf('.');
        
        String name = (lastDot > 0) ? fileName.substring(0, lastDot) : fileName;
        String extension = (lastDot > 0) ? fileName.substring(lastDot) : "";
        
        if (!additionalNameInfo.isEmpty())
        {
            name += "." + additionalNameInfo;
        }
        
        backup(getFilePath(), backupsDirectoryPath.resolve(name + extension), exceptions);
        save();
    }
    
    public void backupThenSave(Path backupsDirectoryPath)
    {
        backupThenSave(backupsDirectoryPath, "");
    }
    
    @SuppressWarnings("deprecation")
    public String header()
    {
        @NullOr String header = data.options().header();
        return (header != null) ? header : "";
    }
    
    @SuppressWarnings("deprecation")
    public void header(String header)
    {
        data.options().header(header);
        // Though this may technically "update" the config, if only the header
        // is changed, that should not constitute rewriting the entire file.
    }
    
    public void headerFromResource(String resource)
    {
        Objects.requireNonNull(resource, "resource");
        
        try
        {
            @NullOr URL resourceUrl = getClass().getClassLoader().getResource(resource);
            if (resourceUrl == null) { throw new IllegalStateException("No such resource: " + resource); }
            
            // Solution to FileSystemNotFoundException -> https://stackoverflow.com/a/22605905
            URI resourceUri = resourceUrl.toURI();
            String[] parts = resourceUri.toString().split("!");
            
            try (FileSystem fs = FileSystems.newFileSystem(URI.create(parts[0]), Map.of()))
            {
                String header = Files.readString(fs.getPath(parts[1]));
                if (!header.isEmpty()) { header(header); }
            }
        }
        catch (IOException | URISyntaxException | RuntimeException e)
        {
            exceptions.accept(e);
        }
    }
    
    private void migrate(YamlValue<?> value, ConfigurationSection existing)
    {
        if (value.migrations().isEmpty()) { return; }
        for (Migration migration : value.migrations()) { migration.migrate(existing, this, value.key()); }
    }
    
    public void migrateValues(List<YamlValue<?>> values, ConfigurationSection existing)
    {
        Objects.requireNonNull(values, "values");
        Objects.requireNonNull(existing, "existing");
        
        for (YamlValue<?> value : values) { migrate(value, existing); }
    }
    
    public void defaultValues(List<YamlValue<?>> defaults)
    {
        boolean fileExists = Files.isRegularFile(filePath);
        
        for (YamlValue<?> value : defaults)
        {
            migrate(value, data);
            
            if (value instanceof DefaultYamlValue<?>)
            {
                // Successfully loaded values from disk already, don't set example value.
                // Examples should only be set when the file is created for the first time.
                if (value instanceof ExampleYamlValue<?> && fileExists) { continue; }
                
                setAsDefaultIfUnset((DefaultYamlValue<?>) value);
            }
            
            List<String> comments = value.comments();
            Comments.set(data, value.key(), (comments.isEmpty()) ? null : comments);
        }
    }
    
    protected static void write(Path filePath, String contents, Consumer<? super IOException> exceptions)
    {
        try
        {
            Path dir = filePath.getParent();
            if (!Files.isDirectory(dir)) { Files.createDirectories(dir); }
            Files.writeString(filePath, contents);
        }
        catch (IOException e) { exceptions.accept(e); }
    }
    
    protected static void backup(Path existingFilePath, Path backupFilePath, Consumer<? super IOException> exceptions)
    {
        // Nothing to back up.
        if (!Files.isRegularFile(existingFilePath)) { return; }
        
        try
        {
            Path backupDirectory = backupFilePath.getParent();
            if (!Files.isDirectory(backupDirectory)) { Files.createDirectories(backupDirectory); }
            
            String backupFileName = backupFilePath.getFileName().toString();
            int lastDot = backupFileName.lastIndexOf('.');
            String name = (lastDot > 0) ? backupFileName.substring(0, lastDot) : "";
            String extension = (lastDot > 0) ? backupFileName.substring(lastDot) : "";
            
            for (int i = 1; i < 1000; i++)
            {
                String attemptedName = name + ".backup_" + LocalDate.now() + "_" + i + extension;
                Path attemptedBackupFilePath = backupDirectory.resolve(attemptedName);
                
                if (Files.isRegularFile(attemptedBackupFilePath)) { continue; }
                
                Files.move(existingFilePath, attemptedBackupFilePath);
                return;
            }
        }
        catch (IOException e) { exceptions.accept(e); }
    }
}
