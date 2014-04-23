/**
 * Copyright 2014, Carsten Saathoff
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package akka.contrib.mailbox

import scala.concurrent.duration._
import java.util.concurrent.atomic.AtomicInteger
import com.typesafe.config.Config
import akka.actor.{ActorRef, ActorSystem}
import akka.dispatch.{Envelope, MailboxType, MessageQueue, UnboundedMailbox}
import akka.event.Logging
import nl.grons.metrics.scala.InstrumentedBuilder

/**
 * Tracks number of messages in the mailbox and rate of enqueued messages using
 * Codahale Metrics. Uses the InstrumentedMailboxRegistry singleton object, which
 * contains a reference to a metrics registry.
 *
 * Configuration:
 * <pre>
 * akka.actor.default-mailbox {
 *   mailbox-type = akka.contrib.mailbox.InstrumentedMailboxType
 * }
 * </pre>
 */
class InstrumentedMailboxType(settings: ActorSystem.Settings, config: Config) extends MailboxType {
  override def create(owner: Option[ActorRef], system: Option[ActorSystem]) = (owner, system) match {
    case (Some(o), Some(s)) ⇒
      val mailbox = new InstrumentedMailbox(o, s)
      mailbox
    case _ ⇒ throw new Exception("no mailbox owner or system given")
  }
}

class InstrumentedMailbox(owner: ActorRef, system: ActorSystem) extends UnboundedMailbox.MessageQueue with InstrumentedBuilder {
  
	val metricRegistry = InstrumentedMailboxRegistry.metricRegistry
  private val queueSize = new AtomicInteger
	
  private val path = owner.path.toStringWithoutAddress
  private val enqueueMeter = metrics.meter(path, "enqueued")
	metrics.gauge(path, "queue-size") {
		queueSize.get()
	}

  override def dequeue(): Envelope = {
    val x = super.dequeue()
    if (x ne null) {
      queueSize.decrementAndGet
    }
    x
  }

  override def enqueue(receiver: ActorRef, handle: Envelope): Unit = {
    super.enqueue(receiver, handle)
    enqueueMeter.mark()
    queueSize.incrementAndGet
  }

  override def numberOfMessages: Int = queueSize.get

  override def cleanUp(owner: ActorRef, deadLetters: MessageQueue): Unit = {
    super.cleanUp(owner, deadLetters)
  }
}