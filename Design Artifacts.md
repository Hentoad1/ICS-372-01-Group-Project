# Design Artifacts

## Overview

The design artifacts document properly details our groups design process throughout the project. It contains all diagrams and other forms of organization and planning that relates to our program architecture.

## Use Case Diagram


```mermaid
flowchart LR

Customer([Customer])
Barista([Barista])
Manager([Manager])

subgraph CafeSystem ["Cafe System"]
    PO((Place Order))
    CI((Customize Item))
    BM((Browse Menu))
    CO((Complete Order))
    UOS((Update Order Status))
    VO((View Orders))
    VR((View Reports))
    MI((Manage Inventory))
    MM((Manage Menu))
end

Customer --> PO
Customer --> CI
Customer --> BM

Barista --> CO
Barista --> UOS
Barista --> VO

Manager --> VR
Manager --> MI
Manager --> MM
```

## Wireframes

### Customer Ordering Screen
![Customer Ordering Screen](wireframes/Customer_Ordering_Screen.png "Customer Ordering Screen")

### Barista Fulfillment Screen
![Barista Fulfillment Screen](wireframes/Barista_Fulfillment_Screen.png "Barista Fulfillment Screen")

### Manager Inventory Management
![Manager Inventory Management](wireframes/Manager_Inventory_Management.png "Manager Inventory Management")

### Manager Menu Management
![Manager Menu Management](wireframes/Manager_Menu_Management.png "Manager Menu Management")

### Manager Sales Overview
![Manager Sales Overview](wireframes/Manager_Sales_Overview.png "Manager Sales Overview")

## Conceptual Classes to Software Classes

| Conceptual Class Name | Software Class Name | Included/Merged/Split/Omitted | Why?                                          |
| --- | --- | --- | --- |
| User | User (abstract) | Included | Represents shared attributes for all roles |
| Customer | Customer | Included | Has specific access for customers |
| Barista | Barista | Included | Has specific access for baristas |
| Manager | Manager | Included | Has specific access for managers |
| Menu Item | MenuItem (abstract) | Included | Shares attributes and methods for all menu items |
| Beverage | Beverage | Included | Requires its own beverage logic |
| Pastry | Pastry | Included | Requires its own pastry logic |
| Order | Order | Included | Core business entity |
| OrderItem | OrderItem | Included | Represents quantity of an order |
| Inventory | Inventory | Included | Centralized ingredient tracking |
| InventoryItem | InventoryItem | Included | More implementation-specific |
| Menu | MenuService | Included | Handles business logic separate from data |
| Authentication | AuthService | Added | Needed for login handling |
| Model | BrewBiteSystem | Added | Simplify backend communications, allow flexibility |
| UI | - | Omitted | JavaFX UI elements can talk directly to backend facade |

## High-Level UML Class Diagram

```mermaid
classDiagram

class User {
  <<abstract>>
  name : String
}

class Customer
class Barista
class Manager

User <|-- Customer
User <|-- Barista
User <|-- Manager


class OrderQueue

class Order {
  orderId : int
  status : string
  totalPrice : double
}

class OrderItem {
  quantity : int
}

OrderQueue --> Order
Order "1" *-- "*" OrderItem
OrderItem --> MenuItem


class MenuItem {
  <<abstract>>
  name : String
  basePrice : double
  getPrice() double
}

class Beverage {
  size : String
  customizations : List
}

class Pastry

MenuItem <|-- Beverage
MenuItem <|-- Pastry


class Inventory

class InventoryItem {
  name : String
  quantity : int
}

Inventory "1" *-- "*" InventoryItem
```

## Delegating Responsibilities

### Sequence 1: Customer Orders Item

```mermaid
sequenceDiagram

actor Customer
participant UI
participant Controller
participant Order
participant Inventory
participant OrderQueue

Customer ->> UI: Select Item
UI ->> Controller: addItem()
Controller ->> Order: createOrder()
Controller ->> Inventory: checkStock()
Inventory -->> Controller: OK
Controller ->> OrderQueue: addOrder()
Controller -->> UI: Update Screen
UI -->> Customer: Display Updated View
```

This reflects object-oriented principles and patterns by ...

### Sequence 2: ???

```mermaid
```

This reflects object-oriented principles and patterns by ...

### Sequence 3: ???

```mermaid
```

This reflects object-oriented principles and patterns by ...

## Application Layers & MVC Implementation

The following class UML diagram displays the layers of the MVC implementation along with the class interactions and dataflow using this architecture. 

```mermaid
classDiagram

class User {
  <<abstract>>
  name : String
}

class Customer
class Barista
class Manager

User <|-- Customer
User <|-- Barista
User <|-- Manager


class OrderQueue

class Order {
  orderId : int
  status : string
  totalPrice : double
}

class OrderItem {
  quantity : int
}

OrderQueue --> Order
Order "1" *-- "*" OrderItem
OrderItem --> MenuItem


class MenuItem {
  <<abstract>>
  name : String
  basePrice : double
  getPrice() double
}

class Beverage {
  size : String
  customizations : List
}

class Pastry

MenuItem <|-- Beverage
MenuItem <|-- Pastry


class Inventory

class InventoryItem {
  name : String
  quantity : int
}

Inventory "1" *-- "*" InventoryItem

class View
class Model
class Controller
class AuthService

User <--> View
View <--> Controller
Controller <--> Model
Model <--> AuthService
Model <--> Inventory
Model <--> OrderQueue
```

## Applied OO Principles & Patterns

### Principle 1: Observer Pattern (MANDATORY) 

Principle 1 explanation here.

### Principle 2: Factory Pattern (MANDATORY)

Principle 2 explanation here.

### Principle 3: Strategy Pattern

The strategy pattern is used in the different forms of pricing logic based one what Implementation of MenuItem is being called. Beverage and Pastry will run separate logic and yield different results because they are using different strategies.

### Principle 4: Facade Pattern 

The Model / BrewBiteSystem acts as a facade for checking inventory, processing orders, and getting menu information.

### Single Responsibility Principle Explanation

Single Responsibility Principle Adherence explanation here.