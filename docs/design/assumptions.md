# Design Assumptions

- Future shuffle algorithms can be added without modifying existing code
- In future, new card with new effect (draw, use , combo)would be added and we do not want to lots of change of code or we want flexible.
- NOPE handling can be consistently applied to any future card that can be NOPEed, which means we expect NOPE handling would not change.
- New card types with custom effects can be registered in the factory, like what we organized , use effect , draw effect and may can be cancelled by Nope(optional).
- We are open to new Combo effect and rule without modifying current code.
