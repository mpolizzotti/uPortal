/**
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.jasig.portal.tools.dbloader;

import java.io.IOException;
import java.sql.Connection;

import javax.sql.DataSource;

import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceUtils;

/**
 * Runs the Hibernate Schema Export tool using the specified DataSource for the target DB.
 * 
 * @author Eric Dalquist
 * @version $Revision$
 */
public class DataSourceSchemaExport implements ISchemaExport {
    private Resource configuration;
    private DataSource dataSource;
    private String dialect;
    
    /**
     * @return the configuration
     */
    public Resource getConfiguration() {
        return configuration;
    }
    /**
     * @param configuration the configuration to set
     */
    public void setConfiguration(Resource configuration) {
        this.configuration = configuration;
    }

    /**
     * @return the dataSource
     */
    public DataSource getDataSource() {
        return dataSource;
    }
    /**
     * @param dataSource the dataSource to set
     */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * @return the dialect
     */
    public String getDialect() {
        return dialect;
    }
    /**
     * @param dialect the dialect to set
     */
    public void setDialect(String dialect) {
        this.dialect = dialect;
    }

    /**
     * @param export If the database should have the SQL executed agaisnt it
     * @param drop If existing database objects should be dropped before creating new objects
     * @param outputFile Optional file to write out the SQL to.
     */
    public void hbm2ddl(boolean export, boolean create, boolean drop, String outputFile) {
        final AnnotationConfiguration configuration = new AnnotationConfiguration();
        try {
            configuration.configure(this.configuration.getURL());
        }
        catch (IOException e) {
            throw new IllegalArgumentException("Could not load configuration file '" + this.configuration + "'", e);
        }
        
        configuration.setProperty("hibernate.dialect", this.dialect);
        configuration.buildMappings();
        
        final Connection connection = DataSourceUtils.getConnection(this.dataSource);
        try {
            final SchemaExport exporter = new SchemaExport(configuration, connection);
            exporter.setFormat(false);
            if (outputFile != null) {
                exporter.setOutputFile(outputFile);
            }
            
            exporter.execute(true, export, !create, !drop);
        }
        finally {
            DataSourceUtils.releaseConnection(connection, this.dataSource);
        }
    }
}
