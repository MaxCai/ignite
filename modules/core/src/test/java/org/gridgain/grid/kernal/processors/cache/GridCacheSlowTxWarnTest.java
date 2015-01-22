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

package org.gridgain.grid.kernal.processors.cache;

import org.apache.ignite.*;
import org.apache.ignite.cache.*;
import org.apache.ignite.configuration.*;
import org.apache.ignite.internal.*;
import org.apache.ignite.internal.processors.cache.*;
import org.apache.ignite.transactions.*;
import org.apache.ignite.spi.discovery.tcp.*;
import org.apache.ignite.spi.discovery.tcp.ipfinder.*;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.*;
import org.gridgain.testframework.junits.common.*;

import static org.apache.ignite.cache.GridCacheMode.*;

/**
 * Test to check slow TX warning timeout defined by
 * {@link org.apache.ignite.IgniteSystemProperties#GG_SLOW_TX_WARN_TIMEOUT}
 * system property.
 */
public class GridCacheSlowTxWarnTest extends GridCommonAbstractTest {
    /** IP finder. */
    private static final TcpDiscoveryIpFinder ipFinder = new TcpDiscoveryVmIpFinder(true);

    /** {@inheritDoc} */
    @Override protected IgniteConfiguration getConfiguration(String gridName) throws Exception {
        IgniteConfiguration c = super.getConfiguration(gridName);

        CacheConfiguration cc1 = defaultCacheConfiguration();

        cc1.setName("partitioned");
        cc1.setCacheMode(PARTITIONED);
        cc1.setBackups(1);

        CacheConfiguration cc2 = defaultCacheConfiguration();

        cc2.setName("replicated");
        cc2.setCacheMode(REPLICATED);

        CacheConfiguration cc3 = defaultCacheConfiguration();

        cc3.setName("local");
        cc3.setCacheMode(LOCAL);

        c.setCacheConfiguration(cc1, cc2, cc3);

        TcpDiscoverySpi disco = new TcpDiscoverySpi();

        disco.setIpFinder(ipFinder);

        c.setDiscoverySpi(disco);

        return c;
    }

    /**
     * @throws Exception If failed.
     */
    public void testWarningOutput() throws Exception {
        try {
            GridKernal g = (GridKernal)startGrid(1);

            info(">>> Slow tx timeout is not set, long-live txs simulated.");

            checkCache(g, "partitioned", true, false);
            checkCache(g, "replicated", true, false);
            checkCache(g, "local", true, false);

            info(">>> Slow tx timeout is set, long-live tx simulated.");

            checkCache(g, "partitioned", true, true);
            checkCache(g, "replicated", true, true);
            checkCache(g, "local", true, true);

            info(">>> Slow tx timeout is set, no long-live txs.");

            checkCache(g, "partitioned", false, true);
            checkCache(g, "replicated", false, true);
            checkCache(g, "local", false, true);
        }
        finally {
            stopAllGrids();
        }
    }

    /**
     * @param g Grid.
     * @param cacheName Cache.
     * @param simulateTimeout Simulate timeout.
     * @param configureTimeout Alter configuration of TX manager.
     * @throws Exception If failed.
     */
    private void checkCache(Ignite g, String cacheName, boolean simulateTimeout,
        boolean configureTimeout) throws Exception {
        if (configureTimeout) {
            GridCacheAdapter<Integer, Integer> cache = ((GridKernal)g).internalCache(cacheName);

            cache.context().tm().slowTxWarnTimeout(500);
        }

        GridCache<Object, Object> cache1 = g.cache(cacheName);

        IgniteTx tx = cache1.txStart();

        try {
            cache1.put(1, 1);

            if (simulateTimeout)
                Thread.sleep(800);

            tx.commit();
        }
        finally {
            tx.close();
        }

        tx = cache1.txStart();

        try {
            cache1.put(1, 1);

            if (simulateTimeout)
                Thread.sleep(800);

            tx.rollback();
        }
        finally {
            tx.close();
        }
    }
}
