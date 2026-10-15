package com.satyakush.pulseops.incident;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import com.satyakush.pulseops.realtime.PulseOpsEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IncidentServicePaginationValidationTest {
    private IncidentRepository repository;
    private IncidentService service;

    @BeforeEach
    void setUp() {
        repository = mock(IncidentRepository.class);
        service = new IncidentService(
                repository,
                mock(PulseOpsEventPublisher.class),
                mock(IncidentActivityService.class));
    }

    @Test
    void rejectsNegativePageBeforeQueryingRepository() {
        assertThatThrownBy(() -> service.findPage(-1, 20, null, null, null))
                .isInstanceOf(InvalidIncidentPageRequestException.class)
                .hasMessage("page must be zero or greater");

        verifyNoInteractions(repository);
    }

    @Test
    void rejectsZeroPageSizeBeforeQueryingRepository() {
        assertThatThrownBy(() -> service.findPage(0, 0, null, null, null))
                .isInstanceOf(InvalidIncidentPageRequestException.class)
                .hasMessage("size must be between 1 and 100");

        verifyNoInteractions(repository);
    }

    @Test
    void rejectsPageSizeAboveMaximumBeforeQueryingRepository() {
        assertThatThrownBy(() -> service.findPage(0, 101, null, null, null))
                .isInstanceOf(InvalidIncidentPageRequestException.class)
                .hasMessage("size must be between 1 and 100");

        verifyNoInteractions(repository);
    }

    @Test
    void acceptsMaximumSupportedPageSizeAndDelegatesQuery() {
        IncidentPage expected = new IncidentPage(List.of(), 0, 100, 0, 0);
        when(repository.findPage(0, 100, null, null, null)).thenReturn(expected);

        assertThat(service.findPage(0, 100, null, null, null)).isSameAs(expected);

        verify(repository).findPage(0, 100, null, null, null);
    }
}
