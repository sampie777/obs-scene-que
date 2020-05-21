
### Que List

#### Execute After Previous
Mark a que item to be executed after the previous que item is executed. In this way, when a que item becomes active, all the directly following que items which are marked as Execute After Previous, will also be activated and executed in order.
 
Marking a que item as Execte After Previous: 
- `SHIFT + RIGHT ARROW` will mark a que item to be executed after the execution of the previous que item.
- `SHIFT + LEFT ARROW` will unmark a que item to be executed after the execution of the previous que item.

### Quick Access Buttons

Drag and drop any que item from the Source or Que list onto a Quick Access Button to assign this item to this button. Clicking the Quick Access Button will execute the que item. Note that this will not interfere with the que at all.
Use `CTRL + MOUSE CLICK` on a Quick Access Button to clear this button's state. 

### Properties

To set the default location for the Control Window to the center of the main window, manually set the property `controlWindowLocation` to `-1,-1`. Other values will be interpreted to absolute coordinates relative to the left upper screen corner. 

