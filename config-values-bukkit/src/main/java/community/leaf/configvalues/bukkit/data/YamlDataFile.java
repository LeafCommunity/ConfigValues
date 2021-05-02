/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/ConfigValues>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.configvalues.bukkit.data;

import community.leaf.configvalues.bukkit.DefaultYamlValue;
import community.leaf.configvalues.bukkit.YamlValue;
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
import java.util.Optional;
import java.util.function.Consumer;

public class YamlDataFile implements UpdatableYamlDataSource
{
    private final Path filePath;
    private final YamlConfiguration data;
    private final Consumer<Exception> exceptions;
    
    private boolean isLoaded = false;
    private boolean isUpdated = false;
    private @NullOr Exception invalid = null;
    
    public YamlDataFile(Path directoryPath, String name)
    {
        this(directoryPath, name, Throwable::printStackTrace);
    }
    
    public YamlDataFile(Path directoryPath, String name, Consumer<Exception> exceptions)
    {
        this.filePath = directoryPath.resolve(name);
        this.data = new YamlConfiguration();
        this.exceptions = exceptions;
        
        reload();
    }
    
    public Path getFilePath() { return filePath; }
    
    @Override
    public YamlConfiguration data() { return data; }
    
    public boolean isLoaded() { return isLoaded; }
    
    @Override
    public boolean isUpdated() { return isUpdated; }
    
    @Override
    public void updated(boolean state) { this.isUpdated = state; }
    
    public boolean isInvalid() { return invalid != null; }
    
    public @NullOr Exception getInvalidReason() { return invalid; }
    
    public final void reload()
    {
        invalid = null;
        boolean isAlreadyLoaded = isLoaded;
        
        if (Files.isRegularFile(filePath))
        {
            try
            {
                data.loadFromString(Files.readString(filePath));
                isLoaded = true;
            }
            catch (InvalidConfigurationException | IOException | RuntimeException e)
            {
                invalid = e;
                exceptions.accept(e);
            }
        }
        
        if (isAlreadyLoaded) { handleReload(); }
    }
    
    // Override me.
    protected void handleReload() {}
    
    public String toYamlString() { return data.saveToString(); }
    
    public void save()
    {
        write(getFilePath(), toYamlString(), exceptions);
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
    
    protected void setupHeader(String resource)
    {
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
                if (header.isEmpty()) { return; }
                
                data.options().header(header);
                // Though this may technically "update" the config, if only the header
                // changes, that should not constitute rewriting the entire file.
            }
        }
        catch (IOException | URISyntaxException | RuntimeException e)
        {
            exceptions.accept(e);
        }
    }
    
    public void migrateValues(List<YamlValue<?>> values, ConfigurationSection existing)
    {
        for (YamlValue<?> value : values)
        {
            if (value.migrations().isEmpty()) { continue; }
            
            for (String path : value.migrations())
            {
                @NullOr Object existingValue = existing.get(path);
                if (existingValue == null) { continue; }
    
                Optional<?> newValue = get(value);
                if (newValue.isEmpty() || !existingValue.equals(newValue.get()))
                {
                    set(value.key(), existingValue);
                }
            }
        }
    }
    
    protected void setupDefaults(List<YamlValue<?>> defaults)
    {
        for (YamlValue<?> value : defaults)
        {
            if (!(value instanceof DefaultYamlValue<?>)) { continue; }
            if (((DefaultYamlValue<?>) value).setAsDefaultIfUnset(data)) { updated(true); }
        }
        
        migrateValues(defaults, data);
    }
    
    public static void write(Path filePath, String contents, Consumer<? super IOException> exceptions)
    {
        try { Files.writeString(filePath, contents); }
        catch (IOException e) { exceptions.accept(e); }
    }
    
    public static void backup(Path existingFilePath, Path backupFilePath, Consumer<? super IOException> exceptions)
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
            
            for (int i = 1 ;; i++)
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
