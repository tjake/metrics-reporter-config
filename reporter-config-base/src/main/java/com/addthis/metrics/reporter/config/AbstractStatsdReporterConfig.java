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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class AbstractStatsdReporterConfig extends AbstractHostPortReporterConfig {

    private static final Logger log = LoggerFactory.getLogger(AbstractStatsdReporterConfig.class);

    public AbstractStatsdReporterConfig() {
        super();
    }

    @Override
    public List<HostPort> getFullHostList()
    {
        return getHostListAndStringList();
    }

    protected boolean setup(String className)
    {
        if (!isClassAvailable(className))
        {
            log.error("Tried to enable StatsdReporter, but class {} was not found", className);
            return false;
        }
        List<HostPort> hosts = getFullHostList();
        if (hosts == null || hosts.isEmpty())
        {
            log.error("No hosts specified, cannot enable StatsdReporter");
            return false;
        }

        return true;
    }
}
