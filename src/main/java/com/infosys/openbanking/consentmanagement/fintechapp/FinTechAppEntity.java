package com.infosys.openbanking.consentmanagement.fintechapp;

import kalix.javasdk.annotations.EntityKey;
import kalix.javasdk.annotations.EntityType;
import kalix.javasdk.annotations.EventHandler;
import kalix.javasdk.eventsourcedentity.EventSourcedEntity;
import kalix.javasdk.eventsourcedentity.EventSourcedEntityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Instant;

@EntityKey("finTechAppId")
@EntityType("finTechApp")
@RequestMapping("/fintechapp/{finTechAppId}")
public class FinTechAppEntity extends EventSourcedEntity<FinTechAppEntity.State,FinTechAppEntity.Event> {
    private static final Logger logger = LoggerFactory.getLogger(FinTechAppEntity.class);

    public interface Event{
        record Validated(String finTechAppId, Instant validTillTimestamp, Instant timestamp) implements Event{}
        record Invalidated(String finTechAppId, Instant timestamp) implements  Event{}
    }
    public record State(boolean valid, Instant lastValidatedTimestamp, Instant validTillTimestamp){
        public static State empty(){
            return new State(false, null, null);
        }
        public State onValidatedEvent(Event.Validated event){
            return new State(true,event.timestamp(), event.validTillTimestamp());
        }
        public State onInvalidatedEvent(Event.Invalidated event){
            return new State(false,event.timestamp(), null);
        }
    }

    public interface Api{
        record ValidRequest(Instant validTillTimestamp)implements Api{}
        record InvalidRequest()implements Api{}
        record GetResponse(boolean valid, Instant validTillTimestamp) implements Api{}
    }

    private static final String RESPONSE_OK = "OK";

    private final String finTechAppId;

    public FinTechAppEntity(EventSourcedEntityContext context) {
        this.finTechAppId = context.entityId();
    }

    @Override
    public State emptyState() {
        return State.empty();
    }

    @PostMapping("/valid")
    public Effect<String> valid(@RequestBody  Api.ValidRequest request){
        logger.info("valid [{}] -> request {}",finTechAppId,request);
        return effects().emitEvent(new FinTechAppEntity.Event.Validated(finTechAppId,request.validTillTimestamp(),Instant.now())).thenReply(updatedState -> RESPONSE_OK);
    }

    @PostMapping("/invalid")
    public Effect<String> invalid(@RequestBody  Api.ValidRequest request){
        logger.info("invalid [{}] -> is already valid: {} -> request: {}",finTechAppId, currentState().valid, request);
        if(currentState().valid)
            return effects().emitEvent(new FinTechAppEntity.Event.Invalidated(finTechAppId,Instant.now())).thenReply(updatedState -> RESPONSE_OK);
        else
            return effects().reply(RESPONSE_OK);
    }

    @GetMapping
    public Effect<Api.GetResponse> get(){
        logger.info("get [{}] -> {}",currentState());
        return effects().reply(new Api.GetResponse(currentState().valid(), currentState().validTillTimestamp));
    }

    @EventHandler
    public FinTechAppEntity.State onValidated(Event.Validated event){
        return currentState().onValidatedEvent(event);
    }
    public FinTechAppEntity.State onInvalidated(Event.Invalidated event){
        return currentState().onInvalidatedEvent(event);
    }
}
