package be.ac.ua.ansymo.cheopsj.distiller.connections.connector;

import java.io.File;

import org.eclipse.core.runtime.IProgressMonitor;

import be.ac.ua.ansymo.cheopsj.distiller.connections.loghandler.RepositoryLogHandler;

public interface RepositoryConnector {
	public void initialize();
	public void updateToRevision(File file, long revisionNumber, IProgressMonitor monitor);
	public long getCurrentRevision(File file);
	public void getCommitMessage(File file, long revisionNumber, RepositoryLogHandler entryhandler);
	public String getFileContents(String filePath, long revision);
	public void setURL(String url);
}
