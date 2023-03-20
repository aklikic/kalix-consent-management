import com.infosys.openbanking.consentmanagement.fintechapp.FinTechAppEntity;
import kalix.javasdk.testkit.EventSourcedTestKit;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class UnitTest {
    @Test
    public void happyPath(){
        var fintechAppId = UUID.randomUUID().toString();
        var validTill = Instant.now().plus(1, ChronoUnit.MINUTES);

        EventSourcedTestKit<FinTechAppEntity.State,FinTechAppEntity.Event,FinTechAppEntity> testKit = EventSourcedTestKit.of(fintechAppId,FinTechAppEntity::new);

        var validRes = testKit.call(entity -> entity.valid(new FinTechAppEntity.Api.ValidRequest(validTill)));
        validRes.getNextEventOfType(FinTechAppEntity.Event.Validated.class);
        var state = (FinTechAppEntity.State)validRes.getUpdatedState();
        assertEquals(validTill,state.validTillTimestamp());
        assertTrue(state.valid());

        validRes = testKit.call(entity -> entity.invalid());
        validRes.getNextEventOfType(FinTechAppEntity.Event.Invalidated.class);
        state = (FinTechAppEntity.State)validRes.getUpdatedState();
        assertNull(state.validTillTimestamp());
        assertFalse(state.valid());


    }
}
