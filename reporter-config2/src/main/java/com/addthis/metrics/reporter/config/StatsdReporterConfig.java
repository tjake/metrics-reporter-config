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
package com.addthis.metrics.reporter.config;


import com.readytalk.metrics.StatsDReporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class StatsdReporterConfig extends AbstractStatsdReporterConfig implements MetricsReporterConfigTwo
{
    private static final Logger log = LoggerFactory.getLogger(StatsdReporterConfig.class);

    private StatsDReporter reporter;
    

    private void enableMetrics2(HostPort hostPort)
    {
        reporter = new StatsDReporter(hostPort.getHost(), hostPort.getPort(), getResolvedPrefix());
        reporter.start(getPeriod(), getRealTimeunit());
    }


    @Override
    public boolean enable()
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
                enableMetrics2(hostPort);
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
