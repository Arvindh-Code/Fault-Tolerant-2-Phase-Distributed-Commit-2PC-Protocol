# fault-tolerant-2-phase-distributed-commit-2PC-protocol


Project Description

Implement a fault-tolerant 2-phase distributed commit(2PC) protocol and use controlled and randomly injected failures to study how the 2PC protocol handles node crashes. Assume one transaction coordinator (TC) and at least two participants in the 2PC protocol. Similar to the previous projects, we use multiple processes to emulate multiple nodes. Each node (both the TC and the participants) devises a time-out mechanism when no response is received and transits to either the abort or commit state. For simplicity, you can assume that only one node fails in the controlled test. Evaluate different possibilities of failures:

If the coordinator fails before sending the "prepare" message, nodes will not receive the "prepare" message until the time-out and will abort. So, they will respond "no" to the "prepare" message after the coordinator comes back up and sends the "prepare" message.

If the transaction coordinator does not receive "yes" from a node, it will abort the transaction.

TC needs to store the transaction information on disk before sending the "commit" message to the nodes. If the TC fails after sending one "commit" message to the nodes, it can't abort. When it comes back up it will send the "commit" message to the nodes that it didn't send the "commit" message to.

A node needs to store the transaction information before replying "yes" to the TC. If it fails (time-out) after replying "yes"; after it comes back up, it will fetch the commit information from the TC for that particular transaction.

To emulate a failure, you can impose a much longer delay at a failed node than the time-out period used by other active nodes. Node prints their states(commit/abort) before termination. 

--------------------------------------------------------------------------------------------------------------------------------

How to run it?
* Import the project file DS_Project2 as maven using IntelliJ IDEA or Eclipse.
* Ensure the system runs JAVA 11.
* Start the Coordinator.java
* Then Run the Node1.java.
* Then Run the Node2.java.

* Note : txt file will populated to store the transaction and dont delete it.
