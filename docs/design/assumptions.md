# Design Assumptions

## Refactored Card System

- Future shuffle algorithms can be added without modifying existing code
- In future, new card would be added and we do not want to lots of change of code or we want flexible.
- New special card effects can be added through CardEffect interface
- NOPE handling can be consistently applied to any future card that can be NOPEed.
- New card types with custom effects can be registered in the factory, like what we organized , use effect , draw effect and may can be cancelled by Nope(optional).

