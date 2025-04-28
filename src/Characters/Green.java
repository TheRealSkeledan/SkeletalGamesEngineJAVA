// Green.java

package Characters;

import Abstract.Character;
import java.io.IOException;

public class Green extends Character {
    public Green(int x, int y) throws IOException {
        super("Green", x, y, 6, 5, 2, 3, 0.3);
    }
}