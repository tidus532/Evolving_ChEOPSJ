package be.ac.ua.ansymo.cheopsj.distiller.connections.loghandler;

import java.util.Date;
import java.util.Map;

public interface RepositoryLogHandler {
	public Map<?, ?> getChangedPaths();
	public String getUser();
	public String getMessage();
	public Date getDate();
}
