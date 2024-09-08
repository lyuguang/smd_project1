# SWEN30006 Report Project1

---
## Part 1: Design Change and Analysis
### 1.1 Introduction of New Item Types
The original system only had a Letter class. Now, an Item base class has been introduced, along with a new Parcel class. This change follows the GRASP Polymorphism principle, allowing the system to handle different types of mail items more flexibly.

### 1.2 Robot System Enhancement
The single Robot class has been expanded into an IRobot interface with multiple implementations (Robot, ColumnRobot, FlooringRobot). This design leverages GRASP's Polymorphism and Low Coupling principles, enabling different types of robots to coexist and be easily extended.

### 1.3 Delivery Mode Implementation
The introduction of the DeliveryMode interface and concrete implementations (CyclingMode, FlooringMode) demonstrates the Strategy pattern, adhering to GRASP's Polymorphism and Protected Variations principles. This makes adding new delivery modes straightforward.

### 1.4 Item Management Improvement
The new ItemQueue and ItemFactory classes follow GRASP's Pure Fabrication and Creator principles, increasing the system's modularity and maintainability.

### 1.5 Simulation Control Refactoring
Separating some of the Simulation class functionality into the new AutomailController class reflects the Single Responsibility Principle, improving system cohesion.

---
## Part 2: An analysis of the current design based on the GRASP
### 2.1 Information Expert
The ItemQueue class embodies the Information Expert principle by being responsible for managing items waiting for delivery. This class encapsulates all the logic related to storing, retrieving, and prioritizing items for delivery. By assigning these responsibilities to ItemQueue, we ensure that the class with the most relevant information is handling these tasks, leading to a more cohesive and maintainable design.

### 2.2 Creator
The ItemFactory class adheres to the Creator principle by being responsible for instantiating different types of Items (Letter, Parcel). This centralization of object creation logic makes it easier to manage and modify the creation process. If new item types are introduced or creation logic changes, only the ItemFactory needs to be updated, reducing the impact on other parts of the system.

### 2.3 Low Coupling
The introduction of the IRobot interface significantly reduces coupling between the MailRoom and specific robot implementations. By programming to an interface rather than concrete classes, the MailRoom can work with any type of robot that implements IRobot without needing to know the specific details of each robot type. This design makes it easier to introduce new robot types or modify existing ones without affecting the MailRoom class.

### 2.4 High Cohesion
The separation of delivery mode logic into distinct classes (CyclingMode, FlooringMode) increases system cohesion. Each delivery mode class has a single, well-defined responsibility, making the system easier to understand, maintain, and extend. This separation also allows for easy addition of new delivery modes without impacting existing code.

### 2.5 Polymorphism
The use of the IRobot interface allows the system to handle different types of robots uniformly. This polymorphic design enables the system to work with various robot implementations (Robot, ColumnRobot, FlooringRobot) through a common interface. This approach simplifies the code in classes that use robots, as they don't need to be aware of the specific robot types they're working with.

### 2.6 Protected Variations
The DeliveryMode interface acts as a stable interface around the varying delivery strategies, protecting the rest of the system from changes in delivery logic. This design allows new delivery modes to be added or existing ones to be modified without affecting other parts of the system, demonstrating the Protected Variations principle.

### 2.7 Controller
Your code includes an AutomailController class, which exemplifies the Controller pattern from GRASP principles. The AutomailController acts as a coordinator between the various components of the system, managing the overall flow of the simulation.

## Part 3: Proposed Design for the Extended Version
### 3.1 Introducing New Mail Item Types
To further extend the system's capabilities, we propose introducing additional mail item types:
1. PriorityItem: A new subclass of Item with a priority level attribute.

   - Implement a PriorityItem class extending Item.
   - Add a createPriorityItem method to ItemFactory.
   - Modify ItemQueue to consider priority when ordering items.


2. FragileItem: A subclass requiring special handling.

   - Implement a FragileItem class extending Item. 
   - Add a createFragileItem method to ItemFactory.
   - Introduce a new Robot subclass (e.g., CarefulRobot) capable of handling fragile items.
These additions would demonstrate the system's flexibility in accommodating new item types without major restructuring.

This approach doesn't require modifying existing Robot or MailRoom classes, demonstrating the Open-Closed Principle.

### 3.2 Introducing New Delivery Modes
To accommodate various delivery scenarios:
1. Implement an EmergencyMode for urgent deliveries:
   - Create an EmergencyMode class implementing DeliveryMode.
   - Modify MailRoom to switch to EmergencyMode when high-priority items are present.

2. Develop a LoadBalancingMode to optimize robot utilization:
   - Implement a LoadBalancingMode class.
   - Introduce a RobotLoadBalancer class to distribute work evenly among robots.

### 3.3 Facilitating Future Mail Item Additions
The proposed changes significantly enhance the system's ability to incorporate new mail item types in the future:
1. Extensible Item Hierarchy: The Item base class provides a solid foundation for creating new mail types. Future developers can easily add new item types by extending the Item class, ensuring consistent behavior across all mail items.
2. Flexible ItemFactory: The ItemFactory's design allows for easy addition of new creation methods for future item types. This centralized creation logic simplifies the process of integrating new mail items into the system.
3. Adaptable ItemQueue: By modifying the ItemQueue to handle different item attributes (like priority), we've made it more flexible for future item types with unique characteristics or handling requirements.
4. Polymorphic Robot Handling: The use of the IRobot interface and various Robot subclasses allows for specialized handling of different item types. This design makes it easy to introduce new Robot types capable of handling future, possibly more complex, mail items.
5. Extensible Delivery Modes: The DeliveryMode interface allows for the creation of new delivery strategies that can accommodate the unique requirements of future mail item types.


## Conclusion
By introducing more abstractions and interfaces, this design significantly improves the system's flexibility and extensibility. The use of GRASP principles makes the code more modular and easier to maintain. These improvements allow the system to easily adapt to new mail types and delivery strategies, providing a solid foundation for future extensions.


