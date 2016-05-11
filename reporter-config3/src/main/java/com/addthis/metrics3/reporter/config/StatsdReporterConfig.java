/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.addthis.metrics3.reporter.config;

import com.addthis.metrics.reporter.config.AbstractStatsdReporterConfig;
import com.addthis.metrics.reporter.config.HostPort;
import com.codahale.metrics.MetricRegistry;
import com.readytalk.metrics.StatsDReporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class StatsdReporterConfig extends AbstractStatsdReporterConfig implements MetricsReporterConfigThree
{
    private static final Logger log = LoggerFactory.getLogger(StatsdReporterConfig.class);

    private StatsDReporter reporter;

    private void enableMetrics3(HostPort hostPort, MetricRegistry registry)
    {
        reporter = StatsDReporter.forRegistry(registry)
                .convertRatesTo(getRealRateunit())
                .convertDurationsTo(getRealDurationunit())
                .prefixedWith(getResolvedPrefix())
                .filter(MetricFilterTransformer.generateFilter(getPredicate()))
                .build(hostPort.getHost(), hostPort.getPort());
        reporter.start(getPeriod(), getRealTimeunit());
    }

    @Override
    public void report() {
        if (reporter != null) {
            reporter.report();
        }
    }

    @Override
    public boolean enable(MetricRegistry registry)
    {
        boolean success = setup("com.readytalk.metrics.StatsDReporter");
        if (!success)
        {
            return false;
        }
        List<HostPort> hosts = getFullHostList();
        for (HostPort hostPort : hosts)
        {
            log.info("Enabling StatsDReporter to {}:{}", new Object[]{hostPort.getHost(), hostPort.getPort()});
            try
            {
                enableMetrics3(hostPort, registry);
            }
            catch (Exception e)
            {
                log.error("Failed to enable StatsDReporter", e);
                return false;
            }
        }
        return true;
    }
}
