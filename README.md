1. We decided to keep the responsibility of adding and removing elements to the
BrickerGameManager class and passed instance of the BrickerGameManager class to
the constructor of the BasicCollisionStrategy class with creating a public method
in the BrickerGameManager for deleting Bricks to be called in the onCollision()
method (in BasicCollisionStrategy class) for deleting the first parameter in the
method parameter list. This approach avoids unnecessarily sending private fields
from the BrickerGameManager class for applying the Encapsulation principle that
The BrickerGameManager fully encapsulates brick management, exposing only a
controlled removeBrick() method, Internal data structures are hidden from
BasicCollisionStrategy. and Single Responsibility Principle (SRP)
BrickerGameManager handles only element management, BrickerGameManager handles
only element management. However, it violates the Open/Closed Principle (OCP)
that Adding new collision behaviors might require modifying BrickerGameManager
to expose more methods. Also, violates the Polymorphism If another game manager
is introduced, collision strategies remain tied to BrickerGameManager.

2. We decided to keep the implementation of displaying the lives counters to
the user (graphic counter and numeric counter) in the BrickerGameManager class
only and without adding any new classes. With adding a method for creating hearts
for displaying the graphic counter and a method for displaying the numeric lives
counter, method for updating the numeric counter color each it changed, and a method
for increasing the lives counters after gaining hearts.

3. The extra paddle strategy: we added a public method for creating extra paddle in
the middle of the screen in the BrickerGameManager class with adding a condition
inside the method for checking if there is already an extra paddle in the game, and
created the ExtraPaddleStrategy class that extends the BasicCollisionStrategy class
with passing BrickerGameManager instance to it's constructor and called to the method
of creating extra paddle in the onCollision method.
The hearts strategy: we added a public method for creating fallen hearts from the
position of the center of the deleted brick in the BrickerGameManager class and
a method for increasing the lives counters after gaining hearts.
And created the HeartsStrategy class extends the BasicCollisionStrategy class
with passing BrickerGameManager instance to it's constructor and called to the method
of creating the fallen hearts in the onCollision method.
The pucks strategy: we added a public method for creating two pucks in the middle
of the screen in the BrickerGameManager class. And created the PucksStrategy class
that extends the BasicCollisionStrategy class with passing BrickerGameManager
instance to it's constructor and called the method of creating pucks in the
onCollision method.
The turbo strategy: we created a method for transferring the main ball into
turbo mode in the BrickerGameManager class, and created the TurboStrategy
class that extends the BasicCollisionStrategy class with passing
BrickerGameManager instance to it's constructor and called the method of
transferring the main ball into turbo mode in the onCollision method.
Also created a public method for returning the main ball to the normal mode
after a number of collisions in the BrickerGameManager class.

4. The Double Strategy in our project is implemented using the Decorator design pattern.
To achieve this, we created an interface called CollisionDecorator, which extends the
CollisionStrategy interface. The DoubleBehaviorStrategy class, which implements this
interface, takes an array of CollisionStrategy objects and applies each strategy’s
`onCollision` method in sequence during a collision event. This allows the game to execute
multiple behaviors in response to a single collision. Thus, the DoubleBehaviorStrategy
essentially “decorates” the CollisionStrategy, enhancing the collision handling by
combining multiple strategies, such as handling multiple behaviors simultaneously.
To limit the number of DoubleBehavior strategies to a maximum of three, we added a method
in the DoubleBehaviorStrategy class called `chooseStrategies`. This method is
responsible for generating the strategies and ensuring the proper balance by checking all
the cases and for each case it handles it accordingly in a way where we dont get more than
three strategies. By implementing this approach, we ensure that the game mechanics
stay manageable and coherent while allowing the flexibility of combining multiple behaviors for
enhanced gameplay.
