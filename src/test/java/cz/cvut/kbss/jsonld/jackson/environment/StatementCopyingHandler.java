/**
 * Copyright (C) 2016 Czech Technical University in Prague
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
