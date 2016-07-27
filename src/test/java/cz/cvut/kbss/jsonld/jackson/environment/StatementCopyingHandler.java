package cz.cvut.kbss.jsonld.jackson.environment;

import org.openrdf.model.Statement;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.helpers.RDFHandlerBase;

public class StatementCopyingHandler extends RDFHandlerBase {

    private final RepositoryConnection connection;

    public StatementCopyingHandler(RepositoryConnection connection) {
        this.connection = connection;
    }

    @Override
    public void handleStatement(Statement st) throws RDFHandlerException {
        try {
            connection.add(st);
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }
}
