import com.steamy.model.ControlDesk;
import com.steamy.views.ControlDeskView;

public class Main {

    public static void main(String[] args) {

        final int NUM_LANES = 3;
        final int MAX_PATRONS_PER_PARTY = 5;

        ControlDesk controlDesk = new ControlDesk(NUM_LANES);

        ControlDeskView cdv = new ControlDeskView(controlDesk, MAX_PATRONS_PER_PARTY);
        controlDesk.subscribe(cdv);

    }
}
