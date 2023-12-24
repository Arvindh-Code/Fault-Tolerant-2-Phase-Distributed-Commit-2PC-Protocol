# Fault-Tolerant 2-Phase Distributed Commit (2PC) Protocol

## Project Description

This project implements a fault-tolerant 2-phase distributed commit (2PC) protocol and explores how the protocol handles node crashes with controlled and randomly injected failures. The system assumes one transaction coordinator (TC) and at least two participants in the 2PC protocol. Multiple processes are used to emulate multiple nodes, with each node (TC and participants) employing a time-out mechanism in the absence of responses, transitioning to either the abort or commit state.

### Failure Scenarios

1. **Coordinator Failure Before "Prepare" Message:**
    - If the coordinator fails before sending the "prepare" message, nodes will not receive the "prepare" message until the time-out and will abort. They respond "no" to the "prepare" message after the coordinator comes back up and sends the "prepare" message.

2. **Coordinator Does Not Receive "Yes" from a Node:**
    - If the transaction coordinator does not receive "yes" from a node, it will abort the transaction.

3. **Coordinator Failure After Sending "Commit" Message:**
    - TC needs to store transaction information on disk before sending the "commit" message to the nodes. If the TC fails after sending one "commit" message to the nodes, it can't abort. When it comes back up, it will send the "commit" message to the nodes that didn't receive it.

4. **Node Failure After Replying "Yes" to TC:**
    - A node needs to store transaction information before replying "yes" to the TC. If it fails (time-out) after replying "yes," it will fetch the commit information from the TC for that particular transaction upon recovery.

### Emulating Failures

To emulate a failure, a much longer delay is imposed at a failed node than the time-out period used by other active nodes. Nodes print their states (commit/abort) before termination.

## How to Run

1. Import the project file `DS_Project2` as Maven using IntelliJ IDEA or Eclipse.
2. Ensure the system runs JAVA 11.
3. Start `Coordinator.java`.
4. Run `Node1.java`.
5. Run `Node2.java`.

*Note: A text file will be populated to store transaction information, and it should not be deleted.*

## License

This project is licensed under the [MIT License](LICENSE).

## Author

Aravindh Gopalsamy
