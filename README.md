Provides an instrumented mailbox that tracks size of the message queue and the rate of enqueued messages. The metrics registry is provided via a singleton object. Probably this should only be used for development, not during production. Inspired by Patrik Nordwalls [LoggingMailbox](https://gist.githubusercontent.com/patriknw/5946678/raw/2d4012ac8afdfa690bffbb92dfeb68bc6745ae0f/LoggingMailbox.scala)

Either copy the sources to your project or publish the project locally:

    sbt publish-local
    
You can also publish it to some maven repository, if you control one. Depend on it using something like:

    libraryDependencies += "de.kodemaniak" %% "akka-instrumented-mailbox" % 2.3.2

Using a reporter for the registry:

    val reporter = ConsoleReporter
      .forRegistry(InstrumentedMailboxRegistry.metricRegistry)
      .convertRatesTo(TimeUnit.SECONDS)
      .convertDurationsTo(TimeUnit.MILLISECONDS).build()
    reporter.start(5, TimeUnit.SECONDS)	

###LICENSE

Copyright 2014, Carsten Saathoff

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

[http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.