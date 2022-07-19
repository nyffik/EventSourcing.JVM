package io.eventdriven.uniqueness.core.esdb;

import io.eventdriven.uniqueness.core.processing.Decider;
import io.eventdriven.uniqueness.core.processing.ETag;

import java.util.Arrays;

import static io.eventdriven.uniqueness.core.esdb.EventStore.AppendResult;
import static io.eventdriven.uniqueness.core.processing.FunctionalTools.FoldLeft.foldLeft;

public record CommandHandler<State, Command, Event>(
  EventStore eventStore,
  Decider<State, Command, Event> decider
) {
  public AppendResult handle(
    String streamId,
    Command command,
    ETag eTag
  ) {
    var events = eventStore.<Event>read(streamId);

    var state = events.stream()
      .collect(foldLeft(decider.getInitialState(), decider.evolve()));

    var newEvents = decider.decide().apply(command, state);

    return eventStore.append(
      streamId,
      eTag,
      Arrays.stream(newEvents).iterator()
    );
  }

}
