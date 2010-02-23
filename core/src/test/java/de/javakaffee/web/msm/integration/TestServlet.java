/*
 * Copyright 2009 Martin Grotzke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an &quot;AS IS&quot; BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package de.javakaffee.web.msm.integration;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

/**
 * The servlet used for integration testing.
 *
 * @author <a href="mailto:martin.grotzke@javakaffee.de">Martin Grotzke</a>
 * @version $Id$
 */
public class TestServlet extends HttpServlet {

    private static final long serialVersionUID = 7954803132860358448L;

    private static final Log LOG = LogFactory.getLog( TestServlet.class );

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet( final HttpServletRequest request, final HttpServletResponse response )
            throws ServletException, IOException {

        LOG.info( "invoked" );

        final PrintWriter out = response.getWriter();
        out.println( "id=" + request.getSession().getId() );

        final HttpSession session = request.getSession( false );
        if ( session != null ) {
            final Enumeration<?> attributeNames = session.getAttributeNames();
            while ( attributeNames.hasMoreElements() ) {
                final String name = attributeNames.nextElement().toString();
                final Object value = session.getAttribute( name );
                out.println( name + "=" + value );
            }
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doPost( final HttpServletRequest request, final HttpServletResponse response ) throws ServletException, IOException {

        LOG.info( "invoked" );

        final PrintWriter out = response.getWriter();
        final HttpSession session = request.getSession();

        out.println( "OK: " + session.getId() );

        @SuppressWarnings( "unchecked" )
        final Enumeration<String> names = request.getParameterNames();
        while ( names.hasMoreElements() ) {
            final String name = names.nextElement();
            final String value = request.getParameter( name );
            session.setAttribute( name, value );
        }

    }

}