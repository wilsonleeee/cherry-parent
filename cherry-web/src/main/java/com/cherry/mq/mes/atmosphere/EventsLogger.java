/*
 *
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://jersey.dev.java.net/CDDL+GPL.html
 * or jersey/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at jersey/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 *
 * Contributor(s):
 *
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package com.cherry.mq.mes.atmosphere;

import org.atmosphere.cpr.AtmosphereResourceEvent;
import org.atmosphere.websocket.WebSocketEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventsLogger implements WebSocketEventListener {

    private static final Logger logger = LoggerFactory.getLogger(EventsLogger.class);

    public EventsLogger() {
    }

    @Override
    public void onPreSuspend(AtmosphereResourceEvent event) {
    }

    public void onSuspend(final AtmosphereResourceEvent event) {
        logger.debug("onSuspend(): {}:{}", event.getResource().getRequest().getRemoteAddr(),
                event.getResource().getRequest().getRemotePort());
    }

    public void onResume(AtmosphereResourceEvent event) {
        logger.debug("onResume(): {}:{}", event.getResource().getRequest().getRemoteAddr(),
                event.getResource().getRequest().getRemotePort());
    }

    public void onDisconnect(AtmosphereResourceEvent event) {
        logger.debug("onDisconnect(): {}:{}", event.getResource().getRequest().getRemoteAddr(),
                event.getResource().getRequest().getRemotePort());
    }

    public void onBroadcast(AtmosphereResourceEvent event) {
        logger.debug("onBroadcast(): {}", event.getMessage(),event);
    }

    public void onThrowable(AtmosphereResourceEvent event) {
        logger.debug("onThrowable(): {}", event);
    }

    @Override
    public void onClose(AtmosphereResourceEvent event) {
        logger.debug("onClose(): {}", event.getMessage(),event);

    }

    public void onHandshake(WebSocketEvent event) {
        logger.debug("onHandshake(): {}", event);
    }

    public void onMessage(WebSocketEvent event) {
        logger.debug("onMessage(): {}", event);
    }

    public void onClose(WebSocketEvent event) {
        logger.debug("onClose(): {}", event);
    }

    public void onControl(WebSocketEvent event) {
        logger.debug("onControl(): {}", event);
    }

    public void onDisconnect(WebSocketEvent event) {
        logger.debug("onDisconnect(): {}", event);
    }

    public void onConnect(WebSocketEvent event) {
        logger.debug("onConnect(): {}", event);
    }
}
