/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.vfs2.impl;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs2.CacheStrategy;
import org.apache.commons.vfs2.Capability;
import org.apache.commons.vfs2.FileContentInfoFactory;
import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystem;
import org.apache.commons.vfs2.FileSystemConfigBuilder;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.FileType;
import org.apache.commons.vfs2.FilesCache;
import org.apache.commons.vfs2.NameScope;
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.cache.SoftRefFilesCache;
import org.apache.commons.vfs2.operations.FileOperationProvider;
import org.apache.commons.vfs2.provider.AbstractFileName;
import org.apache.commons.vfs2.provider.AbstractFileProvider;
import org.apache.commons.vfs2.provider.DefaultURLStreamHandler;
import org.apache.commons.vfs2.provider.FileProvider;
import org.apache.commons.vfs2.provider.FileReplicator;
import org.apache.commons.vfs2.provider.LocalFileProvider;
import org.apache.commons.vfs2.provider.TemporaryFileStore;
import org.apache.commons.vfs2.provider.UriParser;
import org.apache.commons.vfs2.provider.VfsComponent;

/**
 * A default file system manager implementation.
 *
 * @author <a href="http://commons.apache.org/vfs/team-list.html">Commons VFS team</a>
 *          2006) $
 */
public class DefaultFileSystemManager implements FileSystemManager
{
    /**
     * The provider for local files.
     */
    private LocalFileProvider localFileProvider;

    /**
     * The default provider.
     */
    private FileProvider defaultProvider;

    /**
     * The file replicator to use.
     */
    private FileReplicator fileReplicator;

    /**
     * Mapping from URI scheme to FileProvider.
     */
    private final Map<String, FileProvider> providers = new HashMap<String, FileProvider>();

    /**
     * All components used by this manager.
     */
    private final ArrayList<Object> components = new ArrayList<Object>();

    /**
     * The base file to use for relative URI.
     */
    private FileObject baseFile;

    /**
     * The files cache
     */
    private FilesCache filesCache;

    /**
     * The cache strategy
     */
    private CacheStrategy fileCacheStrategy;

    /**
     * Class which decorates all returned fileObjects
     */
    private Class<?> fileObjectDecorator;
    private Constructor<?> fileObjectDecoratorConst;

    /**
     * The class to use to determine the content-type (mime-type)
     */
    private FileContentInfoFactory fileContentInfoFactory;

    /**
     * The logger to use.
     */
    private Log log = LogFactory.getLog(getClass());

    /**
     * The context to pass to providers.
     */
    private final DefaultVfsComponentContext context = new DefaultVfsComponentContext(
        this);

    private TemporaryFileStore tempFileStore;
    private final FileTypeMap map = new FileTypeMap();
    private final VirtualFileProvider vfsProvider = new VirtualFileProvider();
    private boolean init;

    private final Map<String, List<FileOperationProvider>> operationProviders =
        new HashMap<String, List<FileOperationProvider>>();

    /**
     * Returns the logger used by this manager.
     * @return the Logger.
     */
    protected Log getLogger()
    {
        return log;
    }

    /**
     * Registers a file system provider. The manager takes care of all lifecycle
     * management. A provider may be registered multiple times.
     *
     * @param urlScheme
     *            The scheme the provider will handle.
     * @param provider
     *            The provider.
     * @throws FileSystemException if an error occurs adding the provider.
     */
    public void addProvider(final String urlScheme, final FileProvider provider)
    throws FileSystemException
    {
        addProvider(new String[] {urlScheme}, provider);
    }

    /**
     * Registers a file system provider. The manager takes care of all lifecycle
     * management. A provider may be registered multiple times.
     *
     * @param urlSchemes
     *            The schemes the provider will handle.
     * @param provider
     *            The provider.
     * @throws FileSystemException if an error occurs adding the provider.
     */
    public void addProvider(final String[] urlSchemes,
                            final FileProvider provider) throws FileSystemException
    {
        // Warn about duplicate providers
        for (int i = 0; i < urlSchemes.length; i++)
        {
            final String scheme = urlSchemes[i];
            if (providers.containsKey(scheme))
            {
                throw new FileSystemException(
                    "vfs.impl/multiple-providers-for-scheme.error", scheme);
            }
        }

        // Contextualise the component (if not already)
        setupComponent(provider);

        // Add to map
        for (int i = 0; i < urlSchemes.length; i++)
        {
            final String scheme = urlSchemes[i];
            providers.put(scheme, provider);
        }

        if (provider instanceof LocalFileProvider && localFileProvider == null)
        {
            localFileProvider = (LocalFileProvider) provider;
        }
    }

    /**
     * Returns true if this manager has a provider for a particular scheme.
     * @param scheme The scheme to check.
     * @return true if a provider is configured for this scheme, false otherwise.
     */
    public boolean hasProvider(final String scheme)
    {
        return providers.containsKey(scheme);
    }

    /**
     * Adds an filename extension mapping.
     *
     * @param extension The file name extension.
     * @param scheme The scheme to use for files with this extension.
     */
    public void addExtensionMap(final String extension, final String scheme)
    {
        map.addExtension(extension, scheme);
    }

    /**
     * Adds a mime type mapping.
     *
     * @param mimeType The mime type.
     * @param scheme The scheme to use for files with this mime type.
     */
    public void addMimeTypeMap(final String mimeType, final String scheme)
    {
        map.addMimeType(mimeType, scheme);
    }

    /**
     * Sets the default provider. This is the provider that will handle URI with
     * unknown schemes. The manager takes care of all lifecycle management.
     * @param provider The FileProvider.
     * @throws FileSystemException if an error occurs setting the provider.
     */
    public void setDefaultProvider(final FileProvider provider)
    throws FileSystemException
    {
        setupComponent(provider);
        defaultProvider = provider;
    }

    /**
     * Returns the filesCache implementation used to cache files.
     * @return The FilesCache.
     */
    public FilesCache getFilesCache()
    {
        return filesCache;
    }

    /**
     * Sets the filesCache implementation used to cache files.
     * @param filesCache The FilesCache.
     * @throws FileSystemException if an error occurs setting the cache..
     */
    public void setFilesCache(final FilesCache filesCache)
    throws FileSystemException
    {
        if (init)
        {
            throw new FileSystemException("vfs.impl/already-inited.error");
        }

        this.filesCache = filesCache;
    }

    /**
     * <p>
     * Set the cache strategy to use when dealing with file object data. You can
     * set it only once before the FileSystemManager is initialized.
     * <p />
     * <p>
     * The default is {@link CacheStrategy#ON_RESOLVE}
     * </p>
     *
     * @param fileCacheStrategy The CacheStrategy to use.
     * @throws FileSystemException
     *             if this is not possible. e.g. it is already set.
     */
    public void setCacheStrategy(final CacheStrategy fileCacheStrategy)
    throws FileSystemException
    {
        if (init)
        {
            throw new FileSystemException("vfs.impl/already-inited.error");
        }

        this.fileCacheStrategy = fileCacheStrategy;
    }

    /**
     * Get the cache strategy used.
     * @return The CacheStrategy.
     */
    public CacheStrategy getCacheStrategy()
    {
        return fileCacheStrategy;
    }

    /**
     * Get the file object decorator used.
     * @return The decorator.
     */
    public Class<?> getFileObjectDecorator()
    {
        return fileObjectDecorator;
    }

    /**
     * The constructor associated to the fileObjectDecorator.
     * We cache it here for performance reasons.
     * @return The decorator's Constructor.
     */
    public Constructor<?> getFileObjectDecoratorConst()
    {
        return fileObjectDecoratorConst;
    }

    /**
     * Set a fileObject decorator to be used for ALL returned file objects.
     *
     * @param fileObjectDecorator must be inherted from {@link DecoratedFileObject} a has to provide a
     * constructor with a single {@link FileObject} as argument
     * @throws FileSystemException if an error occurs setting the decorator.
     */
    public void setFileObjectDecorator(Class<?> fileObjectDecorator) throws FileSystemException
    {
        if (init)
        {
            throw new FileSystemException("vfs.impl/already-inited.error");
        }
        if (!DecoratedFileObject.class.isAssignableFrom(fileObjectDecorator))
        {
            throw new FileSystemException("vfs.impl/invalid-decorator.error", fileObjectDecorator.getName());
        }

        try
        {
            fileObjectDecoratorConst = fileObjectDecorator.getConstructor(new Class[] {FileObject.class});
        }
        catch (NoSuchMethodException e)
        {
            throw new FileSystemException("vfs.impl/invalid-decorator.error", fileObjectDecorator.getName(), e);
        }

        this.fileObjectDecorator = fileObjectDecorator;
    }

    /**
     * get the fileContentInfoFactory used to determine the infos of a file
     * content.
     * @return The FileContentInfoFactory.
     */
    public FileContentInfoFactory getFileContentInfoFactory()
    {
        return fileContentInfoFactory;
    }

    /**
     * set the fileContentInfoFactory used to determine the infos of a file
     * content.
     * @param fileContentInfoFactory The FileContentInfoFactory.
     * @throws FileSystemException if an error occurs setting the FileContentInfoFactory.
     */
    public void setFileContentInfoFactory(FileContentInfoFactory fileContentInfoFactory)
    throws FileSystemException
    {
        if (init)
        {
            throw new FileSystemException("vfs.impl/already-inited.error");
        }

        this.fileContentInfoFactory = fileContentInfoFactory;
    }

    /**
     * Sets the file replicator to use. The manager takes care of all lifecycle
     * management.
     * @param replicator The FileReplicator.
     * @throws FileSystemException if an error occurs setting the replicator.
     */
    public void setReplicator(final FileReplicator replicator)
    throws FileSystemException
    {
        setupComponent(replicator);
        fileReplicator = replicator;
    }

    /**
     * Sets the temporary file store to use. The manager takes care of all
     * lifecycle management.
     * @param tempFileStore The temporary FileStore.
     * @throws FileSystemException if an error occurs adding the file store.
     */
    public void setTemporaryFileStore(final TemporaryFileStore tempFileStore)
    throws FileSystemException
    {
        setupComponent(tempFileStore);
        this.tempFileStore = tempFileStore;
    }

    /**
     * Sets the logger to use.
     * @param log The Logger to use.
     */
    public void setLogger(final Log log)
    {
        this.log = log;
    }

    /**
     * Initialises a component, if it has not already been initialised.
     * @param component The component to setup.
     * @throws FileSystemException if an error occurs.
     */
    private void setupComponent(final Object component)
    throws FileSystemException
    {
        if (!components.contains(component))
        {
            if (component instanceof VfsComponent)
            {
                final VfsComponent vfsComponent = (VfsComponent) component;
                vfsComponent.setLogger(getLogger());
                vfsComponent.setContext(context);
                vfsComponent.init();
            }
            components.add(component);
        }
    }

    /**
     * Closes a component, if it has not already been closed.
     * @param component The component to close.
     */
    private void closeComponent(final Object component)
    {
        if (component != null && components.contains(component))
        {
            if (component instanceof VfsComponent)
            {
                final VfsComponent vfsComponent = (VfsComponent) component;
                vfsComponent.close();
            }
            components.remove(component);
        }
    }

    /**
     * Returns the file replicator.
     *
     * @return The file replicator. Never returns null.
     * @throws FileSystemException if there is no FileReplicator.
     */
    public FileReplicator getReplicator() throws FileSystemException
    {
        if (fileReplicator == null)
        {
            throw new FileSystemException("vfs.impl/no-replicator.error");
        }
        return fileReplicator;
    }

    /**
     * Returns the temporary file store.
     *
     * @return The file store. Never returns null.
     * @throws FileSystemException if there is no TemporaryFileStore.
     */
    public TemporaryFileStore getTemporaryFileStore()
    throws FileSystemException
    {
        if (tempFileStore == null)
        {
            throw new FileSystemException("vfs.impl/no-temp-file-store.error");
        }
        return tempFileStore;
    }

    /**
     * Initialises this manager.
     * @throws FileSystemException if an error occurs during initialization.
     */
    public void init() throws FileSystemException
    {
        if (filesCache == null)
        {
            // filesCache = new DefaultFilesCache();
            filesCache = new SoftRefFilesCache();
        }
        if (fileContentInfoFactory == null)
        {
            fileContentInfoFactory = new FileContentInfoFilenameFactory();
        }

        if (fileCacheStrategy == null)
        {
            fileCacheStrategy = CacheStrategy.ON_RESOLVE;
        }

        setupComponent(filesCache);
        setupComponent(vfsProvider);

        init = true;
    }

    /**
     * Closes all files created by this manager, and cleans up any temporary
     * files. Also closes all providers and the replicator.
     */
    public void close()
    {
        if (!init)
        {
            return;
        }

        // Close the providers.
        for (Iterator<?> iterator = providers.values().iterator(); iterator
                .hasNext();)
        {
            final Object provider = iterator.next();
            closeComponent(provider);
        }

        // Close the other components
        closeComponent(defaultProvider);
        closeComponent(fileReplicator);
        closeComponent(tempFileStore);

        components.clear();
        providers.clear();
        filesCache.close();
        localFileProvider = null;
        defaultProvider = null;
        fileReplicator = null;
        tempFileStore = null;
        init = false;
    }

    /**
     * Free all resources used by unused filesystems created by this manager.
     */
    public void freeUnusedResources()
    {
        if (!init)
        {
            return;
        }

        // Close the providers.
        for (Iterator<FileProvider> iterator = providers.values().iterator(); iterator
                .hasNext();)
        {
            final AbstractFileProvider provider = (AbstractFileProvider) iterator
                                                  .next();
            provider.freeUnusedResources();
        }
    }

    /**
     * Sets the base file to use when resolving relative URI.
     * @param baseFile The new base FileObject.
     * @throws FileSystemException if an error occurs.
     */
    public void setBaseFile(final FileObject baseFile)
    throws FileSystemException
    {
        this.baseFile = baseFile;
    }

    /**
     * Sets the base file to use when resolving relative URI.
     * @param baseFile The new base FileObject.
     * @throws FileSystemException if an error occurs.
     */
    public void setBaseFile(final File baseFile) throws FileSystemException
    {
        this.baseFile = getLocalFileProvider().findLocalFile(baseFile);
    }

    /**
     * Returns the base file used to resolve relative URI.
     * @return The FileObject that represents the base file.
     * @throws FileSystemException if an error occurs.
     */
    public FileObject getBaseFile() throws FileSystemException
    {
        return baseFile;
    }

    /**
     * Locates a file by URI.
     * @param uri The URI of the file to locate.
     * @return The FileObject for the located file.
     * @throws FileSystemException if the file cannot be located or an error occurs.
     */
    public FileObject resolveFile(final String uri) throws FileSystemException
    {
        // return resolveFile(baseFile, uri);
        return resolveFile(getBaseFile(), uri);
    }

    /**
     * Locate a file by URI, use the FileSystemOptions for file-system creation.
     * @param uri The URI of the file to locate.
     * @param fileSystemOptions The options for the FileSystem.
     * @return The FileObject for the located file.
     * @throws FileSystemException if the file cannot be located or an error occurs.
     */

    public FileObject resolveFile(final String uri,
                                  final FileSystemOptions fileSystemOptions)
    throws FileSystemException
    {
        // return resolveFile(baseFile, uri, fileSystemOptions);
        return resolveFile(getBaseFile(), uri, fileSystemOptions);
    }

    /**
     * Locates a file by URI.
     * @param baseFile The base File to use to locate the file.
     * @param uri The URI of the file to locate.
     * @return The FileObject for the located file.
     * @throws FileSystemException if the file cannot be located or an error occurs.
     */
    public FileObject resolveFile(final File baseFile, final String uri)
    throws FileSystemException
    {
        final FileObject baseFileObj = getLocalFileProvider().findLocalFile(
                                           baseFile);
        return resolveFile(baseFileObj, uri);
    }

    /**
     * Resolves a URI, relative to a base file.
     * @param baseFile The base FileOjbect to use to locate the file.
     * @param uri The URI of the file to locate.
     * @return The FileObject for the located file.
     * @throws FileSystemException if the file cannot be located or an error occurs.
     */
    public FileObject resolveFile(final FileObject baseFile, final String uri)
    throws FileSystemException
    {
        return resolveFile(baseFile, uri, baseFile == null ? null : baseFile
                           .getFileSystem().getFileSystemOptions());
    }

    /**
     * Resolves a URI, realtive to a base file with specified FileSystem
     * configuration.
     * @param baseFile The base file.
     * @param uri The file name. May be a fully qualified or relative path or a url.
     * @param fileSystemOptions Options to pass to the file system.
     * @return A FileObject representing the target file.
     * @throws FileSystemException if an error occurs accessing the file.
     */
    public FileObject resolveFile(final FileObject baseFile, final String uri,
                                  final FileSystemOptions fileSystemOptions)
    throws FileSystemException
    {
        final FileObject realBaseFile;
        if (baseFile != null && VFS.isUriStyle()
                && baseFile.getName().getType() == FileType.FILE)
        {
            realBaseFile = baseFile.getParent();
        }
        else
        {
            realBaseFile = baseFile;
        }
        // TODO: use resolveName and use this name to resolve the fileObject

        UriParser.checkUriEncoding(uri);

        if (uri == null)
        {
            throw new IllegalArgumentException();
        }

        // Extract the scheme
        final String scheme = UriParser.extractScheme(uri);
        if (scheme != null)
        {
            // An absolute URI - locate the provider
            final FileProvider provider = providers.get(scheme);
            if (provider != null)
            {
                return provider.findFile(realBaseFile, uri, fileSystemOptions);
            }
            // Otherwise, assume a local file
        }

        // Handle absolute file names
        if (localFileProvider != null
                && localFileProvider.isAbsoluteLocalName(uri))
        {
            return localFileProvider.findLocalFile(uri);
        }

        if (scheme != null)
        {
            // An unknown scheme - hand it to the default provider
            if (defaultProvider == null)
            {
                throw new FileSystemException("vfs.impl/unknown-scheme.error",
                                              new Object[] {scheme, uri});
            }
            return defaultProvider.findFile(realBaseFile, uri, fileSystemOptions);
        }

        // Assume a relative name - use the supplied base file
        if (realBaseFile == null)
        {
            throw new FileSystemException("vfs.impl/find-rel-file.error", uri);
        }

        return realBaseFile.resolveFile(uri);
    }

    /**
     * Resolves a name, relative to the file. If the supplied name is an
     * absolute path, then it is resolved relative to the root of the file
     * system that the file belongs to. If a relative name is supplied, then it
     * is resolved relative to this file name.
     * @param root The base FileName.
     * @param path The path to the file relative to the base FileName or an absolute path.
     * @return The constructed FileName.
     * @throws FileSystemException if an error occurs constructing the FileName.
     */
    public FileName resolveName(final FileName root, final String path)
    throws FileSystemException
    {
        return resolveName(root, path, NameScope.FILE_SYSTEM);
    }

    /**
     * Resolves a name, relative to the root.
     *
     * @param base the base filename
     * @param name the name
     * @param scope the {@link NameScope}
     * @return The FileName of the file.
     * @throws FileSystemException if an error occurs.
     */
    public FileName resolveName(final FileName base, final String name,
                                final NameScope scope) throws FileSystemException
    {
        final FileName realBase;
        if (base != null && VFS.isUriStyle() && base.getType() == FileType.FILE)
        {
            realBase = base.getParent();
        }
        else
        {
            realBase = base;
        }

        final StringBuilder buffer = new StringBuilder(name);

        // Adjust separators
        UriParser.fixSeparators(buffer);
        String scheme = UriParser.extractScheme(buffer.toString());

        // Determine whether to prepend the base path
        if (name.length() == 0 || (scheme == null && buffer.charAt(0) != FileName.SEPARATOR_CHAR))
        {
            // Supplied path is not absolute
            if (!VFS.isUriStyle())
            {
                // when using uris the parent already do have the trailing "/"
                buffer.insert(0, FileName.SEPARATOR_CHAR);
            }
            buffer.insert(0, realBase.getPath());
        }

        // // UriParser.canonicalizePath(buffer, 0, name.length());

        // Normalise the path
        FileType fileType = UriParser.normalisePath(buffer);

        // Check the name is ok
        final String resolvedPath = buffer.toString();
        if (!AbstractFileName
                .checkName(realBase.getPath(), resolvedPath, scope))
        {
            throw new FileSystemException(
                "vfs.provider/invalid-descendent-name.error", name);
        }

        String fullPath;
        if (scheme != null)
        {
            fullPath = resolvedPath;
        }
        else
        {
            scheme = realBase.getScheme();
            fullPath = realBase.getRootURI() + resolvedPath;
        }
        final FileProvider provider = providers.get(scheme);
        if (provider != null)
        {
            // todo: extend the filename parser to be able to parse
            // only a pathname and take the missing informations from
            // the base. Then we can get rid of the string operation.
            // // String fullPath = base.getRootURI() +
            // resolvedPath.substring(1);

            return provider.parseUri(realBase, fullPath);
        }

        if (scheme != null)
        {
            // An unknown scheme - hand it to the default provider - if possible
            if (defaultProvider != null)
            {
                return defaultProvider.parseUri(realBase, fullPath);
            }
        }

        // todo: avoid fallback to this point
        // this happens if we have a virtual filesystem (no provider for scheme)
        return ((AbstractFileName) realBase).createName(resolvedPath, fileType);
    }

    /**
     * Resolve the uri to a filename.
     * @param uri The URI to resolve.
     * @return The FileName of the file.
     * @throws FileSystemException if an error occurs.
     */
    public FileName resolveURI(String uri) throws FileSystemException
    {
        UriParser.checkUriEncoding(uri);

        if (uri == null)
        {
            throw new IllegalArgumentException();
        }

        // Extract the scheme
        final String scheme = UriParser.extractScheme(uri);
        if (scheme != null)
        {
            // An absolute URI - locate the provider
            final FileProvider provider = providers.get(scheme);
            if (provider != null)
            {
                return provider.parseUri(null, uri);
            }

            // Otherwise, assume a local file
        }

        // Handle absolute file names
        if (localFileProvider != null
                && localFileProvider.isAbsoluteLocalName(uri))
        {
            return localFileProvider.parseUri(null, uri);
        }

        if (scheme != null)
        {
            // An unknown scheme - hand it to the default provider
            if (defaultProvider == null)
            {
                throw new FileSystemException("vfs.impl/unknown-scheme.error",
                                              new Object[] {scheme, uri});
            }
            return defaultProvider.parseUri(null, uri);
        }

        // Assume a relative name - use the supplied base file
        if (baseFile == null)
        {
            throw new FileSystemException("vfs.impl/find-rel-file.error", uri);
        }

        return resolveName(baseFile.getName(), uri, NameScope.FILE_SYSTEM);
    }

    /**
     * Converts a local file into a {@link FileObject}.
     * @param file The input File.
     * @return the create FileObject
     * @throws FileSystemException if an error occurs creating the file naem.
     */
    public FileObject toFileObject(final File file) throws FileSystemException
    {
        return getLocalFileProvider().findLocalFile(file);
    }

    /**
     * Creates a layered file system.
     * @param scheme The scheme to use.
     * @param file The FileObject.
     * @return The layered FileObject.
     * @throws FileSystemException if an error occurs.
     */
    public FileObject createFileSystem(final String scheme,
                                       final FileObject file) throws FileSystemException
    {
        final FileProvider provider = providers.get(scheme);
        if (provider == null)
        {
            throw new FileSystemException("vfs.impl/unknown-provider.error",
                                          new Object[] {scheme, file});
        }
        return provider.createFileSystem(scheme, file, file.getFileSystem().getFileSystemOptions());
    }

    /**
     * Creates a layered file system.
     * @param file The FileObject to use.
     * @return The layered FileObject.
     * @throws FileSystemException if an error occurs.
     */
    public FileObject createFileSystem(final FileObject file)
    throws FileSystemException
    {
        final String scheme = map.getScheme(file);
        if (scheme == null)
        {
            throw new FileSystemException(
                "vfs.impl/no-provider-for-file.error", file);
        }

        return createFileSystem(scheme, file);
    }

    /**
     * Determines if a layered file system can be created for a given file.
     *
     * @param file The file to check for.
     * @return true if the FileSystem can be created.
     * @throws FileSystemException if an error occurs.
     */
    public boolean canCreateFileSystem(final FileObject file)
    throws FileSystemException
    {
        return map.getScheme(file) != null;
    }

    /**
     * Creates a virtual file system.
     * @param rootFile The FileObject to use.
     * @return The FileObject in the VirtualFileSystem.
     * @throws FileSystemException if an error occurs creating the file.
     */
    public FileObject createVirtualFileSystem(final FileObject rootFile)
    throws FileSystemException
    {
        return vfsProvider.createFileSystem(rootFile);
    }

    /**
     * Creates an empty virtual file system.
     * @param rootUri The URI to use as the root of the FileSystem.
     * @return A FileObject in the virtual FileSystem.
     * @throws FileSystemException if an error occurs.
     */
    public FileObject createVirtualFileSystem(final String rootUri)
    throws FileSystemException
    {
        return vfsProvider.createFileSystem(rootUri);
    }

    /**
     * Locates the local file provider.
     * @return The LocalFileProvider.
     * @throws FileSystemException if an error occurs.
     */
    private LocalFileProvider getLocalFileProvider() throws FileSystemException
    {
        if (localFileProvider == null)
        {
            throw new FileSystemException(
                "vfs.impl/no-local-file-provider.error");
        }
        return localFileProvider;
    }

    /**
     * Get the URLStreamHandlerFactory.
     * @return The URLStreamHandlerFactory.
     */
    public URLStreamHandlerFactory getURLStreamHandlerFactory()
    {
        return new VfsStreamHandlerFactory();
    }

    /**
     * Closes the given filesystem.<br />
     * If you use VFS as singleton it is VERY dangerous to call this method
     * @param filesystem The FileSystem to close.
     */
    public void closeFileSystem(FileSystem filesystem)
    {
        // inform the cache ...
        getFilesCache().clear(filesystem);

        // just in case the cache didnt call _closeFileSystem
        _closeFileSystem(filesystem);
    }

    /**
     * Closes the given filesystem.<br />
     * If you use VFS as singleton it is VERY dangerous to call this method
     * @param filesystem The FileSystem to close.
     */
    public void _closeFileSystem(FileSystem filesystem)
    {
        FileProvider provider = providers.get(filesystem.getRootName().getScheme());
        if (provider != null)
        {
            ((AbstractFileProvider) provider).closeFileSystem(filesystem);
        }
    }

    /**
     * This is an internal class because it needs access to the private member
     * providers.
     */
    final class VfsStreamHandlerFactory implements URLStreamHandlerFactory
    {
        public URLStreamHandler createURLStreamHandler(final String protocol)
        {
            FileProvider provider = providers.get(protocol);
            if (provider != null)
            {
                return new DefaultURLStreamHandler(context);
            }

            // Route all other calls to the default URLStreamHandlerFactory
            return new URLStreamHandlerProxy();
        }
    }

    /**
     * Get the schemes currently available.
     * @return The array of scheme names.
     */
    public String[] getSchemes()
    {
        String[] schemes = new String[providers.size()];
        providers.keySet().toArray(schemes);
        return schemes;
    }

    /**
     * Get the capabilities for a given scheme.
     *
     * @param scheme The scheme to located.
     * @return A Collection of capabilities.
     * @throws FileSystemException if the given scheme is not konwn
     */
    public Collection<Capability> getProviderCapabilities(final String scheme)
    throws FileSystemException
    {
        FileProvider provider = providers.get(scheme);
        if (provider == null)
        {
            throw new FileSystemException("vfs.impl/unknown-scheme.error",
                                          new Object[] {scheme});
        }

        return provider.getCapabilities();
    }

    /**
     * Get the configuration builder for the given scheme.
     * @param scheme The scheme to locate.
     * @return The FileSystemConfigBuilder for the scheme.
     * @throws FileSystemException if the given scheme is not konwn
     */
    public FileSystemConfigBuilder getFileSystemConfigBuilder(final String scheme)
    throws FileSystemException
    {
        FileProvider provider = providers.get(scheme);
        if (provider == null)
        {
            throw new FileSystemException("vfs.impl/unknown-scheme.error", new Object[] {scheme});
        }

        return provider.getConfigBuilder();
    }

    // -- OPERATIONS --

    /**
     * Adds the specified FileOperationProvider for the specified scheme.
     * Several FileOperationProvider's might be registered for the same scheme.
     * For example, for "file" scheme we can register SvnWsOperationProvider and
     * CvsOperationProvider.
     *
     * @param scheme The scheme the provider should be registered for.
     * @param operationProvider The FileOperationProvider.
     * @throws FileSystemException if an error occurs adding the provider.
     */
    public void addOperationProvider(final String scheme,
                                     final FileOperationProvider operationProvider)
    throws FileSystemException
    {
        addOperationProvider(new String[] {scheme}, operationProvider);
    }

    /**
     * @see FileSystemManager#addOperationProvider(String,
     *      org.apache.commons.vfs2.operations.FileOperationProvider)
     *
     * @param schemes The array of schemes the provider should apply to.
     * @param operationProvider The FileOperationProvider.
     * @throws FileSystemException if an error occurs.
     */
    public void addOperationProvider(final String[] schemes,
                                     final FileOperationProvider operationProvider)
    throws FileSystemException
    {
        for (int i = 0; i < schemes.length; i++)
        {
            final String scheme = schemes[i];

            if (!operationProviders.containsKey(scheme))
            {
                final List<FileOperationProvider> providers = new ArrayList<FileOperationProvider>();
                operationProviders.put(scheme, providers);
            }

            final List<FileOperationProvider> providers = operationProviders.get(scheme);

            if (providers.contains(operationProvider))
            {
                throw new FileSystemException(
                    "vfs.operation/operation-provider-already-added.error", scheme);
            }

            setupComponent(operationProvider);

            providers.add(operationProvider);
        }
    }

    /**
     * @param scheme
     *            the scheme for wich we want to get the list af registered
     *            providers.
     *
     * @return the registered FileOperationProviders for the specified scheme.
     *         If there were no providers registered for the scheme, it returns
     *         null.
     *
     * @throws FileSystemException if an error occurs.
     */
    public FileOperationProvider[] getOperationProviders(final String scheme)
    throws FileSystemException
    {

        List<?> providers = operationProviders.get(scheme);
        if (providers == null || providers.size() == 0)
        {
            return null;
        }
        return providers.toArray(new FileOperationProvider[] {});
    }
}
