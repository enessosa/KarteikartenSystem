import com.google.common.collect.Lists;
import core.Karte;
import core.enums.RecognitionLevel;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class KarteiKartenSystemTest {

    @Test
    public void testeMaxLength(){
        Karte k1 = new Karte(1, 2, "hallo", "fünftse", RecognitionLevel.BAD);
        Karte k2 = new Karte(1, 2, "hallo", "fünfts", RecognitionLevel.BAD);
        Karte k3 = new Karte(1, 2, "hallo", "fünfts", RecognitionLevel.BAD);
        Karte k4 = new Karte(1, 2, "hallo", "fünfts", RecognitionLevel.BAD);
        Karte k5 = new Karte(1, 2, "hallo", "fünfts", RecognitionLevel.BAD);
        List<Karte> k = Lists.newArrayList(k1, k2, k3, k4, k5);

        int i = Karte.getMaxListLength(k);

        Assertions.assertEquals(7, i, "stimmt nicht");
    }

}
